begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.cache
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|cache
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
name|cache
operator|.
name|LocalCache
operator|.
name|ValueReference
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
name|compatqual
operator|.
name|NullableDecl
import|;
end_import

begin_comment
comment|/**  * An entry in a reference map.  *  *<p>Entries in the map can be in the following states:  *  *<p>Valid:  *  *<ul>  *<li>Live: valid key/value are set  *<li>Loading: loading is pending  *</ul>  *  *<p>Invalid:  *  *<ul>  *<li>Expired: time expired (key/value may still be set)  *<li>Collected: key/value was partially collected, but not yet cleaned up  *<li>Unset: marked as unset, awaiting cleanup or reuse  *</ul>  */
end_comment

begin_interface
annotation|@
name|GwtIncompatible
DECL|interface|ReferenceEntry
interface|interface
name|ReferenceEntry
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
block|{
comment|/** Returns the value reference from this entry. */
DECL|method|getValueReference ()
name|ValueReference
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getValueReference
parameter_list|()
function_decl|;
comment|/** Sets the value reference for this entry. */
DECL|method|setValueReference (ValueReference<K, V> valueReference)
name|void
name|setValueReference
parameter_list|(
name|ValueReference
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|valueReference
parameter_list|)
function_decl|;
comment|/** Returns the next entry in the chain. */
annotation|@
name|NullableDecl
DECL|method|getNext ()
name|ReferenceEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getNext
parameter_list|()
function_decl|;
comment|/** Returns the entry's hash. */
DECL|method|getHash ()
name|int
name|getHash
parameter_list|()
function_decl|;
comment|/** Returns the key for this entry. */
annotation|@
name|NullableDecl
DECL|method|getKey ()
name|K
name|getKey
parameter_list|()
function_decl|;
comment|/*    * Used by entries that use access order. Access entries are maintained in a doubly-linked list.    * New entries are added at the tail of the list at write time; stale entries are expired from    * the head of the list.    */
comment|/** Returns the time that this entry was last accessed, in ns. */
DECL|method|getAccessTime ()
name|long
name|getAccessTime
parameter_list|()
function_decl|;
comment|/** Sets the entry access time in ns. */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"GoodTime"
argument_list|)
comment|// b/122668874
DECL|method|setAccessTime (long time)
name|void
name|setAccessTime
parameter_list|(
name|long
name|time
parameter_list|)
function_decl|;
comment|/** Returns the next entry in the access queue. */
DECL|method|getNextInAccessQueue ()
name|ReferenceEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getNextInAccessQueue
parameter_list|()
function_decl|;
comment|/** Sets the next entry in the access queue. */
DECL|method|setNextInAccessQueue (ReferenceEntry<K, V> next)
name|void
name|setNextInAccessQueue
parameter_list|(
name|ReferenceEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|next
parameter_list|)
function_decl|;
comment|/** Returns the previous entry in the access queue. */
DECL|method|getPreviousInAccessQueue ()
name|ReferenceEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getPreviousInAccessQueue
parameter_list|()
function_decl|;
comment|/** Sets the previous entry in the access queue. */
DECL|method|setPreviousInAccessQueue (ReferenceEntry<K, V> previous)
name|void
name|setPreviousInAccessQueue
parameter_list|(
name|ReferenceEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|previous
parameter_list|)
function_decl|;
comment|/*    * Implemented by entries that use write order. Write entries are maintained in a doubly-linked    * list. New entries are added at the tail of the list at write time and stale entries are    * expired from the head of the list.    */
comment|/** Returns the time that this entry was last written, in ns. */
DECL|method|getWriteTime ()
name|long
name|getWriteTime
parameter_list|()
function_decl|;
comment|/** Sets the entry write time in ns. */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"GoodTime"
argument_list|)
comment|// b/122668874
DECL|method|setWriteTime (long time)
name|void
name|setWriteTime
parameter_list|(
name|long
name|time
parameter_list|)
function_decl|;
comment|/** Returns the next entry in the write queue. */
DECL|method|getNextInWriteQueue ()
name|ReferenceEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getNextInWriteQueue
parameter_list|()
function_decl|;
comment|/** Sets the next entry in the write queue. */
DECL|method|setNextInWriteQueue (ReferenceEntry<K, V> next)
name|void
name|setNextInWriteQueue
parameter_list|(
name|ReferenceEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|next
parameter_list|)
function_decl|;
comment|/** Returns the previous entry in the write queue. */
DECL|method|getPreviousInWriteQueue ()
name|ReferenceEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getPreviousInWriteQueue
parameter_list|()
function_decl|;
comment|/** Sets the previous entry in the write queue. */
DECL|method|setPreviousInWriteQueue (ReferenceEntry<K, V> previous)
name|void
name|setPreviousInWriteQueue
parameter_list|(
name|ReferenceEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|previous
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

