package dev.lone.itemsadder.item.conversion;

public interface ItemConversion {
   String GP();

   String GK();

   boolean needsConversion();

   void convert();

   String GN();
}
