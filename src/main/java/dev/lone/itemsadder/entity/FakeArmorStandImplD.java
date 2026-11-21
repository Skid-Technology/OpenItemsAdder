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
import net.minecraft.core.Vector3f;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.protocol.game.PacketPlayOutUpdateAttributes;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcher.Item;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAreaEffectCloud;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.CustomModelData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_21_R7.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R7.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class FakeArmorStandImplD extends AbstractFakeEntity {
   static Field aTh = ServerCompat.findFieldByType(DataWatcher.class, Item[].class);
   private final EntityArmorStand aTB;
   private final EntityAreaEffectCloud aTC;
   private final PacketPlayOutEntityDestroy aTD;
   ClientboundBundlePacket aTl;
   private final Item aTE;
   private final Item aTF;
   private final EnumItemSlot aTG;

   public FakeArmorStandImplD(Location var1, EquipmentSlot var2, boolean var3, boolean var4, boolean var5) {
      super(var1, var3);
      WorldServer var6 = ((CraftWorld)var1.getWorld()).getHandle();
      this.aTB = new EntityArmorStand(var6, var1.getX(), var1.getY(), var1.getZ());
      this.aQk = new DoubleVector3(this.aTB.dI().g, this.aTB.dI().h, this.aTB.dI().i);
      this.aQn = this.aTB.aA();
      this.aTB.g(true);
      this.aTB.p(false);
      this.aTB.l(true);
      this.aTB.b(true);
      this.aTB.v(var4 || !var5);
      this.aTB.v(0.0F);
      this.aTB.w(0.0F);
      this.aTC = new EntityAreaEffectCloud(var6, var1.getX(), var1.getY(), var1.getZ());
      this.aQm = this.aTC.aA();
      this.aQl = new DoubleVector3(this.aTC.dI().g, this.aTC.dI().h, this.aTC.dI().i);
      this.aTC.at = Integer.MIN_VALUE;
      this.aTC.a(-1);
      this.aTC.d(Integer.MIN_VALUE);
      this.aTC.a(0.0F);
      this.aTC.a(new PotionContents(this.aTC.u.e(), Optional.of(new Color(0, 0, 0, 0).getRGB()), this.aTC.u.d(), this.aTC.u.g()));
      this.aTC.l(true);
      this.aQw = this.aTC.ay().n().b() * 0.75;
      this.aQg = this.aQl.acy();
      this.aQh = this.aQk.acy();
      this.aQi = this.aQl.acy();
      this.aQj = this.aQk.acy();
      this.aTD = new PacketPlayOutEntityDestroy(new int[]{this.aTB.aA(), this.aTC.aA()});
      this.aTG = EnumItemSlot.a(PlayerInteractionFacade.slotName(var2));
      if (ServerCompat.isPaper()) {
         this.aTE = this.aTB.aD().b(this.aTG == EnumItemSlot.a ? EntityArmorStand.r : EntityArmorStand.o);
         this.aTF = this.aTB.aD().b((DataWatcherObject)ServerCompat.getFieldValueByTypeIndex(Entity.class, DataWatcherObject.class, 0));
      } else {
         try {
            Item[] var7 = (Item[])aTh.get(this.aTB.aD());
            this.aTE = var7[this.aTG == EnumItemSlot.a ? EntityArmorStand.r.a() : EntityArmorStand.o.a()];
            this.aTF = var7[((DataWatcherObject)ServerCompat.getFieldValueByTypeIndex(Entity.class, DataWatcherObject.class, 0)).a()];
         } catch (IllegalAccessException var8) {
            throw new RuntimeException(var8);
         }
      }
   }

   @Override
   public void setAngles(float x, float y, float z) {
      Vector3f var4 = (Vector3f)this.aTE.b();
      if (!g(var4.a(), x) || !g(var4.b(), y) || !g(var4.c(), z)) {
         this.aTE.a(new Vector3f(x, y, z));
         this.aQr = true;
      }
   }

   @Override
   public boolean method2994(float yaw) {
      if (!super.method2994(yaw)) {
         return false;
      }

      this.aTB.bE = yaw;
      this.aTB.v(yaw);
      return true;
   }

   private static PacketPlayOutSpawnEntity createSpawnPacket(Entity var0, DoubleVector3 var1, double var2) {
      return new PacketPlayOutSpawnEntity(var0.getId(), var0.getUUID(), var1.pW, var1.pX, var1.pY, var0.getXRot(), var0.getYRot(), var0.getType(), 0, var0.getDeltaMovement(), var2);
   }

   private ClientboundBundlePacket buildBundle() {
      if (this.aTG == EnumItemSlot.a) {
         this.aTB.d((Vector3f)this.aTE.b());
      } else {
         this.aTB.a((Vector3f)this.aTE.b());
      }

      PacketPlayOutSpawnEntity var1 = createSpawnPacket(this.aTB, this.aQk, this.aTB.bE);
      PacketPlayOutEntityMetadata var2 = new PacketPlayOutEntityMetadata(this.aTB.aA(), this.aTB.aD().c());
      PacketPlayOutSpawnEntity var3 = createSpawnPacket(this.aTC, this.aQl, 0.0);
      PacketPlayOutEntityMetadata var4 = new PacketPlayOutEntityMetadata(this.aTC.aA(), this.aTC.aD().c());
      this.aTC.aS = ImmutableList.of(this.aTB);
      PacketPlayOutMount var5 = new PacketPlayOutMount(this.aTC);
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
      this.sendPacketToPlayer(viewer.li, this.aTD);
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
         var3.b(DataComponents.POTION_CONTENTS, new PotionContents(PotionContents.EMPTY.e(), Optional.of(16744062), PotionContents.EMPTY.d(), PotionContents.EMPTY.g()));
      } else {
         var3.b(DataComponents.POTION_CONTENTS, new PotionContents(PotionContents.EMPTY.e(), Optional.of(this.arJ), PotionContents.EMPTY.d(), PotionContents.EMPTY.g()));
      }

      if (this.aQu) {
         var3.a(CraftEnchantment.bukkitToMinecraftHolder(Enchantment.SHARPNESS), 1);
      }

      var3.b(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(Integer.valueOf(var1).floatValue()), List.of(), List.of(), List.of()));
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
         Vector3f var2 = (Vector3f)this.aTE.b();
         this.method3016(viewers, var2.a(), var2.b(), var2.c());
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
            this.aTF.a((byte)((Byte)this.aTF.b() | 1));
         } else {
            this.aTF.a((byte)((Byte)this.aTF.b() & -2));
         }

         this.method3020(viewers, (Byte)this.aTF.b());
      }
   }

   @Override
   public void onViewerAdded(Player bukkitPlayer) {
      this.aTB.h(GenericAttributes.u).a(1.0);
      this.sendPacketToPlayer(bukkitPlayer, new PacketPlayOutUpdateAttributes(this.aQn, this.aTB.fw().c()));
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
         for (Packet var5 : var2.b()) {
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
