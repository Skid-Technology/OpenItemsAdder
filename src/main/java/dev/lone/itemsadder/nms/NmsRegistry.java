package dev.lone.itemsadder.nms;

import beer.devs.fastnbt.nms.Version;
import dev.lone.itemsadder.entity.AbstractFakeEntity;
import dev.lone.itemsadder.entity.AbstractItemDisplayEntity;
import dev.lone.itemsadder.entity.BodyRotationController;
import dev.lone.itemsadder.entity.EntityEventSink;
import dev.lone.itemsadder.entity.PacketDisplayBridge;
import dev.lone.itemsadder.entity.PlayerInteractionHandler;
import dev.lone.itemsadder.event.AbstractPlayerTrigger;
import dev.lone.itemsadder.internal.SwitchMap7;
import dev.lone.itemsadder.item.ItemRegistryBridge;
import dev.lone.itemsadder.loot.MerchantOfferInterceptor;
import dev.lone.itemsadder.particle.ParticleResolver;
import dev.lone.itemsadder.recipe.AbstractRecipeManager;
import dev.lone.itemsadder.recipe.RecipeManager;
import dev.lone.itemsadder.text.NmsChatAdapter;
import dev.lone.itemsadder.util.AnnouncementSink;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.RaycastStrategy;
import dev.lone.itemsadder.util.ServerCompat;
import dev.lone.itemsadder.world.BlockEffectsAdapter;
import dev.lone.itemsadder.world.ChunkBlockHook;
import dev.lone.itemsadder.world.TreeFeatureHook;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

public class NmsRegistry {
   private static final Map aNk = new HashMap();

   @Nullable
   public static Field method2774(Class var0, Class var1) {
      return ServerCompat.findFieldByType(var0, var1);
   }

