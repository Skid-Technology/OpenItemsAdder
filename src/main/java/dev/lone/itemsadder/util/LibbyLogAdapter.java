package dev.lone.itemsadder.util;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.internal.SwitchMap92;
import ia.sh.libby.logging.LogLevel;
import ia.sh.libby.logging.adapters.LogAdapter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;

class LibbyLogAdapter implements LogAdapter {
   LibbyLogAdapter(Main var1) {
      this.plugin = var1;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public void log(@NotNull LogLevel level, String message) {
      Logger var3 = this.plugin.getLogger();
      switch (SwitchMap92.aEx[level.ordinal()]) {
         case 1:
            var3.log(Level.INFO, message);
            break;
         case 2:
            var3.log(Level.INFO, message);
            break;
         case 3:
            var3.log(Level.WARNING, message);
            break;
         case 4:
            var3.log(Level.SEVERE, message);
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public void log(@NotNull LogLevel level, String message, Throwable throwable) {
      Logger var4 = this.plugin.getLogger();
      switch (SwitchMap92.aEx[level.ordinal()]) {
         case 1:
            var4.log(Level.INFO, message, throwable);
            break;
         case 2:
            var4.log(Level.INFO, message, throwable);
            break;
         case 3:
            var4.log(Level.WARNING, message, throwable);
            break;
         case 4:
            var4.log(Level.SEVERE, message, throwable);
      }
   }
}
