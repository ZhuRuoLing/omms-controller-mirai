package net.zhuruoling.omms.controller.mirai.network

class Target(var address: String, var port: Int) {

    override fun hashCode(): Int {
        return (address + port).hashCode()
    }

    override fun toString(): String {
        return "Target{" +
            "address='" + address + '\'' +
            ", port=" + port +
            '}'
    }

    fun port(): Int {
        return port
    }

    fun address(): String {
        return address
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Target

        if (address != other.address) return false
        if (port != other.port) return false

        return true
    }
}