package dev.lone.itemsadder.gui;

import dev.lone.itemsadder.fontimage.FontImageSettingsBuilder;
import dev.lone.itemsadder.util.AdventureText;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public class GuiEntryBuilder {
   public final String asJ;
   public final String asK;
   @Nullable
   private String asD;
   @Nullable
   private Boolean asE;
   @Nullable
   private Boolean asF;
   @Nullable
   private Component asG;
   @Nullable
   private Integer asH;
   List asI;

   public GuiEntryBuilder(String var1, String var2) {
      this.asJ = var1;
      this.asK = var2;
   }

   public GuiEntryBuilder dq(@Nullable String var1) {
      this.asD = var1;
      return this;
   }

   public GuiEntryBuilder method1942(@Nullable Boolean var1) {
      this.asE = var1;
      return this;
   }

   public GuiEntryBuilder method1943(List var1) {
      this.asI = var1;
      return this;
   }

   public void method1944(FontImageSettingsBuilder var1) {
      if (this.asI == null) {
         this.asI = new ArrayList();
      }

      this.asI.add(var1);
   }

   public GuiEntryBuilder method1945(Boolean var1) {
      this.asF = var1;
      return this;
   }

   @Nullable
   public Boolean Gu() {
      return this.asF;
   }

   public GuiEntryBuilder dr(String var1) {
      if (var1 == null) {
         this.asG = null;
      } else {
         this.asG = AdventureText.parseRichText(var1);
      }

      return this;
   }

   public GuiEntryBuilder method1946(Integer var1) {
      this.asH = var1;
      return this;
   }

   public GuiEntryRecord Gv() {
      ArrayList var1 = new ArrayList();
      if (this.asI != null) {
         for (FontImageSettingsBuilder var3 : this.asI) {
            var1.add(var3.method1956());
         }
      }

      return new GuiEntryRecord(this.asJ, this.asK, this.asD, this.asE, this.asF, this.asG, this.asH, var1);
   }
}
