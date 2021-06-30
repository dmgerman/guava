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
comment|/**  * A bimap (or "bidirectional map") is a map that preserves the uniqueness of its values as well as  * that of its keys. This constraint enables bimaps to support an "inverse view", which is another  * bimap containing the same entries as this bimap but with reversed keys and values.  *  *<p>See the Guava User Guide article on<a href=  * "https://github.com/google/guava/wiki/NewCollectionTypesExplained#bimap">{@code BiMap}</a>.  *  * @author Kevin Bourrillion  * @since 2.0  */
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
DECL|interface|BiMap
specifier|public
expr|interface
name|BiMap
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
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|// Modification Operations
comment|/**    * {@inheritDoc}    *    * @throws IllegalArgumentException if the given value is already bound to a different key in this    *     bimap. The bimap will remain unmodified in this event. To avoid this exception, call {@link    *     #forcePut} instead.    */
block|@
name|CanIgnoreReturnValue
expr|@
name|Override
expr|@
name|CheckForNull
DECL|method|put (@arametricNullness K key, @ParametricNullness V value)
name|V
name|put
argument_list|(
annotation|@
name|ParametricNullness
name|K
name|key
argument_list|,
annotation|@
name|ParametricNullness
name|V
name|value
argument_list|)
block|;
comment|/**    * An alternate form of {@code put} that silently removes any existing entry with the value {@code    * value} before proceeding with the {@link #put} operation. If the bimap previously contained the    * provided key-value mapping, this method has no effect.    *    *<p>Note that a successful call to this method could cause the size of the bimap to increase by    * one, stay the same, or even decrease by one.    *    *<p><b>Warning:</b> If an existing entry with this value is removed, the key for that entry is    * discarded and not returned.    *    * @param key the key with which the specified value is to be associated    * @param value the value to be associated with the specified key    * @return the value that was previously associated with the key, or {@code null} if there was no    *     previous entry. (If the bimap contains null values, then {@code forcePut}, like {@code    *     put}, returns {@code null} both if the key is absent and if it is present with a null    *     value.)    */
block|@
name|CanIgnoreReturnValue
expr|@
name|CheckForNull
DECL|method|forcePut (@arametricNullness K key, @ParametricNullness V value)
name|V
name|forcePut
argument_list|(
annotation|@
name|ParametricNullness
name|K
name|key
argument_list|,
annotation|@
name|ParametricNullness
name|V
name|value
argument_list|)
block|;
comment|// Bulk Operations
comment|/**    * {@inheritDoc}    *    *<p><b>Warning:</b> the results of calling this method may vary depending on the iteration order    * of {@code map}.    *    * @throws IllegalArgumentException if an attempt to {@code put} any entry fails. Note that some    *     map entries may have been added to the bimap before the exception was thrown.    */
block|@
name|Override
DECL|method|putAll (Map<? extends K, ? extends V> map)
name|void
name|putAll
argument_list|(
name|Map
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|map
argument_list|)
block|;
comment|// Views
comment|/**    * {@inheritDoc}    *    *<p>Because a bimap has unique values, this method returns a {@link Set}, instead of the {@link    * java.util.Collection} specified in the {@link Map} interface.    */
block|@
name|Override
DECL|method|values ()
name|Set
argument_list|<
name|V
argument_list|>
name|values
argument_list|()
block|;
comment|/**    * Returns the inverse view of this bimap, which maps each of this bimap's values to its    * associated key. The two bimaps are backed by the same data; any changes to one will appear in    * the other.    *    *<p><b>Note:</b>There is no guaranteed correspondence between the iteration order of a bimap and    * that of its inverse.    *    * @return the inverse view of this bimap    */
DECL|method|inverse ()
name|BiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inverse
argument_list|()
block|; }
end_expr_stmt

end_unit

