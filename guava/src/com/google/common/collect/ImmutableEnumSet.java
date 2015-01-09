begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|io
operator|.
name|Serializable
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
name|EnumSet
import|;
end_import

begin_comment
comment|/**  * Implementation of {@link ImmutableSet} backed by a non-empty {@link  * java.util.EnumSet}.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|,
name|emulated
operator|=
literal|true
argument_list|)
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
comment|// we're overriding default serialization
DECL|class|ImmutableEnumSet
specifier|final
class|class
name|ImmutableEnumSet
parameter_list|<
name|E
extends|extends
name|Enum
parameter_list|<
name|E
parameter_list|>
parameter_list|>
extends|extends
name|ImmutableSet
argument_list|<
name|E
argument_list|>
block|{
DECL|method|asImmutable (EnumSet<E> set)
specifier|static
parameter_list|<
name|E
extends|extends
name|Enum
argument_list|<
name|E
argument_list|>
parameter_list|>
name|ImmutableSet
argument_list|<
name|E
argument_list|>
name|asImmutable
parameter_list|(
name|EnumSet
argument_list|<
name|E
argument_list|>
name|set
parameter_list|)
block|{
switch|switch
condition|(
name|set
operator|.
name|size
argument_list|()
condition|)
block|{
case|case
literal|0
case|:
return|return
name|ImmutableSet
operator|.
name|of
argument_list|()
return|;
case|case
literal|1
case|:
return|return
name|ImmutableSet
operator|.
name|of
argument_list|(
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|set
argument_list|)
argument_list|)
return|;
default|default:
return|return
operator|new
name|ImmutableEnumSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|set
argument_list|)
return|;
block|}
block|}
comment|/*    * Notes on EnumSet and<E extends Enum<E>>:    *    * This class isn't an arbitrary ForwardingImmutableSet because we need to    * know that calling {@code clone()} during deserialization will return an    * object that no one else has a reference to, allowing us to guarantee    * immutability. Hence, we support only {@link EnumSet}.    */
DECL|field|delegate
specifier|private
specifier|final
specifier|transient
name|EnumSet
argument_list|<
name|E
argument_list|>
name|delegate
decl_stmt|;
DECL|method|ImmutableEnumSet (EnumSet<E> delegate)
specifier|private
name|ImmutableEnumSet
parameter_list|(
name|EnumSet
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
DECL|method|isPartialView ()
annotation|@
name|Override
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|iterator ()
annotation|@
name|Override
specifier|public
name|UnmodifiableIterator
argument_list|<
name|E
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|Iterators
operator|.
name|unmodifiableIterator
argument_list|(
name|delegate
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|contains (Object object)
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|contains
argument_list|(
name|object
argument_list|)
return|;
block|}
DECL|method|containsAll (Collection<?> collection)
annotation|@
name|Override
specifier|public
name|boolean
name|containsAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|)
block|{
if|if
condition|(
name|collection
operator|instanceof
name|ImmutableEnumSet
argument_list|<
name|?
argument_list|>
condition|)
block|{
name|collection
operator|=
operator|(
operator|(
name|ImmutableEnumSet
argument_list|<
name|?
argument_list|>
operator|)
name|collection
operator|)
operator|.
name|delegate
expr_stmt|;
block|}
return|return
name|delegate
operator|.
name|containsAll
argument_list|(
name|collection
argument_list|)
return|;
block|}
DECL|method|isEmpty ()
annotation|@
name|Override
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|equals (Object object)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|==
name|this
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|object
operator|instanceof
name|ImmutableEnumSet
condition|)
block|{
name|object
operator|=
operator|(
operator|(
name|ImmutableEnumSet
argument_list|<
name|?
argument_list|>
operator|)
name|object
operator|)
operator|.
name|delegate
expr_stmt|;
block|}
return|return
name|delegate
operator|.
name|equals
argument_list|(
name|object
argument_list|)
return|;
block|}
DECL|field|hashCode
specifier|private
specifier|transient
name|int
name|hashCode
decl_stmt|;
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
name|hashCode
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|0
operator|)
condition|?
name|hashCode
operator|=
name|delegate
operator|.
name|hashCode
argument_list|()
else|:
name|result
return|;
block|}
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|toString
argument_list|()
return|;
block|}
comment|// All callers of the constructor are restricted to<E extends Enum<E>>.
DECL|method|writeReplace ()
annotation|@
name|Override
name|Object
name|writeReplace
parameter_list|()
block|{
return|return
operator|new
name|EnumSerializedForm
argument_list|<
name|E
argument_list|>
argument_list|(
name|delegate
argument_list|)
return|;
block|}
comment|/*    * This class is used to serialize ImmutableEnumSet instances.    */
DECL|class|EnumSerializedForm
specifier|private
specifier|static
class|class
name|EnumSerializedForm
parameter_list|<
name|E
extends|extends
name|Enum
parameter_list|<
name|E
parameter_list|>
parameter_list|>
implements|implements
name|Serializable
block|{
DECL|field|delegate
specifier|final
name|EnumSet
argument_list|<
name|E
argument_list|>
name|delegate
decl_stmt|;
DECL|method|EnumSerializedForm (EnumSet<E> delegate)
name|EnumSerializedForm
parameter_list|(
name|EnumSet
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
DECL|method|readResolve ()
name|Object
name|readResolve
parameter_list|()
block|{
comment|// EJ2 #76: Write readObject() methods defensively.
return|return
operator|new
name|ImmutableEnumSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|delegate
operator|.
name|clone
argument_list|()
argument_list|)
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
block|}
end_class

end_unit

