begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2010 Google Inc. All Rights Reserved.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS-IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * A class exactly like {@link MapMaker}, except restricted in the types of maps  * it can build. This type is returned by {@link MapMaker#evictionListener} to  * prevent the user from trying to build a map that's incompatible with the key  * and value types of the listener.  *  * @param<K0> the base type for all key types of maps built by this map maker  * @param<V0> the base type for all value types of maps built by this map maker  * @author Kevin Bourrillion  * @since 7  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|GenericMapMaker
specifier|public
specifier|abstract
class|class
name|GenericMapMaker
parameter_list|<
name|K0
parameter_list|,
name|V0
parameter_list|>
block|{
comment|// Set by MapMaker, but sits in this class to preserve the type relationship
annotation|@
name|GwtIncompatible
argument_list|(
literal|"To be supported"
argument_list|)
DECL|field|evictionListener
name|MapEvictionListener
argument_list|<
name|K0
argument_list|,
name|V0
argument_list|>
name|evictionListener
decl_stmt|;
comment|// No subclasses but our own
DECL|method|GenericMapMaker ()
name|GenericMapMaker
parameter_list|()
block|{}
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
comment|/**    * See {@link MapMaker#maximumSize}.    *    * @since 8    */
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
argument_list|(
literal|"To be supported"
argument_list|)
DECL|method|maximumSize (int maximumSize)
specifier|public
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
annotation|@
name|GwtIncompatible
argument_list|(
literal|"java.util.concurrent.ConcurrentHashMap concurrencyLevel"
argument_list|)
DECL|method|concurrencyLevel ( int concurrencyLevel)
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
argument_list|(
literal|"java.lang.ref.WeakReference"
argument_list|)
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
comment|/**    * See {@link MapMaker#softKeys}.    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"java.lang.ref.SoftReference"
argument_list|)
DECL|method|softKeys ()
specifier|public
specifier|abstract
name|GenericMapMaker
argument_list|<
name|K0
argument_list|,
name|V0
argument_list|>
name|softKeys
parameter_list|()
function_decl|;
comment|/**    * See {@link MapMaker#weakValues}.    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"java.lang.ref.WeakReference"
argument_list|)
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
comment|/**    * See {@link MapMaker#softValues}.    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"java.lang.ref.SoftReference"
argument_list|)
DECL|method|softValues ()
specifier|public
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
comment|/**    * See {@link MapMaker#expiration}.    */
comment|// TODO(user): deprecate
DECL|method|expiration ( long duration, TimeUnit unit)
specifier|public
specifier|abstract
name|GenericMapMaker
argument_list|<
name|K0
argument_list|,
name|V0
argument_list|>
name|expiration
parameter_list|(
name|long
name|duration
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
function_decl|;
comment|/**    * See {@link MapMaker#expireAfterWrite}.    *    * @since 8    */
annotation|@
name|Beta
DECL|method|expireAfterWrite ( long duration, TimeUnit unit)
specifier|public
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
comment|/**    * See {@link MapMaker#expireAfterAccess}.    *    * @since 8    */
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
argument_list|(
literal|"To be supported"
argument_list|)
DECL|method|expireAfterAccess ( long duration, TimeUnit unit)
specifier|public
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
comment|/*    * Note that MapMaker's evictionListener() is not here, because once you're    * interacting with a GenericMapMaker you've already called that, and    * shouldn't be calling it again.    */
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
comment|/**    * See {@link MapMaker#makeComputingMap}.    */
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
DECL|method|makeComputingMap (Function<? super K, ? extends V> computingFunction)
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

