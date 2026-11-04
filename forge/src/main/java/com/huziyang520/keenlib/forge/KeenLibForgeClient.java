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

/**
 * Forge 1.20.1 的客户端侧：配置界面入口 + 进入世界提示的 tick 派发（用事件代替客户端 Mixin）。
 */
public final class KeenLibForgeClient {

    private KeenLibForgeClient() {
    }

    public static void init(ModLoadingContext context) {
        if (KeenConfigScreenHook.isAvailable()) {
            context.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new ConfigScreenHandler.ConfigScreenFactory(
                            (minecraft, parent) -> KeenConfigScreenHook.create(parent)));
        } else {
            Constants.LOG.debug("Cloth Config not installed; KeenLib config screen is unavailable");
        }
        MinecraftForge.EVENT_BUS.register(ClientTicks.class);
    }

    /** 客户端 tick 钩子：检测「进入世界」并派发聊天框提示。 */
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
