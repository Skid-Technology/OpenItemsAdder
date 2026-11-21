package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.ItemOperationsService;
import dev.lone.itemsadder.item.ItemRegistryBridge;
import dev.lone.itemsadder.network.ConnectionRecord;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.resourcepack.HumanoidEquipmentBuilder;
import dev.lone.itemsadder.resourcepack.HumanoidEquipmentPack;
import dev.lone.itemsadder.resourcepack.PackBinding;
import dev.lone.itemsadder.resourcepack.PackDefinition;
import dev.lone.itemsadder.resourcepack.PackPlan;
import dev.lone.itemsadder.resourcepack.PackTemplateGroup;
import dev.lone.itemsadder.resourcepack.ResourcePackManager;
import dev.lone.itemsadder.util.DoubleVector3Dup2;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.Map.Entry;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

final class HumanoidEquipmentRenderer {
   private static final EquipmentLayer wT = new EquipmentLayer(null, null, EquipmentSnapshot.CU, Map.of());
   private final CustomEntityImpl wU;
   private EquipmentLayer wV = wT;
   private EquipmentLayer wW = wT;
   private HumanoidEquipmentPack wX;
   private UUID wY;
   private final Map wZ = new HashMap();

   HumanoidEquipmentRenderer(CustomEntityImpl var1) {
      this.wU = var1;
   }

   void syncEquipment(EntityEquipment var1) {
      HumanoidEquipmentSet var2 = this.wU.getModel().nX();
      ResourcePackManager var3 = ContentManager.method8084().aL.manager;
      HumanoidEquipmentPack var4 = var3.getHumanoidEquipmentPack();
      HashSet var5 = new HashSet();
      if (var2 != null && var4 != null) {
         for (EntityViewer var7 : this.wU.getViewers()) {
            ConnectionRecord var8 = var7.getConnection();
            if (var8 != null) {
               PackBinding var9 = var3.getPackBinding(var7.uz, var8.bl());
               if (var9 != null && var9.mb() == var4) {
                  var5.add(var9.SF().version());
               }
            }
         }
      }

      if (var5.isEmpty()) {
         if (this.wW != wT) {
            this.wW = wT;
            this.wU.dispatchToManager(() -> this.wV = wT);
         }
      } else {
         EquipmentSnapshot var27 = EquipmentSnapshot.capture(var1, var2);
         EquipmentLayer var28 = this.wW;
         boolean var29 = var28.xk == var4 && var28.xl == var2 && var28.xm.equals(var27);
         if (!var29 || !var28.xn.keySet().equals(var5)) {
            HashMap var30 = new HashMap();

            for (Entry var11 : var4.catalogs().entrySet()) {
               if (var5.contains(var11.getKey())) {
                  if (var29 && var28.xn.containsKey(var11.getKey())) {
                     var30.put((String)var11.getKey(), (Map)var28.xn.get(var11.getKey()));
                  } else {
                     LinkedHashMap var12 = new LinkedHashMap();
                     PackDefinition var13 = (PackDefinition)var11.getValue();

                     for (EquipmentSlotRecord var15 : var2.pf()) {
                        EquipmentItemRecord var16 = (EquipmentItemRecord)var27.slots().get(var15.ph());
                        if (var16 != null) {
                           String var17 = var15.ph().equals("legs") ? "humanoid_leggings" : "humanoid";
                           PackPlan var18 = (PackPlan)var13.equipment().get(var16.material());
                           if (var18 != null && !var18.layers().getOrDefault(var17, List.of()).isEmpty()) {
                              String var19 = HumanoidEquipmentBuilder.buildSlotKey(var2, var15);
                              Float var20 = (Float)var4.geometryScales().get(var19);
                              if (var20 != null) {
                                 PackTemplateGroup var21 = var13.resolveTemplate(var16, var17);
                                 if (var21 == null && var16.pt() != null) {
                                    var3.warnHumanoidEquipment(
                                       (String)var11.getKey() + ":" + var16.material() + ":" + var16.pt() + ":" + var16.pu(),
                                       var16.pt()
                                          + " / "
                                          + var16.pu()
                                          + " on "
                                          + var16.material()
                                          + " is unavailable for "
                                          + (String)var11.getKey()
                                          + "; showing the base armor"
                                    );
                                 }

                                 String var22 = var21 == null ? "" : (var21.colors() == null ? "direct/" : "packed/") + var21.texture();
                                 ArrayList var23 = new ArrayList(9);
                                 var23.add(Color.fromRGB(var16.customModelData() == null ? 16777215 : var16.customModelData()));

                                 for (int var24 = 0; var24 < 8; var24++) {
                                    var23.add(Color.fromRGB(var21 != null && var21.colors() != null ? (Integer)var21.colors().get(var24) : 16777215));
                                 }

                                 ItemStack var32 = new ItemStack(Material.PAPER);
                                 ItemMeta var25 = var32.getItemMeta();
                                 var25.setEnchantmentGlintOverride(var16.enchanted());
                                 var32.setItemMeta(var25);
                                 ItemRegistryBridge var26 = ItemOperationsService.getRegistryBridge();
                                 var26.method3437(var32, var19);
                                 var26.method3440(var32, List.of(var16.material(), var22));
                                 var26.method3439(var32, var23);
                                 var26.method3442(var32, List.of(var16.customModelData() != null));
                                 var12.put(
                                    var15.ph() + "/" + var15.pi(),
                                    new EquipmentEntry(var15, var16, var19, var20, NativePacketsHolder.PE().Py().wrapBukkitItem(var32))
                                 );
                              }
                           } else {
                              var3.warnHumanoidEquipment(
                                 (String)var11.getKey() + ":" + var16.material() + ":" + var17,
                                 var16.material() + " has no supported " + var17 + " layers for " + (String)var11.getKey()
                              );
                           }
                        }
                     }

                     var30.put((String)var11.getKey(), Map.copyOf(var12));
                  }
               }
            }

            EquipmentLayer var31 = new EquipmentLayer(var4, var2, var27, Map.copyOf(var30));
            this.wW = var31;
            this.wU.dispatchToManager(() -> this.wV = var31);
         }
      }
   }

