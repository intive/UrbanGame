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
using System.Threading;
using System.Windows.Threading;
using UrbanGame.Models;

namespace UrbanGame.ViewModels
{
    public class TaskViewModel : BaseViewModel, IHandle<GameChangedEvent>, IHandle<TaskChangedEvent>, 
        IHandle<SolutionStatusChanged>
    {
        IAppbarManager _appbarManager;

        public TaskViewModel(INavigationService navigationService, Func<IUnitOfWork> unitOfWorkLocator,
                                    IGameWebService gameWebService, IEventAggregator gameEventAggregator, IAppbarManager appbarManager, IGameAuthorizationService authorizationService)
            : base(navigationService, unitOfWorkLocator, gameWebService, gameEventAggregator, authorizationService)
        {
            _appbarManager = appbarManager;
        }

        protected override void OnViewReady(object view)
        {
            SetAppBarContent();
        }

        #region appbar

        private List<AppbarItem> BasicAppbar = new List<AppbarItem>()
        {
            new AppbarItem() {  Text = Localization.AppResources.ReportTask,Message="ReportTask" } 
        };

        private void SetAppBarContent()
        {
            Deployment.Current.Dispatcher.BeginInvoke(() =>
            {
                _appbarManager.ConfigureAppbar(BasicAppbar);
            });
        }

        #endregion

        #region IHandle<GameChangedEvent>
        public void Handle(GameChangedEvent game)
        {
            if (game.Id == GameId)
            {
                RefreshGame();
                RefreshTask();
            }
        }
        #endregion

        #region IHandle<TaskChangedEvent>
        public void Handle(TaskChangedEvent task)
        {
            if (IsActive && task.Id == TaskId)
            {
                using (var uow = _unitOfWorkLocator())
                {
                    CurrentTask = uow.GetRepository<ITask>().All().First(t => t.Id == task.Id);

                    if (!String.IsNullOrEmpty(CurrentTask.ListOfChanges))
                    {
                        MessageBox.Show(CurrentTask.ListOfChanges);
                        CurrentTask.ListOfChanges = null;
                        uow.Commit();
                    }
                }
            }
        }
        #endregion

        #region IHandle<SolutionStatusChanged>
        public void Handle(SolutionStatusChanged message)
        {
            if (message.TaskId == CurrentTask.Id)
            {
                CurrentTask.SolutionStatus = message.Status;
                CurrentTask.UserPoints = message.Points;
            }
        }
        #endregion

        #region navigation properties

        public int GameId { get; set; }

        public int TaskId { get; set; }

        #endregion

        #region bindable properties

        #region Game

        private IGame _game;

        public IGame Game
        {
            get
            {
                return _game;
            }
            set
            {
                if (_game != value)
                {
                    _game = value;
                    NotifyOfPropertyChange(() => Game);
                }
            }
        }
        #endregion

        #region CurrentTask

        private ITask _currentTask;

        public ITask CurrentTask
        {
            get
            {
                return _currentTask;
            }
            set
            {
                if (_currentTask != value)
                {
                    _currentTask = value;
                    NotifyOfPropertyChange(() => CurrentTask);
                }
            }
        }
        #endregion

        #region Solution

        private IBaseSolution _solution;

        public IBaseSolution Solution
        {
            get
            {
                return _solution;
            }
            set
            {
                if (_solution != value)
                {
                    _solution = value;
                    NotifyOfPropertyChange(() => Solution);
                }
            }
        }
        #endregion

        #region Answers

        private BindableCollection<ABCDAnswer> _answers;

        public BindableCollection<ABCDAnswer> Answers
        {
            get
            {
                return _answers;
            }
            set
            {
                if (_answers != value)
                {
                    _answers = value;
                    NotifyOfPropertyChange(() => Answers);
                }
            }
        }
        #endregion

        #region VisualStateName

        private string _visualStateName;

        public string VisualStateName
        {
            get
            {
                return _visualStateName;
            }
            set
            {
                if (_visualStateName != value)
                {
                    _visualStateName = value;
                    NotifyOfPropertyChange(() => VisualStateName);
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
            await RefreshGame();
            await RefreshTask();
            VisualStateName = "Normal";
        }

        protected override void OnViewLoaded(object view)
        {
            base.OnViewLoaded(view);
            
            new Timer(new TimerCallback((obj) =>
            {
                if (CurrentTask == null)
                {
                    var task = RefreshTask();
                    task.Wait();
                }

                if (!String.IsNullOrEmpty(CurrentTask.ListOfChanges))
                    System.Windows.Deployment.Current.Dispatcher.BeginInvoke(() =>
                    {
                        MessageBox.Show(CurrentTask.ListOfChanges);

                        CurrentTask.ListOfChanges = null;
                        using (var uow = _unitOfWorkLocator())
                        {
                            uow.GetRepository<ITask>().All().First(t => t.Id == TaskId).ListOfChanges = null;
                            uow.Commit();
                        }
                    });
            }), null, 700, System.Threading.Timeout.Infinite);
        }

        #endregion

        #region operations

        public async Task RefreshGame()
        {
            await Task.Factory.StartNew(async () =>
            {
                using (var uow = _unitOfWorkLocator())
                {
                    IQueryable<IGame> games = uow.GetRepository<IGame>().All();
                    Game = games.FirstOrDefault(g => g.Id == GameId) ?? await _gameWebService.GetGameInfo(GameId);
                }
            });
        }

        public async Task RefreshTask()
        {
            await Task.Factory.StartNew(() =>
            {
                using (var uow = _unitOfWorkLocator())
                {
                    IQueryable<ITask> tasks = uow.GetRepository<ITask>().All();
                    CurrentTask = tasks.FirstOrDefault(t => t.Id == TaskId) ?? _gameWebService.GetTaskDetails(GameId, TaskId);
                    if (CurrentTask.SolutionStatus == SolutionStatus.NotSend)
                    {
                        VisualStateName = "FirstSending";
                    }
                    else if (CurrentTask.IsRepeatable)
                    {
                        VisualStateName = "ReSending";
                    }
                    else
                    {
                        VisualStateName = "Sent";
                    }
                    
                }
            });
            await RefreshAnswear();
        }

        public async Task RefreshAnswear()
        {
            await Task.Factory.StartNew(() =>
            {
                Answers = new BindableCollection<ABCDAnswer>();

                using (var uow = _unitOfWorkLocator())
                {
                    IQueryable<IABCDPossibleAnswer> possibleAnswers = uow.GetRepository<IABCDPossibleAnswer>().All().Where(a => a.Task.Id == CurrentTask.Id);

                    foreach (IABCDPossibleAnswer possible in possibleAnswers.ToList())
                    {
                        IABCDUserAnswer userAnswer = uow.GetRepository<IABCDUserAnswer>().All().Where(a => a.ABCDPossibleAnswer.Id == possible.Id).FirstOrDefault();
                        bool isChecked = false;
                        if (userAnswer != null && userAnswer.Answer == true)
                        {
                            isChecked = true;
                        }

                        Answers.Add(new ABCDAnswer() { PossibleAnswer = possible, IsChecked = isChecked });
                    }
                }
            });
        }

        public void Retry()
        {
            VisualStateName = "Sending";
            SubmitSolution(Solution);
        }

        private void SubmitSolution(IBaseSolution solution)
        {
            //sending solution
            var result = _gameWebService.SubmitTaskSolution(GameId, CurrentTask.Id, Solution);
            //todo: update points in Views (gameDetailsView & TaskView)
            //todo: change WebService.SubmitTaskSolution to return gained points

            using (IUnitOfWork unitOfWork = _unitOfWorkLocator())
            {
                var sol = unitOfWork.GetRepository<IBaseSolution>().All().First(s => s.Id == solution.Id);                          

                System.Windows.Deployment.Current.Dispatcher.BeginInvoke(() => CurrentTask.UserPoints = result.ScoredPoints);

                switch(result.SubmitResult)
                {
                    case SubmitResult.AnswerCorrect:
                        VisualStateName = "Correct";
                        sol.Task.SolutionStatus = SolutionStatus.Accepted;
                        break;
                    case SubmitResult.AnswerIncorrect:
                        VisualStateName = "Wrong";
                        sol.Task.SolutionStatus = SolutionStatus.Rejected;
                        break;
                    case SubmitResult.ScoreDelayed:
                        VisualStateName = "Delayed";
                        sol.Task.SolutionStatus = SolutionStatus.Pending;
                        break;
                    default: 
                        VisualStateName = "Timeout";
                        break;
                }
            }

            RefreshTask();
        }

        public async void SubmitGPS()
        {
            VisualStateName = "Sending";

            await Task.Factory.StartNew(() =>
            {
                GPSLocation gps = new GPSLocation();
                    gps.GetCurrentCoordinates(coords =>
                    {
                        Solution = new TaskSolution() { Latitude = coords.Latitude, Longitude = coords.Longitude, TaskType = TaskType.GPS };
                        //saving solution in database
                        using (IUnitOfWork unitOfWork = _unitOfWorkLocator())
                        {
                            GameTask task = (GameTask)unitOfWork.GetRepository<ITask>().All().First(t => t.Id == CurrentTask.Id);
                            Solution.Task = task;
                            Solution.Task.SolutionStatus = SolutionStatus.Pending;

                            unitOfWork.GetRepository<IBaseSolution>().MarkForAdd(Solution);
                            unitOfWork.Commit();
                        }
                        SubmitSolution(Solution);
                    });
            });
        }

        public async void SubmitABCD()
        {
            VisualStateName = "Sending";
            await Task.Factory.StartNew(() =>
            {
                
                //saving solution in database
                using (IUnitOfWork unitOfWork = _unitOfWorkLocator())
                {
                    var solutionRepo=unitOfWork.GetRepository<IABCDSolution>();
                    IABCDSolution solution = solutionRepo.CreateInstance();
                    solutionRepo.MarkForAdd(solution);
                    ITask task = unitOfWork.GetRepository<ITask>().All().First(t => t.Id == CurrentTask.Id);
                    solution.Task = task;
                    solution.Task.SolutionStatus = SolutionStatus.Pending;

                    var answerRepo = unitOfWork.GetRepository<IABCDUserAnswer>();
                    foreach (var check in Answers)
                    {
                        IABCDUserAnswer answer =answerRepo.CreateInstance();
                        answer.Answer = check.IsChecked;
                        answer.ABCDPossibleAnswer =unitOfWork.GetRepository<IABCDPossibleAnswer>().All().First(x=>x.Id== check.PossibleAnswer.Id);
                        answerRepo.MarkForAdd(answer);
                        solution.ABCDUserAnswers.Add(answer);
                    }

                    unitOfWork.GetRepository<IBaseSolution>().MarkForAdd(solution);
                    unitOfWork.Commit();
                    Solution = solution;
                }
                
                SubmitSolution(Solution);
            });
        }

        public void ChangeToNormal()
        {
            VisualStateName = "Normal";
        }

        public void CanGoBack(ActionExecutionContext executionContext)
        {
            (executionContext.EventArgs as System.ComponentModel.CancelEventArgs).Cancel = VisualStateName == "Sending";
        }

        public void ReportTask()
        {
            _navigationService.UriFor<ReportTaskViewModel>().WithParam(rt => rt.GameId, GameId).WithParam(rt => rt.TaskId, TaskId).Navigate();
        }

        #endregion        
    }
}
