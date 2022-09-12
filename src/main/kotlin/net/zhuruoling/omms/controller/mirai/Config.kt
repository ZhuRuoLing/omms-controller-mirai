package net.zhuruoling.omms.controller.mirai

import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.nio.file.Files
import java.util.Properties
import kotlin.io.path.Path

object Config {
    var botId: Long = 0L
    var groups = mutableListOf<Long>()
    var channel = ""

    private val defaultProperties: Properties

    init {
        val properties = Properties()
        properties.setProperty("botId", "123456")
        properties.setProperty("groups", "12345678")
        properties.setProperty("channel", "GLOBAL")
        defaultProperties = properties
    }

    @JvmStatic
    fun createConfig(configFolder: File){
        val config = configFolder.absolutePath + "\\config.properties"
        Files.createFile(Path(config))
        Files.write(
            Path(config), """
                botId=123456
                groups=12345678
                channel=GLOBAL
            """.trimIndent().encodeToByteArray())
    }

    @JvmStatic
    fun readConfig(configPath:String){
        if (!Files.exists(Path(configPath))){
            throw FileNotFoundException(configPath)
        }
        val properties: Properties = defaultProperties
        properties.load(FileReader(configPath))
        this.botId = properties.getProperty("botId").toLong()
        this.channel = properties.getProperty("channel")
        val groupString = properties.getProperty("groups")
        if ("," in groupString){
            val groups = groupString.split(",").toMutableList()
            groups.forEach {
                this.groups.add(it.toLong())
            }
        }
        else{
            this.groups.add(groupString.toLong())
        }
    }
}