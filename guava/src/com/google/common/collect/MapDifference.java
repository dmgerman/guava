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
name|java
operator|.
name|util
operator|.
name|Map
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
comment|/**  * An object representing the differences between two maps.  *  * @author Kevin Bourrillion  * @since 2.0  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
DECL|interface|MapDifference
specifier|public
interface|interface
name|MapDifference
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
block|{
comment|/**    * Returns {@code true} if there are no differences between the two maps;    * that is, if the maps are equal.    */
DECL|method|areEqual ()
name|boolean
name|areEqual
parameter_list|()
function_decl|;
comment|/**    * Returns an unmodifiable map containing the entries from the left map whose    * keys are not present in the right map.    */
DECL|method|entriesOnlyOnLeft ()
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entriesOnlyOnLeft
parameter_list|()
function_decl|;
comment|/**    * Returns an unmodifiable map containing the entries from the right map whose    * keys are not present in the left map.    */
DECL|method|entriesOnlyOnRight ()
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entriesOnlyOnRight
parameter_list|()
function_decl|;
comment|/**    * Returns an unmodifiable map containing the entries that appear in both    * maps; that is, the intersection of the two maps.    */
DECL|method|entriesInCommon ()
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entriesInCommon
parameter_list|()
function_decl|;
comment|/**    * Returns an unmodifiable map describing keys that appear in both maps, but    * with different values.    */
DECL|method|entriesDiffering ()
name|Map
argument_list|<
name|K
argument_list|,
name|ValueDifference
argument_list|<
name|V
argument_list|>
argument_list|>
name|entriesDiffering
parameter_list|()
function_decl|;
comment|/**    * Compares the specified object with this instance for equality. Returns    * {@code true} if the given object is also a {@code MapDifference} and the    * values returned by the {@link #entriesOnlyOnLeft()}, {@link    * #entriesOnlyOnRight()}, {@link #entriesInCommon()} and {@link    * #entriesDiffering()} of the two instances are equal.    */
annotation|@
name|Override
DECL|method|equals (@ullable Object object)
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
function_decl|;
comment|/**    * Returns the hash code for this instance. This is defined as the hash code    * of<pre>   {@code    *    *   Arrays.asList(entriesOnlyOnLeft(), entriesOnlyOnRight(),    *       entriesInCommon(), entriesDiffering())}</pre>    */
annotation|@
name|Override
DECL|method|hashCode ()
name|int
name|hashCode
parameter_list|()
function_decl|;
comment|/**    * A difference between the mappings from two maps with the same key. The    * {@link #leftValue} and {@link #rightValue} are not equal, and one but not    * both of them may be null.    *    * @since 2.0    */
DECL|interface|ValueDifference
interface|interface
name|ValueDifference
parameter_list|<
name|V
parameter_list|>
block|{
comment|/**      * Returns the value from the left map (possibly null).      */
DECL|method|leftValue ()
name|V
name|leftValue
parameter_list|()
function_decl|;
comment|/**      * Returns the value from the right map (possibly null).      */
DECL|method|rightValue ()
name|V
name|rightValue
parameter_list|()
function_decl|;
comment|/**      * Two instances are considered equal if their {@link #leftValue()}      * values are equal and their {@link #rightValue()} values are also equal.      */
DECL|method|equals (@ullable Object other)
annotation|@
name|Override
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|other
parameter_list|)
function_decl|;
comment|/**      * The hash code equals the value      * {@code Arrays.asList(leftValue(), rightValue()).hashCode()}.      */
DECL|method|hashCode ()
annotation|@
name|Override
name|int
name|hashCode
parameter_list|()
function_decl|;
block|}
block|}
end_interface

end_unit

