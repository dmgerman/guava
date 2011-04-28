begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Equivalence
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
name|Function
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
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
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentMap
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
name|atomic
operator|.
name|AtomicReferenceArray
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

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|concurrent
operator|.
name|GuardedBy
import|;
end_import

begin_comment
comment|/**  * Adds computing functionality to {@link CustomConcurrentHashMap}.  *  * @author Bob Lee  * @author Charles Fry  */
end_comment

begin_class
DECL|class|ComputingConcurrentHashMap
class|class
name|ComputingConcurrentHashMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|CustomConcurrentHashMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|computingFunction
specifier|final
name|Function
argument_list|<
name|?
super|super
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|computingFunction
decl_stmt|;
comment|/**    * Creates a new, empty map with the specified strategy, initial capacity, load factor and    * concurrency level.    */
DECL|method|ComputingConcurrentHashMap (MapMaker builder, Function<? super K, ? extends V> computingFunction)
name|ComputingConcurrentHashMap
parameter_list|(
name|MapMaker
name|builder
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|computingFunction
parameter_list|)
block|{
name|super
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|this
operator|.
name|computingFunction
operator|=
name|checkNotNull
argument_list|(
name|computingFunction
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createSegment (int initialCapacity, int maxSegmentSize)
name|Segment
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|createSegment
parameter_list|(
name|int
name|initialCapacity
parameter_list|,
name|int
name|maxSegmentSize
parameter_list|)
block|{
return|return
operator|new
name|ComputingSegment
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|this
argument_list|,
name|initialCapacity
argument_list|,
name|maxSegmentSize
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|segmentFor (int hash)
name|ComputingSegment
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|segmentFor
parameter_list|(
name|int
name|hash
parameter_list|)
block|{
return|return
operator|(
name|ComputingSegment
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|super
operator|.
name|segmentFor
argument_list|(
name|hash
argument_list|)
return|;
block|}
DECL|method|compute (K key)
name|V
name|compute
parameter_list|(
name|K
name|key
parameter_list|)
block|{
name|int
name|hash
init|=
name|hash
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
name|segmentFor
argument_list|(
name|hash
argument_list|)
operator|.
name|compute
argument_list|(
name|key
argument_list|,
name|hash
argument_list|,
name|computingFunction
argument_list|)
return|;
block|}
comment|/**    * Overrides get() to compute on demand. Also throws an exception when null is returned from a    * computation.    */
DECL|class|ComputingMapAdapter
specifier|static
class|class
name|ComputingMapAdapter
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ComputingConcurrentHashMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
DECL|method|ComputingMapAdapter (MapMaker mapMaker, Function<? super K, ? extends V> computingFunction)
name|ComputingMapAdapter
parameter_list|(
name|MapMaker
name|mapMaker
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|computingFunction
parameter_list|)
block|{
name|super
argument_list|(
name|mapMaker
argument_list|,
name|computingFunction
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// unsafe, which is why this is deprecated
annotation|@
name|Override
DECL|method|get (Object key)
specifier|public
name|V
name|get
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|V
name|value
init|=
name|compute
argument_list|(
operator|(
name|K
operator|)
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
name|computingFunction
operator|+
literal|" returned null for key "
operator|+
name|key
operator|+
literal|"."
argument_list|)
throw|;
block|}
return|return
name|value
return|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
comment|// This class is never serialized.
DECL|class|ComputingSegment
specifier|static
class|class
name|ComputingSegment
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|Segment
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|method|ComputingSegment (CustomConcurrentHashMap<K, V> map, int initialCapacity, int maxSegmentSize)
name|ComputingSegment
parameter_list|(
name|CustomConcurrentHashMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|,
name|int
name|initialCapacity
parameter_list|,
name|int
name|maxSegmentSize
parameter_list|)
block|{
name|super
argument_list|(
name|map
argument_list|,
name|initialCapacity
argument_list|,
name|maxSegmentSize
argument_list|)
expr_stmt|;
block|}
DECL|method|compute (K key, int hash, Function<? super K, ? extends V> computingFunction)
name|V
name|compute
parameter_list|(
name|K
name|key
parameter_list|,
name|int
name|hash
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|computingFunction
parameter_list|)
block|{
try|try
block|{
name|outer
label|:
while|while
condition|(
literal|true
condition|)
block|{
name|ReferenceEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|e
init|=
name|getEntry
argument_list|(
name|key
argument_list|,
name|hash
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
name|V
name|value
init|=
name|getLiveValue
argument_list|(
name|e
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
comment|// TODO(user): recordHit
name|recordRead
argument_list|(
name|e
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
block|}
comment|// at this point e is either null, computing, or expired;
comment|// avoid locking if it's already computing
if|if
condition|(
name|e
operator|==
literal|null
operator|||
operator|!
name|e
operator|.
name|getValueReference
argument_list|()
operator|.
name|isComputingReference
argument_list|()
condition|)
block|{
name|ComputingValueReference
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|computingValueReference
init|=
literal|null
decl_stmt|;
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|preWriteCleanup
argument_list|()
expr_stmt|;
comment|// getFirst, but remember the index
name|AtomicReferenceArray
argument_list|<
name|ReferenceEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|table
init|=
name|this
operator|.
name|table
decl_stmt|;
name|int
name|index
init|=
name|hash
operator|&
operator|(
name|table
operator|.
name|length
argument_list|()
operator|-
literal|1
operator|)
decl_stmt|;
name|ReferenceEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|first
init|=
name|table
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
name|boolean
name|createNewEntry
init|=
literal|true
decl_stmt|;
for|for
control|(
name|e
operator|=
name|first
init|;
name|e
operator|!=
literal|null
condition|;
name|e
operator|=
name|e
operator|.
name|getNext
argument_list|()
control|)
block|{
name|K
name|entryKey
init|=
name|e
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|.
name|getHash
argument_list|()
operator|==
name|hash
operator|&&
name|entryKey
operator|!=
literal|null
operator|&&
name|map
operator|.
name|keyEquivalence
operator|.
name|equivalent
argument_list|(
name|key
argument_list|,
name|entryKey
argument_list|)
condition|)
block|{
name|ValueReference
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|valueReference
init|=
name|e
operator|.
name|getValueReference
argument_list|()
decl_stmt|;
if|if
condition|(
name|valueReference
operator|.
name|isComputingReference
argument_list|()
condition|)
block|{
name|createNewEntry
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
comment|// never return expired entries
name|V
name|value
init|=
name|getLiveValue
argument_list|(
name|e
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|recordLockedRead
argument_list|(
name|e
argument_list|)
expr_stmt|;
comment|// TODO(user): recordHit
return|return
name|value
return|;
block|}
comment|// immediately reuse invalid entries
name|clearLiveEntry
argument_list|(
name|e
argument_list|,
name|hash
argument_list|,
name|valueReference
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
block|}
if|if
condition|(
name|createNewEntry
condition|)
block|{
name|computingValueReference
operator|=
operator|new
name|ComputingValueReference
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|computingFunction
argument_list|)
expr_stmt|;
if|if
condition|(
name|e
operator|==
literal|null
condition|)
block|{
name|e
operator|=
name|map
operator|.
name|newEntry
argument_list|(
name|key
argument_list|,
name|hash
argument_list|,
name|first
argument_list|)
expr_stmt|;
name|table
operator|.
name|set
argument_list|(
name|index
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|e
operator|.
name|setValueReference
argument_list|(
name|computingValueReference
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|unlock
argument_list|()
expr_stmt|;
name|postWriteCleanup
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|computingValueReference
operator|!=
literal|null
condition|)
block|{
comment|// This thread solely created the entry.
name|V
name|value
init|=
literal|null
decl_stmt|;
try|try
block|{
comment|// Synchronizes on the entry to allow failing fast when a recursive computation is
comment|// detected. This is not fool-proof since the entry may be copied when the segment
comment|// is written to.
synchronized|synchronized
init|(
name|e
init|)
block|{
name|value
operator|=
name|computingValueReference
operator|.
name|compute
argument_list|(
name|key
argument_list|,
name|hash
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
comment|// putIfAbsent
name|put
argument_list|(
name|key
argument_list|,
name|hash
argument_list|,
name|value
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|clearValue
argument_list|(
name|key
argument_list|,
name|hash
argument_list|,
name|computingValueReference
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// The entry already exists. Wait for the computation.
name|checkState
argument_list|(
operator|!
name|Thread
operator|.
name|holdsLock
argument_list|(
name|e
argument_list|)
argument_list|,
literal|"Recursive computation"
argument_list|)
expr_stmt|;
name|V
name|value
init|=
name|e
operator|.
name|getValueReference
argument_list|()
operator|.
name|waitForValue
argument_list|()
decl_stmt|;
comment|// don't consider expiration as we're concurrent with computation
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|recordRead
argument_list|(
name|e
argument_list|)
expr_stmt|;
comment|// TODO(user): recordMiss
return|return
name|value
return|;
block|}
comment|// else computing thread will clearValue
continue|continue
name|outer
continue|;
block|}
block|}
finally|finally
block|{
name|postReadCleanup
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Used to provide computation exceptions to other threads.    */
DECL|class|ComputationExceptionReference
specifier|private
specifier|static
class|class
name|ComputationExceptionReference
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|ValueReference
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|t
specifier|final
name|Throwable
name|t
decl_stmt|;
DECL|method|ComputationExceptionReference (Throwable t)
name|ComputationExceptionReference
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|this
operator|.
name|t
operator|=
name|t
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|V
name|get
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|copyFor (ReferenceEntry<K, V> entry)
specifier|public
name|ValueReference
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|copyFor
parameter_list|(
name|ReferenceEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
parameter_list|)
block|{
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|isComputingReference ()
specifier|public
name|boolean
name|isComputingReference
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|waitForValue ()
specifier|public
name|V
name|waitForValue
parameter_list|()
block|{
throw|throw
operator|new
name|AsynchronousComputationException
argument_list|(
name|t
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|notifyValueReclaimed ()
specifier|public
name|void
name|notifyValueReclaimed
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|clear (ValueReference<K, V> newValue)
specifier|public
name|void
name|clear
parameter_list|(
name|ValueReference
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newValue
parameter_list|)
block|{}
block|}
comment|/**    * Used to provide computation result to other threads.    */
DECL|class|ComputedReference
specifier|private
specifier|static
class|class
name|ComputedReference
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|ValueReference
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|value
specifier|final
name|V
name|value
decl_stmt|;
DECL|method|ComputedReference (@ullable V value)
name|ComputedReference
parameter_list|(
annotation|@
name|Nullable
name|V
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|V
name|get
parameter_list|()
block|{
return|return
name|value
return|;
block|}
annotation|@
name|Override
DECL|method|copyFor (ReferenceEntry<K, V> entry)
specifier|public
name|ValueReference
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|copyFor
parameter_list|(
name|ReferenceEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
parameter_list|)
block|{
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|isComputingReference ()
specifier|public
name|boolean
name|isComputingReference
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|waitForValue ()
specifier|public
name|V
name|waitForValue
parameter_list|()
block|{
return|return
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|notifyValueReclaimed ()
specifier|public
name|void
name|notifyValueReclaimed
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|clear (ValueReference<K, V> newValue)
specifier|public
name|void
name|clear
parameter_list|(
name|ValueReference
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newValue
parameter_list|)
block|{}
block|}
DECL|class|ComputingValueReference
specifier|private
specifier|static
class|class
name|ComputingValueReference
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|ValueReference
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|computingFunction
specifier|final
name|Function
argument_list|<
name|?
super|super
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|computingFunction
decl_stmt|;
annotation|@
name|GuardedBy
argument_list|(
literal|"ComputingValueReference.this"
argument_list|)
comment|// writes
DECL|field|computedReference
specifier|volatile
name|ValueReference
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|computedReference
init|=
name|unset
argument_list|()
decl_stmt|;
DECL|method|ComputingValueReference (Function<? super K, ? extends V> computingFunction)
specifier|public
name|ComputingValueReference
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|computingFunction
parameter_list|)
block|{
name|this
operator|.
name|computingFunction
operator|=
name|computingFunction
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|V
name|get
parameter_list|()
block|{
comment|// All computation lookups go through waitForValue. This method thus is
comment|// only used by put, to whom we always want to appear absent.
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|copyFor (ReferenceEntry<K, V> entry)
specifier|public
name|ValueReference
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|copyFor
parameter_list|(
name|ReferenceEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
parameter_list|)
block|{
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|isComputingReference ()
specifier|public
name|boolean
name|isComputingReference
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Waits for a computation to complete. Returns the result of the computation.      */
annotation|@
name|Override
DECL|method|waitForValue ()
specifier|public
name|V
name|waitForValue
parameter_list|()
block|{
if|if
condition|(
name|computedReference
operator|==
name|UNSET
condition|)
block|{
name|boolean
name|interrupted
init|=
literal|false
decl_stmt|;
try|try
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
while|while
condition|(
name|computedReference
operator|==
name|UNSET
condition|)
block|{
try|try
block|{
name|wait
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ie
parameter_list|)
block|{
name|interrupted
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|interrupted
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
name|computedReference
operator|.
name|waitForValue
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|clear (ValueReference<K, V> newValue)
specifier|public
name|void
name|clear
parameter_list|(
name|ValueReference
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newValue
parameter_list|)
block|{
comment|// The pending computation was clobbered by a manual write. Unblock all
comment|// pending gets, and have them return the new value.
name|setValueReference
argument_list|(
name|newValue
argument_list|)
expr_stmt|;
comment|// TODO(user): could also cancel computation if we had a thread handle
block|}
annotation|@
name|Override
DECL|method|notifyValueReclaimed ()
specifier|public
name|void
name|notifyValueReclaimed
parameter_list|()
block|{}
DECL|method|compute (K key, int hash)
name|V
name|compute
parameter_list|(
name|K
name|key
parameter_list|,
name|int
name|hash
parameter_list|)
block|{
name|V
name|value
decl_stmt|;
try|try
block|{
name|value
operator|=
name|computingFunction
operator|.
name|apply
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ComputationException
name|e
parameter_list|)
block|{
comment|// if computingFunction has thrown a computation exception,
comment|// propagate rather than wrap
name|setValueReference
argument_list|(
operator|new
name|ComputationExceptionReference
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|setValueReference
argument_list|(
operator|new
name|ComputationExceptionReference
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|t
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ComputationException
argument_list|(
name|t
argument_list|)
throw|;
block|}
name|setValueReference
argument_list|(
operator|new
name|ComputedReference
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
DECL|method|setValueReference (ValueReference<K, V> valueReference)
name|void
name|setValueReference
parameter_list|(
name|ValueReference
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|valueReference
parameter_list|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|computedReference
operator|==
name|UNSET
condition|)
block|{
name|computedReference
operator|=
name|valueReference
expr_stmt|;
name|notifyAll
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// Serialization Support
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|2
decl_stmt|;
annotation|@
name|Override
DECL|method|writeReplace ()
name|Object
name|writeReplace
parameter_list|()
block|{
return|return
operator|new
name|ComputingSerializationProxy
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|keyStrength
argument_list|,
name|valueStrength
argument_list|,
name|keyEquivalence
argument_list|,
name|valueEquivalence
argument_list|,
name|expireAfterWriteNanos
argument_list|,
name|expireAfterAccessNanos
argument_list|,
name|maximumSize
argument_list|,
name|concurrencyLevel
argument_list|,
name|evictionListener
argument_list|,
name|this
argument_list|,
name|computingFunction
argument_list|)
return|;
block|}
DECL|class|ComputingSerializationProxy
specifier|static
class|class
name|ComputingSerializationProxy
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractSerializationProxy
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|computingFunction
specifier|final
name|Function
argument_list|<
name|?
super|super
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|computingFunction
decl_stmt|;
DECL|method|ComputingSerializationProxy (Strength keyStrength, Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, long expireAfterWriteNanos, long expireAfterAccessNanos, int maximumSize, int concurrencyLevel, MapEvictionListener<? super K, ? super V> evictionListener, ConcurrentMap<K, V> delegate, Function<? super K, ? extends V> computingFunction)
name|ComputingSerializationProxy
parameter_list|(
name|Strength
name|keyStrength
parameter_list|,
name|Strength
name|valueStrength
parameter_list|,
name|Equivalence
argument_list|<
name|Object
argument_list|>
name|keyEquivalence
parameter_list|,
name|Equivalence
argument_list|<
name|Object
argument_list|>
name|valueEquivalence
parameter_list|,
name|long
name|expireAfterWriteNanos
parameter_list|,
name|long
name|expireAfterAccessNanos
parameter_list|,
name|int
name|maximumSize
parameter_list|,
name|int
name|concurrencyLevel
parameter_list|,
name|MapEvictionListener
argument_list|<
name|?
super|super
name|K
argument_list|,
name|?
super|super
name|V
argument_list|>
name|evictionListener
parameter_list|,
name|ConcurrentMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|computingFunction
parameter_list|)
block|{
name|super
argument_list|(
name|keyStrength
argument_list|,
name|valueStrength
argument_list|,
name|keyEquivalence
argument_list|,
name|valueEquivalence
argument_list|,
name|expireAfterWriteNanos
argument_list|,
name|expireAfterAccessNanos
argument_list|,
name|maximumSize
argument_list|,
name|concurrencyLevel
argument_list|,
name|evictionListener
argument_list|,
name|delegate
argument_list|)
expr_stmt|;
name|this
operator|.
name|computingFunction
operator|=
name|computingFunction
expr_stmt|;
block|}
DECL|method|writeObject (ObjectOutputStream out)
specifier|private
name|void
name|writeObject
parameter_list|(
name|ObjectOutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|out
operator|.
name|defaultWriteObject
argument_list|()
expr_stmt|;
name|writeMapTo
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|readObject (ObjectInputStream in)
specifier|private
name|void
name|readObject
parameter_list|(
name|ObjectInputStream
name|in
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|in
operator|.
name|defaultReadObject
argument_list|()
expr_stmt|;
name|MapMaker
name|mapMaker
init|=
name|readMapMaker
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|delegate
operator|=
name|mapMaker
operator|.
name|makeComputingMap
argument_list|(
name|computingFunction
argument_list|)
expr_stmt|;
name|readEntries
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
DECL|method|readResolve ()
name|Object
name|readResolve
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|2
decl_stmt|;
block|}
block|}
end_class

end_unit

