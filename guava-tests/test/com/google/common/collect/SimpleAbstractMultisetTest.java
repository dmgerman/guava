begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
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
name|io
operator|.
name|Serializable
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
comment|/**  * Unit test for {@link AbstractMultiset}.  *  * @author Kevin Bourrillion  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
comment|// No serialization is used in this test
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|SimpleAbstractMultisetTest
specifier|public
class|class
name|SimpleAbstractMultisetTest
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
name|addTestSuite
argument_list|(
name|SimpleAbstractMultisetTest
operator|.
name|class
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|MultisetTestSuiteBuilder
operator|.
name|using
argument_list|(
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
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms
init|=
operator|new
name|NoRemoveMultiset
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|Collections
operator|.
name|addAll
argument_list|(
name|ms
argument_list|,
name|elements
argument_list|)
expr_stmt|;
return|return
name|ms
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"NoRemoveMultiset"
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
name|ALLOWS_NULL_VALUES
argument_list|,
name|CollectionFeature
operator|.
name|SUPPORTS_ADD
argument_list|)
operator|.
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|suite
return|;
block|}
DECL|method|testFastAddAllMultiset ()
specifier|public
name|void
name|testFastAddAllMultiset
parameter_list|()
block|{
specifier|final
name|AtomicInteger
name|addCalls
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|multiset
init|=
operator|new
name|NoRemoveMultiset
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|add
parameter_list|(
name|String
name|element
parameter_list|,
name|int
name|occurrences
parameter_list|)
block|{
name|addCalls
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
return|return
name|super
operator|.
name|add
argument_list|(
name|element
argument_list|,
name|occurrences
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|ImmutableMultiset
argument_list|<
name|String
argument_list|>
name|adds
init|=
operator|new
name|ImmutableMultiset
operator|.
name|Builder
argument_list|<
name|String
argument_list|>
argument_list|()
operator|.
name|addCopies
argument_list|(
literal|"x"
argument_list|,
literal|10
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|multiset
operator|.
name|addAll
argument_list|(
name|adds
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|addCalls
operator|.
name|get
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|testRemoveUnsupported ()
specifier|public
name|void
name|testRemoveUnsupported
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|multiset
init|=
operator|new
name|NoRemoveMultiset
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|multiset
operator|.
name|add
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
try|try
block|{
name|multiset
operator|.
name|remove
argument_list|(
literal|"a"
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
block|{}
name|assertTrue
argument_list|(
name|multiset
operator|.
name|contains
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|class|NoRemoveMultiset
specifier|private
specifier|static
class|class
name|NoRemoveMultiset
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractMultiset
argument_list|<
name|E
argument_list|>
implements|implements
name|Serializable
block|{
DECL|field|backingMap
specifier|final
name|Map
argument_list|<
name|E
argument_list|,
name|Integer
argument_list|>
name|backingMap
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
DECL|method|add (@ullable E element, int occurrences)
annotation|@
name|Override
specifier|public
name|int
name|add
parameter_list|(
annotation|@
name|Nullable
name|E
name|element
parameter_list|,
name|int
name|occurrences
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|occurrences
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|Integer
name|frequency
init|=
name|backingMap
operator|.
name|get
argument_list|(
name|element
argument_list|)
decl_stmt|;
if|if
condition|(
name|frequency
operator|==
literal|null
condition|)
block|{
name|frequency
operator|=
literal|0
expr_stmt|;
block|}
if|if
condition|(
name|occurrences
operator|==
literal|0
condition|)
block|{
return|return
name|frequency
return|;
block|}
name|checkArgument
argument_list|(
name|occurrences
operator|<=
name|Integer
operator|.
name|MAX_VALUE
operator|-
name|frequency
argument_list|)
expr_stmt|;
name|backingMap
operator|.
name|put
argument_list|(
name|element
argument_list|,
name|frequency
operator|+
name|occurrences
argument_list|)
expr_stmt|;
return|return
name|frequency
return|;
block|}
annotation|@
name|Override
DECL|method|entryIterator ()
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|entryIterator
parameter_list|()
block|{
specifier|final
name|Iterator
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
name|backingEntries
init|=
name|backingMap
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
return|return
operator|new
name|UnmodifiableIterator
argument_list|<
name|Multiset
operator|.
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
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|backingEntries
operator|.
name|hasNext
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
name|next
parameter_list|()
block|{
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|E
argument_list|,
name|Integer
argument_list|>
name|mapEntry
init|=
name|backingEntries
operator|.
name|next
argument_list|()
decl_stmt|;
return|return
operator|new
name|Multisets
operator|.
name|AbstractEntry
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|E
name|getElement
parameter_list|()
block|{
return|return
name|mapEntry
operator|.
name|getKey
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getCount
parameter_list|()
block|{
name|Integer
name|frequency
init|=
name|backingMap
operator|.
name|get
argument_list|(
name|getElement
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|(
name|frequency
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|frequency
return|;
block|}
block|}
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|distinctElements ()
name|int
name|distinctElements
parameter_list|()
block|{
return|return
name|backingMap
operator|.
name|size
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

