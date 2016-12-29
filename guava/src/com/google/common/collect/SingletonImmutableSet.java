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
name|common
operator|.
name|base
operator|.
name|Preconditions
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
name|concurrent
operator|.
name|LazyInit
import|;
end_import

begin_comment
comment|/**  * Implementation of {@link ImmutableSet} with exactly one element.  *  * @author Kevin Bourrillion  * @author Nick Kralevich  */
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
comment|// uses writeReplace(), not default serialization
DECL|class|SingletonImmutableSet
specifier|final
class|class
name|SingletonImmutableSet
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableSet
argument_list|<
name|E
argument_list|>
block|{
DECL|field|element
specifier|final
specifier|transient
name|E
name|element
decl_stmt|;
comment|// This is transient because it will be recalculated on the first
comment|// call to hashCode().
comment|//
comment|// A race condition is avoided since threads will either see that the value
comment|// is zero and recalculate it themselves, or two threads will see it at
comment|// the same time, and both recalculate it.  If the cachedHashCode is 0,
comment|// it will always be recalculated, unfortunately.
annotation|@
name|LazyInit
DECL|field|cachedHashCode
specifier|private
specifier|transient
name|int
name|cachedHashCode
decl_stmt|;
DECL|method|SingletonImmutableSet (E element)
name|SingletonImmutableSet
parameter_list|(
name|E
name|element
parameter_list|)
block|{
name|this
operator|.
name|element
operator|=
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
DECL|method|SingletonImmutableSet (E element, int hashCode)
name|SingletonImmutableSet
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|hashCode
parameter_list|)
block|{
comment|// Guaranteed to be non-null by the presence of the pre-computed hash code.
name|this
operator|.
name|element
operator|=
name|element
expr_stmt|;
name|cachedHashCode
operator|=
name|hashCode
expr_stmt|;
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
literal|1
return|;
block|}
annotation|@
name|Override
DECL|method|contains (Object target)
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|target
parameter_list|)
block|{
return|return
name|element
operator|.
name|equals
argument_list|(
name|target
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
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
name|singletonIterator
argument_list|(
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createAsList ()
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|createAsList
parameter_list|()
block|{
return|return
name|ImmutableList
operator|.
name|of
argument_list|(
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isPartialView ()
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|copyIntoArray (Object[] dst, int offset)
name|int
name|copyIntoArray
parameter_list|(
name|Object
index|[]
name|dst
parameter_list|,
name|int
name|offset
parameter_list|)
block|{
name|dst
index|[
name|offset
index|]
operator|=
name|element
expr_stmt|;
return|return
name|offset
operator|+
literal|1
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
specifier|final
name|int
name|hashCode
parameter_list|()
block|{
comment|// Racy single-check.
name|int
name|code
init|=
name|cachedHashCode
decl_stmt|;
if|if
condition|(
name|code
operator|==
literal|0
condition|)
block|{
name|cachedHashCode
operator|=
name|code
operator|=
name|element
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
return|return
name|code
return|;
block|}
annotation|@
name|Override
DECL|method|isHashCodeFast ()
name|boolean
name|isHashCodeFast
parameter_list|()
block|{
return|return
name|cachedHashCode
operator|!=
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|'['
operator|+
name|element
operator|.
name|toString
argument_list|()
operator|+
literal|']'
return|;
block|}
block|}
end_class

end_unit

