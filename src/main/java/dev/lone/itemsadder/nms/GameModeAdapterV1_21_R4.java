package dev.lone.itemsadder.nms;

import beer.devs.nativepackets.api.ClientGameEvent;
import beer.devs.nativepackets.api.NativePlayerAbilities;
import dev.lone.itemsadder.network.NativePacketsHolder;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.player.PlayerAbilities;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class GameModeAdapterV1_21_R4 implements GameModeAdapter {
   @Override
   public void method3423(Player bukkitPlayer, float gamemode) {
      NativePacketsHolder.PE().method1867(bukkitPlayer, NativePacketsHolder.PE().Pv().gameEvent(ClientGameEvent.CHANGE_GAME_MODE, gamemode));
   }

   @Override
   public void cw(Player bukkitPlayer) {
      EntityPlayer var2 = ((CraftPlayer)bukkitPlayer).getHandle();
      PlayerAbilities var3 = var2.gk();
      NativePacketsHolder.PE()
         .method1868(bukkitPlayer, NativePacketsHolder.PE().Pv().playerAbilities(new NativePlayerAbilities(var3.a, var3.b, var3.c, var3.d, var3.m, var3.n)));
   }

   @Override
   public void method3425(Player bukkitPlayer, boolean canFly, boolean flying, float flySpeed) {
      EntityPlayer var5 = ((CraftPlayer)bukkitPlayer).getHandle();
      PlayerAbilities var6 = var5.gk();
      NativePacketsHolder.PE()
         .method1868(bukkitPlayer, NativePacketsHolder.PE().Pv().playerAbilities(new NativePlayerAbilities(var6.a, flying, canFly, var6.d, flySpeed, var6.n)));
   }
}
