package com.sigmundgranaas.forgero.fabric.mixins;

import com.sigmundgranaas.forgero.minecraft.common.toolhandler.PropertyHelper;
import com.sigmundgranaas.forgero.minecraft.common.toolhandler.ToolBlockHandler;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class PlayerServerInteractionManagerMixin {
    @Shadow
    @Final
    protected ServerPlayerEntity player;
    @Shadow
    protected ServerWorld world;

    @Shadow
    public abstract void finishMining(BlockPos pos, int sequence, String reason);

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerInteractionManager;finishMining(Lnet/minecraft/util/math/BlockPos;ILjava/lang/String;)V"), method = "processBlockBreakingAction")
    public void processBlockBreakingAction(BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight, int sequence, CallbackInfo ci) {
        PropertyHelper.ofPlayerHands(player)
                .flatMap(container -> ToolBlockHandler.of(container, world, pos, player))
                .ifPresent(handler -> handler.handleExceptOrigin(info -> this.finishMining(info.pos(), sequence, "destroyed")));
    }
}
