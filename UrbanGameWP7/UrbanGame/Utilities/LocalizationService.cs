using Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Resources;
using System.Text;
using System.Windows;

namespace UrbanGame.Utilities
{
    public class LocalizationService : ILocalizationService
    {
        ResourceManager rm;

        public LocalizationService()
        {
            rm = new ResourceManager("UrbanGame.Localization.AppResources", Assembly.GetExecutingAssembly());
        }

        public string GetText(string identifier)
        {
            return rm.GetString(identifier);
        }
    }
}
