using Common;
using System;
using System.Collections.Generic;
using System.Device.Location;
using System.Linq;
using System.Text;
using WebService.BOMock;
using System.IO;
using System.Windows;
using System.Net;
using System.Threading.Tasks;
using Newtonsoft.Json;
using WebService.JsonConverters;
using WebService.DTOs;
using System.Diagnostics;

namespace WebService
{
    public class GameWebService : IGameWebService
    {
        #region GameWebServiceMock

        /// <summary>
        /// Simple constructor
        /// </summary>
        public GameWebService()
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

        #region WebAPI

        const string APIurl = "http://urbangame.patronage.blstream.com/api/";
        JsonConverter[] _jsonConverters = new JsonConverter[] { new JsonGameTypeConverter(), new JsonEnumConverter(), new JsonDateTimeConverter() };

        public static async Task<string> GetJson(string relativeUrl)
        {
            WebRequest request = HttpWebRequest.Create(APIurl + relativeUrl);
            request.Credentials = new NetworkCredential() { UserName = "maxikq", Password = "pass" };

            Task<WebResponse> task = request.GetResponseAsync();
            string text = string.Empty;

            await task.ContinueWith((taskParam) =>
            {
                Stream response = task.Result.GetResponseStream();
                using (StreamReader sr = new StreamReader(response))
                    text = sr.ReadToEnd();
            });
            return text;
        }

        public async Task<TObject> GetViaApi<TObject>(string relativeUrl) 
        {
            return await GetViaApi<TObject>(relativeUrl, null);
        }

        public async Task<TObject> GetViaApi<TObject>(string relativeUrl, params object[] args)
        {
            string url = args != null ? String.Format(relativeUrl, args) : relativeUrl;
            string json = await GetJson(url);
            return JsonConvert.DeserializeObject<TObject>(json, _jsonConverters);
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
            return await GetViaApi<Game>("games/{0}/static", gid);
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
            int r = new Random().Next(100);

            if (r < 20)
                return SubmitResult.AnswerIncorrect;
            else if (r < 60)
                return SubmitResult.AnswerCorrect;
            else
                return SubmitResult.ScoreDelayed;
        }
        #endregion

        #region GetSolutionStatus

        public async Task<SolutionStatusResponse> GetSolutionStatus(int taskId)
        {
            SolutionStatusResponse result = new SolutionStatusResponse();
            result.Status = new Random().Next(10) >= 5 ? SolutionStatus.Accepted : SolutionStatus.Rejected;
            result.Points = 5;
            return result;
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

        #region UserNearbyGames
        public async Task<IGame[]> UserNearbyGames(GeoCoordinate coordinate)
        {
            if (coordinate == null)
                throw new ArgumentNullException("coordinate");

            var result = await GetViaApi<ListOfGames>("games?lat={0}&lon={1}", coordinate.Latitude, coordinate.Longitude);
            if (result.Games == null)
                result.Games = new List<Game>();
            return result.Games.Cast<IGame>().ToArray();
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
