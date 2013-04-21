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

        protected override void OnActivate()
        {
            base.OnActivate();
            RefreshGame();
            RemoveButtonItem(Localization.AppResources.JoinIn);
            RemoveButtonItem(Localization.AppResources.Leave);
            _gameWebService.Authorize("ffsdfsf", "fsfdf");//this line should be deleted
            if (_gameWebService.IsAuthorized)
            {
                if (Game.GameState == GameState.Joined)
                {
                    AddButtonItem(new AppBarButton() { IconUri = new Uri("/Images/appbarSearch.png", UriKind.Relative), Text = Localization.AppResources.Leave, Message = Localization.AppResources.Leave }, Leave);
                }
                else if (Game.GameState == GameState.None)
                {
                    AddButtonItem(new AppBarButton() { IconUri = new Uri("/Images/appbar.check.png", UriKind.Relative), Text = Localization.AppResources.JoinIn, Message = Localization.AppResources.JoinIn }, JoinIn);
                }
            }
        }

        #endregion

        #region operations

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
            if(MessageBox.Show("join in","join in",MessageBoxButton.OKCancel) == MessageBoxResult.OK)
            {
                using (IUnitOfWork uow = _unitOfWorkLocator())
                {
                    var games = uow.GetRepository<IGame>();
                    Game.GameState = GameState.Joined;
                    games.MarkForAdd(new UrbanGame.Storage.Game() { Id = Game.Id });
                    uow.Commit();
                    NotifyOfPropertyChange(() => IsJoined);
                }
                RemoveButtonItem(Localization.AppResources.JoinIn);
                AddButtonItem(new AppBarButton() { IconUri = new Uri("/Images/appbarSearch.png", UriKind.Relative), Text = Localization.AppResources.Leave, Message = Localization.AppResources.Leave }, Leave);
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
                    uow.Commit();
                    Game.GameState = GameState.None;
                    NotifyOfPropertyChange(() => IsJoined);
                }
                AddButtonItem(new AppBarButton() { IconUri = new Uri("/Images/appbar.check.png", UriKind.Relative), Text = Localization.AppResources.JoinIn, Message = Localization.AppResources.JoinIn }, JoinIn);
                RemoveButtonItem(Localization.AppResources.Leave);
            }
        }

        #endregion
    }
}
