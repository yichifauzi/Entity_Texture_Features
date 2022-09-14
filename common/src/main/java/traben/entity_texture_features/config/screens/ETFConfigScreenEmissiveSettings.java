package traben.entity_texture_features.config.screens;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.text.Text;
import traben.entity_texture_features.ETFClientCommon;
import traben.entity_texture_features.ETFVersionDifferenceHandler;

import java.util.Objects;

//inspired by puzzles custom gui code
public class ETFConfigScreenEmissiveSettings extends ETFConfigScreen {
    protected ETFConfigScreenEmissiveSettings(Screen parent) {
        super(ETFVersionDifferenceHandler.getTextFromTranslation("config." + ETFClientCommon.MOD_ID + ".emissive_settings.title"), parent);

    }


    @Override
    protected void init() {
        super.init();
        this.addButton(getETFButton((int) (this.width * 0.55), (int) (this.height * 0.9), (int) (this.width * 0.2), 20,
                ScreenTexts.BACK,
                (button) -> Objects.requireNonNull(client).openScreen(parent)));
        this.addButton(getETFButton((int) (this.width * 0.25), (int) (this.height * 0.9), (int) (this.width * 0.2), 20,
                ETFVersionDifferenceHandler.getTextFromTranslation("config." + ETFClientCommon.MOD_ID + ".reset_defaults"),
                (button) -> {
                    //temporaryETFConfig = new ETFConfig();
                    ETFConfigScreenMain.temporaryETFConfig.enableEmissiveTextures = true;
                    ETFConfigScreenMain.temporaryETFConfig.fullBrightEmissives = false;
                    ETFConfigScreenMain.temporaryETFConfig.alwaysCheckVanillaEmissiveSuffix = true;
                    ETFConfigScreenMain.temporaryETFConfig.enableElytra = true;
                    ETFConfigScreenMain.temporaryETFConfig.specialEmissiveShield = true;
                    ETFConfigScreenMain.temporaryETFConfig.enableEmissiveBlockEntities = true;
                    Objects.requireNonNull(client).openScreen(new ETFConfigScreenEmissiveSettings(parent));
                    this.removed();
                }));

        this.addButton(getETFButton((int) (this.width * 0.025), (int) (this.height * 0.3), (int) (this.width * 0.45), 20,
                Text.of(ETFVersionDifferenceHandler.getTextFromTranslation(
                        "config." + ETFClientCommon.MOD_ID + ".enable_emissive_textures.title"
                ).getString() + ": " + (ETFConfigScreenMain.temporaryETFConfig.enableEmissiveTextures ? ScreenTexts.ON : ScreenTexts.OFF).getString()),
                (button) -> {
                    ETFConfigScreenMain.temporaryETFConfig.enableEmissiveTextures = !ETFConfigScreenMain.temporaryETFConfig.enableEmissiveTextures;
                    button.setMessage(Text.of(ETFVersionDifferenceHandler.getTextFromTranslation(
                            "config." + ETFClientCommon.MOD_ID + ".enable_emissive_textures.title"
                    ).getString() + ": " + (ETFConfigScreenMain.temporaryETFConfig.enableEmissiveTextures ? ScreenTexts.ON : ScreenTexts.OFF).getString()));
                },
                ETFVersionDifferenceHandler.getTextFromTranslation("config." + ETFClientCommon.MOD_ID + ".enable_emissive_textures.tooltip")
        ));
        this.addButton(getETFButton((int) (this.width * 0.025), (int) (this.height * 0.4), (int) (this.width * 0.45), 20,
                Text.of(ETFVersionDifferenceHandler.getTextFromTranslation(
                        "config." + ETFClientCommon.MOD_ID + ".full_bright_emissives.title"
                ).getString() + ": " + (ETFConfigScreenMain.temporaryETFConfig.fullBrightEmissives ? ScreenTexts.ON : ScreenTexts.OFF).getString()),
                (button) -> {
                    ETFConfigScreenMain.temporaryETFConfig.fullBrightEmissives = !ETFConfigScreenMain.temporaryETFConfig.fullBrightEmissives;
                    button.setMessage(Text.of(ETFVersionDifferenceHandler.getTextFromTranslation(
                            "config." + ETFClientCommon.MOD_ID + ".full_bright_emissives.title"
                    ).getString() + ": " + (ETFConfigScreenMain.temporaryETFConfig.fullBrightEmissives ? ScreenTexts.ON : ScreenTexts.OFF).getString()));
                },
                ETFVersionDifferenceHandler.getTextFromTranslation("config." + ETFClientCommon.MOD_ID + ".full_bright_emissives.tooltip")
        ));
        this.addButton(getETFButton((int) (this.width * 0.025), (int) (this.height * 0.5), (int) (this.width * 0.45), 20,
                Text.of(ETFVersionDifferenceHandler.getTextFromTranslation(
                        "config." + ETFClientCommon.MOD_ID + ".always_check_vanilla_emissive_suffix.title"
                ).getString() + ": " + (ETFConfigScreenMain.temporaryETFConfig.alwaysCheckVanillaEmissiveSuffix ? ScreenTexts.ON : ScreenTexts.OFF).getString()),
                (button) -> {
                    ETFConfigScreenMain.temporaryETFConfig.alwaysCheckVanillaEmissiveSuffix = !ETFConfigScreenMain.temporaryETFConfig.alwaysCheckVanillaEmissiveSuffix;
                    button.setMessage(Text.of(ETFVersionDifferenceHandler.getTextFromTranslation(
                            "config." + ETFClientCommon.MOD_ID + ".always_check_vanilla_emissive_suffix.title"
                    ).getString() + ": " + (ETFConfigScreenMain.temporaryETFConfig.alwaysCheckVanillaEmissiveSuffix ? ScreenTexts.ON : ScreenTexts.OFF).getString()));
                },
                ETFVersionDifferenceHandler.getTextFromTranslation("config." + ETFClientCommon.MOD_ID + ".always_check_vanilla_emissive_suffix.tooltip")
        ));

        this.addButton(getETFButton((int) (this.width * 0.025), (int) (this.height * 0.7), (int) (this.width * 0.8), 20,
                ETFVersionDifferenceHandler.getTextFromTranslation("config." + ETFClientCommon.MOD_ID + ".emissive_fix.button"),
                (button) -> Objects.requireNonNull(client).openScreen(new ETFConfigScreenEmissiveFixSettings(this))
        ));


        this.addButton(getETFButton((int) (this.width * 0.525), (int) (this.height * 0.3), (int) (this.width * 0.45), 20,
                Text.of(ETFVersionDifferenceHandler.getTextFromTranslation(
                        "config." + ETFClientCommon.MOD_ID + ".enable_elytra.title"
                ).getString() + ": " + (ETFConfigScreenMain.temporaryETFConfig.enableElytra ? ScreenTexts.ON : ScreenTexts.OFF).getString()),
                (button) -> {
                    ETFConfigScreenMain.temporaryETFConfig.enableElytra = !ETFConfigScreenMain.temporaryETFConfig.enableElytra;
                    button.setMessage(Text.of(ETFVersionDifferenceHandler.getTextFromTranslation(
                            "config." + ETFClientCommon.MOD_ID + ".enable_elytra.title"
                    ).getString() + ": " + (ETFConfigScreenMain.temporaryETFConfig.enableElytra ? ScreenTexts.ON : ScreenTexts.OFF).getString()));
                },
                ETFVersionDifferenceHandler.getTextFromTranslation("config." + ETFClientCommon.MOD_ID + ".enable_elytra.tooltip")
        ));

        this.addButton(getETFButton((int) (this.width * 0.525), (int) (this.height * 0.4), (int) (this.width * 0.45), 20,
                Text.of(ETFVersionDifferenceHandler.getTextFromTranslation(
                        "config." + ETFClientCommon.MOD_ID + ".emissive_shield.title"
                ).getString() + ": " + (ETFConfigScreenMain.temporaryETFConfig.specialEmissiveShield ? ScreenTexts.ON : ScreenTexts.OFF).getString()),
                (button) -> {
                    ETFConfigScreenMain.temporaryETFConfig.specialEmissiveShield = !ETFConfigScreenMain.temporaryETFConfig.specialEmissiveShield;
                    button.setMessage(Text.of(ETFVersionDifferenceHandler.getTextFromTranslation(
                            "config." + ETFClientCommon.MOD_ID + ".emissive_shield.title"
                    ).getString() + ": " + (ETFConfigScreenMain.temporaryETFConfig.specialEmissiveShield ? ScreenTexts.ON : ScreenTexts.OFF).getString()));
                },
                ETFVersionDifferenceHandler.getTextFromTranslation("config." + ETFClientCommon.MOD_ID + ".emissive_shield.tooltip")
        ));
        this.addButton(getETFButton((int) (this.width * 0.525), (int) (this.height * 0.5), (int) (this.width * 0.45), 20,
                Text.of(ETFVersionDifferenceHandler.getTextFromTranslation(
                        "config." + ETFClientCommon.MOD_ID + ".emissive_block_entity.title"
                ).getString() + ": " + (ETFConfigScreenMain.temporaryETFConfig.enableEmissiveBlockEntities ? ScreenTexts.ON : ScreenTexts.OFF).getString()),
                (button) -> {
                    ETFConfigScreenMain.temporaryETFConfig.enableEmissiveBlockEntities = !ETFConfigScreenMain.temporaryETFConfig.enableEmissiveBlockEntities;
                    button.setMessage(Text.of(ETFVersionDifferenceHandler.getTextFromTranslation(
                            "config." + ETFClientCommon.MOD_ID + ".emissive_block_entity.title"
                    ).getString() + ": " + (ETFConfigScreenMain.temporaryETFConfig.enableEmissiveBlockEntities ? ScreenTexts.ON : ScreenTexts.OFF).getString()));
                },
                ETFVersionDifferenceHandler.getTextFromTranslation("config." + ETFClientCommon.MOD_ID + ".emissive_block_entity.tooltip")
        ));


    }


    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);

        drawCenteredText(matrices, textRenderer, ETFVersionDifferenceHandler.getTextFromTranslation("config." + ETFClientCommon.MOD_ID + ".special_emissive_settings.title"), (int) (width * 0.75), (int) (height * 0.25), 0xFFFFFF);

    }

}