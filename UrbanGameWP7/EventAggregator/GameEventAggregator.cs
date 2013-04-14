using System;
using System.Collections.Generic;
using System.Net;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;

namespace GameChangeListener
{
    public class GameEventAggregator
    {
        private List<object> listOfEvents = new List<object>();

        public void RegisterEvent<TEventData>()
        {
            if (!listOfEvents.Any(e => e is SubscriptionEvent<TEventData>))
                listOfEvents.Add(new SubscriptionEvent<TEventData>());
        }

        public SubscriptionEvent<TEventData> GetEvent<TEventData>()
        {
            SubscriptionEvent<TEventData> e =
                listOfEvents.FirstOrDefault(ev => ev is SubscriptionEvent<TEventData>) as SubscriptionEvent<TEventData>;

            if (e == null)
                throw new ArgumentOutOfRangeException("Event not found in GameEventAggregator.");
            else
                return e;
        }
    }
}
