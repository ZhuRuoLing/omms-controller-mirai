package net.zhuruoling.omms.controller.mirai.system

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

class StorageInfo {
    // TODO: 2022/9/10
    @SerializedName("storages")
    var storageList: List<Storage> = ArrayList()

    inner class Storage(val name:String, val model: String, val size: Long) {

        fun asJsonString(storageInfo: StorageInfo?): String {
            return GsonBuilder().serializeNulls().create().toJson(storageInfo)
        }
    }
}