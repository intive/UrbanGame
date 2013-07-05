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
        #region GameWebService

        /// <summary>
        /// Simple constructor
        /// </summary>
        public GameWebService()
        {
            ListOfGames = new List<IGame>();
            ListOfTasks = new List<ITask>();
        }

        #endregion

        #region WebAPI

        const string APIurl = "http://urbangame.patronage.blstream.com/api/";
        JsonConverter[] _jsonConverters = new JsonConverter[] { new JsonGameTypeConverter(), new JsonEnumConverter(), new JsonDateTimeConverter() };

        private static string _username = "maxikq";
        private static string _password = "pass";

        private static async Task<WebApiResponse> GetResponse(WebRequest request)
        {
            WebApiResponse result = new WebApiResponse() { Success = true, Status = HttpStatusCode.OK };

            try
            {
                Stream response = (await request.GetResponseAsync()).GetResponseStream();
                using (StreamReader sr = new StreamReader(response))
                    result.Json = sr.ReadToEnd();
            }
            catch (Exception ex)
            {
                result.Success = false;

                if (ex.InnerException != null && ex.InnerException is System.Net.WebException)
                {
                    System.Net.WebException webEx = (System.Net.WebException)ex.InnerException;

                    if (webEx.Response != null)
                    {
                        var resp = (HttpWebResponse)webEx.Response;
                        result.Status = resp.StatusCode;
                    }
                }
                else
                {
                    result.Status = HttpStatusCode.BadRequest;
                    System.Windows.Deployment.Current.Dispatcher.BeginInvoke(() =>
                    {
                        var message = ex.InnerException != null ? ex.InnerException.Message : ex.Message;
                        MessageBox.Show("Error while sending request to API.\r\n" + message, "Error", MessageBoxButton.OK);
                    });
                }
            }

            return result;
        }

        public static async Task<WebApiResponse> GetJson(string relativeUrl)
        {           
            WebRequest request = HttpWebRequest.Create(APIurl + relativeUrl);
            request.Credentials = new NetworkCredential() { UserName = _username, Password = _password };
            return await GetResponse(request);
        }

        public static async Task<WebApiResponse> PostJson(string relativeUrl, string postData)
        {            
            WebRequest request = HttpWebRequest.Create(APIurl + relativeUrl);
            request.Method = "POST";
            request.ContentType = "application/json";
            request.Credentials = new NetworkCredential() { UserName = _username, Password = _password };            

            Task<Stream> requestTask = request.GetRequestStreamAsync();
            await requestTask.ContinueWith((taskParam) =>
                {
                    byte[] byteData = System.Text.Encoding.UTF8.GetBytes(postData);
                    requestTask.Result.Write(byteData, 0, byteData.Length);
                    requestTask.Result.Close();
                });

            return await GetResponse(request);
        }

        public async Task<TObject> GetViaApi<TObject>(string relativeUrl) 
        {
            return await GetViaApi<TObject>(relativeUrl, null);
        }

        public async Task<TObject> GetViaApi<TObject>(string relativeUrl, params object[] args)
        {
            string url = args != null ? String.Format(relativeUrl, args) : relativeUrl;
            WebApiResponse response = await GetJson(url);
            return JsonConvert.DeserializeObject<TObject>(response.Json, _jsonConverters);
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

        #region CheckGameOver
        public async Task<GameOverResponse> CheckGameOver(int gid)
        {
            var game = await GetGameInfo(gid);
            bool gameOver = game.GameState == GameState.Won || game.GameState == GameState.Lost;
            return new GameOverResponse() { State = game.GameState, Rank = game.Rank, IsGameOver = gameOver };
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
        public SolutionResultScore SubmitTaskSolution(int gid, int tid, IBaseSolution solution)
        {
            int r = new Random().Next(100);

            if (r < 20)
                return new SolutionResultScore(){ SubmitResult = SubmitResult.AnswerIncorrect, ScoredPoints = r  };
            else if (r < 60)
                return new SolutionResultScore(){ SubmitResult = SubmitResult.AnswerCorrect, ScoredPoints = r  };
            else
                return new SolutionResultScore() { SubmitResult = SubmitResult.ScoreDelayed, ScoredPoints = r };
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

        #region CreateAccount

        public async Task<CreateAccountResponse> CreateAccount(string username, string email, string password)
        {
            WebApiResponse result = await PostJson("register", String.Format("{{ \"login\": \"{0}\", \"password\": \"{1}\" }}", username, password));

            if (!result.Success)
            {
                switch (result.Status)
                {
                    case HttpStatusCode.Conflict:
                        return CreateAccountResponse.LoginUnavailable;
                    case HttpStatusCode.RequestTimeout:
                        return CreateAccountResponse.Timeout;
                    default:
                        return CreateAccountResponse.UnknownError;
                }                
            }
            else
                return CreateAccountResponse.Success;
        }

        #endregion

        #region Authorize
        public async Task<AuthorizeState> Authorize(string username, string password)
        {
            _username = username;
            _password = password;

            WebApiResponse result = await GetJson("login");

            if (!result.Success)
            {
                switch (result.Status)
                {
                    case HttpStatusCode.Unauthorized:
                        return AuthorizeState.WrongPassword;
                    default:
                        return AuthorizeState.Unknown;
                }
            }
            else
            {
                return AuthorizeState.Success;
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
