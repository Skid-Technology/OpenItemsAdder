package dev.lone.itemsadder.item;

import java.util.Map;
import lombok.Generated;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

public final class ItemSpecWithTexture extends ItemSpec {
   private final String aqE;
   private final Map aqF;

   public ItemSpecWithTexture(
      String var1,
      Material var2,
      String var3,
      Map var4,
      @Nullable String var5,
      @Nullable Boolean var6,
      @Nullable Boolean var7,
      @Nullable Integer var8,
      @Nullable Integer var9
   ) {
      super(var1, var2, var5, var6, var7, var8, var9);
      this.aqE = var3;
      this.aqF = var4;
   }

   @Override
   public String Ej() {
      String var1 = this.apw.split(":")[0];
      String var2 = this.apw.split(":")[1];
      return "assets/" + var1 + "/models/item/ia_auto/" + var2 + ".json";
   }

   @Override
   public String cU(@Nullable String var1) {
      if (var1 != null && !var1.isEmpty() && !var1.equals("normal")) {
         String var2 = this.apw.split(":")[0];
         String var3 = this.apw.split(":")[1];
         return "assets/" + var2 + "/models/item/ia_auto/" + var3 + "_" + var1 + ".json";
      } else {
         return this.Ej();
      }
   }

   @Override
   public String Ek() {
      String var1 = this.apw.split(":")[0];
      String var2 = this.apw.split(":")[1];
      return var1 + ":item/ia_auto/" + var2;
   }

   @Override
   public String cV(String var1) {
      if (var1 != null && !var1.isEmpty() && !var1.equals("normal")) {
         String var2 = this.apw.split(":")[0];
         String var3 = this.apw.split(":")[1];
         return var2 + ":item/ia_auto/" + var3 + "_" + var1;
      } else {
         return this.Ek();
      }
   }

   @Generated
   public String Fc() {
      return this.aqE;
   }

   @Generated
   public Map Fd() {
      return this.aqF;
   }
}
