begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * A transforming wrapper around an ImmutableList. For internal use only. {@link  * #transform(Object)} must be functional.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
comment|// uses writeReplace(), not default serialization
DECL|class|TransformedImmutableList
specifier|abstract
class|class
name|TransformedImmutableList
parameter_list|<
name|D
parameter_list|,
name|E
parameter_list|>
extends|extends
name|ImmutableList
argument_list|<
name|E
argument_list|>
block|{
DECL|class|TransformedView
specifier|private
class|class
name|TransformedView
extends|extends
name|TransformedImmutableList
argument_list|<
name|D
argument_list|,
name|E
argument_list|>
block|{
DECL|method|TransformedView (ImmutableList<D> backingList)
name|TransformedView
parameter_list|(
name|ImmutableList
argument_list|<
name|D
argument_list|>
name|backingList
parameter_list|)
block|{
name|super
argument_list|(
name|backingList
argument_list|)
expr_stmt|;
block|}
DECL|method|transform (D d)
annotation|@
name|Override
name|E
name|transform
parameter_list|(
name|D
name|d
parameter_list|)
block|{
return|return
name|TransformedImmutableList
operator|.
name|this
operator|.
name|transform
argument_list|(
name|d
argument_list|)
return|;
block|}
block|}
DECL|field|backingList
specifier|private
specifier|transient
specifier|final
name|ImmutableList
argument_list|<
name|D
argument_list|>
name|backingList
decl_stmt|;
DECL|method|TransformedImmutableList (ImmutableList<D> backingList)
name|TransformedImmutableList
parameter_list|(
name|ImmutableList
argument_list|<
name|D
argument_list|>
name|backingList
parameter_list|)
block|{
name|this
operator|.
name|backingList
operator|=
name|checkNotNull
argument_list|(
name|backingList
argument_list|)
expr_stmt|;
block|}
DECL|method|transform (D d)
specifier|abstract
name|E
name|transform
parameter_list|(
name|D
name|d
parameter_list|)
function_decl|;
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
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|equals
argument_list|(
name|object
argument_list|)
condition|)
block|{
return|return
name|i
return|;
block|}
block|}
return|return
operator|-
literal|1
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
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
for|for
control|(
name|int
name|i
init|=
name|size
argument_list|()
operator|-
literal|1
init|;
name|i
operator|>=
literal|0
condition|;
name|i
operator|--
control|)
block|{
if|if
condition|(
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|equals
argument_list|(
name|object
argument_list|)
condition|)
block|{
return|return
name|i
return|;
block|}
block|}
return|return
operator|-
literal|1
return|;
block|}
DECL|method|get (int index)
annotation|@
name|Override
specifier|public
name|E
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|transform
argument_list|(
name|backingList
operator|.
name|get
argument_list|(
name|index
argument_list|)
argument_list|)
return|;
block|}
DECL|method|listIterator (int index)
annotation|@
name|Override
specifier|public
name|UnmodifiableListIterator
argument_list|<
name|E
argument_list|>
name|listIterator
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
operator|new
name|AbstractIndexedListIterator
argument_list|<
name|E
argument_list|>
argument_list|(
name|size
argument_list|()
argument_list|,
name|index
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|E
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|TransformedImmutableList
operator|.
name|this
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|method|size ()
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|backingList
operator|.
name|size
argument_list|()
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
return|return
operator|new
name|TransformedView
argument_list|(
name|backingList
operator|.
name|subList
argument_list|(
name|fromIndex
argument_list|,
name|toIndex
argument_list|)
argument_list|)
return|;
block|}
DECL|method|equals (Object obj)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
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
name|obj
operator|instanceof
name|List
condition|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
return|return
name|size
argument_list|()
operator|==
name|list
operator|.
name|size
argument_list|()
operator|&&
name|Iterators
operator|.
name|elementsEqual
argument_list|(
name|iterator
argument_list|()
argument_list|,
name|list
operator|.
name|iterator
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
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|hashCode
init|=
literal|1
decl_stmt|;
for|for
control|(
name|E
name|e
range|:
name|this
control|)
block|{
name|hashCode
operator|=
literal|31
operator|*
name|hashCode
operator|+
operator|(
name|e
operator|==
literal|null
condition|?
literal|0
else|:
name|e
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
block|}
return|return
name|hashCode
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
name|ObjectArrays
operator|.
name|toArrayImpl
argument_list|(
name|this
argument_list|)
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
return|return
name|ObjectArrays
operator|.
name|toArrayImpl
argument_list|(
name|this
argument_list|,
name|array
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
literal|true
return|;
block|}
block|}
end_class

end_unit

