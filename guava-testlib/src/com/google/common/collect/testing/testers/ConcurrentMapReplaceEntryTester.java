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
name|MapFeature
operator|.
name|ALLOWS_NULL_VALUE_QUERIES
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
name|SUPPORTS_PUT
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
name|concurrent
operator|.
name|ConcurrentMap
import|;
end_import

begin_comment
comment|/**  * A generic JUnit test which tests {@code replace(K, V, V)} operations on a concurrent map. Can't  * be invoked directly; please see {@link  * com.google.common.collect.testing.ConcurrentMapTestSuiteBuilder}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ConcurrentMapReplaceEntryTester
specifier|public
class|class
name|ConcurrentMapReplaceEntryTester
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
name|Override
DECL|method|getMap ()
specifier|protected
name|ConcurrentMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getMap
parameter_list|()
block|{
return|return
operator|(
name|ConcurrentMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|super
operator|.
name|getMap
argument_list|()
return|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_PUT
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
DECL|method|testReplaceEntry_supportedPresent ()
specifier|public
name|void
name|testReplaceEntry_supportedPresent
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|getMap
argument_list|()
operator|.
name|replace
argument_list|(
name|k0
argument_list|()
argument_list|,
name|v0
argument_list|()
argument_list|,
name|v3
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|expectReplacement
argument_list|(
name|entry
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
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_PUT
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
DECL|method|testReplaceEntry_supportedPresentUnchanged ()
specifier|public
name|void
name|testReplaceEntry_supportedPresentUnchanged
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|getMap
argument_list|()
operator|.
name|replace
argument_list|(
name|k0
argument_list|()
argument_list|,
name|v0
argument_list|()
argument_list|,
name|v0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_PUT
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
DECL|method|testReplaceEntry_supportedWrongValue ()
specifier|public
name|void
name|testReplaceEntry_supportedWrongValue
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|getMap
argument_list|()
operator|.
name|replace
argument_list|(
name|k0
argument_list|()
argument_list|,
name|v3
argument_list|()
argument_list|,
name|v4
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_PUT
argument_list|)
DECL|method|testReplaceEntry_supportedAbsentKey ()
specifier|public
name|void
name|testReplaceEntry_supportedAbsentKey
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|getMap
argument_list|()
operator|.
name|replace
argument_list|(
name|k3
argument_list|()
argument_list|,
name|v3
argument_list|()
argument_list|,
name|v4
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|value
operator|=
name|SUPPORTS_PUT
argument_list|,
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
DECL|method|testReplaceEntry_presentNullValueUnsupported ()
specifier|public
name|void
name|testReplaceEntry_presentNullValueUnsupported
parameter_list|()
block|{
try|try
block|{
name|getMap
argument_list|()
operator|.
name|replace
argument_list|(
name|k0
argument_list|()
argument_list|,
name|v0
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected NullPointerException"
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
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|value
operator|=
name|SUPPORTS_PUT
argument_list|,
name|absent
operator|=
name|ALLOWS_NULL_VALUE_QUERIES
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
DECL|method|testReplaceEntry_wrongValueNullValueUnsupported ()
specifier|public
name|void
name|testReplaceEntry_wrongValueNullValueUnsupported
parameter_list|()
block|{
try|try
block|{
name|assertFalse
argument_list|(
name|getMap
argument_list|()
operator|.
name|replace
argument_list|(
name|k0
argument_list|()
argument_list|,
name|v3
argument_list|()
argument_list|,
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
block|{
comment|// the operation would be a no-op, so exceptions are allowed but not required
block|}
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|value
operator|=
name|SUPPORTS_PUT
argument_list|,
name|absent
operator|=
name|ALLOWS_NULL_VALUE_QUERIES
argument_list|)
DECL|method|testReplaceEntry_absentKeyNullValueUnsupported ()
specifier|public
name|void
name|testReplaceEntry_absentKeyNullValueUnsupported
parameter_list|()
block|{
try|try
block|{
name|assertFalse
argument_list|(
name|getMap
argument_list|()
operator|.
name|replace
argument_list|(
name|k3
argument_list|()
argument_list|,
name|v3
argument_list|()
argument_list|,
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
block|{
comment|// the operation would be a no-op, so exceptions are allowed but not required
block|}
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
block|{
name|SUPPORTS_PUT
block|,
name|ALLOWS_NULL_VALUE_QUERIES
block|}
argument_list|)
DECL|method|testReplaceEntry_nullDifferentFromAbsent ()
specifier|public
name|void
name|testReplaceEntry_nullDifferentFromAbsent
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|getMap
argument_list|()
operator|.
name|replace
argument_list|(
name|k3
argument_list|()
argument_list|,
literal|null
argument_list|,
name|v3
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|value
operator|=
name|SUPPORTS_PUT
argument_list|,
name|absent
operator|=
name|ALLOWS_NULL_VALUE_QUERIES
argument_list|)
DECL|method|testReplaceEntry_expectNullUnsupported ()
specifier|public
name|void
name|testReplaceEntry_expectNullUnsupported
parameter_list|()
block|{
try|try
block|{
name|assertFalse
argument_list|(
name|getMap
argument_list|()
operator|.
name|replace
argument_list|(
name|k3
argument_list|()
argument_list|,
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
block|{
comment|// the operation would be a no-op, so exceptions are allowed but not required
block|}
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_PUT
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
DECL|method|testReplaceEntry_unsupportedPresent ()
specifier|public
name|void
name|testReplaceEntry_unsupportedPresent
parameter_list|()
block|{
try|try
block|{
name|getMap
argument_list|()
operator|.
name|replace
argument_list|(
name|k0
argument_list|()
argument_list|,
name|v0
argument_list|()
argument_list|,
name|v3
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected UnsupportedOperationException"
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
name|MapFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_PUT
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
DECL|method|testReplaceEntry_unsupportedWrongValue ()
specifier|public
name|void
name|testReplaceEntry_unsupportedWrongValue
parameter_list|()
block|{
try|try
block|{
name|getMap
argument_list|()
operator|.
name|replace
argument_list|(
name|k0
argument_list|()
argument_list|,
name|v3
argument_list|()
argument_list|,
name|v4
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|tolerated
parameter_list|)
block|{
comment|// the operation would be a no-op, so exceptions are allowed but not required
block|}
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_PUT
argument_list|)
DECL|method|testReplaceEntry_unsupportedAbsentKey ()
specifier|public
name|void
name|testReplaceEntry_unsupportedAbsentKey
parameter_list|()
block|{
try|try
block|{
name|getMap
argument_list|()
operator|.
name|replace
argument_list|(
name|k3
argument_list|()
argument_list|,
name|v3
argument_list|()
argument_list|,
name|v4
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|tolerated
parameter_list|)
block|{
comment|// the operation would be a no-op, so exceptions are allowed but not required
block|}
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

