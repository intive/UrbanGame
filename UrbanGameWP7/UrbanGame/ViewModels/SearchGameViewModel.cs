using System;
using System.Collections.Generic;
using System.Device.Location;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Caliburn.Micro;
using Common;
using System.Windows;
using Microsoft.Phone.Controls;

namespace UrbanGame.ViewModels
{
    public class SearchGameViewModel : BaseViewModel
    {
        private string _currentSearchPhrase;
        private object _syncRoot = new object();

        public SearchGameViewModel(INavigationService navigationService, Func<IUnitOfWork> unitOfWorkLocator,
                                    IGameWebService gameWebService, IEventAggregator gameEventAggregator, IAppbarManager appbarManager, IGameAuthorizationService authorizationService)
            : base(navigationService, unitOfWorkLocator, gameWebService, gameEventAggregator, authorizationService)
        {
            NearestGames = new BindableCollection<IGame>();
        }


        #region bindable propoerties

        //The binding to Text property of AutoCopleteBox wasn't working, so I used $source as parameter in Events
        #region SearchPhrase

        private string _searchPhrase;

        public string SearchPhrase
        {
            get
            {
                return _searchPhrase;
            }
            set
            {
                if (_searchPhrase != value)
                {
                    _searchPhrase = value;
                    RefreshNearbyGames(SearchPhrase.ToLower());
                    NotifyOfPropertyChange(() => SearchPhrase);
                }
            }
        }

        #endregion

        #region AllNearestGames

        private BindableCollection<IGame> _allNearestGames;

        public BindableCollection<IGame> AllNearestGames
        {
            get
            {
                return _allNearestGames;
            }
            set
            {
                if (_allNearestGames != value)
                {
                    _allNearestGames = value;
                    NotifyOfPropertyChange(() => AllNearestGames);
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

        #endregion

        #region lifecycle

        protected override void OnCreated()
        {
            base.OnCreated();
        }

        protected async override void OnActivate()
        {
            base.OnActivate();
            await GetAllNearbyGames();
        }

        #endregion

        #region operations

        public async Task GetAllNearbyGames()
        {
            await Task.Factory.StartNew(async () =>
            {
                var allNearestGames = (await _gameWebService.UserNearbyGames(new GeoCoordinate(1, 1))).OrderBy(g => g.GameEnd);

                using (var uow = _unitOfWorkLocator())
                {
                    var loadedGames = uow.GetRepository<IGame>().All().Select(x => x.Id);
                    AllNearestGames = new BindableCollection<IGame>(allNearestGames.Where(x => !loadedGames.Contains(x.Id)));
                }
            });
        }

        public void ShowDetails(IGame game)
        {
            //EventArgs isn't working and game is null
            //_navigationService.UriFor<GameDetailsPreviewViewModel>().WithParam(g => g.GameId, game.Id).Navigate();
        }

        public void TextChanged(AutoCompleteBox sender)
        {
            SearchPhrase = sender.Text;
        }

        public void MouseEntered(AutoCompleteBox sender)
        {
            if (sender.Text == Localization.AppResources.Enter)
            {
                sender.Text = "";
            }
        }

        #endregion

        #region private

        private void RefreshNearbyGames(String searchPhrase = null)
        {
            NearestGames = new BindableCollection<IGame>();
            foreach (var game in AllNearestGames)
            {
                if(game.Name.ToLower().Contains(searchPhrase))
                {
                    NearestGames.Add(game);
                }
            }
        }

        #endregion
    }
}
