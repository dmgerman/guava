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
name|features
operator|.
name|CollectionSize
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
comment|/**  * The subject-generator interface accepted by Collection testers, for testing  * a Collection at one particular {@link CollectionSize}.  *  *<p>This interface should not be implemented outside this package;  * {@link PerCollectionSizeTestSuiteBuilder} constructs instances of it from  * a more general {@link TestCollectionGenerator}.  *  *<p>This class is GWT compatible.  *  * @author George van den Driessche  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
DECL|interface|OneSizeTestContainerGenerator
specifier|public
interface|interface
name|OneSizeTestContainerGenerator
parameter_list|<
name|T
parameter_list|,
name|E
parameter_list|>
extends|extends
name|TestSubjectGenerator
argument_list|<
name|T
argument_list|>
extends|,
name|TestContainerGenerator
argument_list|<
name|T
argument_list|,
name|E
argument_list|>
block|{
DECL|method|getInnerGenerator ()
name|TestContainerGenerator
argument_list|<
name|T
argument_list|,
name|E
argument_list|>
name|getInnerGenerator
parameter_list|()
function_decl|;
DECL|method|getSampleElements (int howMany)
name|Collection
argument_list|<
name|E
argument_list|>
name|getSampleElements
parameter_list|(
name|int
name|howMany
parameter_list|)
function_decl|;
DECL|method|getCollectionSize ()
name|CollectionSize
name|getCollectionSize
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

