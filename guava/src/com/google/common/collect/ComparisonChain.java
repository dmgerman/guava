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
name|primitives
operator|.
name|Booleans
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
name|primitives
operator|.
name|Ints
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
name|primitives
operator|.
name|Longs
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|CheckReturnValue
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
comment|/**  * A utility for performing a chained comparison statement. For example:  *<pre>   {@code  *  *   public int compareTo(Foo that) {  *     return ComparisonChain.start()  *         .compare(this.aString, that.aString)  *         .compare(this.anInt, that.anInt)  *         .compare(this.anEnum, that.anEnum, Ordering.natural().nullsLast())  *         .result();  *   }}</pre>  *  *<p>The value of this expression will have the same sign as the<i>first  * nonzero</i> comparison result in the chain, or will be zero if every  * comparison result was zero.  *  *<p><b>Note:</b> {@code ComparisonChain} instances are<b>immutable</b>. For  * this utility to work correctly, calls must be chained as illustrated above.  *  *<p>Performance note: Even though the {@code ComparisonChain} caller always  * invokes its {@code compare} methods unconditionally, the {@code  * ComparisonChain} implementation stops calling its inputs' {@link  * Comparable#compareTo compareTo} and {@link Comparator#compare compare}  * methods as soon as one of them returns a nonzero result. This optimization is  * typically important only in the presence of expensive {@code compareTo} and  * {@code compare} implementations.  *  *<p>See the Guava User Guide article on<a href=  * "http://code.google.com/p/guava-libraries/wiki/CommonObjectUtilitiesExplained#compare/compareTo">  * {@code ComparisonChain}</a>.  *  * @author Mark Davis  * @author Kevin Bourrillion  * @since 2.0  */
end_comment

