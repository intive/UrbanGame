using Common;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UrbanGame.Storage;
using UrbanGameTests.Mocks;

namespace UrbanGameTests.Tests
{
    [TestClass]
    public class UnitOfWorkMockTest
    {
        #region UOWandRepoTest
        [TestMethod]
        public void UOWandRepoTest()
        {
            IDatabaseMock database = new DatabaseMock();
            IUnitOfWork unitOfWork = new UnitOfWorkMock(database);
                        
            IRepository<IGame> gameRepo = unitOfWork.GetRepository<IGame>();
            IRepository<ITask> taskRepo = unitOfWork.GetRepository<ITask>();
            IGame game = new Game() { Id = 1, Name = "Test" };
            IGame game2 = new Game() { Id = 2, Name = "Test2" };
            ITask task = new GameTask() { Id = 1, Name = "Test" };

            //inserting
            gameRepo.MarkForAdd(game);
            Assert.AreEqual(gameRepo.All().Count(), 0);
            unitOfWork.Commit();
            Assert.AreEqual(gameRepo.All().Count(), 1);

            taskRepo.MarkForAdd(task);
            Assert.AreEqual(taskRepo.All().Count(), 0);
            unitOfWork.Commit();
            Assert.AreEqual(taskRepo.All().Count(), 1);

            //checking if getting table again returns the same table
            Assert.IsTrue(unitOfWork.GetRepository<IGame>().All().Any(g => g.Id == 1));
            Assert.IsTrue(unitOfWork.GetRepository<ITask>().All().Any(t => t.Id == 1));

            //deleting
            gameRepo.MarkForDeletion(game);
            Assert.AreEqual(gameRepo.All().Count(), 1);
            unitOfWork.Commit();
            Assert.AreEqual(gameRepo.All().Count(), 0);

            taskRepo.MarkForDeletion(task);
            Assert.AreEqual(taskRepo.All().Count(), 1);
            unitOfWork.Commit();
            Assert.AreEqual(taskRepo.All().Count(), 0);

            //deleting & inserting           
            gameRepo.MarkForAdd(game);
            unitOfWork.Commit();
            Assert.AreEqual(gameRepo.All().Count(), 1);

            //before submit
            gameRepo.MarkForAdd(game2);
            gameRepo.MarkForDeletion(game);
            Assert.IsFalse(gameRepo.All().Contains(game2)); 
            Assert.IsTrue(gameRepo.All().Contains(game));

            //after submit
            unitOfWork.Commit();
            Assert.AreEqual(gameRepo.All().Count(), 1);
            Assert.IsTrue(gameRepo.All().Contains(game2));
            Assert.IsFalse(gameRepo.All().Contains(game));

            gameRepo.MarkForDeletion(game2);
            unitOfWork.Commit();
            Assert.AreEqual(gameRepo.All().Count(), 0);

            //multiple insert
            gameRepo.MarkForAdd(game);
            gameRepo.MarkForAdd(game2);
            Assert.AreEqual(gameRepo.All().Count(), 0);
            unitOfWork.Commit();
            Assert.AreEqual(gameRepo.All().Count(), 2);

            //multiple delete
            gameRepo.MarkForDeletion(game);
            gameRepo.MarkForDeletion(game2);
            Assert.AreEqual(gameRepo.All().Count(), 2);
            unitOfWork.Commit();
            Assert.AreEqual(gameRepo.All().Count(), 0);
        }
        #endregion
    }
}
