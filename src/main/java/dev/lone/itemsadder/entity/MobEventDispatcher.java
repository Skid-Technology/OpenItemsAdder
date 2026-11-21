package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.command.AbstractCommandPermissionRule;
import dev.lone.itemsadder.command.EntityPermissionRule;
import dev.lone.itemsadder.command.EntityPermissionRuleBuilder;
import dev.lone.itemsadder.command.EventPermissionRule;
import dev.lone.itemsadder.command.EventPermissionRuleBuilder;
import dev.lone.itemsadder.command.PermissionRuleConsumer;
import dev.lone.itemsadder.command.PermissionRuleOutcome;
import dev.lone.itemsadder.command.PermissionRuleStatus;
import dev.lone.itemsadder.command.ScriptPermissionRule;
import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.EffectChainMode;
import dev.lone.itemsadder.core.EffectChainRule;
import dev.lone.itemsadder.core.NamespaceRegistrar;
import dev.lone.itemsadder.internal.SwitchMap56;
import dev.lone.itemsadder.script.CompiledScript;
import dev.lone.itemsadder.util.CollectionUtils;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class MobEventDispatcher {
   final CustomMobSettings iP;
   public final HashMap iQ = new HashMap();
   public final HashMap iR = new HashMap();

   public MobEventDispatcher(CustomMobSettings var1) {
      this.iP = var1;
   }

   public void load() {
      String var1 = this.iP.vn.getNamespace();
      String var2 = this.iP.vn.getNamespacedID();
      YamlConfig var3 = this.iP.getConfig();
      if (var3 != null) {
         ConfigSectionWrapper var4 = var3.kc("entities." + this.iP.vn.dl + ".events");
         if (var4 != null) {
            for (MobEventType var8 : MobEventType.values()) {
               if (var4.hA(var8.toString())) {
                  if (var3.contains("entities." + this.iP.vn.dl + ".events." + var8 + ".cooldown")) {
                     this.iR.put(var8, var3.getInt("entities." + this.iP.vn.dl + ".events." + var8 + ".cooldown", 0) / 20 * 1000L);
                  }

                  ConfigSectionWrapper var9 = var4.kc(var8.toString());
                  if (var9 != null) {
                     for (String var11 : YamlConfig.method7043(var9, "")) {
                        ConfigSectionWrapper var12 = var9.kc(var11);
                        if (var12 != null) {
                           if (var11.startsWith("summon_entity")) {
                              String var13 = var12.jO("entity");
                              if (var13 == null) {
                                 Log.error("Missing 'entity' for action '" + var11 + "' in event '" + var8 + "' for entity '" + var2 + "'. File: " + var3.DJ());
                              } else if (!CustomEntityManager.getInstance().oN.containsKey(var13)) {
                                 Log.error(
                                    "Unknown entity '"
                                       + var13
                                       + "' for action '"
                                       + var11
                                       + "' in event '"
                                       + var8
                                       + "' for entity '"
                                       + var2
                                       + "'. File: "
                                       + var3.DJ()
                                 );
                              } else {
                                 try {
                                    EntityPermissionRule var14 = ((EntityPermissionRuleBuilder)((EntityPermissionRuleBuilder)((EntityPermissionRuleBuilder)((EntityPermissionRuleBuilder)EntityPermissionRule.ai(
                                                      var13
                                                   )
                                                   .method5022(var12.method7077("max_per_chunk", null))
                                                   .ad(var12.bc("permission", null)))
                                                .method5015(var12.method7100("delay", null)))
                                             .method5016(this.method5327(var12)))
                                          .ae(var11))
                                       .cO();
                                    this.method5328(var8, var14);
                                 } catch (IllegalArgumentException var17) {
                                    Log.error(
                                       "Failed to register action '"
                                          + var11
                                          + "' in event '"
                                          + var8
                                          + "' for entity '"
                                          + var2
                                          + "'. "
                                          + var17.getMessage()
                                          + ". File: "
                                          + var3.DJ()
                                    );
                                 }
                              }
                           } else if (var11.startsWith("execute_command")) {
                              EventPermissionRule var18 = ((EventPermissionRuleBuilder)((EventPermissionRuleBuilder)((EventPermissionRuleBuilder)((EventPermissionRuleBuilder)EventPermissionRule.cE()
                                             .af(var12.jO("command"))
                                             .method5018(var12.method7078("as_console", true))
                                             .method5019(var12.method7078("silent", true))
                                             .ag(var12.bc("flow_success_if_message_contains", null))
                                             .ad(var12.bc("permission", null)))
                                          .method5015(var12.method7100("delay", null)))
                                       .method5016(this.method5327(var12)))
                                    .ae(var11))
                                 .cG();
                              this.method5328(var8, var18);
                           } else if (var11.startsWith("script")) {
                              String var19 = var12.jO("name");
                              if (var19 == null) {
                                 Log.error("&cMissing script name for action &e" + var11 + "&c of custom entity &e" + var2);
                              } else {
                                 var19 = var19.replace(".jspp", "");
                                 var19 = var19.replace(".js", "");
                                 var19 = var19.replace(".java", "");
                                 String var23 = var19;
                                 if (!var23.contains(":")) {
                                    var23 = var1 + ":" + var19;
                                 }

                                 CompiledScript var15 = NamespaceRegistrar.do_(var23);
                                 if (var15 == null) {
                                    Log.error("&cScript &e" + var23 + "&c not found for action &e" + var11 + "&c of item &e" + var2);
                                 } else {
                                    ScriptPermissionRule var16 = ScriptPermissionRule.cI().ah(var23).method5020(var15).cK();
                                    this.method5328(var8, var16);
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public EffectChainRule method5327(ConfigSectionWrapper var1) {
      for (EffectChainMode var5 : EffectChainMode.values()) {
         if (var1.hA(var5.key)) {
            String var6 = null;
            if (!(var1.kf(var5.key) instanceof Boolean)) {
               var6 = var1.jO(var5.key);
            }

            return new EffectChainRule(var5, var6);
         }
      }

      return null;
   }

   public AbstractCommandPermissionRule method5328(MobEventType var1, AbstractCommandPermissionRule var2) {
      List var3;
      if (this.iQ.containsKey(var1)) {
         var3 = (List)this.iQ.get(var1);
      } else {
         var3 = new ArrayList();
      }

      var3.add(var2);
      this.iQ.put(var1, var3);
      return var2;
   }

   public boolean hasEvent(MobEventType var1) {
      return this.iQ.containsKey(var1);
   }

   public List getRules(MobEventType var1) {
      return this.hasEvent(var1) ? (List)this.iQ.get(var1) : new ArrayList();
   }

   @Deprecated
   public List getRulesFor(LivingEntity var1, MobEventType var2) {
      if (this.hasEvent(var2) && this.iP.wl != null && !var1.hasPermission(this.iP.wl)) {
         Log.sendPrefixed(var1, Main.aFi.lj("no-permission-detail").replace("{permission}", this.iP.wl));
         return new ArrayList();
      } else {
         return new ArrayList();
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public void dispatch(Entity var1, MobEventType var2, PermissionRuleConsumer var3) {
      if (var1 instanceof LivingEntity) {
         if (this.hasEvent(var2)) {
            if (this.iP.wl != null && !var1.hasPermission(this.iP.wl)) {
               Log.sendPrefixed(var1, Main.aFi.lj("no-permission-detail").replace("{permission}", this.iP.wl));
               return;
            }

            boolean var4 = false;
            PermissionRuleStatus var5 = null;
            boolean var6 = false;
            boolean var7 = false;
            List var8 = null;
            List var9 = null;
            List var10 = (List)this.iQ.get(var2);

            for (AbstractCommandPermissionRule var12 : var10) {
               if (var12.cq() && !var12.method4940(var1)) {
                  if (!var4) {
                     Log.sendPrefixed(var1, Main.aFi.lj("no-permission-detail").replace("{permission}", var12.getPermission()));
                     var4 = true;
                  }
               } else if (var12.cp()) {
                  SchedulerUtils.runLaterSync(() -> var3.accept(var12), var12.cs());
               } else if (var10.size() <= 1) {
                  var3.accept(var12);
               } else {
                  EffectChainRule var13 = var12.ct();
                  if (var13 != null && var5 != null) {
                     switch (SwitchMap56.iS[var13.acR.ordinal()]) {
                        case 1:
                           if (var5 == PermissionRuleStatus.SUCCESS) {
                              return;
                           }
                           break;
                        case 2:
                           if (var5 == PermissionRuleStatus.FAILED) {
                              return;
                           }
                           break;
                        case 3:
                           if (var5 == PermissionRuleStatus.SUCCESS) {
                              continue;
                           }
                           break;
                        case 4:
                           if (var5 == PermissionRuleStatus.FAILED) {
                              continue;
                           }
                           break;
                        case 5:
                           if (var7) {
                              return;
                           }
                           break;
                        case 6:
                           if (var6) {
                              return;
                           }
                           break;
                        case 7:
                           if (var7) {
                              continue;
                           }
                           break;
                        case 8:
                           if (var6) {
                              continue;
                           }
                           break;
                        case 9:
                           if (var7 && CollectionUtils.listContains(var9, var13.acS)) {
                              return;
                           }
                           break;
                        case 10:
                           if (var6 && CollectionUtils.listContains(var8, var13.acS)) {
                              return;
                           }
                           break;
                        case 11:
                           if (var7 && CollectionUtils.listContains(var9, var13.acS)) {
                              continue;
                           }
                           break;
                        case 12:
                           if (var6 && CollectionUtils.listContains(var8, var13.acS)) {
                              continue;
                           }
                     }
                  }

                  PermissionRuleOutcome var14 = var3.accept(var12);
                  var5 = var14.cA();
                  if (var14.cA() == PermissionRuleStatus.SUCCESS) {
                     var7 = true;
                     var9 = CollectionUtils.addOrCreate(var9, var12.cr());
                  } else if (var14.cA() == PermissionRuleStatus.FAILED) {
                     var6 = true;
                     var8 = CollectionUtils.addOrCreate(var8, var12.cr());
                  }
               }
            }
         }
      }
   }

   public boolean isOnCooldown(MobEventType var1) {
      return this.iR.containsKey(var1);
   }

   public Long getCooldownMillis(MobEventType var1) {
      return this.isOnCooldown(var1) ? (Long)this.iR.get(var1) : 0L;
   }
}
