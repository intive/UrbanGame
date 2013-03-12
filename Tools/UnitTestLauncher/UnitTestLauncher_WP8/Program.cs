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
using Microsoft.SmartDevice.Connectivity.Interface;
using Microsoft.SmartDevice.MultiTargeting.Connectivity;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace UnitTestLauncher
{
    // Code base from: http://stackoverflow.com/questions/13420733/connect-to-windows-phone-8-using-console-application
    class Program
    {
        static readonly string UNIT_TESTS_DONE_REMOTE_FILE = "UnitTestDone.txt";
        static readonly string UNIT_TESTS_RESULT_REMOTE_FILE = "UnitTestResult.txt";

        enum Mode { EmulatorWP7, EmulatorWP8, DeviceWP7, DeviceWP8 };

        static void PrintUsage()
        {
            Console.WriteLine(AppDomain.CurrentDomain.FriendlyName + " \"device_name\" unitTestFile appId timeoutInSeconds appIconpath appXapPath");
            Console.WriteLine(AppDomain.CurrentDomain.FriendlyName + " \"Emulator WXGA\" UnitTestResult.txt {1aa2091e-a043-493f-9af1-a4c3e4245529} 15 ..\\UrbanGame\\UrbanGameTests\\Bin\\Debug\\ApplicationIcon.png ..\\UrbanGame\\UrbanGameTests\\Bin\\Debug\\UrbanGameTests.xap");
            Console.WriteLine("  Where device name can be: ");
            Console.WriteLine("    Device");
            Console.WriteLine("    Emulator WVGA 512MB");
            Console.WriteLine("    Emulator WVGA");
            Console.WriteLine("    Emulator WXGA");
            Console.WriteLine("    Emulator 720P");
            Console.WriteLine("    Emulator 7.1 256MB");
            Console.WriteLine("    Emulator 7.1");
        }

        static int Main(string[] args)
        {
            if (args.Length != 6)
            {
                PrintUsage();
                return -1;
            }

            string deviceName = args[0];
            string unitTestFile = args[1];
            Guid appID = new Guid(args[2]);
            int seconds = int.Parse(args[3]);
            string appIcon = args[4];
            string appXap = args[5];

            Console.WriteLine("Accessing to Windows Phone SDK");
            MultiTargetingConnectivity connectivity = new MultiTargetingConnectivity(CultureInfo.CurrentUICulture.LCID);

            ConnectableDevice connectableDevice = connectivity.GetConnectableDevices().Single(d => d.Name.Equals(deviceName, StringComparison.CurrentCultureIgnoreCase));
            Console.WriteLine("Found Connectable Device \'" + connectableDevice.Name + "\' for Device id {" + connectableDevice.Id + "}.");

            // Get Emulator / Device
            IDevice wpDevice = null;
            Console.WriteLine("Connecting to " + connectableDevice.Name + "...");
            try
            {
                wpDevice = connectableDevice.Connect();
            }
            catch (SmartDeviceException e)
            {
                Console.WriteLine(e.Message);
                return -1;
            }
            Console.WriteLine("Connected to " + connectableDevice.Name);

            IRemoteApplication app;
            if (wpDevice.IsApplicationInstalled(appID))
            {
                app = wpDevice.GetApplication(appID);
                Console.WriteLine("Uninstalling existing applicaiton...");
                app.Uninstall();
            }

            Console.WriteLine("Installing new version of application...");
            try
            {
                app = wpDevice.InstallApplication(appID, appID, "NormalApp", appIcon, appXap);
            }
            catch (ArgumentException)
            {
                Console.WriteLine("Failed to install application, make sure that path to application is correct");
                return -1;
            }

            Console.WriteLine("Launching the application...");
            var store = app.GetIsolatedStore();
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
