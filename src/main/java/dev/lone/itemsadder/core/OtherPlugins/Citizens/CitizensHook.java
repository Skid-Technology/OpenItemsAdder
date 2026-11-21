package dev.lone.itemsadder.Core.OtherPlugins.Citizens;

import com.google.common.collect.Iterables;
import dev.lone.itemsadder.entity.CustomEntityImpl;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.ServerCompat;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.SimpleNPCDataStore;
import net.citizensnpcs.api.npc.NPC.Metadata;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitInfo;
import net.citizensnpcs.api.util.DataKey;
import net.citizensnpcs.api.util.Storage;
import org.apache.commons.lang.reflect.MethodUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class CitizensHook {
   private static TraitInfo trait;
   private static boolean isReload = false;

   public static boolean isRegistered() {
      return trait != null;
   }

   public static void register() {
      trait = TraitInfo.create(CustomEntityTrait.class).withName("customentity");
      CitizensAPI.getTraitFactory().registerTrait(trait);
      Log.log("Registered Citizens NPC Trait: " + trait.getTraitName());
   }

   public static void unregister() {
      if (trait != null) {
         try {
            CitizensAPI.getTraitFactory().deregisterTrait(trait);
            trait = null;

            for (NPC var1 : CitizensAPI.getNPCRegistry()) {
               for (Trait var3 : var1.getTraits()) {
                  if (var3.getName().equals("customentity")) {
                     var1.data().set("has_customentity", true);
                     var1.despawn(DespawnReason.RELOAD);
                     Log.log("Despawned Citizens NPC " + var1.getId() + " on ItemsAdder unload.");
                     break;
                  }
               }
            }
         } catch (Throwable var4) {
            Log.error("Failed to unregister CustomEntity trait for Citizens . Try to update to the latest version.", var4);
         }
      }
   }

   public static void reload() {
      if (Iterables.size(CitizensAPI.getNPCRegistry()) != 0) {
         boolean var0 = false;
         int var1 = 0;
         if (isReload) {
            saveAll();
         }

         try {
            Citizens var2 = (Citizens)CitizensAPI.getPlugin();
            SimpleNPCDataStore var3 = (SimpleNPCDataStore)ServerCompat.getFieldValue(Citizens.class, "saves", var2);
            Storage var4 = (Storage)ServerCompat.getFieldValue(SimpleNPCDataStore.class, "root", var3);
            Iterable var5 = var4.getKey("npc").getIntegerSubKeys();

            for (NPC var7 : CitizensAPI.getNPCRegistry()) {
               boolean var8 = false;

               for (Trait var10 : var7.getTraits()) {
                  if (var10.getName().equals("customentity")) {
                     respawn(var7);
                     var1++;
                     if (!var0) {
                        Log.log("Reloading ItemsAdder Custom Entities Citizens NPCs...");
                        var0 = true;
                     }
                     break;
                  }
               }

               if (!var8 && var7.data().has("has_customentity")) {
                  var7.data().remove("has_customentity");
                  var7.despawn(DespawnReason.RELOAD);

                  try {
                     for (DataKey var21 : var5) {
                        int var11 = Integer.parseInt(var21.name());
                        if (var11 == var7.getId()) {
                           if (!var21.keyExists("name")) {
                              throw new RuntimeException("Cound not find a name for NPC: " + var7.getId());
                           }

                           String var12 = var21.getString("traitnames");
                           if (var12.contains("customentity")) {
                              var7.load(var21);
                           }
                        }
                     }

                     var1++;
                     if (!var0) {
                        Log.log("Reloading ItemsAdder Custom Entities Citizens NPCs...");
                        var0 = true;
                     }

                     Log.log("Spawned Citizens NPC " + var7.getId() + " after ItemsAdder resources load.");
                  } catch (Throwable var17) {
                     Log.error("Failed to respawn Custom Entities Citizens NPC: " + var7, var17);
                  }
               }
            }
         } catch (Throwable var18) {
            Log.error("Failed to respawn Custom Entities Citizens NPCs", var18);
         } finally {
            if (var1 > 0) {
               Log.log("Reloaded " + var1 + " ItemsAdder Custom Entities Citizens NPCs.");
            }
         }

         isReload = true;
      }
   }

   public static void saveAll() {
      saveAll(false);
   }

   public static void saveAll(boolean async) {
      Log.devLog("Saving Citizens NPCs");

      try {
         Method var1 = MethodUtils.getAccessibleMethod(Citizens.class, "storeNPCsNow", new Class[0]);
         var1.invoke(CitizensAPI.getPlugin());
      } catch (Throwable var6) {
         try {
            Method var2 = MethodUtils.getAccessibleMethod(Citizens.class, "storeNPCs", new Class[0]);
            var2.invoke(CitizensAPI.getPlugin());
         } catch (Throwable var5) {
            try {
               Method var3 = MethodUtils.getAccessibleMethod(Citizens.class, "storeNPCs", boolean.class);
               var3.invoke(CitizensAPI.getPlugin(), async);
            } catch (Throwable var4) {
               Log.error("Failed to save Citizens NPCs. Try to update to the latest version.", var6);
            }
         }
      }
   }

   public static boolean isNPC(@Nullable LivingEntity livingEntity) {
      return livingEntity == null ? false : CitizensAPI.getNPCRegistry().isNPC(livingEntity);
   }

   public static void spawn(NPC npc) {
      if (!npc.isSpawned()) {
         npc.spawn(npc.getStoredLocation());
      }
   }

   public static void respawn(NPC npc) {
      if (npc.isSpawned()) {
         npc.despawn();
         npc.spawn(npc.getStoredLocation());
      }
   }

   public static boolean isNameplateVisible(NPC npc) {
      return (Boolean)npc.data().get(Metadata.NAMEPLATE_VISIBLE, true);
   }

   @Nullable
   public static NPC getSelected(CommandSender sender) {
      return CitizensAPI.getDefaultNPCSelector().getSelected(sender);
   }

   @Nullable
   public static CustomEntityImpl getSelectedNpcEntityRenderer(CommandSender sender) {
      NPC var1 = CitizensAPI.getDefaultNPCSelector().getSelected(sender);
      if (var1 == null) {
         return null;
      }

      CustomEntityTrait var2 = (CustomEntityTrait)var1.getTraitNullable(CustomEntityTrait.class);
      return var2 == null ? null : var2.getRenderer();
   }

   public static List getNpcsIds() {
      ArrayList var0 = new ArrayList();

      for (NPC var2 : CitizensAPI.getNPCRegistry()) {
         var0.add(Integer.toString(var2.getId()));
      }

      return var0;
   }
}
