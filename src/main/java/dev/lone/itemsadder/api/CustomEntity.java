package dev.lone.itemsadder.api;

import dev.lone.itemsadder.api.CustomEntity.Bone;
import dev.lone.itemsadder.api.CustomEntity.MountBone;
import dev.lone.itemsadder.api.Events.CustomEntityAnimationSignalEvent;
import dev.lone.itemsadder.api.Events.CustomEntityNametagPrepareEvent;
import dev.lone.itemsadder.api.Events.CustomEntityPartBreakEvent;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.CustomEntityImpl;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.entity.CustomMobSettings;
import dev.lone.itemsadder.entity.ModelDefinitionData;
import dev.lone.itemsadder.entity.ModelPartController;
import dev.lone.itemsadder.entity.ModelPartDisplay;
import dev.lone.itemsadder.entity.ModelRiderController;
import dev.lone.itemsadder.loot.LootTable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Consumer;
import org.jetbrains.annotations.Nullable;

public class CustomEntity {
   static final String EXCEPTION = "Entities API not available. Check if it's disabled in config.yml of ItemsAdder";
   CustomEntityImpl renderer;

   CustomEntity(CustomEntityImpl renderer) {
      this.renderer = renderer;
      this.renderer.rk = this;
   }

   public CustomEntityNametag getNametag() {
      return this.renderer.rc;
   }

   public static Set getNamespacedIdsInRegistry() {
      return CustomEntityManager.getInstance().oN.keySet();
   }

   public static boolean isInRegistry(String namespacedId) {
      return CustomEntityManager.getInstance().oN.containsKey(namespacedId);
   }

   @Nullable
   public static CustomEntity spawn(String namespacedId, Location location) {
      return spawn(namespacedId, location, true);
   }

   @Nullable
   public static CustomEntity spawn(String namespacedId, Location location, boolean frustumCulling) {
      return spawn(namespacedId, location, frustumCulling, false, false);
   }

   @Nullable
   public static CustomEntity spawn(String namespacedId, Location location, List viewers, boolean frustumCulling, @Nullable Consumer function) {
      return spawn(namespacedId, location, viewers, frustumCulling, false, false, function);
   }

   @Nullable
   public static CustomEntity spawn(String namespacedId, Location location, boolean frustumCulling, boolean noBase, boolean noHitbox) {
      if (!CustomEntityManager.isInitialized()) {
         throw new RuntimeException("Entities API not available. Check if it's disabled in config.yml of ItemsAdder");
      }

      if (!CustomEntityManager.getInstance().oN.containsKey(namespacedId)) {
         return null;
      }

      CustomEntityImpl var5 = CustomEntityManager.getInstance().spawnCustomEntity(namespacedId, location, frustumCulling, noBase, noHitbox);
      return var5 == null ? null : new CustomEntity(var5);
   }

   @Nullable
   public static CustomEntity spawn(
      String namespacedId, Location location, List viewers, boolean frustumCulling, boolean noBase, boolean noHitbox, @Nullable Consumer function
   ) {
      if (!CustomEntityManager.isInitialized()) {
         throw new RuntimeException("Entities API not available. Check if it's disabled in config.yml of ItemsAdder");
      }

      if (!CustomEntityManager.getInstance().oN.containsKey(namespacedId)) {
         return null;
      }

      CustomEntityImpl var7 = CustomEntityManager.getInstance().method8260(namespacedId, location, null, viewers, frustumCulling, noBase, noHitbox, function);
      return var7 == null ? null : new CustomEntity(var7);
   }

   @Nullable
   public static CustomEntity convert(String namespacedId, LivingEntity bukkitEntity) {
      return convert(namespacedId, bukkitEntity, true);
   }

   @Nullable
   public static CustomEntity convert(String namespacedId, LivingEntity bukkitEntity, boolean frustumCulling) {
      return convert(namespacedId, bukkitEntity, frustumCulling, false, true, true);
   }

   @Nullable
   public static CustomEntity convert(
      String namespacedId, LivingEntity bukkitEntity, boolean frustumCulling, boolean noHitbox, boolean canBaseEntityBeDestroyed, boolean hideBaseEntity
   ) {
      if (!CustomEntityManager.isInitialized()) {
         throw new RuntimeException("Entities API not available. Check if it's disabled in config.yml of ItemsAdder");
      }

      if (!CustomEntityManager.getInstance().oN.containsKey(namespacedId)) {
         return null;
      }

      CustomEntityImpl var6 = CustomEntityManager.getInstance()
         .attachModelWithSkin(namespacedId, null, bukkitEntity, frustumCulling, hideBaseEntity, noHitbox, canBaseEntityBeDestroyed);
      return var6 == null ? null : new CustomEntity(var6);
   }

