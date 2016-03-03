begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|errorprone
operator|.
name|annotations
operator|.
name|CanIgnoreReturnValue
import|;
end_import

begin_comment
comment|/**  * A constraint that an element must satisfy in order to be added to a  * collection. For example, {@link Constraints#notNull()}, which prevents a  * collection from including any null elements, could be implemented like this:  *<pre>   {@code  *  *   public Object checkElement(Object element) {  *     if (element == null) {  *       throw new NullPointerException();  *     }  *     return element;  *   }}</pre>  *  *<p>In order to be effective, constraints should be deterministic; that is,  * they should not depend on state that can change (such as external state,  * random variables, and time) and should only depend on the value of the  * passed-in element. A non-deterministic constraint cannot reliably enforce  * that all the collection's elements meet the constraint, since the constraint  * is only enforced when elements are added.  *  * @author Mike Bostock  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
DECL|interface|Constraint
interface|interface
name|Constraint
parameter_list|<
name|E
parameter_list|>
block|{
comment|/**    * Throws a suitable {@code RuntimeException} if the specified element is    * illegal. Typically this is either a {@link NullPointerException}, an    * {@link IllegalArgumentException}, or a {@link ClassCastException}, though    * an application-specific exception class may be used if appropriate.    *    * @param element the element to check    * @return the provided element    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|checkElement (E element)
name|E
name|checkElement
parameter_list|(
name|E
name|element
parameter_list|)
function_decl|;
comment|/**    * Returns a brief human readable description of this constraint, such as    * "Not null" or "Positive number".    */
annotation|@
name|Override
DECL|method|toString ()
name|String
name|toString
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

