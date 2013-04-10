using System;
using System.Collections.Generic;
using System.Data.Linq.Mapping;
using System.Linq;
using System.Text;
using Common;
using System.Device.Location;

namespace UrbanGame.Storage
{
    [Table]
    public class Game : EntityBase, IGame
    {        
        #region Id

        private long _id;

        [Column(IsPrimaryKey = true, IsDbGenerated = true, CanBeNull = false, AutoSync = AutoSync.OnInsert)]
        public long Id
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

        [Column]
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

        #region Operator

        private string _operator;

        [Column]
        public string Operator
        {
            get
            {
                return _operator;
            }
            set
            {
                if (_operator != value)
                {
                    NotifyPropertyChanging("Operator");
                    _operator = value;
                    NotifyPropertyChanged("Operator");
                }
            }
        }
        #endregion

        #region Logo

        private string _logo;

        [Column]
        public string Logo
        {
            get
            {
                return _logo;
            }
            set
            {
                if (_logo != value)
                {
                    NotifyPropertyChanging("Logo");
                    _logo = value;
                    NotifyPropertyChanged("Logo");
                }
            }
        }
        #endregion

        #region GameStart

        private DateTime _gameStart;

        [Column]
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

        [Column]
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

        #region Joined

        private bool _joined;

        [Column]
        public bool Joined
        {
            get
            {
                return _joined;
            }
            set
            {
                if (_joined != value)
                {
                    NotifyPropertyChanging("Joined");
                    _joined = value;
                    NotifyPropertyChanged("Joined");
                }
            }
        }
        #endregion

        #region Points

        private int _points;

        [Column]
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

        [Column]
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

        [Column]
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

        [Column]
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

        [Column]
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

        [Column]
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

        #region GameLocalization

        private GeoCoordinate _gameLocalization;

        [Column]
        public GeoCoordinate GameLocalization
        {
            get
            {
                return _gameLocalization;
            }
            set
            {
                if (_gameLocalization != value)
                {
                    NotifyPropertyChanging("GameLocalization");
                    _gameLocalization = value;
                    NotifyPropertyChanged("GameLocalization");
                }
            }
        }
        #endregion

        #region GameState

        private GameState _gameState;

        [Column]
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

        [Column]
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

        [Column]
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

        [Column]
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
    }
}
