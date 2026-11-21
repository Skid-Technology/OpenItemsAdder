package dev.lone.itemsadder.item;

import dev.lone.itemsadder.util.SoundPlayer;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class ItemSoundEffects {
   public static final ItemSoundEffects apP = new ItemSoundEffects();
   public SoundPlayer apQ;
   public SoundPlayer apR;
   public SoundPlayer apS;
   public SoundPlayer apT;
   public SoundPlayer apU;

   public void method9993(String var1, float var2, float var3) {
      this.apQ = new SoundPlayer(var1, Sound.BLOCK_STONE_BREAK);
      this.apQ.setVolume(var2);
      this.apQ.setPitch(var3);
   }

   public void method9994(String var1, float var2, float var3) {
      this.apR = new SoundPlayer(var1, Sound.BLOCK_STONE_PLACE);
      this.apR.setVolume(var2);
      this.apR.setPitch(var3);
   }

   public void method9995(String var1, float var2, float var3) {
      this.apS = new SoundPlayer(var1, Sound.BLOCK_STONE_HIT);
      this.apS.setVolume(var2);
      this.apS.setPitch(var3);
   }

   public void method9996(String var1, float var2, float var3) {
      this.apT = new SoundPlayer(var1, Sound.BLOCK_STONE_STEP);
      this.apT.setVolume(var2);
      this.apT.setPitch(var3);
   }

   public void method9997(String var1, float var2, float var3) {
      this.apU = new SoundPlayer(var1, Sound.BLOCK_STONE_FALL);
      this.apU.setVolume(var2);
      this.apU.setPitch(var3);
   }

   public void method9998(SoundPlayer var1, Player var2, Location var3) {
      var1.play(var2, var3, SoundCategory.BLOCKS);
   }

   public void method9999(Location var1) {
      this.apQ.play(var1, SoundCategory.BLOCKS);
   }

   public void method10000(Location var1) {
      this.apR.play(var1, SoundCategory.BLOCKS);
   }

   public void method10001(Location var1) {
      this.apS.play(var1, SoundCategory.BLOCKS);
   }

   public void method10002(Location var1) {
      this.apT.play(var1, SoundCategory.BLOCKS);
   }

   public void method10003(Location var1) {
      this.apU.play(var1, SoundCategory.BLOCKS);
   }

   static {
      apP.method9993(null, 1.0F, 1.0F);
      apP.method9994(null, 1.0F, 1.0F);
      apP.method9995(null, 1.0F, 1.0F);
      apP.method9996(null, 1.0F, 1.0F);
      apP.method9997(null, 1.0F, 1.0F);
   }
}
