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
    }
}
