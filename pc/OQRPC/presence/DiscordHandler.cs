using DiscordRPC;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace OQRPC.presence {

    class DiscordHandler {

        private static DiscordRpcClient client;
        private static RichPresence presence;
        private static Timestamps timestamps;
        private static Assets assets;

        public static void Init() {
            client = new DiscordRpcClient("772853901396279376");
            client.Initialize();

            //timestamps = GetTimestamps();
            assets = new Assets {
                LargeImageKey = "gmod",
                LargeImageText = "Garry's Mod RPC by MadMagic"
            };
            presence = new RichPresence {
                Details = "Just started playing"
            };
            //UpdatePresence();
        }

        public static void StopPresence() {
            if (client == null || client.IsDisposed) return;
            client.Dispose();
        }
    }
}
