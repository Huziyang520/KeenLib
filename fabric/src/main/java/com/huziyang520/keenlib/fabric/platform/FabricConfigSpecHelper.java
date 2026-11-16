package com.huziyang520.keenlib.fabric.platform;

import com.huziyang520.keenlib.config.KeenBusinessConfig;
import com.huziyang520.keenlib.platform.Services;
import com.huziyang520.keenlib.platform.services.IConfigSpecHelper;
import com.huziyang520.keenlib.platform.services.SpecBinding;

/**
 * Fabric has no config system of its own, so the spec backend only exists when Forge Config API Port is installed.
 *
 * <p>This class must stay free of that mod's types: it is loaded from {@code Services} on every launch.
 */
public final class FabricConfigSpecHelper implements IConfigSpecHelper {

    private static final String FCAP_ID = "forgeconfigapiport";

    @Override
    public SpecBinding register(KeenBusinessConfig config, String fileName) {
        if (!Services.PLATFORM.isModLoaded(FCAP_ID)) {
            return null;
        }
        try {
            return FcapConfigBridge.register(config, fileName);
        } catch (Throwable t) {
            return null;
        }
    }
}
