using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    /// <summary>
    /// Represents a single user answer for ABCD task
    /// </summary>
    public interface IABCDUserAnswer
    {
        int Id { get; set; }

        IABCDSolution Solution { get; set; }

        IABCDPossibleAnswer ABCDPossibleAnswer { get; set; }

        bool Answer { get; set; }
    }
}
