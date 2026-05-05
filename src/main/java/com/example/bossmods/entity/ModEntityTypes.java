package com.example.bossmods.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.example.bossmods.BossMod;
import com.example.bossmods.entity.custom.AnimatedBoss;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = 
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BossMod.MOD_ID);

    public static final RegistryObject<EntityType<AnimatedBoss>> ANIMATED_BOSS = 
        ENTITY_TYPES.register("animated_boss",
            () -> EntityType.Builder.of(AnimatedBoss::new, MobCategory.MONSTER)
                .sized(1.2f, 2.5f)
                .build("animated_boss"));
}
