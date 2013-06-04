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
name|collect
operator|.
name|Multiset
operator|.
name|Entry
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
name|UnmodifiableCollectionTests
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
comment|/**  * Common tests for a {@link Multiset}.  *  * @author Kevin Bourrillion  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|AbstractMultisetTest
specifier|public
specifier|abstract
class|class
name|AbstractMultisetTest
extends|extends
name|AbstractCollectionTest
block|{
DECL|method|create ()
annotation|@
name|Override
specifier|protected
specifier|abstract
parameter_list|<
name|E
parameter_list|>
name|Multiset
argument_list|<
name|E
argument_list|>
name|create
parameter_list|()
function_decl|;
DECL|field|ms
specifier|protected
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms
decl_stmt|;
comment|// public for GWT
DECL|method|setUp ()
annotation|@
name|Override
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|c
operator|=
name|ms
operator|=
name|create
argument_list|()
expr_stmt|;
block|}
comment|/**    * Validates that multiset size returned by {@code size()} is the same as the    * size generated by summing the counts of all multiset entries.    */
DECL|method|assertSize (Multiset<String> multiset)
specifier|protected
name|void
name|assertSize
parameter_list|(
name|Multiset
argument_list|<
name|String
argument_list|>
name|multiset
parameter_list|)
block|{
name|long
name|size
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Multiset
operator|.
name|Entry
argument_list|<
name|String
argument_list|>
name|entry
range|:
name|multiset
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|size
operator|+=
name|entry
operator|.
name|getCount
argument_list|()
expr_stmt|;
block|}
name|assertEquals
argument_list|(
operator|(
name|int
operator|)
name|Math
operator|.
name|min
argument_list|(
name|size
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
argument_list|,
name|multiset
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertSize ()
specifier|protected
name|void
name|assertSize
parameter_list|()
block|{
name|assertSize
argument_list|(
name|ms
argument_list|)
expr_stmt|;
block|}
DECL|method|assertContents (String... expected)
annotation|@
name|Override
specifier|protected
name|void
name|assertContents
parameter_list|(
name|String
modifier|...
name|expected
parameter_list|)
block|{
name|super
operator|.
name|assertContents
argument_list|(
name|expected
argument_list|)
expr_stmt|;
name|assertSize
argument_list|()
expr_stmt|;
block|}
DECL|class|WrongType
specifier|static
class|class
name|WrongType
block|{}
DECL|method|testEqualsNo ()
annotation|@
name|Override
specifier|public
name|void
name|testEqualsNo
parameter_list|()
block|{
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
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms2
init|=
name|create
argument_list|()
decl_stmt|;
name|ms2
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|ms2
operator|.
name|add
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ms
operator|.
name|equals
argument_list|(
name|ms2
argument_list|)
argument_list|)
expr_stmt|;
name|assertSize
argument_list|()
expr_stmt|;
block|}
DECL|method|testElementSetIsNotACopy ()
specifier|public
name|void
name|testElementSetIsNotACopy
parameter_list|()
block|{
name|ms
operator|.
name|add
argument_list|(
literal|"a"
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
name|Set
argument_list|<
name|String
argument_list|>
name|elementSet
init|=
name|ms
operator|.
name|elementSet
argument_list|()
decl_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"c"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|ms
operator|.
name|setCount
argument_list|(
literal|"b"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"a"
argument_list|,
literal|"c"
argument_list|)
argument_list|,
name|elementSet
argument_list|)
expr_stmt|;
name|assertSize
argument_list|()
expr_stmt|;
block|}
DECL|method|testRemoveFromElementSetYes ()
specifier|public
name|void
name|testRemoveFromElementSetYes
parameter_list|()
block|{
name|ms
operator|.
name|add
argument_list|(
literal|"a"
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
name|Set
argument_list|<
name|String
argument_list|>
name|elementSet
init|=
name|ms
operator|.
name|elementSet
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|elementSet
operator|.
name|remove
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
block|}
DECL|method|testRemoveFromElementSetNo ()
specifier|public
name|void
name|testRemoveFromElementSetNo
parameter_list|()
block|{
name|ms
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|elementSet
init|=
name|ms
operator|.
name|elementSet
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|elementSet
operator|.
name|remove
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
block|}
DECL|method|testClearViaElementSet ()
specifier|public
name|void
name|testClearViaElementSet
parameter_list|()
block|{
name|ms
operator|=
name|createSample
argument_list|()
expr_stmt|;
name|ms
operator|.
name|elementSet
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertContents
argument_list|()
expr_stmt|;
block|}
DECL|method|testClearViaEntrySet ()
specifier|public
name|void
name|testClearViaEntrySet
parameter_list|()
block|{
name|ms
operator|=
name|createSample
argument_list|()
expr_stmt|;
name|ms
operator|.
name|entrySet
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertContents
argument_list|()
expr_stmt|;
block|}
DECL|method|testReallyBig ()
specifier|public
name|void
name|testReallyBig
parameter_list|()
block|{
name|ms
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
operator|-
literal|1
argument_list|,
name|ms
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"b"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
comment|// See Collection.size() contract
name|assertEquals
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
name|ms
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Make sure we didn't forget our size
name|ms
operator|.
name|remove
argument_list|(
literal|"a"
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
operator|-
literal|2
argument_list|,
name|ms
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSize
argument_list|()
expr_stmt|;
block|}
DECL|method|testToStringNull ()
specifier|public
name|void
name|testToStringNull
parameter_list|()
block|{
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
name|ms
operator|.
name|add
argument_list|(
literal|null
argument_list|,
literal|4
argument_list|)
expr_stmt|;
comment|// This test is brittle. The original test was meant to validate the
comment|// contents of the string itself, but key ordering tended to change
comment|// under unpredictable circumstances. Instead, we're just ensuring
comment|// that the string not return null, and implicitly, not throw an exception.
name|assertNotNull
argument_list|(
name|ms
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertSize
argument_list|()
expr_stmt|;
block|}
DECL|method|testEntryAfterRemove ()
specifier|public
name|void
name|testEntryAfterRemove
parameter_list|()
block|{
name|ms
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|8
argument_list|)
expr_stmt|;
name|Multiset
operator|.
name|Entry
argument_list|<
name|String
argument_list|>
name|entry
init|=
name|ms
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|ms
operator|.
name|remove
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|7
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|ms
operator|.
name|remove
argument_list|(
literal|"a"
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|ms
operator|.
name|elementSet
argument_list|()
operator|.
name|remove
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testEntryAfterClear ()
specifier|public
name|void
name|testEntryAfterClear
parameter_list|()
block|{
name|ms
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|Multiset
operator|.
name|Entry
argument_list|<
name|String
argument_list|>
name|entry
init|=
name|ms
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|ms
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testEntryAfterEntrySetClear ()
specifier|public
name|void
name|testEntryAfterEntrySetClear
parameter_list|()
block|{
name|ms
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|Multiset
operator|.
name|Entry
argument_list|<
name|String
argument_list|>
name|entry
init|=
name|ms
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|ms
operator|.
name|entrySet
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testEntryAfterEntrySetIteratorRemove ()
specifier|public
name|void
name|testEntryAfterEntrySetIteratorRemove
parameter_list|()
block|{
name|ms
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|Multiset
operator|.
name|Entry
argument_list|<
name|String
argument_list|>
argument_list|>
name|iterator
init|=
name|ms
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Multiset
operator|.
name|Entry
argument_list|<
name|String
argument_list|>
name|entry
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|expected
parameter_list|)
block|{}
name|ms
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testEntryAfterElementSetIteratorRemove ()
specifier|public
name|void
name|testEntryAfterElementSetIteratorRemove
parameter_list|()
block|{
name|ms
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|Multiset
operator|.
name|Entry
argument_list|<
name|String
argument_list|>
name|entry
init|=
name|ms
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|iterator
init|=
name|ms
operator|.
name|elementSet
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
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testEntrySetRemove ()
specifier|public
name|void
name|testEntrySetRemove
parameter_list|()
block|{
name|ms
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|>
argument_list|>
name|es
init|=
name|ms
operator|.
name|entrySet
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|es
operator|.
name|remove
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|es
operator|.
name|remove
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|"a"
argument_list|,
literal|3
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|es
operator|.
name|remove
argument_list|(
name|Multisets
operator|.
name|immutableEntry
argument_list|(
literal|"a"
argument_list|,
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|es
operator|.
name|remove
argument_list|(
name|Multisets
operator|.
name|immutableEntry
argument_list|(
literal|"b"
argument_list|,
literal|3
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|es
operator|.
name|remove
argument_list|(
name|Multisets
operator|.
name|immutableEntry
argument_list|(
literal|"b"
argument_list|,
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|ms
operator|.
name|count
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|es
operator|.
name|remove
argument_list|(
name|Multisets
operator|.
name|immutableEntry
argument_list|(
literal|"a"
argument_list|,
literal|3
argument_list|)
argument_list|)
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
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testUnmodifiableMultiset ()
specifier|public
name|void
name|testUnmodifiableMultiset
parameter_list|()
block|{
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
literal|"b"
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"c"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|Multiset
argument_list|<
name|Object
argument_list|>
name|unmodifiable
init|=
name|Multisets
operator|.
expr|<
name|Object
operator|>
name|unmodifiableMultiset
argument_list|(
name|ms
argument_list|)
decl_stmt|;
name|UnmodifiableCollectionTests
operator|.
name|assertMultisetIsUnmodifiable
argument_list|(
name|unmodifiable
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
block|}
DECL|method|createSample ()
annotation|@
name|Override
specifier|protected
name|Multiset
argument_list|<
name|String
argument_list|>
name|createSample
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms
init|=
name|create
argument_list|()
decl_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"a"
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
literal|"d"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
return|return
name|ms
return|;
block|}
block|}
end_class

end_unit

