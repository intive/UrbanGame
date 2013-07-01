using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Common;

namespace UrbanGame.Storage
{

    /// <summary>
    /// Linq to sql specific implementation of UnitOfWork
    /// </summary>
    public class UnitOfWork : IUnitOfWork
    {
        UrbanGameDataContext _dataContext;

        public UnitOfWork(UrbanGameDataContext dataContext)
        {
            _dataContext = dataContext;
        }
        public IRepository<TBusinessEntity> GetRepository<TBusinessEntity>()
            where TBusinessEntity : class
        {
            var enitityType = BusinessTypeMapper.Map<TBusinessEntity>();
            Type repositoryType = typeof(Repository<,>);
            repositoryType = repositoryType.MakeGenericType(typeof(TBusinessEntity), enitityType);
            return (IRepository<TBusinessEntity>)Activator.CreateInstance(repositoryType, _dataContext);
        }

        public void Commit()
        {
            _dataContext.SubmitChanges();
        }

        public void Dispose()
        {
            _dataContext.Dispose();
        }

        public void DeleteDatabase()
        {
            _dataContext.DeleteDatabase();
        }

        public void CreateDatabase()
        {
            _dataContext.CreateDatabase();
        }
    }
}