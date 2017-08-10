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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|VisibleForTesting
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Spliterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Spliterators
import|;
end_import

begin_comment
comment|/**  * Implementation of {@link ImmutableList} backed by a simple array.  *  * @author Kevin Bourrillion  */
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
DECL|class|RegularImmutableList
class|class
name|RegularImmutableList
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableList
argument_list|<
name|E
argument_list|>
block|{
DECL|field|EMPTY
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|Object
argument_list|>
name|EMPTY
init|=
operator|new
name|RegularImmutableList
argument_list|<
name|Object
argument_list|>
argument_list|(
operator|new
name|Object
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
annotation|@
name|VisibleForTesting
DECL|field|array
specifier|final
specifier|transient
name|Object
index|[]
name|array
decl_stmt|;
DECL|method|RegularImmutableList (Object[] array)
name|RegularImmutableList
parameter_list|(
name|Object
index|[]
name|array
parameter_list|)
block|{
name|this
operator|.
name|array
operator|=
name|array
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
name|array
operator|.
name|length
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
DECL|method|copyIntoArray (Object[] dst, int dstOff)
name|int
name|copyIntoArray
parameter_list|(
name|Object
index|[]
name|dst
parameter_list|,
name|int
name|dstOff
parameter_list|)
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|array
argument_list|,
literal|0
argument_list|,
name|dst
argument_list|,
name|dstOff
argument_list|,
name|array
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|dstOff
operator|+
name|array
operator|.
name|length
return|;
block|}
comment|// The fake cast to E is safe because the creation methods only allow E's
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|get (int index)
specifier|public
name|E
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
operator|(
name|E
operator|)
name|array
index|[
name|index
index|]
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|listIterator (int index)
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
comment|// for performance
comment|// The fake cast to E is safe because the creation methods only allow E's
return|return
operator|(
name|UnmodifiableListIterator
argument_list|<
name|E
argument_list|>
operator|)
name|Iterators
operator|.
name|forArray
argument_list|(
name|array
argument_list|,
literal|0
argument_list|,
name|array
operator|.
name|length
argument_list|,
name|index
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|spliterator ()
specifier|public
name|Spliterator
argument_list|<
name|E
argument_list|>
name|spliterator
parameter_list|()
block|{
return|return
name|Spliterators
operator|.
name|spliterator
argument_list|(
name|array
argument_list|,
name|SPLITERATOR_CHARACTERISTICS
argument_list|)
return|;
block|}
comment|// TODO(lowasser): benchmark optimizations for equals() and see if they're worthwhile
block|}
end_class

end_unit

