using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace WebService.DTOs
{
    public class Score
    {
        public string status { get; set; }
        public int attempts { get; set; }
        public int points { get; set; }
        public bool canRepeat { get; set; }
    }
}
