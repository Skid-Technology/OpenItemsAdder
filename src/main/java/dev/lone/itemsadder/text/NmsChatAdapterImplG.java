package dev.lone.itemsadder.text;

import dev.lone.itemsadder.util.AdventureText;
import dev.lone.itemsadder.util.EnvironmentDetector;
import dev.lone.itemsadder.util.ServerCompat;
import io.papermc.paper.adventure.PaperAdventure;
import java.awt.Color;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.EnumChatFormat;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.ChatModifier;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.chat.IChatMutableComponent;
import net.minecraft.network.chat.ChatClickable.OpenUrl;
import net.minecraft.network.chat.contents.LiteralContents.a;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.dialog.ActionButton;
import net.minecraft.server.dialog.CommonButtonData;
import net.minecraft.server.dialog.CommonDialogData;
import net.minecraft.server.dialog.DialogAction;
import net.minecraft.server.dialog.MultiActionDialog;
import net.minecraft.server.dialog.action.StaticAction;
import net.minecraft.server.dialog.body.ItemBody;
import net.minecraft.server.dialog.body.PlainMessage;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WrittenBookContent;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_21_R5.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R5.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R5.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class NmsChatAdapterImplG implements NmsChatAdapter {
   private static final IChatBaseComponent aPw = CraftChatMessage.fromStringOrNull(" ");

   @Override
   public void method2954(Player player, String message) {
      EntityPlayer var3 = ((CraftPlayer)player).getHandle();
      var3.g.b(new ClientboundSetActionBarTextPacket((IChatBaseComponent)this.eQ(message)));
   }

   @Override
   public void method2955(Player player, Object component) {
      EntityPlayer var3 = ((CraftPlayer)player).getHandle();
      var3.g.b(new ClientboundSetActionBarTextPacket((IChatBaseComponent)component));
   }

   @Override
   public void cl(Player player) {
      EntityPlayer var2 = ((CraftPlayer)player).getHandle();
      var2.g.b(new ClientboundSetActionBarTextPacket(aPw));
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

   @Override
   public void cm(Player bukkitPlayer) {
      MultiActionDialog var2;
      try {
         IChatMutableComponent var3 = IChatBaseComponent.b(" ".repeat(150));
         PlainMessage var4 = new PlainMessage(IChatBaseComponent.i(), 200);
         var2 = new MultiActionDialog(
            new CommonDialogData(
               var3,
               Optional.empty(),
               true,
               false,
               DialogAction.b,
               List.of(
                  new ItemBody(CraftItemStack.asNMSCopy(new ItemStack(Material.PURPLE_CONCRETE)), Optional.empty(), false, false, 16, 16),
                  new PlainMessage(
                     ((IChatBaseComponent)AdventureText.toNative(AdventureText.parseRichText("<gradient:#ac52d4:#6c3484>ItemsAdder</gradient>")))
                        .f()
                        .a(EnumChatFormat.r),
                     200
                  ),
                  new PlainMessage(
                     IChatBaseComponent.b("Welcome!")
                        .f("\n")
                        .b(IChatBaseComponent.b("Thanks for buying my plugin! ").b(Color.YELLOW.getRGB()))
                        .b(IChatBaseComponent.b("<3").b(Color.RED.getRGB()))
                        .f("\n\n")
                        .b(IChatBaseComponent.b("Before using the plugin you have to finish the installation (it's easy).")),
                     200
                  ),
                  new PlainMessage(IChatBaseComponent.b("Have fun! :D"), 200),
                  var4,
                  var4,
                  var4,
                  new PlainMessage(IChatBaseComponent.b("(press ESC to close)"), 200)
               ),
               List.of()
            ),
            List.of(
               new ActionButton(
                  new CommonButtonData(IChatBaseComponent.b("Hosting Tutorial"), Optional.empty(), 150),
                  Optional.of(new StaticAction(new OpenUrl(new URI("https://a.devs.beer/ia-host-pack"))))
               ),
               new ActionButton(
                  new CommonButtonData(IChatBaseComponent.b("First Installation"), Optional.empty(), 150),
                  Optional.of(new StaticAction(new OpenUrl(new URI("https://a.devs.beer/ia-welcome-first-install"))))
               ),
               new ActionButton(
                  new CommonButtonData(IChatBaseComponent.b("Wiki"), Optional.empty(), 150),
                  Optional.of(new StaticAction(new OpenUrl(new URI("https://a.devs.beer/ia-welcome-wiki"))))
               )
            ),
            Optional.empty(),
            1
         );
      } catch (URISyntaxException var5) {
         throw new RuntimeException(var5);
      }

      EntityPlayer var6 = ((CraftPlayer)bukkitPlayer).getHandle();
      var6.b(Holder.a(var2));
   }

   @Override
   public void method2965(Player bukkitPlayer, Book adventureBook) {
      if (ServerCompat.isPaper()) {
         bukkitPlayer.openBook(adventureBook);
      } else {
         WrittenBookContent var3 = new WrittenBookContent(
            Filterable.a(AdventureText.toPlainText(adventureBook.title())),
            AdventureText.toPlainText(adventureBook.author()),
            0,
            adventureBook.pages().stream().map(var0 -> Filterable.a((IChatBaseComponent)AdventureText.toNative(var0))).toList(),
            true
         );
         net.minecraft.world.item.ItemStack var4 = Items.vB.m();
         var4.b(DataComponents.V, var3);
         bukkitPlayer.openBook(CraftItemStack.asCraftMirror(var4));
      }
   }
}
