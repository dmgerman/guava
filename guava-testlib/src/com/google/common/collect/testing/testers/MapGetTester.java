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
name|MapFeature
operator|.
name|ALLOWS_NULL_KEYS
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
name|MapFeature
operator|.
name|ALLOWS_NULL_KEY_QUERIES
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
name|AbstractMapTester
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
name|MapFeature
import|;
end_import

begin_comment
comment|/**  * A generic JUnit test which tests {@code get} operations on a map. Can't be  * invoked directly; please see  * {@link com.google.common.collect.testing.MapTestSuiteBuilder}.  *  * @author Kevin Bourrillion  * @author Chris Povirk  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|MapGetTester
specifier|public
class|class
name|MapGetTester
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractMapTester
argument_list|<
name|K
argument_list|,
name|V
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
DECL|method|testGet_yes ()
specifier|public
name|void
name|testGet_yes
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"get(present) should return the associated value"
argument_list|,
name|v0
argument_list|()
argument_list|,
name|get
argument_list|(
name|k0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGet_no ()
specifier|public
name|void
name|testGet_no
parameter_list|()
block|{
name|assertNull
argument_list|(
literal|"get(notPresent) should return null"
argument_list|,
name|get
argument_list|(
name|k3
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|ALLOWS_NULL_KEY_QUERIES
argument_list|)
DECL|method|testGet_nullNotContainedButAllowed ()
specifier|public
name|void
name|testGet_nullNotContainedButAllowed
parameter_list|()
block|{
name|assertNull
argument_list|(
literal|"get(null) should return null"
argument_list|,
name|get
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ALLOWS_NULL_KEY_QUERIES
argument_list|)
DECL|method|testGet_nullNotContainedAndUnsupported ()
specifier|public
name|void
name|testGet_nullNotContainedAndUnsupported
parameter_list|()
block|{
try|try
block|{
name|assertNull
argument_list|(
literal|"get(null) should return null or throw"
argument_list|,
name|get
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
name|MapFeature
operator|.
name|Require
argument_list|(
name|ALLOWS_NULL_KEYS
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
DECL|method|testGet_nonNullWhenNullContained ()
specifier|public
name|void
name|testGet_nonNullWhenNullContained
parameter_list|()
block|{
name|initMapWithNullKey
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
literal|"get(notPresent) should return null"
argument_list|,
name|get
argument_list|(
name|k3
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|ALLOWS_NULL_KEYS
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
DECL|method|testGet_nullContained ()
specifier|public
name|void
name|testGet_nullContained
parameter_list|()
block|{
name|initMapWithNullKey
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"get(null) should return the associated value"
argument_list|,
name|getValueForNullKey
argument_list|()
argument_list|,
name|get
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGet_wrongType ()
specifier|public
name|void
name|testGet_wrongType
parameter_list|()
block|{
try|try
block|{
name|assertNull
argument_list|(
literal|"get(wrongType) should return null or throw"
argument_list|,
name|getMap
argument_list|()
operator|.
name|get
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

