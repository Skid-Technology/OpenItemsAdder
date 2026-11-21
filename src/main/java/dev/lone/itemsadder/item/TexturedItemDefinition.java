package dev.lone.itemsadder.item;

import java.util.Collections;
import java.util.List;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

public class TexturedItemDefinition extends TexturedItemBase {
   @Nullable
   private final String apd;

   public TexturedItemDefinition(Material var1, String var2, String var3, @Nullable CustomItemDefinition var4, @Nullable String var5) {
      super(var1, var2, var3, var4);
      this.apd = var5;
   }

   @Nullable
   @Override
   public String DO() {
      return "item/generated";
   }

   @Override
   public List getTextures() {
      return Collections.singletonList(this.apd);
   }

   @Override
   public boolean nB() {
      return true;
   }
}
