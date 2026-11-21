package dev.lone.itemsadder.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.WeakHashMap;

public class TypedList extends ArrayList {
   public TypedList(Collection var1) {
      super(var1);
   }

   public static TypedList ofType(Class var0) {
      return new TypedList(Collections.newSetFromMap(new WeakHashMap()));
   }

   public static TypedList filterType(Class var0) {
      return new TypedList(Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap())));
   }
}
