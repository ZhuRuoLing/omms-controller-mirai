package net.zhuruoling.omms.controller.mirai.controller

public class Status {
    var isAlive = false
    var name: String = ""
    var type = ControllerTypes.FABRIC
    var playerCount = 0
    var maxPlayerCount = 0
    var players: List<String> = ArrayList()
    var isQueryable = false
    constructor() {}
    constructor(type: ControllerTypes, playerCount: Int, maxPlayerCount: Int, players: List<String>) {
        this.type = type
        this.playerCount = playerCount
        this.maxPlayerCount = maxPlayerCount
        this.players = players
    }
}