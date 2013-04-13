using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Caliburn.Micro;
using UrbanGame.ViewModels;
using System.Windows;
using Microsoft.Phone.Shell;
namespace UrbanGame
{
    public class AppBootstrapper : PhoneBootstrapper
    {
        PhoneContainer container;
        protected override void Configure()
        {
            container = new PhoneContainer(RootFrame);
            container.RegisterPhoneServices();
            container.PerRequest<MainViewModel>();
            container.PerRequest<GameDetailsViewModel>();
            container.PerRequest<GamesListViewModel>();
        }

        protected override object GetInstance(Type service, string key)
        {
            return container.GetInstance(service, key);
        }

        protected override IEnumerable<object> GetAllInstances(Type service)
        {
            return container.GetAllInstances(service);
        }

        protected override void BuildUp(object instance)
        {
            container.BuildUp(instance);
        }

        protected override void PrepareApplication()
        {
            base.PrepareApplication();
            Resurecting = false;
        }

        public static bool Resurecting { get; set; }

        protected override void OnActivate(object sender, ActivatedEventArgs e)
        {
            if (!e.IsApplicationInstancePreserved)
            {
                Resurecting = true;
            }
            base.OnActivate(sender, e);
        }

        protected override void OnUnhandledException(object sender, ApplicationUnhandledExceptionEventArgs e)
        {
            MessageBox.Show("Unexpected problem occured. Application will be closed. " + e.ExceptionObject.Message + " " + e.ExceptionObject.Data, "Unexpected problem", MessageBoxButton.OK);
            base.OnUnhandledException(sender, e);
        }
    }
}
