using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    public interface IGameAuthorizationService
    {
        bool PersistCredentials();
        LoginResult LogIn(string login, string pasword);
        void Logout(); // clear cached credentials
        User AuthenticatedUser{get;set;}
        bool IsUserAuthenticated();
        RegisterResult Register(User userData);
    }
}
