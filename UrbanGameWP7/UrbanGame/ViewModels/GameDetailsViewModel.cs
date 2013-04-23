using Caliburn.Micro;
using Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using UrbanGame.Storage;

namespace UrbanGame.ViewModels
{
    public class GameDetailsViewModel : BaseViewModel, IHandle<GameChangedEvent>
    {
        public GameDetailsViewModel(INavigationService navigationService, Func<IUnitOfWork> unitOfWorkLocator,
                                    IGameWebService gameWebService, IEventAggregator gameEventAggregator)
            : base(navigationService, unitOfWorkLocator, gameWebService, gameEventAggregator)
        {
        }

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
            AddMenuItem(new AppBarMenuItem() { Text = Localization.AppResources.LogIn }, LogoutOrLogin);
        }

        protected override void OnActivate()
        {
            base.OnActivate();
            RefreshGame();
            SetAppBarContent();
        }

        #endregion

        #region operations

        public void ToogleMenuItemText()
        {
            var appbarButtons = GetAppBar().MenuItems;
            foreach (AppBarMenuItem button in appbarButtons)
            {
                if (button.Text == Localization.AppResources.LogIn)
                {
                    button.Text = Localization.AppResources.Logout;
                    break;
                }
                else
                {
                    button.Text = Localization.AppResources.LogIn;
                    break;
                }
            }
        }

        public async void LogoutOrLogin()
        {
            ToogleMenuItemText();
            if (!_gameWebService.IsAuthorized)
            {
                //to do
                _gameWebService.Authorize("username", "password");
                await RefreshGame();
                SetAppBarContent();
            }
            else
            {
                //to do implement login logout
                _gameWebService.IsAuthorized = false;
                SetAppBarContent();
            }
        }

        private void SetAppBarContent()
        {
            Deployment.Current.Dispatcher.BeginInvoke(() =>
                {
                    RemoveButtonItem(Localization.AppResources.JoinIn);
                    RemoveButtonItem(Localization.AppResources.Leave);
                    if (_gameWebService.IsAuthorized)
                    {
                        if (Game.GameState == GameState.Joined)
                        {
                            AddButtonItem(new AppBarButton() { IconUri = new Uri("/Images/appbarSearch.png", UriKind.Relative), Text = Localization.AppResources.Leave, Message = Localization.AppResources.Leave }, Leave);
                        }
                        else
                        {
                            AddButtonItem(new AppBarButton() { IconUri = new Uri("/Images/appbar.check.png", UriKind.Relative), Text = Localization.AppResources.JoinIn, Message = Localization.AppResources.JoinIn }, JoinIn);
                        }
                    }
                });
        }

        public async Task RefreshGame()
        {
            await Task.Factory.StartNew(() =>
                {
                    if (_gameWebService.IsAuthorized)
                    {
                        IQueryable<IGame> games = _unitOfWorkLocator().GetRepository<IGame>().All();
                        Game = games.FirstOrDefault(g => g.Id == GameId) ?? _gameWebService.GetGameInfo(GameId);
                    }
                    else
                    {
                        Game = _gameWebService.GetGameInfo(GameId);
                    }
                });
        }

        public async void JoinIn()
        {
            if (MessageBox.Show("join in", "join in", MessageBoxButton.OKCancel) == MessageBoxResult.OK)
            {
                using (IUnitOfWork uow = _unitOfWorkLocator())
                {

                    //remove a game if stored in the db(it can be inactive)
                    if (uow.GetRepository<IGame>().All().ToArray<IGame>().Count() > 0)
                    {
                        var gameToDelete = uow.GetRepository<IGame>().All().First(x => x.Id == Game.Id);
                        uow.GetRepository<IGame>().MarkForDeletion(gameToDelete);
                    }
                    //store game into the db
                    var games = uow.GetRepository<IGame>();
                    games.MarkForAdd(CreateInstance(GameState.Joined, uow));
                    uow.Commit();
                }
                await RefreshGame();
                NotifyOfPropertyChange(() => IsJoined);
                SetAppBarContent();
            }
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
                NotifyOfPropertyChange(() => IsJoined);
                SetAppBarContent();
            }
        }

        public IGame CreateInstance(GameState state, IUnitOfWork uow)
        {
            var newGame = uow.GetRepository<IGame>().CreateInstance();
            newGame.Id = GameId;
            newGame.Name = Game.Name;
            newGame.OperatorName = Game.OperatorName;
            newGame.OperatorLogo = Game.OperatorLogo;
            newGame.GameLogo = Game.GameLogo;
            newGame.GameStart = Game.GameStart;
            newGame.GameEnd = Game.GameEnd;
            newGame.GameState = state;
            newGame.NumberOfPlayers = Game.NumberOfPlayers;
            newGame.NumberOfSlots = Game.NumberOfSlots;
            newGame.GameType = Game.GameType;
            newGame.Description = Game.Description;
            newGame.Difficulty = Game.Difficulty;
            newGame.Prizes = Game.Prizes;
            return newGame;
        }

        #endregion
    }
}
