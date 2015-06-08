begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|javax
operator|.
name|annotation
operator|.
name|CheckReturnValue
import|;
end_import

begin_comment
comment|/**  * Static utility methods pertaining to {@code byte} primitives that  * interpret values as signed. The corresponding methods that treat the values  * as unsigned are found in {@link UnsignedBytes}, and the methods for which  * signedness is not an issue are in {@link Bytes}.  *  *<p>See the Guava User Guide article on<a href=  * "https://github.com/google/guava/wiki/PrimitivesExplained">  * primitive utilities</a>.  *  * @author Kevin Bourrillion  * @since 1.0  */
end_comment

begin_comment
comment|// TODO(kevinb): how to prevent warning on UnsignedBytes when building GWT
end_comment

begin_comment
comment|// javadoc?
end_comment

begin_class
annotation|@
name|CheckReturnValue
annotation|@
name|GwtCompatible
DECL|class|SignedBytes
specifier|public
specifier|final
class|class
name|SignedBytes
block|{
DECL|method|SignedBytes ()
specifier|private
name|SignedBytes
parameter_list|()
block|{}
comment|/**    * The largest power of two that can be represented as a signed {@code byte}.    *    * @since 10.0    */
DECL|field|MAX_POWER_OF_TWO
specifier|public
specifier|static
specifier|final
name|byte
name|MAX_POWER_OF_TWO
init|=
literal|1
operator|<<
literal|6
decl_stmt|;
comment|/**    * Returns the {@code byte} value that is equal to {@code value}, if possible.    *    * @param value any value in the range of the {@code byte} type    * @return the {@code byte} value that equals {@code value}    * @throws IllegalArgumentException if {@code value} is greater than {@link    *     Byte#MAX_VALUE} or less than {@link Byte#MIN_VALUE}    */
DECL|method|checkedCast (long value)
specifier|public
specifier|static
name|byte
name|checkedCast
parameter_list|(
name|long
name|value
parameter_list|)
block|{
name|byte
name|result
init|=
operator|(
name|byte
operator|)
name|value
decl_stmt|;
if|if
condition|(
name|result
operator|!=
name|value
condition|)
block|{
comment|// don't use checkArgument here, to avoid boxing
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Out of range: "
operator|+
name|value
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
comment|/**    * Returns the {@code byte} nearest in value to {@code value}.    *    * @param value any {@code long} value    * @return the same value cast to {@code byte} if it is in the range of the    *     {@code byte} type, {@link Byte#MAX_VALUE} if it is too large,    *     or {@link Byte#MIN_VALUE} if it is too small    */
DECL|method|saturatedCast (long value)
specifier|public
specifier|static
name|byte
name|saturatedCast
parameter_list|(
name|long
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|>
name|Byte
operator|.
name|MAX_VALUE
condition|)
block|{
return|return
name|Byte
operator|.
name|MAX_VALUE
return|;
block|}
if|if
condition|(
name|value
operator|<
name|Byte
operator|.
name|MIN_VALUE
condition|)
block|{
return|return
name|Byte
operator|.
name|MIN_VALUE
return|;
block|}
return|return
operator|(
name|byte
operator|)
name|value
return|;
block|}
comment|/**    * Compares the two specified {@code byte} values. The sign of the value    * returned is the same as that of {@code ((Byte) a).compareTo(b)}.    *    *<p><b>Note:</b> this method behaves identically to the JDK 7 method {@link    * Byte#compare}.    *    * @param a the first {@code byte} to compare    * @param b the second {@code byte} to compare    * @return a negative value if {@code a} is less than {@code b}; a positive    *     value if {@code a} is greater than {@code b}; or zero if they are equal    */
comment|// TODO(kevinb): if Ints.compare etc. are ever removed, *maybe* remove this
comment|// one too, which would leave compare methods only on the Unsigned* classes.
DECL|method|compare (byte a, byte b)
specifier|public
specifier|static
name|int
name|compare
parameter_list|(
name|byte
name|a
parameter_list|,
name|byte
name|b
parameter_list|)
block|{
return|return
name|a
operator|-
name|b
return|;
comment|// safe due to restricted range
block|}
comment|/**    * Returns the least value present in {@code array}.    *    * @param array a<i>nonempty</i> array of {@code byte} values    * @return the value present in {@code array} that is less than or equal to    *     every other value in the array    * @throws IllegalArgumentException if {@code array} is empty    */
DECL|method|min (byte... array)
specifier|public
specifier|static
name|byte
name|min
parameter_list|(
name|byte
modifier|...
name|array
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|array
operator|.
name|length
operator|>
literal|0
argument_list|)
expr_stmt|;
name|byte
name|min
init|=
name|array
index|[
literal|0
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|array
operator|.
name|length
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
operator|<
name|min
condition|)
block|{
name|min
operator|=
name|array
index|[
name|i
index|]
expr_stmt|;
block|}
block|}
return|return
name|min
return|;
block|}
comment|/**    * Returns the greatest value present in {@code array}.    *    * @param array a<i>nonempty</i> array of {@code byte} values    * @return the value present in {@code array} that is greater than or equal to    *     every other value in the array    * @throws IllegalArgumentException if {@code array} is empty    */
DECL|method|max (byte... array)
specifier|public
specifier|static
name|byte
name|max
parameter_list|(
name|byte
modifier|...
name|array
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|array
operator|.
name|length
operator|>
literal|0
argument_list|)
expr_stmt|;
name|byte
name|max
init|=
name|array
index|[
literal|0
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|array
operator|.
name|length
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
operator|>
name|max
condition|)
block|{
name|max
operator|=
name|array
index|[
name|i
index|]
expr_stmt|;
block|}
block|}
return|return
name|max
return|;
block|}
comment|/**    * Returns a string containing the supplied {@code byte} values separated    * by {@code separator}. For example, {@code join(":", 0x01, 0x02, -0x01)}    * returns the string {@code "1:2:-1"}.    *    * @param separator the text that should appear between consecutive values in    *     the resulting string (but not at the start or end)    * @param array an array of {@code byte} values, possibly empty    */
DECL|method|join (String separator, byte... array)
specifier|public
specifier|static
name|String
name|join
parameter_list|(
name|String
name|separator
parameter_list|,
name|byte
modifier|...
name|array
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|separator
argument_list|)
expr_stmt|;
if|if
condition|(
name|array
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|""
return|;
block|}
comment|// For pre-sizing a builder, just get the right order of magnitude
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|(
name|array
operator|.
name|length
operator|*
literal|5
argument_list|)
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|array
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|array
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|separator
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
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**    * Returns a comparator that compares two {@code byte} arrays    * lexicographically. That is, it compares, using {@link    * #compare(byte, byte)}), the first pair of values that follow any common    * prefix, or when one array is a prefix of the other, treats the shorter    * array as the lesser. For example, {@code []< [0x01]< [0x01, 0x80]<    * [0x01, 0x7F]< [0x02]}. Values are treated as signed.    *    *<p>The returned comparator is inconsistent with {@link    * Object#equals(Object)} (since arrays support only identity equality), but    * it is consistent with {@link java.util.Arrays#equals(byte[], byte[])}.    *    * @see<a href="http://en.wikipedia.org/wiki/Lexicographical_order">    *     Lexicographical order article at Wikipedia</a>    * @since 2.0    */
DECL|method|lexicographicalComparator ()
specifier|public
specifier|static
name|Comparator
argument_list|<
name|byte
index|[]
argument_list|>
name|lexicographicalComparator
parameter_list|()
block|{
return|return
name|LexicographicalComparator
operator|.
name|INSTANCE
return|;
block|}
DECL|enum|LexicographicalComparator
specifier|private
enum|enum
name|LexicographicalComparator
implements|implements
name|Comparator
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
DECL|method|compare (byte[] left, byte[] right)
specifier|public
name|int
name|compare
parameter_list|(
name|byte
index|[]
name|left
parameter_list|,
name|byte
index|[]
name|right
parameter_list|)
block|{
name|int
name|minLength
init|=
name|Math
operator|.
name|min
argument_list|(
name|left
operator|.
name|length
argument_list|,
name|right
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|minLength
condition|;
name|i
operator|++
control|)
block|{
name|int
name|result
init|=
name|SignedBytes
operator|.
name|compare
argument_list|(
name|left
index|[
name|i
index|]
argument_list|,
name|right
index|[
name|i
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|!=
literal|0
condition|)
block|{
return|return
name|result
return|;
block|}
block|}
return|return
name|left
operator|.
name|length
operator|-
name|right
operator|.
name|length
return|;
block|}
block|}
block|}
end_class

end_unit

