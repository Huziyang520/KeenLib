package com.huziyang520.keenlib.mixin;

import com.huziyang520.keenlib.notice.KeenNoticeDispatcher;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Inject(method = "tick", at = @At("HEAD"))
    private void keenlib$onClientTick(CallbackInfo info) {
        KeenNoticeDispatcher.clientTick((Minecraft) (Object) this);
    }
}
