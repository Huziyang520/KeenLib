package com.huziyang520.keenlib.forge;

import com.huziyang520.keenlib.Constants;
import com.huziyang520.keenlib.client.KeenConfigScreenHook;
import com.huziyang520.keenlib.notice.KeenNoticeDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;

public final class KeenLibForgeClient {

    private KeenLibForgeClient() {
    }

    public static void init(ModLoadingContext context) {
        com.huziyang520.keenlib.forge.client.ForgeScreenBridge.registerOwnConfig(context);
        MinecraftForge.EVENT_BUS.register(ClientTicks.class);
    }

    public static final class ClientTicks {

        private ClientTicks() {
        }

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.END) {
                return;
            }
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft == null) {
                return;
            }
            KeenNoticeDispatcher.clientTick(minecraft);
        }
    }
}
