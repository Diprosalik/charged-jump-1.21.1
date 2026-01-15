package net.diprosalik.chargedjump;

import net.diprosalik.chargedjump.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

public class ChargedJumpClient implements ClientModInitializer {
    private boolean wasOnGround = false;
    private int sprintingTime = 0;
    private static double baseSpeed;
    private static final double boostStrength = 0.35;


    @Override
    public void onInitializeClient() {
        Configs.init();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            handleTick(client.player, client.options.jumpKey.isPressed());
        });
    }

    private void handleTick(PlayerEntity player, boolean jumpPressed) {
        boolean onGround = player.isOnGround();
        ModConfig config = Configs.INSTANCE;
        baseSpeed = config.getChargingSpeed();

        if (player.isSprinting()) {
            sprintingTime++;
        } else {
            sprintingTime = 0;
        }

        if (onGround && isCharged(config)) {
            prepareJump(player);
            if (player.age % 2 == 0 && config.chargingEffects) {
                spawnParticles(player);
            }
        }

        if (!onGround && isCharged(config) && wasOnGround && jumpPressed && player.isSprinting()) {
            executeLongJump(player);
        }

        if (player.fallDistance >= 2 || jumpPressed || player.getVelocity().horizontalLength() < 0.1) {
            sprintingTime = 0;
        }

        wasOnGround = onGround;
    }

    private boolean isCharged(ModConfig config) {
        return sprintingTime > config.cooldownTicks;
    }

    private void prepareJump(PlayerEntity player) {
        Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)).setBaseValue(baseSpeed);
    }

    private void executeLongJump(PlayerEntity player) {
        Vec3d look = player.getRotationVector();
        player.addVelocity(look.x * ChargedJumpClient.boostStrength, 0, look.z * ChargedJumpClient.boostStrength);
    }

    private void spawnParticles(PlayerEntity player) {
        player.getWorld().addParticle(
                ParticleTypes.TRIAL_SPAWNER_DETECTION,
                player.getX() + (player.getWorld().random.nextDouble() - 0.5),
                player.getY() + 0.1,
                player.getZ() + (player.getWorld().random.nextDouble() - 0.5),
                0, 0, 0
        );
    }

}
