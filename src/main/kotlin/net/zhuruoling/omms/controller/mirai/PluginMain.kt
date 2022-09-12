package net.zhuruoling.omms.controller.mirai

import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.BotOfflineEvent
import net.mamoe.mirai.event.events.BotOnlineEvent
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.message.data.isContentEmpty
import net.mamoe.mirai.utils.info
import net.zhuruoling.omms.controller.mirai.network.broadcast.UdpBroadcastSender
import net.zhuruoling.omms.controller.mirai.network.broadcast.UdpReceiver
import net.zhuruoling.omms.controller.mirai.network.broadcast.Broadcast
import net.zhuruoling.omms.controller.mirai.util.TARGET_CHAT
import net.zhuruoling.omms.controller.mirai.util.rpWithComment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "net.zhuruoling.omms.controller.mirai",
        name = "omms-controller",
        version = "0.0.1"
    ) { author("ZhuRuoLing") }) {

    private val udpBroadcastSender = UdpBroadcastSender()
    private lateinit var udpReceiver: UdpReceiver

    @OptIn(DelicateCoroutinesApi::class)
    override fun onEnable() {
        val config = configFolder.absolutePath + "\\config.properties"
        if (!File(config).exists()) {
            Config.createConfig(this.configFolder)
            Config.readConfig(config)
        } else {
            Config.readConfig(config)
        }
        logger.info { "Registering events." }
        //配置文件目录 "${dataFolder.absolutePath}/"
        val eventChannel = globalEventChannel(this.coroutineContext)
        eventChannel.subscribeAlways<BotOnlineEvent> {
            if (this.bot.id == Config.botId) {
                udpReceiver = UdpReceiver(this.bot, TARGET_CHAT) { bot, info ->
                    if (bot.isOnline) {
                        Config.groups.forEach {
                            try {
                                val broadcast = Gson().fromJson(info, Broadcast::class.java)
                                if (broadcast.player.startsWith("\ufff3\ufff4") || broadcast.server == "OMMS CENTRAL") {
                                    GlobalScope.launch(this@PluginMain.coroutineContext) {
                                        bot.getGroup(it)?.sendMessage(
                                            "[${
                                                if (broadcast.server == "OMMS CENTRAL") "CONSOLE" else (broadcast.player.removePrefix(
                                                    "\ufff3\ufff4"
                                                ))
                                            }@${broadcast.server}] ${broadcast.content}"
                                        )
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    }
                }
            }
            udpReceiver.start()
            logger.info("BotOnlineEvent")
        }
        eventChannel.subscribeAlways<BotOfflineEvent> {
            udpReceiver.interrupt()
        }

        eventChannel.subscribeAlways<GroupMessageEvent> {
            val groupId = this.group.id
            if (groupId !in Config.groups || this.bot.id != Config.botId) {
                return@subscribeAlways
            }
            if (this.message.contentToString() == ".help") {
                this.group.sendMessage(
                    """
                    .mc <消息内容>  :将消息广播到mc服务器
                    .jrrp :今日人品
                """.trimIndent()
                )
            }
            if (this.message.contentToString().startsWith(".mc ") || this.message.contentToString().startsWith("。mc")) {
                if (TARGET_CHAT != null) {
                    udpBroadcastSender.addToQueue(
                        TARGET_CHAT,
                        Broadcast(
                            Config.channel,
                            "QQ",
                            this.sender.nameCardOrNick,
                            this.message.contentToString().removePrefix(".mc ").removePrefix("。mc ")
                        ).asJsonString()
                    )
                }
                return@subscribeAlways
            }

            it.message.forEach { singleMessage ->
                if (!singleMessage.isContentEmpty()) {
                    if (singleMessage.content.startsWith(".j") || singleMessage.content.startsWith("。j")) {
                        val rp = Random(
                            it.sender.id xor SimpleDateFormat("YYYYMMDD").format(Date()).hashCode().toLong()
                        ).nextInt(IntRange(0, 100))
                        this.group.sendMessage(rpWithComment(rp))
                        return@forEach
                    }
                }
            }
        }

        udpBroadcastSender.start()
        logger.info { "Plugin loaded" }
    }

    override fun onDisable() {
        super.onDisable()
        udpBroadcastSender.isStopped = true
    }



}
