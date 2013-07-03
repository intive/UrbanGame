using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Common;
using System.Windows.Data;

namespace UrbanGame.Converters
{
    public class TypeToImageSourceConverter :IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            if (value is ITask)
            {
                ITask task = (ITask)value;
                if (task.IsRepeatable)
                {
                    switch (task.Type)
                    {
                        case TaskType.GPS: return "/Images/TaskIconGPSRepeat.png";
                        case TaskType.ABCD: return "/Images/TaskIconQuizRepeat.png";
                        case TaskType.OpenQuestion: return "";
                        case TaskType.Photo: return "";
                        case TaskType.QRCode: return "";
                        default: return "";
                    }
                }
                else
                {
                    switch (task.Type)
                    {
                        case TaskType.GPS: return "/Images/TaskIconGPS.png";
                        case TaskType.ABCD: return "/Images/TaskIconQuiz.png";
                        case TaskType.OpenQuestion: return "";
                        case TaskType.Photo: return "";
                        case TaskType.QRCode: return "";
                        default: return "";
                    }
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
