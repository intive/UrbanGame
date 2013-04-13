using Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Data;
using UrbanGame.Localization;

namespace UrbanGame.Converters
{
    public class GameStartEndConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            return ((DateTime)value).ToString("MMM dd, hh:ss tt");
        }

        public object ConvertBack(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}
