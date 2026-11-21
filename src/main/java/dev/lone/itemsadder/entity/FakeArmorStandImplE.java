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
import org.bukkit.craftbukkit.v1_21_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R2.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_21_R2.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class FakeArmorStandImplE extends AbstractFakeEntity {
   static Field aTh = ServerCompat.findFieldByType(DataWatcher.class, Item[].class);
   private final EntityArmorStand aTH;
   private final EntityAreaEffectCloud aTI;
   private final PacketPlayOutEntityDestroy aTJ;
   ClientboundBundlePacket aTl;
   private final Item aTK;
   private final Item aTL;
   private final EnumItemSlot aTM;

   public FakeArmorStandImplE(Location var1, EquipmentSlot var2, boolean var3, boolean var4, boolean var5) {
      super(var1, var3);
      WorldServer var6 = ((CraftWorld)var1.getWorld()).getHandle();
      this.aTH = new EntityArmorStand(var6, var1.getX(), var1.getY(), var1.getZ());
      this.aQk = new DoubleVector3(this.aTH.du().d, this.aTH.du().e, this.aTH.du().f);
      this.aQn = this.aTH.ar();
      this.aTH.f(true);
      this.aTH.p(false);
      this.aTH.k(true);
      this.aTH.b(true);
      this.aTH.v(var4 || !var5);
      this.aTH.v(0.0F);
      this.aTH.w(0.0F);
      this.aTI = new EntityAreaEffectCloud(var6, var1.getX(), var1.getY(), var1.getZ());
      this.aQm = this.aTI.ar();
      this.aQl = new DoubleVector3(this.aTI.du().d, this.aTI.du().e, this.aTI.du().f);
      this.aTI.af = Integer.MIN_VALUE;
      this.aTI.a(-1);
      this.aTI.c(Integer.MIN_VALUE);
      this.aTI.a(0.0F);
      this.aTI.a(new PotionContents(this.aTI.k.e(), Optional.of(new Color(0, 0, 0, 0).getRGB()), this.aTI.k.d(), this.aTI.k.g()));
      this.aTI.k(true);
      this.aQw = this.aTI.aq().n().b() * 0.75;
      this.aQg = this.aQl.acy();
      this.aQh = this.aQk.acy();
      this.aQi = this.aQl.acy();
      this.aQj = this.aQk.acy();
      this.aTJ = new PacketPlayOutEntityDestroy(new int[]{this.aTH.ar(), this.aTI.ar()});
      this.aTM = EnumItemSlot.a(PlayerInteractionFacade.slotName(var2));
      if (ServerCompat.isPaper()) {
         this.aTK = this.aTH.au().b(this.aTM == EnumItemSlot.a ? EntityArmorStand.bN : EntityArmorStand.bK);
         this.aTL = this.aTH.au().b((DataWatcherObject)ServerCompat.getFieldValueByTypeIndex(Entity.class, DataWatcherObject.class, 0));
      } else {
         try {
            Item[] var7 = (Item[])aTh.get(this.aTH.au());
            this.aTK = var7[this.aTM == EnumItemSlot.a ? EntityArmorStand.bN.a() : EntityArmorStand.bK.a()];
            this.aTL = var7[((DataWatcherObject)ServerCompat.getFieldValueByTypeIndex(Entity.class, DataWatcherObject.class, 0)).a()];
         } catch (IllegalAccessException var8) {
            throw new RuntimeException(var8);
         }
      }
   }

   @Override
   public void setAngles(float x, float y, float z) {
      Vector3f var4 = (Vector3f)this.aTK.b();
      if (!g(var4.b(), x) || !g(var4.c(), y) || !g(var4.d(), z)) {
         this.aTK.a(new Vector3f(x, y, z));
         this.aQr = true;
      }
   }

   @Override
   public boolean method2994(float yaw) {
      if (!super.method2994(yaw)) {
         return false;
      }

      this.aTH.aZ = yaw;
      this.aTH.v(yaw);
      return true;
   }

   private static PacketPlayOutSpawnEntity createSpawnPacket(Entity var0, DoubleVector3 var1, double var2) {
      return new PacketPlayOutSpawnEntity(var0.ar(), var0.cG(), var1.pW, var1.pX, var1.pY, var0.dO(), var0.dM(), var0.aq(), 0, var0.dz(), var2);
   }

   private ClientboundBundlePacket buildBundle() {
      if (this.aTM == EnumItemSlot.a) {
         this.aTH.d((Vector3f)this.aTK.b());
      } else {
         this.aTH.a((Vector3f)this.aTK.b());
      }

      PacketPlayOutSpawnEntity var1 = createSpawnPacket(this.aTH, this.aQk, this.aTH.aZ);
      PacketPlayOutEntityMetadata var2 = new PacketPlayOutEntityMetadata(this.aTH.ar(), this.aTH.au().c());
      PacketPlayOutSpawnEntity var3 = createSpawnPacket(this.aTI, this.aQl, 0.0);
      PacketPlayOutEntityMetadata var4 = new PacketPlayOutEntityMetadata(this.aTI.ar(), this.aTI.au().c());
      this.aTI.q = ImmutableList.of(this.aTH);
      PacketPlayOutMount var5 = new PacketPlayOutMount(this.aTI);
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
      this.sendPacketToPlayer(viewer.li, this.aTJ);
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

      net.minecraft.world.item.ItemStack var3 = new net.minecraft.world.item.ItemStack(Items.sZ, 1);
      if (var2) {
         var3.b(DataComponents.Q, new PotionContents(PotionContents.a.e(), Optional.of(16744062), PotionContents.a.d(), PotionContents.a.g()));
      } else {
         var3.b(DataComponents.Q, new PotionContents(PotionContents.a.e(), Optional.of(this.arJ), PotionContents.a.d(), PotionContents.a.g()));
      }

      if (this.aQu) {
         var3.a(CraftEnchantment.bukkitToMinecraftHolder(Enchantment.SHARPNESS), 1);
      }

      var3.b(DataComponents.p, new CustomModelData(var1));
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
         Vector3f var2 = (Vector3f)this.aTK.b();
         this.method3016(viewers, var2.b(), var2.c(), var2.d());
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
            this.aTL.a((byte)((Byte)this.aTL.b() | 1));
         } else {
            this.aTL.a((byte)((Byte)this.aTL.b() & -2));
         }

         this.method3020(viewers, (Byte)this.aTL.b());
      }
   }

   @Override
   public void onViewerAdded(Player bukkitPlayer) {
      this.aTH.g(GenericAttributes.s).a(1.0);
      this.sendPacketToPlayer(bukkitPlayer, new PacketPlayOutUpdateAttributes(this.aQn, this.aTH.eY().c()));
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
