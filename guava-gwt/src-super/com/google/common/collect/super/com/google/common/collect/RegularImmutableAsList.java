begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * An {@link ImmutableAsList} implementation specialized for when the delegate collection is  * already backed by an {@code ImmutableList} or array.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
comment|// uses writeReplace, not default serialization
DECL|class|RegularImmutableAsList
class|class
name|RegularImmutableAsList
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableAsList
argument_list|<
name|E
argument_list|>
block|{
DECL|field|delegate
specifier|private
specifier|final
name|ImmutableCollection
argument_list|<
name|E
argument_list|>
name|delegate
decl_stmt|;
DECL|field|delegateList
specifier|private
specifier|final
name|ImmutableList
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|delegateList
decl_stmt|;
DECL|method|RegularImmutableAsList (ImmutableCollection<E> delegate, ImmutableList<? extends E> delegateList)
name|RegularImmutableAsList
parameter_list|(
name|ImmutableCollection
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|,
name|ImmutableList
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|delegateList
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|this
operator|.
name|delegateList
operator|=
name|delegateList
expr_stmt|;
block|}
DECL|method|RegularImmutableAsList (ImmutableCollection<E> delegate, Object[] array)
name|RegularImmutableAsList
parameter_list|(
name|ImmutableCollection
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|,
name|Object
index|[]
name|array
parameter_list|)
block|{
name|this
argument_list|(
name|delegate
argument_list|,
name|ImmutableList
operator|.
expr|<
name|E
operator|>
name|asImmutableList
argument_list|(
name|array
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|delegateCollection ()
name|ImmutableCollection
argument_list|<
name|E
argument_list|>
name|delegateCollection
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
DECL|method|delegateList ()
name|ImmutableList
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|delegateList
parameter_list|()
block|{
return|return
name|delegateList
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// safe covariant cast!
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
return|return
operator|(
name|UnmodifiableListIterator
argument_list|<
name|E
argument_list|>
operator|)
name|delegateList
operator|.
name|listIterator
argument_list|(
name|index
argument_list|)
return|;
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
return|return
name|delegateList
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
block|}
end_class

end_unit

