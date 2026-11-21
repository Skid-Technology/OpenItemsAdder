package dev.lone.itemsadder.util;

import dev.lone.itemsadder.config.Settings;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

public class NameRegistry {
   public Map itemAliases = new HashMap();
   public Map blockAliases = new HashMap();
   private final Map itemReverse = new HashMap();
   private final Map blockReverse = new HashMap();
   public Map generatedIds = new HashMap();
   int nextId = 0;

   public boolean containsName(String var1, boolean var2) {
      return var2 ? this.blockAliases.containsKey(var1) : this.itemAliases.containsKey(var1);
   }

   public String obfuscateName(String var1, boolean var2) {
      return this.generateObfuscatedName(var1, Settings.get().obfuscateShortTextureRefs, var2);
   }

   public String generateObfuscatedName(String var1, boolean var2, boolean var3) {
      String var4;
      if (var2) {
         String var5 = (var3 ? "b_" : "i_") + var1;
         Integer var6 = (Integer)this.generatedIds.get(var5);
         if (var6 == null) {
            this.generatedIds.put(var5, this.nextId++);
            var6 = (Integer)this.generatedIds.get(var5);
         }

         var4 = "ia:" + var6;
      } else if (var1.contains(":")) {
         var4 = "_" + (var3 ? "b_" : "") + var1;
      } else {
         var4 = "_alias:" + (var3 ? "b_" : "") + var1;
      }

      this.putMapping(var1, var4, var3);
      return var4;
   }

   @Nullable
   public String getAlias(String var1, boolean var2) {
      return var2 ? (String)this.blockAliases.get(var1) : (String)this.itemAliases.get(var1);
   }

   @Nullable
   public String getOrGenerate(String var1, boolean var2) {
      String var3 = this.getAlias(var1, var2);
      return var3 != null ? var3 : this.obfuscateName(var1, var2);
   }

   void restoreMapping(String var1, String var2, boolean var3) {
      this.putMapping(var1, var2, var3);
      if (var2.startsWith("ia:")) {
         try {
            int var4 = Integer.parseInt(var2.substring("ia:".length()));
            this.generatedIds.put((var3 ? "b_" : "i_") + var1, var4);
            this.nextId = Math.max(this.nextId, var4 + 1);
         } catch (NumberFormatException var5) {
         }
      }
   }

   public void clear() {
      this.itemAliases.clear();
      this.blockAliases.clear();
      this.itemReverse.clear();
      this.blockReverse.clear();
      this.generatedIds.clear();
      this.nextId = 0;
   }

   @Nullable
   String getOriginal(String var1, boolean var2) {
      return (String)(var2 ? this.blockReverse : this.itemReverse).get(var1);
   }

   private void putMapping(String var1, String var2, boolean var3) {
      Map var4 = var3 ? this.blockAliases : this.itemAliases;
      Map var5 = var3 ? this.blockReverse : this.itemReverse;
      String var6 = var4.put(var1, var2);
      if (var6 != null) {
         var5.remove(var6, var1);
      }

      var5.putIfAbsent(var2, var1);
   }
}
