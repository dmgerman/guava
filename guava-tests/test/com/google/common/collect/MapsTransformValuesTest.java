begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Function
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
name|Functions
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
name|testing
operator|.
name|MapInterfaceTest
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
name|Iterator
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
comment|/**  * Tests for {@link Maps#transformValues}.  *  * @author Isaac Shum  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|MapsTransformValuesTest
specifier|public
class|class
name|MapsTransformValuesTest
extends|extends
name|MapInterfaceTest
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
block|{
DECL|method|MapsTransformValuesTest ()
specifier|public
name|MapsTransformValuesTest
parameter_list|()
block|{
name|super
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|,
literal|false
comment|/*supportsPut*/
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|makeEmptyMap ()
annotation|@
name|Override
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|makeEmptyMap
parameter_list|()
block|{
return|return
name|Maps
operator|.
name|transformValues
argument_list|(
name|Maps
operator|.
expr|<
name|String
argument_list|,
name|String
operator|>
name|newHashMap
argument_list|()
argument_list|,
name|Functions
operator|.
expr|<
name|String
operator|>
name|identity
argument_list|()
argument_list|)
return|;
block|}
DECL|method|makePopulatedMap ()
annotation|@
name|Override
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|makePopulatedMap
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|underlying
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|underlying
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|underlying
operator|.
name|put
argument_list|(
literal|"b"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|underlying
operator|.
name|put
argument_list|(
literal|"c"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
return|return
name|Maps
operator|.
name|transformValues
argument_list|(
name|underlying
argument_list|,
name|Functions
operator|.
name|toStringFunction
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getKeyNotInPopulatedMap ()
annotation|@
name|Override
specifier|protected
name|String
name|getKeyNotInPopulatedMap
parameter_list|()
throws|throws
name|UnsupportedOperationException
block|{
return|return
literal|"z"
return|;
block|}
DECL|method|getValueNotInPopulatedMap ()
annotation|@
name|Override
specifier|protected
name|String
name|getValueNotInPopulatedMap
parameter_list|()
throws|throws
name|UnsupportedOperationException
block|{
return|return
literal|"26"
return|;
block|}
comment|/** Helper assertion comparing two maps */
DECL|method|assertMapsEqual (Map<?, ?> expected, Map<?, ?> map)
specifier|private
name|void
name|assertMapsEqual
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|expected
parameter_list|,
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|hashCode
argument_list|()
argument_list|,
name|map
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|entrySet
argument_list|()
argument_list|,
name|map
operator|.
name|entrySet
argument_list|()
argument_list|)
expr_stmt|;
comment|// Assert that expectedValues> mapValues and that
comment|// mapValues> expectedValues; i.e. that expectedValues == mapValues.
name|Collection
argument_list|<
name|?
argument_list|>
name|expectedValues
init|=
name|expected
operator|.
name|values
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|?
argument_list|>
name|mapValues
init|=
name|map
operator|.
name|values
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedValues
operator|.
name|size
argument_list|()
argument_list|,
name|mapValues
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|expectedValues
operator|.
name|containsAll
argument_list|(
name|mapValues
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|mapValues
operator|.
name|containsAll
argument_list|(
name|expectedValues
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testTransformEmptyMapEquality ()
specifier|public
name|void
name|testTransformEmptyMapEquality
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|Maps
operator|.
name|transformValues
argument_list|(
name|ImmutableMap
operator|.
expr|<
name|String
argument_list|,
name|Integer
operator|>
name|of
argument_list|()
argument_list|,
name|Functions
operator|.
name|toStringFunction
argument_list|()
argument_list|)
decl_stmt|;
name|assertMapsEqual
argument_list|(
name|Maps
operator|.
name|newHashMap
argument_list|()
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
DECL|method|testTransformSingletonMapEquality ()
specifier|public
name|void
name|testTransformSingletonMapEquality
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|Maps
operator|.
name|transformValues
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"a"
argument_list|,
literal|1
argument_list|)
argument_list|,
name|Functions
operator|.
name|toStringFunction
argument_list|()
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|expected
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"a"
argument_list|,
literal|"1"
argument_list|)
decl_stmt|;
name|assertMapsEqual
argument_list|(
name|expected
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|get
argument_list|(
literal|"a"
argument_list|)
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testTransformIdentityFunctionEquality ()
specifier|public
name|void
name|testTransformIdentityFunctionEquality
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|underlying
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"a"
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|Maps
operator|.
name|transformValues
argument_list|(
name|underlying
argument_list|,
name|Functions
operator|.
expr|<
name|Integer
operator|>
name|identity
argument_list|()
argument_list|)
decl_stmt|;
name|assertMapsEqual
argument_list|(
name|underlying
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
DECL|method|testTransformPutEntryIsUnsupported ()
specifier|public
name|void
name|testTransformPutEntryIsUnsupported
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|Maps
operator|.
name|transformValues
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"a"
argument_list|,
literal|1
argument_list|)
argument_list|,
name|Functions
operator|.
name|toStringFunction
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|map
operator|.
name|put
argument_list|(
literal|"b"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
try|try
block|{
name|map
operator|.
name|putAll
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"b"
argument_list|,
literal|"2"
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
try|try
block|{
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"one"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testTransformRemoveEntry ()
specifier|public
name|void
name|testTransformRemoveEntry
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|underlying
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|underlying
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|Maps
operator|.
name|transformValues
argument_list|(
name|underlying
argument_list|,
name|Functions
operator|.
name|toStringFunction
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|map
operator|.
name|remove
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|remove
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testTransformEqualityOfMapsWithNullValues ()
specifier|public
name|void
name|testTransformEqualityOfMapsWithNullValues
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|underlying
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|underlying
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|underlying
operator|.
name|put
argument_list|(
literal|"b"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|map
init|=
name|Maps
operator|.
name|transformValues
argument_list|(
name|underlying
argument_list|,
operator|new
name|Function
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Boolean
name|apply
parameter_list|(
annotation|@
name|Nullable
name|String
name|from
parameter_list|)
block|{
return|return
name|from
operator|==
literal|null
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|expected
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"a"
argument_list|,
literal|true
argument_list|,
literal|"b"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertMapsEqual
argument_list|(
name|expected
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|get
argument_list|(
literal|"a"
argument_list|)
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|containsKey
argument_list|(
literal|"a"
argument_list|)
argument_list|,
name|map
operator|.
name|containsKey
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|get
argument_list|(
literal|"b"
argument_list|)
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|containsKey
argument_list|(
literal|"b"
argument_list|)
argument_list|,
name|map
operator|.
name|containsKey
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|get
argument_list|(
literal|"c"
argument_list|)
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"c"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|containsKey
argument_list|(
literal|"c"
argument_list|)
argument_list|,
name|map
operator|.
name|containsKey
argument_list|(
literal|"c"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testTransformReflectsUnderlyingMap ()
specifier|public
name|void
name|testTransformReflectsUnderlyingMap
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|underlying
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|underlying
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|underlying
operator|.
name|put
argument_list|(
literal|"b"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|underlying
operator|.
name|put
argument_list|(
literal|"c"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|Maps
operator|.
name|transformValues
argument_list|(
name|underlying
argument_list|,
name|Functions
operator|.
name|toStringFunction
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|underlying
operator|.
name|size
argument_list|()
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|underlying
operator|.
name|put
argument_list|(
literal|"d"
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|underlying
operator|.
name|size
argument_list|()
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"4"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"d"
argument_list|)
argument_list|)
expr_stmt|;
name|underlying
operator|.
name|remove
argument_list|(
literal|"c"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|underlying
operator|.
name|size
argument_list|()
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|map
operator|.
name|containsKey
argument_list|(
literal|"c"
argument_list|)
argument_list|)
expr_stmt|;
name|underlying
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|underlying
operator|.
name|size
argument_list|()
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testTransformChangesAreReflectedInUnderlyingMap ()
specifier|public
name|void
name|testTransformChangesAreReflectedInUnderlyingMap
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|underlying
init|=
name|Maps
operator|.
name|newLinkedHashMap
argument_list|()
decl_stmt|;
name|underlying
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|underlying
operator|.
name|put
argument_list|(
literal|"b"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|underlying
operator|.
name|put
argument_list|(
literal|"c"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|underlying
operator|.
name|put
argument_list|(
literal|"d"
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|underlying
operator|.
name|put
argument_list|(
literal|"e"
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|underlying
operator|.
name|put
argument_list|(
literal|"f"
argument_list|,
literal|6
argument_list|)
expr_stmt|;
name|underlying
operator|.
name|put
argument_list|(
literal|"g"
argument_list|,
literal|7
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|Maps
operator|.
name|transformValues
argument_list|(
name|underlying
argument_list|,
name|Functions
operator|.
name|toStringFunction
argument_list|()
argument_list|)
decl_stmt|;
name|map
operator|.
name|remove
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|underlying
operator|.
name|containsKey
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|map
operator|.
name|keySet
argument_list|()
decl_stmt|;
name|keys
operator|.
name|remove
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|underlying
operator|.
name|containsKey
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|keyIterator
init|=
name|keys
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|keyIterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|keyIterator
operator|.
name|remove
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|underlying
operator|.
name|containsKey
argument_list|(
literal|"c"
argument_list|)
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|values
init|=
name|map
operator|.
name|values
argument_list|()
decl_stmt|;
name|values
operator|.
name|remove
argument_list|(
literal|"4"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|underlying
operator|.
name|containsKey
argument_list|(
literal|"d"
argument_list|)
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|valueIterator
init|=
name|values
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|valueIterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|valueIterator
operator|.
name|remove
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|underlying
operator|.
name|containsKey
argument_list|(
literal|"e"
argument_list|)
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|entries
init|=
name|map
operator|.
name|entrySet
argument_list|()
decl_stmt|;
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|firstEntry
init|=
name|entries
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|entries
operator|.
name|remove
argument_list|(
name|firstEntry
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|underlying
operator|.
name|containsKey
argument_list|(
literal|"f"
argument_list|)
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|entryIterator
init|=
name|entries
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|entryIterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|entryIterator
operator|.
name|remove
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|underlying
operator|.
name|containsKey
argument_list|(
literal|"g"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|underlying
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|keys
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|values
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|entries
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testTransformEquals ()
specifier|public
name|void
name|testTransformEquals
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|underlying
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"a"
argument_list|,
literal|0
argument_list|,
literal|"b"
argument_list|,
literal|1
argument_list|,
literal|"c"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|expected
init|=
name|Maps
operator|.
name|transformValues
argument_list|(
name|underlying
argument_list|,
name|Functions
operator|.
expr|<
name|Integer
operator|>
name|identity
argument_list|()
argument_list|)
decl_stmt|;
name|assertMapsEqual
argument_list|(
name|expected
argument_list|,
name|expected
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|equalToUnderlying
init|=
name|Maps
operator|.
name|newTreeMap
argument_list|()
decl_stmt|;
name|equalToUnderlying
operator|.
name|putAll
argument_list|(
name|underlying
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|Maps
operator|.
name|transformValues
argument_list|(
name|equalToUnderlying
argument_list|,
name|Functions
operator|.
expr|<
name|Integer
operator|>
name|identity
argument_list|()
argument_list|)
decl_stmt|;
name|assertMapsEqual
argument_list|(
name|expected
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|map
operator|=
name|Maps
operator|.
name|transformValues
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"a"
argument_list|,
literal|1
argument_list|,
literal|"b"
argument_list|,
literal|2
argument_list|,
literal|"c"
argument_list|,
literal|3
argument_list|)
argument_list|,
operator|new
name|Function
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Integer
name|apply
parameter_list|(
name|Integer
name|from
parameter_list|)
block|{
return|return
name|from
operator|-
literal|1
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertMapsEqual
argument_list|(
name|expected
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
DECL|method|testTransformEntrySetContains ()
specifier|public
name|void
name|testTransformEntrySetContains
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|underlying
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|underlying
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|underlying
operator|.
name|put
argument_list|(
literal|"b"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|underlying
operator|.
name|put
argument_list|(
literal|null
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|map
init|=
name|Maps
operator|.
name|transformValues
argument_list|(
name|underlying
argument_list|,
operator|new
name|Function
argument_list|<
name|Boolean
argument_list|,
name|Boolean
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Boolean
name|apply
parameter_list|(
annotation|@
name|Nullable
name|Boolean
name|from
parameter_list|)
block|{
return|return
operator|(
name|from
operator|==
literal|null
operator|)
condition|?
literal|true
else|:
literal|null
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
argument_list|>
name|entries
init|=
name|map
operator|.
name|entrySet
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|entries
operator|.
name|contains
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|"a"
argument_list|,
literal|true
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|entries
operator|.
name|contains
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|"b"
argument_list|,
operator|(
name|Boolean
operator|)
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|entries
operator|.
name|contains
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
operator|(
name|String
operator|)
literal|null
argument_list|,
operator|(
name|Boolean
operator|)
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|entries
operator|.
name|contains
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|"c"
argument_list|,
operator|(
name|Boolean
operator|)
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|entries
operator|.
name|contains
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
operator|(
name|String
operator|)
literal|null
argument_list|,
literal|true
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testKeySetRemoveAllNullFromEmpty ()
annotation|@
name|Override
specifier|public
name|void
name|testKeySetRemoveAllNullFromEmpty
parameter_list|()
block|{
try|try
block|{
name|super
operator|.
name|testKeySetRemoveAllNullFromEmpty
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|tolerated
parameter_list|)
block|{
comment|// GWT's HashMap.keySet().removeAll(null) doesn't throws NPE.
block|}
block|}
DECL|method|testEntrySetRemoveAllNullFromEmpty ()
annotation|@
name|Override
specifier|public
name|void
name|testEntrySetRemoveAllNullFromEmpty
parameter_list|()
block|{
try|try
block|{
name|super
operator|.
name|testEntrySetRemoveAllNullFromEmpty
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|tolerated
parameter_list|)
block|{
comment|// GWT's HashMap.entrySet().removeAll(null) doesn't throws NPE.
block|}
block|}
block|}
end_class

end_unit

