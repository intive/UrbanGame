using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    /// <summary>
    /// Represents a solution
    /// </summary>
    public interface IBaseSolution
    {
        int Id { get; set; }

        TaskType TaskType { get; set; }
    }
}
