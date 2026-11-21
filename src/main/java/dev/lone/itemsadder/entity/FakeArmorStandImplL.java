package dev.lone.itemsadder.entity;

import beer.devs.nativepackets.api.NativeItem;
import com.google.common.collect.ImmutableList;
import dev.lone.itemsadder.anim.AnimationTicker;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.util.DoubleVector3;
import dev.lone.itemsadder.util.ServerCompat;
import java.awt.Color;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Rotations;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.DataItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.CustomModelData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R7.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class FakeArmorStandImplL extends AbstractFakeEntity {
   static Field aTh = ServerCompat.findFieldByType(SynchedEntityData.class, DataItem[].class);
   private final ArmorStand aUxx;
   private final AreaEffectCloud aUy;
   private final ClientboundRemoveEntitiesPacket aUz;
   ClientboundBundlePacket aTl;
   private final DataItem aUA;
   private final DataItem aUB;
   private final EquipmentSlot aUC;

   public FakeArmorStandImplL(Location var1, org.bukkit.inventory.EquipmentSlot var2, boolean var3, boolean var4, boolean var5) {
      super(var1, var3);
      ServerLevel var6 = ((CraftWorld)var1.getWorld()).getHandle();
      this.aUxx = new ArmorStand(var6, var1.getX(), var1.getY(), var1.getZ());
      this.aQk = new DoubleVector3(this.aUxx.position().x, this.aUxx.position().y, this.aUxx.position().z);
      this.aQn = this.aUxx.getId();
      this.aUxx.setNoGravity(true);
      this.aUxx.setCustomNameVisible(false);
      this.aUxx.setInvisible(true);
      this.aUxx.setNoBasePlate(true);
      this.aUxx.setMarker(var4 || !var5);
      this.aUxx.setYRot(0.0F);
      this.aUxx.setXRot(0.0F);
      this.aUy = new AreaEffectCloud(var6, var1.getX(), var1.getY(), var1.getZ());
      this.aQm = this.aUy.getId();
      this.aQl = new DoubleVector3(this.aUy.position().x, this.aUy.position().y, this.aUy.position().z);
      this.aUy.tickCount = Integer.MIN_VALUE;
      this.aUy.setDuration(-1);
      this.aUy.setWaitTime(Integer.MIN_VALUE);
      this.aUy.setRadius(0.0F);
      this.aUy
         .setPotionContents(
            new PotionContents(
               this.aUy.potionContents.potion(),
               Optional.of(new Color(0, 0, 0, 0).getRGB()),
               this.aUy.potionContents.customEffects(),
               this.aUy.potionContents.customName()
            )
         );
      this.aUy.setInvisible(true);
      this.aQw = this.aUy.getType().getDimensions().height() * 0.75;
      this.aQg = this.aQl.acy();
      this.aQh = this.aQk.acy();
      this.aQi = this.aQl.acy();
      this.aQj = this.aQk.acy();
      this.aUz = new ClientboundRemoveEntitiesPacket(new int[]{this.aUxx.getId(), this.aUy.getId()});
      this.aUC = EquipmentSlot.byName(PlayerInteractionFacade.slotName(var2));
      if (ServerCompat.isPaper()) {
         this.aUA = this.aUxx.getEntityData().getItem(this.aUC == EquipmentSlot.MAINHAND ? ArmorStand.DATA_RIGHT_ARM_POSE : ArmorStand.DATA_HEAD_POSE);
         this.aUB = this.aUxx.getEntityData().getItem((EntityDataAccessor)ServerCompat.getFieldValueByTypeIndex(Entity.class, EntityDataAccessor.class, 0));
      } else {
         try {
            DataItem[] var7 = (DataItem[])aTh.get(this.aUxx.getEntityData());
            this.aUA = var7[this.aUC == EquipmentSlot.MAINHAND ? ArmorStand.DATA_RIGHT_ARM_POSE.id() : ArmorStand.DATA_HEAD_POSE.id()];
            this.aUB = var7[((EntityDataAccessor)ServerCompat.getFieldValueByTypeIndex(Entity.class, EntityDataAccessor.class, 0)).id()];
         } catch (IllegalAccessException var8) {
            throw new RuntimeException(var8);
         }
      }
   }

   @Override
   public void setAngles(float x, float y, float z) {
      Rotations var4 = (Rotations)this.aUA.getValue();
      if (!g(var4.x(), x) || !g(var4.y(), y) || !g(var4.z(), z)) {
         this.aUA.setValue(new Rotations(x, y, z));
         this.aQr = true;
      }
   }

   @Override
   public boolean method2994(float yaw) {
      if (!super.method2994(yaw)) {
         return false;
      }

      this.aUxx.yHeadRot = yaw;
      this.aUxx.setYRot(yaw);
      return true;
   }

   private static ClientboundAddEntityPacket createSpawnPacket(Entity var0, DoubleVector3 var1, double var2) {
      return new ClientboundAddEntityPacket(
         var0.getId(), var0.getUUID(), var1.pW, var1.pX, var1.pY, var0.getXRot(), var0.getYRot(), var0.getType(), 0, var0.getDeltaMovement(), var2
      );
   }

   private ClientboundBundlePacket buildBundle() {
      if (this.aUC == EquipmentSlot.MAINHAND) {
         this.aUxx.setRightArmPose((Rotations)this.aUA.getValue());
      } else {
         this.aUxx.setHeadPose((Rotations)this.aUA.getValue());
      }

      ClientboundAddEntityPacket var1 = createSpawnPacket(this.aUxx, this.aQk, this.aUxx.yHeadRot);
      ClientboundSetEntityDataPacket var2 = new ClientboundSetEntityDataPacket(this.aUxx.getId(), this.aUxx.getEntityData().getNonDefaultValues());
      ClientboundAddEntityPacket var3 = createSpawnPacket(this.aUy, this.aQl, 0.0);
      ClientboundSetEntityDataPacket var4 = new ClientboundSetEntityDataPacket(this.aUy.getId(), this.aUy.getEntityData().getNonDefaultValues());
      this.aUy.passengers = ImmutableList.of(this.aUxx);
      ClientboundSetPassengersPacket var5 = new ClientboundSetPassengersPacket(this.aUy);
      return new ClientboundBundlePacket(List.of(var1, var2, var3, var4, var5));
   }

   @Override
   public void addSpawnPackets(List viewers) {
      ClientboundBundlePacket var2 = this.buildBundle();

      for (int var3 = 0; var3 < viewers.size(); var3++) {
         EntityViewer var4 = viewers.get(var3);
         if (var4.kW()) {
            this.sendPacketTo(var4, var2);
         }
      }
   }

   @Override
   public void spawnFor(EntityViewer to) {
      ClientboundBundlePacket var2 = this.buildBundle();
      this.sendPacketTo(to, var2);
   }

   @Override
   public void despawnFor(EntityViewer viewer) {
      this.sendPacketToPlayer(viewer.li, this.aUz);
   }

   @Override
   public void addUpdatePackets(List viewers, int customModelData) {
      this.method3012(viewers, this.getNativeItemOrNull(customModelData));
   }

   @Override
   public void updateFor(EntityViewer viewer, int customModelData) {
      this.method3013(viewer, this.getNativeItemOrNull(customModelData));
   }

   @Override
   public void addTickPackets(List viewers, int customModelData) {
      this.method3012(viewers, this.getNativeItem(customModelData));
   }

   @Override
   public void sendEquipment(@Nullable Player basePlayer, List viewers, ItemStack bukkitItem) {
      this.method3014(viewers, NativePacketsHolder.PE().Py().nativeItem(CraftItemStack.asNMSCopy(bukkitItem)));
   }

   private net.minecraft.world.item.ItemStack createItemStack(int var1, boolean var2) {
      if (var1 == -1) {
         return CraftItemStack.asNMSCopy(new ItemStack(Material.AIR));
      }

      if (this.aQt && this.playerSkin != null) {
         ItemStack var4 = this.method3027(var1, this.playerSkin);
         return CraftItemStack.asNMSCopy(var4);
      }

      net.minecraft.world.item.ItemStack var3 = new net.minecraft.world.item.ItemStack(Items.POTION, 1);
      if (var2) {
         var3.set(
            DataComponents.POTION_CONTENTS,
            new PotionContents(PotionContents.EMPTY.potion(), Optional.of(16744062), PotionContents.EMPTY.customEffects(), PotionContents.EMPTY.customName())
         );
      } else {
         var3.set(
            DataComponents.POTION_CONTENTS,
            new PotionContents(PotionContents.EMPTY.potion(), Optional.of(this.arJ), PotionContents.EMPTY.customEffects(), PotionContents.EMPTY.customName())
         );
      }

      if (this.aQu) {
         var3.enchant(CraftEnchantment.bukkitToMinecraftHolder(Enchantment.SHARPNESS), 1);
      }

      var3.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(Integer.valueOf(var1).floatValue()), List.of(), List.of(), List.of()));
      return var3;
   }

   private NativeItem getNativeItem(int var1) {
      NativeItem var2 = (NativeItem)this.aQe.get(var1);
      if (var2 == null) {
         var2 = NativePacketsHolder.PE().Py().nativeItem(this.createItemStack(var1, true));
         this.aQe.put(var1, var2);
      }

      return var2;
   }

   private NativeItem getNativeItemOrNull(int var1) {
      NativeItem var2 = (NativeItem)this.aQd.get(var1);
      if (var2 == null) {
         var2 = NativePacketsHolder.PE().Py().nativeItem(this.createItemStack(var1, false));
         this.aQd.put(var1, var2);
      }

      return var2;
   }

   @Override
   public void addMetadataPackets(List viewers) {
      if (this.aQs || !viewers.isEmpty() && this.aQr) {
         Rotations var2 = (Rotations)this.aUA.getValue();
         this.method3016(viewers, var2.x(), var2.y(), var2.z());
         this.aQr = false;
      }
   }

   @Override
   public void addEquipmentPackets(boolean fixClientDelay, @Nullable LivingEntity baseEntity, List viewers) {
      if (this.aQs || !viewers.isEmpty() || this.aQp || this.aQq) {
         if (fixClientDelay) {
            this.addDisplayPackets(viewers);
         } else {
            this.aTl = null;
            this.method3018(viewers, this.aQo.getYaw(), this.aQl, this.aQk);
            this.aQp = false;
            this.aQq = false;
         }
      }
   }

   public void addDisplayPackets(List var1) {
      this.Ny();
      this.aTl = null;
      this.method3018(var1, this.aQo.getYaw(), this.aQg, this.aQh);
      this.aQp = false;
   }

   @Override
   public void addTeleportPackets(List viewers) {
      if (!viewers.isEmpty()) {
         if (this.aQf) {
            this.aUB.setValue((byte)((Byte)this.aUB.getValue() | 1));
         } else {
            this.aUB.setValue((byte)((Byte)this.aUB.getValue() & -2));
         }

         this.method3020(viewers, (Byte)this.aUB.getValue());
      }
   }

   @Override
   public void onViewerAdded(Player bukkitPlayer) {
      this.aUxx.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1.0);
      this.sendPacketToPlayer(bukkitPlayer, new ClientboundUpdateAttributesPacket(this.aQn, this.aUxx.getAttributes().getSyncableAttributes()));
   }

   public void sendPacketTo(EntityViewer var1, Packet var2) {
      NativePacketsHolder.PE().method1868(var1.li, var2);
   }

   public void sendPacketToPlayer(Player var1, Packet var2) {
      NativePacketsHolder.PE().method1868(var1, var2);
   }

   private void sendRaw(Packet var1) {
      AnimationTicker.method5252(var1.getClass().getName());
      if (var1 instanceof ClientboundBundlePacket var2) {
         for (Packet var5 : var2.subPackets()) {
            AnimationTicker.method5252(var5.getClass().getName());
         }
      }
   }

   static {
      if (aTh == null) {
         throw new RuntimeException("Failed to get field SynchedEntityData.DataItem[]");
      }
   }
}
