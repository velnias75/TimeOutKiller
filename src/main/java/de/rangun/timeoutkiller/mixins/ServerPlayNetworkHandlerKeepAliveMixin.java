package de.rangun.timeoutkiller.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

@Mixin(ServerPlayNetworkHandler.class)
public final class ServerPlayNetworkHandlerKeepAliveMixin {
	@Shadow
	private long lastKeepAliveTime;

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;disconnect"
			+ "(Lnet/minecraft/text/Text;)V", ordinal = 2))
	private void disconnect(ServerPlayNetworkHandler handler, Text reason) {
		final long keepAliveTimeoutMillis = 120 * 1000L;

		if (Util.getMeasuringTimeMs() - lastKeepAliveTime >= keepAliveTimeoutMillis) {
			handler.disconnect(reason);
		}
	}

	@ModifyConstant(method = "tick", constant = { @Constant(longValue = 15000L),
			// CraftBukkit changes it to 25000.
			@Constant(longValue = 25000L) })
	private long getKeepAlivePacketInterval(long interval) {
		return 7000 * 1000L;
	}
}