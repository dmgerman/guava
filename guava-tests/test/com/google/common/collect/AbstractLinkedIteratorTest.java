begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|collect
operator|.
name|testing
operator|.
name|IteratorFeature
operator|.
name|UNMODIFIABLE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|contrib
operator|.
name|truth
operator|.
name|Truth
operator|.
name|ASSERT
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
name|IteratorTester
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|AssertionFailedError
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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|NoSuchElementException
import|;
end_import

begin_comment
comment|/** Tests for {@link AbstractLinkedIterator}. */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|AbstractLinkedIteratorTest
specifier|public
class|class
name|AbstractLinkedIteratorTest
extends|extends
name|TestCase
block|{
annotation|@
name|GwtIncompatible
argument_list|(
literal|"Too slow"
argument_list|)
DECL|method|testDoublerExhaustive ()
specifier|public
name|void
name|testDoublerExhaustive
parameter_list|()
block|{
operator|new
name|IteratorTester
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|3
argument_list|,
name|UNMODIFIABLE
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|,
name|IteratorTester
operator|.
name|KnownOrder
operator|.
name|KNOWN_ORDER
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|newTargetIterator
parameter_list|()
block|{
return|return
name|newDoubler
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
return|;
block|}
block|}
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testDoubler ()
specifier|public
name|void
name|testDoubler
parameter_list|()
block|{
name|Iterable
argument_list|<
name|Integer
argument_list|>
name|doubled
init|=
operator|new
name|Iterable
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|newDoubler
argument_list|(
literal|2
argument_list|,
literal|32
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|doubled
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|2
argument_list|,
literal|4
argument_list|,
literal|8
argument_list|,
literal|16
argument_list|,
literal|32
argument_list|)
expr_stmt|;
block|}
DECL|method|testEmpty ()
specifier|public
name|void
name|testEmpty
parameter_list|()
block|{
name|Iterator
argument_list|<
name|Object
argument_list|>
name|empty
init|=
name|newEmpty
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|empty
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|empty
operator|.
name|next
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|expected
parameter_list|)
block|{     }
try|try
block|{
name|empty
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
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testBroken ()
specifier|public
name|void
name|testBroken
parameter_list|()
block|{
name|Iterator
argument_list|<
name|Object
argument_list|>
name|broken
init|=
name|newBroken
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|broken
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
comment|// We can't retrieve even the known first element:
try|try
block|{
name|broken
operator|.
name|next
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MyException
name|expected
parameter_list|)
block|{     }
try|try
block|{
name|broken
operator|.
name|next
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MyException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|newDoubler (int first, final int last)
specifier|private
specifier|static
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|newDoubler
parameter_list|(
name|int
name|first
parameter_list|,
specifier|final
name|int
name|last
parameter_list|)
block|{
return|return
operator|new
name|AbstractLinkedIterator
argument_list|<
name|Integer
argument_list|>
argument_list|(
name|first
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|Integer
name|computeNext
parameter_list|(
name|Integer
name|previous
parameter_list|)
block|{
return|return
operator|(
name|previous
operator|==
name|last
operator|)
condition|?
literal|null
else|:
name|previous
operator|*
literal|2
return|;
block|}
block|}
return|;
block|}
DECL|method|newEmpty ()
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Iterator
argument_list|<
name|T
argument_list|>
name|newEmpty
parameter_list|()
block|{
return|return
operator|new
name|AbstractLinkedIterator
argument_list|<
name|T
argument_list|>
argument_list|(
literal|null
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|T
name|computeNext
parameter_list|(
name|T
name|previous
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionFailedError
argument_list|()
throw|;
block|}
block|}
return|;
block|}
DECL|method|newBroken ()
specifier|private
specifier|static
name|Iterator
argument_list|<
name|Object
argument_list|>
name|newBroken
parameter_list|()
block|{
return|return
operator|new
name|AbstractLinkedIterator
argument_list|<
name|Object
argument_list|>
argument_list|(
literal|"UNUSED"
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|Object
name|computeNext
parameter_list|(
name|Object
name|previous
parameter_list|)
block|{
throw|throw
operator|new
name|MyException
argument_list|()
throw|;
block|}
block|}
return|;
block|}
DECL|class|MyException
specifier|private
specifier|static
class|class
name|MyException
extends|extends
name|RuntimeException
block|{}
block|}
end_class

end_unit

