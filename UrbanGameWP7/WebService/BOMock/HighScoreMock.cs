using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Common;

namespace WebService.BOMock
{
    public class HighScoreMock : BOBase, IHighScore
    {
        public HighScoreMock()
        {
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

        #region IHighScore.Game

        private IGame _game;

        public IGame Game
        {
            get
            {
                return _game;
            }
            set
            {
                if (_game != value)
                {
                    NotifyPropertyChanging("Game");
                    _game = value;
                    NotifyPropertyChanged("Game");
                }
            }
        }
        #endregion

        #region UserLogin

        private string _userLogin;

        public string UserLogin
        {
            get
            {
                return _userLogin;
            }
            set
            {
                if (_userLogin != value)
                {
                    NotifyPropertyChanging("UserLogin");
                    _userLogin = value;
                    NotifyPropertyChanged("UserLogin");
                }
            }
        }

        #endregion

        #region Points

        private int _points;

        public int Points
        {
            get
            {
                return _points;
            }
            set
            {
                if (_points != value)
                {
                    NotifyPropertyChanging("Points");
                    _points = value;
                    NotifyPropertyChanged("Points");
                }
            }
        }

        #endregion
    }
}
