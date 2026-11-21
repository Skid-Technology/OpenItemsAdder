package dev.lone.itemsadder.text;

public class SignLines {
   private final String aMm;
   private final String aMn;
   private final String aMo;
   private final String aMp;

   private SignLines(String var1, String var2, String var3, String var4) {
      this.aMm = var1;
      this.aMn = var2;
      this.aMo = var3;
      this.aMp = var4;
   }

   String[] MO() {
      return new String[]{this.aMm, this.aMn, this.aMo, this.aMp};
   }

   public String MP() {
      return this.aMm;
   }

   public String MQ() {
      return this.aMn;
   }

   public String MR() {
      return this.aMo;
   }

   public String MS() {
      return this.aMp;
   }
}
