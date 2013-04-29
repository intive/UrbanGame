using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace UrbanGameTests.Mocks
{
    public class DatabaseMock : IDatabaseMock
    {
        private List<ITableMock> _tables = new List<ITableMock>();

        public ITableMock<TEntity> GetTable<TEntity>()
            where TEntity : class
        {
            ITableMock<TEntity> table = (ITableMock<TEntity>)_tables.FirstOrDefault(t => t is ITableMock<TEntity>);

            if (table != null)
                return table;
            else
            {
                ITableMock<TEntity> newTable = new TableMock<TEntity>();
                _tables.Add(newTable);
                return newTable;
            }
        }

        public void SubmitAllChanges()
        {
            foreach (ITableMock table in _tables)
                table.SubmitAllChanges();
        }
    }
}
