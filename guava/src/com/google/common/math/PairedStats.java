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
name|annotations
operator|.
name|GwtIncompatible
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
comment|/**  * An immutable value object capturing some basic statistics about a collection of paired double  * values (e.g. points on a plane). Build instances with {@link PairedStatsAccumulator#snapshot}.  *  * @author Pete Gillin  * @since 20.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
DECL|class|PairedStats
specifier|public
specifier|final
class|class
name|PairedStats
implements|implements
name|Serializable
block|{
DECL|field|xStats
specifier|private
specifier|final
name|Stats
name|xStats
decl_stmt|;
DECL|field|yStats
specifier|private
specifier|final
name|Stats
name|yStats
decl_stmt|;
DECL|field|sumOfProductsOfDeltas
specifier|private
specifier|final
name|double
name|sumOfProductsOfDeltas
decl_stmt|;
comment|/**    * Internal constructor. Users should use {@link PairedStatsAccumulator#snapshot}.    *    *<p>To ensure that the created instance obeys its contract, the parameters should satisfy the    * following constraints. This is the callers responsibility and is not enforced here.    *    *<ul>    *<li>Both {@code xStats} and {@code yStats} must have the same {@code count}.    *<li>If that {@code count} is 1, {@code sumOfProductsOfDeltas} must be exactly 0.0.    *<li>If that {@code count} is more than 1, {@code sumOfProductsOfDeltas} must be finite.    *</ul>    */
DECL|method|PairedStats (Stats xStats, Stats yStats, double sumOfProductsOfDeltas)
name|PairedStats
parameter_list|(
name|Stats
name|xStats
parameter_list|,
name|Stats
name|yStats
parameter_list|,
name|double
name|sumOfProductsOfDeltas
parameter_list|)
block|{
name|this
operator|.
name|xStats
operator|=
name|xStats
expr_stmt|;
name|this
operator|.
name|yStats
operator|=
name|yStats
expr_stmt|;
name|this
operator|.
name|sumOfProductsOfDeltas
operator|=
name|sumOfProductsOfDeltas
expr_stmt|;
block|}
comment|/** Returns the number of pairs in the dataset. */
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
comment|/** Returns the statistics on the {@code x} values alone. */
DECL|method|xStats ()
specifier|public
name|Stats
name|xStats
parameter_list|()
block|{
return|return
name|xStats
return|;
block|}
comment|/** Returns the statistics on the {@code y} values alone. */
DECL|method|yStats ()
specifier|public
name|Stats
name|yStats
parameter_list|()
block|{
return|return
name|yStats
return|;
block|}
comment|/**    * Returns the population covariance of the values. The count must be non-zero.    *    *<p>This is guaranteed to return zero if the dataset contains a single pair of finite values. It    * is not guaranteed to return zero when the dataset consists of the same pair of values multiple    * times, due to numerical errors.    *    *<h3>Non-finite values</h3>    *    *<p>If the dataset contains any non-finite values ({@link Double#POSITIVE_INFINITY}, {@link    * Double#NEGATIVE_INFINITY}, or {@link Double#NaN}) then the result is {@link Double#NaN}.    *    * @throws IllegalStateException if the dataset is empty    */
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
comment|/**    * Returns the sample covariance of the values. The count must be greater than one.    *    *<p>This is not guaranteed to return zero when the dataset consists of the same pair of values    * multiple times, due to numerical errors.    *    *<h3>Non-finite values</h3>    *    *<p>If the dataset contains any non-finite values ({@link Double#POSITIVE_INFINITY}, {@link    * Double#NEGATIVE_INFINITY}, or {@link Double#NaN}) then the result is {@link Double#NaN}.    *    * @throws IllegalStateException if the dataset is empty or contains a single pair of values    */
DECL|method|sampleCovariance ()
specifier|public
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
comment|/**    * Returns the<a href="http://mathworld.wolfram.com/CorrelationCoefficient.html">Pearson's or    * product-moment correlation coefficient</a> of the values. The count must greater than one, and    * the {@code x} and {@code y} values must both have non-zero population variance (i.e. {@code    * xStats().populationVariance()> 0.0&& yStats().populationVariance()> 0.0}). The result is not    * guaranteed to be exactly +/-1 even when the data are perfectly (anti-)correlated, due to    * numerical errors. However, it is guaranteed to be in the inclusive range [-1, +1].    *    *<h3>Non-finite values</h3>    *    *<p>If the dataset contains any non-finite values ({@link Double#POSITIVE_INFINITY}, {@link    * Double#NEGATIVE_INFINITY}, or {@link Double#NaN}) then the result is {@link Double#NaN}.    *    * @throws IllegalStateException if the dataset is empty or contains a single pair of values, or    *     either the {@code x} and {@code y} dataset has zero population variance    */
DECL|method|pearsonsCorrelationCoefficient ()
specifier|public
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
argument_list|()
operator|.
name|sumOfSquaresOfDeltas
argument_list|()
decl_stmt|;
name|double
name|ySumOfSquaresOfDeltas
init|=
name|yStats
argument_list|()
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
comment|/**    * Returns a linear transformation giving the best fit to the data according to<a    * href="http://mathworld.wolfram.com/LeastSquaresFitting.html">Ordinary Least Squares linear    * regression</a> of {@code y} as a function of {@code x}. The count must be greater than one, and    * either the {@code x} or {@code y} data must have a non-zero population variance (i.e. {@code    * xStats().populationVariance()> 0.0 || yStats().populationVariance()> 0.0}). The result is    * guaranteed to be horizontal if there is variance in the {@code x} data but not the {@code y}    * data, and vertical if there is variance in the {@code y} data but not the {@code x} data.    *    *<p>This fit minimizes the root-mean-square error in {@code y} as a function of {@code x}. This    * error is defined as the square root of the mean of the squares of the differences between the    * actual {@code y} values of the data and the values predicted by the fit for the {@code x}    * values (i.e. it is the square root of the mean of the squares of the vertical distances between    * the data points and the best fit line). For this fit, this error is a fraction {@code sqrt(1 -    * R*R)} of the population standard deviation of {@code y}, where {@code R} is the Pearson's    * correlation coefficient (as given by {@link #pearsonsCorrelationCoefficient()}).    *    *<p>The corresponding root-mean-square error in {@code x} as a function of {@code y} is a    * fraction {@code sqrt(1/(R*R) - 1)} of the population standard deviation of {@code x}. This fit    * does not normally minimize that error: to do that, you should swap the roles of {@code x} and    * {@code y}.    *    *<h3>Non-finite values</h3>    *    *<p>If the dataset contains any non-finite values ({@link Double#POSITIVE_INFINITY}, {@link    * Double#NEGATIVE_INFINITY}, or {@link Double#NaN}) then the result is {@link    * LinearTransformation#forNaN()}.    *    * @throws IllegalStateException if the dataset is empty or contains a single pair of values, or    *     both the {@code x} and {@code y} dataset must have zero population variance    */
DECL|method|leastSquaresFit ()
specifier|public
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
comment|/**    * {@inheritDoc}    *    *<p><b>Note:</b> This tests exact equality of the calculated statistics, including the floating    * point values. Two instances are guaranteed to be considered equal if one is copied from the    * other using {@code second = new PairedStatsAccumulator().addAll(first).snapshot()}, if both    * were obtained by calling {@code snapshot()} on the same {@link PairedStatsAccumulator} without    * adding any values in between the two calls, or if one is obtained from the other after    * round-tripping through java serialization. However, floating point rounding errors mean that it    * may be false for some instances where the statistics are mathematically equal, including    * instances constructed from the same values in a different order... or (in the general case)    * even in the same order. (It is guaranteed to return true for instances constructed from the    * same values in the same order if {@code strictfp} is in effect, or if the system architecture    * guarantees {@code strictfp}-like semantics.)    */
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
name|PairedStats
name|other
init|=
operator|(
name|PairedStats
operator|)
name|obj
decl_stmt|;
return|return
name|xStats
operator|.
name|equals
argument_list|(
name|other
operator|.
name|xStats
argument_list|)
operator|&&
name|yStats
operator|.
name|equals
argument_list|(
name|other
operator|.
name|yStats
argument_list|)
operator|&&
name|doubleToLongBits
argument_list|(
name|sumOfProductsOfDeltas
argument_list|)
operator|==
name|doubleToLongBits
argument_list|(
name|other
operator|.
name|sumOfProductsOfDeltas
argument_list|)
return|;
block|}
comment|/**    * {@inheritDoc}    *    *<p><b>Note:</b> This hash code is consistent with exact equality of the calculated statistics,    * including the floating point values. See the note on {@link #equals} for details.    */
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
name|xStats
argument_list|,
name|yStats
argument_list|,
name|sumOfProductsOfDeltas
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
if|if
condition|(
name|count
argument_list|()
operator|>
literal|0
condition|)
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
literal|"xStats"
argument_list|,
name|xStats
argument_list|)
operator|.
name|add
argument_list|(
literal|"yStats"
argument_list|,
name|yStats
argument_list|)
operator|.
name|add
argument_list|(
literal|"populationCovariance"
argument_list|,
name|populationCovariance
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
else|else
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
literal|"xStats"
argument_list|,
name|xStats
argument_list|)
operator|.
name|add
argument_list|(
literal|"yStats"
argument_list|,
name|yStats
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
DECL|method|sumOfProductsOfDeltas ()
name|double
name|sumOfProductsOfDeltas
parameter_list|()
block|{
return|return
name|sumOfProductsOfDeltas
return|;
block|}
DECL|method|ensurePositive (double value)
specifier|private
specifier|static
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
comment|// Serialization helpers
comment|/** The size of byte array representation in bytes. */
DECL|field|BYTES
specifier|private
specifier|static
specifier|final
name|int
name|BYTES
init|=
name|Stats
operator|.
name|BYTES
operator|*
literal|2
operator|+
name|Double
operator|.
name|SIZE
operator|/
name|Byte
operator|.
name|SIZE
decl_stmt|;
comment|/**    * Gets a byte array representation of this instance.    *    *<p><b>Note:</b> No guarantees are made regarding stability of the representation between    * versions.    */
DECL|method|toByteArray ()
specifier|public
name|byte
index|[]
name|toByteArray
parameter_list|()
block|{
name|ByteBuffer
name|buffer
init|=
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
decl_stmt|;
name|xStats
operator|.
name|writeTo
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
name|yStats
operator|.
name|writeTo
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|putDouble
argument_list|(
name|sumOfProductsOfDeltas
argument_list|)
expr_stmt|;
return|return
name|buffer
operator|.
name|array
argument_list|()
return|;
block|}
comment|/**    * Creates a {@link PairedStats} instance from the given byte representation which was obtained by    * {@link #toByteArray}.    *    *<p><b>Note:</b> No guarantees are made regarding stability of the representation between    * versions.    */
DECL|method|fromByteArray (byte[] byteArray)
specifier|public
specifier|static
name|PairedStats
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
operator|==
name|BYTES
argument_list|,
literal|"Expected PairedStats.BYTES = %s, got %s"
argument_list|,
name|BYTES
argument_list|,
name|byteArray
operator|.
name|length
argument_list|)
expr_stmt|;
name|ByteBuffer
name|buffer
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
name|Stats
name|xStats
init|=
name|Stats
operator|.
name|readFrom
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
name|Stats
name|yStats
init|=
name|Stats
operator|.
name|readFrom
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
name|double
name|sumOfProductsOfDeltas
init|=
name|buffer
operator|.
name|getDouble
argument_list|()
decl_stmt|;
return|return
operator|new
name|PairedStats
argument_list|(
name|xStats
argument_list|,
name|yStats
argument_list|,
name|sumOfProductsOfDeltas
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

