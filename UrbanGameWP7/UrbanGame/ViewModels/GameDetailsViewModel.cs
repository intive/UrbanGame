using Caliburn.Micro;
using Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using UrbanGame.Storage;
using System.Threading.Tasks;

namespace UrbanGame.ViewModels
{
    public class GameDetailsViewModel : BaseViewModel, IHandle<GameChangedEvent>
    {
        IAppbarManager _appbarManager;
        private string _activeSection;

        public GameDetailsViewModel(INavigationService navigationService, Func<IUnitOfWork> unitOfWorkLocator,
                                    IGameWebService gameWebService, IEventAggregator gameEventAggregator, IAppbarManager appbarManager)
            : base(navigationService, unitOfWorkLocator, gameWebService, gameEventAggregator)
        {
            _appbarManager = appbarManager;
        }

        protected override void OnViewReady(object view)
        {
            ChangeAppbarButtons();
        }

        #region appbar configurations

        private List<AppbarItem> BasicAppbar = new List<AppbarItem>()
        {
            new AppbarItem() {  Text = Localization.AppResources.AbandonGame,Message="AbandonGame" } 
        };

        private List<AppbarItem> DescriptionAppbar = new List<AppbarItem>()
        {
            new AppbarItem() {  Text = Localization.AppResources.AbandonGame,Message="AbandonGame" } 
        };

        #endregion

        #region IHandle<GameChangedEvent>
        public void Handle(GameChangedEvent game)
        {

        }
        #endregion

        #region navigation properties

        public int GameId { get; set; }

        #endregion

        #region bindable properties

        #region ShowMore

        private bool _showsMore = false;

        public bool ShowsMore
        {
            get
            {
                return _showsMore;
            }
            set
            {
                if (_showsMore != value)
                {
                    _showsMore = value;
                    NotifyOfPropertyChange(() => ShowsMore);
                }
            }
        }

        #endregion

        #region ActiveTasks

        private BindableCollection<ITask> _activeTasks;

        public BindableCollection<ITask> ActiveTasks
        {
            get
            {
                return _activeTasks;
            }
            set
            {
                if (_activeTasks != value)
                {
                    _activeTasks = value;
                    NotifyOfPropertyChange(() => ActiveTasks);
                }
            }
        }
        #endregion

        #region InactiveTasks

        private BindableCollection<ITask> _inactiveTasks;

        public BindableCollection<ITask> InactiveTasks
        {
            get
            {
                return _inactiveTasks;
            }
            set
            {
                if (_inactiveTasks != value)
                {
                    _inactiveTasks = value;
                    NotifyOfPropertyChange(() => InactiveTasks);
                }
            }
        }
        #endregion

        #region AccomplishedTasks

        private BindableCollection<ITask> _accomplishedTasks;

        public BindableCollection<ITask> AccomplishedTasks
        {
            get
            {
                return _accomplishedTasks;
            }
            set
            {
                if (_accomplishedTasks != value)
                {
                    _accomplishedTasks = value;
                    NotifyOfPropertyChange(() => AccomplishedTasks);
                }
            }
        }
        #endregion

        #region CancelledTasks

        private BindableCollection<ITask> _cancelledTasks;

        public BindableCollection<ITask> CancelledTasks
        {
            get
            {
                return _cancelledTasks;
            }
            set
            {
                if (_cancelledTasks != value)
                {
                    _cancelledTasks = value;
                    NotifyOfPropertyChange(() => CancelledTasks);
                }
            }
        }
        #endregion

        #region GameAlerts

        private BindableCollection<IAlert> _gameAlerts;

        public BindableCollection<IAlert> GameAlerts
        {
            get
            {
                return _gameAlerts;
            }
            set
            {
                if (_gameAlerts != value)
                {
                    _gameAlerts = value;
                    NotifyOfPropertyChange(() => GameAlerts);
                }
            }
        }

        #endregion

        #region GameHighScores

        private BindableCollection<PositionedHighScores> _gameHighScores;

