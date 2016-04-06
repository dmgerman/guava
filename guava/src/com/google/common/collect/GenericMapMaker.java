begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS-IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Beta
import|;
end_import

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
name|GwtCompatible
import|;
end_import

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
name|common
operator|.
name|base
operator|.
name|Equivalence
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
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
name|concurrent
operator|.
name|ConcurrentMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_comment
comment|/**  * A class exactly like {@link MapMaker}, except restricted in the types of maps it can build.  * For the most part, you should probably just ignore the existence of this class.  *  * @param<K0> the base type for all key types of maps built by this map maker  * @param<V0> the base type for all value types of maps built by this map maker  * @author Kevin Bourrillion  * @since 7.0  * @deprecated This class existed only to support the generic paramterization necessary for the  *     caching functionality in {@code MapMaker}. That functionality has been moved to {@link  *     com.google.common.cache.CacheBuilder}, which is a properly generified class and thus needs no  *     "Generic" equivalent; simple use {@code CacheBuilder} naturally. For general migration  *     instructions, see the<a  *     href="https://github.com/google/guava/wiki/MapMakerMigration">MapMaker Migration  *     Guide</a>.  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|Deprecated
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|GenericMapMaker
specifier|abstract
class|class
name|GenericMapMaker
parameter_list|<
name|K0
parameter_list|,
name|V0
parameter_list|>
block|{
comment|// No subclasses but our own
DECL|method|GenericMapMaker ()
name|GenericMapMaker
parameter_list|()
block|{}
comment|/**    * See {@link MapMaker#keyEquivalence}.    */
annotation|@
name|GwtIncompatible
comment|// To be supported
DECL|method|keyEquivalence (Equivalence<Object> equivalence)
specifier|abstract
name|GenericMapMaker
argument_list|<
name|K0
argument_list|,
name|V0
argument_list|>
name|keyEquivalence
parameter_list|(
name|Equivalence
argument_list|<
name|Object
argument_list|>
name|equivalence
parameter_list|)
function_decl|;
comment|/**    * See {@link MapMaker#initialCapacity}.    */
DECL|method|initialCapacity (int initialCapacity)
specifier|public
specifier|abstract
name|GenericMapMaker
argument_list|<
name|K0
argument_list|,
name|V0
argument_list|>
name|initialCapacity
parameter_list|(
name|int
name|initialCapacity
parameter_list|)
function_decl|;
comment|/**    * See {@link MapMaker#maximumSize}.    */
DECL|method|maximumSize (int maximumSize)
specifier|abstract
name|GenericMapMaker
argument_list|<
name|K0
argument_list|,
name|V0
argument_list|>
name|maximumSize
parameter_list|(
name|int
name|maximumSize
parameter_list|)
function_decl|;
comment|/**    * See {@link MapMaker#concurrencyLevel}.    */
DECL|method|concurrencyLevel (int concurrencyLevel)
specifier|public
specifier|abstract
name|GenericMapMaker
argument_list|<
name|K0
argument_list|,
name|V0
argument_list|>
name|concurrencyLevel
parameter_list|(
name|int
name|concurrencyLevel
parameter_list|)
function_decl|;
comment|/**    * See {@link MapMaker#weakKeys}.    */
annotation|@
name|GwtIncompatible
comment|// java.lang.ref.WeakReference
DECL|method|weakKeys ()
specifier|public
specifier|abstract
name|GenericMapMaker
argument_list|<
name|K0
argument_list|,
name|V0
argument_list|>
name|weakKeys
parameter_list|()
function_decl|;
comment|/**    * See {@link MapMaker#weakValues}.    */
annotation|@
name|GwtIncompatible
comment|// java.lang.ref.WeakReference
DECL|method|weakValues ()
specifier|public
specifier|abstract
name|GenericMapMaker
argument_list|<
name|K0
argument_list|,
name|V0
argument_list|>
name|weakValues
parameter_list|()
function_decl|;
comment|/**    * See {@link MapMaker#softValues}.    *    * @deprecated Caching functionality in {@code MapMaker} has been moved to {@link    *     com.google.common.cache.CacheBuilder}, with {@link #softValues} being replaced by {@link    *     com.google.common.cache.CacheBuilder#softValues}. Note that {@code CacheBuilder} is simply    *     an enhanced API for an implementation which was branched from {@code MapMaker}.    */
annotation|@
name|Deprecated
annotation|@
name|GwtIncompatible
comment|// java.lang.ref.SoftReference
DECL|method|softValues ()
specifier|abstract
name|GenericMapMaker
argument_list|<
name|K0
argument_list|,
name|V0
argument_list|>
name|softValues
parameter_list|()
function_decl|;
comment|/**    * See {@link MapMaker#expireAfterWrite}.    */
DECL|method|expireAfterWrite (long duration, TimeUnit unit)
specifier|abstract
name|GenericMapMaker
argument_list|<
name|K0
argument_list|,
name|V0
argument_list|>
name|expireAfterWrite
parameter_list|(
name|long
name|duration
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
function_decl|;
comment|/**    * See {@link MapMaker#expireAfterAccess}.    */
annotation|@
name|GwtIncompatible
DECL|method|expireAfterAccess (long duration, TimeUnit unit)
specifier|abstract
name|GenericMapMaker
argument_list|<
name|K0
argument_list|,
name|V0
argument_list|>
name|expireAfterAccess
parameter_list|(
name|long
name|duration
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
function_decl|;
comment|/**    * See {@link MapMaker#makeMap}.    */
DECL|method|makeMap ()
specifier|public
specifier|abstract
parameter_list|<
name|K
extends|extends
name|K0
parameter_list|,
name|V
extends|extends
name|V0
parameter_list|>
name|ConcurrentMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|makeMap
parameter_list|()
function_decl|;
comment|/**    * See {@link MapMaker#makeCustomMap}.    */
annotation|@
name|GwtIncompatible
comment|// MapMakerInternalMap
DECL|method|makeCustomMap ()
specifier|abstract
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|MapMakerInternalMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|makeCustomMap
parameter_list|()
function_decl|;
comment|/**    * See {@link MapMaker#makeComputingMap}.    */
annotation|@
name|Deprecated
DECL|method|makeComputingMap ( Function<? super K, ? extends V> computingFunction)
specifier|abstract
parameter_list|<
name|K
extends|extends
name|K0
parameter_list|,
name|V
extends|extends
name|V0
parameter_list|>
name|ConcurrentMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|makeComputingMap
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|computingFunction
parameter_list|)
function_decl|;
block|}
end_class

end_unit

