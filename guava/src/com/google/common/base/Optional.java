begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|com
operator|.
name|google
operator|.
name|errorprone
operator|.
name|annotations
operator|.
name|DoNotMock
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
name|CheckForNull
import|;
end_import

begin_comment
comment|/**  * An immutable object that may contain a non-null reference to another object. Each instance of  * this type either contains a non-null reference, or contains nothing (in which case we say that  * the reference is "absent"); it is never said to "contain {@code null}".  *  *<p>A non-null {@code Optional<T>} reference can be used as a replacement for a nullable {@code T}  * reference. It allows you to represent "a {@code T} that must be present" and a "a {@code T} that  * might be absent" as two distinct types in your program, which can aid clarity.  *  *<p>Some uses of this class include  *  *<ul>  *<li>As a method return type, as an alternative to returning {@code null} to indicate that no  *       value was available  *<li>To distinguish between "unknown" (for example, not present in a map) and "known to have no  *       value" (present in the map, with value {@code Optional.absent()})  *<li>To wrap nullable references for storage in a collection that does not support {@code null}  *       (though there are<a  *       href="https://github.com/google/guava/wiki/LivingWithNullHostileCollections">several other  *       approaches to this</a> that should be considered first)  *</ul>  *  *<p>A common alternative to using this class is to find or create a suitable<a  * href="http://en.wikipedia.org/wiki/Null_Object_pattern">null object</a> for the type in question.  *  *<p>This class is not intended as a direct analogue of any existing "option" or "maybe" construct  * from other programming environments, though it may bear some similarities.  *  *<p><b>Comparison to {@code java.util.Optional} (JDK 8 and higher):</b> A new {@code Optional}  * class was added for Java 8. The two classes are extremely similar, but incompatible (they cannot  * share a common supertype).<i>All</i> known differences are listed either here or with the  * relevant methods below.  *  *<ul>  *<li>This class is serializable; {@code java.util.Optional} is not.  *<li>{@code java.util.Optional} has the additional methods {@code ifPresent}, {@code filter},  *       {@code flatMap}, and {@code orElseThrow}.  *<li>{@code java.util} offers the primitive-specialized versions {@code OptionalInt}, {@code  *       OptionalLong} and {@code OptionalDouble}, the use of which is recommended; Guava does not  *       have these.  *</ul>  *  *<p><b>There are no plans to deprecate this class in the foreseeable future.</b> However, we do  * gently recommend that you prefer the new, standard Java class whenever possible.  *  *<p>See the Guava User Guide article on<a  * href="https://github.com/google/guava/wiki/UsingAndAvoidingNullExplained#optional">using {@code  * Optional}</a>.  *  * @param<T> the type of instance that can be contained. {@code Optional} is naturally covariant on  *     this type, so it is safe to cast an {@code Optional<T>} to {@code Optional<S>} for any  *     supertype {@code S} of {@code T}.  * @author Kurt Alfred Kluever  * @author Kevin Bourrillion  * @since 10.0  */
end_comment

