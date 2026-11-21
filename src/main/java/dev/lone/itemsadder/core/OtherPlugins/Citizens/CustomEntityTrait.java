package dev.lone.itemsadder.Core.OtherPlugins.Citizens;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.entity.CustomEntityImpl;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.entity.ProjectileTypeHelper;
import dev.lone.itemsadder.util.ToggleState;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPC.Metadata;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.util.DataKey;
import net.citizensnpcs.trait.HologramTrait;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class CustomEntityTrait extends Trait {
   @Persist("namespacedId")
   String namespacedId;
   @Persist("customPlayerSkin")
   String customPlayerSkin;
   private LivingEntity invisibleEntity;
   private boolean wasInvisible;

   public CustomEntityTrait() {
      super("customentity");
   }

   public static CustomEntityTrait inject(NPC npc) {
      fixEntityType(npc);
      return (CustomEntityTrait)npc.getOrAddTrait(CustomEntityTrait.class);
   }

   public void load(DataKey key) {
      if (Settings.get().customEntitiesEnabled) {
         ;
      }
   }

   public void onSpawn() {
      if (Settings.get().customEntitiesEnabled) {
         if (this.namespacedId != null) {
            if (!CustomEntityManager.getInstance().oN.containsKey(this.namespacedId)) {
               return;
            }

            this.initRenderer(false);
         }
      }
   }

   public void onDespawn() {
      CustomEntityImpl var1 = this.getRenderer();
      if (var1 != null) {
         CustomEntityManager.getInstance().removeWithEffects(var1, false, false);
      }
   }

   public void save(DataKey key) {
   }

   public void run() {
   }

   public void onAttach() {
   }

   public void onRemove() {
      CustomEntityImpl var1 = this.getRenderer();
      if (var1 != null) {
         CustomEntityManager.getInstance().removeWithEffects(var1, false, true);
      }

      if (this.invisibleEntity != null && this.invisibleEntity == this.npc.getEntity()) {
         this.invisibleEntity.setInvisible(this.wasInvisible);
      }
   }

   public void applyModel(String namespacedId, boolean justAttached) {
      this.namespacedId = namespacedId;
      this.initRenderer(justAttached);
   }

   public void initPlayerModel(@Nullable String playerSkin) {
      this.namespacedId = "_iainternal:player";
      this.customPlayerSkin = this.resolvePlayerSkin(playerSkin);
      this.initRenderer(true);
   }

   private String resolvePlayerSkin(String playerSkin) {
      if (playerSkin != null && (playerSkin.equals("CITIZENS_SKIN") || playerSkin.equals("KEEP_CURRENT_SKIN"))) {
         SkinTrait var2 = (SkinTrait)this.npc.getTraitNullable(SkinTrait.class);
         if (var2 != null) {
            if (var2.getTexture() != null) {
               playerSkin = var2.getTexture();
            } else if (var2.getSkinName() != null) {
               playerSkin = var2.getSkinName();
            } else if (var2.getName() != null) {
               playerSkin = var2.getName();
            }
         }
      }

      return playerSkin;
   }

   public void changePlayerSkin(String playerSkin) {
      CustomEntityImpl var2 = CustomEntityManager.getInstance().getByUuid(this.npc.getEntity().getUniqueId());
      if (var2 != null) {
         if (!var2.qK.vn.AM) {
            this.namespacedId = "_iainternal:player";
            var2 = this.initRenderer(false);
         }

         playerSkin = this.resolvePlayerSkin(playerSkin);
         var2.changePlayerSkin(playerSkin);
         this.customPlayerSkin = var2.getPlayerSkin();
      }
   }

   public static void fixEntityType(NPC npc) {
      if (!ProjectileTypeHelper.getAdapter().af(npc.getEntity())) {
         npc.setBukkitEntityType(EntityType.ZOMBIE);
      }
   }

   @Nullable
   CustomEntityImpl initRenderer(boolean justAttached) {
      if (justAttached) {
         this.npc.setAlwaysUseNameHologram(true);
         this.npc.data().setPersistent(Metadata.SILENT, true);
         this.npc.data().setPersistent(Metadata.AMBIENT_SOUND, "");
         this.npc.data().setPersistent(Metadata.DEATH_SOUND, "");
         this.npc.data().setPersistent(Metadata.HURT_SOUND, "");
         HologramTrait var2 = (HologramTrait)this.npc.getTraitNullable(HologramTrait.class);
         if (var2 != null && var2.getLineHeight() == -1.0) {
            var2.setLineHeight(0.0);
         }
      }

      return this.attachNewPlayerRenderer();
   }

   private CustomEntityImpl attachNewPlayerRenderer() {
      LivingEntity var1 = (LivingEntity)this.npc.getEntity();
      if (this.invisibleEntity != var1) {
         this.invisibleEntity = var1;
         this.wasInvisible = var1.isInvisible();
      }

      var1.setInvisible(true);
      CustomEntityImpl var2 = CustomEntityManager.getInstance().attachModelInternal(this.namespacedId, null, var1, true, false, false, true, true);
      if (this.customPlayerSkin != null) {
         var2.changePlayerSkin(this.customPlayerSkin);
      }

      var2.initBodyRotation();
      var2.applySensitivity();
      return var2;
   }

   public void playAnim(String animName, int repeatCount) {
      CustomEntityImpl var3 = CustomEntityManager.getInstance().getByUuid(this.npc.getEntity().getUniqueId());
      if (var3 != null) {
         if (!CustomEntityManager.isDefaultEntity(var3) && !CustomEntityManager.getInstance().getPlayerModelIds().contains(this.namespacedId)) {
            var3.playAnim(animName, repeatCount);
            return;
         }

         ToggleState var4 = CustomEntityManager.method8320(var3, animName);
         this.namespacedId = var4.pO;
      }
   }

   public void pauseAnim() {
      CustomEntityImpl var1 = CustomEntityManager.getInstance().getByUuid(this.npc.getEntity().getUniqueId());
      if (var1 != null) {
         var1.getModelInstance().qN();
      }
   }

   public CustomEntityImpl getRenderer() {
      return CustomEntityManager.getInstance().getByEntity(this.npc.getEntity());
   }
}
