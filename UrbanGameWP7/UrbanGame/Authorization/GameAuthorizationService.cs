using System;
using System.Linq;
using System.Text;
using Common;
using System.IO;
using System.Collections.Generic;
using System.IO.IsolatedStorage;
using System.Security.Cryptography;
using System.Windows;

namespace UrbanGame.Authorization
{
    public class GameAuthorizationService :IGameAuthorizationService
    {
        string fileName = "data-file.txt";

        public void SaveToIsolatedStorage(string dataToSave)
        {
            byte[] data = ProtectedData.Protect(Encoding.UTF8.GetBytes(dataToSave), null);
            using (var store = IsolatedStorageFile.GetUserStoreForApplication())
            {
                using (var stream = new IsolatedStorageFileStream(fileName, FileMode.Create, FileAccess.Write, store))
                {
                    stream.Write(data, 0, data.Length);
                }
            }
        }

        public string LoadDataFromIsolatedStorage()
        {
            byte[] data;

            string savedData = "";

            using (IsolatedStorageFile isf = IsolatedStorageFile.GetUserStoreForApplication())
            {
                if (isf.FileExists(fileName))
                {
                    using (IsolatedStorageFileStream isfs = isf.OpenFile(fileName, FileMode.Open, FileAccess.Read))
                    {
                        data = new byte[isfs.Length];
                        isfs.Read(data, 0, data.Length);
                        isfs.Close();
                    }

                    byte[] decryptedData = ProtectedData.Unprotect(data, null);
                    savedData = UTF8Encoding.UTF8.GetString(decryptedData, 0, decryptedData.Length);
                }
            }

            return savedData;
        }

        public void ClearIsolatedStorage()
        {
            using (IsolatedStorageFile isf = IsolatedStorageFile.GetUserStoreForApplication())
            {
                if (isf.FileExists(fileName))
                {
                    isf.DeleteFile(fileName);
                }
            }
        }
    }
}
