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
    public class NewTaskToBackgroundConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            ITask task = value as ITask;

            if (task.IsNewTask)
                return new SolidColorBrush(Color.FromArgb(0x38, 255, 255, 255));
            else
                return new SolidColorBrush(Colors.Transparent);
        }

        public object ConvertBack(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}
