using Common;
using System;
using System.Collections.Generic;
using System.Device.Location;
using System.Linq;
using System.Text;
using WebService.BOMock;

namespace WebService
{
    public class GameWebServiceMock : IGameWebService
    {
        #region GameWebServiceMock
        /// <summary>
        /// Simple constructor
        /// </summary>
        public GameWebServiceMock()
        {
            ListOfGames = new List<IGame>();
            ListOfTasks = new List<IBaseTask>();

            string lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam aliquam mauris vel elit tincidunt ac bibendum tortor scelerisque. Mauris nisi augue, malesuada ac lobortis sed, rhoncus et mauris. Vivamus dictum turpis congue arcu euismod in pulvinar mi volutpat. Aliquam euismod pharetra velit eu sagittis. Proin et nisi nibh, ut egestas enim.";
            ListOfTasks.Add(new TaskMock() { Id = 1, Type = TaskType.ABCD, ABCDPossibleAnswers = "odp1\nodp2\nodp3\nodp4", ABCDCorrectAnswer = 1, Description = lorem, Picture = "/ApplicationIcon.png", SolutionStatus = SolutionStatus.NotSend, IsRepeatable = false, IsCancelled = false, UserPoints = null, MaxPoints = 20, EndDate = DateTime.Now.AddDays(1), Version = 1 });
            ListOfTasks.Add(new TaskMock() { Id = 2, Type = TaskType.OpenQuestion, OpenQuestionCorrectAnswer = "balloon", Description = lorem, Picture = "/ApplicationIcon.png", SolutionStatus = SolutionStatus.Pending, IsRepeatable = true, IsCancelled = true, UserPoints = null, MaxPoints = 20, EndDate = DateTime.Now.AddDays(1), Version = 1 });
            ListOfTasks.Add(new TaskMock() { Id = 3, Type = TaskType.Photo, Description = lorem, Picture = "/ApplicationIcon.png", SolutionStatus = SolutionStatus.Rejected, IsRepeatable = true, IsCancelled = false, UserPoints = null, MaxPoints = 20, EndDate = DateTime.Now.AddDays(1), Version = 1 });
            ListOfTasks.Add(new TaskMock() { Id = 4, Type = TaskType.QRCode, QRCode = "ABCD1234", Description = lorem, Picture = "/ApplicationIcon.png", SolutionStatus = SolutionStatus.Accepted, IsRepeatable = false, IsCancelled = false, UserPoints = 10, MaxPoints = 20, EndDate = DateTime.Now.AddDays(1), Version = 1 });
            ListOfTasks.Add(new TaskMock() { Id = 5, Type = TaskType.GPS, Longitude = 51.111565, Latitude = 17.060416, Description = lorem, Picture = "/ApplicationIcon.png", SolutionStatus = SolutionStatus.Pending, IsRepeatable = false, IsCancelled = false, UserPoints = null, MaxPoints = 20, EndDate = DateTime.Now.AddDays(1), Version = 1 });
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
        public List<IBaseTask> ListOfTasks
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
                }
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
            return new GameMock()
            {
                Id = gid,
                Name = "Hydrozagadka",
                OperatorName = "CAFETERIA",
                OperatorLogo = "/ApplicationIcon.png",
                GameLogo = "/ApplicationIcon.png",
                GameStart = DateTime.Now.AddHours(3).AddMinutes(23),
                GameEnd = DateTime.Now.AddDays(2).AddHours(5),
                NumberOfPlayers = 24,
                NumberOfSlots = 50,
                GameType = GameType.ScoreAttack,
                Description = DateTime.Now.ToLongTimeString() + "\nsadsa sad ads  adsa dssa sad  asas asd as a sas as as  asas  asdas as ads as d",
                Difficulty = GameDifficulty.Medium,
                Prizes = "1st Bicycle\n2nd Bicycle\n3rd Bicycle\n4-8th Bicycle bicycle bicycle"
            };
        }
        #endregion

        #region GetGameProgress
        public int GetGameProgress(int gid)
        {
            return 0;
        }
        #endregion

        #region GetTasks
        public IBaseTask[] GetTasks(int gid)
        {
            return ListOfTasks.ToArray();
        }
        #endregion

