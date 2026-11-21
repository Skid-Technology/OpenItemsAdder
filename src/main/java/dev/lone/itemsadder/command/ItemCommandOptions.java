package dev.lone.itemsadder.command;

import java.util.ArrayList;

class ItemCommandOptions extends ArrayList {
   ItemCommandOptions(ItemCommandTabCompleter var1) {
      this.aHu = var1;
      this.add("--player");
      this.add("--slot");
      this.add("--item");
   }
}