        public BindableCollection<PositionedHighScores> GameHighScores
        {
            get
            {
                return _gameHighScores;
            }
            set
            {
                if (_gameHighScores != value)
                {
                    _gameHighScores = value;
                    NotifyOfPropertyChange(() => GameHighScores);
                }
            }
        }

        #endregion

        #endregion

        #region lifecycle

        protected override void OnCreated()
        {
            base.OnCreated();
        }

        protected async override void OnActivate()
        {
            base.OnActivate();
            RefreshActiveTasks();
            RefreshInactiveTasks();
            RefreshAccomplishedTasks();
            RefreshCancelledTasks();
            RefreshHighScores();
            RefreshAlerts();
        }

        #endregion

        #region operations

        public void ShowTask(ITask task)
        {
            _navigationService.UriFor<TaskViewModel>().WithParam(t => t.TaskId, task.Id).Navigate();
        }

        public void ChangeAppbarButtons(SelectionChangedEventArgs args)
        {
            _activeSection = ((FrameworkElement)args.AddedItems[0]).Name;
            ChangeAppbarButtons();
        }

        public void ChangeAppbarButtons()
        {
            if (_activeSection == "Description")
            {
                //_appbarManager.ConfigureAppbar(DescriptionAppbar);
            }
            else
            {
                //_appbarManager.ConfigureAppbar(BasicAppbar);
            }
        }

        public void ShowMoreDescription()
        {
            ShowsMore = true;
        }

        public void ShowLessDescription()
        {
            ShowsMore = false;
        }

        public void RefreshAlerts()
        {
            IQueryable<IAlert> alerts = _unitOfWorkLocator().GetRepository<IAlert>().All();

            GameAlerts = new BindableCollection<IAlert>(alerts.Where(a => a.Game.Id == GameId)
                                                                    .AsEnumerable());
        }

        public void RefreshHighScores()
        {
            IQueryable<IHighScore> highScores = _unitOfWorkLocator().GetRepository<IHighScore>().All();

            BindableCollection<IHighScore> GameHighScoresTemp = new BindableCollection<IHighScore>(highScores.Where(h => h.Game.Id == GameId)
                                                                                .OrderBy(h => h.Points)
                                                                                .AsEnumerable());

            GameHighScores = new BindableCollection<PositionedHighScores>();
            for (int i = 0; i < GameHighScoresTemp.Count; i++)
            {
                GameHighScores.Add(new PositionedHighScores() { Position = i + 1, Entity = GameHighScoresTemp.ElementAt(i) });
            }
        }

        public void RefreshActiveTasks()
        {
            IQueryable<ITask> task = _unitOfWorkLocator().GetRepository<ITask>().All();

            ActiveTasks = new BindableCollection<ITask>(task.Where(t => t.State == TaskState.Active)
                                                                                .OrderBy(t => t.EndDate)
                                                                                .AsEnumerable());
        }

        public void RefreshInactiveTasks()
        {
            IQueryable<ITask> task = _unitOfWorkLocator().GetRepository<ITask>().All();

            InactiveTasks = new BindableCollection<ITask>(task.Where(t => t.State == TaskState.Inactive)
                                                                                .OrderBy(t => t.EndDate)
                                                                                .AsEnumerable());
        }

        public void RefreshAccomplishedTasks()
        {
            IQueryable<ITask> task = _unitOfWorkLocator().GetRepository<ITask>().All();

            AccomplishedTasks = new BindableCollection<ITask>(task.Where(t => t.State == TaskState.Accomplished)
                                                                                .OrderBy(t => t.EndDate)
                                                                                .AsEnumerable());
        }

        public void RefreshCancelledTasks()
        {
            IQueryable<ITask> task = _unitOfWorkLocator().GetRepository<ITask>().All();

            CancelledTasks = new BindableCollection<ITask>(task.Where(t => t.State == TaskState.Cancelled)
                                                                                .OrderBy(t => t.EndDate)
                                                                                .AsEnumerable());
        }

        #endregion
    }
}
