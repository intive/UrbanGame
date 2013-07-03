using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Data;

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
                    return leftTime.Days + " d " + leftTime.Hours + " h";
                }
                else if (leftTime.Hours > 0)
                {
                    return leftTime.Hours + " h " + leftTime.Minutes + " m";
                }
                else
                {
                    return leftTime.Minutes + " m " + leftTime.Seconds + " s";
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
