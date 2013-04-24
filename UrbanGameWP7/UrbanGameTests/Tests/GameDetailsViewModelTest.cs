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

namespace UrbanGameTests.Tests
{
    [TestClass]
    public class GameDetailsViewModelTest
    {
        #region GameTest
        
        [TestMethod]
        public async void GameRefreshingTest()
        {
            #region preparing view model
            IUnitOfWork unitOfWork = new UnitOfWork(new UrbanGameDataContext());
            IGameWebService webService = new GameWebServiceMock();
            IEventAggregator eventAgg = new EventAggregator();

            //removing current records
            foreach (IGame g in unitOfWork.GetRepository<IGame>().All().ToList())
                unitOfWork.GetRepository<IGame>().MarkForDeletion(g);
            unitOfWork.Commit();

            //test data            
            unitOfWork.GetRepository<IGame>().MarkForAdd(new Game()
            {
                Id = 1,
                Name = "FromDatabase",
                OperatorName = "CAFETERIA",
                OperatorLogo = "/ApplicationIcon.png",
                GameLogo = "/ApplicationIcon.png",
                GameStart = DateTime.Now.AddHours(3).AddMinutes(23),
                GameEnd = DateTime.Now.AddDays(2).AddHours(5),
                NumberOfPlayers = 24,
                NumberOfSlots = 50,
                GameType = GameType.Quiz,
                Description = "sadsa sad ads  adsa dssa sad  asas asd as a sas as as  asas  asdas as ads as d",
                Difficulty = GameDifficulty.Medium,
                Prizes = "1st Bicycle\n2nd Bicycle\n3rd Bicycle\n4-8th Bicycle bicycle bicycle"
            });
            unitOfWork.Commit();

            GameDetailsViewModel vm = new GameDetailsViewModel(null, () => unitOfWork, webService, eventAgg,new AppbarManagerMock()) { GameId = 1 };
            #endregion
            
            //if user is unauthorized, then game should be downloaded from WebService
            webService.IsAuthorized = false;
            await vm.RefreshGame();
            Thread.Sleep(1000);
            Assert.IsNotNull(vm.Game);
            Assert.AreNotEqual(vm.Game.Name, "FromDatabase");
            
            
            //handling operator's updates
            //description changes each time in mock-up WebService results
            string oldDesc = vm.Game.Description;
            eventAgg.Publish(new GameChangedEvent() { Id = 1 });
            Thread.Sleep(1000);
            Assert.AreNotEqual(vm.Game.Description, oldDesc);
            

            
            //if user is authorized, then game should be downloaded from database
            webService.IsAuthorized = true;
            await vm.RefreshGame();
            Thread.Sleep(1000);
            Assert.IsNotNull(vm.Game);
            Assert.AreEqual(vm.Game.Name, "FromDatabase");
            
            
            //handling operator's updates
            unitOfWork.GetRepository<IGame>().All().First(g => g.Id == 1).Name = "Changed";
            unitOfWork.Commit();
            eventAgg.Publish(new GameChangedEvent() { Id = 1 });
            Thread.Sleep(1000);
            Assert.AreEqual(vm.Game.Name, "Changed");
            
        }
        
        #endregion
    }
}
