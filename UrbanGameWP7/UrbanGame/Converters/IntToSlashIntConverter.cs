using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Data;

namespace UrbanGame.Converters
{
    public class IntToSlashIntConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            if (value is Int32)
            {
                if ((Int32)value == -1)
                {
                    return "";
                }
                else
                {
                    if ((string)parameter == "false")
                        return value;
                    else
                        return "/" + value;
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
