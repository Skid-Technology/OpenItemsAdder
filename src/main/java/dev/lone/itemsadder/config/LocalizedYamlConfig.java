package dev.lone.itemsadder.config;

import dev.lone.itemsadder.util.AdventureText;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class LocalizedYamlConfig extends YamlConfig {
   YamlConfig bMz;

   public LocalizedYamlConfig(Plugin var1, String var2, boolean var3) {
      super(var1, true, "lang/" + var2 + ".yml", var3, true);
      this.li(var2);
   }

   private void li(String var1) {
      if (!var1.equals("en")) {
         this.bMz = new YamlConfig(this.bLL, true, "lang/en.yml", true, true, false);
         this.bMz.abO().forEach(var1x -> {
            if (!this.contains(var1x)) {
               this.method7048(var1x, this.bMz.getString(var1x));
            }
         });
         this.save();
      }
   }

   public String lj(String var1) {
      return this.bg(var1, (String)null);
   }

   public String bg(String var1, String var2) {
      if (this.contains(var1)) {
         return this.jX(var1);
      }

      if (this.bMz != null && this.bMz.contains(var1)) {
         return this.bMz.jX(var1);
      }

      this.jH(var1);
      return var2;
   }

   public String method7069(String var1, String var2, boolean var3) {
      if (this.contains(var1)) {
         return this.jX(var1);
      }

      if (this.bMz != null && this.bMz.contains(var1)) {
         return this.bMz.jX(var1);
      }

      if (var3) {
         this.jH(var1);
      }

      return var2;
   }

   @Nullable
   String lk(String var1) {
      if (this.contains(var1)) {
         return this.getString(var1);
      }

      if (this.bMz != null && this.bMz.contains(var1)) {
         return this.bMz.getString(var1);
      }

      this.jH(var1);
      return null;
   }

   public Component method7070(String var1) {
      String var2 = this.lk(var1);
      return AdventureText.parseRichText(var2 != null ? var2 : var1);
   }

   public String method7071(String var1, Player var2) {
      return this.bh(var1, var2.getName());
   }

   public String bh(String var1, String var2) {
      String var3 = this.lk(var1);
      return var3 == null ? null : AdventureText.plainTextOf(var3.replace("{player}", var2));
   }
}
