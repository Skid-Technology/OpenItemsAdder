package dev.lone.itemsadder.util.serial;

public final class SerialWriterBuffered extends SerialWriterDelegate {
   private final SerialIdentityMap identityMap;
   private int depth = 0;

   public SerialWriterBuffered(StreamSerialWriter var1) {
      super(var1);
      this.identityMap = new SerialIdentityMap();
   }

   public SerialWriterBuffered(StreamSerialWriter var1, int var2) {
      super(var1);
      this.identityMap = new SerialIdentityMap(var2);
   }

   @Override
   public void writeObject(int var1, Object var2, SerialType var3, boolean var4) {
      StreamSerialWriter var5 = (StreamSerialWriter)this.delegate;
      if (this.identityMap.writeIdentity(this.depth, var2, var5, var1)) {
         this.depth++;
         var5.cursor = var5.mode.method252(WireTypes.makeTag(var1, 3), var5, var5.cursor);
         var3.write(this, var2);
         var5.cursor = var5.mode.method252(WireTypes.makeTag(var1, 4), var5, var5.cursor);
      }
   }
}
