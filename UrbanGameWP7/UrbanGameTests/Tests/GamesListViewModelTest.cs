using Microsoft.VisualStudio.TestTools.UnitTesting;
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
using UrbanGameTests.Mocks;
using UrbanGame.Utilities;

namespace UrbanGameTests.Tests
{
    [TestClass]
    public class GamesListViewModelTest
    {
        #region CreateTestGame
        private IGame CreateTestGame(GameState gameState, int id, string name = "Hydrozagadka")
        {
            return new Game()
            {
                Id = id,
                Name = name,
                OperatorName = "CAFETERIA",
                GameLogo = "/ApplicationIcon.png",
                GameStart = DateTime.Now.AddHours(3).AddMinutes(23),
                GameEnd = DateTime.Now.AddDays(2).AddHours(5),
                GameState = gameState,
                NumberOfPlayers = 24,
                NumberOfSlots = 50,
                GameType = GameType.ScoreAttack,
                Description = DateTime.Now.ToLongTimeString() + "\nsadsa sad ads  adsa dssa sad  asas asd as a sas as as  asas  asdas as ads as d",
                Difficulty = GameDifficulty.Medium,
                Prizes = "1st Bicycle\n2nd Bicycle\n3rd Bicycle\n4-8th Bicycle bicycle bicycle"
            };
        }
        #endregion

        #region UserGamesTest
        [TestMethod]
        public async void UserGameListsTest()
        {
            #region preparing view model
            IDatabaseMock database = new DatabaseMock();
            IUnitOfWork unitOfWork = new UnitOfWorkMock(database);
            Func<IUnitOfWork> uow = new Func<IUnitOfWork>(() => unitOfWork);
            IGameWebService webService = new GameWebServiceMock();
            IEventAggregator eventAgg = new EventAggregator();
            IGameAuthorizationService authorizationService = new GameAuthorizationService(uow, webService);

            //removing current records
            foreach (IGame g in unitOfWork.GetRepository<IGame>().All().ToList())
                unitOfWork.GetRepository<IGame>().MarkForDeletion(g);
            unitOfWork.Commit();

            //test data            
            unitOfWork.GetRepository<IGame>().MarkForAdd(CreateTestGame(GameState.Joined, 1));
            unitOfWork.GetRepository<IGame>().MarkForAdd(CreateTestGame(GameState.Won, 2, "Hydrozagadka2"));
            unitOfWork.GetRepository<IGame>().MarkForAdd(CreateTestGame(GameState.Lost, 3, "Hydrozagadka2"));
            unitOfWork.GetRepository<IGame>().MarkForAdd(CreateTestGame(GameState.None, 4, "Hydrozagadka2"));
            unitOfWork.Commit();
            GamesListViewModel vm = new GamesListViewModel(null, () => new UnitOfWorkMock(database), 
                                                           webService, eventAgg, new AppbarManagerMock(), authorizationService);
            #endregion

            //when user is authorized
            authorizationService.AuthenticatedUser = new User { Login = "Login", Password = "Admin", Email = "Login@gmail.com" };
            await vm.RefreshUserGames();
            Assert.AreEqual(vm.UserActiveGames.Count, 1); //that with Joined game state
            Assert.AreEqual(vm.UserInactiveGames.Count, 3); //that with game state != Joined

            //when user is unauthorized
            authorizationService.AuthenticatedUser = null;
            await vm.RefreshUserGames();
            Assert.AreEqual(vm.UserActiveGames.Count, 0); //that with Joined game state
            Assert.AreEqual(vm.UserInactiveGames.Count, 0); //that with game state != Joined


            //handling updates test
            authorizationService.AuthenticatedUser = new User { Login = "Login", Password = "Admin", Email = "Login@gmail.com" };
            await vm.RefreshUserGames();

            string oldDescription = unitOfWork.GetRepository<IGame>().All().First(g => g.Id == 3).Description;
            unitOfWork.GetRepository<IGame>().All().First(g => g.Id == 3).Description = "sadasdsada";
            unitOfWork.Commit();
            Thread.Sleep(1000);
            eventAgg.Publish(new GameChangedEvent() { Id = 3 });
            Thread.Sleep(1000);
            Assert.AreNotEqual(vm.UserInactiveGames.First(g => g.Id == 3).Description, oldDescription);

            oldDescription = unitOfWork.GetRepository<IGame>().All().First(g => g.Id == 1).Description;
            unitOfWork.GetRepository<IGame>().All().First(g => g.Id == 1).Description = "sadasdsada";
            unitOfWork.Commit();
            Thread.Sleep(1000);
            eventAgg.Publish(new GameChangedEvent() { Id = 1 });
            Thread.Sleep(1000);
            Assert.AreNotEqual(vm.UserActiveGames.First(g => g.Id == 1).Description, oldDescription);
        }
        #endregion
    }
}
