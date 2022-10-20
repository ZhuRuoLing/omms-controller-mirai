package net.zhuruoling.omms.controller.mirai.system

class DirectoryInfo {
    var folders: List<String>? = null
    var files: List<String>? = null
    var result = SystemResult.__NULL

    constructor() {}
    constructor(folders: List<String>?, files: List<String>?) {
        this.folders = folders
        this.files = files
    }
}