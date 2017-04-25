begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ZERO
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
name|Helpers
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
name|List
import|;
end_import

begin_comment
comment|/**  * A generic JUnit test which tests {@code add(Object)} operations on a list.  * Can't be invoked directly; please see  * {@link com.google.common.collect.testing.ListTestSuiteBuilder}.  *  * @author Chris Povirk  */
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
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|ListAddTester
specifier|public
class|class
name|ListAddTester
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
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD
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
DECL|method|testAdd_supportedPresent ()
specifier|public
name|void
name|testAdd_supportedPresent
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"add(present) should return true"
argument_list|,
name|getList
argument_list|()
operator|.
name|add
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|expectAdded
argument_list|(
name|e0
argument_list|()
argument_list|)
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
DECL|method|testAdd_unsupportedPresent ()
specifier|public
name|void
name|testAdd_unsupportedPresent
parameter_list|()
block|{
try|try
block|{
name|getList
argument_list|()
operator|.
name|add
argument_list|(
name|e0
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"add(present) should throw"
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
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|value
operator|=
block|{
name|SUPPORTS_ADD
block|,
name|ALLOWS_NULL_VALUES
block|}
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
DECL|method|testAdd_supportedNullPresent ()
specifier|public
name|void
name|testAdd_supportedNullPresent
parameter_list|()
block|{
name|E
index|[]
name|array
init|=
name|createArrayWithNullElement
argument_list|()
decl_stmt|;
name|collection
operator|=
name|getSubjectGenerator
argument_list|()
operator|.
name|create
argument_list|(
name|array
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"add(nullPresent) should return true"
argument_list|,
name|getList
argument_list|()
operator|.
name|add
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|E
argument_list|>
name|expected
init|=
name|Helpers
operator|.
name|copyToList
argument_list|(
name|array
argument_list|)
decl_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|expectContents
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns the {@link Method} instance for    * {@link #testAdd_supportedNullPresent()} so that tests can suppress it. See    * {@link CollectionAddTester#getAddNullSupportedMethod()} for details.    */
annotation|@
name|GwtIncompatible
comment|// reflection
DECL|method|getAddSupportedNullPresentMethod ()
specifier|public
specifier|static
name|Method
name|getAddSupportedNullPresentMethod
parameter_list|()
block|{
return|return
name|Helpers
operator|.
name|getMethod
argument_list|(
name|ListAddTester
operator|.
name|class
argument_list|,
literal|"testAdd_supportedNullPresent"
argument_list|)
return|;
block|}
block|}
end_class

end_unit
