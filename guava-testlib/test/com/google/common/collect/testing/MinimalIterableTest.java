begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|testing
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
name|NoSuchElementException
import|;
end_import

begin_comment
comment|/**  * Unit test for {@link MinimalIterable}.  *  * @author Kevin Bourrillion  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|MinimalIterableTest
specifier|public
class|class
name|MinimalIterableTest
extends|extends
name|TestCase
block|{
DECL|method|testOf_empty ()
specifier|public
name|void
name|testOf_empty
parameter_list|()
block|{
name|Iterable
argument_list|<
name|String
argument_list|>
name|iterable
init|=
name|MinimalIterable
operator|.
expr|<
name|String
operator|>
name|of
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|iterator
init|=
name|iterable
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|iterator
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|iterator
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
name|iterable
operator|.
name|iterator
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
DECL|method|testOf_one ()
specifier|public
name|void
name|testOf_one
parameter_list|()
block|{
name|Iterable
argument_list|<
name|String
argument_list|>
name|iterable
init|=
name|MinimalIterable
operator|.
name|of
argument_list|(
literal|"a"
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|iterator
init|=
name|iterable
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|iterator
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|iterator
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|iterator
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|iterator
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
name|iterable
operator|.
name|iterator
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
DECL|method|testFrom_empty ()
specifier|public
name|void
name|testFrom_empty
parameter_list|()
block|{
name|Iterable
argument_list|<
name|String
argument_list|>
name|iterable
init|=
name|MinimalIterable
operator|.
name|from
argument_list|(
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptySet
argument_list|()
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|iterator
init|=
name|iterable
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|iterator
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|iterator
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
name|iterable
operator|.
name|iterator
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
DECL|method|testFrom_one ()
specifier|public
name|void
name|testFrom_one
parameter_list|()
block|{
name|Iterable
argument_list|<
name|String
argument_list|>
name|iterable
init|=
name|MinimalIterable
operator|.
name|from
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|iterator
init|=
name|iterable
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|iterator
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|iterator
operator|.
name|next
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
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
name|assertFalse
argument_list|(
name|iterator
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|iterator
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
name|iterable
operator|.
name|iterator
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
block|}
end_class

end_unit

