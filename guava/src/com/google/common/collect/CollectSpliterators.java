begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2015 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|GwtCompatible
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
name|java
operator|.
name|util
operator|.
name|Spliterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Consumer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|IntConsumer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|IntFunction
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Predicate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|IntStream
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
comment|/** Spliterator utilities for {@code common.collect} internals. */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|CollectSpliterators
specifier|final
class|class
name|CollectSpliterators
block|{
DECL|method|CollectSpliterators ()
specifier|private
name|CollectSpliterators
parameter_list|()
block|{}
DECL|method|indexed (int size, int extraCharacteristics, IntFunction<T> function)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Spliterator
argument_list|<
name|T
argument_list|>
name|indexed
parameter_list|(
name|int
name|size
parameter_list|,
name|int
name|extraCharacteristics
parameter_list|,
name|IntFunction
argument_list|<
name|T
argument_list|>
name|function
parameter_list|)
block|{
return|return
name|indexed
argument_list|(
name|size
argument_list|,
name|extraCharacteristics
argument_list|,
name|function
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|indexed ( int size, int extraCharacteristics, IntFunction<T> function, Comparator<? super T> comparator)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Spliterator
argument_list|<
name|T
argument_list|>
name|indexed
parameter_list|(
name|int
name|size
parameter_list|,
name|int
name|extraCharacteristics
parameter_list|,
name|IntFunction
argument_list|<
name|T
argument_list|>
name|function
parameter_list|,
name|Comparator
argument_list|<
name|?
super|super
name|T
argument_list|>
name|comparator
parameter_list|)
block|{
if|if
condition|(
name|comparator
operator|!=
literal|null
condition|)
block|{
name|checkArgument
argument_list|(
operator|(
name|extraCharacteristics
operator|&
operator|(
name|Spliterator
operator|.
name|SORTED
operator|)
operator|)
operator|!=
literal|0
argument_list|)
expr_stmt|;
block|}
class|class
name|WithCharacteristics
implements|implements
name|Spliterator
argument_list|<
name|T
argument_list|>
block|{
specifier|private
specifier|final
name|Spliterator
operator|.
name|OfInt
name|delegate
decl_stmt|;
name|WithCharacteristics
parameter_list|(
name|Spliterator
operator|.
name|OfInt
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|tryAdvance
parameter_list|(
name|Consumer
argument_list|<
name|?
super|super
name|T
argument_list|>
name|action
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|tryAdvance
argument_list|(
operator|(
name|IntConsumer
operator|)
name|i
lambda|->
name|action
operator|.
name|accept
argument_list|(
name|function
operator|.
name|apply
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|forEachRemaining
parameter_list|(
name|Consumer
argument_list|<
name|?
super|super
name|T
argument_list|>
name|action
parameter_list|)
block|{
name|delegate
operator|.
name|forEachRemaining
argument_list|(
operator|(
name|IntConsumer
operator|)
name|i
lambda|->
name|action
operator|.
name|accept
argument_list|(
name|function
operator|.
name|apply
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Nullable
specifier|public
name|Spliterator
argument_list|<
name|T
argument_list|>
name|trySplit
parameter_list|()
block|{
name|Spliterator
operator|.
name|OfInt
name|split
init|=
name|delegate
operator|.
name|trySplit
argument_list|()
decl_stmt|;
return|return
operator|(
name|split
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
operator|new
name|WithCharacteristics
argument_list|(
name|split
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|long
name|estimateSize
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|estimateSize
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|characteristics
parameter_list|()
block|{
return|return
name|Spliterator
operator|.
name|ORDERED
operator||
name|Spliterator
operator|.
name|SIZED
operator||
name|Spliterator
operator|.
name|SUBSIZED
operator||
name|extraCharacteristics
return|;
block|}
annotation|@
name|Override
specifier|public
name|Comparator
argument_list|<
name|?
super|super
name|T
argument_list|>
name|getComparator
parameter_list|()
block|{
if|if
condition|(
name|hasCharacteristics
argument_list|(
name|Spliterator
operator|.
name|SORTED
argument_list|)
condition|)
block|{
return|return
name|comparator
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|()
throw|;
block|}
block|}
block|}
return|return
operator|new
name|WithCharacteristics
argument_list|(
name|IntStream
operator|.
name|range
argument_list|(
literal|0
argument_list|,
name|size
argument_list|)
operator|.
name|spliterator
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns a {@code Spliterator} over the elements of {@code fromSpliterator} mapped by {@code    * function}.    */
DECL|method|map ( Spliterator<F> fromSpliterator, Function<? super F, ? extends T> function)
specifier|static
parameter_list|<
name|F
parameter_list|,
name|T
parameter_list|>
name|Spliterator
argument_list|<
name|T
argument_list|>
name|map
parameter_list|(
name|Spliterator
argument_list|<
name|F
argument_list|>
name|fromSpliterator
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|F
argument_list|,
name|?
extends|extends
name|T
argument_list|>
name|function
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|fromSpliterator
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|function
argument_list|)
expr_stmt|;
return|return
operator|new
name|Spliterator
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|tryAdvance
parameter_list|(
name|Consumer
argument_list|<
name|?
super|super
name|T
argument_list|>
name|action
parameter_list|)
block|{
return|return
name|fromSpliterator
operator|.
name|tryAdvance
argument_list|(
name|fromElement
lambda|->
name|action
operator|.
name|accept
argument_list|(
name|function
operator|.
name|apply
argument_list|(
name|fromElement
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|forEachRemaining
parameter_list|(
name|Consumer
argument_list|<
name|?
super|super
name|T
argument_list|>
name|action
parameter_list|)
block|{
name|fromSpliterator
operator|.
name|forEachRemaining
argument_list|(
name|fromElement
lambda|->
name|action
operator|.
name|accept
argument_list|(
name|function
operator|.
name|apply
argument_list|(
name|fromElement
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Spliterator
argument_list|<
name|T
argument_list|>
name|trySplit
parameter_list|()
block|{
name|Spliterator
argument_list|<
name|F
argument_list|>
name|fromSplit
init|=
name|fromSpliterator
operator|.
name|trySplit
argument_list|()
decl_stmt|;
return|return
operator|(
name|fromSplit
operator|!=
literal|null
operator|)
condition|?
name|map
argument_list|(
name|fromSplit
argument_list|,
name|function
argument_list|)
else|:
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|long
name|estimateSize
parameter_list|()
block|{
return|return
name|fromSpliterator
operator|.
name|estimateSize
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|characteristics
parameter_list|()
block|{
return|return
name|fromSpliterator
operator|.
name|characteristics
argument_list|()
operator|&
operator|~
operator|(
name|Spliterator
operator|.
name|DISTINCT
operator||
name|Spliterator
operator|.
name|NONNULL
operator||
name|Spliterator
operator|.
name|SORTED
operator|)
return|;
block|}
block|}
return|;
block|}
comment|/** Returns a {@code Spliterator} filtered by the specified predicate. */
DECL|method|filter (Spliterator<T> fromSpliterator, Predicate<? super T> predicate)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Spliterator
argument_list|<
name|T
argument_list|>
name|filter
parameter_list|(
name|Spliterator
argument_list|<
name|T
argument_list|>
name|fromSpliterator
parameter_list|,
name|Predicate
argument_list|<
name|?
super|super
name|T
argument_list|>
name|predicate
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|fromSpliterator
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|predicate
argument_list|)
expr_stmt|;
class|class
name|Splitr
implements|implements
name|Spliterator
argument_list|<
name|T
argument_list|>
implements|,
name|Consumer
argument_list|<
name|T
argument_list|>
block|{
name|T
name|holder
init|=
literal|null
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|accept
parameter_list|(
name|T
name|t
parameter_list|)
block|{
name|this
operator|.
name|holder
operator|=
name|t
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|tryAdvance
parameter_list|(
name|Consumer
argument_list|<
name|?
super|super
name|T
argument_list|>
name|action
parameter_list|)
block|{
while|while
condition|(
name|fromSpliterator
operator|.
name|tryAdvance
argument_list|(
name|this
argument_list|)
condition|)
block|{
try|try
block|{
if|if
condition|(
name|predicate
operator|.
name|test
argument_list|(
name|holder
argument_list|)
condition|)
block|{
name|action
operator|.
name|accept
argument_list|(
name|holder
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
finally|finally
block|{
name|holder
operator|=
literal|null
expr_stmt|;
block|}
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|Spliterator
argument_list|<
name|T
argument_list|>
name|trySplit
parameter_list|()
block|{
name|Spliterator
argument_list|<
name|T
argument_list|>
name|fromSplit
init|=
name|fromSpliterator
operator|.
name|trySplit
argument_list|()
decl_stmt|;
return|return
operator|(
name|fromSplit
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|filter
argument_list|(
name|fromSplit
argument_list|,
name|predicate
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|long
name|estimateSize
parameter_list|()
block|{
return|return
name|fromSpliterator
operator|.
name|estimateSize
argument_list|()
operator|/
literal|2
return|;
block|}
annotation|@
name|Override
specifier|public
name|Comparator
argument_list|<
name|?
super|super
name|T
argument_list|>
name|getComparator
parameter_list|()
block|{
return|return
name|fromSpliterator
operator|.
name|getComparator
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|characteristics
parameter_list|()
block|{
return|return
name|fromSpliterator
operator|.
name|characteristics
argument_list|()
operator|&
operator|(
name|Spliterator
operator|.
name|DISTINCT
operator||
name|Spliterator
operator|.
name|NONNULL
operator||
name|Spliterator
operator|.
name|ORDERED
operator||
name|Spliterator
operator|.
name|SORTED
operator|)
return|;
block|}
block|}
return|return
operator|new
name|Splitr
argument_list|()
return|;
block|}
comment|/**    * Returns a {@code Spliterator} that iterates over the elements of the spliterators generated by    * applying {@code function} to the elements of {@code fromSpliterator}.    */
DECL|method|flatMap ( Spliterator<F> fromSpliterator, Function<? super F, Spliterator<T>> function, int topCharacteristics, long topSize)
specifier|static
parameter_list|<
name|F
parameter_list|,
name|T
parameter_list|>
name|Spliterator
argument_list|<
name|T
argument_list|>
name|flatMap
parameter_list|(
name|Spliterator
argument_list|<
name|F
argument_list|>
name|fromSpliterator
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|F
argument_list|,
name|Spliterator
argument_list|<
name|T
argument_list|>
argument_list|>
name|function
parameter_list|,
name|int
name|topCharacteristics
parameter_list|,
name|long
name|topSize
parameter_list|)
block|{
name|checkArgument
argument_list|(
operator|(
name|topCharacteristics
operator|&
name|Spliterator
operator|.
name|SUBSIZED
operator|)
operator|==
literal|0
argument_list|,
literal|"flatMap does not support SUBSIZED characteristic"
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
operator|(
name|topCharacteristics
operator|&
name|Spliterator
operator|.
name|SORTED
operator|)
operator|==
literal|0
argument_list|,
literal|"flatMap does not support SORTED characteristic"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|fromSpliterator
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|function
argument_list|)
expr_stmt|;
class|class
name|FlatMapSpliterator
implements|implements
name|Spliterator
argument_list|<
name|T
argument_list|>
block|{
annotation|@
name|Nullable
name|Spliterator
argument_list|<
name|T
argument_list|>
name|prefix
decl_stmt|;
specifier|final
name|Spliterator
argument_list|<
name|F
argument_list|>
name|from
decl_stmt|;
name|int
name|characteristics
decl_stmt|;
name|long
name|estimatedSize
decl_stmt|;
name|FlatMapSpliterator
parameter_list|(
name|Spliterator
argument_list|<
name|T
argument_list|>
name|prefix
parameter_list|,
name|Spliterator
argument_list|<
name|F
argument_list|>
name|from
parameter_list|,
name|int
name|characteristics
parameter_list|,
name|long
name|estimatedSize
parameter_list|)
block|{
name|this
operator|.
name|prefix
operator|=
name|prefix
expr_stmt|;
name|this
operator|.
name|from
operator|=
name|from
expr_stmt|;
name|this
operator|.
name|characteristics
operator|=
name|characteristics
expr_stmt|;
name|this
operator|.
name|estimatedSize
operator|=
name|estimatedSize
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|tryAdvance
parameter_list|(
name|Consumer
argument_list|<
name|?
super|super
name|T
argument_list|>
name|action
parameter_list|)
block|{
while|while
condition|(
literal|true
condition|)
block|{
if|if
condition|(
name|prefix
operator|!=
literal|null
operator|&&
name|prefix
operator|.
name|tryAdvance
argument_list|(
name|action
argument_list|)
condition|)
block|{
if|if
condition|(
name|estimatedSize
operator|!=
name|Long
operator|.
name|MAX_VALUE
condition|)
block|{
name|estimatedSize
operator|--
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
else|else
block|{
name|prefix
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|from
operator|.
name|tryAdvance
argument_list|(
name|fromElement
lambda|->
name|prefix
operator|=
name|function
operator|.
name|apply
argument_list|(
name|fromElement
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|forEachRemaining
parameter_list|(
name|Consumer
argument_list|<
name|?
super|super
name|T
argument_list|>
name|action
parameter_list|)
block|{
if|if
condition|(
name|prefix
operator|!=
literal|null
condition|)
block|{
name|prefix
operator|.
name|forEachRemaining
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|prefix
operator|=
literal|null
expr_stmt|;
block|}
name|from
operator|.
name|forEachRemaining
argument_list|(
name|fromElement
lambda|->
name|function
operator|.
name|apply
argument_list|(
name|fromElement
argument_list|)
operator|.
name|forEachRemaining
argument_list|(
name|action
argument_list|)
argument_list|)
expr_stmt|;
name|estimatedSize
operator|=
literal|0
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Spliterator
argument_list|<
name|T
argument_list|>
name|trySplit
parameter_list|()
block|{
name|Spliterator
argument_list|<
name|F
argument_list|>
name|fromSplit
init|=
name|from
operator|.
name|trySplit
argument_list|()
decl_stmt|;
if|if
condition|(
name|fromSplit
operator|!=
literal|null
condition|)
block|{
name|int
name|splitCharacteristics
init|=
name|characteristics
operator|&
operator|~
name|Spliterator
operator|.
name|SIZED
decl_stmt|;
name|long
name|estSplitSize
init|=
name|estimateSize
argument_list|()
decl_stmt|;
if|if
condition|(
name|estSplitSize
operator|<
name|Long
operator|.
name|MAX_VALUE
condition|)
block|{
name|estSplitSize
operator|/=
literal|2
expr_stmt|;
name|this
operator|.
name|estimatedSize
operator|-=
name|estSplitSize
expr_stmt|;
name|this
operator|.
name|characteristics
operator|=
name|splitCharacteristics
expr_stmt|;
block|}
name|Spliterator
argument_list|<
name|T
argument_list|>
name|result
init|=
operator|new
name|FlatMapSpliterator
argument_list|(
name|this
operator|.
name|prefix
argument_list|,
name|fromSplit
argument_list|,
name|splitCharacteristics
argument_list|,
name|estSplitSize
argument_list|)
decl_stmt|;
name|this
operator|.
name|prefix
operator|=
literal|null
expr_stmt|;
return|return
name|result
return|;
block|}
elseif|else
if|if
condition|(
name|prefix
operator|!=
literal|null
condition|)
block|{
name|Spliterator
argument_list|<
name|T
argument_list|>
name|result
init|=
name|prefix
decl_stmt|;
name|this
operator|.
name|prefix
operator|=
literal|null
expr_stmt|;
return|return
name|result
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|long
name|estimateSize
parameter_list|()
block|{
if|if
condition|(
name|prefix
operator|!=
literal|null
condition|)
block|{
name|estimatedSize
operator|=
name|Math
operator|.
name|max
argument_list|(
name|estimatedSize
argument_list|,
name|prefix
operator|.
name|estimateSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|Math
operator|.
name|max
argument_list|(
name|estimatedSize
argument_list|,
literal|0
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|characteristics
parameter_list|()
block|{
return|return
name|characteristics
return|;
block|}
block|}
return|return
operator|new
name|FlatMapSpliterator
argument_list|(
literal|null
argument_list|,
name|fromSpliterator
argument_list|,
name|topCharacteristics
argument_list|,
name|topSize
argument_list|)
return|;
block|}
block|}
end_class

end_unit

