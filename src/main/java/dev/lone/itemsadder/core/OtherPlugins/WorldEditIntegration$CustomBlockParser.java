package dev.lone.itemsadder.Core.OtherPlugins;

import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.internal.registry.InputParser;
import com.sk89q.worldedit.world.block.BaseBlock;
import dev.lone.itemsadder.api.CustomBlock;
import java.util.Map;
import java.util.stream.Stream;

final class WorldEditIntegration$CustomBlockParser extends InputParser {
   private WorldEditIntegration$CustomBlockParser(WorldEditIntegration var1) {
      super(WorldEdit.getInstance());
   }

   public Stream getSuggestions(String input) {
      return CustomBlock.getNamespacedIdsInRegistry().stream().filter(id -> id.contains(input));
   }

   public BaseBlock parseFromInput(String input, ParserContext context) {
      if (!CustomBlock.isInRegistry(input)) {
         return null;
      }

      CustomBlock var3 = CustomBlock.getInstance(input);
      return var3 != null && var3.getBaseBlockData() != null
         ? BukkitAdapter.adapt(var3.getBaseBlockData()).toBaseBlock(new CompoundTag(Map.of("ItemsAdderId", new StringTag(input))))
         : null;
   }
}
