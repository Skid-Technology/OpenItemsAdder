package dev.lone.itemsadder.util;

import it.unimi.dsi.fastutil.objects.ObjectArrays;
import java.util.ArrayList;
import java.util.Objects;

@Deprecated
public class ObjectList extends ArrayList {
   @Override
   public int hashCode() {
      Object[] var1 = this.toArray(new Object[0]);
      ObjectArrays.quickSort(var1);
      int var2 = 1;

      for (Object var6 : var1) {
         var2 = 31 * var2 + (var6 == null ? 0 : var6.hashCode());
      }

      return Objects.hash(var2, this.size());
   }
}
