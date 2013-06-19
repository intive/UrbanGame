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
    public class ABCDUserAnswer : EntityBase, IABCDUserAnswer
    {
        #region Id

        private int _id;

        [Column(IsPrimaryKey=true, AutoSync=AutoSync.OnInsert, IsDbGenerated=true)]
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

        #region SolutionId

        private int? _solutionId;

        [Column]
        public int? SolutionId
        {
            get
            {
                return _solutionId;
            }
            set
            {
                if (_solutionId != value)
                {
                    NotifyPropertyChanging("SolutionId");
                    _solutionId = value;
                    NotifyPropertyChanged("SolutionId");
                }
            }
        }
        #endregion

        #region Solution

        private EntityRef<TaskSolution> _taskSolutionRef = new EntityRef<TaskSolution>();

        [Association(Name = "FK_ABCDUserAnswer_TaskSolution", Storage = "_taskSolutionRef", ThisKey = "SolutionId", OtherKey = "Id", IsForeignKey = true)]
        public TaskSolution Solution
        {
            get
            {
                return _taskSolutionRef.Entity;
            }
            set
            {
                TaskSolution previousValue = _taskSolutionRef.Entity;
                if (((previousValue != value) || (_taskSolutionRef.HasLoadedOrAssignedValue == false)))
                {
                    _taskSolutionRef.Entity = value;

                    //remove answer from previous solution
                    if (previousValue != null)
                        previousValue.ABCDUserAnswers.Remove(this);

                    //add answer to the new solution
                    if ((value != null))
                    {
                        value.ABCDUserAnswers.Add(this);
                        this.SolutionId = value.Id;
                    }
                    else
                        this.SolutionId = default(Nullable<int>);
                }
            }
        }
        #endregion

        #region IABCDUserAnswer.Solution

        IABCDSolution IABCDUserAnswer.Solution
        {
            get
            {
                return Solution;
            }

            set
            {
                Solution = (TaskSolution)value;
            }
        }
        #endregion

        #region ABCDPossibleAnswerId

        private int? _abcdPossibleAnswerId;

        [Column]
        public int? ABCDPossibleAnswerId
        {
            get
            {
                return _abcdPossibleAnswerId;
            }
            set
            {
                if (_abcdPossibleAnswerId != value)
                {
                    NotifyPropertyChanging("ABCDPossibleAnswerId");
                    _abcdPossibleAnswerId = value;
                    NotifyPropertyChanged("ABCDPossibleAnswerId");
                }
            }
        }
        #endregion

        #region ABCDPossibleAnswer

        private EntityRef<ABCDPossibleAnswer> _abcdPossibleAnswerRef = new EntityRef<ABCDPossibleAnswer>();

        [Association(Name = "FK_ABCDPossibleAnswer_ABCDUserAnswers", Storage = "_abcdPossibleAnswerRef", ThisKey = "ABCDPossibleAnswerId", OtherKey = "Id", IsForeignKey = true)]
        public ABCDPossibleAnswer ABCDPossibleAnswer
        {
            get
            {
                return _abcdPossibleAnswerRef.Entity;
            }
            set
            {
                ABCDPossibleAnswer previousValue = _abcdPossibleAnswerRef.Entity;
                if (((previousValue != value) || (_abcdPossibleAnswerRef.HasLoadedOrAssignedValue == false)))
                {
                    _abcdPossibleAnswerRef.Entity = value;

                    //remove task from previous possibleAnswer
                    if (previousValue != null)
                        previousValue.ABCDUserAnswers.Remove(this);

                    //add task to the new possibleAnswer
                    if ((value != null))
                    {
                        value.ABCDUserAnswers.Add(this);
                        this.ABCDPossibleAnswerId = value.Id;
                    }
                    else
                        this.ABCDPossibleAnswerId = default(Nullable<int>);
                }
            }
        }
        #endregion

        #region IABCDUserAnswer.ABCDPossibleAnswer

        IABCDPossibleAnswer IABCDUserAnswer.ABCDPossibleAnswer
        {
            get
            {
                return ABCDPossibleAnswer;
            }

            set
            {
                ABCDPossibleAnswer = (ABCDPossibleAnswer)value;
            }
        }
        #endregion

        #region Answer

        private bool _answer;

        [Column]
        public bool Answer
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
