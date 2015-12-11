begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.math
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|math
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkState
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|math
operator|.
name|DoubleUtils
operator|.
name|ensureNonNegative
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|math
operator|.
name|StatsAccumulator
operator|.
name|calculateNewMeanNonFinite
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|Doubles
operator|.
name|isFinite
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|Double
operator|.
name|NaN
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|Double
operator|.
name|doubleToLongBits
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|Double
operator|.
name|isNaN
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
name|base
operator|.
name|MoreObjects
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
name|base
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteOrder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
comment|/**  * An immutable value object capturing some basic statistics about a collection of double values.  * Build instances with {@link #of} or {@link StatsAccumulator#snapshot}. If you only want to  * calculate the mean of a dataset, use {@link #meanOf} instead.  *  * @author Pete Gillin  * @author Kevin Bourrillion  * @since 20.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|Stats
specifier|public
specifier|final
class|class
name|Stats
implements|implements
name|Serializable
block|{
DECL|field|count
specifier|private
specifier|final
name|long
name|count
decl_stmt|;
DECL|field|mean
specifier|private
specifier|final
name|double
name|mean
decl_stmt|;
DECL|field|sumOfSquaresOfDeltas
specifier|private
specifier|final
name|double
name|sumOfSquaresOfDeltas
decl_stmt|;
DECL|field|min
specifier|private
specifier|final
name|double
name|min
decl_stmt|;
DECL|field|max
specifier|private
specifier|final
name|double
name|max
decl_stmt|;
comment|/**    * Internal constructor. Users should use {@link #of} or {@link StatsAccumulator#snapshot}.    *    *<p>To ensure that the created instance obeys its contract, the parameters should satisfy the    * following constraints. This is the callers responsibility and is not enforced here.    *<ul>    *<li>If {@code count} is 0, {@code mean} may have any finite value (its only usage will be to    * get multiplied by 0 to calculate the sum), and the other parameters may have any values (they    * will not be used).    *<li>If {@code count} is 1, {@code sumOfSquaresOfDeltas} must be exactly 0.0 or    * {@link Double#NaN}.    *</ul>    */
DECL|method|Stats (long count, double mean, double sumOfSquaresOfDeltas, double min, double max)
name|Stats
parameter_list|(
name|long
name|count
parameter_list|,
name|double
name|mean
parameter_list|,
name|double
name|sumOfSquaresOfDeltas
parameter_list|,
name|double
name|min
parameter_list|,
name|double
name|max
parameter_list|)
block|{
name|this
operator|.
name|count
operator|=
name|count
expr_stmt|;
name|this
operator|.
name|mean
operator|=
name|mean
expr_stmt|;
name|this
operator|.
name|sumOfSquaresOfDeltas
operator|=
name|sumOfSquaresOfDeltas
expr_stmt|;
name|this
operator|.
name|min
operator|=
name|min
expr_stmt|;
name|this
operator|.
name|max
operator|=
name|max
expr_stmt|;
block|}
comment|/**    * Returns statistics over a dataset containing the given values.    *    * @param values a series of values, which will be converted to {@code double} values (this may    *     cause loss of precision)    */
DECL|method|of (Iterable<? extends Number> values)
specifier|public
specifier|static
name|Stats
name|of
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|Number
argument_list|>
name|values
parameter_list|)
block|{
name|StatsAccumulator
name|accumulator
init|=
operator|new
name|StatsAccumulator
argument_list|()
decl_stmt|;
name|accumulator
operator|.
name|addAll
argument_list|(
name|values
argument_list|)
expr_stmt|;
return|return
name|accumulator
operator|.
name|snapshot
argument_list|()
return|;
block|}
comment|/**    * Returns statistics over a dataset containing the given values.    *    * @param values a series of values, which will be converted to {@code double} values (this may    *     cause loss of precision)    */
DECL|method|of (Iterator<? extends Number> values)
specifier|public
specifier|static
name|Stats
name|of
parameter_list|(
name|Iterator
argument_list|<
name|?
extends|extends
name|Number
argument_list|>
name|values
parameter_list|)
block|{
name|StatsAccumulator
name|accumulator
init|=
operator|new
name|StatsAccumulator
argument_list|()
decl_stmt|;
name|accumulator
operator|.
name|addAll
argument_list|(
name|values
argument_list|)
expr_stmt|;
return|return
name|accumulator
operator|.
name|snapshot
argument_list|()
return|;
block|}
comment|/**    * Returns statistics over a dataset containing the given values.    *    * @param values a series of values    */
DECL|method|of (double... values)
specifier|public
specifier|static
name|Stats
name|of
parameter_list|(
name|double
modifier|...
name|values
parameter_list|)
block|{
name|StatsAccumulator
name|acummulator
init|=
operator|new
name|StatsAccumulator
argument_list|()
decl_stmt|;
name|acummulator
operator|.
name|addAll
argument_list|(
name|values
argument_list|)
expr_stmt|;
return|return
name|acummulator
operator|.
name|snapshot
argument_list|()
return|;
block|}
comment|/**    * Returns statistics over a dataset containing the given values.    *    * @param values a series of values    */
DECL|method|of (int... values)
specifier|public
specifier|static
name|Stats
name|of
parameter_list|(
name|int
modifier|...
name|values
parameter_list|)
block|{
name|StatsAccumulator
name|acummulator
init|=
operator|new
name|StatsAccumulator
argument_list|()
decl_stmt|;
name|acummulator
operator|.
name|addAll
argument_list|(
name|values
argument_list|)
expr_stmt|;
return|return
name|acummulator
operator|.
name|snapshot
argument_list|()
return|;
block|}
comment|/**    * Returns statistics over a dataset containing the given values.    *    * @param values a series of values, which will be converted to {@code double} values (this may    *     cause loss of precision for longs of magnitude over 2^53 (slightly over 9e15))    */
DECL|method|of (long... values)
specifier|public
specifier|static
name|Stats
name|of
parameter_list|(
name|long
modifier|...
name|values
parameter_list|)
block|{
name|StatsAccumulator
name|acummulator
init|=
operator|new
name|StatsAccumulator
argument_list|()
decl_stmt|;
name|acummulator
operator|.
name|addAll
argument_list|(
name|values
argument_list|)
expr_stmt|;
return|return
name|acummulator
operator|.
name|snapshot
argument_list|()
return|;
block|}
comment|/**    * Returns the number of values.    */
DECL|method|count ()
specifier|public
name|long
name|count
parameter_list|()
block|{
return|return
name|count
return|;
block|}
comment|/**    * Returns the<a href="http://en.wikipedia.org/wiki/Arithmetic_mean">arithmetic mean</a> of the    * values. The count must be non-zero.    *    *<p>If these values are a sample drawn from a population, this is also an unbiased estimator of    * the arithmetic mean of the population.    *    *<h3>Non-finite values</h3>    *    *<p>If the dataset contains {@link Double#NaN} then the result is {@link Double#NaN}. If it    * contains both {@link Double#POSITIVE_INFINITY} and {@link Double#NEGATIVE_INFINITY} then the    * result is {@link Double#NaN}. If it contains {@link Double#POSITIVE_INFINITY} and finite values    * only or {@link Double#POSITIVE_INFINITY} only, the result is {@link Double#POSITIVE_INFINITY}.    * If it contains {@link Double#NEGATIVE_INFINITY} and finite values only or    * {@link Double#NEGATIVE_INFINITY} only, the result is {@link Double#NEGATIVE_INFINITY}.    *    *<p>If you only want to calculate the mean, use {#meanOf} instead of creating a {@link Stats}    * instance.    *    * @throws IllegalStateException if the dataset is empty    */
DECL|method|mean ()
specifier|public
name|double
name|mean
parameter_list|()
block|{
name|checkState
argument_list|(
name|count
operator|!=
literal|0
argument_list|)
expr_stmt|;
return|return
name|mean
return|;
block|}
comment|/**    * Returns the sum of the values.    *    *<h3>Non-finite values</h3>    *    *<p>If the dataset contains {@link Double#NaN} then the result is {@link Double#NaN}. If it    * contains both {@link Double#POSITIVE_INFINITY} and {@link Double#NEGATIVE_INFINITY} then the    * result is {@link Double#NaN}. If it contains {@link Double#POSITIVE_INFINITY} and finite values    * only or {@link Double#POSITIVE_INFINITY} only, the result is {@link Double#POSITIVE_INFINITY}.    * If it contains {@link Double#NEGATIVE_INFINITY} and finite values only or    * {@link Double#NEGATIVE_INFINITY} only, the result is {@link Double#NEGATIVE_INFINITY}.    */
DECL|method|sum ()
specifier|public
name|double
name|sum
parameter_list|()
block|{
return|return
name|mean
operator|*
name|count
return|;
block|}
comment|/**    * Returns the<a href="http://en.wikipedia.org/wiki/Variance#Population_variance">population    * variance</a> of the values. The count must be non-zero.    *    *<p>This is guaranteed to return zero if the the dataset contains only exactly one finite value.    * It is not guaranteed to return zero when the dataset consists of the same value multiple times,    * due to numerical errors. However, it is guaranteed never to return a negative result.    *    *<h3>Non-finite values</h3>    *    *<p>If the dataset contains any non-finite values ({@link Double#POSITIVE_INFINITY},    * {@link Double#NEGATIVE_INFINITY}, or {@link Double#NaN}) then the result is {@link Double#NaN}.    *    * @throws IllegalStateException if the dataset is empty    */
DECL|method|populationVariance ()
specifier|public
name|double
name|populationVariance
parameter_list|()
block|{
name|checkState
argument_list|(
name|count
operator|>
literal|0
argument_list|)
expr_stmt|;
if|if
condition|(
name|isNaN
argument_list|(
name|sumOfSquaresOfDeltas
argument_list|)
condition|)
block|{
return|return
name|NaN
return|;
block|}
if|if
condition|(
name|count
operator|==
literal|1
condition|)
block|{
return|return
literal|0.0
return|;
block|}
return|return
name|ensureNonNegative
argument_list|(
name|sumOfSquaresOfDeltas
argument_list|)
operator|/
name|count
argument_list|()
return|;
block|}
comment|/**    * Returns the    *<a href="http://en.wikipedia.org/wiki/Standard_deviation#Definition_of_population_values">    * population standard deviation</a> of the values. The count must be non-zero.    *    *<p>This is guaranteed to return zero if the the dataset contains only exactly one finite value.    * It is not guaranteed to return zero when the dataset consists of the same value multiple times,    * due to numerical errors. However, it is guaranteed never to return a negative result.    *    *<h3>Non-finite values</h3>    *    *<p>If the dataset contains any non-finite values ({@link Double#POSITIVE_INFINITY},    * {@link Double#NEGATIVE_INFINITY}, or {@link Double#NaN}) then the result is {@link Double#NaN}.    *    * @throws IllegalStateException if the dataset is empty    */
DECL|method|populationStandardDeviation ()
specifier|public
name|double
name|populationStandardDeviation
parameter_list|()
block|{
return|return
name|Math
operator|.
name|sqrt
argument_list|(
name|populationVariance
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns the<a href="http://en.wikipedia.org/wiki/Variance#Sample_variance">unbaised sample    * variance</a> of the values. If this dataset is a sample drawn from a population, this is an    * unbiased estimator of the population variance of the population. The count must be greater than    * one.    *    *<p>This is not guaranteed to return zero when the dataset consists of the same value multiple    * times, due to numerical errors. However, it is guaranteed never to return a negative result.    *    *<h3>Non-finite values</h3>    *    *<p>If the dataset contains any non-finite values ({@link Double#POSITIVE_INFINITY},    * {@link Double#NEGATIVE_INFINITY}, or {@link Double#NaN}) then the result is {@link Double#NaN}.    *    * @throws IllegalStateException if the dataset is empty or contains a single value    */
DECL|method|sampleVariance ()
specifier|public
name|double
name|sampleVariance
parameter_list|()
block|{
name|checkState
argument_list|(
name|count
operator|>
literal|1
argument_list|)
expr_stmt|;
if|if
condition|(
name|isNaN
argument_list|(
name|sumOfSquaresOfDeltas
argument_list|)
condition|)
block|{
return|return
name|NaN
return|;
block|}
return|return
name|ensureNonNegative
argument_list|(
name|sumOfSquaresOfDeltas
argument_list|)
operator|/
operator|(
name|count
operator|-
literal|1
operator|)
return|;
block|}
comment|/**    * Returns the    *<a href="http://en.wikipedia.org/wiki/Standard_deviation#Corrected_sample_standard_deviation">    * corrected sample standard deviation</a> of the values. If this dataset is a sample drawn from a    * population, this is an estimator of the population standard deviation of the population which    * is less biased than {@link #populationStandardDeviation()} (the unbiased estimator depends on    * the distribution). The count must be greater than one.    *    *<p>This is not guaranteed to return zero when the dataset consists of the same value multiple    * times, due to numerical errors. However, it is guaranteed never to return a negative result.    *    *<h3>Non-finite values</h3>    *    *<p>If the dataset contains any non-finite values ({@link Double#POSITIVE_INFINITY},    * {@link Double#NEGATIVE_INFINITY}, or {@link Double#NaN}) then the result is {@link Double#NaN}.    *    * @throws IllegalStateException if the dataset is empty or contains a single value    */
DECL|method|sampleStandardDeviation ()
specifier|public
name|double
name|sampleStandardDeviation
parameter_list|()
block|{
return|return
name|Math
operator|.
name|sqrt
argument_list|(
name|sampleVariance
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns the lowest value in the dataset. The count must be non-zero.    *    *<h3>Non-finite values</h3>    *    *<p>If the dataset contains {@link Double#NaN} then the result is {@link Double#NaN}. If it    * contains {@link Double#NEGATIVE_INFINITY} and not {@link Double#NaN} then the result is    * {@link Double#NEGATIVE_INFINITY}. If it contains {@link Double#POSITIVE_INFINITY} and finite    * values only then the result is the lowest finite value. If it contains    * {@link Double#POSITIVE_INFINITY} only then the result is {@link Double#POSITIVE_INFINITY}.    *    * @throws IllegalStateException if the dataset is empty    */
DECL|method|min ()
specifier|public
name|double
name|min
parameter_list|()
block|{
name|checkState
argument_list|(
name|count
operator|!=
literal|0
argument_list|)
expr_stmt|;
return|return
name|min
return|;
block|}
comment|/**    * Returns the highest value in the dataset. The count must be non-zero.    *    *<h3>Non-finite values</h3>    *    *<p>If the dataset contains {@link Double#NaN} then the result is {@link Double#NaN}. If it    * contains {@link Double#POSITIVE_INFINITY} and not {@link Double#NaN} then the result is    * {@link Double#POSITIVE_INFINITY}. If it contains {@link Double#NEGATIVE_INFINITY} and finite    * values only then the result is the highest finite value. If it contains    * {@link Double#NEGATIVE_INFINITY} only then the result is {@link Double#NEGATIVE_INFINITY}.    *    * @throws IllegalStateException if the dataset is empty    */
DECL|method|max ()
specifier|public
name|double
name|max
parameter_list|()
block|{
name|checkState
argument_list|(
name|count
operator|!=
literal|0
argument_list|)
expr_stmt|;
return|return
name|max
return|;
block|}
comment|/**    * {@inheritDoc}    *    *<p><b>Note:</b> This tests exact equality of the calculated statistics, including the floating    * point values. It is definitely true for instances constructed from exactly the same values in    * the same order. It is also true for an instance round-tripped through java serialization.    * However, floating point rounding errors mean that it may be false for some instances where the    * statistics are mathematically equal, including the same values in a different order.    */
annotation|@
name|Override
DECL|method|equals (@ullable Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|getClass
argument_list|()
operator|!=
name|obj
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Stats
name|other
init|=
operator|(
name|Stats
operator|)
name|obj
decl_stmt|;
return|return
operator|(
name|count
operator|==
name|other
operator|.
name|count
operator|)
operator|&&
operator|(
name|doubleToLongBits
argument_list|(
name|mean
argument_list|)
operator|==
name|doubleToLongBits
argument_list|(
name|other
operator|.
name|mean
argument_list|)
operator|)
operator|&&
operator|(
name|doubleToLongBits
argument_list|(
name|sumOfSquaresOfDeltas
argument_list|)
operator|==
name|doubleToLongBits
argument_list|(
name|other
operator|.
name|sumOfSquaresOfDeltas
argument_list|)
operator|)
operator|&&
operator|(
name|doubleToLongBits
argument_list|(
name|min
argument_list|)
operator|==
name|doubleToLongBits
argument_list|(
name|other
operator|.
name|min
argument_list|)
operator|)
operator|&&
operator|(
name|doubleToLongBits
argument_list|(
name|max
argument_list|)
operator|==
name|doubleToLongBits
argument_list|(
name|other
operator|.
name|max
argument_list|)
operator|)
return|;
block|}
comment|/**    * {@inheritDoc}    *    *<p><b>Note:</b> This hash code is consistent with exact equality of the calculated    * statistics, including the floating point values. See the note on {@link #equals} for details.    */
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hashCode
argument_list|(
name|count
argument_list|,
name|mean
argument_list|,
name|sumOfSquaresOfDeltas
argument_list|,
name|min
argument_list|,
name|max
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|MoreObjects
operator|.
name|toStringHelper
argument_list|(
name|this
argument_list|)
operator|.
name|add
argument_list|(
literal|"count"
argument_list|,
name|count
argument_list|)
operator|.
name|add
argument_list|(
literal|"mean"
argument_list|,
name|mean
argument_list|)
operator|.
name|add
argument_list|(
literal|"populationStandardDeviation"
argument_list|,
name|populationStandardDeviation
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
literal|"min"
argument_list|,
name|min
argument_list|)
operator|.
name|add
argument_list|(
literal|"max"
argument_list|,
name|max
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|sumOfSquaresOfDeltas ()
name|double
name|sumOfSquaresOfDeltas
parameter_list|()
block|{
return|return
name|sumOfSquaresOfDeltas
return|;
block|}
comment|/**    * Returns the<a href="http://en.wikipedia.org/wiki/Arithmetic_mean">arithmetic mean</a> of the    * values. The count must be non-zero.    *    *<p>The definition of the mean is the same as {@link Stats#mean}.    *    * @param values a series of values, which will be converted to {@code double} values (this may    *     cause loss of precision)    * @throws IllegalArgumentException if the dataset is empty    */
DECL|method|meanOf (Iterable<? extends Number> values)
specifier|public
specifier|static
name|double
name|meanOf
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|Number
argument_list|>
name|values
parameter_list|)
block|{
return|return
name|meanOf
argument_list|(
name|values
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns the<a href="http://en.wikipedia.org/wiki/Arithmetic_mean">arithmetic mean</a> of the    * values. The count must be non-zero.    *    *<p>The definition of the mean is the same as {@link Stats#mean}.    *    * @param values a series of values, which will be converted to {@code double} values (this may    *     cause loss of precision)    * @throws IllegalArgumentException if the dataset is empty    */
DECL|method|meanOf (Iterator<? extends Number> values)
specifier|public
specifier|static
name|double
name|meanOf
parameter_list|(
name|Iterator
argument_list|<
name|?
extends|extends
name|Number
argument_list|>
name|values
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|values
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|long
name|count
init|=
literal|1
decl_stmt|;
name|double
name|mean
init|=
name|values
operator|.
name|next
argument_list|()
operator|.
name|doubleValue
argument_list|()
decl_stmt|;
while|while
condition|(
name|values
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|double
name|value
init|=
name|values
operator|.
name|next
argument_list|()
operator|.
name|doubleValue
argument_list|()
decl_stmt|;
name|count
operator|++
expr_stmt|;
if|if
condition|(
name|isFinite
argument_list|(
name|value
argument_list|)
operator|&&
name|isFinite
argument_list|(
name|mean
argument_list|)
condition|)
block|{
comment|// Art of Computer Programming vol. 2, Knuth, 4.2.2, (15)
name|mean
operator|+=
operator|(
name|value
operator|-
name|mean
operator|)
operator|/
name|count
expr_stmt|;
block|}
else|else
block|{
name|mean
operator|=
name|calculateNewMeanNonFinite
argument_list|(
name|mean
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|mean
return|;
block|}
comment|/**    * Returns the<a href="http://en.wikipedia.org/wiki/Arithmetic_mean">arithmetic mean</a> of the    * values. The count must be non-zero.    *    *<p>The definition of the mean is the same as {@link Stats#mean}.    *    * @param values a series of values    * @throws IllegalArgumentException if the dataset is empty    */
DECL|method|meanOf (double... values)
specifier|public
specifier|static
name|double
name|meanOf
parameter_list|(
name|double
modifier|...
name|values
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|values
operator|.
name|length
operator|>
literal|0
argument_list|)
expr_stmt|;
name|double
name|mean
init|=
name|values
index|[
literal|0
index|]
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|1
init|;
name|index
operator|<
name|values
operator|.
name|length
condition|;
name|index
operator|++
control|)
block|{
name|double
name|value
init|=
name|values
index|[
name|index
index|]
decl_stmt|;
if|if
condition|(
name|isFinite
argument_list|(
name|value
argument_list|)
operator|&&
name|isFinite
argument_list|(
name|mean
argument_list|)
condition|)
block|{
comment|// Art of Computer Programming vol. 2, Knuth, 4.2.2, (15)
name|mean
operator|+=
operator|(
name|value
operator|-
name|mean
operator|)
operator|/
operator|(
name|index
operator|+
literal|1
operator|)
expr_stmt|;
block|}
else|else
block|{
name|mean
operator|=
name|calculateNewMeanNonFinite
argument_list|(
name|mean
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|mean
return|;
block|}
comment|/**    * Returns the<a href="http://en.wikipedia.org/wiki/Arithmetic_mean">arithmetic mean</a> of the    * values. The count must be non-zero.    *    *<p>The definition of the mean is the same as {@link Stats#mean}.    *    * @param values a series of values    * @throws IllegalArgumentException if the dataset is empty    */
DECL|method|meanOf (int... values)
specifier|public
specifier|static
name|double
name|meanOf
parameter_list|(
name|int
modifier|...
name|values
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|values
operator|.
name|length
operator|>
literal|0
argument_list|)
expr_stmt|;
name|double
name|mean
init|=
name|values
index|[
literal|0
index|]
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|1
init|;
name|index
operator|<
name|values
operator|.
name|length
condition|;
name|index
operator|++
control|)
block|{
name|double
name|value
init|=
name|values
index|[
name|index
index|]
decl_stmt|;
if|if
condition|(
name|isFinite
argument_list|(
name|value
argument_list|)
operator|&&
name|isFinite
argument_list|(
name|mean
argument_list|)
condition|)
block|{
comment|// Art of Computer Programming vol. 2, Knuth, 4.2.2, (15)
name|mean
operator|+=
operator|(
name|value
operator|-
name|mean
operator|)
operator|/
operator|(
name|index
operator|+
literal|1
operator|)
expr_stmt|;
block|}
else|else
block|{
name|mean
operator|=
name|calculateNewMeanNonFinite
argument_list|(
name|mean
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|mean
return|;
block|}
comment|/**    * Returns the<a href="http://en.wikipedia.org/wiki/Arithmetic_mean">arithmetic mean</a> of the    * values. The count must be non-zero.    *    *<p>The definition of the mean is the same as {@link Stats#mean}.    *    * @param values a series of values, which will be converted to {@code double} values (this may    *     cause loss of precision for longs of magnitude over 2^53 (slightly over 9e15))    * @throws IllegalArgumentException if the dataset is empty    */
DECL|method|meanOf (long... values)
specifier|public
specifier|static
name|double
name|meanOf
parameter_list|(
name|long
modifier|...
name|values
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|values
operator|.
name|length
operator|>
literal|0
argument_list|)
expr_stmt|;
name|double
name|mean
init|=
name|values
index|[
literal|0
index|]
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|1
init|;
name|index
operator|<
name|values
operator|.
name|length
condition|;
name|index
operator|++
control|)
block|{
name|double
name|value
init|=
name|values
index|[
name|index
index|]
decl_stmt|;
if|if
condition|(
name|isFinite
argument_list|(
name|value
argument_list|)
operator|&&
name|isFinite
argument_list|(
name|mean
argument_list|)
condition|)
block|{
comment|// Art of Computer Programming vol. 2, Knuth, 4.2.2, (15)
name|mean
operator|+=
operator|(
name|value
operator|-
name|mean
operator|)
operator|/
operator|(
name|index
operator|+
literal|1
operator|)
expr_stmt|;
block|}
else|else
block|{
name|mean
operator|=
name|calculateNewMeanNonFinite
argument_list|(
name|mean
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|mean
return|;
block|}
comment|// Serialization helpers
comment|/**    * The size of byte array representaion in bytes.    */
DECL|field|BYTES
specifier|public
specifier|static
specifier|final
name|int
name|BYTES
init|=
operator|(
name|Long
operator|.
name|SIZE
operator|+
name|Double
operator|.
name|SIZE
operator|*
literal|4
operator|)
operator|/
name|Byte
operator|.
name|SIZE
decl_stmt|;
comment|/**    * Gets a {@link #BYTES}-long byte array representation of this instance.    *    *<p>NOTE: NO guarantees are made regarding stability of the representation between versions.    */
DECL|method|toByteArray ()
specifier|public
name|byte
index|[]
name|toByteArray
parameter_list|()
block|{
return|return
name|ByteBuffer
operator|.
name|allocate
argument_list|(
name|BYTES
argument_list|)
operator|.
name|order
argument_list|(
name|ByteOrder
operator|.
name|LITTLE_ENDIAN
argument_list|)
operator|.
name|putLong
argument_list|(
name|count
argument_list|)
operator|.
name|putDouble
argument_list|(
name|mean
argument_list|)
operator|.
name|putDouble
argument_list|(
name|sumOfSquaresOfDeltas
argument_list|)
operator|.
name|putDouble
argument_list|(
name|min
argument_list|)
operator|.
name|putDouble
argument_list|(
name|max
argument_list|)
operator|.
name|array
argument_list|()
return|;
block|}
comment|/**    * Creates a Stats instance from the given byte representation. The array must be at least    * {@link #BYTES} long, and only the first {@link #BYTES} bytes will be used.    *    *<p>NOTE: NO guarantees are made regarding stability of the representation between versions.    */
DECL|method|fromByteArray (byte[] byteArray)
specifier|public
specifier|static
name|Stats
name|fromByteArray
parameter_list|(
name|byte
index|[]
name|byteArray
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|byteArray
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|byteArray
operator|.
name|length
operator|>=
name|BYTES
argument_list|,
literal|"Expected at least Stats.BYTES = %s, got %s"
argument_list|,
name|BYTES
argument_list|,
name|byteArray
operator|.
name|length
argument_list|)
expr_stmt|;
name|ByteBuffer
name|buff
init|=
name|ByteBuffer
operator|.
name|wrap
argument_list|(
name|byteArray
argument_list|)
operator|.
name|order
argument_list|(
name|ByteOrder
operator|.
name|LITTLE_ENDIAN
argument_list|)
decl_stmt|;
return|return
operator|new
name|Stats
argument_list|(
name|buff
operator|.
name|getLong
argument_list|()
argument_list|,
name|buff
operator|.
name|getDouble
argument_list|()
argument_list|,
name|buff
operator|.
name|getDouble
argument_list|()
argument_list|,
name|buff
operator|.
name|getDouble
argument_list|()
argument_list|,
name|buff
operator|.
name|getDouble
argument_list|()
argument_list|)
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
end_class

end_unit

