begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|ForOverride
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
name|concurrent
operator|.
name|LazyInit
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|j2objc
operator|.
name|annotations
operator|.
name|RetainedWith
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
comment|/**  * A function from {@code A} to {@code B} with an associated<i>reverse</i> function from {@code B}  * to {@code A}; used for converting back and forth between<i>different representations of the same  * information</i>.  *  *<h3>Invertibility</h3>  *  *<p>The reverse operation<b>may</b> be a strict<i>inverse</i> (meaning that {@code  * converter.reverse().convert(converter.convert(a)).equals(a)} is always true). However, it is very  * common (perhaps<i>more</i> common) for round-trip conversion to be<i>lossy</i>. Consider an  * example round-trip using {@link com.google.common.primitives.Doubles#stringConverter}:  *  *<ol>  *<li>{@code stringConverter().convert("1.00")} returns the {@code Double} value {@code 1.0}  *<li>{@code stringConverter().reverse().convert(1.0)} returns the string {@code "1.0"} --  *<i>not</i> the same string ({@code "1.00"}) we started with  *</ol>  *  *<p>Note that it should still be the case that the round-tripped and original objects are  *<i>similar</i>.  *  *<h3>Nullability</h3>  *  *<p>A converter always converts {@code null} to {@code null} and non-null references to non-null  * references. It would not make sense to consider {@code null} and a non-null reference to be  * "different representations of the same information", since one is distinguishable from  *<i>missing</i> information and the other is not. The {@link #convert} method handles this null  * behavior for all converters; implementations of {@link #doForward} and {@link #doBackward} are  * guaranteed to never be passed {@code null}, and must never return {@code null}.  *  *  *<h3>Common ways to use</h3>  *  *<p>Getting a converter:  *  *<ul>  *<li>Use a provided converter implementation, such as {@link Enums#stringConverter}, {@link  *       com.google.common.primitives.Ints#stringConverter Ints.stringConverter} or the {@linkplain  *       #reverse reverse} views of these.  *<li>Convert between specific preset values using {@link  *       com.google.common.collect.Maps#asConverter Maps.asConverter}. For example, use this to  *       create a "fake" converter for a unit test. It is unnecessary (and confusing) to<i>mock</i>  *       the {@code Converter} type using a mocking framework.  *<li>Extend this class and implement its {@link #doForward} and {@link #doBackward} methods.  *<li><b>Java 8 users:</b> you may prefer to pass two lambda expressions or method references to  *       the {@link #from from} factory method.  *</ul>  *  *<p>Using a converter:  *  *<ul>  *<li>Convert one instance in the "forward" direction using {@code converter.convert(a)}.  *<li>Convert multiple instances "forward" using {@code converter.convertAll(as)}.  *<li>Convert in the "backward" direction using {@code converter.reverse().convert(b)} or {@code  *       converter.reverse().convertAll(bs)}.  *<li>Use {@code converter} or {@code converter.reverse()} anywhere a {@link  *       java.util.function.Function} is accepted (for example {@link java.util.stream.Stream#map  *       Stream.map}).  *<li><b>Do not</b> call {@link #doForward} or {@link #doBackward} directly; these exist only to  *       be overridden.  *</ul>  *  *<h3>Example</h3>  *  *<pre>  *   return new Converter&lt;Integer, String&gt;() {  *     protected String doForward(Integer i) {  *       return Integer.toHexString(i);  *     }  *  *     protected Integer doBackward(String s) {  *       return parseUnsignedInt(s, 16);  *     }  *   };</pre>  *  *<p>An alternative using Java 8:  *  *<pre>{@code  * return Converter.from(  *     Integer::toHexString,  *     s -> parseUnsignedInt(s, 16));  * }</pre>  *  * @author Mike Ward  * @author Kurt Alfred Kluever  * @author Gregory Kick  * @since 16.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|Converter
specifier|public
specifier|abstract
class|class
name|Converter
parameter_list|<
name|A
parameter_list|,
name|B
parameter_list|>
implements|implements
name|Function
argument_list|<
name|A
argument_list|,
name|B
argument_list|>
block|{
DECL|field|handleNullAutomatically
specifier|private
specifier|final
name|boolean
name|handleNullAutomatically
decl_stmt|;
comment|// We lazily cache the reverse view to avoid allocating on every call to reverse().
DECL|field|reverse
annotation|@
name|LazyInit
annotation|@
name|NullableDecl
specifier|private
specifier|transient
name|Converter
argument_list|<
name|B
argument_list|,
name|A
argument_list|>
name|reverse
decl_stmt|;
comment|/** Constructor for use by subclasses. */
DECL|method|Converter ()
specifier|protected
name|Converter
parameter_list|()
block|{
name|this
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/** Constructor used only by {@code LegacyConverter} to suspend automatic null-handling. */
DECL|method|Converter (boolean handleNullAutomatically)
name|Converter
parameter_list|(
name|boolean
name|handleNullAutomatically
parameter_list|)
block|{
name|this
operator|.
name|handleNullAutomatically
operator|=
name|handleNullAutomatically
expr_stmt|;
block|}
comment|// SPI methods (what subclasses must implement)
comment|/**    * Returns a representation of {@code a} as an instance of type {@code B}. If {@code a} cannot be    * converted, an unchecked exception (such as {@link IllegalArgumentException}) should be thrown.    *    * @param a the instance to convert; will never be null    * @return the converted instance;<b>must not</b> be null    */
annotation|@
name|ForOverride
DECL|method|doForward (A a)
specifier|protected
specifier|abstract
name|B
name|doForward
parameter_list|(
name|A
name|a
parameter_list|)
function_decl|;
comment|/**    * Returns a representation of {@code b} as an instance of type {@code A}. If {@code b} cannot be    * converted, an unchecked exception (such as {@link IllegalArgumentException}) should be thrown.    *    * @param b the instance to convert; will never be null    * @return the converted instance;<b>must not</b> be null    * @throws UnsupportedOperationException if backward conversion is not implemented; this should be    *     very rare. Note that if backward conversion is not only unimplemented but    *     unimplement<i>able</i> (for example, consider a {@code Converter<Chicken, ChickenNugget>}),    *     then this is not logically a {@code Converter} at all, and should just implement {@link    *     Function}.    */
annotation|@
name|ForOverride
DECL|method|doBackward (B b)
specifier|protected
specifier|abstract
name|A
name|doBackward
parameter_list|(
name|B
name|b
parameter_list|)
function_decl|;
comment|// API (consumer-side) methods
comment|/**    * Returns a representation of {@code a} as an instance of type {@code B}.    *    * @return the converted value; is null<i>if and only if</i> {@code a} is null    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|NullableDecl
DECL|method|convert (@ullableDecl A a)
specifier|public
specifier|final
name|B
name|convert
parameter_list|(
annotation|@
name|NullableDecl
name|A
name|a
parameter_list|)
block|{
return|return
name|correctedDoForward
argument_list|(
name|a
argument_list|)
return|;
block|}
annotation|@
name|NullableDecl
DECL|method|correctedDoForward (@ullableDecl A a)
name|B
name|correctedDoForward
parameter_list|(
annotation|@
name|NullableDecl
name|A
name|a
parameter_list|)
block|{
if|if
condition|(
name|handleNullAutomatically
condition|)
block|{
comment|// TODO(kevinb): we shouldn't be checking for a null result at runtime. Assert?
return|return
name|a
operator|==
literal|null
condition|?
literal|null
else|:
name|checkNotNull
argument_list|(
name|doForward
argument_list|(
name|a
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|doForward
argument_list|(
name|a
argument_list|)
return|;
block|}
block|}
annotation|@
name|NullableDecl
DECL|method|correctedDoBackward (@ullableDecl B b)
name|A
name|correctedDoBackward
parameter_list|(
annotation|@
name|NullableDecl
name|B
name|b
parameter_list|)
block|{
if|if
condition|(
name|handleNullAutomatically
condition|)
block|{
comment|// TODO(kevinb): we shouldn't be checking for a null result at runtime. Assert?
return|return
name|b
operator|==
literal|null
condition|?
literal|null
else|:
name|checkNotNull
argument_list|(
name|doBackward
argument_list|(
name|b
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|doBackward
argument_list|(
name|b
argument_list|)
return|;
block|}
block|}
comment|/**    * Returns an iterable that applies {@code convert} to each element of {@code fromIterable}. The    * conversion is done lazily.    *    *<p>The returned iterable's iterator supports {@code remove()} if the input iterator does. After    * a successful {@code remove()} call, {@code fromIterable} no longer contains the corresponding    * element.    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|convertAll (final Iterable<? extends A> fromIterable)
specifier|public
name|Iterable
argument_list|<
name|B
argument_list|>
name|convertAll
parameter_list|(
specifier|final
name|Iterable
argument_list|<
name|?
extends|extends
name|A
argument_list|>
name|fromIterable
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|fromIterable
argument_list|,
literal|"fromIterable"
argument_list|)
expr_stmt|;
return|return
operator|new
name|Iterable
argument_list|<
name|B
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|B
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|Iterator
argument_list|<
name|B
argument_list|>
argument_list|()
block|{
specifier|private
specifier|final
name|Iterator
argument_list|<
name|?
extends|extends
name|A
argument_list|>
name|fromIterator
init|=
name|fromIterable
operator|.
name|iterator
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|fromIterator
operator|.
name|hasNext
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|B
name|next
parameter_list|()
block|{
return|return
name|convert
argument_list|(
name|fromIterator
operator|.
name|next
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|fromIterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
return|;
block|}
comment|/**    * Returns the reversed view of this converter, which converts {@code this.convert(a)} back to a    * value roughly equivalent to {@code a}.    *    *<p>The returned converter is serializable if {@code this} converter is.    *    *<p><b>Note:</b> you should not override this method. It is non-final for legacy reasons.    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|reverse ()
specifier|public
name|Converter
argument_list|<
name|B
argument_list|,
name|A
argument_list|>
name|reverse
parameter_list|()
block|{
name|Converter
argument_list|<
name|B
argument_list|,
name|A
argument_list|>
name|result
init|=
name|reverse
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
name|reverse
operator|=
operator|new
name|ReverseConverter
argument_list|<>
argument_list|(
name|this
argument_list|)
else|:
name|result
return|;
block|}
DECL|class|ReverseConverter
specifier|private
specifier|static
specifier|final
class|class
name|ReverseConverter
parameter_list|<
name|A
parameter_list|,
name|B
parameter_list|>
extends|extends
name|Converter
argument_list|<
name|B
argument_list|,
name|A
argument_list|>
implements|implements
name|Serializable
block|{
DECL|field|original
annotation|@
name|RetainedWith
specifier|final
name|Converter
argument_list|<
name|A
argument_list|,
name|B
argument_list|>
name|original
decl_stmt|;
DECL|method|ReverseConverter (Converter<A, B> original)
name|ReverseConverter
parameter_list|(
name|Converter
argument_list|<
name|A
argument_list|,
name|B
argument_list|>
name|original
parameter_list|)
block|{
name|this
operator|.
name|original
operator|=
name|original
expr_stmt|;
block|}
comment|/*      * These gymnastics are a little confusing. Basically this class has neither legacy nor      * non-legacy behavior; it just needs to let the behavior of the backing converter shine      * through. So, we override the correctedDo* methods, after which the do* methods should never      * be reached.      */
annotation|@
name|Override
DECL|method|doForward (B b)
specifier|protected
name|A
name|doForward
parameter_list|(
name|B
name|b
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|doBackward (A a)
specifier|protected
name|B
name|doBackward
parameter_list|(
name|A
name|a
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
annotation|@
name|Override
annotation|@
name|NullableDecl
DECL|method|correctedDoForward (@ullableDecl B b)
name|A
name|correctedDoForward
parameter_list|(
annotation|@
name|NullableDecl
name|B
name|b
parameter_list|)
block|{
return|return
name|original
operator|.
name|correctedDoBackward
argument_list|(
name|b
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|NullableDecl
DECL|method|correctedDoBackward (@ullableDecl A a)
name|B
name|correctedDoBackward
parameter_list|(
annotation|@
name|NullableDecl
name|A
name|a
parameter_list|)
block|{
return|return
name|original
operator|.
name|correctedDoForward
argument_list|(
name|a
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|reverse ()
specifier|public
name|Converter
argument_list|<
name|A
argument_list|,
name|B
argument_list|>
name|reverse
parameter_list|()
block|{
return|return
name|original
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
name|ReverseConverter
condition|)
block|{
name|ReverseConverter
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|that
init|=
operator|(
name|ReverseConverter
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
return|return
name|this
operator|.
name|original
operator|.
name|equals
argument_list|(
name|that
operator|.
name|original
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
operator|~
name|original
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
name|original
operator|+
literal|".reverse()"
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0L
decl_stmt|;
block|}
comment|/**    * Returns a converter whose {@code convert} method applies {@code secondConverter} to the result    * of this converter. Its {@code reverse} method applies the converters in reverse order.    *    *<p>The returned converter is serializable if {@code this} converter and {@code secondConverter}    * are.    */
DECL|method|andThen (Converter<B, C> secondConverter)
specifier|public
specifier|final
parameter_list|<
name|C
parameter_list|>
name|Converter
argument_list|<
name|A
argument_list|,
name|C
argument_list|>
name|andThen
parameter_list|(
name|Converter
argument_list|<
name|B
argument_list|,
name|C
argument_list|>
name|secondConverter
parameter_list|)
block|{
return|return
name|doAndThen
argument_list|(
name|secondConverter
argument_list|)
return|;
block|}
comment|/** Package-private non-final implementation of andThen() so only we can override it. */
DECL|method|doAndThen (Converter<B, C> secondConverter)
parameter_list|<
name|C
parameter_list|>
name|Converter
argument_list|<
name|A
argument_list|,
name|C
argument_list|>
name|doAndThen
parameter_list|(
name|Converter
argument_list|<
name|B
argument_list|,
name|C
argument_list|>
name|secondConverter
parameter_list|)
block|{
return|return
operator|new
name|ConverterComposition
argument_list|<>
argument_list|(
name|this
argument_list|,
name|checkNotNull
argument_list|(
name|secondConverter
argument_list|)
argument_list|)
return|;
block|}
DECL|class|ConverterComposition
specifier|private
specifier|static
specifier|final
class|class
name|ConverterComposition
parameter_list|<
name|A
parameter_list|,
name|B
parameter_list|,
name|C
parameter_list|>
extends|extends
name|Converter
argument_list|<
name|A
argument_list|,
name|C
argument_list|>
implements|implements
name|Serializable
block|{
DECL|field|first
specifier|final
name|Converter
argument_list|<
name|A
argument_list|,
name|B
argument_list|>
name|first
decl_stmt|;
DECL|field|second
specifier|final
name|Converter
argument_list|<
name|B
argument_list|,
name|C
argument_list|>
name|second
decl_stmt|;
DECL|method|ConverterComposition (Converter<A, B> first, Converter<B, C> second)
name|ConverterComposition
parameter_list|(
name|Converter
argument_list|<
name|A
argument_list|,
name|B
argument_list|>
name|first
parameter_list|,
name|Converter
argument_list|<
name|B
argument_list|,
name|C
argument_list|>
name|second
parameter_list|)
block|{
name|this
operator|.
name|first
operator|=
name|first
expr_stmt|;
name|this
operator|.
name|second
operator|=
name|second
expr_stmt|;
block|}
comment|/*      * These gymnastics are a little confusing. Basically this class has neither legacy nor      * non-legacy behavior; it just needs to let the behaviors of the backing converters shine      * through (which might even differ from each other!). So, we override the correctedDo* methods,      * after which the do* methods should never be reached.      */
annotation|@
name|Override
DECL|method|doForward (A a)
specifier|protected
name|C
name|doForward
parameter_list|(
name|A
name|a
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|doBackward (C c)
specifier|protected
name|A
name|doBackward
parameter_list|(
name|C
name|c
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
annotation|@
name|Override
annotation|@
name|NullableDecl
DECL|method|correctedDoForward (@ullableDecl A a)
name|C
name|correctedDoForward
parameter_list|(
annotation|@
name|NullableDecl
name|A
name|a
parameter_list|)
block|{
return|return
name|second
operator|.
name|correctedDoForward
argument_list|(
name|first
operator|.
name|correctedDoForward
argument_list|(
name|a
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|NullableDecl
DECL|method|correctedDoBackward (@ullableDecl C c)
name|A
name|correctedDoBackward
parameter_list|(
annotation|@
name|NullableDecl
name|C
name|c
parameter_list|)
block|{
return|return
name|first
operator|.
name|correctedDoBackward
argument_list|(
name|second
operator|.
name|correctedDoBackward
argument_list|(
name|c
argument_list|)
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
name|ConverterComposition
condition|)
block|{
name|ConverterComposition
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
name|that
init|=
operator|(
name|ConverterComposition
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
return|return
name|this
operator|.
name|first
operator|.
name|equals
argument_list|(
name|that
operator|.
name|first
argument_list|)
operator|&&
name|this
operator|.
name|second
operator|.
name|equals
argument_list|(
name|that
operator|.
name|second
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
literal|31
operator|*
name|first
operator|.
name|hashCode
argument_list|()
operator|+
name|second
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
name|first
operator|+
literal|".andThen("
operator|+
name|second
operator|+
literal|")"
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0L
decl_stmt|;
block|}
comment|/**    * @deprecated Provided to satisfy the {@code Function} interface; use {@link #convert} instead.    */
annotation|@
name|Deprecated
annotation|@
name|Override
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|NullableDecl
DECL|method|apply (@ullableDecl A a)
specifier|public
specifier|final
name|B
name|apply
parameter_list|(
annotation|@
name|NullableDecl
name|A
name|a
parameter_list|)
block|{
return|return
name|convert
argument_list|(
name|a
argument_list|)
return|;
block|}
comment|/**    * Indicates whether another object is equal to this converter.    *    *<p>Most implementations will have no reason to override the behavior of {@link Object#equals}.    * However, an implementation may also choose to return {@code true} whenever {@code object} is a    * {@link Converter} that it considers<i>interchangeable</i> with this one. "Interchangeable"    *<i>typically</i> means that {@code Objects.equal(this.convert(a), that.convert(a))} is true for    * all {@code a} of type {@code A} (and similarly for {@code reverse}). Note that a {@code false}    * result from this method does not imply that the converters are known<i>not</i> to be    * interchangeable.    */
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
return|return
name|super
operator|.
name|equals
argument_list|(
name|object
argument_list|)
return|;
block|}
comment|// Static converters
comment|/**    * Returns a converter based on separate forward and backward functions. This is useful if the    * function instances already exist, or so that you can supply lambda expressions. If those    * circumstances don't apply, you probably don't need to use this; subclass {@code Converter} and    * implement its {@link #doForward} and {@link #doBackward} methods directly.    *    *<p>These functions will never be passed {@code null} and must not under any circumstances    * return {@code null}. If a value cannot be converted, the function should throw an unchecked    * exception (typically, but not necessarily, {@link IllegalArgumentException}).    *    *<p>The returned converter is serializable if both provided functions are.    *    * @since 17.0    */
DECL|method|from ( Function<? super A, ? extends B> forwardFunction, Function<? super B, ? extends A> backwardFunction)
specifier|public
specifier|static
parameter_list|<
name|A
parameter_list|,
name|B
parameter_list|>
name|Converter
argument_list|<
name|A
argument_list|,
name|B
argument_list|>
name|from
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|A
argument_list|,
name|?
extends|extends
name|B
argument_list|>
name|forwardFunction
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|B
argument_list|,
name|?
extends|extends
name|A
argument_list|>
name|backwardFunction
parameter_list|)
block|{
return|return
operator|new
name|FunctionBasedConverter
argument_list|<>
argument_list|(
name|forwardFunction
argument_list|,
name|backwardFunction
argument_list|)
return|;
block|}
DECL|class|FunctionBasedConverter
specifier|private
specifier|static
specifier|final
class|class
name|FunctionBasedConverter
parameter_list|<
name|A
parameter_list|,
name|B
parameter_list|>
extends|extends
name|Converter
argument_list|<
name|A
argument_list|,
name|B
argument_list|>
implements|implements
name|Serializable
block|{
DECL|field|forwardFunction
specifier|private
specifier|final
name|Function
argument_list|<
name|?
super|super
name|A
argument_list|,
name|?
extends|extends
name|B
argument_list|>
name|forwardFunction
decl_stmt|;
DECL|field|backwardFunction
specifier|private
specifier|final
name|Function
argument_list|<
name|?
super|super
name|B
argument_list|,
name|?
extends|extends
name|A
argument_list|>
name|backwardFunction
decl_stmt|;
DECL|method|FunctionBasedConverter ( Function<? super A, ? extends B> forwardFunction, Function<? super B, ? extends A> backwardFunction)
specifier|private
name|FunctionBasedConverter
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|A
argument_list|,
name|?
extends|extends
name|B
argument_list|>
name|forwardFunction
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|B
argument_list|,
name|?
extends|extends
name|A
argument_list|>
name|backwardFunction
parameter_list|)
block|{
name|this
operator|.
name|forwardFunction
operator|=
name|checkNotNull
argument_list|(
name|forwardFunction
argument_list|)
expr_stmt|;
name|this
operator|.
name|backwardFunction
operator|=
name|checkNotNull
argument_list|(
name|backwardFunction
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doForward (A a)
specifier|protected
name|B
name|doForward
parameter_list|(
name|A
name|a
parameter_list|)
block|{
return|return
name|forwardFunction
operator|.
name|apply
argument_list|(
name|a
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doBackward (B b)
specifier|protected
name|A
name|doBackward
parameter_list|(
name|B
name|b
parameter_list|)
block|{
return|return
name|backwardFunction
operator|.
name|apply
argument_list|(
name|b
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
name|FunctionBasedConverter
condition|)
block|{
name|FunctionBasedConverter
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|that
init|=
operator|(
name|FunctionBasedConverter
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
return|return
name|this
operator|.
name|forwardFunction
operator|.
name|equals
argument_list|(
name|that
operator|.
name|forwardFunction
argument_list|)
operator|&&
name|this
operator|.
name|backwardFunction
operator|.
name|equals
argument_list|(
name|that
operator|.
name|backwardFunction
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
name|forwardFunction
operator|.
name|hashCode
argument_list|()
operator|*
literal|31
operator|+
name|backwardFunction
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
literal|"Converter.from("
operator|+
name|forwardFunction
operator|+
literal|", "
operator|+
name|backwardFunction
operator|+
literal|")"
return|;
block|}
block|}
comment|/** Returns a serializable converter that always converts or reverses an object to itself. */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// implementation is "fully variant"
DECL|method|identity ()
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Converter
argument_list|<
name|T
argument_list|,
name|T
argument_list|>
name|identity
parameter_list|()
block|{
return|return
operator|(
name|IdentityConverter
argument_list|<
name|T
argument_list|>
operator|)
name|IdentityConverter
operator|.
name|INSTANCE
return|;
block|}
comment|/**    * A converter that always converts or reverses an object to itself. Note that T is now a    * "pass-through type".    */
DECL|class|IdentityConverter
specifier|private
specifier|static
specifier|final
class|class
name|IdentityConverter
parameter_list|<
name|T
parameter_list|>
extends|extends
name|Converter
argument_list|<
name|T
argument_list|,
name|T
argument_list|>
implements|implements
name|Serializable
block|{
DECL|field|INSTANCE
specifier|static
specifier|final
name|IdentityConverter
argument_list|<
name|?
argument_list|>
name|INSTANCE
init|=
operator|new
name|IdentityConverter
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|doForward (T t)
specifier|protected
name|T
name|doForward
parameter_list|(
name|T
name|t
parameter_list|)
block|{
return|return
name|t
return|;
block|}
annotation|@
name|Override
DECL|method|doBackward (T t)
specifier|protected
name|T
name|doBackward
parameter_list|(
name|T
name|t
parameter_list|)
block|{
return|return
name|t
return|;
block|}
annotation|@
name|Override
DECL|method|reverse ()
specifier|public
name|IdentityConverter
argument_list|<
name|T
argument_list|>
name|reverse
parameter_list|()
block|{
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|doAndThen (Converter<T, S> otherConverter)
argument_list|<
name|S
argument_list|>
name|Converter
argument_list|<
name|T
argument_list|,
name|S
argument_list|>
name|doAndThen
parameter_list|(
name|Converter
argument_list|<
name|T
argument_list|,
name|S
argument_list|>
name|otherConverter
parameter_list|)
block|{
return|return
name|checkNotNull
argument_list|(
name|otherConverter
argument_list|,
literal|"otherConverter"
argument_list|)
return|;
block|}
comment|/*      * We *could* override convertAll() to return its input, but it's a rather pointless      * optimization and opened up a weird type-safety problem.      */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Converter.identity()"
return|;
block|}
DECL|method|readResolve ()
specifier|private
name|Object
name|readResolve
parameter_list|()
block|{
return|return
name|INSTANCE
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0L
decl_stmt|;
block|}
block|}
end_class

end_unit

