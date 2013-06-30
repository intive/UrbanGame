using System;
using System.Linq;
using System.Text;
using Common;
using System.IO;
using System.Xml.Linq;
using System.Collections.Generic;
using System.IO.IsolatedStorage;
using System.Security.Cryptography;
using System.Threading.Tasks;
using System.Windows;
using WebService;
using Caliburn.Micro;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using UrbanGame.Storage;

namespace UrbanGame.Utilities
{
    public class GameAuthorizationService : EntityBase, IGameAuthorizationService
    {
        protected string fileName = "data-file.xml";
        IGameWebService _gameWebService;
        IUnitOfWork _unitOfWorkLocator;

        public GameAuthorizationService(IUnitOfWork unitOfWorkLocator, IGameWebService gameWebService)
        {
            _gameWebService = gameWebService;
            _unitOfWorkLocator = unitOfWorkLocator;
        }

        public bool PersistCredentials()
        {
            AuthenticatedUser = null;
            using (var store = IsolatedStorageFile.GetUserStoreForApplication())
            {
                if(store.FileExists(fileName))
                {
                    using (var stream = new IsolatedStorageFileStream(fileName, FileMode.Open, store))
                    {
                        XDocument xmlFile = XDocument.Load(stream);
                        XElement root = xmlFile.Root;
                        byte[] encrypted = System.Convert.FromBase64String(root.Value);
                        byte[] decrypted = ProtectedData.Unprotect(encrypted, null);
                        string[] data = Encoding.UTF8.GetString(decrypted, 0, decrypted.Length).Split(' ');

                        AuthenticatedUser = new User{ Login = data[0], Password = data[1], Email = data[2] };
                    }
                }
            }

            return IsUserAuthenticated();
        }

        public LoginResult LogIn(string email, string password)
        {
            AuthorizeState authState =  _gameWebService.Authorize(email, password).Result;

            switch(authState)
            {
                case AuthorizeState.Success:
                    AuthenticatedUser = new User() { Login = "Login", Email = email, Password = password };

                    byte[] data = ProtectedData.Protect(Encoding.UTF8.GetBytes("login" + " " + password + " " + email), null);
                    string encrypted = System.Convert.ToBase64String(data, 0, data.Length);
                    XDocument xmlFile = new XDocument(new XElement("Root",encrypted));
            

                    using (IsolatedStorageFile store = IsolatedStorageFile.GetUserStoreForApplication())
                    {
                        using (IsolatedStorageFileStream stream = new IsolatedStorageFileStream(fileName, FileMode.Create, FileAccess.Write, store))
                        {
                            xmlFile.Save(stream);
                        }
                    }

                    return LoginResult.Success;
                case AuthorizeState.WrongPassword:
                    return LoginResult.Failure;
                case AuthorizeState.NoUserFound:
                    return LoginResult.Failure;
                default:
                    return LoginResult.Timeout;
            }
        }

        /// <summary>
        /// Clear credentials and all data from database
        /// </summary>
        public void Logout()
        {
            var repo = _unitOfWorkLocator.GetRepository<IGame>();
            var allGamesList = repo.All();

            foreach (IGame game in allGamesList)
            {
                repo.MarkForDeletion(game);
            }

            _unitOfWorkLocator.Commit();

            AuthenticatedUser = null;
            using (IsolatedStorageFile isf = IsolatedStorageFile.GetUserStoreForApplication())
            {
                if (isf.FileExists(fileName))
                {
                    isf.DeleteFile(fileName);
                }
            }
        }

        private User _authenticatedUser;
        public User AuthenticatedUser
        {
            get
            {
                return _authenticatedUser;
            }
            set
            {
                if (_authenticatedUser != value)
                {
                    NotifyPropertyChanging("AuthenticatedUser");
                    _authenticatedUser = value;
                    NotifyPropertyChanged("AuthenticatedUser");
                }
            }
        }

        public bool IsUserAuthenticated()
        {
            return AuthenticatedUser == null ? false : true;
        }

        public RegisterResult Register(User userData)
        {
            AuthenticatedUser = userData;
            CreateAccountResponse accuontResponse = _gameWebService.CreateAccount(userData.Login, userData.Email, userData.Password).Result;

            switch (accuontResponse)
            {
                case CreateAccountResponse.Success:
                    return RegisterResult.Success;
                case CreateAccountResponse.LoginUnavailable:
                    return RegisterResult.Failure;
                case CreateAccountResponse.Timeout:
                    return RegisterResult.Timeout;
                default:
                    return RegisterResult.UnknownError;
                    
            }
        }
    }
}
