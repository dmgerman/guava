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
name|Nullable
import|;
end_import

begin_comment
comment|/**  * A bimap (or "bidirectional map") is a map that preserves the uniqueness of  * its values as well as that of its keys. This constraint enables bimaps to  * support an "inverse view", which is another bimap containing the same entries  * as this bimap but with reversed keys and values.  *  *<p>See the Guava User Guide article on<a href=  * "https://github.com/google/guava/wiki/NewCollectionTypesExplained#bimap">  * {@code BiMap}</a>.  *  * @author Kevin Bourrillion  * @since 2.0  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
DECL|interface|BiMap
specifier|public
interface|interface
name|BiMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|// Modification Operations
comment|/**    * {@inheritDoc}    *    * @throws IllegalArgumentException if the given value is already bound to a    *     different key in this bimap. The bimap will remain unmodified in this    *     event. To avoid this exception, call {@link #forcePut} instead.    */
annotation|@
name|Override
annotation|@
name|Nullable
DECL|method|put (@ullable K key, @Nullable V value)
name|V
name|put
parameter_list|(
annotation|@
name|Nullable
name|K
name|key
parameter_list|,
annotation|@
name|Nullable
name|V
name|value
parameter_list|)
function_decl|;
comment|/**    * An alternate form of {@code put} that silently removes any existing entry    * with the value {@code value} before proceeding with the {@link #put}    * operation. If the bimap previously contained the provided key-value    * mapping, this method has no effect.    *    *<p>Note that a successful call to this method could cause the size of the    * bimap to increase by one, stay the same, or even decrease by one.    *    *<p><b>Warning:</b> If an existing entry with this value is removed, the key    * for that entry is discarded and not returned.    *    * @param key the key with which the specified value is to be associated    * @param value the value to be associated with the specified key    * @return the value which was previously associated with the key, which may    *     be {@code null}, or {@code null} if there was no previous entry    */
annotation|@
name|Nullable
DECL|method|forcePut (@ullable K key, @Nullable V value)
name|V
name|forcePut
parameter_list|(
annotation|@
name|Nullable
name|K
name|key
parameter_list|,
annotation|@
name|Nullable
name|V
name|value
parameter_list|)
function_decl|;
comment|// Bulk Operations
comment|/**    * {@inheritDoc}    *    *<p><b>Warning:</b> the results of calling this method may vary depending on    * the iteration order of {@code map}.    *    * @throws IllegalArgumentException if an attempt to {@code put} any    *     entry fails. Note that some map entries may have been added to the    *     bimap before the exception was thrown.    */
annotation|@
name|Override
DECL|method|putAll (Map<? extends K, ? extends V> map)
name|void
name|putAll
parameter_list|(
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
parameter_list|)
function_decl|;
comment|// Views
comment|/**    * {@inheritDoc}    *    *<p>Because a bimap has unique values, this method returns a {@link Set},    * instead of the {@link java.util.Collection} specified in the {@link Map}    * interface.    */
annotation|@
name|Override
DECL|method|values ()
name|Set
argument_list|<
name|V
argument_list|>
name|values
parameter_list|()
function_decl|;
comment|/**    * Returns the inverse view of this bimap, which maps each of this bimap's    * values to its associated key. The two bimaps are backed by the same data;    * any changes to one will appear in the other.    *    *<p><b>Note:</b>There is no guaranteed correspondence between the iteration    * order of a bimap and that of its inverse.    *    * @return the inverse view of this bimap    */
DECL|method|inverse ()
name|BiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inverse
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

