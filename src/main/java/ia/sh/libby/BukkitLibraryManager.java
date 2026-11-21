package ia.sh.libby;

import ia.sh.libby.classloader.URLClassLoaderHelper;
import ia.sh.libby.logging.adapters.JDKLogAdapter;
import ia.sh.libby.logging.adapters.LogAdapter;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Objects;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class BukkitLibraryManager extends LibraryManager {
   @NotNull
   private final URLClassLoaderHelper classLoader;
   @NotNull
   private final Plugin plugin;

   public BukkitLibraryManager(@NotNull Plugin plugin) {
      this(plugin, "lib");
   }

   public BukkitLibraryManager(@NotNull Plugin plugin, @NotNull String directoryName) {
      this(plugin, directoryName, new JDKLogAdapter(Objects.requireNonNull(plugin, "plugin").getLogger()));
   }

   public BukkitLibraryManager(@NotNull Plugin plugin, @NotNull String directoryName, @NotNull LogAdapter logAdapter) {
      super(logAdapter, plugin.getDataFolder().toPath(), directoryName);
      this.classLoader = new URLClassLoaderHelper((URLClassLoader)plugin.getClass().getClassLoader(), this);
      this.plugin = plugin;
   }

   @Override
   protected void addToClasspath(@NotNull Path file) {
      this.classLoader.addToClasspath(file);
   }

   @Override
   protected InputStream getResourceAsStream(@NotNull String path) {
      return this.plugin.getResource(path);
   }
}
