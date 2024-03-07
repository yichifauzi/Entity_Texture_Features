package traben.entity_features.config.gui.builders;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;

import java.util.function.Supplier;

public class EFOptionCustomScreenOpener extends EFOption {





    private Screen screen = null;

    //don't need to init screen each time
    private Screen getScreen(){
        if (!screenIsSingleton) return screenSupplier.get();
        if(screen == null){
            screen = screenSupplier.get();
        }
        return screen;
    }

    @Override
    public <T extends Element & Drawable & Selectable> T  getWidget(final int x, final int y, final int width, final int height) {
        //noinspection unchecked
        return (T) ButtonWidget.builder(getText(),(button)-> MinecraftClient.getInstance().setScreen(getScreen())
        ).dimensions(x,y,width,height).tooltip(getTooltip()).build();
    }

    @Override
    boolean saveValuesToConfig() {
        return savedSupplier.get();
    }

    private final Supplier<Screen> screenSupplier;
    private final Supplier<Boolean> savedSupplier;
    private final Runnable setValuesDefault;
    private final Runnable resetValuesToInitial;
    private final boolean screenIsSingleton;
    public EFOptionCustomScreenOpener(final String text, final String tooltip,
                                      Supplier<Screen> screenSupplier, Supplier<Boolean> savedSupplier,
                                      Runnable setValuesDefault, Runnable resetValuesToInitial,
                                      boolean screenIsSingleton) {
        super(text, tooltip);
        this.screenSupplier = screenSupplier;
        this.savedSupplier = savedSupplier;
        this.screenIsSingleton = screenIsSingleton;
        this.setValuesDefault = setValuesDefault;
        this.resetValuesToInitial = resetValuesToInitial;
    }

    public EFOptionCustomScreenOpener(final String text, final String tooltip,
                                      Supplier<Screen> screenSupplier,
                                      boolean screenIsSingleton) {
        this(text, tooltip, screenSupplier, ()->false, ()->{},()->{},screenIsSingleton);
    }

    @Override
    void setValuesToDefault() {
        setValuesDefault.run();
    }

    @Override
    void resetValuesToInitial() {
        resetValuesToInitial.run();
    }
}
