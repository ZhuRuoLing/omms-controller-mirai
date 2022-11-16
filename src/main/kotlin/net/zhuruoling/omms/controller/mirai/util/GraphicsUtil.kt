package net.zhuruoling.omms.controller.mirai.util

import net.zhuruoling.omms.controller.mirai.controller.Status
import net.zhuruoling.omms.controller.mirai.system.SystemInfo
import okhttp3.internal.notify
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.lang.management.ManagementFactory
import java.nio.file.Path
import javax.imageio.ImageIO
import javax.rmi.CORBA.Util
import kotlin.io.path.Path

fun createImage(width: Int, height: Int): BufferedImage {
    return BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
}

fun saveImage(path: Path, bufferedImage: BufferedImage) {
    ImageIO.write(bufferedImage, "png", path.toFile())
}

fun getGraphics(bufferedImage: BufferedImage, action: (Graphics2D) -> Unit) {
    val graphics2D = bufferedImage.createGraphics()
    action.invoke(graphics2D)
    graphics2D.dispose()
}

fun clearImage(bufferedImage: BufferedImage, color: Color = Color.WHITE) {
    getGraphics(bufferedImage) {
        it.color = color
        it.stroke = BasicStroke(1f)
        it.fillRect(0, 0, bufferedImage.width, bufferedImage.height)
    }
}

fun test(len: Int) {
    println("Creating image")
    val image = createImage(1080, 1720)
    clearImage(image)
    getGraphics(image) {
        it.font = Font("Consolas", Font.PLAIN, 40)
        it.color = Color.BLACK
        val h = it.fontMetrics.height
        for (i in 1..len) {
            it.drawString(randomStringGen(i), 0, (i) * h)
        }
    }
    saveImage(Path(joinFilePaths("image", "${randomStringGen(8)}.png")), image)
}

fun genImage(systemInfo: SystemInfo, fromJson: Map<String, Status>): InputStream? {
    println("Creating image")
    val info = systemInfo

    val strings = mutableListOf<String>()
    strings.add("Operating system:${info.osName} ${info.osVersion} ${info.osArch}")
    strings.add("Processor: ${info.processorInfo.processorName?.trimEnd()} x${info.processorInfo.physicalCPUCount}")
    val runtime = ManagementFactory.getRuntimeMXBean()
    val upTime = runtime.uptime / 1000.0
    strings.add(String.format("Uptime: %.3fS", upTime))
    val memoryMXBean = ManagementFactory.getMemoryMXBean()
    val heapMemoryUsage = memoryMXBean.heapMemoryUsage
    val nonHeapMemoryUsage = memoryMXBean.nonHeapMemoryUsage
    val maxMemory = (heapMemoryUsage.max + nonHeapMemoryUsage.max) / 1024.0 / 1024.0
    val usedMemory = (heapMemoryUsage.used + nonHeapMemoryUsage.used) / 1024.0 / 1024.0
    strings.add(String.format("JVM Memory usage: %.3fMiB/%.3fMiB", usedMemory, maxMemory))
    strings.add(
        "RAM: ${info.memoryInfo.memoryUsed / 1024 / 1024}MB/${info.memoryInfo.memoryTotal / 1024 / 1024}MB(${
            String.format(
                "%.3f",
                1.0 - info.memoryInfo.memoryUsed * 1.0f / info.memoryInfo.memoryTotal
            )
        })"
    )
    strings.add("Disks:")
    info.storageInfo.storageList.forEach {
        strings.add("  ${it.model} size:${it.size / 1024 / 1024}MiB")
    }
    strings.add("Partitions:")
    info.fileSystemInfo.fileSystemList.forEach {
        strings.add(
            "  ${it.mountPoint} : ${it.fileSystemType} ${it.free / 1024 / 1024}MB/${it.total / 1024 / 1024}MB(${
                String.format(
                    "%.3f",
                    1.0 - it.free * 1.0 / it.total
                )
            })"
        )
    }
    strings.add("Controllers:")
    fromJson.forEach {
        val status = it.value
        strings.add("  [${if (status.isAlive) "o" else if (!status.isQueryable) "?" else "x"}] ${it.key}: ${if (!status.isQueryable) "(Not Queryable.)" else "" }")
        if (status.isAlive) {
            strings.add("    Players: ${status.playerCount}/${status.maxPlayerCount} ${if (status.playerCount != 0) "->" else ""} ")
            val players = status.players
            if (status.playerCount % 2 == 0) {
                //0     2     4
                //0 1   2 3   4 5
                for (i in 0 until (status.playerCount / 2)) {
                    strings.add("      ${players[i]} ${players[i + 1]}")
                }

            } else {
                for (i in 0 until (status.playerCount - 1) / 2)
                    strings.add("      ${players[i]} ${players[i + 1]}")
                strings.add("      ${players.last()} ")
            }
        }
    }

    val font = Font("Consolas", Font.PLAIN, 32)
    val image = createImage(
        1080,
        (font.getLineMetrics(
            "wy0OaabbcGg",
            createImage(600, 800).createGraphics().fontRenderContext
        ).height * strings.size).plus(64).toInt()
    )
    clearImage(image)
    getGraphics(image) { graphics2D ->
        graphics2D.font = font
        graphics2D.color = Color.BLACK
        val h = graphics2D.fontMetrics.height
        for (i in 0 until strings.size) {
            graphics2D.drawString(strings[i], 0, (i + 1) * h)
        }
    }
    val stream = ByteArrayOutputStream()
    return try {
        ImageIO.write(image, "png", stream)
        ByteArrayInputStream(stream.toByteArray())
    } catch (e: Throwable) {
        e.printStackTrace()
        null
    }
    //saveImage(Path(joinFilePaths("image", "system_info.png")), image)
}