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
name|annotations
operator|.
name|GwtIncompatible
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
name|common
operator|.
name|collect
operator|.
name|ImmutableList
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
name|Iterables
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
name|testing
operator|.
name|EqualsTester
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
name|testing
operator|.
name|NullPointerTester
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_comment
comment|/**  * {@link TestCase} for {@link InternetDomainName}.  *  * @author Craig Berry  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|InternetDomainNameTest
specifier|public
specifier|final
class|class
name|InternetDomainNameTest
extends|extends
name|TestCase
block|{
DECL|field|UNICODE_EXAMPLE
specifier|private
specifier|static
specifier|final
name|InternetDomainName
name|UNICODE_EXAMPLE
init|=
name|InternetDomainName
operator|.
name|from
argument_list|(
literal|"j\u00f8rpeland.no"
argument_list|)
decl_stmt|;
DECL|field|PUNYCODE_EXAMPLE
specifier|private
specifier|static
specifier|final
name|InternetDomainName
name|PUNYCODE_EXAMPLE
init|=
name|InternetDomainName
operator|.
name|from
argument_list|(
literal|"xn--jrpeland-54a.no"
argument_list|)
decl_stmt|;
comment|/**    * The Greek letter delta, used in unicode testing.    */
DECL|field|DELTA
specifier|private
specifier|static
specifier|final
name|String
name|DELTA
init|=
literal|"\u0394"
decl_stmt|;
comment|/**    * A domain part which is valid under lenient validation, but invalid under    * strict validation.    */
DECL|field|LOTS_OF_DELTAS
specifier|static
specifier|final
name|String
name|LOTS_OF_DELTAS
init|=
name|Strings
operator|.
name|repeat
argument_list|(
name|DELTA
argument_list|,
literal|62
argument_list|)
decl_stmt|;
DECL|field|ALMOST_TOO_MANY_LEVELS
specifier|private
specifier|static
specifier|final
name|String
name|ALMOST_TOO_MANY_LEVELS
init|=
name|Strings
operator|.
name|repeat
argument_list|(
literal|"a."
argument_list|,
literal|127
argument_list|)
decl_stmt|;
DECL|field|ALMOST_TOO_LONG
specifier|private
specifier|static
specifier|final
name|String
name|ALMOST_TOO_LONG
init|=
name|Strings
operator|.
name|repeat
argument_list|(
literal|"aaaaa."
argument_list|,
literal|40
argument_list|)
operator|+
literal|"1234567890.c"
decl_stmt|;
DECL|field|VALID_NAME
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|VALID_NAME
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"foo.com"
argument_list|,
literal|"f-_-o.cOM"
argument_list|,
literal|"f--1.com"
argument_list|,
literal|"f11-1.com"
argument_list|,
literal|"www"
argument_list|,
literal|"abc.a23"
argument_list|,
literal|"biz.com.ua"
argument_list|,
literal|"x"
argument_list|,
literal|"fOo"
argument_list|,
literal|"f--o"
argument_list|,
literal|"f_a"
argument_list|,
literal|"foo.net.us\uFF61ocm"
argument_list|,
literal|"woo.com."
argument_list|,
literal|"a"
operator|+
name|DELTA
operator|+
literal|"b.com"
argument_list|,
name|ALMOST_TOO_MANY_LEVELS
argument_list|,
name|ALMOST_TOO_LONG
argument_list|)
decl_stmt|;
DECL|field|INVALID_NAME
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|INVALID_NAME
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|""
argument_list|,
literal|" "
argument_list|,
literal|"127.0.0.1"
argument_list|,
literal|"::1"
argument_list|,
literal|"13"
argument_list|,
literal|"abc.12c"
argument_list|,
literal|"foo-.com"
argument_list|,
literal|"_bar.quux"
argument_list|,
literal|"foo+bar.com"
argument_list|,
literal|"foo!bar.com"
argument_list|,
literal|".foo.com"
argument_list|,
literal|"..bar.com"
argument_list|,
literal|"baz..com"
argument_list|,
literal|"..quiffle.com"
argument_list|,
literal|"fleeb.com.."
argument_list|,
literal|"."
argument_list|,
literal|".."
argument_list|,
literal|"..."
argument_list|,
literal|"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.com"
argument_list|,
literal|"a"
operator|+
name|DELTA
operator|+
literal|" .com"
argument_list|,
name|ALMOST_TOO_MANY_LEVELS
operator|+
literal|"com"
argument_list|,
name|ALMOST_TOO_LONG
operator|+
literal|".c"
argument_list|)
decl_stmt|;
DECL|field|PS
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|PS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"com"
argument_list|,
literal|"co.uk"
argument_list|,
literal|"foo.ar"
argument_list|,
literal|"xxxxxx.ar"
argument_list|,
literal|"org.mK"
argument_list|,
literal|"us"
argument_list|,
literal|"uk\uFF61com."
argument_list|,
comment|// Alternate dot character
literal|"\u7f51\u7edc.Cn"
argument_list|,
comment|// "ç½ç».Cn"
literal|"j\u00f8rpeland.no"
argument_list|,
comment|// "jorpeland.no" (first o slashed)
literal|"xn--jrpeland-54a.no"
comment|// IDNA (punycode) encoding of above
argument_list|)
decl_stmt|;
DECL|field|NO_PS
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|NO_PS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"www"
argument_list|,
literal|"foo.google"
argument_list|,
literal|"x.y.z"
argument_list|)
decl_stmt|;
DECL|field|NON_PS
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|NON_PS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"foo.bar.com"
argument_list|,
literal|"foo.ca"
argument_list|,
literal|"foo.bar.ca"
argument_list|,
literal|"foo.bar.co.il"
argument_list|,
literal|"state.CA.us"
argument_list|,
literal|"www.state.pa.us"
argument_list|,
literal|"pvt.k12.ca.us"
argument_list|,
literal|"www.google.com"
argument_list|,
literal|"www4.yahoo.co.uk"
argument_list|,
literal|"home.netscape.com"
argument_list|,
literal|"web.MIT.edu"
argument_list|,
literal|"foo.eDu.au"
argument_list|,
literal|"utenti.blah.IT"
argument_list|,
literal|"dominio.com.co"
argument_list|)
decl_stmt|;
DECL|field|TOP_PRIVATE_DOMAIN
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|TOP_PRIVATE_DOMAIN
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"google.com"
argument_list|,
literal|"foo.Co.uk"
argument_list|,
literal|"foo.ca.us."
argument_list|)
decl_stmt|;
DECL|field|UNDER_PRIVATE_DOMAIN
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|UNDER_PRIVATE_DOMAIN
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"foo.bar.google.com"
argument_list|,
literal|"a.b.co.uk"
argument_list|,
literal|"x.y.ca.us"
argument_list|)
decl_stmt|;
DECL|field|VALID_IP_ADDRS
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|VALID_IP_ADDRS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"1.2.3.4"
argument_list|,
literal|"127.0.0.1"
argument_list|,
literal|"::1"
argument_list|,
literal|"2001:db8::1"
argument_list|)
decl_stmt|;
DECL|field|INVALID_IP_ADDRS
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|INVALID_IP_ADDRS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|""
argument_list|,
literal|"1"
argument_list|,
literal|"1.2.3"
argument_list|,
literal|"..."
argument_list|,
literal|"1.2.3.4.5"
argument_list|,
literal|"400.500.600.700"
argument_list|,
literal|":"
argument_list|,
literal|":::1"
argument_list|,
literal|"2001:db8:"
argument_list|)
decl_stmt|;
DECL|field|SOMEWHERE_UNDER_PS
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|SOMEWHERE_UNDER_PS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"foo.bar.google.com"
argument_list|,
literal|"a.b.c.1.2.3.ca.us"
argument_list|,
literal|"site.jp"
argument_list|,
literal|"uomi-online.kir.jp"
argument_list|,
literal|"jprs.co.jp"
argument_list|,
literal|"site.quick.jp"
argument_list|,
literal|"site.tenki.jp"
argument_list|,
literal|"site.or.jp"
argument_list|,
literal|"site.gr.jp"
argument_list|,
literal|"site.ne.jp"
argument_list|,
literal|"site.ac.jp"
argument_list|,
literal|"site.ad.jp"
argument_list|,
literal|"site.ed.jp"
argument_list|,
literal|"site.geo.jp"
argument_list|,
literal|"site.go.jp"
argument_list|,
literal|"site.lg.jp"
argument_list|,
literal|"1.fm"
argument_list|,
literal|"site.cc"
argument_list|,
literal|"site.ee"
argument_list|,
literal|"site.fi"
argument_list|,
literal|"site.fm"
argument_list|,
literal|"site.gr"
argument_list|,
literal|"www.leguide.ma"
argument_list|,
literal|"site.ma"
argument_list|,
literal|"some.org.mk"
argument_list|,
literal|"site.mk"
argument_list|,
literal|"site.tv"
argument_list|,
literal|"site.us"
argument_list|,
literal|"www.odev.us"
argument_list|,
literal|"www.GOOGLE.com"
argument_list|,
literal|"www.com"
argument_list|,
literal|"google.com"
argument_list|,
literal|"www7.google.co.uk"
argument_list|,
literal|"google.Co.uK"
argument_list|,
literal|"jobs.kt.com."
argument_list|,
literal|"home.netscape.com"
argument_list|,
literal|"web.stanford.edu"
argument_list|,
literal|"stanford.edu"
argument_list|,
literal|"state.ca.us"
argument_list|,
literal|"www.state.ca.us"
argument_list|,
literal|"state.ca.us"
argument_list|,
literal|"pvt.k12.ca.us"
argument_list|,
literal|"www.rave.ca."
argument_list|,
literal|"cnn.ca"
argument_list|,
literal|"ledger-enquirer.com"
argument_list|,
literal|"it-trace.ch"
argument_list|,
literal|"cool.dk"
argument_list|,
literal|"cool.co.uk"
argument_list|,
literal|"cool.de"
argument_list|,
literal|"cool.es"
argument_list|,
literal|"cool\uFF61fr"
argument_list|,
comment|// Alternate dot character
literal|"cool.nl"
argument_list|,
literal|"members.blah.nl."
argument_list|,
literal|"cool.se"
argument_list|,
literal|"utenti.blah.it"
argument_list|,
literal|"kt.co"
argument_list|,
literal|"a\u7f51\u7edcA.\u7f51\u7edc.Cn"
comment|// "aç½ç»A.ç½ç».Cn"
argument_list|)
decl_stmt|;
DECL|method|testValid ()
specifier|public
name|void
name|testValid
parameter_list|()
block|{
for|for
control|(
name|String
name|name
range|:
name|VALID_NAME
control|)
block|{
name|InternetDomainName
operator|.
name|from
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testInvalid ()
specifier|public
name|void
name|testInvalid
parameter_list|()
block|{
for|for
control|(
name|String
name|name
range|:
name|INVALID_NAME
control|)
block|{
try|try
block|{
name|InternetDomainName
operator|.
name|from
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have been invalid: '"
operator|+
name|name
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{
comment|// Expected case
block|}
block|}
block|}
DECL|method|testPublicSuffix ()
specifier|public
name|void
name|testPublicSuffix
parameter_list|()
block|{
for|for
control|(
name|String
name|name
range|:
name|PS
control|)
block|{
specifier|final
name|InternetDomainName
name|domain
init|=
name|InternetDomainName
operator|.
name|from
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|name
argument_list|,
name|domain
operator|.
name|isPublicSuffix
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|name
argument_list|,
name|domain
operator|.
name|hasPublicSuffix
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|name
argument_list|,
name|domain
operator|.
name|isUnderPublicSuffix
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|name
argument_list|,
name|domain
operator|.
name|isTopPrivateDomain
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|domain
argument_list|,
name|domain
operator|.
name|publicSuffix
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|name
range|:
name|NO_PS
control|)
block|{
specifier|final
name|InternetDomainName
name|domain
init|=
name|InternetDomainName
operator|.
name|from
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|name
argument_list|,
name|domain
operator|.
name|isPublicSuffix
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|name
argument_list|,
name|domain
operator|.
name|hasPublicSuffix
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|name
argument_list|,
name|domain
operator|.
name|isUnderPublicSuffix
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|name
argument_list|,
name|domain
operator|.
name|isTopPrivateDomain
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|domain
operator|.
name|publicSuffix
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|name
range|:
name|NON_PS
control|)
block|{
specifier|final
name|InternetDomainName
name|domain
init|=
name|InternetDomainName
operator|.
name|from
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|name
argument_list|,
name|domain
operator|.
name|isPublicSuffix
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|name
argument_list|,
name|domain
operator|.
name|hasPublicSuffix
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|name
argument_list|,
name|domain
operator|.
name|isUnderPublicSuffix
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testUnderPublicSuffix ()
specifier|public
name|void
name|testUnderPublicSuffix
parameter_list|()
block|{
for|for
control|(
name|String
name|name
range|:
name|SOMEWHERE_UNDER_PS
control|)
block|{
specifier|final
name|InternetDomainName
name|domain
init|=
name|InternetDomainName
operator|.
name|from
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|name
argument_list|,
name|domain
operator|.
name|isPublicSuffix
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|name
argument_list|,
name|domain
operator|.
name|hasPublicSuffix
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|name
argument_list|,
name|domain
operator|.
name|isUnderPublicSuffix
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testTopPrivateDomain ()
specifier|public
name|void
name|testTopPrivateDomain
parameter_list|()
block|{
for|for
control|(
name|String
name|name
range|:
name|TOP_PRIVATE_DOMAIN
control|)
block|{
specifier|final
name|InternetDomainName
name|domain
init|=
name|InternetDomainName
operator|.
name|from
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|name
argument_list|,
name|domain
operator|.
name|isPublicSuffix
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|name
argument_list|,
name|domain
operator|.
name|hasPublicSuffix
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|name
argument_list|,
name|domain
operator|.
name|isUnderPublicSuffix
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|name
argument_list|,
name|domain
operator|.
name|isTopPrivateDomain
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|domain
operator|.
name|parent
argument_list|()
argument_list|,
name|domain
operator|.
name|publicSuffix
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testUnderPrivateDomain ()
specifier|public
name|void
name|testUnderPrivateDomain
parameter_list|()
block|{
for|for
control|(
name|String
name|name
range|:
name|UNDER_PRIVATE_DOMAIN
control|)
block|{
specifier|final
name|InternetDomainName
name|domain
init|=
name|InternetDomainName
operator|.
name|from
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|name
argument_list|,
name|domain
operator|.
name|isPublicSuffix
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|name
argument_list|,
name|domain
operator|.
name|hasPublicSuffix
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|name
argument_list|,
name|domain
operator|.
name|isUnderPublicSuffix
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|name
argument_list|,
name|domain
operator|.
name|isTopPrivateDomain
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testParent ()
specifier|public
name|void
name|testParent
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"com"
argument_list|,
name|InternetDomainName
operator|.
name|from
argument_list|(
literal|"google.com"
argument_list|)
operator|.
name|parent
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"uk"
argument_list|,
name|InternetDomainName
operator|.
name|from
argument_list|(
literal|"co.uk"
argument_list|)
operator|.
name|parent
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"google.com"
argument_list|,
name|InternetDomainName
operator|.
name|from
argument_list|(
literal|"www.google.com"
argument_list|)
operator|.
name|parent
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|InternetDomainName
operator|.
name|from
argument_list|(
literal|"com"
argument_list|)
operator|.
name|parent
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"'com' should throw ISE on .parent() call"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testChild ()
specifier|public
name|void
name|testChild
parameter_list|()
block|{
name|InternetDomainName
name|domain
init|=
name|InternetDomainName
operator|.
name|from
argument_list|(
literal|"foo.com"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"www.foo.com"
argument_list|,
name|domain
operator|.
name|child
argument_list|(
literal|"www"
argument_list|)
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|domain
operator|.
name|child
argument_list|(
literal|"www."
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"www..google.com should have been invalid"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{
comment|// Expected outcome
block|}
block|}
DECL|method|testParentChild ()
specifier|public
name|void
name|testParentChild
parameter_list|()
block|{
name|InternetDomainName
name|origin
init|=
name|InternetDomainName
operator|.
name|from
argument_list|(
literal|"foo.com"
argument_list|)
decl_stmt|;
name|InternetDomainName
name|parent
init|=
name|origin
operator|.
name|parent
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"com"
argument_list|,
name|parent
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
comment|// These would throw an exception if leniency were not preserved during parent() and child()
comment|// calls.
name|InternetDomainName
name|child
init|=
name|parent
operator|.
name|child
argument_list|(
name|LOTS_OF_DELTAS
argument_list|)
decl_stmt|;
name|child
operator|.
name|child
argument_list|(
name|LOTS_OF_DELTAS
argument_list|)
expr_stmt|;
block|}
DECL|method|testValidTopPrivateDomain ()
specifier|public
name|void
name|testValidTopPrivateDomain
parameter_list|()
block|{
name|InternetDomainName
name|googleDomain
init|=
name|InternetDomainName
operator|.
name|from
argument_list|(
literal|"google.com"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|googleDomain
argument_list|,
name|googleDomain
operator|.
name|topPrivateDomain
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|googleDomain
argument_list|,
name|googleDomain
operator|.
name|child
argument_list|(
literal|"mail"
argument_list|)
operator|.
name|topPrivateDomain
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|googleDomain
argument_list|,
name|googleDomain
operator|.
name|child
argument_list|(
literal|"foo.bar"
argument_list|)
operator|.
name|topPrivateDomain
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testInvalidTopPrivateDomain ()
specifier|public
name|void
name|testInvalidTopPrivateDomain
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|badCookieDomains
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"co.uk"
argument_list|,
literal|"foo"
argument_list|,
literal|"com"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|domain
range|:
name|badCookieDomains
control|)
block|{
try|try
block|{
name|InternetDomainName
operator|.
name|from
argument_list|(
name|domain
argument_list|)
operator|.
name|topPrivateDomain
argument_list|()
expr_stmt|;
name|fail
argument_list|(
name|domain
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|expected
parameter_list|)
block|{       }
block|}
block|}
DECL|method|testIsValid ()
specifier|public
name|void
name|testIsValid
parameter_list|()
block|{
specifier|final
name|Iterable
argument_list|<
name|String
argument_list|>
name|validCases
init|=
name|Iterables
operator|.
name|concat
argument_list|(
name|VALID_NAME
argument_list|,
name|PS
argument_list|,
name|NO_PS
argument_list|,
name|NON_PS
argument_list|)
decl_stmt|;
specifier|final
name|Iterable
argument_list|<
name|String
argument_list|>
name|invalidCases
init|=
name|Iterables
operator|.
name|concat
argument_list|(
name|INVALID_NAME
argument_list|,
name|VALID_IP_ADDRS
argument_list|,
name|INVALID_IP_ADDRS
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|valid
range|:
name|validCases
control|)
block|{
name|assertTrue
argument_list|(
name|valid
argument_list|,
name|InternetDomainName
operator|.
name|isValid
argument_list|(
name|valid
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|invalid
range|:
name|invalidCases
control|)
block|{
name|assertFalse
argument_list|(
name|invalid
argument_list|,
name|InternetDomainName
operator|.
name|isValid
argument_list|(
name|invalid
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// TODO(hhchan): Resurrect this test after removing the reference to
comment|// String.toLowerCase(Locale)
annotation|@
name|GwtIncompatible
argument_list|(
literal|"String.toLowerCase(Locale)"
argument_list|)
DECL|method|testName ()
specifier|public
name|void
name|testName
parameter_list|()
block|{
for|for
control|(
name|String
name|inputName
range|:
name|SOMEWHERE_UNDER_PS
control|)
block|{
name|InternetDomainName
name|domain
init|=
name|InternetDomainName
operator|.
name|from
argument_list|(
name|inputName
argument_list|)
decl_stmt|;
comment|/*        * We would ordinarily use constants for the expected results, but        * doing it by derivation allows us to reuse the test case definitions        * used in other tests.        */
name|String
name|expectedName
init|=
name|inputName
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
decl_stmt|;
name|expectedName
operator|=
name|expectedName
operator|.
name|replaceAll
argument_list|(
literal|"[\u3002\uFF0E\uFF61]"
argument_list|,
literal|"."
argument_list|)
expr_stmt|;
if|if
condition|(
name|expectedName
operator|.
name|endsWith
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
name|expectedName
operator|=
name|expectedName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|expectedName
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|expectedName
argument_list|,
name|domain
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testExclusion ()
specifier|public
name|void
name|testExclusion
parameter_list|()
block|{
name|InternetDomainName
name|domain
init|=
name|InternetDomainName
operator|.
name|from
argument_list|(
literal|"foo.nic.uk"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|domain
operator|.
name|hasPublicSuffix
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"uk"
argument_list|,
name|domain
operator|.
name|publicSuffix
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
comment|// Behold the weirdness!
name|assertFalse
argument_list|(
name|domain
operator|.
name|publicSuffix
argument_list|()
operator|.
name|isPublicSuffix
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testEquality ()
specifier|public
name|void
name|testEquality
parameter_list|()
block|{
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|idn
argument_list|(
literal|"google.com"
argument_list|)
argument_list|,
name|idn
argument_list|(
literal|"google.com"
argument_list|)
argument_list|,
name|idn
argument_list|(
literal|"GOOGLE.COM"
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|idn
argument_list|(
literal|"www.google.com"
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|UNICODE_EXAMPLE
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|PUNYCODE_EXAMPLE
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|idn (String domain)
specifier|private
specifier|static
name|InternetDomainName
name|idn
parameter_list|(
name|String
name|domain
parameter_list|)
block|{
return|return
name|InternetDomainName
operator|.
name|from
argument_list|(
name|domain
argument_list|)
return|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"NullPointerTester"
argument_list|)
DECL|method|testNulls ()
specifier|public
name|void
name|testNulls
parameter_list|()
block|{
specifier|final
name|NullPointerTester
name|tester
init|=
operator|new
name|NullPointerTester
argument_list|()
decl_stmt|;
name|tester
operator|.
name|testAllPublicStaticMethods
argument_list|(
name|InternetDomainName
operator|.
name|class
argument_list|)
expr_stmt|;
name|tester
operator|.
name|testAllPublicInstanceMethods
argument_list|(
name|InternetDomainName
operator|.
name|from
argument_list|(
literal|"google.com"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

