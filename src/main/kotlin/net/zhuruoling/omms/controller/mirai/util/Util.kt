package net.zhuruoling.omms.controller.mirai.util

import net.zhuruoling.omms.controller.mirai.network.Target
import net.zhuruoling.omms.controller.mirai.network.broadcast.UdpBroadcastSender

val TARGET_CHAT: Target? = UdpBroadcastSender.createTarget("224.114.51.4", 10086)
val TARGET_CONTROL: Target? = UdpBroadcastSender.createTarget("224.114.51.4", 10087)

enum class RPType {
    NICE, FUCK, NORMAL, NULL
}

fun rpWithComment(rp: Int): String {
    val message = "你今日的人品是：$rp"
    return when (rpType(rp)) {
        RPType.FUCK -> message.plus(" 呜哇...")
        RPType.NORMAL -> message.plus(" 还行啦...")
        RPType.NICE -> message.plus(" 芜湖！")
        else -> message
    }
}

fun rpType(rp: Int): RPType {
    if (rp in 0..30) {
        return RPType.FUCK
    }
    if (rp in 31..60) {
        return RPType.NORMAL
    }
    if (rp in 61..100) {
        return RPType.NICE
    }
    return RPType.NULL
}