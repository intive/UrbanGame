using Caliburn.Micro;
using Coding4Fun.Toolkit.Controls;
using Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Windows;
using System.Windows.Media;
using UrbanGame.ViewModels;
using UrbanGame.Views;

namespace UrbanGame.Utilities
{
    public class ToastPromptService : IToastPromptService
    {
        INavigationService _navigationService;
        Queue<ToastData> _toastQueue = new Queue<ToastData>();
        Timer _queueTimer;

        public ToastPromptService(INavigationService navigationService)
        {
            _navigationService = navigationService;
            _queueTimer = new Timer(new TimerCallback(CheckQueue), null, 2000, 5000);
        }

        public string GetDifferencesText(IList<string> diff)
        {
            string str = String.Concat(diff.Select(d => d + ", "));

            return str.Substring(0, str.Length - 2);
        }

        protected void ShowToast(ToastData toast)
        {
            System.Windows.Deployment.Current.Dispatcher.BeginInvoke(() =>
            {
                var t = new ToastPrompt()
                {
                    Title = toast.Title,
                    Background = new SolidColorBrush(Color.FromArgb(255, 0x97, 0xcb, 0x16)),
                    TextWrapping = System.Windows.TextWrapping.Wrap,
                    MillisecondsUntilHidden = toast.Timeout,
                    Message = toast.Text
                };

                t.Tap += toast.Tap;
                t.Show();
            });
        }

        private void CheckQueue(object obj)
        {
            if (_toastQueue.Count > 0)
                ShowToast(_toastQueue.Dequeue());           
        }

        protected void AddToQueue(int id, ToastType type, string title, string text, int timeout, EventHandler<System.Windows.Input.GestureEventArgs> tap)
        {
            var oldToast = _toastQueue.FirstOrDefault(t => t.Id == id && t.Type == type);
            if (oldToast != null)
            {
                oldToast.Text = text;
                oldToast.Title = title;
                oldToast.Tap = tap;
                oldToast.Timeout = timeout;
            }
            else
                _toastQueue.Enqueue(new ToastData() { Id = id, Type = type, Title = title, Text = text, Timeout = timeout, Tap = tap });
        }

        public void ShowGameChanged(int gameId, string title, string text)
        {
            System.Windows.Deployment.Current.Dispatcher.BeginInvoke(() =>
            {
                string navURI = _navigationService.UriFor<GameDetailsViewModel>().WithParam(vm => vm.GameId, gameId).BuildUri().OriginalString;
                bool currentPage = _navigationService.CurrentSource.OriginalString == navURI;

                if (!currentPage)
                    AddToQueue(gameId, ToastType.Game, title, text, 5000, (s, e) => _navigationService.UriFor<GameDetailsViewModel>().WithParam(vm => vm.GameId, gameId).Navigate());   
            });
        }

        public void ShowTaskChanged(int gameId, int taskId, string title, string text)
        {
            System.Windows.Deployment.Current.Dispatcher.BeginInvoke(() =>
            {
                string navURI = _navigationService.UriFor<TaskViewModel>()
                                    .WithParam(vm => vm.TaskId, taskId)
                                    .WithParam(vm => vm.GameId, gameId)
                                    .BuildUri().OriginalString;
                bool currentPage = _navigationService.CurrentSource.OriginalString == navURI;

                if (!currentPage)
                    AddToQueue(taskId, ToastType.Task, title, text, 5000,
                                (s, e) => _navigationService.UriFor<TaskViewModel>()
                                            .WithParam(vm => vm.TaskId, taskId)
                                            .WithParam(vm => vm.GameId, gameId)
                                            .Navigate());
            });
        }

        public void ShowSolutionStatusChanged(int taskId, int gameId, string title, string text)
        {
            AddToQueue(taskId, ToastType.Solution, title, text, 5000, (s, e) => 
                {
                    _navigationService.UriFor<TaskViewModel>().WithParam(vm => vm.TaskId, taskId).WithParam(vm => vm.GameId, gameId).Navigate();
                });
        }
    }
}
