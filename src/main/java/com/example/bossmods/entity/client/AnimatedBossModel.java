package com.example.bossmods.entity.client;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import com.example.bossmods.BossMod;
import com.example.bossmods.entity.custom.AnimatedBoss;

public class AnimatedBossModel extends GeoModel<AnimatedBoss> {
    @Override
    public ResourceLocation getModelResource(AnimatedBoss animatable) {
        return new ResourceLocation(BossMod.MOD_ID, "geo/animated_boss.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AnimatedBoss animatable) {
        return new ResourceLocation(BossMod.MOD_ID, "textures/entity/animated_boss.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AnimatedBoss animatable) {
        return new ResourceLocation(BossMod.MOD_ID, "animations/animated_boss.animation.json");
    }
}
