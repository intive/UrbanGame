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
            if ((string)parameter == "start")
            {
                if ((DateTime)value < DateTime.Now)
                    return AppResources.Started;
            }
            else
            {
                if ((DateTime)value < DateTime.Now)
                    return AppResources.Completed;
            }

            TimeSpan timeLeft = (DateTime)value - DateTime.Now;
            if (timeLeft.Days > 0)
            {
                return timeLeft.Days + " " + (timeLeft.Days == 1 ? AppResources.Day : AppResources.Days) + " " + timeLeft.Hours + " h";
            }
            else
            {
                return timeLeft.Hours + " h " + timeLeft.Minutes + " m";
            }
        }

        public object ConvertBack(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}
