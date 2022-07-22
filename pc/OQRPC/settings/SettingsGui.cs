using Flurl.Http;
using Newtonsoft.Json.Linq;
using Oculus_Quest_Presence.Properties;
using OQRPC.api;
using System;
using System.Drawing;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace OQRPC.settings {

    class SettingsGui : Form {

        private TextBox tbAddress;
        private Label txtFeedback;
        private CheckBox cbBoot;
        private CheckBox cbSleepWake;
        private CheckBox cbNotifs;
        private NumericUpDown nudDelay;

        private bool validated;
        private Config c;

        public SettingsGui() {
            c = Config.cfg;

            ClientSize = new Size(300, 171);
            Text = Resources.name + " settings";
            Icon = Resources.AppIcon;
            MinimizeBox = false;
            MaximizeBox = false;
            FormBorderStyle = FormBorderStyle.FixedSingle;

            Label txtAddress = new Label {
                Text = "Quest address:",
                Location = new Point(3, 3),
                AutoSize = true
            };
            Controls.Add(txtAddress);

            Button btnValidate = new Button {
                Text = "Validate",
                Height = 22
            };
            Controls.Add(btnValidate);
            btnValidate.Location = new Point(ClientSize.Width - btnValidate.Width - 5, GetEndY(txtAddress) + 4);
            btnValidate.Click += BtnValidate_Click;


            string address = c.address;
            bool already = address != null;
            this.tbAddress = new TextBox {
                Text = address ?? "Not Set",
                Location = new Point(5, btnValidate.Location.Y + 1),
                Width = btnValidate.Location.X - 12,
            };
            Controls.Add(this.tbAddress);

            txtFeedback = new Label {
                Text = already ? "" : "Attempting to get address automatically...",
                Location = new Point(3, GetEndY(this.tbAddress) + 5),
                Width = ClientSize.Width - 10
            };
            Controls.Add(txtFeedback);

            Label txtDelay = new Label {
                Text = "Presence update delay (Seconds)",
                Location = new Point(3, GetEndY(txtFeedback) + 2),
            };
            Controls.Add(txtDelay);

            nudDelay = new NumericUpDown {
                Value = 3,
                Minimum = 1,
                Location = new Point(btnValidate.Location.X, txtDelay.Location.Y),
                Width = btnValidate.Width -2,
            };
            Controls.Add(nudDelay);
            txtDelay.Width = nudDelay.Location.X - 10;

            cbBoot = new CheckBox {
                Text = "Start with windows",
                Checked = c.boot,
                Location = new Point(5, GetEndY(txtDelay) + 5),
                AutoSize = true,
            };
            Controls.Add(cbBoot);

            cbSleepWake = new CheckBox {
                Text = "Pause presence when quest screen turns off",
                Checked = c.sleepWake,
                Location = new Point(5, GetEndY(cbBoot) + 5),
                AutoSize= true
            };
            Controls.Add(cbSleepWake);

            cbNotifs = new CheckBox {
                Text = "Notify when presence starts/stops",
                Checked = c.notifs,
                Location = new Point(5, GetEndY(cbSleepWake) + 5),
                AutoSize = true
            };
            Controls.Add(cbNotifs);

            Button btnSave = new Button {
                Text = "Save",
                Size = btnValidate.Size,
                Location = new Point(ClientSize.Width - btnValidate.Width - 5, ClientSize.Height - btnValidate.Height - 3),
            };
            Controls.Add(btnSave);
            btnSave.Click += (_, e) => {
                if (validated) c.address = tbAddress.Text;
                c.boot = cbBoot.Checked;
                c.sleepWake = cbSleepWake.Checked;
                c.notifs = cbNotifs.Checked;
                c.delay = (int)nudDelay.Value;
                Config.Save();
                Close();
            };
            
            Show();

            Task.Run(() => {
                ADBUtils au = new();

                FormClosed += (_, _) => {
                    au.Stop();
                };

                if (!already) {
                    string adbAddress = au.TryGetAddress();
                    if (adbAddress != null) {
                        txtFeedback.Text = "Address automatically retrieved";
                        tbAddress.Text = adbAddress.Trim();
                    } else txtFeedback.Text = "Failed to retrieve address automatically";
                }

                if (au.IsInstalled()) return;
                DialogResult d = MessageBox.Show("OQRPC app was not detected on your quest, install now?", "OQRPC not detected on quest", MessageBoxButtons.OKCancel, MessageBoxIcon.Information);
                if (d == DialogResult.OK) Invoke(() => {
                    au.Install();
                    Program.SendNotif("OQPRC successfully installed on quest");
                    au.Launch();
                });
            });
        }

        private void BtnValidate_Click(object sender, EventArgs e) {
            string address = tbAddress.Text.Trim();
            validated = false;

            Task.Run(() => {
                if (!IPUtils.IsAddress(address) || string.IsNullOrEmpty(address)) {
                    txtFeedback.Text = "Not a valid address";
                    return;
                }

                string mac = IPUtils.GetMac(address);
                if (mac.Equals("00-00-00-00-00-00")) {
                    txtFeedback.Text = "No device found @" + address;
                    return;
                } else if (!mac.StartsWith("2C-26-17")) {
                    txtFeedback.Text = "Device found @" + address + " is not a quest";
                    return;
                }

                try {
                    JObject o = ApiSender.Post(new JObject {
                        ["message"] = "validate"
                    }, address);
                    if (!o.ContainsKey("valid")) throw new Exception();
                    txtFeedback.Text = "successfully validated quest";
                    validated = true;
                } catch (Exception ex) {
                    Console.WriteLine(ex.InnerException);
                    txtFeedback.Text = "Quest found but service didn't respond";
                }
            });
        }

        private int GetEndY(Control c) {
            return c.Location.Y + c.Height;
        }

        public static T GetIfOpen<T>(bool show = false) where T : Form, new() {
            foreach (Form f in Application.OpenForms) if (f is T t) return t;
            if (show) new T().Show();
            return (T)(object)null;
        }
    }
}