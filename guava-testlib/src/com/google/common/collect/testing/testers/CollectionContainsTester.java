begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|WrongType
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
comment|/**  * A generic JUnit test which tests {@code contains()} operations on a  * collection. Can't be invoked directly; please see  * {@link com.google.common.collect.testing.CollectionTestSuiteBuilder}.  *  *<p>This class is GWT compatible.  *  * @author Kevin Bourrillion  * @author Chris Povirk  */
end_comment

begin_class
DECL|class|CollectionContainsTester
specifier|public
class|class
name|CollectionContainsTester
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
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testContains_yes ()
specifier|public
name|void
name|testContains_yes
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"contains(present) should return true"
argument_list|,
name|collection
operator|.
name|contains
argument_list|(
name|samples
operator|.
name|e0
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testContains_no ()
specifier|public
name|void
name|testContains_no
parameter_list|()
block|{
name|assertFalse
argument_list|(
literal|"contains(notPresent) should return false"
argument_list|,
name|collection
operator|.
name|contains
argument_list|(
name|samples
operator|.
name|e3
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|ALLOWS_NULL_VALUES
argument_list|)
DECL|method|testContains_nullNotContainedButSupported ()
specifier|public
name|void
name|testContains_nullNotContainedButSupported
parameter_list|()
block|{
name|assertFalse
argument_list|(
literal|"contains(null) should return false"
argument_list|,
name|collection
operator|.
name|contains
argument_list|(
literal|null
argument_list|)
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
DECL|method|testContains_nullNotContainedAndUnsupported ()
specifier|public
name|void
name|testContains_nullNotContainedAndUnsupported
parameter_list|()
block|{
name|expectNullMissingWhenNullUnsupported
argument_list|(
literal|"contains(null) should return false or throw"
argument_list|)
expr_stmt|;
block|}
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
DECL|method|testContains_nonNullWhenNullContained ()
specifier|public
name|void
name|testContains_nonNullWhenNullContained
parameter_list|()
block|{
name|initCollectionWithNullElement
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
literal|"contains(notPresent) should return false"
argument_list|,
name|collection
operator|.
name|contains
argument_list|(
name|samples
operator|.
name|e3
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
DECL|method|testContains_nullContained ()
specifier|public
name|void
name|testContains_nullContained
parameter_list|()
block|{
name|initCollectionWithNullElement
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"contains(null) should return true"
argument_list|,
name|collection
operator|.
name|contains
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testContains_wrongType ()
specifier|public
name|void
name|testContains_wrongType
parameter_list|()
block|{
try|try
block|{
name|assertFalse
argument_list|(
literal|"contains(wrongType) should return false or throw"
argument_list|,
name|collection
operator|.
name|contains
argument_list|(
name|WrongType
operator|.
name|VALUE
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|tolerated
parameter_list|)
block|{     }
block|}
block|}
end_class

end_unit

