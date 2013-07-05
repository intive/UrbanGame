using System;
using System.Windows;
using System.Collections.Generic;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using UrbanGameTests.Mocks;
using System.Linq;
using System.Text;
using Common;
using UrbanGame.Storage;
using WebService;
using Caliburn.Micro;
using UrbanGame.ViewModels;
using UrbanGame.Utilities;

namespace UrbanGameTests.Tests
{
    [TestClass]
    public class GameDetailsViewModelTest
    {
        #region GameTest

        #region CreateTestGame
        private IGame CreateTestGame(GameState gameState, int id, string name = "Gdzie jest kot")
        {
            return new Game()
            {
                Id = id,
                Name = name,
                OperatorName = "CAFETERIA",
                GameLogo = "/ApplicationIcon.png",
                GameStart = DateTime.Now.AddHours(5).AddMinutes(3),
                GameEnd = DateTime.Now.AddDays(2).AddHours(15),
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

        #region CreateTestAlert
        private IAlert CreateTestAlert(int id, IGame game)
        {
            return new GameAlert()
            {
                Id = id,
                Topic = "More points",
                Description = "There will be an oportunity to get more points for players who just started, so be ready",
                Game = new Game(){Id = game.Id},
                GameId = game.Id,
            };
        }
        #endregion

        #region CreateTestHighScore
        private IHighScore CreateTestHighScore(int id, String userLogin, int points, IGame game)
        {
            return new GameHighScore()
            {
                Id = id,
                UserLogin = userLogin,
                Points = points,
                GameId = game.Id,
                Game = new Game() { Id = game.Id }
            };
        }
        #endregion

        #region CreateTestTask
        private ITask CreateTestTask(int id, String name, TaskState state, IGame game)
        {
            return new GameTask()
            {
                Id = id,
                Name = name,
                State = state,
                Picture = "/ApplicationIcon.png",
                IsRepeatable = true,
                EndDate = DateTime.Now.AddHours(2).AddMinutes(7),
                UserPoints = 20,
                MaxPoints = 300,
                Game = new Game() { Id = game.Id },
                GameId = game.Id
            };
        }
        #endregion


        [TestMethod]
        public async void GameTasksTest()
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
            foreach (ITask g in unitOfWork.GetRepository<ITask>().All().ToList())
                unitOfWork.GetRepository<ITask>().MarkForDeletion(g);
            foreach (IAlert g in unitOfWork.GetRepository<IAlert>().All().ToList())
                unitOfWork.GetRepository<IAlert>().MarkForDeletion(g);
            foreach (IHighScore g in unitOfWork.GetRepository<IHighScore>().All().ToList())
                unitOfWork.GetRepository<IHighScore>().MarkForDeletion(g);
            unitOfWork.Commit();

            IGame game = CreateTestGame(GameState.Joined, 1, "Where is he");
            //test data
            unitOfWork.GetRepository<IGame>().MarkForAdd(game);
            unitOfWork.GetRepository<ITask>().MarkForAdd(CreateTestTask(1, "Where is he", TaskState.Active, game));
            unitOfWork.GetRepository<ITask>().MarkForAdd(CreateTestTask(2, "Where is he", TaskState.Active, game));
            unitOfWork.GetRepository<ITask>().MarkForAdd(CreateTestTask(3, "Where is he", TaskState.Active, game));
            unitOfWork.GetRepository<ITask>().MarkForAdd(CreateTestTask(3, "Die Another Day", TaskState.Inactive, game));
            unitOfWork.GetRepository<ITask>().MarkForAdd(CreateTestTask(4, "Poison for As", TaskState.Accomplished, game));
            unitOfWork.GetRepository<ITask>().MarkForAdd(CreateTestTask(5, "Where am I?", TaskState.Cancelled, game));
            unitOfWork.GetRepository<IAlert>().MarkForAdd(CreateTestAlert(1, game));
            unitOfWork.GetRepository<IHighScore>().MarkForAdd(CreateTestHighScore(1, "Ksiaze Narni", 350, game));
            unitOfWork.GetRepository<IHighScore>().MarkForAdd(CreateTestHighScore(2, "Aladyn", 40, game));
            unitOfWork.GetRepository<IHighScore>().MarkForAdd(CreateTestHighScore(2, "Korona", 90, game));

            unitOfWork.Commit();
            GameDetailsViewModel vm = new GameDetailsViewModel(null, () => unitOfWork,
                                                           webService, eventAgg, new AppbarManagerMock(), authorizationService);
            #endregion

            vm.GameId = game.Id;

            await vm.RefreshActiveTasks();
            Assert.AreEqual(vm.ActiveTasks.Count, 3); //that there are exactly 3 active tasks

            await vm.RefreshInactiveTasks();
            Assert.AreEqual(vm.InactiveTasks.Count, 1); //that there is exactly 1 inactive task

            await vm.RefreshAccomplishedTasks();
            Assert.AreEqual(vm.AccomplishedTasks.Count, 1); //that there is exactly 1 accomplished task

            await vm.RefreshCancelledTasks();
            Assert.AreEqual(vm.CancelledTasks.Count, 1); //that there is exactly 1 cancelled task

            await vm.RefreshAlerts();
            Assert.AreEqual(vm.GameAlerts.Count, 1); //that there is exactly 1 alert

            await vm.RefreshHighScores();
            Assert.AreEqual(vm.GameHighScores.Count, 3); //that there are exactly 3 high scores
            Assert.AreEqual(vm.GameHighScores.ElementAt(0).Entity.Points, 350); //that at firs position is player who has 350 points


        }

        #endregion
    }
}
