package dev.lone.itemsadder.item;

import dev.lone.itemsadder.config.YamlConfig;
import org.bukkit.Material;

public class SimpleItemDefinition extends CustomItemDefinition {
   public final boolean ano;
   public final boolean anp;
   public final double anq;
   public final double anr;

   public SimpleItemDefinition(YamlConfig var1, String var2, String var3, Material var4, boolean var5, boolean var6, double var7, double var9) {
      super(var1, var2, var3, var4);
      this.ano = var5;
      this.anp = var6;
      this.anq = var7;
      this.anr = var9;
   }
}
