using System;
using System.Collections.Generic;
using System.Linq;
using System.Data.Linq;
using System.Text;
using Common;
using Caliburn.Micro;
using System.Threading.Tasks;
using System.Threading;
using System.Windows;
using System.Windows.Media;
using System.Reflection;

namespace WebService
{
    public class SubmittedSolution
    {
        public int TaskId { get; set; }
    }

    public class GameChangesManager : IGameChangesManager
    {
        IGameWebService _gameWebService;
        IEventAggregator _gameEventAggregator;
        Func<IUnitOfWork> _unitOfWorkLocator;
        ILocalizationService _localizationService;
        IToastPromptService _toastPromptService;
        Timer _solutionUpdaterTimer;
        Timer _gameUpdaterTimer;

        private const int _gameUpdaterPeriod = 60 * 1000;
        private const int _solutionUpdaterPeriod = 30 * 1000;

        public GameChangesManager(IGameWebService gameWebService, IEventAggregator gameEventAggregator,
                                  Func<IUnitOfWork> unitOfWorkLocator, ILocalizationService localizationService,
                                  IToastPromptService toastPromptService)
        {
            _gameWebService = gameWebService;
            _gameEventAggregator = gameEventAggregator;
            _unitOfWorkLocator = unitOfWorkLocator;
            _localizationService = localizationService;
            _toastPromptService = toastPromptService;

            _solutionUpdaterTimer = new Timer(new TimerCallback(CheckSolutionStatusChanged), null, _solutionUpdaterPeriod, _solutionUpdaterPeriod);
            _gameUpdaterTimer = new Timer(new TimerCallback(CheckGameChanges), null, _gameUpdaterPeriod, _gameUpdaterPeriod);
        }

        #region GameChanges

        public PropertyInfo[] GetPublicProperties(Type type)
        {
            if (type.IsInterface)
            {
                var propertyInfos = new List<PropertyInfo>();

                var considered = new List<Type>();
                var queue = new Queue<Type>();
                considered.Add(type);
                queue.Enqueue(type);
                while (queue.Count > 0)
                {
                    var subType = queue.Dequeue();
                    foreach (var subInterface in subType.GetInterfaces())
                    {
                        if (considered.Contains(subInterface)) continue;

                        considered.Add(subInterface);
                        queue.Enqueue(subInterface);
                    }

                    var typeProperties = subType.GetProperties(
                        BindingFlags.FlattenHierarchy
                        | BindingFlags.Public
                        | BindingFlags.Instance);

                    var newPropertyInfos = typeProperties
                        .Where(x => !propertyInfos.Contains(x));

                    propertyInfos.InsertRange(0, newPropertyInfos);
                }

                return propertyInfos.ToArray();
            }

            return type.GetProperties(BindingFlags.FlattenHierarchy
                | BindingFlags.Public | BindingFlags.Instance);
        }

        protected IList<string> UpdateGame(IGame oldGame, IGame newGame)
        {
            List<string> differences = new List<string>();
            PropertyInfo[] newProperties = GetPublicProperties(newGame.GetType());
            PropertyInfo[] oldProperties = GetPublicProperties(oldGame.GetType());

            foreach (PropertyInfo oldProperty in oldProperties)
            {
                PropertyInfo newProperty = newProperties.First(p => p.Name == oldProperty.Name);

                //todo: do not override fields which are stored only localy
                if (oldProperty.Name != "GameState" && oldProperty.CanWrite)
                {
                    //skip collections - only basic data
                    if (!oldProperty.PropertyType.IsGenericType ||
                        (oldProperty.PropertyType.GetGenericTypeDefinition() != typeof(IEntityEnumerable<>) &&
                         oldProperty.PropertyType.GetGenericTypeDefinition() != typeof(EntitySet<>)))
                    {
                        object oldValue = oldProperty.GetValue(oldGame, null);
                        object newValue = newProperty.GetValue(newGame, null);

                        if (((oldValue == null || newValue == null) && oldValue != newValue) ||
                                (oldValue != null && newValue != null && !oldValue.Equals(newValue)))
                        {
                            oldProperty.SetValue(oldGame, newProperty.GetValue(newGame, null), null);
                            differences.Add(oldProperty.Name);
                        }
                    }
                }
            }
            return differences;
        }

        protected void CheckGameChanges(object obj)
        {
            if (!_gameWebService.IsAuthorized)
                return;

            Task.Factory.StartNew(async () =>
                {
                    _gameUpdaterTimer.Dispose();

                    try
                    {
                        using (var uow = _unitOfWorkLocator())
                        {
                            var activeGames = uow.GetRepository<IGame>().All().Where(g => g.GameState == GameState.Joined);

                            foreach (var oldGame in activeGames)
                            {
                                IGame newGame = await _gameWebService.GetGameInfo(oldGame.Id);

                                if (newGame.Version != oldGame.Version)
                                {
                                    IList<string> diff = UpdateGame(oldGame, newGame);
                                    uow.Commit(); //todo: to check: sometimes throws InvalidOperationExceptions (only if use GameWebService - not mock), it doesn't happen if you put that line after foreach

                                    if (diff.Count == 0)
                                        return;

                                    if (_toastPromptService != null)
                                        _toastPromptService.ShowGameChanged(oldGame.Id, oldGame.Name, _localizationService.GetText("GameChangedToast"), diff);

                                    _gameEventAggregator.Publish(new GameChangedEvent() { Id = oldGame.Id });
                                }
                            }
                        }
                    }
                    finally
                    {
                        _gameUpdaterTimer = new Timer(new TimerCallback(CheckGameChanges), null, _gameUpdaterPeriod, _gameUpdaterPeriod);
                    }                    
                });
        }

        #endregion


        #region SolutionChanges

        protected void CheckSolutionStatusChanged(object obj)
        {
            if (!_gameWebService.IsAuthorized)
                return;

            Task.Factory.StartNew(async () =>
            {
                _solutionUpdaterTimer.Dispose();

                try
                {
                    using (var uow = _unitOfWorkLocator())
                    {
                        var pendingSolutions = uow.GetRepository<IBaseSolution>().All().Where(s => s.Task.SolutionStatus == SolutionStatus.Pending);

                        foreach (var solution in pendingSolutions)
                        {
                            SolutionStatusResponse response = await _gameWebService.GetSolutionStatus(solution.Id);

                            if (response.Status == SolutionStatus.Accepted || response.Status == SolutionStatus.Rejected)
                            {
                                solution.Task.SolutionStatus = response.Status;
                                solution.Task.UserPoints = response.Points;
                                uow.Commit();

                                string message = _localizationService.GetText("SolutionStatusChanged") + " " +
                                                 (response.Status == SolutionStatus.Accepted ? _localizationService.GetText("Accepted") : _localizationService.GetText("Rejected"));
                                if (_toastPromptService != null)
                                    _toastPromptService.ShowSolutionStatusChanged(solution.Task.Id, solution.Task.Name, message);

                                _gameEventAggregator.Publish(new SolutionStatusChanged() { Status = response.Status, Points = response.Points, TaskId = solution.Task.Id });
                            }
                        }                        
                    }
                }
                finally
                {
                    _solutionUpdaterTimer = new Timer(new TimerCallback(CheckSolutionStatusChanged), null, _solutionUpdaterPeriod, _solutionUpdaterPeriod);
                }
            });
        }

        #endregion
    }
}
