begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|base
operator|.
name|FinalizableReferenceQueue
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
name|FinalizableWeakReference
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
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentMap
import|;
end_import

begin_comment
comment|/**  * Contains static methods pertaining to instances of {@link Interner}.  *  * @author Kevin Bourrillion  * @since 3  */
end_comment

begin_class
annotation|@
name|Beta
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
comment|/**    * Returns a new thread-safe interner which retains a strong reference to each    * instance it has interned, thus preventing these instances from being    * garbage-collected. If this retention is acceptable, this implementation may    * perform better than {@link #newWeakInterner}. Note that unlike {@link    * String#intern}, using this interner does not consume memory in the    * permanent generation.    */
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
specifier|final
name|ConcurrentMap
argument_list|<
name|E
argument_list|,
name|E
argument_list|>
name|map
init|=
operator|new
name|MapMaker
argument_list|()
operator|.
name|makeMap
argument_list|()
decl_stmt|;
return|return
operator|new
name|Interner
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|E
name|intern
parameter_list|(
name|E
name|sample
parameter_list|)
block|{
name|E
name|canonical
init|=
name|map
operator|.
name|putIfAbsent
argument_list|(
name|checkNotNull
argument_list|(
name|sample
argument_list|)
argument_list|,
name|sample
argument_list|)
decl_stmt|;
return|return
operator|(
name|canonical
operator|==
literal|null
operator|)
condition|?
name|sample
else|:
name|canonical
return|;
block|}
block|}
return|;
block|}
comment|/**    * Returns a new thread-safe interner which retains a weak reference to each    * instance it has interned, and so does not prevent these instances from    * being garbage-collected. This most likely does not perform as well as    * {@link #newStrongInterner}, but is the best alternative when the memory    * usage of that implementation is unacceptable. Note that unlike {@link    * String#intern}, using this interner does not consume memory in the    * permanent generation.    */
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
operator|new
name|WeakInterner
argument_list|<
name|E
argument_list|>
argument_list|()
return|;
block|}
DECL|class|WeakInterner
specifier|private
specifier|static
class|class
name|WeakInterner
parameter_list|<
name|E
parameter_list|>
implements|implements
name|Interner
argument_list|<
name|E
argument_list|>
block|{
DECL|field|map
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|InternReference
argument_list|,
name|InternReference
argument_list|>
name|map
init|=
operator|new
name|MapMaker
argument_list|()
operator|.
name|makeMap
argument_list|()
decl_stmt|;
DECL|method|intern (final E sample)
specifier|public
name|E
name|intern
parameter_list|(
specifier|final
name|E
name|sample
parameter_list|)
block|{
specifier|final
name|int
name|hashCode
init|=
name|sample
operator|.
name|hashCode
argument_list|()
decl_stmt|;
comment|// TODO(kevinb): stop using the dummy instance; use custom Equivalence?
name|Object
name|fakeReference
init|=
operator|new
name|Object
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|hashCode
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|.
name|hashCode
argument_list|()
operator|!=
name|hashCode
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|/*            * Implicitly an unchecked cast to WeakInterner<?>.InternReference,            * though until OpenJDK 7, the compiler doesn't recognize this. If we            * could explicitly cast to the wildcard type            * WeakInterner<?>.InternReference, that would be sufficient for our            * purposes. The compiler, however, rejects such casts (or rather, it            * does until OpenJDK 7).            *            * See Sun bug 6665356.            */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|InternReference
name|that
init|=
operator|(
name|InternReference
operator|)
name|object
decl_stmt|;
return|return
name|sample
operator|.
name|equals
argument_list|(
name|that
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
block|}
decl_stmt|;
comment|// Fast-path; avoid creating the reference if possible
name|InternReference
name|existingRef
init|=
name|map
operator|.
name|get
argument_list|(
name|fakeReference
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingRef
operator|!=
literal|null
condition|)
block|{
name|E
name|canonical
init|=
name|existingRef
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|canonical
operator|!=
literal|null
condition|)
block|{
return|return
name|canonical
return|;
block|}
block|}
name|InternReference
name|newRef
init|=
operator|new
name|InternReference
argument_list|(
name|sample
argument_list|,
name|hashCode
argument_list|)
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|InternReference
name|sneakyRef
init|=
name|map
operator|.
name|putIfAbsent
argument_list|(
name|newRef
argument_list|,
name|newRef
argument_list|)
decl_stmt|;
if|if
condition|(
name|sneakyRef
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
name|E
name|canonical
init|=
name|sneakyRef
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|canonical
operator|!=
literal|null
condition|)
block|{
return|return
name|canonical
return|;
block|}
block|}
block|}
block|}
DECL|field|frq
specifier|private
specifier|static
specifier|final
name|FinalizableReferenceQueue
name|frq
init|=
operator|new
name|FinalizableReferenceQueue
argument_list|()
decl_stmt|;
DECL|class|InternReference
class|class
name|InternReference
extends|extends
name|FinalizableWeakReference
argument_list|<
name|E
argument_list|>
block|{
DECL|field|hashCode
specifier|final
name|int
name|hashCode
decl_stmt|;
DECL|method|InternReference (E key, int hash)
name|InternReference
parameter_list|(
name|E
name|key
parameter_list|,
name|int
name|hash
parameter_list|)
block|{
name|super
argument_list|(
name|key
argument_list|,
name|frq
argument_list|)
expr_stmt|;
name|hashCode
operator|=
name|hash
expr_stmt|;
block|}
DECL|method|finalizeReferent ()
specifier|public
name|void
name|finalizeReferent
parameter_list|()
block|{
name|map
operator|.
name|remove
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|get ()
annotation|@
name|Override
specifier|public
name|E
name|get
parameter_list|()
block|{
name|E
name|referent
init|=
name|super
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|referent
operator|==
literal|null
condition|)
block|{
name|finalizeReferent
argument_list|()
expr_stmt|;
block|}
return|return
name|referent
return|;
block|}
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|hashCode
return|;
block|}
DECL|method|equals (Object object)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
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
name|WeakInterner
operator|.
name|InternReference
condition|)
block|{
comment|/*            * On the following line, Eclipse wants a type parameter, producing            * WeakInterner<?>.InternReference. The problem is that javac rejects            * that form. Omitting WeakInterner satisfies both, though this seems            * odd, since we are inside a WeakInterner<E> and thus the            * WeakInterner<E> is implied, yet there is no reason to believe that            * the other object's WeakInterner has type E. That's right -- we've            * found a way to perform an unchecked cast without receiving a            * warning from either Eclipse or javac. Taking advantage of that            * seems questionable, even though we don't depend upon the type of            * that.get(), so we'll just suppress the warning.            */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|WeakInterner
operator|.
name|InternReference
name|that
init|=
operator|(
name|WeakInterner
operator|.
name|InternReference
operator|)
name|object
decl_stmt|;
if|if
condition|(
name|that
operator|.
name|hashCode
operator|!=
name|hashCode
condition|)
block|{
return|return
literal|false
return|;
block|}
name|E
name|referent
init|=
name|super
operator|.
name|get
argument_list|()
decl_stmt|;
return|return
name|referent
operator|!=
literal|null
operator|&&
name|referent
operator|.
name|equals
argument_list|(
name|that
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
return|return
name|object
operator|.
name|equals
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
block|}
comment|/**    * Returns a function that delegates to the {@link Interner#intern} method of    * the given interner.    *    * @since 8    */
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
DECL|method|apply (E input)
annotation|@
name|Override
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
DECL|method|hashCode ()
annotation|@
name|Override
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
DECL|method|equals (Object other)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|other
parameter_list|)
block|{
if|if
condition|(
name|other
operator|instanceof
name|InternerFunction
argument_list|<
name|?
argument_list|>
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

