begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.base
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
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
name|base
operator|.
name|Joiner
operator|.
name|MapJoiner
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
name|ImmutableMap
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
name|ImmutableMultimap
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
name|Lists
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
name|Maps
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
name|io
operator|.
name|IOException
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

begin_comment
comment|/**  * Unit test for {@link Joiner}.  *  * @author Kevin Bourrillion  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|JoinerTest
specifier|public
class|class
name|JoinerTest
extends|extends
name|TestCase
block|{
DECL|field|J
specifier|private
specifier|static
specifier|final
name|Joiner
name|J
init|=
name|Joiner
operator|.
name|on
argument_list|(
literal|"-"
argument_list|)
decl_stmt|;
comment|//<Integer> needed to prevent warning :(
DECL|field|ITERABLE_
specifier|private
specifier|static
specifier|final
name|Iterable
argument_list|<
name|Integer
argument_list|>
name|ITERABLE_
init|=
name|Arrays
operator|.
expr|<
name|Integer
operator|>
name|asList
argument_list|()
decl_stmt|;
DECL|field|ITERABLE_1
specifier|private
specifier|static
specifier|final
name|Iterable
argument_list|<
name|Integer
argument_list|>
name|ITERABLE_1
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|ITERABLE_12
specifier|private
specifier|static
specifier|final
name|Iterable
argument_list|<
name|Integer
argument_list|>
name|ITERABLE_12
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
decl_stmt|;
DECL|field|ITERABLE_123
specifier|private
specifier|static
specifier|final
name|Iterable
argument_list|<
name|Integer
argument_list|>
name|ITERABLE_123
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
decl_stmt|;
DECL|field|ITERABLE_NULL
specifier|private
specifier|static
specifier|final
name|Iterable
argument_list|<
name|Integer
argument_list|>
name|ITERABLE_NULL
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|(
name|Integer
operator|)
literal|null
argument_list|)
decl_stmt|;
DECL|field|ITERABLE_NULL_NULL
specifier|private
specifier|static
specifier|final
name|Iterable
argument_list|<
name|Integer
argument_list|>
name|ITERABLE_NULL_NULL
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|(
name|Integer
operator|)
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
DECL|field|ITERABLE_NULL_1
specifier|private
specifier|static
specifier|final
name|Iterable
argument_list|<
name|Integer
argument_list|>
name|ITERABLE_NULL_1
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|null
argument_list|,
literal|1
argument_list|)
decl_stmt|;
DECL|field|ITERABLE_1_NULL
specifier|private
specifier|static
specifier|final
name|Iterable
argument_list|<
name|Integer
argument_list|>
name|ITERABLE_1_NULL
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|1
argument_list|,
literal|null
argument_list|)
decl_stmt|;
DECL|field|ITERABLE_1_NULL_2
specifier|private
specifier|static
specifier|final
name|Iterable
argument_list|<
name|Integer
argument_list|>
name|ITERABLE_1_NULL_2
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|1
argument_list|,
literal|null
argument_list|,
literal|2
argument_list|)
decl_stmt|;
DECL|field|ITERABLE_FOUR_NULLS
specifier|private
specifier|static
specifier|final
name|Iterable
argument_list|<
name|Integer
argument_list|>
name|ITERABLE_FOUR_NULLS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|(
name|Integer
operator|)
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
DECL|method|testNoSpecialNullBehavior ()
specifier|public
name|void
name|testNoSpecialNullBehavior
parameter_list|()
block|{
name|checkNoOutput
argument_list|(
name|J
argument_list|,
name|ITERABLE_
argument_list|)
expr_stmt|;
name|checkResult
argument_list|(
name|J
argument_list|,
name|ITERABLE_1
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|checkResult
argument_list|(
name|J
argument_list|,
name|ITERABLE_12
argument_list|,
literal|"1-2"
argument_list|)
expr_stmt|;
name|checkResult
argument_list|(
name|J
argument_list|,
name|ITERABLE_123
argument_list|,
literal|"1-2-3"
argument_list|)
expr_stmt|;
try|try
block|{
name|J
operator|.
name|join
argument_list|(
name|ITERABLE_NULL
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{     }
try|try
block|{
name|J
operator|.
name|join
argument_list|(
name|ITERABLE_1_NULL_2
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{     }
try|try
block|{
name|J
operator|.
name|join
argument_list|(
name|ITERABLE_NULL
operator|.
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{     }
try|try
block|{
name|J
operator|.
name|join
argument_list|(
name|ITERABLE_1_NULL_2
operator|.
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testOnCharOverride ()
specifier|public
name|void
name|testOnCharOverride
parameter_list|()
block|{
name|Joiner
name|onChar
init|=
name|Joiner
operator|.
name|on
argument_list|(
literal|'-'
argument_list|)
decl_stmt|;
name|checkNoOutput
argument_list|(
name|onChar
argument_list|,
name|ITERABLE_
argument_list|)
expr_stmt|;
name|checkResult
argument_list|(
name|onChar
argument_list|,
name|ITERABLE_1
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|checkResult
argument_list|(
name|onChar
argument_list|,
name|ITERABLE_12
argument_list|,
literal|"1-2"
argument_list|)
expr_stmt|;
name|checkResult
argument_list|(
name|onChar
argument_list|,
name|ITERABLE_123
argument_list|,
literal|"1-2-3"
argument_list|)
expr_stmt|;
block|}
DECL|method|testSkipNulls ()
specifier|public
name|void
name|testSkipNulls
parameter_list|()
block|{
name|Joiner
name|skipNulls
init|=
name|J
operator|.
name|skipNulls
argument_list|()
decl_stmt|;
name|checkNoOutput
argument_list|(
name|skipNulls
argument_list|,
name|ITERABLE_
argument_list|)
expr_stmt|;
name|checkNoOutput
argument_list|(
name|skipNulls
argument_list|,
name|ITERABLE_NULL
argument_list|)
expr_stmt|;
name|checkNoOutput
argument_list|(
name|skipNulls
argument_list|,
name|ITERABLE_NULL_NULL
argument_list|)
expr_stmt|;
name|checkNoOutput
argument_list|(
name|skipNulls
argument_list|,
name|ITERABLE_FOUR_NULLS
argument_list|)
expr_stmt|;
name|checkResult
argument_list|(
name|skipNulls
argument_list|,
name|ITERABLE_1
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|checkResult
argument_list|(
name|skipNulls
argument_list|,
name|ITERABLE_12
argument_list|,
literal|"1-2"
argument_list|)
expr_stmt|;
name|checkResult
argument_list|(
name|skipNulls
argument_list|,
name|ITERABLE_123
argument_list|,
literal|"1-2-3"
argument_list|)
expr_stmt|;
name|checkResult
argument_list|(
name|skipNulls
argument_list|,
name|ITERABLE_NULL_1
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|checkResult
argument_list|(
name|skipNulls
argument_list|,
name|ITERABLE_1_NULL
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|checkResult
argument_list|(
name|skipNulls
argument_list|,
name|ITERABLE_1_NULL_2
argument_list|,
literal|"1-2"
argument_list|)
expr_stmt|;
block|}
DECL|method|testUseForNull ()
specifier|public
name|void
name|testUseForNull
parameter_list|()
block|{
name|Joiner
name|zeroForNull
init|=
name|J
operator|.
name|useForNull
argument_list|(
literal|"0"
argument_list|)
decl_stmt|;
name|checkNoOutput
argument_list|(
name|zeroForNull
argument_list|,
name|ITERABLE_
argument_list|)
expr_stmt|;
name|checkResult
argument_list|(
name|zeroForNull
argument_list|,
name|ITERABLE_1
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|checkResult
argument_list|(
name|zeroForNull
argument_list|,
name|ITERABLE_12
argument_list|,
literal|"1-2"
argument_list|)
expr_stmt|;
name|checkResult
argument_list|(
name|zeroForNull
argument_list|,
name|ITERABLE_123
argument_list|,
literal|"1-2-3"
argument_list|)
expr_stmt|;
name|checkResult
argument_list|(
name|zeroForNull
argument_list|,
name|ITERABLE_NULL
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
name|checkResult
argument_list|(
name|zeroForNull
argument_list|,
name|ITERABLE_NULL_NULL
argument_list|,
literal|"0-0"
argument_list|)
expr_stmt|;
name|checkResult
argument_list|(
name|zeroForNull
argument_list|,
name|ITERABLE_NULL_1
argument_list|,
literal|"0-1"
argument_list|)
expr_stmt|;
name|checkResult
argument_list|(
name|zeroForNull
argument_list|,
name|ITERABLE_1_NULL
argument_list|,
literal|"1-0"
argument_list|)
expr_stmt|;
name|checkResult
argument_list|(
name|zeroForNull
argument_list|,
name|ITERABLE_1_NULL_2
argument_list|,
literal|"1-0-2"
argument_list|)
expr_stmt|;
name|checkResult
argument_list|(
name|zeroForNull
argument_list|,
name|ITERABLE_FOUR_NULLS
argument_list|,
literal|"0-0-0-0"
argument_list|)
expr_stmt|;
block|}
DECL|method|checkNoOutput (Joiner joiner, Iterable<Integer> set)
specifier|private
specifier|static
name|void
name|checkNoOutput
parameter_list|(
name|Joiner
name|joiner
parameter_list|,
name|Iterable
argument_list|<
name|Integer
argument_list|>
name|set
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|joiner
operator|.
name|join
argument_list|(
name|set
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|joiner
operator|.
name|join
argument_list|(
name|set
operator|.
name|iterator
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Object
index|[]
name|array
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|set
argument_list|)
operator|.
name|toArray
argument_list|(
operator|new
name|Integer
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|joiner
operator|.
name|join
argument_list|(
name|array
argument_list|)
argument_list|)
expr_stmt|;
name|StringBuilder
name|sb1FromIterable
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|sb1FromIterable
argument_list|,
name|joiner
operator|.
name|appendTo
argument_list|(
name|sb1FromIterable
argument_list|,
name|set
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|sb1FromIterable
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|StringBuilder
name|sb1FromIterator
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|sb1FromIterator
argument_list|,
name|joiner
operator|.
name|appendTo
argument_list|(
name|sb1FromIterator
argument_list|,
name|set
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|sb1FromIterator
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|StringBuilder
name|sb2
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|sb2
argument_list|,
name|joiner
operator|.
name|appendTo
argument_list|(
name|sb2
argument_list|,
name|array
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|sb2
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|joiner
operator|.
name|appendTo
argument_list|(
name|NASTY_APPENDABLE
argument_list|,
name|set
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|e
argument_list|)
throw|;
block|}
try|try
block|{
name|joiner
operator|.
name|appendTo
argument_list|(
name|NASTY_APPENDABLE
argument_list|,
name|set
operator|.
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|e
argument_list|)
throw|;
block|}
try|try
block|{
name|joiner
operator|.
name|appendTo
argument_list|(
name|NASTY_APPENDABLE
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|field|NASTY_APPENDABLE
specifier|private
specifier|static
specifier|final
name|Appendable
name|NASTY_APPENDABLE
init|=
operator|new
name|Appendable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Appendable
name|append
parameter_list|(
name|CharSequence
name|csq
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|IOException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Appendable
name|append
parameter_list|(
name|CharSequence
name|csq
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|end
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|IOException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Appendable
name|append
parameter_list|(
name|char
name|c
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|IOException
argument_list|()
throw|;
block|}
block|}
decl_stmt|;
DECL|method|checkResult (Joiner joiner, Iterable<Integer> parts, String expected)
specifier|private
specifier|static
name|void
name|checkResult
parameter_list|(
name|Joiner
name|joiner
parameter_list|,
name|Iterable
argument_list|<
name|Integer
argument_list|>
name|parts
parameter_list|,
name|String
name|expected
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|joiner
operator|.
name|join
argument_list|(
name|parts
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|joiner
operator|.
name|join
argument_list|(
name|parts
operator|.
name|iterator
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|StringBuilder
name|sb1FromIterable
init|=
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
literal|'x'
argument_list|)
decl_stmt|;
name|joiner
operator|.
name|appendTo
argument_list|(
name|sb1FromIterable
argument_list|,
name|parts
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"x"
operator|+
name|expected
argument_list|,
name|sb1FromIterable
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|StringBuilder
name|sb1FromIterator
init|=
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
literal|'x'
argument_list|)
decl_stmt|;
name|joiner
operator|.
name|appendTo
argument_list|(
name|sb1FromIterator
argument_list|,
name|parts
operator|.
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"x"
operator|+
name|expected
argument_list|,
name|sb1FromIterator
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|Integer
index|[]
name|partsArray
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|parts
argument_list|)
operator|.
name|toArray
argument_list|(
operator|new
name|Integer
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|joiner
operator|.
name|join
argument_list|(
name|partsArray
argument_list|)
argument_list|)
expr_stmt|;
name|StringBuilder
name|sb2
init|=
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
literal|'x'
argument_list|)
decl_stmt|;
name|joiner
operator|.
name|appendTo
argument_list|(
name|sb2
argument_list|,
name|partsArray
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"x"
operator|+
name|expected
argument_list|,
name|sb2
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|num
init|=
name|partsArray
operator|.
name|length
operator|-
literal|2
decl_stmt|;
if|if
condition|(
name|num
operator|>=
literal|0
condition|)
block|{
name|Object
index|[]
name|rest
init|=
operator|new
name|Integer
index|[
name|num
index|]
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
name|num
condition|;
name|i
operator|++
control|)
block|{
name|rest
index|[
name|i
index|]
operator|=
name|partsArray
index|[
name|i
operator|+
literal|2
index|]
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|joiner
operator|.
name|join
argument_list|(
name|partsArray
index|[
literal|0
index|]
argument_list|,
name|partsArray
index|[
literal|1
index|]
argument_list|,
name|rest
argument_list|)
argument_list|)
expr_stmt|;
name|StringBuilder
name|sb3
init|=
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
literal|'x'
argument_list|)
decl_stmt|;
name|joiner
operator|.
name|appendTo
argument_list|(
name|sb3
argument_list|,
name|partsArray
index|[
literal|0
index|]
argument_list|,
name|partsArray
index|[
literal|1
index|]
argument_list|,
name|rest
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"x"
operator|+
name|expected
argument_list|,
name|sb3
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|test_useForNull_skipNulls ()
specifier|public
name|void
name|test_useForNull_skipNulls
parameter_list|()
block|{
name|Joiner
name|j
init|=
name|Joiner
operator|.
name|on
argument_list|(
literal|"x"
argument_list|)
operator|.
name|useForNull
argument_list|(
literal|"y"
argument_list|)
decl_stmt|;
try|try
block|{
name|j
operator|=
name|j
operator|.
name|skipNulls
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
DECL|method|test_skipNulls_useForNull ()
specifier|public
name|void
name|test_skipNulls_useForNull
parameter_list|()
block|{
name|Joiner
name|j
init|=
name|Joiner
operator|.
name|on
argument_list|(
literal|"x"
argument_list|)
operator|.
name|skipNulls
argument_list|()
decl_stmt|;
try|try
block|{
name|j
operator|=
name|j
operator|.
name|useForNull
argument_list|(
literal|"y"
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
DECL|method|test_useForNull_twice ()
specifier|public
name|void
name|test_useForNull_twice
parameter_list|()
block|{
name|Joiner
name|j
init|=
name|Joiner
operator|.
name|on
argument_list|(
literal|"x"
argument_list|)
operator|.
name|useForNull
argument_list|(
literal|"y"
argument_list|)
decl_stmt|;
try|try
block|{
name|j
operator|=
name|j
operator|.
name|useForNull
argument_list|(
literal|"y"
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
DECL|method|testMap ()
specifier|public
name|void
name|testMap
parameter_list|()
block|{
name|MapJoiner
name|j
init|=
name|Joiner
operator|.
name|on
argument_list|(
literal|';'
argument_list|)
operator|.
name|withKeyValueSeparator
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|j
operator|.
name|join
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|":"
argument_list|,
name|j
operator|.
name|join
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mapWithNulls
init|=
name|Maps
operator|.
name|newLinkedHashMap
argument_list|()
decl_stmt|;
name|mapWithNulls
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|mapWithNulls
operator|.
name|put
argument_list|(
literal|null
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
try|try
block|{
name|j
operator|.
name|join
argument_list|(
name|mapWithNulls
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{     }
name|assertEquals
argument_list|(
literal|"a:00;00:b"
argument_list|,
name|j
operator|.
name|useForNull
argument_list|(
literal|"00"
argument_list|)
operator|.
name|join
argument_list|(
name|mapWithNulls
argument_list|)
argument_list|)
expr_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|j
operator|.
name|appendTo
argument_list|(
name|sb
argument_list|,
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|4
argument_list|,
literal|5
argument_list|,
literal|6
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1:2;3:4;5:6"
argument_list|,
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testEntries ()
specifier|public
name|void
name|testEntries
parameter_list|()
block|{
name|MapJoiner
name|j
init|=
name|Joiner
operator|.
name|on
argument_list|(
literal|";"
argument_list|)
operator|.
name|withKeyValueSeparator
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|j
operator|.
name|join
argument_list|(
name|ImmutableMultimap
operator|.
name|of
argument_list|()
operator|.
name|entries
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|j
operator|.
name|join
argument_list|(
name|ImmutableMultimap
operator|.
name|of
argument_list|()
operator|.
name|entries
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|":"
argument_list|,
name|j
operator|.
name|join
argument_list|(
name|ImmutableMultimap
operator|.
name|of
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|)
operator|.
name|entries
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|":"
argument_list|,
name|j
operator|.
name|join
argument_list|(
name|ImmutableMultimap
operator|.
name|of
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|)
operator|.
name|entries
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1:a;1:b"
argument_list|,
name|j
operator|.
name|join
argument_list|(
name|ImmutableMultimap
operator|.
name|of
argument_list|(
literal|"1"
argument_list|,
literal|"a"
argument_list|,
literal|"1"
argument_list|,
literal|"b"
argument_list|)
operator|.
name|entries
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1:a;1:b"
argument_list|,
name|j
operator|.
name|join
argument_list|(
name|ImmutableMultimap
operator|.
name|of
argument_list|(
literal|"1"
argument_list|,
literal|"a"
argument_list|,
literal|"1"
argument_list|,
literal|"b"
argument_list|)
operator|.
name|entries
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mapWithNulls
init|=
name|Maps
operator|.
name|newLinkedHashMap
argument_list|()
decl_stmt|;
name|mapWithNulls
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|mapWithNulls
operator|.
name|put
argument_list|(
literal|null
argument_list|,
literal|"b"
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
name|entriesWithNulls
init|=
name|mapWithNulls
operator|.
name|entrySet
argument_list|()
decl_stmt|;
try|try
block|{
name|j
operator|.
name|join
argument_list|(
name|entriesWithNulls
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{     }
try|try
block|{
name|j
operator|.
name|join
argument_list|(
name|entriesWithNulls
operator|.
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{     }
name|assertEquals
argument_list|(
literal|"a:00;00:b"
argument_list|,
name|j
operator|.
name|useForNull
argument_list|(
literal|"00"
argument_list|)
operator|.
name|join
argument_list|(
name|entriesWithNulls
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a:00;00:b"
argument_list|,
name|j
operator|.
name|useForNull
argument_list|(
literal|"00"
argument_list|)
operator|.
name|join
argument_list|(
name|entriesWithNulls
operator|.
name|iterator
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|StringBuilder
name|sb1
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|j
operator|.
name|appendTo
argument_list|(
name|sb1
argument_list|,
name|ImmutableMultimap
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|4
argument_list|,
literal|5
argument_list|,
literal|6
argument_list|,
literal|1
argument_list|,
literal|3
argument_list|,
literal|5
argument_list|,
literal|10
argument_list|)
operator|.
name|entries
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1:2;1:3;3:4;5:6;5:10"
argument_list|,
name|sb1
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|StringBuilder
name|sb2
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|j
operator|.
name|appendTo
argument_list|(
name|sb2
argument_list|,
name|ImmutableMultimap
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|4
argument_list|,
literal|5
argument_list|,
literal|6
argument_list|,
literal|1
argument_list|,
literal|3
argument_list|,
literal|5
argument_list|,
literal|10
argument_list|)
operator|.
name|entries
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1:2;1:3;3:4;5:6;5:10"
argument_list|,
name|sb2
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|test_skipNulls_onMap ()
specifier|public
name|void
name|test_skipNulls_onMap
parameter_list|()
block|{
name|Joiner
name|j
init|=
name|Joiner
operator|.
name|on
argument_list|(
literal|","
argument_list|)
operator|.
name|skipNulls
argument_list|()
decl_stmt|;
try|try
block|{
name|j
operator|.
name|withKeyValueSeparator
argument_list|(
literal|"/"
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
DECL|class|DontStringMeBro
specifier|private
specifier|static
class|class
name|DontStringMeBro
implements|implements
name|CharSequence
block|{
annotation|@
name|Override
DECL|method|length ()
specifier|public
name|int
name|length
parameter_list|()
block|{
return|return
literal|3
return|;
block|}
annotation|@
name|Override
DECL|method|charAt (int index)
specifier|public
name|char
name|charAt
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
literal|"foo"
operator|.
name|charAt
argument_list|(
name|index
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|subSequence (int start, int end)
specifier|public
name|CharSequence
name|subSequence
parameter_list|(
name|int
name|start
parameter_list|,
name|int
name|end
parameter_list|)
block|{
return|return
literal|"foo"
operator|.
name|subSequence
argument_list|(
name|start
argument_list|,
name|end
argument_list|)
return|;
block|}
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
throw|throw
operator|new
name|AssertionFailedError
argument_list|(
literal|"shouldn't be invoked"
argument_list|)
throw|;
block|}
block|}
comment|// Don't do this.
DECL|class|IterableIterator
specifier|private
specifier|static
class|class
name|IterableIterator
implements|implements
name|Iterable
argument_list|<
name|Integer
argument_list|>
implements|,
name|Iterator
argument_list|<
name|Integer
argument_list|>
block|{
DECL|field|INTEGERS
specifier|private
specifier|static
specifier|final
name|ImmutableSet
argument_list|<
name|Integer
argument_list|>
name|INTEGERS
init|=
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|4
argument_list|)
decl_stmt|;
DECL|field|iterator
specifier|private
specifier|final
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|iterator
decl_stmt|;
DECL|method|IterableIterator ()
specifier|public
name|IterableIterator
parameter_list|()
block|{
name|this
operator|.
name|iterator
operator|=
name|iterator
argument_list|()
expr_stmt|;
block|}
DECL|method|iterator ()
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
name|INTEGERS
operator|.
name|iterator
argument_list|()
return|;
block|}
DECL|method|hasNext ()
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|iterator
operator|.
name|hasNext
argument_list|()
return|;
block|}
DECL|method|next ()
annotation|@
name|Override
specifier|public
name|Integer
name|next
parameter_list|()
block|{
return|return
name|iterator
operator|.
name|next
argument_list|()
return|;
block|}
DECL|method|remove ()
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|GwtIncompatible
comment|// StringBuilder.append in GWT invokes Object.toString(), unlike the JRE version.
DECL|method|testDontConvertCharSequenceToString ()
specifier|public
name|void
name|testDontConvertCharSequenceToString
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"foo,foo"
argument_list|,
name|Joiner
operator|.
name|on
argument_list|(
literal|","
argument_list|)
operator|.
name|join
argument_list|(
operator|new
name|DontStringMeBro
argument_list|()
argument_list|,
operator|new
name|DontStringMeBro
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo,bar,foo"
argument_list|,
name|Joiner
operator|.
name|on
argument_list|(
literal|","
argument_list|)
operator|.
name|useForNull
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|join
argument_list|(
operator|new
name|DontStringMeBro
argument_list|()
argument_list|,
literal|null
argument_list|,
operator|new
name|DontStringMeBro
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// NullPointerTester
DECL|method|testNullPointers ()
specifier|public
name|void
name|testNullPointers
parameter_list|()
block|{
name|NullPointerTester
name|tester
init|=
operator|new
name|NullPointerTester
argument_list|()
decl_stmt|;
name|tester
operator|.
name|testAllPublicStaticMethods
argument_list|(
name|Joiner
operator|.
name|class
argument_list|)
expr_stmt|;
name|tester
operator|.
name|testInstanceMethods
argument_list|(
name|Joiner
operator|.
name|on
argument_list|(
literal|","
argument_list|)
argument_list|,
name|NullPointerTester
operator|.
name|Visibility
operator|.
name|PACKAGE
argument_list|)
expr_stmt|;
name|tester
operator|.
name|testInstanceMethods
argument_list|(
name|Joiner
operator|.
name|on
argument_list|(
literal|","
argument_list|)
operator|.
name|skipNulls
argument_list|()
argument_list|,
name|NullPointerTester
operator|.
name|Visibility
operator|.
name|PACKAGE
argument_list|)
expr_stmt|;
name|tester
operator|.
name|testInstanceMethods
argument_list|(
name|Joiner
operator|.
name|on
argument_list|(
literal|","
argument_list|)
operator|.
name|useForNull
argument_list|(
literal|"x"
argument_list|)
argument_list|,
name|NullPointerTester
operator|.
name|Visibility
operator|.
name|PACKAGE
argument_list|)
expr_stmt|;
name|tester
operator|.
name|testInstanceMethods
argument_list|(
name|Joiner
operator|.
name|on
argument_list|(
literal|","
argument_list|)
operator|.
name|withKeyValueSeparator
argument_list|(
literal|"="
argument_list|)
argument_list|,
name|NullPointerTester
operator|.
name|Visibility
operator|.
name|PACKAGE
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

