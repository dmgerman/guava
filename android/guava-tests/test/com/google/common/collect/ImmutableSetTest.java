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
name|ImmutableSetSizedBuilderGenerator
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
name|ImmutableSetTooBigBuilderGenerator
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
name|ImmutableSetTooSmallBuilderGenerator
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
name|ImmutableSetUnsizedBuilderGenerator
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
name|ImmutableSetUnsizedBuilderGenerator
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
literal|", with unsized builder"
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
name|ImmutableSetSizedBuilderGenerator
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
literal|", with exactly sized builder"
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
name|ImmutableSetTooBigBuilderGenerator
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
literal|", with oversized builder"
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
name|ImmutableSetTooSmallBuilderGenerator
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
literal|", with undersized builder"
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
annotation|@
name|Override
DECL|method|of ()
specifier|protected
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|E
argument_list|>
parameter_list|>
name|Set
argument_list|<
name|E
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
annotation|@
name|Override
DECL|method|of (E e)
specifier|protected
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|E
argument_list|>
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|of
parameter_list|(
name|E
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
annotation|@
name|Override
DECL|method|of (E e1, E e2)
specifier|protected
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|E
argument_list|>
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|of
parameter_list|(
name|E
name|e1
parameter_list|,
name|E
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
annotation|@
name|Override
DECL|method|of (E e1, E e2, E e3)
specifier|protected
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|E
argument_list|>
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|of
parameter_list|(
name|E
name|e1
parameter_list|,
name|E
name|e2
parameter_list|,
name|E
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
annotation|@
name|Override
DECL|method|of (E e1, E e2, E e3, E e4)
specifier|protected
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|E
argument_list|>
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|of
parameter_list|(
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
annotation|@
name|Override
DECL|method|of (E e1, E e2, E e3, E e4, E e5)
specifier|protected
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|E
argument_list|>
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|of
parameter_list|(
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
parameter_list|,
name|E
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|of ( E e1, E e2, E e3, E e4, E e5, E e6, E... rest)
specifier|protected
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|E
argument_list|>
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|of
parameter_list|(
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
parameter_list|,
name|E
name|e5
parameter_list|,
name|E
name|e6
parameter_list|,
name|E
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
annotation|@
name|Override
DECL|method|copyOf (E[] elements)
specifier|protected
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|E
argument_list|>
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|copyOf
parameter_list|(
name|E
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
annotation|@
name|Override
DECL|method|copyOf (Collection<? extends E> elements)
specifier|protected
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|E
argument_list|>
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|copyOf
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
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
annotation|@
name|Override
DECL|method|copyOf (Iterable<? extends E> elements)
specifier|protected
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|E
argument_list|>
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|copyOf
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|E
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
annotation|@
name|Override
DECL|method|copyOf (Iterator<? extends E> elements)
specifier|protected
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|E
argument_list|>
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|copyOf
parameter_list|(
name|Iterator
argument_list|<
name|?
extends|extends
name|E
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
annotation|@
name|GwtIncompatible
argument_list|(
literal|"Builder impl"
argument_list|)
DECL|method|testBuilderForceCopy ()
specifier|public
name|void
name|testBuilderForceCopy
parameter_list|()
block|{
name|ImmutableSet
operator|.
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
name|builder
operator|.
name|add
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|Object
index|[]
name|prevArray
init|=
literal|null
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
literal|10
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
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|builder
operator|.
name|contents
argument_list|,
name|prevArray
argument_list|)
expr_stmt|;
name|prevArray
operator|=
name|builder
operator|.
name|contents
expr_stmt|;
name|ImmutableSet
argument_list|<
name|Integer
argument_list|>
name|unused
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
block|}
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"Builder impl"
argument_list|)
DECL|method|testPresizedBuilderDedups ()
specifier|public
name|void
name|testPresizedBuilderDedups
parameter_list|()
block|{
name|ImmutableSet
operator|.
name|Builder
argument_list|<
name|String
argument_list|>
name|builder
init|=
name|ImmutableSet
operator|.
name|builderWithExpectedSize
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|builder
operator|.
name|add
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|builder
operator|.
name|size
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|builder
operator|.
name|size
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
literal|"b"
argument_list|,
literal|"c"
argument_list|,
literal|"d"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|builder
operator|.
name|size
argument_list|)
expr_stmt|;
name|Object
index|[]
name|table
init|=
name|builder
operator|.
name|hashTable
decl_stmt|;
name|assertNotNull
argument_list|(
name|table
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|table
argument_list|,
operator|(
operator|(
name|RegularImmutableSet
argument_list|<
name|String
argument_list|>
operator|)
name|builder
operator|.
name|build
argument_list|()
operator|)
operator|.
name|table
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"Builder impl"
argument_list|)
DECL|method|testPresizedBuilderForceCopy ()
specifier|public
name|void
name|testPresizedBuilderForceCopy
parameter_list|()
block|{
for|for
control|(
name|int
name|expectedSize
init|=
literal|1
init|;
name|expectedSize
operator|<
literal|4
condition|;
name|expectedSize
operator|++
control|)
block|{
name|ImmutableSet
operator|.
name|Builder
argument_list|<
name|Integer
argument_list|>
name|builder
init|=
name|ImmutableSet
operator|.
name|builderWithExpectedSize
argument_list|(
name|expectedSize
argument_list|)
decl_stmt|;
name|builder
operator|.
name|add
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|Object
index|[]
name|prevArray
init|=
literal|null
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
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|ImmutableSet
argument_list|<
name|Integer
argument_list|>
name|prevBuilt
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|prevBuilt
operator|.
name|contains
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|builder
operator|.
name|contents
argument_list|,
name|prevArray
argument_list|)
expr_stmt|;
name|prevArray
operator|=
name|builder
operator|.
name|contents
expr_stmt|;
block|}
block|}
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
operator|(
literal|1
operator|<<
literal|29
operator|)
operator|*
literal|3
operator|/
literal|5
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
operator|(
literal|1
operator|<<
literal|30
operator|)
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
literal|60
argument_list|,
literal|60
argument_list|,
literal|128
argument_list|)
expr_stmt|;
name|verifyTableSize
argument_list|(
literal|120
argument_list|,
literal|60
argument_list|,
literal|256
argument_list|)
expr_stmt|;
comment|// if the table is only double the necessary size, we don't bother resizing it
name|verifyTableSize
argument_list|(
literal|180
argument_list|,
literal|60
argument_list|,
literal|128
argument_list|)
expr_stmt|;
comment|// but if it's even bigger than double, we rebuild the table
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
comment|// TODO(b/172823566): Use mainline testToImmutableSet once CollectorTester is usable to java7.
DECL|method|testToImmutableSet_java7 ()
specifier|public
name|void
name|testToImmutableSet_java7
parameter_list|()
block|{
name|ImmutableSet
operator|.
name|Builder
argument_list|<
name|String
argument_list|>
name|zis
init|=
name|ImmutableSet
operator|.
expr|<
name|String
operator|>
name|builder
argument_list|()
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
decl_stmt|;
name|ImmutableSet
operator|.
name|Builder
argument_list|<
name|String
argument_list|>
name|zat
init|=
name|ImmutableSet
operator|.
expr|<
name|String
operator|>
name|builder
argument_list|()
operator|.
name|add
argument_list|(
literal|"c"
argument_list|,
literal|"b"
argument_list|,
literal|"d"
argument_list|,
literal|"c"
argument_list|)
decl_stmt|;
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|set
init|=
name|zis
operator|.
name|combine
argument_list|(
name|zat
argument_list|)
operator|.
name|build
argument_list|()
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
argument_list|,
literal|"d"
argument_list|)
operator|.
name|inOrder
argument_list|()
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
annotation|@
name|Override
DECL|method|builder ()
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
annotation|@
name|Override
DECL|method|getComplexBuilderSetLastElement ()
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
annotation|@
name|GwtIncompatible
argument_list|(
literal|"internals"
argument_list|)
DECL|method|testControlsArraySize ()
specifier|public
name|void
name|testControlsArraySize
parameter_list|()
block|{
name|ImmutableSet
operator|.
name|Builder
argument_list|<
name|String
argument_list|>
name|builder
init|=
operator|new
name|ImmutableSet
operator|.
name|Builder
argument_list|<
name|String
argument_list|>
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
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|builder
operator|.
name|add
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|add
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|RegularImmutableSet
argument_list|<
name|String
argument_list|>
name|set
init|=
operator|(
name|RegularImmutableSet
argument_list|<
name|String
argument_list|>
operator|)
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|set
operator|.
name|elements
operator|.
name|length
operator|<=
literal|2
operator|*
name|set
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"internals"
argument_list|)
DECL|method|testReusedBuilder ()
specifier|public
name|void
name|testReusedBuilder
parameter_list|()
block|{
name|ImmutableSet
operator|.
name|Builder
argument_list|<
name|String
argument_list|>
name|builder
init|=
operator|new
name|ImmutableSet
operator|.
name|Builder
argument_list|<
name|String
argument_list|>
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
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|builder
operator|.
name|add
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|add
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|RegularImmutableSet
argument_list|<
name|String
argument_list|>
name|set
init|=
operator|(
name|RegularImmutableSet
argument_list|<
name|String
argument_list|>
operator|)
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
name|builder
operator|.
name|add
argument_list|(
literal|"baz"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|set
operator|.
name|elements
operator|!=
name|builder
operator|.
name|contents
argument_list|)
expr_stmt|;
block|}
DECL|method|testReuseBuilderReducingHashTableSizeWithPowerOfTwoTotalElements ()
specifier|public
name|void
name|testReuseBuilderReducingHashTableSizeWithPowerOfTwoTotalElements
parameter_list|()
block|{
name|ImmutableSet
operator|.
name|Builder
argument_list|<
name|Object
argument_list|>
name|builder
init|=
name|ImmutableSet
operator|.
name|builderWithExpectedSize
argument_list|(
literal|6
argument_list|)
decl_stmt|;
name|builder
operator|.
name|add
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|ImmutableSet
argument_list|<
name|Object
argument_list|>
name|unused
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
name|ImmutableSet
argument_list|<
name|Object
argument_list|>
name|subject
init|=
name|builder
operator|.
name|add
argument_list|(
literal|1
argument_list|)
operator|.
name|add
argument_list|(
literal|2
argument_list|)
operator|.
name|add
argument_list|(
literal|3
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|subject
operator|.
name|contains
argument_list|(
literal|4
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

