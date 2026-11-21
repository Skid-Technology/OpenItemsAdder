package dev.lone.itemsadder.util;

import java.util.HashMap;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public class LocalizedMessages {
   final HashMap defaults = new HashMap();
   final HashMap overrides = new HashMap();

   public void putOverride(String var1, String var2) {
      this.overrides.put(var1, ComponentText.of(AdventureText.parseRichText(var2)));
   }

   public void putDefault(String var1, String var2) {
      this.defaults.put(var1, ComponentText.of(AdventureText.parseRichText(var2)));
   }

   public Component getComponent(String var1) {
      if (this.hasKey(var1)) {
         ComponentText var4 = this.findEntry(var1);
         return var4 == null ? AdventureText.parseRichText(var1) : var4.component;
      }

      if (var1.startsWith("{")) {
         var1 = var1.replace("\\\"", "\"");
         if (var1.startsWith("{\"") || var1.startsWith("{'")) {
            try {
               Component var2 = AdventureText.parseJson(var1);
               if (var2 != null) {
                  return var2;
               }
            } catch (Exception var3) {
               Log.warn("Failed to parse string as JSON component: " + var1);
            }
         }
      }

      return AdventureText.parseRichText(var1);
   }

   public String getPlainText(String var1) {
      if (this.hasKey(var1)) {
         ComponentText var2 = this.findEntry(var1);
         return var2 == null ? AdventureText.serializePlain(AdventureText.parseRichText(var1)) : var2.plain;
      } else {
         return AdventureText.serializePlain(AdventureText.parseRichText(var1));
      }
   }

   public String getText(String var1) {
      return this.getText(var1, false);
   }

   public String getText(String var1, boolean var2) {
      String var3;
      if (this.hasKey(var1)) {
         ComponentText var4 = this.findEntry(var1);
         var3 = var4 == null ? Utils.componentToLegacy(AdventureText.parseRichText(var1)) : var4.getPlain();
      } else {
         var3 = Utils.componentToLegacy(AdventureText.parseRichText(var1));
      }

      if (!var2 && var3.startsWith("§f")) {
         var3 = var3.substring(2);
      }

      return var3;
   }

   @Nullable
   ComponentText findEntry(String var1) {
      ComponentText var2 = (ComponentText)this.overrides.get(var1);
      if (var2 == null) {
         var2 = (ComponentText)this.defaults.get(var1);
      }

      return var2;
   }

   public boolean hasKey(String var1) {
      return this.overrides.containsKey(var1) || this.defaults.containsKey(var1);
   }
}
