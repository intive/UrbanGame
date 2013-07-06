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
using UrbanGame.Utilities;
using UrbanGame.Localization;

namespace UrbanGame.ViewModels
{
    public class GameDetailsPreviewViewModel : BaseViewModel, IHandle<GameChangedEvent>
    {

        IAppbarManager _appbarManager;
        public GameDetailsPreviewViewModel(INavigationService navigationService, Func<IUnitOfWork> unitOfWorkLocator,
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

        private List<AppbarItem> BasicAppbar = new List<AppbarItem>()
        {
            new AppbarItem() {  Text = Localization.AppResources.LogIn,Message="LogoutOrLogin" } 
        };

        private List<AppbarItem> AuthorizedAppbar = new List<AppbarItem>()
        {
            new AppbarItem() {  Text = Localization.AppResources.LogIn,Message="LogoutOrLogin" } ,
            new AppbarItem() { IconUri = new Uri("/Images/appbar.group.add.png", UriKind.Relative), Text = Localization.AppResources.JoinIn, Message = "JoinIn" }
        };

        #endregion

        #region appbar

        public void RefreshMenuItemText()
        {
            if (_authorizationService.IsUserAuthenticated())
            {
                _appbarManager.ChangeItemText("LogoutOrLogin", Localization.AppResources.Logout);
            }
            else
            {
                _appbarManager.ChangeItemText("LogoutOrLogin", Localization.AppResources.LogIn);
            }
        }

        private void SetAppBarContent()
        {
            Deployment.Current.Dispatcher.BeginInvoke(() =>
            {
                if (_authorizationService.IsUserAuthenticated())
                {
                    _appbarManager.ShowAppbar();
                    _appbarManager.ConfigureAppbar(AuthorizedAppbar);
                }
                else
                {
                    _appbarManager.ConfigureAppbar(BasicAppbar);
                }
                RefreshMenuItemText();
            });
        }

        #endregion

        #region IHandle<GameChangedEvent>
        public void Handle(GameChangedEvent game)
        {
            if (game.Id == GameId)
                RefreshGame();
        }
        #endregion

        #region navigation properties

        public int GameId { get; set; }

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

        #region IsJoined

        public bool IsJoined
        {
            get
            {
                return Game != null ? Game.GameState == GameState.Joined : false;
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
        }

        #endregion

        #region operations

        public async void LogoutOrLogin()
        {

            if (!_authorizationService.IsUserAuthenticated())
            {
                _navigationService.UriFor<LoginAndRegistrerViewModel>().Navigate();
            }
            else
            {
                _authorizationService.Logout();

                SetAppBarContent();
            }
        }

        public async Task RefreshGame()
        {
            await Task.Factory.StartNew(async () =>
            {
                if (_authorizationService.IsUserAuthenticated())
                {
                    using (var uow = _unitOfWorkLocator())
                    {
                        IQueryable<IGame> games = uow.GetRepository<IGame>().All();
                        Game = games.FirstOrDefault(g => g.Id == GameId) ?? await _gameWebService.GetGameInfo(GameId);
                    }
                }
                else
                {
                    Game = await _gameWebService.GetGameInfo(GameId);
                }
            });
        }

        public async void JoinIn()
        {
            //todo : disable joining when game hasn't started yet
            if (MessageBox.Show("join in", "join in", MessageBoxButton.OKCancel) == MessageBoxResult.OK)
            {
                bool result = false;
                try
                {
                    await SaveGameToDB();
                    result = await _gameWebService.JoinGame(Game.Id);
                }
                catch
                {                    
                }


                if (result)
                {
                    _navigationService.UriFor<GameDetailsViewModel>().WithParam(x => x.GameId, Game.Id).Navigate();
                }
                else
                {
                    //remove game - reversing changes
                    using (var uow = _unitOfWorkLocator())
                    {
                        var game = uow.GetRepository<IGame>().All().FirstOrDefault(g => g.Id == GameId);
                        if (game != null)
                        {
                            uow.GetRepository<IGame>().MarkForDeletion(game);
                            uow.Commit();
                        }
                    }

                    MessageBox.Show(AppResources.ErrorJoiningGame);
                }
            }
        }

        public async Task SaveGameToDB()
        {
            using (IUnitOfWork uow = _unitOfWorkLocator())
            {
                var newGame = new Game();
                newGame.Id = GameId;
                newGame.Name = Game.Name;
                newGame.OperatorName = Game.OperatorName;
                newGame.GameLogo = Game.GameLogo;
                newGame.Localization = Game.Localization;
                newGame.GameStart = Game.GameStart;
                newGame.GameEnd = Game.GameEnd;
                newGame.GameState = GameState.Joined;
                newGame.NumberOfPlayers = Game.NumberOfPlayers;
                newGame.NumberOfSlots = Game.NumberOfSlots;
                newGame.GameType = Game.GameType;
                newGame.Description = Game.Description;
                newGame.Difficulty = Game.Difficulty;
                newGame.Version = Game.Version;
                newGame.Prizes = Game.Prizes;

                #region Alerts & HighScores

                foreach (var a in Game.Alerts)
                {
                    GameAlert alert = new GameAlert()
                    {
                        Id = a.Id,
                        Description = a.Description,
                        Topic = a.Topic,
                        AlertAppear = a.AlertAppear,
                        Game = newGame,
                        GameId = newGame.Id
                    };
                }

                foreach (var hs in Game.HighScores)
                {
                    GameHighScore highScore = new GameHighScore()
                    {
                        Id = hs.Id,
                        Points = hs.Points,
                        UserLogin = hs.UserLogin,
                        AchievedAt = hs.AchievedAt,
                        Game = newGame,
                        GameId = newGame.Id
                    };
                }

                #endregion

                #region adding tasks

                var tasks = await _gameWebService.GetTasks(newGame.Id);

                foreach (var t in tasks)
                {
                    //todo: replace endDate when will be available in API
                    //todo: add [JsonProperty(Name="something")] to GameTask.cs (WebService) when picture will be available in API
                    //todo: add jsonProperty to GameTask.cs (WebService) when IsRepeatable will be available in API (now it's something strange - maxattemps - which we doesn't handle)
                    //todo: consider what we gonna do with protected access to details preview of a game - maybe it should be changed in API
                    var taskDTO = await _gameWebService.GetTaskDetails(newGame.Id, t.Id);
                    var task = new GameTask()
                    {
                        AdditionalText = taskDTO.AdditionalText,
                        Description = taskDTO.Description,
                        EndDate = DateTime.Now.AddDays(2),//taskDTO.EndDate,
                        Game = newGame,
                        GameId = newGame.Id,
                        Id = taskDTO.Id,
                        IsNewTask = false,
                        IsRepeatable = taskDTO.IsRepeatable,
                        MaxPoints = taskDTO.MaxPoints,
                        Name = taskDTO.Name,
                        Picture = taskDTO.Picture,
                        SolutionStatus = SolutionStatus.NotSend,
                        State = taskDTO.State,
                        Type = taskDTO.Type,
                        UserPoints = taskDTO.UserPoints,
                        Version = taskDTO.Version
                    };

                    if (task.Type == TaskType.ABCD)
                    {
                        foreach (var answerDTO in taskDTO.ABCDPossibleAnswers)
                        {
                            var answer = new ABCDPossibleAnswer()
                            {
                                Answer = answerDTO.Answer,
                                CharId = answerDTO.CharId,
                                Task = task,
                            };
                            task.ABCDPossibleAnswers.Add(answer);
                        }
                    }

                    uow.GetRepository<ITask>().MarkForAdd(task);
                    uow.Commit();
                }

                #endregion
            }
        }

        #endregion
    }
}