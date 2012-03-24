begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|NoSuchElementException
import|;
end_import

begin_comment
comment|/**  * A descriptor for a<i>discrete</i> {@code Comparable} domain such as all  * {@link Integer}s. A discrete domain is one that supports the three basic  * operations: {@link #next}, {@link #previous} and {@link #distance}, according  * to their specifications. The methods {@link #minValue} and {@link #maxValue}  * should also be overridden for bounded types.  *  *<p>A discrete domain always represents the<i>entire</i> set of values of its  * type; it cannot represent partial domains such as "prime integers" or  * "strings of length 5."  *  *<p>See the Guava User Guide section on<a href=  * "http://code.google.com/p/guava-libraries/wiki/RangesExplained#Discrete_Domains">  * {@code DiscreteDomain}</a>.  *  * @author Kevin Bourrillion  * @since 10.0  * @see DiscreteDomains  */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|Beta
DECL|class|DiscreteDomain
specifier|public
specifier|abstract
class|class
name|DiscreteDomain
parameter_list|<
name|C
extends|extends
name|Comparable
parameter_list|>
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|DiscreteDomain ()
specifier|protected
name|DiscreteDomain
parameter_list|()
block|{}
comment|/**    * Returns the unique least value of type {@code C} that is greater than    * {@code value}, or {@code null} if none exists. Inverse operation to {@link    * #previous}.    *    * @param value any value of type {@code C}    * @return the least value greater than {@code value}, or {@code null} if    *     {@code value} is {@code maxValue()}    */
DECL|method|next (C value)
specifier|public
specifier|abstract
name|C
name|next
parameter_list|(
name|C
name|value
parameter_list|)
function_decl|;
comment|/**    * Returns the unique greatest value of type {@code C} that is less than    * {@code value}, or {@code null} if none exists. Inverse operation to {@link    * #next}.    *    * @param value any value of type {@code C}    * @return the greatest value less than {@code value}, or {@code null} if    *     {@code value} is {@code minValue()}    */
DECL|method|previous (C value)
specifier|public
specifier|abstract
name|C
name|previous
parameter_list|(
name|C
name|value
parameter_list|)
function_decl|;
comment|/**    * Returns a signed value indicating how many nested invocations of {@link    * #next} (if positive) or {@link #previous} (if negative) are needed to reach    * {@code end} starting from {@code start}. For example, if {@code end =    * next(next(next(start)))}, then {@code distance(start, end) == 3} and {@code    * distance(end, start) == -3}. As well, {@code distance(a, a)} is always    * zero.    *    *<p>Note that this function is necessarily well-defined for any discrete    * type.    *    * @return the distance as described above, or {@link Long#MIN_VALUE} or    *     {@link Long#MAX_VALUE} if the distance is too small or too large,    *     respectively.    */
DECL|method|distance (C start, C end)
specifier|public
specifier|abstract
name|long
name|distance
parameter_list|(
name|C
name|start
parameter_list|,
name|C
name|end
parameter_list|)
function_decl|;
comment|/**    * Returns the minimum value of type {@code C}, if it has one. The minimum    * value is the unique value for which {@link Comparable#compareTo(Object)}    * never returns a positive value for any input of type {@code C}.    *    *<p>The default implementation throws {@code NoSuchElementException}.    *    * @return the minimum value of type {@code C}; never null    * @throws NoSuchElementException if the type has no (practical) minimum    *     value; for example, {@link java.math.BigInteger}    */
DECL|method|minValue ()
specifier|public
name|C
name|minValue
parameter_list|()
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
comment|/**    * Returns the maximum value of type {@code C}, if it has one. The maximum    * value is the unique value for which {@link Comparable#compareTo(Object)}    * never returns a negative value for any input of type {@code C}.    *    *<p>The default implementation throws {@code NoSuchElementException}.    *    * @return the maximum value of type {@code C}; never null    * @throws NoSuchElementException if the type has no (practical) maximum    *     value; for example, {@link java.math.BigInteger}    */
DECL|method|maxValue ()
specifier|public
name|C
name|maxValue
parameter_list|()
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
block|}
end_class

end_unit

