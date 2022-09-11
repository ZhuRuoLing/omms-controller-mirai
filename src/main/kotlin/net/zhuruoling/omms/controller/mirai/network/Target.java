package net.zhuruoling.omms.controller.mirai.network;

public class Target {
    String address;
    int port;

    public Target(String ip, int port) {
        this.address = ip;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int hashCode() {
        return (address + port).hashCode();
    }

    @Override
    public String toString() {
        return "Target{" +
                "address='" + address + '\'' +
                ", port=" + port +
                '}';
    }
}
