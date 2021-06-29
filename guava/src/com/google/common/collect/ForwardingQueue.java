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
name|NoSuchElementException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Queue
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
comment|/**  * A queue which forwards all its method calls to another queue. Subclasses should override one or  * more methods to modify the behavior of the backing queue as desired per the<a  * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  *<p><b>Warning:</b> The methods of {@code ForwardingQueue} forward<b>indiscriminately</b> to the  * methods of the delegate. For example, overriding {@link #add} alone<b>will not</b> change the  * behavior of {@link #offer} which can lead to unexpected behavior. In this case, you should  * override {@code offer} as well, either providing your own implementation, or delegating to the  * provided {@code standardOffer} method.  *  *<p><b>{@code default} method warning:</b> This class does<i>not</i> forward calls to {@code  * default} methods. Instead, it inherits their default implementations. When those implementations  * invoke methods, they invoke methods on the {@code ForwardingQueue}.  *  *<p>The {@code standard} methods are not guaranteed to be thread-safe, even when all of the  * methods that they depend on are thread-safe.  *  * @author Mike Bostock  * @author Louis Wasserman  * @since 2.0  */
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
DECL|class|ForwardingQueue
specifier|public
specifier|abstract
name|class
name|ForwardingQueue
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|ForwardingCollection
argument_list|<
name|E
argument_list|>
expr|implements
name|Queue
argument_list|<
name|E
argument_list|>
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|ForwardingQueue ()
specifier|protected
name|ForwardingQueue
argument_list|()
block|{}
expr|@
name|Override
DECL|method|delegate ()
specifier|protected
specifier|abstract
name|Queue
argument_list|<
name|E
argument_list|>
name|delegate
argument_list|()
block|;    @
name|CanIgnoreReturnValue
comment|// TODO(cpovirk): Consider removing this?
expr|@
name|Override
DECL|method|offer (@arametricNullness E o)
specifier|public
name|boolean
name|offer
argument_list|(
annotation|@
name|ParametricNullness
name|E
name|o
argument_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|offer
argument_list|(
name|o
argument_list|)
return|;
block|}
expr|@
name|CanIgnoreReturnValue
comment|// TODO(cpovirk): Consider removing this?
expr|@
name|Override
expr|@
name|CheckForNull
DECL|method|poll ()
specifier|public
name|E
name|poll
argument_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|poll
argument_list|()
return|;
block|}
end_expr_stmt

begin_function
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
annotation|@
name|ParametricNullness
DECL|method|remove ()
specifier|public
name|E
name|remove
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|remove
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|Override
annotation|@
name|CheckForNull
DECL|method|peek ()
specifier|public
name|E
name|peek
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|peek
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|Override
annotation|@
name|ParametricNullness
DECL|method|element ()
specifier|public
name|E
name|element
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|element
argument_list|()
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #offer} in terms of {@link #add}. If you override {@link #add},    * you may wish to override {@link #offer} to forward to this implementation.    *    * @since 7.0    */
end_comment

begin_function
DECL|method|standardOffer (@arametricNullness E e)
specifier|protected
name|boolean
name|standardOffer
parameter_list|(
annotation|@
name|ParametricNullness
name|E
name|e
parameter_list|)
block|{
try|try
block|{
return|return
name|add
argument_list|(
name|e
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|caught
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #peek} in terms of {@link #element}. If you override {@link    * #element}, you may wish to override {@link #peek} to forward to this implementation.    *    * @since 7.0    */
end_comment

begin_function
annotation|@
name|CheckForNull
DECL|method|standardPeek ()
specifier|protected
name|E
name|standardPeek
parameter_list|()
block|{
try|try
block|{
return|return
name|element
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|caught
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #poll} in terms of {@link #remove}. If you override {@link    * #remove}, you may wish to override {@link #poll} to forward to this implementation.    *    * @since 7.0    */
end_comment

begin_function
annotation|@
name|CheckForNull
DECL|method|standardPoll ()
specifier|protected
name|E
name|standardPoll
parameter_list|()
block|{
try|try
block|{
return|return
name|remove
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|caught
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_function

unit|}
end_unit

