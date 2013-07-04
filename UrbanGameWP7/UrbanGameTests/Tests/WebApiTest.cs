using Common;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UrbanGame.Storage;
using WebService;
using WebService.BOMock;

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

        #region GetTasksTest
        [TestMethod]
        public async void GetTasksTest()
        {
            var ggwsm = GetGameWebServiceMock();
            var games = new IGame[] { new Game() { Id = 1 }, new Game() { Id = 2 }, new Game() { Id = 3 }, new Game() { Id = 4 }, new Game() { Id = 5 } };
            ggwsm.ListOfGames.AddRange(games);
            var result = await ggwsm.GetTasks(1);
            Assert.IsTrue(result.Length == 0);
        }
        #endregion

        #region GetTasksTest2
        [TestMethod]
        public async void GetTasksTest2()
        {
            var ggwsm = GetGameWebServiceMock();
            IGame game = new Game() { Id = 1 };
            ITask task = new GameTask() { Id = 1 };
            game.Tasks.Add(task);
            ggwsm.ListOfTasks.Add(task);
            var games = new IGame[] { game, new Game() { Id = 2 }, new Game() { Id = 3 }, new Game() { Id = 4 }, new Game() { Id = 5 } };
            ggwsm.ListOfGames.AddRange(games);
            var result = await ggwsm.GetTasks(1);
            Assert.IsTrue(result[0] == task);
        }
        #endregion
    }
}
