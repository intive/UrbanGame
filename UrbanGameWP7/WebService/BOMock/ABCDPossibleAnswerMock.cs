using Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data.Linq;

namespace WebService.BOMock
{
    public class ABCDPossibleAnswerMock : BOBase, IABCDPossibleAnswer
    {
        public ABCDPossibleAnswerMock()
        {
            _abcdUserAnswers = new EntityEnumerable<IABCDUserAnswer, ABCDUserAnswerMock>(new EntitySet<ABCDUserAnswerMock>(OnABCDUserAnswerAdded, OnABCDUserAnswerRemoved));
        }

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

        #region CharId

        private char _charId;

        public char CharId
        {
            get
            {
                return _charId;
            }
            set
            {
                if (_charId != value)
                {
                    NotifyPropertyChanging("CharId");
                    _charId = value;
                    NotifyPropertyChanged("CharId");
                }
            }
        }
        #endregion

        #region IABCDPossibleAnswer.Task

        private ITask _task;

        public ITask Task
        {
            get
            {
                return _task;
            }
            set
            {
                if (_task != value)
                {
                    NotifyPropertyChanging("Task");
                    _task = value;
                    NotifyPropertyChanged("Task");
                }
            }
        }
        #endregion

        #region IABCDPossibleAnswer.ABCDUserAnswers

        private IEntityEnumerable<IABCDUserAnswer> _abcdUserAnswers;

        public IEntityEnumerable<IABCDUserAnswer> ABCDUserAnswers
        {
            get
            {
                return _abcdUserAnswers;
            }
        }

        private void OnABCDUserAnswerAdded(ABCDUserAnswerMock abcdUserAnswer)
        {
            abcdUserAnswer.ABCDPossibleAnswer = this;
        }

        private void OnABCDUserAnswerRemoved(ABCDUserAnswerMock abcdUserAnswer)
        {
            abcdUserAnswer.ABCDPossibleAnswer = null;
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
