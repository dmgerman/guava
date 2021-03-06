begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.net
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|net
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
name|checkState
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
name|CharMatcher
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
name|Objects
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
name|Strings
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
name|javax
operator|.
name|annotation
operator|.
name|CheckForNull
import|;
end_import

begin_comment
comment|/**  * An immutable representation of a host and port.  *  *<p>Example usage:  *  *<pre>  * HostAndPort hp = HostAndPort.fromString("[2001:db8::1]")  *     .withDefaultPort(80)  *     .requireBracketsForIPv6();  * hp.getHost();   // returns "2001:db8::1"  * hp.getPort();   // returns 80  * hp.toString();  // returns "[2001:db8::1]:80"  *</pre>  *  *<p>Here are some examples of recognized formats:  *  *<ul>  *<li>example.com  *<li>example.com:80  *<li>192.0.2.1  *<li>192.0.2.1:80  *<li>[2001:db8::1] - {@link #getHost()} omits brackets  *<li>[2001:db8::1]:80 - {@link #getHost()} omits brackets  *<li>2001:db8::1 - Use {@link #requireBracketsForIPv6()} to prohibit this  *</ul>  *  *<p>Note that this is not an exhaustive list, because these methods are only concerned with  * brackets, colons, and port numbers. Full validation of the host field (if desired) is the  * caller's responsibility.  *  * @author Paul Marks  * @since 10.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|Immutable
annotation|@
name|GwtCompatible
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|HostAndPort
specifier|public
specifier|final
class|class
name|HostAndPort
implements|implements
name|Serializable
block|{
comment|/** Magic value indicating the absence of a port number. */
DECL|field|NO_PORT
specifier|private
specifier|static
specifier|final
name|int
name|NO_PORT
init|=
operator|-
literal|1
decl_stmt|;
comment|/** Hostname, IPv4/IPv6 literal, or unvalidated nonsense. */
DECL|field|host
specifier|private
specifier|final
name|String
name|host
decl_stmt|;
comment|/** Validated port number in the range [0..65535], or NO_PORT */
DECL|field|port
specifier|private
specifier|final
name|int
name|port
decl_stmt|;
comment|/** True if the parsed host has colons, but no surrounding brackets. */
DECL|field|hasBracketlessColons
specifier|private
specifier|final
name|boolean
name|hasBracketlessColons
decl_stmt|;
DECL|method|HostAndPort (String host, int port, boolean hasBracketlessColons)
specifier|private
name|HostAndPort
parameter_list|(
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|,
name|boolean
name|hasBracketlessColons
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
name|this
operator|.
name|hasBracketlessColons
operator|=
name|hasBracketlessColons
expr_stmt|;
block|}
comment|/**    * Returns the portion of this {@code HostAndPort} instance that should represent the hostname or    * IPv4/IPv6 literal.    *    *<p>A successful parse does not imply any degree of sanity in this field. For additional    * validation, see the {@link HostSpecifier} class.    *    * @since 20.0 (since 10.0 as {@code getHostText})    */
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
comment|/** Return true if this instance has a defined port. */
DECL|method|hasPort ()
specifier|public
name|boolean
name|hasPort
parameter_list|()
block|{
return|return
name|port
operator|>=
literal|0
return|;
block|}
comment|/**    * Get the current port number, failing if no port is defined.    *    * @return a validated port number, in the range [0..65535]    * @throws IllegalStateException if no port is defined. You can use {@link #withDefaultPort(int)}    *     to prevent this from occurring.    */
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
name|checkState
argument_list|(
name|hasPort
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|port
return|;
block|}
comment|/** Returns the current port number, with a default if no port is defined. */
DECL|method|getPortOrDefault (int defaultPort)
specifier|public
name|int
name|getPortOrDefault
parameter_list|(
name|int
name|defaultPort
parameter_list|)
block|{
return|return
name|hasPort
argument_list|()
condition|?
name|port
else|:
name|defaultPort
return|;
block|}
comment|/**    * Build a HostAndPort instance from separate host and port values.    *    *<p>Note: Non-bracketed IPv6 literals are allowed. Use {@link #requireBracketsForIPv6()} to    * prohibit these.    *    * @param host the host string to parse. Must not contain a port number.    * @param port a port number from [0..65535]    * @return if parsing was successful, a populated HostAndPort object.    * @throws IllegalArgumentException if {@code host} contains a port number, or {@code port} is out    *     of range.    */
DECL|method|fromParts (String host, int port)
specifier|public
specifier|static
name|HostAndPort
name|fromParts
parameter_list|(
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|isValidPort
argument_list|(
name|port
argument_list|)
argument_list|,
literal|"Port out of range: %s"
argument_list|,
name|port
argument_list|)
expr_stmt|;
name|HostAndPort
name|parsedHost
init|=
name|fromString
argument_list|(
name|host
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
operator|!
name|parsedHost
operator|.
name|hasPort
argument_list|()
argument_list|,
literal|"Host has a port: %s"
argument_list|,
name|host
argument_list|)
expr_stmt|;
return|return
operator|new
name|HostAndPort
argument_list|(
name|parsedHost
operator|.
name|host
argument_list|,
name|port
argument_list|,
name|parsedHost
operator|.
name|hasBracketlessColons
argument_list|)
return|;
block|}
comment|/**    * Build a HostAndPort instance from a host only.    *    *<p>Note: Non-bracketed IPv6 literals are allowed. Use {@link #requireBracketsForIPv6()} to    * prohibit these.    *    * @param host the host-only string to parse. Must not contain a port number.    * @return if parsing was successful, a populated HostAndPort object.    * @throws IllegalArgumentException if {@code host} contains a port number.    * @since 17.0    */
DECL|method|fromHost (String host)
specifier|public
specifier|static
name|HostAndPort
name|fromHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|HostAndPort
name|parsedHost
init|=
name|fromString
argument_list|(
name|host
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
operator|!
name|parsedHost
operator|.
name|hasPort
argument_list|()
argument_list|,
literal|"Host has a port: %s"
argument_list|,
name|host
argument_list|)
expr_stmt|;
return|return
name|parsedHost
return|;
block|}
comment|/**    * Split a freeform string into a host and port, without strict validation.    *    *<p>Note that the host-only formats will leave the port field undefined. You can use {@link    * #withDefaultPort(int)} to patch in a default value.    *    * @param hostPortString the input string to parse.    * @return if parsing was successful, a populated HostAndPort object.    * @throws IllegalArgumentException if nothing meaningful could be parsed.    */
DECL|method|fromString (String hostPortString)
specifier|public
specifier|static
name|HostAndPort
name|fromString
parameter_list|(
name|String
name|hostPortString
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|hostPortString
argument_list|)
expr_stmt|;
name|String
name|host
decl_stmt|;
name|String
name|portString
init|=
literal|null
decl_stmt|;
name|boolean
name|hasBracketlessColons
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|hostPortString
operator|.
name|startsWith
argument_list|(
literal|"["
argument_list|)
condition|)
block|{
name|String
index|[]
name|hostAndPort
init|=
name|getHostAndPortFromBracketedHost
argument_list|(
name|hostPortString
argument_list|)
decl_stmt|;
name|host
operator|=
name|hostAndPort
index|[
literal|0
index|]
expr_stmt|;
name|portString
operator|=
name|hostAndPort
index|[
literal|1
index|]
expr_stmt|;
block|}
else|else
block|{
name|int
name|colonPos
init|=
name|hostPortString
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
name|colonPos
operator|>=
literal|0
operator|&&
name|hostPortString
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|,
name|colonPos
operator|+
literal|1
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
comment|// Exactly 1 colon. Split into host:port.
name|host
operator|=
name|hostPortString
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|colonPos
argument_list|)
expr_stmt|;
name|portString
operator|=
name|hostPortString
operator|.
name|substring
argument_list|(
name|colonPos
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// 0 or 2+ colons. Bare hostname or IPv6 literal.
name|host
operator|=
name|hostPortString
expr_stmt|;
name|hasBracketlessColons
operator|=
operator|(
name|colonPos
operator|>=
literal|0
operator|)
expr_stmt|;
block|}
block|}
name|int
name|port
init|=
name|NO_PORT
decl_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|portString
argument_list|)
condition|)
block|{
comment|// Try to parse the whole port string as a number.
comment|// JDK7 accepts leading plus signs. We don't want to.
name|checkArgument
argument_list|(
operator|!
name|portString
operator|.
name|startsWith
argument_list|(
literal|"+"
argument_list|)
operator|&&
name|CharMatcher
operator|.
name|ascii
argument_list|()
operator|.
name|matchesAllOf
argument_list|(
name|portString
argument_list|)
argument_list|,
literal|"Unparseable port number: %s"
argument_list|,
name|hostPortString
argument_list|)
expr_stmt|;
try|try
block|{
name|port
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|portString
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unparseable port number: "
operator|+
name|hostPortString
argument_list|)
throw|;
block|}
name|checkArgument
argument_list|(
name|isValidPort
argument_list|(
name|port
argument_list|)
argument_list|,
literal|"Port number out of range: %s"
argument_list|,
name|hostPortString
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|HostAndPort
argument_list|(
name|host
argument_list|,
name|port
argument_list|,
name|hasBracketlessColons
argument_list|)
return|;
block|}
comment|/**    * Parses a bracketed host-port string, throwing IllegalArgumentException if parsing fails.    *    * @param hostPortString the full bracketed host-port specification. Post might not be specified.    * @return an array with 2 strings: host and port, in that order.    * @throws IllegalArgumentException if parsing the bracketed host-port string fails.    */
DECL|method|getHostAndPortFromBracketedHost (String hostPortString)
specifier|private
specifier|static
name|String
index|[]
name|getHostAndPortFromBracketedHost
parameter_list|(
name|String
name|hostPortString
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|hostPortString
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|==
literal|'['
argument_list|,
literal|"Bracketed host-port string must start with a bracket: %s"
argument_list|,
name|hostPortString
argument_list|)
expr_stmt|;
name|int
name|colonIndex
init|=
name|hostPortString
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
name|int
name|closeBracketIndex
init|=
name|hostPortString
operator|.
name|lastIndexOf
argument_list|(
literal|']'
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|colonIndex
operator|>
operator|-
literal|1
operator|&&
name|closeBracketIndex
operator|>
name|colonIndex
argument_list|,
literal|"Invalid bracketed host/port: %s"
argument_list|,
name|hostPortString
argument_list|)
expr_stmt|;
name|String
name|host
init|=
name|hostPortString
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|closeBracketIndex
argument_list|)
decl_stmt|;
if|if
condition|(
name|closeBracketIndex
operator|+
literal|1
operator|==
name|hostPortString
operator|.
name|length
argument_list|()
condition|)
block|{
return|return
operator|new
name|String
index|[]
block|{
name|host
block|,
literal|""
block|}
return|;
block|}
else|else
block|{
name|checkArgument
argument_list|(
name|hostPortString
operator|.
name|charAt
argument_list|(
name|closeBracketIndex
operator|+
literal|1
argument_list|)
operator|==
literal|':'
argument_list|,
literal|"Only a colon may follow a close bracket: %s"
argument_list|,
name|hostPortString
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|closeBracketIndex
operator|+
literal|2
init|;
name|i
operator|<
name|hostPortString
operator|.
name|length
argument_list|()
condition|;
operator|++
name|i
control|)
block|{
name|checkArgument
argument_list|(
name|Character
operator|.
name|isDigit
argument_list|(
name|hostPortString
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|,
literal|"Port must be numeric: %s"
argument_list|,
name|hostPortString
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|String
index|[]
block|{
name|host
block|,
name|hostPortString
operator|.
name|substring
argument_list|(
name|closeBracketIndex
operator|+
literal|2
argument_list|)
block|}
return|;
block|}
block|}
comment|/**    * Provide a default port if the parsed string contained only a host.    *    *<p>You can chain this after {@link #fromString(String)} to include a port in case the port was    * omitted from the input string. If a port was already provided, then this method is a no-op.    *    * @param defaultPort a port number, from [0..65535]    * @return a HostAndPort instance, guaranteed to have a defined port.    */
DECL|method|withDefaultPort (int defaultPort)
specifier|public
name|HostAndPort
name|withDefaultPort
parameter_list|(
name|int
name|defaultPort
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|isValidPort
argument_list|(
name|defaultPort
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|hasPort
argument_list|()
condition|)
block|{
return|return
name|this
return|;
block|}
return|return
operator|new
name|HostAndPort
argument_list|(
name|host
argument_list|,
name|defaultPort
argument_list|,
name|hasBracketlessColons
argument_list|)
return|;
block|}
comment|/**    * Generate an error if the host might be a non-bracketed IPv6 literal.    *    *<p>URI formatting requires that IPv6 literals be surrounded by brackets, like "[2001:db8::1]".    * Chain this call after {@link #fromString(String)} to increase the strictness of the parser, and    * disallow IPv6 literals that don't contain these brackets.    *    *<p>Note that this parser identifies IPv6 literals solely based on the presence of a colon. To    * perform actual validation of IP addresses, see the {@link InetAddresses#forString(String)}    * method.    *    * @return {@code this}, to enable chaining of calls.    * @throws IllegalArgumentException if bracketless IPv6 is detected.    */
DECL|method|requireBracketsForIPv6 ()
specifier|public
name|HostAndPort
name|requireBracketsForIPv6
parameter_list|()
block|{
name|checkArgument
argument_list|(
operator|!
name|hasBracketlessColons
argument_list|,
literal|"Possible bracketless IPv6 literal: %s"
argument_list|,
name|host
argument_list|)
expr_stmt|;
return|return
name|this
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
name|this
operator|==
name|other
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|other
operator|instanceof
name|HostAndPort
condition|)
block|{
name|HostAndPort
name|that
init|=
operator|(
name|HostAndPort
operator|)
name|other
decl_stmt|;
return|return
name|Objects
operator|.
name|equal
argument_list|(
name|this
operator|.
name|host
argument_list|,
name|that
operator|.
name|host
argument_list|)
operator|&&
name|this
operator|.
name|port
operator|==
name|that
operator|.
name|port
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
name|Objects
operator|.
name|hashCode
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
return|;
block|}
comment|/** Rebuild the host:port string, including brackets if necessary. */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
comment|// "[]:12345" requires 8 extra bytes.
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|(
name|host
operator|.
name|length
argument_list|()
operator|+
literal|8
argument_list|)
decl_stmt|;
if|if
condition|(
name|host
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
operator|>=
literal|0
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|'['
argument_list|)
operator|.
name|append
argument_list|(
name|host
argument_list|)
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|append
argument_list|(
name|host
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|hasPort
argument_list|()
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|':'
argument_list|)
operator|.
name|append
argument_list|(
name|port
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
comment|/** Return true for valid port numbers. */
DECL|method|isValidPort (int port)
specifier|private
specifier|static
name|boolean
name|isValidPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
return|return
name|port
operator|>=
literal|0
operator|&&
name|port
operator|<=
literal|65535
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

