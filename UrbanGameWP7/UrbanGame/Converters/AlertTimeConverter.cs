using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Data;

namespace UrbanGame.Converters
{
    public class AlertTimeConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            if (value is DateTime)
            {
                DateTime alertTime = (DateTime)value;
                TimeSpan difference = DateTime.Now - alertTime;

                if (difference.Days >= 7)
                {
                    return string.Format("{0}/{1}", alertTime.Day,alertTime.Month);
                }
                else if (difference.Days >= 1)
                {
                    return string.Format("{0}", alertTime.DayOfWeek);
                }
                else
                {
                    return alertTime.ToString("t");
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
