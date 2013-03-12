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
