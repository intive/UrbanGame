using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Common;

namespace WebService.BOMock
{
    public class TaskMock : BOBase,  ITask
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

        #region Name

        private string _name;

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

        #region AdditionalText

        private string _additionalText;

        public string AdditionalText
        {
            get
            {
                return _additionalText;
            }
            set
            {
                if (_additionalText != value)
                {
                    NotifyPropertyChanging("AdditionalText");
                    _additionalText = value;
                    NotifyPropertyChanged("AdditionalText");
                }
            }
        }
        #endregion

        #region AssignedGame - ITask

        public IGame AssignedGame
        {
            get
            {
                return null;
            }
        }
        #endregion

        #region ABCDPossibleAnswersList - ITask

        private IList<IABCDPossibleAnswer> _abcdPossibleAnswersList = new List<IABCDPossibleAnswer>();

        public IList<IABCDPossibleAnswer> ABCDPossibleAnswersList 
        {
            get { return _abcdPossibleAnswersList; }
        }

        #endregion

        #region Picture

        private string _picture;

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
    }
}
