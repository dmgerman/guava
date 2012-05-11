begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing.features
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
name|features
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * Base class for enumerating the features of an interface to be tested.  *  *<p>This class is GWT compatible.  *  * @param<T> The interface whose features are to be enumerated.  * @author George van den Driessche  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
DECL|interface|Feature
specifier|public
interface|interface
name|Feature
parameter_list|<
name|T
parameter_list|>
block|{
comment|/** Returns the set of features that are implied by this feature. */
DECL|method|getImpliedFeatures ()
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
super|super
name|T
argument_list|>
argument_list|>
name|getImpliedFeatures
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

