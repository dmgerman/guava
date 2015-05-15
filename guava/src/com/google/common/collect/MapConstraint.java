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
name|Beta
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
name|GwtCompatible
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * A constraint on the keys and values that may be added to a {@code Map} or  * {@code Multimap}. For example, {@link MapConstraints#notNull()}, which  * prevents a map from including any null keys or values, could be implemented  * like this:<pre>   {@code  *  *   public void checkKeyValue(Object key, Object value) {  *     if (key == null || value == null) {  *       throw new NullPointerException();  *     }  *   }}</pre>  *  *<p>In order to be effective, constraints should be deterministic; that is, they  * should not depend on state that can change (such as external state, random  * variables, and time) and should only depend on the value of the passed-in key  * and value. A non-deterministic constraint cannot reliably enforce that all  * the collection's elements meet the constraint, since the constraint is only  * enforced when elements are added.  *  * @author Mike Bostock  * @see MapConstraints  * @see Constraint  * @since 3.0  * @deprecated Use {@link Preconditions} for basic checks. In place of  *     constrained maps, we encourage you to check your preconditions  *     explicitly instead of leaving that work to the map implementation.  *     For the specific case of rejecting null, consider {@link ImmutableMap}.  *     This class is scheduled for removal in Guava 20.0.  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
annotation|@
name|Beta
annotation|@
name|Deprecated
DECL|interface|MapConstraint
specifier|public
interface|interface
name|MapConstraint
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
block|{
comment|/**    * Throws a suitable {@code RuntimeException} if the specified key or value is    * illegal. Typically this is either a {@link NullPointerException}, an    * {@link IllegalArgumentException}, or a {@link ClassCastException}, though    * an application-specific exception class may be used if appropriate.    */
DECL|method|checkKeyValue (@ullable K key, @Nullable V value)
name|void
name|checkKeyValue
parameter_list|(
annotation|@
name|Nullable
name|K
name|key
parameter_list|,
annotation|@
name|Nullable
name|V
name|value
parameter_list|)
function_decl|;
comment|/**    * Returns a brief human readable description of this constraint, such as    * "Not null".    */
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

