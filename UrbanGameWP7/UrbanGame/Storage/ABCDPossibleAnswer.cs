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
    public class ABCDPossibleAnswer : EntityBase, IABCDPossibleAnswer
    {
        public ABCDPossibleAnswer()
        {
            _abcdUserAnswerRefs = new EntitySet<ABCDUserAnswer>(OnABCDUserAnswerAdded, OnABCDUserAnswerRemoved);
        }

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

        [Association(Name = "FK_Answer_Task", Storage = "_taskRef", ThisKey = "TaskId", OtherKey = "Id", IsForeignKey = true)]
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

                    //remove answer from previous task
                    if (previousValue != null)
                        previousValue.ABCDPossibleAnswers.Remove(this);

                    //add answer to the new task
                    if ((value != null))
                    {
                        value.ABCDPossibleAnswers.Add(this);
                        this.TaskId = value.Id;
                    }
                    else
                        this.TaskId = default(Nullable<int>);
                }
            }
        }
        #endregion

        #region IABCDPossibleAnswer.Task

        ITask IABCDPossibleAnswer.Task
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

        #region ABCDUserAnswers

        private EntitySet<ABCDUserAnswer> _abcdUserAnswerRefs;

        [Association(Name = "FK_ABCDPossibleAnswer_ABCDUserAnswers", Storage = "_abcdUserAnswerRefs", ThisKey = "Id", OtherKey = "ABCDPossibleAnswerId", DeleteRule = "CASCADE")]
        public EntitySet<ABCDUserAnswer> ABCDUserAnswers
        {
            get { return _abcdUserAnswerRefs; }
        }

        private void OnABCDUserAnswerAdded(ABCDUserAnswer abcdUserAnswer)
        {
            abcdUserAnswer.ABCDPossibleAnswer = this;
        }

        private void OnABCDUserAnswerRemoved(ABCDUserAnswer abcdUserAnswer)
        {
            abcdUserAnswer.ABCDPossibleAnswer = null;
        }

        #endregion

        #region IABCDPossibleAnswer.ABCDUserAnswers
        IEntityEnumerable<IABCDUserAnswer> IABCDPossibleAnswer.ABCDUserAnswers
        {
            get
            {
                return new EntityEnumerable<IABCDUserAnswer, ABCDUserAnswer>(_abcdUserAnswerRefs);
            }
        }
        #endregion

        #region Answer

        private string _answer;

        [Column]
        public string Answer
        {
            get
            {
                return _answer;
            }
            set
            {
                if (_answer != value)
                {
                    NotifyPropertyChanging("Answer");
                    _answer = value;
                    NotifyPropertyChanged("Answer");
                }
            }
        }
        #endregion
    }
}
