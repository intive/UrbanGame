using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    public interface ICredentialsService
    {
        User AuthenticatedUser { get; set; }

        bool IsUserAuthenticated { get; }
    }
}
