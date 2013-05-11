using Caliburn.Micro;
using Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using UrbanGame.Storage;
using System.Threading.Tasks;

namespace UrbanGame.ViewModels
{
    public class TaskViewModel : BaseViewModel, IHandle<GameChangedEvent>
    {

        IAppbarManager _appbarManager;
        public TaskViewModel(INavigationService navigationService, Func<IUnitOfWork> unitOfWorkLocator,
                                    IGameWebService gameWebService, IEventAggregator gameEventAggregator, IAppbarManager appbarManager)
            : base(navigationService, unitOfWorkLocator, gameWebService, gameEventAggregator)
        {
            _appbarManager = appbarManager;
        }

        protected override void OnViewReady(object view)
        {
            SetAppBarContent();
        }

        #region appbar configurations

        #endregion

        #region appbar

        public void RefreshMenuItemText()
        {

        }

        private void SetAppBarContent()
        {
            Deployment.Current.Dispatcher.BeginInvoke(() =>
            {
                RefreshMenuItemText();
            });
        }

        #endregion

        #region IHandle<GameChangedEvent>
        public void Handle(GameChangedEvent game)
        {
            if (game.Id == GameId)
            {
                RefreshGame();
                RefreshTask();
            }
        }
        #endregion

        #region IHandle<GameChangedEvent>
        public void Handle(TaskChangedEvent task)
        {
            if (task.Id == TaskId)
            {
                RefreshTask();
            }
        }
        #endregion

        #region navigation properties

        public int GameId { get; set; }

        public int TaskId { get; set; }

        #endregion

        #region bindable properties

        #region Game

        private IGame _game;

        public IGame Game
        {
            get
            {
                return _game;
            }
            set
            {
                if (_game != value)
                {
                    _game = value;
                    NotifyOfPropertyChange(() => Game);
                }
            }
        }
        #endregion

        #region CurrentTask

        private ITask _currentTask;

        public ITask CurrentTask
        {
            get
            {
                return _currentTask;
            }
            set
            {
                if (_currentTask != value)
                {
                    _currentTask = value;
                    NotifyOfPropertyChange(() => CurrentTask);
                }
            }
        }
        #endregion

        #endregion

        #region lifecycle

        protected override void OnCreated()
        {
            base.OnCreated();
        }

        protected async override void OnActivate()
        {
            base.OnActivate();
            await RefreshGame();
            await RefreshTask();

        }

        #endregion

        #region operations

        public async Task RefreshGame()
        {
            await Task.Factory.StartNew(() =>
            {
                if (_gameWebService.IsAuthorized)
                {
                    IQueryable<IGame> games = _unitOfWorkLocator().GetRepository<IGame>().All();
                    Game = games.FirstOrDefault(g => g.Id == GameId) ?? _gameWebService.GetGameInfo(GameId);
                }
                else
                {
                    Game = _gameWebService.GetGameInfo(GameId);
                }
            });
        }

        public async Task RefreshTask()
        {
            await Task.Factory.StartNew(() =>
            {
                if (_gameWebService.IsAuthorized)
                {
                    IQueryable<ITask> tasks = _unitOfWorkLocator().GetRepository<ITask>().All();
                    CurrentTask = tasks.FirstOrDefault(t => t.Id == TaskId) ?? _gameWebService.GetTaskDetails(GameId, TaskId);
                }
                else
                {
                    CurrentTask = _gameWebService.GetTaskDetails(GameId, TaskId);
                }
            });
        }

        public async Task SubmitGPS()
        {
            await Task.Factory.StartNew(() =>
            {

            });
        }

        public async Task SubmitABCD()
        {
            await Task.Factory.StartNew(() =>
            {

            });
        }

        #endregion
    }
}
