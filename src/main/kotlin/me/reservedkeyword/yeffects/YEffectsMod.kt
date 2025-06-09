package me.reservedkeyword.yeffects

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(YEffectsMod.MOD_ID)
class YEffectsMod {
    companion object {
        const val MOD_ID = "yeffectsmod"
        val LOGGER: Logger = LogManager.getLogger()
    }

    init {
        LOGGER.info("$MOD_ID has been loaded.")
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC)
        MinecraftForge.EVENT_BUS.register(PlayerTickHandler)
    }
}
