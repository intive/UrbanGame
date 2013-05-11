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
    public class GameHighScore : EntityBase, IHighScore
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

        #region GameId

        private int? _gameId;

        [Column]
        public int? GameId
        {
            get
            {
                return _gameId;
            }
            set
            {
                if (_gameId != value)
                {
                    NotifyPropertyChanging("GameId");
                    _gameId = value;
                    NotifyPropertyChanged("GameId");
                }
            }
        }
        #endregion

        #region Game

        private EntityRef<Game> _gameRef = new EntityRef<Game>();

        [Association(Name = "FK_Game_Tasks", Storage = "_gameRef", ThisKey = "GameId", OtherKey = "Id", IsForeignKey = true)]
        public Game Game
        {
            get
            {
                return _gameRef.Entity;
            }
            set
            {
                Game previousValue = _gameRef.Entity;
                if (((previousValue != value) || (_gameRef.HasLoadedOrAssignedValue == false)))
                {
                    _gameRef.Entity = value;

                    //remove high score from previous game
                    if (previousValue != null)
                        previousValue.HighScores.Remove(this);

                    //add high score to the new game
                    if ((value != null))
                    {
                        value.HighScores.Add(this);
                        this.GameId = value.Id;
                    }
                    else
                        this.GameId = default(Nullable<int>);
                }
            }
        }
        #endregion

        #region IHighScore.Game

        IGame IHighScore.Game
        {
            get
            {
                return Game;
            }

            set
            {
                Game = (Game)value;
            }
        }
        #endregion

        #region UserLogin

        private string _userLogin;

        [Column]
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

        [Column]
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