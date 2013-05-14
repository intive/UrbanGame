using Common;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UrbanGame.Storage;

namespace UrbanGameTests.Tests
{
    [TestClass]
    public class DatabaseTest
    {
        #region RecreateDatabase
        private UrbanGameDataContext RecreateDatabase()
        {
            UrbanGameDataContext dataContext = new UrbanGameDataContext("Data Source=isostore:/UrbanGameTest.sdf");
            if (dataContext.DatabaseExists())
                dataContext.DeleteDatabase();
            dataContext.CreateDatabase();

            return dataContext;
        }
        #endregion

        #region CreateSampleEntities
        private void CreateSampleEntities(out Game game, out GameTask task, out GameAlert alert, out GameHighScore highScore, out ABCDPossibleAnswer answer)
        {
            game = new Game()
            {
                Id = 1,
                Name = "TestGame",
                OperatorName = "CAFETERIA",
                Localization = "Wroclaw",
                GameLogo = "/ApplicationIcon.png",
                GameStart = DateTime.Now.AddHours(3).AddMinutes(23),
                GameEnd = DateTime.Now.AddDays(2).AddHours(5),
                NumberOfPlayers = 24,
                NumberOfSlots = 50,
                GameType = GameType.ScoreAttack,
                Description = DateTime.Now.ToLongTimeString() + "\nsadsa sad ads  adsa dssa sad  asas asd as a sas as as  asas  asdas as ads as d",
                Difficulty = GameDifficulty.Medium,
                Prizes = "1st Bicycle\n2nd Bicycle\n3rd Bicycle\n4-8th Bicycle bicycle bicycle"
            };

            task = new GameTask()
            {
                Id = 1,
                Name = "TestTask",
                AdditionalText = "Bla bla",
                Description = "sad as ads  adsdas  assad sad ads ",
                EndDate = DateTime.Now.AddDays(2),
                State = TaskState.Active,
                IsRepeatable = true,
                MaxPoints = 100,
                Picture = "/path/picture.jpeg",
                SolutionStatus = SolutionStatus.Accepted,
                Type = TaskType.OpenQuestion,
                UserPoints = 50,
                Version = 1
            };

            alert = new GameAlert()
            {
                Id = 1,
                Topic = "Operator",
                Description = "There were more than one possible answears before, now only 1 is correct"
            };

            highScore = new GameHighScore()
            {
                Id = 1,
                UserLogin = "LoganXxX",
                Points = 130
            };

            answer = new ABCDPossibleAnswer()
            {
                Id = 1,
                Answer = "Simple answer"
            };
        }

        private void CreateSampleEntities(out IGame game, out ITask task, out IAlert alert, out IHighScore highScore, out IABCDPossibleAnswer answer)
        {
            Game g;
            GameTask t;
            GameAlert al;
            GameHighScore h;
            ABCDPossibleAnswer a;
            CreateSampleEntities(out g, out t, out al, out h, out a);

            game = g;
            task = t;
            alert = al;
            highScore = h;
            answer = a;
        }
        #endregion

        #region DatabaseCreationTest
        [TestMethod]
        public void DatabaseCreationTest()
        {
            RecreateDatabase().Dispose();  
        }
        #endregion

