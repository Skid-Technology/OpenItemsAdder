package dev.lone.itemsadder.Core.OtherPlugins;

import com.fastasyncworldedit.core.configuration.Settings;
import com.fastasyncworldedit.core.extent.clipboard.URIClipboardHolder;
import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.session.ClipboardHolder;
import dev.lone.itemsadder.Core.OtherPlugins.FaweWorldEditBridge.Processor;
import java.util.List;
import org.bukkit.World;
import org.bukkit.entity.Player;

final class FaweWorldEditBridge {
   static void allowExtent(String className) {
      List var1 = Settings.settings().EXTENT.ALLOWED_PLUGINS;
      if (!var1.contains(className)) {
         var1.add(className);
      }
   }

   static Clipboard entitySource(Clipboard clipboard) {
      return clipboard instanceof BlockArrayClipboard var1 ? var1.getParent() : clipboard;
   }

   static void beforePaste(Player player, WorldEditIntegration integration) {
      try {
         ClipboardHolder var2 = WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(player)).getClipboard();
         Clipboard var3 = var2.getClipboard();
         integration.beforeFawePaste(var3, var2 instanceof URIClipboardHolder var4 ? var4.getURI(var3) : null);
      } catch (EmptyClipboardException var5) {
      }
   }

   static Object registerPasteListener(WorldEditIntegration integration) {
      WorldEditPasteListener var1 = new WorldEditPasteListener(integration);
      WorldEdit.getInstance().getEventBus().register(var1);
      return var1;
   }

   static void register(EditSessionEvent event, World world, WorldEditIntegration integration) {
      Processor var3 = new Processor(world, integration);
      event.getExtent().addProcessor(var3);
      event.getExtent().addPostProcessor(var3);
   }
}
