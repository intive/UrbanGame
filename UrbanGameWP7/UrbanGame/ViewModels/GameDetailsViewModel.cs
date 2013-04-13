using Caliburn.Micro;
using Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UrbanGame.Storage;
using WebService;

namespace UrbanGame.ViewModels
{
    public class GameDetailsViewModel : BaseViewModel
    {
        public GameDetailsViewModel(INavigationService navigationService, Func<IUnitOfWork> unitOfWorkLocator)
            : base(navigationService, unitOfWorkLocator)
        {
            //temporarily mock object
            _gameWebService = new GameWebServiceMock();
            _gameWebService.GameChanged += GameWebService_GameChanged;
        }

        #region navigation properties

        public int GameId { get; set; }

        #endregion

        #region private

        IGameWebService _gameWebService;

        void GameWebService_GameChanged(object sender, GameEventArgs e)
        {
            if (e.Id == GameId)
                RefreshGame();
        }

        void RefreshGame()
        {
            Task.Run(() =>
            {                
                if (_gameWebService.IsAuthorized)
                {
                    IQueryable<Game> games = _unitOfWorkLocator().GetRepository<Game>().All();
                    Game = games.FirstOrDefault(g => g.Id == GameId) ?? _gameWebService.GetGameInfo(GameId);
                }
                else
                {
                    Game = _gameWebService.GetGameInfo(GameId);
                }                
            });
        }

        #endregion        

        #region lifecycle

        protected override void OnActivate()
        {
            base.OnActivate();
            RefreshGame();
        }

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

        #endregion
    }
}
