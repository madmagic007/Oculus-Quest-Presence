using Newtonsoft.Json.Linq;
using OQRPC.api;
using OQRPC.settings;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace OQRPC.presence {
    class Timers {

        public static Timer requester;
        public static Timer timeOut;

        public static void StartRequesting() {
            StopRequester();
            StartTimeOut();

            int fails = 0;
            int delayMs = Config.cfg.delay * 1000;
            requester = new Timer((_) => {
                if (fails >= 10) {
                    PresenceHandler.Stop();
                    return;
                }

                try {
                    JObject game = ApiSender.Post(new JObject {
                        ["message"] = "game"
                    });
                    if (!game.HasValues) throw new Exception();
                    PresenceHandler.Handle(game);
                    StartTimeOut();
                    fails = 0;
                } catch (Exception) {
                    fails++;
                }
            }, null, delayMs, delayMs);
        }

        private static void StopRequester() {
            try {
                requester.Dispose();
            } catch (Exception) { }
        }

        public static void StartTimeOut() {
            StopTimeOut();
            timeOut = new Timer((_) => {
                PresenceHandler.Stop();
            }, null, 10000, 10000);
        }

        private static void StopTimeOut() {
            try {
                timeOut.Dispose();
            } catch (Exception) { }
        }

        public static void StopAll() {
            StopRequester();
            StopTimeOut();
        }
    }
}
