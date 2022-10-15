package net.zhuruoling.omms.controller.mirai.util

import net.zhuruoling.omms.controller.mirai.network.Target
import net.zhuruoling.omms.controller.mirai.network.broadcast.UdpBroadcastSender
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

val TARGET_CHAT: Target = UdpBroadcastSender.createTarget("224.114.51.4", 10086)
val TARGET_CONTROL: Target = UdpBroadcastSender.createTarget("224.114.51.4", 10087)

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

fun base64Encode(content: String): String? {
    return Base64.getEncoder().encodeToString(content.toByteArray(StandardCharsets.UTF_8))
}

fun getTimeCode(): String? {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmm"))
}
fun calculateToken(name: String): String {
    val tk = name + "O" + getTimeCode()
    return calculateTokenByDate(name.hashCode()).toString() + tk + base64Encode(tk)
}

fun calculateTokenByDate(password: Int): Int {
    val date = Date()
    val i = SimpleDateFormat("yyyyMMdd").format(date).toInt()
    val j = SimpleDateFormat("hhmm").format(date).toInt()
    val k = SimpleDateFormat("yyyyMMddhhmm").format(date).hashCode()
    return calculateToken(password, i, j, k)
}

fun calculateToken(password: Int, i: Int, j: Int, k: Int): Int {
    var token = 114514
    token += i
    token += j - k
    token = password xor token
    return token
}