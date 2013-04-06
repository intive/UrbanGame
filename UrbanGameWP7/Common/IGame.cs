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
        long Id { get; set; }

        string Name { get; set; }

        string Operator { get; set; }

        string Logo { get; set; }

        DateTime GameStart { get; set; }

        DateTime GameEnd { get; set; }

        bool Joined { get; set; }

        int Points { get; set; }

        int MaxPoints { get; set; }

        int NumberOfTasks { get; set; }

        int NumberOfCompletedTasks { get; set; }

        int NumberOfPlayers { get; set; }

        int NumberOfSlots { get; set; }
    }
}
