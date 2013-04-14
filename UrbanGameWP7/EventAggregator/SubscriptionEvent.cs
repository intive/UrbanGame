using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace GameChangeListener
{
    public class SubscriptionEvent<TEventData>
    {
        private event Action<TEventData> subscribers;

        public void Subscribe(Action<TEventData> action)
        {
            subscribers -= action;
            subscribers += action;
        }

        public void Unsubscribe(Action<TEventData> action)
        {
            subscribers -= action;
        }

        public void Notify(TEventData data)
        {
            subscribers(data);
        }

        public bool Contains(Action<TEventData> subscriber)
        {
            return subscribers.GetInvocationList().Contains(subscriber);
        }
    }
}
