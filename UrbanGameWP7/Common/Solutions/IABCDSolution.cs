using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    public interface IABCDSolution : IBaseSolution
    {
        IEntityEnumerable<IABCDUserAnswer> ABCDUserAnswers { get; }
    }
}
