begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|net
operator|.
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
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
comment|/**  * A syntactically valid host specifier, suitable for use in a URI.  * This may be either a numeric IP address in IPv4 or IPv6 notation, or a  * domain name.  *  *<p>Because this class is intended to represent host specifiers which can  * reasonably be used in a URI, the domain name case is further restricted to  * include only those domain names which end in a recognized public suffix; see  * {@link InternetDomainName#isPublicSuffix()} for details.  *  *<p>Note that no network lookups are performed by any {@code HostSpecifier}  * methods.  No attempt is made to verify that a provided specifier corresponds  * to a real or accessible host.  Only syntactic and pattern-based checks are  * performed.  *  *<p>If you know that a given string represents a numeric IP address, use  * {@link InetAddresses} to obtain and manipulate a  * {@link java.net.InetAddress} instance from it rather than using this class.  * Similarly, if you know that a given string represents a domain name, use  * {@link InternetDomainName} rather than this class.  *  * @author Craig Berry  * @since 5  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|HostSpecifier
specifier|public
specifier|final
class|class
name|HostSpecifier
block|{
DECL|field|canonicalForm
specifier|private
specifier|final
name|String
name|canonicalForm
decl_stmt|;
DECL|method|HostSpecifier (String canonicalForm)
specifier|private
name|HostSpecifier
parameter_list|(
name|String
name|canonicalForm
parameter_list|)
block|{
name|this
operator|.
name|canonicalForm
operator|=
name|canonicalForm
expr_stmt|;
block|}
comment|/**    * Returns a {@code HostSpecifier} built from the provided {@code specifier},    * which is already known to be valid.  If the {@code specifier} might be    * valid, use {@link #from(String)} instead.    *    *<p>The specifier must be in one of these formats:    *<ul>    *<li>A domain name, like {@code google.com}    *<li>A IPv4 address string, like {@code 127.0.0.1}    *<li>An IPv6 address string with or without brackets, like    *     {@code [2001:db8::1]} or {@code 2001:db8::1}    *<li>An IPv6 address string enclosed in square brackets, like    *     {[2001:db8::1]}    *</ul>    *    * @throws IllegalArgumentException if the specifier is not valid.    */
DECL|method|fromValid (String specifier)
specifier|public
specifier|static
name|HostSpecifier
name|fromValid
parameter_list|(
name|String
name|specifier
parameter_list|)
block|{
comment|// First, try to interpret the specifier as an IP address.  Note we build
comment|// the address rather than using the .is* methods because we want to
comment|// use InetAddresses.toUriString to convert the result to a string in
comment|// canonical form.
name|InetAddress
name|addr
init|=
literal|null
decl_stmt|;
try|try
block|{
name|addr
operator|=
name|InetAddresses
operator|.
name|forString
argument_list|(
name|specifier
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// It is not an IPv4 or bracketless IPv6 specifier
block|}
if|if
condition|(
name|addr
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|addr
operator|=
name|InetAddresses
operator|.
name|forUriString
argument_list|(
name|specifier
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// It is not a bracketed IPv6 specifier
block|}
block|}
if|if
condition|(
name|addr
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|HostSpecifier
argument_list|(
name|InetAddresses
operator|.
name|toUriString
argument_list|(
name|addr
argument_list|)
argument_list|)
return|;
block|}
comment|// It is not any kind of IP address; must be a domain name or invalid.
specifier|final
name|InternetDomainName
name|domain
init|=
name|InternetDomainName
operator|.
name|from
argument_list|(
name|specifier
argument_list|)
decl_stmt|;
if|if
condition|(
name|domain
operator|.
name|isUnderPublicSuffix
argument_list|()
condition|)
block|{
return|return
operator|new
name|HostSpecifier
argument_list|(
name|domain
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Domain name not under a recognized TLD: "
operator|+
name|specifier
argument_list|)
throw|;
block|}
comment|/**    * Attempts to return a {@code HostSpecifier} for the given string, throwing    * an exception if parsing fails. Always use this method in preference to    * {@link #fromValid(String)} for a specifier that is not already known to be    * valid.    *    * @throws ParseException if the specifier is not valid.    */
DECL|method|from (String specifier)
specifier|public
specifier|static
name|HostSpecifier
name|from
parameter_list|(
name|String
name|specifier
parameter_list|)
throws|throws
name|ParseException
block|{
try|try
block|{
return|return
name|fromValid
argument_list|(
name|specifier
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// Since the IAE can originate at several different points inside
comment|// fromValid(), we implement this method in terms of that one rather
comment|// than the reverse.
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"Invalid host specifier: "
operator|+
name|specifier
argument_list|,
literal|0
argument_list|)
throw|;
block|}
block|}
comment|/**    * Determines whether {@code specifier} represents a valid    * {@link HostSpecifier} as described in the documentation for    * {@link #fromValid(String)}.    */
DECL|method|isValid (String specifier)
specifier|public
specifier|static
name|boolean
name|isValid
parameter_list|(
name|String
name|specifier
parameter_list|)
block|{
try|try
block|{
name|fromValid
argument_list|(
name|specifier
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|equals (@ullable Object other)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
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
name|HostSpecifier
condition|)
block|{
specifier|final
name|HostSpecifier
name|that
init|=
operator|(
name|HostSpecifier
operator|)
name|other
decl_stmt|;
return|return
name|this
operator|.
name|canonicalForm
operator|.
name|equals
argument_list|(
name|that
operator|.
name|canonicalForm
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
name|canonicalForm
operator|.
name|hashCode
argument_list|()
return|;
block|}
comment|/**    * Returns a string representation of the host specifier suitable for    * inclusion in a URI.  If the host specifier is a domain name, the    * string will be normalized to all lower case.  If the specifier was    * an IPv6 address without brackets, brackets are added so that the    * result will be usable in the host part of a URI.    */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|canonicalForm
return|;
block|}
block|}
end_class

end_unit

