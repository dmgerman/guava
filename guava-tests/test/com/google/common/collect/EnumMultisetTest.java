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
name|collect
operator|.
name|testing
operator|.
name|AnEnum
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
name|features
operator|.
name|CollectionFeature
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
name|features
operator|.
name|CollectionSize
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
name|google
operator|.
name|MultisetFeature
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
name|google
operator|.
name|MultisetTestSuiteBuilder
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
name|google
operator|.
name|TestEnumMultisetGenerator
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
name|ClassSanityTester
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
name|NullPointerTester
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
name|Test
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
name|junit
operator|.
name|framework
operator|.
name|TestSuite
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
annotation|@
name|GwtIncompatible
comment|// suite
DECL|method|suite ()
specifier|public
specifier|static
name|Test
name|suite
parameter_list|()
block|{
name|TestSuite
name|suite
init|=
operator|new
name|TestSuite
argument_list|()
decl_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|MultisetTestSuiteBuilder
operator|.
name|using
argument_list|(
name|enumMultisetGenerator
argument_list|()
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ANY
argument_list|,
name|CollectionFeature
operator|.
name|KNOWN_ORDER
argument_list|,
name|CollectionFeature
operator|.
name|GENERAL_PURPOSE
argument_list|,
name|CollectionFeature
operator|.
name|SUPPORTS_ITERATOR_REMOVE
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_QUERIES
argument_list|,
name|MultisetFeature
operator|.
name|ENTRIES_ARE_VIEWS
argument_list|)
operator|.
name|named
argument_list|(
literal|"EnumMultiset"
argument_list|)
operator|.
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTestSuite
argument_list|(
name|EnumMultisetTest
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|suite
return|;
block|}
DECL|method|enumMultisetGenerator ()
specifier|private
specifier|static
name|TestEnumMultisetGenerator
name|enumMultisetGenerator
parameter_list|()
block|{
return|return
operator|new
name|TestEnumMultisetGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Multiset
argument_list|<
name|AnEnum
argument_list|>
name|create
parameter_list|(
name|AnEnum
index|[]
name|elements
parameter_list|)
block|{
return|return
operator|(
name|elements
operator|.
name|length
operator|==
literal|0
operator|)
condition|?
name|EnumMultiset
operator|.
name|create
argument_list|(
name|AnEnum
operator|.
name|class
argument_list|)
else|:
name|EnumMultiset
operator|.
name|create
argument_list|(
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|enum|Color
specifier|private
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
DECL|enum|Gender
specifier|private
enum|enum
name|Gender
block|{
DECL|enumConstant|MALE
DECL|enumConstant|FEMALE
name|MALE
block|,
name|FEMALE
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
DECL|method|testCreateEmptyWithClass ()
specifier|public
name|void
name|testCreateEmptyWithClass
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
name|ImmutableList
operator|.
expr|<
name|Color
operator|>
name|of
argument_list|()
argument_list|,
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
block|}
DECL|method|testCreateEmptyWithoutClassFails ()
specifier|public
name|void
name|testCreateEmptyWithoutClassFails
parameter_list|()
block|{
try|try
block|{
name|EnumMultiset
operator|.
name|create
argument_list|(
name|ImmutableList
operator|.
expr|<
name|Color
operator|>
name|of
argument_list|()
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
comment|// SerializableTester
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
comment|// Wrapper of EnumMultiset factory methods, because we need to skip create(Class).
comment|// create(Enum1.class) is equal to create(Enum2.class) but testEquals() expects otherwise.
comment|// For the same reason, we need to skip create(Iterable, Class).
DECL|class|EnumMultisetFactory
specifier|private
specifier|static
class|class
name|EnumMultisetFactory
block|{
DECL|method|create (Iterable<E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Enum
argument_list|<
name|E
argument_list|>
parameter_list|>
name|EnumMultiset
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Iterable
argument_list|<
name|E
argument_list|>
name|elements
parameter_list|)
block|{
return|return
name|EnumMultiset
operator|.
name|create
argument_list|(
name|elements
argument_list|)
return|;
block|}
block|}
annotation|@
name|GwtIncompatible
comment|// reflection
DECL|method|testEquals ()
specifier|public
name|void
name|testEquals
parameter_list|()
throws|throws
name|Exception
block|{
operator|new
name|ClassSanityTester
argument_list|()
operator|.
name|setDistinctValues
argument_list|(
name|Class
operator|.
name|class
argument_list|,
name|Color
operator|.
name|class
argument_list|,
name|Gender
operator|.
name|class
argument_list|)
operator|.
name|setDistinctValues
argument_list|(
name|Enum
operator|.
name|class
argument_list|,
name|Color
operator|.
name|BLUE
argument_list|,
name|Color
operator|.
name|RED
argument_list|)
operator|.
name|forAllPublicStaticMethods
argument_list|(
name|EnumMultisetFactory
operator|.
name|class
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// reflection
DECL|method|testNulls ()
specifier|public
name|void
name|testNulls
parameter_list|()
throws|throws
name|Exception
block|{
operator|new
name|NullPointerTester
argument_list|()
operator|.
name|setDefault
argument_list|(
name|Class
operator|.
name|class
argument_list|,
name|Color
operator|.
name|class
argument_list|)
operator|.
name|setDefault
argument_list|(
name|Iterable
operator|.
name|class
argument_list|,
name|EnumSet
operator|.
name|allOf
argument_list|(
name|Color
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|testAllPublicStaticMethods
argument_list|(
name|EnumMultiset
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

