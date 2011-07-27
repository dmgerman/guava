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
name|checkState
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * A path to a node in a binary search tree, originating at the root.  *  * @author Louis Wasserman  * @param<N> The type of nodes in this binary search tree.  * @param<P> This path type, and the path type of all suffix paths.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|BSTPath
specifier|abstract
class|class
name|BSTPath
parameter_list|<
name|N
extends|extends
name|BSTNode
parameter_list|<
name|?
parameter_list|,
name|N
parameter_list|>
parameter_list|,
name|P
extends|extends
name|BSTPath
parameter_list|<
name|N
parameter_list|,
name|P
parameter_list|>
parameter_list|>
block|{
DECL|field|tip
specifier|private
specifier|final
name|N
name|tip
decl_stmt|;
annotation|@
name|Nullable
DECL|field|prefix
specifier|private
specifier|final
name|P
name|prefix
decl_stmt|;
DECL|method|BSTPath (N tip, @Nullable P prefix)
name|BSTPath
parameter_list|(
name|N
name|tip
parameter_list|,
annotation|@
name|Nullable
name|P
name|prefix
parameter_list|)
block|{
name|this
operator|.
name|tip
operator|=
name|checkNotNull
argument_list|(
name|tip
argument_list|)
expr_stmt|;
name|this
operator|.
name|prefix
operator|=
name|prefix
expr_stmt|;
block|}
comment|/**    * Return the end of this {@code BSTPath}, the deepest node in the path.    */
DECL|method|getTip ()
specifier|public
specifier|final
name|N
name|getTip
parameter_list|()
block|{
return|return
name|tip
return|;
block|}
comment|/**    * Returns {@code true} if this path has a prefix.    */
DECL|method|hasPrefix ()
specifier|public
specifier|final
name|boolean
name|hasPrefix
parameter_list|()
block|{
return|return
name|prefix
operator|!=
literal|null
return|;
block|}
comment|/**    * Returns the prefix of this path, which reaches to the parent of the end of this path. Returns    * {@code null} if this path has no prefix.    */
annotation|@
name|Nullable
DECL|method|prefixOrNull ()
specifier|public
specifier|final
name|P
name|prefixOrNull
parameter_list|()
block|{
return|return
name|prefix
return|;
block|}
comment|/**    * Returns the prefix of this path, which reaches to the parent of the end of this path.    *    * @throws IllegalStateException if this path has no prefix.    */
DECL|method|getPrefix ()
specifier|public
specifier|final
name|P
name|getPrefix
parameter_list|()
block|{
name|checkState
argument_list|(
name|hasPrefix
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|prefix
return|;
block|}
block|}
end_class

end_unit

