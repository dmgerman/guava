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
name|List
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * A {@code Multimap} that can hold duplicate key-value pairs and that maintains  * the insertion ordering of values for a given key. See the {@link Multimap}  * documentation for information common to all multimaps.  *  *<p>The {@link #get}, {@link #removeAll}, and {@link #replaceValues} methods  * each return a {@link List} of values. Though the method signature doesn't say  * so explicitly, the map returned by {@link #asMap} has {@code List} values.  *  *<p>See the Guava User Guide article on<a href=  * "https://github.com/google/guava/wiki/NewCollectionTypesExplained#multimap">  * {@code Multimap}</a>.  *  * @author Jared Levy  * @since 2.0  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
DECL|interface|ListMultimap
specifier|public
interface|interface
name|ListMultimap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|/**    * {@inheritDoc}    *    *<p>Because the values for a given key may have duplicates and follow the    * insertion ordering, this method returns a {@link List}, instead of the    * {@link java.util.Collection} specified in the {@link Multimap} interface.    */
annotation|@
name|Override
DECL|method|get (@ullable K key)
name|List
argument_list|<
name|V
argument_list|>
name|get
parameter_list|(
annotation|@
name|Nullable
name|K
name|key
parameter_list|)
function_decl|;
comment|/**    * {@inheritDoc}    *    *<p>Because the values for a given key may have duplicates and follow the    * insertion ordering, this method returns a {@link List}, instead of the    * {@link java.util.Collection} specified in the {@link Multimap} interface.    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|removeAll (@ullable Object key)
name|List
argument_list|<
name|V
argument_list|>
name|removeAll
parameter_list|(
annotation|@
name|Nullable
name|Object
name|key
parameter_list|)
function_decl|;
comment|/**    * {@inheritDoc}    *    *<p>Because the values for a given key may have duplicates and follow the    * insertion ordering, this method returns a {@link List}, instead of the    * {@link java.util.Collection} specified in the {@link Multimap} interface.    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|replaceValues (K key, Iterable<? extends V> values)
name|List
argument_list|<
name|V
argument_list|>
name|replaceValues
parameter_list|(
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
function_decl|;
comment|/**    * {@inheritDoc}    *    *<p><b>Note:</b> The returned map's values are guaranteed to be of type    * {@link List}. To obtain this map with the more specific generic type    * {@code Map<K, List<V>>}, call {@link Multimaps#asMap(ListMultimap)}    * instead.    */
annotation|@
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
parameter_list|()
function_decl|;
comment|/**    * Compares the specified object to this multimap for equality.    *    *<p>Two {@code ListMultimap} instances are equal if, for each key, they    * contain the same values in the same order. If the value orderings disagree,    * the multimaps will not be considered equal.    *    *<p>An empty {@code ListMultimap} is equal to any other empty {@code    * Multimap}, including an empty {@code SetMultimap}.    */
annotation|@
name|Override
DECL|method|equals (@ullable Object obj)
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|obj
parameter_list|)
function_decl|;
block|}
end_interface

end_unit
