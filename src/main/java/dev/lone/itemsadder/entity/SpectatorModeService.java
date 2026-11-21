package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.internal.SwitchMap54;
import dev.lone.itemsadder.nms.GameModeAdapter;
import dev.lone.itemsadder.nms.NmsRegistry;
import dev.lone.itemsadder.util.SchedulerUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public final class SpectatorModeService {
   public final GameModeAdapter aYN = (GameModeAdapter)NmsRegistry.method2777(GameModeAdapter.class);
   private static SpectatorModeService aYO;

   SpectatorModeService() {
   }

   private static SpectatorModeService getInstance() {
      if (aYO == null) {
         aYO = new SpectatorModeService();
      }

      return aYO;
   }

   public static GameModeAdapter getAdapter() {
      return getInstance().aYN;
   }

   public static void enterSpectator(Player var0) {
      if (var0.getGameMode() != GameMode.SPECTATOR) {
         getInstance().aYN.method3424(var0, GameMode.SPECTATOR);
      }
   }

   public static void exitSpectator(Player var0) {
      if (var0.getGameMode() != GameMode.SPECTATOR) {
         getInstance().aYN.cv(var0);
         SchedulerUtils.runLaterSync(() -> getInstance().aYN.cw(var0), 5L);
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static float method3422(GameMode var0) {
      return switch (SwitchMap54.aYP[var0.ordinal()]) {
         case 1 -> 0.0F;
         case 2 -> 1.0F;
         case 3 -> 2.0F;
         case 4 -> 3.0F;
         default -> throw new MatchException(null, null);
      };
   }
}
