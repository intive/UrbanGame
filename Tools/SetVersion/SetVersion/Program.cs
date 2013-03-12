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
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Linq;

namespace SetVersion
{
    class Program
    {
        static int Main(string[] args)
        {
            if (args.Length != 2)
            {
                Console.WriteLine(AppDomain.CurrentDomain.FriendlyName + @" WMAppManifest.xml buildNumber");
                Console.WriteLine(AppDomain.CurrentDomain.FriendlyName + @" ..\UrbanGame\UrbanGame\Properties\WMAppManifest.xml 25");
                return -1;
            }

            try
            {
                var xml = XDocument.Load(args[0]);
                foreach (var i in xml.Descendants("App"))
                {
                    var versionStr = i.Attribute("Version").Value.ToString();
                    var numbers = versionStr.Split('.');
                    numbers[numbers.Length - 1] = args[1];
                    versionStr = String.Join(".", numbers);
                    i.SetAttributeValue("Version", versionStr);
                }
                xml.Save(args[0]);

                return 0;
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                return -1;
            }
        }
    }
}
