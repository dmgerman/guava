begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.hash
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|hash
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
name|VisibleForTesting
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Predicate
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
name|hash
operator|.
name|BloomFilterStrategies
operator|.
name|BitArray
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * A Bloom filter for instances of {@code T}. A Bloom filter offers an approximate containment test  * with one-sided error: if it claims that an element is contained in it, this might be in error,  * but if it claims that an element is<i>not</i> contained in it, then this is definitely true.  *  *<p>If you are unfamiliar with Bloom filters, this nice  *<a href="http://llimllib.github.com/bloomfilter-tutorial/">tutorial</a> may help you understand  * how they work.  *  *<p>The false positive probability ({@code FPP}) of a bloom filter is defined as the probability  * that {@linkplain #mightContain(Object)} will erroneously return {@code true} for an object that  * has not actually been put in the {@code BloomFilter}.  *  *  * @param<T> the type of instances that the {@code BloomFilter} accepts  * @author Dimitris Andreou  * @author Kevin Bourrillion  * @since 11.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|BloomFilter
specifier|public
specifier|final
class|class
name|BloomFilter
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Predicate
argument_list|<
name|T
argument_list|>
implements|,
name|Serializable
block|{
comment|/**    * A strategy to translate T instances, to {@code numHashFunctions} bit indexes.    *    *<p>Implementations should be collections of pure functions (i.e. stateless).    */
DECL|interface|Strategy
interface|interface
name|Strategy
extends|extends
name|java
operator|.
name|io
operator|.
name|Serializable
block|{
comment|/**      * Sets {@code numHashFunctions} bits of the given bit array, by hashing a user element.      *      *<p>Returns whether any bits changed as a result of this operation.      */
DECL|method|put (T object, Funnel<? super T> funnel, int numHashFunctions, BitArray bits)
parameter_list|<
name|T
parameter_list|>
name|boolean
name|put
parameter_list|(
name|T
name|object
parameter_list|,
name|Funnel
argument_list|<
name|?
super|super
name|T
argument_list|>
name|funnel
parameter_list|,
name|int
name|numHashFunctions
parameter_list|,
name|BitArray
name|bits
parameter_list|)
function_decl|;
comment|/**      * Queries {@code numHashFunctions} bits of the given bit array, by hashing a user element;      * returns {@code true} if and only if all selected bits are set.      */
DECL|method|mightContain ( T object, Funnel<? super T> funnel, int numHashFunctions, BitArray bits)
parameter_list|<
name|T
parameter_list|>
name|boolean
name|mightContain
parameter_list|(
name|T
name|object
parameter_list|,
name|Funnel
argument_list|<
name|?
super|super
name|T
argument_list|>
name|funnel
parameter_list|,
name|int
name|numHashFunctions
parameter_list|,
name|BitArray
name|bits
parameter_list|)
function_decl|;
comment|/**      * Identifier used to encode this strategy, when marshalled as part of a BloomFilter.      * Only values in the [-128, 127] range are valid for the compact serial form.      * Non-negative values are reserved for enums defined in BloomFilterStrategies;      * negative values are reserved for any custom, stateful strategy we may define      * (e.g. any kind of strategy that would depend on user input).      */
DECL|method|ordinal ()
name|int
name|ordinal
parameter_list|()
function_decl|;
block|}
comment|/** The bit set of the BloomFilter (not necessarily power of 2!)*/
DECL|field|bits
specifier|private
specifier|final
name|BitArray
name|bits
decl_stmt|;
comment|/** Number of hashes per element */
DECL|field|numHashFunctions
specifier|private
specifier|final
name|int
name|numHashFunctions
decl_stmt|;
comment|/** The funnel to translate Ts to bytes */
DECL|field|funnel
specifier|private
specifier|final
name|Funnel
argument_list|<
name|T
argument_list|>
name|funnel
decl_stmt|;
comment|/**    * The strategy we employ to map an element T to {@code numHashFunctions} bit indexes.    */
DECL|field|strategy
specifier|private
specifier|final
name|Strategy
name|strategy
decl_stmt|;
comment|/**    * Creates a BloomFilter.    */
DECL|method|BloomFilter (BitArray bits, int numHashFunctions, Funnel<T> funnel, Strategy strategy)
specifier|private
name|BloomFilter
parameter_list|(
name|BitArray
name|bits
parameter_list|,
name|int
name|numHashFunctions
parameter_list|,
name|Funnel
argument_list|<
name|T
argument_list|>
name|funnel
parameter_list|,
name|Strategy
name|strategy
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|numHashFunctions
operator|>
literal|0
argument_list|,
literal|"numHashFunctions (%s) must be> 0"
argument_list|,
name|numHashFunctions
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|numHashFunctions
operator|<=
literal|255
argument_list|,
literal|"numHashFunctions (%s) must be<= 255"
argument_list|,
name|numHashFunctions
argument_list|)
expr_stmt|;
name|this
operator|.
name|bits
operator|=
name|checkNotNull
argument_list|(
name|bits
argument_list|)
expr_stmt|;
name|this
operator|.
name|numHashFunctions
operator|=
name|numHashFunctions
expr_stmt|;
name|this
operator|.
name|funnel
operator|=
name|checkNotNull
argument_list|(
name|funnel
argument_list|)
expr_stmt|;
name|this
operator|.
name|strategy
operator|=
name|checkNotNull
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
block|}
comment|/**    * Creates a new {@code BloomFilter} that's a copy of this instance. The new instance is equal to    * this instance but shares no mutable state.    *    * @since 12.0    */
DECL|method|copy ()
specifier|public
name|BloomFilter
argument_list|<
name|T
argument_list|>
name|copy
parameter_list|()
block|{
return|return
operator|new
name|BloomFilter
argument_list|<
name|T
argument_list|>
argument_list|(
name|bits
operator|.
name|copy
argument_list|()
argument_list|,
name|numHashFunctions
argument_list|,
name|funnel
argument_list|,
name|strategy
argument_list|)
return|;
block|}
comment|/**    * Returns {@code true} if the element<i>might</i> have been put in this Bloom filter,    * {@code false} if this is<i>definitely</i> not the case.    */
DECL|method|mightContain (T object)
specifier|public
name|boolean
name|mightContain
parameter_list|(
name|T
name|object
parameter_list|)
block|{
return|return
name|strategy
operator|.
name|mightContain
argument_list|(
name|object
argument_list|,
name|funnel
argument_list|,
name|numHashFunctions
argument_list|,
name|bits
argument_list|)
return|;
block|}
comment|/**    * Equivalent to {@link #mightContain}; provided only to satisfy the {@link Predicate} interface.    * When using a reference of type {@code BloomFilter}, always invoke {@link #mightContain}    * directly instead.    */
DECL|method|apply (T input)
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
name|T
name|input
parameter_list|)
block|{
return|return
name|mightContain
argument_list|(
name|input
argument_list|)
return|;
block|}
comment|/**    * Puts an element into this {@code BloomFilter}. Ensures that subsequent invocations of    * {@link #mightContain(Object)} with the same element will always return {@code true}.    *    * @return true if the bloom filter's bits changed as a result of this operation. If the bits    *     changed, this is<i>definitely</i> the first time {@code object} has been added to the    *     filter. If the bits haven't changed, this<i>might</i> be the first time {@code object}    *     has been added to the filter. Note that {@code put(t)} always returns the    *<i>opposite</i> result to what {@code mightContain(t)} would have returned at the time    *     it is called."    * @since 12.0 (present in 11.0 with {@code void} return type})    */
DECL|method|put (T object)
specifier|public
name|boolean
name|put
parameter_list|(
name|T
name|object
parameter_list|)
block|{
return|return
name|strategy
operator|.
name|put
argument_list|(
name|object
argument_list|,
name|funnel
argument_list|,
name|numHashFunctions
argument_list|,
name|bits
argument_list|)
return|;
block|}
comment|/**    * Returns the probability that {@linkplain #mightContain(Object)} will erroneously return    * {@code true} for an object that has not actually been put in the {@code BloomFilter}.    *    *<p>Ideally, this number should be close to the {@code fpp} parameter    * passed in {@linkplain #create(Funnel, int, double)}, or smaller. If it is    * significantly higher, it is usually the case that too many elements (more than    * expected) have been put in the {@code BloomFilter}, degenerating it.    *    * @since 14.0 (since 11.0 as expectedFalsePositiveProbability())    */
DECL|method|expectedFpp ()
specifier|public
name|double
name|expectedFpp
parameter_list|()
block|{
comment|// You down with FPP? (Yeah you know me!) Who's down with FPP? (Every last homie!)
return|return
name|Math
operator|.
name|pow
argument_list|(
operator|(
name|double
operator|)
name|bits
operator|.
name|bitCount
argument_list|()
operator|/
name|bitSize
argument_list|()
argument_list|,
name|numHashFunctions
argument_list|)
return|;
block|}
comment|/**    * Returns the number of bits in the underlying bit array.    */
DECL|method|bitSize ()
annotation|@
name|VisibleForTesting
name|long
name|bitSize
parameter_list|()
block|{
return|return
name|bits
operator|.
name|bitSize
argument_list|()
return|;
block|}
comment|/**    * Determines whether a given bloom filter is compatible with this bloom filter. For two    * bloom filters to be compatible, they must:    *<ul>    *<li>not be the same instance    *<li>have the same number of hash functions    *<li>have the same bit size    *<li>have the same strategy    *<li>have equal funnels    *<ul>    *    * @param that The bloom filter to check for compatibility.    *    * @since 15.0    */
DECL|method|isCompatible (BloomFilter that)
specifier|public
name|boolean
name|isCompatible
parameter_list|(
name|BloomFilter
name|that
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|that
argument_list|)
expr_stmt|;
return|return
operator|(
name|this
operator|!=
name|that
operator|)
operator|&&
operator|(
name|this
operator|.
name|numHashFunctions
operator|==
name|that
operator|.
name|numHashFunctions
operator|)
operator|&&
operator|(
name|this
operator|.
name|bitSize
argument_list|()
operator|==
name|that
operator|.
name|bitSize
argument_list|()
operator|)
operator|&&
operator|(
name|this
operator|.
name|strategy
operator|.
name|equals
argument_list|(
name|that
operator|.
name|strategy
argument_list|)
operator|)
operator|&&
operator|(
name|this
operator|.
name|funnel
operator|.
name|equals
argument_list|(
name|that
operator|.
name|funnel
argument_list|)
operator|)
return|;
block|}
comment|/**    * Combines this bloom filter with another bloom filter by performing a bitwise OR of the    * underlying data. The mutations happen to<b>this</b> instance. Callers must ensure the    * bloom filters are appropriately sized to avoid saturating them.    *    * @param that The bloom filter to combine this bloom filter with. It is not mutated.    * @throws IllegalArgumentException if {@code isCompatible(that) == false}    *    * @since 15.0    */
DECL|method|putAll (BloomFilter that)
specifier|public
name|void
name|putAll
parameter_list|(
name|BloomFilter
name|that
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|that
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|this
operator|!=
name|that
argument_list|,
literal|"Cannot combine a BloomFilter with itself."
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|this
operator|.
name|numHashFunctions
operator|==
name|that
operator|.
name|numHashFunctions
argument_list|,
literal|"BloomFilters must have the same number of hash functions (%s != %s)"
argument_list|,
name|this
operator|.
name|numHashFunctions
argument_list|,
name|that
operator|.
name|numHashFunctions
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|this
operator|.
name|bitSize
argument_list|()
operator|==
name|that
operator|.
name|bitSize
argument_list|()
argument_list|,
literal|"BloomFilters must have the same size underlying bit arrays (%s != %s)"
argument_list|,
name|this
operator|.
name|bitSize
argument_list|()
argument_list|,
name|that
operator|.
name|bitSize
argument_list|()
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|this
operator|.
name|strategy
operator|.
name|equals
argument_list|(
name|that
operator|.
name|strategy
argument_list|)
argument_list|,
literal|"BloomFilters must have equal strategies (%s != %s)"
argument_list|,
name|this
operator|.
name|strategy
argument_list|,
name|that
operator|.
name|strategy
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|this
operator|.
name|funnel
operator|.
name|equals
argument_list|(
name|that
operator|.
name|funnel
argument_list|)
argument_list|,
literal|"BloomFilters must have equal funnels (%s != %s)"
argument_list|,
name|this
operator|.
name|funnel
argument_list|,
name|that
operator|.
name|funnel
argument_list|)
expr_stmt|;
name|this
operator|.
name|bits
operator|.
name|putAll
argument_list|(
name|that
operator|.
name|bits
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|equals (@ullable Object object)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|==
name|this
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|object
operator|instanceof
name|BloomFilter
condition|)
block|{
name|BloomFilter
argument_list|<
name|?
argument_list|>
name|that
init|=
operator|(
name|BloomFilter
argument_list|<
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
return|return
name|this
operator|.
name|numHashFunctions
operator|==
name|that
operator|.
name|numHashFunctions
operator|&&
name|this
operator|.
name|funnel
operator|.
name|equals
argument_list|(
name|that
operator|.
name|funnel
argument_list|)
operator|&&
name|this
operator|.
name|bits
operator|.
name|equals
argument_list|(
name|that
operator|.
name|bits
argument_list|)
operator|&&
name|this
operator|.
name|strategy
operator|.
name|equals
argument_list|(
name|that
operator|.
name|strategy
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
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
name|numHashFunctions
argument_list|,
name|funnel
argument_list|,
name|strategy
argument_list|,
name|bits
argument_list|)
return|;
block|}
comment|/**    * Creates a {@link BloomFilter BloomFilter<T>} with the expected number of    * insertions and expected false positive probability.    *    *<p>Note that overflowing a {@code BloomFilter} with significantly more elements    * than specified, will result in its saturation, and a sharp deterioration of its    * false positive probability.    *    *<p>The constructed {@code BloomFilter<T>} will be serializable if the provided    * {@code Funnel<T>} is.    *    *<p>It is recommended that the funnel be implemented as a Java enum. This has the    * benefit of ensuring proper serialization and deserialization, which is important    * since {@link #equals} also relies on object identity of funnels.    *    * @param funnel the funnel of T's that the constructed {@code BloomFilter<T>} will use    * @param expectedInsertions the number of expected insertions to the constructed    *     {@code BloomFilter<T>}; must be positive    * @param fpp the desired false positive probability (must be positive and less than 1.0)    * @return a {@code BloomFilter}    */
DECL|method|create ( Funnel<T> funnel, int expectedInsertions , double fpp)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|BloomFilter
argument_list|<
name|T
argument_list|>
name|create
parameter_list|(
name|Funnel
argument_list|<
name|T
argument_list|>
name|funnel
parameter_list|,
name|int
name|expectedInsertions
comment|/* n */
parameter_list|,
name|double
name|fpp
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|funnel
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|expectedInsertions
operator|>=
literal|0
argument_list|,
literal|"Expected insertions (%s) must be>= 0"
argument_list|,
name|expectedInsertions
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|fpp
operator|>
literal|0.0
argument_list|,
literal|"False positive probability (%s) must be> 0.0"
argument_list|,
name|fpp
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|fpp
operator|<
literal|1.0
argument_list|,
literal|"False positive probability (%s) must be< 1.0"
argument_list|,
name|fpp
argument_list|)
expr_stmt|;
if|if
condition|(
name|expectedInsertions
operator|==
literal|0
condition|)
block|{
name|expectedInsertions
operator|=
literal|1
expr_stmt|;
block|}
comment|/*      * TODO(user): Put a warning in the javadoc about tiny fpp values,      * since the resulting size is proportional to -log(p), but there is not      * much of a point after all, e.g. optimalM(1000, 0.0000000000000001) = 76680      * which is less that 10kb. Who cares!      */
name|long
name|numBits
init|=
name|optimalNumOfBits
argument_list|(
name|expectedInsertions
argument_list|,
name|fpp
argument_list|)
decl_stmt|;
name|int
name|numHashFunctions
init|=
name|optimalNumOfHashFunctions
argument_list|(
name|expectedInsertions
argument_list|,
name|numBits
argument_list|)
decl_stmt|;
try|try
block|{
return|return
operator|new
name|BloomFilter
argument_list|<
name|T
argument_list|>
argument_list|(
operator|new
name|BitArray
argument_list|(
name|numBits
argument_list|)
argument_list|,
name|numHashFunctions
argument_list|,
name|funnel
argument_list|,
name|BloomFilterStrategies
operator|.
name|MURMUR128_MITZ_32
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Could not create BloomFilter of "
operator|+
name|numBits
operator|+
literal|" bits"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**    * Creates a {@link BloomFilter BloomFilter<T>} with the expected number of    * insertions and a default expected false positive probability of 3%.    *    *<p>Note that overflowing a {@code BloomFilter} with significantly more elements    * than specified, will result in its saturation, and a sharp deterioration of its    * false positive probability.    *    *<p>The constructed {@code BloomFilter<T>} will be serializable if the provided    * {@code Funnel<T>} is.    *    * @param funnel the funnel of T's that the constructed {@code BloomFilter<T>} will use    * @param expectedInsertions the number of expected insertions to the constructed    *     {@code BloomFilter<T>}; must be positive    * @return a {@code BloomFilter}    */
DECL|method|create (Funnel<T> funnel, int expectedInsertions )
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|BloomFilter
argument_list|<
name|T
argument_list|>
name|create
parameter_list|(
name|Funnel
argument_list|<
name|T
argument_list|>
name|funnel
parameter_list|,
name|int
name|expectedInsertions
comment|/* n */
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|funnel
argument_list|,
name|expectedInsertions
argument_list|,
literal|0.03
argument_list|)
return|;
comment|// FYI, for 3%, we always get 5 hash functions
block|}
comment|/*    * Cheat sheet:    *    * m: total bits    * n: expected insertions    * b: m/n, bits per insertion     * p: expected false positive probability    *    * 1) Optimal k = b * ln2    * 2) p = (1 - e ^ (-kn/m))^k    * 3) For optimal k: p = 2 ^ (-k) ~= 0.6185^b    * 4) For optimal k: m = -nlnp / ((ln2) ^ 2)    */
comment|/**    * Computes the optimal k (number of hashes per element inserted in Bloom filter), given the    * expected insertions and total number of bits in the Bloom filter.    *    * See http://en.wikipedia.org/wiki/File:Bloom_filter_fp_probability.svg for the formula.    *    * @param n expected insertions (must be positive)    * @param m total number of bits in Bloom filter (must be positive)    */
annotation|@
name|VisibleForTesting
DECL|method|optimalNumOfHashFunctions (long n, long m)
specifier|static
name|int
name|optimalNumOfHashFunctions
parameter_list|(
name|long
name|n
parameter_list|,
name|long
name|m
parameter_list|)
block|{
return|return
name|Math
operator|.
name|max
argument_list|(
literal|1
argument_list|,
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
name|m
operator|/
name|n
operator|*
name|Math
operator|.
name|log
argument_list|(
literal|2
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Computes m (total bits of Bloom filter) which is expected to achieve, for the specified    * expected insertions, the required false positive probability.    *    * See http://en.wikipedia.org/wiki/Bloom_filter#Probability_of_false_positives for the formula.    *    * @param n expected insertions (must be positive)    * @param p false positive rate (must be 0< p< 1)    */
annotation|@
name|VisibleForTesting
DECL|method|optimalNumOfBits (long n, double p)
specifier|static
name|long
name|optimalNumOfBits
parameter_list|(
name|long
name|n
parameter_list|,
name|double
name|p
parameter_list|)
block|{
if|if
condition|(
name|p
operator|==
literal|0
condition|)
block|{
name|p
operator|=
name|Double
operator|.
name|MIN_VALUE
expr_stmt|;
block|}
return|return
call|(
name|long
call|)
argument_list|(
operator|-
name|n
operator|*
name|Math
operator|.
name|log
argument_list|(
name|p
argument_list|)
operator|/
operator|(
name|Math
operator|.
name|log
argument_list|(
literal|2
argument_list|)
operator|*
name|Math
operator|.
name|log
argument_list|(
literal|2
argument_list|)
operator|)
argument_list|)
return|;
block|}
DECL|method|writeReplace ()
specifier|private
name|Object
name|writeReplace
parameter_list|()
block|{
return|return
operator|new
name|SerialForm
argument_list|<
name|T
argument_list|>
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|class|SerialForm
specifier|private
specifier|static
class|class
name|SerialForm
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Serializable
block|{
DECL|field|data
specifier|final
name|long
index|[]
name|data
decl_stmt|;
DECL|field|numHashFunctions
specifier|final
name|int
name|numHashFunctions
decl_stmt|;
DECL|field|funnel
specifier|final
name|Funnel
argument_list|<
name|T
argument_list|>
name|funnel
decl_stmt|;
DECL|field|strategy
specifier|final
name|Strategy
name|strategy
decl_stmt|;
DECL|method|SerialForm (BloomFilter<T> bf)
name|SerialForm
parameter_list|(
name|BloomFilter
argument_list|<
name|T
argument_list|>
name|bf
parameter_list|)
block|{
name|this
operator|.
name|data
operator|=
name|bf
operator|.
name|bits
operator|.
name|data
expr_stmt|;
name|this
operator|.
name|numHashFunctions
operator|=
name|bf
operator|.
name|numHashFunctions
expr_stmt|;
name|this
operator|.
name|funnel
operator|=
name|bf
operator|.
name|funnel
expr_stmt|;
name|this
operator|.
name|strategy
operator|=
name|bf
operator|.
name|strategy
expr_stmt|;
block|}
DECL|method|readResolve ()
name|Object
name|readResolve
parameter_list|()
block|{
return|return
operator|new
name|BloomFilter
argument_list|<
name|T
argument_list|>
argument_list|(
operator|new
name|BitArray
argument_list|(
name|data
argument_list|)
argument_list|,
name|numHashFunctions
argument_list|,
name|funnel
argument_list|,
name|strategy
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
literal|1
decl_stmt|;
block|}
block|}
end_class

end_unit

