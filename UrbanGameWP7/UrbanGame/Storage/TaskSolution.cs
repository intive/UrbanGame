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
    public class TaskSolution : EntityBase, 
        IBaseSolution, IGPSSolution, IPhotoSolution, IOpenQuestionSolution, IABCDSolution, IQRCodeSolution
    {
        public TaskSolution() : base()
        {
            _abcdUserAnswersRefs = new EntitySet<ABCDUserAnswer>(OnABCDUserAnswerAdded, OnABCDUserAnswerRemoved);
        }

        #region Id

        private int _id;

        [Column(IsPrimaryKey = true, AutoSync = AutoSync.OnInsert, IsDbGenerated = true)]
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

        #region TaskType

        private TaskType _taskType;

        [Column]
        public TaskType TaskType
        {
            get
            {
                return _taskType;
            }
            set
            {
                if (_taskType != value)
                {
                    NotifyPropertyChanging("TaskType");
                    _taskType = value;
                    NotifyPropertyChanged("TaskType");
                }
            }
        }
        #endregion

        #region Longitude

        private double? _longitude;

        [Column]
        public double? Longitude
        {
            get
            {
                return _longitude;
            }
            set
            {
                if (_longitude != value)
                {
                    NotifyPropertyChanging("Longitude");
                    _longitude = value;
                    NotifyPropertyChanged("Longitude");
                }
            }
        }
        #endregion

        #region Latitude

        private double? _latitude;

        [Column]
        public double? Latitude
        {
            get
            {
                return _latitude;
            }
            set
            {
                if (_latitude != value)
                {
                    NotifyPropertyChanging("Latitude");
                    _latitude = value;
                    NotifyPropertyChanged("Latitude");
                }
            }
        }
        #endregion

        #region Photo

        private string _photo;

        [Column]
        public string Photo
        {
            get
            {
                return _photo;
            }
            set
            {
                if (_photo != value)
                {
                    NotifyPropertyChanging("Photo");
                    _photo = value;
                    NotifyPropertyChanged("Photo");
                }
            }
        }
        #endregion

        #region TextAnswer

        private string _textAnswer;

        [Column]
        public string TextAnswer
        {
            get
            {
                return _textAnswer;
            }
            set
            {
                if (_textAnswer != value)
                {
                    NotifyPropertyChanging("TextAnswer");
                    _textAnswer = value;
                    NotifyPropertyChanged("TextAnswer");
                }
            }
        }
        #endregion

        #region ABCDUserAnswers

        private EntitySet<ABCDUserAnswer> _abcdUserAnswersRefs;

        [Association(Name = "FK_TaskSolution_ABCDUserAnswers", Storage = "_abcdUserAnswersRefs", ThisKey = "Id", OtherKey = "SolutionId", DeleteRule = "CASCADE")]
        public EntitySet<ABCDUserAnswer> ABCDUserAnswers
        {
            get { return _abcdUserAnswersRefs; }
        }

        private void OnABCDUserAnswerAdded(ABCDUserAnswer answer)
        {
            answer.Solution = this;
        }

        private void OnABCDUserAnswerRemoved(ABCDUserAnswer answer)
        {
            answer.Solution = null;
        }
        #endregion

        #region IABCDSolution.ABCDUserAnswers

        IEntityEnumerable<IABCDUserAnswer> IABCDSolution.ABCDUserAnswers
        {
            get
            {
                return new EntityEnumerable<IABCDUserAnswer, ABCDUserAnswer>(_abcdUserAnswersRefs);
            }
        }

        #endregion

        #region TaskId

        private int? _taskId;

        [Column]
        public int? TaskId
        {
            get
            {
                return _taskId;
            }
            set
            {
                if (_taskId != value)
                {
                    NotifyPropertyChanging("TaskId");
                    _taskId = value;
                    NotifyPropertyChanged("TaskId");
                }
            }
        }
        #endregion

        #region Task

        private EntityRef<GameTask> _taskRef = new EntityRef<GameTask>();

        [Association(Name = "FK_Solution_Task", Storage = "_taskRef", ThisKey = "TaskId", OtherKey = "Id", IsForeignKey = true)]
        public GameTask Task
        {
            get
            {
                return _taskRef.Entity;
            }
            set
            {
                GameTask previousValue = _taskRef.Entity;
                if (((previousValue != value) || (_taskRef.HasLoadedOrAssignedValue == false)))
                {
                    _taskRef.Entity = value;

                    //remove solution from previous task
                    if (previousValue != null)
                        previousValue.Solutions.Remove(this);

                    //add solution to the new task
                    if ((value != null))
                    {
                        value.Solutions.Add(this);
                        this.TaskId = value.Id;
                    }
                    else
                        this.TaskId = default(Nullable<int>);
                }
            }
        }
        #endregion

        #region IBaseSolution.Task

        ITask IBaseSolution.Task
        {
            get
            {
                return Task;
            }

            set
            {
                Task = (GameTask)value;
            }
        }
        #endregion

        #region QRCode

        private string _qrCode;

        [Column]
        public string QRCode
        {
            get
            {
                return _qrCode;
            }
            set
            {
                if (_qrCode != value)
                {
                    NotifyPropertyChanging("QRCode");
                    _qrCode = value;
                    NotifyPropertyChanged("QRCode");
                }
            }
        }
        #endregion
    }
}