begin_class
annotation|@
name|DoNotMock
argument_list|(
literal|"Use Optional.of(value) or Optional.absent()"
argument_list|)
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
annotation|@
name|ElementTypesAreNonnullByDefault
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
comment|/**    * Returns an {@code Optional} instance with no contained reference.    *    *<p><b>Comparison to {@code java.util.Optional}:</b> this method is equivalent to Java 8's    * {@code Optional.empty}.    */
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
name|Absent
operator|.
name|withType
argument_list|()
return|;
block|}
comment|/**    * Returns an {@code Optional} instance containing the given non-null reference. To have {@code    * null} treated as {@link #absent}, use {@link #fromNullable} instead.    *    *<p><b>Comparison to {@code java.util.Optional}:</b> no differences.    *    * @throws NullPointerException if {@code reference} is null    */
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
comment|/**    * If {@code nullableReference} is non-null, returns an {@code Optional} instance containing that    * reference; otherwise returns {@link Optional#absent}.    *    *<p><b>Comparison to {@code java.util.Optional}:</b> this method is equivalent to Java 8's    * {@code Optional.ofNullable}.    */
DECL|method|fromNullable (@heckForNull T nullableReference)
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
name|CheckForNull
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
comment|/**    * Returns the equivalent {@code com.google.common.base.Optional} value to the given {@code    * java.util.Optional}, or {@code null} if the argument is null.    *    * @since 21.0    */
annotation|@
name|CheckForNull
DECL|method|fromJavaUtil (@heckForNull java.util.Optional<T> javaUtilOptional)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Optional
argument_list|<
name|T
argument_list|>
name|fromJavaUtil
parameter_list|(
annotation|@
name|CheckForNull
name|java
operator|.
name|util
operator|.
name|Optional
argument_list|<
name|T
argument_list|>
name|javaUtilOptional
parameter_list|)
block|{
return|return
operator|(
name|javaUtilOptional
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|fromNullable
argument_list|(
name|javaUtilOptional
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns the equivalent {@code java.util.Optional} value to the given {@code    * com.google.common.base.Optional}, or {@code null} if the argument is null.    *    *<p>If {@code googleOptional} is known to be non-null, use {@code googleOptional.toJavaUtil()}    * instead.    *    *<p>Unfortunately, the method reference {@code Optional::toJavaUtil} will not work, because it    * could refer to either the static or instance version of this method. Write out the lambda    * expression {@code o -> Optional.toJavaUtil(o)} instead.    *    * @since 21.0    */
annotation|@
name|CheckForNull
DECL|method|toJavaUtil (@heckForNull Optional<T> googleOptional)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|java
operator|.
name|util
operator|.
name|Optional
argument_list|<
name|T
argument_list|>
name|toJavaUtil
parameter_list|(
annotation|@
name|CheckForNull
name|Optional
argument_list|<
name|T
argument_list|>
name|googleOptional
parameter_list|)
block|{
return|return
name|googleOptional
operator|==
literal|null
condition|?
literal|null
else|:
name|googleOptional
operator|.
name|toJavaUtil
argument_list|()
return|;
block|}
comment|/**    * Returns the equivalent {@code java.util.Optional} value to this optional.    *    *<p>Unfortunately, the method reference {@code Optional::toJavaUtil} will not work, because it    * could refer to either the static or instance version of this method. Write out the lambda    * expression {@code o -> o.toJavaUtil()} instead.    *    * @since 21.0    */
DECL|method|toJavaUtil ()
specifier|public
name|java
operator|.
name|util
operator|.
name|Optional
argument_list|<
name|T
argument_list|>
name|toJavaUtil
parameter_list|()
block|{
return|return
name|java
operator|.
name|util
operator|.
name|Optional
operator|.
name|ofNullable
argument_list|(
name|orNull
argument_list|()
argument_list|)
return|;
block|}
DECL|method|Optional ()
name|Optional
parameter_list|()
block|{}
comment|/**    * Returns {@code true} if this holder contains a (non-null) instance.    *    *<p><b>Comparison to {@code java.util.Optional}:</b> no differences.    */
DECL|method|isPresent ()
specifier|public
specifier|abstract
name|boolean
name|isPresent
parameter_list|()
function_decl|;
comment|/**    * Returns the contained instance, which must be present. If the instance might be absent, use    * {@link #or(Object)} or {@link #orNull} instead.    *    *<p><b>Comparison to {@code java.util.Optional}:</b> when the value is absent, this method    * throws {@link IllegalStateException}, whereas the Java 8 counterpart throws {@link    * java.util.NoSuchElementException NoSuchElementException}.    *    * @throws IllegalStateException if the instance is absent ({@link #isPresent} returns {@code    *     false}); depending on this<i>specific</i> exception type (over the more general {@link    *     RuntimeException}) is discouraged    */
DECL|method|get ()
specifier|public
specifier|abstract
name|T
name|get
parameter_list|()
function_decl|;
comment|/**    * Returns the contained instance if it is present; {@code defaultValue} otherwise. If no default    * value should be required because the instance is known to be present, use {@link #get()}    * instead. For a default value of {@code null}, use {@link #orNull}.    *    *<p>Note about generics: The signature {@code public T or(T defaultValue)} is overly    * restrictive. However, the ideal signature, {@code public<S super T> S or(S)}, is not legal    * Java. As a result, some sensible operations involving subtypes are compile errors:    *    *<pre>{@code    * Optional<Integer> optionalInt = getSomeOptionalInt();    * Number value = optionalInt.or(0.5); // error    *    * FluentIterable<? extends Number> numbers = getSomeNumbers();    * Optional<? extends Number> first = numbers.first();    * Number value = first.or(0.5); // error    * }</pre>    *    *<p>As a workaround, it is always safe to cast an {@code Optional<? extends T>} to {@code    * Optional<T>}. Casting either of the above example {@code Optional} instances to {@code    * Optional<Number>} (where {@code Number} is the desired output type) solves the problem:    *    *<pre>{@code    * Optional<Number> optionalInt = (Optional) getSomeOptionalInt();    * Number value = optionalInt.or(0.5); // fine    *    * FluentIterable<? extends Number> numbers = getSomeNumbers();    * Optional<Number> first = (Optional) numbers.first();    * Number value = first.or(0.5); // fine    * }</pre>    *    *<p><b>Comparison to {@code java.util.Optional}:</b> this method is similar to Java 8's {@code    * Optional.orElse}, but will not accept {@code null} as a {@code defaultValue} ({@link #orNull}    * must be used instead). As a result, the value returned by this method is guaranteed non-null,    * which is not the case for the {@code java.util} equivalent.    */
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
comment|/**    * Returns this {@code Optional} if it has a value present; {@code secondChoice} otherwise.    *    *<p><b>Comparison to {@code java.util.Optional}:</b> this method has no equivalent in Java 8's    * {@code Optional} class; write {@code thisOptional.isPresent() ? thisOptional : secondChoice}    * instead.    */
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
comment|/**    * Returns the contained instance if it is present; {@code supplier.get()} otherwise.    *    *<p><b>Comparison to {@code java.util.Optional}:</b> this method is similar to Java 8's {@code    * Optional.orElseGet}, except when {@code supplier} returns {@code null}. In this case this    * method throws an exception, whereas the Java 8 method returns the {@code null} to the caller.    *    * @throws NullPointerException if this optional's value is absent and the supplier returns {@code    *     null}    */
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
comment|/**    * Returns the contained instance if it is present; {@code null} otherwise. If the instance is    * known to be present, use {@link #get()} instead.    *    *<p><b>Comparison to {@code java.util.Optional}:</b> this method is equivalent to Java 8's    * {@code Optional.orElse(null)}.    */
annotation|@
name|CheckForNull
DECL|method|orNull ()
specifier|public
specifier|abstract
name|T
name|orNull
parameter_list|()
function_decl|;
comment|/**    * Returns an immutable singleton {@link Set} whose only element is the contained instance if it    * is present; an empty immutable {@link Set} otherwise.    *    *<p><b>Comparison to {@code java.util.Optional}:</b> this method has no equivalent in Java 8's    * {@code Optional} class. However, this common usage:    *    *<pre>{@code    * for (Foo foo : possibleFoo.asSet()) {    *   doSomethingWith(foo);    * }    * }</pre>    *    * ... can be replaced with:    *    *<pre>{@code    * possibleFoo.ifPresent(foo -> doSomethingWith(foo));    * }</pre>    *    *<p><b>Java 9 users:</b> some use cases can be written with calls to {@code optional.stream()}.    *    * @since 11.0    */
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
comment|/**    * If the instance is present, it is transformed with the given {@link Function}; otherwise,    * {@link Optional#absent} is returned.    *    *<p><b>Comparison to {@code java.util.Optional}:</b> this method is similar to Java 8's {@code    * Optional.map}, except when {@code function} returns {@code null}. In this case this method    * throws an exception, whereas the Java 8 method returns {@code Optional.absent()}.    *    * @throws NullPointerException if the function returns {@code null}    * @since 12.0    */
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
comment|/**    * Returns {@code true} if {@code object} is an {@code Optional} instance, and either the    * contained references are {@linkplain Object#equals equal} to each other or both are absent.    * Note that {@code Optional} instances of differing parameterized types can be equal.    *    *<p><b>Comparison to {@code java.util.Optional}:</b> no differences.    */
annotation|@
name|Override
DECL|method|equals (@heckForNull Object object)
specifier|public
specifier|abstract
name|boolean
name|equals
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|object
parameter_list|)
function_decl|;
comment|/**    * Returns a hash code for this instance.    *    *<p><b>Comparison to {@code java.util.Optional}:</b> this class leaves the specific choice of    * hash code unspecified, unlike the Java 8 equivalent.    */
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
specifier|abstract
name|int
name|hashCode
parameter_list|()
function_decl|;
comment|/**    * Returns a string representation for this instance.    *    *<p><b>Comparison to {@code java.util.Optional}:</b> this class leaves the specific string    * representation unspecified, unlike the Java 8 equivalent.    */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
specifier|abstract
name|String
name|toString
parameter_list|()
function_decl|;
comment|/**    * Returns the value of each present instance from the supplied {@code optionals}, in order,    * skipping over occurrences of {@link Optional#absent}. Iterators are unmodifiable and are    * evaluated lazily.    *    *<p><b>Comparison to {@code java.util.Optional}:</b> this method has no equivalent in Java 8's    * {@code Optional} class; use {@code    * optionals.stream().filter(Optional::isPresent).map(Optional::get)} instead.    *    *<p><b>Java 9 users:</b> use {@code optionals.stream().flatMap(Optional::stream)} instead.    *    * @since 11.0 (generics widened in 13.0)    */
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
annotation|@
name|CheckForNull
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

