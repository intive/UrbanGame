using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    public interface IEntityEnumerable<T> : IEnumerable<T> where T : class
    {
        void Add(T element);
        bool Remove(T element);
    }
}
