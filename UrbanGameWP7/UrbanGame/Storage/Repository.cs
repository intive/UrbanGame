using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data.Linq;
using Common;

namespace UrbanGame.Common
{
    /// <summary>
    /// Generic repository implementation consuming Linq to sql 
    /// </summary>
    public class Repository<TBusinessType, TEntity> : IRepository<TBusinessType>
        where TBusinessType : class
        where TEntity : class,TBusinessType
    {
        protected UrbanGameDataContext _context;
        public Repository(UrbanGameDataContext context)
        {
            _context = context;
        }

        private Table<TEntity> GetEntityTable()
        {
            return _context.GetTable<TEntity>();
        }

        public IQueryable<TBusinessType> All()
        {
            return GetEntityTable().Cast<TBusinessType>();
        }

        public IQueryable<TBusinessType> FindAll(Func<TBusinessType, bool> exp)
        {
            return All().Where(x => exp(x));
        }

        public void MarkForDeletion(TBusinessType entity)
        {
            if (entity is TEntity)
            {
                GetEntityTable().DeleteOnSubmit((TEntity)entity);
            }
            else
            {
                throw new ArgumentException("Passed entity has not been created using repository!");
            }
        }

        public void MarkForAdd(TBusinessType entity)
        {
            if (entity is TEntity)
            {
                GetEntityTable().InsertOnSubmit((TEntity)entity);
            }
            else
            {
                throw new ArgumentException("Passed entity has not been created using repository!");
            }
        }

        public TBusinessType CreateInstance()
        {
            return Activator.CreateInstance<TEntity>();
        }

        public void Dispose()
        {
            _context.Dispose();
        }
    }
}

