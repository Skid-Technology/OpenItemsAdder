package dev.lone.itemsadder.item;

import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.NamespacedId;
import org.bukkit.Color;
import org.jetbrains.annotations.Nullable;

public class LeatherColorDefinition extends NamespacedId {
   public final YamlConfig apD;
   public Color apE;
   @Nullable
   public String apF;
   @Nullable
   public String apG;
   @Nullable
   public String apH;
   @Nullable
   public String apI;
   public int apJ;
   public boolean apK;
   public int apL;
   public boolean apM;

   public LeatherColorDefinition(
      YamlConfig var1,
      String var2,
      String var3,
      Color var4,
      @Nullable String var5,
      @Nullable String var6,
      @Nullable String var7,
      @Nullable String var8,
      boolean var9,
      int var10,
      boolean var11,
      int var12
   ) {
      super(var2, var3);
      this.apD = var1;
      this.apE = var4;
      this.apF = this.cW(var5);
      this.apG = this.cW(var6);
      this.apH = this.cW(var7);
      this.apI = this.cW(var8);
      this.apK = var9;
      this.apL = var10;
      this.apM = var11;
      this.apJ = var12;
   }

   private String cW(String var1) {
      if (var1 == null) {
         return null;
      }

      var1 = var1.replace(".png", "");
      String var2;
      if (var1.contains(":")) {
         String[] var3 = var1.split(":");
         var2 = var3[0];
         var1 = var3[1];
      } else {
         var2 = this.dk;
      }

      return "assets/" + var2 + "/textures/" + var1 + ".png";
   }

   @Nullable
   public String cX(String var1) {
      return var1 == null ? null : var1.replace("assets/", "").replace("/textures", ":textures");
   }
}
