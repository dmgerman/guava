begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|testing
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
import|;
end_import

begin_comment
comment|/**  * A container class for the five sample elements we need for testing.  *  * @author Kevin Bourrillion  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|SampleElements
specifier|public
class|class
name|SampleElements
parameter_list|<
name|E
parameter_list|>
implements|implements
name|Iterable
argument_list|<
name|E
argument_list|>
block|{
comment|// TODO: rename e3, e4 => missing1, missing2
DECL|field|e0
specifier|private
specifier|final
name|E
name|e0
decl_stmt|;
DECL|field|e1
specifier|private
specifier|final
name|E
name|e1
decl_stmt|;
DECL|field|e2
specifier|private
specifier|final
name|E
name|e2
decl_stmt|;
DECL|field|e3
specifier|private
specifier|final
name|E
name|e3
decl_stmt|;
DECL|field|e4
specifier|private
specifier|final
name|E
name|e4
decl_stmt|;
DECL|method|SampleElements (E e0, E e1, E e2, E e3, E e4)
specifier|public
name|SampleElements
parameter_list|(
name|E
name|e0
parameter_list|,
name|E
name|e1
parameter_list|,
name|E
name|e2
parameter_list|,
name|E
name|e3
parameter_list|,
name|E
name|e4
parameter_list|)
block|{
name|this
operator|.
name|e0
operator|=
name|e0
expr_stmt|;
name|this
operator|.
name|e1
operator|=
name|e1
expr_stmt|;
name|this
operator|.
name|e2
operator|=
name|e2
expr_stmt|;
name|this
operator|.
name|e3
operator|=
name|e3
expr_stmt|;
name|this
operator|.
name|e4
operator|=
name|e4
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|Iterator
argument_list|<
name|E
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
DECL|method|asList ()
specifier|public
name|List
argument_list|<
name|E
argument_list|>
name|asList
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|e0
argument_list|()
argument_list|,
name|e1
argument_list|()
argument_list|,
name|e2
argument_list|()
argument_list|,
name|e3
argument_list|()
argument_list|,
name|e4
argument_list|()
argument_list|)
return|;
block|}
DECL|class|Strings
specifier|public
specifier|static
class|class
name|Strings
extends|extends
name|SampleElements
argument_list|<
name|String
argument_list|>
block|{
DECL|method|Strings ()
specifier|public
name|Strings
parameter_list|()
block|{
comment|// elements aren't sorted, to better test SortedSet iteration ordering
name|super
argument_list|(
literal|"b"
argument_list|,
literal|"a"
argument_list|,
literal|"c"
argument_list|,
literal|"d"
argument_list|,
literal|"e"
argument_list|)
expr_stmt|;
block|}
comment|// for testing SortedSet and SortedMap methods
DECL|field|BEFORE_FIRST
specifier|public
specifier|static
specifier|final
name|String
name|BEFORE_FIRST
init|=
literal|"\0"
decl_stmt|;
DECL|field|BEFORE_FIRST_2
specifier|public
specifier|static
specifier|final
name|String
name|BEFORE_FIRST_2
init|=
literal|"\0\0"
decl_stmt|;
DECL|field|MIN_ELEMENT
specifier|public
specifier|static
specifier|final
name|String
name|MIN_ELEMENT
init|=
literal|"a"
decl_stmt|;
DECL|field|AFTER_LAST
specifier|public
specifier|static
specifier|final
name|String
name|AFTER_LAST
init|=
literal|"z"
decl_stmt|;
DECL|field|AFTER_LAST_2
specifier|public
specifier|static
specifier|final
name|String
name|AFTER_LAST_2
init|=
literal|"zz"
decl_stmt|;
block|}
DECL|class|Chars
specifier|public
specifier|static
class|class
name|Chars
extends|extends
name|SampleElements
argument_list|<
name|Character
argument_list|>
block|{
DECL|method|Chars ()
specifier|public
name|Chars
parameter_list|()
block|{
comment|// elements aren't sorted, to better test SortedSet iteration ordering
name|super
argument_list|(
literal|'b'
argument_list|,
literal|'a'
argument_list|,
literal|'c'
argument_list|,
literal|'d'
argument_list|,
literal|'e'
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|Enums
specifier|public
specifier|static
class|class
name|Enums
extends|extends
name|SampleElements
argument_list|<
name|AnEnum
argument_list|>
block|{
DECL|method|Enums ()
specifier|public
name|Enums
parameter_list|()
block|{
comment|// elements aren't sorted, to better test SortedSet iteration ordering
name|super
argument_list|(
name|AnEnum
operator|.
name|B
argument_list|,
name|AnEnum
operator|.
name|A
argument_list|,
name|AnEnum
operator|.
name|C
argument_list|,
name|AnEnum
operator|.
name|D
argument_list|,
name|AnEnum
operator|.
name|E
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|Ints
specifier|public
specifier|static
class|class
name|Ints
extends|extends
name|SampleElements
argument_list|<
name|Integer
argument_list|>
block|{
DECL|method|Ints ()
specifier|public
name|Ints
parameter_list|()
block|{
comment|// elements aren't sorted, to better test SortedSet iteration ordering
name|super
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|4
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|mapEntries ( SampleElements<K> keys, SampleElements<V> values)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|SampleElements
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|mapEntries
parameter_list|(
name|SampleElements
argument_list|<
name|K
argument_list|>
name|keys
parameter_list|,
name|SampleElements
argument_list|<
name|V
argument_list|>
name|values
parameter_list|)
block|{
return|return
operator|new
name|SampleElements
argument_list|<>
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|keys
operator|.
name|e0
argument_list|()
argument_list|,
name|values
operator|.
name|e0
argument_list|()
argument_list|)
argument_list|,
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|keys
operator|.
name|e1
argument_list|()
argument_list|,
name|values
operator|.
name|e1
argument_list|()
argument_list|)
argument_list|,
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|keys
operator|.
name|e2
argument_list|()
argument_list|,
name|values
operator|.
name|e2
argument_list|()
argument_list|)
argument_list|,
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|keys
operator|.
name|e3
argument_list|()
argument_list|,
name|values
operator|.
name|e3
argument_list|()
argument_list|)
argument_list|,
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|keys
operator|.
name|e4
argument_list|()
argument_list|,
name|values
operator|.
name|e4
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|e0 ()
specifier|public
name|E
name|e0
parameter_list|()
block|{
return|return
name|e0
return|;
block|}
DECL|method|e1 ()
specifier|public
name|E
name|e1
parameter_list|()
block|{
return|return
name|e1
return|;
block|}
DECL|method|e2 ()
specifier|public
name|E
name|e2
parameter_list|()
block|{
return|return
name|e2
return|;
block|}
DECL|method|e3 ()
specifier|public
name|E
name|e3
parameter_list|()
block|{
return|return
name|e3
return|;
block|}
DECL|method|e4 ()
specifier|public
name|E
name|e4
parameter_list|()
block|{
return|return
name|e4
return|;
block|}
DECL|class|Unhashables
specifier|public
specifier|static
class|class
name|Unhashables
extends|extends
name|SampleElements
argument_list|<
name|UnhashableObject
argument_list|>
block|{
DECL|method|Unhashables ()
specifier|public
name|Unhashables
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|UnhashableObject
argument_list|(
literal|1
argument_list|)
argument_list|,
operator|new
name|UnhashableObject
argument_list|(
literal|2
argument_list|)
argument_list|,
operator|new
name|UnhashableObject
argument_list|(
literal|3
argument_list|)
argument_list|,
operator|new
name|UnhashableObject
argument_list|(
literal|4
argument_list|)
argument_list|,
operator|new
name|UnhashableObject
argument_list|(
literal|5
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|Colliders
specifier|public
specifier|static
class|class
name|Colliders
extends|extends
name|SampleElements
argument_list|<
name|Object
argument_list|>
block|{
DECL|method|Colliders ()
specifier|public
name|Colliders
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|Collider
argument_list|(
literal|1
argument_list|)
argument_list|,
operator|new
name|Collider
argument_list|(
literal|2
argument_list|)
argument_list|,
operator|new
name|Collider
argument_list|(
literal|3
argument_list|)
argument_list|,
operator|new
name|Collider
argument_list|(
literal|4
argument_list|)
argument_list|,
operator|new
name|Collider
argument_list|(
literal|5
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|Collider
specifier|private
specifier|static
class|class
name|Collider
block|{
DECL|field|value
specifier|final
name|int
name|value
decl_stmt|;
DECL|method|Collider (int value)
name|Collider
parameter_list|(
name|int
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
return|return
name|obj
operator|instanceof
name|Collider
operator|&&
operator|(
operator|(
name|Collider
operator|)
name|obj
operator|)
operator|.
name|value
operator|==
name|value
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
literal|1
return|;
comment|// evil!
block|}
block|}
block|}
end_class

end_unit

