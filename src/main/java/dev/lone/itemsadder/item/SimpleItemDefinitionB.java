package dev.lone.itemsadder.item;

import dev.lone.itemsadder.config.YamlConfig;
import org.bukkit.Material;

public class SimpleItemDefinitionB extends SimpleItemDefinition {
   public final boolean ans;
   public final boolean ant;

   public SimpleItemDefinitionB(
      YamlConfig var1, String var2, String var3, Material var4, boolean var5, boolean var6, double var7, double var9, boolean var11, boolean var12
   ) {
      super(var1, var2, var3, var4, var5, var6, var7, var9);
      this.ans = var11;
      this.ant = var12;
   }
}
