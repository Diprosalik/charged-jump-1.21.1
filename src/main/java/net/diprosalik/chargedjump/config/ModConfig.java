package net.diprosalik.chargedjump.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.diprosalik.chargedjump.enums.Particles;

@Config(name = "chargedjump")
public class ModConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    public boolean chargingEffects = true;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public Particles particleType1 = Particles.TRIAL_SPAWNER_DETECTION;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 10, max = 60)
    public int cooldownTicks = 20;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 10, max = 20)
    public int chargingSpeedScaled = 11;

    public double getChargingSpeed() {
        return chargingSpeedScaled / 100.0;
    }

    @ConfigEntry.Gui.Tooltip
    public boolean enableVignette = false;

    @ConfigEntry.Gui.Tooltip
    public boolean looseHungerOnJump = true;
}
