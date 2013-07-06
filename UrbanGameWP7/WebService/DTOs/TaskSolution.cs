using Common;
using System;
using System.Collections.Generic;
using System.Data.Linq;
using System.Linq;
using System.Text;

namespace WebService.DTOs
{
    public class TaskSolution : DTOBase, 
        IGPSSolution, IPhotoSolution, IOpenQuestionSolution, IABCDSolution, IQRCodeSolution
    {
        public TaskSolution() : base()
        {
            EntitySet<ABCDUserAnswer> es = new EntitySet<ABCDUserAnswer>(OnABCDUserAnswerAdded, OnABCDUserAnswerRemoved);
            _abcdUserAnswers = new EntityEnumerable<IABCDUserAnswer, ABCDUserAnswer>(es);
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

        #region TaskType

        private TaskType _taskType;

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

        #region IBaseSolution.ABCDUserAnswers

        private IEntityEnumerable<IABCDUserAnswer> _abcdUserAnswers;

        public IEntityEnumerable<IABCDUserAnswer> ABCDUserAnswers
        {
            get
            {
                return _abcdUserAnswers;
            }
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

        #region IBaseSolution.Task

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

        #region QRCode

        private string _qrCode;

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
