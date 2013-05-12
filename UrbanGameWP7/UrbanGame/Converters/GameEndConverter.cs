using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Data;
using UrbanGame.Localization;

namespace UrbanGame.Converters
{
    public class GameEndConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            return ((DateTime)value).Subtract(DateTime.Today).Days + " " + AppResources.DayShortcut + " " + ((DateTime)value).Subtract(DateTime.Today).Hours
                + " " + AppResources.HoursShortcut + " " + ((DateTime)value).Subtract(DateTime.Today).Minutes + " " + AppResources.MinutesShortcut + " " + AppResources.ToEndLowCase;
        }

        public object ConvertBack(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            throw new NotImplementedException();
        }

    }
}
