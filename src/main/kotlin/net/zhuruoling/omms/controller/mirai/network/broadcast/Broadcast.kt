package net.zhuruoling.omms.controller.mirai.network.broadcast

import com.google.gson.GsonBuilder

class Broadcast(var channel: String, var server: String, var player: String, var content: String) {

    override fun toString(): String {
        return "Broadcast{" +
                "channel='" + channel + '\'' +
                ", server='" + server + '\'' +
                ", player='" + player + '\'' +
                ", content='" + content + '\'' +
                '}'
    }

    fun asJsonString(): String {
        return GsonBuilder().serializeNulls().create().toJson(this)
    }
}