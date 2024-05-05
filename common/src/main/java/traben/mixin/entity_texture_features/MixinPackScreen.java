package traben.mixin.entity_texture_features;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import traben.entity_texture_features.ETF;
import traben.entity_texture_features.config.screens.ETFConfigScreenMain;

import java.nio.file.Path;
import java.util.Objects;

@Mixin(PackSelectionScreen.class)
public abstract class MixinPackScreen extends Screen {


    @Unique
    private static final ResourceLocation etf$FOCUSED = new ResourceLocation("entity_features", "textures/gui/settings_focused.png");
    @Unique
    private static final ResourceLocation etf$UNFOCUSED = new ResourceLocation("entity_features", "textures/gui/settings_unfocused.png");
    @Shadow
    @Final
    private Path packDir;
    @Shadow
    private Button doneButton;

    @SuppressWarnings("unused")
    protected MixinPackScreen(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void etf$etfButton(CallbackInfo ci) {
        if (!ETF.config().getConfig().hideConfigButton
                && this.minecraft != null
                //ensure this is the resource-pack screen and not the data-pack screen
                && this.packDir.equals(this.minecraft.getResourcePackDirectory())
                //fabric api required for mod asset texture loading
                && (ETF.isFabric() == ETF.isThisModLoaded("fabric"))
            //check for 1.20.2
//                && MinecraftClient.getInstance().getResourceManager().getResource(etf$focused).isPresent()
        ) {

            int x = doneButton.getX() + doneButton.getWidth() + 8;
            int y = doneButton.getY();

// simple text button
//            this.addDrawableChild(ButtonWidget.builder(Text.of("ETF"),
//                            (button) -> Objects.requireNonNull(client).setScreen(new ETFConfigScreenMain(this)))
//                    .dimensions(x, y, 24, 20)
//                    .tooltip(Tooltip.of(ETFVersionDifferenceHandler.getTextFromTranslation(
//                            "config.entity_texture_features.button_tooltip")))
//                    .build()
//            );

            //1.20.2 onwards textured button requires these overrides
            this.addRenderableWidget(new ImageButton(
                    x, y, 24, 20,
                    new WidgetSprites(etf$UNFOCUSED, etf$FOCUSED),
                    (button) -> Objects.requireNonNull(minecraft).setScreen(new ETFConfigScreenMain(this)),
                    Component.nullToEmpty("")) {
                {
                    setTooltip(Tooltip.create(ETF.getTextFromTranslation(
                            "config.entity_features.button_tooltip")));
                }

                //override required because textured button widget just doesnt work
                @Override
                public void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
                    ResourceLocation identifier = this.isHoveredOrFocused() ? etf$FOCUSED : etf$UNFOCUSED;
                    context.blit(identifier, this.getX(), this.getY(), 0, 0, this.width, this.height, this.width, this.height);
                }

            });

        }
    }
}

