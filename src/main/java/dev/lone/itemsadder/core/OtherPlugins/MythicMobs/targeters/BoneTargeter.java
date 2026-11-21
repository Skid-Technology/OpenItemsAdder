package dev.lone.itemsadder.Core.OtherPlugins.MythicMobs.targeters;

import dev.lone.itemsadder.entity.CustomEntityImpl;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.entity.ModelPartDisplay;
import dev.lone.itemsadder.util.Log;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.targeters.ILocationTargeter;
import io.lumine.mythic.bukkit.BukkitAdapter;
import java.util.Collection;
import java.util.Collections;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class BoneTargeter implements ILocationTargeter {
   private final String boneName;

   public BoneTargeter(MythicLineConfig mlc) {
      this.boneName = mlc.getString(new String[]{"bone"});
   }

   public Collection getLocations(SkillMetadata data) {
      Entity var2 = BukkitAdapter.adapt(data.getCaster().getEntity());
      if (!(var2 instanceof LivingEntity)) {
         Log.error("Error loading MythicMobs entity: " + var2.getName() + ". You must use only living entities. For example: ZOMBIE, SKELETON...");
         return Collections.emptyList();
      }

      CustomEntityImpl var3 = CustomEntityManager.getInstance().getByEntity(var2);
      if (var3 != null) {
         ModelPartDisplay var4 = var3.getModelPart(this.boneName);
         if (var4 != null) {
            double[] var5 = var4.Hn.Nw();
            return Collections.singletonList(BukkitAdapter.adapt(new Location(var2.getWorld(), var5[0], var5[1], var5[2])));
         }

         Log.warn("[Mythicmobs] Warning: Unknown bone name for entity " + var3.getNamespacedId());
      } else {
         String var6 = var2.getUniqueId() + " " + var2.getName() + " - " + var2.getLocation();
         Log.warn("Warning: MythicMobs entity " + var6 + " is not a custom entity!");
      }

      return Collections.emptyList();
   }
}
