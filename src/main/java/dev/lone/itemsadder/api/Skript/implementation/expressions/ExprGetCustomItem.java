package dev.lone.itemsadder.api.Skript.implementation.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import javax.annotation.Nullable;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Name("ItemsAdderItem")
@Description("Get an ItemsAdder item by NamespacedID")
@Examples({"give player a customitem itemsadder:ruby", "set slot 1 of inventory of player to customitem ruby"})
public class ExprGetCustomItem extends PropertyExpression {
   private Expression itemName;

   public boolean init(Expression[] exprs, int i, @NotNull Kleenean kleenean, @NotNull ParseResult parseResult) {
      this.setExpr(exprs[0]);
      this.itemName = exprs[0];
      return true;
   }

   @NotNull
   protected ItemType[] get(@NotNull Event e, @NotNull String[] source) {
      String var3 = (String)this.itemName.getSingle(e);
      CustomItemDefinition var4 = ContentManager.method8084().method8154(var3);
      if (var4 == null) {
         return null;
      }

      ItemStack var6 = var4.clone();
      ItemType var5 = new ItemType(var6);
      return new ItemType[]{var5};
   }

   @NotNull
   public Class getReturnType() {
      return ItemType.class;
   }

   @NotNull
   public String toString(@Nullable Event e, boolean d) {
      return "customitem" + (String)this.itemName.getSingle(e);
   }

   static {
      Skript.registerExpression(ExprGetCustomItem.class, ItemType.class, ExpressionType.SIMPLE, new String[]{"customitem %string%"});
   }
}
