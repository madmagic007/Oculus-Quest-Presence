using OQRPC.settings;
using SharpAdbClient;
using SharpAdbClient.DeviceCommands;
using System;
using System.Text;
using System.Windows.Forms;

namespace OQRPC {
    class ADBUtils {

        private static string dir = Config.dir.FullName + "/adb.exe";
        private AdbServer server;
        private AdbClient client;
        private DeviceData device;

        public ADBUtils() {
            if (server != null) return;
            server = new AdbServer();
            server.StartServer(dir, false);
            client = new AdbClient();
        }

        public string TryGetAddress() {
            ConsoleOutputReceiver rec = new ConsoleOutputReceiver();
            foreach (var device in client.GetDevices()) {
                //if (!device.Model.ToLower().Equals("quest")) continue;
                client.ExecuteRemoteCommand("ip addr show wlan0 | grep 'link/ether '", device, rec);
                //if (!rec.ToString().ToLower().Contains("2c:26:17")) continue;
                this.device = device;
                rec = new ConsoleOutputReceiver();
                client.ExecuteRemoteCommand("ip addr show wlan0 | grep 'inet' | awk '{print $2}' | awk -F'/' '{print $1}'", device, rec);
                string[] split = rec.ToString().Split('\n');
                if (split.Length == 0) continue;
                return split[0];
            }
            return "";
        }

        public bool IsInstalled() {
            if (device == null) return true;
            Console.WriteLine(device == null);
            PackageManager pm = new PackageManager(client, device);
            return pm.Packages.ContainsKey("com.madmagic.oqrpc");
        }

        public void Install() {
            PackageManager pm = new PackageManager(client, device);
            OpenFileDialog ofd = new OpenFileDialog {
                Title = "Select OQRPC apk",
                InitialDirectory = ".",
                Filter = "APK files (*.apk)|*.apk",
                Multiselect = false
            };
            if (ofd.ShowDialog().Equals(false)) return;
            pm.InstallPackage(ofd.FileName, reinstall: false);
        }

        public void Launch() {
            client.ExecuteRemoteCommand("monkey -p com.madmagic.oqrpc 1", device, null);
        }
    }
}
