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
comment|/**  * A {@code Multimap} that cannot hold duplicate key-value pairs. Adding a key-value pair that's  * already in the multimap has no effect. See the {@link Multimap} documentation for information  * common to all multimaps.  *  *<p>The {@link #get}, {@link #removeAll}, and {@link #replaceValues} methods each return a {@link  * Set} of values, while {@link #entries} returns a {@code Set} of map entries. Though the method  * signature doesn't say so explicitly, the map returned by {@link #asMap} has {@code Set} values.  *  *<p>If the values corresponding to a single key should be ordered according to a {@link  * java.util.Comparator} (or the natural order), see the {@link SortedSetMultimap} subinterface.  *  *<p>Since the value collections are sets, the behavior of a {@code SetMultimap} is not specified  * if key<em>or value</em> objects already present in the multimap change in a manner that affects  * {@code equals} comparisons. Use caution if mutable objects are used as keys or values in a {@code  * SetMultimap}.  *  *<p><b>Warning:</b> Do not modify either a key<i>or a value</i> of a {@code SetMultimap} in a way  * that affects its {@link Object#equals} behavior. Undefined behavior and bugs will result.  *  *<p>See the Guava User Guide article on<a href=  * "https://github.com/google/guava/wiki/NewCollectionTypesExplained#multimap"> {@code  * Multimap}</a>.  *  * @author Jared Levy  * @since 2.0  */
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
DECL|interface|SetMultimap
specifier|public
expr|interface
name|SetMultimap
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
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|/**    * {@inheritDoc}    *    *<p>Because a {@code SetMultimap} has unique values for a given key, this method returns a    * {@link Set}, instead of the {@link java.util.Collection} specified in the {@link Multimap}    * interface.    */
block|@
name|Override
DECL|method|get (@arametricNullness K key)
name|Set
argument_list|<
name|V
argument_list|>
name|get
argument_list|(
annotation|@
name|ParametricNullness
name|K
name|key
argument_list|)
block|;
comment|/**    * {@inheritDoc}    *    *<p>Because a {@code SetMultimap} has unique values for a given key, this method returns a    * {@link Set}, instead of the {@link java.util.Collection} specified in the {@link Multimap}    * interface.    */
block|@
name|CanIgnoreReturnValue
expr|@
name|Override
DECL|method|removeAll (@heckForNull Object key)
name|Set
argument_list|<
name|V
argument_list|>
name|removeAll
argument_list|(
annotation|@
name|CheckForNull
name|Object
name|key
argument_list|)
block|;
comment|/**    * {@inheritDoc}    *    *<p>Because a {@code SetMultimap} has unique values for a given key, this method returns a    * {@link Set}, instead of the {@link java.util.Collection} specified in the {@link Multimap}    * interface.    *    *<p>Any duplicates in {@code values} will be stored in the multimap once.    */
block|@
name|CanIgnoreReturnValue
expr|@
name|Override
DECL|method|replaceValues (@arametricNullness K key, Iterable<? extends V> values)
name|Set
argument_list|<
name|V
argument_list|>
name|replaceValues
argument_list|(
annotation|@
name|ParametricNullness
name|K
name|key
argument_list|,
name|Iterable
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|values
argument_list|)
block|;
comment|/**    * {@inheritDoc}    *    *<p>Because a {@code SetMultimap} has unique values for a given key, this method returns a    * {@link Set}, instead of the {@link java.util.Collection} specified in the {@link Multimap}    * interface.    */
block|@
name|Override
DECL|method|entries ()
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
argument_list|()
block|;
comment|/**    * {@inheritDoc}    *    *<p><b>Note:</b> The returned map's values are guaranteed to be of type {@link Set}. To obtain    * this map with the more specific generic type {@code Map<K, Set<V>>}, call {@link    * Multimaps#asMap(SetMultimap)} instead.    */
block|@
name|Override
DECL|method|asMap ()
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
argument_list|()
block|;
comment|/**    * Compares the specified object to this multimap for equality.    *    *<p>Two {@code SetMultimap} instances are equal if, for each key, they contain the same values.    * Equality does not depend on the ordering of keys or values.    *    *<p>An empty {@code SetMultimap} is equal to any other empty {@code Multimap}, including an    * empty {@code ListMultimap}.    */
block|@
name|Override
DECL|method|equals (@heckForNull Object obj)
name|boolean
name|equals
argument_list|(
annotation|@
name|CheckForNull
name|Object
name|obj
argument_list|)
block|; }
end_expr_stmt

end_unit

