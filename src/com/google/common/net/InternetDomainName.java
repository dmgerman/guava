begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Ascii
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
comment|/**  * An immutable well-formed internet domain name, such as {@code com} or {@code  * foo.co.uk}. Only syntactic analysis is performed; no DNS lookups or other  * network interactions take place. Thus there is no guarantee that the domain  * actually exists on the internet.  *  *<p>One common use of this class is to determine whether a given string is  * likely to represent an addressable domain on the web -- that is, for a  * candidate string {@code "xxx"}, might browsing to {@code "http://xxx/"}  * result in a webpage being displayed? In the past, this test was frequently  * done by determining whether the domain ended with a {@linkplain  * #isPublicSuffix() public suffix} but was not itself a public suffix. However,  * this test is no longer accurate. There are many domains which are both public  * suffixes and addressable as hosts; {@code "uk.com"} is one example. As a  * result, the only useful test to determine if a domain is a plausible web host  * is {@link #hasPublicSuffix()}. This will return {@code true} for many domains  * which (currently) are not hosts, such as {@code "com"}), but given that any  * public suffix may become a host without warning, it is better to err on the  * side of permissiveness and thus avoid spurious rejection of valid sites.  *  *<p>During construction, names are normalized in two ways:  *<ol>  *<li>ASCII uppercase characters are converted to lowercase.  *<li>Unicode dot separators other than the ASCII period ({@code '.'}) are  * converted to the ASCII period.  *</ol>  * The normalized values will be returned from {@link #name()} and  * {@link #parts()}, and will be reflected in the result of  * {@link #equals(Object)}.  *  *<p><a href="http://en.wikipedia.org/wiki/Internationalized_domain_name">  * internationalized domain names</a> such as {@code ç½ç».cn} are supported, as  * are the equivalent<a  * href="http://en.wikipedia.org/wiki/Internationalized_domain_name">IDNA  * Punycode-encoded</a> versions.  *  * @author Craig Berry  * @since 5  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|InternetDomainName
specifier|public
class|class
name|InternetDomainName
block|{
DECL|field|DOTS_MATCHER
specifier|private
specifier|static
specifier|final
name|CharMatcher
name|DOTS_MATCHER
init|=
name|CharMatcher
operator|.
name|anyOf
argument_list|(
literal|".\u3002\uFF0E\uFF61"
argument_list|)
decl_stmt|;
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
comment|/**    * Maximum parts (labels) in a domain name. This value arises from    * the 255-octet limit described in    *<a href="http://www.ietf.org/rfc/rfc2181.txt">RFC 2181</a> part 11 with    * the fact that the encoding of each part occupies at least two bytes    * (dot plus label externally, length byte plus label internally). Thus, if    * all labels have the minimum size of one byte, 127 of them will fit.    */
DECL|field|MAX_PARTS
specifier|private
specifier|static
specifier|final
name|int
name|MAX_PARTS
init|=
literal|127
decl_stmt|;
comment|/**    * Maximum length of a full domain name, including separators, and    * leaving room for the root label. See    *<a href="http://www.ietf.org/rfc/rfc2181.txt">RFC 2181</a> part 11.    */
DECL|field|MAX_LENGTH
specifier|private
specifier|static
specifier|final
name|int
name|MAX_LENGTH
init|=
literal|253
decl_stmt|;
comment|/**    * Maximum size of a single part of a domain name. See    *<a href="http://www.ietf.org/rfc/rfc2181.txt">RFC 2181</a> part 11.    */
DECL|field|MAX_DOMAIN_PART_LENGTH
specifier|private
specifier|static
specifier|final
name|int
name|MAX_DOMAIN_PART_LENGTH
init|=
literal|63
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
comment|/**    * Private constructor used to implement {@link #fromLenient(String)}.    */
DECL|method|InternetDomainName (String name)
specifier|private
name|InternetDomainName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
comment|// Normalize:
comment|// * ASCII characters to lowercase
comment|// * All dot-like characters to '.'
comment|// * Strip trailing '.'
name|name
operator|=
name|Ascii
operator|.
name|toLowerCase
argument_list|(
name|DOTS_MATCHER
operator|.
name|replaceFrom
argument_list|(
name|name
argument_list|,
literal|'.'
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|checkArgument
argument_list|(
name|name
operator|.
name|length
argument_list|()
operator|<=
name|MAX_LENGTH
argument_list|,
literal|"Domain name too long: '%s':"
argument_list|,
name|name
argument_list|)
expr_stmt|;
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
name|parts
operator|.
name|size
argument_list|()
operator|<=
name|MAX_PARTS
argument_list|,
literal|"Domain has too many parts: '%s'"
argument_list|,
name|name
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
comment|/**    * Returns the index of the leftmost part of the public suffix, or -1 if not    * found. Note that the value defined as the "public suffix" may not be a    * public suffix according to {@link #isPublicSuffix()} if the domain ends    * with an excluded domain pattern such as {@code "nhs.uk"}.    */
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
name|TldPatterns
operator|.
name|EXACT
operator|.
name|contains
argument_list|(
name|ancestorName
argument_list|)
condition|)
block|{
return|return
name|i
return|;
block|}
comment|// Excluded domains (e.g. !nhs.uk) use the next highest
comment|// domain as the effective public suffix (e.g. uk).
if|if
condition|(
name|TldPatterns
operator|.
name|EXCLUDED
operator|.
name|contains
argument_list|(
name|ancestorName
argument_list|)
condition|)
block|{
return|return
name|i
operator|+
literal|1
return|;
block|}
if|if
condition|(
name|matchesWildcardPublicSuffix
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
comment|/**    * Returns an instance of {@link InternetDomainName} after lenient    * validation.  Specifically, validation against<a    * href="http://www.ietf.org/rfc/rfc3490.txt">RFC 3490</a>    * ("Internationalizing Domain Names in Applications") is skipped, while    * validation against<a    * href="http://www.ietf.org/rfc/rfc1035.txt">RFC 1035</a> is relaxed in    * the following ways:    *<ul>    *<li>Any part containing non-ASCII characters is considered valid.    *<li>Underscores ('_') are permitted wherever dashes ('-') are permitted.    *<li>Parts other than the final part may start with a digit.    *</ul>    *    * @param domain A domain name (not IP address)    * @throws IllegalArgumentException if {@code name} is not syntactically valid    *     according to {@link #isValidLenient}    * @since 8 (previously named {@code from})    */
DECL|method|fromLenient (String domain)
specifier|public
specifier|static
name|InternetDomainName
name|fromLenient
parameter_list|(
name|String
name|domain
parameter_list|)
block|{
return|return
operator|new
name|InternetDomainName
argument_list|(
name|checkNotNull
argument_list|(
name|domain
argument_list|)
argument_list|)
return|;
block|}
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
comment|/*      * GWT claims to support java.lang.Character's char-classification methods,      * but it actually only works for ASCII. So for now, assume any non-ASCII      * characters are valid. The only place this seems to be documented is here:      * http://osdir.com/ml/GoogleWebToolkitContributors/2010-03/msg00178.html      *      *<p>ASCII characters in the part are expected to be valid per RFC 1035,      * with underscore also being allowed due to widespread practice.      */
name|String
name|asciiChars
init|=
name|CharMatcher
operator|.
name|ASCII
operator|.
name|retainFrom
argument_list|(
name|part
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|PART_CHAR_MATCHER
operator|.
name|matchesAllOf
argument_list|(
name|asciiChars
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// No initial or final dashes or underscores.
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
comment|/*      * Note that we allow (in contravention of a strict interpretation of the      * relevant RFCs) domain parts other than the last may begin with a digit      * (for example, "3com.com"). It's important to disallow an initial digit in      * the last part; it's the only thing that stops an IPv4 numeric address      * like 127.0.0.1 from looking like a valid domain name.      */
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
comment|/**    * Indicates whether this domain name ends in a {@linkplain #isPublicSuffix()    * public suffix}, including if it is a public suffix itself. For example,    * returns {@code true} for {@code www.google.com}, {@code foo.co.uk} and    * {@code com}, but not for {@code google} or {@code google.foo}. This is    * the recommended method for determining whether a domain is potentially an    * addressable host.    *    * @since 6    */
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
comment|/**    * Indicates whether this domain name ends in a {@linkplain #isPublicSuffix()    * public suffix}, while not being a public suffix itself. For example,    * returns {@code true} for {@code www.google.com}, {@code foo.co.uk} and    * {@code bar.ca.us}, but not for {@code google}, {@code com}, or {@code    * google.foo}.    *    *<p><b>Warning:</b> a {@code false} result from this method does not imply    * that the domain does not represent an addressable host, as many public    * suffixes are also addressable hosts. Use {@link #hasPublicSuffix()} for    * that test.    *    *<p>This method can be used to determine whether it will probably be    * possible to set cookies on the domain, though even that depends on    * individual browsers' implementations of cookie controls. See    *<a href="http://www.ietf.org/rfc/rfc2109.txt">RFC 2109</a> for details.    *    * @since 6    */
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
comment|/**    * Indicates whether this domain name is composed of exactly one subdomain    * component followed by a {@linkplain #isPublicSuffix() public suffix}. For    * example, returns {@code true} for {@code google.com} and {@code foo.co.uk},    * but not for {@code www.google.com} or {@code co.uk}.    *    *<p><b>Warning:</b> A {@code true} result from this method does not imply    * that the domain is at the highest level which is addressable as a host, as    * many public suffixes are also addressable hosts. For example, the domain    * {@code bar.uk.com} has a public suffix of {@code uk.com}, so it would    * return {@code true} from this method. But {@code uk.com} is itself an    * addressable host.    *    *<p>This method can be used to determine whether a domain is probably the    * highest level for which cookies may be set, though even that depends on    * individual browsers' implementations of cookie controls. See    *<a href="http://www.ietf.org/rfc/rfc2109.txt">RFC 2109</a> for details.    *    * @since 6    */
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
comment|/**    * Returns the portion of this domain name that is one level beneath the    * public suffix. For example, for {@code x.adwords.google.co.uk} it returns    * {@code google.co.uk}, since {@code co.uk} is a public suffix.    *    *<p>If {@link #isTopPrivateDomain()} is true, the current domain name    * instance is returned.    *    *<p>This method should not be used to determine the topmost parent domain    * which is addressable as a host, as many public suffixes are also    * addressable hosts. For example, the domain {@code foo.bar.uk.com} has    * a public suffix of {@code uk.com}, so it would return {@code bar.uk.com}    * from this method. But {@code uk.com} is itself an addressable host.    *    *<p>This method can be used to determine the probable highest level parent    * domain for which cookies may be set, though even that depends on individual    * browsers' implementations of cookie controls.    *    * @throws IllegalStateException if this domain does not end with a    *     public suffix    * @since 6    */
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
name|fromInternal
argument_list|(
name|DOT_JOINER
operator|.
name|join
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
argument_list|)
return|;
block|}
comment|/**    * Creates and returns a new {@code InternetDomainName} by prepending the    * argument and a dot to the current name. For example, {@code    * InternetDomainName.fromLenient("foo.com").child("www.bar")} returns a new    * {@code InternetDomainName} with the value {@code www.bar.foo.com}.    *    * @throws NullPointerException if leftParts is null    * @throws IllegalArgumentException if the resulting name is not valid    */
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
name|fromInternal
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
comment|/**    * Returns a new {@link InternetDomainName} instance with the given {@code    * name}, using the same validation as the instance on which it is called.    */
DECL|method|fromInternal (String name)
name|InternetDomainName
name|fromInternal
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|fromLenient
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**    * Indicates whether the argument is a syntactically valid domain name using    * lenient validation. Specifically, validation against<a    * href="http://www.ietf.org/rfc/rfc3490.txt">RFC 3490</a>    * ("Internationalizing Domain Names in Applications") is skipped.    *    *<p>The following two code snippets are equivalent:    *    *<pre>   {@code    *    *   if (InternetDomainName.isValidLenient(name)) {    *     domainName = InternetDomainName.fromLenient(name);    *   } else {    *     domainName = DEFAULT_DOMAIN;    *   }}</pre>    *    *<pre>   {@code    *    *   try {    *     domainName = InternetDomainName.fromLenient(name);    *   } catch (IllegalArgumentException e) {    *     domainName = DEFAULT_DOMAIN;    *   }}</pre>    *    * The latter form is preferred as it avoids doing validation twice.    *    * @since 8 (previously named {@code isValid})    */
DECL|method|isValidLenient (String name)
specifier|public
specifier|static
name|boolean
name|isValidLenient
parameter_list|(
name|String
name|name
parameter_list|)
block|{
try|try
block|{
name|fromLenient
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
comment|/**    * Does the domain name match one of the "wildcard" patterns (e.g.    * {@code "*.ar"})?    */
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

