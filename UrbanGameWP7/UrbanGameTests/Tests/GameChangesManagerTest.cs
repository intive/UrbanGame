using Caliburn.Micro;
using Common;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UrbanGame.Localization;
using UrbanGame.Storage;
using UrbanGame.Utilities;
using UrbanGameTests.Mocks;
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

            IDatabaseMock database = new DatabaseMock();
            IGameChangesManager notifier = new GameChangesManager(webService, aggregator, () => new UnitOfWorkMock(database), new LocalizationService());
            System.Threading.Thread.Sleep(8000);

            Assert.IsTrue(handler.handled);
        }
        #endregion
    }
}
