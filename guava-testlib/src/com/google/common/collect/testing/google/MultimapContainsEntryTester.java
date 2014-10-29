begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing.google
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
name|google
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
name|Multimap
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
comment|/**  * Tester for {@link Multimap#containsEntry}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|MultimapContainsEntryTester
specifier|public
class|class
name|MultimapContainsEntryTester
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractMultimapTester
argument_list|<
name|K
argument_list|,
name|V
argument_list|,
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
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
DECL|method|testContainsEntryYes ()
specifier|public
name|void
name|testContainsEntryYes
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|multimap
argument_list|()
operator|.
name|containsEntry
argument_list|(
name|k0
argument_list|()
argument_list|,
name|v0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testContainsEntryNo ()
specifier|public
name|void
name|testContainsEntryNo
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|multimap
argument_list|()
operator|.
name|containsEntry
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
DECL|method|testContainsEntryAgreesWithGet ()
specifier|public
name|void
name|testContainsEntryAgreesWithGet
parameter_list|()
block|{
for|for
control|(
name|K
name|k
range|:
name|sampleKeys
argument_list|()
control|)
block|{
for|for
control|(
name|V
name|v
range|:
name|sampleValues
argument_list|()
control|)
block|{
name|assertEquals
argument_list|(
name|multimap
argument_list|()
operator|.
name|get
argument_list|(
name|k
argument_list|)
operator|.
name|contains
argument_list|(
name|v
argument_list|)
argument_list|,
name|multimap
argument_list|()
operator|.
name|containsEntry
argument_list|(
name|k
argument_list|,
name|v
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
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
name|MapFeature
operator|.
name|Require
argument_list|(
block|{
name|ALLOWS_NULL_KEYS
block|,
name|ALLOWS_NULL_VALUES
block|}
argument_list|)
DECL|method|testContainsEntryNullYes ()
specifier|public
name|void
name|testContainsEntryNullYes
parameter_list|()
block|{
name|initMultimapWithNullKeyAndValue
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
argument_list|()
operator|.
name|containsEntry
argument_list|(
literal|null
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
block|{
name|ALLOWS_NULL_KEY_QUERIES
block|,
name|ALLOWS_NULL_VALUE_QUERIES
block|}
argument_list|)
DECL|method|testContainsEntryNullNo ()
specifier|public
name|void
name|testContainsEntryNullNo
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|multimap
argument_list|()
operator|.
name|containsEntry
argument_list|(
literal|null
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
name|absent
operator|=
name|ALLOWS_NULL_KEY_QUERIES
argument_list|)
DECL|method|testContainsEntryNullDisallowedBecauseKeyQueriesDisallowed ()
specifier|public
name|void
name|testContainsEntryNullDisallowedBecauseKeyQueriesDisallowed
parameter_list|()
block|{
try|try
block|{
name|multimap
argument_list|()
operator|.
name|containsEntry
argument_list|(
literal|null
argument_list|,
name|v3
argument_list|()
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
block|{
comment|// success
block|}
block|}
comment|/**    * Copy of the {@link #testContainsEntryNullDisallowed} test. Needed because    * "optional" feature requirements are not supported.    */
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ALLOWS_NULL_VALUE_QUERIES
argument_list|)
DECL|method|testContainsEntryNullDisallowedBecauseValueQueriesDisallowed ()
specifier|public
name|void
name|testContainsEntryNullDisallowedBecauseValueQueriesDisallowed
parameter_list|()
block|{
try|try
block|{
name|multimap
argument_list|()
operator|.
name|containsEntry
argument_list|(
name|k3
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
block|{
comment|// success
block|}
block|}
block|}
end_class

end_unit

