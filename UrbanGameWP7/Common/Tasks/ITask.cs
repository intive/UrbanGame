using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    /// <summary>
    /// Represents a task
    /// </summary>
    public interface ITask
    {
        int Id { get; set; }

        string Name { get; set; }

        TaskType Type { get; set; }

        /// <summary>
        /// Field shows that taks is active, inactive, accomplished or cancelled
        /// </summary>
        TaskState State { get; set; }

        string Description { get; set; }

        string AdditionalText { get; set; }

        IEntityEnumerable<IABCDPossibleAnswer> ABCDPossibleAnswers { get; }

        IEntityEnumerable<IBaseSolution> Solutions { get; }

        IGame Game { get; set; }

        string Picture { get; set; }

        SolutionStatus SolutionStatus { get; set; }

        bool IsRepeatable { get; set; }

        int? UserPoints { get; set; }

        int MaxPoints { get; set; }

        DateTime? EndDate { get; set; }

        int Version { get; set; }
    }
}
