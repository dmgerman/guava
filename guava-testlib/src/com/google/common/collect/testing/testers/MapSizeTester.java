begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * A generic JUnit test which tests {@code size()} operations on a map.  * Can't be invoked directly; please see  * {@link com.google.common.collect.testing.MapTestSuiteBuilder}.  *  *<p>This class is GWT compatible.  *  * @author George van den Driessche  */
end_comment

begin_class
DECL|class|MapSizeTester
specifier|public
class|class
name|MapSizeTester
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
DECL|method|testSize ()
specifier|public
name|void
name|testSize
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"size():"
argument_list|,
name|getNumElements
argument_list|()
argument_list|,
name|getMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

