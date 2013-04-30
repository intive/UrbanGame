using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace UrbanGameTests.Mocks
{
    public interface ITableMock
    {
        void SubmitAllChanges();

        void ClearTable();
    }

    public interface ITableMock<TEntity> : ITableMock
    {
        void InsertOnSubmit(TEntity record);

        void DeleteOnSubmit(TEntity record);

        IQueryable<TEntity> All();
    }
}
