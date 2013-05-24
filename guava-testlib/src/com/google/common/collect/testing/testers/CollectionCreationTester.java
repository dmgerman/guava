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
name|AbstractCollectionTester
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

begin_comment
comment|/**  * A generic JUnit test which tests creation (typically through a constructor or  * static factory method) of a collection. Can't be invoked directly; please see  * {@link com.google.common.collect.testing.CollectionTestSuiteBuilder}.  *  * @author Chris Povirk  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|CollectionCreationTester
specifier|public
class|class
name|CollectionCreationTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractCollectionTester
argument_list|<
name|E
argument_list|>
block|{
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|ALLOWS_NULL_VALUES
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
DECL|method|testCreateWithNull_supported ()
specifier|public
name|void
name|testCreateWithNull_supported
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
name|expectContents
argument_list|(
name|array
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
name|ALLOWS_NULL_VALUES
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
DECL|method|testCreateWithNull_unsupported ()
specifier|public
name|void
name|testCreateWithNull_unsupported
parameter_list|()
block|{
name|E
index|[]
name|array
init|=
name|createArrayWithNullElement
argument_list|()
decl_stmt|;
try|try
block|{
name|getSubjectGenerator
argument_list|()
operator|.
name|create
argument_list|(
name|array
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Creating a collection containing null should fail"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{     }
block|}
comment|/**    * Returns the {@link Method} instance for {@link    * #testCreateWithNull_unsupported()} so that tests can suppress it    * with {@code FeatureSpecificTestSuiteBuilder.suppressing()} until<a    * href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5045147">Sun    * bug 5045147</a> is fixed.    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"reflection"
argument_list|)
DECL|method|getCreateWithNullUnsupportedMethod ()
specifier|public
specifier|static
name|Method
name|getCreateWithNullUnsupportedMethod
parameter_list|()
block|{
return|return
name|Helpers
operator|.
name|getMethod
argument_list|(
name|CollectionCreationTester
operator|.
name|class
argument_list|,
literal|"testCreateWithNull_unsupported"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

