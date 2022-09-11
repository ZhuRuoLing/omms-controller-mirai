package net.zhuruoling.omms.controller.mirai

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.message.data.isContentEmpty
import net.mamoe.mirai.utils.info
import net.zhuruoling.omms.controller.mirai.network.UdpBroadcastSender
import net.zhuruoling.omms.controller.mirai.network.broadcast.Broadcast
import net.zhuruoling.omms.controller.mirai.util.TARGET_CHAT
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt

object PluginMain : KotlinPlugin(JvmPluginDescription(id = "net.zhuruoling.omms.controller.mirai", name = "omms-controller", version = "0.0.1") {author("ZhuRuoLing") }) {

    private val udpBroadcastSender = UdpBroadcastSender()

    override fun onEnable() {
        val config = configFolder.absolutePath + "\\config.properties"
        if (!File(config).exists()){
            Config.createConfig(this.configFolder)
            Config.readConfig()
        }
        else{
            Config.readConfig()
        }
        logger.info { "Plugin loaded" }

        //配置文件目录 "${dataFolder.absolutePath}/"
        val eventChannel = GlobalEventChannel.parentScope(this)
        eventChannel.subscribeAlways<GroupMessageEvent>{
            val groupId = this.group.id
            if (groupId !in Config.groups || this.bot.id != Config.botId){
                return@subscribeAlways
            }
            if (this.message.contentToString() == ".help"){
                this.group.sendMessage("""
                    .mc <消息内容>  :将消息广播到mc服务器
                    .jrrp :今日人品
                """.trimIndent())
            }
            if (this.message.contentToString().startsWith(".mc ") || this.message.contentToString().startsWith("。mc")){
                udpBroadcastSender.addToQueue(TARGET_CHAT, Broadcast(Config.channel, "QQ", this.sender.nameCardOrNick, this.message.contentToString()).asJsonString())
                return@subscribeAlways
            }

            it.message.forEach{singleMessage ->
                if (!singleMessage.isContentEmpty()){
                    if (singleMessage.content.startsWith(".j") || singleMessage.content.startsWith("。j")){
                        val rp = Random(it.sender.id xor SimpleDateFormat("YYYYMMDD").format(Date()).hashCode().toLong()).nextInt(IntRange(0,100))
                        this.group.sendMessage(rpWithComment(rp))
                        return@forEach
                    }
                }
            }
        }
        eventChannel.subscribeAlways<FriendMessageEvent>{

        }

        udpBroadcastSender.start()
    }

    override fun onDisable() {
        super.onDisable()
        udpBroadcastSender.isStopped = true
    }

    enum class RPType{
        NICE, FUCK, NORMAL, NULL
    }

    fun rpWithComment(rp: Int): String{
        val message = "你今日的人品是：$rp"
        return when(rpType(rp)){
            RPType.FUCK -> message.plus(" 呜哇...")
            RPType.NORMAL -> message.plus(" 还行啦...")
            RPType.NICE -> message.plus(" 芜湖！")
            else -> message
        }
    }

    fun rpType(rp: Int): RPType {
        if (rp in 0..30){
            return RPType.FUCK
        }
        if (rp in 31 .. 60){
            return RPType.NORMAL
        }
        if (rp in 61 .. 100) {
            return RPType.NICE
        }
        return RPType.NULL
    }

}
