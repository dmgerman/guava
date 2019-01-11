begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.cache
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|cache
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
name|util
operator|.
name|concurrent
operator|.
name|Callable
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
name|compatqual
operator|.
name|NullableDecl
import|;
end_import

begin_comment
comment|/**  * Statistics about the performance of a {@link Cache}. Instances of this class are immutable.  *  *<p>Cache statistics are incremented according to the following rules:  *  *<ul>  *<li>When a cache lookup encounters an existing cache entry {@code hitCount} is incremented.  *<li>When a cache lookup first encounters a missing cache entry, a new entry is loaded.  *<ul>  *<li>After successfully loading an entry {@code missCount} and {@code loadSuccessCount}  *             are incremented, and the total loading time, in nanoseconds, is added to {@code  *             totalLoadTime}.  *<li>When an exception is thrown while loading an entry, {@code missCount} and {@code  *             loadExceptionCount} are incremented, and the total loading time, in nanoseconds, is  *             added to {@code totalLoadTime}.  *<li>Cache lookups that encounter a missing cache entry that is still loading will wait  *             for loading to complete (whether successful or not) and then increment {@code  *             missCount}.  *</ul>  *<li>When an entry is evicted from the cache, {@code evictionCount} is incremented.  *<li>No stats are modified when a cache entry is invalidated or manually removed.  *<li>No stats are modified by operations invoked on the {@linkplain Cache#asMap asMap} view of  *       the cache.  *</ul>  *  *<p>A lookup is specifically defined as an invocation of one of the methods {@link  * LoadingCache#get(Object)}, {@link LoadingCache#getUnchecked(Object)}, {@link Cache#get(Object,  * Callable)}, or {@link LoadingCache#getAll(Iterable)}.  *  * @author Charles Fry  * @since 10.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|CacheStats
specifier|public
specifier|final
class|class
name|CacheStats
block|{
DECL|field|hitCount
specifier|private
specifier|final
name|long
name|hitCount
decl_stmt|;
DECL|field|missCount
specifier|private
specifier|final
name|long
name|missCount
decl_stmt|;
DECL|field|loadSuccessCount
specifier|private
specifier|final
name|long
name|loadSuccessCount
decl_stmt|;
DECL|field|loadExceptionCount
specifier|private
specifier|final
name|long
name|loadExceptionCount
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"GoodTime"
argument_list|)
comment|// should be a java.time.Duration
DECL|field|totalLoadTime
specifier|private
specifier|final
name|long
name|totalLoadTime
decl_stmt|;
DECL|field|evictionCount
specifier|private
specifier|final
name|long
name|evictionCount
decl_stmt|;
comment|/**    * Constructs a new {@code CacheStats} instance.    *    *<p>Five parameters of the same type in a row is a bad thing, but this class is not constructed    * by end users and is too fine-grained for a builder.    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"GoodTime"
argument_list|)
comment|// should accept a java.time.Duration
DECL|method|CacheStats ( long hitCount, long missCount, long loadSuccessCount, long loadExceptionCount, long totalLoadTime, long evictionCount)
specifier|public
name|CacheStats
parameter_list|(
name|long
name|hitCount
parameter_list|,
name|long
name|missCount
parameter_list|,
name|long
name|loadSuccessCount
parameter_list|,
name|long
name|loadExceptionCount
parameter_list|,
name|long
name|totalLoadTime
parameter_list|,
name|long
name|evictionCount
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|hitCount
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|missCount
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|loadSuccessCount
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|loadExceptionCount
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|totalLoadTime
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|evictionCount
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|this
operator|.
name|hitCount
operator|=
name|hitCount
expr_stmt|;
name|this
operator|.
name|missCount
operator|=
name|missCount
expr_stmt|;
name|this
operator|.
name|loadSuccessCount
operator|=
name|loadSuccessCount
expr_stmt|;
name|this
operator|.
name|loadExceptionCount
operator|=
name|loadExceptionCount
expr_stmt|;
name|this
operator|.
name|totalLoadTime
operator|=
name|totalLoadTime
expr_stmt|;
name|this
operator|.
name|evictionCount
operator|=
name|evictionCount
expr_stmt|;
block|}
comment|/**    * Returns the number of times {@link Cache} lookup methods have returned either a cached or    * uncached value. This is defined as {@code hitCount + missCount}.    */
DECL|method|requestCount ()
specifier|public
name|long
name|requestCount
parameter_list|()
block|{
return|return
name|hitCount
operator|+
name|missCount
return|;
block|}
comment|/** Returns the number of times {@link Cache} lookup methods have returned a cached value. */
DECL|method|hitCount ()
specifier|public
name|long
name|hitCount
parameter_list|()
block|{
return|return
name|hitCount
return|;
block|}
comment|/**    * Returns the ratio of cache requests which were hits. This is defined as {@code hitCount /    * requestCount}, or {@code 1.0} when {@code requestCount == 0}. Note that {@code hitRate +    * missRate =~ 1.0}.    */
DECL|method|hitRate ()
specifier|public
name|double
name|hitRate
parameter_list|()
block|{
name|long
name|requestCount
init|=
name|requestCount
argument_list|()
decl_stmt|;
return|return
operator|(
name|requestCount
operator|==
literal|0
operator|)
condition|?
literal|1.0
else|:
operator|(
name|double
operator|)
name|hitCount
operator|/
name|requestCount
return|;
block|}
comment|/**    * Returns the number of times {@link Cache} lookup methods have returned an uncached (newly    * loaded) value, or null. Multiple concurrent calls to {@link Cache} lookup methods on an absent    * value can result in multiple misses, all returning the results of a single cache load    * operation.    */
DECL|method|missCount ()
specifier|public
name|long
name|missCount
parameter_list|()
block|{
return|return
name|missCount
return|;
block|}
comment|/**    * Returns the ratio of cache requests which were misses. This is defined as {@code missCount /    * requestCount}, or {@code 0.0} when {@code requestCount == 0}. Note that {@code hitRate +    * missRate =~ 1.0}. Cache misses include all requests which weren't cache hits, including    * requests which resulted in either successful or failed loading attempts, and requests which    * waited for other threads to finish loading. It is thus the case that {@code missCount&gt;=    * loadSuccessCount + loadExceptionCount}. Multiple concurrent misses for the same key will result    * in a single load operation.    */
DECL|method|missRate ()
specifier|public
name|double
name|missRate
parameter_list|()
block|{
name|long
name|requestCount
init|=
name|requestCount
argument_list|()
decl_stmt|;
return|return
operator|(
name|requestCount
operator|==
literal|0
operator|)
condition|?
literal|0.0
else|:
operator|(
name|double
operator|)
name|missCount
operator|/
name|requestCount
return|;
block|}
comment|/**    * Returns the total number of times that {@link Cache} lookup methods attempted to load new    * values. This includes both successful load operations, as well as those that threw exceptions.    * This is defined as {@code loadSuccessCount + loadExceptionCount}.    */
DECL|method|loadCount ()
specifier|public
name|long
name|loadCount
parameter_list|()
block|{
return|return
name|loadSuccessCount
operator|+
name|loadExceptionCount
return|;
block|}
comment|/**    * Returns the number of times {@link Cache} lookup methods have successfully loaded a new value.    * This is usually incremented in conjunction with {@link #missCount}, though {@code missCount} is    * also incremented when an exception is encountered during cache loading (see {@link    * #loadExceptionCount}). Multiple concurrent misses for the same key will result in a single load    * operation. This may be incremented not in conjunction with {@code missCount} if the load occurs    * as a result of a refresh or if the cache loader returned more items than was requested. {@code    * missCount} may also be incremented not in conjunction with this (nor {@link    * #loadExceptionCount}) on calls to {@code getIfPresent}.    */
DECL|method|loadSuccessCount ()
specifier|public
name|long
name|loadSuccessCount
parameter_list|()
block|{
return|return
name|loadSuccessCount
return|;
block|}
comment|/**    * Returns the number of times {@link Cache} lookup methods threw an exception while loading a new    * value. This is usually incremented in conjunction with {@code missCount}, though {@code    * missCount} is also incremented when cache loading completes successfully (see {@link    * #loadSuccessCount}). Multiple concurrent misses for the same key will result in a single load    * operation. This may be incremented not in conjunction with {@code missCount} if the load occurs    * as a result of a refresh or if the cache loader returned more items than was requested. {@code    * missCount} may also be incremented not in conjunction with this (nor {@link #loadSuccessCount})    * on calls to {@code getIfPresent}.    */
DECL|method|loadExceptionCount ()
specifier|public
name|long
name|loadExceptionCount
parameter_list|()
block|{
return|return
name|loadExceptionCount
return|;
block|}
comment|/**    * Returns the ratio of cache loading attempts which threw exceptions. This is defined as {@code    * loadExceptionCount / (loadSuccessCount + loadExceptionCount)}, or {@code 0.0} when {@code    * loadSuccessCount + loadExceptionCount == 0}.    */
DECL|method|loadExceptionRate ()
specifier|public
name|double
name|loadExceptionRate
parameter_list|()
block|{
name|long
name|totalLoadCount
init|=
name|loadSuccessCount
operator|+
name|loadExceptionCount
decl_stmt|;
return|return
operator|(
name|totalLoadCount
operator|==
literal|0
operator|)
condition|?
literal|0.0
else|:
operator|(
name|double
operator|)
name|loadExceptionCount
operator|/
name|totalLoadCount
return|;
block|}
comment|/**    * Returns the total number of nanoseconds the cache has spent loading new values. This can be    * used to calculate the miss penalty. This value is increased every time {@code loadSuccessCount}    * or {@code loadExceptionCount} is incremented.    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"GoodTime"
argument_list|)
comment|// should return a java.time.Duration
DECL|method|totalLoadTime ()
specifier|public
name|long
name|totalLoadTime
parameter_list|()
block|{
return|return
name|totalLoadTime
return|;
block|}
comment|/**    * Returns the average time spent loading new values. This is defined as {@code totalLoadTime /    * (loadSuccessCount + loadExceptionCount)}.    */
DECL|method|averageLoadPenalty ()
specifier|public
name|double
name|averageLoadPenalty
parameter_list|()
block|{
name|long
name|totalLoadCount
init|=
name|loadSuccessCount
operator|+
name|loadExceptionCount
decl_stmt|;
return|return
operator|(
name|totalLoadCount
operator|==
literal|0
operator|)
condition|?
literal|0.0
else|:
operator|(
name|double
operator|)
name|totalLoadTime
operator|/
name|totalLoadCount
return|;
block|}
comment|/**    * Returns the number of times an entry has been evicted. This count does not include manual    * {@linkplain Cache#invalidate invalidations}.    */
DECL|method|evictionCount ()
specifier|public
name|long
name|evictionCount
parameter_list|()
block|{
return|return
name|evictionCount
return|;
block|}
comment|/**    * Returns a new {@code CacheStats} representing the difference between this {@code CacheStats}    * and {@code other}. Negative values, which aren't supported by {@code CacheStats} will be    * rounded up to zero.    */
DECL|method|minus (CacheStats other)
specifier|public
name|CacheStats
name|minus
parameter_list|(
name|CacheStats
name|other
parameter_list|)
block|{
return|return
operator|new
name|CacheStats
argument_list|(
name|Math
operator|.
name|max
argument_list|(
literal|0
argument_list|,
name|hitCount
operator|-
name|other
operator|.
name|hitCount
argument_list|)
argument_list|,
name|Math
operator|.
name|max
argument_list|(
literal|0
argument_list|,
name|missCount
operator|-
name|other
operator|.
name|missCount
argument_list|)
argument_list|,
name|Math
operator|.
name|max
argument_list|(
literal|0
argument_list|,
name|loadSuccessCount
operator|-
name|other
operator|.
name|loadSuccessCount
argument_list|)
argument_list|,
name|Math
operator|.
name|max
argument_list|(
literal|0
argument_list|,
name|loadExceptionCount
operator|-
name|other
operator|.
name|loadExceptionCount
argument_list|)
argument_list|,
name|Math
operator|.
name|max
argument_list|(
literal|0
argument_list|,
name|totalLoadTime
operator|-
name|other
operator|.
name|totalLoadTime
argument_list|)
argument_list|,
name|Math
operator|.
name|max
argument_list|(
literal|0
argument_list|,
name|evictionCount
operator|-
name|other
operator|.
name|evictionCount
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns a new {@code CacheStats} representing the sum of this {@code CacheStats} and {@code    * other}.    *    * @since 11.0    */
DECL|method|plus (CacheStats other)
specifier|public
name|CacheStats
name|plus
parameter_list|(
name|CacheStats
name|other
parameter_list|)
block|{
return|return
operator|new
name|CacheStats
argument_list|(
name|hitCount
operator|+
name|other
operator|.
name|hitCount
argument_list|,
name|missCount
operator|+
name|other
operator|.
name|missCount
argument_list|,
name|loadSuccessCount
operator|+
name|other
operator|.
name|loadSuccessCount
argument_list|,
name|loadExceptionCount
operator|+
name|other
operator|.
name|loadExceptionCount
argument_list|,
name|totalLoadTime
operator|+
name|other
operator|.
name|totalLoadTime
argument_list|,
name|evictionCount
operator|+
name|other
operator|.
name|evictionCount
argument_list|)
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
name|hitCount
argument_list|,
name|missCount
argument_list|,
name|loadSuccessCount
argument_list|,
name|loadExceptionCount
argument_list|,
name|totalLoadTime
argument_list|,
name|evictionCount
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@ullableDecl Object object)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|NullableDecl
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|CacheStats
condition|)
block|{
name|CacheStats
name|other
init|=
operator|(
name|CacheStats
operator|)
name|object
decl_stmt|;
return|return
name|hitCount
operator|==
name|other
operator|.
name|hitCount
operator|&&
name|missCount
operator|==
name|other
operator|.
name|missCount
operator|&&
name|loadSuccessCount
operator|==
name|other
operator|.
name|loadSuccessCount
operator|&&
name|loadExceptionCount
operator|==
name|other
operator|.
name|loadExceptionCount
operator|&&
name|totalLoadTime
operator|==
name|other
operator|.
name|totalLoadTime
operator|&&
name|evictionCount
operator|==
name|other
operator|.
name|evictionCount
return|;
block|}
return|return
literal|false
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
literal|"hitCount"
argument_list|,
name|hitCount
argument_list|)
operator|.
name|add
argument_list|(
literal|"missCount"
argument_list|,
name|missCount
argument_list|)
operator|.
name|add
argument_list|(
literal|"loadSuccessCount"
argument_list|,
name|loadSuccessCount
argument_list|)
operator|.
name|add
argument_list|(
literal|"loadExceptionCount"
argument_list|,
name|loadExceptionCount
argument_list|)
operator|.
name|add
argument_list|(
literal|"totalLoadTime"
argument_list|,
name|totalLoadTime
argument_list|)
operator|.
name|add
argument_list|(
literal|"evictionCount"
argument_list|,
name|evictionCount
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

