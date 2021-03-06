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
name|errorprone
operator|.
name|annotations
operator|.
name|CanIgnoreReturnValue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Map
operator|.
name|Entry
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
comment|/**  * Basic implementation of the {@link SetMultimap} interface. It's a wrapper around {@link  * AbstractMapBasedMultimap} that converts the returned collections into {@code Sets}. The {@link  * #createCollection} method must return a {@code Set}.  *  * @author Jared Levy  */
end_comment

begin_annotation
annotation|@
name|GwtCompatible
end_annotation

begin_annotation
annotation|@
name|ElementTypesAreNonnullByDefault
end_annotation

begin_expr_stmt
DECL|class|AbstractSetMultimap
specifier|abstract
name|class
name|AbstractSetMultimap
operator|<
name|K
expr|extends @
name|Nullable
name|Object
operator|,
name|V
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|AbstractMapBasedMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
expr|implements
name|SetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|/**    * Creates a new multimap that uses the provided map.    *    * @param map place to store the mapping from each key to its corresponding values    */
DECL|method|AbstractSetMultimap (Map<K, Collection<V>> map)
specifier|protected
name|AbstractSetMultimap
argument_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|map
argument_list|)
block|{
name|super
argument_list|(
name|map
argument_list|)
block|;   }
expr|@
name|Override
DECL|method|createCollection ()
specifier|abstract
name|Set
argument_list|<
name|V
argument_list|>
name|createCollection
argument_list|()
block|;    @
name|Override
DECL|method|createUnmodifiableEmptyCollection ()
name|Set
argument_list|<
name|V
argument_list|>
name|createUnmodifiableEmptyCollection
argument_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
expr|@
name|Override
DECL|method|unmodifiableCollectionSubclass ( Collection<E> collection)
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
name|Collection
argument_list|<
name|E
argument_list|>
name|unmodifiableCollectionSubclass
argument_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|collection
argument_list|)
block|{
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
operator|(
name|Set
argument_list|<
name|E
argument_list|>
operator|)
name|collection
argument_list|)
return|;
block|}
end_expr_stmt

begin_function
annotation|@
name|Override
DECL|method|wrapCollection (@arametricNullness K key, Collection<V> collection)
name|Collection
argument_list|<
name|V
argument_list|>
name|wrapCollection
parameter_list|(
annotation|@
name|ParametricNullness
name|K
name|key
parameter_list|,
name|Collection
argument_list|<
name|V
argument_list|>
name|collection
parameter_list|)
block|{
return|return
operator|new
name|WrappedSet
argument_list|(
name|key
argument_list|,
operator|(
name|Set
argument_list|<
name|V
argument_list|>
operator|)
name|collection
argument_list|)
return|;
block|}
end_function

begin_comment
comment|// Following Javadoc copied from SetMultimap.
end_comment

begin_comment
comment|/**    * {@inheritDoc}    *    *<p>Because a {@code SetMultimap} has unique values for a given key, this method returns a    * {@link Set}, instead of the {@link Collection} specified in the {@link Multimap} interface.    */
end_comment

begin_function
annotation|@
name|Override
DECL|method|get (@arametricNullness K key)
specifier|public
name|Set
argument_list|<
name|V
argument_list|>
name|get
parameter_list|(
annotation|@
name|ParametricNullness
name|K
name|key
parameter_list|)
block|{
return|return
operator|(
name|Set
argument_list|<
name|V
argument_list|>
operator|)
name|super
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * {@inheritDoc}    *    *<p>Because a {@code SetMultimap} has unique values for a given key, this method returns a    * {@link Set}, instead of the {@link Collection} specified in the {@link Multimap} interface.    */
end_comment

begin_function
annotation|@
name|Override
DECL|method|entries ()
specifier|public
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
parameter_list|()
block|{
return|return
operator|(
name|Set
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
operator|)
name|super
operator|.
name|entries
argument_list|()
return|;
block|}
end_function

begin_comment
comment|/**    * {@inheritDoc}    *    *<p>Because a {@code SetMultimap} has unique values for a given key, this method returns a    * {@link Set}, instead of the {@link Collection} specified in the {@link Multimap} interface.    */
end_comment

begin_function
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|removeAll (@heckForNull Object key)
specifier|public
name|Set
argument_list|<
name|V
argument_list|>
name|removeAll
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|key
parameter_list|)
block|{
return|return
operator|(
name|Set
argument_list|<
name|V
argument_list|>
operator|)
name|super
operator|.
name|removeAll
argument_list|(
name|key
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * {@inheritDoc}    *    *<p>Because a {@code SetMultimap} has unique values for a given key, this method returns a    * {@link Set}, instead of the {@link Collection} specified in the {@link Multimap} interface.    *    *<p>Any duplicates in {@code values} will be stored in the multimap once.    */
end_comment

begin_function
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|replaceValues (@arametricNullness K key, Iterable<? extends V> values)
specifier|public
name|Set
argument_list|<
name|V
argument_list|>
name|replaceValues
parameter_list|(
annotation|@
name|ParametricNullness
name|K
name|key
parameter_list|,
name|Iterable
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|values
parameter_list|)
block|{
return|return
operator|(
name|Set
argument_list|<
name|V
argument_list|>
operator|)
name|super
operator|.
name|replaceValues
argument_list|(
name|key
argument_list|,
name|values
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * {@inheritDoc}    *    *<p>Though the method signature doesn't say so explicitly, the returned map has {@link Set}    * values.    */
end_comment

begin_function
annotation|@
name|Override
DECL|method|asMap ()
specifier|public
name|Map
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|asMap
parameter_list|()
block|{
return|return
name|super
operator|.
name|asMap
argument_list|()
return|;
block|}
end_function

begin_comment
comment|/**    * Stores a key-value pair in the multimap.    *    * @param key key to store in the multimap    * @param value value to store in the multimap    * @return {@code true} if the method increased the size of the multimap, or {@code false} if the    *     multimap already contained the key-value pair    */
end_comment

begin_function
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|put (@arametricNullness K key, @ParametricNullness V value)
specifier|public
name|boolean
name|put
parameter_list|(
annotation|@
name|ParametricNullness
name|K
name|key
parameter_list|,
annotation|@
name|ParametricNullness
name|V
name|value
parameter_list|)
block|{
return|return
name|super
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Compares the specified object to this multimap for equality.    *    *<p>Two {@code SetMultimap} instances are equal if, for each key, they contain the same values.    * Equality does not depend on the ordering of keys or values.    */
end_comment

begin_function
annotation|@
name|Override
DECL|method|equals (@heckForNull Object object)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|object
parameter_list|)
block|{
return|return
name|super
operator|.
name|equals
argument_list|(
name|object
argument_list|)
return|;
block|}
end_function

begin_decl_stmt
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|7431625294878419160L
decl_stmt|;
end_decl_stmt

unit|}
end_unit

