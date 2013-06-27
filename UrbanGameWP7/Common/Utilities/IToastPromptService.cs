using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    public interface IToastPromptService
    {
        void ShowGameChanged(int gameId, string title, string text, IList<string> diff);
        void ShowSolutionStatusChanged(int taskId, string title, string text);
    }
}
