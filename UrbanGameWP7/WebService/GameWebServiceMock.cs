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
            ListOfTasks = new List<ITask>();

            /*string lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam aliquam mauris vel elit tincidunt ac bibendum tortor scelerisque. Mauris nisi augue, malesuada ac lobortis sed, rhoncus et mauris. Vivamus dictum turpis congue arcu euismod in pulvinar mi volutpat. Aliquam euismod pharetra velit eu sagittis. Proin et nisi nibh, ut egestas enim.";
            ListOfTasks.Add(new TaskMock() { Id = 1, Type = TaskType.ABCD, Description = lorem, Picture = "/ApplicationIcon.png", SolutionStatus = SolutionStatus.NotSend, IsRepeatable = false, IsCancelled = false, UserPoints = null, MaxPoints = 20, EndDate = DateTime.Now.AddDays(1), Version = 1 });
            ListOfTasks.Add(new TaskMock() { Id = 2, Type = TaskType.OpenQuestion, Description = lorem, Picture = "/ApplicationIcon.png", SolutionStatus = SolutionStatus.Pending, IsRepeatable = true, IsCancelled = true, UserPoints = null, MaxPoints = 20, EndDate = DateTime.Now.AddDays(1), Version = 1 });
            ListOfTasks.Add(new TaskMock() { Id = 3, Type = TaskType.Photo, Description = lorem, Picture = "/ApplicationIcon.png", SolutionStatus = SolutionStatus.Rejected, IsRepeatable = true, IsCancelled = false, UserPoints = null, MaxPoints = 20, EndDate = DateTime.Now.AddDays(1), Version = 1 });
            ListOfTasks.Add(new TaskMock() { Id = 4, Type = TaskType.QRCode, Description = lorem, Picture = "/ApplicationIcon.png", SolutionStatus = SolutionStatus.Accepted, IsRepeatable = false, IsCancelled = false, UserPoints = 10, MaxPoints = 20, EndDate = DateTime.Now.AddDays(1), Version = 1 });
            ListOfTasks.Add(new TaskMock() { Id = 5, Type = TaskType.GPS, Description = lorem, Picture = "/ApplicationIcon.png", SolutionStatus = SolutionStatus.Pending, IsRepeatable = false, IsCancelled = false, UserPoints = null, MaxPoints = 20, EndDate = DateTime.Now.AddDays(1), Version = 1 });*/
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
            return UserNearbyGames(null).Union(UsersActiveGames()).Union(UsersInactiveGames()).FirstOrDefault(x => x.Id == gid);
        }
        #endregion

        #region GetGameProgress
        public int GetGameProgress(int gid)
        {
            return 0;
        }
        #endregion

        #region GetTasks
        public ITask[] GetTasks(int gid)
        {
            return ListOfTasks.Where(task => task.Game == null ? false : task.Game.Id == gid).ToArray();
        }
        #endregion

        #region GetTaskDetails
        public ITask GetTaskDetails(int gid, int tid)
        {
            return new TaskMock()
            {
                Id = gid,
                Name = "Where is he",
                IsRepeatable = true,
                Description = "Lorem ipsum dolor sit amet, consecteturadipiscing elit. Aliquam sit amet elementum nulla. Aliquam sed labortis libero. In id orci ac turpis adipiscing lictus. Liquam sed lobortis libero. In id orci ac turpis adipiscing luctus.",
                Picture = "/ApplicationIcon.png",
                UserPoints = 0,
                MaxPoints = 12,
                EndDate = DateTime.Now.AddHours(21).AddDays(4),
                Type = TaskType.GPS
            };
        }
        #endregion

        #region GetTaskDetails generic
        public TTaskType GetTaskDetails<TTaskType>(int gid, int tid) 
            where TTaskType : ITask
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
        public SubmitResult SubmitTaskSolution(int gid, int tid, IBaseSolution solution)
        {
            GameChangesManager.AddSolution(new SubmittedSolution() { TaskId = tid });
            SubmitResult sbResult = SubmitResult.AnswerCorrect;
            
            return sbResult;
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
                new GameMock(){Name = "M For The Mission", GameType = GameType.Race, GameState = GameState.Joined, Id = 6, GameLogo = "/ApplicationIcon.png", GameEnd = DateTime.Now.AddDays(2).AddHours(13), Rank = 4},
                new GameMock(){Name = "Thanks For All That Fish", GameType = GameType.ScoreAttack, GameState = GameState.Joined, Id = 7, GameLogo = "/ApplicationIcon.png", GameEnd = DateTime.Now.AddDays(3).AddHours(5), Rank = null},
                new GameMock(){Name = "Pontifex", GameType = GameType.Race, GameState = GameState.Joined, Id = 8, GameLogo = "/ApplicationIcon.png", GameEnd = DateTime.Now.AddDays(8), Rank = 8}};
        }
        #endregion

        #region UserNearbyGames
        public IGame[] UserNearbyGames(GeoCoordinate coordinate)
        {
            var games = new IGame[] {
                new GameMock(){Name = "Hydromystery", GameType = GameType.ScoreAttack, OperatorName = "Cafeteria", NumberOfPlayers = 23, NumberOfSlots = 48, Id = 1, GameLogo = "/ApplicationIcon.png", GameStart = new DateTime(2013, 4, 8, 12, 12,0) ,GameEnd = DateTime.Now.AddDays(2).AddHours(10), GameState = GameState.None, Difficulty = GameDifficulty.Easy, Description = "Le 10 septembre 2008, quelques jours après avoir fêté son vingtième anniversaire, Lewandowski débute sa carrière internationale avec la Pologne face à Saint-Marin, lors des éliminatoires de la coupe du monde 2010."},
                new GameMock(){Name = "North & South", GameType = GameType.Race, OperatorName = "Infogrames", NumberOfPlayers = 23, Id = 2, GameLogo = "/ApplicationIcon.png", GameStart = new DateTime(2013, 5, 8, 12, 12,0), GameEnd = DateTime.Now.AddDays(3).AddHours(12), GameState = GameState.None},
                new GameMock(){Name = "Ultimate Quest", GameType = GameType.ScoreAttack, OperatorName = "JCVD", NumberOfPlayers = 23,Id = 3, GameLogo = "/ApplicationIcon.png", GameStart = DateTime.Now.AddDays(1).AddHours(12), GameEnd = DateTime.Now.AddDays(10), GameState = GameState.None},
                new GameMock(){Name = "Galaxy Quest", GameType = GameType.Race, OperatorName = "NSEA", NumberOfPlayers = 23,Id = 4, GameLogo = "/ApplicationIcon.png", GameStart = new DateTime(2013,4,10,8,12,0), GameEnd = new DateTime(2013,6,5,4,12,30), GameState = GameState.None},
                new GameMock(){Name = "The Quest for NEETs", GameType = GameType.Race, OperatorName = "Ron Jeremy", NumberOfPlayers = 23,Id = 5, GameLogo = "/ApplicationIcon.png", GameStart = new DateTime(2013,5,9,21,5,8),GameEnd = DateTime.Now.AddDays(2).AddHours(10), GameState = GameState.None}};

            string lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam aliquam mauris vel elit tincidunt ac bibendum tortor scelerisque. Mauris nisi augue, malesuada ac lobortis sed, rhoncus et mauris. Vivamus dictum turpis congue arcu euismod in pulvinar mi volutpat. Aliquam euismod pharetra velit eu sagittis. Proin et nisi nibh, ut egestas enim.";
            int id = 1;
            foreach(var g in games)
            {
                g.Tasks.Add(new TaskMock() { Id = id++, Type = TaskType.GPS, Description = lorem, Picture = "/ApplicationIcon.png", SolutionStatus = SolutionStatus.Pending, IsRepeatable = false, UserPoints = null, MaxPoints = 20, EndDate = DateTime.Now.AddDays(1), Version = 1 });
                g.Tasks.Add(new TaskMock() { Id = id++, Type = TaskType.ABCD, Description = lorem, Picture = "/ApplicationIcon.png", SolutionStatus = SolutionStatus.NotSend, IsRepeatable = false, UserPoints = null, MaxPoints = 20, EndDate = DateTime.Now.AddDays(1), Version = 1 });
            }

            return games;
        }
        #endregion

        #region UsersInactiveGames
        public IGame[] UsersInactiveGames()
        {
            return new IGame[] {
                new GameMock(){Name = "Wilqu!", GameType = GameType.ScoreAttack, Id = 9, GameLogo = "/ApplicationIcon.png", GameState = GameState.Ended, Rank = 4},
                new GameMock(){Name = "Torghal", GameType = GameType.Race, Id = 10, GameLogo = "/ApplicationIcon.png", GameState = GameState.Withdraw, Rank = null}};
        }
        #endregion

        #region ActiveTasks
        public ITask[] ActiveTasks()
        {
            return new ITask[] {
                new TaskMock(){Id = 1, Name="Suprise", Description = "Where am I", Picture = "/ApplicationIcon.png", IsRepeatable = true, EndDate = DateTime.Now.AddHours(5), State = TaskState.Active, MaxPoints = 20, UserPoints = 10, Type = TaskType.ABCD},
                new TaskMock(){Id = 2, Name="Second", Description = "Where am I", Picture = "/ApplicationIcon.png", IsRepeatable = false, EndDate = DateTime.Now.AddDays(1), State = TaskState.Active, MaxPoints = 5, UserPoints = 0 , Type = TaskType.GPS}};
        }
        #endregion

        #region InactiveTasks
        public ITask[] InactiveTasks()
        {
            return new ITask[] {
                new TaskMock(){Id = 5, Name="Quest", Description = "Don't cross the river", Picture = "/ApplicationIcon.png", State = TaskState.Inactive },
                new TaskMock(){Id = 6, Name="Second", Description = "Where am I", IsRepeatable = false, Picture = "/ApplicationIcon.png", EndDate = DateTime.Now.AddHours(5), State = TaskState.Inactive, MaxPoints = 5 }};
        }
        #endregion

        #region AccomplishedTasks
        public ITask[] AccomplishedTasks()
        {
            return new ITask[] {
                new TaskMock(){Id = 7, Name="Poison for Ass", Description = "Poison ...", State = TaskState.Accomplished, Picture = "/ApplicationIcon.png", MaxPoints = 34, UserPoints = 12 },
                new TaskMock(){Id = 8, Name="Poison for Ass", Description = "Poison ...", State = TaskState.Accomplished, Picture = "/ApplicationIcon.png", MaxPoints = 34, UserPoints = 12 }};
        }
        #endregion

        #region CancelledTasks
        public ITask[] CancelledTasks()
        {
            return new ITask[] {
                new TaskMock(){Id = 8, Name="Where am I?", Description = "Poison ...", Picture = "/ApplicationIcon.png", State = TaskState.Cancelled },
                new TaskMock(){Id = 9, Name="Where am I?", Description = "Poison ...", Picture = "/ApplicationIcon.png", State = TaskState.Cancelled }};
        }
        #endregion

        #region Alerts
        public IAlert[] Alerts()
        {
            return new IAlert[] {
                new AlertMock(){Id = 1, Topic = "Unreal alert title", Description = "Sth happened at route 27"},
                new AlertMock(){Id = 2, Topic = "Unreal alert title", Description = "Sth happened at route 27"}};
        }
        #endregion

        #region HighScores
        public IHighScore[] HighScores()
        {
            return new IHighScore[]{
                new HighScoreMock(){Id = 1, UserLogin = "Korona", Points =199},
                new HighScoreMock(){Id = 1, UserLogin = "Amanda99", Points =99},
                new HighScoreMock(){Id = 2, UserLogin = "LoganXxX", Points =299}};
        }
        #endregion
    }
}
