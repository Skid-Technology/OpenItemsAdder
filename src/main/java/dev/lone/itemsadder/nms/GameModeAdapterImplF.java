package dev.lone.itemsadder.nms;

import beer.devs.nativepackets.api.ClientGameEvent;
import beer.devs.nativepackets.api.NativePlayerAbilities;
import dev.lone.itemsadder.network.NativePacketsHolder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Abilities;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class GameModeAdapterImplF implements GameModeAdapter {
   @Override
   public void method3423(Player bukkitPlayer, float gamemode) {
      NativePacketsHolder.PE().method1867(bukkitPlayer, NativePacketsHolder.PE().Pv().gameEvent(ClientGameEvent.CHANGE_GAME_MODE, gamemode));
   }

   @Override
   public void cw(Player bukkitPlayer) {
      ServerPlayer var2 = ((CraftPlayer)bukkitPlayer).getHandle();
      Abilities var3 = var2.getAbilities();
      NativePacketsHolder.PE()
         .method1868(
            bukkitPlayer,
            NativePacketsHolder.PE()
               .Pv()
               .playerAbilities(
                  new NativePlayerAbilities(var3.invulnerable, var3.flying, var3.mayfly, var3.instabuild, var3.getFlyingSpeed(), var3.getWalkingSpeed())
               )
         );
   }

   @Override
   public void method3425(Player bukkitPlayer, boolean canFly, boolean flying, float flySpeed) {
      ServerPlayer var5 = ((CraftPlayer)bukkitPlayer).getHandle();
      Abilities var6 = var5.getAbilities();
      NativePacketsHolder.PE()
         .method1868(
            bukkitPlayer,
            NativePacketsHolder.PE()
               .Pv()
               .playerAbilities(new NativePlayerAbilities(var6.invulnerable, flying, canFly, var6.instabuild, flySpeed, var6.getWalkingSpeed()))
         );
   }
}
