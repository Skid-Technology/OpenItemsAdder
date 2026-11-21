package dev.lone.itemsadder.util;

final class CustomHashMapTreeNode extends CustomHashMapLinkedNode {
   CustomHashMapTreeNode bOE;
   CustomHashMapTreeNode bOF;
   CustomHashMapTreeNode bOG;
   CustomHashMapTreeNode bOH;
   boolean aTa;

   CustomHashMapTreeNode(int var1, Object var2, Object var3, CustomHashMapNode var4) {
      super(var1, var2, var3, var4);
   }

   final CustomHashMapTreeNode root() {
      CustomHashMapTreeNode var1 = this;

      while (true) {
         CustomHashMapTreeNode var2 = var1.bOE;
         if (var1.bOE == null) {
            return var1;
         }

         var1 = var2;
      }
   }

   static void moveRootToFront(CustomHashMapNode[] var0, CustomHashMapTreeNode var1) {
      int var2;
      if (var1 != null && var0 != null && (var2 = var0.length) > 0) {
         int var3 = var2 - 1 & var1.bOC;
         CustomHashMapTreeNode var4 = (CustomHashMapTreeNode)var0[var3];
         if (var1 != var4) {
            var0[var3] = var1;
            CustomHashMapTreeNode var6 = var1.bOH;
            CustomHashMapNode var5 = var1.bOu;
            if (var1.bOu != null) {
               ((CustomHashMapTreeNode)var5).bOH = var6;
            }

            if (var6 != null) {
               var6.bOu = var5;
            }

            if (var4 != null) {
               var4.bOH = var1;
            }

            var1.bOu = var4;
            var1.bOH = null;
         }

         assert checkInvariants(var1);
      }
   }

   final CustomHashMapTreeNode find(int var1, Object var2, Class var3) {
      CustomHashMapTreeNode var4 = this;

      do {
         CustomHashMapTreeNode var8 = var4.bOF;
         CustomHashMapTreeNode var9 = var4.bOG;
         int var5 = var4.bOC;
         if (var4.bOC > var1) {
            var4 = var8;
         } else if (var5 < var1) {
            var4 = var9;
         } else {
            Object var7 = var4.bOD;
            if (var4.bOD == var2 || var2 != null && var2.equals(var7)) {
               return var4;
            }

            if (var8 == null) {
               var4 = var9;
            } else if (var9 == null) {
               var4 = var8;
            } else {
               int var6;
               if ((var3 != null || (var3 = var2 != null ? var2.getClass() : null) != null) && (var6 = CustomHashMap.tieBreakOrder(var2, var7)) != 0) {
                  var4 = var6 < 0 ? var8 : var9;
               } else {
                  CustomHashMapTreeNode var10;
                  if ((var10 = var9.find(var1, var2, var3)) != null) {
                     return var10;
                  }

                  var4 = var8;
               }
            }
         }
      } while (var4 != null);

      return null;
   }

   final CustomHashMapTreeNode getTreeNode(int var1, Object var2) {
      return (this.bOE != null ? this.root() : this).find(var1, var2, null);
   }

   static int tieBreakOrder(Object var0, Object var1) {
      int var2;
      if (var0 == null || var1 == null || (var2 = var0.getClass().getName().compareTo(var1.getClass().getName())) == 0) {
         var2 = System.identityHashCode(var0) <= System.identityHashCode(var1) ? -1 : 1;
      }

      return var2;
   }

