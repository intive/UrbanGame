using Common;
using System;
using System.Collections.Generic;
using System.Data.Linq;
using System.Data.Linq.Mapping;
using System.Linq;
using System.Text;

namespace UrbanGame.Storage
{
    [Table]
    public class GameTask : EntityBase, ITask
    {
        public GameTask() : base()
        {
            _abcdAnswersRefs = new EntitySet<ABCDPossibleAnswer>(OnABCDAnswerAdded, OnABCDAnswerRemoved);
            _solutionsRefs = new EntitySet<TaskSolution>(OnSolutionAdded, OnSolutionRemoved);
        }

        #region Id

        private int _id;

        [Column(IsPrimaryKey=true)]
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

        #region GameId

        private int? _gameId;

        [Column]
        public int? GameId
        {
            get
            {
                return _gameId;
            }
            set
            {
                if (_gameId != value)
                {
                    NotifyPropertyChanging("GameId");
                    _gameId = value;
                    NotifyPropertyChanged("GameId");
                }
            }
        }
        #endregion

        #region Game

        private EntityRef<Game> _gameRef = new EntityRef<Game>();

        [Association(Name = "FK_Game_Tasks", Storage = "_gameRef", ThisKey = "GameId", OtherKey = "Id", IsForeignKey = true)]
        public Game Game
        {
            get
            {
                return _gameRef.Entity;
            }
            set
            {
                Game previousValue = _gameRef.Entity;
                if (((previousValue != value) || (_gameRef.HasLoadedOrAssignedValue == false)))
                {
                    _gameRef.Entity = value;

                    //remove task from previous game
                    if (previousValue != null)
                        previousValue.Tasks.Remove(this);

                    //add task to the new game
                    if ((value != null))
                    {
                        value.Tasks.Add(this);
                        this.GameId = value.Id;
                    }
                    else
                        this.GameId = default(Nullable<int>);
                }
            }
        }
        #endregion

        #region ITask.Game

        IGame ITask.Game
        {
            get
            {
                return Game;
            }

            set
            {
                Game = (Game)value;
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

        #region Type

        private TaskType _type;

        [Column]
        public TaskType Type
        {
            get
            {
                return _type;
            }
            set
            {
                if (_type != value)
                {
                    NotifyPropertyChanging("Type");
                    _type = value;
                    NotifyPropertyChanged("Type");
                }
            }
        }
        #endregion

        #region State

        private TaskState _state;

        [Column]
        public TaskState State
        {
            get
            {
                return _state;
            }
            set
            {
                if (_state != value)
                {
                    NotifyPropertyChanging("Steate");
                    _state = value;
                    NotifyPropertyChanged("State");
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

        #region AdditionalText

        private string _additionalText;

        [Column]
        public string AdditionalText
        {
            get
            {
                return _additionalText;
            }
            set
            {
                if (_additionalText != value)
                {
                    NotifyPropertyChanging("AdditionalText");
                    _additionalText = value;
                    NotifyPropertyChanged("AdditionalText");
                }
            }
        }
        #endregion

        #region ABCDPossibleAnswers

        private EntitySet<ABCDPossibleAnswer> _abcdAnswersRefs;

        [Association(Name = "FK_GameTask_ABCDAnswers", Storage = "_abcdAnswersRefs", ThisKey = "Id", OtherKey = "TaskId", DeleteRule = "CASCADE")]
        public EntitySet<ABCDPossibleAnswer> ABCDPossibleAnswers
        {
            get { return _abcdAnswersRefs; }
        }

        private void OnABCDAnswerAdded(ABCDPossibleAnswer answer)
        {
            answer.Task = this;
        }

        private void OnABCDAnswerRemoved(ABCDPossibleAnswer answer)
        {
            answer.Task = null;
        }

        #endregion

        #region ITask.ABCDPossibleAnswers

        IEntityEnumerable<IABCDPossibleAnswer> ITask.ABCDPossibleAnswers
        {
            get 
            { 
                return new EntityEnumerable<IABCDPossibleAnswer, ABCDPossibleAnswer>(_abcdAnswersRefs); 
            }
        }

        #endregion

        #region Solutions

        private EntitySet<TaskSolution> _solutionsRefs;

        [Association(Name = "FK_GameTask_Solutions", Storage = "_solutionsRefs", ThisKey = "Id", OtherKey = "TaskId", DeleteRule = "CASCADE")]
        public EntitySet<TaskSolution> Solutions
        {
            get { return _solutionsRefs; }
        }

        private void OnSolutionAdded(TaskSolution solution)
        {
            solution.Task = this;
        }

        private void OnSolutionRemoved(TaskSolution solution)
        {
            solution.Task = null;
        }

        #endregion

        #region ITask.Solutions

        IEntityEnumerable<IBaseSolution> ITask.Solutions
        {
            get
            {
                return new EntityEnumerable<IBaseSolution, TaskSolution>(_solutionsRefs);
            }
        }

        #endregion

        #region Picture

        private string _picture;

        [Column]
        public string Picture
        {
            get
            {
                return _picture;
            }
            set
            {
                if (_picture != value)
                {
                    NotifyPropertyChanging("Picture");
                    _picture = value;
                    NotifyPropertyChanged("Picture");
                }
            }
        }
        #endregion

        #region SolutionStatus

        private SolutionStatus _solutionStatus;

        [Column]
        public SolutionStatus SolutionStatus
        {
            get
            {
                return _solutionStatus;
            }
            set
            {
                if (_solutionStatus != value)
                {
                    NotifyPropertyChanging("SolutionStatus");
                    _solutionStatus = value;
                    NotifyPropertyChanged("SolutionStatus");
                }
            }
        }
        #endregion

        #region IsRepeatable

        private bool _isRepeatable;

        [Column]
        public bool IsRepeatable
        {
            get
            {
                return _isRepeatable;
            }
            set
            {
                if (_isRepeatable != value)
                {
                    NotifyPropertyChanging("IsRepeatable");
                    _isRepeatable = value;
                    NotifyPropertyChanged("IsRepeatable");
                }
            }
        }
        #endregion

        #region UserPoints

        private int? _userPoints;

        [Column]
        public int? UserPoints
        {
            get
            {
                return _userPoints;
            }
            set
            {
                if (_userPoints != value)
                {
                    NotifyPropertyChanging("UserPoints");
                    _userPoints = value;
                    NotifyPropertyChanged("UserPoints");
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

        #region EndDate

        private DateTime? _endDate;

        [Column]
        public DateTime? EndDate
        {
            get
            {
                return _endDate;
            }
            set
            {
                if (_endDate != value)
                {
                    NotifyPropertyChanging("EndDate");
                    _endDate = value;
                    NotifyPropertyChanged("EndDate");
                }
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
    }
}
