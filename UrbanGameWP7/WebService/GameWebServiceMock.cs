using Common;
using System;
using System.Collections.Generic;
using System.Device.Location;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
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
            InitializeData();
        }
        #endregion

        #region InitializeData
        private void InitializeData()
        {
            ListOfTasks.Clear();
            ListOfGames.Clear();

            ListOfGames.AddRange(new IGame[] {
                new GameMock(){Name = "Hydromystery", GameType = GameType.ScoreAttack, OperatorName = "Cafeteria", NumberOfPlayers = 23, NumberOfSlots = 48, Id = 1, GameLogo = "/ApplicationIcon.png", GameStart = new DateTime(2013, 4, 8, 12, 12,0) ,GameEnd = DateTime.Now.AddDays(2).AddHours(10), Difficulty = GameDifficulty.Easy, Description = "Le 10 septembre 2008, quelques jours après avoir fêté son vingtième anniversaire, Lewandowski débute sa carrière internationale avec la Pologne face à Saint-Marin, lors des éliminatoires de la coupe du monde 2010.", Localization = "Wroclaw"},
                new GameMock(){Name = "North & South", GameType = GameType.Race, OperatorName = "Infogrames", NumberOfPlayers = 23, Id = 2, GameLogo = "/ApplicationIcon.png", GameStart = new DateTime(2013, 5, 8, 12, 12,0), GameEnd = DateTime.Now.AddDays(3).AddHours(12)},
                new GameMock(){Name = "Ultimate Quest", GameType = GameType.ScoreAttack, OperatorName = "JCVD", NumberOfPlayers = 23,Id = 3, GameLogo = "/ApplicationIcon.png", GameStart = DateTime.Now.AddDays(1).AddHours(12), GameEnd = DateTime.Now.AddDays(10).AddHours(2)},
                new GameMock(){Name = "Galaxy Quest", GameType = GameType.Race, OperatorName = "NSEA", NumberOfPlayers = 23,Id = 4, GameLogo = "/ApplicationIcon.png", GameStart = new DateTime(2013,4,10,8,12,0), GameEnd = DateTime.Now.AddDays(3).AddHours(7).AddMinutes(18).AddSeconds(43)},
                new GameMock(){Name = "The Quest for NEETs", GameType = GameType.Race, OperatorName = "Ron Jeremy", NumberOfPlayers = 23,Id = 5, GameLogo = "/ApplicationIcon.png", GameStart = new DateTime(2013,5,9,21,5,8),GameEnd = DateTime.Now.AddDays(2).AddHours(10)}});

            string lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam aliquam mauris vel elit tincidunt ac bibendum tortor scelerisque. Mauris nisi augue, malesuada ac lobortis sed, rhoncus et mauris. Vivamus dictum turpis congue arcu euismod in pulvinar mi volutpat. Aliquam euismod pharetra velit eu sagittis. Proin et nisi nibh, ut egestas enim.";
            string accident = "There might be a problem getting to center, bacause of bus crash. Furthermore the police imidiately came and now tries to figure out what realy happened.";

            int taskId = 1;
            int possibleAnswerId = 1;
            int alertId = 1;
            int highScoreId = 1;

            foreach(var g in ListOfGames)
            {
                var task1 = new TaskMock() { Id = taskId++, Name = "Find Wally", Type = TaskType.GPS, Description = lorem, Picture = "/ApplicationIcon.png", SolutionStatus = SolutionStatus.NotSend, IsRepeatable = true, UserPoints = null, MaxPoints = 20, EndDate = DateTime.Now.AddDays(1), Version = 1 };
                g.Tasks.Add(task1);
                ListOfTasks.Add(task1);

                var task2 = new TaskMock() { Id = taskId++, Name = "IQ Test!", AdditionalText = "What is red with dots?",  Type = TaskType.ABCD, Description = lorem, Picture = "/ApplicationIcon.png", SolutionStatus = SolutionStatus.NotSend, IsRepeatable = false, UserPoints = null, MaxPoints = 20, EndDate = DateTime.Now.AddDays(1), Version = 1 };
                task2.ABCDPossibleAnswers.Add(new ABCDPossibleAnswerMock() { Id = possibleAnswerId++, Answer = "Zebra" });
                task2.ABCDPossibleAnswers.Add(new ABCDPossibleAnswerMock() { Id = possibleAnswerId++, Answer = "Dragon" });
                task2.ABCDPossibleAnswers.Add(new ABCDPossibleAnswerMock() { Id = possibleAnswerId++, Answer = "Leaf" });
                task2.ABCDPossibleAnswers.Add(new ABCDPossibleAnswerMock() { Id = possibleAnswerId++, Answer = "Ladybug" });
                g.Tasks.Add(task2);
                ListOfTasks.Add(task2);

                var task3 = new TaskMock() { Id = taskId++, Name = "Meet Frank", Type = TaskType.GPS, Description = lorem, Picture = "/ApplicationIcon.png", SolutionStatus = SolutionStatus.NotSend, IsRepeatable = false, UserPoints = null, MaxPoints = 20, EndDate = DateTime.Now.AddDays(3).AddHours(7), Version = 1 };
                ListOfTasks.Add(task3);
                g.Tasks.Add(task3);

                var task4 = new TaskMock() { Id = taskId++, Name = "Brain Storm!", AdditionalText = "What color is a red car?", Type = TaskType.ABCD, Description = lorem, Picture = "/ApplicationIcon.png", SolutionStatus = SolutionStatus.NotSend, IsRepeatable = true, UserPoints = null, MaxPoints = 30, EndDate = DateTime.Now.AddDays(1).AddHours(15), Version = 1 };
                task4.ABCDPossibleAnswers.Add(new ABCDPossibleAnswerMock() { Id = possibleAnswerId++, Answer = "Green" });
                task4.ABCDPossibleAnswers.Add(new ABCDPossibleAnswerMock() { Id = possibleAnswerId++, Answer = "Red"});
                task4.ABCDPossibleAnswers.Add(new ABCDPossibleAnswerMock() { Id = possibleAnswerId++, Answer = "Blue"});
                task4.ABCDPossibleAnswers.Add(new ABCDPossibleAnswerMock() { Id = possibleAnswerId++, Answer = "DarkBlack"});
                g.Tasks.Add(task4);
                ListOfTasks.Add(task4);

                g.HighScores.Add(new HighScoreMock() { Id = highScoreId++, UserLogin = "XTerminator", Points = 329, AchievedAt = DateTime.Now.AddDays(1).AddHours(13).AddMinutes(37) });
                g.HighScores.Add(new HighScoreMock() { Id = highScoreId++, UserLogin = "RunnungRabit", Points = 310, AchievedAt = DateTime.Now.AddDays(1).AddHours(3).AddMinutes(7) });
                g.HighScores.Add(new HighScoreMock() { Id = highScoreId++, UserLogin = "$ebastian", Points = 150, AchievedAt = DateTime.Now.AddHours(9).AddMinutes(27) });
                g.HighScores.Add(new HighScoreMock() { Id = highScoreId++, UserLogin = "xX_Warior_Xx", Points = 90, AchievedAt = DateTime.Now.AddHours(1).AddMinutes(1) });

                g.Alerts.Add(new AlertMock() { Id = alertId++, Topic = "Information", Description = accident, AlertAppear = new DateTime(2013, 6, 27, 12, 10, 0) });
                g.Alerts.Add(new AlertMock() { Id = alertId++, Topic = "Information", Description = accident, AlertAppear = new DateTime(2013, 6, 27, 11, 10, 0) });
                g.Alerts.Add(new AlertMock() { Id = alertId++, Topic = "Information", Description = accident, AlertAppear = new DateTime(2013, 7, 4, 12, 1, 0) });
            }
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

        #region SingUpToTheGame
        public bool SingUpToTheGame(int gid)
        {
            throw new NotImplementedException();
        }
        #endregion

        #region GetGameInfo
        public async Task<IGame> GetGameInfo(int gid)
        {
            return (await UserNearbyGames(new GeoCoordinate(1,1))).FirstOrDefault(x => x.Id == gid);
        }
        #endregion

        #region CheckGameOver
        public async Task<GameOverResponse> CheckGameOver(int gid)
        {
            /*int r = new Random().Next(100);
            int rank = new Random().Next(1, 100);

            if (r < 20)
                return new GameOverResponse() { State = GameState.Won, Rank = rank, IsGameOver = true };
            if (r < 35)
                return new GameOverResponse() { State = GameState.Lost, Rank = rank, IsGameOver = true };
            else*/
                return new GameOverResponse() { State = GameState.Joined, IsGameOver = false };
        }
        #endregion

        #region GetGameProgress
        public int GetGameProgress(int gid)
        {
            return 0;
        }
        #endregion

        #region GetTasks
        public async Task<ITask[]> GetTasks(int gid)
        {
            return ListOfTasks.Where(t => t.Game.Id == gid).ToArray();
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
        public SolutionResultScore SubmitTaskSolution(int gid, int tid, IBaseSolution solution)
        {
            int r = new Random().Next(100);
            var task = ListOfTasks.First(t => t.Id == tid);

            if (r < 20)
            {
                if (!task.IsRepeatable)
                    task.State = TaskState.Accomplished;

                return new SolutionResultScore() { SubmitResult = SubmitResult.AnswerIncorrect, ScoredPoints = 0 };
            }
            else if (r < 60)
            {
                if (!task.IsRepeatable || task.MaxPoints == r)
                    task.State = TaskState.Accomplished;
                return new SolutionResultScore() { SubmitResult = SubmitResult.AnswerCorrect, ScoredPoints = r };
            }
            else
            {
                return new SolutionResultScore() { SubmitResult = SubmitResult.ScoreDelayed };
            }
        }
        #endregion

        #region GetSolutionStatus

        public async Task<SolutionStatusResponse> GetSolutionStatus(int taskId)
        {
            SolutionStatusResponse result = new SolutionStatusResponse();
            result.Status = new Random().Next(10) >= 5 ? SolutionStatus.Accepted : SolutionStatus.Rejected;
            result.Status = new Random().Next(10) >= 5 ? SolutionStatus.Accepted : SolutionStatus.Rejected;
            result.Points = result.Status == SolutionStatus.Rejected ? 0 : new Random().Next(30);
            var task = ListOfTasks.First(t => t.Id == taskId);
            if ((result.Status == SolutionStatus.Accepted && result.Points == task.MaxPoints) || !task.IsRepeatable)
                task.State = TaskState.Accomplished;
            task.UserPoints = result.Points;
            return result;
        }

        #endregion

        #region Authorize
        public async Task<AuthorizeState> Authorize(string username, string password)
        {
            return AuthorizeState.Success;
        }
        #endregion

        #region CreateAccount

        public async Task<CreateAccountResponse> CreateAccount(string username, string email, string password)
        {
            return CreateAccountResponse.Success;
        }

        #endregion

        #region UserNearbyGames
        public async Task<IGame[]> UserNearbyGames(GeoCoordinate coordinate)
        {        
            return ListOfGames.ToArray();
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
