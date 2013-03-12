/*
 * Copyright 2013 BLStream, BLStream's Patronage Program Contributors
 * http://blstream.github.com/UrbanGame/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

using Microsoft.SmartDevice.Connectivity;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace UnitTestLauncher
{
    // Code base from: http://justinangel.net/WindowsPhone7EmulatorAutomation
    class Program
    {
        static readonly string UNIT_TESTS_DONE_REMOTE_FILE = "UnitTestDone.txt";
        static readonly string UNIT_TESTS_RESULT_REMOTE_FILE = "UnitTestResult.txt";

        static int Main(string[] args)
        {
            if (args.Length != 6)
            {
                Console.WriteLine(AppDomain.CurrentDomain.FriendlyName + @" emulator|device unitTestFile appId timeoutInSeconds appIconpath appXapPath");
                Console.WriteLine(AppDomain.CurrentDomain.FriendlyName + @" device UnitTestResult.txt {1aa2091e-a043-493f-9af1-a4c3e4245529} 15 ..\PlanetSaver\PlanetSaverTests\Bin\Debug\ApplicationIcon.png ..\PlanetSaver\PlanetSaverTests\Bin\Debug\PlanetSaverTests.xap");
                return -1;
            }

            bool useEmulator = args[0].Equals("emulator");
            string unitTestFile = args[1];
            Guid appID = new Guid(args[2]);
            int seconds = int.Parse(args[3]);
            string appIcon = args[4];
            string appXap = args[5];

            Console.WriteLine("Accessing to Windows Phone 7 SDK");
            DatastoreManager datastore = new DatastoreManager(1033);
            Platform wp7 = datastore.GetPlatforms().Single(p => p.Name == "Windows Phone 7");

            // Get Emulator / Device
            Device wp7Device = null;
            if (useEmulator)
            {
                wp7Device = wp7.GetDevices().Single(d => d.Name.Contains("Emulator"));
            }
            else
            {
                wp7Device = wp7.GetDevices().Single(d => d.Name.Contains("Device"));
            }

            Console.WriteLine("Connecting to " + wp7Device.Name + "...");
            try
            {
                wp7Device.Connect();
            }
            catch (SmartDeviceException e)
            {
                Console.WriteLine(e.Message);
                return -1;
            }
            Console.WriteLine("Connected to " + wp7Device.Name);

            RemoteApplication app;
            if (wp7Device.IsApplicationInstalled(appID))
            {
                app = wp7Device.GetApplication(appID);
                Console.WriteLine("Uninstalling existing applicaiton...");
                app.Uninstall();
            }

            Console.WriteLine("Installing new version of application...");
            try
            {
                app = wp7Device.InstallApplication(appID, appID, "NormalApp", appIcon, appXap);
            }
            catch (ArgumentException)
            {
                Console.WriteLine("Failed to install application, make sure that path to application is correct");
                return -1;
            }

            Console.WriteLine("Launching the application...");
            var store = app.GetIsolatedStore();
            try
            {
                store.DeleteFile(UNIT_TESTS_DONE_REMOTE_FILE); // Remove the file, just in case
            }
            catch (FileNotFoundException)
            {
                // That's fine, nothing to do
            }
            app.Launch();

            Console.WriteLine("Waiting for result...");
            int count = 0;
            while (! store.FileExists(UNIT_TESTS_DONE_REMOTE_FILE))
            {
                Thread.Sleep(1000);
                ++count;
                if (count > seconds)
                {
                    Console.WriteLine("Failed to get test result");
                    return -1;
                }
            }
            
            Console.WriteLine("Getting results");
            try
            {
                store.ReceiveFile(UNIT_TESTS_RESULT_REMOTE_FILE, unitTestFile, true);
            }
            catch (FileNotFoundException)
            {
                Console.WriteLine("Failed to test result file " + UNIT_TESTS_RESULT_REMOTE_FILE);
                return -1;
            }
            app.TerminateRunningInstances();
            string content = File.ReadAllText(unitTestFile);
            if (content.Contains("FAILED"))
            {
                Console.WriteLine("Failed: Unit test failed, check " + unitTestFile + " for details");
                Console.WriteLine(content);
                return -1;
            }
            Console.WriteLine("OK: All test passed");
            return 0;
        }
    }
}
