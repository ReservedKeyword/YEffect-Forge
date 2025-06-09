package me.reservedkeyword.yeffects

import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.registries.ForgeRegistries

object PlayerTickHandler {
    data class EffectRule(
        val minY: Int,
        val maxY: Int,
        val effect: MobEffect,
        val durationTicks: Int,
        val amplifier: Int
    )

    @SubscribeEvent
    fun onPlayerTick(event: TickEvent.PlayerTickEvent) {
        if (event.phase != TickEvent.Phase.END || event.player !is ServerPlayer) {
            return
        }

        val serverPlayer = event.player as ServerPlayer
        val serverPlayerY = serverPlayer.y.toInt()

        Config.SERVER_CONFIG.effectsConfig.get().forEach { ruleStr ->
            runCatching {
                parseRule(ruleStr)
            }.onSuccess { effectRule ->
                if (serverPlayerY in effectRule.minY..effectRule.maxY) {
                    applyEffectToPlayer(serverPlayer, effectRule)
                }
            }.onFailure { error ->
                YEffectsMod.Companion.LOGGER.warn("Failed to parse effect rule $ruleStr. Reason: ${error.message}.")
            }
        }

    }

    private fun parseRule(ruleStr: String): EffectRule {
        val parts = ruleStr.split(";")
        if (parts.size != 5) throw IllegalArgumentException("Rule must have 5 distinct parts.")

        val minY = parts[0].toIntOrNull() ?: throw IllegalArgumentException("Invalid minY value.")
        val maxY = parts[1].toIntOrNull() ?: throw IllegalArgumentException("Invalid maxY value.")
        val effectsId = parts[2]
        val durationTicks = parts[3].toIntOrNull() ?: throw IllegalArgumentException("Invalid duration value.")
        val amplifier = parts[4].toIntOrNull() ?: throw IllegalArgumentException("Invalid amplifier value.")

        val effectLocation = ResourceLocation.tryParse(effectsId)
        val mobEffect = ForgeRegistries.MOB_EFFECTS.getValue(effectLocation)
            ?: throw IllegalArgumentException("Mob effect $effectsId not found in Forge registry.")

        return EffectRule(
            minY,
            maxY,
            mobEffect,
            durationTicks,
            amplifier
        )
    }

    private fun applyEffectToPlayer(serverPlayer: ServerPlayer, effectRule: EffectRule) {
        val effectInstance = MobEffectInstance(
            effectRule.effect,
            effectRule.durationTicks,
            effectRule.amplifier,
            true,
            false
        )

        serverPlayer.addEffect(effectInstance)
    }
}
