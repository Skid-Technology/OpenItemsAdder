package dev.lone.itemsadder.entity;

import java.awt.image.BufferedImage;

public class PlayerSkinData {
   private final String aQA;
   private final BufferedImage aQB;
   private final boolean aQC;

   public PlayerSkinData(String var1, BufferedImage var2, boolean var3) {
      this.aQA = var1;
      this.aQB = var2;
      this.aQC = var3;
   }

   public String name() {
      return this.aQA;
   }

   public BufferedImage image() {
      return this.aQB;
   }

   public boolean slim() {
      return this.aQC;
   }
}
