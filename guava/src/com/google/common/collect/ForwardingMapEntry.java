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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Objects
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
name|Map
operator|.
name|Entry
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
comment|/**  * A map entry which forwards all its method calls to another map entry.  * Subclasses should override one or more methods to modify the behavior of the  * backing map entry as desired per the<a  * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  *<p><i>Warning:</i> The methods of {@code ForwardingMapEntry} forward  *<i>indiscriminately</i> to the methods of the delegate. For example,  * overriding {@link #getValue} alone<i>will not</i> change the behavior of  * {@link #equals}, which can lead to unexpected behavior. In this case, you  * should override {@code equals} as well, either providing your own  * implementation, or delegating to the provided {@code standardEquals} method.  *  *<p>Each of the {@code standard} methods, where appropriate, use {@link  * Objects#equal} to test equality for both keys and values. This may not be  * the desired behavior for map implementations that use non-standard notions of  * key equality, such as the entry of a {@code SortedMap} whose comparator is  * not consistent with {@code equals}.  *  *<p>The {@code standard} methods are not guaranteed to be thread-safe, even  * when all of the methods that they depend on are thread-safe.  *  * @author Mike Bostock  * @author Louis Wasserman  * @since 2.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ForwardingMapEntry
specifier|public
specifier|abstract
class|class
name|ForwardingMapEntry
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ForwardingObject
implements|implements
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|// TODO(user): identify places where thread safety is actually lost
comment|/** Constructor for use by subclasses. */
DECL|method|ForwardingMapEntry ()
specifier|protected
name|ForwardingMapEntry
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
specifier|abstract
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|getKey ()
specifier|public
name|K
name|getKey
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|getKey
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getValue ()
specifier|public
name|V
name|getValue
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|getValue
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setValue (V value)
specifier|public
name|V
name|setValue
parameter_list|(
name|V
name|value
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@ullable Object object)
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
name|delegate
argument_list|()
operator|.
name|equals
argument_list|(
name|object
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
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
comment|/**    * A sensible definition of {@link #equals(Object)} in terms of {@link    * #getKey()} and {@link #getValue()}. If you override either of these    * methods, you may wish to override {@link #equals(Object)} to forward to    * this implementation.    *    * @since 7.0    */
DECL|method|standardEquals (@ullable Object object)
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
if|if
condition|(
name|object
operator|instanceof
name|Entry
condition|)
block|{
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|that
init|=
operator|(
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
return|return
name|Objects
operator|.
name|equal
argument_list|(
name|this
operator|.
name|getKey
argument_list|()
argument_list|,
name|that
operator|.
name|getKey
argument_list|()
argument_list|)
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|this
operator|.
name|getValue
argument_list|()
argument_list|,
name|that
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**    * A sensible definition of {@link #hashCode()} in terms of {@link #getKey()}    * and {@link #getValue()}. If you override either of these methods, you may    * wish to override {@link #hashCode()} to forward to this implementation.    *    * @since 7.0    */
DECL|method|standardHashCode ()
specifier|protected
name|int
name|standardHashCode
parameter_list|()
block|{
name|K
name|k
init|=
name|getKey
argument_list|()
decl_stmt|;
name|V
name|v
init|=
name|getValue
argument_list|()
decl_stmt|;
return|return
operator|(
operator|(
name|k
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|k
operator|.
name|hashCode
argument_list|()
operator|)
operator|^
operator|(
operator|(
name|v
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|v
operator|.
name|hashCode
argument_list|()
operator|)
return|;
block|}
comment|/**    * A sensible definition of {@link #toString} in terms of {@link    * #getKey} and {@link #getValue}. If you override either of these    * methods, you may wish to override {@link #equals} to forward to this    * implementation.    *    * @since 7.0    */
annotation|@
name|Beta
DECL|method|standardToString ()
specifier|protected
name|String
name|standardToString
parameter_list|()
block|{
return|return
name|getKey
argument_list|()
operator|+
literal|"="
operator|+
name|getValue
argument_list|()
return|;
block|}
block|}
end_class

end_unit

