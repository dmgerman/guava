begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2017 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.primitives
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
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
name|Preconditions
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|errorprone
operator|.
name|annotations
operator|.
name|CanIgnoreReturnValue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|errorprone
operator|.
name|annotations
operator|.
name|CheckReturnValue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|errorprone
operator|.
name|annotations
operator|.
name|Immutable
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
name|AbstractList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|RandomAccess
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
comment|/**  * An immutable array of {@code int} values, with an API resembling {@link List}.  *  *<p>Advantages compared to {@code int[]}:  *  *<ul>  *<li>All the many well-known advantages of immutability (read<i>Effective Java</i>, second  *       edition, Item 15).  *<li>Has the value-based (not identity-based) {@link #equals}, {@link #hashCode}, and {@link  *       #toString} behavior you expect  *<li>Offers useful operations beyond just {@code get} and {@code length}, so you don't have to  *       hunt through classes like {@link Arrays} and {@link Ints} for them.  *<li>Supports a copy-free {@link #subArray} view, so methods that accept this type don't need to  *       add overloads that accept start and end indexes.  *<li>Access to all collection-based utilities via {@link #asList} (though at the cost of  *       allocating garbage).  *</ul>  *  *<p>Disadvantages compared to {@code int[]}:  *  *<ul>  *<li>Memory footprint has a fixed overhead (about 24 bytes per instance).  *<li><i>Some</i> construction use cases force the data to be copied (though several construction  *       APIs are offered that don't).  *<li>Can't be passed directly to methods that expect {@code int[]} (though the most common  *       utilities do have replacements here).  *<li>Dependency on {@code com.google.common} / Guava.  *</ul>  *  *<p>Advantages compared to {@link com.google.common.collect.ImmutableList ImmutableList}{@code  *<Integer>}:  *  *<ul>  *<li>Improved memory compactness and locality  *<li>Can be queried without allocating garbage  *</ul>  *  *<p>Disadvantages compared to {@code ImmutableList<Integer>}:  *  *<ul>  *<li>Can't be passed directly to methods that expect {@code Iterable}, {@code Collection}, or  *       {@code List} (though the most common utilities do have replacements here, and there is a  *       lazy {@link #asList} view).  *</ul>  *  * @since 22.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
annotation|@
name|Immutable
DECL|class|ImmutableIntArray
specifier|public
specifier|final
class|class
name|ImmutableIntArray
implements|implements
name|Serializable
block|{
DECL|field|EMPTY
specifier|private
specifier|static
specifier|final
name|ImmutableIntArray
name|EMPTY
init|=
operator|new
name|ImmutableIntArray
argument_list|(
operator|new
name|int
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
comment|/** Returns the empty array. */
DECL|method|of ()
specifier|public
specifier|static
name|ImmutableIntArray
name|of
parameter_list|()
block|{
return|return
name|EMPTY
return|;
block|}
comment|/** Returns an immutable array containing a single value. */
DECL|method|of (int e0)
specifier|public
specifier|static
name|ImmutableIntArray
name|of
parameter_list|(
name|int
name|e0
parameter_list|)
block|{
return|return
operator|new
name|ImmutableIntArray
argument_list|(
operator|new
name|int
index|[]
block|{
name|e0
block|}
argument_list|)
return|;
block|}
comment|/** Returns an immutable array containing the given values, in order. */
DECL|method|of (int e0, int e1)
specifier|public
specifier|static
name|ImmutableIntArray
name|of
parameter_list|(
name|int
name|e0
parameter_list|,
name|int
name|e1
parameter_list|)
block|{
return|return
operator|new
name|ImmutableIntArray
argument_list|(
operator|new
name|int
index|[]
block|{
name|e0
block|,
name|e1
block|}
argument_list|)
return|;
block|}
comment|/** Returns an immutable array containing the given values, in order. */
DECL|method|of (int e0, int e1, int e2)
specifier|public
specifier|static
name|ImmutableIntArray
name|of
parameter_list|(
name|int
name|e0
parameter_list|,
name|int
name|e1
parameter_list|,
name|int
name|e2
parameter_list|)
block|{
return|return
operator|new
name|ImmutableIntArray
argument_list|(
operator|new
name|int
index|[]
block|{
name|e0
block|,
name|e1
block|,
name|e2
block|}
argument_list|)
return|;
block|}
comment|/** Returns an immutable array containing the given values, in order. */
DECL|method|of (int e0, int e1, int e2, int e3)
specifier|public
specifier|static
name|ImmutableIntArray
name|of
parameter_list|(
name|int
name|e0
parameter_list|,
name|int
name|e1
parameter_list|,
name|int
name|e2
parameter_list|,
name|int
name|e3
parameter_list|)
block|{
return|return
operator|new
name|ImmutableIntArray
argument_list|(
operator|new
name|int
index|[]
block|{
name|e0
block|,
name|e1
block|,
name|e2
block|,
name|e3
block|}
argument_list|)
return|;
block|}
comment|/** Returns an immutable array containing the given values, in order. */
DECL|method|of (int e0, int e1, int e2, int e3, int e4)
specifier|public
specifier|static
name|ImmutableIntArray
name|of
parameter_list|(
name|int
name|e0
parameter_list|,
name|int
name|e1
parameter_list|,
name|int
name|e2
parameter_list|,
name|int
name|e3
parameter_list|,
name|int
name|e4
parameter_list|)
block|{
return|return
operator|new
name|ImmutableIntArray
argument_list|(
operator|new
name|int
index|[]
block|{
name|e0
block|,
name|e1
block|,
name|e2
block|,
name|e3
block|,
name|e4
block|}
argument_list|)
return|;
block|}
comment|/** Returns an immutable array containing the given values, in order. */
DECL|method|of (int e0, int e1, int e2, int e3, int e4, int e5)
specifier|public
specifier|static
name|ImmutableIntArray
name|of
parameter_list|(
name|int
name|e0
parameter_list|,
name|int
name|e1
parameter_list|,
name|int
name|e2
parameter_list|,
name|int
name|e3
parameter_list|,
name|int
name|e4
parameter_list|,
name|int
name|e5
parameter_list|)
block|{
return|return
operator|new
name|ImmutableIntArray
argument_list|(
operator|new
name|int
index|[]
block|{
name|e0
block|,
name|e1
block|,
name|e2
block|,
name|e3
block|,
name|e4
block|,
name|e5
block|}
argument_list|)
return|;
block|}
comment|// TODO(kevinb): go up to 11?
comment|/**    * Returns an immutable array containing the given values, in order.    *    *<p>The array {@code rest} must not be longer than {@code Integer.MAX_VALUE - 1}.    */
comment|// Use (first, rest) so that `of(someIntArray)` won't compile (they should use copyOf), which is
comment|// okay since we have to copy the just-created array anyway.
DECL|method|of (int first, int... rest)
specifier|public
specifier|static
name|ImmutableIntArray
name|of
parameter_list|(
name|int
name|first
parameter_list|,
name|int
modifier|...
name|rest
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|rest
operator|.
name|length
operator|<=
name|Integer
operator|.
name|MAX_VALUE
operator|-
literal|1
argument_list|,
literal|"the total number of elements must fit in an int"
argument_list|)
expr_stmt|;
name|int
index|[]
name|array
init|=
operator|new
name|int
index|[
name|rest
operator|.
name|length
operator|+
literal|1
index|]
decl_stmt|;
name|array
index|[
literal|0
index|]
operator|=
name|first
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|rest
argument_list|,
literal|0
argument_list|,
name|array
argument_list|,
literal|1
argument_list|,
name|rest
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
operator|new
name|ImmutableIntArray
argument_list|(
name|array
argument_list|)
return|;
block|}
comment|/** Returns an immutable array containing the given values, in order. */
DECL|method|copyOf (int[] values)
specifier|public
specifier|static
name|ImmutableIntArray
name|copyOf
parameter_list|(
name|int
index|[]
name|values
parameter_list|)
block|{
return|return
name|values
operator|.
name|length
operator|==
literal|0
condition|?
name|EMPTY
else|:
operator|new
name|ImmutableIntArray
argument_list|(
name|Arrays
operator|.
name|copyOf
argument_list|(
name|values
argument_list|,
name|values
operator|.
name|length
argument_list|)
argument_list|)
return|;
block|}
comment|/** Returns an immutable array containing the given values, in order. */
DECL|method|copyOf (Collection<Integer> values)
specifier|public
specifier|static
name|ImmutableIntArray
name|copyOf
parameter_list|(
name|Collection
argument_list|<
name|Integer
argument_list|>
name|values
parameter_list|)
block|{
return|return
name|values
operator|.
name|isEmpty
argument_list|()
condition|?
name|EMPTY
else|:
operator|new
name|ImmutableIntArray
argument_list|(
name|Ints
operator|.
name|toArray
argument_list|(
name|values
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable array containing the given values, in order.    *    *<p><b>Performance note:</b> this method delegates to {@link #copyOf(Collection)} if {@code    * values} is a {@link Collection}. Otherwise it creates a {@link #builder} and uses {@link    * Builder#addAll(Iterable)}, with all the performance implications associated with that.    */
DECL|method|copyOf (Iterable<Integer> values)
specifier|public
specifier|static
name|ImmutableIntArray
name|copyOf
parameter_list|(
name|Iterable
argument_list|<
name|Integer
argument_list|>
name|values
parameter_list|)
block|{
if|if
condition|(
name|values
operator|instanceof
name|Collection
condition|)
block|{
return|return
name|copyOf
argument_list|(
operator|(
name|Collection
argument_list|<
name|Integer
argument_list|>
operator|)
name|values
argument_list|)
return|;
block|}
return|return
name|builder
argument_list|()
operator|.
name|addAll
argument_list|(
name|values
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**    * Returns a new, empty builder for {@link ImmutableIntArray} instances, sized to hold up to    * {@code initialCapacity} values without resizing. The returned builder is not thread-safe.    *    *<p><b>Performance note:</b> When feasible, {@code initialCapacity} should be the exact number    * of values that will be added, if that knowledge is readily available. It is better to guess a    * value slightly too high than slightly too low. If the value is not exact, the {@link    * ImmutableIntArray} that is built will very likely occupy more memory than strictly necessary;    * to trim memory usage, build using {@code builder.build().trimmed()}.    */
DECL|method|builder (int initialCapacity)
specifier|public
specifier|static
name|Builder
name|builder
parameter_list|(
name|int
name|initialCapacity
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|initialCapacity
operator|>=
literal|0
argument_list|,
literal|"Invalid initialCapacity: %s"
argument_list|,
name|initialCapacity
argument_list|)
expr_stmt|;
return|return
operator|new
name|Builder
argument_list|(
name|initialCapacity
argument_list|)
return|;
block|}
comment|/**    * Returns a new, empty builder for {@link ImmutableIntArray} instances, with a default initial    * capacity. The returned builder is not thread-safe.    *    *<p><b>Performance note:</b> The {@link ImmutableIntArray} that is built will very likely occupy    * more memory than necessary; to trim memory usage, build using {@code    * builder.build().trimmed()}.    */
DECL|method|builder ()
specifier|public
specifier|static
name|Builder
name|builder
parameter_list|()
block|{
return|return
operator|new
name|Builder
argument_list|(
literal|10
argument_list|)
return|;
block|}
comment|/**    * A builder for {@link ImmutableIntArray} instances; obtained using {@link    * ImmutableIntArray#builder}.    */
annotation|@
name|CanIgnoreReturnValue
DECL|class|Builder
specifier|public
specifier|static
specifier|final
class|class
name|Builder
block|{
DECL|field|array
specifier|private
name|int
index|[]
name|array
decl_stmt|;
DECL|field|count
specifier|private
name|int
name|count
init|=
literal|0
decl_stmt|;
comment|//<= array.length
DECL|method|Builder (int initialCapacity)
name|Builder
parameter_list|(
name|int
name|initialCapacity
parameter_list|)
block|{
name|array
operator|=
operator|new
name|int
index|[
name|initialCapacity
index|]
expr_stmt|;
block|}
comment|/**      * Appends {@code value} to the end of the values the built {@link ImmutableIntArray} will      * contain.      */
DECL|method|add (int value)
specifier|public
name|Builder
name|add
parameter_list|(
name|int
name|value
parameter_list|)
block|{
name|ensureRoomFor
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|array
index|[
name|count
index|]
operator|=
name|value
expr_stmt|;
name|count
operator|+=
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Appends {@code values}, in order, to the end of the values the built {@link      * ImmutableIntArray} will contain.      */
DECL|method|addAll (int[] values)
specifier|public
name|Builder
name|addAll
parameter_list|(
name|int
index|[]
name|values
parameter_list|)
block|{
name|ensureRoomFor
argument_list|(
name|values
operator|.
name|length
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|values
argument_list|,
literal|0
argument_list|,
name|array
argument_list|,
name|count
argument_list|,
name|values
operator|.
name|length
argument_list|)
expr_stmt|;
name|count
operator|+=
name|values
operator|.
name|length
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Appends {@code values}, in order, to the end of the values the built {@link      * ImmutableIntArray} will contain.      */
DECL|method|addAll (Iterable<Integer> values)
specifier|public
name|Builder
name|addAll
parameter_list|(
name|Iterable
argument_list|<
name|Integer
argument_list|>
name|values
parameter_list|)
block|{
if|if
condition|(
name|values
operator|instanceof
name|Collection
condition|)
block|{
return|return
name|addAll
argument_list|(
operator|(
name|Collection
argument_list|<
name|Integer
argument_list|>
operator|)
name|values
argument_list|)
return|;
block|}
for|for
control|(
name|Integer
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
return|return
name|this
return|;
block|}
comment|/**      * Appends {@code values}, in order, to the end of the values the built {@link      * ImmutableIntArray} will contain.      */
DECL|method|addAll (Collection<Integer> values)
specifier|public
name|Builder
name|addAll
parameter_list|(
name|Collection
argument_list|<
name|Integer
argument_list|>
name|values
parameter_list|)
block|{
name|ensureRoomFor
argument_list|(
name|values
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Integer
name|value
range|:
name|values
control|)
block|{
name|array
index|[
name|count
operator|++
index|]
operator|=
name|value
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      * Appends {@code values}, in order, to the end of the values the built {@link      * ImmutableIntArray} will contain.      */
DECL|method|addAll (ImmutableIntArray values)
specifier|public
name|Builder
name|addAll
parameter_list|(
name|ImmutableIntArray
name|values
parameter_list|)
block|{
name|ensureRoomFor
argument_list|(
name|values
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|values
operator|.
name|array
argument_list|,
name|values
operator|.
name|start
argument_list|,
name|array
argument_list|,
name|count
argument_list|,
name|values
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|count
operator|+=
name|values
operator|.
name|length
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|ensureRoomFor (int numberToAdd)
specifier|private
name|void
name|ensureRoomFor
parameter_list|(
name|int
name|numberToAdd
parameter_list|)
block|{
name|int
name|newCount
init|=
name|count
operator|+
name|numberToAdd
decl_stmt|;
comment|// TODO(kevinb): check overflow now?
if|if
condition|(
name|newCount
operator|>
name|array
operator|.
name|length
condition|)
block|{
name|int
index|[]
name|newArray
init|=
operator|new
name|int
index|[
name|expandedCapacity
argument_list|(
name|array
operator|.
name|length
argument_list|,
name|newCount
argument_list|)
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|array
argument_list|,
literal|0
argument_list|,
name|newArray
argument_list|,
literal|0
argument_list|,
name|count
argument_list|)
expr_stmt|;
name|this
operator|.
name|array
operator|=
name|newArray
expr_stmt|;
block|}
block|}
comment|// Unfortunately this is pasted from ImmutableCollection.Builder.
DECL|method|expandedCapacity (int oldCapacity, int minCapacity)
specifier|private
specifier|static
name|int
name|expandedCapacity
parameter_list|(
name|int
name|oldCapacity
parameter_list|,
name|int
name|minCapacity
parameter_list|)
block|{
if|if
condition|(
name|minCapacity
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"cannot store more than MAX_VALUE elements"
argument_list|)
throw|;
block|}
comment|// careful of overflow!
name|int
name|newCapacity
init|=
name|oldCapacity
operator|+
operator|(
name|oldCapacity
operator|>>
literal|1
operator|)
operator|+
literal|1
decl_stmt|;
if|if
condition|(
name|newCapacity
operator|<
name|minCapacity
condition|)
block|{
name|newCapacity
operator|=
name|Integer
operator|.
name|highestOneBit
argument_list|(
name|minCapacity
operator|-
literal|1
argument_list|)
operator|<<
literal|1
expr_stmt|;
block|}
if|if
condition|(
name|newCapacity
operator|<
literal|0
condition|)
block|{
name|newCapacity
operator|=
name|Integer
operator|.
name|MAX_VALUE
expr_stmt|;
comment|// guaranteed to be>= newCapacity
block|}
return|return
name|newCapacity
return|;
block|}
comment|/**      * Returns a new immutable array. The builder can continue to be used after this call, to append      * more values and build again.      *      *<p><b>Performance note:</b> the returned array is backed by the same array as the builder, so      * no data is copied as part of this step, but this may occupy more memory than strictly      * necessary. To copy the data to a right-sized backing array, use {@code .build().trimmed()}.      */
annotation|@
name|CheckReturnValue
DECL|method|build ()
specifier|public
name|ImmutableIntArray
name|build
parameter_list|()
block|{
return|return
name|count
operator|==
literal|0
condition|?
name|EMPTY
else|:
operator|new
name|ImmutableIntArray
argument_list|(
name|array
argument_list|,
literal|0
argument_list|,
name|count
argument_list|)
return|;
block|}
block|}
comment|// Instance stuff here
comment|// The array is never mutated after storing in this field and the construction strategies ensure
comment|// it doesn't escape this class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"Immutable"
argument_list|)
DECL|field|array
specifier|private
specifier|final
name|int
index|[]
name|array
decl_stmt|;
comment|/*    * TODO(kevinb): evaluate the trade-offs of going bimorphic to save these two fields from most    * instances. Note that the instances that would get smaller are the right set to care about    * optimizing, because the rest have the option of calling `trimmed`.    */
DECL|field|start
specifier|private
specifier|final
specifier|transient
name|int
name|start
decl_stmt|;
comment|// it happens that we only serialize instances where this is 0
DECL|field|end
specifier|private
specifier|final
name|int
name|end
decl_stmt|;
comment|// exclusive
DECL|method|ImmutableIntArray (int[] array)
specifier|private
name|ImmutableIntArray
parameter_list|(
name|int
index|[]
name|array
parameter_list|)
block|{
name|this
argument_list|(
name|array
argument_list|,
literal|0
argument_list|,
name|array
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
DECL|method|ImmutableIntArray (int[] array, int start, int end)
specifier|private
name|ImmutableIntArray
parameter_list|(
name|int
index|[]
name|array
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|end
parameter_list|)
block|{
name|this
operator|.
name|array
operator|=
name|array
expr_stmt|;
name|this
operator|.
name|start
operator|=
name|start
expr_stmt|;
name|this
operator|.
name|end
operator|=
name|end
expr_stmt|;
block|}
comment|/** Returns the number of values in this array. */
DECL|method|length ()
specifier|public
name|int
name|length
parameter_list|()
block|{
return|return
name|end
operator|-
name|start
return|;
block|}
comment|/** Returns {@code true} if there are no values in this array ({@link #length} is zero). */
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|end
operator|==
name|start
return|;
block|}
comment|/**    * Returns the {@code int} value present at the given index.    *    * @throws IndexOutOfBoundsException if {@code index} is negative, or greater than or equal to    *     {@link #length}    */
DECL|method|get (int index)
specifier|public
name|int
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkElementIndex
argument_list|(
name|index
argument_list|,
name|length
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|array
index|[
name|start
operator|+
name|index
index|]
return|;
block|}
comment|/**    * Returns the smallest index for which {@link #get} returns {@code target}, or {@code -1} if no    * such index exists. Equivalent to {@code asList().indexOf(target)}.    */
DECL|method|indexOf (int target)
specifier|public
name|int
name|indexOf
parameter_list|(
name|int
name|target
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
name|start
init|;
name|i
operator|<
name|end
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|array
index|[
name|i
index|]
operator|==
name|target
condition|)
block|{
return|return
name|i
operator|-
name|start
return|;
block|}
block|}
return|return
operator|-
literal|1
return|;
block|}
comment|/**    * Returns the largest index for which {@link #get} returns {@code target}, or {@code -1} if no    * such index exists. Equivalent to {@code asList().lastIndexOf(target)}.    */
DECL|method|lastIndexOf (int target)
specifier|public
name|int
name|lastIndexOf
parameter_list|(
name|int
name|target
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
name|end
operator|-
literal|1
init|;
name|i
operator|>=
name|start
condition|;
name|i
operator|--
control|)
block|{
if|if
condition|(
name|array
index|[
name|i
index|]
operator|==
name|target
condition|)
block|{
return|return
name|i
operator|-
name|start
return|;
block|}
block|}
return|return
operator|-
literal|1
return|;
block|}
comment|/**    * Returns {@code true} if {@code target} is present at any index in this array. Equivalent to    * {@code asList().contains(target)}.    */
DECL|method|contains (int target)
specifier|public
name|boolean
name|contains
parameter_list|(
name|int
name|target
parameter_list|)
block|{
return|return
name|indexOf
argument_list|(
name|target
argument_list|)
operator|>=
literal|0
return|;
block|}
comment|/** Returns a new, mutable copy of this array's values, as a primitive {@code int[]}. */
DECL|method|toArray ()
specifier|public
name|int
index|[]
name|toArray
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|array
argument_list|,
name|start
argument_list|,
name|end
argument_list|)
return|;
block|}
comment|/**    * Returns a new immutable array containing the values in the specified range.    *    *<p><b>Performance note:</b> The returned array has the same full memory footprint as this one    * does (no actual copying is performed). To reduce memory usage, use {@code subArray(start,    * end).trimmed()}.    */
DECL|method|subArray (int startIndex, int endIndex)
specifier|public
name|ImmutableIntArray
name|subArray
parameter_list|(
name|int
name|startIndex
parameter_list|,
name|int
name|endIndex
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkPositionIndexes
argument_list|(
name|startIndex
argument_list|,
name|endIndex
argument_list|,
name|length
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|startIndex
operator|==
name|endIndex
condition|?
name|EMPTY
else|:
operator|new
name|ImmutableIntArray
argument_list|(
name|array
argument_list|,
name|start
operator|+
name|startIndex
argument_list|,
name|start
operator|+
name|endIndex
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable<i>view</i> of this array's values as a {@code List}; note that {@code    * int} values are boxed into {@link Integer} instances on demand, which can be very expensive.    * The returned list should be used once and discarded. For any usages beyond that, pass the    * returned list to {@link com.google.common.collect.ImmutableList#copyOf(Collection)    * ImmutableList.copyOf} and use that list instead.    */
DECL|method|asList ()
specifier|public
name|List
argument_list|<
name|Integer
argument_list|>
name|asList
parameter_list|()
block|{
comment|/*      * Typically we cache this kind of thing, but much repeated use of this view is a performance      * anti-pattern anyway. If we cache, then everyone pays a price in memory footprint even if      * they never use this method.      */
return|return
operator|new
name|AsList
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|class|AsList
specifier|static
class|class
name|AsList
extends|extends
name|AbstractList
argument_list|<
name|Integer
argument_list|>
implements|implements
name|RandomAccess
implements|,
name|Serializable
block|{
DECL|field|parent
specifier|private
specifier|final
name|ImmutableIntArray
name|parent
decl_stmt|;
DECL|method|AsList (ImmutableIntArray parent)
specifier|private
name|AsList
parameter_list|(
name|ImmutableIntArray
name|parent
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
block|}
comment|// inherit: isEmpty, containsAll, toArray x2, {,list,spl}iterator, stream, forEach, mutations
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|parent
operator|.
name|length
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|get (int index)
specifier|public
name|Integer
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|parent
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|contains (Object target)
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|target
parameter_list|)
block|{
return|return
name|indexOf
argument_list|(
name|target
argument_list|)
operator|>=
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|indexOf (Object target)
specifier|public
name|int
name|indexOf
parameter_list|(
name|Object
name|target
parameter_list|)
block|{
return|return
name|target
operator|instanceof
name|Integer
condition|?
name|parent
operator|.
name|indexOf
argument_list|(
operator|(
name|Integer
operator|)
name|target
argument_list|)
else|:
operator|-
literal|1
return|;
block|}
annotation|@
name|Override
DECL|method|lastIndexOf (Object target)
specifier|public
name|int
name|lastIndexOf
parameter_list|(
name|Object
name|target
parameter_list|)
block|{
return|return
name|target
operator|instanceof
name|Integer
condition|?
name|parent
operator|.
name|lastIndexOf
argument_list|(
operator|(
name|Integer
operator|)
name|target
argument_list|)
else|:
operator|-
literal|1
return|;
block|}
annotation|@
name|Override
DECL|method|subList (int fromIndex, int toIndex)
specifier|public
name|List
argument_list|<
name|Integer
argument_list|>
name|subList
parameter_list|(
name|int
name|fromIndex
parameter_list|,
name|int
name|toIndex
parameter_list|)
block|{
return|return
name|parent
operator|.
name|subArray
argument_list|(
name|fromIndex
argument_list|,
name|toIndex
argument_list|)
operator|.
name|asList
argument_list|()
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
name|AsList
condition|)
block|{
name|AsList
name|that
init|=
operator|(
name|AsList
operator|)
name|object
decl_stmt|;
return|return
name|this
operator|.
name|parent
operator|.
name|equals
argument_list|(
name|that
operator|.
name|parent
argument_list|)
return|;
block|}
comment|// We could delegate to super now but it would still box too much
if|if
condition|(
operator|!
operator|(
name|object
operator|instanceof
name|List
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|List
argument_list|<
name|?
argument_list|>
name|that
init|=
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|size
argument_list|()
operator|!=
name|that
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|int
name|i
init|=
name|parent
operator|.
name|start
decl_stmt|;
comment|// Since `that` is very likely RandomAccess we could avoid allocating this iterator...
for|for
control|(
name|Object
name|element
range|:
name|that
control|)
block|{
if|if
condition|(
operator|!
operator|(
name|element
operator|instanceof
name|Integer
operator|)
operator|||
name|parent
operator|.
name|array
index|[
name|i
operator|++
index|]
operator|!=
operator|(
name|Integer
operator|)
name|element
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|// Because we happen to use the same formula. If that changes, just don't override this.
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|parent
operator|.
name|hashCode
argument_list|()
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
name|parent
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
comment|/**    * Returns {@code true} if {@code object} is an {@code ImmutableIntArray} containing the same    * values as this one, in the same order.    */
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
operator|!
operator|(
name|object
operator|instanceof
name|ImmutableIntArray
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ImmutableIntArray
name|that
init|=
operator|(
name|ImmutableIntArray
operator|)
name|object
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|length
argument_list|()
operator|!=
name|that
operator|.
name|length
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|this
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|!=
name|that
operator|.
name|get
argument_list|(
name|i
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/** Returns an unspecified hash code for the contents of this immutable array. */
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|hash
init|=
literal|1
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|start
init|;
name|i
operator|<
name|end
condition|;
name|i
operator|++
control|)
block|{
name|hash
operator|*=
literal|31
expr_stmt|;
name|hash
operator|+=
name|Ints
operator|.
name|hashCode
argument_list|(
name|array
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|hash
return|;
block|}
comment|/**    * Returns a string representation of this array in the same form as {@link    * Arrays#toString(int[])}, for example {@code "[1, 2, 3]"}.    */
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
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|"[]"
return|;
block|}
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|(
name|length
argument_list|()
operator|*
literal|5
argument_list|)
decl_stmt|;
comment|// rough estimate is fine
name|builder
operator|.
name|append
argument_list|(
literal|'['
argument_list|)
operator|.
name|append
argument_list|(
name|array
index|[
name|start
index|]
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|start
operator|+
literal|1
init|;
name|i
operator|<
name|end
condition|;
name|i
operator|++
control|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
operator|.
name|append
argument_list|(
name|array
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**    * Returns an immutable array containing the same values as {@code this} array. This is logically    * a no-op, and in some circumstances {@code this} itself is returned. However, if this instance    * is a {@link #subArray} view of a larger array, this method will copy only the appropriate range    * of values, resulting in an equivalent array with a smaller memory footprint.    */
DECL|method|trimmed ()
specifier|public
name|ImmutableIntArray
name|trimmed
parameter_list|()
block|{
return|return
name|isPartialView
argument_list|()
condition|?
operator|new
name|ImmutableIntArray
argument_list|(
name|toArray
argument_list|()
argument_list|)
else|:
name|this
return|;
block|}
DECL|method|isPartialView ()
specifier|private
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
name|start
operator|>
literal|0
operator|||
name|end
operator|<
name|array
operator|.
name|length
return|;
block|}
DECL|method|writeReplace ()
name|Object
name|writeReplace
parameter_list|()
block|{
return|return
name|trimmed
argument_list|()
return|;
block|}
DECL|method|readResolve ()
name|Object
name|readResolve
parameter_list|()
block|{
return|return
name|isEmpty
argument_list|()
condition|?
name|EMPTY
else|:
name|this
return|;
block|}
block|}
end_class

end_unit

