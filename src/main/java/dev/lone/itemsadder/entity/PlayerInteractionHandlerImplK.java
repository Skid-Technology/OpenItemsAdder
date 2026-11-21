package dev.lone.itemsadder.entity;

import com.mojang.datafixers.util.Pair;
import dev.lone.itemsadder.network.PacketListenerStubC;
import dev.lone.itemsadder.network.RunnablePacketF;
import dev.lone.itemsadder.network.ViaVersionIntegration;
import dev.lone.itemsadder.util.ServerCompat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundCustomChatCompletionsPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundCustomChatCompletionsPacket.Action;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResult.Pass;
import net.minecraft.world.InteractionResult.Success;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_21_R7.block.CraftBlock;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class PlayerInteractionHandlerImplK implements PlayerInteractionHandler {
   private static final PacketListener bnD = new PacketListenerStubC();
   static ItemStack aVS = Items.AIR.getDefaultInstance();
   static List bnm = new ArrayList();

   @Override
   public boolean isAvailable() {
      return true;
   }

   @Override
   public void runOnMainThread(Runnable callback) {
      MinecraftServer.getServer().packetProcessor().scheduleIfPossible(bnD, new RunnablePacketF(Objects.requireNonNull(callback, "callback")));
   }

   @Override
   public int cx(Player player) {
      ServerPlayer var2 = ((CraftPlayer)player).getHandle();
      return var2.containerMenu.containerId;
   }

   @Override
   public void interactEntity(Player player, Entity entity) {
      ServerPlayer var3 = ((CraftPlayer)player).getHandle();
      net.minecraft.world.entity.Entity var4 = ((CraftEntity)entity).getHandle();
      var3.attack(var4);
   }

   @Override
   public void interactEntityWithSlot(Player player, Entity entity, EquipmentSlot hand) {
      ServerPlayer var4 = ((CraftPlayer)player).getHandle();
      net.minecraft.world.entity.Entity var5 = ((CraftEntity)entity).getHandle();
      var4.interactOn(var5, hand == EquipmentSlot.HAND ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, new Vec3(0.5, 0.5, 0.5));
   }

   @Override
   public void dropItems(Player bukkitPlayer, @NotNull Collection completions) {
      ServerPlayer var3 = ((CraftPlayer)bukkitPlayer).getHandle();
      var3.connection.send(new ClientboundCustomChatCompletionsPacket(Action.ADD, new ArrayList<>(completions)));
   }

   @Override
   public void dropItemsDup2(Player bukkitPlayer, @NotNull Collection completions) {
      ServerPlayer var3 = ((CraftPlayer)bukkitPlayer).getHandle();
      var3.connection.send(new ClientboundCustomChatCompletionsPacket(Action.REMOVE, new ArrayList<>(completions)));
   }

   @Override
   public void setReach(Player player, float speed) {
      ServerPlayer var3 = ((CraftPlayer)player).getHandle();
      AttributeInstance var4 = new AttributeInstance(Attributes.BLOCK_BREAK_SPEED, var0 -> {});
      var4.setBaseValue(speed);
      ClientboundUpdateAttributesPacket var5 = new ClientboundUpdateAttributesPacket(var3.getId(), List.of(var4));
      var3.connection.send(var5);
      if (ServerCompat.viaVersionPresent && ViaVersionIntegration.method4869(player)) {
         var3.connection
            .send(new ClientboundUpdateMobEffectPacket(var3.getId(), new MobEffectInstance(MobEffects.MINING_FATIGUE, -1, -1, false, false, false), true));
      }
   }

   @Override
   public void be(Player player) {
      ServerPlayer var2 = ((CraftPlayer)player).getHandle();
      ClientboundUpdateAttributesPacket var3 = new ClientboundUpdateAttributesPacket(
         var2.getId(), List.of(var2.getAttributes().getInstance(Attributes.BLOCK_BREAK_SPEED))
      );
      var2.connection.send(var3);
      if (ServerCompat.viaVersionPresent && ViaVersionIntegration.method4869(player)) {
         PotionEffect var4 = player.getPotionEffect(PotionEffectType.MINING_FATIGUE);
         if (var4 == null) {
            var2.connection.send(new ClientboundRemoveMobEffectPacket(var2.getId(), MobEffects.MINING_FATIGUE));
         } else {
            var2.connection
               .send(
                  new ClientboundUpdateMobEffectPacket(
                     var2.getId(),
                     new MobEffectInstance(
                        MobEffects.MINING_FATIGUE, var4.getDuration(), var4.getAmplifier(), var4.isAmbient(), var4.hasParticles(), var4.hasIcon()
                     ),
                     true
                  )
               );
         }
      }
   }

   @Override
   public double method5269(Player player, double range) {
      ServerPlayer var4 = ((CraftPlayer)player).getHandle();
      double var5 = var4.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE);
      AttributeInstance var7 = new AttributeInstance(Attributes.BLOCK_INTERACTION_RANGE, var0 -> {});
      var7.setBaseValue(range);
      var4.connection.send(new ClientboundUpdateAttributesPacket(var4.getId(), List.of(var7)));
      return var5;
   }

   @Override
   public void cH(Player player) {
      ServerPlayer var2 = ((CraftPlayer)player).getHandle();
      ClientboundUpdateAttributesPacket var3 = new ClientboundUpdateAttributesPacket(
         var2.getId(), List.of(var2.getAttributes().getInstance(Attributes.BLOCK_INTERACTION_RANGE))
      );
      var2.connection.send(var3);
   }

   @Override
   public boolean placeBlock(Player player, EquipmentSlot equipmentSlot, Block clickedBlock, Location interactLocation, BlockFace blockFace) {
      ServerPlayer var6 = ((CraftPlayer)player).getHandle();
      ItemStack var7 = var6.getItemInHand(equipmentSlot == EquipmentSlot.HAND ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
      Item var8 = var7.getItem();
      InteractionHand var9 = equipmentSlot == EquipmentSlot.HAND ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
      ServerLevel var10 = var6.level();
      Direction var11 = CraftBlock.blockFaceToNotch(blockFace);
      UseOnContext var12 = new UseOnContext(
         var10,
         var6,
         var9,
         var7,
         new BlockHitResult(
            new Vec3(interactLocation.getX(), interactLocation.getY(), interactLocation.getZ()),
            var11,
            new BlockPos(interactLocation.getBlockX(), interactLocation.getBlockY(), interactLocation.getBlockZ()),
            false
         )
      );
      InteractionResult var13 = var7.useOn(var12);
      if (var13 instanceof Success) {
         if (var8 instanceof BlockItem var14) {
            Block var15 = clickedBlock.getRelative(blockFace);
            BlockPos var16 = new BlockPos(var15.getX(), var15.getY(), var15.getZ());
            if (var14 instanceof SignItem && var10.getBlockEntity(var16) instanceof SignBlockEntity var26) {
               var26.setAllowedPlayerEditor(var6.getUUID());
               var6.openTextEdit(var26, true);
               return true;
            }

            if (var14.getBlock() instanceof TrapDoorBlock var24 && var10.getBlockState(var16).getBlock() instanceof TrapDoorBlock) {
               Direction var25 = var12.getClickedFace();
               if (var25.getAxis().isHorizontal()) {
                  BlockPlaceContext var19 = new BlockPlaceContext(var12);
                  BlockState var20 = var24.getStateForPlacement(var19);
                  assert var20 != null;
                  ((BlockState)var20.setValue(HorizontalDirectionalBlock.FACING, var19.getClickedFace()))
                     .setValue(TrapDoorBlock.HALF, var19.getClickLocation().y - var19.getClickedPos().getY() > 0.5 ? Half.TOP : Half.BOTTOM);
                  var10.getChunk(var16).setBlockState(var16, var20, 0);
                  return true;
               }
            }
         }
      } else if (var13 instanceof Pass && var8.use(var10, var6, var9) instanceof Success var22) {
         ItemStack var23 = var22.heldItemTransformedTo();
         if (var23 != null) {
            var6.setItemInHand(var9, var23);
         }

         return true;
      }

      return false;
   }

   @Override
   public void consumeItem(Player player, org.bukkit.inventory.ItemStack bukkitItemStack, int ticks) {
      player.setCooldown(bukkitItemStack, ticks);
   }

   @Override
   public void swingHand(Player player, EquipmentSlot bukkitSlot) {
      ServerPlayer var3 = ((CraftPlayer)player).getHandle();
      ClientboundAnimatePacket var4 = new ClientboundAnimatePacket(var3, bukkitSlot == EquipmentSlot.HAND ? 0 : 3);
      var3.connection.send(var4);
   }

   static {
      for (net.minecraft.world.entity.EquipmentSlot var3 : net.minecraft.world.entity.EquipmentSlot.values()) {
         bnm.add(new Pair(var3, aVS));
      }
   }
}
