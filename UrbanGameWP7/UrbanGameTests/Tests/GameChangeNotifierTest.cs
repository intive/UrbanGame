using Caliburn.Micro;
using Common;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using WebService;

namespace UrbanGameTests.Tests
{
    [TestClass]
    public class GameChangeNotifierTest
    {
        #region NotificationTest
        class HandlerClass : IHandle<IGame>
        {
            public bool handled = false;
            public void Handle(IGame message)
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

            IGameChangeNotifier notifier = new GameChangeNotifierMock(webService, aggregator, 1000);
            System.Threading.Thread.Sleep(1500);

            Assert.IsTrue(handler.handled);
        }
        #endregion
    }
}
