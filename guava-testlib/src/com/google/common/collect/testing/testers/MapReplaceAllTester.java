begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2016 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|KNOWN_ORDER
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
name|SampleElements
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
name|ArrayList
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
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
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
comment|/**  * A generic JUnit test which tests {@code replaceAll()} operations on a map. Can't be invoked  * directly; please see {@link com.google.common.collect.testing.MapTestSuiteBuilder}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|Ignore
comment|// Affects only Android test runner, which respects JUnit 4 annotations on JUnit 3 tests.
DECL|class|MapReplaceAllTester
specifier|public
class|class
name|MapReplaceAllTester
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
DECL|method|keys ()
specifier|private
name|SampleElements
argument_list|<
name|K
argument_list|>
name|keys
parameter_list|()
block|{
return|return
operator|new
name|SampleElements
argument_list|<
name|K
argument_list|>
argument_list|(
name|k0
argument_list|()
argument_list|,
name|k1
argument_list|()
argument_list|,
name|k2
argument_list|()
argument_list|,
name|k3
argument_list|()
argument_list|,
name|k4
argument_list|()
argument_list|)
return|;
block|}
DECL|method|values ()
specifier|private
name|SampleElements
argument_list|<
name|V
argument_list|>
name|values
parameter_list|()
block|{
return|return
operator|new
name|SampleElements
argument_list|<
name|V
argument_list|>
argument_list|(
name|v0
argument_list|()
argument_list|,
name|v1
argument_list|()
argument_list|,
name|v2
argument_list|()
argument_list|,
name|v3
argument_list|()
argument_list|,
name|v4
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_PUT
argument_list|)
DECL|method|testReplaceAllRotate ()
specifier|public
name|void
name|testReplaceAllRotate
parameter_list|()
block|{
name|getMap
argument_list|()
operator|.
name|replaceAll
argument_list|(
parameter_list|(
name|K
name|k
parameter_list|,
name|V
name|v
parameter_list|)
lambda|->
block|{
name|int
name|index
init|=
name|keys
argument_list|()
operator|.
name|asList
argument_list|()
operator|.
name|indexOf
argument_list|(
name|k
argument_list|)
decl_stmt|;
return|return
name|values
argument_list|()
operator|.
name|asList
argument_list|()
operator|.
name|get
argument_list|(
name|index
operator|+
literal|1
argument_list|)
return|;
block|}
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|expectedEntries
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
range|:
name|getSampleEntries
argument_list|()
control|)
block|{
name|int
name|index
init|=
name|keys
argument_list|()
operator|.
name|asList
argument_list|()
operator|.
name|indexOf
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|expectedEntries
operator|.
name|add
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|values
argument_list|()
operator|.
name|asList
argument_list|()
operator|.
name|get
argument_list|(
name|index
operator|+
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|expectContents
argument_list|(
name|expectedEntries
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
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|KNOWN_ORDER
argument_list|)
DECL|method|testReplaceAllPreservesOrder ()
specifier|public
name|void
name|testReplaceAllPreservesOrder
parameter_list|()
block|{
name|getMap
argument_list|()
operator|.
name|replaceAll
argument_list|(
parameter_list|(
name|K
name|k
parameter_list|,
name|V
name|v
parameter_list|)
lambda|->
block|{
name|int
name|index
init|=
name|keys
argument_list|()
operator|.
name|asList
argument_list|()
operator|.
name|indexOf
argument_list|(
name|k
argument_list|)
decl_stmt|;
return|return
name|values
argument_list|()
operator|.
name|asList
argument_list|()
operator|.
name|get
argument_list|(
name|index
operator|+
literal|1
argument_list|)
return|;
block|}
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|orderedEntries
init|=
name|getOrderedElements
argument_list|()
decl_stmt|;
name|int
name|index
init|=
literal|0
decl_stmt|;
for|for
control|(
name|K
name|key
range|:
name|getMap
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
name|assertEquals
argument_list|(
name|orderedEntries
operator|.
name|get
argument_list|(
name|index
argument_list|)
operator|.
name|getKey
argument_list|()
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|index
operator|++
expr_stmt|;
block|}
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
DECL|method|testReplaceAll_unsupported ()
specifier|public
name|void
name|testReplaceAll_unsupported
parameter_list|()
block|{
try|try
block|{
name|getMap
argument_list|()
operator|.
name|replaceAll
argument_list|(
parameter_list|(
name|K
name|k
parameter_list|,
name|V
name|v
parameter_list|)
lambda|->
block|{
name|int
name|index
init|=
name|keys
argument_list|()
operator|.
name|asList
argument_list|()
operator|.
name|indexOf
argument_list|(
name|k
argument_list|)
decl_stmt|;
return|return
name|values
argument_list|()
operator|.
name|asList
argument_list|()
operator|.
name|get
argument_list|(
name|index
operator|+
literal|1
argument_list|)
return|;
block|}
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"replaceAll() should throw UnsupportedOperation if a map does "
operator|+
literal|"not support it and is not empty."
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
name|ZERO
argument_list|)
DECL|method|testReplaceAll_unsupportedByEmptyCollection ()
specifier|public
name|void
name|testReplaceAll_unsupportedByEmptyCollection
parameter_list|()
block|{
try|try
block|{
name|getMap
argument_list|()
operator|.
name|replaceAll
argument_list|(
parameter_list|(
name|K
name|k
parameter_list|,
name|V
name|v
parameter_list|)
lambda|->
block|{
name|int
name|index
init|=
name|keys
argument_list|()
operator|.
name|asList
argument_list|()
operator|.
name|indexOf
argument_list|(
name|k
argument_list|)
decl_stmt|;
return|return
name|values
argument_list|()
operator|.
name|asList
argument_list|()
operator|.
name|get
argument_list|(
name|index
operator|+
literal|1
argument_list|)
return|;
block|}
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
name|MapFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_PUT
argument_list|)
DECL|method|testReplaceAll_unsupportedNoOpFunction ()
specifier|public
name|void
name|testReplaceAll_unsupportedNoOpFunction
parameter_list|()
block|{
try|try
block|{
name|getMap
argument_list|()
operator|.
name|replaceAll
argument_list|(
parameter_list|(
name|K
name|k
parameter_list|,
name|V
name|v
parameter_list|)
lambda|->
name|v
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
block|}
end_class

end_unit