   @Nullable
   public static CustomEntity convert(String namespacedId, LivingEntity bukkitEntity, List viewers, boolean frustumCulling) {
      if (!CustomEntityManager.isInitialized()) {
         throw new RuntimeException("Entities API not available. Check if it's disabled in config.yml of ItemsAdder");
      }

      if (!CustomEntityManager.getInstance().oN.containsKey(namespacedId)) {
         return null;
      }

      CustomEntityImpl var4 = CustomEntityManager.getInstance().attachModelWithViewers(namespacedId, null, bukkitEntity, viewers, frustumCulling, true);
      return var4 == null ? null : new CustomEntity(var4);
   }

   @Nullable
   public static CustomEntity byAlreadySpawned(Entity entity) {
      if (!CustomEntityManager.isInitialized()) {
         throw new RuntimeException("Entities API not available. Check if it's disabled in config.yml of ItemsAdder");
      }

      CustomEntityImpl var1 = CustomEntityManager.getInstance().getByEntity(entity);
      return var1 == null ? null : new CustomEntity(var1);
   }

   public static boolean isCustomEntity(Entity entity) {
      if (!CustomEntityManager.isInitialized()) {
         throw new RuntimeException("Entities API not available. Check if it's disabled in config.yml of ItemsAdder");
      } else {
         return CustomEntityManager.getInstance().isCustomEntityLoaded(entity);
      }
   }

   public static boolean isCustomEntity(UUID uuid) {
      if (!CustomEntityManager.isInitialized()) {
         throw new RuntimeException("Entities API not available. Check if it's disabled in config.yml of ItemsAdder");
      } else {
         return CustomEntityManager.getInstance().isCustomEntityUuid(uuid);
      }
   }

   public Entity getEntity() {
      return this.renderer.getBukkitEntity();
   }

   public List getDamageableParts() {
      return this.renderer.getDamageableParts();
   }

   @Nullable
   public static CustomEntity getDamageablePartOwner(Entity hitbox) {
      LivingEntity var1 = ModelPartController.asLiving(hitbox);
      return var1 == null ? null : byAlreadySpawned(var1);
   }

   @Nullable
   public static UUID getDamageablePartId(Entity hitbox) {
      return ModelPartController.uuidOf(hitbox);
   }

   public EntityType getType() {
      return this.renderer.getBukkitEntity().getType();
   }

   public Location getLocation() {
      return this.renderer.getLocation();
   }

   public void teleport(Location location) {
      this.renderer.teleport(location);
   }

   public String getNamespacedID() {
      return this.renderer.getNamespacedId();
   }

   public String getNamespace() {
      return this.renderer.getModel().getNamespace();
   }

   public String getId() {
      return this.renderer.getModel().getId();
   }

   public void respawn(Player player) {
      this.renderer.activateViewer(player);
   }

   public void addViewer(Player player) {
      this.renderer.addViewer(player, true);
   }

   public void removeViewer(Player player) {
      this.renderer.scheduleViewerRemoval(player);
   }

   public void setVisible(Player player, boolean visible) {
      this.renderer.setVisibleTo(player, visible);
   }

   public void setFrustumCulling(boolean cull) {
      this.renderer.qO = cull;
   }

   public boolean getFrustumCulling() {
      return this.renderer.qO;
   }

   public boolean playAnimation(String name, @Nullable Runnable callback) {
      ModelDefinitionData var3 = this.renderer.getModelDefinition(name);
      if (var3 == null) {
         return false;
      }

      long var4 = this.renderer.getModelInstance().method8913(var3);
      this.renderer.dispatchToManager(() -> this.renderer.getModelInstance().method8916(var3, 0, callback, var4));
      return true;
   }

   public boolean playAnimation(String name) {
      return this.playAnimation(name, null);
   }

   public boolean playAnimation(String name, AnimationPlaybackOptions options, @Nullable Runnable callback) {
      Objects.requireNonNull(options, "options");
      ModelDefinitionData var4 = this.renderer.getModelDefinition(name);
      if (var4 != null && this.renderer.getModelInstance().method8918(var4, options)) {
         long var5 = this.renderer.getModelInstance().method8913(var4);
         this.renderer.dispatchToManager(() -> this.renderer.getModelInstance().method8917(var4, 0, callback, var5, options));
         return true;
      } else {
         return false;
      }
   }

