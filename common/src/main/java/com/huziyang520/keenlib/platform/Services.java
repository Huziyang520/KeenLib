package com.huziyang520.keenlib.platform;

import com.huziyang520.keenlib.Constants;
import com.huziyang520.keenlib.client.IConfigScreenProvider;
import com.huziyang520.keenlib.platform.services.IConfigSpecHelper;
import com.huziyang520.keenlib.platform.services.IPlatformHelper;
import com.huziyang520.keenlib.platform.services.IRegistryHelper;

import java.util.ServiceLoader;

public class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    public static final IRegistryHelper REGISTRY = load(IRegistryHelper.class);

    public static final IConfigSpecHelper CONFIG_SPEC = load(IConfigSpecHelper.class);

    private static IConfigScreenProvider screenProvider;

    /**
     * Client-only service, resolved on first use so the server never links a {@code Screen} type.
     */
    public static synchronized IConfigScreenProvider screenProvider() {
        if (screenProvider == null) {
            screenProvider = load(IConfigScreenProvider.class);
        }
        return screenProvider;
    }

    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz, Services.class.getClassLoader())
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
