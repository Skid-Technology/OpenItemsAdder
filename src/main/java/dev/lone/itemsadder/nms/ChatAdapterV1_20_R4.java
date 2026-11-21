package dev.lone.itemsadder.nms;

import dev.lone.itemsadder.text.NmsChatAdapter;
import dev.lone.itemsadder.util.EnvironmentDetector;
import io.papermc.paper.adventure.PaperAdventure;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.network.chat.ChatModifier;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.chat.IChatMutableComponent;
import net.minecraft.network.chat.contents.LiteralContents.a;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R4.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class ChatAdapterV1_20_R4 implements NmsChatAdapter {
   private static final IChatBaseComponent aPo = CraftChatMessage.fromStringOrNull(" ");

   @Override
   public void method2954(Player player, String message) {
      EntityPlayer var3 = ((CraftPlayer)player).getHandle();
      var3.c.b(new ClientboundSetActionBarTextPacket((IChatBaseComponent)this.eQ(message)));
   }

   @Override
   public void method2955(Player player, Object component) {
      EntityPlayer var3 = ((CraftPlayer)player).getHandle();
      var3.c.b(new ClientboundSetActionBarTextPacket((IChatBaseComponent)component));
   }

   @Override
   public void cl(Player player) {
      EntityPlayer var2 = ((CraftPlayer)player).getHandle();
      var2.c.b(new ClientboundSetActionBarTextPacket(aPo));
   }

   @Override
   public Object eQ(String str) {
      return IChatMutableComponent.a(new a(str));
   }

   @Override
   public Object method2956(String str, int rgbColor) {
      return IChatMutableComponent.a(new a(str)).c(ChatModifier.a.a(rgbColor));
   }

   @Override
   public boolean method2957(Object object, String str) {
      return object instanceof IChatBaseComponent var3 ? var3.getString().contains(str) : false;
   }

   @Override
   public String method2958(Object signedMessageBody_Packed) {
      net.minecraft.network.chat.SignedMessageBody.a var2 = (net.minecraft.network.chat.SignedMessageBody.a)signedMessageBody_Packed;
      return var2.a();
   }

   @Nullable
   @Override
   public Component method2959(Object componentObj) {
      if (componentObj instanceof IChatBaseComponent var2) {
         return EnvironmentDetector.paperAdventure
            ? PaperAdventure.asAdventure(var2)
            : GsonComponentSerializer.gson().deserialize(CraftChatMessage.toJSON(var2));
      } else {
         return null;
      }
   }

   @Nullable
   @Override
   public Object method2960(Object componentAdvObj) {
      if (componentAdvObj instanceof Component var2) {
         return EnvironmentDetector.paperAdventure
            ? PaperAdventure.asVanilla(var2)
            : CraftChatMessage.fromJSON((String)GsonComponentSerializer.gson().serialize(var2));
      } else {
         return null;
      }
   }

   @Override
   public String method2964(Object nmsComponent) {
      if (nmsComponent instanceof IChatBaseComponent var2) {
         StringBuilder var3 = new StringBuilder();
         var2.a(var1x -> {
            var3.append(var1x);
            return Optional.empty();
         });
         return var3.toString();
      } else {
         return nmsComponent.toString();
      }
   }
}
