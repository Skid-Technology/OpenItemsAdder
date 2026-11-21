package dev.lone.itemsadder.util;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CustomHashMap extends AbstractMap implements Serializable, Cloneable, Map {
   private static final long serialVersionUID = 6767664750549425943L;
   static final int bOg = 16;
   static final int bOh = 1073741824;
   static final float bOi = 0.75F;
   static final int bOj = 8;
   static final int bOk = 6;
   static final int bOl = 64;
   transient CustomHashMapNode[] bOm;
   transient Set bOn;
   transient int XN;
   transient int bOo;
   int bOp;
   final float bOq;

   static final int hash(Object var0) {
      int var1;
      return var0 == null ? 0 : (var1 = var0.hashCode()) ^ var1 >>> 16;
   }

   static int tieBreakOrder(Object var0, Object var1) {
      return var1 == null ? 0 : ((Comparable)var0).compareTo(var1);
   }

   static final int tableSizeFor(int var0) {
      int var1 = -1 >>> Integer.numberOfLeadingZeros(var0 - 1);
      return var1 < 0 ? 1 : (var1 >= 1073741824 ? 1073741824 : var1 + 1);
   }

   public CustomHashMap(int var1, float var2) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Illegal initial capacity: " + var1);
      }

      if (var1 > 1073741824) {
         var1 = 1073741824;
      }

      if (!(var2 <= 0.0F) && !Float.isNaN(var2)) {
         this.bOq = var2;
         this.bOp = tableSizeFor(var1);
      } else {
         throw new IllegalArgumentException("Illegal load factor: " + var2);
      }
   }

   public CustomHashMap(int var1) {
      this(var1, 0.75F);
   }

   public CustomHashMap() {
      this.bOq = 0.75F;
   }

   public CustomHashMap(Map var1) {
      this.bOq = 0.75F;
      this.putMapEntries(var1, false);
   }

   final void putMapEntries(Map var1, boolean var2) {
      int var3 = var1.size();
      if (var3 > 0) {
         if (this.bOm == null) {
            float var4 = var3 / this.bOq + 1.0F;
            int var5 = var4 < 1.0737418E9F ? (int)var4 : 1073741824;
            if (var5 > this.bOp) {
               this.bOp = tableSizeFor(var5);
            }
         } else {
            while (var3 > this.bOp && this.bOm.length < 1073741824) {
               this.resize();
            }
         }

         for (Entry var9 : var1.entrySet()) {
            Object var6 = var9.getKey();
            Object var7 = var9.getValue();
            this.putVal(hash(var6), var6, var7, false, var2);
         }
      }
   }

   @Override
   public int size() {
      return this.XN;
   }

   @Override
   public boolean isEmpty() {
      return this.XN == 0;
   }

   @Override
   public Object get(Object key) {
      CustomHashMapNode var2;
      return (var2 = this.getNode(key)) == null ? null : var2.auj;
   }

   final CustomHashMapNode getNode(Object var1) {
      CustomHashMapNode[] var2 = this.bOm;
      CustomHashMapNode var3;
      int var5;
      int var6;
      if (this.bOm != null && (var5 = var2.length) > 0 && (var3 = var2[var5 - 1 & (var6 = hash(var1))]) != null) {
         if (var3.bOC == var6) {
            Object var7 = var3.bOD;
            if (var3.bOD == var1 || var1 != null && var1.equals(var7)) {
               return var3;
            }
         }

         CustomHashMapNode var4 = var3.bOu;
         if (var3.bOu != null) {
            if (var3 instanceof CustomHashMapTreeNode) {
               return ((CustomHashMapTreeNode)var3).getTreeNode(var6, var1);
            }

            do {
               if (var4.bOC == var6) {
                  Object var8 = var4.bOD;
                  if (var4.bOD == var1 || var1 != null && var1.equals(var8)) {
                     return var4;
                  }
               }
            } while ((var4 = var4.bOu) != null);
         }
      }

      return null;
   }

   @Override
   public boolean containsKey(Object key) {
      return this.getNode(key) != null;
   }

   @Override
   public Object put(Object key, Object value) {
      return this.putVal(hash(key), key, value, false, true);
   }

   final Object putVal(int var1, Object var2, Object var3, boolean var4, boolean var5) {
      CustomHashMapNode[] var6 = this.bOm;
      int var8;
      if (this.bOm == null || (var8 = var6.length) == 0) {
         var8 = (var6 = this.resize()).length;
      }

      CustomHashMapNode var7;
      int var9;
      if ((var7 = var6[var9 = var8 - 1 & var1]) == null) {
         var6[var9] = this.newNode(var1, var2, var3, null);
      } else {
         CustomHashMapNode var10;
         label73: {
            if (var7.bOC == var1) {
               Object var11 = var7.bOD;
               if (var7.bOD == var2 || var2 != null && var2.equals(var11)) {
                  var10 = var7;
                  break label73;
               }
            }

            if (var7 instanceof CustomHashMapTreeNode) {
               var10 = ((CustomHashMapTreeNode)var7).putTreeVal(this, var6, var1, var2, var3);
            } else {
               int var12 = 0;

               while (true) {
                  var10 = var7.bOu;
                  if (var7.bOu == null) {
                     var7.bOu = this.newNode(var1, var2, var3, null);
                     if (var12 >= 7) {
                        this.treeifyBin(var6, var1);
                     }
                     break;
                  }

                  if (var10.bOC == var1) {
                     Object var13 = var10.bOD;
                     if (var10.bOD == var2 || var2 != null && var2.equals(var13)) {
                        break;
                     }
                  }

                  var7 = var10;
                  var12++;
               }
            }
         }

         if (var10 != null) {
            Object var14 = var10.auj;
            if (!var4 || var14 == null) {
               var10.auj = var3;
            }

            this.afterNodeAccess(var10);
            return var14;
         }
      }

      this.bOo++;
      if (++this.XN > this.bOp) {
         this.resize();
      }

      this.bO(var5);
      return null;
   }

   final CustomHashMapNode[] resize() {
      CustomHashMapNode[] var1 = this.bOm;
      int var2 = var1 == null ? 0 : var1.length;
      int var3 = this.bOp;
      int var5 = 0;
      int var4;
      if (var2 > 0) {
         if (var2 >= 1073741824) {
            this.bOp = Integer.MAX_VALUE;
            return var1;
         }

         if ((var4 = var2 << 1) < 1073741824 && var2 >= 16) {
            var5 = var3 << 1;
         }
      } else if (var3 > 0) {
         var4 = var3;
      } else {
         var4 = 16;
         var5 = 12;
      }

      if (var5 == 0) {
         float var6 = var4 * this.bOq;
         var5 = var4 < 1073741824 && var6 < 1.0737418E9F ? (int)var6 : Integer.MAX_VALUE;
      }

      this.bOp = var5;
      CustomHashMapNode[] var14 = new CustomHashMapNode[var4];
      this.bOm = var14;
      if (var1 != null) {
         for (int var7 = 0; var7 < var2; var7++) {
            CustomHashMapNode var8;
            if ((var8 = var1[var7]) != null) {
               var1[var7] = null;
               if (var8.bOu == null) {
                  var14[var8.bOC & var4 - 1] = var8;
               } else if (var8 instanceof CustomHashMapTreeNode) {
                  ((CustomHashMapTreeNode)var8).split(this, var14, var7, var2);
               } else {
                  CustomHashMapNode var9 = null;
                  CustomHashMapNode var10 = null;
                  CustomHashMapNode var11 = null;
                  CustomHashMapNode var12 = null;

                  CustomHashMapNode var13;
                  do {
                     var13 = var8.bOu;
                     if ((var8.bOC & var2) == 0) {
                        if (var10 == null) {
                           var9 = var8;
                        } else {
                           var10.bOu = var8;
                        }

                        var10 = var8;
                     } else {
                        if (var12 == null) {
                           var11 = var8;
                        } else {
                           var12.bOu = var8;
                        }

                        var12 = var8;
                     }

                     var8 = var13;
                  } while (var13 != null);

                  if (var10 != null) {
                     var10.bOu = null;
                     var14[var7] = var9;
                  }

                  if (var12 != null) {
                     var12.bOu = null;
                     var14[var7 + var2] = var11;
                  }
               }
            }
         }
      }

      return var14;
   }

   final void treeifyBin(CustomHashMapNode[] var1, int var2) {
      int var3;
      if (var1 == null || (var3 = var1.length) < 64) {
         this.resize();
      } else {
         int var4;
         CustomHashMapNode var5;
         if ((var5 = var1[var4 = var3 - 1 & var2]) != null) {
            CustomHashMapTreeNode var6 = null;
            CustomHashMapTreeNode var7 = null;

            do {
               CustomHashMapTreeNode var8 = this.replacementTreeNode(var5, null);
               if (var7 == null) {
                  var6 = var8;
               } else {
                  var8.bOH = var7;
                  var7.bOu = var8;
               }

               var7 = var8;
            } while ((var5 = var5.bOu) != null);

            if ((var1[var4] = var6) != null) {
               var6.treeify(var1);
            }
         }
      }
   }

   @Override
   public void putAll(Map m) {
      this.putMapEntries(m, true);
   }

   @Override
   public Object remove(Object key) {
      CustomHashMapNode var2;
      return (var2 = this.removeNode(hash(key), key, null, false, true)) == null ? null : var2.auj;
   }

   final CustomHashMapNode removeNode(int var1, Object var2, Object var3, boolean var4, boolean var5) {
      CustomHashMapNode[] var6 = this.bOm;
      CustomHashMapNode var7;
      int var8;
      int var9;
      if (this.bOm != null && (var8 = var6.length) > 0 && (var7 = var6[var9 = var8 - 1 & var1]) != null) {
         CustomHashMapNode var10;
         label70: {
            var10 = null;
            if (var7.bOC == var1) {
               Object var12 = var7.bOD;
               if (var7.bOD == var2 || var2 != null && var2.equals(var12)) {
                  var10 = var7;
                  break label70;
               }
            }

            CustomHashMapNode var11 = var7.bOu;
            if (var7.bOu != null) {
               if (var7 instanceof CustomHashMapTreeNode) {
                  var10 = ((CustomHashMapTreeNode)var7).getTreeNode(var1, var2);
               } else {
                  do {
                     if (var11.bOC == var1) {
                        Object var14 = var11.bOD;
                        if (var11.bOD == var2 || var2 != null && var2.equals(var14)) {
                           var10 = var11;
                           break;
                        }
                     }

                     var7 = var11;
                  } while ((var11 = var11.bOu) != null);
               }
            }
         }

         if (var10 != null) {
            if (var4) {
               Object var13 = var10.auj;
               if (var10.auj != var3 && (var3 == null || !var3.equals(var13))) {
                  return null;
               }
            }

            if (var10 instanceof CustomHashMapTreeNode) {
               ((CustomHashMapTreeNode)var10).removeTreeNode(this, var6, var5);
            } else if (var10 == var7) {
               var6[var9] = var10.bOu;
            } else {
               var7.bOu = var10.bOu;
            }

            this.bOo++;
            this.XN--;
            this.afterNodeInsertion(var10);
            return var10;
         }
      }

      return null;
   }

   @Override
   public void clear() {
      this.bOo++;
      CustomHashMapNode[] var1 = this.bOm;
      if (this.bOm != null && this.XN > 0) {
         this.XN = 0;

         for (int var2 = 0; var2 < var1.length; var2++) {
            var1[var2] = null;
         }
      }
   }

   @Override
   public boolean containsValue(Object value) {
      CustomHashMapNode[] var2 = this.bOm;
      if (this.bOm != null && this.XN > 0) {
         CustomHashMapNode[] var4 = var2;
         int var5 = var4.length;

         for (int var6 = 0; var6 < var5; var6++) {
            for (CustomHashMapNode var7 = var4[var6]; var7 != null; var7 = var7.bOu) {
               Object var3 = var7.auj;
               if (var7.auj == value || value != null && value.equals(var3)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   @Override
   public Set keySet() {
      throw new RuntimeException("Not implemented!");
   }

   final Object[] method293(Object[] var1) {
      int var2 = this.XN;
      if (var1.length < var2) {
         return (Object[])Array.newInstance(var1.getClass().getComponentType(), var2);
      }

      if (var1.length > var2) {
         var1[var2] = null;
      }

      return var1;
   }

   Object[] method294(Object[] var1) {
      Object[] var2 = var1;
      int var4 = 0;
      if (this.XN > 0) {
         CustomHashMapNode[] var3 = this.bOm;
         if (this.bOm != null) {
            CustomHashMapNode[] var5 = var3;
            int var6 = var5.length;

            for (int var7 = 0; var7 < var6; var7++) {
               for (CustomHashMapNode var8 = var5[var7]; var8 != null; var8 = var8.bOu) {
                  var2[var4++] = var8.bOD;
               }
            }
         }
      }

      return var1;
   }

   Object[] method295(Object[] var1) {
      Object[] var2 = var1;
      int var4 = 0;
      if (this.XN > 0) {
         CustomHashMapNode[] var3 = this.bOm;
         if (this.bOm != null) {
            CustomHashMapNode[] var5 = var3;
            int var6 = var5.length;

            for (int var7 = 0; var7 < var6; var7++) {
               for (CustomHashMapNode var8 = var5[var7]; var8 != null; var8 = var8.bOu) {
                  var2[var4++] = var8.auj;
               }
            }
         }
      }

      return var1;
   }

   @Override
   public Collection values() {
      throw new RuntimeException("Not implemented!");
   }

   @Override
   public Set entrySet() {
      throw new RuntimeException("Not implemented!");
   }

   @Override
   public Object getOrDefault(Object key, Object defaultValue) {
      CustomHashMapNode var3;
      return (var3 = this.getNode(key)) == null ? defaultValue : var3.auj;
   }

   @Override
   public Object putIfAbsent(Object key, Object value) {
      return this.putVal(hash(key), key, value, true, true);
   }

   @Override
   public boolean remove(Object key, Object value) {
      return this.removeNode(hash(key), key, value, true, true) != null;
   }

   @Override
   public boolean replace(Object key, Object oldValue, Object newValue) {
      CustomHashMapNode var4;
      if ((var4 = this.getNode(key)) != null) {
         Object var5 = var4.auj;
         if (var4.auj == oldValue || var5 != null && var5.equals(oldValue)) {
            var4.auj = newValue;
            this.afterNodeAccess(var4);
            return true;
         }
      }

      return false;
   }

   @Override
   public Object replace(Object key, Object value) {
      CustomHashMapNode var3;
      if ((var3 = this.getNode(key)) != null) {
         Object var4 = var3.auj;
         var3.auj = value;
         this.afterNodeAccess(var3);
         return var4;
      } else {
         return null;
      }
   }

   @Override
   public Object computeIfAbsent(Object key, Function mappingFunction) {
      if (mappingFunction == null) {
         throw new NullPointerException();
      }

      int var3;
      CustomHashMapNode[] var4;
      int var6;
      int var8;
      CustomHashMapTreeNode var9;
      CustomHashMapNode var10;
      label67: {
         var3 = hash(key);
         var8 = 0;
         var9 = null;
         var10 = null;
         if (this.XN <= this.bOp) {
            var4 = this.bOm;
            if (this.bOm != null && (var6 = var4.length) != 0) {
               break label67;
            }
         }

         var6 = (var4 = this.resize()).length;
      }

      CustomHashMapNode var5;
      int var7;
      if ((var5 = var4[var7 = var6 - 1 & var3]) != null) {
         if (var5 instanceof CustomHashMapTreeNode) {
            var10 = (var9 = (CustomHashMapTreeNode)var5).getTreeNode(var3, key);
         } else {
            CustomHashMapNode var11 = var5;

            do {
               if (var11.bOC == var3) {
                  Object var12 = var11.bOD;
                  if (var11.bOD == key || key != null && key.equals(var12)) {
                     var10 = var11;
                     break;
                  }
               }

               var8++;
            } while ((var11 = var11.bOu) != null);
         }

         if (var10 != null) {
            Object var13 = var10.auj;
            if (var10.auj != null) {
               this.afterNodeAccess(var10);
               return var13;
            }
         }
      }

      int var14 = this.bOo;
      Object var15 = mappingFunction.apply(key);
      if (var14 != this.bOo) {
         throw new ConcurrentModificationException();
      }

      if (var15 == null) {
         return null;
      }

      if (var10 != null) {
         var10.auj = var15;
         this.afterNodeAccess(var10);
         return var15;
      }

      if (var9 != null) {
         var9.putTreeVal(this, var4, var3, key, var15);
      } else {
         var4[var7] = this.newNode(var3, key, var15, var5);
         if (var8 >= 7) {
            this.treeifyBin(var4, var3);
         }
      }

      this.bOo = var14 + 1;
      this.XN++;
      this.bO(true);
      return var15;
   }

   @Override
   public Object computeIfPresent(Object key, BiFunction remappingFunction) {
      if (remappingFunction == null) {
         throw new NullPointerException();
      }

      CustomHashMapNode var3;
      if ((var3 = this.getNode(key)) != null) {
         Object var4 = var3.auj;
         if (var3.auj != null) {
            int var5 = this.bOo;
            Object var6 = remappingFunction.apply(key, var4);
            if (var5 != this.bOo) {
               throw new ConcurrentModificationException();
            }

            if (var6 != null) {
               var3.auj = var6;
               this.afterNodeAccess(var3);
               return var6;
            }

            int var7 = hash(key);
            this.removeNode(var7, key, null, false, true);
         }
      }

      return null;
   }

   @Override
   public Object compute(Object key, BiFunction remappingFunction) {
      if (remappingFunction == null) {
         throw new NullPointerException();
      }

      int var3;
      CustomHashMapNode[] var4;
      int var6;
      int var8;
      CustomHashMapTreeNode var9;
      CustomHashMapNode var10;
      label67: {
         var3 = hash(key);
         var8 = 0;
         var9 = null;
         var10 = null;
         if (this.XN <= this.bOp) {
            var4 = this.bOm;
            if (this.bOm != null && (var6 = var4.length) != 0) {
               break label67;
            }
         }

         var6 = (var4 = this.resize()).length;
      }

      CustomHashMapNode var5;
      int var7;
      if ((var5 = var4[var7 = var6 - 1 & var3]) != null) {
         if (var5 instanceof CustomHashMapTreeNode) {
            var10 = (var9 = (CustomHashMapTreeNode)var5).getTreeNode(var3, key);
         } else {
            CustomHashMapNode var11 = var5;

            do {
               if (var11.bOC == var3) {
                  Object var12 = var11.bOD;
                  if (var11.bOD == key || key != null && key.equals(var12)) {
                     var10 = var11;
                     break;
                  }
               }

               var8++;
            } while ((var11 = var11.bOu) != null);
         }
      }

      Object var14 = var10 == null ? null : var10.auj;
      int var15 = this.bOo;
      Object var13 = remappingFunction.apply(key, var14);
      if (var15 != this.bOo) {
         throw new ConcurrentModificationException();
      }

      if (var10 != null) {
         if (var13 != null) {
            var10.auj = var13;
            this.afterNodeAccess(var10);
         } else {
            this.removeNode(var3, key, null, false, true);
         }
      } else if (var13 != null) {
         if (var9 != null) {
            var9.putTreeVal(this, var4, var3, key, var13);
         } else {
            var4[var7] = this.newNode(var3, key, var13, var5);
            if (var8 >= 7) {
               this.treeifyBin(var4, var3);
            }
         }

         this.bOo = var15 + 1;
         this.XN++;
         this.bO(true);
      }

      return var13;
   }

   @Override
   public Object merge(Object key, Object value, BiFunction remappingFunction) {
      if (value != null && remappingFunction != null) {
         int var4;
         CustomHashMapNode[] var5;
         int var7;
         int var9;
         CustomHashMapTreeNode var10;
         CustomHashMapNode var11;
         label65: {
            var4 = hash(key);
            var9 = 0;
            var10 = null;
            var11 = null;
            if (this.XN <= this.bOp) {
               var5 = this.bOm;
               if (this.bOm != null && (var7 = var5.length) != 0) {
                  break label65;
               }
            }

            var7 = (var5 = this.resize()).length;
         }

         CustomHashMapNode var6;
         int var8;
         if ((var6 = var5[var8 = var7 - 1 & var4]) != null) {
            if (var6 instanceof CustomHashMapTreeNode) {
               var11 = (var10 = (CustomHashMapTreeNode)var6).getTreeNode(var4, key);
            } else {
               CustomHashMapNode var12 = var6;

               do {
                  if (var12.bOC == var4) {
                     Object var13 = var12.bOD;
                     if (var12.bOD == key || key != null && key.equals(var13)) {
                        var11 = var12;
                        break;
                     }
                  }

                  var9++;
               } while ((var12 = var12.bOu) != null);
            }
         }

         if (var11 != null) {
            Object var14;
            if (var11.auj != null) {
               int var15 = this.bOo;
               var14 = remappingFunction.apply(var11.auj, value);
               if (var15 != this.bOo) {
                  throw new ConcurrentModificationException();
               }
            } else {
               var14 = value;
            }

            if (var14 != null) {
               var11.auj = var14;
               this.afterNodeAccess(var11);
            } else {
               this.removeNode(var4, key, null, false, true);
            }

            return var14;
         } else {
            if (var10 != null) {
               var10.putTreeVal(this, var5, var4, key, value);
            } else {
               var5[var8] = this.newNode(var4, key, value, var6);
               if (var9 >= 7) {
                  this.treeifyBin(var5, var4);
               }
            }

            this.bOo++;
            this.XN++;
            this.bO(true);
            return value;
         }
      } else {
         throw new NullPointerException();
      }
   }

   @Override
   public void forEach(BiConsumer action) {
      if (action == null) {
         throw new NullPointerException();
      }

      if (this.XN > 0) {
         CustomHashMapNode[] var2 = this.bOm;
         if (this.bOm != null) {
            int var3 = this.bOo;
            CustomHashMapNode[] var4 = var2;
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; var6++) {
               for (CustomHashMapNode var7 = var4[var6]; var7 != null; var7 = var7.bOu) {
                  action.accept(var7.bOD, var7.auj);
               }
            }

            if (this.bOo != var3) {
               throw new ConcurrentModificationException();
            }
         }
      }
   }

   @Override
   public void replaceAll(BiFunction function) {
      if (function == null) {
         throw new NullPointerException();
      }

      if (this.XN > 0) {
         CustomHashMapNode[] var2 = this.bOm;
         if (this.bOm != null) {
            int var3 = this.bOo;
            CustomHashMapNode[] var4 = var2;
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; var6++) {
               for (CustomHashMapNode var7 = var4[var6]; var7 != null; var7 = var7.bOu) {
                  var7.auj = function.apply(var7.bOD, var7.auj);
               }
            }

            if (this.bOo != var3) {
               throw new ConcurrentModificationException();
            }
         }
      }
   }

   @Override
   public Object clone() {
      CustomHashMap var1;
      try {
         var1 = (CustomHashMap)super.clone();
      } catch (CloneNotSupportedException var3) {
         throw new InternalError(var3);
      }

      var1.reinitialize();
      var1.putMapEntries(this, false);
      return var1;
   }

   final float getLoadFactor() {
      return this.bOq;
   }

   final int getThreshold() {
      return this.bOm != null ? this.bOm.length : (this.bOp > 0 ? this.bOp : 16);
   }

   CustomHashMapNode newNode(int var1, Object var2, Object var3, CustomHashMapNode var4) {
      return new CustomHashMapNode(var1, var2, var3, var4);
   }

   CustomHashMapNode replacementNode(CustomHashMapNode var1, CustomHashMapNode var2) {
      return new CustomHashMapNode(var1.bOC, var1.bOD, var1.auj, var2);
   }

   CustomHashMapTreeNode newTreeNode(int var1, Object var2, Object var3, CustomHashMapNode var4) {
      return new CustomHashMapTreeNode(var1, var2, var3, var4);
   }

   CustomHashMapTreeNode replacementTreeNode(CustomHashMapNode var1, CustomHashMapNode var2) {
      return new CustomHashMapTreeNode(var1.bOC, var1.bOD, var1.auj, var2);
   }

   void reinitialize() {
      throw new RuntimeException("Not implemented!");
   }

   void afterNodeAccess(CustomHashMapNode var1) {
   }

   void bO(boolean var1) {
   }

   void afterNodeInsertion(CustomHashMapNode var1) {
   }

   void internalWriteEntries(ObjectOutputStream var1) {
      if (this.XN > 0) {
         CustomHashMapNode[] var2 = this.bOm;
         if (this.bOm != null) {
            CustomHashMapNode[] var3 = var2;
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; var5++) {
               for (CustomHashMapNode var6 = var3[var5]; var6 != null; var6 = var6.bOu) {
                  var1.writeObject(var6.bOD);
                  var1.writeObject(var6.auj);
               }
            }
         }
      }
   }
}
