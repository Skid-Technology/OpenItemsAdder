package dev.lone.itemsadder.command;

import dev.lone.itemsadder.nms.CommandCaptureResult;
import dev.lone.itemsadder.nms.CommandSenderGenericDup2;
import dev.lone.itemsadder.util.MarkerAnnotation;
import java.lang.StackWalker.Option;
import java.lang.StackWalker.StackFrame;
import java.lang.invoke.MethodType;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandSourceProxyGenericDup2 implements CommandSource {
   CommandSourceProxyGenericDup2(CommandSourceStack var1) {
      this.aVA = var1;
   }

   @MarkerAnnotation
   public void sendSystemMessage(@NotNull Component component) {
      CommandCaptureResult var2 = (CommandCaptureResult)CommandSenderGenericDup2.aVy.getIfPresent(this);
      if (!var2.iV) {
         this.aVA.source.sendSystemMessage(component);
      }

      var2.aUP = component.toString();
      StackWalker var3 = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE);

      for (StackFrame var5 : (List)var3.walk(Stream::toList)) {
         if (var5.getMethodName().equals("a") || var5.getMethodName().equals("sendSuccess")) {
            MethodType var6 = var5.getMethodType();
            if (var6.returnType().equals(void.class) && var6.parameterCount() == 2) {
               List var7 = var6.parameterList();
               if (((Class)var7.get(0)).equals(Supplier.class) && ((Class)var7.get(1)).equals(boolean.class)) {
                  var2.aUQ = true;
                  return;
               }
            }
         }

         if (var5.getMethodName().equals("b") || var5.getMethodName().equals("sendFailure")) {
            MethodType var8 = var5.getMethodType();
            if (var8.returnType().equals(void.class)) {
               if (var8.parameterCount() == 1) {
                  List var9 = var8.parameterList();
                  if (((Class)var9.get(0)).equals(Component.class)) {
                     var2.aUQ = false;
                     return;
                  }
               } else if (var8.parameterCount() == 2) {
                  List var10 = var8.parameterList();
                  if (((Class)var10.get(0)).equals(Component.class) && ((Class)var10.get(1)).equals(boolean.class)) {
                     var2.aUQ = false;
                     return;
                  }
               }
            }
         }
      }

      var2.aUQ = null;
   }

   @MarkerAnnotation
   public boolean acceptsSuccess() {
      return this.aVA.source.acceptsSuccess();
   }

   @MarkerAnnotation
   public boolean acceptsFailure() {
      return this.aVA.source.acceptsFailure();
   }

   @MarkerAnnotation
   public boolean shouldInformAdmins() {
      return this.aVA.source.shouldInformAdmins();
   }

   @MarkerAnnotation
   public CommandSender getBukkitSender(@NotNull CommandSourceStack commandSourceStack) {
      return this.aVA.source.getBukkitSender(commandSourceStack);
   }
}
