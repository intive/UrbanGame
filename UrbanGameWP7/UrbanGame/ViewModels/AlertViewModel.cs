using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Caliburn.Micro;
using Common;

namespace UrbanGame.ViewModels
{
    public class AlertViewModel : BaseViewModel
    {
        public AlertViewModel(INavigationService navigationService, Func<IUnitOfWork> unitOfWorkLocator,
                                    IGameWebService gameWebService, IEventAggregator gameEventAggregator, IAppbarManager appbarManager, IGameAuthorizationService authorizationService)
            : base(navigationService, unitOfWorkLocator, gameWebService, gameEventAggregator, authorizationService)
        {
        }

        #region navigation properties

        public int GameId { get; set; }

        public int AlertId { get; set; }

        #endregion

        #region bindable propoerties

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


        #region CurrentAlert

        private IAlert _currentAlert;

        public IAlert CurrentAlert
        {
            get
            {
                return _currentAlert;
            }
            set
            {
                if (_currentAlert != value)
                {
                    _currentAlert = value;
                    NotifyOfPropertyChange(() => CurrentAlert);
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
            await RefreshAlert();
        }

        #endregion

        #region operations

        public async Task RefreshGame()
        {
            await Task.Factory.StartNew(async () =>
            {
                using (var uow = _unitOfWorkLocator())
                {
                    IQueryable<IGame> games = uow.GetRepository<IGame>().All();
                    Game = games.FirstOrDefault(g => g.Id == GameId) ?? await _gameWebService.GetGameInfo(GameId);
                }
            });
        }

        public async Task RefreshAlert()
        {
            await Task.Factory.StartNew(() =>
            {
                using (var uow = _unitOfWorkLocator())
                {
                    IQueryable<IAlert> alerts = uow.GetRepository<IAlert>().All();
                    CurrentAlert = alerts.FirstOrDefault(a => a.Id == AlertId);
                }
            });
        }


        #endregion
    }
}
