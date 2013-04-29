using Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UrbanGame.Storage;

namespace UrbanGameTests.Mocks
{
    public class UnitOfWorkMock : IUnitOfWork
    {
        private IDatabaseMock _database;

        public UnitOfWorkMock(IDatabaseMock database)
        {
            _database = database;
        }

        public IRepository<TBusinessEntity> GetRepository<TBusinessEntity>() 
            where TBusinessEntity : class
        {
            var enitityType = BusinessTypeMapper.Map<TBusinessEntity>();
            Type repositoryType = typeof(RepositoryMock<,>);
            repositoryType = repositoryType.MakeGenericType(typeof(TBusinessEntity), enitityType);
            return (IRepository<TBusinessEntity>)Activator.CreateInstance(repositoryType, _database.GetTable<TBusinessEntity>());
        }

        public void Commit()
        {
            _database.SubmitAllChanges();
        }

        public void Dispose()
        {
        }
    }
}
