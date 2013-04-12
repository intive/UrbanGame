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
            GameStart = DateTime.Now.AddHours(3).AddMinutes(23);
            GameEnd = DateTime.Now.AddDays(2).AddHours(5);
            NumberOfPlayers = 24;
            MaxPlayers = 50;
            GameType = GameType.HighestScore;
            Description = "sadsa sad ads  adsa dssa sad  asas asd as a sas as as  asas  asdas as ads as d";
            Difficulty = GameDifficulty.Medium;
            Prizes = "1st Bicycle\n2nd Bicycle\n3rd Bicycle\n4-8th Bicycle bicycle bicycle";
        }

        #region navigation properties

        public int GameId { get; set; }

        #endregion

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

        #region GameStart

        private DateTime _gameStart;

        public DateTime GameStart
        {
            get
            {
                return _gameStart;
            }
            set
            {
                if (_gameStart != value)
                {
                    _gameStart = value;
                    NotifyOfPropertyChange(() => GameStart);
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

        #region GameType

        private GameType _gameType;

        public GameType GameType
        {
            get
            {
                return _gameType;
            }
            set
            {
                if (_gameType != value)
                {
                    _gameType = value;
                    NotifyOfPropertyChange(() => GameType);
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

        #region Difficulty

        private GameDifficulty _difficulty;

        public GameDifficulty Difficulty
        {
            get
            {
                return _difficulty;
            }
            set
            {
                if (_difficulty != value)
                {
                    _difficulty = value;
                    NotifyOfPropertyChange(() => Difficulty);
                }
            }
        }
        #endregion

        #region Prizes

        private string _prizes;

        public string Prizes
        {
            get
            {
                return _prizes;
            }
            set
            {
                if (_prizes != value)
                {
                    _prizes = value;
                    NotifyOfPropertyChange(() => Prizes);
                }
            }
        }
        #endregion

        #endregion
    }
}
