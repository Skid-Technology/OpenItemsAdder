package dev.lone.itemsadder.gui;

import dev.lone.itemsadder.fontimage.FontImageLoader;
import dev.lone.itemsadder.fontimage.FontImageSettingsRecord;
import dev.lone.itemsadder.util.AdventureText;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public class GuiEntryRecord {
   private final String asB;
   private final String asC;
   @Nullable
   private final String asD;
   @Nullable
   private final Boolean asE;
   @Nullable
   private final Boolean asF;
   @Nullable
   private final Component asG;
   @Nullable
   private final Integer asH;
   private final List asI;

   public GuiEntryRecord(
      String var1,
      String var2,
      @Nullable String var3,
      @Nullable Boolean var4,
      @Nullable Boolean var5,
      @Nullable Component var6,
      @Nullable Integer var7,
      List var8
   ) {
      var8 = var8 == null ? List.of() : List.copyOf(var8);
      this.asB = var1;
      this.asC = var2;
      this.asD = var3;
      this.asE = var4;
      this.asF = var5;
      this.asG = var6;
      this.asH = var7;
      this.asI = var8;
   }

   public boolean Gm() {
      return Boolean.TRUE.equals(this.asF) && !this.asI.isEmpty() && FontImageLoader.method1931(((FontImageSettingsRecord)this.asI.getFirst()).GD());
   }

   public String cV() {
      return this.asB + ":" + this.asC;
   }

   public Component Gn() {
      return this.asG == null ? AdventureText.fromLegacy("Music Disc") : this.asG;
   }

   public String Go() {
      return this.asB;
   }

   public String nk() {
      return this.asC;
   }

   @Nullable
   public String Gp() {
      return this.asD;
   }

   @Nullable
   public Boolean Gq() {
      return this.asE;
   }

   @Nullable
   public Boolean Gr() {
      return this.asF;
   }

   @Nullable
   public Integer Gs() {
      return this.asH;
   }

   public List Gt() {
      return this.asI;
   }
}
