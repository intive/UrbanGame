using Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace UrbanGame.Storage
{
    public class PositionedHighScores
    {
        public int Position { get; set; }
        public IHighScore Entity { get; set; }
    }
}
