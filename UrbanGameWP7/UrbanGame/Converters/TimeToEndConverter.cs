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
                TimeSpan leftTime = (DateTime)value - DateTime.Now;

                if (leftTime.Days > 0)
                {
                    return string.Format("{0} d {1} h", leftTime.Days, leftTime.Hours);
                }
                else if (leftTime.Hours > 0)
                {
                    return string.Format("{0} h {1} m", leftTime.Hours, leftTime.Minutes);
                }
                else
                {
                    return string.Format("{0} m {1} s", leftTime.Minutes, leftTime.Seconds);
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
