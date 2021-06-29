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
comment|/**  * An iterator which forwards all its method calls to another iterator. Subclasses should override  * one or more methods to modify the behavior of the backing iterator as desired per the<a  * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  *<p><b>{@code default} method warning:</b> This class forwards calls to<i>only some</i> {@code  * default} methods. Specifically, it forwards calls only for methods that existed<a  * href="https://docs.oracle.com/javase/7/docs/api/java/util/Iterator.html">before {@code default}  * methods were introduced</a>. For newer methods, like {@code forEachRemaining}, it inherits their  * default implementations. When those implementations invoke methods, they invoke methods on the  * {@code ForwardingIterator}.  *  * @author Kevin Bourrillion  * @since 2.0  */
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
DECL|class|ForwardingIterator
specifier|public
specifier|abstract
name|class
name|ForwardingIterator
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|ForwardingObject
expr|implements
name|Iterator
argument_list|<
name|T
argument_list|>
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|ForwardingIterator ()
specifier|protected
name|ForwardingIterator
argument_list|()
block|{}
expr|@
name|Override
DECL|method|delegate ()
specifier|protected
specifier|abstract
name|Iterator
argument_list|<
name|T
argument_list|>
name|delegate
argument_list|()
block|;    @
name|Override
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
argument_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|hasNext
argument_list|()
return|;
block|}
expr|@
name|CanIgnoreReturnValue
expr|@
name|Override
expr|@
name|ParametricNullness
DECL|method|next ()
specifier|public
name|T
name|next
argument_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
end_expr_stmt

begin_function
annotation|@
name|Override
DECL|method|remove ()
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|delegate
argument_list|()
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
end_function

unit|}
end_unit

