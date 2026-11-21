package dev.lone.itemsadder.molang;

import dev.lone.itemsadder.particle.BedrockParticleParser;

public class NoUvContext implements MolangContext {
   @Override
   public double dH(String var1) {
      throw BedrockParticleParser.bj("UV variable " + var1);
   }

   @Override
   public void method2090(String var1, double var2) {
      throw BedrockParticleParser.bj("UV assignment " + var1);
   }

   @Override
   public double Ii() {
      throw BedrockParticleParser.bj("UV randomness requires a finite frame set");
   }
}
