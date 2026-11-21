package dev.lone.itemsadder.util;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class SyntheticBlockBreakEvent extends BlockBreakEvent {
   public SyntheticBlockBreakEvent(Block var1, Player var2) {
      super(var1, var2);
   }
}
