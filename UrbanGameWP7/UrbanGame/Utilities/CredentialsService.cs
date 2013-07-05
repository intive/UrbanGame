using Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace UrbanGame.Utilities
{
    public class CredentialsService : ICredentialsService
    {
        public User AuthenticatedUser { get; set; }

        public bool IsUserAuthenticated
        {
            get
            {
                return AuthenticatedUser != null;
            }
        }
    }
}
