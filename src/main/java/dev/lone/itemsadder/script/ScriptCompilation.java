package dev.lone.itemsadder.script;

import java.util.ArrayList;
import java.util.List;

public class ScriptCompilation {
   CompiledScript iY;
   List aCY;
   List aCZ;
   long aDa = System.currentTimeMillis();
   boolean aDb;

   ScriptCompilation() {
      this.aCY = new ArrayList();
      this.aCZ = new ArrayList();
   }

   public CompiledScript KA() {
      return this.iY;
   }

   public List KB() {
      return this.aCY;
   }

   public List KC() {
      return this.aCZ;
   }

   public long KD() {
      return System.currentTimeMillis() - this.aDa;
   }

   public boolean vz() {
      return this.aCY == null || this.aCY.isEmpty();
   }

   public boolean KE() {
      return this.aDb;
   }
}
