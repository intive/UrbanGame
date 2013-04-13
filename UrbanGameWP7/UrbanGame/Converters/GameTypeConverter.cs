using Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Data;
using UrbanGame.Localization;

namespace UrbanGame.Converters
{
    public class GameTypeConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {         
            switch ((GameType)value)
            {
                case GameType.Quiz: return AppResources.GameTypeQuiz;
                case GameType.Race: return AppResources.GameTypeRace;
                default: return "";
            }
        }

        public object ConvertBack(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}
