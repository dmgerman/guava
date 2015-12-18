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
name|ImmutableSet
operator|.
name|Builder
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
name|ListTestSuiteBuilder
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
name|SetTestSuiteBuilder
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
name|SetGenerators
operator|.
name|DegeneratedImmutableSetGenerator
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
name|SetGenerators
operator|.
name|ImmutableSetAsListGenerator
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
name|SetGenerators
operator|.
name|ImmutableSetCopyOfGenerator
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
name|SetGenerators
operator|.
name|ImmutableSetWithBadHashesGenerator
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
name|EqualsTester
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
name|Collections
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
name|Set
import|;
end_import

begin_comment
comment|/**  * Unit test for {@link ImmutableSet}.  *  * @author Kevin Bourrillion  * @author Jared Levy  * @author Nick Kralevich  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|ImmutableSetTest
specifier|public
class|class
name|ImmutableSetTest
extends|extends
name|AbstractImmutableSetTest
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
name|SetTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|ImmutableSetCopyOfGenerator
argument_list|()
argument_list|)
operator|.
name|named
argument_list|(
name|ImmutableSetTest
operator|.
name|class
operator|.
name|getName
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
name|SERIALIZABLE
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_QUERIES
argument_list|)
operator|.
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|SetTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|ImmutableSetWithBadHashesGenerator
argument_list|()
argument_list|)
operator|.
name|named
argument_list|(
name|ImmutableSetTest
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|", with bad hashes"
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
name|ALLOWS_NULL_QUERIES
argument_list|)
operator|.
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|SetTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|DegeneratedImmutableSetGenerator
argument_list|()
argument_list|)
operator|.
name|named
argument_list|(
name|ImmutableSetTest
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|", degenerate"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ONE
argument_list|,
name|CollectionFeature
operator|.
name|KNOWN_ORDER
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_QUERIES
argument_list|)
operator|.
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|ListTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|ImmutableSetAsListGenerator
argument_list|()
argument_list|)
operator|.
name|named
argument_list|(
literal|"ImmutableSet.asList"
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
name|REJECTS_DUPLICATES_AT_CREATION
argument_list|,
name|CollectionFeature
operator|.
name|SERIALIZABLE
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_QUERIES
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
name|ImmutableSetTest
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|suite
return|;
block|}
DECL|method|of ()
annotation|@
name|Override
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|of
parameter_list|()
block|{
return|return
name|ImmutableSet
operator|.
name|of
argument_list|()
return|;
block|}
DECL|method|of (String e)
annotation|@
name|Override
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|of
parameter_list|(
name|String
name|e
parameter_list|)
block|{
return|return
name|ImmutableSet
operator|.
name|of
argument_list|(
name|e
argument_list|)
return|;
block|}
DECL|method|of (String e1, String e2)
annotation|@
name|Override
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|of
parameter_list|(
name|String
name|e1
parameter_list|,
name|String
name|e2
parameter_list|)
block|{
return|return
name|ImmutableSet
operator|.
name|of
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|)
return|;
block|}
DECL|method|of (String e1, String e2, String e3)
annotation|@
name|Override
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|of
parameter_list|(
name|String
name|e1
parameter_list|,
name|String
name|e2
parameter_list|,
name|String
name|e3
parameter_list|)
block|{
return|return
name|ImmutableSet
operator|.
name|of
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|,
name|e3
argument_list|)
return|;
block|}
DECL|method|of ( String e1, String e2, String e3, String e4)
annotation|@
name|Override
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|of
parameter_list|(
name|String
name|e1
parameter_list|,
name|String
name|e2
parameter_list|,
name|String
name|e3
parameter_list|,
name|String
name|e4
parameter_list|)
block|{
return|return
name|ImmutableSet
operator|.
name|of
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|,
name|e3
argument_list|,
name|e4
argument_list|)
return|;
block|}
DECL|method|of ( String e1, String e2, String e3, String e4, String e5)
annotation|@
name|Override
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|of
parameter_list|(
name|String
name|e1
parameter_list|,
name|String
name|e2
parameter_list|,
name|String
name|e3
parameter_list|,
name|String
name|e4
parameter_list|,
name|String
name|e5
parameter_list|)
block|{
return|return
name|ImmutableSet
operator|.
name|of
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|,
name|e3
argument_list|,
name|e4
argument_list|,
name|e5
argument_list|)
return|;
block|}
DECL|method|of (String e1, String e2, String e3, String e4, String e5, String e6, String... rest)
annotation|@
name|Override
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|of
parameter_list|(
name|String
name|e1
parameter_list|,
name|String
name|e2
parameter_list|,
name|String
name|e3
parameter_list|,
name|String
name|e4
parameter_list|,
name|String
name|e5
parameter_list|,
name|String
name|e6
parameter_list|,
name|String
modifier|...
name|rest
parameter_list|)
block|{
return|return
name|ImmutableSet
operator|.
name|of
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|,
name|e3
argument_list|,
name|e4
argument_list|,
name|e5
argument_list|,
name|e6
argument_list|,
name|rest
argument_list|)
return|;
block|}
DECL|method|copyOf (String[] elements)
annotation|@
name|Override
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|copyOf
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
return|return
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|elements
argument_list|)
return|;
block|}
DECL|method|copyOf (Collection<String> elements)
annotation|@
name|Override
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|copyOf
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|elements
parameter_list|)
block|{
return|return
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|elements
argument_list|)
return|;
block|}
DECL|method|copyOf (Iterable<String> elements)
annotation|@
name|Override
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|copyOf
parameter_list|(
name|Iterable
argument_list|<
name|String
argument_list|>
name|elements
parameter_list|)
block|{
return|return
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|elements
argument_list|)
return|;
block|}
DECL|method|copyOf (Iterator<String> elements)
annotation|@
name|Override
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|copyOf
parameter_list|(
name|Iterator
argument_list|<
name|String
argument_list|>
name|elements
parameter_list|)
block|{
return|return
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|elements
argument_list|)
return|;
block|}
DECL|method|testCreation_allDuplicates ()
specifier|public
name|void
name|testCreation_allDuplicates
parameter_list|()
block|{
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|set
init|=
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|Lists
operator|.
name|newArrayList
argument_list|(
literal|"a"
argument_list|,
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|set
operator|instanceof
name|SingletonImmutableSet
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Lists
operator|.
name|newArrayList
argument_list|(
literal|"a"
argument_list|)
argument_list|,
name|Lists
operator|.
name|newArrayList
argument_list|(
name|set
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreation_oneDuplicate ()
specifier|public
name|void
name|testCreation_oneDuplicate
parameter_list|()
block|{
comment|// now we'll get the varargs overload
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|set
init|=
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|,
literal|"d"
argument_list|,
literal|"e"
argument_list|,
literal|"f"
argument_list|,
literal|"g"
argument_list|,
literal|"h"
argument_list|,
literal|"i"
argument_list|,
literal|"j"
argument_list|,
literal|"k"
argument_list|,
literal|"l"
argument_list|,
literal|"m"
argument_list|,
literal|"a"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Lists
operator|.
name|newArrayList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|,
literal|"d"
argument_list|,
literal|"e"
argument_list|,
literal|"f"
argument_list|,
literal|"g"
argument_list|,
literal|"h"
argument_list|,
literal|"i"
argument_list|,
literal|"j"
argument_list|,
literal|"k"
argument_list|,
literal|"l"
argument_list|,
literal|"m"
argument_list|)
argument_list|,
name|Lists
operator|.
name|newArrayList
argument_list|(
name|set
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreation_manyDuplicates ()
specifier|public
name|void
name|testCreation_manyDuplicates
parameter_list|()
block|{
comment|// now we'll get the varargs overload
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|set
init|=
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|,
literal|"c"
argument_list|,
literal|"c"
argument_list|,
literal|"c"
argument_list|,
literal|"b"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|,
literal|"a"
argument_list|,
literal|"c"
argument_list|,
literal|"c"
argument_list|,
literal|"c"
argument_list|,
literal|"a"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|set
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
block|}
DECL|method|testCreation_arrayOfArray ()
specifier|public
name|void
name|testCreation_arrayOfArray
parameter_list|()
block|{
name|String
index|[]
name|array
init|=
operator|new
name|String
index|[]
block|{
literal|"a"
block|}
decl_stmt|;
name|Set
argument_list|<
name|String
index|[]
argument_list|>
name|set
init|=
name|ImmutableSet
operator|.
expr|<
name|String
index|[]
operator|>
name|of
argument_list|(
name|array
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|array
argument_list|)
argument_list|,
name|set
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// ImmutableSet.chooseTableSize
DECL|method|testChooseTableSize ()
specifier|public
name|void
name|testChooseTableSize
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|ImmutableSet
operator|.
name|chooseTableSize
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|ImmutableSet
operator|.
name|chooseTableSize
argument_list|(
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
operator|<<
literal|29
argument_list|,
name|ImmutableSet
operator|.
name|chooseTableSize
argument_list|(
literal|1
operator|<<
literal|28
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
operator|<<
literal|29
argument_list|,
name|ImmutableSet
operator|.
name|chooseTableSize
argument_list|(
literal|1
operator|<<
literal|29
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
comment|// Now we hit the cap
name|assertEquals
argument_list|(
literal|1
operator|<<
literal|30
argument_list|,
name|ImmutableSet
operator|.
name|chooseTableSize
argument_list|(
literal|1
operator|<<
literal|29
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
operator|<<
literal|30
argument_list|,
name|ImmutableSet
operator|.
name|chooseTableSize
argument_list|(
literal|1
operator|<<
literal|30
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
comment|// Now we've gone too far
try|try
block|{
name|ImmutableSet
operator|.
name|chooseTableSize
argument_list|(
literal|1
operator|<<
literal|30
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
block|{     }
block|}
annotation|@
name|GwtIncompatible
comment|// RegularImmutableSet.table not in emulation
DECL|method|testResizeTable ()
specifier|public
name|void
name|testResizeTable
parameter_list|()
block|{
name|verifyTableSize
argument_list|(
literal|100
argument_list|,
literal|2
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|verifyTableSize
argument_list|(
literal|100
argument_list|,
literal|5
argument_list|,
literal|8
argument_list|)
expr_stmt|;
name|verifyTableSize
argument_list|(
literal|100
argument_list|,
literal|33
argument_list|,
literal|64
argument_list|)
expr_stmt|;
name|verifyTableSize
argument_list|(
literal|17
argument_list|,
literal|17
argument_list|,
literal|32
argument_list|)
expr_stmt|;
name|verifyTableSize
argument_list|(
literal|17
argument_list|,
literal|16
argument_list|,
literal|32
argument_list|)
expr_stmt|;
name|verifyTableSize
argument_list|(
literal|17
argument_list|,
literal|15
argument_list|,
literal|32
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// RegularImmutableSet.table not in emulation
DECL|method|verifyTableSize (int inputSize, int setSize, int tableSize)
specifier|private
name|void
name|verifyTableSize
parameter_list|(
name|int
name|inputSize
parameter_list|,
name|int
name|setSize
parameter_list|,
name|int
name|tableSize
parameter_list|)
block|{
name|Builder
argument_list|<
name|Integer
argument_list|>
name|builder
init|=
name|ImmutableSet
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|inputSize
condition|;
name|i
operator|++
control|)
block|{
name|builder
operator|.
name|add
argument_list|(
name|i
operator|%
name|setSize
argument_list|)
expr_stmt|;
block|}
name|ImmutableSet
argument_list|<
name|Integer
argument_list|>
name|set
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|set
operator|instanceof
name|RegularImmutableSet
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Input size "
operator|+
name|inputSize
operator|+
literal|" and set size "
operator|+
name|setSize
argument_list|,
name|tableSize
argument_list|,
operator|(
operator|(
name|RegularImmutableSet
argument_list|<
name|Integer
argument_list|>
operator|)
name|set
operator|)
operator|.
name|table
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
DECL|method|testCopyOf_copiesImmutableSortedSet ()
specifier|public
name|void
name|testCopyOf_copiesImmutableSortedSet
parameter_list|()
block|{
name|ImmutableSortedSet
argument_list|<
name|String
argument_list|>
name|sortedSet
init|=
name|ImmutableSortedSet
operator|.
name|of
argument_list|(
literal|"a"
argument_list|)
decl_stmt|;
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|copy
init|=
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|sortedSet
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|sortedSet
argument_list|,
name|copy
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// GWT is single threaded
DECL|method|testCopyOf_threadSafe ()
specifier|public
name|void
name|testCopyOf_threadSafe
parameter_list|()
block|{
name|verifyThreadSafe
argument_list|()
expr_stmt|;
block|}
DECL|method|builder ()
annotation|@
name|Override
argument_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|E
argument_list|>
argument_list|>
name|Builder
argument_list|<
name|E
argument_list|>
name|builder
parameter_list|()
block|{
return|return
name|ImmutableSet
operator|.
name|builder
argument_list|()
return|;
block|}
DECL|method|getComplexBuilderSetLastElement ()
annotation|@
name|Override
name|int
name|getComplexBuilderSetLastElement
parameter_list|()
block|{
return|return
name|LAST_COLOR_ADDED
return|;
block|}
DECL|method|testEquals ()
specifier|public
name|void
name|testEquals
parameter_list|()
block|{
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|()
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|()
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|1
argument_list|)
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|1
argument_list|)
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|1
argument_list|)
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

