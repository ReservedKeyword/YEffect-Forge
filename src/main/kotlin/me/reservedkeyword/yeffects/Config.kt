package me.reservedkeyword.yeffects

import net.minecraftforge.common.ForgeConfigSpec

object Config {
    val SERVER_CONFIG: ServerConfig
    val SERVER_SPEC: ForgeConfigSpec

    init {
        val builder = ForgeConfigSpec.Builder()
        SERVER_CONFIG = ServerConfig(builder)
        SERVER_SPEC = builder.build()
    }

    class ServerConfig(builder: ForgeConfigSpec.Builder) {
        val effectsConfig: ForgeConfigSpec.ConfigValue<List<String>>

        init {
            builder.push("general")

            effectsConfig = builder.comment(
                "A list of effect rule(s). Each rule is a string with 5 distinct parts, each separated by a semicolon (;).",
                "Format: \"minY;maxY;effectId;durationTicks;amplifier\"",
                "Example 1: From Y=64 to Y=128, apply Slow Falling I for 10 seconds.",
                "   \"64;128;minecraft:slow_falling;200;0\"",
                "Example 2: From Y=128 to Y=512, apply Poison II for 5 seconds.",
                "   \"128;512;minecraft:poison;100;1\""
            ).define(
                "effectsConfig", listOf(
                    "64;128;minecraft:slow_falling;200;0",
                    "128;512;minecraft:poison;100;1"
                )
            )

            builder.pop()
        }
    }
}
