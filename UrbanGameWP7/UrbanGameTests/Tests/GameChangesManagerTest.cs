using Caliburn.Micro;
using Common;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UrbanGame.Storage;
using WebService;

namespace UrbanGameTests.Tests
{
    [TestClass]
    public class GameChangesManagerTest
    {
        #region NotificationTest
        class HandlerClass : IHandle<GameChangedEvent>
        {
            public bool handled = false;
            public void Handle(GameChangedEvent message)
            {
                handled = true;
            }
        }

        [TestMethod]
        public void NotificationTest()
        {
            IGameWebService webService = new GameWebServiceMock();
            IEventAggregator aggregator = new EventAggregator();

            HandlerClass handler = new HandlerClass();
            aggregator.Subscribe(handler);

            IGameChangesManager notifier = 
                new GameChangesManager(webService, aggregator, () => new UnitOfWork(new UrbanGameDataContext()));
            System.Threading.Thread.Sleep(6500);

            Assert.IsTrue(handler.handled);
        }
        #endregion
    }
}
