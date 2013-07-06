using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    public class GameChangedEvent
    {
        public int Id { get; set; }

        public bool NewTasks { get; set; }
    }
}
