using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Common;
using System.Data.Linq;
using Newtonsoft.Json;

namespace WebService.DTOs
{
    public class GameTask : DTOBase, ITask
    {
        public GameTask()
        {            
            EntitySet<TaskSolution> es2 = new EntitySet<TaskSolution>(OnSolutionAdded, OnSolutionRemoved);
            _solutions = new EntityEnumerable<IBaseSolution, TaskSolution>(es2);
        }

        #region Id

        private int _id;

        [JsonProperty(PropertyName = "tid")]
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

        #region Type

        private TaskType _type;

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
                    NotifyPropertyChanging("State");
                    _state = value;
                    NotifyPropertyChanged("State");
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

        #region AdditionalText

        private string _additionalText;

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

        #region ITask.Game

        private IGame _game;

        public IGame Game
        {
            get
            {
                return _game;
            }
            set
            {
                if (_game != value)
                {
                    NotifyPropertyChanging("Game");
                    _game = value;
                    NotifyPropertyChanged("Game");
                }
            }
        }
        #endregion

        #region ABCDChoices - for json parser
        [JsonProperty("choices")]
        public List<ABCDPossibleAnswer> ABCDChoices { get; set; }
        #endregion

        #region ITask.ABCDPossibleAnswers

        private IEntityEnumerable<IABCDPossibleAnswer> _abcdPossibleAnswers;

        public IEntityEnumerable<IABCDPossibleAnswer> ABCDPossibleAnswers
        {
            get
            {
                var es = new EntitySet<ABCDPossibleAnswer>(OnAnswerAdded, OnAnswerRemoved);
                if (ABCDChoices != null)
                    foreach (var answ in ABCDChoices)
                        es.Add(answ);

                return new EntityEnumerable<IABCDPossibleAnswer, ABCDPossibleAnswer>(es);
            }
        }

        private void OnAnswerAdded(ABCDPossibleAnswer answer)
        {
            answer.Task = this;
        }

        private void OnAnswerRemoved(ABCDPossibleAnswer answer)
        {
            answer.Task = null;
        }
        #endregion

        #region ITask.Solutions

        private IEntityEnumerable<IBaseSolution> _solutions;

        public IEntityEnumerable<IBaseSolution> Solutions
        {
            get
            {
                return _solutions;
            }
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

        #region Picture

        private string _picture;

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

        #region IsNewTask

        private bool _isNewTask;

        public bool IsNewTask
        {
            get
            {
                return _isNewTask;
            }
            set
            {
                if (_isNewTask != value)
                {
                    NotifyPropertyChanging("IsNewTask");
                    _isNewTask = value;
                    NotifyPropertyChanged("IsNewTask");
                }
            }
        }
        #endregion        
    }
}
