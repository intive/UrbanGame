using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows;
using Common;
using UrbanGame.Utilities;
using System.Text;
using Caliburn.Micro;
using System.Text.RegularExpressions;
using System.Windows.Threading;
using System.Windows.Controls;

namespace UrbanGame.ViewModels
{
    public class LoginAndRegistrerViewModel :BaseViewModel
    {
        IAppbarManager _appbarManager;
        private string _previousState;

        public LoginAndRegistrerViewModel(INavigationService navigationService, Func<IUnitOfWork> unitOfWorkLocator,
                                    IGameWebService gameWebService, IEventAggregator gameEventAggregator, IAppbarManager appbarManager, IGameAuthorizationService authorizationService)
            : base(navigationService, unitOfWorkLocator, gameWebService, gameEventAggregator, authorizationService)
        {
            _appbarManager = appbarManager;          
        }

        protected override void OnViewReady(object view)
        {
            SetAppBarContent();
        }

        #region appbar

        public void RefreshMenuItemText()
        {

        }

        private void SetAppBarContent()
        {
            Deployment.Current.Dispatcher.BeginInvoke(() =>
            {
                RefreshMenuItemText();
            });
            _appbarManager.HideAppbar();
        }

        #endregion

        #region bindable properties

        #region VisualStateName

        private string _visualStateName;

        public string VisualStateName
        {
            get
            {
                return _visualStateName;
            }
            set
            {
                if (_visualStateName != value)
                {
                    _visualStateName = value;
                    NotifyOfPropertyChange(() => VisualStateName);
                }
            }
        }

        #endregion

        #region Login

        private string _login;

        public string Login
        {
            get
            {
                return _login;
            }
            set
            {
                if (_login != value)
                {
                    _login = value;
                    NotifyOfPropertyChange(() => Login);
                }
            }
        }

        #endregion

        #region Email

        private string _email;

        public string Email
        {
            get
            {
                return _email;
            }
            set
            {
                if (_email != value)
                {
                    _email = value;
                    NotifyOfPropertyChange(() => Email);
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

        protected override void OnActivate()
        {
            base.OnActivate();
            VisualStateName = "Normal";
        }

        #endregion

        #region operations

        public static bool IsValidEmail(string strIn)
        {
            return Regex.IsMatch(strIn,
                   @"^(?("")("".+?""@)|(([0-9a-zA-Z]((\.(?!\.))|[-!#\$%&'\*\+/=\?\^`\{\}\|~\w])*)(?<=[0-9a-zA-Z])@))" +
                   @"(?(\[)(\[(\d{1,3}\.){3}\d{1,3}\])|(([0-9a-zA-Z][-\w]*[0-9a-zA-Z]\.)+[a-zA-Z]{2,6}))$");
        }

        public async void LogIn(string Password)
        {
            if (!string.IsNullOrWhiteSpace(Email) && IsValidEmail(Email) && !string.IsNullOrWhiteSpace(Password) && await _gameWebService.Authorize(Email, Password) == AuthorizeState.Success)
            {
                VisualStateName = "LoggingIn";

                var result = _authorizationService.LogIn(Email, Password);

                switch (result)
                {
                    case LoginResult.Success: VisualStateName = "LoggedIn"; _previousState = "LoggedIn"; break;
                    case LoginResult.Failure: VisualStateName = "Incorrect"; break;
                    case LoginResult.Timeout: VisualStateName = "Timeout"; break;
                }
            }
            else
            {
                MessageBox.Show(Localization.AppResources.IncorrectLogin, Localization.AppResources.Incorrect, MessageBoxButton.OK);
            }
        }

        public async void CreateAccount(string Password, string PasswordConfirmation)
        {
            if (string.IsNullOrWhiteSpace(Login))
            {
                MessageBox.Show(Localization.AppResources.EmptyLogin);
            }
            else if (string.IsNullOrWhiteSpace(Email))
            {
                MessageBox.Show(Localization.AppResources.IncorrectEmail);
            }
            else if (!IsValidEmail(Email))
            {
                MessageBox.Show(Localization.AppResources.IncorrectEmail);
            }
            else if (string.IsNullOrWhiteSpace(Password))
            {
                MessageBox.Show(Localization.AppResources.EmptyPassword);
            }
            else if (string.IsNullOrWhiteSpace(PasswordConfirmation) || Password != PasswordConfirmation)
            {
                MessageBox.Show(Localization.AppResources.RepeatYourPassword);
            }
            else
            {
                VisualStateName = "CreatingAccount";
                var result = _authorizationService.Register(new User { Login = Login, Password = Password, Email = Email });

                switch (result)
                {
                    case RegisterResult.Success: 
                        VisualStateName = "AccountCreated"; 
                        break;
                    case RegisterResult.Failure:
                        VisualStateName = "LoginUnavailable";
                        break;
                    case RegisterResult.Timeout:
                        _previousState = "CreatingAccount";
                        VisualStateName = "Timeout";
                        break;
                    default:
                        VisualStateName = "UnknownError";
                        break;
                }
            }
        }

                

        public void Retry()
        {
            switch (_previousState)
            {
                case "CreatingAccount": CreateAccount(_authorizationService.AuthenticatedUser.Password, _authorizationService.AuthenticatedUser.Password); break;
                case "LoggingIn": LogIn(_authorizationService.AuthenticatedUser.Password); break;
            }
        }

        public void ChangeToNormal()
        {
            VisualStateName = "Normal";

            if (_previousState == "LoggedIn")
            {
                _navigationService.GoBack();
            }
            else
            {
                _authorizationService.AuthenticatedUser = null;
            }
        }

        #endregion
    }
}
