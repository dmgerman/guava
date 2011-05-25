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
comment|// Non-volatile because:
comment|//   - Integer is immutable and thus thread-safe;
comment|//   - no problems if one thread overwrites the cachedHashCode from another.
DECL|field|cachedHashCode
specifier|private
specifier|transient
name|Integer
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
DECL|method|isEmpty ()
annotation|@
name|Override
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|contains (Object target)
annotation|@
name|Override
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
name|singletonIterator
argument_list|(
name|element
argument_list|)
return|;
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
DECL|method|toArray ()
annotation|@
name|Override
specifier|public
name|Object
index|[]
name|toArray
parameter_list|()
block|{
return|return
operator|new
name|Object
index|[]
block|{
name|element
block|}
return|;
block|}
DECL|method|toArray (T[] array)
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|toArray
parameter_list|(
name|T
index|[]
name|array
parameter_list|)
block|{
if|if
condition|(
name|array
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|array
operator|=
name|ObjectArrays
operator|.
name|newArray
argument_list|(
name|array
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|array
operator|.
name|length
operator|>
literal|1
condition|)
block|{
name|array
index|[
literal|1
index|]
operator|=
literal|null
expr_stmt|;
block|}
comment|// Writes will produce ArrayStoreException when the toArray() doc requires.
name|Object
index|[]
name|objectArray
init|=
name|array
decl_stmt|;
name|objectArray
index|[
literal|0
index|]
operator|=
name|element
expr_stmt|;
return|return
name|array
return|;
block|}
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
name|Set
condition|)
block|{
name|Set
argument_list|<
name|?
argument_list|>
name|that
init|=
operator|(
name|Set
argument_list|<
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
return|return
name|that
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|element
operator|.
name|equals
argument_list|(
name|that
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
specifier|final
name|int
name|hashCode
parameter_list|()
block|{
name|Integer
name|code
init|=
name|cachedHashCode
decl_stmt|;
if|if
condition|(
name|code
operator|==
literal|null
condition|)
block|{
return|return
name|cachedHashCode
operator|=
name|element
operator|.
name|hashCode
argument_list|()
return|;
block|}
return|return
name|code
return|;
block|}
DECL|method|isHashCodeFast ()
annotation|@
name|Override
name|boolean
name|isHashCodeFast
parameter_list|()
block|{
return|return
literal|false
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
name|String
name|elementToString
init|=
name|element
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
operator|new
name|StringBuilder
argument_list|(
name|elementToString
operator|.
name|length
argument_list|()
operator|+
literal|2
argument_list|)
operator|.
name|append
argument_list|(
literal|'['
argument_list|)
operator|.
name|append
argument_list|(
name|elementToString
argument_list|)
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

