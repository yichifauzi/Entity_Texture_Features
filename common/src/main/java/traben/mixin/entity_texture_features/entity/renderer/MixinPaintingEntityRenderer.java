package traben.mixin.entity_texture_features.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PaintingRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import traben.entity_texture_features.ETF;
import traben.entity_texture_features.features.ETFManager;
import traben.entity_texture_features.features.ETFRenderContext;
import traben.entity_texture_features.features.texture_handlers.ETFSprite;
import traben.entity_texture_features.features.texture_handlers.ETFTexture;
import traben.entity_texture_features.utils.ETFEntity;

@Mixin(PaintingRenderer.class)
public abstract class MixinPaintingEntityRenderer extends EntityRenderer<Painting> {



    #if MC >= MC_20_6
    @Shadow protected abstract void vertex(final PoseStack.Pose matrix, final VertexConsumer vertexConsumer, final float x, final float y, final float u, final float v, final float z, final int normalX, final int normalY, final int normalZ, final int light);
    #else
    @Shadow protected abstract void vertex(final Matrix4f matrix4f, final Matrix3f matrix3f, final VertexConsumer vertexConsumer, final float f, final float g, final float h, final float i, final float j, final int k, final int l, final int m, final int n);
    #endif
    @Unique
    private static final ResourceLocation etf$BACK_SPRITE_ID = new ResourceLocation("textures/painting/back.png");


