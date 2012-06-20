begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.base
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
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
name|annotations
operator|.
name|GwtCompatible
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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
comment|/**  * An immutable object that may contain a non-null reference to another object. Each  * instance of this type either contains a non-null reference, or contains nothing (in  * which case we say that the reference is "absent"); it is never said to "contain {@code  * null}".  *  *<p>A non-null {@code Optional<T>} reference can be used as a replacement for a nullable  * {@code T} reference. It allows you to represent "a {@code T} that must be present" and  * a "a {@code T} that might be absent" as two distinct types in your program, which can  * aid clarity.  *  *<p>Some uses of this class include  *  *<ul>  *<li>As a method return type, as an alternative to returning {@code null} to indicate  *     that no value was available  *<li>To distinguish between "unknown" (for example, not present in a map) and "known to  *     have no value" (present in the map, with value {@code Optional.absent()})  *<li>To wrap nullable references for storage in a collection that does not support  *     {@code null} (though there are  *<a href="http://code.google.com/p/guava-libraries/wiki/LivingWithNullHostileCollections">  *     several other approaches to this</a> that should be considered first)  *</ul>  *  *<p>A common alternative to using this class is to find or create a suitable  *<a href="http://en.wikipedia.org/wiki/Null_Object_pattern">null object</a> for the  * type in question.  *  *<p>This class is not intended as a direct analogue of any existing "option" or "maybe"  * construct from other programming environments, though it may bear some similarities.  *  *<p>See the Guava User Guide article on<a  * href="http://code.google.com/p/guava-libraries/wiki/UsingAndAvoidingNullExplained#Optional">  * using {@code Optional}</a>.  *  * @param<T> the type of instance that can be contained. {@code Optional} is naturally  *     covariant on this type, so it is safe to cast an {@code Optional<T>} to {@code  *     Optional<S>} for any supertype {@code S} of {@code T}.  * @author Kurt Alfred Kluever  * @author Kevin Bourrillion  * @since 10.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|class|Optional
specifier|public
specifier|abstract
class|class
name|Optional
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Serializable
block|{
comment|/**    * Returns an {@code Optional} instance with no contained reference.    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|absent ()
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Optional
argument_list|<
name|T
argument_list|>
name|absent
parameter_list|()
block|{
return|return
operator|(
name|Optional
argument_list|<
name|T
argument_list|>
operator|)
name|Absent
operator|.
name|INSTANCE
return|;
block|}
comment|/**    * Returns an {@code Optional} instance containing the given non-null reference.    */
DECL|method|of (T reference)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Optional
argument_list|<
name|T
argument_list|>
name|of
parameter_list|(
name|T
name|reference
parameter_list|)
block|{
return|return
operator|new
name|Present
argument_list|<
name|T
argument_list|>
argument_list|(
name|checkNotNull
argument_list|(
name|reference
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * If {@code nullableReference} is non-null, returns an {@code Optional} instance containing that    * reference; otherwise returns {@link Optional#absent}.    */
DECL|method|fromNullable (@ullable T nullableReference)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Optional
argument_list|<
name|T
argument_list|>
name|fromNullable
parameter_list|(
annotation|@
name|Nullable
name|T
name|nullableReference
parameter_list|)
block|{
return|return
operator|(
name|nullableReference
operator|==
literal|null
operator|)
condition|?
name|Optional
operator|.
expr|<
name|T
operator|>
name|absent
argument_list|()
else|:
operator|new
name|Present
argument_list|<
name|T
argument_list|>
argument_list|(
name|nullableReference
argument_list|)
return|;
block|}
DECL|method|Optional ()
name|Optional
parameter_list|()
block|{}
comment|/**    * Returns {@code true} if this holder contains a (non-null) instance.    */
DECL|method|isPresent ()
specifier|public
specifier|abstract
name|boolean
name|isPresent
parameter_list|()
function_decl|;
comment|/**    * Returns the contained instance, which must be present. If the instance might be    * absent, use {@link #or(Object)} or {@link #orNull} instead.    *    * @throws IllegalStateException if the instance is absent ({@link #isPresent} returns    *     {@code false})    */
DECL|method|get ()
specifier|public
specifier|abstract
name|T
name|get
parameter_list|()
function_decl|;
comment|/**    * Returns the contained instance if it is present; {@code defaultValue} otherwise. If    * no default value should be required because the instance is known to be present, use    * {@link #get()} instead. For a default value of {@code null}, use {@link #orNull}.    *    *<p>Note about generics: The signature {@code public T or(T defaultValue)} is overly    * restrictive. However, the ideal signature, {@code public<S super T> S or(S)}, is not legal    * Java. As a result, some sensible operations involving subtypes are compile errors:    *<pre>   {@code    *    *   Optional<Integer> optionalInt = getSomeOptionalInt();    *   Number value = optionalInt.or(0.5); // error    *    *   FluentIterable<? extends Number> numbers = getSomeNumbers();    *   Optional<? extends Number> first = numbers.first();    *   Number value = first.or(0.5); // error}</pre>    *    * As a workaround, it is always safe to cast an {@code Optional<? extends T>} to {@code    * Optional<T>}. Casting either of the above example {@code Optional} instances to {@code    * Optional<Number>} (where {@code Number} is the desired output type) solves the problem:    *<pre>   {@code    *    *   Optional<Number> optionalInt = (Optional) getSomeOptionalInt();    *   Number value = optionalInt.or(0.5); // fine    *    *   FluentIterable<? extends Number> numbers = getSomeNumbers();    *   Optional<Number> first = (Optional) numbers.first();    *   Number value = first.or(0.5); // fine}</pre>    */
DECL|method|or (T defaultValue)
specifier|public
specifier|abstract
name|T
name|or
parameter_list|(
name|T
name|defaultValue
parameter_list|)
function_decl|;
comment|/**    * Returns this {@code Optional} if it has a value present; {@code secondChoice}    * otherwise.    */
annotation|@
name|Beta
DECL|method|or (Optional<? extends T> secondChoice)
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|T
argument_list|>
name|or
parameter_list|(
name|Optional
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|secondChoice
parameter_list|)
function_decl|;
comment|/**    * Returns the contained instance if it is present; {@code supplier.get()} otherwise. If the    * supplier returns {@code null}, a {@link NullPointerException} is thrown.    *    * @throws NullPointerException if the supplier returns {@code null}    */
annotation|@
name|Beta
DECL|method|or (Supplier<? extends T> supplier)
specifier|public
specifier|abstract
name|T
name|or
parameter_list|(
name|Supplier
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|supplier
parameter_list|)
function_decl|;
comment|/**    * Returns the contained instance if it is present; {@code null} otherwise. If the    * instance is known to be present, use {@link #get()} instead.    */
annotation|@
name|Nullable
DECL|method|orNull ()
specifier|public
specifier|abstract
name|T
name|orNull
parameter_list|()
function_decl|;
comment|/**    * Returns an immutable singleton {@link Set} whose only element is the contained instance    * if it is present; an empty immutable {@link Set} otherwise.    *    * @since 11.0    */
DECL|method|asSet ()
specifier|public
specifier|abstract
name|Set
argument_list|<
name|T
argument_list|>
name|asSet
parameter_list|()
function_decl|;
comment|/**    * If the instance is present, it is transformed with the given {@link Function}; otherwise,    * {@link Optional#absent} is returned. If the function returns {@code null}, a    * {@link NullPointerException} is thrown.    *    * @throws NullPointerException if the function returns {@code null}    *    * @since 12.0    */
annotation|@
name|Beta
DECL|method|transform (Function<? super T, V> function)
specifier|public
specifier|abstract
parameter_list|<
name|V
parameter_list|>
name|Optional
argument_list|<
name|V
argument_list|>
name|transform
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|V
argument_list|>
name|function
parameter_list|)
function_decl|;
comment|/**    * Returns {@code true} if {@code object} is an {@code Optional} instance, and either    * the contained references are {@linkplain Object#equals equal} to each other or both    * are absent. Note that {@code Optional} instances of differing parameterized types can    * be equal.    */
annotation|@
name|Override
DECL|method|equals (@ullable Object object)
specifier|public
specifier|abstract
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
function_decl|;
comment|/**    * Returns a hash code for this instance.    */
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
specifier|abstract
name|int
name|hashCode
parameter_list|()
function_decl|;
comment|/**    * Returns a string representation for this instance. The form of this string    * representation is unspecified.    */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
specifier|abstract
name|String
name|toString
parameter_list|()
function_decl|;
comment|/**    * Returns the value of each present instance from the supplied {@code optionals}, in order,    * skipping over occurrences of {@link Optional#absent}. Iterators are unmodifiable and are    * evaluated lazily.    *    * @since 11.0 (generics widened in 13.0)    */
annotation|@
name|Beta
DECL|method|presentInstances ( final Iterable<? extends Optional<? extends T>> optionals)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Iterable
argument_list|<
name|T
argument_list|>
name|presentInstances
parameter_list|(
specifier|final
name|Iterable
argument_list|<
name|?
extends|extends
name|Optional
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
name|optionals
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|optionals
argument_list|)
expr_stmt|;
return|return
operator|new
name|Iterable
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|T
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|AbstractIterator
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
specifier|private
specifier|final
name|Iterator
argument_list|<
name|?
extends|extends
name|Optional
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
name|iterator
init|=
name|checkNotNull
argument_list|(
name|optionals
operator|.
name|iterator
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|T
name|computeNext
parameter_list|()
block|{
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Optional
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|optional
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|optional
operator|.
name|isPresent
argument_list|()
condition|)
block|{
return|return
name|optional
operator|.
name|get
argument_list|()
return|;
block|}
block|}
return|return
name|endOfData
argument_list|()
return|;
block|}
block|}
return|;
block|}
empty_stmt|;
block|}
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

