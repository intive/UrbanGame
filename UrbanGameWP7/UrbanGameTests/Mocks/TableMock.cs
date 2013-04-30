using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace UrbanGameTests.Mocks
{
    public class TableMock<TEntity> : ITableMock, ITableMock<TEntity>
        where TEntity : class
    {
        private List<TEntity> _table = new List<TEntity>();
        private List<TEntity> _toInsert = new List<TEntity>();
        private List<TEntity> _toRemove = new List<TEntity>();

        public IQueryable<TEntity> All()
        {
            return _table.AsQueryable();
        }

        public void InsertOnSubmit(TEntity record)
        {
            _toInsert.Add(record);
        }

        public void DeleteOnSubmit(TEntity record)
        {
            _toRemove.Add(record);
        }

        public void SubmitAllChanges()
        {
            _table.AddRange(_toInsert);
            _toInsert.Clear();

            foreach (var record in _toRemove)
                _table.Remove(record);
            _toRemove.Clear();
        }

        public void ClearTable()
        {
            _table.Clear();
            _toInsert.Clear();
            _toRemove.Clear();
        }
    }
}
