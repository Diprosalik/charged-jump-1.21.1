package net.diprosalik.chargedjump;

import com.mojang.blaze3d.systems.RenderSystem;
import net.diprosalik.chargedjump.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class Textures {

    public void renderOrangeVignette(DrawContext drawContext, float tickDelta, ChargedJumpClient chargedJumpClient) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        ModConfig config = Configs.INSTANCE;

        // Jetzt nutzt die Methode die echten Daten aus der Hauptklasse
        if (client.player.isOnGround() && chargedJumpClient.isCharged(config) && config.enableVignette) {

            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();

            Identifier VIGNETTE_TEXTURE = Identifier.of("chargedjump", "textures/misc/vignette.png");

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            float pulse = 0.45f + (float) Math.sin((client.player.age + tickDelta) * 0.15f) * 0.15f;

            RenderSystem.setShaderColor(1.0f, 0.5f, 0.0f, pulse);

            drawContext.drawTexture(VIGNETTE_TEXTURE, 0, 0, 0, 0, width, height, width, height);

            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.disableBlend();
        }
    }
}
