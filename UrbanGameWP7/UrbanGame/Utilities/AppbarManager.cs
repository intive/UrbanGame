using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Windows;
using System.Windows.Interactivity;
using Caliburn.Micro;
using Common;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
namespace UrbanGame.Utilities
{
    public class AppbarManager : IAppbarManager
    {
        private PhoneApplicationPage GetPage()
        {
            return ((PhoneApplicationFrame)(App.Current.RootVisual)).Content as PhoneApplicationPage;
        }
        private IApplicationBar GetAppBar()
        {

            return GetPage().ApplicationBar;
        }

        public void ConfigureAppbar(List<AppbarItem> appbarItems)
        {
            var applicationBar = GetAppBar();
           applicationBar.Buttons.Clear();
           applicationBar.MenuItems.Clear();
           if (applicationBar.IsVisible == false)
           {
               applicationBar.IsVisible = true;
           }
           foreach (var appBarItem in appbarItems)
           {
               if (appBarItem.IconUri == null)
               {
                   var applicationBarMenu = new AppBarMenuItem();
                   applicationBarMenu.Message = appBarItem.Message;
                   applicationBarMenu.Text = appBarItem.Text;

                   applicationBar.MenuItems.Add(applicationBarMenu);
               }
               else
               {
                   var applicationBarButton = new AppBarButton();
                   applicationBarButton.Message = appBarItem.Message;
                   applicationBarButton.Text = appBarItem.Text;
                   applicationBarButton.IconUri = appBarItem.IconUri;

                   applicationBar.Buttons.Add(applicationBarButton);
               }
           }
           ViewModelBinder.BindAppBar(GetPage());
        }

        public void HideAppbar()
        {
            GetAppBar().IsVisible = false;
        }

        public void ShowAppbar()
        {
            GetAppBar().IsVisible = true;
        }


        public void ChangeItemText(string itemMessage, string newText)
        {
            foreach (AppBarMenuItem menuItem in GetAppBar().MenuItems)
            {
                if (menuItem.Message == itemMessage)
                {
                    menuItem.Text = newText;
                    return;
                }
            }
            foreach (AppBarButton menuItem in GetAppBar().Buttons)
            {
                if (menuItem.Message == itemMessage)
                {
                    menuItem.Text = newText;
                    return;
                }
            }
        }
    }
}
