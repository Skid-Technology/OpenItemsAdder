package dev.lone.itemsadder.api.Skript.implementation.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.item.CustomItemDefinition;
import javax.annotation.Nullable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

@Name("Is a customitem")
public class ExprIsCustomItem extends Condition {
   Expression itemType;

   public boolean init(Expression[] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
      this.itemType = expressions[0];
      this.setNegated(matchedPattern < 1);
      return true;
   }

   @NotNull
   public String toString(@Nullable Event event, boolean debug) {
      return "Entity is customitem " + this.itemType.toString(event, debug);
   }

   public boolean check(@NotNull Event event) {
      ItemType var2 = (ItemType)this.itemType.getSingle(event);
      return var2 == null ? this.isNegated() : CustomItemDefinition.isCustomItem(var2.getRandom()) == this.isNegated();
   }

   static {
      Skript.registerCondition(ExprIsCustomItem.class, new String[]{"%itemtypes% (is|are) [(a|the) ](c|C)ustom[ ](i|I)tem"});
   }
}
