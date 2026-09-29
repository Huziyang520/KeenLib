package com.huziyang520.keenlib.fabric;

import com.huziyang520.keenlib.KeenLib;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

/**
 * KeenLib 是客户端与服务端均可用、且允许仅客户端安装的共享库，因此主/客户端入口都进行初始化。
 */
public class KeenLibFabric implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        KeenLib.init();
    }

    @Override
    public void onInitializeClient() {
        KeenLib.init();
    }
}
