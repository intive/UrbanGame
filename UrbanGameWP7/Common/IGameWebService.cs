using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Device.Location;

namespace Common
{
    /// <summary>
    /// Interfece responsible for communication with the web server
    /// </summary>
    public interface IGameWebService
    {

        /// <summary>
        /// Method returns an array of existing games(partially filled in)
        /// </summary>
        /// <returns>Array of IGame</returns>
        IGame[] GetGames();

        /// <summary>
        /// Method responses for signing up user into the game
        /// </summary>
        /// <param name="gid">Integer parameter - game's identifier</param>
        /// <returns>a bool</returns>
        bool SingUpToTheGame(int gid);

        /// <summary>
        /// Returns completely filled in IGame
        /// </summary>
        /// <param name="gid">Integer parameter - game's identifier</param>
        /// <returns></returns>
        IGame GetGameInfo(int gid);

        /// <summary>
        /// Method returns an array of game's tasks
        /// </summary>
        /// <returns>Array of ITask</returns>
        ITask[] GetTasks(int gid);

        /// <summary>
        /// Returns task's details
        /// </summary>
        /// <param name="gid">Integer parameter - game's identifier</param>
        /// <param name="tid">Integer parameter - task's identifier</param>
        /// <returns>Task's details</returns>
        ITaskDetails GetTaskDetails(int gid, int tid);

        /// <summary>
        /// Returns user game progress
        /// </summary>
        /// <param name="gid">Integer parameter - game's identifier</param>
        /// <returns>game progress</returns>
        int GetGameProgress(int gid);

        /// <summary>
        /// Returns user task progress
        /// </summary>
        /// <param name="gid">Integer parameter - game's identifier</param>
        /// <param name="tid">Integer parameter - task's identifier</param>
        /// <returns>task progress</returns>
        int GetTaskProgress(int gid, int tid);

        /// <summary>
        /// Sends solution to the web server
        /// </summary>
        /// <param name="gid">Integer parameter - game's identifier</param>
        /// <param name="tid">Integer parameter - task's identifier</param>
        /// <param name="solution">Solution of task</param>
        /// <returns>a bool</returns>
        bool SubmitTaskSolution(int gid, int tid, ISolution solution);

        /// <summary>
        /// Method responses for user authorization
        /// </summary>
        /// <param name="username">Username</param>
        /// <returns>a bool</returns>
        bool Authorize(string username, string password);

        /// <summary>
        /// Shows that user is authorize
        /// </summary>
        bool isAuthorize
        {
            get;
            set;
        }

        /// <summary>
        /// Method returns array of games in which user plays(only needed fields filled in)
        /// </summary>
        /// <returns></returns>
        IGame[] GamesUserPlays();

        /// <summary>
        /// Method returns array of the nearest games(only needed fields filled in)
        /// </summary>
        /// <returns></returns>
        IGame[] NearestGames(GeoCoordinate coordinate);

        /// <summary>
        /// Method returns array of games in which user played(only needed fields filled in)
        /// </summary>
        /// <returns></returns>
        IGame[] GamesUserPlayed();

        /// <summary>
        /// Event fire when a game changes
        /// </summary>
        event GameChangedEventHandler GameChanged;


    }
}
