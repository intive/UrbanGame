using Common;
using System;
using System.Collections.Generic;
using System.Device.Location;
using System.Linq;
using System.Text;
using UrbanGame.Storage;

namespace WebService
{
    public class GameWebServiceMock:IGameWebService
    {
        #region GameWebServiceMock
        /// <summary>
        /// Simple constructor
        /// </summary>
        public GameWebServiceMock()
        {
            ListOfGames = new List<IGame>();
            ListOfTasks = new List<ITask>();
        }
        #endregion

        #region Containers
        #region ListOfGames
        /// <summary>
        /// Game's containter
        /// </summary>
        public List<IGame> ListOfGames
        {
            get;
            private set;
        }
        #endregion

        #region ListOfTasks
        /// <summary>
        /// Task's containter
        /// </summary>
        public List<ITask> ListOfTasks
        {
            get;
            private set;
        }
        #endregion
        #endregion

        #region ChangeGame
        /// <summary>
        /// Shows how GameChanged works
        /// </summary>
        /// <param name="gid"></param>
        public void ChangeGame(int gid)
        {
            foreach (IGame g in ListOfGames)
            {
                if (g.Id == gid)
                {
                    g.NumberOfCompletedTasks = 1;
                    OnGameChanged(new GameEventArgs());
                }
            }
        }
        #endregion

        #region OnGameChanged
        /// <summary>
        /// Method fire GameChanged
        /// </summary>
        /// <param name="e">GameEventArgs</param>
        private void OnGameChanged(GameEventArgs e)
        {
            if (GameChanged != null)
            {
                GameChanged(this, e);
            }
        }
        #endregion

        #region GetGames
        public IGame[] GetGames()
        {
            return ListOfGames.ToArray<IGame>();
        }
        #endregion

        #region SingUpToTheGame
        public bool SingUpToTheGame(int gid)
        {
            throw new NotImplementedException();
        }
        #endregion

        #region GetGameInfo
        public IGame GetGameInfo(int gid)
        {
            throw new NotImplementedException();
        }
        #endregion

        #region GetTasks
        public ITask[] GetTasks(int gid)
        {
            return ListOfTasks.ToArray<ITask>();
        }
        #endregion

        #region GetTaskDetails
        public ITaskDetails GetTaskDetails(int gid, int tid)
        {
            throw new NotImplementedException();
        }
        #endregion

        #region GetGameProgress
        public int GetGameProgress(int gid)
        {
            return 0;
        }
        #endregion

        #region GetTaskProgress
        public int GetTaskProgress(int gid, int tid)
        {
            return 0;
        }
        #endregion

        #region SubmitTaskSolution
        public bool SubmitTaskSolution(int gid, int tid, ISolution solution)
        {
            throw new NotImplementedException();
        }
        #endregion

        #region GameChanged
        public event GameChangedEventHandler GameChanged;
        #endregion

        #region Authorize
        public AuthorizeState Authorize(string username, string password)
        {
            IsAuthorized = true;
            return AuthorizeState.Success;
        }
        #endregion

        #region IsAuthorized

        private bool _isAuthorized;

        public bool IsAuthorized
        {
            get
            {
                return _isAuthorized;
            }
            set
            {
                _isAuthorized = value;
            }
        }
        #endregion

        #region UsersActiveGames
        public IGame[] UsersActiveGames()
        {
            return new IGame[] {
                new Game(){Name = "M For The Mission", GameState = GameState.Joined, Id = 1, Logo = "/ApplicationIcon.png", GameEnd = DateTime.Now.AddDays(2).AddHours(13), Rank = 4},
            new Game(){Name = "Thanks For All That Fish", GameState = GameState.Joined, Id = 2, Logo = "/ApplicationIcon.png", GameEnd = DateTime.Now.AddDays(3).AddHours(5), Rank = null},
            new Game(){Name = "Pontifex", GameState = GameState.Joined, Id = 3, Logo = "/ApplicationIcon.png", GameEnd = DateTime.Now.AddDays(8), Rank = 8}};
        }
        #endregion

        #region UserNearbyGames
        public IGame[] UserNearbyGames(GeoCoordinate coordinate)
        {
            return new IGame[] {
                new Game(){Name = "Hydromystery", Operator = "Cafeteria", NumberOfPlayers = 23, NumberOfSlots = 48, Id = 4, Logo = "/ApplicationIcon.png", GameStart = new DateTime(2013, 4, 8, 12, 12,0) ,GameEnd = DateTime.Now.AddDays(2).AddHours(10), GameState = GameState.None, Difficulty = GameDifficulty.Easy, Description = "Le 10 septembre 2008, quelques jours après avoir fêté son vingtième anniversaire, Lewandowski débute sa carrière internationale avec la Pologne face à Saint-Marin, lors des éliminatoires de la coupe du monde 2010."},
            new Game(){Name = "North & South", Operator = "Infogrames", NumberOfPlayers = 23, Id = 5, Logo = "/ApplicationIcon.png", GameEnd = DateTime.Now.AddDays(3).AddHours(12), GameState = GameState.None},
            new Game(){Name = "Ultimate Quest",  Operator = "JCVD", NumberOfPlayers = 23,Id = 6, Logo = "/ApplicationIcon.png",  GameEnd = DateTime.Now.AddDays(10), GameState = GameState.None},
            new Game(){Name = "Galaxy Quest",  Operator = "NSEA", NumberOfPlayers = 23,Id = 7, Logo = "/ApplicationIcon.png", GameStart = new DateTime(2013,4,10,8,12,0), GameState = GameState.None},
            new Game(){Name = "The Quest for NEETs",  Operator = "Ron Jeremy", NumberOfPlayers = 23,Id = 8, Logo = "/ApplicationIcon.png", GameStart = new DateTime(2013,5,9,21,5,8), GameState = GameState.None}};
        }
        #endregion

        #region UsersInactiveGames
        public IGame[] UsersInactiveGames()
        {
            return new IGame[] {
                new Game(){Name = "Wilqu!", Id = 9, Logo = "/ApplicationIcon.png", GameState = GameState.Ended, Rank = 4},
            new Game(){Name = "Torghal", Id = 10, Logo = "/ApplicationIcon.png", GameState = GameState.Withdraw, Rank = null}};
        }
        #endregion
    }
}
