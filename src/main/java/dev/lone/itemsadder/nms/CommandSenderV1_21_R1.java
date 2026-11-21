package dev.lone.itemsadder.nms;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.minecraft.commands.CommandListenerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_21_R1.CraftServer;
import org.bukkit.craftbukkit.v1_21_R1.command.ProxiedNativeCommandSender;

public class CommandSenderV1_21_R1 extends ProxiedNativeCommandSender implements NmsCommandSender {
   private static final Cache aUU = CacheBuilder.newBuilder().expireAfterWrite(1L, TimeUnit.MINUTES).build();

   public CommandSenderV1_21_R1() {
      this(((CraftServer)Bukkit.getConsoleSender().getServer()).getServer().aI(), Bukkit.getConsoleSender(), Bukkit.getConsoleSender());
   }

   public CommandSenderV1_21_R1(CommandListenerWrapper var1, CommandSender var2, CommandSender var3) {
      super(var1.a(new CommandListenerV1_21_R1(var1)), var2, var3);
   }

   @Override
   public CommandCaptureResult method3171(String command, boolean silent) {
      CommandCaptureResult var3 = new CommandCaptureResult();
      aUU.put(this.getHandle().c, var3);
      var3.iV = silent;
      Bukkit.dispatchCommand(this, command);
      return (CommandCaptureResult)aUU.getIfPresent(this.getHandle().c);
   }

   public void sendMessage(String message) {
      CommandCaptureResult var2 = (CommandCaptureResult)aUU.getIfPresent(this.getHandle().c);
      assert var2 != null;
      var2.aUP = message;
      if (!var2.iV) {
         super.sendMessage(message);
      }
   }

   public void sendMessage(String... messages) {
      CommandCaptureResult var2 = (CommandCaptureResult)aUU.getIfPresent(this.getHandle().c);
      assert var2 != null;
      var2.aUP = String.join(" ", messages);
      if (!var2.iV) {
         super.sendMessage(messages);
      }
   }

   public void sendMessage(UUID sender, String message) {
      CommandCaptureResult var3 = (CommandCaptureResult)aUU.getIfPresent(this.getHandle().c);
      assert var3 != null;
      var3.aUP = message;
      if (!var3.iV) {
         super.sendMessage(sender, message);
      }
   }

   public void sendMessage(UUID sender, String... messages) {
      CommandCaptureResult var3 = (CommandCaptureResult)aUU.getIfPresent(this.getHandle().c);
      assert var3 != null;
      var3.aUP = String.join(" ", messages);
      if (!var3.iV) {
         super.sendMessage(sender, messages);
      }
   }
}
