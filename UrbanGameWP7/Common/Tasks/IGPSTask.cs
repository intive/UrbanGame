using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    public interface IGPSTask : IBaseTask
    {
        double? Latitude { get; set; }

        double? Longitude { get; set; }
    }
}
