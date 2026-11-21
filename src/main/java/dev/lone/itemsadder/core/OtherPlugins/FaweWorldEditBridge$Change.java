package dev.lone.itemsadder.Core.OtherPlugins;

import com.sk89q.worldedit.world.block.BlockType;

class FaweWorldEditBridge$Change {
   private final int field1;
   private final int field2;
   private final int field3;
   private final String id;
   private final BlockType blockType;

   private FaweWorldEditBridge$Change(int x, int y, int z, String id, BlockType blockType) {
      this.field1 = x;
      this.field2 = y;
      this.field3 = z;
      this.id = id;
      this.blockType = blockType;
   }

   public int method1() {
      return this.field1;
   }

   public int method2() {
      return this.field2;
   }

   public int method3() {
      return this.field3;
   }
}
