using System;
using System.Collections.Generic;
using System.Device.Location;
using System.Linq;
using System.Text;

namespace Common
{
    /// <summary>
    /// Represents a game
    /// </summary>
    public interface IGame
    {
        long Id { get; set; }

        string Name { get; set; }

        string Operator { get; set; }

        string Logo { get; set; }

        DateTime GameStart { get; set; }

        DateTime GameEnd { get; set; }

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

        /// <summary>
        /// User's rank in that game
        /// </summary>
        int? Rank { get; set; } 

        GeoCoordinate GameLocalization { get; set; }

        GameDifficulty Difficulty { get; set; }

        string Description { get; set; }
    }
}
