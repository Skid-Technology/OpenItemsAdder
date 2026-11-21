package dev.lone.itemsadder.util.serial;

public interface SerialCollectionReader extends SerialReader {
   void addItem(Object var1, Object var2);

   boolean isPackedEncoding();
}
