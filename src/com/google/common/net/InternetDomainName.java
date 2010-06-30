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
name|Joiner
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
name|Splitter
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
name|ImmutableList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
comment|/**  * An immutable well-formed internet domain name, as defined by  *<a href="http://www.ietf.org/rfc/rfc1035.txt">RFC 1035</a>, with the  * exception that names ending in {@code "."} are not supported (as they are not  * generally used in browsers, email, and other end-user applications. Examples  * include {@code com} and {@code foo.co.uk}. Only syntactic analysis is  * performed; no DNS lookups or other network interactions take place. Thus  * there is no guarantee that the domain actually exists on the internet.  * Invalid domain names throw {@link IllegalArgumentException} on construction.  *  *<p>It is often the case that domains of interest are those under a  * {@linkplain #isPublicSuffix() public suffix} but not themselves a public  * suffix; {@link #hasPublicSuffix()} and {@link #isTopPrivateDomain()} test for  * this. Similarly, one often needs to obtain the domain consisting of the  * public suffix plus one subdomain level, typically to obtain the highest-level  * domain for which cookies may be set. Use {@link #topPrivateDomain()} for this  * purpose.  *  *<p>{@linkplain #equals(Object) Equality} of domain names is case-insensitive,  * so for convenience, the {@link #name()} and {@link #parts()} methods return  * the lowercase form of the name.  *  *<p><a href="http://en.wikipedia.org/wiki/Internationalized_domain_name">  * internationalized domain names (IDN)</a> such as {@code ç½ç».cn} are  * supported.  *  * @author Craig Berry  * @since 5  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|InternetDomainName
specifier|public
specifier|final
class|class
name|InternetDomainName
block|{
DECL|field|DOT_SPLITTER
specifier|private
specifier|static
specifier|final
name|Splitter
name|DOT_SPLITTER
init|=
name|Splitter
operator|.
name|on
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
DECL|field|DOT_JOINER
specifier|private
specifier|static
specifier|final
name|Joiner
name|DOT_JOINER
init|=
name|Joiner
operator|.
name|on
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
comment|/**    * Value of {@link #publicSuffixIndex} which indicates that no public suffix    * was found.    */
DECL|field|NO_PUBLIC_SUFFIX_FOUND
specifier|private
specifier|static
specifier|final
name|int
name|NO_PUBLIC_SUFFIX_FOUND
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|DOT_REGEX
specifier|private
specifier|static
specifier|final
name|String
name|DOT_REGEX
init|=
literal|"\\."
decl_stmt|;
comment|/**    * The full domain name, converted to lower case.    */
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
comment|/**    * The parts of the domain name, converted to lower case.    */
DECL|field|parts
specifier|private
specifier|final
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|parts
decl_stmt|;
comment|/**    * The index in the {@link #parts()} list at which the public suffix begins.    * For example, for the domain name {@code www.google.co.uk}, the value would    * be 2 (the index of the {@code co} part). The value is negative    * (specifically, {@link #NO_PUBLIC_SUFFIX_FOUND}) if no public suffix was    * found.    */
DECL|field|publicSuffixIndex
specifier|private
specifier|final
name|int
name|publicSuffixIndex
decl_stmt|;
comment|/**    * Private constructor used to implement {@link #from(String)}.    */
DECL|method|InternetDomainName (String name)
specifier|private
name|InternetDomainName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|parts
operator|=
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|DOT_SPLITTER
operator|.
name|split
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|validateSyntax
argument_list|(
name|parts
argument_list|)
argument_list|,
literal|"Not a valid domain name: '%s'"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|this
operator|.
name|publicSuffixIndex
operator|=
name|findPublicSuffix
argument_list|()
expr_stmt|;
block|}
comment|/**    * Private constructor used to implement {@link #ancestor(int)}. Argument    * parts are assumed to be valid, as they always come from an existing domain.    */
DECL|method|InternetDomainName (List<String> parts)
specifier|private
name|InternetDomainName
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|parts
parameter_list|)
block|{
name|checkArgument
argument_list|(
operator|!
name|parts
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|parts
operator|=
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|parts
argument_list|)
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|DOT_JOINER
operator|.
name|join
argument_list|(
name|parts
argument_list|)
expr_stmt|;
name|this
operator|.
name|publicSuffixIndex
operator|=
name|findPublicSuffix
argument_list|()
expr_stmt|;
block|}
comment|/**    * Returns the index of the leftmost part of the public suffix, or -1 if not    * found.    */
DECL|method|findPublicSuffix ()
specifier|private
name|int
name|findPublicSuffix
parameter_list|()
block|{
specifier|final
name|int
name|partsSize
init|=
name|parts
operator|.
name|size
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
name|partsSize
condition|;
name|i
operator|++
control|)
block|{
name|String
name|ancestorName
init|=
name|DOT_JOINER
operator|.
name|join
argument_list|(
name|parts
operator|.
name|subList
argument_list|(
name|i
argument_list|,
name|partsSize
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|isPublicSuffixInternal
argument_list|(
name|ancestorName
argument_list|)
condition|)
block|{
return|return
name|i
return|;
block|}
block|}
return|return
name|NO_PUBLIC_SUFFIX_FOUND
return|;
block|}
comment|/**    * A factory method for creating {@code InternetDomainName} objects.    *    * @param domain A domain name (not IP address)    * @throws IllegalArgumentException If name is not syntactically valid    */
DECL|method|from (String domain)
specifier|public
specifier|static
name|InternetDomainName
name|from
parameter_list|(
name|String
name|domain
parameter_list|)
block|{
comment|// RFC 1035 defines domain names to be case-insensitive; normalizing
comment|// to lower case allows us to simplify matching.
return|return
operator|new
name|InternetDomainName
argument_list|(
name|domain
operator|.
name|toLowerCase
argument_list|()
argument_list|)
return|;
block|}
comment|// TODO: For the moment, we validate that all parts of a domain
comment|// * Start and end with an alphanumeric character
comment|// * Have alphanumeric, dash, or underscore characters internally
comment|// An additional constraint is that the first character of the last part
comment|// may not be numeric.
comment|// All of this is a compromise to allow relatively accurate and efficient
comment|// checking. We may soon move to using java.net.IDN for this purpose in
comment|// non-GWT code.
comment|/**    * Validation method used by {@from} to ensure that the domain name is    * syntactically valid according to RFC 1035.    *    * @return Is the domain name syntactically valid?    */
DECL|method|validateSyntax (List<String> parts)
specifier|private
specifier|static
name|boolean
name|validateSyntax
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|parts
parameter_list|)
block|{
specifier|final
name|int
name|lastIndex
init|=
name|parts
operator|.
name|size
argument_list|()
operator|-
literal|1
decl_stmt|;
comment|// Validate the last part specially, as it has different syntax rules.
if|if
condition|(
operator|!
name|validatePart
argument_list|(
name|parts
operator|.
name|get
argument_list|(
name|lastIndex
argument_list|)
argument_list|,
literal|true
argument_list|)
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
name|lastIndex
condition|;
name|i
operator|++
control|)
block|{
name|String
name|part
init|=
name|parts
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|validatePart
argument_list|(
name|part
argument_list|,
literal|false
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**    * The maximum size of a single part of a domain name.    */
DECL|field|MAX_DOMAIN_PART_LENGTH
specifier|private
specifier|static
specifier|final
name|int
name|MAX_DOMAIN_PART_LENGTH
init|=
literal|63
decl_stmt|;
DECL|field|DASH_MATCHER
specifier|private
specifier|static
specifier|final
name|CharMatcher
name|DASH_MATCHER
init|=
name|CharMatcher
operator|.
name|anyOf
argument_list|(
literal|"-_"
argument_list|)
decl_stmt|;
DECL|field|PART_CHAR_MATCHER
specifier|private
specifier|static
specifier|final
name|CharMatcher
name|PART_CHAR_MATCHER
init|=
name|CharMatcher
operator|.
name|JAVA_LETTER_OR_DIGIT
operator|.
name|or
argument_list|(
name|DASH_MATCHER
argument_list|)
decl_stmt|;
comment|/**    * Helper method for {@link #validateSyntax(List)}. Validates that one part of    * a domain name is valid.    *    * @param part The domain name part to be validated    * @param isFinalPart Is this the final (rightmost) domain part?    * @return Whether the part is valid    */
DECL|method|validatePart (String part, boolean isFinalPart)
specifier|private
specifier|static
name|boolean
name|validatePart
parameter_list|(
name|String
name|part
parameter_list|,
name|boolean
name|isFinalPart
parameter_list|)
block|{
comment|// These tests could be collapsed into one big boolean expression, but
comment|// they have been left as independent tests for clarity.
if|if
condition|(
name|part
operator|.
name|length
argument_list|()
operator|<
literal|1
operator|||
name|part
operator|.
name|length
argument_list|()
operator|>
name|MAX_DOMAIN_PART_LENGTH
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// GWT claims to support java.lang.Character's char-classification
comment|// methods, but it actually only works for ASCII. So for now,
comment|// assume anything with non-ASCII characters is valid.
comment|// The only place this seems to be documented is here:
comment|// http://osdir.com/ml/GoogleWebToolkitContributors/2010-03/msg00178.html
if|if
condition|(
operator|!
name|CharMatcher
operator|.
name|ASCII
operator|.
name|matchesAllOf
argument_list|(
name|part
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
name|PART_CHAR_MATCHER
operator|.
name|matchesAllOf
argument_list|(
name|part
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|DASH_MATCHER
operator|.
name|matches
argument_list|(
name|part
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|||
name|DASH_MATCHER
operator|.
name|matches
argument_list|(
name|part
operator|.
name|charAt
argument_list|(
name|part
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|isFinalPart
operator|&&
name|CharMatcher
operator|.
name|DIGIT
operator|.
name|matches
argument_list|(
name|part
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
comment|/**    * Returns the domain name, normalized to all lower case.    */
DECL|method|name ()
specifier|public
name|String
name|name
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**    * Returns the individual components of this domain name, normalized to all    * lower case. For example, for the domain name {@code mail.google.com}, this    * method returns the list {@code ["mail", "google", "com"]}.    */
DECL|method|parts ()
specifier|public
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|parts
parameter_list|()
block|{
return|return
name|parts
return|;
block|}
comment|/**    * Old location of {@link #isPublicSuffix()}.    *    * @deprecated use {@link #isPublicSuffix()}    */
DECL|method|isRecognizedTld ()
annotation|@
name|Deprecated
specifier|public
name|boolean
name|isRecognizedTld
parameter_list|()
block|{
return|return
name|isPublicSuffix
argument_list|()
return|;
block|}
comment|/**    * Old location of {@link #isUnderPublicSuffix()}.    *    * @deprecated use {@link #isUnderPublicSuffix()}    */
DECL|method|isUnderRecognizedTld ()
annotation|@
name|Deprecated
specifier|public
name|boolean
name|isUnderRecognizedTld
parameter_list|()
block|{
return|return
name|isUnderPublicSuffix
argument_list|()
return|;
block|}
comment|/**    * Old location of {@link #hasPublicSuffix()}.    *    * @deprecated use {@link #hasPublicSuffix()}    */
DECL|method|hasRecognizedTld ()
annotation|@
name|Deprecated
specifier|public
name|boolean
name|hasRecognizedTld
parameter_list|()
block|{
return|return
name|hasPublicSuffix
argument_list|()
return|;
block|}
comment|/**    * Old location of {@link #publicSuffix()}.    *    * @deprecated use {@link #publicSuffix()}    */
DECL|method|recognizedTld ()
annotation|@
name|Deprecated
specifier|public
name|InternetDomainName
name|recognizedTld
parameter_list|()
block|{
return|return
name|publicSuffix
argument_list|()
return|;
block|}
comment|/**    * Old location of {@link #isTopPrivateDomain()}.    *    * @deprecated use {@link #isTopPrivateDomain()}    */
DECL|method|isImmediatelyUnderTld ()
annotation|@
name|Deprecated
specifier|public
name|boolean
name|isImmediatelyUnderTld
parameter_list|()
block|{
return|return
name|isTopPrivateDomain
argument_list|()
return|;
block|}
comment|/**    * Old location of {@link #topPrivateDomain()}.    *    * @deprecated use {@link #topPrivateDomain()}    */
DECL|method|topCookieDomain ()
annotation|@
name|Deprecated
specifier|public
name|InternetDomainName
name|topCookieDomain
parameter_list|()
block|{
return|return
name|topPrivateDomain
argument_list|()
return|;
block|}
comment|/**    * Returns the rightmost non-{@linkplain #isRecognizedTld() TLD} domain name    * part.  For example    * {@code new InternetDomainName("www.google.com").rightmostNonTldPart()}    * returns {@code "google"}.  Returns null if either no    * {@linkplain #isRecognizedTld() TLD} is found, or the whole domain name is    * itself a {@linkplain #isRecognizedTld() TLD}.    *    * @deprecated use the first {@linkplain #parts part} of the {@link    *     #topPrivateDomain()}    */
DECL|method|rightmostNonTldPart ()
annotation|@
name|Deprecated
specifier|public
name|String
name|rightmostNonTldPart
parameter_list|()
block|{
return|return
name|publicSuffixIndex
operator|>=
literal|1
condition|?
name|parts
operator|.
name|get
argument_list|(
name|publicSuffixIndex
operator|-
literal|1
argument_list|)
else|:
literal|null
return|;
block|}
comment|/**    * Indicates whether this domain name represents a<i>public suffix</i>, as    * defined by the Mozilla Foundation's    *<a href="http://publicsuffix.org/">Public Suffix List</a> (PSL). A public    * suffix is one under which Internet users can directly register names, such    * as {@code com}, {@code co.uk} or {@code pvt.k12.wy.us}. Examples of domain    * names that are<i>not</i> public suffixes include {@code google}, {@code    * google.com} and {@code foo.co.uk}.    *    * @return {@code true} if this domain name appears exactly on the public    *     suffix list    * @since 6    */
DECL|method|isPublicSuffix ()
specifier|public
name|boolean
name|isPublicSuffix
parameter_list|()
block|{
return|return
name|publicSuffixIndex
operator|==
literal|0
return|;
block|}
comment|/**    * Indicates whether this domain name ends in a {@linkplain #isPublicSuffix()    * public suffix}, including if it is a public suffix itself. For example,    * returns {@code true} for {@code www.google.com}, {@code foo.co.uk} and    * {@code com}, but not for {@code google} or {@code google.foo}.    *    * @since 6    */
DECL|method|hasPublicSuffix ()
specifier|public
name|boolean
name|hasPublicSuffix
parameter_list|()
block|{
return|return
name|publicSuffixIndex
operator|!=
name|NO_PUBLIC_SUFFIX_FOUND
return|;
block|}
comment|/**    * Returns the {@linkplain #isPublicSuffix() public suffix} portion of the    * domain name, or {@code null} if no public suffix is present.    *    * @since 6    */
DECL|method|publicSuffix ()
specifier|public
name|InternetDomainName
name|publicSuffix
parameter_list|()
block|{
return|return
name|hasPublicSuffix
argument_list|()
condition|?
name|ancestor
argument_list|(
name|publicSuffixIndex
argument_list|)
else|:
literal|null
return|;
block|}
comment|/**    * Indicates whether this domain name ends in a {@linkplain #isPublicSuffix()    * public suffix}, while not being a public suffix itself. For example,    * returns {@code true} for {@code www.google.com}, {@code foo.co.uk} and    * {@code bar.ca.us}, but not for {@code google}, {@code com}, or {@code    * google.foo}.    *    * @since 6    */
DECL|method|isUnderPublicSuffix ()
specifier|public
name|boolean
name|isUnderPublicSuffix
parameter_list|()
block|{
return|return
name|publicSuffixIndex
operator|>
literal|0
return|;
block|}
comment|/**    * Indicates whether this domain name is composed of exactly one subdomain    * component followed by a {@linkplain #isPublicSuffix() public suffix}. For    * example, returns {@code true} for {@code google.com} and {@code foo.co.uk},    * but not for {@code www.google.com} or {@code co.uk}.    *    * @since 6    */
DECL|method|isTopPrivateDomain ()
specifier|public
name|boolean
name|isTopPrivateDomain
parameter_list|()
block|{
return|return
name|publicSuffixIndex
operator|==
literal|1
return|;
block|}
comment|/**    * Returns the portion of this domain name that is one level beneath the    * public suffix. For example, for {@code x.adwords.google.co.uk} it returns    * {@code google.co.uk}, since {@code co.uk} is a public suffix. This is the    * highest-level parent of this domain for which cookies may be set, as    * cookies cannot be set on a public suffix itself.    *    *<p>If {@link #isTopPrivateDomain()} is true, the current domain name    * instance is returned.    *    * @throws IllegalStateException if this domain does not end with a    *     public suffix    * @since 6    */
DECL|method|topPrivateDomain ()
specifier|public
name|InternetDomainName
name|topPrivateDomain
parameter_list|()
block|{
if|if
condition|(
name|isTopPrivateDomain
argument_list|()
condition|)
block|{
return|return
name|this
return|;
block|}
name|checkState
argument_list|(
name|isUnderPublicSuffix
argument_list|()
argument_list|,
literal|"Not under a public suffix: %s"
argument_list|,
name|name
argument_list|)
expr_stmt|;
return|return
name|ancestor
argument_list|(
name|publicSuffixIndex
operator|-
literal|1
argument_list|)
return|;
block|}
comment|/**    * Indicates whether this domain is composed of two or more parts.    */
DECL|method|hasParent ()
specifier|public
name|boolean
name|hasParent
parameter_list|()
block|{
return|return
name|parts
operator|.
name|size
argument_list|()
operator|>
literal|1
return|;
block|}
comment|/**    * Returns an {@code InternetDomainName} that is the immediate ancestor of    * this one; that is, the current domain with the leftmost part removed. For    * example, the parent of {@code www.google.com} is {@code google.com}.    *    * @throws IllegalStateException if the domain has no parent, as determined    *     by {@link #hasParent}    */
DECL|method|parent ()
specifier|public
name|InternetDomainName
name|parent
parameter_list|()
block|{
name|checkState
argument_list|(
name|hasParent
argument_list|()
argument_list|,
literal|"Domain '%s' has no parent"
argument_list|,
name|name
argument_list|)
expr_stmt|;
return|return
name|ancestor
argument_list|(
literal|1
argument_list|)
return|;
block|}
comment|/**    * Returns the ancestor of the current domain at the given number of levels    * "higher" (rightward) in the subdomain list. The number of levels must be    * non-negative, and less than {@code N-1}, where {@code N} is the number of    * parts in the domain.    *    *<p>TODO: Reasonable candidate for addition to public API.    */
DECL|method|ancestor (int levels)
specifier|private
name|InternetDomainName
name|ancestor
parameter_list|(
name|int
name|levels
parameter_list|)
block|{
return|return
operator|new
name|InternetDomainName
argument_list|(
name|parts
operator|.
name|subList
argument_list|(
name|levels
argument_list|,
name|parts
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Creates and returns a new {@code InternetDomainName} by prepending the    * argument and a dot to the current name. For example, {@code    * InternetDomainName.from("foo.com").child("www.bar")} returns a new {@code    * InternetDomainName} with the value {@code www.bar.foo.com}.    *    * @throws NullPointerException if leftParts is null    * @throws IllegalArgumentException if the resulting name is not valid    */
DECL|method|child (String leftParts)
specifier|public
name|InternetDomainName
name|child
parameter_list|(
name|String
name|leftParts
parameter_list|)
block|{
return|return
name|InternetDomainName
operator|.
name|from
argument_list|(
name|checkNotNull
argument_list|(
name|leftParts
argument_list|)
operator|+
literal|"."
operator|+
name|name
argument_list|)
return|;
block|}
comment|/**    * Indicates whether the argument is a syntactically valid domain name.  This    * method is intended for the case where a {@link String} must be validated as    * a valid domain name, but no further work with that {@link String} as an    * {@link InternetDomainName} will be required. Code like the following will    * unnecessarily repeat the work of validation:<pre>   {@code    *    *   if (InternetDomainName.isValid(name)) {    *     domainName = InternetDomainName.from(name);    *   } else {    *     domainName = DEFAULT_DOMAIN;    *   }}</pre>    *    * Such code could instead be written as follows:<pre>   {@code    *    *   try {    *     domainName = InternetDomainName.from(name);    *   } catch (IllegalArgumentException e) {    *     domainName = DEFAULT_DOMAIN;    *   }}</pre>    */
DECL|method|isValid (String name)
specifier|public
specifier|static
name|boolean
name|isValid
parameter_list|(
name|String
name|name
parameter_list|)
block|{
try|try
block|{
name|from
argument_list|(
name|name
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
comment|/**    * Does the domain name satisfy the Mozilla criteria for a {@linkplain    * #isPublicSuffix() public suffix}?    */
DECL|method|isPublicSuffixInternal (String domain)
specifier|private
specifier|static
name|boolean
name|isPublicSuffixInternal
parameter_list|(
name|String
name|domain
parameter_list|)
block|{
return|return
name|TldPatterns
operator|.
name|EXACT
operator|.
name|contains
argument_list|(
name|domain
argument_list|)
operator|||
operator|(
operator|!
name|TldPatterns
operator|.
name|EXCLUDED
operator|.
name|contains
argument_list|(
name|domain
argument_list|)
operator|&&
name|matchesWildcardPublicSuffix
argument_list|(
name|domain
argument_list|)
operator|)
return|;
block|}
comment|/**    * Does the domain name match one of the "wildcard" patterns (e.g. "*.ar")?    */
DECL|method|matchesWildcardPublicSuffix (String domain)
specifier|private
specifier|static
name|boolean
name|matchesWildcardPublicSuffix
parameter_list|(
name|String
name|domain
parameter_list|)
block|{
specifier|final
name|String
index|[]
name|pieces
init|=
name|domain
operator|.
name|split
argument_list|(
name|DOT_REGEX
argument_list|,
literal|2
argument_list|)
decl_stmt|;
return|return
name|pieces
operator|.
name|length
operator|==
literal|2
operator|&&
name|TldPatterns
operator|.
name|UNDER
operator|.
name|contains
argument_list|(
name|pieces
index|[
literal|1
index|]
argument_list|)
return|;
block|}
comment|// TODO: specify this to return the same as name(); remove name()
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|toStringHelper
argument_list|(
name|this
argument_list|)
operator|.
name|add
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@ullable Object object)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
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
name|InternetDomainName
condition|)
block|{
name|InternetDomainName
name|that
init|=
operator|(
name|InternetDomainName
operator|)
name|object
decl_stmt|;
return|return
name|this
operator|.
name|name
operator|.
name|equals
argument_list|(
name|that
operator|.
name|name
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
name|name
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
end_class

end_unit

