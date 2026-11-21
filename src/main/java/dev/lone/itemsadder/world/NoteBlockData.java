package dev.lone.itemsadder.world;

import org.bukkit.Instrument;
import org.jetbrains.annotations.Nullable;

public class NoteBlockData {
   public Instrument instrument;
   public byte note;
   @Nullable
   public String customSound = null;
   public Object handle;
   public float volume;
   public boolean active;
}
