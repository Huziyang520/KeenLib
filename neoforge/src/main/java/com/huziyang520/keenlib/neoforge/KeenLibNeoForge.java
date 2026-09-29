package com.huziyang520.keenlib.neoforge;

import com.huziyang520.keenlib.KeenLib;
import com.huziyang520.keenlib.Constants;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

/**
 * KeenLib 是客户端与服务端均可用、且允许仅客户端安装的共享库，故不限定 dist（默认双端加载）。
 */
@Mod(Constants.MOD_ID)
public class KeenLibNeoForge {

    public KeenLibNeoForge(IEventBus eventBus) {

        Constants.LOG.debug("KeenNeoForge constructor");
        KeenLib.init();
    }
}