begin_class
annotation|@
name|CheckReturnValue
annotation|@
name|GwtCompatible
DECL|class|ComparisonChain
specifier|public
specifier|abstract
class|class
name|ComparisonChain
block|{
DECL|method|ComparisonChain ()
specifier|private
name|ComparisonChain
parameter_list|()
block|{}
comment|/**    * Begins a new chained comparison statement. See example in the class    * documentation.    */
DECL|method|start ()
specifier|public
specifier|static
name|ComparisonChain
name|start
parameter_list|()
block|{
return|return
name|ACTIVE
return|;
block|}
DECL|field|ACTIVE
specifier|private
specifier|static
specifier|final
name|ComparisonChain
name|ACTIVE
init|=
operator|new
name|ComparisonChain
argument_list|()
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
name|ComparisonChain
name|compare
parameter_list|(
name|Comparable
name|left
parameter_list|,
name|Comparable
name|right
parameter_list|)
block|{
return|return
name|classify
argument_list|(
name|left
operator|.
name|compareTo
argument_list|(
name|right
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|ComparisonChain
name|compare
parameter_list|(
annotation|@
name|Nullable
name|T
name|left
parameter_list|,
annotation|@
name|Nullable
name|T
name|right
parameter_list|,
name|Comparator
argument_list|<
name|T
argument_list|>
name|comparator
parameter_list|)
block|{
return|return
name|classify
argument_list|(
name|comparator
operator|.
name|compare
argument_list|(
name|left
argument_list|,
name|right
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ComparisonChain
name|compare
parameter_list|(
name|int
name|left
parameter_list|,
name|int
name|right
parameter_list|)
block|{
return|return
name|classify
argument_list|(
name|Ints
operator|.
name|compare
argument_list|(
name|left
argument_list|,
name|right
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ComparisonChain
name|compare
parameter_list|(
name|long
name|left
parameter_list|,
name|long
name|right
parameter_list|)
block|{
return|return
name|classify
argument_list|(
name|Longs
operator|.
name|compare
argument_list|(
name|left
argument_list|,
name|right
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ComparisonChain
name|compare
parameter_list|(
name|float
name|left
parameter_list|,
name|float
name|right
parameter_list|)
block|{
return|return
name|classify
argument_list|(
name|Float
operator|.
name|compare
argument_list|(
name|left
argument_list|,
name|right
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ComparisonChain
name|compare
parameter_list|(
name|double
name|left
parameter_list|,
name|double
name|right
parameter_list|)
block|{
return|return
name|classify
argument_list|(
name|Double
operator|.
name|compare
argument_list|(
name|left
argument_list|,
name|right
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ComparisonChain
name|compareTrueFirst
parameter_list|(
name|boolean
name|left
parameter_list|,
name|boolean
name|right
parameter_list|)
block|{
return|return
name|classify
argument_list|(
name|Booleans
operator|.
name|compare
argument_list|(
name|right
argument_list|,
name|left
argument_list|)
argument_list|)
return|;
comment|// reversed
block|}
annotation|@
name|Override
specifier|public
name|ComparisonChain
name|compareFalseFirst
parameter_list|(
name|boolean
name|left
parameter_list|,
name|boolean
name|right
parameter_list|)
block|{
return|return
name|classify
argument_list|(
name|Booleans
operator|.
name|compare
argument_list|(
name|left
argument_list|,
name|right
argument_list|)
argument_list|)
return|;
block|}
name|ComparisonChain
name|classify
parameter_list|(
name|int
name|result
parameter_list|)
block|{
return|return
operator|(
name|result
operator|<
literal|0
operator|)
condition|?
name|LESS
else|:
operator|(
name|result
operator|>
literal|0
operator|)
condition|?
name|GREATER
else|:
name|ACTIVE
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|result
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
block|}
decl_stmt|;
DECL|field|LESS
specifier|private
specifier|static
specifier|final
name|ComparisonChain
name|LESS
init|=
operator|new
name|InactiveComparisonChain
argument_list|(
operator|-
literal|1
argument_list|)
decl_stmt|;
DECL|field|GREATER
specifier|private
specifier|static
specifier|final
name|ComparisonChain
name|GREATER
init|=
operator|new
name|InactiveComparisonChain
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|class|InactiveComparisonChain
specifier|private
specifier|static
specifier|final
class|class
name|InactiveComparisonChain
extends|extends
name|ComparisonChain
block|{
DECL|field|result
specifier|final
name|int
name|result
decl_stmt|;
DECL|method|InactiveComparisonChain (int result)
name|InactiveComparisonChain
parameter_list|(
name|int
name|result
parameter_list|)
block|{
name|this
operator|.
name|result
operator|=
name|result
expr_stmt|;
block|}
DECL|method|compare ( @ullable Comparable left, @Nullable Comparable right)
annotation|@
name|Override
specifier|public
name|ComparisonChain
name|compare
parameter_list|(
annotation|@
name|Nullable
name|Comparable
name|left
parameter_list|,
annotation|@
name|Nullable
name|Comparable
name|right
parameter_list|)
block|{
return|return
name|this
return|;
block|}
DECL|method|compare (@ullable T left, @Nullable T right, @Nullable Comparator<T> comparator)
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|ComparisonChain
name|compare
parameter_list|(
annotation|@
name|Nullable
name|T
name|left
parameter_list|,
annotation|@
name|Nullable
name|T
name|right
parameter_list|,
annotation|@
name|Nullable
name|Comparator
argument_list|<
name|T
argument_list|>
name|comparator
parameter_list|)
block|{
return|return
name|this
return|;
block|}
DECL|method|compare (int left, int right)
annotation|@
name|Override
specifier|public
name|ComparisonChain
name|compare
parameter_list|(
name|int
name|left
parameter_list|,
name|int
name|right
parameter_list|)
block|{
return|return
name|this
return|;
block|}
DECL|method|compare (long left, long right)
annotation|@
name|Override
specifier|public
name|ComparisonChain
name|compare
parameter_list|(
name|long
name|left
parameter_list|,
name|long
name|right
parameter_list|)
block|{
return|return
name|this
return|;
block|}
DECL|method|compare (float left, float right)
annotation|@
name|Override
specifier|public
name|ComparisonChain
name|compare
parameter_list|(
name|float
name|left
parameter_list|,
name|float
name|right
parameter_list|)
block|{
return|return
name|this
return|;
block|}
DECL|method|compare (double left, double right)
annotation|@
name|Override
specifier|public
name|ComparisonChain
name|compare
parameter_list|(
name|double
name|left
parameter_list|,
name|double
name|right
parameter_list|)
block|{
return|return
name|this
return|;
block|}
DECL|method|compareTrueFirst (boolean left, boolean right)
annotation|@
name|Override
specifier|public
name|ComparisonChain
name|compareTrueFirst
parameter_list|(
name|boolean
name|left
parameter_list|,
name|boolean
name|right
parameter_list|)
block|{
return|return
name|this
return|;
block|}
DECL|method|compareFalseFirst (boolean left, boolean right)
annotation|@
name|Override
specifier|public
name|ComparisonChain
name|compareFalseFirst
parameter_list|(
name|boolean
name|left
parameter_list|,
name|boolean
name|right
parameter_list|)
block|{
return|return
name|this
return|;
block|}
DECL|method|result ()
annotation|@
name|Override
specifier|public
name|int
name|result
parameter_list|()
block|{
return|return
name|result
return|;
block|}
block|}
comment|/**    * Compares two comparable objects as specified by {@link    * Comparable#compareTo},<i>if</i> the result of this comparison chain    * has not already been determined.    */
DECL|method|compare ( Comparable<?> left, Comparable<?> right)
specifier|public
specifier|abstract
name|ComparisonChain
name|compare
parameter_list|(
name|Comparable
argument_list|<
name|?
argument_list|>
name|left
parameter_list|,
name|Comparable
argument_list|<
name|?
argument_list|>
name|right
parameter_list|)
function_decl|;
comment|/**    * Compares two objects using a comparator,<i>if</i> the result of this    * comparison chain has not already been determined.    */
DECL|method|compare ( @ullable T left, @Nullable T right, Comparator<T> comparator)
specifier|public
specifier|abstract
parameter_list|<
name|T
parameter_list|>
name|ComparisonChain
name|compare
parameter_list|(
annotation|@
name|Nullable
name|T
name|left
parameter_list|,
annotation|@
name|Nullable
name|T
name|right
parameter_list|,
name|Comparator
argument_list|<
name|T
argument_list|>
name|comparator
parameter_list|)
function_decl|;
comment|/**    * Compares two {@code int} values as specified by {@link Ints#compare},    *<i>if</i> the result of this comparison chain has not already been    * determined.    */
DECL|method|compare (int left, int right)
specifier|public
specifier|abstract
name|ComparisonChain
name|compare
parameter_list|(
name|int
name|left
parameter_list|,
name|int
name|right
parameter_list|)
function_decl|;
comment|/**    * Compares two {@code long} values as specified by {@link Longs#compare},    *<i>if</i> the result of this comparison chain has not already been    * determined.    */
DECL|method|compare (long left, long right)
specifier|public
specifier|abstract
name|ComparisonChain
name|compare
parameter_list|(
name|long
name|left
parameter_list|,
name|long
name|right
parameter_list|)
function_decl|;
comment|/**    * Compares two {@code float} values as specified by {@link    * Float#compare},<i>if</i> the result of this comparison chain has not    * already been determined.    */
DECL|method|compare (float left, float right)
specifier|public
specifier|abstract
name|ComparisonChain
name|compare
parameter_list|(
name|float
name|left
parameter_list|,
name|float
name|right
parameter_list|)
function_decl|;
comment|/**    * Compares two {@code double} values as specified by {@link    * Double#compare},<i>if</i> the result of this comparison chain has not    * already been determined.    */
DECL|method|compare (double left, double right)
specifier|public
specifier|abstract
name|ComparisonChain
name|compare
parameter_list|(
name|double
name|left
parameter_list|,
name|double
name|right
parameter_list|)
function_decl|;
comment|/**    * Compares two {@code boolean} values, considering {@code true} to be less    * than {@code false},<i>if</i> the result of this comparison chain has not    * already been determined.    *    * @since 12.0    */
DECL|method|compareTrueFirst (boolean left, boolean right)
specifier|public
specifier|abstract
name|ComparisonChain
name|compareTrueFirst
parameter_list|(
name|boolean
name|left
parameter_list|,
name|boolean
name|right
parameter_list|)
function_decl|;
comment|/**    * Compares two {@code boolean} values, considering {@code false} to be less    * than {@code true},<i>if</i> the result of this comparison chain has not    * already been determined.    *    * @since 12.0 (present as {@code compare} since 2.0)    */
DECL|method|compareFalseFirst (boolean left, boolean right)
specifier|public
specifier|abstract
name|ComparisonChain
name|compareFalseFirst
parameter_list|(
name|boolean
name|left
parameter_list|,
name|boolean
name|right
parameter_list|)
function_decl|;
comment|/**    * Ends this comparison chain and returns its result: a value having the    * same sign as the first nonzero comparison result in the chain, or zero if    * every result was zero.    */
DECL|method|result ()
specifier|public
specifier|abstract
name|int
name|result
parameter_list|()
function_decl|;
block|}
end_class

end_unit

