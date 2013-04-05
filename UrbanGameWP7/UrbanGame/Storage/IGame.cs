using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace UrbanGame.Storage
{
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

        byte NumberOfTasks { get; set; }

        byte NumberOfCompletedTasks { get; set; }

        int NumberOfPlayers { get; set; }

        int NumberOfSlots { get; set; }
    }
}
