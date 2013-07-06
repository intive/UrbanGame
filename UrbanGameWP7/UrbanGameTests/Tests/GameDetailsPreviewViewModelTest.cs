using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UrbanGame.ViewModels;
using Caliburn.Micro;
using Common;
using UrbanGame.Storage;
using WebService;
using System.Threading;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using UrbanGameTests.Mocks;
using UrbanGame.Utilities;

namespace UrbanGameTests.Tests
{
    [TestClass]
    public class GameDetailsPreviewViewModelTest
    {
        #region GameTest
        
        [TestMethod]
        public async void GameRefreshingTest()
        {
            #region preparing view model
            IDatabaseMock database = new DatabaseMock();
            IUnitOfWork unitOfWork = new UnitOfWorkMock(database);
            Func<IUnitOfWork> uow = new Func<IUnitOfWork>(() => unitOfWork);
            IGameWebService webService = new GameWebServiceMock();
            IEventAggregator eventAgg = new EventAggregator();
            ICredentialsService credentials = new CredentialsService();
            IGameAuthorizationService authorizationService = new GameAuthorizationService(uow, webService, credentials);

            //removing current records
            foreach (IGame g in unitOfWork.GetRepository<IGame>().All())
                unitOfWork.GetRepository<IGame>().MarkForDeletion(g);
            unitOfWork.Commit();

            //test data            
            unitOfWork.GetRepository<IGame>().MarkForAdd(new Game()
            {
                Id = 1,
                Name = "FromDatabase",
                OperatorName = "CAFETERIA",
                //Localization = "Wroclaw",
                GameLogo = "/ApplicationIcon.png",
                GameStart = DateTime.Now.AddHours(3).AddMinutes(23),
                GameEnd = DateTime.Now.AddDays(2).AddHours(5),
                NumberOfPlayers = 24,
                NumberOfSlots = 50,
                GameType = GameType.ScoreAttack,
                Description = "sadsa sad ads  adsa dssa sad  asas asd as a sas as as  asas  asdas as ads as d",
                Difficulty = GameDifficulty.Medium,
                Prizes = "1st Bicycle\n2nd Bicycle\n3rd Bicycle\n4-8th Bicycle bicycle bicycle"
            });
            unitOfWork.Commit();

            GameDetailsPreviewViewModel vm = 
                new GameDetailsPreviewViewModel(null, () => new UnitOfWorkMock(database), 
                                         webService, eventAgg, new AppbarManagerMock(), authorizationService) { GameId = 1 };
            #endregion
            
            //if user is unauthorized, then game should be downloaded from WebService
            authorizationService.AuthenticatedUser = null;
            await vm.RefreshGame();
            Assert.IsNotNull(vm.Game);
            Assert.AreNotEqual(vm.Game.Name, "FromDatabase");
            
            //if user is authorized, then game should be downloaded from database
            authorizationService.AuthenticatedUser = new User { Login = "Login", Password = "Admin", Email = "Login@gmail.com" };
            await vm.RefreshGame();
            Assert.IsNotNull(vm.Game);
            Assert.AreEqual(vm.Game.Name, "FromDatabase");
            
            
            //handling operator's updates
            unitOfWork.GetRepository<IGame>().All().First(g => g.Id == 1).Name = "Changed";
            unitOfWork.Commit();
            eventAgg.Publish(new GameChangedEvent() { Id = 1 });
            Assert.AreEqual(vm.Game.Name, "Changed");            
        }
        
        #endregion
    }
}
