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
name|ALLOWS_NULL_VALUES
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
comment|/**  * A generic JUnit test which tests {@link Map#getOrDefault}. Can't be invoked directly; please see  * {@link com.google.common.collect.testing.MapTestSuiteBuilder}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|Ignore
comment|// Affects only Android test runner, which respects JUnit 4 annotations on JUnit 3 tests.
DECL|class|MapGetOrDefaultTester
specifier|public
class|class
name|MapGetOrDefaultTester
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
DECL|method|testGetOrDefault_present ()
specifier|public
name|void
name|testGetOrDefault_present
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"getOrDefault(present, def) should return the associated value"
argument_list|,
name|v0
argument_list|()
argument_list|,
name|getMap
argument_list|()
operator|.
name|getOrDefault
argument_list|(
name|k0
argument_list|()
argument_list|,
name|v3
argument_list|()
argument_list|)
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
DECL|method|testGetOrDefault_presentNullDefault ()
specifier|public
name|void
name|testGetOrDefault_presentNullDefault
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"getOrDefault(present, null) should return the associated value"
argument_list|,
name|v0
argument_list|()
argument_list|,
name|getMap
argument_list|()
operator|.
name|getOrDefault
argument_list|(
name|k0
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetOrDefault_absent ()
specifier|public
name|void
name|testGetOrDefault_absent
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"getOrDefault(absent, def) should return the default value"
argument_list|,
name|v3
argument_list|()
argument_list|,
name|getMap
argument_list|()
operator|.
name|getOrDefault
argument_list|(
name|k3
argument_list|()
argument_list|,
name|v3
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetOrDefault_absentNullDefault ()
specifier|public
name|void
name|testGetOrDefault_absentNullDefault
parameter_list|()
block|{
name|assertNull
argument_list|(
literal|"getOrDefault(absent, null) should return null"
argument_list|,
name|getMap
argument_list|()
operator|.
name|getOrDefault
argument_list|(
name|k3
argument_list|()
argument_list|,
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
name|ALLOWS_NULL_KEY_QUERIES
argument_list|)
DECL|method|testGetOrDefault_absentNull ()
specifier|public
name|void
name|testGetOrDefault_absentNull
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"getOrDefault(null, def) should return the default value"
argument_list|,
name|v3
argument_list|()
argument_list|,
name|getMap
argument_list|()
operator|.
name|getOrDefault
argument_list|(
literal|null
argument_list|,
name|v3
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
name|absent
operator|=
name|ALLOWS_NULL_KEY_QUERIES
argument_list|)
DECL|method|testGetOrDefault_nullAbsentAndUnsupported ()
specifier|public
name|void
name|testGetOrDefault_nullAbsentAndUnsupported
parameter_list|()
block|{
try|try
block|{
name|assertEquals
argument_list|(
literal|"getOrDefault(null, def) should return default or throw"
argument_list|,
name|v3
argument_list|()
argument_list|,
name|getMap
argument_list|()
operator|.
name|getOrDefault
argument_list|(
literal|null
argument_list|,
name|v3
argument_list|()
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
DECL|method|testGetOrDefault_nonNullWhenNullContained ()
specifier|public
name|void
name|testGetOrDefault_nonNullWhenNullContained
parameter_list|()
block|{
name|initMapWithNullKey
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getOrDefault(absent, default) should return default"
argument_list|,
name|v3
argument_list|()
argument_list|,
name|getMap
argument_list|()
operator|.
name|getOrDefault
argument_list|(
name|k3
argument_list|()
argument_list|,
name|v3
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
DECL|method|testGetOrDefault_presentNull ()
specifier|public
name|void
name|testGetOrDefault_presentNull
parameter_list|()
block|{
name|initMapWithNullKey
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getOrDefault(null, default) should return the associated value"
argument_list|,
name|getValueForNullKey
argument_list|()
argument_list|,
name|getMap
argument_list|()
operator|.
name|getOrDefault
argument_list|(
literal|null
argument_list|,
name|v3
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
DECL|method|testGetOrDefault_presentMappedToNull ()
specifier|public
name|void
name|testGetOrDefault_presentMappedToNull
parameter_list|()
block|{
name|initMapWithNullValue
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
literal|"getOrDefault(mappedToNull, default) should return null"
argument_list|,
name|getMap
argument_list|()
operator|.
name|getOrDefault
argument_list|(
name|getKeyForNullValue
argument_list|()
argument_list|,
name|v3
argument_list|()
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
name|assertEquals
argument_list|(
literal|"getOrDefault(wrongType, default) should return default or throw"
argument_list|,
name|v3
argument_list|()
argument_list|,
name|getMap
argument_list|()
operator|.
name|getOrDefault
argument_list|(
name|WrongType
operator|.
name|VALUE
argument_list|,
name|v3
argument_list|()
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

