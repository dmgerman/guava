begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2010 The Guava Authors All Rights Reserved.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS-IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Objects
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
name|collect
operator|.
name|MapMaker
operator|.
name|RemovalListener
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
name|collect
operator|.
name|MapMaker
operator|.
name|RemovalNotification
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
comment|/**  * A class exactly like {@link MapMaker}, except restricted in the types of maps it can build. This  * type is returned by {@link MapMaker#removalListener} to prevent the user from trying to build a  * map that's incompatible with the key and value types of the listener.  *  * @param<K0> the base type for all key types of maps built by this map maker  * @param<V0> the base type for all value types of maps built by this map maker  * @author Kevin Bourrillion  * @since Guava release 07  */
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
annotation|@
name|GwtIncompatible
argument_list|(
literal|"To be supported"
argument_list|)
DECL|enum|NullListener
enum|enum
name|NullListener
implements|implements
name|RemovalListener
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
annotation|@
name|Override
DECL|method|onRemoval (RemovalNotification<Object, Object> notification)
specifier|public
name|void
name|onRemoval
parameter_list|(
name|RemovalNotification
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|notification
parameter_list|)
block|{}
block|}
comment|// Set by MapMaker, but sits in this class to preserve the type relationship
annotation|@
name|GwtIncompatible
argument_list|(
literal|"To be supported"
argument_list|)
DECL|field|removalListener
name|RemovalListener
argument_list|<
name|K0
argument_list|,
name|V0
argument_list|>
name|removalListener
decl_stmt|;
comment|// No subclasses but our own
DECL|method|GenericMapMaker ()
name|GenericMapMaker
parameter_list|()
block|{}
comment|// TODO(kevinb): undo this indirection once keyEquiv is made package-private
annotation|@
name|GwtIncompatible
argument_list|(
literal|"To be removed when #keyEquivalence is supported"
argument_list|)
DECL|method|privateKeyEquivalence ( Equivalence<Object> equivalence)
specifier|abstract
name|GenericMapMaker
argument_list|<
name|K0
argument_list|,
name|V0
argument_list|>
name|privateKeyEquivalence
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
comment|/**    * See {@link MapMaker#maximumSize}.    *    * @since Guava release 08    */
annotation|@
name|Beta
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
comment|/**    * See {@link MapMaker#strongKeys}.    */
DECL|method|strongKeys ()
specifier|abstract
name|GenericMapMaker
argument_list|<
name|K0
argument_list|,
name|V0
argument_list|>
name|strongKeys
parameter_list|()
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
comment|/**    * See {@link MapMaker#strongValues}.    */
DECL|method|strongValues ()
specifier|abstract
name|GenericMapMaker
argument_list|<
name|K0
argument_list|,
name|V0
argument_list|>
name|strongValues
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
annotation|@
name|Deprecated
specifier|public
DECL|method|expiration (long duration, TimeUnit unit)
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
comment|/**    * See {@link MapMaker#expireAfterWrite}.    *    * @since Guava release 08    */
DECL|method|expireAfterWrite (long duration, TimeUnit unit)
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
comment|/**    * See {@link MapMaker#expireAfterAccess}.    *    * @since Guava release 08    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"To be supported"
argument_list|)
DECL|method|expireAfterAccess (long duration, TimeUnit unit)
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
comment|/*    * Note that MapMaker's removalListener() is not here, because once you're interacting with a    * GenericMapMaker you've already called that, and shouldn't be calling it again.    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// safe covariant cast
annotation|@
name|GwtIncompatible
argument_list|(
literal|"To be supported"
argument_list|)
DECL|method|getRemovalListener ()
parameter_list|<
name|K
extends|extends
name|K0
parameter_list|,
name|V
extends|extends
name|V0
parameter_list|>
name|RemovalListener
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getRemovalListener
parameter_list|()
block|{
return|return
operator|(
name|RemovalListener
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|Objects
operator|.
name|firstNonNull
argument_list|(
name|removalListener
argument_list|,
name|NullListener
operator|.
name|INSTANCE
argument_list|)
return|;
block|}
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
argument_list|(
literal|"CustomConcurrentHashMap"
argument_list|)
DECL|method|makeCustomMap ()
specifier|abstract
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|CustomConcurrentHashMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|makeCustomMap
parameter_list|()
function_decl|;
comment|/**    * See {@link MapMaker#makeComputingMap}.    */
DECL|method|makeComputingMap ( Function<? super K, ? extends V> computingFunction)
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

