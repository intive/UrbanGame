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
    public class TaskViewModel : BaseViewModel, IHandle<GameChangedEvent>, IHandle<TaskChangedEvent>, IHandle<SolutionStatusChanged>
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

        #region appbar configurations

        #endregion

        #region appbar

        public void RefreshMenuItemText()
        {

        }

        private void SetAppBarContent()
        {
            Deployment.Current.Dispatcher.BeginInvoke(() =>
            {
                RefreshMenuItemText();
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
            if (task.Id == TaskId)
            {
                RefreshTask();
            }
        }
        #endregion

        #region IHandle<SolutionStatusChanged>
        public void Handle(SolutionStatusChanged message)
        {
            if (message.TaskId == CurrentTask.Id)
            {
                CurrentTask.SolutionStatus = message.Status;
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

        private BindableCollection<ABCDAnswear> _answers;

        public BindableCollection<ABCDAnswear> Answers
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

        #endregion

        #region operations

        public async Task RefreshGame()
        {
            await Task.Factory.StartNew(async () =>
            {               
                IQueryable<IGame> games = _unitOfWorkLocator().GetRepository<IGame>().All();
                Game = games.FirstOrDefault(g => g.Id == GameId) ?? await _gameWebService.GetGameInfo(GameId);
            });
        }

        public async Task RefreshTask()
        {
            await Task.Factory.StartNew(() =>
            {
                IQueryable<ITask> tasks = _unitOfWorkLocator().GetRepository<ITask>().All();
                CurrentTask = tasks.FirstOrDefault(t => t.Id == TaskId) ?? _gameWebService.GetTaskDetails(GameId, TaskId);
            });
        }

        public async Task RefreshAnswear()
        {
            await Task.Factory.StartNew(() =>
            {
                Answers = new BindableCollection<ABCDAnswear>();

                IQueryable<IABCDPossibleAnswer> possibleAnswers = _unitOfWorkLocator().GetRepository<IABCDPossibleAnswer>().All().Where(a => a.Task.Id == CurrentTask.Id);

                foreach(IABCDPossibleAnswer possible in possibleAnswers.ToList())
                {
                    IABCDUserAnswer userAnswer = _unitOfWorkLocator().GetRepository<IABCDUserAnswer>().All().Where(a => a.ABCDPossibleAnswer.Id == possible.Id).Last();
                    bool isChecked = false;
                    if(userAnswer != null && userAnswer.Answer == true)
                    {
                        isChecked = true;
                    }

                    Answers.Add(new ABCDAnswear() { possibleAnswear = possible, isChecked = isChecked });
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
            //saving solution in database

            using (IUnitOfWork unitOfWork = _unitOfWorkLocator())
            {
                GameTask task = (GameTask)unitOfWork.GetRepository<ITask>().All().First(t => t.Id == CurrentTask.Id);
                solution.Task = task;

                unitOfWork.GetRepository<IBaseSolution>().MarkForAdd(solution);
                unitOfWork.Commit();
            }

            //sending solution
            var result = _gameWebService.SubmitTaskSolution(Game.Id, CurrentTask.Id, Solution);

            using (IUnitOfWork unitOfWork = _unitOfWorkLocator())
            {
                GameTask task = (GameTask)unitOfWork.GetRepository<ITask>().All().First(t => t.Id == CurrentTask.Id);
                
                task.UserPoints = 20;
                solution.Task = task;

                unitOfWork.Commit();
            }

            RefreshTask();

            switch(result)
            {
                case SubmitResult.AnswerCorrect: VisualStateName = "Correct"; break;
                case SubmitResult.AnswerIncorrect: VisualStateName = "Wrong"; break;
                case SubmitResult.Timeout: VisualStateName = "Timeout"; break;
                default: break;
            }
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
                        SubmitSolution(Solution);
                    });
            });
        }

        public async void SubmitABCD()
        {
            VisualStateName = "Sending";
            await Task.Factory.StartNew(() =>
            {
                IABCDSolution solution = new TaskSolution() { TaskType = TaskType.ABCD };

                foreach (var check in Answers)
                {
                    IABCDUserAnswer answer = new ABCDUserAnswer() { Answer = check.isChecked, Solution = new TaskSolution() { TextAnswer = check.possibleAnswear.Answer, TaskId = check.possibleAnswear.Task.Id, TaskType = TaskType.ABCD } };
                    solution.ABCDUserAnswers.Add(answer);
                }
                Solution = solution;
                SubmitSolution(Solution);
            });
        }

        public void ChangeToNormal()
        {
            VisualStateName = "Normal";
        }

        #endregion        
    }
}
