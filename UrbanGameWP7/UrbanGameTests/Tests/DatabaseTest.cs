using Common;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;
using System.Data.Linq;
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
        private void CreateSampleEntities(out Game game, out GameTask task, out GameAlert alert, out GameHighScore highScore, out ABCDPossibleAnswer possibleAnswer, out ABCDUserAnswer userAnswer)
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

            possibleAnswer = new ABCDPossibleAnswer()
            {
                Id = 1,
                Answer = "The width of this spear is about 2m",
            };

            userAnswer = new ABCDUserAnswer()
            {
                Id = 1,
                Answer = false
            };
        }

        private void CreateSampleEntities(out IGame game, out ITask task, out IAlert alert, out IHighScore highScore, out IABCDPossibleAnswer possibleAnswer, out IABCDUserAnswer userAnswer)
        {
            Game g;
            GameTask t;
            GameAlert al;
            GameHighScore h;
            ABCDPossibleAnswer pa;
            ABCDUserAnswer ua;
            CreateSampleEntities(out g, out t, out al, out h, out pa, out ua);

            game = g;
            task = t;
            alert = al;
            highScore = h;
            possibleAnswer = pa;
            userAnswer = ua;
        }
        #endregion

        #region TestEntitiesRelationship
        private void TestEntitiesRelationship<TParent, TChild>(Func<TParent> createSampleParent,
                                                               Func<TChild> createSampleChild,
                                                               Func<TParent, EntitySet<TChild>> getChildren,
                                                               Func<TChild, TParent> getParent,
                                                               Action<TChild, TParent> setParent)
            where TParent : class
            where TChild : class
        {
            TParent parent = createSampleParent();
            TChild child = createSampleChild();            

            using (UrbanGameDataContext dataContext = RecreateDatabase())
            {
                dataContext.GetTable<TParent>().InsertOnSubmit(parent);
                dataContext.SubmitChanges();

                //adding child to parent
                parent = dataContext.GetTable<TParent>().First();
                getChildren(parent).Add(child);
                Assert.AreSame(parent, getParent(child));

                dataContext.GetTable<TChild>().InsertOnSubmit(child);
                dataContext.SubmitChanges();
                Assert.IsTrue(dataContext.GetTable<TChild>().Count() == 1);
                Assert.AreEqual(1, getChildren(dataContext.GetTable<TParent>().First()).Count);
                Assert.IsNotNull(getParent(dataContext.GetTable<TChild>().First()));

                //removing child from parent
                getChildren(parent).Remove(child);
                Assert.IsNull(getChildren(parent).FirstOrDefault());
                Assert.IsNull(getParent(child));
                dataContext.SubmitChanges();

                //removing
                dataContext.GetTable<TChild>().DeleteOnSubmit(child);
                dataContext.GetTable<TParent>().DeleteOnSubmit(parent);
                dataContext.SubmitChanges();
                Assert.AreEqual(0, dataContext.GetTable<TParent>().Count());
                Assert.AreEqual(0, dataContext.GetTable<TChild>().Count());

                parent = createSampleParent();
                child = createSampleChild(); 


                //adding parent to child
                setParent(child, parent);
                Assert.AreSame(child, getChildren(parent).First());

                dataContext.GetTable<TParent>().InsertOnSubmit(parent);
                dataContext.SubmitChanges();
                Assert.AreEqual(1, dataContext.GetTable<TChild>().Count());
                Assert.AreEqual(1, dataContext.GetTable<TParent>().Count());
                Assert.AreEqual(1, getChildren(dataContext.GetTable<TParent>().First()).Count);
                Assert.IsNotNull(getParent(dataContext.GetTable<TChild>().First()));
            }
        }
        #endregion

        #region TestInterfaceRelationship
        private void TestInterfaceRelationship<TParent, TChild, TParentEntity, TChildEntity>(
                                                                Func<TParent> createSampleParent, 
                                                                Func<TChild> createSampleChild,                                                                
                                                                Func<TParent, IEntityEnumerable<TChild>> getChildren,
                                                                Func<TChild, TParent> getParent,
                                                                Action<TChild, TParent> setParent)
            where TParent : class
            where TChild : class
            where TParentEntity : class, TParent
            where TChildEntity : class, TChild
        {
            TParent parent = createSampleParent();
            TChild child = createSampleChild();

            using (UrbanGameDataContext dataContext = RecreateDatabase())
            {
                dataContext.GetTable<TParentEntity>().InsertOnSubmit((TParentEntity)parent);
                dataContext.SubmitChanges();

                //adding child to parent
                parent = dataContext.GetTable<TParentEntity>().First();
                getChildren(parent).Add(child);
                Assert.AreSame(parent, getParent(child));

                dataContext.GetTable<TChildEntity>().InsertOnSubmit((TChildEntity)child);
                dataContext.SubmitChanges();
                Assert.IsTrue(dataContext.GetTable<TChildEntity>().Count() == 1);
                Assert.AreEqual(1, getChildren(dataContext.GetTable<TParentEntity>().First()).Count());
                Assert.IsNotNull(getParent(dataContext.GetTable<TChildEntity>().First()));

                //removing child from parent
                getChildren(parent).Remove(child);
                Assert.IsNull(getChildren(parent).FirstOrDefault());
                Assert.IsNull(getParent(child));
                dataContext.SubmitChanges();

                //removing
                dataContext.GetTable<TChildEntity>().DeleteOnSubmit((TChildEntity)child);
                dataContext.GetTable<TParentEntity>().DeleteOnSubmit((TParentEntity)parent);
                dataContext.SubmitChanges();
                Assert.AreEqual(0, dataContext.GetTable<TParentEntity>().Count());
                Assert.AreEqual(0, dataContext.GetTable<TChildEntity>().Count());

                parent = createSampleParent();
                child = createSampleChild();


                //adding parent to child
                setParent(child, parent);
                Assert.AreSame(child, getChildren(parent).First());

                dataContext.GetTable<TParentEntity>().InsertOnSubmit((TParentEntity)parent);
                dataContext.SubmitChanges();
                Assert.AreEqual(1, dataContext.GetTable<TChildEntity>().Count());
                Assert.AreEqual(1, dataContext.GetTable<TParentEntity>().Count());
                Assert.AreEqual(1, getChildren(dataContext.GetTable<TParentEntity>().First()).Count());
                Assert.IsNotNull(getParent(dataContext.GetTable<TChildEntity>().First()));
            }
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
            ABCDPossibleAnswer possibleAnswer;
            ABCDUserAnswer userAnswer;
            CreateSampleEntities(out game, out task, out alert, out highScore, out possibleAnswer, out userAnswer);            

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

                CreateSampleEntities(out game, out task, out alert, out highScore, out possibleAnswer, out userAnswer);

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

                #region TaskSolution <-> ABCDUserAnswer relation

                possibleAnswer.ABCDUserAnswers.Add(userAnswer);
                Assert.AreSame(possibleAnswer, userAnswer.ABCDPossibleAnswer);


                #endregion
            }

            #region Task <-> ABCDPossibleAnswer relation

            Func<GameTask> sampleGameTask = () => new GameTask()
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
            Func<ABCDPossibleAnswer> sampleABCDPossibleAnswer = () => new ABCDPossibleAnswer()
            {
                Id = 1,
                Answer = "Simple answer"
            };

            TestEntitiesRelationship<GameTask, ABCDPossibleAnswer>(sampleGameTask, sampleABCDPossibleAnswer,
                                                                   t => t.ABCDPossibleAnswers,
                                                                   a => a.Task,
                                                                   (a, t) => a.Task = t);
            #endregion

            #region TaskSolution <-> ABCDUserAnswer relation

            Func<TaskSolution> sampleTaskSolution = () => new TaskSolution()
            {
                Id = 1,
                TaskType = TaskType.ABCD
            };
            Func<ABCDUserAnswer> sampleAnswer = () => new ABCDUserAnswer()
            {
                Id = 1,
                ABCDPossibleAnswerId  = 1,
                Answer = false
            };

            TestEntitiesRelationship<TaskSolution, ABCDUserAnswer>(sampleTaskSolution, sampleAnswer,
                                                                   solution => solution.ABCDUserAnswers,
                                                                   ua => ua.Solution,
                                                                   (ua, solution) => ua.Solution = solution);

            #endregion

            #region Task <-> TaskSolution relation

            Func<GameTask> sampleTask = () => new GameTask()
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

            TestEntitiesRelationship<GameTask, TaskSolution>(sampleTask, sampleTaskSolution,
                                                             t => t.Solutions,
                                                             s => s.Task,
                                                             (s, t) => s.Task = t);

            #endregion
        }
        #endregion

        #region InterfaceRelationsTest
        [TestMethod]
        public void InterfaceRelationsTest()
        {
            Func<ITask> sampleGameTask = () => new GameTask()
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

            #region IGame <-> ITask relation

            Func<IGame> sampleGame = () => new Game()
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

            TestInterfaceRelationship<IGame, ITask, Game, GameTask>(sampleGame, sampleGameTask,
                                                                    g => g.Tasks,
                                                                    t => t.Game,
                                                                    (t, g) => t.Game = g);

            #endregion                                        

            #region ITask <-> IABCDPossibleAnswer relation

            Func<IABCDPossibleAnswer> sampleABCDPossibleAnswer = () => new ABCDPossibleAnswer()
            {
                Id = 1,
                Answer = "Simple answer"
            };

            TestInterfaceRelationship<ITask, IABCDPossibleAnswer,
                                     GameTask, ABCDPossibleAnswer>(sampleGameTask, sampleABCDPossibleAnswer,
                                                                   t => t.ABCDPossibleAnswers,
                                                                   a => a.Task,
                                                                   (a, t) => a.Task = t);
            #endregion

            #region ITask <-> ITaskSolution relation

            Func<IBaseSolution> sampleTaskSolution = () => new TaskSolution()
            {
                Id = 1,
                TaskType = TaskType.ABCD
            };
            Func<ITask> sampleTask = () => new GameTask()
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

            TestInterfaceRelationship<ITask, IBaseSolution, GameTask, TaskSolution>(sampleTask, sampleTaskSolution,
                                                             t => t.Solutions,
                                                             s => s.Task,
                                                             (s, t) => s.Task = t);

            #endregion

            #region IABCDPossibleAnswer <-> IABCDUserAnswer relation

            Func<IABCDUserAnswer> sampleUserAnswer = () => new ABCDUserAnswer
            {
                Id = 1,
                Answer = false
            };

            TestInterfaceRelationship<IABCDPossibleAnswer, IABCDUserAnswer, ABCDPossibleAnswer, ABCDUserAnswer>(sampleABCDPossibleAnswer, sampleUserAnswer,
                                                                    a => a.ABCDUserAnswers,
                                                                    us => us.ABCDPossibleAnswer,
                                                                    (us, a) => us.ABCDPossibleAnswer = a);

            #endregion
        }
        #endregion
    }
}
