using Caliburn.Micro;
using Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace UrbanGame.ViewModels
{
    public class GameDetailsViewModel : BaseViewModel
    {
        public GameDetailsViewModel(INavigationService navigationService, Func<IUnitOfWork> unitOfWorkLocator)
            : base(navigationService, unitOfWorkLocator)
        {
            //GUI Test Data - will be removed later
            GameName = "Hydrozagadka";
            OperatorName = "CAFETERIA";
            OperatorLogo = "/ApplicationIcon.png";
            GameEnd = DateTime.Now.AddDays(2).AddHours(5);
            NumberOfPlayers = 24;
            MaxPlayers = 50;
            Description = "sadsa sad ads  adsa dssa sad  asas asd as a sas as as  asas  asdas as ads as d";
        }

        #region bindable properties

        #region GameName

        private string _gameName;

        public string GameName
        {
            get
            {
                return _gameName;
            }
            set
            {
                if (_gameName != value)
                {                    
                    _gameName = value;
                    NotifyOfPropertyChange(() => GameName);
                }
            }
        }
        #endregion

        #region OperatorName

        private string _operatorName;

        public string OperatorName
        {
            get
            {
                return _operatorName;
            }
            set
            {
                if (_operatorName != value)
                {
                    _operatorName = value;
                    NotifyOfPropertyChange(() => OperatorName);
                }
            }
        }
        #endregion

        #region OperatorLogo

        private string _operatorLogo;

        public string OperatorLogo
        {
            get
            {
                return _operatorLogo;
            }
            set
            {
                if (_operatorLogo != value)
                {
                    _operatorLogo = value;
                    NotifyOfPropertyChange(() => OperatorLogo);
                }
            }
        }
        #endregion

        #region GameEnd

        private DateTime _gameEnd;

        public DateTime GameEnd
        {
            get
            {
                return _gameEnd;
            }
            set
            {
                if (_gameEnd != value)
                {
                    _gameEnd = value;
                    NotifyOfPropertyChange(() => GameEnd);
                }
            }
        }
        #endregion

        #region NumberOfPlayers

        private int _numberOfPlayers;

        public int NumberOfPlayers
        {
            get
            {
                return _numberOfPlayers;
            }
            set
            {
                if (_numberOfPlayers != value)
                {
                    _numberOfPlayers = value;
                    NotifyOfPropertyChange(() => NumberOfPlayers);
                }
            }
        }
        #endregion

        #region MaxPlayers

        private int _maxPlayers;

        public int MaxPlayers
        {
            get
            {
                return _maxPlayers;
            }
            set
            {
                if (_maxPlayers != value)
                {
                    _maxPlayers = value;
                    NotifyOfPropertyChange(() => MaxPlayers);
                }
            }
        }
        #endregion

        #region Description

        private string _description;

        public string Description
        {
            get
            {
                return _description;
            }
            set
            {
                if (_description != value)
                {
                    _description = value;
                    NotifyOfPropertyChange(() => Description);
                }
            }
        }
        #endregion

        #endregion
    }
}
