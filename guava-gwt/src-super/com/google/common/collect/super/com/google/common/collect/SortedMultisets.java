begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not  * use this file except in compliance with the License. You may obtain a copy of  * the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the  * License for the specific language governing permissions and limitations under  * the License.  */
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
name|collect
operator|.
name|BoundType
operator|.
name|CLOSED
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|BoundType
operator|.
name|OPEN
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
name|collect
operator|.
name|Multiset
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|NoSuchElementException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
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
comment|/**  * Provides static utility methods for creating and working with  * {@link SortedMultiset} instances.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|SortedMultisets
specifier|final
class|class
name|SortedMultisets
block|{
DECL|method|SortedMultisets ()
specifier|private
name|SortedMultisets
parameter_list|()
block|{   }
comment|/**    * A skeleton implementation for {@link SortedMultiset#elementSet}.    */
DECL|class|ElementSet
specifier|static
class|class
name|ElementSet
parameter_list|<
name|E
parameter_list|>
extends|extends
name|Multisets
operator|.
name|ElementSet
argument_list|<
name|E
argument_list|>
implements|implements
name|SortedSet
argument_list|<
name|E
argument_list|>
block|{
DECL|field|multiset
specifier|private
specifier|final
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|multiset
decl_stmt|;
DECL|method|ElementSet (SortedMultiset<E> multiset)
name|ElementSet
parameter_list|(
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|multiset
parameter_list|)
block|{
name|this
operator|.
name|multiset
operator|=
name|multiset
expr_stmt|;
block|}
DECL|method|multiset ()
annotation|@
name|Override
specifier|final
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|multiset
parameter_list|()
block|{
return|return
name|multiset
return|;
block|}
DECL|method|comparator ()
annotation|@
name|Override
specifier|public
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
parameter_list|()
block|{
return|return
name|multiset
argument_list|()
operator|.
name|comparator
argument_list|()
return|;
block|}
DECL|method|subSet (E fromElement, E toElement)
annotation|@
name|Override
specifier|public
name|SortedSet
argument_list|<
name|E
argument_list|>
name|subSet
parameter_list|(
name|E
name|fromElement
parameter_list|,
name|E
name|toElement
parameter_list|)
block|{
return|return
name|multiset
argument_list|()
operator|.
name|subMultiset
argument_list|(
name|fromElement
argument_list|,
name|CLOSED
argument_list|,
name|toElement
argument_list|,
name|OPEN
argument_list|)
operator|.
name|elementSet
argument_list|()
return|;
block|}
DECL|method|headSet (E toElement)
annotation|@
name|Override
specifier|public
name|SortedSet
argument_list|<
name|E
argument_list|>
name|headSet
parameter_list|(
name|E
name|toElement
parameter_list|)
block|{
return|return
name|multiset
argument_list|()
operator|.
name|headMultiset
argument_list|(
name|toElement
argument_list|,
name|OPEN
argument_list|)
operator|.
name|elementSet
argument_list|()
return|;
block|}
DECL|method|tailSet (E fromElement)
annotation|@
name|Override
specifier|public
name|SortedSet
argument_list|<
name|E
argument_list|>
name|tailSet
parameter_list|(
name|E
name|fromElement
parameter_list|)
block|{
return|return
name|multiset
argument_list|()
operator|.
name|tailMultiset
argument_list|(
name|fromElement
argument_list|,
name|CLOSED
argument_list|)
operator|.
name|elementSet
argument_list|()
return|;
block|}
DECL|method|first ()
annotation|@
name|Override
specifier|public
name|E
name|first
parameter_list|()
block|{
return|return
name|getElementOrThrow
argument_list|(
name|multiset
argument_list|()
operator|.
name|firstEntry
argument_list|()
argument_list|)
return|;
block|}
DECL|method|last ()
annotation|@
name|Override
specifier|public
name|E
name|last
parameter_list|()
block|{
return|return
name|getElementOrThrow
argument_list|(
name|multiset
argument_list|()
operator|.
name|lastEntry
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|method|getElementOrThrow (Entry<E> entry)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|E
name|getElementOrThrow
parameter_list|(
name|Entry
argument_list|<
name|E
argument_list|>
name|entry
parameter_list|)
block|{
if|if
condition|(
name|entry
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
return|return
name|entry
operator|.
name|getElement
argument_list|()
return|;
block|}
DECL|method|getElementOrNull (@ullable Entry<E> entry)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|E
name|getElementOrNull
parameter_list|(
annotation|@
name|Nullable
name|Entry
argument_list|<
name|E
argument_list|>
name|entry
parameter_list|)
block|{
return|return
operator|(
name|entry
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|entry
operator|.
name|getElement
argument_list|()
return|;
block|}
block|}
end_class

end_unit

