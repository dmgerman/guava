begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|annotations
operator|.
name|GwtIncompatible
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

begin_comment
comment|/**  * A mutable object which accumulates double values and tracks some basic statistics over all the  * values added so far. The values may be added singly or in groups. This class is not thread safe.  *  * @author Pete Gillin  * @author Kevin Bourrillion  * @since 20.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|StatsAccumulator
specifier|public
specifier|final
class|class
name|StatsAccumulator
block|{
comment|// These fields must satisfy the requirements of Stats' constructor as well as those of the stat
comment|// methods of this class.
DECL|field|count
specifier|private
name|long
name|count
init|=
literal|0
decl_stmt|;
DECL|field|mean
specifier|private
name|double
name|mean
init|=
literal|0.0
decl_stmt|;
comment|// any finite value will do, we only use it to multiply by zero for sum
DECL|field|sumOfSquaresOfDeltas
specifier|private
name|double
name|sumOfSquaresOfDeltas
init|=
literal|0.0
decl_stmt|;
DECL|field|min
specifier|private
name|double
name|min
init|=
name|NaN
decl_stmt|;
comment|// any value will do
DECL|field|max
specifier|private
name|double
name|max
init|=
name|NaN
decl_stmt|;
comment|// any value will do
comment|/** Adds the given value to the dataset. */
DECL|method|add (double value)
specifier|public
name|void
name|add
parameter_list|(
name|double
name|value
parameter_list|)
block|{
if|if
condition|(
name|count
operator|==
literal|0
condition|)
block|{
name|count
operator|=
literal|1
expr_stmt|;
name|mean
operator|=
name|value
expr_stmt|;
name|min
operator|=
name|value
expr_stmt|;
name|max
operator|=
name|value
expr_stmt|;
if|if
condition|(
operator|!
name|isFinite
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|sumOfSquaresOfDeltas
operator|=
name|NaN
expr_stmt|;
block|}
block|}
else|else
block|{
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
comment|// Art of Computer Programming vol. 2, Knuth, 4.2.2, (15) and (16)
name|double
name|delta
init|=
name|value
operator|-
name|mean
decl_stmt|;
name|mean
operator|+=
name|delta
operator|/
name|count
expr_stmt|;
name|sumOfSquaresOfDeltas
operator|+=
name|delta
operator|*
operator|(
name|value
operator|-
name|mean
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
name|sumOfSquaresOfDeltas
operator|=
name|NaN
expr_stmt|;
block|}
name|min
operator|=
name|Math
operator|.
name|min
argument_list|(
name|min
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|max
operator|=
name|Math
operator|.
name|max
argument_list|(
name|max
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Adds the given values to the dataset.    *    * @param values a series of values, which will be converted to {@code double} values (this may    *     cause loss of precision)    */
DECL|method|addAll (Iterable<? extends Number> values)
specifier|public
name|void
name|addAll
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
for|for
control|(
name|Number
name|value
range|:
name|values
control|)
block|{
name|add
argument_list|(
name|value
operator|.
name|doubleValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Adds the given values to the dataset.    *    * @param values a series of values, which will be converted to {@code double} values (this may    *     cause loss of precision)    */
DECL|method|addAll (Iterator<? extends Number> values)
specifier|public
name|void
name|addAll
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
while|while
condition|(
name|values
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|add
argument_list|(
name|values
operator|.
name|next
argument_list|()
operator|.
name|doubleValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Adds the given values to the dataset.    *    * @param values a series of values    */
DECL|method|addAll (double... values)
specifier|public
name|void
name|addAll
parameter_list|(
name|double
modifier|...
name|values
parameter_list|)
block|{
for|for
control|(
name|double
name|value
range|:
name|values
control|)
block|{
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Adds the given values to the dataset.    *    * @param values a series of values    */
DECL|method|addAll (int... values)
specifier|public
name|void
name|addAll
parameter_list|(
name|int
modifier|...
name|values
parameter_list|)
block|{
for|for
control|(
name|int
name|value
range|:
name|values
control|)
block|{
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Adds the given values to the dataset.    *    * @param values a series of values, which will be converted to {@code double} values (this may    *     cause loss of precision for longs of magnitude over 2^53 (slightly over 9e15))    */
DECL|method|addAll (long... values)
specifier|public
name|void
name|addAll
parameter_list|(
name|long
modifier|...
name|values
parameter_list|)
block|{
for|for
control|(
name|long
name|value
range|:
name|values
control|)
block|{
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Adds the given statistics to the dataset, as if the individual values used to compute the    * statistics had been added directly.    */
DECL|method|addAll (Stats values)
specifier|public
name|void
name|addAll
parameter_list|(
name|Stats
name|values
parameter_list|)
block|{
if|if
condition|(
name|values
operator|.
name|count
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|merge
argument_list|(
name|values
operator|.
name|count
argument_list|()
argument_list|,
name|values
operator|.
name|mean
argument_list|()
argument_list|,
name|values
operator|.
name|sumOfSquaresOfDeltas
argument_list|()
argument_list|,
name|values
operator|.
name|min
argument_list|()
argument_list|,
name|values
operator|.
name|max
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Adds the given statistics to the dataset, as if the individual values used to compute the    * statistics had been added directly.    *    * @since 28.2    */
DECL|method|addAll (StatsAccumulator values)
specifier|public
name|void
name|addAll
parameter_list|(
name|StatsAccumulator
name|values
parameter_list|)
block|{
if|if
condition|(
name|values
operator|.
name|count
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|merge
argument_list|(
name|values
operator|.
name|count
argument_list|()
argument_list|,
name|values
operator|.
name|mean
argument_list|()
argument_list|,
name|values
operator|.
name|sumOfSquaresOfDeltas
argument_list|()
argument_list|,
name|values
operator|.
name|min
argument_list|()
argument_list|,
name|values
operator|.
name|max
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|merge ( long otherCount, double otherMean, double otherSumOfSquaresOfDeltas, double otherMin, double otherMax)
specifier|private
name|void
name|merge
parameter_list|(
name|long
name|otherCount
parameter_list|,
name|double
name|otherMean
parameter_list|,
name|double
name|otherSumOfSquaresOfDeltas
parameter_list|,
name|double
name|otherMin
parameter_list|,
name|double
name|otherMax
parameter_list|)
block|{
if|if
condition|(
name|count
operator|==
literal|0
condition|)
block|{
name|count
operator|=
name|otherCount
expr_stmt|;
name|mean
operator|=
name|otherMean
expr_stmt|;
name|sumOfSquaresOfDeltas
operator|=
name|otherSumOfSquaresOfDeltas
expr_stmt|;
name|min
operator|=
name|otherMin
expr_stmt|;
name|max
operator|=
name|otherMax
expr_stmt|;
block|}
else|else
block|{
name|count
operator|+=
name|otherCount
expr_stmt|;
if|if
condition|(
name|isFinite
argument_list|(
name|mean
argument_list|)
operator|&&
name|isFinite
argument_list|(
name|otherMean
argument_list|)
condition|)
block|{
comment|// This is a generalized version of the calculation in add(double) above.
name|double
name|delta
init|=
name|otherMean
operator|-
name|mean
decl_stmt|;
name|mean
operator|+=
name|delta
operator|*
name|otherCount
operator|/
name|count
expr_stmt|;
name|sumOfSquaresOfDeltas
operator|+=
name|otherSumOfSquaresOfDeltas
operator|+
name|delta
operator|*
operator|(
name|otherMean
operator|-
name|mean
operator|)
operator|*
name|otherCount
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
name|otherMean
argument_list|)
expr_stmt|;
name|sumOfSquaresOfDeltas
operator|=
name|NaN
expr_stmt|;
block|}
name|min
operator|=
name|Math
operator|.
name|min
argument_list|(
name|min
argument_list|,
name|otherMin
argument_list|)
expr_stmt|;
name|max
operator|=
name|Math
operator|.
name|max
argument_list|(
name|max
argument_list|,
name|otherMax
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Returns an immutable snapshot of the current statistics. */
DECL|method|snapshot ()
specifier|public
name|Stats
name|snapshot
parameter_list|()
block|{
return|return
operator|new
name|Stats
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
comment|/** Returns the number of values. */
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
comment|/**    * Returns the<a href="http://en.wikipedia.org/wiki/Arithmetic_mean">arithmetic mean</a> of the    * values. The count must be non-zero.    *    *<p>If these values are a sample drawn from a population, this is also an unbiased estimator of    * the arithmetic mean of the population.    *    *<h3>Non-finite values</h3>    *    *<p>If the dataset contains {@link Double#NaN} then the result is {@link Double#NaN}. If it    * contains both {@link Double#POSITIVE_INFINITY} and {@link Double#NEGATIVE_INFINITY} then the    * result is {@link Double#NaN}. If it contains {@link Double#POSITIVE_INFINITY} and finite values    * only or {@link Double#POSITIVE_INFINITY} only, the result is {@link Double#POSITIVE_INFINITY}.    * If it contains {@link Double#NEGATIVE_INFINITY} and finite values only or {@link    * Double#NEGATIVE_INFINITY} only, the result is {@link Double#NEGATIVE_INFINITY}.    *    * @throws IllegalStateException if the dataset is empty    */
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
comment|/**    * Returns the sum of the values.    *    *<h3>Non-finite values</h3>    *    *<p>If the dataset contains {@link Double#NaN} then the result is {@link Double#NaN}. If it    * contains both {@link Double#POSITIVE_INFINITY} and {@link Double#NEGATIVE_INFINITY} then the    * result is {@link Double#NaN}. If it contains {@link Double#POSITIVE_INFINITY} and finite values    * only or {@link Double#POSITIVE_INFINITY} only, the result is {@link Double#POSITIVE_INFINITY}.    * If it contains {@link Double#NEGATIVE_INFINITY} and finite values only or {@link    * Double#NEGATIVE_INFINITY} only, the result is {@link Double#NEGATIVE_INFINITY}.    */
DECL|method|sum ()
specifier|public
specifier|final
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
comment|/**    * Returns the<a href="http://en.wikipedia.org/wiki/Variance#Population_variance">population    * variance</a> of the values. The count must be non-zero.    *    *<p>This is guaranteed to return zero if the dataset contains only exactly one finite value. It    * is not guaranteed to return zero when the dataset consists of the same value multiple times,    * due to numerical errors. However, it is guaranteed never to return a negative result.    *    *<h3>Non-finite values</h3>    *    *<p>If the dataset contains any non-finite values ({@link Double#POSITIVE_INFINITY}, {@link    * Double#NEGATIVE_INFINITY}, or {@link Double#NaN}) then the result is {@link Double#NaN}.    *    * @throws IllegalStateException if the dataset is empty    */
DECL|method|populationVariance ()
specifier|public
specifier|final
name|double
name|populationVariance
parameter_list|()
block|{
name|checkState
argument_list|(
name|count
operator|!=
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
return|;
block|}
comment|/**    * Returns the<a    * href="http://en.wikipedia.org/wiki/Standard_deviation#Definition_of_population_values">    * population standard deviation</a> of the values. The count must be non-zero.    *    *<p>This is guaranteed to return zero if the dataset contains only exactly one finite value. It    * is not guaranteed to return zero when the dataset consists of the same value multiple times,    * due to numerical errors. However, it is guaranteed never to return a negative result.    *    *<h3>Non-finite values</h3>    *    *<p>If the dataset contains any non-finite values ({@link Double#POSITIVE_INFINITY}, {@link    * Double#NEGATIVE_INFINITY}, or {@link Double#NaN}) then the result is {@link Double#NaN}.    *    * @throws IllegalStateException if the dataset is empty    */
DECL|method|populationStandardDeviation ()
specifier|public
specifier|final
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
comment|/**    * Returns the<a href="http://en.wikipedia.org/wiki/Variance#Sample_variance">unbiased sample    * variance</a> of the values. If this dataset is a sample drawn from a population, this is an    * unbiased estimator of the population variance of the population. The count must be greater than    * one.    *    *<p>This is not guaranteed to return zero when the dataset consists of the same value multiple    * times, due to numerical errors. However, it is guaranteed never to return a negative result.    *    *<h3>Non-finite values</h3>    *    *<p>If the dataset contains any non-finite values ({@link Double#POSITIVE_INFINITY}, {@link    * Double#NEGATIVE_INFINITY}, or {@link Double#NaN}) then the result is {@link Double#NaN}.    *    * @throws IllegalStateException if the dataset is empty or contains a single value    */
DECL|method|sampleVariance ()
specifier|public
specifier|final
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
comment|/**    * Returns the<a    * href="http://en.wikipedia.org/wiki/Standard_deviation#Corrected_sample_standard_deviation">    * corrected sample standard deviation</a> of the values. If this dataset is a sample drawn from a    * population, this is an estimator of the population standard deviation of the population which    * is less biased than {@link #populationStandardDeviation()} (the unbiased estimator depends on    * the distribution). The count must be greater than one.    *    *<p>This is not guaranteed to return zero when the dataset consists of the same value multiple    * times, due to numerical errors. However, it is guaranteed never to return a negative result.    *    *<h3>Non-finite values</h3>    *    *<p>If the dataset contains any non-finite values ({@link Double#POSITIVE_INFINITY}, {@link    * Double#NEGATIVE_INFINITY}, or {@link Double#NaN}) then the result is {@link Double#NaN}.    *    * @throws IllegalStateException if the dataset is empty or contains a single value    */
DECL|method|sampleStandardDeviation ()
specifier|public
specifier|final
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
comment|/**    * Returns the lowest value in the dataset. The count must be non-zero.    *    *<h3>Non-finite values</h3>    *    *<p>If the dataset contains {@link Double#NaN} then the result is {@link Double#NaN}. If it    * contains {@link Double#NEGATIVE_INFINITY} and not {@link Double#NaN} then the result is {@link    * Double#NEGATIVE_INFINITY}. If it contains {@link Double#POSITIVE_INFINITY} and finite values    * only then the result is the lowest finite value. If it contains {@link    * Double#POSITIVE_INFINITY} only then the result is {@link Double#POSITIVE_INFINITY}.    *    * @throws IllegalStateException if the dataset is empty    */
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
comment|/**    * Returns the highest value in the dataset. The count must be non-zero.    *    *<h3>Non-finite values</h3>    *    *<p>If the dataset contains {@link Double#NaN} then the result is {@link Double#NaN}. If it    * contains {@link Double#POSITIVE_INFINITY} and not {@link Double#NaN} then the result is {@link    * Double#POSITIVE_INFINITY}. If it contains {@link Double#NEGATIVE_INFINITY} and finite values    * only then the result is the highest finite value. If it contains {@link    * Double#NEGATIVE_INFINITY} only then the result is {@link Double#NEGATIVE_INFINITY}.    *    * @throws IllegalStateException if the dataset is empty    */
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
DECL|method|sumOfSquaresOfDeltas ()
name|double
name|sumOfSquaresOfDeltas
parameter_list|()
block|{
return|return
name|sumOfSquaresOfDeltas
return|;
block|}
comment|/**    * Calculates the new value for the accumulated mean when a value is added, in the case where at    * least one of the previous mean and the value is non-finite.    */
DECL|method|calculateNewMeanNonFinite (double previousMean, double value)
specifier|static
name|double
name|calculateNewMeanNonFinite
parameter_list|(
name|double
name|previousMean
parameter_list|,
name|double
name|value
parameter_list|)
block|{
comment|/*      * Desired behaviour is to match the results of applying the naive mean formula. In particular,      * the update formula can subtract infinities in cases where the naive formula would add them.      *      * Consequently:      * 1. If the previous mean is finite and the new value is non-finite then the new mean is that      *    value (whether it is NaN or infinity).      * 2. If the new value is finite and the previous mean is non-finite then the mean is unchanged      *    (whether it is NaN or infinity).      * 3. If both the previous mean and the new value are non-finite and...      * 3a. ...either or both is NaN (so mean != value) then the new mean is NaN.      * 3b. ...they are both the same infinities (so mean == value) then the mean is unchanged.      * 3c. ...they are different infinities (so mean != value) then the new mean is NaN.      */
if|if
condition|(
name|isFinite
argument_list|(
name|previousMean
argument_list|)
condition|)
block|{
comment|// This is case 1.
return|return
name|value
return|;
block|}
elseif|else
if|if
condition|(
name|isFinite
argument_list|(
name|value
argument_list|)
operator|||
name|previousMean
operator|==
name|value
condition|)
block|{
comment|// This is case 2. or 3b.
return|return
name|previousMean
return|;
block|}
else|else
block|{
comment|// This is case 3a. or 3c.
return|return
name|NaN
return|;
block|}
block|}
block|}
end_class

end_unit

