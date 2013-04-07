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
            PopularGames = new BindableCollection<IGame>();
            PrivateGames = new BindableCollection<IGame>();
            RecommendedGames = new BindableCollection<IGame>();
            CloseGames = new BindableCollection<IGame>();

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

                updateGame(AllGames, e.Id, game);
                updateGame(PopularGames, e.Id, game);
                updateGame(PrivateGames, e.Id, game);
                updateGame(RecommendedGames, e.Id, game);
                updateGame(CloseGames, e.Id, game);
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

        #region CloseGames

        private BindableCollection<IGame> _closeGames;

        public BindableCollection<IGame> CloseGames
        {
            get
            {
                return _closeGames;
            }
            set
            {
                if (_closeGames != value)
                {
                    _closeGames = value;
                    NotifyOfPropertyChange(() => CloseGames);
                }
            }
        }
        #endregion

        #region UserGames

        private BindableCollection<IGame> _userGames;

        public BindableCollection<IGame> UserGames
        {
            get
            {
                return _userGames;
            }
            set
            {
                if (_userGames != value)
                {
                    _userGames = value;
                    NotifyOfPropertyChange(() => UserGames);
                }
            }
        }
        #endregion

        #region RecommendedGames

        private BindableCollection<IGame> _recommendedGames;

        public BindableCollection<IGame> RecommendedGames
        {
            get
            {
                return _recommendedGames;
            }
            set
            {
                if (_recommendedGames != value)
                {
                    _recommendedGames = value;
                    NotifyOfPropertyChange(() => RecommendedGames);
                }
            }
        }
        #endregion

        #region PopularGames

        private BindableCollection<IGame> _popularGames;

        public BindableCollection<IGame> PopularGames
        {
            get
            {
                return _popularGames;
            }
            set
            {
                if (_popularGames != value)
                {
                    _popularGames = value;
                    NotifyOfPropertyChange(() => PopularGames);
                }
            }
        }
        #endregion

        #region PrivateGames

        private BindableCollection<IGame> _privateGames;

        public BindableCollection<IGame> PrivateGames
        {
            get
            {
                return _privateGames;
            }
            set
            {
                if (_privateGames != value)
                {
                    _privateGames = value;
                    NotifyOfPropertyChange(() => PrivateGames);
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
                            //PopularGames = new BindableCollection<IGame>(AllGames.Where(g => g.NumberOfPlayers > 20));
                            //PrivateGames = new BindableCollection<IGame>(AllGames.Where(g => ));
                            //RecommendedGames = new BindableCollection<IGame>(AllGames.Where(g => ));
                            //CloseGames = new BindableCollection<IGame>(AllGames.Where(g => ));
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
