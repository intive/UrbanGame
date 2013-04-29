using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    public interface IGPSSolution : IBaseSolution
    {
        double? Longitude { get; set; }

        double? Latitude { get; set; }
    }
}
