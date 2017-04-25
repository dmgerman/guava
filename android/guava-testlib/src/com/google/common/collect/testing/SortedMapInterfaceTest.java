begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|SortedMap
import|;
end_import

begin_comment
comment|/**  * Tests representing the contract of {@link SortedMap}. Concrete subclasses of  * this base class test conformance of concrete {@link SortedMap} subclasses to  * that contract.  *  * @author Jared Levy  */
end_comment

begin_comment
comment|// TODO: Use this class to test classes besides ImmutableSortedMap.
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|SortedMapInterfaceTest
specifier|public
specifier|abstract
class|class
name|SortedMapInterfaceTest
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|MapInterfaceTest
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|method|SortedMapInterfaceTest ( boolean allowsNullKeys, boolean allowsNullValues, boolean supportsPut, boolean supportsRemove, boolean supportsClear)
specifier|protected
name|SortedMapInterfaceTest
parameter_list|(
name|boolean
name|allowsNullKeys
parameter_list|,
name|boolean
name|allowsNullValues
parameter_list|,
name|boolean
name|supportsPut
parameter_list|,
name|boolean
name|supportsRemove
parameter_list|,
name|boolean
name|supportsClear
parameter_list|)
block|{
name|super
argument_list|(
name|allowsNullKeys
argument_list|,
name|allowsNullValues
argument_list|,
name|supportsPut
argument_list|,
name|supportsRemove
argument_list|,
name|supportsClear
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|makeEmptyMap ()
specifier|protected
specifier|abstract
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|makeEmptyMap
parameter_list|()
throws|throws
name|UnsupportedOperationException
function_decl|;
annotation|@
name|Override
DECL|method|makePopulatedMap ()
specifier|protected
specifier|abstract
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|makePopulatedMap
parameter_list|()
throws|throws
name|UnsupportedOperationException
function_decl|;
annotation|@
name|Override
DECL|method|makeEitherMap ()
specifier|protected
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|makeEitherMap
parameter_list|()
block|{
try|try
block|{
return|return
name|makePopulatedMap
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
return|return
name|makeEmptyMap
argument_list|()
return|;
block|}
block|}
DECL|method|testTailMapWriteThrough ()
specifier|public
name|void
name|testTailMapWriteThrough
parameter_list|()
block|{
specifier|final
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
decl_stmt|;
try|try
block|{
name|map
operator|=
name|makePopulatedMap
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
return|return;
block|}
if|if
condition|(
name|map
operator|.
name|size
argument_list|()
operator|<
literal|2
operator|||
operator|!
name|supportsPut
condition|)
block|{
return|return;
block|}
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|iterator
init|=
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|firstEntry
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|secondEntry
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|K
name|key
init|=
name|secondEntry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|subMap
init|=
name|map
operator|.
name|tailMap
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|V
name|value
init|=
name|getValueNotInPopulatedMap
argument_list|()
decl_stmt|;
name|subMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|secondEntry
operator|.
name|getValue
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|,
name|value
argument_list|)
expr_stmt|;
try|try
block|{
name|subMap
operator|.
name|put
argument_list|(
name|firstEntry
operator|.
name|getKey
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testTailMapRemoveThrough ()
specifier|public
name|void
name|testTailMapRemoveThrough
parameter_list|()
block|{
specifier|final
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
decl_stmt|;
try|try
block|{
name|map
operator|=
name|makePopulatedMap
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
return|return;
block|}
name|int
name|oldSize
init|=
name|map
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|map
operator|.
name|size
argument_list|()
operator|<
literal|2
operator|||
operator|!
name|supportsRemove
condition|)
block|{
return|return;
block|}
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|iterator
init|=
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|firstEntry
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|secondEntry
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|K
name|key
init|=
name|secondEntry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|subMap
init|=
name|map
operator|.
name|tailMap
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|subMap
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|subMap
operator|.
name|remove
argument_list|(
name|firstEntry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|map
operator|.
name|size
argument_list|()
argument_list|,
name|oldSize
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|map
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|subMap
operator|.
name|size
argument_list|()
argument_list|,
name|oldSize
operator|-
literal|2
argument_list|)
expr_stmt|;
block|}
DECL|method|testTailMapClearThrough ()
specifier|public
name|void
name|testTailMapClearThrough
parameter_list|()
block|{
specifier|final
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
decl_stmt|;
try|try
block|{
name|map
operator|=
name|makePopulatedMap
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
return|return;
block|}
name|int
name|oldSize
init|=
name|map
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|map
operator|.
name|size
argument_list|()
operator|<
literal|2
operator|||
operator|!
name|supportsClear
condition|)
block|{
return|return;
block|}
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|iterator
init|=
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
comment|// advance
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|secondEntry
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|K
name|key
init|=
name|secondEntry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|subMap
init|=
name|map
operator|.
name|tailMap
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|int
name|subMapSize
init|=
name|subMap
operator|.
name|size
argument_list|()
decl_stmt|;
name|subMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|map
operator|.
name|size
argument_list|()
argument_list|,
name|oldSize
operator|-
name|subMapSize
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|subMap
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
