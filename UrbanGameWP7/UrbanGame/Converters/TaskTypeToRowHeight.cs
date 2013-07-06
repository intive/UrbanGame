using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Data;
using Common;
using System.Windows;

namespace UrbanGame.Converters
{
    public class TaskTypeToRowHeight : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            if ((TaskType)value == TaskType.ABCD)
            {
                return "*";
            }
            else
            {
                return "Auto";
            }
        }

        public object ConvertBack(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}
