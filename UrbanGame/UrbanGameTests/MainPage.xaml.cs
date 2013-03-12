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

using System;
using System.Collections.Generic;
using System.IO;
using System.IO.IsolatedStorage;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using Microsoft.Silverlight.Testing;
using Microsoft.Silverlight.Testing.Harness;
using UrbanGameTests.Resources;

namespace UrbanGameTests
{
    public partial class MainPage : PhoneApplicationPage
    {
        // Constructor
        public MainPage()
        {
            InitializeComponent();
            this.Loaded += new RoutedEventHandler(MainPage_Loaded);
        }

        void MainPage_Loaded(object sender, RoutedEventArgs e)
        {
            SystemTray.IsVisible = false;
            UnitTestSettings settings = UnitTestSystem.CreateDefaultSettings();
            settings.TestHarness.TestHarnessCompleted += TestRunCompletedCallback;

            var testPage = UnitTestSystem.CreateTestPage(settings);
            (Application.Current.RootVisual as PhoneApplicationFrame).Content = testPage;
        }

        void TestRunCompletedCallback(object sender, TestHarnessCompletedEventArgs e)
        {
            using (var isoStore = IsolatedStorageFile.GetUserStoreForApplication())
            {
                using (var sw = new StreamWriter(isoStore.OpenFile("UnitTestResult.txt", FileMode.OpenOrCreate, FileAccess.Write)))
                {
                    var testHarness = sender as UnitTestHarness;
                    foreach (var result in testHarness.Results)
                    {
                        string value = null;
                        switch (result.Result)
                        {
                            case TestOutcome.Completed:
                            case TestOutcome.Passed:
                                value = "PASS: (" + (result.Finished - result.Started).TotalMilliseconds + "ms) " + result.TestClass + "." + result.TestMethod;

                                break;
                            default:
                                value = "FAILED: " + result.TestClass + "." + result.TestMethod;
                                break;
                        }
                        if (value != null)
                        {
                            sw.WriteLine(value);
                            System.Diagnostics.Debug.WriteLine(value);
                        }
                    }
                }

                // Mark that unit tests are done
                using (var f = isoStore.OpenFile("UnitTestDone.txt", FileMode.OpenOrCreate, FileAccess.Write))
                {
                    f.Close();
                }
            }
        }
    }
}