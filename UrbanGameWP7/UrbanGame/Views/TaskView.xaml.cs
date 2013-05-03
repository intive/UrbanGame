using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using Microsoft.Phone.Controls;

namespace UrbanGame.Views
{
    public partial class TaskView : PhoneApplicationPage
    {
        public TaskView()
        {
            InitializeComponent();
        }

        private void CheckBox_KeyDown_1(object sender, KeyEventArgs e)
        {
            var checkBox = (CheckBox)sender;
            checkBox.Background = new SolidColorBrush(Color.FromArgb(0, 128, 128, 0));
        }

        private void CheckBox_KeyUp_1(object sender, KeyEventArgs e)
        {
            var checkBox = (CheckBox)sender;
            checkBox.Background = new SolidColorBrush(Color.FromArgb(255, 0, 0, 0));
        }
    }
}