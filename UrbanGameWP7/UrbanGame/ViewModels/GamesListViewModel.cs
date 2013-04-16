using Caliburn.Micro;
using Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Device.Location;
using UrbanGame.Storage;

namespace UrbanGame.ViewModels
{
    public class GamesListViewModel : BaseViewModel, IHandle<GameChangedEvent>
    {
        public GamesListViewModel(INavigationService navigationService, Func<IUnitOfWork> unitOfWorkLocator,
                                  IGameWebService gameWebService, IEventAggregator gameEventAggregator)
            : base(navigationService, unitOfWorkLocator, gameWebService, gameEventAggregator)
        {            
            UserActiveGames = new BindableCollection<IGame>();
            UserInactiveGames = new BindableCollection<IGame>();
            NearestGames = new BindableCollection<IGame>();

            IsRefreshing = false;
        }

        #region IHandle<GameChangedEvent>
        public void Handle(GameChangedEvent e)
        {
            Task.Run(() =>
                {
                    IGame game = _gameWebService.GetGameInfo(e.Id);

                    UpdateGame(UserActiveGames, game);
                    UpdateGame(UserInactiveGames, game);
                    UpdateGame(NearestGames, game);
                });
        }
        #endregion

        #region private

        void UpdateGame(BindableCollection<IGame> games, IGame newGame)
        {
            for (int i = 0; i < games.Count; i++)
                if (games[i].Id == newGame.Id)
                {
                    games[i] = newGame;
                    break;
                }
        }

        #endregion

        #region bindable properties

        #region UserActiveGames

        private BindableCollection<IGame> _userActiveGames;

        public BindableCollection<IGame> UserActiveGames
        {
            get
            {
                return _userActiveGames;
            }
            set
            {
                if (_userActiveGames != value)
                {
                    _userActiveGames = value;
                    NotifyOfPropertyChange(() => UserActiveGames);
                }
            }
        }
        #endregion

        #region UserInactiveGames

        private BindableCollection<IGame> _userInactiveGames;

        public BindableCollection<IGame> UserInactiveGames
        {
            get
            {
                return _userInactiveGames;
            }
            set
            {
                if (_userInactiveGames != value)
                {
                    _userInactiveGames = value;
                    NotifyOfPropertyChange(() => UserInactiveGames);
                }
            }
        }
        #endregion

        #region NearestGames

        private BindableCollection<IGame> _nearestGames;

        public BindableCollection<IGame> NearestGames
        {
            get
            {
                return _nearestGames;
            }
            set
            {
                if (_nearestGames != value)
                {
                    _nearestGames = value;
                    NotifyOfPropertyChange(() => NearestGames);
                }
            }
        }
        #endregion

        #region IsRefreshing

        private bool _isRefreshing;

        public bool IsRefreshing
        {
            get
            {
                return _isRefreshing;
            }
            set
            {
                if (_isRefreshing != value)
                {
                    _isRefreshing = value;
                    NotifyOfPropertyChange(() => IsRefreshing);
                }
            }
        }
        #endregion

        #region UserAvatar

        private string _userAvatar;

        public string UserAvatar
        {
            get
            {
                return _userAvatar;
            }
            set
            {
                if (_userAvatar != value)
                {
                    _userAvatar = value;
                    NotifyOfPropertyChange(() => UserAvatar);
                }
            }
        }
        #endregion

        #region UserName

        private string _userName;

        public string UserName
        {
            get
            {
                return _userName;
            }
            set
            {
                if (_userName != value)
                {
                    _userName = value;
                    NotifyOfPropertyChange(() => UserName);
                }
            }
        }
        #endregion

        #region UserMail

        private string _userMail;

        public string UserMail
        {
            get
            {
                return _userMail;
            }
            set
            {
                if (_userMail != value)
                {
                    _userMail = value;
                    NotifyOfPropertyChange(() => UserMail);
                }
            }
        }
        #endregion

        #endregion

        #region lifecycle

        protected override void OnActivate()
        {
            base.OnActivate();
            RefreshUserGames();
            RefreshNearestGames();
        }

        #endregion

        #region operations

        public void RefreshUserGames()
        {
            Task.Run(() =>
            {
                UserActiveGames = null;
                UserInactiveGames = null;

                if (_gameWebService.IsAuthorized)
                {   
                    IQueryable<IGame> games = _unitOfWorkLocator().GetRepository<IGame>().All();

                    UserActiveGames = new BindableCollection<IGame>(games.Where(g => g.GameState == GameState.Joined)
                                                                         .OrderBy(g => g.GameEnd)
                                                                         .AsEnumerable());

                    UserInactiveGames = new BindableCollection<IGame>(games.Where(g => g.GameState == GameState.Ended || 
                                                                                       g.GameState == GameState.Won || 
                                                                                       g.GameState == GameState.Withdraw)
                                                                           .OrderByDescending(g => g.GameStart)
                                                                           .AsEnumerable());                   
                }                   
            });
        }

        public void RefreshNearestGames()
        {
            if (IsRefreshing) 
                return;
            else
                IsRefreshing = true;

            Task.Run(() =>
            {
                try
                {
                    NearestGames = null;
                    NearestGames = new BindableCollection<IGame>(_gameWebService.UserNearbyGames(new GeoCoordinate()).OrderBy(g => g.GameEnd));
                }
                finally
                {
                    IsRefreshing = false;                        
                }
            });
        }

        public void ShowDetails(int gid)
        {            
            _navigationService.UriFor<GameDetailsViewModel>().WithParam(g => g.GameId, gid).Navigate();
        }

        public void Logout()
        {
            throw new NotImplementedException();
        }

        #endregion
    }
}
