using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    public interface IAppbarManager
    {
        void ConfigureAppbar(List<AppbarItem> appbarItems);
        void ChangeItemText(string itemMessage, string newText);
        void HideAppbar();
        void ShowAppbar();
    }
}
