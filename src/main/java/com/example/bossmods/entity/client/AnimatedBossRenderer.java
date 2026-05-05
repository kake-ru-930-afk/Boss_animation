package com.example.bossmods.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import com.example.bossmods.BossMod;
import com.example.bossmods.entity.custom.AnimatedBoss;

public class AnimatedBossRenderer extends GeoEntityRenderer<AnimatedBoss> {
    public AnimatedBossRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AnimatedBossModel());
    }
}
