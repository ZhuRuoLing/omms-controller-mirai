package net.zhuruoling.omms.controller.mirai.system

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

class FileSystemInfo {
    // TODO: 2022/9/10
    @SerializedName("filesystems")
    val fileSystemList: List<FileSystem> = ArrayList()

    inner class FileSystem(
        val free: Long,
        val total: Long,
        val volume: String,
        val mountPoint: String,
        var fileSystemType: String
    ) {
        fun asJsonString(fileSystemInfo: FileSystemInfo?): String {
            return GsonBuilder().serializeNulls().create().toJson(fileSystemInfo)
        }
    }
}