using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    /// <summary>
    /// Unified contract for accesing and persising data
    /// </summary>
    /// <typeparam name="T"></typeparam>
    public interface IRepository<TBussinesType> : IDisposable
        where TBussinesType : class
    {

        /// <summary>
        /// Return query for all instances of type T.
        /// </summary>
        /// <returns></returns>
        IQueryable<TBussinesType> All();

        /// <summary>
        /// Return query for all instances of type T that match the expression exp.
        /// </summary>
        /// <param name="exp"></param>
        /// <returns></returns>
        IQueryable<TBussinesType> FindAll(Func<TBussinesType, bool> exp);

        /// <summary>
        /// Mark an entity to be deleted when the unit of work is saved.
        /// </summary>
        /// <param name="entity"></param>
        void MarkForDeletion(TBussinesType entity);

        /// <summary>
        /// Mark an entity to be added when the unit of work is saved.
        /// </summary>
        /// <param name="entity"></param>
        void MarkForAdd(TBussinesType entity);

        /// <summary>
        /// Create a new instance of type T.
        /// </summary>
        /// <returns></returns>
        TBussinesType CreateInstance();

    }
}
