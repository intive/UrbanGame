using System;
using System.Collections.Generic;
using System.Data.Linq.Mapping;
using System.Linq;
using System.Text;
using Common;
using System.Data.Linq;

namespace UrbanGame.Storage
{
    [Table]
    public class Game : EntityBase, IGame
    {
        public Game() : base()
        {
            _taskRefs = new EntitySet<GameTask>(OnTaskAdded, OnTaskRemoved);
            _alertRefs = new EntitySet<GameAlert>(OnAlertAdded, OnAlertRemoved);
            _highScoreRefs = new EntitySet<GameHighScore>(OnHighScoreAdded, OnHighScoreRemoved);
        }

        const string imagesUrl = "http://urbangame.patronage.blstream.com/assets/images/";

        #region Id

        private int _id;

        [Column(IsPrimaryKey = true)]
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

        #region OperatorName

        private string _operatorName;

        [Column]
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

        #region GameLogo

        private string _gameLogo;

        [Column]
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
                    NotifyPropertyChanged("GameLogoFullUrl");
                }
            }
        }
        #endregion

        #region GameLogoFullUrl
        public string GameLogoFullUrl
        {
            get
            {
                return imagesUrl + GameLogo;
            }
        }
        #endregion

        #region Localization

        private string _localization;

        [Column]
        public string Localization
        {
            get
            {
                return _localization;
            }
            set
            {
                if (_localization != value)
                {
                    NotifyPropertyChanging("Localization");
                    _localization = value;
                    NotifyPropertyChanged("Localization");
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

        #region GameType

        private GameType _gameType;

        [Column]
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

        #region GameLatitude

        private double _gameLatitude;

        [Column]
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

        [Column]
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

        #region GameOverDisplayed
        private bool _gameOverDisplayed;

        [Column]
        public bool GameOverDisplayed
        {
            get
            {
                return _gameOverDisplayed;
            }
            set
            {
                if (_gameOverDisplayed != value)
                {
                    NotifyPropertyChanging("GameOverDisplayed");
                    _gameOverDisplayed = value;
                    NotifyPropertyChanged("GameOverDisplayed");
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

        #region Prizes

        private string _prizes;

        [Column]
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

        #region Tasks

        private EntitySet<GameTask> _taskRefs;

        [Association(Name = "FK_Game_Tasks", Storage = "_taskRefs", ThisKey = "Id", OtherKey = "GameId", DeleteRule = "CASCADE")]
        public EntitySet<GameTask> Tasks
        {
            get { return _taskRefs; }
        }

        private void OnTaskAdded(GameTask task)
        {
            task.Game = this;
        }

        private void OnTaskRemoved(GameTask task)
        {
            task.Game = null;
        }

        #endregion

        #region IGame.Tasks
        IEntityEnumerable<ITask> IGame.Tasks
        {
            get
            {
                return new EntityEnumerable<ITask, GameTask>(_taskRefs);
            }
        }
        #endregion
        
        #region Alerts

        private EntitySet<GameAlert> _alertRefs;

        [Association(Name = "FK_Game_Alerts", Storage = "_alertRefs", ThisKey = "Id", OtherKey = "GameId", DeleteRule = "CASCADE")]
        public EntitySet<GameAlert> Alerts
        {
            get { return _alertRefs; }
        }

        private void OnAlertAdded(GameAlert alert)
        {
            alert.Game = this;
        }

        private void OnAlertRemoved(GameAlert alert)
        {
            alert.Game = null;
        }

        #endregion

        #region IGame.Alerts
        IEntityEnumerable<IAlert> IGame.Alerts
        {
            get
            {
                return new EntityEnumerable<IAlert, GameAlert>(_alertRefs);
            }
        }
        #endregion

        #region High Scores

        private EntitySet<GameHighScore> _highScoreRefs;

        [Association(Name = "FK_Game_HighScores", Storage = "_highScoreRefs", ThisKey = "Id", OtherKey = "GameId", DeleteRule = "CASCADE")]
        public EntitySet<GameHighScore> HighScores
        {
            get { return _highScoreRefs; }
        }

        private void OnHighScoreAdded(GameHighScore highScore)
        {
            highScore.Game = this;
        }

        private void OnHighScoreRemoved(GameHighScore highScore)
        {
            highScore.Game = null;
        }

        #endregion

        #region IGame.HighScores
        IEntityEnumerable<IHighScore> IGame.HighScores
        {
            get
            {
                return new EntityEnumerable<IHighScore, GameHighScore>(_highScoreRefs);
            }
        }
        #endregion

        #region Version

        private int _version;

        [Column]
        public int Version
        {
            get
            {
                return _version;
            }
            set
            {
                if (_version != value)
                {
                    NotifyPropertyChanging("Version");
                    _version = value;
                    NotifyPropertyChanged("Version");
                }
            }
        }
        #endregion

        #region ListOfChanges

        private string _listOfChanges;

        [Column]
        public string ListOfChanges
        {
            get
            {
                return _listOfChanges;
            }
            set
            {
                if (_listOfChanges != value)
                {
                    NotifyPropertyChanging("ListOfChanges");
                    _listOfChanges = value;
                    NotifyPropertyChanged("ListOfChanges");
                }
            }
        }
        #endregion
    }
}
