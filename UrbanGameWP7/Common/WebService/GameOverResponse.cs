using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    public class GameOverResponse
    {
        public bool IsGameOver { get; set; }
        public GameState State { get; set; }
        public int? Rank { get; set; }
    }
}
