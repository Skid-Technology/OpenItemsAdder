package dev.lone.itemsadder.entity;

import com.mojang.datafixers.util.Pair;
import dev.lone.itemsadder.network.ViaVersionIntegration;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.ServerCompat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.network.protocol.game.ClientboundCustomChatCompletionsPacket;
import net.minecraft.network.protocol.game.PacketPlayOutAnimation;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEffect;
import net.minecraft.network.protocol.game.PacketPlayOutRemoveEntityEffect;
import net.minecraft.network.protocol.game.PacketPlayOutUpdateAttributes;
import net.minecraft.network.protocol.game.ClientboundCustomChatCompletionsPacket.Action;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.EnumHand;
import net.minecraft.world.EnumInteractionResult;
import net.minecraft.world.EnumInteractionResult.c;
import net.minecraft.world.EnumInteractionResult.d;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifiable;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemBlock;
import net.minecraft.world.item.ItemSign;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockActionContext;
import net.minecraft.world.item.context.ItemActionContext;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.BlockFacingHorizontal;
import net.minecraft.world.level.block.BlockTrapdoor;
import net.minecraft.world.level.block.entity.TileEntitySign;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.block.state.properties.BlockPropertyHalf;
import net.minecraft.world.phys.MovingObjectPositionBlock;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_21_R4.block.CraftBlock;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class PlayerInteractionHandlerImplG implements PlayerInteractionHandler {
   static ItemStack aVS = Items.a.m();
   static List bnm = new ArrayList();

   @Override
   public boolean isAvailable() {
      return true;
   }

   @Override
   public void runOnMainThread(Runnable callback) {
      Objects.requireNonNull(callback, "callback");
      MinecraftServer.getServer().c(() -> {
         try {
            callback.run();
         } catch (Exception | LinkageError var2) {
            Log.warn("Failed to finish native mining input", var2);
         }
      });
   }

   @Override
   public int cx(Player player) {
      EntityPlayer var2 = ((CraftPlayer)player).getHandle();
      return var2.bR.l;
   }

   @Override
   public void interactEntity(Player player, Entity entity) {
      EntityPlayer var3 = ((CraftPlayer)player).getHandle();
      net.minecraft.world.entity.Entity var4 = ((CraftEntity)entity).getHandle();
      var3.e(var4);
   }

   @Override
   public void interactEntityWithSlot(Player player, Entity entity, EquipmentSlot hand) {
      EntityPlayer var4 = ((CraftPlayer)player).getHandle();
      net.minecraft.world.entity.Entity var5 = ((CraftEntity)entity).getHandle();
      var4.a(var5, hand == EquipmentSlot.HAND ? EnumHand.a : EnumHand.b);
   }

   @Override
   public void dropItems(Player bukkitPlayer, @NotNull Collection completions) {
      EntityPlayer var3 = ((CraftPlayer)bukkitPlayer).getHandle();
      var3.f.b(new ClientboundCustomChatCompletionsPacket(Action.a, new ArrayList<>(completions)));
   }

   @Override
   public void dropItemsDup2(Player bukkitPlayer, @NotNull Collection completions) {
      EntityPlayer var3 = ((CraftPlayer)bukkitPlayer).getHandle();
      var3.f.b(new ClientboundCustomChatCompletionsPacket(Action.b, new ArrayList<>(completions)));
   }

   @Override
   public void setReach(Player player, float speed) {
      EntityPlayer var3 = ((CraftPlayer)player).getHandle();
      AttributeModifiable var4 = new AttributeModifiable(GenericAttributes.f, var0 -> {});
      var4.a(speed);
      PacketPlayOutUpdateAttributes var5 = new PacketPlayOutUpdateAttributes(var3.ao(), List.of(var4));
      var3.f.b(var5);
      if (ServerCompat.viaVersionPresent && ViaVersionIntegration.method4869(player)) {
         var3.f.b(new PacketPlayOutEntityEffect(var3.ao(), new MobEffect(MobEffects.d, -1, -1, false, false, false), true));
      }
   }

   @Override
   public void be(Player player) {
      EntityPlayer var2 = ((CraftPlayer)player).getHandle();
      PacketPlayOutUpdateAttributes var3 = new PacketPlayOutUpdateAttributes(var2.ao(), List.of(var2.fa().a(GenericAttributes.f)));
      var2.f.b(var3);
      if (ServerCompat.viaVersionPresent && ViaVersionIntegration.method4869(player)) {
         PotionEffect var4 = player.getPotionEffect(PotionEffectType.MINING_FATIGUE);
         if (var4 == null) {
            var2.f.b(new PacketPlayOutRemoveEntityEffect(var2.ao(), MobEffects.d));
         } else {
            var2.f
               .b(
                  new PacketPlayOutEntityEffect(
                     var2.ao(),
                     new MobEffect(MobEffects.d, var4.getDuration(), var4.getAmplifier(), var4.isAmbient(), var4.hasParticles(), var4.hasIcon()),
                     true
                  )
               );
         }
      }
   }

   @Override
   public double method5269(Player player, double range) {
      EntityPlayer var4 = ((CraftPlayer)player).getHandle();
      double var5 = var4.h(GenericAttributes.g);
      AttributeModifiable var7 = new AttributeModifiable(GenericAttributes.g, var0 -> {});
      var7.a(range);
      var4.f.b(new PacketPlayOutUpdateAttributes(var4.ao(), List.of(var7)));
      return var5;
   }

   @Override
   public void cH(Player player) {
      EntityPlayer var2 = ((CraftPlayer)player).getHandle();
      PacketPlayOutUpdateAttributes var3 = new PacketPlayOutUpdateAttributes(var2.ao(), List.of(var2.fa().a(GenericAttributes.g)));
      var2.f.b(var3);
   }

   @Override
   public boolean placeBlock(Player player, EquipmentSlot equipmentSlot, Block clickedBlock, Location interactLocation, BlockFace blockFace) {
      EntityPlayer var6 = ((CraftPlayer)player).getHandle();
      ItemStack var7 = var6.b(equipmentSlot == EquipmentSlot.HAND ? EnumHand.a : EnumHand.b);
      Item var8 = var7.h();
      EnumHand var9 = equipmentSlot == EquipmentSlot.HAND ? EnumHand.a : EnumHand.b;
      World var10 = var6.dV();
      EnumDirection var11 = CraftBlock.blockFaceToNotch(blockFace);
      ItemActionContext var12 = new ItemActionContext(
         var10,
         var6,
         var9,
         var7,
         new MovingObjectPositionBlock(
            new Vec3D(interactLocation.getX(), interactLocation.getY(), interactLocation.getZ()),
            var11,
            new BlockPosition(interactLocation.getBlockX(), interactLocation.getBlockY(), interactLocation.getBlockZ()),
            false
         )
      );
      EnumInteractionResult var13 = var7.a(var12);
      if (var13 instanceof d) {
         if (var8 instanceof ItemBlock var14) {
            Block var15 = clickedBlock.getRelative(blockFace);
            BlockPosition var16 = new BlockPosition(var15.getX(), var15.getY(), var15.getZ());
            if (var14 instanceof ItemSign && var10.c_(var16) instanceof TileEntitySign var26) {
               var26.a(var6.cG());
               var6.a(var26, true);
               return true;
            }

            if (var14.c() instanceof BlockTrapdoor var24 && var10.a_(var16).b() instanceof BlockTrapdoor) {
               EnumDirection var25 = var12.k();
               if (var25.o().d()) {
                  BlockActionContext var19 = new BlockActionContext(var12);
                  IBlockData var20 = var24.a(var19);
                  assert var20 != null;
                  ((IBlockData)var20.b(BlockFacingHorizontal.e, var19.k()))
                     .b(BlockTrapdoor.c, var19.l().e - var19.a().v() > 0.5 ? BlockPropertyHalf.a : BlockPropertyHalf.b);
                  var10.z(var16).a(var16, var20, 0);
                  return true;
               }
            }
         }
      } else if (var13 instanceof c && var8.a(var10, var6, var9) instanceof d var22) {
         ItemStack var23 = var22.d();
         if (var23 != null) {
            var6.a(var9, var23);
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
      EntityPlayer var3 = ((CraftPlayer)player).getHandle();
      PacketPlayOutAnimation var4 = new PacketPlayOutAnimation(var3, bukkitSlot == EquipmentSlot.HAND ? 0 : 3);
      var3.f.b(var4);
   }

   static {
      for (EnumItemSlot var3 : EnumItemSlot.values()) {
         bnm.add(new Pair(var3, aVS));
      }
   }
}
