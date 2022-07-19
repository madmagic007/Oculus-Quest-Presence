using DiscordRPC;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace OQRPC.presence {

    class DiscordHandler {

        private static DiscordRpcClient client;
        private static string lastId = "";

        public static void Init(string id = "664525664946356230") {
            if (!lastId.Equals(id)) StopPresence();

            client = new DiscordRpcClient(id);
            lastId = id;
            client.Initialize();

            client.SetPresence(new RichPresence {
                Details = "Just started playing",
                Assets = new Assets {
                    LargeImageKey = "quest",
                    LargeImageText = "OQRPC by MadMagic"
                }
            });
        }

        private static ulong current = 0;
        public static void Handle(JObject o) {
            string curId = (string)o["appId"];
            if (curId != null && !lastId.Equals(curId)) Init(curId);

            Timestamps ts = new Timestamps();
            if (o.ContainsKey("remaining")) ts.EndUnixMilliseconds = (ulong)DateTimeOffset.Now.ToUnixTimeMilliseconds() + (ulong)o["remaining"];
            if (o.ContainsKey("elapsed")) {
                if ((bool)o["elasped"]) {
                    if (current == 0) current = (ulong) DateTimeOffset.Now.ToUnixTimeMilliseconds();
                    ts.StartUnixMilliseconds = current;
                } else current = 0;
            } else current = 0;

            client.SetPresence(new RichPresence {
                Details = (string)o["details"],
                State = (string)o["state"],
                Assets = new Assets {
                    LargeImageKey = (string)o["largeImageKey"],
                    LargeImageText = (string)o["largeImageText"],
                    SmallImageKey = (string)o["smallImageKey"],
                    SmallImageText = (string)o["smallImageText"]
                },
                Timestamps = ts
            });
        }

        public static void StopPresence() {
            if (client == null || client.IsDisposed) return;
            client.Dispose();
        }
    }
}
