using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Common;
using Caliburn.Micro;

namespace UrbanGame.ViewModels
{
    public class ReportTaskViewModel :BaseViewModel
    {
        public ReportTaskViewModel(INavigationService navigationService, Func<IUnitOfWork> unitOfWorkLocator,
                                    IGameWebService gameWebService, IEventAggregator gameEventAggregator, IAppbarManager appbarManager, IGameAuthorizationService authorizationService)
            : base(navigationService, unitOfWorkLocator, gameWebService, gameEventAggregator, authorizationService)
        {
        }

        #region navigation properties

        public int GameId { get; set; }

        public int TaskId { get; set; }

        #endregion

        #region bindable properties

        private string _message;

        public string Message
        {
            get
            {
                return _message;
            }
            set
            {
                if (_message != value)
                {
                    _message = value;
                    NotifyOfPropertyChange(() => Message);
                }
            }
        }

        #endregion

        #region operations

        public void Report()
        {
            // todo: report task in gameWebService

            //_gameWebService.Report(GameId, TaskId, Message);
        }

        #endregion
    }
}
