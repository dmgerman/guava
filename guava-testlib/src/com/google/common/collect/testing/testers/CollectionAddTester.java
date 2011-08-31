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
comment|/**  * A generic JUnit test which tests {@code add} operations on a collection.  * Can't be invoked directly; please see  * {@link com.google.common.collect.testing.CollectionTestSuiteBuilder}.  *  *<p>This class is GWT compatible.  *  * @author Chris Povirk  * @author Kevin Bourrillion  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// too many "unchecked generic array creations"
DECL|class|CollectionAddTester
specifier|public
class|class
name|CollectionAddTester
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
name|SUPPORTS_ADD
argument_list|)
DECL|method|testAdd_supportedNotPresent ()
specifier|public
name|void
name|testAdd_supportedNotPresent
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"add(notPresent) should return true"
argument_list|,
name|collection
operator|.
name|add
argument_list|(
name|samples
operator|.
name|e3
argument_list|)
argument_list|)
expr_stmt|;
name|expectAdded
argument_list|(
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
name|absent
operator|=
name|SUPPORTS_ADD
argument_list|)
DECL|method|testAdd_unsupportedNotPresent ()
specifier|public
name|void
name|testAdd_unsupportedNotPresent
parameter_list|()
block|{
try|try
block|{
name|collection
operator|.
name|add
argument_list|(
name|samples
operator|.
name|e3
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"add(notPresent) should throw"
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
DECL|method|testAdd_unsupportedPresent ()
specifier|public
name|void
name|testAdd_unsupportedPresent
parameter_list|()
block|{
try|try
block|{
name|assertFalse
argument_list|(
literal|"add(present) should return false or throw"
argument_list|,
name|collection
operator|.
name|add
argument_list|(
name|samples
operator|.
name|e0
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|tolerated
parameter_list|)
block|{     }
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
block|{
name|SUPPORTS_ADD
block|,
name|ALLOWS_NULL_VALUES
block|}
argument_list|)
DECL|method|testAdd_nullSupported ()
specifier|public
name|void
name|testAdd_nullSupported
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"add(null) should return true"
argument_list|,
name|collection
operator|.
name|add
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|expectAdded
argument_list|(
operator|(
name|E
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|value
operator|=
name|SUPPORTS_ADD
argument_list|,
name|absent
operator|=
name|ALLOWS_NULL_VALUES
argument_list|)
DECL|method|testAdd_nullUnsupported ()
specifier|public
name|void
name|testAdd_nullUnsupported
parameter_list|()
block|{
try|try
block|{
name|collection
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"add(null) should throw"
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
literal|"Should not contain null after unsupported add(null)"
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns the {@link Method} instance for {@link #testAdd_nullSupported()} so    * that tests of {@link    * java.util.Collections#checkedCollection(java.util.Collection, Class)} can    * suppress it with {@code FeatureSpecificTestSuiteBuilder.suppressing()}    * until<a    * href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6409434">Sun bug    * 6409434</a> is fixed. It's unclear whether nulls were to be permitted or    * forbidden, but presumably the eventual fix will be to permit them, as it    * seems more likely that code would depend on that behavior than on the    * other. Thus, we say the bug is in add(), which fails to support null.    */
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
name|CollectionAddTester
operator|.
name|class
argument_list|,
literal|"testAdd_nullSupported"
argument_list|)
return|;
block|}
comment|/**    * Returns the {@link Method} instance for {@link #testAdd_nullSupported()} so    * that tests of {@link    * java.util.Collections#checkedCollection(java.util.Collection, Class)} can    * suppress it with {@code FeatureSpecificTestSuiteBuilder.suppressing()}    * until<a    * href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5045147">Sun    * bug 5045147</a> is fixed.    */
DECL|method|getAddNullUnsupportedMethod ()
specifier|public
specifier|static
name|Method
name|getAddNullUnsupportedMethod
parameter_list|()
block|{
return|return
name|Platform
operator|.
name|getMethod
argument_list|(
name|CollectionAddTester
operator|.
name|class
argument_list|,
literal|"testAdd_nullUnsupported"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

