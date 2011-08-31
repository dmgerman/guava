begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * A generic JUnit test which tests unconditional {@code setCount()} operations  * on a multiset. Can't be invoked directly; please see  * {@link MultisetTestSuiteBuilder}.  *  * @author Chris Povirk  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|MultisetSetCountUnconditionallyTester
specifier|public
class|class
name|MultisetSetCountUnconditionallyTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractMultisetSetCountTester
argument_list|<
name|E
argument_list|>
block|{
DECL|method|setCountCheckReturnValue (E element, int count)
annotation|@
name|Override
name|void
name|setCountCheckReturnValue
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|count
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"multiset.setCount() should return the old count"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|count
argument_list|(
name|element
argument_list|)
argument_list|,
name|setCount
argument_list|(
name|element
argument_list|,
name|count
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|setCountNoCheckReturnValue (E element, int count)
annotation|@
name|Override
name|void
name|setCountNoCheckReturnValue
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|count
parameter_list|)
block|{
name|setCount
argument_list|(
name|element
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
DECL|method|setCount (E element, int count)
specifier|private
name|int
name|setCount
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|count
parameter_list|)
block|{
return|return
name|getMultiset
argument_list|()
operator|.
name|setCount
argument_list|(
name|element
argument_list|,
name|count
argument_list|)
return|;
block|}
block|}
end_class

end_unit

