package dev.lone.itemsadder.nms;

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

class CommandListenerV1_20_R4 implements ICommandListener {
   CommandListenerV1_20_R4(CommandListenerWrapper var1) {
      this.aUT = var1;
   }

   @MarkerAnnotation
   public void method3172(@NotNull IChatBaseComponent component) {
      CommandCaptureResult var2 = (CommandCaptureResult)CommandSenderV1_20_R4.aUR.getIfPresent(this);
      if (!var2.iV) {
         this.aUT.c.a(component);
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
   public boolean l_() {
      return this.aUT.c.l_();
   }

   @MarkerAnnotation
   public boolean w_() {
      return this.aUT.c.w_();
   }

   @MarkerAnnotation
   public boolean U_() {
      return this.aUT.c.U_();
   }

   @MarkerAnnotation
   @NotNull
   public CommandSender getBukkitSender(CommandListenerWrapper commandSourceStack) {
      return this.aUT.c.getBukkitSender(commandSourceStack);
   }
}
