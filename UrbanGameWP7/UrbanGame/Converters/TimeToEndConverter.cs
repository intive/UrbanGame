using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Data;
using UrbanGame.Localization;

namespace UrbanGame.Converters
{
    public class TimeToEndConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            if (value is DateTime)
            {
                TimeSpan leftTime = ((DateTime)value).Subtract(DateTime.Now);

                if (leftTime.Days > 0)
                {
                    return leftTime.Days + " " + AppResources.DayShortcut + " " + leftTime.Hours + " " + AppResources.HoursShortcut;
                }
                else if (leftTime.Hours > 0)
                {
                    return leftTime.Hours + " " + AppResources.HoursShortcut + " " + leftTime.Minutes + " " + AppResources.MinutesShortcut;
                }
                else
                {
                    return leftTime.Minutes + " " + AppResources.MinutesShortcut + " " + leftTime.Seconds + " " + AppResources.SecondShortcut;
                }
            }
            else
            {
                return "";
            }
        }

        public object ConvertBack(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            throw new NotImplementedException();
        }

    }
}