    @SuppressWarnings("unused")
    protected MixinPaintingEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }



    @Inject(
            method = "render(Lnet/minecraft/world/entity/decoration/Painting;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "HEAD"), cancellable = true)
    private void etf$getSprites(Painting paintingEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, CallbackInfo ci) {
        try {
            TextureAtlasSprite paintingSprite = Minecraft.getInstance().getPaintingTextures().get(paintingEntity.getVariant().value());
            ResourceLocation paintingId = paintingSprite.contents().name();
            ResourceLocation paintingTexture = new ResourceLocation(paintingId.getNamespace(), "textures/painting/" + paintingId.getPath() + ".png");

            ETFEntity etfEntity = (ETFEntity) paintingEntity;

            ETFTexture frontTexture = ETFManager.getInstance().getETFTextureVariant(paintingTexture, etfEntity);
            ETFSprite etf$Sprite = frontTexture.getSprite(paintingSprite);


            ETFTexture backTexture = ETFManager.getInstance().getETFTextureVariant(etf$BACK_SPRITE_ID, etfEntity);
            ETFSprite etf$BackSprite = backTexture.getSprite(Minecraft.getInstance().getPaintingTextures().getBackSprite());

            if (etf$Sprite.isETFAltered || etf$Sprite.isEmissive() || etf$BackSprite.isETFAltered || etf$BackSprite.isEmissive()) {
                matrixStack.pushPose();
                matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F - f));
                PaintingVariant paintingVariant = paintingEntity.getVariant().value();
                matrixStack.scale(0.0625F, 0.0625F, 0.0625F);

                etf$renderETFPainting(matrixStack,
                        vertexConsumerProvider,
                        paintingEntity,
                        paintingVariant.getWidth(),
                        paintingVariant.getHeight(),
                        etf$Sprite,
                        etf$BackSprite);
                matrixStack.popPose();
                super.render(paintingEntity, f, g, matrixStack, vertexConsumerProvider, i);
                ci.cancel();
            }


        } catch (Exception e) {
            //ETFUtils2.logError("painting failed at "+paintingEntity.getBlockPos().toShortString());
        }

    }

    @Unique
    private void etf$renderETFPainting(PoseStack matrices, MultiBufferSource vertexConsumerProvider, Painting entity, int width, int height, ETFSprite ETFPaintingSprite, ETFSprite ETFBackSprite) {
        ETFRenderContext.preventRenderLayerTextureModify();
        VertexConsumer vertexConsumerFront = vertexConsumerProvider.getBuffer(RenderType.entitySolid(ETFPaintingSprite.getSpriteVariant().atlasLocation()));
        etf$renderETFPaintingFront(matrices, vertexConsumerFront, entity, width, height, ETFPaintingSprite.getSpriteVariant(), false);

        VertexConsumer vertexConsumerBack = vertexConsumerProvider.getBuffer(RenderType.entitySolid(ETFBackSprite.getSpriteVariant().atlasLocation()));
        etf$renderETFPaintingBack(matrices, vertexConsumerBack, entity, width, height, ETFBackSprite.getSpriteVariant(), false);

        if (ETFPaintingSprite.isEmissive()) {
            vertexConsumerFront = vertexConsumerProvider.getBuffer(RenderType.entityTranslucentCull(ETFPaintingSprite.getEmissive().atlasLocation()));
            etf$renderETFPaintingFront(matrices, vertexConsumerFront, entity, width, height, ETFPaintingSprite.getEmissive(), true);
        }

        if (ETFBackSprite.isEmissive()) {
            vertexConsumerFront = vertexConsumerProvider.getBuffer(RenderType.entityTranslucentCull(ETFBackSprite.getEmissive().atlasLocation()));
            etf$renderETFPaintingBack(matrices, vertexConsumerFront, entity, width, height, ETFBackSprite.getEmissive(), true);
        }
        ETFRenderContext.allowRenderLayerTextureModify();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Unique
    private void etf$renderETFPaintingFront(PoseStack matrices, VertexConsumer vertexConsumerFront, Painting entity, int width, int height, TextureAtlasSprite paintingSprite, boolean emissive) {

        PoseStack.Pose entry = matrices.last();
//        Matrix4f matrix4f = entry.getPositionMatrix();
//        Matrix3f matrix3f = entry.getNormalMatrix();

        float f = (float) (-width) / 2.0F;
        float g = (float) (-height) / 2.0F;
        int u = width / 16;
        int v = height / 16;
        double d = 1.0 / (double) u;
        double e = 1.0 / (double) v;

        for (int w = 0; w < u; ++w) {
            for (int x = 0; x < v; ++x) {
                float y = f + (float) ((w + 1) * 16);
                float z = f + (float) (w * 16);
                float aa = g + (float) ((x + 1) * 16);
                float ab = g + (float) (x * 16);

                int light;
                if (emissive) {
                    light = ETF.EMISSIVE_FEATURE_LIGHT_VALUE;
                } else {
                    int ac = entity.getBlockX();
                    int ad = Mth.floor(entity.getY() + (double) ((aa + ab) / 2.0F / 16.0F));
                    int ae = entity.getBlockZ();
                    Direction direction = entity.getDirection();
                    if (direction == Direction.NORTH) {
                        ac = Mth.floor(entity.getX() + (double) ((y + z) / 2.0F / 16.0F));
                    }

                    if (direction == Direction.WEST) {
                        ae = Mth.floor(entity.getZ() - (double) ((y + z) / 2.0F / 16.0F));
                    }

                    if (direction == Direction.SOUTH) {
                        ac = Mth.floor(entity.getX() - (double) ((y + z) / 2.0F / 16.0F));
                    }

                    if (direction == Direction.EAST) {
                        ae = Mth.floor(entity.getZ() + (double) ((y + z) / 2.0F / 16.0F));
                    }

                    light = LevelRenderer.getLightColor(entity.level(), new BlockPos(ac, ad, ae));
                }

                float ag = paintingSprite.getU((float) (d * (double) (u - w)));
                float ah = paintingSprite.getU((float) (d * (double) (u - (w + 1))));
                float ai = paintingSprite.getV((float) (e * (double) (v - x)));
                float aj = paintingSprite.getV((float) (e * (double) (v - (x + 1))));
                this.vertex(#if MC >= MC_20_6 entry #else entry.pose(),entry.normal() #endif, vertexConsumerFront, y, ab, ah, ai, -0.5F, 0, 0, -1, light);
                this.vertex(#if MC >= MC_20_6 entry #else entry.pose(),entry.normal() #endif, vertexConsumerFront, z, ab, ag, ai, -0.5F, 0, 0, -1, light);
                this.vertex(#if MC >= MC_20_6 entry #else entry.pose(),entry.normal() #endif, vertexConsumerFront, z, aa, ag, aj, -0.5F, 0, 0, -1, light);
                this.vertex(#if MC >= MC_20_6 entry #else entry.pose(),entry.normal() #endif, vertexConsumerFront, y, aa, ah, aj, -0.5F, 0, 0, -1, light);

            }
        }

    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Unique
    private void etf$renderETFPaintingBack(PoseStack matrices, VertexConsumer vertexConsumerBack, Painting entity, int width, int height, TextureAtlasSprite backSprite, boolean emissive) {

        PoseStack.Pose entry = matrices.last();
//        Matrix4f matrix4f = entry.getPositionMatrix();
//        Matrix3f matrix3f = entry.getNormalMatrix();


        float f = (float) (-width) / 2.0F;
        float g = (float) (-height) / 2.0F;
        //float h = 0.5F;
        float i = backSprite.getU0();
        float j = backSprite.getU1();
        float k = backSprite.getV0();
        float l = backSprite.getV1();
        float m = backSprite.getU0();
        float n = backSprite.getU1();
        float o = backSprite.getV0();
        float p = backSprite.getV(0.0625F);
        float q = backSprite.getU0();
        float r = backSprite.getU(0.0625F);
        float s = backSprite.getV0();
        float t = backSprite.getV1();
        int u = width / 16;
        int v = height / 16;

        for (int w = 0; w < u; ++w) {
            for (int x = 0; x < v; ++x) {
                float y = f + (float) ((w + 1) * 16);
                float z = f + (float) (w * 16);
                float aa = g + (float) ((x + 1) * 16);
                float ab = g + (float) (x * 16);

                int light;
                if (emissive) {
                    light = ETF.EMISSIVE_FEATURE_LIGHT_VALUE;
                } else {
                    int ac = entity.getBlockX();
                    int ad = Mth.floor(entity.getY() + (double) ((aa + ab) / 2.0F / 16.0F));
                    int ae = entity.getBlockZ();
                    Direction direction = entity.getDirection();
                    if (direction == Direction.NORTH) {
                        ac = Mth.floor(entity.getX() + (double) ((y + z) / 2.0F / 16.0F));
                    }

                    if (direction == Direction.WEST) {
                        ae = Mth.floor(entity.getZ() - (double) ((y + z) / 2.0F / 16.0F));
                    }

                    if (direction == Direction.SOUTH) {
                        ac = Mth.floor(entity.getX() - (double) ((y + z) / 2.0F / 16.0F));
                    }

                    if (direction == Direction.EAST) {
                        ae = Mth.floor(entity.getZ() + (double) ((y + z) / 2.0F / 16.0F));
                    }

                    light = LevelRenderer.getLightColor(entity.level(), new BlockPos(ac, ad, ae));
                }


                this.vertex(#if MC >= MC_20_6 entry #else entry.pose(),entry.normal() #endif, vertexConsumerBack, y, aa, j, k, 0.5F, 0, 0, 1, light);
                this.vertex(#if MC >= MC_20_6 entry #else entry.pose(),entry.normal() #endif, vertexConsumerBack, z, aa, i, k, 0.5F, 0, 0, 1, light);
                this.vertex(#if MC >= MC_20_6 entry #else entry.pose(),entry.normal() #endif, vertexConsumerBack, z, ab, i, l, 0.5F, 0, 0, 1, light);
                this.vertex(#if MC >= MC_20_6 entry #else entry.pose(),entry.normal() #endif, vertexConsumerBack, y, ab, j, l, 0.5F, 0, 0, 1, light);
                this.vertex(#if MC >= MC_20_6 entry #else entry.pose(),entry.normal() #endif, vertexConsumerBack, y, aa, m, o, -0.5F, 0, 1, 0, light);
                this.vertex(#if MC >= MC_20_6 entry #else entry.pose(),entry.normal() #endif, vertexConsumerBack, z, aa, n, o, -0.5F, 0, 1, 0, light);
                this.vertex(#if MC >= MC_20_6 entry #else entry.pose(),entry.normal() #endif, vertexConsumerBack, z, aa, n, p, 0.5F, 0, 1, 0, light);
                this.vertex(#if MC >= MC_20_6 entry #else entry.pose(),entry.normal() #endif, vertexConsumerBack, y, aa, m, p, 0.5F, 0, 1, 0, light);
                this.vertex(#if MC >= MC_20_6 entry #else entry.pose(),entry.normal() #endif, vertexConsumerBack, y, ab, m, o, 0.5F, 0, -1, 0, light);
                this.vertex(#if MC >= MC_20_6 entry #else entry.pose(),entry.normal() #endif, vertexConsumerBack, z, ab, n, o, 0.5F, 0, -1, 0, light);
                this.vertex(#if MC >= MC_20_6 entry #else entry.pose(),entry.normal() #endif, vertexConsumerBack, z, ab, n, p, -0.5F, 0, -1, 0, light);
                this.vertex(#if MC >= MC_20_6 entry #else entry.pose(),entry.normal() #endif, vertexConsumerBack, y, ab, m, p, -0.5F, 0, -1, 0, light);
                this.vertex(#if MC >= MC_20_6 entry #else entry.pose(),entry.normal() #endif, vertexConsumerBack, y, aa, r, s, 0.5F, -1, 0, 0, light);
                this.vertex(#if MC >= MC_20_6 entry #else entry.pose(),entry.normal() #endif, vertexConsumerBack, y, ab, r, t, 0.5F, -1, 0, 0, light);
                this.vertex(#if MC >= MC_20_6 entry #else entry.pose(),entry.normal() #endif, vertexConsumerBack, y, ab, q, t, -0.5F, -1, 0, 0, light);
                this.vertex(#if MC >= MC_20_6 entry #else entry.pose(),entry.normal() #endif, vertexConsumerBack, y, aa, q, s, -0.5F, -1, 0, 0, light);
                this.vertex(#if MC >= MC_20_6 entry #else entry.pose(),entry.normal() #endif, vertexConsumerBack, z, aa, r, s, -0.5F, 1, 0, 0, light);
                this.vertex(#if MC >= MC_20_6 entry #else entry.pose(),entry.normal() #endif, vertexConsumerBack, z, ab, r, t, -0.5F, 1, 0, 0, light);
                this.vertex(#if MC >= MC_20_6 entry #else entry.pose(),entry.normal() #endif, vertexConsumerBack, z, ab, q, t, 0.5F, 1, 0, 0, light);
                this.vertex(#if MC >= MC_20_6 entry #else entry.pose(),entry.normal() #endif, vertexConsumerBack, z, aa, q, s, 0.5F, 1, 0, 0, light);

            }
        }

    }


}

