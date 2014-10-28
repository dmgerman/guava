begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|SEVERAL
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
name|SetMultimap
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
name|testing
operator|.
name|EqualsTester
import|;
end_import

begin_comment
comment|/**  * Testers for {@link SetMultimap#equals(Object)}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|SetMultimapEqualsTester
specifier|public
class|class
name|SetMultimapEqualsTester
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
name|SetMultimap
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
name|SEVERAL
argument_list|)
DECL|method|testOrderingDoesntAffectEqualsComparisons ()
specifier|public
name|void
name|testOrderingDoesntAffectEqualsComparisons
parameter_list|()
block|{
name|SetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|multimap1
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
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|()
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|()
argument_list|)
argument_list|,
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|()
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e1
argument_list|()
argument_list|)
argument_list|,
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|()
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e4
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|SetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|multimap2
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
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|()
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e1
argument_list|()
argument_list|)
argument_list|,
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|()
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|()
argument_list|)
argument_list|,
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|()
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e4
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|multimap1
argument_list|,
name|multimap2
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

