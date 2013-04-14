using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Common;

namespace WebService.BOMock
{
    public class TaskMock : BOBase, ITask
    {

        public int Id
        {
            get;
            set;
        }
    }
}
