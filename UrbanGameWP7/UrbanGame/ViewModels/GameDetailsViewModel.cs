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

        public void LogoutOrLogin()
        {
            ToogleMenuItemText();
            if (!_gameWebService.IsAuthorized)
            {
                //to do
                _gameWebService.Authorize("aaaaaaaaa", "fsffasfasf");
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
        }

        public void RefreshGame()
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
        }

        public void JoinIn()
        {
            if (MessageBox.Show("join in", "join in", MessageBoxButton.OKCancel) == MessageBoxResult.OK)
            {
                using (IUnitOfWork uow = _unitOfWorkLocator())
                {


                    if (uow.GetRepository<IGame>().All().ToArray<IGame>().Count() > 0)
                    {
                        var gameToDelete = uow.GetRepository<IGame>().All().First(x => x.Id == Game.Id);
                        uow.GetRepository<IGame>().MarkForDeletion(gameToDelete);
                    }
                    var games = uow.GetRepository<IGame>();
                    games.MarkForAdd(CreateInstance(GameState.Joined, uow));
                    uow.Commit();
                }
                RefreshGame();
                NotifyOfPropertyChange(() => IsJoined);
                SetAppBarContent();
            }
        }

        public void Leave()
        {
            if (MessageBox.Show("leave", "leave", MessageBoxButton.OKCancel) == MessageBoxResult.OK)
            {
                using (IUnitOfWork uow = _unitOfWorkLocator())
                {
                    var gameToDelete = uow.GetRepository<IGame>().All().First(x => x.Id == Game.Id);
                    uow.GetRepository<IGame>().MarkForDeletion(gameToDelete);
                    uow.GetRepository<IGame>().MarkForAdd(CreateInstance(GameState.Inactive, uow));
                    uow.Commit();
                }
                RefreshGame();
                NotifyOfPropertyChange(() => IsJoined);
                SetAppBarContent();
            }
        }

        public IGame CreateInstance(GameState state, IUnitOfWork uow)
        {
            var newGame = uow.GetRepository<IGame>().CreateInstance();
            newGame.Id = GameId;
            newGame.Name = "Hydrozagadka";
            newGame.OperatorName = "CAFETERIA";
            newGame.OperatorLogo = "/ApplicationIcon.png";
            newGame.GameLogo = "/ApplicationIcon.png";
            newGame.GameStart = DateTime.Now.AddHours(3).AddMinutes(23);
            newGame.GameEnd = DateTime.Now.AddDays(2).AddHours(5);
            newGame.GameState = state;
            newGame.NumberOfPlayers = 24;
            newGame.NumberOfSlots = 50;
            newGame.GameType = GameType.Quiz;
            newGame.Description = DateTime.Now.ToLongTimeString() + "\nsadsa sad ads  adsa dssa sad  asas asd as a sas as as  asas  asdas as ads as d";
            newGame.Difficulty = GameDifficulty.Medium;
            newGame.Prizes = "1st Bicycle\n2nd Bicycle\n3rd Bicycle\n4-8th Bicycle bicycle bicycle";
            return newGame;
        }

        #endregion
    }
}
