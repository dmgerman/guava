begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing.testers
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
name|testers
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
name|ALLOWS_NULL_VALUES
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
name|CollectionFeature
operator|.
name|FAILS_FAST_ON_CONCURRENT_MODIFICATION
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
name|ONE
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
name|ListFeature
operator|.
name|SUPPORTS_ADD_WITH_INDEX
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
name|ListFeature
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ConcurrentModificationException
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

begin_comment
comment|/**  * A generic JUnit test which tests {@code add(int, Object)} operations on a  * list. Can't be invoked directly; please see  * {@link com.google.common.collect.testing.ListTestSuiteBuilder}.  *  *<p>This class is GWT compatible.  *  * @author Chris Povirk  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// too many "unchecked generic array creations"
annotation|@
name|GwtCompatible
DECL|class|ListAddAtIndexTester
specifier|public
class|class
name|ListAddAtIndexTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractListTester
argument_list|<
name|E
argument_list|>
block|{
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD_WITH_INDEX
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testAddAtIndex_supportedPresent ()
specifier|public
name|void
name|testAddAtIndex_supportedPresent
parameter_list|()
block|{
name|getList
argument_list|()
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|samples
operator|.
name|e0
argument_list|)
expr_stmt|;
name|expectAdded
argument_list|(
literal|0
argument_list|,
name|samples
operator|.
name|e0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_ADD_WITH_INDEX
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
comment|/*    * absent = ZERO isn't required, since unmodList.add() must    * throw regardless, but it keeps the method name accurate.    */
DECL|method|testAddAtIndex_unsupportedPresent ()
specifier|public
name|void
name|testAddAtIndex_unsupportedPresent
parameter_list|()
block|{
try|try
block|{
name|getList
argument_list|()
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|samples
operator|.
name|e0
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"add(n, present) should throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD_WITH_INDEX
argument_list|)
DECL|method|testAddAtIndex_supportedNotPresent ()
specifier|public
name|void
name|testAddAtIndex_supportedNotPresent
parameter_list|()
block|{
name|getList
argument_list|()
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|samples
operator|.
name|e3
argument_list|)
expr_stmt|;
name|expectAdded
argument_list|(
literal|0
argument_list|,
name|samples
operator|.
name|e3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|FAILS_FAST_ON_CONCURRENT_MODIFICATION
argument_list|)
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD_WITH_INDEX
argument_list|)
DECL|method|testAddAtIndexConcurrentWithIteration ()
specifier|public
name|void
name|testAddAtIndexConcurrentWithIteration
parameter_list|()
block|{
try|try
block|{
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
init|=
name|collection
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|getList
argument_list|()
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|samples
operator|.
name|e3
argument_list|)
expr_stmt|;
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected ConcurrentModificationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConcurrentModificationException
name|expected
parameter_list|)
block|{
comment|// success
block|}
block|}
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_ADD_WITH_INDEX
argument_list|)
DECL|method|testAddAtIndex_unsupportedNotPresent ()
specifier|public
name|void
name|testAddAtIndex_unsupportedNotPresent
parameter_list|()
block|{
try|try
block|{
name|getList
argument_list|()
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|samples
operator|.
name|e3
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"add(n, notPresent) should throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
name|expectUnchanged
argument_list|()
expr_stmt|;
name|expectMissing
argument_list|(
name|samples
operator|.
name|e3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD_WITH_INDEX
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
block|{
name|ZERO
block|,
name|ONE
block|}
argument_list|)
DECL|method|testAddAtIndex_middle ()
specifier|public
name|void
name|testAddAtIndex_middle
parameter_list|()
block|{
name|getList
argument_list|()
operator|.
name|add
argument_list|(
name|getNumElements
argument_list|()
operator|/
literal|2
argument_list|,
name|samples
operator|.
name|e3
argument_list|)
expr_stmt|;
name|expectAdded
argument_list|(
name|getNumElements
argument_list|()
operator|/
literal|2
argument_list|,
name|samples
operator|.
name|e3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD_WITH_INDEX
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testAddAtIndex_end ()
specifier|public
name|void
name|testAddAtIndex_end
parameter_list|()
block|{
name|getList
argument_list|()
operator|.
name|add
argument_list|(
name|getNumElements
argument_list|()
argument_list|,
name|samples
operator|.
name|e3
argument_list|)
expr_stmt|;
name|expectAdded
argument_list|(
name|getNumElements
argument_list|()
argument_list|,
name|samples
operator|.
name|e3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD_WITH_INDEX
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|ALLOWS_NULL_VALUES
argument_list|)
DECL|method|testAddAtIndex_nullSupported ()
specifier|public
name|void
name|testAddAtIndex_nullSupported
parameter_list|()
block|{
name|getList
argument_list|()
operator|.
name|add
argument_list|(
literal|0
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|expectAdded
argument_list|(
literal|0
argument_list|,
operator|(
name|E
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD_WITH_INDEX
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ALLOWS_NULL_VALUES
argument_list|)
DECL|method|testAddAtIndex_nullUnsupported ()
specifier|public
name|void
name|testAddAtIndex_nullUnsupported
parameter_list|()
block|{
try|try
block|{
name|getList
argument_list|()
operator|.
name|add
argument_list|(
literal|0
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"add(n, null) should throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{     }
name|expectUnchanged
argument_list|()
expr_stmt|;
name|expectNullMissingWhenNullUnsupported
argument_list|(
literal|"Should not contain null after unsupported add(n, null)"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD_WITH_INDEX
argument_list|)
DECL|method|testAddAtIndex_negative ()
specifier|public
name|void
name|testAddAtIndex_negative
parameter_list|()
block|{
try|try
block|{
name|getList
argument_list|()
operator|.
name|add
argument_list|(
operator|-
literal|1
argument_list|,
name|samples
operator|.
name|e3
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"add(-1, e) should throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IndexOutOfBoundsException
name|expected
parameter_list|)
block|{     }
name|expectUnchanged
argument_list|()
expr_stmt|;
name|expectMissing
argument_list|(
name|samples
operator|.
name|e3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD_WITH_INDEX
argument_list|)
DECL|method|testAddAtIndex_tooLarge ()
specifier|public
name|void
name|testAddAtIndex_tooLarge
parameter_list|()
block|{
try|try
block|{
name|getList
argument_list|()
operator|.
name|add
argument_list|(
name|getNumElements
argument_list|()
operator|+
literal|1
argument_list|,
name|samples
operator|.
name|e3
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"add(size + 1, e) should throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IndexOutOfBoundsException
name|expected
parameter_list|)
block|{     }
name|expectUnchanged
argument_list|()
expr_stmt|;
name|expectMissing
argument_list|(
name|samples
operator|.
name|e3
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns the {@link Method} instance for    * {@link #testAddAtIndex_nullSupported()} so that tests can suppress it. See    * {@link CollectionAddTester#getAddNullSupportedMethod()} for details.    */
DECL|method|getAddNullSupportedMethod ()
specifier|public
specifier|static
name|Method
name|getAddNullSupportedMethod
parameter_list|()
block|{
return|return
name|Platform
operator|.
name|getMethod
argument_list|(
name|ListAddAtIndexTester
operator|.
name|class
argument_list|,
literal|"testAddAtIndex_nullSupported"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

