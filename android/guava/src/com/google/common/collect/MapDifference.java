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
name|DoNotMock
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
name|CheckForNull
import|;
end_import

begin_import
import|import
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|qual
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * An object representing the differences between two maps.  *  * @author Kevin Bourrillion  * @since 2.0  */
end_comment

begin_annotation
annotation|@
name|DoNotMock
argument_list|(
literal|"Use Maps.difference"
argument_list|)
end_annotation

begin_annotation
annotation|@
name|GwtCompatible
end_annotation

begin_annotation
annotation|@
name|ElementTypesAreNonnullByDefault
end_annotation

begin_expr_stmt
DECL|interface|MapDifference
specifier|public
expr|interface
name|MapDifference
operator|<
name|K
expr|extends @
name|Nullable
name|Object
operator|,
name|V
expr|extends @
name|Nullable
name|Object
operator|>
block|{
comment|/**    * Returns {@code true} if there are no differences between the two maps; that is, if the maps are    * equal.    */
DECL|method|areEqual ()
name|boolean
name|areEqual
argument_list|()
block|;
comment|/**    * Returns an unmodifiable map containing the entries from the left map whose keys are not present    * in the right map.    */
DECL|method|entriesOnlyOnLeft ()
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entriesOnlyOnLeft
argument_list|()
block|;
comment|/**    * Returns an unmodifiable map containing the entries from the right map whose keys are not    * present in the left map.    */
DECL|method|entriesOnlyOnRight ()
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entriesOnlyOnRight
argument_list|()
block|;
comment|/**    * Returns an unmodifiable map containing the entries that appear in both maps; that is, the    * intersection of the two maps.    */
DECL|method|entriesInCommon ()
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entriesInCommon
argument_list|()
block|;
comment|/**    * Returns an unmodifiable map describing keys that appear in both maps, but with different    * values.    */
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
argument_list|()
block|;
comment|/**    * Compares the specified object with this instance for equality. Returns {@code true} if the    * given object is also a {@code MapDifference} and the values returned by the {@link    * #entriesOnlyOnLeft()}, {@link #entriesOnlyOnRight()}, {@link #entriesInCommon()} and {@link    * #entriesDiffering()} of the two instances are equal.    */
block|@
name|Override
DECL|method|equals (@heckForNull Object object)
name|boolean
name|equals
argument_list|(
annotation|@
name|CheckForNull
name|Object
name|object
argument_list|)
block|;
comment|/**    * Returns the hash code for this instance. This is defined as the hash code of    *    *<pre>{@code    * Arrays.asList(entriesOnlyOnLeft(), entriesOnlyOnRight(),    *     entriesInCommon(), entriesDiffering())    * }</pre>    */
block|@
name|Override
DECL|method|hashCode ()
name|int
name|hashCode
argument_list|()
block|;
comment|/**    * A difference between the mappings from two maps with the same key. The {@link #leftValue} and    * {@link #rightValue} are not equal, and one but not both of them may be null.    *    * @since 2.0    */
block|@
name|DoNotMock
argument_list|(
literal|"Use Maps.difference"
argument_list|)
DECL|interface|ValueDifference
expr|interface
name|ValueDifference
operator|<
name|V
expr|extends @
name|Nullable
name|Object
operator|>
block|{
comment|/** Returns the value from the left map (possibly null). */
block|@
name|ParametricNullness
DECL|method|leftValue ()
name|V
name|leftValue
argument_list|()
block|;
comment|/** Returns the value from the right map (possibly null). */
block|@
name|ParametricNullness
DECL|method|rightValue ()
name|V
name|rightValue
argument_list|()
block|;
comment|/**      * Two instances are considered equal if their {@link #leftValue()} values are equal and their      * {@link #rightValue()} values are also equal.      */
block|@
name|Override
DECL|method|equals (@heckForNull Object other)
name|boolean
name|equals
argument_list|(
annotation|@
name|CheckForNull
name|Object
name|other
argument_list|)
block|;
comment|/**      * The hash code equals the value {@code Arrays.asList(leftValue(), rightValue()).hashCode()}.      */
block|@
name|Override
DECL|method|hashCode ()
name|int
name|hashCode
argument_list|()
block|;   }
block|}
end_expr_stmt

end_unit

