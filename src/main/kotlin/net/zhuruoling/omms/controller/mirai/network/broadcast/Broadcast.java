package net.zhuruoling.omms.controller.mirai.network.broadcast;

import com.google.gson.GsonBuilder;

public class Broadcast {
    public String channel;
    public String server;
    public String player;
    public String content;

    public Broadcast(String channel, String server, String player, String content) {
        this.channel = channel;
        this.server = server;
        this.player = player;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Broadcast{" +
                "channel='" + channel + '\'' +
                ", server='" + server + '\'' +
                ", player='" + player + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String asJsonString(){
        return new GsonBuilder().serializeNulls().create().toJson(this);
    }
}
