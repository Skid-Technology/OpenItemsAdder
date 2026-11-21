package dev.lone.itemsadder.text;

import dev.lone.itemsadder.nms.NmsRegistry;
import org.bukkit.entity.Player;

public final class NmsChatService {
   public static final Object aPl = eP("");
   private final NmsChatAdapter aPm = (NmsChatAdapter)NmsRegistry.method2777(NmsChatAdapter.class);
   private static NmsChatService aPn;

   private static NmsChatService Np() {
      if (aPn == null) {
         aPn = new NmsChatService();
      }

      return aPn;
   }

   public static NmsChatAdapter Nq() {
      return Np().aPm;
   }

   NmsChatService() {
   }

   public static void cl(Player player) {
      Np().aPm.cl(player);
   }

   public static void method2951(Player player, String message) {
      Np().aPm.method2954(player, message);
   }

   public static void method2952(Player player, Object component) {
      Np().aPm.method2955(player, component);
   }

   public static Object eP(String var0) {
      return Np().aPm.eQ(var0);
   }

   public static Object method2953(String var0, int var1) {
      return Np().aPm.method2956(var0, var1);
   }
}
