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
name|List
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
comment|/**  * Implementation of {@link ImmutableList} with exactly one element.  *  * @author Hayward Chan  */
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
DECL|class|SingletonImmutableList
specifier|final
class|class
name|SingletonImmutableList
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableList
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
DECL|method|SingletonImmutableList (E element)
name|SingletonImmutableList
parameter_list|(
name|E
name|element
parameter_list|)
block|{
name|this
operator|.
name|element
operator|=
name|checkNotNull
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get (int index)
specifier|public
name|E
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkElementIndex
argument_list|(
name|index
argument_list|,
literal|1
argument_list|)
expr_stmt|;
return|return
name|element
return|;
block|}
DECL|method|indexOf (@ullable Object object)
annotation|@
name|Override
specifier|public
name|int
name|indexOf
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
return|return
name|element
operator|.
name|equals
argument_list|(
name|object
argument_list|)
condition|?
literal|0
else|:
operator|-
literal|1
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
DECL|method|lastIndexOf (@ullable Object object)
annotation|@
name|Override
specifier|public
name|int
name|lastIndexOf
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
return|return
name|indexOf
argument_list|(
name|object
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
literal|1
return|;
block|}
DECL|method|subList (int fromIndex, int toIndex)
annotation|@
name|Override
specifier|public
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|subList
parameter_list|(
name|int
name|fromIndex
parameter_list|,
name|int
name|toIndex
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkPositionIndexes
argument_list|(
name|fromIndex
argument_list|,
name|toIndex
argument_list|,
literal|1
argument_list|)
expr_stmt|;
return|return
operator|(
name|fromIndex
operator|==
name|toIndex
operator|)
condition|?
name|ImmutableList
operator|.
expr|<
name|E
operator|>
name|of
argument_list|()
else|:
name|this
return|;
block|}
DECL|method|reverse ()
annotation|@
name|Override
specifier|public
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|reverse
parameter_list|()
block|{
return|return
name|this
return|;
block|}
DECL|method|contains (@ullable Object object)
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
return|return
name|element
operator|.
name|equals
argument_list|(
name|object
argument_list|)
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
name|List
condition|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|that
init|=
operator|(
name|List
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
name|get
argument_list|(
literal|0
argument_list|)
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
name|int
name|hashCode
parameter_list|()
block|{
comment|// not caching hash code since it could change if the element is mutable
comment|// in a way that modifies its hash code.
return|return
literal|31
operator|+
name|element
operator|.
name|hashCode
argument_list|()
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
block|}
end_class

end_unit

