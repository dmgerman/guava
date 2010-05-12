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
name|Preconditions
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
comment|/**  * An immutable well-formed internet domain name, as defined by  *<a href="http://www.ietf.org/rfc/rfc1035.txt">RFC 1035</a>, with the  * exception that names ending in "." are not supported (as they are not  * generally used in browsers, email, and other end-user applications.  Examples  * include {@code com} and {@code foo.co.uk}.  Only syntactic analysis is  * performed; no DNS lookups or other network interactions take place.  Thus  * there is no guarantee that the domain actually exists on the internet.  * Invalid domain names throw {@link IllegalArgumentException} on construction.  *  *<p>It is often the case that domains of interest are those under  * {@linkplain #isRecognizedTld() TLD}s but not themselves  * {@linkplain #isRecognizedTld() TLD}s; {@link #hasRecognizedTld()} and  * {@link #isImmediatelyUnderTld()} test for this. Similarly, one  * often needs to obtain the domain consisting of the  * {@linkplain #isRecognizedTld() TLD} plus one subdomain level, typically  * to obtain the highest-level domain for which cookies may be set.  * Use {@link #topCookieDomain()} for this purpose.  *  *<p>{@linkplain #equals(Object) Equality} of domain names is case-insensitive,  * so for convenience, the {@link #name()} and {@link #parts()} methods  * return the lower-case form of the name.  *  *<p>{@linkplain #isRecognizedTld() TLD} identification is done by reference  * to the generated Java class {@link TldPatterns}, which is in turn derived  * from a Mozilla-supplied text file listing known  * {@linkplain #isRecognizedTld() TLD} patterns.  *  *<p>Note that  *<a href="http://en.wikipedia.org/wiki/Internationalized_domain_name">  * internationalized domain names (IDN)</a> are not supported.  If IDN is  * required, the {@code ToASCII} transformation (described in the referenced  * page) should be applied to the domain string before it is provided to this  * class.  *  * @author Craig Berry  * @since 5  */
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
comment|/**    * Value of {@link #tldIndex} which indicates that no TLD was found.    */
DECL|field|NO_TLD_FOUND
specifier|private
specifier|static
specifier|final
name|int
name|NO_TLD_FOUND
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
comment|/**    * The index in the {@link #parts} list at which the TLD begins.  For    * example, for the domain name {@code www.google.co.uk}, the value would    * be 2 (the index of the {@code co} part).  The value is negative    * (specifically, {@link #NO_TLD_FOUND}) if no TLD was found.    */
DECL|field|tldIndex
specifier|private
specifier|final
name|int
name|tldIndex
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
name|Preconditions
operator|.
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
name|tldIndex
operator|=
name|findTld
argument_list|()
expr_stmt|;
block|}
comment|/**    * Private constructor used to implement {@link #ancestor(int)}.    * Argument parts are assumed to be valid, as they always come    * from an existing domain.    */
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
name|Preconditions
operator|.
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
name|tldIndex
operator|=
name|findTld
argument_list|()
expr_stmt|;
block|}
comment|/**    * Returns the index of the leftmost part of the TLD, or -1 if not found.    */
DECL|method|findTld ()
specifier|private
name|int
name|findTld
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
name|isTldInternal
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
name|NO_TLD_FOUND
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
comment|// Patterns used for validation of domain name components.
comment|// We use strings instead of compiled patterns to maintain GWT compatibility.
comment|// Only the intersection of Java regex and Javascript regex is supported.
DECL|field|NORMAL_PART
specifier|private
specifier|static
specifier|final
name|String
name|NORMAL_PART
init|=
literal|"[A-Za-z0-9]([A-Za-z0-9_-]*[A-Za-z0-9])?"
decl_stmt|;
DECL|field|FINAL_PART
specifier|private
specifier|static
specifier|final
name|String
name|FINAL_PART
init|=
literal|"[A-Za-z]([A-Za-z0-9_-]*[A-Za-z0-9])?"
decl_stmt|;
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
name|FINAL_PART
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
name|NORMAL_PART
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
comment|/**    * Helper method for {@link #validateSyntax(List)}.  Validates that one    * part of a domain name is valid.    *    * @param part The domain name part to be validated    * @param pattern The regex pattern against which to validate    * @return Whether the part is valid    */
DECL|method|validatePart (String part, String pattern)
specifier|private
specifier|static
name|boolean
name|validatePart
parameter_list|(
name|String
name|part
parameter_list|,
name|String
name|pattern
parameter_list|)
block|{
return|return
name|part
operator|.
name|length
argument_list|()
operator|<=
name|MAX_DOMAIN_PART_LENGTH
operator|&&
name|part
operator|.
name|matches
argument_list|(
name|pattern
argument_list|)
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
comment|/**    * Returns the parts of the domain name, normalized to all lower case.    */
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
comment|/**    * Returns {@code true} if the domain name is an<b>effective</b> top-level    * domain.  An effective TLD is a domain which is controlled by a national or    * other registrar, and which is not itself in use as a real, separately    * addressable domain name.  Addressable domains occur as subdomains of    * effective TLDs.  Examples of TLDs include {@code com}, {@code co.uk}, and    * {@code ca.us}.  Examples of non-TLDs include {@code google},    * {@code google.com}, and {@code foo.co.uk}.    *    *<p>Identification of effective TLDs is done by reference to a list of    * patterns maintained by the Mozilla project; see    *<a href="https://wiki.mozilla.org/Gecko:Effective_TLD_List">the    * Gecko:Effective TLD List project page</a> for details.    *    *<p>TODO: Note that the Mozilla TLD list does not guarantee that    * the one-part TLDs like {@code com} and {@code us} will necessarily be    * listed explicitly in the patterns file.  All of the ones required for    * proper operation of this class appear to be there in the current version of    * the file, but this might not always be the case.  We may wish to tighten    * this up by providing an auxilliary source for the canonical one-part TLDs,    * using the existing "amendment file" process or a similar mechanism.    */
DECL|method|isRecognizedTld ()
specifier|public
name|boolean
name|isRecognizedTld
parameter_list|()
block|{
return|return
name|tldIndex
operator|==
literal|0
return|;
block|}
comment|/**    * Returns {@code true} if the domain name ends in a    * {@linkplain #isRecognizedTld() TLD}, but is not a complete    * {@linkplain #isRecognizedTld() TLD} itself.  For example, returns    * {@code true} for {@code www.google.com}, {@code foo.co.uk}, and    * {@code bar.ca.us}; returns {@code false} for {@code google}, {@code com},    * and {@code google.foo}.    */
DECL|method|isUnderRecognizedTld ()
specifier|public
name|boolean
name|isUnderRecognizedTld
parameter_list|()
block|{
return|return
name|tldIndex
operator|>
literal|0
return|;
block|}
comment|/**    * Returns {@code true} if the domain name ends in a    * {@linkplain #isRecognizedTld() TLD}, or is a complete    * {@linkplain #isRecognizedTld() TLD} itself.  For example, returns    * {@code true} for {@code www.google.com}, {@code foo.co.uk}, and    * {@code com}; returns {@code false} for {@code google} and    * {@code google.foo}.    */
DECL|method|hasRecognizedTld ()
specifier|public
name|boolean
name|hasRecognizedTld
parameter_list|()
block|{
return|return
name|tldIndex
operator|!=
name|NO_TLD_FOUND
return|;
block|}
comment|/**    * Returns the {@linkplain #isRecognizedTld() TLD} portion of the    * domain name, or null if no TLD is present according to    * {@link #hasRecognizedTld()}.    */
DECL|method|recognizedTld ()
specifier|public
name|InternetDomainName
name|recognizedTld
parameter_list|()
block|{
return|return
name|hasRecognizedTld
argument_list|()
condition|?
name|ancestor
argument_list|(
name|tldIndex
argument_list|)
else|:
literal|null
return|;
block|}
comment|/**    * Returns {@code true} if the domain name is an immediate subdomain of a    * {@linkplain #isRecognizedTld() TLD}, but is not a    * {@linkplain #isRecognizedTld() TLD} itself. For example, returns    * {@code true} for {@code google.com} and {@code foo.co.uk}; returns    * {@code false} for {@code www.google.com} and {@code co.uk}.    */
DECL|method|isImmediatelyUnderTld ()
specifier|public
name|boolean
name|isImmediatelyUnderTld
parameter_list|()
block|{
return|return
name|tldIndex
operator|==
literal|1
return|;
block|}
comment|/**    * Returns the rightmost non-{@linkplain #isRecognizedTld() TLD} domain name    * part.  For example    * {@code new InternetDomainName("www.google.com").rightmostNonTldPart()}    * returns {@code "google"}.  Returns null if either no    * {@linkplain #isRecognizedTld() TLD} is found, or the whole domain name is    * itself a {@linkplain #isRecognizedTld() TLD}.    */
DECL|method|rightmostNonTldPart ()
specifier|public
name|String
name|rightmostNonTldPart
parameter_list|()
block|{
return|return
name|tldIndex
operator|>=
literal|1
condition|?
name|parts
operator|.
name|get
argument_list|(
name|tldIndex
operator|-
literal|1
argument_list|)
else|:
literal|null
return|;
block|}
comment|/**    * Returns the "top cookie domain" for the {@code InternetDomainName}.    * This is defined as the domain consisting of the    * {@linkplain #isRecognizedTld() TLD} plus one subdomain level. This    * is the highest-level parent of this domain for which cookies may be set,    * as cookies cannot be set on {@code TLD}s themselves. Note that this    * information has non-cookie-related uses as well, but determining the    * cookie domain is the most common.    *    *<p>If called on a domain for which {@link #isImmediatelyUnderTld()} is    * {@code true}, this is an identity operation which returns the existing    * object.    *    * @throws IllegalStateException if the domain is not under a recognized TLD.    */
DECL|method|topCookieDomain ()
specifier|public
name|InternetDomainName
name|topCookieDomain
parameter_list|()
block|{
if|if
condition|(
name|isImmediatelyUnderTld
argument_list|()
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
operator|!
name|isUnderRecognizedTld
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Not under TLD: "
operator|+
name|name
argument_list|)
throw|;
block|}
return|return
name|ancestor
argument_list|(
name|tldIndex
operator|-
literal|1
argument_list|)
return|;
block|}
comment|/**    * Does this domain have a parent domain?  That is, does it have two or more    * parts?    */
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
comment|/**    * Create a new {@code InternetDomainName} which is the parent of this one;    * that is, the parent domain is the current domain with the leftmost part    * removed.  For example,    * {@code new InternetDomainName("www.google.com").parent()} returns    * a new {@code InternetDomainName} corresponding to the value    * {@code "google.com"}.    *    * @throws IllegalStateException if the domain has no parent    */
DECL|method|parent ()
specifier|public
name|InternetDomainName
name|parent
parameter_list|()
block|{
name|Preconditions
operator|.
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
comment|/**    * Returns the ancestor of the current domain at the given number of levels    * "higher" (rightward) in the subdomain list. The number of levels must    * be non-negative, and less than {@code N-1}, where {@code N} is the number    * of parts in the domain.    *    *<p>TODO: Reasonable candidate for addition to public API.    */
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
comment|/**    * Creates and returns a new {@code InternetDomainName} by prepending the    * argument and a dot to the current name.  For example,    * {@code InternetDomainName.from("foo.com").child("www.bar")} returns a    * new {@code InternetDomainName} with the value {@code www.bar.foo.com}.    *    * @throws NullPointerException if leftParts is null    * @throws IllegalArgumentException if the resulting name is not valid    */
DECL|method|child (String leftParts)
specifier|public
name|InternetDomainName
name|child
parameter_list|(
name|String
name|leftParts
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|leftParts
argument_list|)
expr_stmt|;
return|return
name|InternetDomainName
operator|.
name|from
argument_list|(
name|leftParts
operator|+
literal|"."
operator|+
name|name
argument_list|)
return|;
block|}
comment|/**    * Determines whether the argument is a syntactically valid domain name.    * This method is intended for the case where a {@link String} must be    * validated as a valid domain name, but no further work with that    * {@link String} as an {@link InternetDomainName} will be required.  Code    * like the following will unnecessarily repeat the work of validation:    *<pre>    *   if (InternetDomainName.isValid(name)) {    *     domainName = InternetDomainName.from(name);    *   } else {    *     domainName = DEFAULT_DOMAIN;    *   }    *</pre>    * Such code should instead be written as follows:    *<pre>    *   try {    *     domainName = InternetDomainName.from(name);    *   } catch (IllegalArgumentException e) {    *     domainName = DEFAULT_DOMAIN;    *   }    *</pre>    */
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
comment|/**    * Does the domain name satisfy the Mozilla criteria for an effective    * {@linkplain #isRecognizedTld() TLD}?    */
DECL|method|isTldInternal (String domain)
specifier|private
specifier|static
name|boolean
name|isTldInternal
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
name|isSubTld
argument_list|(
name|domain
argument_list|)
operator|)
return|;
block|}
comment|/**    * Does the domain name match one of the "under" patterns (e.g. "*.ar")?    */
DECL|method|isSubTld (String domain)
specifier|private
specifier|static
name|boolean
name|isSubTld
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

