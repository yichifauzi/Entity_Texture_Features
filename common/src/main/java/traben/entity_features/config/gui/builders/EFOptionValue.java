package traben.entity_features.config.gui.builders;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class EFOptionValue<V> extends EFOption{


    protected final Supplier<V> getter;
    protected final Consumer<V> setter;
    protected final V defaultValue;


    protected EFOptionValue(String text, String tooltip, Supplier<V> getter, Consumer<V> setter, V defaultValue) {
        super(text, tooltip);
        this.getter = getter;
        this.setter = setter;
        this.defaultValue = defaultValue;
    }

    public boolean saveValuesToConfig(){
        V value = getValueFromWidget();
        boolean changed = !value.equals(getter.get());
        if(changed)
            setter.accept(value);
        return changed;
    }

    protected abstract V getValueFromWidget();


    @Override
    void setValuesToDefault() {
        setWidgetToDefaultValue();
    }

    abstract void setWidgetToDefaultValue();

    @Override
    void resetValuesToInitial() {
        resetWidgetToInitialValue();
    }

    abstract void resetWidgetToInitialValue();
}
