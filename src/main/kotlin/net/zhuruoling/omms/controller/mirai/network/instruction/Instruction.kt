package net.zhuruoling.omms.controller.mirai.network.instruction

import com.google.gson.GsonBuilder

class Instruction {
    var controllerType: ControllerTypes
    var targetControllerName: String
    var commandString: String
    fun matches(types: ControllerTypes, targetControllerName: String): Boolean {
        return controllerType == types && this.targetControllerName == targetControllerName
    }

    constructor() {
        controllerType = ControllerTypes.MCDR
        targetControllerName = ""
        commandString = ""
    }

    constructor(controllerType: ControllerTypes, targetControllerName: String, commandString: String) {
        this.controllerType = controllerType
        this.targetControllerName = targetControllerName
        this.commandString = commandString
    }

    companion object {
        fun asJsonString(instruction: Instruction?): String {
            return GsonBuilder().serializeNulls().create().toJson(instruction, Instruction::class.java)
        }

        fun fromJsonString(json: String?): Instruction {
            return GsonBuilder().serializeNulls().create().fromJson(json, Instruction::class.java)
        }
    }
}