   final void treeify(CustomHashMapNode[] var1) {
      CustomHashMapTreeNode var2 = null;
      CustomHashMapTreeNode var3 = this;

      while (var3 != null) {
         CustomHashMapTreeNode var4 = (CustomHashMapTreeNode)var3.bOu;
         var3.bOF = var3.bOG = null;
         if (var2 == null) {
            var3.bOE = null;
            var3.aTa = false;
            var2 = var3;
         } else {
            Object var5 = var3.bOD;
            int var6 = var3.bOC;
            Class var7 = null;
            CustomHashMapTreeNode var8 = var2;

            int var9;
            CustomHashMapTreeNode var12;
            do {
               Object var11 = var8.bOD;
               int var10 = var8.bOC;
               if (var8.bOC > var6) {
                  var9 = -1;
               } else if (var10 < var6) {
                  var9 = 1;
               } else if (var7 == null && (var7 = var5.getClass()) == null || (var9 = CustomHashMap.tieBreakOrder(var5, var11)) == 0) {
                  var9 = tieBreakOrder(var5, var11);
               }

               var12 = var8;
            } while ((var8 = var9 <= 0 ? var8.bOF : var8.bOG) != null);

            var3.bOE = var12;
            if (var9 <= 0) {
               var12.bOF = var3;
            } else {
               var12.bOG = var3;
            }

            var2 = balanceInsertion(var2, var3);
         }

         var3 = var4;
      }

      moveRootToFront(var1, var2);
   }

   final CustomHashMapNode untreeify(CustomHashMap var1) {
      CustomHashMapNode var2 = null;
      CustomHashMapNode var3 = null;

      for (CustomHashMapNode var4 = this; var4 != null; var4 = var4.bOu) {
         CustomHashMapNode var5 = var1.replacementNode(var4, null);
         if (var3 == null) {
            var2 = var5;
         } else {
            var3.bOu = var5;
         }

         var3 = var5;
      }

      return var2;
   }

   final CustomHashMapTreeNode putTreeVal(CustomHashMap var1, CustomHashMapNode[] var2, int var3, Object var4, Object var5) {
      Class var6 = null;
      boolean var7 = false;
      CustomHashMapTreeNode var8 = this.bOE != null ? this.root() : this;
      CustomHashMapTreeNode var9 = var8;

      int var10;
      CustomHashMapTreeNode var16;
      do {
         int var11 = var9.bOC;
         if (var9.bOC > var3) {
            var10 = -1;
         } else if (var11 < var3) {
            var10 = 1;
         } else {
            Object var12 = var9.bOD;
            if (var9.bOD == var4 || var4 != null && var4.equals(var12)) {
               return var9;
            }

            if (var6 == null && (var6 = var4.getClass()) == null || (var10 = CustomHashMap.tieBreakOrder(var4, var12)) == 0) {
               if (!var7) {
                  var7 = true;
                  CustomHashMapTreeNode var14 = var9.bOF;
                  if (var9.bOF != null && (var16 = var14.find(var3, var4, var6)) != null) {
                     return var16;
                  }

                  var14 = var9.bOG;
                  if (var9.bOG != null && (var16 = var14.find(var3, var4, var6)) != null) {
                     return var16;
                  }
               }

               var10 = tieBreakOrder(var4, var12);
            }
         }

         var16 = var9;
      } while ((var9 = var10 <= 0 ? var9.bOF : var9.bOG) != null);

      CustomHashMapNode var18 = var16.bOu;
      CustomHashMapTreeNode var15 = var1.newTreeNode(var3, var4, var5, var18);
      if (var10 <= 0) {
         var16.bOF = var15;
      } else {
         var16.bOG = var15;
      }

      var16.bOu = var15;
      var15.bOE = var15.bOH = var16;
      if (var18 != null) {
         ((CustomHashMapTreeNode)var18).bOH = var15;
      }

      moveRootToFront(var2, balanceInsertion(var8, var15));
      return null;
   }

