begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|SUPPORTS_PUT
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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|MapFeature
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_comment
comment|/**  * Tester for {@link Multimap#putAll(Multimap)}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|MultimapPutAllMultimapTester
specifier|public
class|class
name|MultimapPutAllMultimapTester
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
name|MapFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_PUT
argument_list|)
DECL|method|testPutUnsupported ()
specifier|public
name|void
name|testPutUnsupported
parameter_list|()
block|{
try|try
block|{
name|multimap
argument_list|()
operator|.
name|putAll
argument_list|(
name|getSubjectGenerator
argument_list|()
operator|.
name|create
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|k3
argument_list|()
argument_list|,
name|v3
argument_list|()
argument_list|)
argument_list|)
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
block|{}
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_PUT
argument_list|)
DECL|method|testPutAllIntoEmpty ()
specifier|public
name|void
name|testPutAllIntoEmpty
parameter_list|()
block|{
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|target
init|=
name|getSubjectGenerator
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
operator|!
name|multimap
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|,
name|target
operator|.
name|putAll
argument_list|(
name|multimap
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|multimap
argument_list|()
argument_list|,
name|target
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
DECL|method|testPutAll ()
specifier|public
name|void
name|testPutAll
parameter_list|()
block|{
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|source
init|=
name|getSubjectGenerator
argument_list|()
operator|.
name|create
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|k0
argument_list|()
argument_list|,
name|v3
argument_list|()
argument_list|)
argument_list|,
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|k3
argument_list|()
argument_list|,
name|v3
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|multimap
argument_list|()
operator|.
name|putAll
argument_list|(
name|source
argument_list|)
argument_list|)
expr_stmt|;
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
name|v3
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
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
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
block|{
name|SUPPORTS_PUT
block|,
name|ALLOWS_NULL_VALUES
block|}
argument_list|)
DECL|method|testPutAllWithNullValue ()
specifier|public
name|void
name|testPutAllWithNullValue
parameter_list|()
block|{
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|source
init|=
name|getSubjectGenerator
argument_list|()
operator|.
name|create
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|k0
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|multimap
argument_list|()
operator|.
name|putAll
argument_list|(
name|source
argument_list|)
argument_list|)
expr_stmt|;
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
name|SUPPORTS_PUT
block|,
name|ALLOWS_NULL_KEYS
block|}
argument_list|)
DECL|method|testPutAllWithNullKey ()
specifier|public
name|void
name|testPutAllWithNullKey
parameter_list|()
block|{
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|source
init|=
name|getSubjectGenerator
argument_list|()
operator|.
name|create
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
literal|null
argument_list|,
name|v0
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|multimap
argument_list|()
operator|.
name|putAll
argument_list|(
name|source
argument_list|)
argument_list|)
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
name|v0
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
name|value
operator|=
name|SUPPORTS_PUT
argument_list|,
name|absent
operator|=
name|ALLOWS_NULL_VALUES
argument_list|)
DECL|method|testPutAllRejectsNullValue ()
specifier|public
name|void
name|testPutAllRejectsNullValue
parameter_list|()
block|{
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|source
init|=
name|getSubjectGenerator
argument_list|()
operator|.
name|create
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|k0
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|multimap
argument_list|()
operator|.
name|putAll
argument_list|(
name|source
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
block|{}
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
name|ALLOWS_NULL_KEYS
argument_list|)
DECL|method|testPutAllRejectsNullKey ()
specifier|public
name|void
name|testPutAllRejectsNullKey
parameter_list|()
block|{
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|source
init|=
name|getSubjectGenerator
argument_list|()
operator|.
name|create
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
literal|null
argument_list|,
name|v0
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|multimap
argument_list|()
operator|.
name|putAll
argument_list|(
name|source
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
block|{}
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
DECL|method|testPutAllPropagatesToGet ()
specifier|public
name|void
name|testPutAllPropagatesToGet
parameter_list|()
block|{
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|source
init|=
name|getSubjectGenerator
argument_list|()
operator|.
name|create
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|k0
argument_list|()
argument_list|,
name|v3
argument_list|()
argument_list|)
argument_list|,
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|k3
argument_list|()
argument_list|,
name|v3
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|V
argument_list|>
name|getCollection
init|=
name|multimap
argument_list|()
operator|.
name|get
argument_list|(
name|k0
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|getCollectionSize
init|=
name|getCollection
operator|.
name|size
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|multimap
argument_list|()
operator|.
name|putAll
argument_list|(
name|source
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|getCollectionSize
operator|+
literal|1
argument_list|,
name|getCollection
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getCollection
argument_list|)
operator|.
name|contains
argument_list|(
name|v3
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

