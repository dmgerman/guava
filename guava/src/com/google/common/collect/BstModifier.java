begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the  * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  * express or implied. See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * A specification for a local change to an entry in a binary search tree.  *  * @author Louis Wasserman  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
DECL|interface|BstModifier
interface|interface
name|BstModifier
parameter_list|<
name|K
parameter_list|,
name|N
extends|extends
name|BstNode
parameter_list|<
name|K
parameter_list|,
name|N
parameter_list|>
parameter_list|>
block|{
comment|/**    * Given a target key and the original entry (if any) with the specified key, returns the entry    * with key {@code key} after this mutation has been performed. The result must either be {@code    * null} or must have a key that compares as equal to {@code key}. A deletion operation, for    * example, would always return {@code null}, or an insertion operation would always return a    * non-null {@code insertedEntry}.    *    *<p>If this method returns a non-null entry of type {@code N}, any children it has will be    * ignored.    *    *<p>This method may return {@code originalEntry} itself to indicate that no change is made.    *    * @param key The key being targeted for modification.    * @param originalEntry The original entry in the binary search tree with the specified key, if    *        any. No guarantees are made about the children of this entry when treated as a node; in    *        particular, they are not necessarily the children of the corresponding node in the    *        binary search tree.    * @return the entry (if any) with the specified key after this modification is performed    */
annotation|@
name|Nullable
DECL|method|modify (K key, @Nullable N originalEntry)
name|N
name|modify
parameter_list|(
name|K
name|key
parameter_list|,
annotation|@
name|Nullable
name|N
name|originalEntry
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