   public void setAnimationPaused(String layer, boolean paused) {
      Objects.requireNonNull(layer, "layer");
      this.renderer.dispatchToManager(() -> this.renderer.getModelInstance().method8919(layer, paused));
   }

   @Nullable
   public static Component internal_prepareNametag(CustomEntityImpl renderer, @Nullable Component originalText, @Nullable Component text) {
      CustomEntity var3 = renderer.rk == null ? new CustomEntity(renderer) : renderer.rk;
      CustomEntityNametagPrepareEvent var4 = new CustomEntityNametagPrepareEvent(var3, originalText, text);
      Bukkit.getPluginManager().callEvent(var4);
      return var4.getText();
   }

   public static void internal_firePartBreak(CustomEntityImpl renderer, UUID partId, String bone, @Nullable DamageSource damageSource) {
      if (!renderer.isRemoved()) {
         CustomEntity var4 = renderer.rk == null ? new CustomEntity(renderer) : renderer.rk;
         Bukkit.getPluginManager().callEvent(new CustomEntityPartBreakEvent(var4, partId, bone, damageSource));
      }
   }

   public static void internal_fireAnimationSignal(CustomEntityImpl renderer, String animation, String layer, long playbackId, String signal) {
      if (!renderer.isRemoved()) {
         CustomEntity var6 = renderer.rk == null ? new CustomEntity(renderer) : renderer.rk;
         Bukkit.getPluginManager().callEvent(new CustomEntityAnimationSignalEvent(var6, animation, layer, playbackId, signal));
      }
   }

   public void stopAnimation() {
      long var1 = this.renderer.getModelInstance().qR();
      this.renderer.dispatchToManager(() -> this.renderer.getModelInstance().method8921(var1));
   }

   public boolean stopAnimationLayer(String layer) {
      long var2 = this.renderer.getModelInstance().bo(layer);
      if (var2 < 0L) {
         return false;
      }

      this.renderer.dispatchToManager(() -> this.renderer.getModelInstance().method8927(layer, var2));
      return true;
   }

   public boolean isPlayingAnimation(String name) {
      return this.renderer.getModelInstance().bl(name);
   }

   public static boolean hasAnimation(String entityNamespacedId, String animationName) {
      CustomMobSettings var2 = (CustomMobSettings)CustomEntityManager.getInstance().oN.get(entityNamespacedId);
      if (var2 == null) {
         throw new NullPointerException("Can't find custom entity '" + entityNamespacedId + "' in config files.");
      } else {
         return var2.vn.AK.containsKey(animationName);
      }
   }

   public boolean hasAnimation(String name) {
      return this.renderer.getModel().AK.containsKey(name);
   }

   public static List getAnimationsNames(String entityNamespacedId) {
      CustomMobSettings var1 = (CustomMobSettings)CustomEntityManager.getInstance().oN.get(entityNamespacedId);
      if (var1 == null) {
         throw new NullPointerException("Can't find custom entity '" + entityNamespacedId + "' in config files.");
      } else {
         return var1.vn.getAnimationsNames();
      }
   }

   public List getAnimationsNames() {
      return this.renderer.getModel().getAnimationsNames();
   }

   public void destroy() {
      CustomEntityManager.getInstance().removeWithEffects(this.renderer, true, false);
   }

   public void playDamageEffect(boolean fire) {
      this.renderer.dispatchToManager(() -> this.renderer.playDamageEffect(fire));
   }

   public void setColorAllBones(int color) {
      for (ModelPartDisplay var3 : this.renderer.rQ.values()) {
         var3.setColorSilently(color);
      }

      this.renderer.dispatchToManager(() -> {
         for (ModelPartDisplay var2 : this.renderer.rQ.values()) {
            if (var2.Hn != null) {
               var2.refreshColor();
            }
         }
      });
   }

   public void setEnchantedAllBones(boolean enchanted) {
      for (ModelPartDisplay var3 : this.renderer.rQ.values()) {
         var3.setEnchantGlintSilently(enchanted);
      }

      this.renderer.dispatchToManager(() -> {
         for (ModelPartDisplay var2 : this.renderer.rQ.values()) {
            if (var2.Hn != null) {
               var2.refreshEnchantGlint();
            }
         }
      });
   }

   public boolean hasMountBones() {
      return this.renderer.hasRiders();
   }

   public boolean addPassenger(LivingEntity passenger) {
      return !this.renderer.hasRiders() ? false : CustomEntityManager.getInstance().mountOnEntity(passenger, this.renderer);
   }

