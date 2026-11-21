package dev.lone.itemsadder.Core.OtherPlugins;

import org.bukkit.World;

public abstract class AdvancedSlimePaperWrapper {
   private static AdvancedSlimePaperWrapper inst;

   public static AdvancedSlimePaperWrapper inst() {
      return inst == null ? null : inst;
   }

   public static void init() {
      try {
         Class.forName("com.infernalsuite.aswm.api.AdvancedSlimePaperAPI");
         inst = new AdvancedSlimePaperWrapperv3();
      } catch (NoClassDefFoundError | ClassNotFoundException var2) {
         try {
            Class.forName("com.infernalsuite.asp.api.AdvancedSlimePaperAPI");
            inst = new AdvancedSlimePaperWrapperv4();
         } catch (NoClassDefFoundError | ClassNotFoundException var1) {
            inst = new AdvancedSlimePaperWrapperDummy();
         }
      }
   }

   public abstract boolean isSlimeWorld(World var1);
}
