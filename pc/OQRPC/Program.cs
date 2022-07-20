using Microsoft.Win32;
using Newtonsoft.Json.Linq;
using Oculus_Quest_Presence.Properties;
using OQRPC.api;
using OQRPC.settings;
using OQRPC.updating;
using System.Net;
using System.Net.Sockets;
using System.Windows.Forms;

namespace OQRPC {

    class Program : ApplicationContext {

        [STAThread]
        static void Main(string[] args) {
            Config.Init();
            bool forceUpdate = args.Length > 0 && args[0].Equals("forceUpdate");
            if (args.Length > 0 && args[0].Equals("boot") && !Config.cfg.boot) Application.Exit();

            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);

            Application.Run(new Program(forceUpdate));
        }

        public static NotifyIcon trayIcon;

        public Program(bool forceUpdate) {
            trayIcon = new NotifyIcon {
                Text = Resources.name,
                Icon = Resources.AppIcon,
                ContextMenuStrip = new ContextMenuStrip(),
                Visible = true
            };

            trayIcon.ContextMenuStrip.Items.AddRange(new ToolStripItem[] {
                new ToolStripMenuItem("Request presence restart", null, (_, e) => {
                    ApiSender.Post(new JObject {
                        ["message"] = "startup"
                    });
                }),
                new ToolStripMenuItem("Settings", null, (_, e) => SettingsGui.GetIfOpen<SettingsGui>(true)?.Show()),
                new ToolStripSeparator(),
                new ToolStripMenuItem("Exit", null, (_, e) => {
                    trayIcon.Dispose();
                    Environment.Exit(0);
                }),
            });

            new ApiSocket();

            if (Config.cfg.address == null) new SettingsGui();
            else {
                Task.Run(() => {
                    ApiSender.Post(new JObject {
                        ["message"] = "startup"
                    });
                });
            }

            UpdateChecker.Check(forceUpdate);
        }

        public static void SendNotif(string text) {
            trayIcon.ShowBalloonTip(0, Resources.name, text, ToolTipIcon.Info);
        }

        private static string address; //only run below code once
        public static string GetOwnAddress() {
            if (address != null) return address;

            foreach (var ip in Dns.GetHostEntry(Dns.GetHostName()).AddressList) {
                if (ip.AddressFamily == AddressFamily.InterNetwork) {
                    address = ip.ToString();
                    return ip.ToString();
                }
            }
            return "";
        }
    }
}
