using System;
using System.Linq;
using System.Net;
using System.Runtime.InteropServices;

namespace OQRPC {

    class IPUtils {

        public static bool IsAddress(string address) {
            var parts = address.Split('.');

            return parts.Length == 4
                           && !parts.Any(
                               x => {
                                   return Int32.TryParse(x, out int y) && y > 255 || y < 0;
                               });
        }

        public static string GetMac(string address) {
            try {
                IPAddress hostIPAddress = IPAddress.Parse(address);
                byte[] ab = new byte[6];
                int len = ab.Length;
                SendARP((int)BitConverter.ToUInt32(hostIPAddress.GetAddressBytes(), 0), 0, ab, ref len);
                string resp = BitConverter.ToString(ab, 0, 6);
                Console.WriteLine(resp);
                return resp;
            } catch (Exception) {}
            return "";
        }


        [DllImport("iphlpapi.dll", ExactSpelling = true)]
        private static extern int SendARP(int DestIP, int SrcIP, [Out] byte[] pMacAddr, ref int PhyAddrLen);
    }
}
