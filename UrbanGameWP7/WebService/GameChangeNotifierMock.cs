using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Common;
using Caliburn.Micro;
using System.Threading.Tasks;
using System.Threading;

namespace WebService
{
    public class GameChangeNotifierMock : IGameChangeNotifier
    {
        IGameWebService _gameWebService;
        IEventAggregator _gameEventAggregator;
        Timer _mockTimer;

        public GameChangeNotifierMock(IGameWebService gameWebService, IEventAggregator gameEventAggregator, int changeInterval = 8000)
        {
            _gameWebService = gameWebService;
            _gameEventAggregator = gameEventAggregator;
            _mockTimer = new Timer(new TimerCallback(RandomChange), null, changeInterval, changeInterval);
        }

        public void GameChanged(int gid)
        {
            Task.Run( () => _gameEventAggregator.Publish(_gameWebService.GetGameInfo(gid)) );            
        }

        private void RandomChange(object obj)
        {
            GameChanged(new Random().Next(1, 5));
        }
    }
}
