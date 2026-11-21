package dev.lone.itemsadder.Core.OtherPlugins;

import org.bukkit.World;

public class AdvancedSlimePaperWrapperDummy extends AdvancedSlimePaperWrapper {
   @Override
   public boolean isSlimeWorld(World world) {
      return false;
   }
}
