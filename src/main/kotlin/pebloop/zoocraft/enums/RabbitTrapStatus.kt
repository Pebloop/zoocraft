package pebloop.zoocraft.enums

import net.minecraft.util.StringIdentifiable

enum class RabbitTrapStatus : StringIdentifiable {
    EMPTY {
        override fun asString(): String {
            return "empty"
        }
    },
    BAITED {
        override fun asString(): String {
            return "baited"
        }
    },
    TRAPPED {
        override fun asString(): String {
            return "trapped"
        }
    };

    companion object {
        fun fromString(status: String): RabbitTrapStatus {
            return when (status) {
                "empty" -> EMPTY
                "baited" -> BAITED
                "trapped" -> TRAPPED
                else -> throw IllegalArgumentException("Invalid status: $status")
            }
        }
    }
}