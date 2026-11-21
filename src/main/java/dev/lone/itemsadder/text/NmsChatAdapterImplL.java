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
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.ClickEvent.OpenUrl;
import net.minecraft.network.chat.SignedMessageBody.Packed;
import net.minecraft.network.chat.contents.PlainTextContents.LiteralContents;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.dialog.ActionButton;
import net.minecraft.server.dialog.CommonButtonData;
import net.minecraft.server.dialog.CommonDialogData;
import net.minecraft.server.dialog.DialogAction;
import net.minecraft.server.dialog.MultiActionDialog;
import net.minecraft.server.dialog.action.StaticAction;
import net.minecraft.server.dialog.body.ItemBody;
import net.minecraft.server.dialog.body.PlainMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WrittenBookContent;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R7.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class NmsChatAdapterImplL implements NmsChatAdapter {
   private static final Component aPB = CraftChatMessage.fromStringOrNull(" ");

   @Override
   public void method2954(Player player, String message) {
      ServerPlayer var3 = ((CraftPlayer)player).getHandle();
      var3.connection.send(new ClientboundSetActionBarTextPacket((Component)this.eQ(message)));
   }

   @Override
   public void method2955(Player player, Object component) {
      ServerPlayer var3 = ((CraftPlayer)player).getHandle();
      var3.connection.send(new ClientboundSetActionBarTextPacket((Component)component));
   }

   @Override
   public void cl(Player player) {
      ServerPlayer var2 = ((CraftPlayer)player).getHandle();
      var2.connection.send(new ClientboundSetActionBarTextPacket(aPB));
   }

   @Override
   public Object eQ(String str) {
      return MutableComponent.create(new LiteralContents(str));
   }

   @Override
   public Object method2956(String str, int rgbColor) {
      return MutableComponent.create(new LiteralContents(str)).withStyle(Style.EMPTY.withColor(rgbColor));
   }

   @Override
   public boolean method2957(Object object, String str) {
      return object instanceof Component var3 ? var3.getString().contains(str) : false;
   }

   @Override
   public String method2958(Object signedMessageBody_Packed) {
      Packed var2 = (Packed)signedMessageBody_Packed;
      return var2.content();
   }

   @Nullable
   @Override
   public Component method2959(Object componentObj) {
      if (componentObj instanceof Component var2) {
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
      if (componentAdvObj instanceof net.kyori.adventure.text.Component var2) {
         return EnvironmentDetector.paperAdventure
            ? PaperAdventure.asVanilla(var2)
            : CraftChatMessage.fromJSON((String)GsonComponentSerializer.gson().serialize(var2));
      } else {
         return null;
      }
   }

   @Override
   public String method2964(Object nmsComponent) {
      if (nmsComponent instanceof Component var2) {
         StringBuilder var3 = new StringBuilder();
         var2.visit(var1x -> {
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
         MutableComponent var3 = Component.literal(" ".repeat(150));
         PlainMessage var4 = new PlainMessage(Component.empty(), 200);
         var2 = new MultiActionDialog(
            new CommonDialogData(
               var3,
               Optional.empty(),
               true,
               false,
               DialogAction.NONE,
               List.of(
                  new ItemBody(
                     ItemStackTemplate.fromNonEmptyStack(CraftItemStack.asNMSCopy(new ItemStack(Material.PURPLE_CONCRETE))),
                     Optional.empty(),
                     false,
                     false,
                     16,
                     16
                  ),
                  new PlainMessage(
                     ((Component)AdventureText.toNative(AdventureText.parseRichText("<gradient:#ac52d4:#6c3484>ItemsAdder</gradient>")))
                        .copy()
                        .withStyle(ChatFormatting.BOLD),
                     200
                  ),
                  new PlainMessage(
                     Component.literal("Welcome!")
                        .append("\n")
                        .append(Component.literal("Thanks for buying my plugin! ").withColor(Color.YELLOW.getRGB()))
                        .append(Component.literal("<3").withColor(Color.RED.getRGB()))
                        .append("\n\n")
                        .append(Component.literal("Before using the plugin you have to finish the installation (it's easy).")),
                     200
                  ),
                  new PlainMessage(Component.literal("Have fun! :D"), 200),
                  var4,
                  var4,
                  var4,
                  new PlainMessage(Component.literal("(press ESC to close)"), 200)
               ),
               List.of()
            ),
            List.of(
               new ActionButton(
                  new CommonButtonData(Component.literal("Hosting Tutorial"), Optional.empty(), 150),
                  Optional.of(new StaticAction(new OpenUrl(new URI("https://a.devs.beer/ia-host-pack"))))
               ),
               new ActionButton(
                  new CommonButtonData(Component.literal("First Installation"), Optional.empty(), 150),
                  Optional.of(new StaticAction(new OpenUrl(new URI("https://a.devs.beer/ia-welcome-first-install"))))
               ),
               new ActionButton(
                  new CommonButtonData(Component.literal("Wiki"), Optional.empty(), 150),
                  Optional.of(new StaticAction(new OpenUrl(new URI("https://a.devs.beer/ia-welcome-wiki"))))
               )
            ),
            Optional.empty(),
            1
         );
      } catch (URISyntaxException var5) {
         throw new RuntimeException(var5);
      }

      ServerPlayer var6 = ((CraftPlayer)bukkitPlayer).getHandle();
      var6.openDialog(Holder.direct(var2));
   }

   @Override
   public void method2965(Player bukkitPlayer, Book adventureBook) {
      if (ServerCompat.isPaper()) {
         bukkitPlayer.openBook(adventureBook);
      } else {
         WrittenBookContent var3 = new WrittenBookContent(
            Filterable.passThrough(AdventureText.toPlainText(adventureBook.title())),
            AdventureText.toPlainText(adventureBook.author()),
            0,
            adventureBook.pages().stream().map(var0 -> Filterable.passThrough((Component)AdventureText.toNative(var0))).toList(),
            true
         );
         net.minecraft.world.item.ItemStack var4 = Items.WRITTEN_BOOK.getDefaultInstance();
         var4.set(DataComponents.WRITTEN_BOOK_CONTENT, var3);
         ItemStack var5 = new ItemStack(Material.WRITTEN_BOOK);
         var5.setItemMeta(CraftItemStack.getItemMeta(var4));
         bukkitPlayer.openBook(var5);
      }
   }
}
