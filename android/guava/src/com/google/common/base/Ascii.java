begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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

begin_comment
comment|/**  * Static methods pertaining to ASCII characters (those in the range of values {@code 0x00} through  * {@code 0x7F}), and to strings containing such characters.  *  *<p>ASCII utilities also exist in other classes of this package:  *  *<ul>  *<!-- TODO(kevinb): how can we make this not produce a warning when building gwt javadoc? -->  *<li>{@link Charsets#US_ASCII} specifies the {@code Charset} of ASCII characters.  *<li>{@link CharMatcher#ascii} matches ASCII characters and provides text processing methods  *       which operate only on the ASCII characters of a string.  *</ul>  *  * @author Craig Berry  * @author Gregory Kick  * @since 7.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|Ascii
specifier|public
specifier|final
class|class
name|Ascii
block|{
DECL|method|Ascii ()
specifier|private
name|Ascii
parameter_list|()
block|{}
comment|/* The ASCII control characters, per RFC 20. */
comment|/**    * Null ('\0'): The all-zeros character which may serve to accomplish time fill and media fill.    * Normally used as a C string terminator.    *    *<p>Although RFC 20 names this as "Null", note that it is distinct from the C/C++ "NULL"    * pointer.    *    * @since 8.0    */
DECL|field|NUL
specifier|public
specifier|static
specifier|final
name|byte
name|NUL
init|=
literal|0
decl_stmt|;
comment|/**    * Start of Heading: A communication control character used at the beginning of a sequence of    * characters which constitute a machine-sensible address or routing information. Such a sequence    * is referred to as the "heading." An STX character has the effect of terminating a heading.    *    * @since 8.0    */
DECL|field|SOH
specifier|public
specifier|static
specifier|final
name|byte
name|SOH
init|=
literal|1
decl_stmt|;
comment|/**    * Start of Text: A communication control character which precedes a sequence of characters that    * is to be treated as an entity and entirely transmitted through to the ultimate destination.    * Such a sequence is referred to as "text." STX may be used to terminate a sequence of characters    * started by SOH.    *    * @since 8.0    */
DECL|field|STX
specifier|public
specifier|static
specifier|final
name|byte
name|STX
init|=
literal|2
decl_stmt|;
comment|/**    * End of Text: A communication control character used to terminate a sequence of characters    * started with STX and transmitted as an entity.    *    * @since 8.0    */
DECL|field|ETX
specifier|public
specifier|static
specifier|final
name|byte
name|ETX
init|=
literal|3
decl_stmt|;
comment|/**    * End of Transmission: A communication control character used to indicate the conclusion of a    * transmission, which may have contained one or more texts and any associated headings.    *    * @since 8.0    */
DECL|field|EOT
specifier|public
specifier|static
specifier|final
name|byte
name|EOT
init|=
literal|4
decl_stmt|;
comment|/**    * Enquiry: A communication control character used in data communication systems as a request for    * a response from a remote station. It may be used as a "Who Are You" (WRU) to obtain    * identification, or may be used to obtain station status, or both.    *    * @since 8.0    */
DECL|field|ENQ
specifier|public
specifier|static
specifier|final
name|byte
name|ENQ
init|=
literal|5
decl_stmt|;
comment|/**    * Acknowledge: A communication control character transmitted by a receiver as an affirmative    * response to a sender.    *    * @since 8.0    */
DECL|field|ACK
specifier|public
specifier|static
specifier|final
name|byte
name|ACK
init|=
literal|6
decl_stmt|;
comment|/**    * Bell ('\a'): A character for use when there is a need to call for human attention. It may    * control alarm or attention devices.    *    * @since 8.0    */
DECL|field|BEL
specifier|public
specifier|static
specifier|final
name|byte
name|BEL
init|=
literal|7
decl_stmt|;
comment|/**    * Backspace ('\b'): A format effector which controls the movement of the printing position one    * printing space backward on the same printing line. (Applicable also to display devices.)    *    * @since 8.0    */
DECL|field|BS
specifier|public
specifier|static
specifier|final
name|byte
name|BS
init|=
literal|8
decl_stmt|;
comment|/**    * Horizontal Tabulation ('\t'): A format effector which controls the movement of the printing    * position to the next in a series of predetermined positions along the printing line.    * (Applicable also to display devices and the skip function on punched cards.)    *    * @since 8.0    */
DECL|field|HT
specifier|public
specifier|static
specifier|final
name|byte
name|HT
init|=
literal|9
decl_stmt|;
comment|/**    * Line Feed ('\n'): A format effector which controls the movement of the printing position to the    * next printing line. (Applicable also to display devices.) Where appropriate, this character may    * have the meaning "New Line" (NL), a format effector which controls the movement of the printing    * point to the first printing position on the next printing line. Use of this convention requires    * agreement between sender and recipient of data.    *    * @since 8.0    */
DECL|field|LF
specifier|public
specifier|static
specifier|final
name|byte
name|LF
init|=
literal|10
decl_stmt|;
comment|/**    * Alternate name for {@link #LF}. ({@code LF} is preferred.)    *    * @since 8.0    */
DECL|field|NL
specifier|public
specifier|static
specifier|final
name|byte
name|NL
init|=
literal|10
decl_stmt|;
comment|/**    * Vertical Tabulation ('\v'): A format effector which controls the movement of the printing    * position to the next in a series of predetermined printing lines. (Applicable also to display    * devices.)    *    * @since 8.0    */
DECL|field|VT
specifier|public
specifier|static
specifier|final
name|byte
name|VT
init|=
literal|11
decl_stmt|;
comment|/**    * Form Feed ('\f'): A format effector which controls the movement of the printing position to the    * first pre-determined printing line on the next form or page. (Applicable also to display    * devices.)    *    * @since 8.0    */
DECL|field|FF
specifier|public
specifier|static
specifier|final
name|byte
name|FF
init|=
literal|12
decl_stmt|;
comment|/**    * Carriage Return ('\r'): A format effector which controls the movement of the printing position    * to the first printing position on the same printing line. (Applicable also to display devices.)    *    * @since 8.0    */
DECL|field|CR
specifier|public
specifier|static
specifier|final
name|byte
name|CR
init|=
literal|13
decl_stmt|;
comment|/**    * Shift Out: A control character indicating that the code combinations which follow shall be    * interpreted as outside of the character set of the standard code table until a Shift In    * character is reached.    *    * @since 8.0    */
DECL|field|SO
specifier|public
specifier|static
specifier|final
name|byte
name|SO
init|=
literal|14
decl_stmt|;
comment|/**    * Shift In: A control character indicating that the code combinations which follow shall be    * interpreted according to the standard code table.    *    * @since 8.0    */
DECL|field|SI
specifier|public
specifier|static
specifier|final
name|byte
name|SI
init|=
literal|15
decl_stmt|;
comment|/**    * Data Link Escape: A communication control character which will change the meaning of a limited    * number of contiguously following characters. It is used exclusively to provide supplementary    * controls in data communication networks.    *    * @since 8.0    */
DECL|field|DLE
specifier|public
specifier|static
specifier|final
name|byte
name|DLE
init|=
literal|16
decl_stmt|;
comment|/**    * Device Control 1. Characters for the control of ancillary devices associated with data    * processing or telecommunication systems, more especially switching devices "on" or "off." (If a    * single "stop" control is required to interrupt or turn off ancillary devices, DC4 is the    * preferred assignment.)    *    * @since 8.0    */
DECL|field|DC1
specifier|public
specifier|static
specifier|final
name|byte
name|DC1
init|=
literal|17
decl_stmt|;
comment|// aka XON
comment|/**    * Transmission On: Although originally defined as DC1, this ASCII control character is now better    * known as the XON code used for software flow control in serial communications. The main use is    * restarting the transmission after the communication has been stopped by the XOFF control code.    *    * @since 8.0    */
DECL|field|XON
specifier|public
specifier|static
specifier|final
name|byte
name|XON
init|=
literal|17
decl_stmt|;
comment|// aka DC1
comment|/**    * Device Control 2. Characters for the control of ancillary devices associated with data    * processing or telecommunication systems, more especially switching devices "on" or "off." (If a    * single "stop" control is required to interrupt or turn off ancillary devices, DC4 is the    * preferred assignment.)    *    * @since 8.0    */
DECL|field|DC2
specifier|public
specifier|static
specifier|final
name|byte
name|DC2
init|=
literal|18
decl_stmt|;
comment|/**    * Device Control 3. Characters for the control of ancillary devices associated with data    * processing or telecommunication systems, more especially switching devices "on" or "off." (If a    * single "stop" control is required to interrupt or turn off ancillary devices, DC4 is the    * preferred assignment.)    *    * @since 8.0    */
DECL|field|DC3
specifier|public
specifier|static
specifier|final
name|byte
name|DC3
init|=
literal|19
decl_stmt|;
comment|// aka XOFF
comment|/**    * Transmission off. See {@link #XON} for explanation.    *    * @since 8.0    */
DECL|field|XOFF
specifier|public
specifier|static
specifier|final
name|byte
name|XOFF
init|=
literal|19
decl_stmt|;
comment|// aka DC3
comment|/**    * Device Control 4. Characters for the control of ancillary devices associated with data    * processing or telecommunication systems, more especially switching devices "on" or "off." (If a    * single "stop" control is required to interrupt or turn off ancillary devices, DC4 is the    * preferred assignment.)    *    * @since 8.0    */
DECL|field|DC4
specifier|public
specifier|static
specifier|final
name|byte
name|DC4
init|=
literal|20
decl_stmt|;
comment|/**    * Negative Acknowledge: A communication control character transmitted by a receiver as a negative    * response to the sender.    *    * @since 8.0    */
DECL|field|NAK
specifier|public
specifier|static
specifier|final
name|byte
name|NAK
init|=
literal|21
decl_stmt|;
comment|/**    * Synchronous Idle: A communication control character used by a synchronous transmission system    * in the absence of any other character to provide a signal from which synchronism may be    * achieved or retained.    *    * @since 8.0    */
DECL|field|SYN
specifier|public
specifier|static
specifier|final
name|byte
name|SYN
init|=
literal|22
decl_stmt|;
comment|/**    * End of Transmission Block: A communication control character used to indicate the end of a    * block of data for communication purposes. ETB is used for blocking data where the block    * structure is not necessarily related to the processing format.    *    * @since 8.0    */
DECL|field|ETB
specifier|public
specifier|static
specifier|final
name|byte
name|ETB
init|=
literal|23
decl_stmt|;
comment|/**    * Cancel: A control character used to indicate that the data with which it is sent is in error or    * is to be disregarded.    *    * @since 8.0    */
DECL|field|CAN
specifier|public
specifier|static
specifier|final
name|byte
name|CAN
init|=
literal|24
decl_stmt|;
comment|/**    * End of Medium: A control character associated with the sent data which may be used to identify    * the physical end of the medium, or the end of the used, or wanted, portion of information    * recorded on a medium. (The position of this character does not necessarily correspond to the    * physical end of the medium.)    *    * @since 8.0    */
DECL|field|EM
specifier|public
specifier|static
specifier|final
name|byte
name|EM
init|=
literal|25
decl_stmt|;
comment|/**    * Substitute: A character that may be substituted for a character which is determined to be    * invalid or in error.    *    * @since 8.0    */
DECL|field|SUB
specifier|public
specifier|static
specifier|final
name|byte
name|SUB
init|=
literal|26
decl_stmt|;
comment|/**    * Escape: A control character intended to provide code extension (supplementary characters) in    * general information interchange. The Escape character itself is a prefix affecting the    * interpretation of a limited number of contiguously following characters.    *    * @since 8.0    */
DECL|field|ESC
specifier|public
specifier|static
specifier|final
name|byte
name|ESC
init|=
literal|27
decl_stmt|;
comment|/**    * File Separator: These four information separators may be used within data in optional fashion,    * except that their hierarchical relationship shall be: FS is the most inclusive, then GS, then    * RS, and US is least inclusive. (The content and length of a File, Group, Record, or Unit are    * not specified.)    *    * @since 8.0    */
DECL|field|FS
specifier|public
specifier|static
specifier|final
name|byte
name|FS
init|=
literal|28
decl_stmt|;
comment|/**    * Group Separator: These four information separators may be used within data in optional fashion,    * except that their hierarchical relationship shall be: FS is the most inclusive, then GS, then    * RS, and US is least inclusive. (The content and length of a File, Group, Record, or Unit are    * not specified.)    *    * @since 8.0    */
DECL|field|GS
specifier|public
specifier|static
specifier|final
name|byte
name|GS
init|=
literal|29
decl_stmt|;
comment|/**    * Record Separator: These four information separators may be used within data in optional    * fashion, except that their hierarchical relationship shall be: FS is the most inclusive, then    * GS, then RS, and US is least inclusive. (The content and length of a File, Group, Record, or    * Unit are not specified.)    *    * @since 8.0    */
DECL|field|RS
specifier|public
specifier|static
specifier|final
name|byte
name|RS
init|=
literal|30
decl_stmt|;
comment|/**    * Unit Separator: These four information separators may be used within data in optional fashion,    * except that their hierarchical relationship shall be: FS is the most inclusive, then GS, then    * RS, and US is least inclusive. (The content and length of a File, Group, Record, or Unit are    * not specified.)    *    * @since 8.0    */
DECL|field|US
specifier|public
specifier|static
specifier|final
name|byte
name|US
init|=
literal|31
decl_stmt|;
comment|/**    * Space: A normally non-printing graphic character used to separate words. It is also a format    * effector which controls the movement of the printing position, one printing position forward.    * (Applicable also to display devices.)    *    * @since 8.0    */
DECL|field|SP
specifier|public
specifier|static
specifier|final
name|byte
name|SP
init|=
literal|32
decl_stmt|;
comment|/**    * Alternate name for {@link #SP}.    *    * @since 8.0    */
DECL|field|SPACE
specifier|public
specifier|static
specifier|final
name|byte
name|SPACE
init|=
literal|32
decl_stmt|;
comment|/**    * Delete: This character is used primarily to "erase" or "obliterate" erroneous or unwanted    * characters in perforated tape.    *    * @since 8.0    */
DECL|field|DEL
specifier|public
specifier|static
specifier|final
name|byte
name|DEL
init|=
literal|127
decl_stmt|;
comment|/**    * The minimum value of an ASCII character.    *    * @since 9.0 (was type {@code int} before 12.0)    */
DECL|field|MIN
specifier|public
specifier|static
specifier|final
name|char
name|MIN
init|=
literal|0
decl_stmt|;
comment|/**    * The maximum value of an ASCII character.    *    * @since 9.0 (was type {@code int} before 12.0)    */
DECL|field|MAX
specifier|public
specifier|static
specifier|final
name|char
name|MAX
init|=
literal|127
decl_stmt|;
comment|/**    * Returns a copy of the input string in which all {@linkplain #isUpperCase(char) uppercase ASCII    * characters} have been converted to lowercase. All other characters are copied without    * modification.    */
DECL|method|toLowerCase (String string)
specifier|public
specifier|static
name|String
name|toLowerCase
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|int
name|length
init|=
name|string
operator|.
name|length
argument_list|()
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
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|isUpperCase
argument_list|(
name|string
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
name|char
index|[]
name|chars
init|=
name|string
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
for|for
control|(
init|;
name|i
operator|<
name|length
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|chars
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|isUpperCase
argument_list|(
name|c
argument_list|)
condition|)
block|{
name|chars
index|[
name|i
index|]
operator|=
call|(
name|char
call|)
argument_list|(
name|c
operator|^
literal|0x20
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|String
operator|.
name|valueOf
argument_list|(
name|chars
argument_list|)
return|;
block|}
block|}
return|return
name|string
return|;
block|}
comment|/**    * Returns a copy of the input character sequence in which all {@linkplain #isUpperCase(char)    * uppercase ASCII characters} have been converted to lowercase. All other characters are copied    * without modification.    *    * @since 14.0    */
DECL|method|toLowerCase (CharSequence chars)
specifier|public
specifier|static
name|String
name|toLowerCase
parameter_list|(
name|CharSequence
name|chars
parameter_list|)
block|{
if|if
condition|(
name|chars
operator|instanceof
name|String
condition|)
block|{
return|return
name|toLowerCase
argument_list|(
operator|(
name|String
operator|)
name|chars
argument_list|)
return|;
block|}
name|char
index|[]
name|newChars
init|=
operator|new
name|char
index|[
name|chars
operator|.
name|length
argument_list|()
index|]
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
name|newChars
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|newChars
index|[
name|i
index|]
operator|=
name|toLowerCase
argument_list|(
name|chars
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|String
operator|.
name|valueOf
argument_list|(
name|newChars
argument_list|)
return|;
block|}
comment|/**    * If the argument is an {@linkplain #isUpperCase(char) uppercase ASCII character} returns the    * lowercase equivalent. Otherwise returns the argument.    */
DECL|method|toLowerCase (char c)
specifier|public
specifier|static
name|char
name|toLowerCase
parameter_list|(
name|char
name|c
parameter_list|)
block|{
return|return
name|isUpperCase
argument_list|(
name|c
argument_list|)
condition|?
call|(
name|char
call|)
argument_list|(
name|c
operator|^
literal|0x20
argument_list|)
else|:
name|c
return|;
block|}
comment|/**    * Returns a copy of the input string in which all {@linkplain #isLowerCase(char) lowercase ASCII    * characters} have been converted to uppercase. All other characters are copied without    * modification.    */
DECL|method|toUpperCase (String string)
specifier|public
specifier|static
name|String
name|toUpperCase
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|int
name|length
init|=
name|string
operator|.
name|length
argument_list|()
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
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|isLowerCase
argument_list|(
name|string
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
name|char
index|[]
name|chars
init|=
name|string
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
for|for
control|(
init|;
name|i
operator|<
name|length
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|chars
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|isLowerCase
argument_list|(
name|c
argument_list|)
condition|)
block|{
name|chars
index|[
name|i
index|]
operator|=
call|(
name|char
call|)
argument_list|(
name|c
operator|&
literal|0x5f
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|String
operator|.
name|valueOf
argument_list|(
name|chars
argument_list|)
return|;
block|}
block|}
return|return
name|string
return|;
block|}
comment|/**    * Returns a copy of the input character sequence in which all {@linkplain #isLowerCase(char)    * lowercase ASCII characters} have been converted to uppercase. All other characters are copied    * without modification.    *    * @since 14.0    */
DECL|method|toUpperCase (CharSequence chars)
specifier|public
specifier|static
name|String
name|toUpperCase
parameter_list|(
name|CharSequence
name|chars
parameter_list|)
block|{
if|if
condition|(
name|chars
operator|instanceof
name|String
condition|)
block|{
return|return
name|toUpperCase
argument_list|(
operator|(
name|String
operator|)
name|chars
argument_list|)
return|;
block|}
name|char
index|[]
name|newChars
init|=
operator|new
name|char
index|[
name|chars
operator|.
name|length
argument_list|()
index|]
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
name|newChars
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|newChars
index|[
name|i
index|]
operator|=
name|toUpperCase
argument_list|(
name|chars
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|String
operator|.
name|valueOf
argument_list|(
name|newChars
argument_list|)
return|;
block|}
comment|/**    * If the argument is a {@linkplain #isLowerCase(char) lowercase ASCII character} returns the    * uppercase equivalent. Otherwise returns the argument.    */
DECL|method|toUpperCase (char c)
specifier|public
specifier|static
name|char
name|toUpperCase
parameter_list|(
name|char
name|c
parameter_list|)
block|{
return|return
name|isLowerCase
argument_list|(
name|c
argument_list|)
condition|?
call|(
name|char
call|)
argument_list|(
name|c
operator|&
literal|0x5f
argument_list|)
else|:
name|c
return|;
block|}
comment|/**    * Indicates whether {@code c} is one of the twenty-six lowercase ASCII alphabetic characters    * between {@code 'a'} and {@code 'z'} inclusive. All others (including non-ASCII characters)    * return {@code false}.    */
DECL|method|isLowerCase (char c)
specifier|public
specifier|static
name|boolean
name|isLowerCase
parameter_list|(
name|char
name|c
parameter_list|)
block|{
comment|// Note: This was benchmarked against the alternate expression "(char)(c - 'a')< 26" (Nov '13)
comment|// and found to perform at least as well, or better.
return|return
operator|(
name|c
operator|>=
literal|'a'
operator|)
operator|&&
operator|(
name|c
operator|<=
literal|'z'
operator|)
return|;
block|}
comment|/**    * Indicates whether {@code c} is one of the twenty-six uppercase ASCII alphabetic characters    * between {@code 'A'} and {@code 'Z'} inclusive. All others (including non-ASCII characters)    * return {@code false}.    */
DECL|method|isUpperCase (char c)
specifier|public
specifier|static
name|boolean
name|isUpperCase
parameter_list|(
name|char
name|c
parameter_list|)
block|{
return|return
operator|(
name|c
operator|>=
literal|'A'
operator|)
operator|&&
operator|(
name|c
operator|<=
literal|'Z'
operator|)
return|;
block|}
comment|/**    * Truncates the given character sequence to the given maximum length. If the length of the    * sequence is greater than {@code maxLength}, the returned string will be exactly {@code    * maxLength} chars in length and will end with the given {@code truncationIndicator}. Otherwise,    * the sequence will be returned as a string with no changes to the content.    *    *<p>Examples:    *    *<pre>{@code    * Ascii.truncate("foobar", 7, "..."); // returns "foobar"    * Ascii.truncate("foobar", 5, "..."); // returns "fo..."    * }</pre>    *    *<p><b>Note:</b> This method<i>may</i> work with certain non-ASCII text but is not safe for use    * with arbitrary Unicode text. It is mostly intended for use with text that is known to be safe    * for use with it (such as all-ASCII text) and for simple debugging text. When using this method,    * consider the following:    *    *<ul>    *<li>it may split surrogate pairs    *<li>it may split characters and combining characters    *<li>it does not consider word boundaries    *<li>if truncating for display to users, there are other considerations that must be taken    *       into account    *<li>the appropriate truncation indicator may be locale-dependent    *<li>it is safe to use non-ASCII characters in the truncation indicator    *</ul>    *    *    * @throws IllegalArgumentException if {@code maxLength} is less than the length of {@code    *     truncationIndicator}    * @since 16.0    */
DECL|method|truncate (CharSequence seq, int maxLength, String truncationIndicator)
specifier|public
specifier|static
name|String
name|truncate
parameter_list|(
name|CharSequence
name|seq
parameter_list|,
name|int
name|maxLength
parameter_list|,
name|String
name|truncationIndicator
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|seq
argument_list|)
expr_stmt|;
comment|// length to truncate the sequence to, not including the truncation indicator
name|int
name|truncationLength
init|=
name|maxLength
operator|-
name|truncationIndicator
operator|.
name|length
argument_list|()
decl_stmt|;
comment|// in this worst case, this allows a maxLength equal to the length of the truncationIndicator,
comment|// meaning that a string will be truncated to just the truncation indicator itself
name|checkArgument
argument_list|(
name|truncationLength
operator|>=
literal|0
argument_list|,
literal|"maxLength (%s) must be>= length of the truncation indicator (%s)"
argument_list|,
name|maxLength
argument_list|,
name|truncationIndicator
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|seq
operator|.
name|length
argument_list|()
operator|<=
name|maxLength
condition|)
block|{
name|String
name|string
init|=
name|seq
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|string
operator|.
name|length
argument_list|()
operator|<=
name|maxLength
condition|)
block|{
return|return
name|string
return|;
block|}
comment|// if the length of the toString() result was> maxLength for some reason, truncate that
name|seq
operator|=
name|string
expr_stmt|;
block|}
return|return
operator|new
name|StringBuilder
argument_list|(
name|maxLength
argument_list|)
operator|.
name|append
argument_list|(
name|seq
argument_list|,
literal|0
argument_list|,
name|truncationLength
argument_list|)
operator|.
name|append
argument_list|(
name|truncationIndicator
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**    * Indicates whether the contents of the given character sequences {@code s1} and {@code s2} are    * equal, ignoring the case of any ASCII alphabetic characters between {@code 'a'} and {@code 'z'}    * or {@code 'A'} and {@code 'Z'} inclusive.    *    *<p>This method is significantly faster than {@link String#equalsIgnoreCase} and should be used    * in preference if at least one of the parameters is known to contain only ASCII characters.    *    *<p>Note however that this method does not always behave identically to expressions such as:    *    *<ul>    *<li>{@code string.toUpperCase().equals("UPPER CASE ASCII")}    *<li>{@code string.toLowerCase().equals("lower case ascii")}    *</ul>    *    *<p>due to case-folding of some non-ASCII characters (which does not occur in {@link    * String#equalsIgnoreCase}). However in almost all cases that ASCII strings are used, the author    * probably wanted the behavior provided by this method rather than the subtle and sometimes    * surprising behavior of {@code toUpperCase()} and {@code toLowerCase()}.    *    * @since 16.0    */
DECL|method|equalsIgnoreCase (CharSequence s1, CharSequence s2)
specifier|public
specifier|static
name|boolean
name|equalsIgnoreCase
parameter_list|(
name|CharSequence
name|s1
parameter_list|,
name|CharSequence
name|s2
parameter_list|)
block|{
comment|// Calling length() is the null pointer check (so do it before we can exit early).
name|int
name|length
init|=
name|s1
operator|.
name|length
argument_list|()
decl_stmt|;
if|if
condition|(
name|s1
operator|==
name|s2
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|length
operator|!=
name|s2
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
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c1
init|=
name|s1
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|char
name|c2
init|=
name|s2
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|c1
operator|==
name|c2
condition|)
block|{
continue|continue;
block|}
name|int
name|alphaIndex
init|=
name|getAlphaIndex
argument_list|(
name|c1
argument_list|)
decl_stmt|;
comment|// This was also benchmarked using '&' to avoid branching (but always evaluate the rhs),
comment|// however this showed no obvious improvement.
if|if
condition|(
name|alphaIndex
operator|<
literal|26
operator|&&
name|alphaIndex
operator|==
name|getAlphaIndex
argument_list|(
name|c2
argument_list|)
condition|)
block|{
continue|continue;
block|}
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
comment|/**    * Returns the non-negative index value of the alpha character {@code c}, regardless of case. Ie,    * 'a'/'A' returns 0 and 'z'/'Z' returns 25. Non-alpha characters return a value of 26 or greater.    */
DECL|method|getAlphaIndex (char c)
specifier|private
specifier|static
name|int
name|getAlphaIndex
parameter_list|(
name|char
name|c
parameter_list|)
block|{
comment|// Fold upper-case ASCII to lower-case and make zero-indexed and unsigned (by casting to char).
return|return
call|(
name|char
call|)
argument_list|(
operator|(
name|c
operator||
literal|0x20
operator|)
operator|-
literal|'a'
argument_list|)
return|;
block|}
block|}
end_class

end_unit

