package net.diprosalik.chargejump;

import com.mojang.datafixers.util.Pair;
import net.diprosalik.chargejump.config.BaseConfig;
import net.diprosalik.chargejump.config.ConfigProvider;

public class Configs {
    public static BaseConfig CONFIG;

    public static boolean chargingEffects;

    private static ConfigProvider provider;

    public static void load() {
        provider = new ConfigProvider();
        createConfigs();

        CONFIG = BaseConfig.of("chargejump" + "_config").provider(provider).request();

        assignConfigValues();
    }

    private static void createConfigs() {
        provider.addComment("Here you can toggle if you want to have charging effects");
        provider.addPair(new Pair<>(Reference.Keys.Config.effects, true));
    }

    private static void assignConfigValues() {
        chargingEffects = CONFIG.getOrDefault(Reference.Keys.Config.effects, true);
    }

    //Logger("All " + provider.getConfigList().size() + "have been set properly");
}
