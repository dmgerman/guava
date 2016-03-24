begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing
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
name|GwtIncompatible
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
name|testers
operator|.
name|QueueElementTester
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
name|testers
operator|.
name|QueueOfferTester
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
name|testers
operator|.
name|QueuePeekTester
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
name|testers
operator|.
name|QueuePollTester
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
name|testers
operator|.
name|QueueRemoveTester
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

begin_comment
comment|/**  * Creates, based on your criteria, a JUnit test suite that exhaustively tests  * a queue implementation.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|QueueTestSuiteBuilder
specifier|public
specifier|final
class|class
name|QueueTestSuiteBuilder
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractCollectionTestSuiteBuilder
argument_list|<
name|QueueTestSuiteBuilder
argument_list|<
name|E
argument_list|>
argument_list|,
name|E
argument_list|>
block|{
DECL|method|using (TestQueueGenerator<E> generator)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|QueueTestSuiteBuilder
argument_list|<
name|E
argument_list|>
name|using
parameter_list|(
name|TestQueueGenerator
argument_list|<
name|E
argument_list|>
name|generator
parameter_list|)
block|{
return|return
operator|new
name|QueueTestSuiteBuilder
argument_list|<
name|E
argument_list|>
argument_list|()
operator|.
name|usingGenerator
argument_list|(
name|generator
argument_list|)
return|;
block|}
DECL|field|runCollectionTests
specifier|private
name|boolean
name|runCollectionTests
init|=
literal|true
decl_stmt|;
comment|/**    * Specify whether to skip the general collection tests. Call this method when    * testing a collection that's both a queue and a list, to avoid running the    * common collection tests twice. By default, collection tests do run.    */
DECL|method|skipCollectionTests ()
specifier|public
name|QueueTestSuiteBuilder
argument_list|<
name|E
argument_list|>
name|skipCollectionTests
parameter_list|()
block|{
name|runCollectionTests
operator|=
literal|false
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|getTesters ()
specifier|protected
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|AbstractTester
argument_list|>
argument_list|>
name|getTesters
parameter_list|()
block|{
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|AbstractTester
argument_list|>
argument_list|>
name|testers
init|=
operator|new
name|ArrayList
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|AbstractTester
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|runCollectionTests
condition|)
block|{
name|testers
operator|.
name|addAll
argument_list|(
name|super
operator|.
name|getTesters
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|testers
operator|.
name|add
argument_list|(
name|QueueElementTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|QueueOfferTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|QueuePeekTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|QueuePollTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|QueueRemoveTester
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|testers
return|;
block|}
block|}
end_class

end_unit

