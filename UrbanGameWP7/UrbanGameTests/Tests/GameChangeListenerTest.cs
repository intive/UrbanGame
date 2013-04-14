using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using GameChangeListener;

namespace UrbanGameTests.Tests
{
    [TestClass]
    public class GameChangeListenerTest
    {
        #region GetEventAggregator
        private GameEventAggregator GetEventAggregator()
        {
            return new GameEventAggregator();
        }
        #endregion

        #region NotifyTest
        [TestMethod]
        public void NotifyTest()
        {
            GameEventAggregator eventAggregator = GetEventAggregator();

            eventAggregator.RegisterEvent<string>();
            eventAggregator.RegisterEvent<int>();

            //subscriber part
            string result_s = "";
            int result_i = 0;
            eventAggregator.GetEvent<string>().Subscribe(s => result_s = s);
            eventAggregator.GetEvent<int>().Subscribe(i => result_i = i);

            //notifier part
            eventAggregator.GetEvent<string>().Notify("data");
            Assert.AreEqual("data", result_s);
            eventAggregator.GetEvent<int>().Notify(99);
            Assert.AreEqual("data", result_s); //result_s shuldn't change
            Assert.AreEqual(99, result_i);
            
        }
        #endregion

        #region EventRegisterTest
        [TestMethod]
        public void EventRegisterTest()
        {
            GameEventAggregator eventAggregator = GetEventAggregator();
            eventAggregator.RegisterEvent<string>();
            eventAggregator.RegisterEvent<int>();

            Assert.IsInstanceOfType(eventAggregator.GetEvent<string>(), typeof(SubscriptionEvent<string>));
            Assert.IsInstanceOfType(eventAggregator.GetEvent<int>(), typeof(SubscriptionEvent<int>));
        }
        #endregion

        #region NotRegisteredEventTest
        [TestMethod]
        [ExpectedException(typeof(ArgumentOutOfRangeException))]
        public void NotRegisteredEventTest()
        {
            GameEventAggregator eventAggregator = GetEventAggregator();
            eventAggregator.RegisterEvent<string>();

            eventAggregator.GetEvent<int>();
        }
        #endregion
    }
}
