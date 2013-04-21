using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    public interface IOpenQuestionSolution : IBaseSolution
    {
        string TextAnswer { get; set; }
    }
}
