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
name|java
operator|.
name|util
operator|.
name|Collection
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * To be implemented by test generators of things that can contain  * elements. Such things include both {@link Collection} and {@link Map}; since  * there isn't an established collective noun that encompasses both of these,  * 'container' is used.  *  *<p>This class is GWT compatible.  *  * @author George van den Driessche  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
DECL|interface|TestContainerGenerator
specifier|public
interface|interface
name|TestContainerGenerator
parameter_list|<
name|T
parameter_list|,
name|E
parameter_list|>
block|{
comment|/**    * Returns the sample elements that this generate populates its container    * with.    */
DECL|method|samples ()
name|SampleElements
argument_list|<
name|E
argument_list|>
name|samples
parameter_list|()
function_decl|;
comment|/**    * Creates a new container containing the given elements. TODO: would be nice    * to figure out how to use E... or E[] as a parameter type, but this doesn't    * seem to work because Java creates an array of the erased type.    */
DECL|method|create (Object .... elements)
name|T
name|create
parameter_list|(
name|Object
modifier|...
name|elements
parameter_list|)
function_decl|;
comment|/**    * Helper method to create an array of the appropriate type used by this    * generator. The returned array will contain only nulls.    */
DECL|method|createArray (int length)
name|E
index|[]
name|createArray
parameter_list|(
name|int
name|length
parameter_list|)
function_decl|;
comment|/**    * Returns the iteration ordering of elements, given the order in    * which they were added to the container. This method may return the    * original list unchanged, the original list modified in place, or a    * different list.    *    *<p>This method runs only when {@link    * com.google.common.collect.testing.features.CollectionFeature#KNOWN_ORDER}    * is specified when creating the test suite. It should never run when testing    * containers such as {@link java.util.HashSet}, which have a    * non-deterministic iteration order.    */
DECL|method|order (List<E> insertionOrder)
name|Iterable
argument_list|<
name|E
argument_list|>
name|order
parameter_list|(
name|List
argument_list|<
name|E
argument_list|>
name|insertionOrder
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

