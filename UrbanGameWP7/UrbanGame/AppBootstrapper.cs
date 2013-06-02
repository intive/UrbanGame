using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Caliburn.Micro;
using UrbanGame.ViewModels;
using System.Windows;
using Microsoft.Phone.Shell;
using Common;
using WebService;
using UrbanGame.Storage;
using UrbanGame.Utilities;
using UrbanGame.Localization;

namespace UrbanGame
{
    public class AppBootstrapper : PhoneBootstrapper
    {
        PhoneContainer container;
        protected override void Configure()
        {
            container = new PhoneContainer(RootFrame);
            container.RegisterPhoneServices();
            container.PerRequest<GameDetailsPreviewViewModel>();
            container.PerRequest<GameDetailsViewModel>();
            container.PerRequest<GamesListViewModel>();
            container.PerRequest<TaskViewModel>();
            container.Singleton<ILocalizationService, LocalizationService>();
            container.PerRequest<IUnitOfWork, UnitOfWork>();       
            container.Singleton<IGameWebService, GameWebServiceMock>();
            container.Singleton<IGameChangesManager, GameChangesManager>();
            container.PerRequest<IAppbarManager, AppbarManager>();            

            container.Handler<UrbanGameDataContext>((sc) =>
            {
                return new UrbanGameDataContext();
            });
        }

        protected override object GetInstance(Type service, string key)
        {
            return container.GetInstance(service, key);
        }

        protected override IEnumerable<object> GetAllInstances(Type service)
        {
            return container.GetAllInstances(service);
        }

        protected override void OnStartup(object sender, StartupEventArgs e)
        {
            base.OnStartup(sender, e);           
            App.GameChangesManager = IoC.Get<IGameChangesManager>();
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
