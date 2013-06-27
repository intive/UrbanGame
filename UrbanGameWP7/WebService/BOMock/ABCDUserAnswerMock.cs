using Common;
using System;
using System.Collections.Generic;
using System.Data.Linq;
using System.Linq;
using System.Text;

namespace WebService.BOMock
{
    public class ABCDUserAnswerMock : BOBase, IABCDUserAnswer
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

        #region IABCDUserAnswer.Solution

        private IABCDSolution _solution;

        public IABCDSolution Solution
        {
            get
            {
                return _solution;
            }
            set
            {
                if (_solution != value)
                {
                    NotifyPropertyChanging("Solution");
                    _solution = value;
                    NotifyPropertyChanged("Solution");
                }
            }
        }
        #endregion

        #region ABCDPossibleAnswer

        private IABCDPossibleAnswer _abcdPossibleAnswer;

        public IABCDPossibleAnswer ABCDPossibleAnswer
        {
            get
            {
                return _abcdPossibleAnswer;
            }
            set
            {
                if (_abcdPossibleAnswer != value)
                {
                    NotifyPropertyChanging("ABCDPossibleAnswer");
                    _abcdPossibleAnswer = value;
                    NotifyPropertyChanged("ABCDPossibleAnswer");
                }
            }
        }
        #endregion

        #region Answer

        private bool _answer;

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
