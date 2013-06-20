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

namespace UrbanGame.ViewModels
{
    public class LoginAndRegistrerViewModel :BaseViewModel
    {
        IAppbarManager _appbarManager;

        public LoginAndRegistrerViewModel(INavigationService navigationService, Func<IUnitOfWork> unitOfWorkLocator,
                                    IGameWebService gameWebService, IEventAggregator gameEventAggregator, IAppbarManager appbarManager)
            : base(navigationService, unitOfWorkLocator, gameWebService, gameEventAggregator)
        {
            _appbarManager = appbarManager;
        }

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

        #region Password

        private string _password;

        public string Password
        {
            get
            {
                return _password;
            }
            set
            {
                if (_password != value)
                {
                    _password = value;
                    NotifyOfPropertyChange(() => Password);
                }
            }
        }

        #endregion

        #region PasswordConfirmation

        private string _passwordConfirmation;

        public string PasswordConfirmation
        {
            get
            {
                return _passwordConfirmation;
            }
            set
            {
                if (_passwordConfirmation != value)
                {
                    _passwordConfirmation = value;
                    NotifyOfPropertyChange(() => PasswordConfirmation);
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

        public void LogIn()
        {
            if (!string.IsNullOrWhiteSpace(Email) && !string.IsNullOrWhiteSpace(Password))
            {
                //chceck if email and password are correct in system
            }
            else
            {
                MessageBox.Show("Provided password or e-mail are incorrect", "Incorrect", MessageBoxButton.OK);
            }
        }

        public void CreateAccount()
        {
            if (string.IsNullOrWhiteSpace(Login))
            {
                MessageBox.Show(Localization.AppResources.EmptyLogin);
            }
            else if (!IsValidEmail(Email) || string.IsNullOrWhiteSpace(Email))
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
                DispatcherTimer timer = new DispatcherTimer();
                timer.Tick +=
                delegate(object s, EventArgs args)
                {
                    VisualStateName = "AccountCreated";
                };

                timer.Interval = new TimeSpan(0, 0, 3);
                timer.Start();


                //create account

                
            }
        }

        #endregion
    }
}
