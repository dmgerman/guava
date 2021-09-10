begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|GwtIncompatible
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|errorprone
operator|.
name|annotations
operator|.
name|DoNotCall
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|BinaryOperator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|qual
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * "Overrides" the {@link ImmutableMap} static methods that lack {@link ImmutableSortedMap}  * equivalents with deprecated, exception-throwing versions. See {@link  * ImmutableSortedSetFauxverideShim} for details.  *  * @author Chris Povirk  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|ImmutableSortedMapFauxverideShim
specifier|abstract
class|class
name|ImmutableSortedMapFauxverideShim
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|/**    * Not supported. Use {@link ImmutableSortedMap#toImmutableSortedMap}, which offers better    * type-safety, instead. This method exists only to hide {@link ImmutableMap#toImmutableMap} from    * consumers of {@code ImmutableSortedMap}.    *    * @throws UnsupportedOperationException always    * @deprecated Use {@link ImmutableSortedMap#toImmutableSortedMap}.    */
annotation|@
name|DoNotCall
argument_list|(
literal|"Use toImmutableSortedMap"
argument_list|)
annotation|@
name|Deprecated
specifier|public
specifier|static
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|,
name|K
operator|,
name|V
operator|>
DECL|method|toImmutableMap ( Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction)
name|Collector
argument_list|<
name|T
argument_list|,
name|?
argument_list|,
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|toImmutableMap
argument_list|(
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|K
argument_list|>
name|keyFunction
operator|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|valueFunction
argument_list|)
block|{
throw|throw
argument_list|new
name|UnsupportedOperationException
argument_list|()
block|;   }
comment|/**    * Not supported. Use {@link ImmutableSortedMap#toImmutableSortedMap}, which offers better    * type-safety, instead. This method exists only to hide {@link ImmutableMap#toImmutableMap} from    * consumers of {@code ImmutableSortedMap}.    *    * @throws UnsupportedOperationException always    * @deprecated Use {@link ImmutableSortedMap#toImmutableSortedMap}.    */
expr|@
name|DoNotCall
argument_list|(
literal|"Use toImmutableSortedMap"
argument_list|)
expr|@
name|Deprecated
specifier|public
specifier|static
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|,
name|K
operator|,
name|V
operator|>
DECL|method|toImmutableMap ( Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction)
name|Collector
argument_list|<
name|T
argument_list|,
name|?
argument_list|,
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|toImmutableMap
argument_list|(
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|K
argument_list|>
name|keyFunction
operator|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|valueFunction
operator|,
name|BinaryOperator
argument_list|<
name|V
argument_list|>
name|mergeFunction
argument_list|)
block|{
throw|throw
argument_list|new
name|UnsupportedOperationException
argument_list|()
block|;   }
comment|/**    * Not supported. Use {@link ImmutableSortedMap#naturalOrder}, which offers better type-safety,    * instead. This method exists only to hide {@link ImmutableMap#builder} from consumers of {@code    * ImmutableSortedMap}.    *    * @throws UnsupportedOperationException always    * @deprecated Use {@link ImmutableSortedMap#naturalOrder}, which offers better type-safety.    */
expr|@
name|DoNotCall
argument_list|(
literal|"Use naturalOrder"
argument_list|)
expr|@
name|Deprecated
DECL|method|builder ()
specifier|public
specifier|static
operator|<
name|K
operator|,
name|V
operator|>
name|ImmutableSortedMap
operator|.
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|builder
argument_list|()
block|{
throw|throw
argument_list|new
name|UnsupportedOperationException
argument_list|()
block|;   }
comment|/**    * Not supported for ImmutableSortedMap.    *    * @throws UnsupportedOperationException always    * @deprecated Not supported for ImmutableSortedMap.    */
expr|@
name|DoNotCall
argument_list|(
literal|"Use naturalOrder (which does not accept an expected size)"
argument_list|)
expr|@
name|Deprecated
DECL|method|builderWithExpectedSize (int expectedSize)
specifier|public
specifier|static
operator|<
name|K
operator|,
name|V
operator|>
name|ImmutableSortedMap
operator|.
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|builderWithExpectedSize
argument_list|(
name|int
name|expectedSize
argument_list|)
block|{
throw|throw
argument_list|new
name|UnsupportedOperationException
argument_list|()
block|;   }
comment|/**    * Not supported.<b>You are attempting to create a map that may contain a non-{@code Comparable}    * key.</b> Proper calls will resolve to the version in {@code ImmutableSortedMap}, not this dummy    * version.    *    * @throws UnsupportedOperationException always    * @deprecated<b>Pass a key of type {@code Comparable} to use {@link    *     ImmutableSortedMap#of(Comparable, Object)}.</b>    */
expr|@
name|DoNotCall
argument_list|(
literal|"Pass a key of type Comparable"
argument_list|)
expr|@
name|Deprecated
DECL|method|of (K k1, V v1)
specifier|public
specifier|static
operator|<
name|K
operator|,
name|V
operator|>
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
argument_list|(
name|K
name|k1
argument_list|,
name|V
name|v1
argument_list|)
block|{
throw|throw
argument_list|new
name|UnsupportedOperationException
argument_list|()
block|;   }
comment|/**    * Not supported.<b>You are attempting to create a map that may contain non-{@code Comparable}    * keys.</b> Proper calls will resolve to the version in {@code ImmutableSortedMap}, not this    * dummy version.    *    * @throws UnsupportedOperationException always    * @deprecated<b>Pass keys of type {@code Comparable} to use {@link    *     ImmutableSortedMap#of(Comparable, Object, Comparable, Object)}.</b>    */
expr|@
name|DoNotCall
argument_list|(
literal|"Pass keys of type Comparable"
argument_list|)
expr|@
name|Deprecated
DECL|method|of (K k1, V v1, K k2, V v2)
specifier|public
specifier|static
operator|<
name|K
operator|,
name|V
operator|>
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
argument_list|(
name|K
name|k1
argument_list|,
name|V
name|v1
argument_list|,
name|K
name|k2
argument_list|,
name|V
name|v2
argument_list|)
block|{
throw|throw
argument_list|new
name|UnsupportedOperationException
argument_list|()
block|;   }
comment|/**    * Not supported.<b>You are attempting to create a map that may contain non-{@code Comparable}    * keys.</b> Proper calls to will resolve to the version in {@code ImmutableSortedMap}, not this    * dummy version.    *    * @throws UnsupportedOperationException always    * @deprecated<b>Pass keys of type {@code Comparable} to use {@link    *     ImmutableSortedMap#of(Comparable, Object, Comparable, Object, Comparable, Object)}.</b>    */
expr|@
name|DoNotCall
argument_list|(
literal|"Pass keys of type Comparable"
argument_list|)
expr|@
name|Deprecated
DECL|method|of (K k1, V v1, K k2, V v2, K k3, V v3)
specifier|public
specifier|static
operator|<
name|K
operator|,
name|V
operator|>
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
argument_list|(
name|K
name|k1
argument_list|,
name|V
name|v1
argument_list|,
name|K
name|k2
argument_list|,
name|V
name|v2
argument_list|,
name|K
name|k3
argument_list|,
name|V
name|v3
argument_list|)
block|{
throw|throw
argument_list|new
name|UnsupportedOperationException
argument_list|()
block|;   }
comment|/**    * Not supported.<b>You are attempting to create a map that may contain non-{@code Comparable}    * keys.</b> Proper calls will resolve to the version in {@code ImmutableSortedMap}, not this    * dummy version.    *    * @throws UnsupportedOperationException always    * @deprecated<b>Pass keys of type {@code Comparable} to use {@link    *     ImmutableSortedMap#of(Comparable, Object, Comparable, Object, Comparable, Object,    *     Comparable, Object)}.</b>    */
expr|@
name|DoNotCall
argument_list|(
literal|"Pass keys of type Comparable"
argument_list|)
expr|@
name|Deprecated
DECL|method|of (K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4)
specifier|public
specifier|static
operator|<
name|K
operator|,
name|V
operator|>
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
argument_list|(
name|K
name|k1
argument_list|,
name|V
name|v1
argument_list|,
name|K
name|k2
argument_list|,
name|V
name|v2
argument_list|,
name|K
name|k3
argument_list|,
name|V
name|v3
argument_list|,
name|K
name|k4
argument_list|,
name|V
name|v4
argument_list|)
block|{
throw|throw
argument_list|new
name|UnsupportedOperationException
argument_list|()
block|;   }
comment|/**    * Not supported.<b>You are attempting to create a map that may contain non-{@code Comparable}    * keys.</b> Proper calls will resolve to the version in {@code ImmutableSortedMap}, not this    * dummy version.    *    * @throws UnsupportedOperationException always    * @deprecated<b>Pass keys of type {@code Comparable} to use {@link    *     ImmutableSortedMap#of(Comparable, Object, Comparable, Object, Comparable, Object,    *     Comparable, Object, Comparable, Object)}.</b>    */
expr|@
name|DoNotCall
argument_list|(
literal|"Pass keys of type Comparable"
argument_list|)
expr|@
name|Deprecated
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5)
specifier|public
specifier|static
operator|<
name|K
operator|,
name|V
operator|>
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
argument_list|(
name|K
name|k1
argument_list|,
name|V
name|v1
argument_list|,
name|K
name|k2
argument_list|,
name|V
name|v2
argument_list|,
name|K
name|k3
argument_list|,
name|V
name|v3
argument_list|,
name|K
name|k4
argument_list|,
name|V
name|v4
argument_list|,
name|K
name|k5
argument_list|,
name|V
name|v5
argument_list|)
block|{
throw|throw
argument_list|new
name|UnsupportedOperationException
argument_list|()
block|;   }
comment|/**    * Not supported.<b>You are attempting to create a map that may contain non-{@code Comparable}    * keys.</b> Proper calls will resolve to the version in {@code ImmutableSortedMap}, not this    * dummy version.    *    * @throws UnsupportedOperationException always    * @deprecated<b>Pass keys of type {@code Comparable} to use {@link    *     ImmutableSortedMap#of(Comparable, Object, Comparable, Object, Comparable, Object,    *     Comparable, Object, Comparable, Object)}.</b>    */
expr|@
name|DoNotCall
argument_list|(
literal|"Pass keys of type Comparable"
argument_list|)
expr|@
name|Deprecated
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6)
specifier|public
specifier|static
operator|<
name|K
operator|,
name|V
operator|>
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
argument_list|(
name|K
name|k1
argument_list|,
name|V
name|v1
argument_list|,
name|K
name|k2
argument_list|,
name|V
name|v2
argument_list|,
name|K
name|k3
argument_list|,
name|V
name|v3
argument_list|,
name|K
name|k4
argument_list|,
name|V
name|v4
argument_list|,
name|K
name|k5
argument_list|,
name|V
name|v5
argument_list|,
name|K
name|k6
argument_list|,
name|V
name|v6
argument_list|)
block|{
throw|throw
argument_list|new
name|UnsupportedOperationException
argument_list|()
block|;   }
comment|/**    * Not supported.<b>You are attempting to create a map that may contain non-{@code Comparable}    * keys.</b> Proper calls will resolve to the version in {@code ImmutableSortedMap}, not this    * dummy version.    *    * @throws UnsupportedOperationException always    * @deprecated<b>Pass keys of type {@code Comparable} to use {@link    *     ImmutableSortedMap#of(Comparable, Object, Comparable, Object, Comparable, Object,    *     Comparable, Object, Comparable, Object)}.</b>    */
expr|@
name|DoNotCall
argument_list|(
literal|"Pass keys of type Comparable"
argument_list|)
expr|@
name|Deprecated
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7)
specifier|public
specifier|static
operator|<
name|K
operator|,
name|V
operator|>
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
argument_list|(
name|K
name|k1
argument_list|,
name|V
name|v1
argument_list|,
name|K
name|k2
argument_list|,
name|V
name|v2
argument_list|,
name|K
name|k3
argument_list|,
name|V
name|v3
argument_list|,
name|K
name|k4
argument_list|,
name|V
name|v4
argument_list|,
name|K
name|k5
argument_list|,
name|V
name|v5
argument_list|,
name|K
name|k6
argument_list|,
name|V
name|v6
argument_list|,
name|K
name|k7
argument_list|,
name|V
name|v7
argument_list|)
block|{
throw|throw
argument_list|new
name|UnsupportedOperationException
argument_list|()
block|;   }
comment|/**    * Not supported.<b>You are attempting to create a map that may contain non-{@code Comparable}    * keys.</b> Proper calls will resolve to the version in {@code ImmutableSortedMap}, not this    * dummy version.    *    * @throws UnsupportedOperationException always    * @deprecated<b>Pass keys of type {@code Comparable} to use {@link    *     ImmutableSortedMap#of(Comparable, Object, Comparable, Object, Comparable, Object,    *     Comparable, Object, Comparable, Object)}.</b>    */
expr|@
name|DoNotCall
argument_list|(
literal|"Pass keys of type Comparable"
argument_list|)
expr|@
name|Deprecated
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8)
specifier|public
specifier|static
operator|<
name|K
operator|,
name|V
operator|>
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
argument_list|(
name|K
name|k1
argument_list|,
name|V
name|v1
argument_list|,
name|K
name|k2
argument_list|,
name|V
name|v2
argument_list|,
name|K
name|k3
argument_list|,
name|V
name|v3
argument_list|,
name|K
name|k4
argument_list|,
name|V
name|v4
argument_list|,
name|K
name|k5
argument_list|,
name|V
name|v5
argument_list|,
name|K
name|k6
argument_list|,
name|V
name|v6
argument_list|,
name|K
name|k7
argument_list|,
name|V
name|v7
argument_list|,
name|K
name|k8
argument_list|,
name|V
name|v8
argument_list|)
block|{
throw|throw
argument_list|new
name|UnsupportedOperationException
argument_list|()
block|;   }
comment|/**    * Not supported.<b>You are attempting to create a map that may contain non-{@code Comparable}    * keys.</b> Proper calls will resolve to the version in {@code ImmutableSortedMap}, not this    * dummy version.    *    * @throws UnsupportedOperationException always    * @deprecated<b>Pass keys of type {@code Comparable} to use {@link    *     ImmutableSortedMap#of(Comparable, Object, Comparable, Object, Comparable, Object,    *     Comparable, Object, Comparable, Object)}.</b>    */
expr|@
name|DoNotCall
argument_list|(
literal|"Pass keys of type Comparable"
argument_list|)
expr|@
name|Deprecated
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9)
specifier|public
specifier|static
operator|<
name|K
operator|,
name|V
operator|>
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
argument_list|(
name|K
name|k1
argument_list|,
name|V
name|v1
argument_list|,
name|K
name|k2
argument_list|,
name|V
name|v2
argument_list|,
name|K
name|k3
argument_list|,
name|V
name|v3
argument_list|,
name|K
name|k4
argument_list|,
name|V
name|v4
argument_list|,
name|K
name|k5
argument_list|,
name|V
name|v5
argument_list|,
name|K
name|k6
argument_list|,
name|V
name|v6
argument_list|,
name|K
name|k7
argument_list|,
name|V
name|v7
argument_list|,
name|K
name|k8
argument_list|,
name|V
name|v8
argument_list|,
name|K
name|k9
argument_list|,
name|V
name|v9
argument_list|)
block|{
throw|throw
argument_list|new
name|UnsupportedOperationException
argument_list|()
block|;   }
comment|/**    * Not supported.<b>You are attempting to create a map that may contain non-{@code Comparable}    * keys.</b> Proper calls will resolve to the version in {@code ImmutableSortedMap}, not this    * dummy version.    *    * @throws UnsupportedOperationException always    * @deprecated<b>Pass keys of type {@code Comparable} to use {@link    *     ImmutableSortedMap#of(Comparable, Object, Comparable, Object, Comparable, Object,    *     Comparable, Object, Comparable, Object)}.</b>    */
expr|@
name|DoNotCall
argument_list|(
literal|"Pass keys of type Comparable"
argument_list|)
expr|@
name|Deprecated
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10)
specifier|public
specifier|static
operator|<
name|K
operator|,
name|V
operator|>
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
argument_list|(
name|K
name|k1
argument_list|,
name|V
name|v1
argument_list|,
name|K
name|k2
argument_list|,
name|V
name|v2
argument_list|,
name|K
name|k3
argument_list|,
name|V
name|v3
argument_list|,
name|K
name|k4
argument_list|,
name|V
name|v4
argument_list|,
name|K
name|k5
argument_list|,
name|V
name|v5
argument_list|,
name|K
name|k6
argument_list|,
name|V
name|v6
argument_list|,
name|K
name|k7
argument_list|,
name|V
name|v7
argument_list|,
name|K
name|k8
argument_list|,
name|V
name|v8
argument_list|,
name|K
name|k9
argument_list|,
name|V
name|v9
argument_list|,
name|K
name|k10
argument_list|,
name|V
name|v10
argument_list|)
block|{
throw|throw
argument_list|new
name|UnsupportedOperationException
argument_list|()
block|;   }
comment|/**    * Not supported. Use {@code ImmutableSortedMap.copyOf(ImmutableMap.ofEntries(...))}.    *    * @deprecated Use {@code ImmutableSortedMap.copyOf(ImmutableMap.ofEntries(...))}.    */
expr|@
name|DoNotCall
argument_list|(
literal|"ImmutableSortedMap.ofEntries not currently available; use ImmutableSortedMap.copyOf"
argument_list|)
expr|@
name|Deprecated
DECL|method|ofEntries ( Entry<? extends K, ? extends V>.... entries)
specifier|public
specifier|static
operator|<
name|K
operator|,
name|V
operator|>
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|ofEntries
argument_list|(
name|Entry
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
operator|...
name|entries
argument_list|)
block|{
throw|throw
argument_list|new
name|UnsupportedOperationException
argument_list|()
block|;   }
comment|// No copyOf() fauxveride; see ImmutableSortedSetFauxverideShim.
block|}
end_class

end_unit

