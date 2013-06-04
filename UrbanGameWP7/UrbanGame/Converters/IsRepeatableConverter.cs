using System;
using System.Windows;
using System.Windows.Data;
using UrbanGame.Localization;

namespace UrbanGame.Converters
{
    public class IsRepeatableConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            if ((bool)value)
            {
                return AppResources.Repeatable;
            }
            else
            {
                return AppResources.NotRepeatable;
            }
        }

        public object ConvertBack(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}
