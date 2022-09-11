package net.zhuruoling.omms.controller.mirai.util

import net.zhuruoling.omms.controller.mirai.network.Target
import net.zhuruoling.omms.controller.mirai.network.UdpBroadcastSender

val TARGET_CHAT: Target = UdpBroadcastSender.createTarget("224.114.51.4", 10086)
val TARGET_CONTROL: Target = UdpBroadcastSender.createTarget("224.114.51.4", 10087)

