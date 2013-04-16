using Caliburn.Micro;
using Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
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
                Task.Run(() =>  Game = _gameWebService.GetGameInfo(game.Id));
        }
        #endregion

        #region navigation properties

        public int GameId { get; set; }

        #endregion

        #region private

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
