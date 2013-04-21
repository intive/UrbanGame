using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    public interface IOpenQuestionTask : IBaseTask
    {
        string OpenQuestionCorrectAnswer { get; set; }
    }
}
