using Flurl.Http;
using Newtonsoft.Json.Linq;

namespace OQRPC.presence {
    class PresenceHandler {

        private static JObject gitObj;

        static PresenceHandler() {
            gitObj = JObject.Parse(("https://raw.githubusercontent.com/madmagic007/Oculus-Quest-Presence/master/lang.json").GetStringAsync().Result);
        }


        public static void Handle(JObject o) {
            Parser p = new Parser(o);

            if (!o.ContainsKey("appId")) {
                o["details"] = p.Get("details", "Currently playing:");
                if (!p.hasGitDetails) o["state"] = p.Get("state", p.name);
                o["largeImageKey"] = p.Get("largeImageKey", "");
                o["largeImageText"] = p.Get("largeImageText", "");
                o["smallImageKey"] = p.Get("smallImageKey", "");
                o["smallImageText"] = p.Get("smallImageText", "");
            }

            System.Console.WriteLine(o.ToString(Newtonsoft.Json.Formatting.Indented));

            if (p.IsEmpty("largeImageKey")) {
                o["largeImageKey"] = "quest";
                o["largeImageText"] = "OQRPC by MadMagic";
            } else if (p.IsEmpty("smallImageKey")) {
                o["smallImageKey"] = "quest";
                o["smallImageText"] = "OQRPC by MadMagic";
            }

            DiscordHandler.Handle(o);
        }

        public static void Stop() {
            Timers.StopAll();
            Program.SendNotif("Presence on your quest has stopped");
            DiscordHandler.StopPresence();
        }

        internal class Parser {

            internal string packageName;
            internal string name;
            JObject o;
            internal bool hasGitDetails;


            public Parser(JObject o) {
                this.o = o;
                packageName = (string)o["packageName"];
                name = (string)o["name"];
            }

            public string Get(string tag, string fallback) {
                if (!gitObj.ContainsKey(packageName)) return fallback;
                JObject gameObj = (JObject)gitObj[packageName];
                hasGitDetails = tag == "details" && gameObj.ContainsKey(tag);
                return (string)gameObj[tag] ?? fallback;
            }

            public bool IsEmpty(string tag) {
                return !o.ContainsKey(tag) || ((string)o[tag]).Equals("");
            }
        }
    }
}
