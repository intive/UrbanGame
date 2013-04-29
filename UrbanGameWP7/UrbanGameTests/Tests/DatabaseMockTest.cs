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
    public class DatabaseMockTest
    {
        #region TablesTest
        [TestMethod]
        public void TablesTest()
        {
            DatabaseMock database = new DatabaseMock();
            ITableMock<IGame> gameTable = database.GetTable<IGame>();
            ITableMock<ITask> taskTable = database.GetTable<ITask>();
            IGame game = new Game() { Id = 1, Name = "Test" };
            IGame game2 = new Game() { Id = 2, Name = "Test2" };
            ITask task = new GameTask() { Id = 1, Name = "Test" };

            //inserting
            gameTable.InsertOnSubmit(game);
            Assert.AreEqual(gameTable.All().Count(), 0);
            database.SubmitAllChanges();
            Assert.AreEqual(gameTable.All().Count(), 1);

            taskTable.InsertOnSubmit(task);
            Assert.AreEqual(taskTable.All().Count(), 0);
            database.SubmitAllChanges();
            Assert.AreEqual(taskTable.All().Count(), 1);

            //checking if getting table again returns the same table
            Assert.IsTrue(database.GetTable<IGame>().All().Any(g => g.Id == 1));
            Assert.IsTrue(database.GetTable<ITask>().All().Any(t => t.Id == 1));

            //deleting
            gameTable.DeleteOnSubmit(game);
            Assert.AreEqual(gameTable.All().Count(), 1);
            database.SubmitAllChanges();
            Assert.AreEqual(gameTable.All().Count(), 0);

            taskTable.DeleteOnSubmit(task);
            Assert.AreEqual(taskTable.All().Count(), 1);
            database.SubmitAllChanges();
            Assert.AreEqual(taskTable.All().Count(), 0);

            //deleting & inserting           
            gameTable.InsertOnSubmit(game);
            database.SubmitAllChanges();
            Assert.AreEqual(gameTable.All().Count(), 1);

            //before submit
            gameTable.InsertOnSubmit(game2);
            gameTable.DeleteOnSubmit(game);
            Assert.IsFalse(gameTable.All().Contains(game2)); 
            Assert.IsTrue(gameTable.All().Contains(game));

            //after submit
            database.SubmitAllChanges();
            Assert.AreEqual(gameTable.All().Count(), 1);
            Assert.IsTrue(gameTable.All().Contains(game2));
            Assert.IsFalse(gameTable.All().Contains(game));

            gameTable.ClearTable();
            Assert.AreEqual(gameTable.All().Count(), 0);

            //multiple insert
            gameTable.InsertOnSubmit(game);
            gameTable.InsertOnSubmit(game2);
            Assert.AreEqual(gameTable.All().Count(), 0);
            database.SubmitAllChanges();
            Assert.AreEqual(gameTable.All().Count(), 2);

            //multiple delete
            gameTable.DeleteOnSubmit(game);
            gameTable.DeleteOnSubmit(game2);
            Assert.AreEqual(gameTable.All().Count(), 2);
            database.SubmitAllChanges();
            Assert.AreEqual(gameTable.All().Count(), 0);
        }
        #endregion
    }
}
