using Caliburn.Micro;
using Coding4Fun.Toolkit.Controls;
using Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Media;
using UrbanGame.ViewModels;
using UrbanGame.Views;

namespace UrbanGame.Utilities
{
    public class ToastPromptService : IToastPromptService
    {
        INavigationService _navigationService;

        public ToastPromptService(INavigationService navigationService)
        {
            _navigationService = navigationService;
        }

        protected void ShowToast(string title, string text, int timeout, EventHandler<System.Windows.Input.GestureEventArgs> tapAction)
        {
            System.Windows.Deployment.Current.Dispatcher.BeginInvoke(() =>
            {
                var toast = new ToastPrompt()
                {
                    Title = title,
                    Background = new SolidColorBrush(Colors.Green),
                    TextWrapping = System.Windows.TextWrapping.Wrap,
                    MillisecondsUntilHidden = timeout,
                    Message = text
                };

                toast.Tap += tapAction;
                toast.Show();
            });
        }

        public string GetDifferencesText(IList<string> diff)
        {
            string str = String.Concat(diff.Select(d => d + ", "));

            return "Changes: " + str.Substring(0, str.Length - 2); 
        }

        public void ShowGameChanged(int gameId, string title, string text, IList<string> diff)
        {
            System.Windows.Deployment.Current.Dispatcher.BeginInvoke(() =>
            {
                string navURI = _navigationService.UriFor<GameDetailsViewModel>().WithParam(vm => vm.GameId, gameId).BuildUri().OriginalString;
                bool currentPage = _navigationService.CurrentSource.OriginalString == navURI;

                if (!currentPage)
                    ShowToast(title, text, 7000, (s, e) => _navigationService.UriFor<GameDetailsViewModel>().WithParam(vm => vm.GameId, gameId).Navigate());   
            });
        }

        public void ShowSolutionStatusChanged(int taskId, string title, string text)
        {
            ShowToast(title, text, 7000, (s, e) => 
                {
                    _navigationService.UriFor<TaskViewModel>().WithParam(vm => vm.TaskId, taskId).Navigate();
                });
        }
    }
}
