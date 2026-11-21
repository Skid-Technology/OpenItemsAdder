package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.ServerCompat;
import java.lang.reflect.Field;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.monster.Slime;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftSlime;
import org.bukkit.entity.Entity;

public class EntityEventSinkImplK implements EntityEventSink {
   static Object bpQ = null;

   @Override
   public void onEntityEvent(Entity slime, int size) {
      if (bpQ == null) {
         try {
            Field var3 = ServerCompat.findFieldByType(Slime.class, EntityDataAccessor.class);
            if (var3 == null) {
               throw new NullPointerException();
            }

            bpQ = ServerCompat.readField(var3, null);
         } catch (Exception var4) {
            Log.error("Error getting SIZE field of Slime entity.");
            var4.printStackTrace();
            return;
         }
      }

      CraftSlime var5 = (CraftSlime)slime;
      var5.getHandle().getEntityData().set((EntityDataAccessor)bpQ, size);
   }
}
