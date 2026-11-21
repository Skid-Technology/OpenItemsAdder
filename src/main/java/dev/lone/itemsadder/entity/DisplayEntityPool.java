package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.block.BlockRealizationMode;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.item.ItemOperationsService;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.network.NativePacketsHolder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntFunction;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public final class DisplayEntityPool {
   static final int VA = 10;
   static final byte VB = 0;
   static final byte VC = 1;
   static final byte VD = 2;
   static final float VE = 1.0E-4F;
   private static final int VF = 11;
   private static final int VG = 12;
   private static final int VH = 13;
   private DisplayEntityKey[] VI = new DisplayEntityKey[0];
   private Map VJ = Map.of();
   private Object[] VK = new Object[0];
   private boolean[] VL = new boolean[0];
   private Material[] VM = new Material[0];
   private int VN;
   private Object VO;

   public void populate(World var1, Collection var2) {
      this.clear();
      if (Settings.get().customBlocksEntity) {
         boolean var3 = var2.stream().anyMatch(var0 -> var0.aps == BlockRealizationMode.ENTITY);
         if (var3) {
            PacketDisplayBridge var4 = NativePacketsHolder.PD();
            int var5 = Settings.get().blockEntityMaxDisplaysPerPlayer;
            byte var6 = 9;
            int var7 = (var5 + var6 - 1) / var6;
            this.method9316(var5 + var7, var2x -> var4.allocateKey(var1));
            float var8 = Math.max(0.1F, Settings.get().blockEntityRenderRadius / 64.0F);
            HashMap var9 = new HashMap();
            HashMap var10 = new HashMap();
            ArrayList var11 = new ArrayList();

            for (CustomBlockDefinition var13 : var2) {
               if (var13.aps == BlockRealizationMode.ENTITY) {
                  String var14 = var13.getEntityModelKey();
                  String var15 = var13.getEntityModelTextures() == null ? null : var13.getEntityModelGroupKey();
                  DisplayItemSpec var16 = new DisplayItemSpec(var14, var15, var13.anF.getRotX(), var13.anF.getRotY(), var13.anC);
                  Integer var17 = (Integer)var10.get(var16);
                  if (var17 == null) {
                     var17 = var10.size();
                     var10.put(var16, var17);
                     float var18 = 2.0F + (var15 == null ? 0.0F : 1.0E-4F);

                     for (int var19 = -1; var19 < 10; var19++) {
                        int var20 = var19;
                        String var21 = var14 + "#" + var19;
                        Material var22 = aB(var19);
                        ItemStack var23 = var9.computeIfAbsent(var21, var3x -> {
                           ItemStack var4x = new ItemStack(var22);
                           ItemOperationsService.getRegistryBridge().method3437(var4x, var14);
                           if (var20 >= 0) {
                              ItemStackUtils.method7259(var4x, var20 + 1);
                           }

                           return var4x;
                        });
                        var11.add(var4.createItemDisplayTransformed(var1, var23, var16.VR, var16.VS, var8, var18, var18, var18, 1.0F, 1.0F));
                     }

                     for (byte var26 = 1; var26 <= 2; var26++) {
                        if (var15 == null) {
                           var11.add(null);
                        } else {
                           ItemStack var27 = var9.computeIfAbsent(var15, var0 -> {
                              ItemStack var1x = new ItemStack(Material.PAPER);
                              ItemOperationsService.getRegistryBridge().method3437(var1x, var0);
                              return var1x;
                           });
                           var11.add(
                              var4.createItemDisplayTransformed(
                                 var1, var27, var16.VR + (var26 == 2 ? 90 : 0), var16.VS, var8, 6.0001F, 6.0001F, 2.0001F, 3.0F, 0.0F
                              )
                           );
                        }
                     }
                  }

                  var13.anB = var17;
               }
            }

            boolean[] var24 = new boolean[var10.size()];
            Material[] var25 = new Material[var10.size()];
            var10.forEach((var2x, var3x) -> {
               var24[var3x] = var2x.VQ != null;
               var25[var3x] = var2x.VT;
            });
            this.VJ = Map.copyOf(var9);
            this.VK = var11.toArray();
            this.VL = var24;
            this.VM = var25;
            this.VN = var10.size();
            this.VO = var4.createDisplayWithScale(var1, var8);
         }
      }
   }

   static Material aB(int var0) {
      return var0 < 0 ? Material.PAPER : Material.GLASS;
   }

   void method9316(int var1, IntFunction var2) {
      if (var1 < 1) {
         throw new IllegalArgumentException("ENTITY block display cap must be positive.");
      }

      DisplayEntityKey[] var3 = new DisplayEntityKey[var1];
      HashSet var4 = new HashSet(var1);

      for (int var5 = 0; var5 < var1; var5++) {
         DisplayEntityKey var6 = Objects.requireNonNull((DisplayEntityKey)var2.apply(var5));
         if (var6.entityId() < 0 || !var4.add(var6.entityId())) {
            throw new IllegalStateException("Invalid vanilla ENTITY block identity: " + var6.entityId());
         }

         var3[var5] = var6;
      }

      this.VI = var3;
   }

   public DisplayEntityKey keyAt(int var1) {
      return this.VI[var1];
   }

   public Object aD(int var1) {
      return this.VK[method9317(var1, -1)];
   }

   Object aE(int var1) {
      return this.VK[var1];
   }

   static int method9317(int var0, int var1) {
      if (var1 >= -1 && var1 < 10) {
         return var0 * 13 + var1 + 1;
      } else {
         throw new IllegalArgumentException("Invalid ENTITY block break stage: " + var1);
      }
   }

   static int method9318(int var0, byte var1) {
      byte var2 = switch (var1) {
         case 1 -> 11;
         case 2 -> 12;
         default -> -1;
      };
      if (var2 < 0) {
         throw new IllegalArgumentException("Not an ENTITY block group kind: " + var1);
      } else {
         return var0 * 13 + var2;
      }
   }

   public boolean hasEntry(int var1) {
      return var1 >= 0 && var1 < this.VL.length && this.VL[var1];
   }

   public Material getMaterialAt(int var1) {
      return this.VM[var1];
   }

   public Object wg() {
      return this.VO;
   }

   public int wh() {
      return this.VI.length;
   }

   public int wi() {
      return this.VN;
   }

   public int wj() {
      return this.VJ.size();
   }

   public void clear() {
      this.VI = new DisplayEntityKey[0];
      this.VJ = Map.of();
      this.VK = new Object[0];
      this.VL = new boolean[0];
      this.VM = new Material[0];
      this.VN = 0;
      this.VO = null;
   }
}
