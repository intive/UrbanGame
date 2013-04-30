using System;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using System.Data.Linq;
using System.IO.IsolatedStorage;
using System.Collections.ObjectModel;


namespace UrbanGame.Storage
{
    public class UrbanGameDataContext : DataContext
    {
        public static string DBConnectionString = "Data Source=isostore:/UrbanGame.sdf";

        public UrbanGameDataContext()
            : base(UrbanGameDataContext.DBConnectionString)
        {
            if (!DatabaseExists())
                CreateDatabase();
        }

        public UrbanGameDataContext(string connectionString) 
            : base(connectionString)
        {            
        }

        public Table<Game> Games;

        public Table<GameTask> Tasks;

        public Table<ABCDPossibleAnswer> ABCDPossibleAnswers;

        public Table<TaskSolution> Solutions;        
    }
}
