begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.hash
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|hash
package|;
end_package

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
name|Preconditions
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|nio
operator|.
name|charset
operator|.
name|Charset
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
comment|/**  * Funnels for common types. All implementations are serializable.  *  * @author Dimitris Andreou  * @since 11.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|Funnels
specifier|public
specifier|final
class|class
name|Funnels
block|{
DECL|method|Funnels ()
specifier|private
name|Funnels
parameter_list|()
block|{}
comment|/** Returns a funnel that extracts the bytes from a {@code byte} array. */
DECL|method|byteArrayFunnel ()
specifier|public
specifier|static
name|Funnel
argument_list|<
name|byte
index|[]
argument_list|>
name|byteArrayFunnel
parameter_list|()
block|{
return|return
name|ByteArrayFunnel
operator|.
name|INSTANCE
return|;
block|}
DECL|enum|ByteArrayFunnel
specifier|private
enum|enum
name|ByteArrayFunnel
implements|implements
name|Funnel
argument_list|<
name|byte
index|[]
argument_list|>
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
annotation|@
name|Override
DECL|method|funnel (byte[] from, PrimitiveSink into)
specifier|public
name|void
name|funnel
parameter_list|(
name|byte
index|[]
name|from
parameter_list|,
name|PrimitiveSink
name|into
parameter_list|)
block|{
name|into
operator|.
name|putBytes
argument_list|(
name|from
argument_list|)
expr_stmt|;
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
literal|"Funnels.byteArrayFunnel()"
return|;
block|}
block|}
comment|/**    * Returns a funnel that extracts the characters from a {@code CharSequence}, a character at a    * time, without performing any encoding. If you need to use a specific encoding, use {@link    * Funnels#stringFunnel(Charset)} instead.    *    * @since 15.0 (since 11.0 as {@code Funnels.stringFunnel()}.    */
DECL|method|unencodedCharsFunnel ()
specifier|public
specifier|static
name|Funnel
argument_list|<
name|CharSequence
argument_list|>
name|unencodedCharsFunnel
parameter_list|()
block|{
return|return
name|UnencodedCharsFunnel
operator|.
name|INSTANCE
return|;
block|}
DECL|enum|UnencodedCharsFunnel
specifier|private
enum|enum
name|UnencodedCharsFunnel
implements|implements
name|Funnel
argument_list|<
name|CharSequence
argument_list|>
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
annotation|@
name|Override
DECL|method|funnel (CharSequence from, PrimitiveSink into)
specifier|public
name|void
name|funnel
parameter_list|(
name|CharSequence
name|from
parameter_list|,
name|PrimitiveSink
name|into
parameter_list|)
block|{
name|into
operator|.
name|putUnencodedChars
argument_list|(
name|from
argument_list|)
expr_stmt|;
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
literal|"Funnels.unencodedCharsFunnel()"
return|;
block|}
block|}
comment|/**    * Returns a funnel that encodes the characters of a {@code CharSequence} with the specified    * {@code Charset}.    *    * @since 15.0    */
DECL|method|stringFunnel (Charset charset)
specifier|public
specifier|static
name|Funnel
argument_list|<
name|CharSequence
argument_list|>
name|stringFunnel
parameter_list|(
name|Charset
name|charset
parameter_list|)
block|{
return|return
operator|new
name|StringCharsetFunnel
argument_list|(
name|charset
argument_list|)
return|;
block|}
DECL|class|StringCharsetFunnel
specifier|private
specifier|static
class|class
name|StringCharsetFunnel
implements|implements
name|Funnel
argument_list|<
name|CharSequence
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|charset
specifier|private
specifier|final
name|Charset
name|charset
decl_stmt|;
DECL|method|StringCharsetFunnel (Charset charset)
name|StringCharsetFunnel
parameter_list|(
name|Charset
name|charset
parameter_list|)
block|{
name|this
operator|.
name|charset
operator|=
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|charset
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|funnel (CharSequence from, PrimitiveSink into)
specifier|public
name|void
name|funnel
parameter_list|(
name|CharSequence
name|from
parameter_list|,
name|PrimitiveSink
name|into
parameter_list|)
block|{
name|into
operator|.
name|putString
argument_list|(
name|from
argument_list|,
name|charset
argument_list|)
expr_stmt|;
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
literal|"Funnels.stringFunnel("
operator|+
name|charset
operator|.
name|name
argument_list|()
operator|+
literal|")"
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@heckForNull Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|StringCharsetFunnel
condition|)
block|{
name|StringCharsetFunnel
name|funnel
init|=
operator|(
name|StringCharsetFunnel
operator|)
name|o
decl_stmt|;
return|return
name|this
operator|.
name|charset
operator|.
name|equals
argument_list|(
name|funnel
operator|.
name|charset
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
name|StringCharsetFunnel
operator|.
name|class
operator|.
name|hashCode
argument_list|()
operator|^
name|charset
operator|.
name|hashCode
argument_list|()
return|;
block|}
DECL|method|writeReplace ()
name|Object
name|writeReplace
parameter_list|()
block|{
return|return
operator|new
name|SerializedForm
argument_list|(
name|charset
argument_list|)
return|;
block|}
DECL|class|SerializedForm
specifier|private
specifier|static
class|class
name|SerializedForm
implements|implements
name|Serializable
block|{
DECL|field|charsetCanonicalName
specifier|private
specifier|final
name|String
name|charsetCanonicalName
decl_stmt|;
DECL|method|SerializedForm (Charset charset)
name|SerializedForm
parameter_list|(
name|Charset
name|charset
parameter_list|)
block|{
name|this
operator|.
name|charsetCanonicalName
operator|=
name|charset
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
DECL|method|readResolve ()
specifier|private
name|Object
name|readResolve
parameter_list|()
block|{
return|return
name|stringFunnel
argument_list|(
name|Charset
operator|.
name|forName
argument_list|(
name|charsetCanonicalName
argument_list|)
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
literal|0
decl_stmt|;
block|}
block|}
comment|/**    * Returns a funnel for integers.    *    * @since 13.0    */
DECL|method|integerFunnel ()
specifier|public
specifier|static
name|Funnel
argument_list|<
name|Integer
argument_list|>
name|integerFunnel
parameter_list|()
block|{
return|return
name|IntegerFunnel
operator|.
name|INSTANCE
return|;
block|}
DECL|enum|IntegerFunnel
specifier|private
enum|enum
name|IntegerFunnel
implements|implements
name|Funnel
argument_list|<
name|Integer
argument_list|>
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
annotation|@
name|Override
DECL|method|funnel (Integer from, PrimitiveSink into)
specifier|public
name|void
name|funnel
parameter_list|(
name|Integer
name|from
parameter_list|,
name|PrimitiveSink
name|into
parameter_list|)
block|{
name|into
operator|.
name|putInt
argument_list|(
name|from
argument_list|)
expr_stmt|;
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
literal|"Funnels.integerFunnel()"
return|;
block|}
block|}
comment|/**    * Returns a funnel that processes an {@code Iterable} by funneling its elements in iteration    * order with the specified funnel. No separators are added between the elements.    *    * @since 15.0    */
DECL|method|sequentialFunnel ( Funnel<E> elementFunnel)
specifier|public
specifier|static
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
name|Funnel
argument_list|<
name|Iterable
argument_list|<
name|?
extends|extends
name|E
argument_list|>
argument_list|>
name|sequentialFunnel
argument_list|(
name|Funnel
argument_list|<
name|E
argument_list|>
name|elementFunnel
argument_list|)
block|{
return|return
operator|new
name|SequentialFunnel
argument_list|<>
argument_list|(
name|elementFunnel
argument_list|)
return|;
block|}
DECL|class|SequentialFunnel
specifier|private
specifier|static
name|class
name|SequentialFunnel
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
expr|implements
name|Funnel
argument_list|<
name|Iterable
argument_list|<
name|?
extends|extends
name|E
argument_list|>
argument_list|>
operator|,
name|Serializable
block|{
DECL|field|elementFunnel
specifier|private
specifier|final
name|Funnel
argument_list|<
name|E
argument_list|>
name|elementFunnel
decl_stmt|;
DECL|method|SequentialFunnel (Funnel<E> elementFunnel)
name|SequentialFunnel
argument_list|(
name|Funnel
argument_list|<
name|E
argument_list|>
name|elementFunnel
argument_list|)
block|{
name|this
operator|.
name|elementFunnel
operator|=
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|elementFunnel
argument_list|)
block|;     }
expr|@
name|Override
DECL|method|funnel (Iterable<? extends E> from, PrimitiveSink into)
specifier|public
name|void
name|funnel
argument_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|from
operator|,
name|PrimitiveSink
name|into
argument_list|)
block|{
for|for
control|(
name|E
name|e
range|:
name|from
control|)
block|{
name|elementFunnel
operator|.
name|funnel
argument_list|(
name|e
argument_list|,
name|into
argument_list|)
expr_stmt|;
block|}
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
literal|"Funnels.sequentialFunnel("
operator|+
name|elementFunnel
operator|+
literal|")"
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@heckForNull Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|SequentialFunnel
condition|)
block|{
name|SequentialFunnel
argument_list|<
name|?
argument_list|>
name|funnel
init|=
operator|(
name|SequentialFunnel
argument_list|<
name|?
argument_list|>
operator|)
name|o
decl_stmt|;
return|return
name|elementFunnel
operator|.
name|equals
argument_list|(
name|funnel
operator|.
name|elementFunnel
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
name|SequentialFunnel
operator|.
name|class
operator|.
name|hashCode
argument_list|()
operator|^
name|elementFunnel
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
end_class

begin_comment
comment|/**    * Returns a funnel for longs.    *    * @since 13.0    */
end_comment

begin_function
DECL|method|longFunnel ()
specifier|public
specifier|static
name|Funnel
argument_list|<
name|Long
argument_list|>
name|longFunnel
parameter_list|()
block|{
return|return
name|LongFunnel
operator|.
name|INSTANCE
return|;
block|}
end_function

begin_enum
DECL|enum|LongFunnel
specifier|private
enum|enum
name|LongFunnel
implements|implements
name|Funnel
argument_list|<
name|Long
argument_list|>
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
annotation|@
name|Override
DECL|method|funnel (Long from, PrimitiveSink into)
specifier|public
name|void
name|funnel
parameter_list|(
name|Long
name|from
parameter_list|,
name|PrimitiveSink
name|into
parameter_list|)
block|{
name|into
operator|.
name|putLong
argument_list|(
name|from
argument_list|)
expr_stmt|;
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
literal|"Funnels.longFunnel()"
return|;
block|}
block|}
end_enum

begin_comment
comment|/**    * Wraps a {@code PrimitiveSink} as an {@link OutputStream}, so it is easy to {@link Funnel#funnel    * funnel} an object to a {@code PrimitiveSink} if there is already a way to write the contents of    * the object to an {@code OutputStream}.    *    *<p>The {@code close} and {@code flush} methods of the returned {@code OutputStream} do nothing,    * and no method throws {@code IOException}.    *    * @since 13.0    */
end_comment

begin_function
DECL|method|asOutputStream (PrimitiveSink sink)
specifier|public
specifier|static
name|OutputStream
name|asOutputStream
parameter_list|(
name|PrimitiveSink
name|sink
parameter_list|)
block|{
return|return
operator|new
name|SinkAsStream
argument_list|(
name|sink
argument_list|)
return|;
block|}
end_function

begin_class
DECL|class|SinkAsStream
specifier|private
specifier|static
class|class
name|SinkAsStream
extends|extends
name|OutputStream
block|{
DECL|field|sink
specifier|final
name|PrimitiveSink
name|sink
decl_stmt|;
DECL|method|SinkAsStream (PrimitiveSink sink)
name|SinkAsStream
parameter_list|(
name|PrimitiveSink
name|sink
parameter_list|)
block|{
name|this
operator|.
name|sink
operator|=
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|sink
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|write (int b)
specifier|public
name|void
name|write
parameter_list|(
name|int
name|b
parameter_list|)
block|{
name|sink
operator|.
name|putByte
argument_list|(
operator|(
name|byte
operator|)
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|write (byte[] bytes)
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
name|sink
operator|.
name|putBytes
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|write (byte[] bytes, int off, int len)
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
block|{
name|sink
operator|.
name|putBytes
argument_list|(
name|bytes
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
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
literal|"Funnels.asOutputStream("
operator|+
name|sink
operator|+
literal|")"
return|;
block|}
block|}
end_class

unit|}
end_unit