   final void removeTreeNode(CustomHashMap var1, CustomHashMapNode[] var2, boolean var3) {
      int var4;
      if (var2 != null && (var4 = var2.length) != 0) {
         int var5 = var4 - 1 & this.bOC;
         CustomHashMapTreeNode var6 = (CustomHashMapTreeNode)var2[var5];
         CustomHashMapTreeNode var7 = var6;
         CustomHashMapTreeNode var9 = (CustomHashMapTreeNode)this.bOu;
         CustomHashMapTreeNode var10 = this.bOH;
         if (var10 == null) {
            var6 = var9;
            var2[var5] = var9;
         } else {
            var10.bOu = var9;
         }

         if (var9 != null) {
            var9.bOH = var10;
         }

         if (var6 != null) {
            if (var7.bOE != null) {
               var7 = var7.root();
            }

            label126:
            if (var7 != null) {
               if (var3) {
                  if (var7.bOG == null) {
                     break label126;
                  }

                  CustomHashMapTreeNode var8 = var7.bOF;
                  if (var7.bOF == null || var8.bOF == null) {
                     break label126;
                  }
               }

               CustomHashMapTreeNode var11 = this;
               CustomHashMapTreeNode var12 = this.bOF;
               CustomHashMapTreeNode var13 = this.bOG;
               CustomHashMapTreeNode var14;
               if (var12 != null && var13 != null) {
                  CustomHashMapTreeNode var15 = var13;

                  while (true) {
                     CustomHashMapTreeNode var16 = var15.bOF;
                     if (var15.bOF == null) {
                        boolean var17 = var15.aTa;
                        var15.aTa = var11.aTa;
                        var11.aTa = var17;
                        CustomHashMapTreeNode var18 = var15.bOG;
                        CustomHashMapTreeNode var19 = var11.bOE;
                        if (var15 == var13) {
                           var11.bOE = var15;
                           var15.bOG = var11;
                        } else {
                           CustomHashMapTreeNode var20 = var15.bOE;
                           if ((var11.bOE = var20) != null) {
                              if (var15 == var20.bOF) {
                                 var20.bOF = var11;
                              } else {
                                 var20.bOG = var11;
                              }
                           }

                           if ((var15.bOG = var13) != null) {
                              var13.bOE = var15;
                           }
                        }

                        var11.bOF = null;
                        if ((var11.bOG = var18) != null) {
                           var18.bOE = var11;
                        }

                        if ((var15.bOF = var12) != null) {
                           var12.bOE = var15;
                        }

                        if ((var15.bOE = var19) == null) {
                           var7 = var15;
                        } else if (var11 == var19.bOF) {
                           var19.bOF = var15;
                        } else {
                           var19.bOG = var15;
                        }

                        if (var18 != null) {
                           var14 = var18;
                        } else {
                           var14 = var11;
                        }
                        break;
                     }

                     var15 = var16;
                  }
               } else if (var12 != null) {
                  var14 = var12;
               } else if (var13 != null) {
                  var14 = var13;
               } else {
                  var14 = var11;
               }

               if (var14 != var11) {
                  CustomHashMapTreeNode var21 = var14.bOE = var11.bOE;
                  if (var21 == null) {
                     var7 = var14;
                     var14.aTa = false;
                  } else if (var11 == var21.bOF) {
                     var21.bOF = var14;
                  } else {
                     var21.bOG = var14;
                  }

                  var11.bOF = var11.bOG = var11.bOE = null;
               }

               CustomHashMapTreeNode var22 = var11.aTa ? var7 : balanceDeletion(var7, var14);
               if (var14 == var11) {
                  CustomHashMapTreeNode var23 = var11.bOE;
                  var11.bOE = null;
                  if (var23 != null) {
                     if (var11 == var23.bOF) {
                        var23.bOF = null;
                     } else if (var11 == var23.bOG) {
                        var23.bOG = null;
                     }
                  }
               }

               if (var3) {
                  moveRootToFront(var2, var22);
               }

               return;
            }

            var2[var5] = var6.untreeify(var1);
         }
      }
   }

   final void split(CustomHashMap var1, CustomHashMapNode[] var2, int var3, int var4) {
      CustomHashMapTreeNode var5 = this;
      CustomHashMapTreeNode var6 = null;
      CustomHashMapTreeNode var7 = null;
      CustomHashMapTreeNode var8 = null;
      CustomHashMapTreeNode var9 = null;
      int var10 = 0;
      int var11 = 0;
      CustomHashMapTreeNode var12 = var5;

      while (var12 != null) {
         CustomHashMapTreeNode var13 = (CustomHashMapTreeNode)var12.bOu;
         var12.bOu = null;
         if ((var12.bOC & var4) == 0) {
            if ((var12.bOH = var7) == null) {
               var6 = var12;
            } else {
               var7.bOu = var12;
            }

            var7 = var12;
            var10++;
         } else {
            if ((var12.bOH = var9) == null) {
               var8 = var12;
            } else {
               var9.bOu = var12;
            }

            var9 = var12;
            var11++;
         }

         var12 = var13;
      }

      if (var6 != null) {
         if (var10 <= 6) {
            var2[var3] = var6.untreeify(var1);
         } else {
            var2[var3] = var6;
            if (var8 != null) {
               var6.treeify(var2);
            }
         }
      }

      if (var8 != null) {
         if (var11 <= 6) {
            var2[var3 + var4] = var8.untreeify(var1);
         } else {
            var2[var3 + var4] = var8;
            if (var6 != null) {
               var8.treeify(var2);
            }
         }
      }
   }

