package net.diprosalik.chargejump.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.diprosalik.chargejump.config.ModConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        // Erzeugt automatisch das Cloth Config GUI basierend auf deiner ModConfig Klasse
        return parent -> AutoConfig.getConfigScreen(ModConfig.class, parent).get();

    }
}
