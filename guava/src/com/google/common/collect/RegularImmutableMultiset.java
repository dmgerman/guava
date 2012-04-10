begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|Map
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
comment|/**  * Implementation of {@link ImmutableMultiset} with one or more elements.  *  * @author Jared Levy  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
comment|// uses writeReplace(), not default serialization
DECL|class|RegularImmutableMultiset
class|class
name|RegularImmutableMultiset
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
block|{
DECL|field|map
specifier|private
specifier|final
specifier|transient
name|ImmutableMap
argument_list|<
name|E
argument_list|,
name|Integer
argument_list|>
name|map
decl_stmt|;
DECL|field|size
specifier|private
specifier|final
specifier|transient
name|int
name|size
decl_stmt|;
DECL|method|RegularImmutableMultiset (ImmutableMap<E, Integer> map, int size)
name|RegularImmutableMultiset
parameter_list|(
name|ImmutableMap
argument_list|<
name|E
argument_list|,
name|Integer
argument_list|>
name|map
parameter_list|,
name|int
name|size
parameter_list|)
block|{
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isPartialView ()
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
name|map
operator|.
name|isPartialView
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|count (@ullable Object element)
specifier|public
name|int
name|count
parameter_list|(
annotation|@
name|Nullable
name|Object
name|element
parameter_list|)
block|{
name|Integer
name|value
init|=
name|map
operator|.
name|get
argument_list|(
name|element
argument_list|)
decl_stmt|;
return|return
operator|(
name|value
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|value
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
name|size
return|;
block|}
annotation|@
name|Override
DECL|method|contains (@ullable Object element)
specifier|public
name|boolean
name|contains
parameter_list|(
annotation|@
name|Nullable
name|Object
name|element
parameter_list|)
block|{
return|return
name|map
operator|.
name|containsKey
argument_list|(
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|elementSet ()
specifier|public
name|ImmutableSet
argument_list|<
name|E
argument_list|>
name|elementSet
parameter_list|()
block|{
return|return
name|map
operator|.
name|keySet
argument_list|()
return|;
block|}
DECL|method|entryFromMapEntry (Map.Entry<E, Integer> entry)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Entry
argument_list|<
name|E
argument_list|>
name|entryFromMapEntry
parameter_list|(
name|Map
operator|.
name|Entry
argument_list|<
name|E
argument_list|,
name|Integer
argument_list|>
name|entry
parameter_list|)
block|{
return|return
name|Multisets
operator|.
name|immutableEntry
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createEntrySet ()
name|ImmutableSet
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|createEntrySet
parameter_list|()
block|{
return|return
operator|new
name|EntrySet
argument_list|()
return|;
block|}
DECL|class|EntrySet
specifier|private
class|class
name|EntrySet
extends|extends
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
operator|.
name|EntrySet
block|{
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|map
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|UnmodifiableIterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|asList
argument_list|()
operator|.
name|iterator
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createAsList ()
name|ImmutableList
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|createAsList
parameter_list|()
block|{
specifier|final
name|ImmutableList
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|E
argument_list|,
name|Integer
argument_list|>
argument_list|>
name|entryList
init|=
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|asList
argument_list|()
decl_stmt|;
return|return
operator|new
name|ImmutableAsList
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Entry
argument_list|<
name|E
argument_list|>
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|entryFromMapEntry
argument_list|(
name|entryList
operator|.
name|get
argument_list|(
name|index
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
name|ImmutableCollection
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|delegateCollection
parameter_list|()
block|{
return|return
name|EntrySet
operator|.
name|this
return|;
block|}
block|}
return|;
block|}
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
name|map
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
end_class

end_unit

