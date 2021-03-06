begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * An iterator that does not support {@link #remove}.  *  *<p>{@code UnmodifiableIterator} is used primarily in conjunction with implementations of {@link  * ImmutableCollection}, such as {@link ImmutableList}. You can, however, convert an existing  * iterator to an {@code UnmodifiableIterator} using {@link Iterators#unmodifiableIterator}.  *  * @author Jared Levy  * @since 2.0  */
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
DECL|class|UnmodifiableIterator
specifier|public
specifier|abstract
name|class
name|UnmodifiableIterator
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
expr|implements
name|Iterator
argument_list|<
name|E
argument_list|>
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|UnmodifiableIterator ()
specifier|protected
name|UnmodifiableIterator
argument_list|()
block|{}
comment|/**    * Guaranteed to throw an exception and leave the underlying data unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
expr|@
name|Deprecated
expr|@
name|Override
expr|@
name|DoNotCall
argument_list|(
literal|"Always throws UnsupportedOperationException"
argument_list|)
DECL|method|remove ()
specifier|public
name|final
name|void
name|remove
argument_list|()
block|{
throw|throw
argument_list|new
name|UnsupportedOperationException
argument_list|()
block|;   }
block|}
end_expr_stmt

end_unit

