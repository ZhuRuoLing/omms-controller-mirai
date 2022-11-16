package net.zhuruoling.omms.controller.mirai

import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.nio.file.Files
import java.util.*
import kotlin.io.path.Path

object Config {
    var botId: Long = 0L
    var groups = mutableListOf<Long>()
    var channel = ""
    var ops = mutableListOf<Long>()
    var name = "omms-controller-mirai"
    var httpAddress = ""
    var jrrpCheats = false
    var jrrpPersons = mutableListOf<Long>()
    var jrrpRangeMap = mutableMapOf<Long, IntRange>()
    private val defaultProperties: Properties

    init {
        val properties = Properties()
        properties.setProperty("botId", "123456")
        properties.setProperty("groups", "12345678")
        properties.setProperty("channel", "GLOBAL")
        properties.setProperty("name", "omms-controller-mirai")
        defaultProperties = properties
    }

    @JvmStatic
    fun createConfig(configFolder: File) {
        val config = configFolder.absolutePath + "\\config.properties"
        Files.createFile(Path(config))
        Files.write(
            Path(config), """
                botId=123456
                groups=12345678
                channel=GLOBAL
                ops=12345678
                name=omms-controller-mirai
                http_query_ip=localhost
                http_query_port=50001
                jrrp_cheat=false
                jrrp_persons=123456
                jrrp.123456.range=1..100
            """.trimIndent().encodeToByteArray()
        )
    }

    @JvmStatic//opString
    fun readConfig(configPath: String) {
        if (!Files.exists(Path(configPath))) {
            throw FileNotFoundException(configPath)
        }
        val properties: Properties = defaultProperties
        properties.load(FileReader(configPath))
        this.botId = properties.getProperty("botId").toLong()
        this.channel = properties.getProperty("channel")
        val groupString = properties.getProperty("groups")
        val opString = properties.getProperty("ops")
        name = properties.getProperty("name")
        httpAddress = properties.getProperty("http_query_ip") + ":" + properties.getProperty("http_query_port")
        jrrpCheats = properties.getProperty("jrrp_cheat").toBoolean()
        val jrrpPersonString = properties.getProperty("jrrp_persons")
        if (jrrpPersonString.contains(",")) {
            jrrpPersonString.split(",").forEach{
                val rangeString = properties.getProperty("jrrp.$it.range")
                jrrpRangeMap[it.toLong()] = IntRange(rangeString.split("..")[0].toInt(), rangeString.split("..")[1].toInt())
            }
        } else {
            val rangeString = properties.getProperty("jrrp.$jrrpPersonString.range")
            jrrpRangeMap[jrrpPersonString.toLong()] = IntRange(rangeString.split("..")[0].toInt(), rangeString.split("..")[1].toInt())
        }
        if ("," in groupString) {
            val groups = groupString.split(",").toMutableList()
            groups.forEach {
                this.groups.add(it.toLong())
            }
        } else {
            this.groups.add(groupString.toLong())
        }


        if ("," in opString) {
            val ops = opString.split(",").toMutableList()
            ops.forEach {
                this.ops.add(it.toLong())
            }
        } else {
            this.ops.add(opString.toLong())
        }
    }
}