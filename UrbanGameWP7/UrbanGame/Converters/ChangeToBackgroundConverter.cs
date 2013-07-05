using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Data;
using Common;
using System.Windows;
using System.Windows.Media;

namespace UrbanGame.Converters
{
    public class ChangeToBackgroundConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            string listOfChanges = value as string;

            if (String.IsNullOrEmpty(listOfChanges))
                return new SolidColorBrush(Colors.Transparent);

            var names = (parameter as string).Split('|');           
            foreach(var name in names)
                if (listOfChanges.Contains(name))
                    return new SolidColorBrush(Color.FromArgb(0x38, 255, 255, 255));

           return new SolidColorBrush(Colors.Transparent);
        }

        public object ConvertBack(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}
