using System;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;

namespace UrbanGame.Utilities
{
    public class PhoneTextBox : TextBox
    {
        public PhoneTextBox()
        {
            this.KeyUp += new KeyEventHandler(SearchTextBox_KeyUp);
        }

        void SearchTextBox_KeyUp(object sender, KeyEventArgs e)
        {
            var binding = this.GetBindingExpression(TextBox.TextProperty);
            if (binding != null)
            {
                binding.UpdateSource();
            }
        }
    }
}
