package dev.lone.itemsadder.resourcepack;

import org.bukkit.plugin.Plugin;

public class HostedPackDeliveryImpl extends HostedPackDelivery {
   public HostedPackDeliveryImpl(Plugin var1, ResourcePackService var2) {
      super(var1, "lobfile", var2, "https://lobfile.com/privacy-policy", "lobfile");
      this.setRequestHandler(new BossBarPackRequest());
   }
}
