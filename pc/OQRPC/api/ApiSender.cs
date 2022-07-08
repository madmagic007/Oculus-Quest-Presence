using Flurl.Http;
using Newtonsoft.Json.Linq;
using OQRPC.settings;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace OQRPC.api {

    class ApiSender {

        public static JObject Get(string url) => JObject.Parse(url.GetStringAsync().Result);

        public static JObject Post(JObject o, string address = "") {
            if (Config.cfg.address != null) address = Config.cfg.address;
            o["pcAddress"] = IPUtils.GetOwnAddress();
            o["sleepWake"] = Config.cfg.sleepWake;
            if (address == "") return null;
            return JObject.Parse(("http://" + address + ":" + ApiSocket.port).PostJsonAsync(o).ReceiveString().Result);
        }
    }
}
