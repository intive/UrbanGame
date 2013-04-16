using Caliburn.Micro;
using Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using WebService;

namespace UrbanGame.ViewModels
{
    public class BaseViewModel : Screen
    {
        protected INavigationService _navigationService;
        protected Func<IUnitOfWork> _unitOfWorkLocator;
        protected IGameWebService _gameWebService;
        protected IEventAggregator _eventAggregator;
        bool _creating = true;

        public BaseViewModel(INavigationService navigationService, Func<IUnitOfWork> unitOfWorkLocator,
                             IGameWebService gameWebService, IEventAggregator eventAggregator)
        {
            eventAggregator.Subscribe(this);

            _navigationService = navigationService;
            _unitOfWorkLocator = unitOfWorkLocator;
            _gameWebService = gameWebService;
            _eventAggregator = eventAggregator;

            //needs to be commented before running test project
            if (App.GameChangesManager == null)
                App.GameChangesManager = IoC.Get<IGameChangesManager>();
        }

        protected override void OnActivate()
        {
            bool resurecting = AppBootstrapper.Resurecting;

            AppBootstrapper.Resurecting = false;
            base.OnActivate();
            if (resurecting)
            {
                LoadState();
            }
            else if (_creating)
            {
                OnCreated();
                _creating = false;
            }

        }

        protected override void OnDeactivate(bool close)
        {
            base.OnDeactivate(close);
            if (!close)
            {
                SaveState();
            }
        }

        protected virtual void LoadState()
        {

        }

        protected virtual void SaveState()
        {

        }

        protected virtual void OnCreated()
        {

        }

        protected void GoBack()
        {
            if (_navigationService.CanGoBack)
            {
                _navigationService.GoBack();
            }
        }
    }
}
