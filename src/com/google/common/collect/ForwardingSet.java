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
name|Beta
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
comment|/**  * A set which forwards all its method calls to another set. Subclasses should  * override one or more methods to modify the behavior of the backing set as  * desired per the<a  * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  *<p><b>Warning:</b> The methods of {@code ForwardingSet} forward  *<b>indiscriminately</b> to the methods of the delegate. For example,  * overriding {@link #add} alone<b>will not</b> change the behavior of {@link  * #addAll}, which can lead to unexpected behavior. In this case, you should  * override {@code addAll} as well, either providing your own implementation, or  * delegating to the provided {@code standardAddAll} method.  *  *<p>The {@code standard} methods are not guaranteed to be thread-safe, even  * when all of the methods that they depend on are thread-safe.  *  * @author Kevin Bourrillion  * @author Louis Wasserman  * @since Guava release 02 (imported from Google Collections Library)  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ForwardingSet
specifier|public
specifier|abstract
class|class
name|ForwardingSet
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ForwardingCollection
argument_list|<
name|E
argument_list|>
implements|implements
name|Set
argument_list|<
name|E
argument_list|>
block|{
comment|// TODO(user): identify places where thread safety is actually lost
comment|/** Constructor for use by subclasses. */
DECL|method|ForwardingSet ()
specifier|protected
name|ForwardingSet
parameter_list|()
block|{}
DECL|method|delegate ()
annotation|@
name|Override
specifier|protected
specifier|abstract
name|Set
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|()
function_decl|;
DECL|method|equals (@ullable Object object)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
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
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
comment|/**    * A sensible definition of {@link #equals} in terms of {@link #size} and    * {@link #containsAll}. If you override either of those methods, you may wish    * to override {@link #equals} to forward to this implementation.    *    * @since Guava release 07    */
DECL|method|standardEquals (@ullable Object object)
annotation|@
name|Beta
specifier|protected
name|boolean
name|standardEquals
parameter_list|(
annotation|@
name|Nullable
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
comment|/**    * A sensible definition of {@link #hashCode} in terms of {@link #iterator}.    * If you override {@link #iterator}, you may wish to override {@link #equals}    * to forward to this implementation.    *    * @since Guava release 07    */
DECL|method|standardHashCode ()
annotation|@
name|Beta
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
block|}
end_class

end_unit

