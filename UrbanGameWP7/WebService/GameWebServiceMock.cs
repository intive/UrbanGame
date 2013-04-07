using Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

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

        #region Authorize
        public bool Authorize(string username)
        {
            throw new NotImplementedException();
        }
        #endregion


        public event GameChangedEventHandler GameChanged;
    }
}
