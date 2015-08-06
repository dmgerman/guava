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
comment|/**  * Common parent class for {@link ListIndexOfTester} and  * {@link ListLastIndexOfTester}.  *  * @author Chris Povirk  */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|SuppressUnderAndroid
DECL|class|AbstractListIndexOfTester
specifier|public
specifier|abstract
class|class
name|AbstractListIndexOfTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractListTester
argument_list|<
name|E
argument_list|>
block|{
comment|/** Override to call {@code indexOf()} or {@code lastIndexOf()}. */
DECL|method|find (Object o)
specifier|protected
specifier|abstract
name|int
name|find
parameter_list|(
name|Object
name|o
parameter_list|)
function_decl|;
comment|/**    * Override to return "indexOf" or "lastIndexOf()" for use in failure    * messages.    */
DECL|method|getMethodName ()
specifier|protected
specifier|abstract
name|String
name|getMethodName
parameter_list|()
function_decl|;
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testFind_yes ()
specifier|public
name|void
name|testFind_yes
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|getMethodName
argument_list|()
operator|+
literal|"(firstElement) should return 0"
argument_list|,
literal|0
argument_list|,
name|find
argument_list|(
name|getOrderedElements
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testFind_no ()
specifier|public
name|void
name|testFind_no
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|getMethodName
argument_list|()
operator|+
literal|"(notPresent) should return -1"
argument_list|,
operator|-
literal|1
argument_list|,
name|find
argument_list|(
name|e3
argument_list|()
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
DECL|method|testFind_nullNotContainedButSupported ()
specifier|public
name|void
name|testFind_nullNotContainedButSupported
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|getMethodName
argument_list|()
operator|+
literal|"(nullNotPresent) should return -1"
argument_list|,
operator|-
literal|1
argument_list|,
name|find
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
DECL|method|testFind_nullNotContainedAndUnsupported ()
specifier|public
name|void
name|testFind_nullNotContainedAndUnsupported
parameter_list|()
block|{
try|try
block|{
name|assertEquals
argument_list|(
name|getMethodName
argument_list|()
operator|+
literal|"(nullNotPresent) should return -1 or throw"
argument_list|,
operator|-
literal|1
argument_list|,
name|find
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|tolerated
parameter_list|)
block|{     }
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
DECL|method|testFind_nonNullWhenNullContained ()
specifier|public
name|void
name|testFind_nonNullWhenNullContained
parameter_list|()
block|{
name|initCollectionWithNullElement
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|getMethodName
argument_list|()
operator|+
literal|"(notPresent) should return -1"
argument_list|,
operator|-
literal|1
argument_list|,
name|find
argument_list|(
name|e3
argument_list|()
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
DECL|method|testFind_nullContained ()
specifier|public
name|void
name|testFind_nullContained
parameter_list|()
block|{
name|initCollectionWithNullElement
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|getMethodName
argument_list|()
operator|+
literal|"(null) should return "
operator|+
name|getNullLocation
argument_list|()
argument_list|,
name|getNullLocation
argument_list|()
argument_list|,
name|find
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testFind_wrongType ()
specifier|public
name|void
name|testFind_wrongType
parameter_list|()
block|{
try|try
block|{
name|assertEquals
argument_list|(
name|getMethodName
argument_list|()
operator|+
literal|"(wrongType) should return -1 or throw"
argument_list|,
operator|-
literal|1
argument_list|,
name|find
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

