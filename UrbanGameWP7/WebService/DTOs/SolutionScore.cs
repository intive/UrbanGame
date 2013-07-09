using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Newtonsoft.Json;

namespace WebService.DTOs
{
    public class SolutionScore
    {
        [JsonProperty("_embedded")]
        public Score[] Score { get; set; }
    }
}
