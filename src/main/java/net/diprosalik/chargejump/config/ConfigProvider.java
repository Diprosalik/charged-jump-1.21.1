package net.diprosalik.chargejump.config;

import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;

public class ConfigProvider implements DefaultConfig{

    private String configContent;
    private final List<Pair> configsList;

    public ConfigProvider() {
        this.configsList = new ArrayList<>();
    }

    public List<Pair> getConfigList() {
        return configsList;
    }

    @Override
    public String get(String namespace) {
        return this.configContent;
    }

    public void addPair(Pair<String, ?> pair, String comment) {
        configsList.add(pair);
        configContent += pair.getFirst() + " = " + pair.getSecond() + " #" +
                comment + " | default: " + pair.getSecond() + "\n";


    }

    public void addPair(Pair<String, ?> pair) {
        configsList.add(pair);
        configContent += pair.getFirst() + " = " + pair.getSecond() + " #" +
                 " default: " + pair.getSecond() + "\n";


    }

    public void addComment(String comment) {
        configContent += "#" + comment + "\n";
    }
}
