using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    public interface IToastPromptService
    {
        void ShowGameChanged(int gameId, string title, string text);
        void ShowSolutionStatusChanged(int taskId, int gameId, string title, string text);
        string GetDifferencesText(IList<string> diff);
    }
}
