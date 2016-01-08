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

begin_comment
comment|/**  * A mutable object which accumulates paired double values (e.g. points on a plane) and tracks  * some basic statistics over all the values added so far. This class is not thread safe.  *  * @author Pete Gillin  * @since 20.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
DECL|class|PairedStatsAccumulator
specifier|public
specifier|final
class|class
name|PairedStatsAccumulator
block|{
comment|// These fields must satisfy the requirements of PairedStats' constructor as well as those of the
comment|// stat methods of this class.
DECL|field|xStats
specifier|private
specifier|final
name|StatsAccumulator
name|xStats
init|=
operator|new
name|StatsAccumulator
argument_list|()
decl_stmt|;
DECL|field|yStats
specifier|private
specifier|final
name|StatsAccumulator
name|yStats
init|=
operator|new
name|StatsAccumulator
argument_list|()
decl_stmt|;
DECL|field|sumOfProductsOfDeltas
specifier|private
name|double
name|sumOfProductsOfDeltas
init|=
literal|0.0
decl_stmt|;
comment|/**    * Adds the given pair of values to the dataset.    */
DECL|method|add (double x, double y)
specifier|public
name|void
name|add
parameter_list|(
name|double
name|x
parameter_list|,
name|double
name|y
parameter_list|)
block|{
comment|// We extend the recursive expression for the one-variable case at Art of Computer Programming
comment|// vol. 2, Knuth, 4.2.2, (16) to the two-variable case. We have two value series x_i and y_i.
comment|// We define the arithmetic means X_n = 1/n \sum_{i=1}^n x_i, and Y_n = 1/n \sum_{i=1}^n y_i.
comment|// We also define the sum of the products of the differences from the means
comment|//           C_n = \sum_{i=1}^n x_i y_i - n X_n Y_n
comment|// for all n>= 1. Then for all n> 1:
comment|//       C_{n-1} = \sum_{i=1}^{n-1} x_i y_i - (n-1) X_{n-1} Y_{n-1}
comment|// C_n - C_{n-1} = x_n y_n - n X_n Y_n + (n-1) X_{n-1} Y_{n-1}
comment|//               = x_n y_n - X_n [ y_n + (n-1) Y_{n-1} ] + [ n X_n - x_n ] Y_{n-1}
comment|//               = x_n y_n - X_n y_n - x_n Y_{n-1} + X_n Y_{n-1}
comment|//               = (x_n - X_n) (y_n - Y_{n-1})
name|xStats
operator|.
name|add
argument_list|(
name|x
argument_list|)
expr_stmt|;
if|if
condition|(
name|isFinite
argument_list|(
name|x
argument_list|)
operator|&&
name|isFinite
argument_list|(
name|y
argument_list|)
condition|)
block|{
if|if
condition|(
name|xStats
operator|.
name|count
argument_list|()
operator|>
literal|1
condition|)
block|{
name|sumOfProductsOfDeltas
operator|+=
operator|(
name|x
operator|-
name|xStats
operator|.
name|mean
argument_list|()
operator|)
operator|*
operator|(
name|y
operator|-
name|yStats
operator|.
name|mean
argument_list|()
operator|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|sumOfProductsOfDeltas
operator|=
name|NaN
expr_stmt|;
block|}
name|yStats
operator|.
name|add
argument_list|(
name|y
argument_list|)
expr_stmt|;
block|}
comment|/**    * Adds the given statistics to the dataset, as if the individual values used to compute the    * statistics had been added directly.    */
DECL|method|addAll (PairedStats values)
specifier|public
name|void
name|addAll
parameter_list|(
name|PairedStats
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
name|xStats
operator|.
name|addAll
argument_list|(
name|values
operator|.
name|xStats
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|yStats
operator|.
name|count
argument_list|()
operator|==
literal|0
condition|)
block|{
name|sumOfProductsOfDeltas
operator|=
name|values
operator|.
name|sumOfProductsOfDeltas
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// This is a generalized version of the calculation in add(double, double) above. Note that
comment|// non-finite inputs will have sumOfProductsOfDeltas = NaN, so non-finite values will result
comment|// in NaN naturally.
name|sumOfProductsOfDeltas
operator|+=
name|values
operator|.
name|sumOfProductsOfDeltas
argument_list|()
operator|+
operator|(
name|values
operator|.
name|xStats
argument_list|()
operator|.
name|mean
argument_list|()
operator|-
name|xStats
operator|.
name|mean
argument_list|()
operator|)
operator|*
operator|(
name|values
operator|.
name|yStats
argument_list|()
operator|.
name|mean
argument_list|()
operator|-
name|yStats
operator|.
name|mean
argument_list|()
operator|)
operator|*
name|values
operator|.
name|count
argument_list|()
expr_stmt|;
block|}
name|yStats
operator|.
name|addAll
argument_list|(
name|values
operator|.
name|yStats
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns an immutable snapshot of the current statistics.    */
DECL|method|snapshot ()
specifier|public
name|PairedStats
name|snapshot
parameter_list|()
block|{
return|return
operator|new
name|PairedStats
argument_list|(
name|xStats
operator|.
name|snapshot
argument_list|()
argument_list|,
name|yStats
operator|.
name|snapshot
argument_list|()
argument_list|,
name|sumOfProductsOfDeltas
argument_list|)
return|;
block|}
comment|/**    * Returns the number of pairs in the dataset.    */
DECL|method|count ()
specifier|public
name|long
name|count
parameter_list|()
block|{
return|return
name|xStats
operator|.
name|count
argument_list|()
return|;
block|}
comment|/**    * Returns an immutable snapshot of the statistics on the {@code x} values alone.    */
DECL|method|xStats ()
specifier|public
name|Stats
name|xStats
parameter_list|()
block|{
return|return
name|xStats
operator|.
name|snapshot
argument_list|()
return|;
block|}
comment|/**    * Returns an immutable snapshot of the statistics on the {@code y} values alone.    */
DECL|method|yStats ()
specifier|public
name|Stats
name|yStats
parameter_list|()
block|{
return|return
name|yStats
operator|.
name|snapshot
argument_list|()
return|;
block|}
comment|/**    * Returns the population covariance of the values. The count must be non-zero.    *    *<p>This is guaranteed to return zero if the dataset contains a single pair of finite values.    * It is not guaranteed to return zero when the dataset consists of the same pair of values    * multiple times, due to numerical errors.    *    *<h3>Non-finite values</h3>    *    *<p>If the dataset contains any non-finite values ({@link Double#POSITIVE_INFINITY},    * {@link Double#NEGATIVE_INFINITY}, or {@link Double#NaN}) then the result is {@link Double#NaN}.    *    * @throws IllegalStateException if the dataset is empty    */
DECL|method|populationCovariance ()
specifier|public
name|double
name|populationCovariance
parameter_list|()
block|{
name|checkState
argument_list|(
name|count
argument_list|()
operator|!=
literal|0
argument_list|)
expr_stmt|;
return|return
name|sumOfProductsOfDeltas
operator|/
name|count
argument_list|()
return|;
block|}
comment|/**    * Returns the sample covariance of the values. The count must be greater than one.    *    *<p>This is not guaranteed to return zero when the dataset consists of the same pair of values    * multiple times, due to numerical errors.    *    *<h3>Non-finite values</h3>    *    *<p>If the dataset contains any non-finite values ({@link Double#POSITIVE_INFINITY},    * {@link Double#NEGATIVE_INFINITY}, or {@link Double#NaN}) then the result is {@link Double#NaN}.    *    * @throws IllegalStateException if the dataset is empty or contains a single pair of values    */
DECL|method|sampleCovariance ()
specifier|public
specifier|final
name|double
name|sampleCovariance
parameter_list|()
block|{
name|checkState
argument_list|(
name|count
argument_list|()
operator|>
literal|1
argument_list|)
expr_stmt|;
return|return
name|sumOfProductsOfDeltas
operator|/
operator|(
name|count
argument_list|()
operator|-
literal|1
operator|)
return|;
block|}
comment|/**    * Returns the<a href="http://mathworld.wolfram.com/CorrelationCoefficient.html">Pearson's or    * product-moment correlation coefficient</a> of the values. The count must greater than one, and    * the {@code x} and {@code y} values must both have non-zero population variance (i.e.    * {@code xStats().populationVariance()> 0.0&& yStats().populationVariance()> 0.0}). The result    * is not guaranteed to be exactly +/-1 even when the data are perfectly (anti-)correlated, due to    * numerical errors. However, it is guaranteed to be in the inclusive range [-1, +1].    *    *<h3>Non-finite values</h3>    *    *<p>If the dataset contains any non-finite values ({@link Double#POSITIVE_INFINITY},    * {@link Double#NEGATIVE_INFINITY}, or {@link Double#NaN}) then the result is {@link Double#NaN}.    *    * @throws IllegalStateException if the dataset is empty or contains a single pair of values, or    *     either the {@code x} and {@code y} dataset has zero population variance    */
DECL|method|pearsonsCorrelationCoefficient ()
specifier|public
specifier|final
name|double
name|pearsonsCorrelationCoefficient
parameter_list|()
block|{
name|checkState
argument_list|(
name|count
argument_list|()
operator|>
literal|1
argument_list|)
expr_stmt|;
if|if
condition|(
name|isNaN
argument_list|(
name|sumOfProductsOfDeltas
argument_list|)
condition|)
block|{
return|return
name|NaN
return|;
block|}
name|double
name|xSumOfSquaresOfDeltas
init|=
name|xStats
operator|.
name|sumOfSquaresOfDeltas
argument_list|()
decl_stmt|;
name|double
name|ySumOfSquaresOfDeltas
init|=
name|yStats
operator|.
name|sumOfSquaresOfDeltas
argument_list|()
decl_stmt|;
name|checkState
argument_list|(
name|xSumOfSquaresOfDeltas
operator|>
literal|0.0
argument_list|)
expr_stmt|;
name|checkState
argument_list|(
name|ySumOfSquaresOfDeltas
operator|>
literal|0.0
argument_list|)
expr_stmt|;
comment|// The product of two positive numbers can be zero if the multiplication underflowed. We
comment|// force a positive value by effectively rounding up to MIN_VALUE.
name|double
name|productOfSumsOfSquaresOfDeltas
init|=
name|ensurePositive
argument_list|(
name|xSumOfSquaresOfDeltas
operator|*
name|ySumOfSquaresOfDeltas
argument_list|)
decl_stmt|;
return|return
name|ensureInUnitRange
argument_list|(
name|sumOfProductsOfDeltas
operator|/
name|Math
operator|.
name|sqrt
argument_list|(
name|productOfSumsOfSquaresOfDeltas
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns a linear transformation giving the best fit to the data according to    *<a href="http://mathworld.wolfram.com/LeastSquaresFitting.html">Ordinary Least Squares linear    * regression</a> of {@code y} as a function of {@code x}. The count must be greater than one, and    * either the {@code x} or {@code y} data must have a non-zero population variance (i.e.    * {@code xStats().populationVariance()> 0.0 || yStats().populationVariance()> 0.0}). The result    * is guaranteed to be horizontal if there is variance in the {@code x} data but not the {@code y}    * data, and vertical if there is variance in the {@code y} data but not the {@code x} data.    *    *<p>This fit minimizes the root-mean-square error in {@code y} as a function of {@code x}. This    * error is defined as the square root of the mean of the squares of the differences between the    * actual {@code y} values of the data and the values predicted by the fit for the {@code x}    * values (i.e. it is the square root of the mean of the squares of the vertical distances between    * the data points and the best fit line). For this fit, this error is a fraction    * {@code sqrt(1 - R*R)} of the population standard deviation of {@code y}, where {@code R} is the    * Pearson's correlation coefficient (as given by {@link #pearsonsCorrelationCoefficient()}).    *    *<p>The corresponding root-mean-square error in {@code x} as a function of {@code y} is a    * fraction {@code sqrt(1/(R*R) - 1)} of the population standard deviation of {@code x}. This fit    * does not normally minimize that error: to do that, you should swap the roles of {@code x} and    * {@code y}.    *    *<h3>Non-finite values</h3>    *    *<p>If the dataset contains any non-finite values ({@link Double#POSITIVE_INFINITY},    * {@link Double#NEGATIVE_INFINITY}, or {@link Double#NaN}) then the result is    * {@link LinearTransformation#forNaN()}.    *    * @throws IllegalStateException if the dataset is empty or contains a single pair of values, or    *     both the {@code x} and {@code y} dataset have zero population variance    */
DECL|method|leastSquaresFit ()
specifier|public
specifier|final
name|LinearTransformation
name|leastSquaresFit
parameter_list|()
block|{
name|checkState
argument_list|(
name|count
argument_list|()
operator|>
literal|1
argument_list|)
expr_stmt|;
if|if
condition|(
name|isNaN
argument_list|(
name|sumOfProductsOfDeltas
argument_list|)
condition|)
block|{
return|return
name|LinearTransformation
operator|.
name|forNaN
argument_list|()
return|;
block|}
name|double
name|xSumOfSquaresOfDeltas
init|=
name|xStats
operator|.
name|sumOfSquaresOfDeltas
argument_list|()
decl_stmt|;
if|if
condition|(
name|xSumOfSquaresOfDeltas
operator|>
literal|0.0
condition|)
block|{
if|if
condition|(
name|yStats
operator|.
name|sumOfSquaresOfDeltas
argument_list|()
operator|>
literal|0.0
condition|)
block|{
return|return
name|LinearTransformation
operator|.
name|mapping
argument_list|(
name|xStats
operator|.
name|mean
argument_list|()
argument_list|,
name|yStats
operator|.
name|mean
argument_list|()
argument_list|)
operator|.
name|withSlope
argument_list|(
name|sumOfProductsOfDeltas
operator|/
name|xSumOfSquaresOfDeltas
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|LinearTransformation
operator|.
name|horizontal
argument_list|(
name|yStats
operator|.
name|mean
argument_list|()
argument_list|)
return|;
block|}
block|}
else|else
block|{
name|checkState
argument_list|(
name|yStats
operator|.
name|sumOfSquaresOfDeltas
argument_list|()
operator|>
literal|0.0
argument_list|)
expr_stmt|;
return|return
name|LinearTransformation
operator|.
name|vertical
argument_list|(
name|xStats
operator|.
name|mean
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|method|ensurePositive (double value)
specifier|private
name|double
name|ensurePositive
parameter_list|(
name|double
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|>
literal|0.0
condition|)
block|{
return|return
name|value
return|;
block|}
else|else
block|{
return|return
name|Double
operator|.
name|MIN_VALUE
return|;
block|}
block|}
DECL|method|ensureInUnitRange (double value)
specifier|private
specifier|static
name|double
name|ensureInUnitRange
parameter_list|(
name|double
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|>=
literal|1.0
condition|)
block|{
return|return
literal|1.0
return|;
block|}
if|if
condition|(
name|value
operator|<=
operator|-
literal|1.0
condition|)
block|{
return|return
operator|-
literal|1.0
return|;
block|}
return|return
name|value
return|;
block|}
block|}
end_class

end_unit

