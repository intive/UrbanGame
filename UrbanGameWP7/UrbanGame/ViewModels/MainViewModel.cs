using Caliburn.Micro;
using Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace UrbanGame.ViewModels
{
    public class MainViewModel : BaseViewModel
    {
        public MainViewModel(INavigationService navigationService, Func<IUnitOfWork> unitOfWorkLocator,
                             IGameWebService gameWebService, IEventAggregator gameEventAggregator)
            : base(navigationService, unitOfWorkLocator, gameWebService, gameEventAggregator)
        {
            
        }

        public void ShowDetails()
        {
            _navigationService.UriFor<GameDetailsViewModel>().WithParam(g => g.GameId, 1).Navigate();
        }
    }
}
