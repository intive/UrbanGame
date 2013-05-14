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
    public class GameAlert : EntityBase, IAlert
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

        [Association(Name = "FK_Game_Alerts", Storage = "_gameRef", ThisKey = "GameId", OtherKey = "Id", IsForeignKey = true)]
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

                    //remove task from previous game
                    if (previousValue != null)
                        previousValue.Alerts.Remove(this);

                    //add task to the new game
                    if ((value != null))
                    {
                        value.Alerts.Add(this);
                        this.GameId = value.Id;
                    }
                    else
                        this.GameId = default(Nullable<int>);
                }
            }
        }
        #endregion

        #region IAlert.Game

        IGame IAlert.Game
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

        #region Topic

        private string _topic;

        [Column]
        public string Topic
        {
            get
            {
                return _topic;
            }
            set
            {
                if (_topic != value)
                {
                    NotifyPropertyChanging("Topic");
                    _topic = value;
                    NotifyPropertyChanged("Topic");
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
    }
}