begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Unit test for {@code AbstractIterator}.  *  * @author Kevin Bourrillion  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
comment|// TODO(cpovirk): why is this slow (>1m/test) under GWT when fully optimized?
DECL|class|AbstractIteratorTest
specifier|public
class|class
name|AbstractIteratorTest
extends|extends
name|TestCase
block|{
DECL|method|testDefaultBehaviorOfNextAndHasNext ()
specifier|public
name|void
name|testDefaultBehaviorOfNextAndHasNext
parameter_list|()
block|{
comment|// This sample AbstractIterator returns 0 on the first call, 1 on the
comment|// second, then signals that it's reached the end of the data
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|iter
init|=
operator|new
name|AbstractIterator
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
specifier|private
name|int
name|rep
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Integer
name|computeNext
parameter_list|()
block|{
switch|switch
condition|(
name|rep
operator|++
condition|)
block|{
case|case
literal|0
case|:
return|return
literal|0
return|;
case|case
literal|1
case|:
return|return
literal|1
return|;
case|case
literal|2
case|:
return|return
name|endOfData
argument_list|()
return|;
default|default:
name|fail
argument_list|(
literal|"Should not have been invoked again"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
block|}
decl_stmt|;
name|assertTrue
argument_list|(
name|iter
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
operator|(
name|int
operator|)
name|iter
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
comment|// verify idempotence of hasNext()
name|assertTrue
argument_list|(
name|iter
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|iter
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|iter
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
operator|(
name|int
operator|)
name|iter
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|iter
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
comment|// Make sure computeNext() doesn't get invoked again
name|assertFalse
argument_list|(
name|iter
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|iter
operator|.
name|next
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"no exception thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testSneakyThrow ()
specifier|public
name|void
name|testSneakyThrow
parameter_list|()
throws|throws
name|Exception
block|{
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|iter
init|=
operator|new
name|AbstractIterator
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
name|boolean
name|haveBeenCalled
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Integer
name|computeNext
parameter_list|()
block|{
if|if
condition|(
name|haveBeenCalled
condition|)
block|{
name|fail
argument_list|(
literal|"Should not have been called again"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|haveBeenCalled
operator|=
literal|true
expr_stmt|;
name|sneakyThrow
argument_list|(
operator|new
name|SomeCheckedException
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
comment|// never reached
block|}
block|}
decl_stmt|;
comment|// The first time, the sneakily-thrown exception comes out
try|try
block|{
name|iter
operator|.
name|hasNext
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"No exception thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|e
operator|instanceof
name|SomeCheckedException
operator|)
condition|)
block|{
throw|throw
name|e
throw|;
block|}
block|}
comment|// But the second time, AbstractIterator itself throws an ISE
try|try
block|{
name|iter
operator|.
name|hasNext
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"No exception thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testException ()
specifier|public
name|void
name|testException
parameter_list|()
block|{
specifier|final
name|SomeUncheckedException
name|exception
init|=
operator|new
name|SomeUncheckedException
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|iter
init|=
operator|new
name|AbstractIterator
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Integer
name|computeNext
parameter_list|()
block|{
throw|throw
name|exception
throw|;
block|}
block|}
decl_stmt|;
comment|// It should pass through untouched
try|try
block|{
name|iter
operator|.
name|hasNext
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"No exception thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SomeUncheckedException
name|e
parameter_list|)
block|{
name|assertSame
argument_list|(
name|exception
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testExceptionAfterEndOfData ()
specifier|public
name|void
name|testExceptionAfterEndOfData
parameter_list|()
block|{
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|iter
init|=
operator|new
name|AbstractIterator
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Integer
name|computeNext
parameter_list|()
block|{
name|endOfData
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|SomeUncheckedException
argument_list|()
throw|;
block|}
block|}
decl_stmt|;
try|try
block|{
name|iter
operator|.
name|hasNext
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"No exception thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SomeUncheckedException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testCantRemove ()
specifier|public
name|void
name|testCantRemove
parameter_list|()
block|{
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|iter
init|=
operator|new
name|AbstractIterator
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
name|boolean
name|haveBeenCalled
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Integer
name|computeNext
parameter_list|()
block|{
if|if
condition|(
name|haveBeenCalled
condition|)
block|{
name|endOfData
argument_list|()
expr_stmt|;
block|}
name|haveBeenCalled
operator|=
literal|true
expr_stmt|;
return|return
literal|0
return|;
block|}
block|}
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
operator|(
name|int
operator|)
name|iter
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|iter
operator|.
name|remove
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"No exception thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testReentrantHasNext ()
specifier|public
name|void
name|testReentrantHasNext
parameter_list|()
block|{
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|iter
init|=
operator|new
name|AbstractIterator
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Integer
name|computeNext
parameter_list|()
block|{
name|hasNext
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
try|try
block|{
name|iter
operator|.
name|hasNext
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
block|{     }
block|}
comment|// Technically we should test other reentrant scenarios (4 combinations of
comment|// hasNext/next), but we'll cop out for now, knowing that
comment|// next() both start by invoking hasNext() anyway.
comment|/**    * Throws a undeclared checked exception.    */
DECL|method|sneakyThrow (Throwable t)
specifier|private
specifier|static
name|void
name|sneakyThrow
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
class|class
name|SneakyThrower
parameter_list|<
name|T
extends|extends
name|Throwable
parameter_list|>
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// intentionally unsafe for test
name|void
name|throwIt
parameter_list|(
name|Throwable
name|t
parameter_list|)
throws|throws
name|T
block|{
throw|throw
operator|(
name|T
operator|)
name|t
throw|;
block|}
block|}
operator|new
name|SneakyThrower
argument_list|<
name|Error
argument_list|>
argument_list|()
operator|.
name|throwIt
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
DECL|class|SomeCheckedException
specifier|private
specifier|static
class|class
name|SomeCheckedException
extends|extends
name|Exception
block|{   }
DECL|class|SomeUncheckedException
specifier|private
specifier|static
class|class
name|SomeUncheckedException
extends|extends
name|RuntimeException
block|{   }
block|}
end_class

end_unit

