using Newtonsoft.Json.Linq;
using Flurl.Http;
using OQRPC.settings;

namespace OQRPC.updating {

    class UpdateChecker {

        private static string updateUrl = "https://raw.githubusercontent.com/madmagic007/Oculus-Quest-Presence/v3-development/update.json";
        private static string version = "3.0.0";

        public static void Check() {

            JObject o = JObject.Parse(updateUrl.GetStringAsync().Result);

            bool app = !((string)o["latest"]).Equals(version);
            bool apk = Config.cfg.apkVersion != null && !Config.cfg.apkVersion.Equals((string)o["apkVersion"]);

            if (apk || app) new UpdaterGUI(apk, app, o).Show();
        }
    }
}
