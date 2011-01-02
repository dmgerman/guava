begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|MapMaker
operator|.
name|Cache
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

begin_comment
comment|/**  * Adds computing functionality to {@link CustomConcurrentHashMap}.  *  * @author Bob Lee  */
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
implements|implements
name|Cache
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
comment|/**    * Creates a new, empty map with the specified strategy, initial capacity,    * load factor and concurrency level.    */
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
DECL|method|asMap ()
specifier|public
name|ConcurrentMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|asMap
parameter_list|()
block|{
return|return
name|this
return|;
block|}
DECL|method|createSegment (int initialCapacity, int maxSegmentSize)
annotation|@
name|Override
name|Segment
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
argument_list|(
name|initialCapacity
argument_list|,
name|maxSegmentSize
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// explain
DECL|method|segmentFor (int hash)
annotation|@
name|Override
name|ComputingSegment
name|segmentFor
parameter_list|(
name|int
name|hash
parameter_list|)
block|{
return|return
operator|(
name|ComputingSegment
operator|)
name|super
operator|.
name|segmentFor
argument_list|(
name|hash
argument_list|)
return|;
block|}
DECL|method|apply (K key)
specifier|public
name|V
name|apply
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
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
comment|// This class is never serialized.
DECL|class|ComputingSegment
class|class
name|ComputingSegment
extends|extends
name|Segment
block|{
DECL|method|ComputingSegment (int initialCapacity, int maxSegmentSize)
name|ComputingSegment
parameter_list|(
name|int
name|initialCapacity
parameter_list|,
name|int
name|maxSegmentSize
parameter_list|)
block|{
name|super
argument_list|(
name|initialCapacity
argument_list|,
name|maxSegmentSize
argument_list|)
expr_stmt|;
block|}
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
name|entry
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
name|entry
operator|==
literal|null
condition|)
block|{
name|boolean
name|created
init|=
literal|false
decl_stmt|;
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
if|if
condition|(
name|expires
argument_list|()
condition|)
block|{
name|expireEntries
argument_list|()
expr_stmt|;
block|}
comment|// Try again--an entry could have materialized in the interim.
name|entry
operator|=
name|getEntry
argument_list|(
name|key
argument_list|,
name|hash
argument_list|)
expr_stmt|;
if|if
condition|(
name|entry
operator|==
literal|null
condition|)
block|{
comment|// Create a new entry.
name|created
operator|=
literal|true
expr_stmt|;
name|int
name|newCount
init|=
name|this
operator|.
name|count
operator|+
literal|1
decl_stmt|;
comment|// read-volatile
if|if
condition|(
name|evictsBySize
argument_list|()
operator|&&
name|newCount
operator|>
name|maxSegmentSize
condition|)
block|{
name|evictEntry
argument_list|()
expr_stmt|;
name|newCount
operator|=
name|this
operator|.
name|count
operator|+
literal|1
expr_stmt|;
comment|// read-volatile
block|}
elseif|else
if|if
condition|(
name|newCount
operator|>
name|threshold
condition|)
block|{
comment|// ensure capacity
name|expand
argument_list|()
expr_stmt|;
block|}
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
operator|++
name|modCount
expr_stmt|;
name|entry
operator|=
name|entryFactory
operator|.
name|newEntry
argument_list|(
name|ComputingConcurrentHashMap
operator|.
name|this
argument_list|,
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
name|entry
argument_list|)
expr_stmt|;
name|this
operator|.
name|count
operator|=
name|newCount
expr_stmt|;
comment|// write-volatile
block|}
block|}
finally|finally
block|{
name|unlock
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|created
condition|)
block|{
comment|// This thread solely created the entry.
name|boolean
name|success
init|=
literal|false
decl_stmt|;
try|try
block|{
name|V
name|value
init|=
name|compute
argument_list|(
name|key
argument_list|,
name|entry
argument_list|)
decl_stmt|;
name|checkNotNull
argument_list|(
name|value
argument_list|,
literal|"compute() returned null unexpectedly"
argument_list|)
expr_stmt|;
name|success
operator|=
literal|true
expr_stmt|;
return|return
name|value
return|;
block|}
finally|finally
block|{
if|if
condition|(
operator|!
name|success
condition|)
block|{
name|removeEntry
argument_list|(
name|entry
argument_list|,
name|hash
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// The entry already exists. Wait for the computation.
name|boolean
name|interrupted
init|=
literal|false
decl_stmt|;
try|try
block|{
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
name|V
name|value
init|=
name|waitForValue
argument_list|(
name|entry
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
comment|// Purge entry and try again.
name|removeEntry
argument_list|(
name|entry
argument_list|,
name|hash
argument_list|)
expr_stmt|;
continue|continue
name|outer
continue|;
block|}
return|return
name|value
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|interrupted
operator|=
literal|true
expr_stmt|;
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
block|}
DECL|method|compute (K key, ReferenceEntry<K, V> entry)
name|V
name|compute
parameter_list|(
name|K
name|key
parameter_list|,
name|ReferenceEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
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
comment|// TODO(user): If we remove the entry before setting the value reference,
comment|// if the caller retries, they'll get the result of a different
comment|// rather than the same result.
name|setValueReference
argument_list|(
name|entry
argument_list|,
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
name|entry
argument_list|,
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
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|String
name|message
init|=
name|computingFunction
operator|+
literal|" returned null for key "
operator|+
name|key
operator|+
literal|"."
decl_stmt|;
comment|// TODO(user): If we remove the entry before setting the value reference,
comment|// if the caller retries, they'll get the result of a different
comment|// rather than the same result.
name|setValueReference
argument_list|(
name|entry
argument_list|,
operator|new
name|NullPointerExceptionReference
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|NullPointerException
argument_list|(
name|message
argument_list|)
throw|;
block|}
name|setComputedValue
argument_list|(
name|entry
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
block|}
DECL|method|setValueReference (ReferenceEntry<K, V> entry, ValueReference<K, V> valueReference)
annotation|@
name|Override
name|void
name|setValueReference
parameter_list|(
name|ReferenceEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
parameter_list|,
name|ValueReference
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|valueReference
parameter_list|)
block|{
name|boolean
name|notifyOthers
init|=
operator|(
name|entry
operator|.
name|getValueReference
argument_list|()
operator|==
name|UNSET
operator|)
decl_stmt|;
name|entry
operator|.
name|setValueReference
argument_list|(
name|valueReference
argument_list|)
expr_stmt|;
if|if
condition|(
name|notifyOthers
condition|)
block|{
synchronized|synchronized
init|(
name|entry
init|)
block|{
name|entry
operator|.
name|notifyAll
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Waits for a computation to complete. Returns the result of the    * computation or null if none was available.    */
DECL|method|waitForValue (ReferenceEntry<K, V> entry)
specifier|public
name|V
name|waitForValue
parameter_list|(
name|ReferenceEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|ValueReference
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|valueReference
init|=
name|entry
operator|.
name|getValueReference
argument_list|()
decl_stmt|;
if|if
condition|(
name|valueReference
operator|==
name|UNSET
condition|)
block|{
synchronized|synchronized
init|(
name|entry
init|)
block|{
while|while
condition|(
operator|(
name|valueReference
operator|=
name|entry
operator|.
name|getValueReference
argument_list|()
operator|)
operator|==
name|UNSET
condition|)
block|{
name|entry
operator|.
name|wait
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
name|valueReference
operator|.
name|waitForValue
argument_list|()
return|;
block|}
comment|/** Used to provide null pointer exceptions to other threads. */
DECL|class|NullPointerExceptionReference
specifier|private
specifier|static
class|class
name|NullPointerExceptionReference
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
DECL|field|message
specifier|final
name|String
name|message
decl_stmt|;
DECL|method|NullPointerExceptionReference (String message)
name|NullPointerExceptionReference
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
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
DECL|method|copyFor ( ReferenceEntry<K, V> entry)
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
DECL|method|waitForValue ()
specifier|public
name|V
name|waitForValue
parameter_list|()
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
name|message
argument_list|)
throw|;
block|}
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{}
block|}
comment|/** Used to provide computation exceptions to other threads. */
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
DECL|method|copyFor ( ReferenceEntry<K, V> entry)
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
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{}
block|}
DECL|method|copyEntry ( ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext)
annotation|@
name|Override
name|ReferenceEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|copyEntry
parameter_list|(
name|ReferenceEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|original
parameter_list|,
name|ReferenceEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newNext
parameter_list|)
block|{
name|ReferenceEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newEntry
init|=
name|entryFactory
operator|.
name|copyEntry
argument_list|(
name|this
argument_list|,
name|original
argument_list|,
name|newNext
argument_list|)
decl_stmt|;
name|ValueReference
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|valueReference
init|=
name|original
operator|.
name|getValueReference
argument_list|()
decl_stmt|;
if|if
condition|(
name|valueReference
operator|==
name|UNSET
condition|)
block|{
name|newEntry
operator|.
name|setValueReference
argument_list|(
operator|new
name|FutureValueReference
argument_list|(
name|original
argument_list|,
name|newEntry
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|newEntry
operator|.
name|setValueReference
argument_list|(
name|valueReference
operator|.
name|copyFor
argument_list|(
name|newEntry
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|newEntry
return|;
block|}
comment|/**    * Points to an old entry where a value is being computed. Used to    * support non-blocking copying of entries during table expansion,    * removals, etc.    */
DECL|class|FutureValueReference
specifier|private
class|class
name|FutureValueReference
implements|implements
name|ValueReference
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|original
specifier|final
name|ReferenceEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|original
decl_stmt|;
DECL|field|newEntry
specifier|final
name|ReferenceEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newEntry
decl_stmt|;
DECL|method|FutureValueReference ( ReferenceEntry<K, V> original, ReferenceEntry<K, V> newEntry)
name|FutureValueReference
parameter_list|(
name|ReferenceEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|original
parameter_list|,
name|ReferenceEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newEntry
parameter_list|)
block|{
name|this
operator|.
name|original
operator|=
name|original
expr_stmt|;
name|this
operator|.
name|newEntry
operator|=
name|newEntry
expr_stmt|;
block|}
DECL|method|get ()
specifier|public
name|V
name|get
parameter_list|()
block|{
name|boolean
name|success
init|=
literal|false
decl_stmt|;
try|try
block|{
name|V
name|value
init|=
name|original
operator|.
name|getValueReference
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|success
operator|=
literal|true
expr_stmt|;
return|return
name|value
return|;
block|}
finally|finally
block|{
if|if
condition|(
operator|!
name|success
condition|)
block|{
name|removeEntry
argument_list|()
expr_stmt|;
block|}
block|}
block|}
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
operator|new
name|FutureValueReference
argument_list|(
name|original
argument_list|,
name|entry
argument_list|)
return|;
block|}
DECL|method|waitForValue ()
specifier|public
name|V
name|waitForValue
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|boolean
name|success
init|=
literal|false
decl_stmt|;
try|try
block|{
comment|// assert that key != null
name|V
name|value
init|=
name|ComputingConcurrentHashMap
operator|.
name|this
operator|.
name|waitForValue
argument_list|(
name|original
argument_list|)
decl_stmt|;
name|success
operator|=
literal|true
expr_stmt|;
return|return
name|value
return|;
block|}
finally|finally
block|{
if|if
condition|(
operator|!
name|success
condition|)
block|{
name|removeEntry
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|original
operator|.
name|getValueReference
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * Removes the entry in the event of an exception. Ideally,      * we'd clean up as soon as the computation completes, but we      * can't do that without keeping a reference to this entry from      * the original.      */
DECL|method|removeEntry ()
name|void
name|removeEntry
parameter_list|()
block|{
name|ComputingConcurrentHashMap
operator|.
name|this
operator|.
name|removeEntry
argument_list|(
name|newEntry
argument_list|)
expr_stmt|;
block|}
block|}
comment|/* ---------------- Serialization Support -------------- */
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|2
decl_stmt|;
DECL|method|writeReplace ()
annotation|@
name|Override
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
DECL|field|cache
specifier|transient
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|cache
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
name|cache
operator|=
name|mapMaker
operator|.
name|makeCache
argument_list|(
name|computingFunction
argument_list|)
expr_stmt|;
name|delegate
operator|=
name|cache
operator|.
name|asMap
argument_list|()
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
name|cache
return|;
block|}
DECL|method|asMap ()
specifier|public
name|ConcurrentMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|asMap
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
DECL|method|apply (@ullable K from)
specifier|public
name|V
name|apply
parameter_list|(
annotation|@
name|Nullable
name|K
name|from
parameter_list|)
block|{
return|return
name|cache
operator|.
name|apply
argument_list|(
name|from
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
literal|2
decl_stmt|;
block|}
block|}
end_class

end_unit

