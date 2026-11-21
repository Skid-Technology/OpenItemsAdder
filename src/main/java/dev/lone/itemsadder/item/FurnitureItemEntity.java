package dev.lone.itemsadder.item;

import beer.devs.fastnbt.nms.Version;
import beer.devs.fastnbt.nms.nbt.NItem;
import com.google.common.util.concurrent.AtomicDouble;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.api.Events.FurniturePlaceSuccessEvent;
import dev.lone.itemsadder.api.Events.FurniturePlacedEvent;
import dev.lone.itemsadder.api.Events.FurniturePrePlaceEvent;
import dev.lone.itemsadder.block.BlockUtils;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.PlayerInteractionFacade;
import dev.lone.itemsadder.entity.ProjectileTypeHelper;
import dev.lone.itemsadder.resourcepack.ModelTransformUtils;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.BukkitRegistryLookup;
import dev.lone.itemsadder.util.EntityUtils;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.IntTableConstantsF;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.util.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import lombok.Generated;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Rotation;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.GlowItemFrame;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.Nullable;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FurnitureItemEntity extends ItemEntityAction {
   public static final String adG = "furniture";
   static NamespacedKey adH = new NamespacedKey("itemsadder", "has_edited_transformation");
   static NamespacedKey adI = new NamespacedKey("itemsadder", "legacy_frame_rotation");
   static NamespacedKey adJ = new NamespacedKey("itemsadder", "placement_face");
   static NamespacedKey adK = new NamespacedKey("itemsadder", "source_display_yaw");
   static NamespacedKey adL = new NamespacedKey("itemsadder", "source_display_pitch");
   static NamespacedKey adM = new NamespacedKey("itemsadder", "furniture_variant_item");
   final boolean adN;
   final int adO;
   final boolean adP;
   final boolean adQ;
   final boolean adR;
   final boolean adS;
   private ItemDisplayTransform adT;
   @Nullable
   String adU;
   @Nullable
   String adV;
   @Nullable
   private Transformation transformation;
   @Nullable
   private FurnitureHostType adW;
   private boolean adX;
   private boolean adY;
   private float adZ;
   private float aea;

   public FurnitureItemEntity(
      CustomItemDefinition var1, EntityType var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7, boolean var8, boolean var9, boolean var10
   ) {
      super(var1, var2, var4, var5);
      this.adN = var3;
      this.adO = var6 == -1 ? -1 : Utils.clampInt(var6, 0, 15);
      this.adP = var7;
      this.adQ = var8;
      this.adR = var9;
      this.adS = var10;
      this.adT = ItemDisplayTransform.FIXED;
   }

   public static String getName() {
      return "furniture";
   }

   public static void method398(Entity var0, ItemStack var1) {
      CustomItemDefinition var2 = ContentManager.method8084().method8149(var1);
      BlockFace var3 = method400((Entity)var0);
      if (var2 != null) {
         var1 = method399(var2, var1, var3);
      }

      switch (var0) {
         case ArmorStand var6:
            var6.setHelmet(var1.clone());
            break;
         case ItemFrame var7:
            var7.setItem(var1.clone(), false);
            break;
         case ItemDisplay var8:
            var8.setItemStack(var1.clone());
            break;
         case null:
         default:
            throw new IllegalArgumentException("Entity is not ArmorStand, ItemFrame or ItemDisplay.");
      }
   }

   private static ItemStack method399(CustomItemDefinition var0, ItemStack var1, BlockFace var2) {
      ItemStack var3 = var1.clone();
      if (Version.isAtLeast(Version.v1_21_3) && (var0.anZ != null || var0.DS().jI("resource.model_path"))) {
         String var4 = ModelTransformUtils.furnitureModelId(var0, var2);
         if (var4 != null) {
            ItemOperationsService.getRegistryBridge().method3437(var3, var4);
            ItemMeta var5 = var3.getItemMeta();
            var5.getPersistentDataContainer().set(adM, PersistentDataType.BYTE, (byte)1);
            var3.setItemMeta(var5);
         } else {
            ItemMeta var8 = var3.getItemMeta();
            if (var8 != null && var8.getPersistentDataContainer().has(adM)) {
               if (var0.anZ != null) {
                  ItemOperationsService.getRegistryBridge().method3437(var3, var0.anZ.Ei());
                  var8 = var3.getItemMeta();
               } else {
                  try {
                     ItemMeta.class.getMethod("setItemModel", NamespacedKey.class).invoke(var8, null);
                  } catch (ReflectiveOperationException var7) {
                     throw new IllegalStateException("Cannot clear generated furniture item model", var7);
                  }
               }

               var8.getPersistentDataContainer().remove(adM);
               var3.setItemMeta(var8);
            }
         }
      }

      return var3;
   }

   private static BlockFace method400(Entity var0) {
      if (var0 instanceof ItemFrame var8) {
         return var8.getFacing();
      } else {
         BlockFace var1 = (BlockFace)BukkitRegistryLookup.enumOrDefault(
            (String)var0.getPersistentDataContainer().get(adJ, PersistentDataType.STRING), BlockFace.class, null
         );
         if (var1 != null) {
            return var1;
         }

         if (var0 instanceof ItemDisplay && Math.abs(var0.getLocation().getPitch()) >= 45.0F) {
            int var9 = Math.floorMod(Math.round(var0.getLocation().getYaw() / 90.0F), 4);

            return switch (var9) {
               case 0 -> BlockFace.SOUTH;
               case 1 -> BlockFace.WEST;
               case 2 -> BlockFace.NORTH;
               default -> BlockFace.EAST;
            };
         } else {
            Block var2 = var0.getLocation().getBlock();
            BlockFace var3 = null;

            for (BlockFace var7 : new BlockFace[]{BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST}) {
               if (var2.getRelative(var7.getOppositeFace()).getType().isSolid()) {
                  if (var3 != null) {
                     return null;
                  }

                  var3 = var7;
               }
            }

            return var3;
         }
      }
   }

   public static boolean method401(CustomItemDefinition var0, CustomItemDefinition var1, Entity var2) {
      return method402(var0, var1, var2, null);
   }

   public static boolean method402(CustomItemDefinition var0, CustomItemDefinition var1, Entity var2, @Nullable Player var3) {
      FurnitureItemEntity var4 = (FurnitureItemEntity)var0.aoh.getAction("furniture");
      FurnitureItemEntity var5 = (FurnitureItemEntity)var1.aoh.getAction("furniture");
      if (var4 != null && var5 != null) {
         var5.method370(var2);
         if (var3 != null) {
            float var6 = method403(var3.getLocation().getYaw(), var4.At(), var4.adQ);
            if (var2 instanceof ItemFrame var7) {
               Rotation var8 = method424(var7) == FurnitureMountFace.WALL
                  ? BlockFaceUtils.yawToRotation(BlockFaceUtils.faceToRotationDegrees(var4.adQ ? BlockFace.SOUTH : BlockFace.NORTH))
                  : BlockFaceUtils.yawToRotation(var6);
               var7.setRotation(var8);
               var7.getPersistentDataContainer().set(adI, PersistentDataType.STRING, var8.name());
            } else {
               var2.setRotation(var6, var2.getLocation().getPitch());
               if (var2 instanceof ItemDisplay) {
                  var2.getPersistentDataContainer().set(adK, PersistentDataType.FLOAT, var6);
               }
            }
         }

         var4.method361(var2, var0);
         var4.method367(var2);
         if (var4.method406()) {
            SchedulerUtils.runLaterIf(() -> var4.method368(var2), 5L, var5.method406());
         }

         BlockUtils.clearBlockState(var2.getLocation().getBlock());
         BlockUtils.markBlockState(var2.getLocation().getBlock(), var4.Az());
         ItemStack var9 = var0.Ds();
         var9 = method416(var9);
         method398(var2, var9);
         return true;
      } else {
         if (var4 == null) {
            Log.warn("Cannot replace furniture. New item is not a furniture: " + var0.getNamespacedID());
         }

         if (var5 == null) {
            Log.warn("Cannot replace furniture. Old item is not a furniture: " + var1.getNamespacedID());
         }

         return false;
      }
   }

   static float method403(float var0, boolean var1, boolean var2) {
      float var3 = var1 ? BlockFaceUtils.yawToRotationDegrees(var0, false) : var0;
      return var2 ? var3 + 180.0F : var3;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static void method404(Entity var0) {
      ItemStack var1 = A(var0);
      if (CustomItemDefinition.isCustomItem(var1)) {
         CustomItemDefinition var2 = ContentManager.method8084().method8149(var1);
         if (var2 != null) {
            FurnitureItemEntity var3 = (FurnitureItemEntity)var2.aoh.getAction("furniture");
            if (var3 != null) {
               String var4 = var2.DS()
                  .bc(
                     "behaviours.furniture.display_transformation.source_entity",
                     var2.DS().bc("behaviours.furniture.display_transformation.adjust_legacy_model", "item_frame")
                  );
               String var5 = var2.DH() + ".behaviours.furniture.display_transformation.source_entity";
               if (var3.acW == EntityType.ITEM_DISPLAY
                  && var3.transformation == null
                  && !var2.lH().contains(var5)
                  && !var2.lH().contains(var2.DH() + ".behaviours.furniture.display_transformation.adjust_legacy_model")
                  && (var0 instanceof ItemFrame || var0 instanceof ArmorStand)) {
                  String var6 = var0 instanceof ItemFrame ? "item_frame" : (((ArmorStand)var0).isSmall() ? "small_armor_stand" : "armor_stand");
                  var3.aG(true);
                  if (var0 instanceof ItemFrame) {
                     var3.AF();
                  } else {
                     var3.aF(((ArmorStand)var0).isSmall());
                  }

                  var2.lH().method7048(var5, var6);
                  var2.lH().abN();
               }

               if (var3.acW == var0.getType()) {
                  if (var0 instanceof ArmorStand var13 && var13.isSmall() != var3.Au()) {
                     var13.setSmall(var3.Au());
                     method398(var13, var2.Ds());
                  }

                  if (var3.acW == EntityType.ITEM_DISPLAY) {
                     if (var3.adY) {
                        ItemDisplay var14 = (ItemDisplay)var0;
                        Boolean var16 = (Boolean)var14.getPersistentDataContainer().get(adH, PersistentDataType.BOOLEAN);
                        if (var16 == null || !var16) {
                           if (var3.transformation != null) {
                              var14.setTransformation(var3.transformation);
                           }

                           if (var3.adT != null) {
                              var14.setItemDisplayTransform(var3.adT);
                           }
                        }
                     }

                     var3.method367(var0);
                     if (var3.adP) {
                        var3.method368(var0);
                     }
                  }
               } else {
                  if (var3.adb != 0.0F && var3.adP && var3.acW == EntityType.ITEM_DISPLAY) {
                     BoundingBox var11 = var0.getBoundingBox();

                     for (Location var8 : method426(
                        var0.getWorld(), var11.getMinX(), var11.getMaxX(), var11.getMinY(), var11.getMaxY(), var11.getMinZ(), var11.getMaxZ()
                     )) {
                        if (var8.getBlock().getType() == Material.BARRIER) {
                           var8.getBlock().setType(Material.AIR);
                        }
                     }
                  }

                  Location var12 = var0.getLocation();
                  if (var0 instanceof ItemFrame var15) {
                     BlockFace var17 = var15.getFacing();
                     switch (IntTableConstantsF.SWITCH_MAP_A[var17.ordinal()]) {
                        case 1:
                           var12.setZ(var12.getZ() - 0.5);
                           var12.setY(var12.getY() - 0.5);
                           break;
                        case 2:
                           var12.setZ(var12.getZ() + 0.5);
                           var12.setY(var12.getY() - 0.5);
                           break;
                        case 3:
                           var12.setX(var12.getX() + 0.5);
                           var12.setY(var12.getY() - 0.5);
                           break;
                        case 4:
                           var12.setX(var12.getX() - 0.5);
                           var12.setY(var12.getY() - 0.5);
                           break;
                        case 5:
                           var12.setYaw(0.0F);
                           var12.setPitch(0.0F);
                           break;
                        case 6:
                           var12.setY(var12.getY() - 1.0);
                     }

                     var12.subtract(var17.getModX() / 32.0, var17.getModY() / 32.0, var17.getModZ() / 32.0);
                     switch (IntTableConstantsF.SWITCH_MAP_B[var15.getRotation().ordinal()]) {
                        case 1:
                           var12.setYaw(45.0F);
                           break;
                        case 2:
                           var12.setYaw(90.0F);
                           break;
                        case 3:
                           var12.setYaw(135.0F);
                           break;
                        case 4:
                           var12.setYaw(180.0F);
                           break;
                        case 5:
                           var12.setYaw(-135.0F);
                           break;
                        case 6:
                           var12.setYaw(-90.0F);
                           break;
                        case 7:
                           var12.setYaw(-45.0F);
                     }

                     if (var3.acW == EntityType.ARMOR_STAND || var3.acW == EntityType.ITEM_DISPLAY && !"item_frame".equals(var4)) {
                        var12.setYaw(var12.getYaw() + 180.0F);
                     }

                     if (var3.acW == EntityType.ARMOR_STAND && var17 != BlockFace.UP && var17 != BlockFace.DOWN) {
                        var12.setYaw(0.0F);
                     }

                     if (var3.acW == EntityType.ITEM_DISPLAY && "item_display".equals(var4)) {
                        var12.setYaw((Float)Optional.ofNullable((Float)var0.getPersistentDataContainer().get(adK, PersistentDataType.FLOAT)).orElse(0.0F));
                        var12.setPitch((Float)Optional.ofNullable((Float)var0.getPersistentDataContainer().get(adL, PersistentDataType.FLOAT)).orElse(0.0F));
                     }

                     var3.method415(null, var12, var1, null, var17, var4x -> {
                        var4x.getPersistentDataContainer().set(adI, PersistentDataType.STRING, var15.getRotation().name());
                        var4x.getPersistentDataContainer().set(adJ, PersistentDataType.STRING, var17.name());
                        method405(var0, var4x, var4);
                        if (var4x instanceof ItemFrame var5x) {
                           var5x.setRotation(var15.getRotation());
                        }

                        var0.remove();
                     });
                  } else {
                     BlockFace var18 = method400(var0);
                     if (var0 instanceof ItemDisplay && var3.acW == EntityType.ARMOR_STAND && "item_display".equals(var4) && var18 != null) {
                        var12.setYaw(0.0F);
                        var12.setPitch(0.0F);
                        if (var18 == BlockFace.DOWN) {
                           var12.subtract(0.0, 1.0, 0.0);
                        } else if (var18 != BlockFace.UP) {
                           var12.add(var18.getModX() * 0.5, -0.5, var18.getModZ() * 0.5);
                        }
                     }

                     if (var3.acW == EntityType.ITEM_DISPLAY && "item_display".equals(var4)) {
                        Float var9 = (Float)var0.getPersistentDataContainer().get(adK, PersistentDataType.FLOAT);
                        Float var10 = (Float)var0.getPersistentDataContainer().get(adL, PersistentDataType.FLOAT);
                        if (var9 != null) {
                           var12.setYaw(var9);
                        }

                        if (var10 != null) {
                           var12.setPitch(var10);
                        }
                     }

                     var3.method415(null, var12, var1, null, var3.acW == EntityType.ITEM_DISPLAY && !"item_display".equals(var4) ? null : var18, var3x -> {
                        if (var18 != null) {
                           var3x.getPersistentDataContainer().set(adJ, PersistentDataType.STRING, var18.name());
                        }

                        method405(var0, var3x, var4);
                        String var4x = (String)var0.getPersistentDataContainer().get(adI, PersistentDataType.STRING);
                        if (var4x != null) {
                           var3x.getPersistentDataContainer().set(adI, PersistentDataType.STRING, var4x);
                           if (var3x instanceof ItemFrame var5x) {
                              Rotation var6x = (Rotation)BukkitRegistryLookup.enumOrDefault(var4x, Rotation.class, null);
                              if (var6x != null) {
                                 var5x.setRotation(var6x);
                              }
                           }
                        }

                        var0.remove();
                     });
                  }
               }
            }
         }
      }
   }

   private static void method405(Entity var0, Entity var1, String var2) {
      if ("item_display".equals(var2)) {
         Float var3 = var0 instanceof ItemDisplay ? var0.getLocation().getYaw() : (Float)var0.getPersistentDataContainer().get(adK, PersistentDataType.FLOAT);
         Float var4 = var0 instanceof ItemDisplay ? var0.getLocation().getPitch() : (Float)var0.getPersistentDataContainer().get(adL, PersistentDataType.FLOAT);
         if (var3 != null) {
            var1.getPersistentDataContainer().set(adK, PersistentDataType.FLOAT, var3);
         }

         if (var4 != null) {
            var1.getPersistentDataContainer().set(adL, PersistentDataType.FLOAT, var4);
         }
      }
   }

   public boolean Ay() {
      return this.adN;
   }

   public int Az() {
      return this.adO;
   }

   public boolean method406() {
      return this.adP;
   }

   public boolean AB() {
      return this.adR;
   }

   public boolean AC() {
      return this.adS;
   }

   @Nullable
   public String AD() {
      return this.adU;
   }

   public void bZ(String var1) {
      this.adU = var1;
   }

   @Nullable
   public String AE() {
      return this.adV;
   }

   public void ca(String var1) {
      this.adV = var1;
   }

   public void setTransformation(Transformation transformation) {
      this.transformation = transformation;
   }

   public void method407(ItemDisplayTransform var1) {
      this.adT = var1;
   }

   public void method408(@Nullable FurnitureHostType var1) {
      this.adW = var1;
   }

   public void AF() {
      this.method407(ItemDisplayTransform.FIXED);
      this.setTransformation(
         new Transformation(
            new Vector3f(), new Quaternionf(), new Vector3f(0.5F), new Quaternionf(new AxisAngle4f((float)Math.toRadians(270.0), 1.0F, 0.0F, 0.0F))
         )
      );
      this.method408(FurnitureHostType.ITEM_FRAME);
   }

   public void aF(boolean var1) {
      this.method407(ItemDisplayTransform.HEAD);
      this.setTransformation(
         new Transformation(
            new Vector3f(this.adb == 0.5F ? -1.0F : 0.0F, var1 ? 0.890625F : 1.6875F, 0.0F),
            new Quaternionf(),
            new Vector3f(var1 ? 0.46875F : 0.625F),
            new Quaternionf()
         )
      );
      this.method408(var1 ? FurnitureHostType.SMALL_ARMOR_STAND : FurnitureHostType.ARMOR_STAND);
   }

   @Override
   public void method367(Entity var1) {
      if (var1 instanceof ItemFrame) {
         this.method423(var1);
      } else if (var1 instanceof ItemDisplay var2) {
         this.method422(var2);
      } else {
         this.method421(var1);
      }
   }

   @Override
   public void method361(Entity var1, CustomItemDefinition var2) {
      super.method361(var1, var2);
      var1.getPersistentDataContainer().set(acV, PersistentDataType.STRING, "furniture");
      var1.setCustomName("ItemsAdder_furniture");
      var1.setCustomNameVisible(false);
   }

   public static boolean method409(Entity var0) {
      String var1 = (String)var0.getPersistentDataContainer().get(acV, PersistentDataType.STRING);
      return var1 != null && var1.equals("furniture");
   }

   @Nullable
   public Entity method410(ItemStack var1, Block var2) {
      return this.method413(var1, null, var2, null, null, null, null);
   }

   @Nullable
   public Entity method411(ItemStack var1, Location var2) {
      return this.method413(var1, var2, null, null, null, null, null);
   }

   @Nullable
   public Entity method412(
      ItemStack var1, Block var2, @Nullable EquipmentSlot var3, @Nullable Player var4, @Nullable ItemListenerBase var5, @Nullable BlockFace var6
   ) {
      return this.method413(var1, null, var2, var3, var4, var5, var6);
   }

   @Nullable
   public Entity method413(
      ItemStack var1,
      @Nullable Location var2,
      @Nullable Block var3,
      @Nullable EquipmentSlot var4,
      @Nullable Player var5,
      @Nullable ItemListenerBase var6,
      @Nullable BlockFace var7
   ) {
      if (var5 != null && var6 != null && var6.aY(var5)) {
         return null;
      }

      if (var7 != null) {
         if (var7.equals(BlockFace.DOWN)) {
            if (!this.ade) {
               return null;
            }
         } else if (var7.equals(BlockFace.UP)) {
            if (!this.adf) {
               return null;
            }
         } else if (!this.add) {
            if (var3 == null || !var3.getRelative(BlockFace.DOWN).getType().isSolid()) {
               return null;
            }

            if (!this.adf) {
               return null;
            }

            var7 = BlockFace.UP;
         }
      } else {
         var7 = BlockFace.UP;
      }

      Location var8;
      if (var3 == null) {
         var8 = var2.clone();
      } else {
         var8 = var3.getLocation();
         var8.setX(var8.getX() + 0.5);
         var8.setY(var8.getY());
         var8.setZ(var8.getZ() + 0.5);
      }

      if (!EventRegistrar.callEvent(new FurniturePrePlaceEvent(var5, this, var8))) {
         return null;
      }

      int var9 = Main.config.getInt("entities.furniture.max_per_chunk");
      long var10 = Arrays.stream(var8.getChunk().getEntities()).filter(var0 -> method409(var0) || ItemThrowAction.method397(var0)).count();
      if (var9 > 0 && var10 + 1L > var9) {
         if (var5 != null) {
            Log.sendPrefixed(var5, Main.aFi.lj("max-furnitures-vehicles-reached").replace("{value}", var9 + ""));
         }

         return null;
      } else {
         if (var5 != null) {
            var8.setYaw(method403(var5.getLocation().getYaw(), this.At(), this.adQ));
         }

         AtomicBoolean var12 = new AtomicBoolean(false);
         Entity var13 = this.method415(var5, var8, var1, var3, var7, var3x -> {
            FurniturePlacedEvent var4x = new FurniturePlacedEvent(var5, var3x, this);
            if (!EventRegistrar.callEvent(var4x)) {
               var3x.remove();
               this.method370(var3x);
               var12.set(true);
            } else {
               EventRegistrar.callEvent(new FurniturePlaceSuccessEvent(var5, var3x, this));
            }
         });
         if (var12.get()) {
            return null;
         }

         if (var13 != null) {
            BlockUtils.markBlockState(var8.getBlock(), this.adO);
            if (var5 != null) {
               this.acY.apR.play(var5, var8);
               PlayerInteractionFacade.method5310(var5, var4);
               if (var5.getGameMode() != GameMode.CREATIVE) {
                  var1.setAmount(var1.getAmount() - 1);
               }

               if (var6 != null) {
                  var6.aZ(var5);
               }
            }
         }

         return var13;
      }
   }

   @Nullable
   private Entity method414(Location var1, ItemStack var2, Consumer var3) {
      return this.method415(null, var1, var2, null, null, var3);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Nullable
   private Entity method415(@Nullable Player var1, Location var2, ItemStack var3, @Nullable Block var4, @Nullable BlockFace var5, Consumer var6) {
      Transformation var7 = null;
      boolean var8 = false;
      Entity var9 = null;
      if (this.acW == EntityType.ARMOR_STAND) {
         var9 = var2.getWorld().spawn(var2, ArmorStand.class, var4x -> {
            var4x.setVisible(false);
            var4x.setAI(true);
            var4x.setRemoveWhenFarAway(false);
            var4x.setCanPickupItems(false);
            var4x.setInvulnerable(false);
            var4x.setSilent(true);
            var4x.setSmall(this.Au());
            var4x.setGravity(this.Ay());
            var4x.setCollidable(true);
            ProjectileTypeHelper.method3217(var4x, false);
            ItemStack var5x;
            if (this.aeT.getType() == var3.getType()) {
               var5x = var3.clone();
            } else {
               var5x = this.aeT.Ds();
            }

            var5x.setAmount(1);
            var5x = method399(this.aeT, var5x, var5);
            ItemStack var6x = var5x;
            SchedulerUtils.runLaterSync(() -> {
               var4x.setHelmet(var6x);
               var6.accept(var4x);
            }, 2L);
         });
      } else if (ProjectileTypeHelper.isAllowedEntityType(this.acW)) {
         ItemStack var10 = method399(this.aeT, method416(var3), var5);

         try {
            Class<GlowItemFrame> var11 = this.acW == EntityType.GLOW_ITEM_FRAME ? GlowItemFrame.class : ItemFrame.class;
            var9 = var2.getWorld()
               .spawn(
                  var2,
                  var11,
                  var5x -> {
                     var5x.setItem(var10, false);
                     var5x.setVisible(false);
                     var5x.setFixed(true);
                     if (var5 != null) {
                        var5x.setFacingDirection(var5);
                     }

                     FurnitureMountFace var6x = method424(var5x);
                     switch (var6x) {
                        case CEILING:
                        case FLOOR:
                           var5x.setRotation(
                              this.At()
                                 ? BlockFaceUtils.yawToRotation(BlockFaceUtils.yawToRotationDegrees(var2.getYaw(), false))
                                 : BlockFaceUtils.yawToRotation(var2.getYaw())
                           );
                           break;
                        case WALL:
                           var5x.setRotation(BlockFaceUtils.yawToRotation(BlockFaceUtils.faceToRotationDegrees(this.adQ ? BlockFace.SOUTH : BlockFace.NORTH)));
                     }

                     var6.accept(var5x);
                  }
               );
         } catch (IllegalArgumentException var12) {
            return null;
         }
      } else if (this.acW == EntityType.ITEM_DISPLAY) {
         if (this.transformation != null) {
            var7 = EntityUtils.copyTransformation(this.transformation);
         }

         if (var5 != null) {
            double var13 = 0.001;
            if (this.adf && var5 == BlockFace.UP) {
               var2.setY(var2.getY() + var13);
            }

            if (this.ade
               && (!this.adX || this.adW != FurnitureHostType.ARMOR_STAND && this.adW != FurnitureHostType.SMALL_ARMOR_STAND)
               && var5 == BlockFace.DOWN) {
               var2.setY(var2.getY() - var13);
               var2.add(0.0, 1.0, 0.0);
               if (var7 == null) {
                  var7 = EntityUtils.identityTransformation();
               }

               if (this.adX && this.adW == FurnitureHostType.ITEM_FRAME) {
                  var7.getLeftRotation().set(new Quaternionf(new AxisAngle4f((float)Math.toRadians(90.0), 1.0F, 0.0F, 0.0F)));
               } else {
                  var7.getLeftRotation().set(new Quaternionf(new AxisAngle4f((float)Math.toRadians(180.0), 0.0F, 0.0F, 1.0F)));
               }

               if (this.adW == FurnitureHostType.ARMOR_STAND) {
                  var7.getTranslation().add(0.0F, -2.45F, 0.0F);
               } else if (this.adW == FurnitureHostType.SMALL_ARMOR_STAND) {
                  var7.getTranslation().add(0.0F, -0.82F, 0.0F);
               }

               var8 = true;
               if (this.adX && this.adW == FurnitureHostType.ITEM_FRAME && var4 != null) {
                  var2.setYaw(var2.getYaw() + 180.0F);
                  var2.setPitch(90.0F);
               }
            }

            if (this.add && (!this.adX || this.adW != FurnitureHostType.ARMOR_STAND && this.adW != FurnitureHostType.SMALL_ARMOR_STAND)) {
               switch (IntTableConstantsF.SWITCH_MAP_A[var5.ordinal()]) {
                  case 1:
                     var2.setZ(var2.getZ() + 0.5 - var13);
                     var2.setY(var2.getY() + 0.5);
                     var2.setYaw(180.0F);
                     var2.setPitch(90.0F);
                     break;
                  case 2:
                     var2.setZ(var2.getZ() - 0.5 + var13);
                     var2.setY(var2.getY() + 0.5);
                     var2.setYaw(0.0F);
                     var2.setPitch(90.0F);
                     break;
                  case 3:
                     var2.setX(var2.getX() - 0.5 + var13);
                     var2.setY(var2.getY() + 0.5);
                     var2.setYaw(-90.0F);
                     var2.setPitch(90.0F);
                     break;
                  case 4:
                     var2.setX(var2.getX() + 0.5 - var13);
                     var2.setY(var2.getY() + 0.5);
                     var2.setYaw(90.0F);
                     var2.setPitch(90.0F);
               }
            } else {
               switch (IntTableConstantsF.SWITCH_MAP_A[var5.ordinal()]) {
                  case 1:
                  case 2:
                  case 3:
                  case 4:
                     var2.setY(var2.getY() + var13);
               }
            }
         }

         if (this.adX && var4 != null && var5 == BlockFace.UP && this.adW == FurnitureHostType.ITEM_FRAME) {
            var2.setYaw(var2.getYaw() + 180.0F);
         }

         Transformation var14 = var7;
         boolean var16 = var8;
         var9 = var2.getWorld().spawn(var2, ItemDisplay.class, var6x -> {
            var6x.setInvulnerable(false);
            var6x.setSilent(true);
            var6x.setGravity(this.Ay());
            var6x.setDisplayWidth(this.adZ);
            var6x.setDisplayHeight(this.aea);
            ItemStack var7x;
            if (this.aeT.getType() == var3.getType()) {
               var7x = var3.clone();
            } else {
               var7x = this.aeT.Ds();
            }

            var7x.setAmount(1);
            var6x.setItemStack(method399(this.aeT, var7x, var5));
            if (var14 != null) {
               var6x.setTransformation(var14);
            }

            if (this.adT != null) {
               var6x.setItemDisplayTransform(this.adT);
            }

            if (var16) {
               var6x.getPersistentDataContainer().set(adH, PersistentDataType.BOOLEAN, true);
            }

            var6.accept(var6x);
         });
      }

      if (var9 != null) {
         if (var5 != null) {
            var9.getPersistentDataContainer().set(adJ, PersistentDataType.STRING, var5.name());
         }

         this.method367(var9);
         if (this.adP && var4 != null) {
            if (this.aeT.getType().isSolid()) {
               Entity var15 = var9;
               SchedulerUtils.runLaterSync(() -> this.method369(var15, var1), 1L);
            } else {
               this.method369(var9, var1);
            }
         }

         this.method361(var9, this.aeT);
         if (var1 != null && var1.hasMetadata("DrawFurnitureHitbox")) {
            method417(var9, var1);
         }
      }

      return var9;
   }

   public static ItemStack method416(ItemStack var0) {
      ItemStack var1 = var0.clone();
      var1.setAmount(1);
      NItem var2 = new NItem(var1);
      String var3 = ItemStackUtils.ar(var1);
      if (var3 != null) {
         var2.setString("IAOrigName", var3);
         var2.save();
         ItemMeta var4 = var1.getItemMeta();
         var4.setDisplayName(null);
         var1.setItemMeta(var4);
      }

      return var1;
   }

   public static void method417(Entity var0, Player var1) {
      method418(var0, var1, true);
   }

   public static void method418(Entity var0, Player var1, boolean var2) {
      AtomicDouble var3 = new AtomicDouble();
      SchedulerUtils.scheduleRepeating(
         var4 -> {
            if (var0.isDead()
               || !var0.isValid()
               || var2 && !var1.hasMetadata("DrawFurnitureHitbox")
               || !var1.isOnline()
               || !var1.getWorld().equals(var0.getWorld())
               || var3.get() >= 50.0) {
               var4.cancel();
            }

            method425(
               var0.getWorld(),
               var0.getBoundingBox().getMinX(),
               var0.getBoundingBox().getMaxX(),
               var0.getBoundingBox().getMinY(),
               var0.getBoundingBox().getMaxY(),
               var0.getBoundingBox().getMinZ(),
               var0.getBoundingBox().getMaxZ()
            );
            var3.set(var3.get() + 1.0);
         },
         0L,
         2L
      );
   }

   public static void method419(BoundingBox var0, Player var1, boolean var2) {
      AtomicDouble var3 = new AtomicDouble();
      SchedulerUtils.scheduleRepeating(var4 -> {
         if (!var1.isOnline() || var2 && !var1.hasMetadata("DrawFurnitureHitbox") || !var1.getWorld().equals(var1.getWorld()) || var3.get() >= 50.0) {
            var4.cancel();
         }

         method425(var1.getWorld(), var0.getMinX(), var0.getMaxX(), var0.getMinY(), var0.getMaxY(), var0.getMinZ(), var0.getMaxZ());
         var3.set(var3.get() + 1.0);
      }, 0L, 2L);
   }

   public static void method420(Entity var0) {
      FurnitureItemEntity var1 = (FurnitureItemEntity)c(var0, "furniture");
      if (var1 != null) {
         var1.method367(var0);
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private void method421(Entity var1) {
      Location var2 = var1.getLocation();
      BlockFace var3 = BlockFaceUtils.yawToFace(var2.getYaw());
      BoundingBox var4 = null;
      float var5 = this.adb;
      switch (IntTableConstantsF.SWITCH_MAP_A[var3.ordinal()]) {
         case 1:
         case 7:
         case 8:
            var4 = new BoundingBox(
               var2.getX() - this.xS / 2.0F + var5,
               var2.getY() + this.adc,
               var2.getZ() - this.acZ / 2.0F + this.ada,
               var2.getX() + this.xS / 2.0F + var5,
               var2.getY() + this.ut + this.adc,
               var2.getZ() + this.acZ / 2.0F + this.ada
            );
            break;
         case 2:
         case 9:
         case 10:
            var4 = new BoundingBox(
               var2.getX() - this.xS / 2.0F - var5,
               var2.getY() + this.adc,
               var2.getZ() - this.acZ / 2.0F - this.ada,
               var2.getX() + this.xS / 2.0F - var5,
               var2.getY() + this.ut + this.adc,
               var2.getZ() + this.acZ / 2.0F - this.ada
            );
            break;
         case 3:
            var4 = new BoundingBox(
               var2.getX() - this.acZ / 2.0F - this.ada,
               var2.getY() + this.adc,
               var2.getZ() - this.xS / 2.0F + var5,
               var2.getX() + this.acZ / 2.0F - this.ada,
               var2.getY() + this.ut + this.adc,
               var2.getZ() + this.xS / 2.0F + var5
            );
            break;
         case 4:
            var4 = new BoundingBox(
               var2.getX() - this.acZ / 2.0F + this.ada,
               var2.getY() + this.adc,
               var2.getZ() - this.xS / 2.0F - var5,
               var2.getX() + this.acZ / 2.0F + this.ada,
               var2.getY() + this.ut + this.adc,
               var2.getZ() + this.xS / 2.0F - var5
            );
         case 5:
         case 6:
      }

      if (var4 != null) {
         ProjectileTypeHelper.setBounds(var1, var4);
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private void method422(ItemDisplay var1) {
      Location var2 = var1.getLocation();
      BlockFace var3 = BlockFaceUtils.yawToFace(var2.getYaw());
      BoundingBox var4 = null;
      float var5 = -this.adb;
      boolean var6 = var1.getTransformation().getLeftRotation().equals(new Quaternionf(new AxisAngle4f((float)Math.toRadians(180.0), 0.0F, 0.0F, 1.0F)));
      boolean var7 = var2.getPitch() == 90.0F;
      if (var6) {
         switch (IntTableConstantsF.SWITCH_MAP_A[var3.ordinal()]) {
            case 1:
            case 7:
            case 8:
               var4 = new BoundingBox(
                  var2.getX() - this.xS / 2.0F + var5,
                  var2.getY() + this.adc,
                  var2.getZ() - this.acZ / 2.0F + this.ada,
                  var2.getX() + this.xS / 2.0F + var5,
                  var2.getY() + this.ut + this.adc,
                  var2.getZ() + this.acZ / 2.0F + this.ada
               );
               if (this.xS == 2.0F) {
                  var4.shift(0.5, 0.0, 0.0);
               }
               break;
            case 2:
            case 9:
            case 10:
               var4 = new BoundingBox(
                  var2.getX() - this.xS / 2.0F - var5,
                  var2.getY() + this.adc,
                  var2.getZ() - this.acZ / 2.0F - this.ada,
                  var2.getX() + this.xS / 2.0F - var5,
                  var2.getY() + this.ut + this.adc,
                  var2.getZ() + this.acZ / 2.0F - this.ada
               );
               if (this.xS == 2.0F) {
                  var4.shift(-0.5, 0.0, 0.0);
               }
               break;
            case 3:
               var4 = new BoundingBox(
                  var2.getX() - this.acZ / 2.0F - this.ada,
                  var2.getY() + this.adc,
                  var2.getZ() - this.xS / 2.0F + var5,
                  var2.getX() + this.acZ / 2.0F - this.ada,
                  var2.getY() + this.ut + this.adc,
                  var2.getZ() + this.xS / 2.0F + var5
               );
               if (this.xS == 2.0F) {
                  var4.shift(0.0, 0.0, 0.5);
               }
               break;
            case 4:
               var4 = new BoundingBox(
                  var2.getX() - this.acZ / 2.0F + this.ada,
                  var2.getY() + this.adc,
                  var2.getZ() - this.xS / 2.0F - var5,
                  var2.getX() + this.acZ / 2.0F + this.ada,
                  var2.getY() + this.ut + this.adc,
                  var2.getZ() + this.xS / 2.0F - var5
               );
               if (this.xS == 2.0F) {
                  var4.shift(0.0, 0.0, -0.5);
               }
            case 5:
            case 6:
         }

         if (this.ut >= 2.0F) {
            var4.shift(0.0, -(this.ut / 2.0F), 0.0);
         }
      } else if (var7) {
         switch (IntTableConstantsF.SWITCH_MAP_A[var3.ordinal()]) {
            case 1:
            case 7:
            case 8:
               var4 = new BoundingBox(
                  var2.getX() - this.xS / 2.0F + var5,
                  var2.getY() + this.adc - 0.5,
                  var2.getZ() - this.acZ / 2.0F + this.ada + 0.5,
                  var2.getX() + this.xS / 2.0F + var5,
                  var2.getY() + this.ut + this.adc - 0.5,
                  var2.getZ() + this.acZ / 2.0F + this.ada + 0.5
               );
               if (this.xS == 2.0F) {
                  var4.shift(-0.5, 0.0, 0.0);
               }
               break;
            case 2:
            case 9:
            case 10:
               var4 = new BoundingBox(
                  var2.getX() - this.xS / 2.0F - var5,
                  var2.getY() + this.adc - 0.5,
                  var2.getZ() - this.acZ / 2.0F - this.ada - 0.5,
                  var2.getX() + this.xS / 2.0F - var5,
                  var2.getY() + this.ut + this.adc - 0.5,
                  var2.getZ() + this.acZ / 2.0F - this.ada - 0.5
               );
               if (this.xS == 2.0F) {
                  var4.shift(0.5, 0.0, 0.0);
               }
               break;
            case 3:
               var4 = new BoundingBox(
                  var2.getX() - this.acZ / 2.0F - this.ada - 0.5,
                  var2.getY() + this.adc - 0.5,
                  var2.getZ() - this.xS / 2.0F + var5,
                  var2.getX() + this.acZ / 2.0F - this.ada - 0.5,
                  var2.getY() + this.ut + this.adc - 0.5,
                  var2.getZ() + this.xS / 2.0F + var5
               );
               if (this.xS == 2.0F) {
                  var4.shift(0.0, 0.0, -0.5);
               }
               break;
            case 4:
               var4 = new BoundingBox(
                  var2.getX() - this.acZ / 2.0F + this.ada + 0.5,
                  var2.getY() + this.adc - 0.5,
                  var2.getZ() - this.xS / 2.0F - var5,
                  var2.getX() + this.acZ / 2.0F + this.ada + 0.5,
                  var2.getY() + this.ut + this.adc - 0.5,
                  var2.getZ() + this.xS / 2.0F - var5
               );
               if (this.xS == 2.0F) {
                  var4.shift(0.0, 0.0, 0.5);
               }
            case 5:
            case 6:
         }
      } else {
         switch (IntTableConstantsF.SWITCH_MAP_A[var3.ordinal()]) {
            case 1:
            case 7:
            case 8:
               var4 = new BoundingBox(
                  var2.getX() - this.xS / 2.0F + var5,
                  var2.getY() + this.adc,
                  var2.getZ() - this.acZ / 2.0F + this.ada,
                  var2.getX() + this.xS / 2.0F + var5,
                  var2.getY() + this.ut + this.adc,
                  var2.getZ() + this.acZ / 2.0F + this.ada
               );
               if (this.xS == 2.0F) {
                  var4.shift(-0.5, 0.0, 0.0);
               }
               break;
            case 2:
            case 9:
            case 10:
               var4 = new BoundingBox(
                  var2.getX() - this.xS / 2.0F - var5,
                  var2.getY() + this.adc,
                  var2.getZ() - this.acZ / 2.0F - this.ada,
                  var2.getX() + this.xS / 2.0F - var5,
                  var2.getY() + this.ut + this.adc,
                  var2.getZ() + this.acZ / 2.0F - this.ada
               );
               if (this.xS == 2.0F) {
                  var4.shift(0.5, 0.0, 0.0);
               }
               break;
            case 3:
               var4 = new BoundingBox(
                  var2.getX() - this.acZ / 2.0F - this.ada,
                  var2.getY() + this.adc,
                  var2.getZ() - this.xS / 2.0F + var5,
                  var2.getX() + this.acZ / 2.0F - this.ada,
                  var2.getY() + this.ut + this.adc,
                  var2.getZ() + this.xS / 2.0F + var5
               );
               if (this.xS == 2.0F) {
                  var4.shift(0.0, 0.0, -0.5);
               }
               break;
            case 4:
               var4 = new BoundingBox(
                  var2.getX() - this.acZ / 2.0F + this.ada,
                  var2.getY() + this.adc,
                  var2.getZ() - this.xS / 2.0F - var5,
                  var2.getX() + this.acZ / 2.0F + this.ada,
                  var2.getY() + this.ut + this.adc,
                  var2.getZ() + this.xS / 2.0F - var5
               );
               if (this.xS == 2.0F) {
                  var4.shift(0.0, 0.0, 0.5);
               }
            case 5:
            case 6:
         }
      }

      if (var4 != null) {
         ProjectileTypeHelper.setBounds(var1, var4);
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private void method423(Entity var1) {
      BoundingBox var6;
      ItemFrame var2 = (ItemFrame)var1;
      Location var3 = var1.getLocation();
      BlockFace var4 = BlockFaceUtils.vectorToFace(var3.getDirection());
      BlockFace var5 = BlockFaceUtils.rotationToFace(var2.getRotation());
      var6 = null;
      label30:
      switch (IntTableConstantsF.SWITCH_MAP_A[var4.ordinal()]) {
         case 1:
            var6 = ProjectileTypeHelper.computeBounds(
                  var1,
                  var3.getX() - this.xS / 2.0F,
                  var3.getX() + this.xS / 2.0F,
                  var3.getY() - this.acZ / 2.0F,
                  var3.getY() + this.acZ / 2.0F,
                  var3.getZ(),
                  var3.getZ() - this.ut
               )
               .shift(this.adb, this.adc, this.ada);
            break;
         case 2:
            var6 = ProjectileTypeHelper.computeBounds(
                  var1,
                  var3.getX() - this.xS / 2.0F,
                  var3.getX() + this.xS / 2.0F,
                  var3.getY() - this.acZ / 2.0F,
                  var3.getY() + this.acZ / 2.0F,
                  var3.getZ(),
                  var3.getZ() + this.ut
               )
               .shift(-this.adb, this.adc, this.ada);
            break;
         case 3:
            var6 = ProjectileTypeHelper.computeBounds(
                  var1,
                  var3.getX(),
                  var3.getX() + this.ut,
                  var3.getY() - this.acZ / 2.0F,
                  var3.getY() + this.acZ / 2.0F,
                  var3.getZ() - this.xS / 2.0F,
                  var3.getZ() + this.xS / 2.0F
               )
               .shift(-this.ada, this.adc, this.adb);
            break;
         case 4:
            var6 = ProjectileTypeHelper.computeBounds(
                  var1,
                  var3.getX(),
                  var3.getX() - this.ut,
                  var3.getY() - this.acZ / 2.0F,
                  var3.getY() + this.acZ / 2.0F,
                  var3.getZ() - this.xS / 2.0F,
                  var3.getZ() + this.xS / 2.0F
               )
               .shift(this.ada, this.adc, -this.adb);
            break;
         case 5:
            switch (IntTableConstantsF.SWITCH_MAP_A[var5.ordinal()]) {
               case 1:
               case 7:
               case 8:
                  var6 = ProjectileTypeHelper.computeBounds(
                        var1,
                        var3.getX() + this.xS / 2.0F,
                        var3.getX() - this.xS / 2.0F,
                        var3.getY(),
                        var3.getY() + this.ut,
                        var3.getZ() - this.acZ / 2.0F,
                        var3.getZ() + this.acZ / 2.0F
                     )
                     .shift(this.adb, this.adc, -this.ada);
                  break label30;
               case 2:
               case 9:
               case 10:
                  var6 = ProjectileTypeHelper.computeBounds(
                        var1,
                        var3.getX() + this.xS / 2.0F,
                        var3.getX() - this.xS / 2.0F,
                        var3.getY(),
                        var3.getY() + this.ut,
                        var3.getZ() - this.acZ / 2.0F,
                        var3.getZ() + this.acZ / 2.0F
                     )
                     .shift(-this.adb, this.adc, this.ada);
                  break label30;
               case 3:
                  var6 = ProjectileTypeHelper.computeBounds(
                        var1,
                        var3.getX() + this.acZ / 2.0F,
                        var3.getX() - this.acZ / 2.0F,
                        var3.getY(),
                        var3.getY() + this.ut,
                        var3.getZ() - this.xS / 2.0F,
                        var3.getZ() + this.xS / 2.0F
                     )
                     .shift(this.ada, this.adc, this.adb);
                  break label30;
               case 4:
                  var6 = ProjectileTypeHelper.computeBounds(
                        var1,
                        var3.getX() + this.acZ / 2.0F,
                        var3.getX() - this.acZ / 2.0F,
                        var3.getY(),
                        var3.getY() + this.ut,
                        var3.getZ() - this.xS / 2.0F,
                        var3.getZ() + this.xS / 2.0F
                     )
                     .shift(-this.ada, this.adc, -this.adb);
               case 5:
               case 6:
               default:
                  break label30;
            }
         case 6:
            switch (IntTableConstantsF.SWITCH_MAP_A[var5.ordinal()]) {
               case 1:
               case 7:
               case 8:
                  var6 = ProjectileTypeHelper.computeBounds(
                        var1,
                        var3.getX() - this.xS / 2.0F,
                        var3.getX() + this.xS / 2.0F,
                        var3.getY(),
                        var3.getY() - this.ut,
                        var3.getZ() - this.acZ / 2.0F,
                        var3.getZ() + this.acZ / 2.0F
                     )
                     .shift(this.adb, this.adc, this.ada);
                  break;
               case 2:
               case 9:
               case 10:
                  var6 = ProjectileTypeHelper.computeBounds(
                        var1,
                        var3.getX() - this.xS / 2.0F,
                        var3.getX() + this.xS / 2.0F,
                        var3.getY(),
                        var3.getY() - this.ut,
                        var3.getZ() - this.acZ / 2.0F,
                        var3.getZ() + this.acZ / 2.0F
                     )
                     .shift(-this.adb, this.adc, -this.ada);
                  break;
               case 3:
                  var6 = ProjectileTypeHelper.computeBounds(
                        var1,
                        var3.getX() - this.acZ / 2.0F,
                        var3.getX() + this.acZ / 2.0F,
                        var3.getY(),
                        var3.getY() - this.ut,
                        var3.getZ() - this.xS / 2.0F,
                        var3.getZ() + this.xS / 2.0F
                     )
                     .shift(this.ada, this.adc, -this.adb);
                  break;
               case 4:
                  var6 = ProjectileTypeHelper.computeBounds(
                        var1,
                        var3.getX() - this.acZ / 2.0F,
                        var3.getX() + this.acZ / 2.0F,
                        var3.getY(),
                        var3.getY() - this.ut,
                        var3.getZ() - this.xS / 2.0F,
                        var3.getZ() + this.xS / 2.0F
                     )
                     .shift(-this.ada, this.adc, this.adb);
               case 5:
               case 6:
            }
      }

      if (var6 != null) {
         ProjectileTypeHelper.setBounds(var1, var6);
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static FurnitureMountFace method424(Entity var0) {
      if (!ProjectileTypeHelper.isAllowedEntityType(var0.getType())) {
         return FurnitureMountFace.FLOOR;
      }

      BlockFace var1 = BlockFaceUtils.vectorToFace(var0.getLocation().getDirection());
      switch (IntTableConstantsF.SWITCH_MAP_A[var1.ordinal()]) {
         case 5:
            return FurnitureMountFace.FLOOR;
         case 6:
            return FurnitureMountFace.CEILING;
         default:
            return FurnitureMountFace.WALL;
      }
   }

   public static void method425(World var0, double var1, double var3, double var5, double var7, double var9, double var11) {
      double var13 = 0.2;

      for (double var15 = var1; var15 <= var3; var15 += var13) {
         for (double var17 = var5; var17 <= var7; var17 += var13) {
            var0.spawnParticle(Particle.DUST, var15, var17, var9, 0, 0.0, 0.0, 0.0, 1.0, new DustOptions(Color.ORANGE, 0.5F));
            var0.spawnParticle(Particle.DUST, var15, var17, var11, 0, 0.0, 0.0, 0.0, 1.0, new DustOptions(Color.ORANGE, 0.5F));
         }

         for (double var20 = var9; var20 <= var11; var20 += var13) {
            var0.spawnParticle(Particle.DUST, var15, var5, var20, 0, 0.0, 0.0, 0.0, 1.0, new DustOptions(Color.ORANGE, 0.5F));
            var0.spawnParticle(Particle.DUST, var15, var7, var20, 0, 0.0, 0.0, 0.0, 1.0, new DustOptions(Color.ORANGE, 0.5F));
         }
      }

      for (double var19 = var5; var19 <= var7; var19 += var13) {
         for (double var21 = var9; var21 <= var11; var21 += var13) {
            var0.spawnParticle(Particle.DUST, var1, var19, var21, 0, 0.0, 0.0, 0.0, 1.0, new DustOptions(Color.ORANGE, 0.5F));
            var0.spawnParticle(Particle.DUST, var3, var19, var21, 0, 0.0, 0.0, 0.0, 1.0, new DustOptions(Color.ORANGE, 0.5F));
         }
      }
   }

   public static List method426(World var0, double var1, double var3, double var5, double var7, double var9, double var11) {
      HashSet var13 = new HashSet();

      for (double var14 = var1; var14 <= var3; var14++) {
         for (double var16 = var5; var16 <= var7; var16++) {
            for (double var18 = var9; var18 <= var11; var18++) {
               var13.add(new Location(var0, var14, var16, var18));
            }
         }
      }

      return new ArrayList(var13);
   }

   public void method427(float var1, float var2) {
      this.adZ = var1;
      this.aea = var2;
   }

   @Generated
   public void aG(boolean var1) {
      this.adX = var1;
   }

   @Generated
   public void aH(boolean var1) {
      this.adY = var1;
   }

   @Generated
   public boolean AG() {
      return this.adY;
   }
}
