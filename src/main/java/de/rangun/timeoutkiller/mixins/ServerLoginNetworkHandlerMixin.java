package de.rangun.timeoutkiller.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

@Mixin(ServerLoginNetworkHandler.class)
public final class ServerLoginNetworkHandlerMixin {
	@Shadow
	private int loginTicks;

	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo info) {
		if (loginTicks >= 2400) {
			((ServerLoginNetworkHandler) (Object) this)
					.disconnect(new TranslatableText("multiplayer.disconnect.slow_login"));
		}
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerLoginNetworkHandler;disconnect"
			+ "(Lnet/minecraft/text/Text;)V"))
	private void disconnect(ServerLoginNetworkHandler handler, Text reason) {
		// No-op.
	}
}