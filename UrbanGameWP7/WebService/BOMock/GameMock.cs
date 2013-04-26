using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Common;
using System.Device.Location;

namespace WebService.BOMock
{
    public class GameMock : BOBase, IGame
    {        
        #region Id

        private int _id;

        public int Id
        {
            get
            {
                return _id;
            }
            set
            {
                if (_id != value)
                {
                    NotifyPropertyChanging("Id");
                    _id = value;
                    NotifyPropertyChanged("Id");
                }
            }
        }
        #endregion

        #region Name

        private string _name;

        public string Name
        {
            get
            {
                return _name;
            }
            set
            {
                if (_name != value)
                {
                    NotifyPropertyChanging("Name");
                    _name = value;
                    NotifyPropertyChanged("Name");
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
                    NotifyPropertyChanging("OperatorName");
                    _operatorName = value;
                    NotifyPropertyChanged("OperatorName");
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
                    NotifyPropertyChanging("OperatorLogo");
                    _operatorLogo = value;
                    NotifyPropertyChanged("OperatorLogo");
                }
            }
        }
        #endregion

        #region GameLogo

        private string _gameLogo;

        public string GameLogo
        {
            get
            {
                return _gameLogo;
            }
            set
            {
                if (_gameLogo != value)
                {
                    NotifyPropertyChanging("GameLogo");
                    _gameLogo = value;
                    NotifyPropertyChanged("GameLogo");
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
                    NotifyPropertyChanging("GameStart");
                    _gameStart = value;
                    NotifyPropertyChanged("GameStart");
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
                    NotifyPropertyChanging("GameEnd");
                    _gameEnd = value;
                    NotifyPropertyChanged("GameEnd");
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
                    NotifyPropertyChanging("GameType");
                    _gameType = value;
                    NotifyPropertyChanged("GameType");
                }
            }
        }
        #endregion

        #region Points

        private int _points;

        public int Points
        {
            get
            {
                return _points;
            }
            set
            {
                if (_points != value)
                {
                    NotifyPropertyChanging("Points");
                    _points = value;
                    NotifyPropertyChanged("Points");
                }
            }
        }
        #endregion

        #region MaxPoints

        private int _maxPoints;

        public int MaxPoints
        {
            get
            {
                return _maxPoints;
            }
            set
            {
                if (_maxPoints != value)
                {
                    NotifyPropertyChanging("MaxPoints");
                    _maxPoints = value;
                    NotifyPropertyChanged("MaxPoints");
                }
            }
        }
        #endregion

        #region NumberOfTasks

        private int _numberOfTasks;

        public int NumberOfTasks
        {
            get
            {
                return _numberOfTasks;
            }
            set
            {
                if (_numberOfTasks != value)
                {
                    NotifyPropertyChanging("NumberOfTasks");
                    _numberOfTasks = value;
                    NotifyPropertyChanged("NumberOfTasks");
                }
            }
        }
        #endregion

        #region NumberOfCompletedTasks

        private int _numberOfCompletedTasks;

        public int NumberOfCompletedTasks
        {
            get
            {
                return _numberOfCompletedTasks;
            }
            set
            {
                if (_numberOfCompletedTasks != value)
                {
                    NotifyPropertyChanging("NumberOfCompletedTasks");
                    _numberOfCompletedTasks = value;
                    NotifyPropertyChanged("NumberOfCompletedTasks");
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
                    NotifyPropertyChanging("NumberOfPlayers");
                    _numberOfPlayers = value;
                    NotifyPropertyChanged("NumberOfPlayers");
                }
            }
        }
        #endregion

        #region NumberOfSlots

        private int _numberOfSlots;

        public int NumberOfSlots
        {
            get
            {
                return _numberOfSlots;
            }
            set
            {
                if (_numberOfSlots != value)
                {
                    NotifyPropertyChanging("NumberOfSlots");
                    _numberOfSlots = value;
                    NotifyPropertyChanged("NumberOfSlots");
                }
            }
        }
        #endregion

        #region GameLatitude

        private double _gameLatitude;

        public double GameLatitude
        {
            get
            {
                return _gameLatitude;
            }
            set
            {
                if (_gameLatitude != value)
                {
                    NotifyPropertyChanging("GameLatitude");
                    _gameLatitude = value;
                    NotifyPropertyChanged("GameLatitude");
                }
            }
        }
        #endregion

        #region GameLongitude

        private double _gameLongitude;

        public double GameLongitude
        {
            get
            {
                return _gameLongitude;
            }
            set
            {
                if (_gameLongitude != value)
                {
                    NotifyPropertyChanging("GameLongitude");
                    _gameLongitude = value;
                    NotifyPropertyChanged("GameLongitude");
                }
            }
        }
        #endregion

        #region GameState

        private GameState _gameState;

        public GameState GameState
        {
            get
            {
                return _gameState;
            }
            set
            {
                if (_gameState != value)
                {
                    NotifyPropertyChanging("GameState");
                    _gameState = value;
                    NotifyPropertyChanged("GameState");
                }
            }
        }
        #endregion

        #region Rank

        private int? _rank;

        public int? Rank
        {
            get
            {
                return _rank;
            }
            set
            {
                if (_rank != value)
                {
                    NotifyPropertyChanging("Rank");
                    _rank = value;
                    NotifyPropertyChanged("Rank");
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
                    NotifyPropertyChanging("GameDifficulty");
                    _difficulty = value;
                    NotifyPropertyChanged("GameDifficulty");
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
                    NotifyPropertyChanging("Description");
                    _description = value;
                    NotifyPropertyChanged("Description");
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
                    NotifyPropertyChanging("Prizes");
                    _prizes = value;
                    NotifyPropertyChanged("Prizes");
                }
            }
        }
        #endregion

        #region TasksList - IGame

        private IList<ITask> _tasksList = new List<ITask>();

        public IList<ITask> TasksList
        {
            get
            {
                return _tasksList;
            }
        }
        #endregion
    }
}
