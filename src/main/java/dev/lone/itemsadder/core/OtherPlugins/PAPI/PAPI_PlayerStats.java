package dev.lone.itemsadder.Core.OtherPlugins.PAPI;

import dev.lone.itemsadder.core.ContentManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PAPI_PlayerStats extends PlaceholderExpansion {
   public boolean canRegister() {
      return true;
   }

   public String getAuthor() {
      return "LoneDev";
   }

   public String getIdentifier() {
      return "iaplayerstat";
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
      return Float.toString(ContentManager.method8084().aG.method1429(p, identifier, 0.0F));
   }
}
