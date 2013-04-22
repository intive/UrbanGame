using Common;
using System;
using System.Collections.Generic;
using System.Data.Linq.Mapping;
using System.Linq;
using System.Text;

namespace UrbanGame.Storage
{
    [Table]
    public class GameTask : EntityBase, 
        IGPSTask, IABCDTask, IOpenQuestionTask, IQRCodeTask, IPhotoTask
    {
        #region Id

        private int _id;

        [Column(IsPrimaryKey=true)]
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

        [Column]
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

        [Column]
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

        #region Description

        private string _description;

        [Column]
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

        #region Picture

        private string _picture;

        [Column]
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

        [Column]
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

        [Column]
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

        #region IsCancelled

        private bool _isCancelled;

        [Column(DbType = "bit DEFAULT 0 NOT NULL")]
        public bool IsCancelled
        {
            get
            {
                return _isCancelled;
            }
            set
            {
                if (_isCancelled != value)
                {
                    NotifyPropertyChanging("IsCancelled");
                    _isCancelled = value;
                    NotifyPropertyChanged("IsCancelled");
                }
            }
        }
        #endregion

        #region UserPoints

        private int? _userPoints;

        [Column]
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

        [Column]
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

        [Column]
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

        [Column]
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



        #region GPS Task

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

        #endregion

        #region ABCD Task

        #region ABCDPossibleAnswers

        private string _abcdPossibleAnswers;

        [Column]
        public string ABCDPossibleAnswers
        {
            get
            {
                return _abcdPossibleAnswers;
            }
            set
            {
                if (_abcdPossibleAnswers != value)
                {
                    NotifyPropertyChanging("ABCDPossibleAnswers");
                    _abcdPossibleAnswers = value;
                    NotifyPropertyChanged("ABCDPossibleAnswers");
                }
            }
        }
        #endregion

        #region ABCDCorrectAnswer

        private byte? _abcdCorrectAnswer;

        [Column]
        public byte? ABCDCorrectAnswer
        {
            get
            {
                return _abcdCorrectAnswer;
            }
            set
            {
                if (_abcdCorrectAnswer != value)
                {
                    NotifyPropertyChanging("ABCDCorrectAnswer");
                    _abcdCorrectAnswer = value;
                    NotifyPropertyChanged("ABCDCorrectAnswer");
                }
            }
        }
        #endregion

        #endregion

        #region QRCode Task

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

        #endregion

        #region OpenQuestion Task

        #region OpenQuestionCorrectAnswer

        private string _openQuestionCorrectAnswer;

        [Column]
        public string OpenQuestionCorrectAnswer
        {
            get
            {
                return _openQuestionCorrectAnswer;
            }
            set
            {
                if (_openQuestionCorrectAnswer != value)
                {
                    NotifyPropertyChanging("OpenQuestionCorrectAnswer");
                    _openQuestionCorrectAnswer = value;
                    NotifyPropertyChanged("OpenQuestionCorrectAnswer");
                }
            }
        }
        #endregion

        #endregion
    }
}
