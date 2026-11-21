package dev.lone.itemsadder.loot;

import beer.devs.nativepackets.api.MerchantOffersPacketView;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.TimeUnit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;

public interface MerchantOfferInterceptor {
   Cache bpR = CacheBuilder.newBuilder().expireAfterWrite(30L, TimeUnit.MINUTES).build();

   default void cP(Player var1) {
      bpR.invalidate(var1);
   }

   default void method5383(Player player, MerchantOffersPacketView packet) {
      throw new RuntimeException("Not implemented!");
   }

   boolean method5384(MerchantInventory var1, int var2, int var3, ItemStack var4);
}
