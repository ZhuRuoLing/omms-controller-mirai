package net.zhuruoling.omms.controller.mirai.network.instruction;


import com.google.gson.GsonBuilder;

public class Instruction {
    ControllerTypes controllerType;
    String targetControllerName;
    String commandString;

    public boolean matches(ControllerTypes types, String targetControllerName){
        return this.controllerType.equals(types) && this.targetControllerName.equals(targetControllerName);
    }

    public Instruction(){
        controllerType = ControllerTypes.MCDR;
        targetControllerName = "";
        commandString = "";
    }

    public Instruction(ControllerTypes controllerType, String targetControllerName, String commandString) {
        this.controllerType = controllerType;
        this.targetControllerName = targetControllerName;
        this.commandString = commandString;
    }

    public static String  asJsonString(Instruction instruction){
        return new GsonBuilder().serializeNulls().create().toJson(instruction, Instruction.class);
    }

    public static Instruction fromJsonString(String json){
        return new GsonBuilder().serializeNulls().create().fromJson(json, Instruction.class);
    }

    public ControllerTypes getControllerType() {
        return controllerType;
    }

    public void setControllerType(ControllerTypes controllerType) {
        this.controllerType = controllerType;
    }

    public String getTargetControllerName() {
        return targetControllerName;
    }

    public void setTargetControllerName(String targetControllerName) {
        this.targetControllerName = targetControllerName;
    }

    public String getCommandString() {
        return commandString;
    }

    public void setCommandString(String commandString) {
        this.commandString = commandString;
    }

}
