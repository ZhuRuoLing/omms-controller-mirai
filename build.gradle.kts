plugins {
    val kotlinVersion = "1.6.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.11.1"
}

group = "net.zhuruoling"
version = "0.7.1"

repositories {
    maven("https://maven.aliyun.com/repository/public") // 阿里云国内代理仓库
    mavenCentral()
}

dependencies{
    implementation("com.google.code.gson:gson:2.9.1")
    implementation("io.ktor:ktor-client-cio-jvm:2.1.2")
    implementation("io.ktor:ktor-client-cio:2.1.2")
    implementation("io.ktor:ktor-client-auth:2.1.2")
    implementation("io.ktor:ktor-client-auth-jvm:2.1.2")
    implementation("com.github.oshi:oshi-core:6.1.6")

}
