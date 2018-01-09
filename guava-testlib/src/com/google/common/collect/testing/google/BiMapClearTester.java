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
name|MapFeature
operator|.
name|SUPPORTS_REMOVE
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
name|BiMap
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
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_comment
comment|/**  * Tester for {@code BiMap.clear}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|Ignore
comment|// Affects only Android test runner, which respects JUnit 4 annotations on JUnit 3 tests.
DECL|class|BiMapClearTester
specifier|public
class|class
name|BiMapClearTester
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractBiMapTester
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testClearClearsInverse ()
specifier|public
name|void
name|testClearClearsInverse
parameter_list|()
block|{
name|BiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inv
init|=
name|getMap
argument_list|()
operator|.
name|inverse
argument_list|()
decl_stmt|;
name|getMap
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|getMap
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|inv
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testKeySetClearClearsInverse ()
specifier|public
name|void
name|testKeySetClearClearsInverse
parameter_list|()
block|{
name|BiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inv
init|=
name|getMap
argument_list|()
operator|.
name|inverse
argument_list|()
decl_stmt|;
name|getMap
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|getMap
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|inv
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testValuesClearClearsInverse ()
specifier|public
name|void
name|testValuesClearClearsInverse
parameter_list|()
block|{
name|BiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inv
init|=
name|getMap
argument_list|()
operator|.
name|inverse
argument_list|()
decl_stmt|;
name|getMap
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|getMap
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|inv
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testClearInverseClears ()
specifier|public
name|void
name|testClearInverseClears
parameter_list|()
block|{
name|BiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inv
init|=
name|getMap
argument_list|()
operator|.
name|inverse
argument_list|()
decl_stmt|;
name|inv
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|getMap
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|inv
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testClearInverseKeySetClears ()
specifier|public
name|void
name|testClearInverseKeySetClears
parameter_list|()
block|{
name|BiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inv
init|=
name|getMap
argument_list|()
operator|.
name|inverse
argument_list|()
decl_stmt|;
name|inv
operator|.
name|keySet
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|getMap
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|inv
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testClearInverseValuesClears ()
specifier|public
name|void
name|testClearInverseValuesClears
parameter_list|()
block|{
name|BiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inv
init|=
name|getMap
argument_list|()
operator|.
name|inverse
argument_list|()
decl_stmt|;
name|inv
operator|.
name|values
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|getMap
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|inv
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

