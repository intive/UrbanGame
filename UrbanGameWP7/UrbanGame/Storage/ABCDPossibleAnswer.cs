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

        #region AssignedTask - IABCDPossibleAnswer

        public ITask AssignedTask
        {
            get
            {
                return Task;
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
