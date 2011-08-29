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
import|import static
name|java
operator|.
name|util
operator|.
name|Arrays
operator|.
name|asList
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
name|annotations
operator|.
name|GwtIncompatible
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
name|testing
operator|.
name|SerializableTester
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|EnumSet
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

begin_comment
comment|/**  * Tests for an {@link EnumMultiset}.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|EnumMultisetTest
specifier|public
class|class
name|EnumMultisetTest
extends|extends
name|TestCase
block|{
DECL|enum|Color
specifier|private
specifier|static
enum|enum
name|Color
block|{
DECL|enumConstant|BLUE
DECL|enumConstant|RED
DECL|enumConstant|YELLOW
DECL|enumConstant|GREEN
DECL|enumConstant|WHITE
name|BLUE
block|,
name|RED
block|,
name|YELLOW
block|,
name|GREEN
block|,
name|WHITE
block|}
DECL|method|testClassCreate ()
specifier|public
name|void
name|testClassCreate
parameter_list|()
block|{
name|Multiset
argument_list|<
name|Color
argument_list|>
name|ms
init|=
name|EnumMultiset
operator|.
name|create
argument_list|(
name|Color
operator|.
name|class
argument_list|)
decl_stmt|;
name|ms
operator|.
name|add
argument_list|(
name|Color
operator|.
name|RED
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
name|Color
operator|.
name|YELLOW
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
name|Color
operator|.
name|RED
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|ms
operator|.
name|count
argument_list|(
name|Color
operator|.
name|BLUE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|ms
operator|.
name|count
argument_list|(
name|Color
operator|.
name|YELLOW
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|ms
operator|.
name|count
argument_list|(
name|Color
operator|.
name|RED
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCollectionCreate ()
specifier|public
name|void
name|testCollectionCreate
parameter_list|()
block|{
name|Multiset
argument_list|<
name|Color
argument_list|>
name|ms
init|=
name|EnumMultiset
operator|.
name|create
argument_list|(
name|asList
argument_list|(
name|Color
operator|.
name|RED
argument_list|,
name|Color
operator|.
name|YELLOW
argument_list|,
name|Color
operator|.
name|RED
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|ms
operator|.
name|count
argument_list|(
name|Color
operator|.
name|BLUE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|ms
operator|.
name|count
argument_list|(
name|Color
operator|.
name|YELLOW
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|ms
operator|.
name|count
argument_list|(
name|Color
operator|.
name|RED
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testIllegalCreate ()
specifier|public
name|void
name|testIllegalCreate
parameter_list|()
block|{
name|Collection
argument_list|<
name|Color
argument_list|>
name|empty
init|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|Color
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|EnumMultiset
operator|.
name|create
argument_list|(
name|empty
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|Multiset
argument_list|<
name|Color
argument_list|>
name|ms
init|=
name|EnumMultiset
operator|.
name|create
argument_list|(
name|Color
operator|.
name|class
argument_list|)
decl_stmt|;
name|ms
operator|.
name|add
argument_list|(
name|Color
operator|.
name|BLUE
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
name|Color
operator|.
name|YELLOW
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
name|Color
operator|.
name|RED
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[BLUE x 3, RED x 2, YELLOW]"
argument_list|,
name|ms
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"SerializableTester"
argument_list|)
DECL|method|testSerializable ()
specifier|public
name|void
name|testSerializable
parameter_list|()
block|{
name|Multiset
argument_list|<
name|Color
argument_list|>
name|ms
init|=
name|EnumMultiset
operator|.
name|create
argument_list|(
name|asList
argument_list|(
name|Color
operator|.
name|RED
argument_list|,
name|Color
operator|.
name|YELLOW
argument_list|,
name|Color
operator|.
name|RED
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|,
name|SerializableTester
operator|.
name|reserialize
argument_list|(
name|ms
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testEntrySet ()
specifier|public
name|void
name|testEntrySet
parameter_list|()
block|{
name|Multiset
argument_list|<
name|Color
argument_list|>
name|ms
init|=
name|EnumMultiset
operator|.
name|create
argument_list|(
name|Color
operator|.
name|class
argument_list|)
decl_stmt|;
name|ms
operator|.
name|add
argument_list|(
name|Color
operator|.
name|BLUE
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
name|Color
operator|.
name|YELLOW
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
name|Color
operator|.
name|RED
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|uniqueEntries
init|=
name|Sets
operator|.
name|newIdentityHashSet
argument_list|()
decl_stmt|;
name|uniqueEntries
operator|.
name|addAll
argument_list|(
name|ms
operator|.
name|entrySet
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|uniqueEntries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

