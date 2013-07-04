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

namespace UrbanGame.ViewModels
{
    public class GameDetailsViewModel : BaseViewModel, IHandle<GameChangedEvent>, IHandle<SolutionStatusChanged>,
        IHandle<GameStateChangedEvent>, IHandle<TaskChangedEvent>
    {
        IAppbarManager _appbarManager;
        private string _activeSection;

        public GameDetailsViewModel(INavigationService navigationService, Func<IUnitOfWork> unitOfWorkLocator,
                                    IGameWebService gameWebService, IEventAggregator gameEventAggregator, IAppbarManager appbarManager, IGameAuthorizationService authorizationService)
            : base(navigationService, unitOfWorkLocator, gameWebService, gameEventAggregator, authorizationService)
        {
            _appbarManager = appbarManager;
        }

        protected override void OnViewReady(object view)
        {
            if (Game == null)
            {
                RefreshGame();
            }

            if (Game.GameState != GameState.Joined)
            {
                BasicAppbar.Clear();
                DescriptionAppbar.Clear();
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
            if (IsActive && game.Id == GameId)
            {
                using (var uow = _unitOfWorkLocator())
                {
                    Game = uow.GetRepository<IGame>().All().First(g => g.Id == game.Id);

                    if (!String.IsNullOrEmpty(Game.ListOfChanges))
                    {
                        MessageBox.Show(Game.ListOfChanges);
                        Game.ListOfChanges = null;
                        uow.Commit();
                    }
                }
            }
        }
        #endregion

        #region IHandle<GameStateChangedEvent>
        public void Handle(GameStateChangedEvent game)
        {
            if (IsActive && game.Id == GameId)
            {
                using (var uow = _unitOfWorkLocator())
                {
                    Game = uow.GetRepository<IGame>().All().First(g => g.Id == game.Id);

                    if (!Game.GameOverDisplayed && (Game.GameState == GameState.Won || Game.GameState == GameState.Lost))
                    {
                        BasicAppbar.Clear();
                        DescriptionAppbar.Clear();
                        ChangeAppbarButtons();
                        
                        switch (Game.GameState)
                        {
                            case GameState.Won:
                                VisualStateName = "YouWon";
                                break;
                            case GameState.Lost:
                                VisualStateName = "YouLost";
                                break;
                        }

                        Game.GameOverDisplayed = true;
                        uow.Commit();
                    }                   
                }
            }
        }
        #endregion

        #region IHandle<TaskChangedEvent>
        public void Handle(TaskChangedEvent task)
        {
            if (task.GameId == GameId)
            {
                RefreshActiveTasks();
                RefreshAccomplishedTasks();
                RefreshCancelledTasks();
                RefreshInactiveTasks();
            }
        }
        #endregion

        #region IHandle<SolutionStatusChanged>
        public void Handle(SolutionStatusChanged status)
        {
            if (ActiveTasks.Any(t => t.Id == status.TaskId))
            {               
                RefreshActiveTasks();
                RefreshAccomplishedTasks();
            }
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

        #region BonusTasks

        private BindableCollection<ITask> _bonusTasks;

        public BindableCollection<ITask> BonusTasks
        {
            get
            {
                return _bonusTasks;
            }
            set
            {
                if (_bonusTasks != value)
                {
                    _bonusTasks = value;
                    NotifyOfPropertyChange(() => BonusTasks);
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
            VisualStateName = "Normal";
            
            new Timer(new TimerCallback((obj) =>
                {
                    //Game changes
                    if (!String.IsNullOrEmpty(Game.ListOfChanges))
                        System.Windows.Deployment.Current.Dispatcher.BeginInvoke(() =>
                        {
                            if (Game.GameState != GameState.Won && Game.GameState != GameState.Lost)
                                MessageBox.Show(Game.ListOfChanges);

                            Game.ListOfChanges = null;
                            using (var uow = _unitOfWorkLocator())
                            {
                                uow.GetRepository<IGame>().All().First(g => g.Id == GameId).ListOfChanges = null;
                                uow.Commit();
                            }
                        });

                    //You won/You lost banner
                    if (!Game.GameOverDisplayed && (Game.GameState == GameState.Won || Game.GameState == GameState.Lost))
                    {
                        switch (Game.GameState)
                        {
                            case GameState.Won:
                                VisualStateName = "YouWon";
                                break;
                            case GameState.Lost:
                                VisualStateName = "YouLost";
                                break;
                        }

                        Game.GameOverDisplayed = true;
                        using (var uow = _unitOfWorkLocator())
                        {
                            uow.GetRepository<IGame>().All().First(g => g.Id == GameId).GameOverDisplayed = true;
                            uow.Commit();
                        }
                    }  
                }), null, 700, System.Threading.Timeout.Infinite);
        }

        #endregion

        #region operations

        public void ChangeToNormal()
        {
            VisualStateName = "Normal";
        }

        public void ShowTask(ITask task)
        {
            _navigationService.UriFor<TaskViewModel>().WithParam(t => t.TaskId, task.Id).WithParam(x => x.GameId, GameId).Navigate();
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

                    ActiveTasks = new BindableCollection<ITask>(tasks.Where(t => t.State == TaskState.Active && t.Game.Id == GameId)
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

                    InactiveTasks = new BindableCollection<ITask>(tasks.Where(t => t.State == TaskState.Inactive && t.Game.Id == GameId)                                                                    
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


                    AccomplishedTasks = new BindableCollection<ITask>(tasks.Where(t => t.State == TaskState.Accomplished && t.Game.Id == GameId)                                                                         
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


                    CancelledTasks = new BindableCollection<ITask>(tasks.Where(t => t.State == TaskState.Cancelled && t.Game.Id == GameId)                                                                        
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

                    IGame game = uow.GetRepository<IGame>().All().First(x => x.Id == Game.Id);
                    game.GameState = GameState.Inactive;
                    game.ListOfChanges = null;
                    uow.Commit();
                }
                await RefreshGame();
                _navigationService.GoBack();
            }
        }

        #endregion
    }
}
