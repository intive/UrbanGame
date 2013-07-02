using System;
using System.Collections.Generic;
using Common;
using System.Linq;
using System.Text;
using Caliburn.Micro;
using System.Windows;
using System.Text.RegularExpressions;

namespace UrbanGame.ViewModels
{
    public class PasswordRecoveryViewModel :BaseViewModel
    {
        public PasswordRecoveryViewModel(INavigationService navigationService, Func<IUnitOfWork> unitOfWorkLocator,
                                  IGameWebService gameWebService, IEventAggregator gameEventAggregator, IAppbarManager appbarManager, IGameAuthorizationService authorizationService)
            : base(navigationService, unitOfWorkLocator, gameWebService, gameEventAggregator, authorizationService)
        {
        }

        #region bindable properties

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
                    NotifyOfPropertyChange("Email");
                }
            }
        }

        #endregion

        #region operations

        public static bool IsValidEmail(string strIn)
        {
            return Regex.IsMatch(strIn,
                   @"^(?("")("".+?""@)|(([0-9a-zA-Z]((\.(?!\.))|[-!#\$%&'\*\+/=\?\^`\{\}\|~\w])*)(?<=[0-9a-zA-Z])@))" +
                   @"(?(\[)(\[(\d{1,3}\.){3}\d{1,3}\])|(([0-9a-zA-Z][-\w]*[0-9a-zA-Z]\.)+[a-zA-Z]{2,6}))$");
        }

        public void SendPasswordReceverRequest()
        {
            if (IsValidEmail(Email))
            {
                //to do run web service request send method
                //_gameWebService.PasswordRecovery(Email)

                MessageBox.Show(Localization.AppResources.NextInstructions, Localization.AppResources.PasswordRecovery, MessageBoxButton.OK);
            }
            else
            {
                MessageBox.Show(Localization.AppResources.IncorrectEmail, Localization.AppResources.Incorrect, MessageBoxButton.OK);
            }
        }

        #endregion
    }
}
