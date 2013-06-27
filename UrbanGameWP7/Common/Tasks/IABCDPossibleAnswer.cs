using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    public interface IABCDPossibleAnswer
    {
        int Id { get; set; }

        string Answer { get; set; }

        ITask Task { get; set; }

        IEntityEnumerable<IABCDUserAnswer> ABCDUserAnswers { get; }
    }
}
