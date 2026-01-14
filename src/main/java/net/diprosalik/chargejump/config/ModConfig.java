package net.diprosalik.chargejump.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "chargejump")
public class ModConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip // Aktiviert den Tooltip
    public boolean chargingEffects = true;

//    @ConfigEntry.Gui.Tooltip
//    public double boostStrength = 0.35;
}
