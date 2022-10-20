package net.zhuruoling.omms.controller.mirai.system

import com.google.gson.GsonBuilder

class NetworkInfo(
    var hostName: String,
    var domainName: String,
    var dnsServers: Array<String>,
    var ipv4DefaultGateway: String,
    var ipv6DefaultGateway: String
) {
    // TODO: 2022/9/10
    var networkInterfaceList: List<NetworkInterface> = ArrayList()

    inner class NetworkInterface
        (
        val name: String,
        val displayName: String,
        val macAddress: String,
        val mtu: Long,
        val speed: Long,
        val ipv4Address: Array<String>,
        val ipv6Address: Array<String>
    ) {
        fun toJsonString(networkInfo: NetworkInfo?): String {
            return GsonBuilder().serializeNulls().create().toJson(networkInfo)
        }
    }
}