package net.diprosalik.chargejump;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Objects;

public class ChargeJumpClient implements ClientModInitializer {
    private boolean wasOnGround = false;
    private static int sprintingTime = 0;
    private static final int COOLDOWN = 20;
    private static final double SPEED = 0.11;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            PlayerEntity player = client.player;

            boolean onGround = player.isOnGround();
            boolean jumpPressed = client.options.jumpKey.isPressed();

            if (player.isSprinting()) {
                sprintingTime++;
            } else {
                sprintingTime = 0;
            }

            if (onGround
                    && sprintingTime > COOLDOWN
                    && player.isSprinting()) {

                readyForLongJump(player, SPEED);
                if (player.age % 2 == 0) {
                    spawnSprintParticles(player);
                }
            }

            if (!onGround
                    && sprintingTime > COOLDOWN
                    && wasOnGround
                    && jumpPressed
                    && player.isSprinting()) {

                applyLongJump(player);
                readyForLongJump(player, 0.1);
            }

            if (!onGround) sprintingTime = 0;

            wasOnGround = onGround;
        });
    }

    private void applyLongJump(PlayerEntity player) {
        Vec3d lookVec = player.getRotationVector();

        double boostStrength = 0.35; // ca. 1 Block extra

        player.addVelocity(
                lookVec.x * boostStrength,
                0,
                lookVec.z * boostStrength
        );
    }

    private void readyForLongJump(PlayerEntity player, double SPEED) {
        Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED))
                .setBaseValue(SPEED);
    }

    private static void spawnSprintParticles(PlayerEntity player) {
        World world = player.getWorld();
        Vec3d pos = player.getPos();

        double spread = 0.5;

        world.addParticle(
                ParticleTypes.TRIAL_SPAWNER_DETECTION,
                pos.x + (world.random.nextDouble() - 0.5) * spread,
                pos.y + 0.05,
                pos.z + (world.random.nextDouble() - 0.5) * spread,
                0.0,
                0.0,
                0.0
        );
    }
}
