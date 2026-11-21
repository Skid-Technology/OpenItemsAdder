package dev.lone.itemsadder.entity;

public class MobDisguiseSpec {
   private final String aHm;
   private final String aHn;
   private final String aHo;
   private final boolean aHp;

   MobDisguiseSpec(String var1, String var2, String var3, boolean var4) {
      this.aHm = var1;
      this.aHn = var2;
      this.aHo = var3;
      this.aHp = var4;
   }

   public String type() {
      return this.aHm;
   }

   public String skin() {
      return this.aHn;
   }

   public String name() {
      return this.aHo;
   }

   public boolean Lp() {
      return this.aHp;
   }
}
