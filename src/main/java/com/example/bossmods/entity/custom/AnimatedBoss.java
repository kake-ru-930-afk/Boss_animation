package com.example.bossmods.entity.custom;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class AnimatedBoss extends Monster implements GeoEntity {
    private final ServerBossEvent bossInfo = new ServerBossEvent(
        Component.literal("Animated Boss"),
        BossEvent.BossBarColor.RED,
        BossEvent.BossBarOverlay.PROGRESS
    );

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    
    private static final RawAnimation POSE_IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation POSE_WALK = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenPlay("attack");
    
    private int attackCooldown = 0;

    public AnimatedBoss(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public ServerBossEvent getBossInfo() {
        return bossInfo;
    }

    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        bossInfo.removePlayer(player);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
        
        if (attackCooldown > 0) {
            attackCooldown--;
        }
    }

    @Override
    public void remove(@NotNull RemovalReason reason) {
        super.remove(reason);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public boolean hurt(@NotNull DamageSource src, float amount) {
        amount = Math.min(amount, 10.0F);
        return super.hurt(src, amount);
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.UNDEFINED;
    }

    @Override
    protected float getStandingEyeHeight(@NotNull Pose pose, @NotNull EntityDimensions size) {
        return 2.0F;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 5, this::movementPredicate));
        controllers.add(new AnimationController<>(this, "attack", 0, this::attackPredicate));
    }

    private <E extends GeoAnimatable> PlayState movementPredicate(AnimationState<E> state) {
        if (!this.isAggressive()) {
            if (this.isMoving()) {
                return state.setAndContinue(POSE_WALK);
            } else {
                return state.setAndContinue(POSE_IDLE);
            }
        }
        return PlayState.STOP;
    }

    private <E extends GeoAnimatable> PlayState attackPredicate(AnimationState<E> state) {
        if (this.isAggressive() && attackCooldown <= 0) {
            state.setAndContinue(ATTACK_ANIM);
            attackCooldown = 30;
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private boolean isMoving() {
        Vec3 motion = this.getDeltaMovement();
        return Math.abs(motion.x) > 0.01 || Math.abs(motion.z) > 0.01;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 100.0D)
            .add(Attributes.ATTACK_DAMAGE, 10.0D)
            .add(Attributes.ARMOR, 5.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.4D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
            .add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    @Mod.EventBusSubscriber(modid = "bossmods")
    public static class BossBarTrackingHandler {
        @SubscribeEvent
        public static void onStartTracking(PlayerEvent.StartTracking event) {
            if (event.getEntity() instanceof ServerPlayer && event.getTarget() instanceof AnimatedBoss) {
                ((AnimatedBoss) event.getTarget()).getBossInfo().addPlayer((ServerPlayer) event.getEntity());
            }
        }

        @SubscribeEvent
        public static void onStopTracking(PlayerEvent.StopTracking event) {
            if (event.getEntity() instanceof ServerPlayer && event.getTarget() instanceof AnimatedBoss) {
                ((AnimatedBoss) event.getTarget()).getBossInfo().removePlayer((ServerPlayer) event.getEntity());
            }
        }
    }
}
