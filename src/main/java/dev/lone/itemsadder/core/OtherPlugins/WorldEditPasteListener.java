package dev.lone.itemsadder.Core.OtherPlugins;

import com.fastasyncworldedit.core.event.extent.ActorSaveClipboardEvent;
import com.fastasyncworldedit.core.event.extent.PasteEvent;
import com.fastasyncworldedit.core.extent.clipboard.URIClipboardHolder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.eventbus.Subscribe;

class WorldEditPasteListener {
   WorldEditPasteListener(WorldEditIntegration var1) {
      this.val$integration = var1;
   }

   @Subscribe
   public void onPaste(PasteEvent event) {
      if (event.getExtent() instanceof EditSession var2) {
         ClipboardHolder var5 = WorldEdit.getInstance().getSessionManager().get(event.getActor()).getClipboard();
         this.val$integration
            .onFawePaste(
               var2,
               event.getActor(),
               event.getClipboard(),
               event.getPosition(),
               var5 instanceof URIClipboardHolder var4 ? var4.getURI(event.getClipboard()) : null,
               event::isCancelled
            );
      }
   }

   @Subscribe
   public void onSchematicSave(ActorSaveClipboardEvent event) {
      if (!event.isCancelled()) {
         this.val$integration.onFaweSchematicSave(event.getActor(), event.getClipboard(), event.getSourceURI(), event.getDestinationURI());
      }
   }
}
