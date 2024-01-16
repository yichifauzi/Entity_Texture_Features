package traben.entity_texture_features.mixin.entity.renderer.feature;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.ShoulderParrotFeatureRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import traben.entity_texture_features.features.ETFRenderContext;
import traben.entity_texture_features.utils.ETFEntity;

import java.util.Optional;

@Mixin(ShoulderParrotFeatureRenderer.class)
public abstract class MixinShoulderParrotFeatureRenderer<T extends PlayerEntity> extends FeatureRenderer<T, PlayerEntityModel<T>> {


    @Unique
    private NbtCompound entity_texture_features$parrotNBT = null;
    @Unique
    private ETFEntity etf$heldEntity = null;

    public MixinShoulderParrotFeatureRenderer(FeatureRendererContext<T, PlayerEntityModel<T>> context) {
        super(context);
    }

    @Inject(method ="method_17958(Lnet/minecraft/client/util/math/MatrixStack;ZLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/nbt/NbtCompound;IFFFFLnet/minecraft/entity/EntityType;)V",
            at = @At(value = "HEAD"))
    private void etf$etf$alterEntity(MatrixStack matrixStack, boolean bl, PlayerEntity playerEntity, VertexConsumerProvider vertexConsumerProvider, NbtCompound nbtCompound, int i, float f, float g, float h, float j, EntityType type, CallbackInfo ci) {
        if (entity_texture_features$parrotNBT != null) {

            etf$heldEntity = ETFRenderContext.getCurrentEntity();

            EntityType.getEntityFromNbt(entity_texture_features$parrotNBT, playerEntity.getWorld());
            Optional<Entity> optionalEntity = EntityType.getEntityFromNbt(entity_texture_features$parrotNBT, playerEntity.getWorld());
            if (optionalEntity.isPresent() && optionalEntity.get() instanceof ParrotEntity parrot) {
                ETFRenderContext.setCurrentEntity((ETFEntity) parrot);
            }
        }
    }

    @Inject(method = "method_17958",
            at = @At(value = "RETURN"))
    private void etf$resetEntity(MatrixStack matrixStack, boolean bl, PlayerEntity playerEntity, VertexConsumerProvider vertexConsumerProvider, NbtCompound nbtCompound, int i, float f, float g, float h, float j, EntityType type, CallbackInfo ci) {
        if (entity_texture_features$parrotNBT != null && etf$heldEntity != null) {
            ETFRenderContext.setCurrentEntity(etf$heldEntity);
        }
        entity_texture_features$parrotNBT = null;
        etf$heldEntity = null;
    }

    @Inject(method = "method_17958(Lnet/minecraft/client/util/math/MatrixStack;ZLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/nbt/NbtCompound;IFFFFLnet/minecraft/entity/EntityType;)V",
            at = @At(value = "HEAD"))
    private <M extends Entity> void etf$getNBT(MatrixStack matrixStack, boolean bl, PlayerEntity playerEntity, VertexConsumerProvider vertexConsumerProvider, NbtCompound nbtCompound, int i, float f, float g, float h, float j, EntityType<M> type, CallbackInfo ci) {
        entity_texture_features$parrotNBT = nbtCompound;
    }

//    @ModifyArg(method = "method_17958(Lnet/minecraft/client/util/math/MatrixStack;ZLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/nbt/NbtCompound;IFFFFLnet/minecraft/entity/EntityType;)V",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider;getBuffer(Lnet/minecraft/client/render/RenderLayer;)Lnet/minecraft/client/render/VertexConsumer;"))
//    private RenderLayer etf$alterTexture(RenderLayer layer) {
//        if (entity_texture_features$parrotNBT != null) {
//            return RenderLayer.getEntityCutoutNoCull(etf$returnAlteredIdentifier());
//        }
//        //vanilla
//        return layer;
//    }
//
//    @Unique
//    private Identifier etf$returnAlteredIdentifier() {
//        EntityType.getEntityFromNbt(entity_texture_features$parrotNBT,entity_texture_features$player.world);
//        Optional<Entity> optionalEntity = EntityType.getEntityFromNbt(entity_texture_features$parrotNBT,entity_texture_features$player.world);
//        if(optionalEntity.isPresent() && optionalEntity.get() instanceof ParrotEntity) {
//            ParrotEntity parrot = (ParrotEntity) optionalEntity.get(); //  new ParrotEntity(EntityType.PARROT, player.world);
//                    //parrot.readCustomDataFromNbt(parrotNBT);
//                    // uuid not manually read from above code
//            //UUID id = parrotNBT.getUuid("UUID");
//            //System.out.println(id);
//            //parrot.setUuid(id);
//
//            entity_texture_features$thisETFTexture = ETFManager.getInstance().getETFTexture(ParrotEntityRenderer.TEXTURES[parrot.getVariant()], new ETFEntityWrapper(parrot), ETFManager.TextureSource.ENTITY, ETFConfigData.removePixelsUnderEmissiveMobs);
//            return entity_texture_features$thisETFTexture.getTextureIdentifier(new ETFEntityWrapper(parrot),true);
//        }else{
//            ETFUtils2.logError("shoulder parrot error");
//            return ParrotEntityRenderer.TEXTURES[entity_texture_features$parrotNBT.getInt("Variant")];
//        }
//    }
//
//    @Inject(method = "method_17958(Lnet/minecraft/client/util/math/MatrixStack;ZLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/nbt/NbtCompound;IFFFFLnet/minecraft/entity/EntityType;)V",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/ParrotEntityModel;poseOnShoulder(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFFI)V",
//                    shift = At.Shift.AFTER))
//    private <M extends Entity> void etf$applyEmissive(MatrixStack matrixStack, boolean bl, PlayerEntity playerEntity, VertexConsumerProvider vertexConsumerProvider, NbtCompound nbtCompound, int i, float f, float g, float h, float j, EntityType<M> type, CallbackInfo ci) {
//        if (entity_texture_features$thisETFTexture != null) entity_texture_features$thisETFTexture.renderEmissive(matrixStack, vertexConsumerProvider, model);
//    }

}


