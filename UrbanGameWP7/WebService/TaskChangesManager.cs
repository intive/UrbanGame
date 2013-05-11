using System;
using System.Collections.Generic;
using System.Linq;
using System.Data.Linq;
using System.Text;
using Common;
using Caliburn.Micro;
using System.Threading.Tasks;
using System.Threading;

namespace WebService
{
    public class TaskChangesManager : ITaskChangesManager
    {
        IGameWebService _gameWebService;
        IEventAggregator _gameEventAggregator;
        Func<IUnitOfWork> _unitOfWorkLocator;

        public TaskChangesManager(IGameWebService gameWebService, IEventAggregator gameEventAggregator,
                                  Func<IUnitOfWork> unitOfWorkLocator)
        {
            _gameWebService = gameWebService;
            _gameEventAggregator = gameEventAggregator;
            _unitOfWorkLocator = unitOfWorkLocator;
        }

        public void TaskChanged(int tid)
        {

            Task.Factory.StartNew(() =>
            {
                ///
            });

        }
    }
}