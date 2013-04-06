using Common;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UrbanGame.Storage;
using WebService;

namespace UrbanGameTests.Tests
{
    [TestClass]
    public class WebApiTest
    {
        #region GetGameWebServiceMock
        private GameWebServiceMock GetGameWebServiceMock()
        {
            var ggwsm = new GameWebServiceMock();
            return ggwsm;
        }
        #endregion

        #region GetGamesTest
        [TestMethod]
        public void GetGamesTest()
        {
            var ggwsm = GetGameWebServiceMock();
            var games = new IGame[] { new Game() { Id = 1 }, new Game() { Id = 2 }, new Game() { Id = 3 }, new Game() { Id = 4 }, new Game() { Id = 5 } };
            ggwsm.ListOfGames.AddRange(games);
            var result = ggwsm.GetGames();
            Assert.IsTrue(result.Length == 5);
        }
        #endregion

        #region GetTasksTest
        [TestMethod]
        public void GetTasksTest()
        {
            var ggwsm = GetGameWebServiceMock();
            var games = new IGame[] { new Game() { Id = 1 }, new Game() { Id = 2 }, new Game() { Id = 3 }, new Game() { Id = 4 }, new Game() { Id = 5 } };
            ggwsm.ListOfGames.AddRange(games);
            var result = ggwsm.GetTasks(1);
            Assert.IsTrue(result.Length == 0);
        }
        #endregion

        #region GetTasksTest2
        [TestMethod]
        public void GetTasksTest2()
        {
            var ggwsm = GetGameWebServiceMock();
            var game = new Game();
            var task = new TaskMock() { Id = 1 };
            task.Id = 5;
            ggwsm.ListOfTasks.Add(task);
            var games = new IGame[] { game, new Game() { Id = 2 }, new Game() { Id = 3 }, new Game() { Id = 4 }, new Game() { Id = 5 } };
            ggwsm.ListOfGames.AddRange(games);
            var result = ggwsm.GetTasks(1);
            Assert.IsTrue(result[0] == task);
        }
        #endregion

        #region GameChangedTest
        [TestMethod]
        public void GameChangedTest()
        {
            var ggwsm = GetGameWebServiceMock();
            var games = new IGame[] { new Game() { Id = 1 }, new Game() { Id = 2 }, new Game() { Id = 3 }, new Game() { Id = 4 }, new Game() { Id = 5 } };
            ggwsm.ListOfGames.AddRange(games);
            bool flag = false;
            ggwsm.GameChanged += delegate(object o, GameEventArgs e) { flag = true; };
            ggwsm.ChangeGame(1);
            var result = ggwsm.GetGames();
            Assert.IsTrue(flag && result[0].NumberOfCompletedTasks == 1);
        }
        #endregion
    }
}
