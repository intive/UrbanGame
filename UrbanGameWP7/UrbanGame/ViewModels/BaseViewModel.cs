using Caliburn.Micro;
using Common;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
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

        }

        public IApplicationBar GetAppBar()
        {
            var rootPage = ((PhoneApplicationFrame)(App.Current.RootVisual)).Content as PhoneApplicationPage;

            return rootPage.ApplicationBar;
        }

        public void AddButtonItem(AppBarButton item, System.Action callback)
        {
            item.Click += (ea, obj) => { callback(); };

            GetAppBar().Buttons.Add(item);
        }

        public void AddMenuItem(AppBarMenuItem item, System.Action callback)
        {
            item.Click += (ea, obj) => { callback(); };

            GetAppBar().MenuItems.Add(item);
        }

        public void RemoveButtonItem(string ItemMessage)
        {
            var appbarButtons = GetAppBar().Buttons;
            AppBarButton toRemove = null;

            foreach (AppBarButton button in appbarButtons)
            {
                if (button.Message == ItemMessage)
                {
                    toRemove = button;
                    break;
                }
            }
            if (toRemove != null)
            {
                appbarButtons.Remove(toRemove);
            }
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
