package net.zhuruoling.omms.controller.mirai.network.broadcast

import net.zhuruoling.omms.controller.mirai.network.Target
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.*
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class UdpBroadcastSender : Thread() {
    private val logger = LoggerFactory.getLogger("UdpBroadcastSender")
    var isStopped = false
    private val queue = ConcurrentHashMap<Target, ByteArray>()
    private val multicastSocketCache = HashMap<Target, MulticastSocket?>()

    init {
        name = "UdpBroadcastSender#" + this.id
    }

    override fun run() {
        logger.info("Starting net.zhuruoling.omms.controller.mirai.network.UdpBroadcastSender.")
        while (!isStopped) {
            if (!queue.isEmpty()) {
                queue.forEach { (target: Target, content: ByteArray) -> this.addToQueue(target, content) }
            }
        }
        logger.info("Stopped!")
    }

    fun addToQueue(target: Target, content: String) {
        queue[target] = content.toByteArray(StandardCharsets.UTF_8)
    }

    private fun addToQueue(target: Target, content: ByteArray) {
        var socket: MulticastSocket? = null
        try {
            if (multicastSocketCache.containsKey(target)) {
                socket = multicastSocketCache[target]
            } else {
                socket = createMulticastSocket(target.address, target.port)
                multicastSocketCache[target] = socket
            }
            val packet = DatagramPacket(
                content,
                content.size,
                InetSocketAddress(target.address, target.port).address,
                target.port
            )
            socket!!.send(packet)
            queue.remove(target, content)
        } catch (e: Exception) {
            logger.error(
                String.format(
                    "Cannot send UDP Broadcast.\n\tTarget=%s\n\tContent=%s", target.toString(), Arrays.toString(content)
                ), e
            )
        }
    }

    companion object {
        fun createTarget(ip: String?, port: Int): Target? {
            return ip?.let { Target(it, port) }
        }

        @Throws(IOException::class)
        private fun createMulticastSocket(addr: String, port: Int): MulticastSocket {
            val inetAddress: InetAddress = InetAddress.getByName(addr)
            val socket = MulticastSocket(port)
            socket.joinGroup(InetSocketAddress(inetAddress, port), NetworkInterface.getByInetAddress(inetAddress))
            return socket
        }
    }
}