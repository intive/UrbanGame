using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    public interface IABCDPossibleAnswer
    {
        string Answer { get; set; }

        ITask AssignedTask { get; }
    }
}
