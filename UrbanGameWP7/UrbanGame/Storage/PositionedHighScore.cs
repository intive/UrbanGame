using Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace UrbanGame.Storage
{
    public class PositionedHighScore
    {
        public int Position { get; set; }
        public IHighScore Entity { get; set; }
    }
}
