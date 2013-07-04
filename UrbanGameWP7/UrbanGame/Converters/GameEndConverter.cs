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
            if (value is DateTime)
            {
                TimeSpan time = (DateTime)value - DateTime.Now;
                if (time.Days > 0)
                {
                    return string.Format("{0} d {1} h {2} m " + AppResources.ToEndLowCase, time.Days, time.Hours, time.Minutes);
                }
                else if (time.Hours > 0)
                {
                    return string.Format("{0} h {1} m " + AppResources.ToEndLowCase, time.Hours, time.Minutes);
                }
                else if (time.Minutes > 0)
                {
                    return string.Format("{0} m " + AppResources.ToEndLowCase, time.Minutes);
                }
                else
                {
                    return AppResources.BelowMinute + " ";
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
