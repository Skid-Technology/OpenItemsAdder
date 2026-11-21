package dev.lone.itemsadder.block;

import com.google.common.collect.ImmutableMap;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemStackPairRecord;
import dev.lone.itemsadder.util.FloatVector3Base;
import dev.lone.itemsadder.util.LocationScanner;
import dev.lone.itemsadder.util.SoundUtils;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockSoundProfile {
   public final String jP;
   public final CustomItemDefinition jQ;
   private final String jR;
   private final String jS;
   private final String jT;
   public final FloatVector3Base jU;
   final ImmutableMap jV;
   public final int jW;
   private final String jX;
   private final Material jY;
   public final boolean jZ;
   public final boolean ka;
   public final double kb;
   public final int kc;
   @Nullable
   public final Integer kd;
   public final float ke;
   public final double kf;
   public final float kg;
   public final float kh;
   public final float ki;
   public final boolean kj;
   @Nullable
   public final ImmutableMap field45;

   public boolean matchesBottomBlock(@NotNull Block var1) {
      if (this.jX == null) {
         return true;
      }

      Block var2 = var1.getRelative(0, -1, 0);
      CustomBlockChunkStoreBase var3 = ContentManager.method8084().method8183(var2);
      if (var3 != null) {
         String var4 = var3.getCustomBlockId(var1);
         if (var4 != null) {
            return var4.endsWith(this.jX);
         }
      }

      BlockData var5 = var2.getBlockData();
      return var5.getAsString().endsWith(this.jX);
   }

   public boolean hasInteractionSound(BlockInteractionKind var1) {
      return this.field45 != null && this.field45.containsKey(var1);
   }

   public boolean handleInteraction(@Nullable Player param1, int param2, Location param3, BlockInteractionKind param4, boolean param5) {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.IllegalStateException: Invalid switch case set: [[const(0)], [const(1)], [const(null), null]] for selector of type Ldev/lone/itemsadder/block/BlockInteractionKind;
      //   at org.jetbrains.java.decompiler.modules.decompiler.exps.SwitchHeadExprent.checkExprTypeBounds(SwitchHeadExprent.java:63)
      //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarTypeProcessor.checkTypeExpr(VarTypeProcessor.java:156)
      //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarTypeProcessor.checkTypeExprent(VarTypeProcessor.java:137)
      //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarTypeProcessor.lambda$processVarTypes$2(VarTypeProcessor.java:125)
      //   at org.jetbrains.java.decompiler.modules.decompiler.flow.DirectGraph.iterateExprents(DirectGraph.java:114)
      //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarTypeProcessor.processVarTypes(VarTypeProcessor.java:125)
      //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarTypeProcessor.calculateVarTypes(VarTypeProcessor.java:44)
      //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarVersionsProcessor.setVarVersions(VarVersionsProcessor.java:68)
      //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarProcessor.setVarVersions(VarProcessor.java:47)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:320)
      //
      // Bytecode:
      // 000: bipush 0
      // 001: istore 6
      // 003: invokestatic dev/lone/itemsadder/util/ServerCompat.isDevMode ()Z
      // 006: ifeq 01b
      // 009: aload 4
      // 00b: invokestatic java/lang/String.valueOf (Ljava/lang/Object;)Ljava/lang/String;
      // 00e: aload 0
      // 00f: getfield dev/lone/itemsadder/block/BlockSoundProfile.jP Ljava/lang/String;
      // 012: iload 2
      // 013: invokedynamic makeConcatWithConstants (Ljava/lang/String;Ljava/lang/String;I)Ljava/lang/String; bsm=java/lang/invoke/StringConcatFactory.makeConcatWithConstants (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite; args=[ "Handling \u0001 drops for crop \u0001 at age \u0001" ]
      // 018: invokestatic dev/lone/itemsadder/util/Log.devLog (Ljava/lang/String;)V
      // 01b: aload 0
      // 01c: getfield dev/lone/itemsadder/block/BlockSoundProfile.field45 Lcom/google/common/collect/ImmutableMap;
      // 01f: ifnonnull 071
      // 022: bipush 0
      // 023: istore 7
      // 025: iload 5
      // 027: ifeq 06e
      // 02a: aload 4
      // 02c: astore 8
      // 02e: bipush 0
      // 02f: istore 9
      // 031: aload 8
      // 033: iload 9
      // 035: invokedynamic typeSwitch (Ljava/lang/Object;I)I bsm=java/lang/runtime/SwitchBootstraps.typeSwitch (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite; args=[ null.invoke Ljava/lang/Enum$EnumDesc;, null.invoke Ljava/lang/Enum$EnumDesc; ]
      // 03a: tableswitch 47 -1 1 47 26 39
      // 054: iload 6
      // 056: ifeq 06e
      // 059: aload 0
      // 05a: aload 3
      // 05b: invokevirtual dev/lone/itemsadder/block/BlockSoundProfile.playInteractSound (Lorg/bukkit/Location;)V
      // 05e: goto 06e
      // 061: aload 0
      // 062: aload 3
      // 063: invokevirtual dev/lone/itemsadder/block/BlockSoundProfile.playExtinguishSound (Lorg/bukkit/Location;)V
      // 066: goto 06e
      // 069: aload 0
      // 06a: aload 3
      // 06b: invokevirtual dev/lone/itemsadder/block/BlockSoundProfile.playBreakSound (Lorg/bukkit/Location;)V
      // 06e: iload 7
      // 070: ireturn
      // 071: aload 4
      // 073: getstatic dev/lone/itemsadder/block/BlockInteractionKind.BREAK Ldev/lone/itemsadder/block/BlockInteractionKind;
      // 076: if_acmpne 0d9
      // 079: aload 1
      // 07a: ifnull 0d9
      // 07d: aload 1
      // 07e: invokeinterface org/bukkit/entity/Player.getGameMode ()Lorg/bukkit/GameMode; 1
      // 083: getstatic org/bukkit/GameMode.CREATIVE Lorg/bukkit/GameMode;
      // 086: if_acmpne 0d9
      // 089: bipush 0
      // 08a: istore 7
      // 08c: iload 5
      // 08e: ifeq 0d6
      // 091: aload 4
      // 093: astore 8
      // 095: bipush 0
      // 096: istore 9
      // 098: aload 8
      // 09a: iload 9
      // 09c: invokedynamic typeSwitch (Ljava/lang/Object;I)I bsm=java/lang/runtime/SwitchBootstraps.typeSwitch (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite; args=[ null.invoke Ljava/lang/Enum$EnumDesc;, null.invoke Ljava/lang/Enum$EnumDesc; ]
      // 0a1: tableswitch 48 -1 1 48 27 40
      // 0bc: iload 6
      // 0be: ifeq 0d6
      // 0c1: aload 0
      // 0c2: aload 3
      // 0c3: invokevirtual dev/lone/itemsadder/block/BlockSoundProfile.playInteractSound (Lorg/bukkit/Location;)V
      // 0c6: goto 0d6
      // 0c9: aload 0
      // 0ca: aload 3
      // 0cb: invokevirtual dev/lone/itemsadder/block/BlockSoundProfile.playExtinguishSound (Lorg/bukkit/Location;)V
      // 0ce: goto 0d6
      // 0d1: aload 0
      // 0d2: aload 3
      // 0d3: invokevirtual dev/lone/itemsadder/block/BlockSoundProfile.playBreakSound (Lorg/bukkit/Location;)V
      // 0d6: iload 7
      // 0d8: ireturn
      // 0d9: bipush 0
      // 0da: istore 7
      // 0dc: bipush 0
      // 0dd: istore 8
      // 0df: aload 0
      // 0e0: getfield dev/lone/itemsadder/block/BlockSoundProfile.field45 Lcom/google/common/collect/ImmutableMap;
      // 0e3: aload 4
      // 0e5: invokevirtual com/google/common/collect/ImmutableMap.get (Ljava/lang/Object;)Ljava/lang/Object;
      // 0e8: checkcast com/google/common/collect/ImmutableList
      // 0eb: astore 9
      // 0ed: aload 9
      // 0ef: ifnull 0fa
      // 0f2: aload 9
      // 0f4: invokevirtual com/google/common/collect/ImmutableList.isEmpty ()Z
      // 0f7: ifeq 169
      // 0fa: bipush 0
      // 0fb: istore 10
      // 0fd: aload 0
      // 0fe: getfield dev/lone/itemsadder/block/BlockSoundProfile.kj Z
      // 101: ifeq 11e
      // 104: iload 8
      // 106: ifne 11e
      // 109: aload 4
      // 10b: getstatic dev/lone/itemsadder/block/BlockInteractionKind.INTERACT Ldev/lone/itemsadder/block/BlockInteractionKind;
      // 10e: if_acmpeq 11e
      // 111: iload 2
      // 112: aload 0
      // 113: getfield dev/lone/itemsadder/block/BlockSoundProfile.jW I
      // 116: if_icmpgt 11e
      // 119: aload 0
      // 11a: aload 3
      // 11b: invokevirtual dev/lone/itemsadder/block/BlockSoundProfile.dropSeed (Lorg/bukkit/Location;)V
      // 11e: iload 5
      // 120: ifeq 166
      // 123: aload 4
      // 125: astore 11
      // 127: bipush 0
      // 128: istore 12
      // 12a: aload 11
      // 12c: iload 12
      // 12e: invokedynamic typeSwitch (Ljava/lang/Object;I)I bsm=java/lang/runtime/SwitchBootstraps.typeSwitch (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite; args=[ null.invoke Ljava/lang/Enum$EnumDesc;, null.invoke Ljava/lang/Enum$EnumDesc; ]
      // 133: tableswitch 46 -1 1 46 25 38
      // 14c: iload 6
      // 14e: ifeq 166
      // 151: aload 0
      // 152: aload 3
      // 153: invokevirtual dev/lone/itemsadder/block/BlockSoundProfile.playInteractSound (Lorg/bukkit/Location;)V
      // 156: goto 166
      // 159: aload 0
      // 15a: aload 3
      // 15b: invokevirtual dev/lone/itemsadder/block/BlockSoundProfile.playExtinguishSound (Lorg/bukkit/Location;)V
      // 15e: goto 166
      // 161: aload 0
      // 162: aload 3
      // 163: invokevirtual dev/lone/itemsadder/block/BlockSoundProfile.playBreakSound (Lorg/bukkit/Location;)V
      // 166: iload 10
      // 168: ireturn
      // 169: aload 9
      // 16b: invokevirtual com/google/common/collect/ImmutableList.iterator ()Lcom/google/common/collect/UnmodifiableIterator;
      // 16e: astore 10
      // 170: aload 10
      // 172: invokeinterface java/util/Iterator.hasNext ()Z 1
      // 177: ifeq 229
      // 17a: aload 10
      // 17c: invokeinterface java/util/Iterator.next ()Ljava/lang/Object; 1
      // 181: checkcast dev/lone/itemsadder/loot/LootDropRulesImplA
      // 184: astore 11
      // 186: iload 7
      // 188: ifne 1b1
      // 18b: aload 11
      // 18d: getfield dev/lone/itemsadder/loot/LootDropRulesImplA.kH Ljava/lang/Integer;
      // 190: ifnull 1b1
      // 193: aload 11
      // 195: getfield dev/lone/itemsadder/loot/LootDropRulesImplA.kI Ljava/lang/Integer;
      // 198: ifnull 1b1
      // 19b: aload 11
      // 19d: getfield dev/lone/itemsadder/loot/LootDropRulesImplA.kH Ljava/lang/Integer;
      // 1a0: invokevirtual java/lang/Integer.intValue ()I
      // 1a3: aload 11
      // 1a5: getfield dev/lone/itemsadder/loot/LootDropRulesImplA.kI Ljava/lang/Integer;
      // 1a8: invokevirtual java/lang/Integer.intValue ()I
      // 1ab: if_icmpgt 1b1
      // 1ae: bipush 1
      // 1af: istore 7
      // 1b1: aload 11
      // 1b3: iload 2
      // 1b4: invokevirtual dev/lone/itemsadder/loot/LootDropRulesImplA.method2014 (I)Z
      // 1b7: ifeq 226
      // 1ba: aload 11
      // 1bc: aload 3
      // 1bd: invokevirtual org/bukkit/Location.getWorld ()Lorg/bukkit/World;
      // 1c0: invokevirtual dev/lone/itemsadder/loot/LootDropRulesImplA.method2010 (Lorg/bukkit/World;)Z
      // 1c3: ifeq 226
      // 1c6: aload 11
      // 1c8: aload 3
      // 1c9: invokevirtual org/bukkit/Location.getBlock ()Lorg/bukkit/block/Block;
      // 1cc: invokeinterface org/bukkit/block/Block.getBiome ()Lorg/bukkit/block/Biome; 1
      // 1d1: invokevirtual dev/lone/itemsadder/loot/LootDropRulesImplA.method2009 (Lorg/bukkit/block/Biome;)Z
      // 1d4: ifeq 226
      // 1d7: aload 11
      // 1d9: aload 3
      // 1da: aload 1
      // 1db: invokevirtual dev/lone/itemsadder/loot/LootDropRulesImplA.method2012 (Lorg/bukkit/Location;Lorg/bukkit/entity/Player;)Z
      // 1de: ifeq 226
      // 1e1: aload 11
      // 1e3: aload 1
      // 1e4: invokevirtual dev/lone/itemsadder/loot/LootDropRulesImplA.method2016 (Lorg/bukkit/entity/Player;)Z
      // 1e7: ifeq 226
      // 1ea: aload 11
      // 1ec: aload 1
      // 1ed: aload 3
      // 1ee: invokevirtual dev/lone/itemsadder/loot/LootDropRulesImplA.method2003 (Lorg/bukkit/entity/Player;Lorg/bukkit/Location;)Z
      // 1f1: ifeq 1f7
      // 1f4: bipush 1
      // 1f5: istore 8
      // 1f7: aload 11
      // 1f9: getfield dev/lone/itemsadder/loot/LootDropRulesImplA.kJ Ljava/lang/Integer;
      // 1fc: ifnull 226
      // 1ff: bipush 1
      // 200: istore 6
      // 202: bipush 0
      // 203: iload 2
      // 204: aload 11
      // 206: getfield dev/lone/itemsadder/loot/LootDropRulesImplA.kJ Ljava/lang/Integer;
      // 209: invokevirtual java/lang/Integer.intValue ()I
      // 20c: isub
      // 20d: invokestatic java/lang/Math.max (II)I
      // 210: istore 2
      // 211: invokestatic dev/lone/itemsadder/core/ContentManager.method8084 ()Ldev/lone/itemsadder/core/ContentManager;
      // 214: invokevirtual dev/lone/itemsadder/core/ContentManager.method8142 ()Ldev/lone/itemsadder/block/BlockInteractionListener;
      // 217: getfield dev/lone/itemsadder/block/BlockInteractionListener.mZ Ldev/lone/itemsadder/block/ChunkBlockTracker;
      // 21a: aload 3
      // 21b: invokevirtual org/bukkit/Location.getBlock ()Lorg/bukkit/block/Block;
      // 21e: aload 0
      // 21f: getfield dev/lone/itemsadder/block/BlockSoundProfile.jP Ljava/lang/String;
      // 222: iload 2
      // 223: invokevirtual dev/lone/itemsadder/block/ChunkBlockTracker.setBlockStateInt (Lorg/bukkit/block/Block;Ljava/lang/String;I)V
      // 226: goto 170
      // 229: aload 0
      // 22a: getfield dev/lone/itemsadder/block/BlockSoundProfile.kj Z
      // 22d: ifeq 273
      // 230: iload 8
      // 232: ifne 273
      // 235: aload 4
      // 237: getstatic dev/lone/itemsadder/block/BlockInteractionKind.INTERACT Ldev/lone/itemsadder/block/BlockInteractionKind;
      // 23a: if_acmpeq 273
      // 23d: iload 2
      // 23e: aload 0
      // 23f: getfield dev/lone/itemsadder/block/BlockSoundProfile.jW I
      // 242: if_icmpgt 273
      // 245: aload 0
      // 246: aload 3
      // 247: invokevirtual dev/lone/itemsadder/block/BlockSoundProfile.dropSeed (Lorg/bukkit/Location;)V
      // 24a: goto 273
      // 24d: astore 13
      // 24f: aload 0
      // 250: getfield dev/lone/itemsadder/block/BlockSoundProfile.kj Z
      // 253: ifeq 270
      // 256: iload 8
      // 258: ifne 270
      // 25b: aload 4
      // 25d: getstatic dev/lone/itemsadder/block/BlockInteractionKind.INTERACT Ldev/lone/itemsadder/block/BlockInteractionKind;
      // 260: if_acmpeq 270
      // 263: iload 2
      // 264: aload 0
      // 265: getfield dev/lone/itemsadder/block/BlockSoundProfile.jW I
      // 268: if_icmpgt 270
      // 26b: aload 0
      // 26c: aload 3
      // 26d: invokevirtual dev/lone/itemsadder/block/BlockSoundProfile.dropSeed (Lorg/bukkit/Location;)V
      // 270: aload 13
      // 272: athrow
      // 273: bipush 1
      // 274: istore 9
      // 276: iload 5
      // 278: ifeq 2be
      // 27b: aload 4
      // 27d: astore 10
      // 27f: bipush 0
      // 280: istore 11
      // 282: aload 10
      // 284: iload 11
      // 286: invokedynamic typeSwitch (Ljava/lang/Object;I)I bsm=java/lang/runtime/SwitchBootstraps.typeSwitch (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite; args=[ null.invoke Ljava/lang/Enum$EnumDesc;, null.invoke Ljava/lang/Enum$EnumDesc; ]
      // 28b: tableswitch 46 -1 1 46 25 38
      // 2a4: iload 6
      // 2a6: ifeq 2be
      // 2a9: aload 0
      // 2aa: aload 3
      // 2ab: invokevirtual dev/lone/itemsadder/block/BlockSoundProfile.playInteractSound (Lorg/bukkit/Location;)V
      // 2ae: goto 2be
      // 2b1: aload 0
      // 2b2: aload 3
      // 2b3: invokevirtual dev/lone/itemsadder/block/BlockSoundProfile.playExtinguishSound (Lorg/bukkit/Location;)V
      // 2b6: goto 2be
      // 2b9: aload 0
      // 2ba: aload 3
      // 2bb: invokevirtual dev/lone/itemsadder/block/BlockSoundProfile.playBreakSound (Lorg/bukkit/Location;)V
      // 2be: iload 9
      // 2c0: ireturn
      // 2c1: astore 14
      // 2c3: iload 5
      // 2c5: ifeq 30e
      // 2c8: aload 4
      // 2ca: astore 15
      // 2cc: bipush 0
      // 2cd: istore 16
      // 2cf: aload 15
      // 2d1: iload 16
      // 2d3: invokedynamic typeSwitch (Ljava/lang/Object;I)I bsm=java/lang/runtime/SwitchBootstraps.typeSwitch (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite; args=[ null.invoke Ljava/lang/Enum$EnumDesc;, null.invoke Ljava/lang/Enum$EnumDesc; ]
      // 2d8: tableswitch 49 -1 1 49 28 41
      // 2f4: iload 6
      // 2f6: ifeq 30e
      // 2f9: aload 0
      // 2fa: aload 3
      // 2fb: invokevirtual dev/lone/itemsadder/block/BlockSoundProfile.playInteractSound (Lorg/bukkit/Location;)V
      // 2fe: goto 30e
      // 301: aload 0
      // 302: aload 3
      // 303: invokevirtual dev/lone/itemsadder/block/BlockSoundProfile.playExtinguishSound (Lorg/bukkit/Location;)V
      // 306: goto 30e
      // 309: aload 0
      // 30a: aload 3
      // 30b: invokevirtual dev/lone/itemsadder/block/BlockSoundProfile.playBreakSound (Lorg/bukkit/Location;)V
      // 30e: aload 14
      // 310: athrow
      // try (81 -> 94): 231 null
      // try (134 -> 215): 231 null
      // try (231 -> 232): 231 null
      // try (2 -> 16): 276 null
      // try (41 -> 52): 276 null
      // try (77 -> 109): 276 null
      // try (134 -> 251): 276 null
      // try (276 -> 277): 276 null
   }

   public void dropSeed(@NotNull Location var1) {
      LocationScanner.dropItem(var1, this.jQ.Ds());
   }

   public boolean matchesPlacementBase(@NotNull Block var1) {
      if (this.jY == null && this.jX == null) {
         return true;
      } else {
         Block var2 = var1.getRelative(0, -1, 0);
         if (this.jX != null) {
            String var3 = ContentManager.method8186(var1);
            return var3 == null ? false : this.jX.endsWith(var3);
         } else {
            return var2.getType() == this.jY;
         }
      }
   }

   public void playBreakSound(Location var1) {
      this.playSound(var1, this.jS);
   }

   public void playPlaceSound(Location var1) {
      this.playSound(var1, this.jR);
   }

   public void playInteractSound(Location var1) {
      if (this.jT == null) {
         this.playSound(var1, this.jS);
      } else {
         this.playSound(var1, this.jT);
      }
   }

   public void playSound(Location location, String sound) {
      if (sound != null) {
         float var3 = 0.8F + (float)(Math.random() * 0.4F - 0.2F);
         SoundUtils.playSound(location, sound, SoundCategory.BLOCKS, 1.0F, var3);
      }
   }

   public void playExtinguishSound(Location var1) {
      this.playSound(var1, Sound.ENTITY_GENERIC_EXTINGUISH_FIRE.getKey().toString());
   }

   @Nullable
   public ItemStackPairRecord getStatusItem(int var1) {
      return this.getStatusItemByAge((byte)var1);
   }

   @Nullable
   public ItemStackPairRecord getStatusItemByAge(byte var1) {
      return (ItemStackPairRecord)this.jV.get(var1);
   }

   @Generated
   private static double defaultAvgSecondsPerStage() {
      return 240.0;
   }

   @Generated
   private static int defaultMinLight() {
      return 9;
   }

   @Generated
   private static float defaultBonemealGrowthMultiplier() {
      return 0.2F;
   }

   @Generated
   private static double defaultMaxGrowthMoistureMultiplier() {
      return 0.5;
   }

   @Generated
   private static float defaultRainGrowthMultiplier() {
      return 1.5F;
   }

   @Generated
   private static float defaultThunderGrowthMultiplier() {
      return -1.0F;
   }

   @Generated
   private static float defaultSnowGrowthMultiplier() {
      return -1.5F;
   }

   @Generated
   private static boolean defaultDropSeedIfImmature() {
      return true;
   }

   @Generated
   BlockSoundProfile(
      String var1,
      CustomItemDefinition var2,
      String var3,
      String var4,
      String var5,
      FloatVector3Base var6,
      ImmutableMap var7,
      int var8,
      String var9,
      Material var10,
      boolean var11,
      boolean var12,
      double var13,
      int var15,
      @Nullable Integer var16,
      float var17,
      double var18,
      float var20,
      float var21,
      float var22,
      boolean var23,
      @Nullable ImmutableMap var24
   ) {
      this.jP = var1;
      this.jQ = var2;
      this.jR = var3;
      this.jS = var4;
      this.jT = var5;
      this.jU = var6;
      this.jV = var7;
      this.jW = var8;
      this.jX = var9;
      this.jY = var10;
      this.jZ = var11;
      this.ka = var12;
      this.kb = var13;
      this.kc = var15;
      this.kd = var16;
      this.ke = var17;
      this.kf = var18;
      this.kg = var20;
      this.kh = var21;
      this.ki = var22;
      this.kj = var23;
      this.field45 = var24;
   }

   @Generated
   public static BlockSoundProfileBuilder builder() {
      return new BlockSoundProfileBuilder();
   }
}
