using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    /// <summary>
    /// Represents high score in game
    /// </summary>
    public interface IHighScore
    {
        int Id { get; set; }

        string UserLogin { get; set; }

        int Points { get; set; }

        IGame Game { get; set; }
    }
}