   boolean lU() {
      return this.wW.xn.values().stream().anyMatch(var0 -> var0.values().stream().anyMatch(var0x -> var0x.xf.ph().equals("helmet")));
   }

   void tick() {
      EquipmentLayer var1 = this.wV;
      ResourcePackManager var2 = ContentManager.method8084().aL.manager;
      if (!this.wU.removed
         && var1.xk != null
         && this.wU.rt != null
         && this.wU.rt.getUID().equals(this.wU.getEntityUuid())
         && var1.xk == var2.getHumanoidEquipmentPack()
         && var1.xl == this.wU.getModel().nX()) {
         if (this.wX != var1.xk || !Objects.equals(this.wY, this.wU.getEntityUuid())) {
            this.close();
            this.wX = var1.xk;
            this.wY = this.wU.getEntityUuid();
         }

         HashMap var3 = new HashMap();

         for (EntityViewer var5 : this.wU.getViewers()) {
            ConnectionRecord var6 = var5.getConnection();
            if (var6 != null && !var5.uW && var5.kW()) {
               PackBinding var7 = var2.getPackBinding(var5.uz, var6.bl());
               if (var7 != null && var7.mb() == var1.xk) {
                  var3.computeIfAbsent(var7.SF().version(), var0 -> new ArrayList<>()).add(var5);
               }
            }
         }

         HashSet var18 = new HashSet();
         Iterator var19 = this.wZ.entrySet().iterator();

         while (var19.hasNext()) {
            Entry var20 = (Entry)var19.next();
            if (!var3.containsKey(var20.getKey())) {
               ((Map)var20.getValue()).values().forEach(var1x -> var1x.method8629(var18));
               var19.remove();
            }
         }

         for (Entry var22 : var3.entrySet()) {
            Map var8 = var1.xn.getOrDefault(var22.getKey(), Map.of());
            Map var9 = this.wZ.computeIfAbsent((String)var22.getKey(), var0 -> new HashMap());
            Iterator var10 = var9.entrySet().iterator();

            while (var10.hasNext()) {
               Entry var11 = (Entry)var10.next();
               EquipmentEntry var12 = (EquipmentEntry)var8.get(var11.getKey());
               ModelPartDisplay var13 = var12 == null ? null : (ModelPartDisplay)this.wU.rQ.get(var12.xf.bone());
               if (var13 == null || !var13.isVisibleToViewers() || !((EquipmentPoseState)var11.getValue()).xa.xf.equals(var12.xf)) {
                  ((EquipmentPoseState)var11.getValue()).method8629(var18);
                  var10.remove();
               }
            }

            for (Entry var24 : var8.entrySet()) {
               EquipmentEntry var25 = (EquipmentEntry)var24.getValue();
               ModelPartDisplay var14 = (ModelPartDisplay)this.wU.rQ.get(var25.xf.bone());
               if (var14 != null && var14.isVisibleToViewers()) {
                  EquipmentPoseState var15 = var9.computeIfAbsent((String)var24.getKey(), var2x -> new EquipmentPoseState(this, var25));
                  var14.getScaledPartMatrix(var15.xd).mul(var15.xc);
                  var15.xb.method3060(var15.xd, 1);
                  if (this.wU.hasRootDisplay()) {
                     var15.xb.NG();
                  }

                  Location var16 = this.wU.getBaseLocation();
                  DoubleVector3Dup2 var17 = this.wU.isRagdolling() ? this.wU.getRagdollPosition() : null;
                  var15.xb
                     .method2998(
                        var17 == null ? var16.getX() : var17.getX(), var17 == null ? var16.getY() : var17.getY(), var17 == null ? var16.getZ() : var17.getZ()
                     );
                  var15.applyPose(var25, (List)var22.getValue(), var18);
                  var15.xb.addMetadataPackets(var15.xe);
                  if (!this.wU.hasRootDisplay()) {
                     var15.xb.addEquipmentPackets(false, null, var15.xe);
                  }
               }
            }
         }

         var18.forEach(this.wU::sendPassengerPackets);
      } else {
         this.close();
      }
   }

   void method8614(EntityViewer var1, List var2) {
      this.wZ.values().forEach(var2x -> var2x.values().forEach(var2xx -> {
         if (var2xx.xe.contains(var1)) {
            var2.add(var2xx.xb.aQn);
         }
      }));
   }

   boolean method8615(EntityViewer var1) {
      for (Map var3 : this.wZ.values()) {
         for (EquipmentPoseState var5 : var3.values()) {
            if (var5.xe.contains(var1)) {
               return true;
            }
         }
      }

      return false;
   }

   void method8616(EntityViewer var1) {
      this.wZ.values().forEach(var1x -> var1x.values().forEach(var1xx -> {
         if (var1xx.xe.remove(var1)) {
            var1xx.xb.despawnFor(var1);
         }
      }));
   }

   void close() {
      if (!this.wZ.isEmpty()) {
         HashSet var1 = new HashSet();
         this.wZ.values().forEach(var1x -> var1x.values().forEach(var1xx -> var1xx.method8629(var1)));
         this.wZ.clear();
         this.wX = null;
         var1.forEach(this.wU::sendPassengerPackets);
      }
   }
}
