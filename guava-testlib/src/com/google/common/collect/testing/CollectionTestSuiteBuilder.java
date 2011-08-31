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

begin_comment
comment|/**  * Concrete instantiation of {@link AbstractCollectionTestSuiteBuilder} for  * testing collections that do not have a more specific tester like  * {@link ListTestSuiteBuilder} or {@link SetTestSuiteBuilder}.  *  * @author Chris Povirk  */
end_comment

begin_class
DECL|class|CollectionTestSuiteBuilder
specifier|public
class|class
name|CollectionTestSuiteBuilder
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractCollectionTestSuiteBuilder
argument_list|<
name|CollectionTestSuiteBuilder
argument_list|<
name|E
argument_list|>
argument_list|,
name|E
argument_list|>
block|{
DECL|method|using ( TestCollectionGenerator<E> generator)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|CollectionTestSuiteBuilder
argument_list|<
name|E
argument_list|>
name|using
parameter_list|(
name|TestCollectionGenerator
argument_list|<
name|E
argument_list|>
name|generator
parameter_list|)
block|{
return|return
operator|new
name|CollectionTestSuiteBuilder
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
block|}
end_class

end_unit

