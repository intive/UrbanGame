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
using Caliburn.Micro;
using UrbanGame.Localization;
using Microsoft.Phone.Shell;

namespace UrbanGame.Views
{
    public partial class GameDetailsView : PhoneApplicationPage
    {
        public GameDetailsView()
        {
            InitializeComponent();
            Loaded += GameDetailsView_Loaded;
        }

        void GameDetailsView_Loaded(object sender, RoutedEventArgs e)
        {
            BuildLocalizedApplicationBar();
        }

        private void BuildLocalizedApplicationBar()
        {
            ApplicationBar = new ApplicationBar();
            ApplicationBar.Buttons.Add(new AppBarButton()
                                        {
                                            IconUri = new Uri("/Images/appbar.check.png", UriKind.Relative),
                                            Text = AppResources.Play,
                                            Message = "JoinGame()"
                                        });
        }                
    }
}