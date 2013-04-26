using Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace WebService.BOMock
{
    public class ABCDPossibleAnswerMock : BOBase, IABCDPossibleAnswer
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

        #region TaskId

        private int? _taskId;

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

        #region AssignedTask - IABCDPossibleAnswer

        public ITask AssignedTask
        {
            get
            {
                return null;
            }
        }
        #endregion

        #region Answer

        private string _answer;

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
