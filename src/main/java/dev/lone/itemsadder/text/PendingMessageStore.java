package dev.lone.itemsadder.text;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class PendingMessageStore {
   private static final Map aMT = Collections.synchronizedMap(new WeakHashMap());

   public static void method2753(Player var0, Component var1) {
      aMT.put(var0, var1);
   }

   @Nullable
   public static Component ce(Player var0) {
      return (Component)aMT.remove(var0);
   }

   public static void clear() {
      aMT.clear();
   }
}
