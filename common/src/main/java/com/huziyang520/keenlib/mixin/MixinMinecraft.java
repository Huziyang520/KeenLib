package com.huziyang520.keenlib.mixin;

import com.huziyang520.keenlib.notice.KeenNoticeDispatcher;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 客户端 tick 钩子：用于检测“进入世界”并派发聊天框提示。
 * 只注册在 mixin 配置的 client 段，服务端不会加载。
 */
@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Inject(method = "tick", at = @At("HEAD"))
    private void keenlib$onClientTick(CallbackInfo info) {
        KeenNoticeDispatcher.clientTick((Minecraft) (Object) this);
    }
}
