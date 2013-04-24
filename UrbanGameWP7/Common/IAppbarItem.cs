using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    public class AppbarItem
    {


        /// <summary>
        /// Gets or sets the message.
        /// </summary>
        /// <value>
        /// The message.
        /// </value>
        public string Message { get; set; }

        /// <summary>
        /// Gets or sets the button text.
        /// </summary>
        /// <value>
        /// The text.
        /// </value>
        public string Text { get; set; }

        /// <summary>
        /// Gets or sets the icon <see cref="Uri"/>.
        /// </summary>
        /// <value>
        /// The icon <see cref="Uri"/>.
        /// </value>
        public Uri IconUri { get; set; }
    }
}
