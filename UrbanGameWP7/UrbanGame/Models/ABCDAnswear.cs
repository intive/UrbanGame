using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Common;

namespace UrbanGame.Models
{
    public class ABCDAnswer
    {
        public IABCDPossibleAnswer PossibleAnswer { get; set; }
        public bool IsChecked { get; set; }
    }
}
