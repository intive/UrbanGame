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
using UrbanGame.Localization;

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
                        if (CurrentTask.ListOfChanges.Contains("ABCDPossibleAnswers"))
                            RefreshAnswers();

                        ListOfChanges = CurrentTask.ListOfChanges;
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
            if (CurrentTask != null && message.TaskId == CurrentTask.Id)
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

        #region ListOfChanges

        private string _listOfChanges;

        public string ListOfChanges
        {
            get
            {
                return _listOfChanges;
            }
            set
            {
                if (_listOfChanges != value)
                {
                    _listOfChanges = value;
                    NotifyOfPropertyChange(() => ListOfChanges);
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
            VisualStateName = "Normal";
            RefreshGame();
            RefreshTask();
            RefreshAnswers();
        }

        protected override void OnViewLoaded(object view)
        {
            base.OnViewLoaded(view);

            Task.Factory.StartNew(async () =>
                {
                    if (CurrentTask == null)
                        RefreshTask();

                    if (CurrentTask.IsNewTask)
                    {
                        using (var uow = _unitOfWorkLocator())
                        {
                            uow.GetRepository<ITask>().All().First(t => t.Id == TaskId).IsNewTask = false;
                            uow.Commit();
                        }
                    }

                    if (!String.IsNullOrEmpty(CurrentTask.ListOfChanges))
                    {
                        ListOfChanges = CurrentTask.ListOfChanges;

                        CurrentTask.ListOfChanges = null;
                        using (var uow = _unitOfWorkLocator())
                        {
                            uow.GetRepository<ITask>().All().First(t => t.Id == TaskId).ListOfChanges = null;
                            uow.Commit();
                        }
                    }
                });
        }

        #endregion

        #region operations

        public void RefreshGame()
        {
            using (var uow = _unitOfWorkLocator())
            {
                IQueryable<IGame> games = uow.GetRepository<IGame>().All();
                Game = games.FirstOrDefault(g => g.Id == GameId);
            }
        }

        public void RefreshTask()
        {
            using (var uow = _unitOfWorkLocator())
            {
                IQueryable<ITask> tasks = uow.GetRepository<ITask>().All();
                CurrentTask = tasks.FirstOrDefault(t => t.Id == TaskId);

                if (CurrentTask.State == TaskState.Cancelled)
                {
                    BasicAppbar.Clear();
                    SetAppBarContent();
                }                    

                if (CurrentTask.State == TaskState.Active)
                {
                    if (Game.GameState != GameState.Joined)
                    {
                        VisualStateName = "Sent";
                    }
                    else if (CurrentTask.SolutionStatus == SolutionStatus.NotSend)
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
                else if (CurrentTask.State == TaskState.Cancelled)
                {
                    VisualStateName = "Cancelled";
                }
                else if (CurrentTask.State == TaskState.Inactive)
                {
                    VisualStateName = "Inactive";
                }
                else
                {
                    VisualStateName = "Sent";
                }
            }          
        }

        bool _refreshingAnswers = false;
        public void RefreshAnswers()
        {
            if (CurrentTask.Type != TaskType.ABCD)
                return;
               
            if (!_refreshingAnswers)
            {
                _refreshingAnswers = true;
                try
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
                }
                finally
                {
                    _refreshingAnswers = false;
                }
            }
        }

        public async Task Retry()
        {
            VisualStateName = "Sending";
            await SubmitSolution(Solution);
        }

        private async Task SubmitSolution(IBaseSolution solution)
        {
            //sending solution
            var result = await _gameWebService.SubmitTaskSolution(GameId, CurrentTask.Id, Solution);

            using (IUnitOfWork unitOfWork = _unitOfWorkLocator())
            {
                var sol = unitOfWork.GetRepository<IBaseSolution>().All().First(s => s.Id == solution.Id);                          

                System.Windows.Deployment.Current.Dispatcher.InvokeAsync(() => CurrentTask.UserPoints = result.ScoredPoints).Wait();

                switch(result.SubmitResult)
                {
                    case SubmitResult.AnswerCorrect:
                        if(result.ScoredPoints < sol.Task.MaxPoints/2)
                        {
                            if (sol.Task.IsRepeatable)
                            {
                                VisualStateName = "NotSoGreatRepeatable";
                            }
                            else
                            {
                                VisualStateName = "NotSoGreatNotRepeatable";
                            }
                        }
                        else
                        {
                            VisualStateName = "Correct";
                        }
                        sol.Task.SolutionStatus = SolutionStatus.Accepted;
                        break;
                    case SubmitResult.AnswerIncorrect:
                        if (sol.Task.IsRepeatable)
                        {
                            VisualStateName = "WrongRepeatable";
                        }
                        else
                        {
                            VisualStateName = "WrongNotRepeatable";
                        }
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

                if (result.SubmitResult == SubmitResult.AnswerCorrect || result.SubmitResult == SubmitResult.AnswerIncorrect)
                {
                    sol.Task.UserPoints = result.ScoredPoints;
                    if ((sol.Task.SolutionStatus == SolutionStatus.Accepted && sol.Task.MaxPoints == result.ScoredPoints) || !sol.Task.IsRepeatable)
                        sol.Task.State = TaskState.Accomplished;
                    unitOfWork.Commit();

                    _eventAggregator.Publish(new TaskChangedEvent() { Id = TaskId, GameId = GameId });
                }
                else if (result.SubmitResult == SubmitResult.ScoreDelayed)
                {
                    unitOfWork.Commit();
                }
            }

            RefreshTask();
        }

        public async Task SubmitGPS()
        {
            VisualStateName = "Sending";

            await Task.Factory.StartNew(() =>
            {
                GPSLocation gps = new GPSLocation();
                gps.GetCurrentCoordinates(coords =>
                {
                    IBaseSolution solution = new TaskSolution() { Latitude = coords.Latitude, Longitude = coords.Longitude, TaskType = TaskType.GPS };
                    //saving solution in database
                    using (IUnitOfWork unitOfWork = _unitOfWorkLocator())
                    {
                        GameTask task = (GameTask)unitOfWork.GetRepository<ITask>().All().First(t => t.Id == CurrentTask.Id);

                        //removing old solutions
                        foreach (var s in task.Solutions)
                        {
                            unitOfWork.GetRepository<IBaseSolution>().MarkForDeletion(s);
                        }
                        
                        solution.Task = task;
                        solution.TaskType = TaskType.GPS;
                        solution.Task.SolutionStatus = SolutionStatus.NotSend;

                        unitOfWork.GetRepository<IBaseSolution>().MarkForAdd(solution);
                        unitOfWork.Commit();
                    }
                    Solution = solution;
                    SubmitSolution(solution).Wait();
                    //TODO: investigate why do we need to use .Wait() here - it freezes if await is here
                });
            });
        }

        public async void SubmitABCD()
        {
            if (Answers.Count(a => a.IsChecked) == 0)
            {
                MessageBox.Show(AppResources.SelectAnswers);
                return;
            }

            VisualStateName = "Sending";
            await Task.Factory.StartNew(async () =>
            {                
                //saving solution in database
                using (IUnitOfWork unitOfWork = _unitOfWorkLocator())
                {
                    var solutionRepo = unitOfWork.GetRepository<IABCDSolution>();
                    IABCDSolution solution = solutionRepo.CreateInstance();
                    ITask task = unitOfWork.GetRepository<ITask>().All().First(t => t.Id == CurrentTask.Id);

                    //removing old solutions
                    foreach (var s in task.Solutions)
                    {
                        var userRepo = unitOfWork.GetRepository<IABCDUserAnswer>();
                        foreach (var userAnswer in userRepo.All().Where(us => us.Solution.Id == s.Id))
                            userRepo.MarkForDeletion(userAnswer);
                        unitOfWork.GetRepository<IBaseSolution>().MarkForDeletion(s);
                    }

                    solution.Task = task;
                    solution.Task.SolutionStatus = SolutionStatus.NotSend;
                    solution.TaskType = TaskType.ABCD;

                    var answerRepo = unitOfWork.GetRepository<IABCDUserAnswer>();
                    foreach (var check in Answers)
                    {
                        IABCDUserAnswer answer = answerRepo.CreateInstance();
                        answer.Answer = check.IsChecked;
                        answer.ABCDPossibleAnswer = unitOfWork.GetRepository<IABCDPossibleAnswer>().All().First(x=>x.Id == check.PossibleAnswer.Id);
                        solution.ABCDUserAnswers.Add(answer);
                    }

                    solutionRepo.MarkForAdd(solution);
                    unitOfWork.Commit();
                    Solution = solution;
                }
                
                await SubmitSolution(Solution);
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
