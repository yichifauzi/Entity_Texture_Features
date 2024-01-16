package traben.entity_texture_features.mixin.entity.renderer;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import traben.entity_texture_features.config.ETFConfig;
import traben.entity_texture_features.features.ETFManager;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer<T extends Entity> {




    @Inject(method = "getLight", at = @At(value = "RETURN"), cancellable = true)
    private void etf$vanillaLightOverrideCancel(T entity, float tickDelta, CallbackInfoReturnable<Integer> cir) {
        //if need to override vanilla brightness behaviour
        if (ETFConfig.getInstance().enableCustomTextures
                && ETFManager.getInstance().ENTITY_TYPE_VANILLA_BRIGHTNESS_OVERRIDE_VALUE.containsKey(entity.getType())) {
            int overrideLightValue = ETFManager.getInstance().ENTITY_TYPE_VANILLA_BRIGHTNESS_OVERRIDE_VALUE.getInt(entity.getType());
            //change return with overridden light value still respecting higher block and sky lights
            cir.setReturnValue(etf$getLight(entity, tickDelta, overrideLightValue));

        }

    }


    //copy of vanilla behaviour with option to override with a minimum light level for the mob
    @Unique
    public final int etf$getLight(T entity, float tickDelta, int overrideLight) {
        BlockPos blockPos = new BlockPos(entity.getClientCameraPosVec(tickDelta));
        return LightmapTextureManager.pack(Math.max(etf$getBlockLight(entity, blockPos), overrideLight), etf$getSkyLight(entity, blockPos));
    }

    //copy of vanilla behaviour
    @Unique
    protected int etf$getSkyLight(T entity, BlockPos pos) {
        return entity.world.getLightLevel(LightType.SKY, pos);
    }

    //copy of vanilla behaviour that gets overridden by some mobs
    @Unique
    protected int etf$getBlockLight(T entity, BlockPos pos) {
        return entity.isOnFire() ? 15 : entity.world.getLightLevel(LightType.BLOCK, pos);
    }
}