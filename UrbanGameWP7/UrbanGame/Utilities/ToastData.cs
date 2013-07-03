using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace UrbanGame.Utilities
{
    public class ToastData
    {
        public int Id { get; set; }

        public ToastType Type { get; set; }

        public string Title { get; set; }

        public string Text { get; set; }

        public int Timeout { get; set; }

        public EventHandler<System.Windows.Input.GestureEventArgs> Tap { get; set; }
    }
}
