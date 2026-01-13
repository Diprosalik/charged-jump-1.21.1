package net.diprosalik.chargejump.config;

public interface DefaultConfig {

    String get( String namespace );

    static String empty( String namespace ) {
        return "";
    }
}
