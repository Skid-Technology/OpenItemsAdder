package dev.lone.itemsadder.Core.OtherPlugins.PAPI;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.lang.MessageFormatter;
import dev.lone.itemsadder.resourcepack.CustomTexture;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PAPI_FontImages extends PlaceholderExpansion {
   public boolean canRegister() {
      return true;
   }

   public String getAuthor() {
      return "LoneDev";
   }

   public String getIdentifier() {
      return "img";
   }

   public String getRequiredPlugin() {
      return null;
   }

   public String getVersion() {
      return "1.0.1";
   }

   public boolean persist() {
      return true;
   }

   public String onPlaceholderRequest(Player p, String identifier) {
      ContentManager var3 = ContentManager.method8084();
      MessageFormatter var4 = var3 == null ? null : var3.method8109();
      if (var4 == null) {
         return identifier + "_NOT_FOUND";
      }

      if (identifier.startsWith("offset_")) {
         return var4.bE(identifier);
      }

      CustomTexture var5 = var4.bB(identifier);
      return var5 == null ? identifier + "_NOT_FOUND" : var5.getDisplayText(true);
   }
}
