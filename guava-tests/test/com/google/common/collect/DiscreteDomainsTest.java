begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
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
name|testing
operator|.
name|SerializableTester
operator|.
name|reserializeAndAssert
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
name|GwtIncompatible
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Tests for {@link DiscreteDomains}.  *  * @author Chris Povirk  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
argument_list|(
literal|"SerializableTester"
argument_list|)
DECL|class|DiscreteDomainsTest
specifier|public
class|class
name|DiscreteDomainsTest
extends|extends
name|TestCase
block|{
DECL|method|testSerialization ()
specifier|public
name|void
name|testSerialization
parameter_list|()
block|{
name|reserializeAndAssert
argument_list|(
name|DiscreteDomains
operator|.
name|integers
argument_list|()
argument_list|)
expr_stmt|;
name|reserializeAndAssert
argument_list|(
name|DiscreteDomains
operator|.
name|longs
argument_list|()
argument_list|)
expr_stmt|;
name|reserializeAndAssert
argument_list|(
name|DiscreteDomains
operator|.
name|bigIntegers
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

