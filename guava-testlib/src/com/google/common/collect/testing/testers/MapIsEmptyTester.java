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

begin_comment
comment|/**  * A generic JUnit test which tests {@code isEmpty()} operations on a  * map. Can't be invoked directly; please see  * {@link com.google.common.collect.testing.MapTestSuiteBuilder}.  *  *<p>This class is GWT compatible.  *  * @author Kevin Bourrillion  */
end_comment

begin_class
DECL|class|MapIsEmptyTester
specifier|public
class|class
name|MapIsEmptyTester
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
name|ZERO
argument_list|)
DECL|method|testIsEmpty_yes ()
specifier|public
name|void
name|testIsEmpty_yes
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"isEmpty() should return true"
argument_list|,
name|getMap
argument_list|()
operator|.
name|isEmpty
argument_list|()
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
DECL|method|testIsEmpty_no ()
specifier|public
name|void
name|testIsEmpty_no
parameter_list|()
block|{
name|assertFalse
argument_list|(
literal|"isEmpty() should return false"
argument_list|,
name|getMap
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

