package dev.lone.itemsadder.api.Events;

import dev.lone.itemsadder.block.CustomBlockDefinition;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CustomBlockPlaceEvent extends PlayerEvent implements Cancellable {
   final Block block;
   public final CustomBlockDefinition internal;
   protected final boolean canBuild;
   protected final Block placedAgainst;
   protected final BlockState replacedBlockState;
   protected final ItemStack itemInHand;
   private static final HandlerList HANDLERS_LIST = new HandlerList();
   private boolean isCancelled;

   public CustomBlockPlaceEvent(
      Player player, Block block, CustomBlockDefinition internal, boolean canBuild, Block placedAgainst, BlockState replacedBlockState, ItemStack itemInHand
   ) {
      super(player);
      this.block = block;
      this.internal = internal;
      this.canBuild = canBuild;
      this.placedAgainst = placedAgainst;
      this.replacedBlockState = replacedBlockState;
      this.itemInHand = itemInHand;
   }

   public CustomBlockPlaceEvent(BlockPlaceEvent e, CustomBlockDefinition customBlockItem) {
      super(e.getPlayer());
      this.internal = customBlockItem;
      this.canBuild = e.canBuild();
      this.placedAgainst = e.getBlockAgainst();
      this.replacedBlockState = e.getBlockReplacedState();
      this.itemInHand = e.getItemInHand();
      this.block = e.getBlock();
      this.isCancelled = false;
   }

   public boolean isCancelled() {
      return this.isCancelled;
   }

   public void setCancelled(boolean cancelled) {
      this.isCancelled = cancelled;
   }

   @NotNull
   public HandlerList getHandlers() {
      return HANDLERS_LIST;
   }

   @NotNull
   public static HandlerList getHandlerList() {
      return HANDLERS_LIST;
   }

   public ItemStack getCustomBlockItem() {
      return this.internal.Ds();
   }

   public String getNamespacedID() {
      return this.internal.getNamespacedID();
   }

   public Block getBlock() {
      return this.block;
   }

   public boolean isCanBuild() {
      return this.canBuild;
   }

   public Block getPlacedAgainst() {
      return this.placedAgainst;
   }

   public BlockState getReplacedBlockState() {
      return this.replacedBlockState;
   }

   public ItemStack getItemInHand() {
      return this.itemInHand;
   }
}
