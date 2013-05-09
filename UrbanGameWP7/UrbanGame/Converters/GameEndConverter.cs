using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Data;

namespace UrbanGame.Converters
{
    public class GameEndConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            return ((DateTime)value).Subtract(DateTime.Today).Days + " d " + ((DateTime)value).Subtract(DateTime.Today).Hours
                + " h " + ((DateTime)value).Subtract(DateTime.Today).Minutes+ " m to end";
        }

        public object ConvertBack(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            throw new NotImplementedException();
        }

    }
}
