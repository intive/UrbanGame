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
    public class GameDetailsViewModel : BaseViewModel, IHandle<GameChangedEvent>, IHandle<SolutionStatusChanged>
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
            if (Game == null)
            {
                RefreshGame();
            }

            ChangeAppbarButtons();
            RemovePreviewFromStack();
        }

        private void RemovePreviewFromStack()
        {
            if (_navigationService.BackStack.First().Source.ToString().Contains("GameDetailsPreviewView"))
            {
                _navigationService.RemoveBackEntry();
            }
        }

        #region appbar configurations

        private List<AppbarItem> BasicAppbar = new List<AppbarItem>()
        {
            new AppbarItem() {  Text = Localization.AppResources.AbandonGame,Message="Leave" } 
        };

        private List<AppbarItem> DescriptionAppbar = new List<AppbarItem>()
        {
            new AppbarItem() {  Text = Localization.AppResources.AbandonGame,Message="Leave" } 
        };

        #endregion

        #region IHandle<GameChangedEvent>
        public void Handle(GameChangedEvent game)
        {
            RefreshGame();
        }
        #endregion

        #region IHandle<SolutionStatusChanged>
        public void Handle(SolutionStatusChanged status)
        {
            ITask task = ActiveTasks.FirstOrDefault(t => t.Id == status.TaskId);
            if (task != null)
            {
                task.SolutionStatus = status.Status;
                task.UserPoints = status.Points;
            }
        }
        #endregion

        #region navigation properties

        public int GameId { get; set; }

        public string DiffComparision { get; set; }

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

        private BindableCollection<PositionedHighScore> _gameHighScores;

        public BindableCollection<PositionedHighScore> GameHighScores
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
            await RefreshGame();
            await RefreshActiveTasks();
            await RefreshInactiveTasks();
            await RefreshAccomplishedTasks();
            await RefreshCancelledTasks();
            await RefreshHighScores();
            await RefreshAlerts();

        }

        protected override void OnViewLoaded(object view)
        {
            base.OnViewLoaded(view);

            Task.Factory.StartNew(() =>
                {
                    System.Threading.Thread.Sleep(700);
                    if (DiffComparision != null)
                    {
                        System.Windows.Deployment.Current.Dispatcher.BeginInvoke(() =>
                            {
                                MessageBox.Show(DiffComparision);
                                DiffComparision = null;
                            });
                        
                    }
                });
        }

        #endregion

        #region operations

        public void ShowTask(ITask task)
        {
            _navigationService.UriFor<TaskViewModel>().WithParam(t => t.TaskId, task.Id).WithParam(x=>x.GameId, GameId).Navigate();
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
                _appbarManager.ConfigureAppbar(DescriptionAppbar);
            }
            else
            {
                _appbarManager.ConfigureAppbar(BasicAppbar);
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

        public async Task RefreshGame()
        {
            using (var uow = _unitOfWorkLocator())
            {
                IQueryable<IGame> games = uow.GetRepository<IGame>().All();
                Game = games.FirstOrDefault(g => g.Id == GameId) ?? await _gameWebService.GetGameInfo(GameId);
            }
        }


        public async Task RefreshAlerts()
        {
            await Task.Factory.StartNew(() =>
            {
                using (var uow = _unitOfWorkLocator())
                {
                    IQueryable<IAlert> alerts = uow.GetRepository<IAlert>().All();

                    GameAlerts = new BindableCollection<IAlert>(alerts.Where(a => a.Game.Id == GameId).AsEnumerable());
                }
            });
        }

        public async Task RefreshHighScores()
        {
            await Task.Factory.StartNew(() =>
            {
                using (var uow = _unitOfWorkLocator())
                {
                    IQueryable<IHighScore> highScores = uow.GetRepository<IHighScore>().All();
                    BindableCollection<IHighScore> GameHighScoresTemp;

                    GameHighScoresTemp = new BindableCollection<IHighScore>(highScores.Where(h => h.Game.Id == GameId)
                                                                                    .OrderByDescending(h => h.Points)
                                                                                    .AsEnumerable());


                    GameHighScores = new BindableCollection<PositionedHighScore>();
                    for (int i = 0; i < GameHighScoresTemp.Count; i++)
                    {
                        GameHighScores.Add(new PositionedHighScore() { Position = i + 1, Entity = GameHighScoresTemp.ElementAt(i) });
                    }
                }
            });
        }

        public async Task RefreshActiveTasks()
        {
            await Task.Factory.StartNew(() =>
            {
                using (var uow = _unitOfWorkLocator())
                {
                    IQueryable<ITask> tasks = uow.GetRepository<ITask>().All();

                    ActiveTasks = new BindableCollection<ITask>(tasks.Where(t => t.State == TaskState.Active)
                                                                         .Where(t => t.Game.Id == GameId)
                                                                                    .OrderBy(t => t.EndDate)
                                                                                    .AsEnumerable());
                }
            });
        }

        public async Task RefreshInactiveTasks()
        {
            await Task.Factory.StartNew(() =>
            {
                using (var uow = _unitOfWorkLocator())
                {
                    IQueryable<ITask> tasks = uow.GetRepository<ITask>().All();

                    InactiveTasks = new BindableCollection<ITask>(tasks.Where(t => t.State == TaskState.Inactive)
                                                                                                .Where(t => t.Game.Id == GameId)
                                                                                                        .OrderBy(t => t.EndDate)
                                                                                                        .AsEnumerable());
                }
            });
        }

        public async Task RefreshAccomplishedTasks()
        {
            await Task.Factory.StartNew(() =>
            {
                using (var uow = _unitOfWorkLocator())
                {
                    IQueryable<ITask> tasks = uow.GetRepository<ITask>().All();

                    AccomplishedTasks = new BindableCollection<ITask>(tasks.Where(t => t.State == TaskState.Accomplished)
                                                                            .Where(t => t.Game.Id == GameId)
                                                                                    .OrderBy(t => t.EndDate)
                                                                                    .AsEnumerable());
                }
            });
        }

        public async Task RefreshCancelledTasks()
        {
            await Task.Factory.StartNew(() =>
            {
                using (var uow = _unitOfWorkLocator())
                {
                    IQueryable<ITask> tasks = uow.GetRepository<ITask>().All();

                    CancelledTasks = new BindableCollection<ITask>(tasks.Where(t => t.State == TaskState.Cancelled)
                                                                            .Where(t => t.Game.Id == GameId)
                                                                                    .OrderBy(t => t.EndDate)
                                                                                    .AsEnumerable());
                }
            });

        }

        public async void Leave()
        {
            if (MessageBox.Show("leave", "leave", MessageBoxButton.OKCancel) == MessageBoxResult.OK)
            {
                using (IUnitOfWork uow = _unitOfWorkLocator())
                {
                    uow.GetRepository<IGame>().All().First(x => x.Id == Game.Id).GameState = GameState.Inactive;
                    uow.Commit();
                }
                await RefreshGame();
                _navigationService.GoBack();
            }
        }

        #endregion
    }
}
