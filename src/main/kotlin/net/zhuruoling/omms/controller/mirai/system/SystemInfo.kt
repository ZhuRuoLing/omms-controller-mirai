package net.zhuruoling.omms.controller.mirai.system

import net.zhuruoling.foo.system.ProcessorInfo

class SystemInfo(
    var osName: String,
    var osVersion: String,
    var osArch: String,
    var fileSystemInfo: FileSystemInfo,
    var memoryInfo: MemoryInfo,
    var networkInfo: NetworkInfo,
    var processorInfo: ProcessorInfo,
    var storageInfo: StorageInfo
)