        #region EntitiesRelationsTest
        [TestMethod]
        public void EntitiesRelationsTest()
        {
            Game game;
            GameTask task;
            GameAlert alert;
            GameHighScore highScore;
            ABCDPossibleAnswer answer;
            CreateSampleEntities(out game, out task, out alert, out highScore, out answer);

            using (UrbanGameDataContext dataContext = RecreateDatabase())
            {
                #region Game <-> GameTask, GameAlert, GameHighScore relation

                //adding task to game
                game.Tasks.Add(task);
                game.Alerts.Add(alert);
                game.HighScores.Add(highScore);

                
                Assert.AreSame(game, task.Game);

                dataContext.GetTable<Game>().InsertOnSubmit(game);
                dataContext.SubmitChanges();
                Assert.IsTrue(dataContext.GetTable<Game>().Any(g => g.Id == 1));
                Assert.IsTrue(dataContext.GetTable<GameTask>().Any(t => t.Id == 1));
                Assert.AreEqual(1, dataContext.GetTable<Game>().First(g => g.Id == 1).Tasks.Count);
                Assert.IsNotNull(dataContext.GetTable<GameTask>().First(t => t.Id == 1).Game);

                //removing task from game
                game.Tasks.Remove(task);
                Assert.IsNull(task.Game);
                dataContext.SubmitChanges();

                //removing
                dataContext.GetTable<Game>().DeleteOnSubmit(game);
                dataContext.GetTable<GameTask>().DeleteOnSubmit(task);
                dataContext.GetTable<GameAlert>().DeleteOnSubmit(alert);
                dataContext.GetTable<GameHighScore>().DeleteOnSubmit(highScore);
                dataContext.SubmitChanges();
                Assert.AreEqual(0, dataContext.GetTable<Game>().Count());
                Assert.AreEqual(0, dataContext.GetTable<GameTask>().Count());
                Assert.AreEqual(0, dataContext.GetTable<GameAlert>().Count());
                Assert.AreEqual(0, dataContext.GetTable<GameHighScore>().Count());

                CreateSampleEntities(out game, out task, out alert, out highScore, out answer);

                //adding game to task
                task.Game = game;
                Assert.AreSame(task, game.Tasks.FirstOrDefault());

                dataContext.GetTable<GameTask>().InsertOnSubmit(task);
                dataContext.SubmitChanges();
                Assert.IsTrue(dataContext.GetTable<Game>().Any(g => g.Id == 1));
                Assert.IsTrue(dataContext.GetTable<GameTask>().Any(t => t.Id == 1));
                Assert.AreEqual(1, dataContext.GetTable<Game>().First(g => g.Id == 1).Tasks.Count);
                Assert.IsNotNull(dataContext.GetTable<GameTask>().First(t => t.Id == 1).Game);

                #endregion

                #region Task <-> ABCDPossibleAnswer relation

                //adding abcdPossibleAnswer to task
                task = dataContext.GetTable<GameTask>().First();
                task.ABCDPossibleAnswers.Add(answer);
                Assert.AreSame(task, answer.Task);

                dataContext.GetTable<ABCDPossibleAnswer>().InsertOnSubmit(answer);
                dataContext.SubmitChanges();
                Assert.IsTrue(dataContext.GetTable<ABCDPossibleAnswer>().Any(a => a.Id == 1));
                Assert.AreEqual(1, dataContext.GetTable<GameTask>().First(t => t.Id == 1).ABCDPossibleAnswers.Count);
                Assert.IsNotNull(dataContext.GetTable<ABCDPossibleAnswer>().First(t => t.Id == 1).Task);

                //removing abcdPossibleAnswer from task
                task.ABCDPossibleAnswers.Remove(answer);
                Assert.IsNull(task.ABCDPossibleAnswers.FirstOrDefault());
                Assert.IsNull(answer.Task);
                dataContext.SubmitChanges();

                //removing
                dataContext.GetTable<Game>().DeleteOnSubmit(game);
                dataContext.GetTable<GameTask>().DeleteOnSubmit(task);
                dataContext.GetTable<ABCDPossibleAnswer>().DeleteOnSubmit(answer);
                dataContext.SubmitChanges();
                Assert.AreEqual(0, dataContext.GetTable<Game>().Count());
                Assert.AreEqual(0, dataContext.GetTable<GameTask>().Count());
                Assert.AreEqual(0, dataContext.GetTable<ABCDPossibleAnswer>().Count());

                CreateSampleEntities(out game, out task, out alert, out highScore, out answer);

                
                //adding task to abcdPossibleAnswer
                answer.Task = task;
                Assert.AreSame(answer, task.ABCDPossibleAnswers.First());

                dataContext.GetTable<GameTask>().InsertOnSubmit(task);
                dataContext.SubmitChanges();
                Assert.IsTrue(dataContext.GetTable<ABCDPossibleAnswer>().Any(a => a.Id == 1));
                Assert.IsTrue(dataContext.GetTable<GameTask>().Any(t => t.Id == 1));
                Assert.AreEqual(1, dataContext.GetTable<GameTask>().First(t => t.Id == 1).ABCDPossibleAnswers.Count);
                Assert.IsNotNull(dataContext.GetTable<ABCDPossibleAnswer>().First(t => t.Id == 1).Task);

                #endregion
            }
        }
        #endregion

