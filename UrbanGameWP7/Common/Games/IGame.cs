using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    /// <summary>
    /// Represents a game
    /// </summary>
    public interface IGame
    {
        int Id { get; set; }

        string Name { get; set; }

        string OperatorName { get; set; }

        string Localization { get; set; }

        string GameLogo { get; set; }

        string GameLogoFullUrl { get; }

        DateTime GameStart { get; set; }

        DateTime GameEnd { get; set; }

        GameType GameType { get; set; }

        int Points { get; set; }

        int MaxPoints { get; set; }

        int NumberOfTasks { get; set; }

        int NumberOfCompletedTasks { get; set; }

        int NumberOfPlayers { get; set; }

        int NumberOfSlots { get; set; }

        /// <summary>
        /// Field shows that user won, joined, ended, withdraw, etc. the game
        /// </summary>
        GameState GameState { get; set; }

        bool GameOverDisplayed { get; set; }

        /// <summary>
        /// User's rank in that game
        /// </summary>
        int? Rank { get; set; }

        double GameLatitude { get; set; }

        double GameLongitude { get; set; }

        GameDifficulty Difficulty { get; set; }

        string Description { get; set; }

        string Prizes { get; set; }

        IEntityEnumerable<ITask> Tasks { get; }

        IEntityEnumerable<IAlert> Alerts { get; }

        IEntityEnumerable<IHighScore> HighScores { get; }

        int Version { get; set; }

        string ListOfChanges { get; set; }
    }
}
