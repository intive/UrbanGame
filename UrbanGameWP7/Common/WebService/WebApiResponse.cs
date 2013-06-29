using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;

namespace Common
{
    public class WebApiResponse
    {
        public string Json { get; set; }

        public HttpStatusCode Status { get; set; }

        public bool Success { get; set; }
    }
}
