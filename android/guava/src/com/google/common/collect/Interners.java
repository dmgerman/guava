begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|Dummy
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
name|MapMakerInternalMap
operator|.
name|InternalEntry
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|CheckForNull
import|;
end_import

begin_comment
comment|/**  * Contains static methods pertaining to instances of {@link Interner}.  *  * @author Kevin Bourrillion  * @since 3.0  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|Interners
specifier|public
specifier|final
class|class
name|Interners
block|{
DECL|method|Interners ()
specifier|private
name|Interners
parameter_list|()
block|{}
comment|/**    * Builder for {@link Interner} instances.    *    * @since 21.0    */
DECL|class|InternerBuilder
specifier|public
specifier|static
class|class
name|InternerBuilder
block|{
DECL|field|mapMaker
specifier|private
specifier|final
name|MapMaker
name|mapMaker
init|=
operator|new
name|MapMaker
argument_list|()
decl_stmt|;
DECL|field|strong
specifier|private
name|boolean
name|strong
init|=
literal|true
decl_stmt|;
DECL|method|InternerBuilder ()
specifier|private
name|InternerBuilder
parameter_list|()
block|{}
comment|/**      * Instructs the {@link InternerBuilder} to build a strong interner.      *      * @see Interners#newStrongInterner()      */
DECL|method|strong ()
specifier|public
name|InternerBuilder
name|strong
parameter_list|()
block|{
name|this
operator|.
name|strong
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Instructs the {@link InternerBuilder} to build a weak interner.      *      * @see Interners#newWeakInterner()      */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"java.lang.ref.WeakReference"
argument_list|)
DECL|method|weak ()
specifier|public
name|InternerBuilder
name|weak
parameter_list|()
block|{
name|this
operator|.
name|strong
operator|=
literal|false
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the concurrency level that will be used by the to-be-built {@link Interner}.      *      * @see MapMaker#concurrencyLevel(int)      */
DECL|method|concurrencyLevel (int concurrencyLevel)
specifier|public
name|InternerBuilder
name|concurrencyLevel
parameter_list|(
name|int
name|concurrencyLevel
parameter_list|)
block|{
name|this
operator|.
name|mapMaker
operator|.
name|concurrencyLevel
argument_list|(
name|concurrencyLevel
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|build ()
specifier|public
parameter_list|<
name|E
parameter_list|>
name|Interner
argument_list|<
name|E
argument_list|>
name|build
parameter_list|()
block|{
if|if
condition|(
operator|!
name|strong
condition|)
block|{
name|mapMaker
operator|.
name|weakKeys
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|InternerImpl
argument_list|<
name|E
argument_list|>
argument_list|(
name|mapMaker
argument_list|)
return|;
block|}
block|}
comment|/** Returns a fresh {@link InternerBuilder} instance. */
DECL|method|newBuilder ()
specifier|public
specifier|static
name|InternerBuilder
name|newBuilder
parameter_list|()
block|{
return|return
operator|new
name|InternerBuilder
argument_list|()
return|;
block|}
comment|/**    * Returns a new thread-safe interner which retains a strong reference to each instance it has    * interned, thus preventing these instances from being garbage-collected. If this retention is    * acceptable, this implementation may perform better than {@link #newWeakInterner}.    */
DECL|method|newStrongInterner ()
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Interner
argument_list|<
name|E
argument_list|>
name|newStrongInterner
parameter_list|()
block|{
return|return
name|newBuilder
argument_list|()
operator|.
name|strong
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**    * Returns a new thread-safe interner which retains a weak reference to each instance it has    * interned, and so does not prevent these instances from being garbage-collected. This most    * likely does not perform as well as {@link #newStrongInterner}, but is the best alternative when    * the memory usage of that implementation is unacceptable.    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"java.lang.ref.WeakReference"
argument_list|)
DECL|method|newWeakInterner ()
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Interner
argument_list|<
name|E
argument_list|>
name|newWeakInterner
parameter_list|()
block|{
return|return
name|newBuilder
argument_list|()
operator|.
name|weak
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|VisibleForTesting
DECL|class|InternerImpl
specifier|static
specifier|final
class|class
name|InternerImpl
parameter_list|<
name|E
parameter_list|>
implements|implements
name|Interner
argument_list|<
name|E
argument_list|>
block|{
comment|// MapMaker is our friend, we know about this type
DECL|field|map
annotation|@
name|VisibleForTesting
specifier|final
name|MapMakerInternalMap
argument_list|<
name|E
argument_list|,
name|Dummy
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
name|map
decl_stmt|;
DECL|method|InternerImpl (MapMaker mapMaker)
specifier|private
name|InternerImpl
parameter_list|(
name|MapMaker
name|mapMaker
parameter_list|)
block|{
name|this
operator|.
name|map
operator|=
name|MapMakerInternalMap
operator|.
name|createWithDummyValues
argument_list|(
name|mapMaker
operator|.
name|keyEquivalence
argument_list|(
name|Equivalence
operator|.
name|equals
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|intern (E sample)
specifier|public
name|E
name|intern
parameter_list|(
name|E
name|sample
parameter_list|)
block|{
while|while
condition|(
literal|true
condition|)
block|{
comment|// trying to read the canonical...
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
comment|// using raw types to avoid a bug in our nullness checker :(
name|InternalEntry
name|entry
init|=
name|map
operator|.
name|getEntry
argument_list|(
name|sample
argument_list|)
decl_stmt|;
if|if
condition|(
name|entry
operator|!=
literal|null
condition|)
block|{
name|Object
name|canonical
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|canonical
operator|!=
literal|null
condition|)
block|{
comment|// only matters if weak/soft keys are used
comment|// The compiler would know this is safe if not for our use of raw types (see above).
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|E
name|result
init|=
operator|(
name|E
operator|)
name|canonical
decl_stmt|;
return|return
name|result
return|;
block|}
block|}
comment|// didn't see it, trying to put it instead...
name|Dummy
name|sneaky
init|=
name|map
operator|.
name|putIfAbsent
argument_list|(
name|sample
argument_list|,
name|Dummy
operator|.
name|VALUE
argument_list|)
decl_stmt|;
if|if
condition|(
name|sneaky
operator|==
literal|null
condition|)
block|{
return|return
name|sample
return|;
block|}
else|else
block|{
comment|/* Someone beat us to it! Trying again...            *            * Technically this loop not guaranteed to terminate, so theoretically (extremely            * unlikely) this thread might starve, but even then, there is always going to be another            * thread doing progress here.            */
block|}
block|}
block|}
block|}
comment|/**    * Returns a function that delegates to the {@link Interner#intern} method of the given interner.    *    * @since 8.0    */
DECL|method|asFunction (Interner<E> interner)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Function
argument_list|<
name|E
argument_list|,
name|E
argument_list|>
name|asFunction
parameter_list|(
name|Interner
argument_list|<
name|E
argument_list|>
name|interner
parameter_list|)
block|{
return|return
operator|new
name|InternerFunction
argument_list|<
name|E
argument_list|>
argument_list|(
name|checkNotNull
argument_list|(
name|interner
argument_list|)
argument_list|)
return|;
block|}
DECL|class|InternerFunction
specifier|private
specifier|static
class|class
name|InternerFunction
parameter_list|<
name|E
parameter_list|>
implements|implements
name|Function
argument_list|<
name|E
argument_list|,
name|E
argument_list|>
block|{
DECL|field|interner
specifier|private
specifier|final
name|Interner
argument_list|<
name|E
argument_list|>
name|interner
decl_stmt|;
DECL|method|InternerFunction (Interner<E> interner)
specifier|public
name|InternerFunction
parameter_list|(
name|Interner
argument_list|<
name|E
argument_list|>
name|interner
parameter_list|)
block|{
name|this
operator|.
name|interner
operator|=
name|interner
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (E input)
specifier|public
name|E
name|apply
parameter_list|(
name|E
name|input
parameter_list|)
block|{
return|return
name|interner
operator|.
name|intern
argument_list|(
name|input
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
name|interner
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@heckForNull Object other)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|other
parameter_list|)
block|{
if|if
condition|(
name|other
operator|instanceof
name|InternerFunction
condition|)
block|{
name|InternerFunction
argument_list|<
name|?
argument_list|>
name|that
init|=
operator|(
name|InternerFunction
argument_list|<
name|?
argument_list|>
operator|)
name|other
decl_stmt|;
return|return
name|interner
operator|.
name|equals
argument_list|(
name|that
operator|.
name|interner
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

