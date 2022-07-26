package traben.entity_texture_features.forge;

import me.shedaniel.clothconfig2.forge.api.ConfigScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.ConfigGuiHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import traben.entity_texture_features.ETFClientCommon;
import traben.entity_texture_features.ETFVersionDifferenceHandler;

import static net.minecraftforge.fml.network.FMLNetworkConstants.IGNORESERVERONLY;


@Mod("entity_texture_features")
public class ETFClientForge {
    public ETFClientForge() {
        // Submit our event bus to let architectury register our content on the right time
        //EventBuses.registerModEventBus(ExampleMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        if(FMLEnvironment.dist == Dist.CLIENT) {
            try {

                ModLoadingContext.get().registerExtensionPoint(
                        ExtensionPoint.CONFIGGUIFACTORY,
                                                () -> (mc, screen) -> ETFVersionDifferenceHandler.getConfigScreen(screen, false)
                );

            } catch (NoClassDefFoundError e) {
                System.out.println("[Entity Texture Features]: Mod settings cannot be edited in GUI without cloth config");
            }

            ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, ()-> Pair.of(()-> FMLNetworkConstants.IGNORESERVERONLY, (version, network) -> {return true;}));
            ETFClientCommon.start();
        } else {

            throw new UnsupportedOperationException("Attempting to load a clientside only mod on the server, refusing");
        }
    }
}