   static CustomHashMapTreeNode rotateLeft(CustomHashMapTreeNode var0, CustomHashMapTreeNode var1) {
      if (var1 != null) {
         CustomHashMapTreeNode var2 = var1.bOG;
         if (var1.bOG != null) {
            CustomHashMapTreeNode var4;
            if ((var4 = var1.bOG = var2.bOF) != null) {
               var4.bOE = var1;
            }

            CustomHashMapTreeNode var3;
            if ((var3 = var2.bOE = var1.bOE) == null) {
               var0 = var2;
               var2.aTa = false;
            } else if (var3.bOF == var1) {
               var3.bOF = var2;
            } else {
               var3.bOG = var2;
            }

            var2.bOF = var1;
            var1.bOE = var2;
         }
      }

      return var0;
   }

   static CustomHashMapTreeNode rotateRight(CustomHashMapTreeNode var0, CustomHashMapTreeNode var1) {
      if (var1 != null) {
         CustomHashMapTreeNode var2 = var1.bOF;
         if (var1.bOF != null) {
            CustomHashMapTreeNode var4;
            if ((var4 = var1.bOF = var2.bOG) != null) {
               var4.bOE = var1;
            }

            CustomHashMapTreeNode var3;
            if ((var3 = var2.bOE = var1.bOE) == null) {
               var0 = var2;
               var2.aTa = false;
            } else if (var3.bOG == var1) {
               var3.bOG = var2;
            } else {
               var3.bOF = var2;
            }

            var2.bOG = var1;
            var1.bOE = var2;
         }
      }

      return var0;
   }

   static CustomHashMapTreeNode balanceInsertion(CustomHashMapTreeNode var0, CustomHashMapTreeNode var1) {
      var1.aTa = true;

      while (true) {
         CustomHashMapTreeNode var2 = var1.bOE;
         if (var1.bOE == null) {
            var1.aTa = false;
            return var1;
         }

         if (!var2.aTa) {
            break;
         }

         CustomHashMapTreeNode var3 = var2.bOE;
         if (var2.bOE == null) {
            break;
         }

         CustomHashMapTreeNode var4 = var3.bOF;
         if (var2 == var3.bOF) {
            CustomHashMapTreeNode var5 = var3.bOG;
            if (var3.bOG != null && var5.aTa) {
               var5.aTa = false;
               var2.aTa = false;
               var3.aTa = true;
               var1 = var3;
            } else {
               if (var1 == var2.bOG) {
                  var1 = var2;
                  var0 = rotateLeft(var0, var2);
                  var2 = var1.bOE;
                  var3 = var1.bOE == null ? null : var2.bOE;
               }

               if (var2 != null) {
                  var2.aTa = false;
                  if (var3 != null) {
                     var3.aTa = true;
                     var0 = rotateRight(var0, var3);
                  }
               }
            }
         } else if (var4 != null && var4.aTa) {
            var4.aTa = false;
            var2.aTa = false;
            var3.aTa = true;
            var1 = var3;
         } else {
            if (var1 == var2.bOF) {
               var1 = var2;
               var0 = rotateRight(var0, var2);
               var2 = var1.bOE;
               var3 = var1.bOE == null ? null : var2.bOE;
            }

            if (var2 != null) {
               var2.aTa = false;
               if (var3 != null) {
                  var3.aTa = true;
                  var0 = rotateLeft(var0, var3);
               }
            }
         }
      }

      return var0;
   }

