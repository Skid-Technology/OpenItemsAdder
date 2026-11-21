package dev.lone.itemsadder.world;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

public final class ReplaceableRegistry {
   private final Map replacements = new LinkedHashMap();

   public void register(Object var1, Object var2, Object var3) {
      this.replacements.compute(var1, (var2x, var3x) -> new ReplacementRecord(var3x == null ? var2 : var3x.original, var3));
   }

   public void restoreAll(Function var1, ReplaceConsumer var2) {
      Exception var3 = null;
      ArrayList var4 = new ArrayList(this.replacements.entrySet());

      try {
         for (int var5 = var4.size() - 1; var5 >= 0; var5--) {
            Entry var6 = (Entry)var4.get(var5);
            ReplacementRecord var7 = (ReplacementRecord)var6.getValue();

            try {
               if (var1.apply(var6.getKey()) == var7.installed) {
                  var2.replace(var6.getKey(), var7.original);
               }
            } catch (Exception var12) {
               if (var3 == null) {
                  var3 = var12;
               } else {
                  var3.addSuppressed(var12);
               }
            }
         }
      } finally {
         this.replacements.clear();
      }

      if (var3 != null) {
         throw var3;
      }
   }

   public void clear() {
      this.replacements.clear();
   }
}
