package dev.lone.itemsadder.text;

import dev.lone.itemsadder.util.AdventureText;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public interface NmsChatAdapter {
   void method2954(Player var1, String var2);

   void method2955(Player var1, Object var2);

   void cl(Player var1);

   Object eQ(String var1);

   Object method2956(String var1, int var2);

   default boolean method2957(Object object, String str) {
      return false;
   }

   String method2958(Object var1);

   Component method2959(Object var1);

   @Nullable
   Object method2960(Object var1);

   default int method2961(Object component) {
      return component.hashCode();
   }

   default Component method2962(Object component) {
      return AdventureText.fromNative(component);
   }

   default String method2963(Object component) {
      return this.method2964(component);
   }

   String method2964(Object var1);

   default void cm(Player bukkitPlayer) {
      throw new RuntimeException("Not supported on this server version.");
   }

   default void method2965(Player bukkitPlayer, Book adventureBook) {
      AdventureText.getAudiences().player(bukkitPlayer).openBook(adventureBook);
   }
}
