using Flurl.Http;
using Newtonsoft.Json.Linq;
using Oculus_Quest_Presence.Properties;
using OQRPC.settings;
using System.Diagnostics;
using System.Drawing;
using System.Net;
using System.Windows.Forms;

namespace OQRPC.updating {

    class UpdaterGUI : Form {

        private JObject o;

        public UpdaterGUI(bool apk, bool self, JObject o) {
            this.o = o;
            ClientSize = new Size(230, 70);
            Text = Resources.tag + " update";
            Icon = Resources.AppIcon;
            MinimizeBox = false;
            MaximizeBox = false;
            FormBorderStyle = FormBorderStyle.FixedSingle;

            Label lblSelf = new() {
                Text = self ? "New version found" : "No new version found",
                AutoSize = true,
                Location = new Point(3, 7),
            };
            Controls.Add(lblSelf);

            Button btnSelf = new() {
                Text = "Download",
                Enabled = self,
                Height = 22,
                AutoSize = true
            };
            btnSelf.Location = new Point(ClientSize.Width - btnSelf.Width - 5, 3);
            Controls.Add(btnSelf);
            btnSelf.Click += BtnSelf_Click;

            Label lblApk = new() {
                Text = apk ? "New Apk version found" : "No new apk version found",
                Location = new Point(3, GetEndY(btnSelf) + 10),
                AutoSize = true
            };
            Controls.Add(lblApk);

            Button btnApk = new() {
                Text = "Download",
                Enabled = apk,
                Height = 22,
                AutoSize = true
            };
            btnApk.Click += BtnApk_Click;
            btnApk.Location = new Point(ClientSize.Width - btnApk.Width - 5, lblApk.Location.Y - 4);
            Controls.Add(btnApk);
        }

        private void BtnApk_Click(object? sender, EventArgs e) {
            string url = (string)o["apkUrl"];
            string dir = Config.dir + "\\OQRPC.apk";
            DownloadTo(dir, url).DownloadFileCompleted += (_, _) => {
                DialogResult d = MessageBox.Show("Updated apk ready to install. Make sure quest is connected via usb", "OQRPC apk update ready to install", MessageBoxButtons.OKCancel, MessageBoxIcon.Information);
                ADBUtils au = new ADBUtils();
                if (d == DialogResult.OK) {

                    if (au.TryGetAddress().Equals("")) {
                        MessageBox.Show("Quest not connected, make sure quest is connected to proceed", "Quest not connected", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                        return;
                    }

                    au.Install(dir);
                    Program.SendNotif("OQPRC successfully updated on quest");
                    au.Launch();
                    au.Stop();
                }
            };
        }

        private async void BtnSelf_Click(object? sender, EventArgs e) {
            string url = (string)o["installer"];
            string dir = Config.dir + "\\OQRPC.msi";
            DownloadTo(dir, url).DownloadFileCompleted += (_, _) => {
                Process.Start(dir);
                Program.trayIcon.Visible = false;
                Environment.Exit(0);
            };
        }

        private WebClient DownloadTo(string dir, string url) {
            using WebClient wc = new();
            wc.DownloadFileAsync(new Uri(url), dir);
            return wc;
        }

        private int GetEndY(Control c) {
            return c.Location.Y + c.Height;
        }
    }
}
