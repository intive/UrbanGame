using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace WebService.DTOs
{
    public class ListOfTasks
    {
        [JsonProperty("_embedded")]
        public List<GameTask> Tasks { get; set; }
    }
}
