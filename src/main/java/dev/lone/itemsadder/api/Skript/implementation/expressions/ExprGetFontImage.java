package dev.lone.itemsadder.api.Skript.implementation.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import javax.annotation.Nullable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public class ExprGetFontImage extends SimpleExpression {
   private Expression fontImageName;

   @NotNull
   public Class getReturnType() {
      return String.class;
   }

   public boolean isSingle() {
      return true;
   }

   public boolean init(Expression[] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
      this.fontImageName = exprs[0];
      return true;
   }

   @NotNull
   public String toString(@Nullable Event e, boolean d) {
      return "fontimage " + this.fontImageName.toString(e, d);
   }

   @Nullable
   protected String[] get(@NotNull Event e) {
      String var2 = (String)this.fontImageName.getSingle(e);
      FontImageWrapper var3 = new FontImageWrapper(var2);
      return !var3.exists() ? null : new String[]{var3.getString()};
   }

   static {
      Skript.registerExpression(ExprGetFontImage.class, String.class, ExpressionType.PROPERTY, new String[]{"fontimage %string%"});
   }
}
