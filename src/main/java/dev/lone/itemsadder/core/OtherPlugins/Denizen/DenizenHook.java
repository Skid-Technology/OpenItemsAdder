package dev.lone.itemsadder.Core.OtherPlugins.Denizen;

import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.scripts.commands.BukkitCommandRegistry;
import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import java.util.Map.Entry;

public class DenizenHook {
   public static void hook() {
      try {
         ItemTag.tagProcessor
            .registerTag(
               ElementTag.class,
               "is_ia_item",
               (attribute, itemTag) -> new ElementTag(ContentManager.method8084().isCustomItem(itemTag.getItemStack())),
               new String[0]
            );
         ItemTag.tagProcessor
            .registerTag(
               ElementTag.class,
               "is_ia_block",
               (attribute, itemTag) -> new ElementTag(ContentManager.method8084().method8156(itemTag.getItemStack())),
               new String[0]
            );
         ItemTag.tagProcessor.registerTag(ElementTag.class, "ia_namespaced_id", (attribute, itemTag) -> {
            CustomItemDefinition var2 = ContentManager.method8084().method8149(itemTag.getItemStack());
            if (var2 == null) {
               attribute.echoError("Not a custom ItemAdder item.");
               return null;
            } else {
               return new ElementTag(var2.getNamespacedID());
            }
         }, new String[0]);
         LocationTag.tagProcessor
            .registerTag(
               ElementTag.class, "is_ia_block", (attribute, itemTag) -> new ElementTag(ContentManager.isCustomBlock(itemTag.getBlock())), new String[0]
            );
         LocationTag.tagProcessor.registerTag(ElementTag.class, "ia_namespaced_id", (attribute, itemTag) -> {
            CustomBlockChunkStoreBase var2 = ContentManager.method8084().method8183(itemTag.getBlock());
            if (!var2.isCustomBlock(itemTag.getBlock())) {
               attribute.echoError("Not a custom ItemAdder block.");
               return null;
            } else {
               return new ElementTag(var2.getCustomBlockId(itemTag.getBlock()));
            }
         }, new String[0]);
         ItemTag.tagProcessor.registerMechanism("ia_namespaced_id", true, ElementTag.class, (object, mechanism, element) -> {
            CustomItemDefinition var3 = ContentManager.method8084().method8154(element.asString());
            if (var3 == null) {
               mechanism.echoError("Not a custom ItemAdder item: " + element.asString());
            } else {
               object.setItemStack(var3.Ds());
            }
         }, new String[0]);
         BukkitCommandRegistry.registerCommand(SetCustomBlock.class);
      } catch (Throwable var1) {
         Log.error("Error while hooking into Denizen", var1);
      }
   }

   public static void unhook() {
      try {
         ItemTag.tagProcessor.registeredObjectTags.remove("is_ia_item");
         ItemTag.tagProcessor.registeredObjectTags.remove("is_ia_block");
         ItemTag.tagProcessor.registeredObjectTags.remove("ia_namespaced_id");
         LocationTag.tagProcessor.registeredObjectTags.remove("is_ia_block");
         LocationTag.tagProcessor.registeredObjectTags.remove("ia_namespaced_id");
         ItemTag.tagProcessor.registeredMechanisms.remove("ia_namespaced_id");
         LocationTag.tagProcessor.registeredMechanisms.remove("ia_namespaced_id");
         String var0 = null;

         for (Entry var2 : DenizenCore.commandRegistry.instances.entrySet()) {
            if (((AbstractCommand)var2.getValue()).getClass().equals(SetCustomBlock.class)) {
               var0 = (String)var2.getKey();
               break;
            }
         }

         if (var0 != null) {
            DenizenCore.commandRegistry.instances.remove(var0);
            DenizenCore.commandRegistry.classes.remove(SetCustomBlock.class);
         }
      } catch (Throwable var3) {
         Log.error("Error while unhooking from Denizen", var3);
      }
   }
}
