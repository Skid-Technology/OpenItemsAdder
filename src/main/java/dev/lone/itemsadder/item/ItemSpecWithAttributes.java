package dev.lone.itemsadder.item;

import java.util.HashMap;
import java.util.Map;
import lombok.Generated;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

public final class ItemSpecWithAttributes extends ItemSpec {
   private final Map aqD;

   public ItemSpecWithAttributes(
      String var1,
      Material var2,
      String var3,
      @Nullable String var4,
      @Nullable Boolean var5,
      @Nullable Boolean var6,
      @Nullable Integer var7,
      @Nullable Integer var8
   ) {
      super(var1, var2, var4, var5, var6, var7, var8);
      this.aqD = new HashMap();
      this.aqD.put("normal", var3);
   }

   public ItemSpecWithAttributes(
      String var1,
      Material var2,
      Map var3,
      @Nullable String var4,
      @Nullable Boolean var5,
      @Nullable Boolean var6,
      @Nullable Integer var7,
      @Nullable Integer var8
   ) {
      super(var1, var2, var4, var5, var6, var7, var8);
      this.aqD = var3;
   }

   @Override
   public String Ej() {
      return this.cU(null);
   }

   @Override
   public String cU(@Nullable String var1) {
      if (var1 == null) {
         var1 = "normal";
      }

      String var2 = (String)this.aqD.get(var1);
      if (var2 == null) {
         throw new NullPointerException("Can't find status '" + var1 + "' model for " + this.apw);
      }

      String var3 = var2.split(":")[0];
      String var4 = var2.split(":")[1];
      return "assets/" + var3 + "/models/" + var4 + ".json";
   }

   @Override
   public String Ek() {
      String var1 = (String)this.aqD.get("normal");
      if (var1 == null) {
         throw new NullPointerException("Model location 'normal' is null for " + this.apw);
      } else {
         return var1;
      }
   }

   @Override
   public String cV(String var1) {
      return (String)this.aqD.get(var1);
   }

   @Generated
   public Map Fb() {
      return this.aqD;
   }
}
