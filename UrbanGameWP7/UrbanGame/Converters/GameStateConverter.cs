using Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Data;
using UrbanGame.Localization;

namespace UrbanGame.Converters
{
    public class GameStateConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            switch ((GameState)value)
            {
                case GameState.None: return AppResources.GameStateNone;
                case GameState.Joined: return AppResources.GameStateJoined;
                case GameState.Won: return AppResources.GameStateWon;
                case GameState.Lost: return AppResources.GameStateLost;
                case GameState.Inactive: return AppResources.GameStateInactive;
                default: return "";
            }
        }

        public object ConvertBack(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}