   static CustomHashMapTreeNode balanceDeletion(CustomHashMapTreeNode var0, CustomHashMapTreeNode var1) {
      while (var1 != null && var1 != var0) {
         CustomHashMapTreeNode var2 = var1.bOE;
         if (var1.bOE == null) {
            var1.aTa = false;
            return var1;
         }

         if (var1.aTa) {
            var1.aTa = false;
            return var0;
         }

         CustomHashMapTreeNode var3 = var2.bOF;
         if (var2.bOF == var1) {
            CustomHashMapTreeNode var4 = var2.bOG;
            if (var2.bOG != null && var4.aTa) {
               var4.aTa = false;
               var2.aTa = true;
               var0 = rotateLeft(var0, var2);
               var2 = var1.bOE;
               var4 = var1.bOE == null ? null : var2.bOG;
            }

            if (var4 == null) {
               var1 = var2;
            } else {
               CustomHashMapTreeNode var8 = var4.bOF;
               CustomHashMapTreeNode var9 = var4.bOG;
               if (var9 != null && var9.aTa || var8 != null && var8.aTa) {
                  if (var9 == null || !var9.aTa) {
                     if (var8 != null) {
                        var8.aTa = false;
                     }

                     var4.aTa = true;
                     var0 = rotateRight(var0, var4);
                     var2 = var1.bOE;
                     var4 = var1.bOE == null ? null : var2.bOG;
                  }

                  if (var4 != null) {
                     var4.aTa = var2 == null ? false : var2.aTa;
                     var9 = var4.bOG;
                     if (var4.bOG != null) {
                        var9.aTa = false;
                     }
                  }

                  if (var2 != null) {
                     var2.aTa = false;
                     var0 = rotateLeft(var0, var2);
                  }

                  var1 = var0;
               } else {
                  var4.aTa = true;
                  var1 = var2;
               }
            }
         } else {
            if (var3 != null && var3.aTa) {
               var3.aTa = false;
               var2.aTa = true;
               var0 = rotateRight(var0, var2);
               var2 = var1.bOE;
               var3 = var1.bOE == null ? null : var2.bOF;
            }

            if (var3 == null) {
               var1 = var2;
            } else {
               CustomHashMapTreeNode var5 = var3.bOF;
               CustomHashMapTreeNode var6 = var3.bOG;
               if (var5 != null && var5.aTa || var6 != null && var6.aTa) {
                  if (var5 == null || !var5.aTa) {
                     if (var6 != null) {
                        var6.aTa = false;
                     }

                     var3.aTa = true;
                     var0 = rotateLeft(var0, var3);
                     var2 = var1.bOE;
                     var3 = var1.bOE == null ? null : var2.bOF;
                  }

                  if (var3 != null) {
                     var3.aTa = var2 == null ? false : var2.aTa;
                     var5 = var3.bOF;
                     if (var3.bOF != null) {
                        var5.aTa = false;
                     }
                  }

                  if (var2 != null) {
                     var2.aTa = false;
                     var0 = rotateRight(var0, var2);
                  }

                  var1 = var0;
               } else {
                  var3.aTa = true;
                  var1 = var2;
               }
            }
         }
      }

      return var0;
   }

   static boolean checkInvariants(CustomHashMapTreeNode var0) {
      CustomHashMapTreeNode var1 = var0.bOE;
      CustomHashMapTreeNode var2 = var0.bOF;
      CustomHashMapTreeNode var3 = var0.bOG;
      CustomHashMapTreeNode var4 = var0.bOH;
      CustomHashMapTreeNode var5 = (CustomHashMapTreeNode)var0.bOu;
      if (var4 != null && var4.bOu != var0) {
         return false;
      }

      if (var5 != null && var5.bOH != var0) {
         return false;
      }

      if (var1 != null && var0 != var1.bOF && var0 != var1.bOG) {
         return false;
      }

      if (var2 == null || var2.bOE == var0 && var2.bOC <= var0.bOC) {
         if (var3 == null || var3.bOE == var0 && var3.bOC >= var0.bOC) {
            if (var0.aTa && var2 != null && var2.aTa && var3 != null && var3.aTa) {
               return false;
            } else {
               return var2 != null && !checkInvariants(var2) ? false : var3 == null || checkInvariants(var3);
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
