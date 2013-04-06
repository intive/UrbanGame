using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    /// <summary>
    /// Used to notify about game changes
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    public delegate void GameChangedEventHandler(object sender, GameEventArgs e);
}
