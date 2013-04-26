using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Reflection;

namespace UrbanGame.Storage
{
    /// <summary>
    /// Mapper is responsible for finding entity type that implement given business interface
    /// </summary>
    public class BusinessTypeMapper
    {
        public static Type Map<TBusinessType>()
        {
            var enitityType = Assembly.GetExecutingAssembly().GetTypes().Where(x => x.GetInterfaces().Contains(typeof(TBusinessType))).FirstOrDefault();
            if (enitityType != null)
            {
                return enitityType;
            }
            else
            {
                throw new ArgumentException("There is no entity for business type " + typeof(TBusinessType).Name);
            }
        }
    }
}
