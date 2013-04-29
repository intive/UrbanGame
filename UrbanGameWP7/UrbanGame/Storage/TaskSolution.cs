using Common;
using System;
using System.Collections.Generic;
using System.Data.Linq.Mapping;
using System.Linq;
using System.Text;

namespace UrbanGame.Storage
{
    [Table]
    public class TaskSolution : EntityBase, 
        IGPSSolution, IPhotoSolution, IOpenQuestionSolution, IABCDSolution, IQRCodeSolution
    {
        #region Id

        private int _id;

        [Column]
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

        #region ABCDAnswer

        private byte _abcdAnswer;

        [Column]
        public byte ABCDAnswer
        {
            get
            {
                return _abcdAnswer;
            }
            set
            {
                if (_abcdAnswer != value)
                {
                    NotifyPropertyChanging("ABCDAnswer");
                    _abcdAnswer = value;
                    NotifyPropertyChanged("ABCDAnswer");
                }
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
