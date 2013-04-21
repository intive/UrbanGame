using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    /// <summary>
    /// Represents a task
    /// </summary>
    public interface IBaseTask
    {
        int Id { get; set; }

        string Name { get; set; }

        TaskType Type { get; set; }

        string Description { get; set; }

        string Picture { get; set; }

        SolutionStatus SolutionStatus { get; set; }

        bool IsRepeatable { get; set; }

        bool IsCancelled { get; set; }

        int? UserPoints { get; set; }

        int MaxPoints { get; set; }

        DateTime? EndDate { get; set; }

        double Version { get; set; }
    }
}
