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
                new GameMock(){Name = "Hydromystery", GameType = GameType.ScoreAttack, OperatorName = "Cafeteria", NumberOfPlayers = 23, NumberOfSlots = 48, Id = 1, GameLogo = "/Images/gameicon.png", GameStart = new DateTime(2013, 4, 8, 12, 12,0) ,GameEnd = DateTime.Now.AddDays(2).AddHours(10), Difficulty = GameDifficulty.Easy, Description = "Le 10 septembre 2008, quelques jours après avoir fêté son vingtième anniversaire, Lewandowski débute sa carrière internationale avec la Pologne face à Saint-Marin, lors des éliminatoires de la coupe du monde 2010.", Localization = "Wroclaw"},
                new GameMock(){Name = "North & South", GameType = GameType.Race, OperatorName = "Infogrames", NumberOfPlayers = 23, Id = 2, GameLogo = "/Images/gameicon.png", GameStart = new DateTime(2013, 5, 8, 12, 12,0), GameEnd = DateTime.Now.AddDays(3).AddHours(12)},
                new GameMock(){Name = "Ultimate Quest", GameType = GameType.ScoreAttack, OperatorName = "JCVD", NumberOfPlayers = 23,Id = 3, GameLogo = "/Images/gameicon.png", GameStart = DateTime.Now.AddDays(1).AddHours(12), GameEnd = DateTime.Now.AddDays(10).AddHours(2)},
                new GameMock(){Name = "Galaxy Quest", GameType = GameType.Race, OperatorName = "NSEA", NumberOfPlayers = 23,Id = 4, GameLogo = "/Images/gameicon.png", GameStart = new DateTime(2013,4,10,8,12,0), GameEnd = DateTime.Now.AddDays(3).AddHours(7).AddMinutes(18).AddSeconds(43)},
                new GameMock(){Name = "The Quest for NEETs", GameType = GameType.Race, OperatorName = "Ron Jeremy", NumberOfPlayers = 23,Id = 5, GameLogo = "/Images/gameicon.png", GameStart = new DateTime(2013,5,9,21,5,8),GameEnd = DateTime.Now.AddDays(2).AddHours(10)}});

            string lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam aliquam mauris vel elit tincidunt ac bibendum tortor scelerisque. Mauris nisi augue, malesuada ac lobortis sed, rhoncus et mauris. Vivamus dictum turpis congue arcu euismod in pulvinar mi volutpat. Aliquam euismod pharetra velit eu sagittis. Proin et nisi nibh, ut egestas enim.";
            int taskId = 1;
            int alertId = 1;
            int highScoreId = 1;

            foreach (var g in ListOfGames)
            {
                var task = new TaskMock() { Id = taskId++, Name = "IQ Test!", AdditionalText = "What is red with dots?", Type = TaskType.ABCD, Description = lorem, Picture = "/ApplicationIcon.png", SolutionStatus = SolutionStatus.NotSend, IsRepeatable = false, UserPoints = null, MaxPoints = 20, EndDate = DateTime.Now.AddDays(1), Version = 1 };
                task.ABCDPossibleAnswers.Add(new ABCDPossibleAnswerMock() { CharId = 'a', Answer = "Zebra" });
                task.ABCDPossibleAnswers.Add(new ABCDPossibleAnswerMock() { CharId = 'b', Answer = "Dragon" });
                task.ABCDPossibleAnswers.Add(new ABCDPossibleAnswerMock() { CharId = 'c', Answer = "Leaf" });
                task.ABCDPossibleAnswers.Add(new ABCDPossibleAnswerMock() { CharId = 'd', Answer = "Ladybug" });
                g.Tasks.Add(task);
                ListOfTasks.Add(task);

                var task1 = new TaskMock() { Id = taskId++, Name = "Find Wally", Type = TaskType.GPS, Description = lorem, Picture = "/ApplicationIcon.png", SolutionStatus = SolutionStatus.NotSend, IsRepeatable = true, UserPoints = null, MaxPoints = 20, EndDate = DateTime.Now.AddDays(1), Version = 1 };
                g.Tasks.Add(task1);
                ListOfTasks.Add(task1);

                var task2 = new TaskMock() { Id = taskId++, Name = "Brain Storm!", AdditionalText = "What color is a red car?", Type = TaskType.ABCD, Description = lorem, Picture = "/ApplicationIcon.png", SolutionStatus = SolutionStatus.NotSend, IsRepeatable = true, UserPoints = null, MaxPoints = 30, EndDate = DateTime.Now.AddDays(1).AddHours(15), Version = 1 };
                task2.ABCDPossibleAnswers.Add(new ABCDPossibleAnswerMock() { CharId = 'a', Answer = "Green" });
                task2.ABCDPossibleAnswers.Add(new ABCDPossibleAnswerMock() { CharId = 'b', Answer = "Red" });
                task2.ABCDPossibleAnswers.Add(new ABCDPossibleAnswerMock() { CharId = 'c', Answer = "Blue" });
                task2.ABCDPossibleAnswers.Add(new ABCDPossibleAnswerMock() { CharId = 'd', Answer = "DarkBlack" });
                g.Tasks.Add(task2);
                ListOfTasks.Add(task2);

                var task3 = new TaskMock() { Id = taskId++, Name = "Find Wally 2", Type = TaskType.GPS, Description = lorem, Picture = "/ApplicationIcon.png", SolutionStatus = SolutionStatus.NotSend, IsRepeatable = false, UserPoints = null, MaxPoints = 20, EndDate = DateTime.Now.AddDays(1), Version = 1 };
                g.Tasks.Add(task3);
                ListOfTasks.Add(task3);

                g.HighScores.Add(new HighScoreMock() { Id = highScoreId++, UserLogin = "XTerminator", Points = 329 });
                g.Alerts.Add(new AlertMock() { Id = alertId++, Topic = "Information", Description = "There might be a problem getting to center, bacause of bus crash" });
                g.HighScores.Add(new HighScoreMock() { Id = highScoreId++, UserLogin = "RunnungRabit", Points = 310 });
                g.HighScores.Add(new HighScoreMock() { Id = highScoreId++, UserLogin = "$ebastian", Points = 150 });
                g.HighScores.Add(new HighScoreMock() { Id = highScoreId++, UserLogin = "xX_Warior_Xx", Points = 90 });
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

        #region JoinGame
        public async Task<bool> JoinGame(int gid)
        {
            return true;
        }
        #endregion

        #region LeaveGame
        public async Task<bool> LeaveGame(int gid)
        {
            return true;
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

        #region GetTasks
        public async Task<ITask[]> GetTasks(int gid)
        {
            return ListOfTasks.Where(t => t.Game.Id == gid).ToArray();
        }
        #endregion

        #region GetTaskDetails
        public async Task<ITask> GetTaskDetails(int gid, int tid)
        {
            return ListOfTasks.FirstOrDefault(t => t.Id == tid);
        }
        #endregion

        #region GetTaskDetails generic
        public async Task<TTaskType> GetTaskDetails<TTaskType>(int gid, int tid) 
            where TTaskType : ITask
        {
            return (TTaskType)await GetTaskDetails(gid, tid);
        }
        #endregion

        #region SubmitTaskSolution
        public async Task<SolutionResultScore> SubmitTaskSolution(int gid, int tid, IBaseSolution solution)
        {
            int r = new Random().Next(100);
            var task = ListOfTasks.First(t => t.Id == tid);

            if (r < 20)
            {
                if (!task.IsRepeatable)
                    task.State = TaskState.Accomplished;
                task.UserPoints = r;
                return new SolutionResultScore() { SubmitResult = SubmitResult.AnswerIncorrect, ScoredPoints = 0 };
            }
            else if (r < 60)
            {
                if (!task.IsRepeatable || task.MaxPoints == r)
                    task.State = TaskState.Accomplished;
                task.UserPoints = r;
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
