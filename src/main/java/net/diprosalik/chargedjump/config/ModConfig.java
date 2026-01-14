package net.diprosalik.chargedjump.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "chargedjump")
public class ModConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    public boolean chargingEffects = true;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 10, max = 60)
    public int cooldownTicks = 20;
}
