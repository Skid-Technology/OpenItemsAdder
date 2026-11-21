package ia.sh.customblockdata;

import dev.lone.itemsadder.util.BooleanPersistentType;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.AbstractMap$SimpleEntry;
import java.util.HashSet;
import java.util.Map$Entry;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomBlockData implements PersistentDataContainer {
   private static final char[] DEFAULT_PACKAGE = new char[]{
      'c', 'o', 'm', '.', 'j', 'e', 'f', 'f', '_', 'm', 'e', 'd', 'i', 'a', '.', 'c', 'u', 's', 't', 'o', 'm', 'b', 'l', 'o', 'c', 'k', 'd', 'a', 't', 'a'
   };
   private static final Set<Map$Entry<UUID, BlockVector>> DIRTY_BLOCKS = new HashSet<>();
   private static final PersistentDataType<?, ?>[] PRIMITIVE_DATA_TYPES = new PersistentDataType[]{
      PersistentDataType.BYTE,
      PersistentDataType.SHORT,
      PersistentDataType.INTEGER,
      PersistentDataType.LONG,
      PersistentDataType.FLOAT,
      PersistentDataType.DOUBLE,
      PersistentDataType.STRING,
      PersistentDataType.BYTE_ARRAY,
      PersistentDataType.INTEGER_ARRAY,
      PersistentDataType.LONG_ARRAY,
      PersistentDataType.TAG_CONTAINER_ARRAY,
      PersistentDataType.TAG_CONTAINER
   };
   private static final NamespacedKey PERSISTENCE_KEY = Objects.requireNonNull(
      NamespacedKey.fromString("customblockdata:protected"), "Could not create persistence NamespacedKey"
   );
   private static final Pattern KEY_REGEX = Pattern.compile("^x(\\d+)y(-?\\d+)z(\\d+)$");
   private static final int CHUNK_MIN_XZ = 0;
   private static final int CHUNK_MAX_XZ = 15;
   private static final boolean HAS_MIN_HEIGHT_METHOD;
   private final PersistentDataContainer pdc;
   private final Chunk chunk;
   private final NamespacedKey key;
   private final Map$Entry<UUID, BlockVector> blockEntry;
   private final Plugin plugin;

   public CustomBlockData(@NotNull Block block, @NotNull Plugin plugin) {
      this.chunk = block.getChunk();
      this.key = getKey(plugin, block);
      this.pdc = this.getPersistentDataContainer();
      this.blockEntry = getBlockEntry(block);
      this.plugin = plugin;
   }

   @Deprecated
   public CustomBlockData(@NotNull Block block, @NotNull String namespace) {
      this.chunk = block.getChunk();
      this.key = new NamespacedKey(namespace, getKey(block));
      this.pdc = this.getPersistentDataContainer();
      this.plugin = JavaPlugin.getProvidingPlugin(CustomBlockData.class);
      this.blockEntry = getBlockEntry(block);
   }

   private static void checkRelocation() {
      if (CustomBlockData.class.getPackage().getName().equals(new String(DEFAULT_PACKAGE))) {
         try {
            JavaPlugin plugin = JavaPlugin.getProvidingPlugin(CustomBlockData.class);
            plugin.getLogger()
               .warning(
                  "Nag author(s) "
                     + String.join(", ", plugin.getDescription().getAuthors())
                     + " of plugin "
                     + plugin.getName()
                     + " for not relocating the CustomBlockData package."
               );
         } catch (IllegalArgumentException var1) {
         }
      }
   }

   private static Map$Entry<UUID, BlockVector> getBlockEntry(@NotNull Block block) {
      UUID uuid = block.getWorld().getUID();
      BlockVector blockVector = new BlockVector(block.getX(), block.getY(), block.getZ());
      return new AbstractMap$SimpleEntry<>(uuid, blockVector);
   }

   static boolean isDirty(Block block) {
      return DIRTY_BLOCKS.contains(getBlockEntry(block));
   }

   static void setDirty(Plugin plugin, Map$Entry<UUID, BlockVector> blockEntry) {
      if (plugin.isEnabled()) {
         DIRTY_BLOCKS.add(blockEntry);
         Bukkit.getScheduler().runTask(plugin, () -> DIRTY_BLOCKS.remove(blockEntry));
      }
   }

   private static NamespacedKey getKey(Plugin plugin, Block block) {
      return new NamespacedKey(plugin, getKey(block));
   }

   @NotNull
   static String getKey(@NotNull Block block) {
      int x = block.getX() & 15;
      int y = block.getY();
      int z = block.getZ() & 15;
      return "x" + x + "y" + y + "z" + z;
   }

   @Nullable
   static Block getBlockFromKey(NamespacedKey key, Chunk chunk) {
      Matcher matcher = KEY_REGEX.matcher(key.getKey());
      if (!matcher.matches()) {
         return null;
      }

      int x = Integer.parseInt(matcher.group(1));
      int y = Integer.parseInt(matcher.group(2));
      int z = Integer.parseInt(matcher.group(3));
      return x >= 0 && x <= 15 && z >= 0 && z <= 15 && y >= getWorldMinHeight(chunk.getWorld()) && y <= chunk.getWorld().getMaxHeight() - 1
         ? chunk.getBlock(x, y, z)
         : null;
   }

   static int getWorldMinHeight(World world) {
      return HAS_MIN_HEIGHT_METHOD ? world.getMinHeight() : 0;
   }

   public static boolean hasCustomBlockData(Block block, Plugin plugin) {
      return block.getChunk().getPersistentDataContainer().has(getKey(plugin, block), PersistentDataType.TAG_CONTAINER);
   }

   public static boolean isProtected(Block block, Plugin plugin) {
      return new CustomBlockData(block, plugin).isProtected();
   }

   public static void registerListener(Plugin plugin) {
      Bukkit.getPluginManager().registerEvents(new BlockDataListener(plugin), plugin);
   }

   @NotNull
   public static Set<Block> getBlocksWithCustomData(Plugin plugin, Chunk chunk) {
      NamespacedKey dummy = new NamespacedKey(plugin, "dummy");
      return getBlocksWithCustomData(chunk, dummy);
   }

   @NotNull
   private static Set<Block> getBlocksWithCustomData(@NotNull Chunk chunk, @NotNull NamespacedKey namespace) {
      PersistentDataContainer chunkPDC = chunk.getPersistentDataContainer();
      return chunkPDC.getKeys()
         .stream()
         .filter(key -> key.getNamespace().equals(namespace.getNamespace()))
         .map(key -> getBlockFromKey(key, chunk))
         .filter(Objects::nonNull)
         .collect(Collectors.toSet());
   }

   @NotNull
   public static Set<Block> getBlocksWithCustomData(String namespace, Chunk chunk) {
      NamespacedKey dummy = new NamespacedKey(namespace, "dummy");
      return getBlocksWithCustomData(chunk, dummy);
   }

   public static PersistentDataType<?, ?> getDataType(PersistentDataContainer pdc, NamespacedKey key) {
      for (PersistentDataType<?, ?> dataType : PRIMITIVE_DATA_TYPES) {
         if (pdc.has(key, dataType)) {
            return dataType;
         }
      }

      return null;
   }

   @Nullable
   public Block getBlock() {
      World world = Bukkit.getWorld(this.blockEntry.getKey());
      if (world == null) {
         return null;
      }

      BlockVector vector = this.blockEntry.getValue();
      return world.getBlockAt(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
   }

   @NotNull
   private PersistentDataContainer getPersistentDataContainer() {
      PersistentDataContainer chunkPDC = this.chunk.getPersistentDataContainer();
      PersistentDataContainer blockPDC = (PersistentDataContainer)chunkPDC.get(this.key, PersistentDataType.TAG_CONTAINER);
      return blockPDC != null ? blockPDC : chunkPDC.getAdapterContext().newPersistentDataContainer();
   }

   public boolean isProtected() {
      return this.has(PERSISTENCE_KEY, CustomBlockData.DataType.BOOLEAN);
   }

   public void setProtected(boolean isProtected) {
      if (isProtected) {
         this.set(PERSISTENCE_KEY, CustomBlockData.DataType.BOOLEAN, true);
      } else {
         this.remove(PERSISTENCE_KEY);
      }
   }

   public void clear() {
      this.pdc.getKeys().forEach(this.pdc::remove);
      this.save();
   }

   private void save() {
      setDirty(this.plugin, this.blockEntry);
      if (this.pdc.isEmpty()) {
         this.chunk.getPersistentDataContainer().remove(this.key);
      } else {
         this.chunk.getPersistentDataContainer().set(this.key, PersistentDataType.TAG_CONTAINER, this.pdc);
      }
   }

   public void copyTo(Block block, Plugin plugin) {
      CustomBlockData newCbd = new CustomBlockData(block, plugin);
      this.getKeys().forEach(key -> {
         PersistentDataType dataType = getDataType(this, key);
         if (dataType != null) {
            newCbd.set(key, dataType, this.get(key, dataType));
         }
      });
   }

   public <T, Z> void set(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataType<T, Z> persistentDataType, @NotNull Z z) {
      this.pdc.set(namespacedKey, persistentDataType, z);
      this.save();
   }

   public <T, Z> boolean has(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataType<T, Z> persistentDataType) {
      return this.pdc.has(namespacedKey, persistentDataType);
   }

   public boolean has(@NotNull NamespacedKey namespacedKey) {
      for (PersistentDataType<?, ?> type : PRIMITIVE_DATA_TYPES) {
         if (this.pdc.has(namespacedKey, type)) {
            return true;
         }
      }

      return false;
   }

   @Nullable
   public <T, Z> Z get(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataType<T, Z> persistentDataType) {
      return (Z)this.pdc.get(namespacedKey, persistentDataType);
   }

   @NotNull
   public <T, Z> Z getOrDefault(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataType<T, Z> persistentDataType, @NotNull Z z) {
      return (Z)this.pdc.getOrDefault(namespacedKey, persistentDataType, z);
   }

   @NotNull
   public Set<NamespacedKey> getKeys() {
      return this.pdc.getKeys();
   }

   public void remove(@NotNull NamespacedKey namespacedKey) {
      this.pdc.remove(namespacedKey);
      this.save();
   }

   public boolean isEmpty() {
      return this.pdc.isEmpty();
   }

   @NotNull
   public PersistentDataAdapterContext getAdapterContext() {
      return this.pdc.getAdapterContext();
   }

   @Deprecated
   @NotNull
   @CustomBlockData.PaperOnly
   public byte[] serializeToBytes() throws IOException {
      return this.pdc.serializeToBytes();
   }

   @Deprecated
   @CustomBlockData.PaperOnly
   public void readFromBytes(byte[] bytes, boolean clear) throws IOException {
      this.pdc.readFromBytes(bytes, clear);
   }

   @Deprecated
   @CustomBlockData.PaperOnly
   public void readFromBytes(byte[] bytes) throws IOException {
      this.pdc.readFromBytes(bytes);
   }

   public PersistentDataType<?, ?> getDataType(NamespacedKey key) {
      return getDataType(this, key);
   }

   static {
      checkRelocation();
      boolean tmpHasMinHeightMethod = false;

      try {
         World.class.getMethod("getMinHeight");
         tmpHasMinHeightMethod = true;
      } catch (ReflectiveOperationException var2) {
      }

      HAS_MIN_HEIGHT_METHOD = tmpHasMinHeightMethod;
   }

   private static final class DataType {
      private static final PersistentDataType<Byte, Boolean> BOOLEAN = new BooleanPersistentType();
   }

   @Retention(RetentionPolicy.CLASS)
   @Target(ElementType.METHOD)
   private @interface PaperOnly {
   }
}
