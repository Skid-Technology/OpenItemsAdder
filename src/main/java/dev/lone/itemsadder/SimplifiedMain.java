package dev.lone.itemsadder;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Entry point for the simplified build. The full plugin entry point ({@code Main}) and the
 * classes that depend on it are excluded from compilation; see compile-exclusions.txt.
 */
public final class SimplifiedMain extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().warning("Simplified build: most ItemsAdder features are not compiled in this build.");
    }
}
