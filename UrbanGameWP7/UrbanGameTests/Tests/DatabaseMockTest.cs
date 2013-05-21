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
            ITableMock<IAlert> alertTable = database.GetTable<IAlert>();
            ITableMock<IHighScore> highScoreTable = database.GetTable<IHighScore>();
            ITableMock<IBaseSolution> solutionTable = database.GetTable<IBaseSolution>();
            ITableMock<IABCDUserAnswer> abcdUserAnswerTable = database.GetTable<IABCDUserAnswer>();
            IGame game = new Game() { Id = 1, Name = "Test" };
            IGame game2 = new Game() { Id = 2, Name = "Test2" };
            ITask task = new GameTask() { Id = 1, Name = "Test" };
            IAlert alert = new GameAlert() { Id = 3, Topic = "Operator" };
            IHighScore highScore = new GameHighScore() { Id = 4, UserLogin = "Test" };
            IBaseSolution solution = new TaskSolution() { Id = 1, TaskType = Common.TaskType.OpenQuestion, TextAnswer = "test" };
            IABCDUserAnswer abcdUserAnswer = new ABCDUserAnswer() { Id = 1, Answer = 1 };

            //inserting
            gameTable.InsertOnSubmit(game);
            Assert.AreEqual(0, gameTable.All().Count());
            database.SubmitAllChanges();
            Assert.AreEqual(1, gameTable.All().Count());

            taskTable.InsertOnSubmit(task);
            Assert.AreEqual(0, taskTable.All().Count());
            database.SubmitAllChanges();
            Assert.AreEqual(1, taskTable.All().Count());

            alertTable.InsertOnSubmit(alert);
            Assert.AreEqual(0, alertTable.All().Count());
            database.SubmitAllChanges();
            Assert.AreEqual(1, alertTable.All().Count());

            highScoreTable.InsertOnSubmit(highScore);
            Assert.AreEqual(0, highScoreTable.All().Count());
            database.SubmitAllChanges();
            Assert.AreEqual(1, highScoreTable.All().Count());

            solutionTable.InsertOnSubmit(solution);
            Assert.AreEqual(0, solutionTable.All().Count());
            database.SubmitAllChanges();
            Assert.AreEqual(1, solutionTable.All().Count());

            abcdUserAnswerTable.InsertOnSubmit(abcdUserAnswer);
            Assert.AreEqual(0, abcdUserAnswerTable.All().Count());
            database.SubmitAllChanges();
            Assert.AreEqual(1, abcdUserAnswerTable.All().Count());

            //checking if getting table again returns the same table
            Assert.IsTrue(database.GetTable<IGame>().All().Any(g => g.Id == 1));
            Assert.IsTrue(database.GetTable<ITask>().All().Any(t => t.Id == 1));
            Assert.IsTrue(database.GetTable<IAlert>().All().Any(a => a.Id == 3));
            Assert.IsTrue(database.GetTable<IHighScore>().All().Any(h => h.Id == 4));
            Assert.IsTrue(database.GetTable<IBaseSolution>().All().Any(s => s.Id == 1));
            Assert.IsTrue(database.GetTable<IABCDUserAnswer>().All().Any(a => a.Id == 1));

            //deleting
            gameTable.DeleteOnSubmit(game);
            Assert.AreEqual(1, gameTable.All().Count());
            database.SubmitAllChanges();
            Assert.AreEqual(0, gameTable.All().Count());

            taskTable.DeleteOnSubmit(task);
            Assert.AreEqual(1, taskTable.All().Count());
            database.SubmitAllChanges();
            Assert.AreEqual(0, taskTable.All().Count());

            alertTable.DeleteOnSubmit(alert);
            Assert.AreEqual(1, alertTable.All().Count());
            database.SubmitAllChanges();
            Assert.AreEqual(0, alertTable.All().Count());

            highScoreTable.DeleteOnSubmit(highScore);
            Assert.AreEqual(1, highScoreTable.All().Count());
            database.SubmitAllChanges();
            Assert.AreEqual(0, highScoreTable.All().Count());

            solutionTable.DeleteOnSubmit(solution);
            Assert.AreEqual(1, solutionTable.All().Count());
            database.SubmitAllChanges();
            Assert.AreEqual(0, solutionTable.All().Count());

            abcdUserAnswerTable.DeleteOnSubmit(abcdUserAnswer);
            Assert.AreEqual(1, abcdUserAnswerTable.All().Count());
            database.SubmitAllChanges();
            Assert.AreEqual(0, abcdUserAnswerTable.All().Count());

            //deleting & inserting           
            gameTable.InsertOnSubmit(game);
            database.SubmitAllChanges();
            Assert.AreEqual(1, gameTable.All().Count());

            //before submit
            gameTable.InsertOnSubmit(game2);
            gameTable.DeleteOnSubmit(game);
            Assert.IsFalse(gameTable.All().Contains(game2)); 
            Assert.IsTrue(gameTable.All().Contains(game));

            //after submit
            database.SubmitAllChanges();
            Assert.AreEqual(1, gameTable.All().Count());
            Assert.IsTrue(gameTable.All().Contains(game2));
            Assert.IsFalse(gameTable.All().Contains(game));

            gameTable.ClearTable();
            Assert.AreEqual(0, gameTable.All().Count());

            //multiple insert
            gameTable.InsertOnSubmit(game);
            gameTable.InsertOnSubmit(game2);
            Assert.AreEqual(0, gameTable.All().Count());
            database.SubmitAllChanges();
            Assert.AreEqual(2, gameTable.All().Count());

            //multiple delete
            gameTable.DeleteOnSubmit(game);
            gameTable.DeleteOnSubmit(game2);
            Assert.AreEqual(2, gameTable.All().Count());
            database.SubmitAllChanges();
            Assert.AreEqual(0, gameTable.All().Count());
        }
        #endregion
    }
}
