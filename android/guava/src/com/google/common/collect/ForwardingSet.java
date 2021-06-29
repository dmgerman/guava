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
name|Collection
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
comment|/**  * A set which forwards all its method calls to another set. Subclasses should override one or more  * methods to modify the behavior of the backing set as desired per the<a  * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  *<p><b>Warning:</b> The methods of {@code ForwardingSet} forward<b>indiscriminately</b> to the  * methods of the delegate. For example, overriding {@link #add} alone<b>will not</b> change the  * behavior of {@link #addAll}, which can lead to unexpected behavior. In this case, you should  * override {@code addAll} as well, either providing your own implementation, or delegating to the  * provided {@code standardAddAll} method.  *  *<p><b>{@code default} method warning:</b> This class does<i>not</i> forward calls to {@code  * default} methods. Instead, it inherits their default implementations. When those implementations  * invoke methods, they invoke methods on the {@code ForwardingSet}.  *  *<p>The {@code standard} methods are not guaranteed to be thread-safe, even when all of the  * methods that they depend on are thread-safe.  *  * @author Kevin Bourrillion  * @author Louis Wasserman  * @since 2.0  */
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
DECL|class|ForwardingSet
specifier|public
specifier|abstract
name|class
name|ForwardingSet
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
name|Set
argument_list|<
name|E
argument_list|>
block|{
comment|// TODO(lowasser): identify places where thread safety is actually lost
comment|/** Constructor for use by subclasses. */
DECL|method|ForwardingSet ()
specifier|protected
name|ForwardingSet
argument_list|()
block|{}
expr|@
name|Override
DECL|method|delegate ()
specifier|protected
specifier|abstract
name|Set
argument_list|<
name|E
argument_list|>
name|delegate
argument_list|()
block|;    @
name|Override
DECL|method|equals (@heckForNull Object object)
specifier|public
name|boolean
name|equals
argument_list|(
annotation|@
name|CheckForNull
name|Object
name|object
argument_list|)
block|{
return|return
name|object
operator|==
name|this
operator|||
name|delegate
argument_list|()
operator|.
name|equals
argument_list|(
name|object
argument_list|)
return|;
block|}
expr|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
argument_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
end_expr_stmt

begin_comment
comment|/**    * A sensible definition of {@link #removeAll} in terms of {@link #iterator} and {@link #remove}.    * If you override {@code iterator} or {@code remove}, you may wish to override {@link #removeAll}    * to forward to this implementation.    *    * @since 7.0 (this version overrides the {@code ForwardingCollection} version as of 12.0)    */
end_comment

begin_function
annotation|@
name|Override
DECL|method|standardRemoveAll (Collection<?> collection)
specifier|protected
name|boolean
name|standardRemoveAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|)
block|{
return|return
name|Sets
operator|.
name|removeAllImpl
argument_list|(
name|this
argument_list|,
name|checkNotNull
argument_list|(
name|collection
argument_list|)
argument_list|)
return|;
comment|// for GWT
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #equals} in terms of {@link #size} and {@link #containsAll}. If    * you override either of those methods, you may wish to override {@link #equals} to forward to    * this implementation.    *    * @since 7.0    */
end_comment

begin_function
DECL|method|standardEquals (@heckForNull Object object)
specifier|protected
name|boolean
name|standardEquals
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|object
parameter_list|)
block|{
return|return
name|Sets
operator|.
name|equalsImpl
argument_list|(
name|this
argument_list|,
name|object
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #hashCode} in terms of {@link #iterator}. If you override    * {@link #iterator}, you may wish to override {@link #equals} to forward to this implementation.    *    * @since 7.0    */
end_comment

begin_function
DECL|method|standardHashCode ()
specifier|protected
name|int
name|standardHashCode
parameter_list|()
block|{
return|return
name|Sets
operator|.
name|hashCodeImpl
argument_list|(
name|this
argument_list|)
return|;
block|}
end_function

unit|}
end_unit

