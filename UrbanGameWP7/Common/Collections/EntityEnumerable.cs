using System;
using System.Collections.Generic;
using System.Data.Linq;
using System.Linq;
using System.Text;

namespace Common
{
    public class EntityEnumerable<TEntityInterface, TEntity> : IEntityEnumerable<TEntityInterface>
        where TEntityInterface : class
        where TEntity : class, TEntityInterface
    {
        private IEnumerable<TEntityInterface> _collection;
        private EntitySet<TEntity> _entitySet;

        public EntityEnumerable(EntitySet<TEntity> entitySet)
        {
            _collection = entitySet.Cast<TEntityInterface>();
            _entitySet = entitySet;
        }

        public void Add(TEntityInterface element)
        {
            _entitySet.Add((TEntity)element);
        }

        public bool Remove(TEntityInterface element)
        {
            return _entitySet.Remove((TEntity)element);
        }

        public IEnumerator<TEntityInterface> GetEnumerator()
        {
            return _collection.GetEnumerator();
        }

        System.Collections.IEnumerator System.Collections.IEnumerable.GetEnumerator()
        {
            return _collection.GetEnumerator();
        }
    }
}
