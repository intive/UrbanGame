using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Data;
using UrbanGame.Localization;

namespace UrbanGame.Converters
{
    public class TimeLeftConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            if ((DateTime)value < DateTime.Now)
                return AppResources.Completed;

            TimeSpan timeLeft = (DateTime)value - DateTime.Now;
            if (timeLeft.Days > 0)
            {
                return timeLeft.Days + " " + AppResources.Days + " " + timeLeft.Hours + "h";
            }
            else
            {
                return timeLeft.Hours + ":" + timeLeft.Minutes;
            }
        }

        public object ConvertBack(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}
