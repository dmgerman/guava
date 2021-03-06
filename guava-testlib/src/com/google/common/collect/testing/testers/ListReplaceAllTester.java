begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2015 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|SUPPORTS_SET
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
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_comment
comment|/**  * A generic JUnit test which tests {@link List#replaceAll}. Can't be invoked directly; please see  * {@link com.google.common.collect.testing.ListTestSuiteBuilder}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|Ignore
comment|// Affects only Android test runner, which respects JUnit 4 annotations on JUnit 3 tests.
DECL|class|ListReplaceAllTester
specifier|public
class|class
name|ListReplaceAllTester
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
name|SUPPORTS_SET
argument_list|)
DECL|method|testReplaceAll ()
specifier|public
name|void
name|testReplaceAll
parameter_list|()
block|{
name|getList
argument_list|()
operator|.
name|replaceAll
argument_list|(
name|e
lambda|->
name|samples
operator|.
name|e3
argument_list|()
argument_list|)
expr_stmt|;
name|expectContents
argument_list|(
name|Collections
operator|.
name|nCopies
argument_list|(
name|getNumElements
argument_list|()
argument_list|,
name|samples
operator|.
name|e3
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_SET
argument_list|)
DECL|method|testReplaceAll_changesSome ()
specifier|public
name|void
name|testReplaceAll_changesSome
parameter_list|()
block|{
name|getList
argument_list|()
operator|.
name|replaceAll
argument_list|(
name|e
lambda|->
name|e
operator|.
name|equals
argument_list|(
name|samples
operator|.
name|e0
argument_list|()
argument_list|)
condition|?
name|samples
operator|.
name|e3
argument_list|()
else|:
name|e
argument_list|)
expr_stmt|;
name|E
index|[]
name|expected
init|=
name|createSamplesArray
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
name|expected
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|expected
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|samples
operator|.
name|e0
argument_list|()
argument_list|)
condition|)
block|{
name|expected
index|[
name|i
index|]
operator|=
name|samples
operator|.
name|e3
argument_list|()
expr_stmt|;
block|}
block|}
name|expectContents
argument_list|(
name|expected
argument_list|)
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
name|ListFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_SET
argument_list|)
DECL|method|testReplaceAll_unsupported ()
specifier|public
name|void
name|testReplaceAll_unsupported
parameter_list|()
block|{
try|try
block|{
name|getList
argument_list|()
operator|.
name|replaceAll
argument_list|(
name|e
lambda|->
name|e
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"replaceAll() should throw UnsupportedOperationException"
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
block|}
end_class

end_unit

