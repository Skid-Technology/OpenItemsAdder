package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemEventHandler;
import dev.lone.itemsadder.item.ItemEventType;
import dev.lone.itemsadder.item.ItemHandlerBinding;
import dev.lone.itemsadder.util.SchedulerUtils;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BookEditListener implements IaListener {
   public BookEditListener(ContentManager var1) {
      var1.aK.method933(new ItemHandlerBinding(PlayerInteractEvent.class, this::onInteractBook));
      var1.aK.method933(new ItemHandlerBinding(PlayerEditBookEvent.class, this::onEditBook));
   }

   @ItemEventHandler
   void onInteractBook(PlayerInteractEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (var1.getItem() != null) {
         if (var1.getItem().getType() == Material.WRITTEN_BOOK || var1.getItem().getType() == Material.WRITABLE_BOOK) {
            var2.method8761(var1.getPlayer(), ItemEventType.BOOK_READ, var3x -> var3x.applyOnInteract(var1, var2, var3));
         }
      }
   }

   @ItemEventHandler
   void onEditBook(PlayerEditBookEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (var2.aog.hasEffects(ItemEventType.BOOK_WRITE)) {
         SchedulerUtils.runLaterSync(() -> {
            CustomItemDefinition.method8745(var3, var2.getNamespace(), var2.getId());
            var2.method8761(var1.getPlayer(), ItemEventType.BOOK_WRITE, var3x -> var3x.applyOnEditBook(var1, var2, var3));
         }, 2L);
      }
   }
}
