package dev.lone.itemsadder.command;

import dev.lone.itemsadder.nms.CommandCaptureResult;
import dev.lone.itemsadder.nms.CommandSenderV1_21_R5;
import dev.lone.itemsadder.util.MarkerAnnotation;
import java.lang.StackWalker.Option;
import java.lang.StackWalker.StackFrame;
import java.lang.invoke.MethodType;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.commands.ICommandListener;
import net.minecraft.network.chat.IChatBaseComponent;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

class CommandListenerProxyV1_21_R5 implements ICommandListener {
   CommandListenerProxyV1_21_R5(CommandListenerWrapper var1) {
      this.aVo = var1;
   }

   @MarkerAnnotation
   public void method3184(@NotNull IChatBaseComponent component) {
      CommandCaptureResult var2 = (CommandCaptureResult)CommandSenderV1_21_R5.aVm.getIfPresent(this);
      if (!var2.iV) {
         this.aVo.d.a(component);
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
                  if (((Class)var9.get(0)).equals(IChatBaseComponent.class)) {
                     var2.aUQ = false;
                     return;
                  }
               } else if (var8.parameterCount() == 2) {
                  List var10 = var8.parameterList();
                  if (((Class)var10.get(0)).equals(IChatBaseComponent.class) && ((Class)var10.get(1)).equals(boolean.class)) {
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
   public boolean x_() {
      return this.aVo.d.x_();
   }

   @MarkerAnnotation
   public boolean y_() {
      return this.aVo.d.y_();
   }

   @MarkerAnnotation
   public boolean method3185() {
      return this.aVo.d.c();
   }

   @MarkerAnnotation
   public CommandSender getBukkitSender(@NotNull CommandListenerWrapper commandSourceStack) {
      return this.aVo.d.getBukkitSender(commandSourceStack);
   }
}
