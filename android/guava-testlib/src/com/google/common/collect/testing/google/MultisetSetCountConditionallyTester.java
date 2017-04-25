begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing.google
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
operator|.
name|google
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
name|features
operator|.
name|CollectionFeature
operator|.
name|SUPPORTS_ADD
import|;
end_import

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
name|features
operator|.
name|CollectionSize
operator|.
name|SEVERAL
import|;
end_import

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
name|features
operator|.
name|CollectionSize
operator|.
name|ZERO
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Collections
operator|.
name|nCopies
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

begin_comment
comment|/**  * A generic JUnit test which tests conditional {@code setCount()} operations on  * a multiset. Can't be invoked directly; please see  * {@link MultisetTestSuiteBuilder}.  *  * @author Chris Povirk  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|MultisetSetCountConditionallyTester
specifier|public
class|class
name|MultisetSetCountConditionallyTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractMultisetSetCountTester
argument_list|<
name|E
argument_list|>
block|{
annotation|@
name|Override
DECL|method|setCountCheckReturnValue (E element, int count)
name|void
name|setCountCheckReturnValue
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|count
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"setCount() with the correct expected present count should return true"
argument_list|,
name|setCount
argument_list|(
name|element
argument_list|,
name|count
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setCountNoCheckReturnValue (E element, int count)
name|void
name|setCountNoCheckReturnValue
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|count
parameter_list|)
block|{
name|setCount
argument_list|(
name|element
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
DECL|method|setCount (E element, int count)
specifier|private
name|boolean
name|setCount
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|count
parameter_list|)
block|{
return|return
name|getMultiset
argument_list|()
operator|.
name|setCount
argument_list|(
name|element
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|count
argument_list|(
name|element
argument_list|)
argument_list|,
name|count
argument_list|)
return|;
block|}
DECL|method|assertSetCountNegativeOldCount ()
specifier|private
name|void
name|assertSetCountNegativeOldCount
parameter_list|()
block|{
try|try
block|{
name|getMultiset
argument_list|()
operator|.
name|setCount
argument_list|(
name|e3
argument_list|()
argument_list|,
operator|-
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"calling setCount() with a negative oldCount should throw IllegalArgumentException"
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
comment|// Negative oldCount.
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD
argument_list|)
DECL|method|testSetCountConditional_negativeOldCount_addSupported ()
specifier|public
name|void
name|testSetCountConditional_negativeOldCount_addSupported
parameter_list|()
block|{
name|assertSetCountNegativeOldCount
argument_list|()
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_ADD
argument_list|)
DECL|method|testSetCountConditional_negativeOldCount_addUnsupported ()
specifier|public
name|void
name|testSetCountConditional_negativeOldCount_addUnsupported
parameter_list|()
block|{
try|try
block|{
name|assertSetCountNegativeOldCount
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|tolerated
parameter_list|)
block|{     }
block|}
comment|// Incorrect expected present count.
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD
argument_list|)
DECL|method|testSetCountConditional_oldCountTooLarge ()
specifier|public
name|void
name|testSetCountConditional_oldCountTooLarge
parameter_list|()
block|{
name|assertFalse
argument_list|(
literal|"setCount() with a too-large oldCount should return false"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|setCount
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD
argument_list|)
DECL|method|testSetCountConditional_oldCountTooSmallZero ()
specifier|public
name|void
name|testSetCountConditional_oldCountTooSmallZero
parameter_list|()
block|{
name|assertFalse
argument_list|(
literal|"setCount() with a too-small oldCount should return false"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|setCount
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|0
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD
argument_list|)
DECL|method|testSetCountConditional_oldCountTooSmallNonzero ()
specifier|public
name|void
name|testSetCountConditional_oldCountTooSmallNonzero
parameter_list|()
block|{
name|initThreeCopies
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
literal|"setCount() with a too-small oldCount should return false"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|setCount
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|1
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|expectContents
argument_list|(
name|nCopies
argument_list|(
literal|3
argument_list|,
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/*    * TODO: test that unmodifiable multisets either throw UOE or return false    * when both are valid options. Currently we test the UOE cases and the    * return-false cases but not their intersection    */
block|}
end_class

end_unit
