using Caliburn.Micro;
using Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WebService;

namespace UrbanGame.ViewModels
{
    public class GamesListViewModel : BaseViewModel
    {
        public GamesListViewModel(INavigationService navigationService, Func<IUnitOfWork> unitOfWorkLocator)
            : base(navigationService, unitOfWorkLocator)
        {
            DownloadedGames = new BindableCollection<IGame>();
            AllGames = new BindableCollection<IGame>();
            UserActiveGames = new BindableCollection<IGame>();
            UserPreviousGames = new BindableCollection<IGame>();
            NearbyGames = new BindableCollection<IGame>();

            //temporarily mock object
            _gameWebService = new GameWebServiceMock();
            _gameWebService.GameChanged += gameWebService_GameChanged;
        }

        #region WebService events

        void gameWebService_GameChanged(object sender, GameEventArgs e)
        {
            Task.Run(() =>
            {
                IGame game = _gameWebService.GetGameInfo(e.Id);

                updateGame(DownloadedGames, e.Id, game);
                updateGame(AllGames, e.Id, game);
                updateGame(UserActiveGames, e.Id, game);
                updateGame(UserPreviousGames, e.Id, game);
                updateGame(NearbyGames, e.Id, game);
            });
            
        }

        #endregion

        #region private methods

        void updateGame(BindableCollection<IGame> games, int gid, IGame newGame)
        {
            for (int i = 0; i < games.Count; i++)
                if (games[i].Id == gid)
                {
                    games[i] = newGame;
                    break;
                }
        }

        #endregion

        #region private fields

        IGameWebService _gameWebService;
        string _currentSearchPhrase;
        object _syncRoot = new object();

        #endregion

        #region bindable properties

        #region DownloadedGames

        private BindableCollection<IGame> _downloadedGames;

        public BindableCollection<IGame> DownloadedGames
        {
            get
            {
                return _downloadedGames;
            }
            set
            {
                if (_downloadedGames != value)
                {
                    _downloadedGames = value;
					NotifyOfPropertyChange(() => DownloadedGames);
                }
            }
        }
        #endregion

        #region AllGames

        private BindableCollection<IGame> _allGames;

        public BindableCollection<IGame> AllGames
        {
            get
            {
                return _allGames;
            }
            set
            {
                if (_allGames != value)
                {
                    _allGames = value;
                    NotifyOfPropertyChange(() => AllGames);
                }
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

        #region UserPreviousGames

        private BindableCollection<IGame> _userPreviousGames;

        public BindableCollection<IGame> UserPreviousGames
        {
            get
            {
                return _userPreviousGames;
            }
            set
            {
                if (_userPreviousGames != value)
                {
                    _userPreviousGames = value;
                    NotifyOfPropertyChange(() => UserPreviousGames);
                }
            }
        }
        #endregion

        #region NearbyGames

        private BindableCollection<IGame> _nearbyGames;

        public BindableCollection<IGame> NearbyGames
        {
            get
            {
                return _nearbyGames;
            }
            set
            {
                if (_nearbyGames != value)
                {
                    _nearbyGames = value;
                    NotifyOfPropertyChange(() => NearbyGames);
                }
            }
        }
        #endregion

        #endregion

        #region lifecycle

        protected override void OnActivate()
        {
            base.OnActivate();
            RefreshGames();
        }

        #endregion

        #region operations

        public void RefreshGames(string searchPhrase = "")
        {         
            Task.Run(() =>
                {
                    lock (_syncRoot)
                    {
                        DownloadedGames.Clear();
                        _currentSearchPhrase = searchPhrase;
                    }

                    DownloadedGames = new BindableCollection<IGame>(_gameWebService.GetGames().OrderBy(g => g.GameEnd)); 

                    lock (_syncRoot)
                    {
                        if (searchPhrase == _currentSearchPhrase)
                        {
                            if (String.IsNullOrEmpty(searchPhrase))
                                AllGames = DownloadedGames;
                            else
                                AllGames = new BindableCollection<IGame>(DownloadedGames.Where(g => g.Name.ToLower().Contains(searchPhrase.ToLower())));

                            //TODO: replace it with correct conditions or implement dedicated method to receive that collections
                            //UserActiveGames = new BindableCollection<IGame>(AllGames.Where(g => g.NumberOfPlayers > 20));
                            //UserPreviousGames = new BindableCollection<IGame>(AllGames.Where(g => ));
                            //UserNearbyGames = new BindableCollection<IGame>(AllGames.Where(g => ));
                        }
                    }                    
                });
        }

        public void ShowDetails(int gid)
        {
            _navigationService.UriFor<GameDetailsViewModel>().WithParam(g => g.GameId, gid).Navigate();
        }

        #endregion
    }
}
