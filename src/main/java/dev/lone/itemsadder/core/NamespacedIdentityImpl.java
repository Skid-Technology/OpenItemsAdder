package dev.lone.itemsadder.core;

public class NamespacedIdentityImpl implements NamespacedIdentity {
   private final int apo;
   private final String app;
   private final String apq;
   private final String apr;

   public NamespacedIdentityImpl(int var1, String var2, String var3, String var4) {
      this.apo = var1;
      this.app = var2;
      this.apq = var3;
      this.apr = var4;
   }

   @Override
   public boolean ny() {
      return true;
   }

   @Override
   public int nz() {
      return this.apo;
   }

   @Override
   public boolean nB() {
      return false;
   }

   @Override
   public String method8734(boolean var1, boolean var2) {
      return this.app;
   }

   @Override
   public String nC() {
      return this.app;
   }

   @Override
   public String getNamespace() {
      return this.apq;
   }

   @Override
   public String getId() {
      return this.apr;
   }

   @Override
   public String getNamespacedID() {
      return this.apq + ":" + this.apr;
   }
}
