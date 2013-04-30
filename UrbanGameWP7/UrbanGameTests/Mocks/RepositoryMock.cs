using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Common;

namespace UrbanGameTests.Mocks
{
    public class RepositoryMock<TEntityInterface, TEntity> : IRepository<TEntityInterface>
        where TEntityInterface : class
        where TEntity : class, TEntityInterface
    {
        private ITableMock<TEntityInterface> _table;

        public RepositoryMock(ITableMock<TEntityInterface> table)
        {
            _table = table;
        }

        public IQueryable<TEntityInterface> All()
        {
            return _table.All();
        }

        public IQueryable<TEntityInterface> FindAll(Func<TEntityInterface, bool> exp)
        {
            return _table.All().Where(exp).AsQueryable();
        }

        public void MarkForAdd(TEntityInterface entity)
        {
            _table.InsertOnSubmit(entity);
        }

        public void MarkForDeletion(TEntityInterface entity)
        {
            _table.DeleteOnSubmit(entity);
        }

        public TEntityInterface CreateInstance()
        {
            return Activator.CreateInstance<TEntity>();
        }

        public void Dispose()
        {
        }
    }
}