   @Nullable
   public static Constructor method2775(Class var0, Class... var1) {
      try {
         return var0.getConstructor(var1);
      } catch (NoSuchMethodException var5) {
         try {
            Constructor var3 = var0.getDeclaredConstructor(var1);
            var3.setAccessible(true);
            return var3;
         } catch (NoSuchMethodException var4) {
            return null;
         }
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private static Class method2776(Class var0) {
      Version var1 = Version.get();
      if (var1 == Version.v1_21) {
         var1 = Version.v1_21_1;
      }

      Class var2 = null;
      if (NmsBlockAdapter.class == var0) {
         var2 = switch (SwitchMap7.aNl[var1.ordinal()]) {
            case 1 -> Class.forName("itemsadder.m.als");
            case 2 -> Class.forName("itemsadder.m.alq");
            case 3 -> Class.forName("itemsadder.m.alo");
            case 4 -> Class.forName("itemsadder.m.alm");
            case 5 -> Class.forName("itemsadder.m.aky");
            case 6 -> Class.forName("itemsadder.m.akw");
            case 7 -> Class.forName("itemsadder.m.alk");
            case 8 -> Class.forName("itemsadder.m.ali");
            case 9 -> Class.forName("itemsadder.m.alg");
            case 10 -> Class.forName("itemsadder.m.ale");
            case 11 -> Class.forName("itemsadder.m.alc");
            case 12 -> Class.forName("itemsadder.m.ala");
            case 13 -> Class.forName("itemsadder.m.aku");
            case 14 -> Class.forName("itemsadder.m.aks");
            default -> null;
         };
      } else if (BlockEffectsAdapter.class == var0) {
         var2 = switch (SwitchMap7.aNl[var1.ordinal()]) {
            case 1 -> Class.forName("itemsadder.m.amw");
            case 2 -> Class.forName("itemsadder.m.amu");
            case 3 -> Class.forName("itemsadder.m.ams");
            case 4 -> Class.forName("itemsadder.m.amq");
            case 5 -> Class.forName("itemsadder.m.amc");
            case 6 -> Class.forName("itemsadder.m.ama");
            case 7 -> Class.forName("itemsadder.m.amo");
            case 8 -> Class.forName("itemsadder.m.amm");
            case 9 -> Class.forName("itemsadder.m.amk");
            case 10 -> Class.forName("itemsadder.m.ami");
            case 11 -> Class.forName("itemsadder.m.amg");
            case 12 -> Class.forName("itemsadder.m.ame");
            case 13 -> Class.forName("itemsadder.m.aly");
            case 14 -> Class.forName("itemsadder.m.alw");
            default -> null;
         };
      } else if (BodyRotationController.class == var0) {
         var2 = switch (SwitchMap7.aNl[var1.ordinal()]) {
            case 1 -> Class.forName("itemsadder.m.ann");
            case 2 -> Class.forName("itemsadder.m.anm");
            case 3 -> Class.forName("itemsadder.m.anl");
            case 4 -> Class.forName("itemsadder.m.ank");
            case 5 -> Class.forName("itemsadder.m.and");
            case 6 -> Class.forName("itemsadder.m.anc");
            case 7 -> Class.forName("itemsadder.m.anj");
            case 8 -> Class.forName("itemsadder.m.ani");
            case 9 -> Class.forName("itemsadder.m.anh");
            case 10 -> Class.forName("itemsadder.m.ang");
            case 11 -> Class.forName("itemsadder.m.anf");
            case 12 -> Class.forName("itemsadder.m.ane");
            case 13 -> Class.forName("itemsadder.m.anb");
            case 14 -> Class.forName("itemsadder.m.ana");
            default -> null;
         };
      } else if (NmsChatAdapter.class == var0) {
         var2 = switch (SwitchMap7.aNl[var1.ordinal()]) {
            case 1 -> Class.forName("itemsadder.m.aod");
            case 2 -> Class.forName("itemsadder.m.aoc");
            case 3 -> Class.forName("itemsadder.m.aob");
            case 4 -> Class.forName("itemsadder.m.aoa");
            case 5 -> Class.forName("itemsadder.m.ant");
            case 6 -> Class.forName("itemsadder.m.ans");
            case 7 -> Class.forName("itemsadder.m.anz");
            case 8 -> Class.forName("itemsadder.m.any");
            case 9 -> Class.forName("itemsadder.m.anx");
            case 10 -> Class.forName("itemsadder.m.anw");
            case 11 -> Class.forName("itemsadder.m.anv");
            case 12 -> Class.forName("itemsadder.m.anu");
            case 13 -> Class.forName("itemsadder.m.anr");
            case 14 -> Class.forName("itemsadder.m.anq");
            default -> null;
         };
      } else if (AbstractFakeEntity.class == var0) {
         var2 = switch (SwitchMap7.aNl[var1.ordinal()]) {
            case 1 -> Class.forName("itemsadder.m.api");
            case 2 -> Class.forName("itemsadder.m.aph");
            case 3 -> Class.forName("itemsadder.m.apg");
            case 4 -> Class.forName("itemsadder.m.apf");
            case 5 -> Class.forName("itemsadder.m.aoy");
            case 6 -> Class.forName("itemsadder.m.aox");
            case 7 -> Class.forName("itemsadder.m.ape");
            case 8 -> Class.forName("itemsadder.m.apd");
            case 9 -> Class.forName("itemsadder.m.apc");
            case 10 -> Class.forName("itemsadder.m.apb");
            case 11 -> Class.forName("itemsadder.m.apa");
            case 12 -> Class.forName("itemsadder.m.aoz");
            case 13 -> Class.forName("itemsadder.m.aow");
            case 14 -> Class.forName("itemsadder.m.aov");
            default -> null;
         };
      } else if (NmsCommandSender.class == var0) {
         var2 = switch (SwitchMap7.aNl[var1.ordinal()]) {
            case 1 -> Class.forName("itemsadder.m.aqm");
            case 2 -> Class.forName("itemsadder.m.aqk");
            case 3 -> Class.forName("itemsadder.m.aqi");
            case 4 -> Class.forName("itemsadder.m.aqg");
            case 5 -> Class.forName("itemsadder.m.aps");
            case 6 -> Class.forName("itemsadder.m.apq");
            case 7 -> Class.forName("itemsadder.m.aqe");
            case 8 -> Class.forName("itemsadder.m.aqc");
            case 9 -> Class.forName("itemsadder.m.aqa");
            case 10 -> Class.forName("itemsadder.m.apy");
            case 11 -> Class.forName("itemsadder.m.apw");
            case 12 -> Class.forName("itemsadder.m.apu");
            case 13 -> Class.forName("itemsadder.m.apo");
            case 14 -> Class.forName("itemsadder.m.apm");
            default -> null;
         };
      } else if (NmsWorldAdapter.class == var0) {
         var2 = switch (SwitchMap7.aNl[var1.ordinal()]) {
            case 1 -> Class.forName("itemsadder.m.are");
            case 2 -> Class.forName("itemsadder.m.ard");
            case 3 -> Class.forName("itemsadder.m.arc");
            case 4 -> Class.forName("itemsadder.m.arb");
            case 5 -> Class.forName("itemsadder.m.aqu");
            case 6 -> Class.forName("itemsadder.m.aqt");
            case 7 -> Class.forName("itemsadder.m.ara");
            case 8 -> Class.forName("itemsadder.m.aqz");
            case 9 -> Class.forName("itemsadder.m.aqy");
            case 10 -> Class.forName("itemsadder.m.aqx");
            case 11 -> Class.forName("itemsadder.m.aqw");
            case 12 -> Class.forName("itemsadder.m.aqv");
            case 13 -> Class.forName("itemsadder.m.aqs");
            case 14 -> Class.forName("itemsadder.m.aqr");
            default -> null;
         };
      } else if (NmsEntityAdapter.class == var0) {
         var2 = switch (SwitchMap7.aNl[var1.ordinal()]) {
            case 1 -> Class.forName("itemsadder.m.asm");
            case 2 -> Class.forName("itemsadder.m.ask");
            case 3 -> Class.forName("itemsadder.m.asi");
            case 4 -> Class.forName("itemsadder.m.asg");
            case 5 -> Class.forName("itemsadder.m.ars");
            case 6 -> Class.forName("itemsadder.m.arq");
            case 7 -> Class.forName("itemsadder.m.ase");
            case 8 -> Class.forName("itemsadder.m.asc");
            case 9 -> Class.forName("itemsadder.m.asa");
            case 10 -> Class.forName("itemsadder.m.ary");
            case 11 -> Class.forName("itemsadder.m.arw");
            case 12 -> Class.forName("itemsadder.m.aru");
            case 13 -> Class.forName("itemsadder.m.aro");
            case 14 -> Class.forName("itemsadder.m.arm");
            default -> null;
         };
      } else if (BlockSetterAdapter.class == var0) {
         var2 = switch (SwitchMap7.aNl[var1.ordinal()]) {
            case 1 -> Class.forName("itemsadder.m.auf");
            case 2 -> Class.forName("itemsadder.m.aue");
            case 3 -> Class.forName("itemsadder.m.aud");
            case 4 -> Class.forName("itemsadder.m.auc");
            case 5 -> Class.forName("itemsadder.m.atv");
            case 6 -> Class.forName("itemsadder.m.atu");
            case 7 -> Class.forName("itemsadder.m.aub");
            case 8 -> Class.forName("itemsadder.m.aua");
            case 9 -> Class.forName("itemsadder.m.atz");
            case 10 -> Class.forName("itemsadder.m.aty");
            case 11 -> Class.forName("itemsadder.m.atx");
            case 12 -> Class.forName("itemsadder.m.atw");
            case 13 -> Class.forName("itemsadder.m.att");
            case 14 -> Class.forName("itemsadder.m.ats");
            default -> null;
         };
      } else if (GameModeAdapter.class == var0) {
         var2 = switch (SwitchMap7.aNl[var1.ordinal()]) {
            case 1 -> Class.forName("itemsadder.m.auw");
            case 2 -> Class.forName("itemsadder.m.auv");
            case 3 -> Class.forName("itemsadder.m.auu");
            case 4 -> Class.forName("itemsadder.m.aut");
            case 5 -> Class.forName("itemsadder.m.aum");
            case 6 -> Class.forName("itemsadder.m.aul");
            case 7 -> Class.forName("itemsadder.m.aus");
            case 8 -> Class.forName("itemsadder.m.aur");
            case 9 -> Class.forName("itemsadder.m.auq");
            case 10 -> Class.forName("itemsadder.m.aup");
            case 11 -> Class.forName("itemsadder.m.auo");
            case 12 -> Class.forName("itemsadder.m.aun");
            case 13 -> Class.forName("itemsadder.m.auk");
            case 14 -> Class.forName("itemsadder.m.auj");
            default -> null;
         };
      } else if (ItemRegistryBridge.class == var0) {
         var2 = switch (SwitchMap7.aNl[var1.ordinal()]) {
            case 1 -> Class.forName("itemsadder.m.avv");
            case 2 -> Class.forName("itemsadder.m.avu");
            case 3 -> Class.forName("itemsadder.m.avt");
            case 4 -> Class.forName("itemsadder.m.avs");
            case 5 -> Class.forName("itemsadder.m.avl");
            case 6 -> Class.forName("itemsadder.m.avk");
            case 7 -> Class.forName("itemsadder.m.avr");
            case 8 -> Class.forName("itemsadder.m.avq");
            case 9 -> Class.forName("itemsadder.m.avp");
            case 10 -> Class.forName("itemsadder.m.avo");
            case 11 -> Class.forName("itemsadder.m.avn");
            case 12 -> Class.forName("itemsadder.m.avm");
            case 13 -> Class.forName("itemsadder.m.avj");
            case 14 -> Class.forName("itemsadder.m.avi");
            default -> null;
         };
      } else if (AnnouncementSink.class == var0) {
         var2 = switch (SwitchMap7.aNl[var1.ordinal()]) {
            case 1 -> Class.forName("itemsadder.m.bfj");
            case 2 -> Class.forName("itemsadder.m.bfi");
            case 3 -> Class.forName("itemsadder.m.bfh");
            case 4 -> Class.forName("itemsadder.m.bfg");
            case 5 -> Class.forName("itemsadder.m.bez");
            case 6 -> Class.forName("itemsadder.m.bey");
            case 7 -> Class.forName("itemsadder.m.bff");
            case 8 -> Class.forName("itemsadder.m.bfe");
            case 9 -> Class.forName("itemsadder.m.bfd");
            case 10 -> Class.forName("itemsadder.m.bfc");
            case 11 -> Class.forName("itemsadder.m.bfb");
            case 12 -> Class.forName("itemsadder.m.bfa");
            case 13 -> Class.forName("itemsadder.m.bex");
            case 14 -> Class.forName("itemsadder.m.bew");
            default -> null;
         };
      } else if (PacketDisplayBridge.class == var0) {
         var2 = switch (SwitchMap7.aNl[var1.ordinal()]) {
            case 1 -> Class.forName("itemsadder.m.bgw");
            case 2 -> Class.forName("itemsadder.m.bgu");
            case 3 -> Class.forName("itemsadder.m.bgs");
            case 4 -> Class.forName("itemsadder.m.bgq");
            case 5 -> Class.forName("itemsadder.m.bgc");
            case 6 -> Class.forName("itemsadder.m.bga");
            case 7 -> Class.forName("itemsadder.m.bgo");
            case 8 -> Class.forName("itemsadder.m.bgm");
            case 9 -> Class.forName("itemsadder.m.bgk");
            case 10 -> Class.forName("itemsadder.m.bgi");
            case 11 -> Class.forName("itemsadder.m.bgg");
            case 12 -> Class.forName("itemsadder.m.bge");
            case 13 -> Class.forName("itemsadder.m.bfy");
            case 14 -> Class.forName("itemsadder.m.bfw");
            default -> null;
         };
      } else if (ParticleResolver.class == var0) {
         var2 = switch (SwitchMap7.aNl[var1.ordinal()]) {
            case 1 -> Class.forName("itemsadder.m.bhn");
            case 2 -> Class.forName("itemsadder.m.bhm");
            case 3 -> Class.forName("itemsadder.m.bhl");
            case 4 -> Class.forName("itemsadder.m.bhk");
            case 5 -> Class.forName("itemsadder.m.bhd");
            case 6 -> Class.forName("itemsadder.m.bhc");
            case 7 -> Class.forName("itemsadder.m.bhj");
            case 8 -> Class.forName("itemsadder.m.bhi");
            case 9 -> Class.forName("itemsadder.m.bhh");
            case 10 -> Class.forName("itemsadder.m.bhg");
            case 11 -> Class.forName("itemsadder.m.bhf");
            case 12 -> Class.forName("itemsadder.m.bhe");
            case 13 -> Class.forName("itemsadder.m.bhb");
            case 14 -> Class.forName("itemsadder.m.bha");
            default -> null;
         };
      } else if (PlayerInteractionHandler.class == var0) {
         var2 = switch (SwitchMap7.aNl[var1.ordinal()]) {
            case 1 -> Class.forName("itemsadder.m.bio");
            case 2 -> Class.forName("itemsadder.m.bil");
            case 3 -> Class.forName("itemsadder.m.bii");
            case 4 -> Class.forName("itemsadder.m.bif");
            case 5 -> Class.forName("itemsadder.m.bhw");
            case 6 -> Class.forName("itemsadder.m.bht");
            case 7 -> Class.forName("itemsadder.m.bie");
            case 8 -> Class.forName("itemsadder.m.bid");
            case 9 -> Class.forName("itemsadder.m.bic");
            case 10 -> Class.forName("itemsadder.m.bib");
            case 11 -> Class.forName("itemsadder.m.bia");
            case 12 -> Class.forName("itemsadder.m.bhz");
            case 13 -> Class.forName("itemsadder.m.bhs");
            case 14 -> Class.forName("itemsadder.m.bhr");
            default -> null;
         };
      } else if (RaycastStrategy.class == var0) {
         var2 = switch (SwitchMap7.aNl[var1.ordinal()]) {
            case 1 -> Class.forName("itemsadder.m.bjk");
            case 2 -> Class.forName("itemsadder.m.bjj");
            case 3 -> Class.forName("itemsadder.m.bji");
            case 4 -> Class.forName("itemsadder.m.bjh");
            case 5 -> Class.forName("itemsadder.m.bja");
            case 6 -> Class.forName("itemsadder.m.biz");
            case 7 -> Class.forName("itemsadder.m.bjg");
            case 8 -> Class.forName("itemsadder.m.bjf");
            case 9 -> Class.forName("itemsadder.m.bje");
            case 10 -> Class.forName("itemsadder.m.bjd");
            case 11 -> Class.forName("itemsadder.m.bjc");
            case 12 -> Class.forName("itemsadder.m.bjb");
            case 13 -> Class.forName("itemsadder.m.biy");
            case 14 -> Class.forName("itemsadder.m.bix");
            default -> null;
         };
      } else if (AbstractPlayerTrigger.class == var0) {
         var2 = switch (SwitchMap7.aNl[var1.ordinal()]) {
            case 1 -> Class.forName("itemsadder.m.bet");
            case 2 -> Class.forName("itemsadder.m.bes");
            case 3 -> Class.forName("itemsadder.m.ber");
            case 4 -> Class.forName("itemsadder.m.beq");
            case 5 -> Class.forName("itemsadder.m.bej");
            case 6 -> Class.forName("itemsadder.m.bei");
            case 7 -> Class.forName("itemsadder.m.bep");
            case 8 -> Class.forName("itemsadder.m.beo");
            case 9 -> Class.forName("itemsadder.m.ben");
            case 10 -> Class.forName("itemsadder.m.bem");
            case 11 -> Class.forName("itemsadder.m.bel");
            case 12 -> Class.forName("itemsadder.m.bek");
            case 13 -> Class.forName("itemsadder.m.beh");
            case 14 -> Class.forName("itemsadder.m.beg");
            default -> null;
         };
      } else if (AbstractRecipeManager.class == var0) {
         var2 = switch (SwitchMap7.aNl[var1.ordinal()]) {
            case 1 -> Class.forName("itemsadder.m.bld");
            case 2 -> Class.forName("itemsadder.m.blb");
            case 3 -> Class.forName("itemsadder.m.bkz");
            case 4 -> Class.forName("itemsadder.m.bkx");
            case 5 -> Class.forName("itemsadder.m.bkj");
            case 6 -> Class.forName("itemsadder.m.bkh");
            case 7 -> Class.forName("itemsadder.m.bkv");
            case 8 -> Class.forName("itemsadder.m.bkt");
            case 9 -> Class.forName("itemsadder.m.bkr");
            case 10 -> Class.forName("itemsadder.m.bkp");
            case 11 -> Class.forName("itemsadder.m.bkn");
            case 12 -> Class.forName("itemsadder.m.bkl");
            case 13 -> Class.forName("itemsadder.m.bkf");
            case 14 -> Class.forName("itemsadder.m.bkd");
            default -> null;
         };
      } else if (AbstractItemDisplayEntity.class == var0) {
         var2 = switch (SwitchMap7.aNl[var1.ordinal()]) {
            case 1 -> Class.forName("itemsadder.m.blu");
            case 2 -> Class.forName("itemsadder.m.blt");
            case 3 -> Class.forName("itemsadder.m.bls");
            case 4 -> Class.forName("itemsadder.m.blr");
            case 5 -> Class.forName("itemsadder.m.blk");
            case 6 -> Class.forName("itemsadder.m.blj");
            case 7 -> Class.forName("itemsadder.m.blq");
            case 8 -> Class.forName("itemsadder.m.blp");
            case 9 -> Class.forName("itemsadder.m.blo");
            case 10 -> Class.forName("itemsadder.m.bln");
            case 11 -> Class.forName("itemsadder.m.blm");
            case 12 -> Class.forName("itemsadder.m.bll");
            case 13 -> Class.forName("itemsadder.m.bli");
            case 14 -> Class.forName("itemsadder.m.blh");
            default -> null;
         };
      } else if (EntityEventSink.class == var0) {
         var2 = switch (SwitchMap7.aNl[var1.ordinal()]) {
            case 1 -> Class.forName("itemsadder.m.bmk");
            case 2 -> Class.forName("itemsadder.m.bmj");
            case 3 -> Class.forName("itemsadder.m.bmi");
            case 4 -> Class.forName("itemsadder.m.bmh");
            case 5 -> Class.forName("itemsadder.m.bma");
            case 6 -> Class.forName("itemsadder.m.blz");
            case 7 -> Class.forName("itemsadder.m.bmg");
            case 8 -> Class.forName("itemsadder.m.bmf");
            case 9 -> Class.forName("itemsadder.m.bme");
            case 10 -> Class.forName("itemsadder.m.bmd");
            case 11 -> Class.forName("itemsadder.m.bmc");
            case 12 -> Class.forName("itemsadder.m.bmb");
            case 13 -> Class.forName("itemsadder.m.bly");
            case 14 -> Class.forName("itemsadder.m.blx");
            default -> null;
         };
      } else if (MerchantOfferInterceptor.class == var0) {
         var2 = switch (SwitchMap7.aNl[var1.ordinal()]) {
            case 1 -> Class.forName("itemsadder.m.bnb");
            case 2 -> Class.forName("itemsadder.m.bna");
            case 3 -> Class.forName("itemsadder.m.bmz");
            case 4 -> Class.forName("itemsadder.m.bmy");
            case 5 -> Class.forName("itemsadder.m.bmr");
            case 6 -> Class.forName("itemsadder.m.bmq");
            case 7 -> Class.forName("itemsadder.m.bmx");
            case 8 -> Class.forName("itemsadder.m.bmw");
            case 9 -> Class.forName("itemsadder.m.bmv");
            case 10 -> Class.forName("itemsadder.m.bmu");
            case 11 -> Class.forName("itemsadder.m.bmt");
            case 12 -> Class.forName("itemsadder.m.bms");
            case 13 -> Class.forName("itemsadder.m.bmp");
            case 14 -> Class.forName("itemsadder.m.bmo");
            default -> null;
         };
      } else if (TreeFeatureHook.class == var0) {
         var2 = switch (SwitchMap7.aNl[var1.ordinal()]) {
            case 1 -> Class.forName("itemsadder.m.boi");
            case 2 -> Class.forName("itemsadder.m.bog");
            case 3 -> Class.forName("itemsadder.m.boe");
            case 4 -> Class.forName("itemsadder.m.boc");
            case 5 -> Class.forName("itemsadder.m.bno");
            case 6 -> Class.forName("itemsadder.m.bnm");
            case 7 -> Class.forName("itemsadder.m.boa");
            case 8 -> Class.forName("itemsadder.m.bny");
            case 9 -> Class.forName("itemsadder.m.bnw");
            case 10 -> Class.forName("itemsadder.m.bnu");
            case 11 -> Class.forName("itemsadder.m.bns");
            case 12 -> Class.forName("itemsadder.m.bnq");
            case 13 -> Class.forName("itemsadder.m.bnk");
            case 14 -> Class.forName("itemsadder.m.bni");
            default -> null;
         };
      } else if (ChunkBlockHook.class == var0) {
         var2 = switch (SwitchMap7.aNl[var1.ordinal()]) {
            case 1 -> Class.forName("itemsadder.m.bpa");
            case 2 -> Class.forName("itemsadder.m.boz");
            case 3 -> Class.forName("itemsadder.m.boy");
            case 4 -> Class.forName("itemsadder.m.box");
            case 5 -> Class.forName("itemsadder.m.boq");
            case 6 -> Class.forName("itemsadder.m.bop");
            case 7 -> Class.forName("itemsadder.m.bow");
            case 8 -> Class.forName("itemsadder.m.bov");
            case 9 -> Class.forName("itemsadder.m.bou");
            case 10 -> Class.forName("itemsadder.m.bot");
            case 11 -> Class.forName("itemsadder.m.bos");
            case 12 -> Class.forName("itemsadder.m.bor");
            case 13 -> Class.forName("itemsadder.m.boo");
            case 14 -> Class.forName("itemsadder.m.bon");
            default -> null;
         };
      } else if (MobGoalFactory.class == var0) {
         var2 = switch (SwitchMap7.aNl[var1.ordinal()]) {
            case 1 -> Class.forName("itemsadder.m.bdt");
            case 2 -> Class.forName("itemsadder.m.bdj");
            case 3 -> Class.forName("itemsadder.m.bcz");
            case 4 -> Class.forName("itemsadder.m.bcp");
            case 5 -> Class.forName("itemsadder.m.azx");
            case 6 -> Class.forName("itemsadder.m.azn");
            case 7 -> Class.forName("itemsadder.m.bcf");
            case 8 -> Class.forName("itemsadder.m.bbv");
            case 9 -> Class.forName("itemsadder.m.bbl");
            case 10 -> Class.forName("itemsadder.m.bbb");
            case 11 -> Class.forName("itemsadder.m.bar");
            case 12 -> Class.forName("itemsadder.m.bah");
            case 13 -> Class.forName("itemsadder.m.azd");
            case 14 -> Class.forName("itemsadder.m.ayt");
            default -> null;
         };
      } else {
         if (RecipeManager.class == var0) {
            throw new RuntimeException("Use RecipePacketHandler instead of IRecipebook!");
         }

         Log.error("Class not found for type: " + var0.getName());
         Log.error("Maybe not an NMS wrapper class or not implemented yet?");
      }

      return var2;
   }

   public static Object method2777(Class var0) {
      Class var1 = null;

      try {
         var1 = method2776(var0);
         if (var1 == null) {
            Log.error("Can't instantiate NMS wrapper");
            Log.error("This server version is not compatible with this plugin.");
            throw new RuntimeException("Class name not found for type: " + var0.getName());
         } else {
            return method2778(var0, var1);
         }
      } catch (Throwable var3) {
         Log.error("Can't instantiate NMS wrapper");
         Log.error("This server version is not compatible with this plugin.");
         throw new RuntimeException("Failed to create instance for class: " + var1, var3);
      }
   }

   private static Object method2778(Class var0, Class var1) {
      Constructor var2 = aNk.computeIfAbsent(var0, var1x -> {
         try {
            return var1.getDeclaredConstructor();
         } catch (NoSuchMethodException var3) {
            throw new RuntimeException(var3);
         }
      });
      return var2.newInstance();
   }

   public static Constructor method2779(Class var0, boolean var1, Class... var2) {
      Class var3;
      try {
         var3 = method2776(var0);
      } catch (ClassNotFoundException var5) {
         throw new RuntimeException(var5);
      }

      return method2775(var3, var2);
   }
}
