using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    /// <summary>
    /// Unit of work allows to save multipile data persist operation in one transaction-like opeartion and abstract for underling implementation
    /// </summary>
    public interface IUnitOfWork : IDisposable
    {
        /// <summary>
        /// Return repository that is attached to this unit of work.
        /// all operations performed on this repository will be saved
        /// when this unit of work is comitted
        /// </summary>
        /// <typeparam name="T">Business entity type</typeparam>
        /// <returns>Return repository that is attached to this unit of work.</returns>
        IRepository<T> GetRepository<T>() where T : class;

        /// <summary>
        /// Saves changes from all repositories registered within this UnitOfWork
        /// </summary>
        void Commit();
    }
}
