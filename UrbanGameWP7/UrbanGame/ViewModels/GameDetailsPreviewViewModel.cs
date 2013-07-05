﻿using Caliburn.Micro;
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
            if (MessageBox.Show("join in", "join in", MessageBoxButton.OKCancel) == MessageBoxResult.OK)
            {
                var result = await _gameWebService.JoinGame(Game.Id);
                if (result)
                {
                    using (IUnitOfWork uow = _unitOfWorkLocator())
                    {
                        IGame game = uow.GetRepository<IGame>().All().FirstOrDefault(x => x.Id == Game.Id);

                        if (game == null)
                            uow.GetRepository<IGame>().MarkForAdd(CreateInstance(GameState.Joined, uow));
                        else
                            game.GameState = GameState.Joined;

                        uow.Commit();
                    }
                    _navigationService.UriFor<GameDetailsViewModel>().WithParam(x => x.GameId, Game.Id).Navigate();
                }
                else
                {
                    MessageBox.Show(AppResources.ErrorJoiningGame);
                }
            }
        }

        public IGame CreateInstance(GameState state, IUnitOfWork uow)
        {
            var newGame = uow.GetRepository<IGame>().CreateInstance();
            newGame.Id = GameId;
            newGame.Name = Game.Name;
            newGame.OperatorName = Game.OperatorName;
            newGame.GameLogo = Game.GameLogo;
            newGame.Localization = Game.Localization;
            newGame.GameStart = Game.GameStart;
            newGame.GameEnd = Game.GameEnd;
            newGame.GameState = state;
            newGame.NumberOfPlayers = Game.NumberOfPlayers;
            newGame.NumberOfSlots = Game.NumberOfSlots;
            newGame.GameType = Game.GameType;
            newGame.Description = Game.Description;
            newGame.Difficulty = Game.Difficulty;
            newGame.Version = Game.Version;
            newGame.Prizes = Game.Prizes;

            foreach (var t in Game.Tasks)
            {
                GameTask task = new GameTask()
                {
                    AdditionalText = t.AdditionalText,
                    Description = t.Description,
                    EndDate = t.EndDate,
                    Game = (Game)newGame,
                    GameId = newGame.Id,
                    Id = t.Id,
                    IsRepeatable = t.IsRepeatable,
                    MaxPoints = t.MaxPoints,
                    Name = t.Name,
                    Picture = t.Picture,
                    SolutionStatus = t.SolutionStatus,
                    State = t.State,
                    Type = t.Type,
                    UserPoints = t.UserPoints,
                    Version = t.Version
                };
                foreach (var a in t.ABCDPossibleAnswers)
                    task.ABCDPossibleAnswers.Add(new ABCDPossibleAnswer() { Answer = a.Answer, CharId = a.CharId, Task = task, TaskId = task.Id });

                newGame.Tasks.Add(task);
            }

            return newGame;
        }

        #endregion
    }
}