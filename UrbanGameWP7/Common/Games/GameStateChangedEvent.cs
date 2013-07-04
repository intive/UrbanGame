using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    public class GameStateChangedEvent
    {
        public int Id { get; set; }

        public GameState NewState { get; set; }

        public int? Rank { get; set; }
    }
}
