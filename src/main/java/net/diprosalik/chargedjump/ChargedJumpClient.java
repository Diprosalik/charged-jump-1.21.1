package net.diprosalik.chargedjump;

import net.diprosalik.chargedjump.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Objects;

public class ChargedJumpClient implements ClientModInitializer {
    private boolean wasOnGround = false;
    private int sprintingTime = 0;
    private static double baseSpeed;
    private static final double boostStrength = 0.35;
    Textures textures = new Textures();


    @Override
    public void onInitializeClient() {
        Configs.init();


        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            handleTick(client.player, client.options.jumpKey.isPressed());
        });

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            // Wir übergeben 'this' (die aktuelle Instanz dieser Klasse)
            textures.renderOrangeVignette(drawContext, tickDelta.getTickDelta(true), this);
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

    boolean isCharged(ModConfig config) {
        return sprintingTime > config.cooldownTicks;
    }

    private void prepareJump(PlayerEntity player) {
        Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)).setBaseValue(baseSpeed);
    }

    private void executeLongJump(PlayerEntity player) {
        ModConfig config = Configs.INSTANCE;
        World world = player.getWorld();
        Vec3d look = player.getRotationVector();
        double variation = 0.8;

        if (isPlayerOnBlock(player, world, Blocks.ICE) || isPlayerOnBlock(player, world, Blocks.BLUE_ICE) ||
                isPlayerOnBlock(player, world, Blocks.FROSTED_ICE) || isPlayerOnBlock(player, world, Blocks.PACKED_ICE)) {
            player.addVelocity(
                    look.x * ChargedJumpClient.boostStrength + (player.getWorld().random.nextDouble() - 0.5) * variation,
                    0,
                    look.z * ChargedJumpClient.boostStrength + (player.getWorld().random.nextDouble() - 0.5) * variation
            );
        } else {
            player.addVelocity(look.x * ChargedJumpClient.boostStrength,
                    0, look.z * ChargedJumpClient.boostStrength);
        }

        if (!player.getAbilities().creativeMode && config.looseHungerOnJump) {
            HungerManager hungerManager = player.getHungerManager();

            float currentSaturation = hungerManager.getSaturationLevel();

            hungerManager.setSaturationLevel(Math.max(currentSaturation - 0.1f, 0.0f));
        }

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

    private boolean isPlayerOnBlock(PlayerEntity player, World world, Block block) {
        BlockPos playerPos = player.getBlockPos();
        BlockPos posBelow = playerPos.down();

        BlockState stateBelow = world.getBlockState(posBelow);
        Block blockBelow = stateBelow.getBlock();

        return (blockBelow == block);
    }



}
