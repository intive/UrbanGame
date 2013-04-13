using Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Data;
using UrbanGame.Localization;

namespace UrbanGame.Converters
{
    public class DifficultyConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {         
            switch ((GameDifficulty)value)
            {
                case GameDifficulty.Easy : return AppResources.Easy;
                case GameDifficulty.Medium : return AppResources.Medium;
                case GameDifficulty.Difficult : return AppResources.Hard;
                default: return AppResources.Undefinied;
            }
        }

        public object ConvertBack(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}