        #region GetTaskDetails
        public IBaseTask GetTaskDetails(int gid, int tid)
        {
            return ListOfTasks[(gid + tid) % 5];
        }
        #endregion

        #region GetTaskDetails generic
        public TTaskType GetTaskDetails<TTaskType>(int gid, int tid) 
            where TTaskType : IBaseTask
        {
            return (TTaskType)GetTaskDetails(gid, tid);
        }
        #endregion

        #region GetTaskProgress
        public int GetTaskProgress(int gid, int tid)
        {
            return GetTaskDetails(gid, tid).UserPoints ?? 0;
        }
        #endregion        

        #region SubmitTaskSolution
        public bool SubmitTaskSolution(int gid, int tid, IBaseSolution solution)
        {
            throw new NotImplementedException();
        }
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
                new GameMock(){Name = "M For The Mission", GameType = GameType.Race, GameState = GameState.Joined, Id = 6, OperatorLogo = "/ApplicationIcon.png", GameEnd = DateTime.Now.AddDays(2).AddHours(13), Rank = 4},
                new GameMock(){Name = "Thanks For All That Fish", GameType = GameType.ScoreAttack, GameState = GameState.Joined, Id = 7, OperatorLogo = "/ApplicationIcon.png", GameEnd = DateTime.Now.AddDays(3).AddHours(5), Rank = null},
                new GameMock(){Name = "Pontifex", GameType = GameType.Race, GameState = GameState.Joined, Id = 8, OperatorLogo = "/ApplicationIcon.png", GameEnd = DateTime.Now.AddDays(8), Rank = 8}};
        }
        #endregion

        #region UserNearbyGames
        public IGame[] UserNearbyGames(GeoCoordinate coordinate)
        {
            return new IGame[] {
                new GameMock(){Name = "Hydromystery", GameType = GameType.ScoreAttack, OperatorName = "Cafeteria", NumberOfPlayers = 23, NumberOfSlots = 48, Id = 1, OperatorLogo = "/ApplicationIcon.png", GameStart = new DateTime(2013, 4, 8, 12, 12,0) ,GameEnd = DateTime.Now.AddDays(2).AddHours(10), GameState = GameState.None, Difficulty = GameDifficulty.Easy, Description = "Le 10 septembre 2008, quelques jours après avoir fêté son vingtième anniversaire, Lewandowski débute sa carrière internationale avec la Pologne face à Saint-Marin, lors des éliminatoires de la coupe du monde 2010."},
                new GameMock(){Name = "North & South", GameType = GameType.Race, OperatorName = "Infogrames", NumberOfPlayers = 23, Id = 2, OperatorLogo = "/ApplicationIcon.png", GameEnd = DateTime.Now.AddDays(3).AddHours(12), GameState = GameState.None},
                new GameMock(){Name = "Ultimate Quest", GameType = GameType.ScoreAttack,  OperatorName = "JCVD", NumberOfPlayers = 23,Id = 3, OperatorLogo = "/ApplicationIcon.png",  GameEnd = DateTime.Now.AddDays(10), GameState = GameState.None},
                new GameMock(){Name = "Galaxy Quest", GameType = GameType.Race,  OperatorName = "NSEA", NumberOfPlayers = 23,Id = 4, OperatorLogo = "/ApplicationIcon.png", GameStart = new DateTime(2013,4,10,8,12,0), GameState = GameState.None},
                new GameMock(){Name = "The Quest for NEETs", GameType = GameType.Race,  OperatorName = "Ron Jeremy", NumberOfPlayers = 23,Id = 5, OperatorLogo = "/ApplicationIcon.png", GameStart = new DateTime(2013,5,9,21,5,8), GameState = GameState.None}};
        }
        #endregion

        #region UsersInactiveGames
        public IGame[] UsersInactiveGames()
        {
            return new IGame[] {
                new GameMock(){Name = "Wilqu!", GameType = GameType.ScoreAttack, Id = 9, OperatorLogo = "/ApplicationIcon.png", GameState = GameState.Ended, Rank = 4},
                new GameMock(){Name = "Torghal", GameType = GameType.Race, Id = 10, OperatorLogo = "/ApplicationIcon.png", GameState = GameState.Withdraw, Rank = null}};
        }
        #endregion
    }
}
