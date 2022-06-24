using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace OQRPC.presence {
    class StatusHandler {

        public static void Handle(JObject o) {
            //sigh i dont want to do this AGAIN
        }

        public static void Stop() {
            Timers.StopAll();
            Program.SendNotif("Presence on your quest has stopped");
        }
    }
}
