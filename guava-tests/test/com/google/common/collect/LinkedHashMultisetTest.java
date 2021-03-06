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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
import|;
end_import

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
name|TestStringMultisetGenerator
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
name|List
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

begin_comment
comment|/**  * Unit test for {@link LinkedHashMultiset}.  *  * @author Kevin Bourrillion  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|LinkedHashMultisetTest
specifier|public
class|class
name|LinkedHashMultisetTest
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
name|linkedHashMultisetGenerator
argument_list|()
argument_list|)
operator|.
name|named
argument_list|(
literal|"LinkedHashMultiset"
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
name|FAILS_FAST_ON_CONCURRENT_MODIFICATION
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
argument_list|,
name|CollectionFeature
operator|.
name|SERIALIZABLE
argument_list|,
name|CollectionFeature
operator|.
name|GENERAL_PURPOSE
argument_list|,
name|MultisetFeature
operator|.
name|ENTRIES_ARE_VIEWS
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
name|LinkedHashMultisetTest
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|suite
return|;
block|}
DECL|method|linkedHashMultisetGenerator ()
specifier|private
specifier|static
name|TestStringMultisetGenerator
name|linkedHashMultisetGenerator
parameter_list|()
block|{
return|return
operator|new
name|TestStringMultisetGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Multiset
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
return|return
name|LinkedHashMultiset
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
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|order
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|insertionOrder
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|order
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|insertionOrder
control|)
block|{
name|int
name|index
init|=
name|order
operator|.
name|indexOf
argument_list|(
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|==
operator|-
literal|1
condition|)
block|{
name|order
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|order
operator|.
name|add
argument_list|(
name|index
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|order
return|;
block|}
block|}
return|;
block|}
DECL|method|testCreate ()
specifier|public
name|void
name|testCreate
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|multiset
init|=
name|LinkedHashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|multiset
operator|.
name|add
argument_list|(
literal|"foo"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|multiset
operator|.
name|add
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|multiset
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|multiset
operator|.
name|count
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[foo x 2, bar]"
argument_list|,
name|multiset
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateWithSize ()
specifier|public
name|void
name|testCreateWithSize
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|multiset
init|=
name|LinkedHashMultiset
operator|.
name|create
argument_list|(
literal|50
argument_list|)
decl_stmt|;
name|multiset
operator|.
name|add
argument_list|(
literal|"foo"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|multiset
operator|.
name|add
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|multiset
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|multiset
operator|.
name|count
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[foo x 2, bar]"
argument_list|,
name|multiset
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateFromIterable ()
specifier|public
name|void
name|testCreateFromIterable
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|multiset
init|=
name|LinkedHashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|multiset
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|multiset
operator|.
name|count
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[foo x 2, bar]"
argument_list|,
name|multiset
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms
init|=
name|LinkedHashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"c"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"b"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[a x 3, c, b x 2]"
argument_list|,
name|ms
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testLosesPlaceInLine ()
specifier|public
name|void
name|testLosesPlaceInLine
parameter_list|()
throws|throws
name|Exception
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms
init|=
name|LinkedHashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"b"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"c"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ms
operator|.
name|elementSet
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|ms
operator|.
name|remove
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ms
operator|.
name|elementSet
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ms
operator|.
name|elementSet
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|ms
operator|.
name|remove
argument_list|(
literal|"b"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ms
operator|.
name|elementSet
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"a"
argument_list|,
literal|"c"
argument_list|,
literal|"b"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