   public static void removePassenger(LivingEntity passenger) {
      CustomEntityManager.getInstance().removePassenger(passenger);
   }

   public Set getPassengers() {
      HashSet var1 = new HashSet();

      for (ModelRiderController var3 : this.renderer.rP) {
         if (var3.isMounted()) {
            var1.add(var3.MZ);
         }
      }

      return var1;
   }

   public boolean hasPassenger(LivingEntity passenger) {
      for (ModelRiderController var3 : this.renderer.rP) {
         if (var3.isMounted() && var3.MZ == passenger) {
            return true;
         }
      }

      return false;
   }

   public boolean hasPassenger() {
      for (ModelRiderController var2 : this.renderer.rP) {
         if (var2.isMounted()) {
            return true;
         }
      }

      return false;
   }

   public boolean setPassenger(LivingEntity passenger, int ordinal) {
      if (!this.renderer.hasRiders()) {
         return false;
      }

      List var3 = this.renderer.rP;
      if (ordinal >= var3.size()) {
         throw new IllegalArgumentException("No mount bone with such index. Max mount bone index: " + (var3.size() - 1));
      }

      ModelRiderController var4 = (ModelRiderController)var3.get(ordinal);
      if (var4 == null) {
         return false;
      }

      if (var4.isMounted()) {
         CustomEntityManager.getInstance().removePassenger(var4.MZ);
         if (var4.isMounted()) {
            return false;
         }
      }

      return CustomEntityManager.getInstance().mountRider(passenger, var4);
   }

   public Set getBones() {
      HashSet var1 = new HashSet();
      int var2 = 0;

      for (Entry var4 : this.renderer.rQ.entrySet()) {
         ModelPartDisplay var5 = (ModelPartDisplay)var4.getValue();
         var1.add(Bone.wrap(var5, var2));
         var2++;
      }

      return var1;
   }

   public Set getMountBones() {
      HashSet var1 = new HashSet();
      int var2 = 0;

      for (Entry var4 : this.renderer.rQ.entrySet()) {
         ModelPartDisplay var5 = (ModelPartDisplay)var4.getValue();
         if (var5.getRider() != null) {
            var1.add((MountBone)Bone.wrap(var5, var2));
         }

         var2++;
      }

      return var1;
   }

   public Bone getBone(int index) {
      if (index >= this.renderer.rQ.size()) {
         throw new IndexOutOfBoundsException("There is no bone with such index");
      }

      int var2 = 0;

      for (Entry var4 : this.renderer.rQ.entrySet()) {
         ModelPartDisplay var5 = (ModelPartDisplay)var4.getValue();
         if (var2 == index) {
            return Bone.wrap(var5, var2);
         }

         var2++;
      }

      throw new IndexOutOfBoundsException("There is no bone with such index");
   }

   @Nullable
   public Bone getBone(String name) {
      byte var2 = 0;

      for (Entry var4 : this.renderer.rQ.entrySet()) {
         ModelPartDisplay var5 = (ModelPartDisplay)var4.getValue();
         if (var5.Ho.getId().equals(name)) {
            return Bone.wrap(var5, var2);
         }
      }

      return null;
   }

   @Nullable
   public MountBone getMountBoneByPassenger(LivingEntity passenger) {
      for (MountBone var4 : this.getMountBones()) {
         if (var4.internal.getRider() != null && var4.internal.getRider().MZ == passenger) {
            return var4;
         }
      }

      return null;
   }

   public void internal_changeWrappedCustomEntity(CustomEntityImpl newRenderer) {
      this.renderer = newRenderer;
      newRenderer.rk = this;
   }

   public List getLoot() {
      return this.getLoot(null);
   }

   public List getLoot(@Nullable ItemStack tool) {
      return getLoot(this.renderer.getBukkitEntity(), tool);
   }

   public static List getLoot(LivingEntity bukkitEntity, @Nullable ItemStack tool) {
      CustomEntityImpl var2 = CustomEntityManager.getInstance().getByEntity(bukkitEntity);
      return var2 == null ? new ArrayList() : getLoot0(var2, tool);
   }

   private static List getLoot0(CustomEntityImpl renderer, @Nullable ItemStack tool) {
      ArrayList var2 = new ArrayList();
      List var3 = ContentManager.method8084().aJ.method1989(renderer);
      if (var3 != null) {
         for (LootTable var5 : var3) {
            var2.addAll(var5.method2005(tool));
         }
      }

      return var2;
   }
}
