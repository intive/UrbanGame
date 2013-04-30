using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace UrbanGameTests.Mocks
{
    public interface IDatabaseMock
    {
        ITableMock<TEntity> GetTable<TEntity>() where TEntity : class;

        void SubmitAllChanges();
    }
}
