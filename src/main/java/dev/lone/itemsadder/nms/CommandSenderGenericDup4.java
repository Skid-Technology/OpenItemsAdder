package dev.lone.itemsadder.nms;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.lone.itemsadder.command.CommandSourceProxyGenericDup4;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.v1_21_R7.command.ProxiedNativeCommandSender;

public class CommandSenderGenericDup4 extends ProxiedNativeCommandSender implements NmsCommandSender {
   private static final Cache aVE = CacheBuilder.newBuilder().expireAfterWrite(1L, TimeUnit.MINUTES).build();

   public CommandSenderGenericDup4() {
      this(((CraftServer)Bukkit.getConsoleSender().getServer()).getServer().createCommandSourceStack(), Bukkit.getConsoleSender(), Bukkit.getConsoleSender());
   }

   public CommandSenderGenericDup4(CommandSourceStack var1, CommandSender var2, CommandSender var3) {
      super(var1.withSource(new CommandSourceProxyGenericDup4(var1)), var2, var3);
   }

   @Override
   public CommandCaptureResult method3171(String command, boolean silent) {
      CommandCaptureResult var3 = new CommandCaptureResult();
      aVE.put(this.getHandle().source, var3);
      var3.iV = silent;
      Bukkit.dispatchCommand(this, command);
      return (CommandCaptureResult)aVE.getIfPresent(this.getHandle().source);
   }

   public void sendMessage(String message) {
      CommandCaptureResult var2 = (CommandCaptureResult)aVE.getIfPresent(this.getHandle().source);
      assert var2 != null;
      var2.aUP = message;
      if (!var2.iV) {
         super.sendMessage(message);
      }
   }

   public void sendMessage(String... messages) {
      CommandCaptureResult var2 = (CommandCaptureResult)aVE.getIfPresent(this.getHandle().source);
      assert var2 != null;
      var2.aUP = String.join(" ", messages);
      if (!var2.iV) {
         super.sendMessage(messages);
      }
   }

   public void sendMessage(UUID sender, String message) {
      CommandCaptureResult var3 = (CommandCaptureResult)aVE.getIfPresent(this.getHandle().source);
      assert var3 != null;
      var3.aUP = message;
      if (!var3.iV) {
         super.sendMessage(sender, message);
      }
   }

   public void sendMessage(UUID sender, String... messages) {
      CommandCaptureResult var3 = (CommandCaptureResult)aVE.getIfPresent(this.getHandle().source);
      assert var3 != null;
      var3.aUP = String.join(" ", messages);
      if (!var3.iV) {
         super.sendMessage(sender, messages);
      }
   }
}
