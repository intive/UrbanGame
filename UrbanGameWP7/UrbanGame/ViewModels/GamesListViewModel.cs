using Caliburn.Micro;
using Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Device.Location;
using UrbanGame.Storage;
using System.Windows.Controls;
using Microsoft.Phone.Controls;
using System.Threading.Tasks;
using System.Windows;
using UrbanGame.Models;

namespace UrbanGame.ViewModels
{
    public class GamesListViewModel : BaseViewModel, IHandle<GameChangedEvent>
    {
        IAppbarManager _appbarManager;
        private string _activeSection;

        public GamesListViewModel(INavigationService navigationService, Func<IUnitOfWork> unitOfWorkLocator,
                                  IGameWebService gameWebService, IEventAggregator gameEventAggregator, IAppbarManager appbarManager, IGameAuthorizationService authorizationService)
            : base(navigationService, unitOfWorkLocator, gameWebService, gameEventAggregator, authorizationService)
        {
            UserActiveGames = new BindableCollection<IGame>();
            UserInactiveGames = new BindableCollection<IGame>();
            NearestGames = new BindableCollection<IGame>();
            _appbarManager = appbarManager;
            IsRefreshing = false;
        }

        protected override void OnViewReady(object view)
        {
            ChangeAppbarButtons();
        }

        #region appbar configurations

        private List<AppbarItem> BasicAppbar = new List<AppbarItem>()
        {
            new AppbarItem() {  Text = Localization.AppResources.LogIn,Message="LogoutOrLogin" } 
        };

        private List<AppbarItem> NearbyAppbar = new List<AppbarItem>()
        {
            new AppbarItem() { Text = Localization.AppResources.LogIn,Message="LogoutOrLogin" } ,
            new AppbarItem() { IconUri = new Uri("/Images/appbarSearch.png", UriKind.Relative), Text = Localization.AppResources.Search, Message = "Search" },
            new AppbarItem() { IconUri = new Uri("/Images/appbarRefresh.png", UriKind.Relative), Text = Localization.AppResources.Refresh, Message = "RefreshNearestGames" }
       
        };

        #endregion

        #region IHandle<GameChangedEvent>
        public void Handle(GameChangedEvent e)
        {
            Task.Factory.StartNew(async () =>
            {
                IGame game = await _gameWebService.GetGameInfo(e.Id);
                game.ListOfChanges = "changed";
                UpdateGame(UserActiveGames, game);
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

        #region IsAuthorized

        public bool IsAuthorized
        {
            get
            {
                return _authorizationService.IsUserAuthenticated();
            }

        }

        #endregion

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

        #region User

        private User _user;

        public User User
        {
            get
            {
                return _authorizationService.AuthenticatedUser;
            }
        }
        #endregion

        #endregion

        #region lifecycle

        protected override async void OnActivate()
        {
            base.OnActivate();
            await _authorizationService.LoadUserData();
            await RefreshUserGames();
            RefreshNearestGames();
            NotifyOfPropertyChange(() => User);
        }

        #endregion

        #region operations

        public void RefreshMenuItemText()
        {
            if (IsAuthorized)
            {
                _appbarManager.ChangeItemText("LogoutOrLogin", Localization.AppResources.Logout);
            }
            else
            {
                _appbarManager.ChangeItemText("LogoutOrLogin", Localization.AppResources.LogIn);
            }
        }

        public void ChangeAppbarButtons(SelectionChangedEventArgs args)
        {
            _activeSection = ((FrameworkElement)args.AddedItems[0]).Name;
            ChangeAppbarButtons();
        }

        public void ChangeAppbarButtons()
        {
            if (_activeSection == "Nearby")
            {
                _appbarManager.ConfigureAppbar(NearbyAppbar);
            }
            else
            {
                _appbarManager.ConfigureAppbar(BasicAppbar);
            }
            RefreshMenuItemText();
        }


        public async Task RefreshUserGames()
        {
            NotifyOfPropertyChange(() => IsAuthorized);
            await Task.Factory.StartNew(() =>
            {
                UserActiveGames.Clear();
                UserInactiveGames.Clear();

                if (IsAuthorized)
                {
                    using (var uow = _unitOfWorkLocator())
                    {
                        IQueryable<IGame> games = uow.GetRepository<IGame>().All();

                        UserActiveGames = new BindableCollection<IGame>(games.Where(g => g.GameState == GameState.Joined)
                                                                             .OrderBy(g => g.GameEnd)
                                                                             .AsEnumerable());

                        UserInactiveGames = new BindableCollection<IGame>(games.Where(g => g.GameState != GameState.Joined)
                                                                               .OrderByDescending(g => g.GameStart)
                                                                               .AsEnumerable());
                    }
                }
            });
        }

        public async void RefreshNearestGames()
        {
            if (IsRefreshing)
            {
                return;
            }
            else
            {
                IsRefreshing = true;
            }

            try
            {
                NearestGames.Clear();
                var allNearestGames = (await _gameWebService.UserNearbyGames(new GeoCoordinate(1, 1))).OrderBy(g => g.GameEnd);

                using (var uow = _unitOfWorkLocator())
                {
                    var loadedGames = uow.GetRepository<IGame>().All().Select(x => x.Id);
                    NearestGames = new BindableCollection<IGame>(allNearestGames.Where(x => !loadedGames.Contains(x.Id)));
                }
                    
            }
            finally
            {
                IsRefreshing = false;
            }
        }

        public void ShowDetails(IGame game)
        {
            _navigationService.UriFor<GameDetailsPreviewViewModel>().WithParam(g => g.GameId, game.Id).Navigate();
        }

        public void ShowFullDetails(IGame game)
        {
            _navigationService.UriFor<GameDetailsViewModel>().WithParam(g => g.GameId, game.Id).Navigate();
        }

        public void Search()
        {

        }

        public async void LogoutOrLogin()
        {
            if (IsAuthorized)
            {
                _authorizationService.Logout();
            }
            else
            {
               _navigationService.UriFor<LoginAndRegistrerViewModel>().Navigate();
            }
            RefreshMenuItemText();
            await RefreshUserGames();
        }

        #endregion
    }
}
