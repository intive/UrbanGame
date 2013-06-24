using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace WebService.DTOs
{
    public class ListOfGames
    {
        [JsonProperty("_embedded")]
        public List<Game> Games { get; set; }
    }
}
