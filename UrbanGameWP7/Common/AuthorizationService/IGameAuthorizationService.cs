using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Common
{
    public interface IGameAuthorizationService
    {
        /// <summary>
        /// Checks if credentials are saved in memory
        /// </summary>
        /// <returns>Checking result</returns>
        Task LoadUserData();

        /// <summary>
        /// Checks if user is registered and logs in
        /// </summary>
        /// <param name="email">Provided email</param>
        /// <param name="pasword">Provided password</param>
        /// <returns></returns>
        LoginResult LogIn(string email, string pasword);

        /// <summary>
        /// Clears saved credentials
        /// </summary>
        void Logout(); 

        /// <summary>
        /// User logged into game
        /// </summary>
        User AuthenticatedUser{get;set;}

        /// <summary>
        /// Checks if user is authenticated
        /// </summary>
        /// <returns>Checking result</returns>
        bool IsUserAuthenticated();

        /// <summary>
        /// Sends the data of new user to webservice
        /// </summary>
        /// <param name="userData">Provided user data</param>
        /// <returns>Webservice response</returns>
        RegisterResult Register(User userData);
    }
}
    