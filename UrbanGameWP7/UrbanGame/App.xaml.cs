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
using System.Windows.Navigation;
using System.Windows.Shapes;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using Common;
using Caliburn.Micro;

namespace UrbanGame
{
    public partial class App : Application
    {
        /// <summary>
        /// Constructor for the Application object.
        /// </summary>
        public App()
        {
            // Standard Silverlight initialization
            InitializeComponent();
            #if AUTOMATION
            WindowsPhoneTestFramework.Client.AutomationClient.Automation.Instance.Initialise();
            #endif    
            this.Startup += App_Startup;
        }

        void App_Startup(object sender, StartupEventArgs e)
        {
            this.Startup -= App_Startup;
            App.GameChangesManager = IoC.Get<IGameChangesManager>();
        }

        

        public static IGameChangesManager GameChangesManager;
    }
}