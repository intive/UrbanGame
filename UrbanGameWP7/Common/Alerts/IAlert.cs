using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    /// <summary>
    /// Represents a alert in game
    /// </summary>
    public interface IAlert
    {
        int Id { get; set; }

        string Topic { get; set; }

        string Description { get; set; }

        DateTime AlertAppear { get; set; }

        IGame Game { get; set; }
    }
}
