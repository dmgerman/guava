begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|checkArgument
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
name|annotations
operator|.
name|GwtCompatible
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|NoSuchElementException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * A sorted map which forwards all its method calls to another sorted map.  * Subclasses should override one or more methods to modify the behavior of  * the backing sorted map as desired per the<a  * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  *<p><i>Warning:</i> The methods of {@code ForwardingSortedMap} forward  *<i>indiscriminately</i> to the methods of the delegate. For example,  * overriding {@link #put} alone<i>will not</i> change the behavior of {@link  * #putAll}, which can lead to unexpected behavior. In this case, you should  * override {@code putAll} as well, either providing your own implementation, or  * delegating to the provided {@code standardPutAll} method.  *  *<p>Each of the {@code standard} methods, where appropriate, use the  * comparator of the map to test equality for both keys and values, unlike  * {@code ForwardingMap}.  *  *<p>The {@code standard} methods and the collection views they return are not  * guaranteed to be thread-safe, even when all of the methods that they depend  * on are thread-safe.  *  * @author Mike Bostock  * @author Louis Wasserman  * @since 2.0 (imported from Google Collections Library)  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ForwardingSortedMap
specifier|public
specifier|abstract
class|class
name|ForwardingSortedMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ForwardingMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|implements
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|// TODO(user): identify places where thread safety is actually lost
comment|/** Constructor for use by subclasses. */
DECL|method|ForwardingSortedMap ()
specifier|protected
name|ForwardingSortedMap
parameter_list|()
block|{}
DECL|method|delegate ()
annotation|@
name|Override
specifier|protected
specifier|abstract
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|comparator ()
specifier|public
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|comparator
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|comparator
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|firstKey ()
specifier|public
name|K
name|firstKey
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|firstKey
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|headMap (K toKey)
specifier|public
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|headMap
parameter_list|(
name|K
name|toKey
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|headMap
argument_list|(
name|toKey
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|lastKey ()
specifier|public
name|K
name|lastKey
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|lastKey
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|subMap (K fromKey, K toKey)
specifier|public
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|subMap
parameter_list|(
name|K
name|fromKey
parameter_list|,
name|K
name|toKey
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|subMap
argument_list|(
name|fromKey
argument_list|,
name|toKey
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|tailMap (K fromKey)
specifier|public
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|tailMap
parameter_list|(
name|K
name|fromKey
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|tailMap
argument_list|(
name|fromKey
argument_list|)
return|;
block|}
comment|/**    * A sensible implementation of {@link SortedMap#keySet} in terms of the methods of    * {@code ForwardingSortedMap}. In many cases, you may wish to override    * {@link ForwardingSortedMap#keySet} to forward to this implementation or a subclass thereof.    *    * @since 15.0    */
annotation|@
name|Beta
DECL|class|StandardKeySet
specifier|protected
class|class
name|StandardKeySet
extends|extends
name|Maps
operator|.
name|SortedKeySet
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|StandardKeySet ()
specifier|public
name|StandardKeySet
parameter_list|()
block|{
name|super
argument_list|(
name|ForwardingSortedMap
operator|.
name|this
argument_list|)
expr_stmt|;
block|}
block|}
comment|// unsafe, but worst case is a CCE is thrown, which callers will be expecting
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|unsafeCompare (Object k1, Object k2)
specifier|private
name|int
name|unsafeCompare
parameter_list|(
name|Object
name|k1
parameter_list|,
name|Object
name|k2
parameter_list|)
block|{
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|comparator
init|=
name|comparator
argument_list|()
decl_stmt|;
if|if
condition|(
name|comparator
operator|==
literal|null
condition|)
block|{
return|return
operator|(
operator|(
name|Comparable
argument_list|<
name|Object
argument_list|>
operator|)
name|k1
operator|)
operator|.
name|compareTo
argument_list|(
name|k2
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|(
operator|(
name|Comparator
argument_list|<
name|Object
argument_list|>
operator|)
name|comparator
operator|)
operator|.
name|compare
argument_list|(
name|k1
argument_list|,
name|k2
argument_list|)
return|;
block|}
block|}
comment|/**    * A sensible definition of {@link #containsKey} in terms of the {@code    * firstKey()} method of {@link #tailMap}. If you override {@link #tailMap},    * you may wish to override {@link #containsKey} to forward to this    * implementation.    *    * @since 7.0    */
DECL|method|standardContainsKey (@ullable Object key)
annotation|@
name|Override
annotation|@
name|Beta
specifier|protected
name|boolean
name|standardContainsKey
parameter_list|(
annotation|@
name|Nullable
name|Object
name|key
parameter_list|)
block|{
try|try
block|{
comment|// any CCE will be caught
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|SortedMap
argument_list|<
name|Object
argument_list|,
name|V
argument_list|>
name|self
init|=
operator|(
name|SortedMap
argument_list|<
name|Object
argument_list|,
name|V
argument_list|>
operator|)
name|this
decl_stmt|;
name|Object
name|ceilingKey
init|=
name|self
operator|.
name|tailMap
argument_list|(
name|key
argument_list|)
operator|.
name|firstKey
argument_list|()
decl_stmt|;
return|return
name|unsafeCompare
argument_list|(
name|ceilingKey
argument_list|,
name|key
argument_list|)
operator|==
literal|0
return|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
comment|/**    * A sensible default implementation of {@link #subMap(Object, Object)} in    * terms of {@link #headMap(Object)} and {@link #tailMap(Object)}. In some    * situations, you may wish to override {@link #subMap(Object, Object)} to    * forward to this implementation.    *    * @since 7.0    */
DECL|method|standardSubMap (K fromKey, K toKey)
annotation|@
name|Beta
specifier|protected
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|standardSubMap
parameter_list|(
name|K
name|fromKey
parameter_list|,
name|K
name|toKey
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|unsafeCompare
argument_list|(
name|fromKey
argument_list|,
name|toKey
argument_list|)
operator|<=
literal|0
argument_list|,
literal|"fromKey must be<= toKey"
argument_list|)
expr_stmt|;
return|return
name|tailMap
argument_list|(
name|fromKey
argument_list|)
operator|.
name|headMap
argument_list|(
name|toKey
argument_list|)
return|;
block|}
block|}
end_class

end_unit