        #region InterfaceRelationsTest
        [TestMethod]
        public void InterfaceRelationsTest()
        {
            IGame game;
            ITask task;
            IAlert alert;
            IHighScore highScore;
            IABCDPossibleAnswer answer;
            CreateSampleEntities(out game, out task, out alert, out highScore, out answer);

            using (UrbanGameDataContext dataContext = RecreateDatabase())
            {
                #region IGame <-> ITask relation

                //adding task to game
                game.Tasks.Add(task);
                Assert.AreSame(game, task.Game);

                dataContext.GetTable<Game>().InsertOnSubmit((Game)game);
                dataContext.SubmitChanges();
                Assert.IsTrue(dataContext.GetTable<Game>().Any(g => g.Id == 1));
                Assert.IsTrue(dataContext.GetTable<GameTask>().Any(t => t.Id == 1));
                Assert.AreEqual(1, dataContext.GetTable<Game>().First(g => g.Id == 1).Tasks.Count);
                Assert.IsNotNull(dataContext.GetTable<GameTask>().First(t => t.Id == 1).Game);

                //removing task from game
                game.Tasks.Remove(task);
                Assert.IsNull(task.Game);
                dataContext.SubmitChanges();

                //removing
                dataContext.GetTable<Game>().DeleteOnSubmit((Game)game);
                dataContext.GetTable<GameTask>().DeleteOnSubmit((GameTask)task);
                dataContext.SubmitChanges();
                Assert.AreEqual(0, dataContext.GetTable<Game>().Count());
                Assert.AreEqual(0, dataContext.GetTable<GameTask>().Count());

                CreateSampleEntities(out game, out task, out alert, out highScore, out answer);

                //adding game to task
                task.Game = game;
                Assert.AreSame(task, game.Tasks.FirstOrDefault());

                dataContext.GetTable<GameTask>().InsertOnSubmit((GameTask)task);
                dataContext.SubmitChanges();
                Assert.IsTrue(dataContext.GetTable<Game>().Any(g => g.Id == 1));
                Assert.IsTrue(dataContext.GetTable<GameTask>().Any(t => t.Id == 1));
                Assert.AreEqual(1, dataContext.GetTable<Game>().First(g => g.Id == 1).Tasks.Count);
                Assert.IsNotNull(dataContext.GetTable<GameTask>().First(t => t.Id == 1).Game);

                #endregion

                #region ITask <-> IABCDPossibleAnswer relation

                //adding abcdPossibleAnswer to task
                task = dataContext.GetTable<GameTask>().First();
                task.ABCDPossibleAnswers.Add(answer);
                Assert.AreSame(task, answer.Task);

                dataContext.GetTable<ABCDPossibleAnswer>().InsertOnSubmit((ABCDPossibleAnswer)answer);
                dataContext.SubmitChanges();
                Assert.IsTrue(dataContext.GetTable<ABCDPossibleAnswer>().Any(a => a.Id == 1));
                Assert.AreEqual(1, dataContext.GetTable<GameTask>().First(t => t.Id == 1).ABCDPossibleAnswers.Count);
                Assert.IsNotNull(dataContext.GetTable<ABCDPossibleAnswer>().First(t => t.Id == 1).Task);

                //removing abcdPossibleAnswer from task
                task.ABCDPossibleAnswers.Remove(answer);
                Assert.IsNull(task.ABCDPossibleAnswers.FirstOrDefault());
                Assert.IsNull(answer.Task);
                dataContext.SubmitChanges();

                //removing
                dataContext.GetTable<Game>().DeleteOnSubmit((Game)game);
                dataContext.GetTable<GameTask>().DeleteOnSubmit((GameTask)task);
                dataContext.GetTable<ABCDPossibleAnswer>().DeleteOnSubmit((ABCDPossibleAnswer)answer);
                dataContext.SubmitChanges();
                Assert.AreEqual(0, dataContext.GetTable<Game>().Count());
                Assert.AreEqual(0, dataContext.GetTable<GameTask>().Count());
                Assert.AreEqual(0, dataContext.GetTable<ABCDPossibleAnswer>().Count());

                CreateSampleEntities(out game, out task, out alert, out highScore, out answer);

                //adding task to abcdPossibleAnswer
                answer.Task = task;
                Assert.AreSame(answer, task.ABCDPossibleAnswers.First());

                dataContext.GetTable<GameTask>().InsertOnSubmit((GameTask)task);
                dataContext.SubmitChanges();
                Assert.IsTrue(dataContext.GetTable<ABCDPossibleAnswer>().Any(a => a.Id == 1));
                Assert.IsTrue(dataContext.GetTable<GameTask>().Any(t => t.Id == 1));
                Assert.AreEqual(1, dataContext.GetTable<GameTask>().First(t => t.Id == 1).ABCDPossibleAnswers.Count);
                Assert.IsNotNull(dataContext.GetTable<ABCDPossibleAnswer>().First(t => t.Id == 1).Task);

                #endregion
            }
        }
        #endregion
    }
}
