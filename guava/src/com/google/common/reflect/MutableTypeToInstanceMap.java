begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.reflect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|reflect
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
name|collect
operator|.
name|ForwardingMap
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
name|ForwardingMapEntry
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
name|ForwardingSet
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
name|Iterators
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
name|Maps
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
name|CanIgnoreReturnValue
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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|CheckForNull
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
comment|/**  * A mutable type-to-instance map. See also {@link ImmutableTypeToInstanceMap}.  *  *<p>This implementation<i>does</i> support null values, despite how it is annotated; see  * discussion at {@link TypeToInstanceMap}.  *  * @author Ben Yu  * @since 13.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|MutableTypeToInstanceMap
specifier|public
specifier|final
class|class
name|MutableTypeToInstanceMap
parameter_list|<
name|B
parameter_list|>
extends|extends
name|ForwardingMap
argument_list|<
name|TypeToken
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
implements|implements
name|TypeToInstanceMap
argument_list|<
name|B
argument_list|>
block|{
DECL|field|backingMap
specifier|private
specifier|final
name|Map
argument_list|<
name|TypeToken
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
name|backingMap
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
annotation|@
name|Override
annotation|@
name|CheckForNull
DECL|method|getInstance (Class<T> type)
specifier|public
parameter_list|<
name|T
extends|extends
name|B
parameter_list|>
name|T
name|getInstance
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|trustedGet
argument_list|(
name|TypeToken
operator|.
name|of
argument_list|(
name|type
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|CheckForNull
DECL|method|getInstance (TypeToken<T> type)
specifier|public
parameter_list|<
name|T
extends|extends
name|B
parameter_list|>
name|T
name|getInstance
parameter_list|(
name|TypeToken
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|trustedGet
argument_list|(
name|type
operator|.
name|rejectTypeVariables
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|CheckForNull
DECL|method|putInstance (Class<T> type, T value)
specifier|public
parameter_list|<
name|T
extends|extends
name|B
parameter_list|>
name|T
name|putInstance
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|T
name|value
parameter_list|)
block|{
return|return
name|trustedPut
argument_list|(
name|TypeToken
operator|.
name|of
argument_list|(
name|type
argument_list|)
argument_list|,
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|CheckForNull
DECL|method|putInstance (TypeToken<T> type, T value)
specifier|public
parameter_list|<
name|T
extends|extends
name|B
parameter_list|>
name|T
name|putInstance
parameter_list|(
name|TypeToken
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|T
name|value
parameter_list|)
block|{
return|return
name|trustedPut
argument_list|(
name|type
operator|.
name|rejectTypeVariables
argument_list|()
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**    * Not supported. Use {@link #putInstance} instead.    *    * @deprecated unsupported operation    * @throws UnsupportedOperationException always    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Deprecated
annotation|@
name|Override
annotation|@
name|DoNotCall
argument_list|(
literal|"Always throws UnsupportedOperationException"
argument_list|)
annotation|@
name|CheckForNull
DECL|method|put (TypeToken<? extends B> key, B value)
specifier|public
name|B
name|put
parameter_list|(
name|TypeToken
argument_list|<
name|?
extends|extends
name|B
argument_list|>
name|key
parameter_list|,
name|B
name|value
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Please use putInstance() instead."
argument_list|)
throw|;
block|}
comment|/**    * Not supported. Use {@link #putInstance} instead.    *    * @deprecated unsupported operation    * @throws UnsupportedOperationException always    */
annotation|@
name|Deprecated
annotation|@
name|Override
annotation|@
name|DoNotCall
argument_list|(
literal|"Always throws UnsupportedOperationException"
argument_list|)
DECL|method|putAll (Map<? extends TypeToken<? extends B>, ? extends B> map)
specifier|public
name|void
name|putAll
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|TypeToken
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|?
extends|extends
name|B
argument_list|>
name|map
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Please use putInstance() instead."
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|entrySet ()
specifier|public
name|Set
argument_list|<
name|Entry
argument_list|<
name|TypeToken
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
return|return
name|UnmodifiableEntry
operator|.
name|transformEntries
argument_list|(
name|super
operator|.
name|entrySet
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|Map
argument_list|<
name|TypeToken
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|backingMap
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// value could not get in if not a T
annotation|@
name|CheckForNull
DECL|method|trustedPut (TypeToken<T> type, T value)
specifier|private
parameter_list|<
name|T
extends|extends
name|B
parameter_list|>
name|T
name|trustedPut
parameter_list|(
name|TypeToken
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|T
name|value
parameter_list|)
block|{
return|return
operator|(
name|T
operator|)
name|backingMap
operator|.
name|put
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// value could not get in if not a T
annotation|@
name|CheckForNull
DECL|method|trustedGet (TypeToken<T> type)
specifier|private
parameter_list|<
name|T
extends|extends
name|B
parameter_list|>
name|T
name|trustedGet
parameter_list|(
name|TypeToken
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|(
name|T
operator|)
name|backingMap
operator|.
name|get
argument_list|(
name|type
argument_list|)
return|;
block|}
DECL|class|UnmodifiableEntry
specifier|private
specifier|static
specifier|final
class|class
name|UnmodifiableEntry
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ForwardingMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|delegate
specifier|private
specifier|final
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
decl_stmt|;
DECL|method|transformEntries (final Set<Entry<K, V>> entries)
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Set
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|transformEntries
parameter_list|(
specifier|final
name|Set
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entries
parameter_list|)
block|{
return|return
operator|new
name|ForwardingSet
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Set
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|entries
return|;
block|}
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|UnmodifiableEntry
operator|.
name|transformEntries
argument_list|(
name|super
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
index|[]
name|toArray
parameter_list|()
block|{
comment|/*            * standardToArray returns `@Nullable Object[]` rather than `Object[]` but only because it            * can be used with collections that may contain null. This collection is a collection of            * non-null Entry objects (Entry objects that might contain null values but are not            * themselves null), so we can treat it as a plain `Object[]`.            */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"nullness"
argument_list|)
name|Object
index|[]
name|result
init|=
name|standardToArray
argument_list|()
decl_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"nullness"
argument_list|)
comment|// b/192354773 in our checker affects toArray declarations
specifier|public
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
name|T
index|[]
name|toArray
argument_list|(
name|T
index|[]
name|array
argument_list|)
block|{
return|return
name|standardToArray
argument_list|(
name|array
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|method|transformEntries (Iterator<Entry<K, V>> entries)
specifier|private
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|transformEntries
parameter_list|(
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entries
parameter_list|)
block|{
return|return
name|Iterators
operator|.
name|transform
argument_list|(
name|entries
argument_list|,
operator|new
name|Function
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|,
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|apply
parameter_list|(
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
parameter_list|)
block|{
return|return
operator|new
name|UnmodifiableEntry
argument_list|<>
argument_list|(
name|entry
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|UnmodifiableEntry (java.util.Map.Entry<K, V> delegate)
specifier|private
name|UnmodifiableEntry
parameter_list|(
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|checkNotNull
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
annotation|@
name|Override
DECL|method|setValue (V value)
specifier|public
name|V
name|setValue
parameter_list|(
name|V
name|value
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

