using System;
using System.Collections.Generic;
using System.Linq;
using System.Data.Linq;
using System.Text;
using Common;
using Caliburn.Micro;
using System.Threading.Tasks;
using System.Threading;

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
        Timer _mockTimer;        

        public GameChangesManager(IGameWebService gameWebService, IEventAggregator gameEventAggregator, 
                                  Func<IUnitOfWork> unitOfWorkLocator)
        {
            _gameWebService = gameWebService;
            _gameEventAggregator = gameEventAggregator;
            _unitOfWorkLocator = unitOfWorkLocator;

            _mockTimer = new Timer(new TimerCallback(RandomChange), null, 5000, 5000);
        }

        public void GameChanged(int gid)
        {
            Task.Factory.StartNew(() =>
                {
                    IGame newGame = _gameWebService.GetGameInfo(gid);
                    IRepository<IGame> repo = _unitOfWorkLocator().GetRepository<IGame>();
                    IGame toUpdate = repo.All().FirstOrDefault(g => g.Id == gid);

                    if (toUpdate != null)
                    {
                        //for tests, until it works as mock-up
                        toUpdate.Description = DateTime.Now.ToLongTimeString() + " " + newGame.Description; 

                        toUpdate.Difficulty = newGame.Difficulty;
                        toUpdate.GameEnd = newGame.GameEnd;
                        toUpdate.GameLongitude = newGame.GameLongitude;
                        toUpdate.GameLatitude = newGame.GameLatitude;
                        toUpdate.GameLogo = newGame.GameLogo;
                        toUpdate.Localization = newGame.Localization;
                        toUpdate.GameStart = newGame.GameStart;
                        toUpdate.GameState = newGame.GameState;
                        toUpdate.GameType = newGame.GameType;
                        toUpdate.MaxPoints = newGame.MaxPoints;
                        toUpdate.Name = newGame.Name;
                        toUpdate.NumberOfCompletedTasks = newGame.NumberOfCompletedTasks;
                        toUpdate.NumberOfPlayers = newGame.NumberOfPlayers;
                        toUpdate.NumberOfSlots = newGame.NumberOfSlots;
                        toUpdate.NumberOfTasks = newGame.NumberOfTasks;
                        toUpdate.OperatorName = newGame.OperatorName;
                        toUpdate.Points = newGame.Points;
                        toUpdate.Prizes = newGame.Prizes;
                        toUpdate.Rank = newGame.Rank;

                        _unitOfWorkLocator().Commit();
                    }

                    _gameEventAggregator.Publish(new GameChangedEvent() { Id = gid });                                      
                });      
        }

        static List<SubmittedSolution> SubmittedSolutions = new List<SubmittedSolution>();
        static object _syncObj = new object();

        public static void AddSolution(SubmittedSolution solution)
        {
            lock (_syncObj)
            {
                SubmittedSolution s = SubmittedSolutions.FirstOrDefault(sol => sol.TaskId == solution.TaskId);
                if (s != null)
                    SubmittedSolutions.Remove(s);

                SubmittedSolutions.Add(solution);
            }
        }

        public void UpdateSolutionStatus()
        {
            Random r = new Random();

            Task.Factory.StartNew(() =>
            {
                lock (_syncObj)
                {
                    IRepository<ITask> repo = _unitOfWorkLocator().GetRepository<ITask>();

                    foreach (SubmittedSolution solution in SubmittedSolutions)
                    {
                        ITask toUpdate = repo.All().FirstOrDefault(t => t.Id == solution.TaskId);

                        if (toUpdate != null)
                        {
                            toUpdate.SolutionStatus = r.Next(10) >= 5 ? SolutionStatus.Accepted : SolutionStatus.Rejected;
                            SubmittedSolutions.Remove(solution);

                            _unitOfWorkLocator().Commit();
                            _gameEventAggregator.Publish(new SolutionStatusChanged() { Id = 1, TaskId = solution.TaskId, Status = toUpdate.SolutionStatus });
                        }
                    }
                };
            });
        }

        private void RandomChange(object obj)
        {
            GameChanged(new Random().Next(1, 5));
            UpdateSolutionStatus();
        }
    }
}
