begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2011 Google Inc. All Rights Reserved.
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
name|Charsets
operator|.
name|UTF_16
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
name|Charsets
operator|.
name|UTF_8
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
name|net
operator|.
name|MediaType
operator|.
name|*
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
name|Optional
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
name|ImmutableListMultimap
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
name|ImmutableMultimap
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
name|nio
operator|.
name|charset
operator|.
name|IllegalCharsetNameException
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
name|UnsupportedCharsetException
import|;
end_import

begin_comment
comment|/**  * Tests for {@link MediaType}.  *  * @author Gregory Kick  */
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
DECL|class|MediaTypeTest
specifier|public
class|class
name|MediaTypeTest
extends|extends
name|TestCase
block|{
DECL|method|testCreate_invalidType ()
specifier|public
name|void
name|testCreate_invalidType
parameter_list|()
block|{
try|try
block|{
name|MediaType
operator|.
name|create
argument_list|(
literal|"te><t"
argument_list|,
literal|"plaintext"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testCreate_invalidSubtype ()
specifier|public
name|void
name|testCreate_invalidSubtype
parameter_list|()
block|{
try|try
block|{
name|MediaType
operator|.
name|create
argument_list|(
literal|"text"
argument_list|,
literal|"pl@intext"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testCreate_wildcardTypeDeclaredSubtype ()
specifier|public
name|void
name|testCreate_wildcardTypeDeclaredSubtype
parameter_list|()
block|{
try|try
block|{
name|MediaType
operator|.
name|create
argument_list|(
literal|"*"
argument_list|,
literal|"text"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testCreateApplicationType ()
specifier|public
name|void
name|testCreateApplicationType
parameter_list|()
block|{
name|MediaType
name|newType
init|=
name|MediaType
operator|.
name|createApplicationType
argument_list|(
literal|"yams"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"application"
argument_list|,
name|newType
operator|.
name|type
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"yams"
argument_list|,
name|newType
operator|.
name|subtype
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateAudioType ()
specifier|public
name|void
name|testCreateAudioType
parameter_list|()
block|{
name|MediaType
name|newType
init|=
name|MediaType
operator|.
name|createAudioType
argument_list|(
literal|"yams"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"audio"
argument_list|,
name|newType
operator|.
name|type
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"yams"
argument_list|,
name|newType
operator|.
name|subtype
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateImageType ()
specifier|public
name|void
name|testCreateImageType
parameter_list|()
block|{
name|MediaType
name|newType
init|=
name|MediaType
operator|.
name|createImageType
argument_list|(
literal|"yams"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"image"
argument_list|,
name|newType
operator|.
name|type
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"yams"
argument_list|,
name|newType
operator|.
name|subtype
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateTextType ()
specifier|public
name|void
name|testCreateTextType
parameter_list|()
block|{
name|MediaType
name|newType
init|=
name|MediaType
operator|.
name|createTextType
argument_list|(
literal|"yams"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"text"
argument_list|,
name|newType
operator|.
name|type
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"yams"
argument_list|,
name|newType
operator|.
name|subtype
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateVideoType ()
specifier|public
name|void
name|testCreateVideoType
parameter_list|()
block|{
name|MediaType
name|newType
init|=
name|MediaType
operator|.
name|createVideoType
argument_list|(
literal|"yams"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"video"
argument_list|,
name|newType
operator|.
name|type
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"yams"
argument_list|,
name|newType
operator|.
name|subtype
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetType ()
specifier|public
name|void
name|testGetType
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"text"
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|type
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"application"
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"application/atom+xml; charset=utf-8"
argument_list|)
operator|.
name|type
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetSubtype ()
specifier|public
name|void
name|testGetSubtype
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"plain"
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|subtype
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"atom+xml"
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"application/atom+xml; charset=utf-8"
argument_list|)
operator|.
name|subtype
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|field|PARAMETERS
specifier|private
specifier|static
specifier|final
name|ImmutableListMultimap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|PARAMETERS
init|=
name|ImmutableListMultimap
operator|.
name|of
argument_list|(
literal|"a"
argument_list|,
literal|"1"
argument_list|,
literal|"a"
argument_list|,
literal|"2"
argument_list|,
literal|"b"
argument_list|,
literal|"3"
argument_list|)
decl_stmt|;
DECL|method|testGetParameters ()
specifier|public
name|void
name|testGetParameters
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|ImmutableListMultimap
operator|.
name|of
argument_list|()
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|parameters
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ImmutableListMultimap
operator|.
name|of
argument_list|(
literal|"charset"
argument_list|,
literal|"utf-8"
argument_list|)
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"application/atom+xml; charset=utf-8"
argument_list|)
operator|.
name|parameters
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PARAMETERS
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"application/atom+xml; a=1; a=2; b=3"
argument_list|)
operator|.
name|parameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testWithoutParameters ()
specifier|public
name|void
name|testWithoutParameters
parameter_list|()
block|{
name|assertSame
argument_list|(
name|MediaType
operator|.
name|parse
argument_list|(
literal|"image/gif"
argument_list|)
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"image/gif"
argument_list|)
operator|.
name|withoutParameters
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MediaType
operator|.
name|parse
argument_list|(
literal|"image/gif"
argument_list|)
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"image/gif; foo=bar"
argument_list|)
operator|.
name|withoutParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testWithParameters ()
specifier|public
name|void
name|testWithParameters
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; a=1; a=2; b=3"
argument_list|)
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|withParameters
argument_list|(
name|PARAMETERS
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; a=1; a=2; b=3"
argument_list|)
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; a=1; a=2; b=3"
argument_list|)
operator|.
name|withParameters
argument_list|(
name|PARAMETERS
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testWithParameters_invalidAttribute ()
specifier|public
name|void
name|testWithParameters_invalidAttribute
parameter_list|()
block|{
name|MediaType
name|mediaType
init|=
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain"
argument_list|)
decl_stmt|;
name|ImmutableListMultimap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parameters
init|=
name|ImmutableListMultimap
operator|.
name|of
argument_list|(
literal|"a"
argument_list|,
literal|"1"
argument_list|,
literal|"@"
argument_list|,
literal|"2"
argument_list|,
literal|"b"
argument_list|,
literal|"3"
argument_list|)
decl_stmt|;
try|try
block|{
name|mediaType
operator|.
name|withParameters
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testWithParameter ()
specifier|public
name|void
name|testWithParameter
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; a=1"
argument_list|)
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|withParameter
argument_list|(
literal|"a"
argument_list|,
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; a=1"
argument_list|)
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; a=1; a=2"
argument_list|)
operator|.
name|withParameter
argument_list|(
literal|"a"
argument_list|,
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; a=3"
argument_list|)
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; a=1; a=2"
argument_list|)
operator|.
name|withParameter
argument_list|(
literal|"a"
argument_list|,
literal|"3"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; a=1; a=2; b=3"
argument_list|)
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; a=1; a=2"
argument_list|)
operator|.
name|withParameter
argument_list|(
literal|"b"
argument_list|,
literal|"3"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testWithParameter_invalidAttribute ()
specifier|public
name|void
name|testWithParameter_invalidAttribute
parameter_list|()
block|{
name|MediaType
name|mediaType
init|=
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain"
argument_list|)
decl_stmt|;
try|try
block|{
name|mediaType
operator|.
name|withParameter
argument_list|(
literal|"@"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testWithCharset ()
specifier|public
name|void
name|testWithCharset
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; charset=utf-8"
argument_list|)
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|withCharset
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; charset=utf-8"
argument_list|)
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; charset=utf-16"
argument_list|)
operator|.
name|withCharset
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testHasWildcard ()
specifier|public
name|void
name|testHasWildcard
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|PLAIN_TEXT_UTF_8
operator|.
name|hasWildcard
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|JPEG
operator|.
name|hasWildcard
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ANY_TYPE
operator|.
name|hasWildcard
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ANY_APPLICATION_TYPE
operator|.
name|hasWildcard
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ANY_AUDIO_TYPE
operator|.
name|hasWildcard
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ANY_IMAGE_TYPE
operator|.
name|hasWildcard
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ANY_TEXT_TYPE
operator|.
name|hasWildcard
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ANY_VIDEO_TYPE
operator|.
name|hasWildcard
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testIs ()
specifier|public
name|void
name|testIs
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|PLAIN_TEXT_UTF_8
operator|.
name|is
argument_list|(
name|ANY_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|JPEG
operator|.
name|is
argument_list|(
name|ANY_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ANY_TEXT_TYPE
operator|.
name|is
argument_list|(
name|ANY_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|PLAIN_TEXT_UTF_8
operator|.
name|is
argument_list|(
name|ANY_TEXT_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|PLAIN_TEXT_UTF_8
operator|.
name|withoutParameters
argument_list|()
operator|.
name|is
argument_list|(
name|ANY_TEXT_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|JPEG
operator|.
name|is
argument_list|(
name|ANY_TEXT_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|PLAIN_TEXT_UTF_8
operator|.
name|is
argument_list|(
name|PLAIN_TEXT_UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|PLAIN_TEXT_UTF_8
operator|.
name|is
argument_list|(
name|PLAIN_TEXT_UTF_8
operator|.
name|withoutParameters
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|PLAIN_TEXT_UTF_8
operator|.
name|withoutParameters
argument_list|()
operator|.
name|is
argument_list|(
name|PLAIN_TEXT_UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|PLAIN_TEXT_UTF_8
operator|.
name|is
argument_list|(
name|HTML_UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|PLAIN_TEXT_UTF_8
operator|.
name|withParameter
argument_list|(
literal|"charset"
argument_list|,
literal|"UTF-16"
argument_list|)
operator|.
name|is
argument_list|(
name|PLAIN_TEXT_UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|PLAIN_TEXT_UTF_8
operator|.
name|is
argument_list|(
name|PLAIN_TEXT_UTF_8
operator|.
name|withParameter
argument_list|(
literal|"charset"
argument_list|,
literal|"UTF-16"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testParse_empty ()
specifier|public
name|void
name|testParse_empty
parameter_list|()
block|{
try|try
block|{
name|MediaType
operator|.
name|parse
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testParse_badInput ()
specifier|public
name|void
name|testParse_badInput
parameter_list|()
block|{
try|try
block|{
name|MediaType
operator|.
name|parse
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|MediaType
operator|.
name|parse
argument_list|(
literal|"te<t/plain"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/pl@in"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain;"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; "
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; a"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; a="
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; a=@"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; a=\"@"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; a=1;"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; a=1; "
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; a=1; b"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; a=1; b="
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; a=\u2025"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testGetCharset ()
specifier|public
name|void
name|testGetCharset
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|Optional
operator|.
name|absent
argument_list|()
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|charset
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Optional
operator|.
name|of
argument_list|(
name|UTF_8
argument_list|)
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; charset=utf-8"
argument_list|)
operator|.
name|charset
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetCharset_utf16 ()
annotation|@
name|GwtIncompatible
argument_list|(
literal|"Non-UTF-8 Charset"
argument_list|)
specifier|public
name|void
name|testGetCharset_utf16
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|Optional
operator|.
name|of
argument_list|(
name|UTF_16
argument_list|)
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; charset=utf-16"
argument_list|)
operator|.
name|charset
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetCharset_tooMany ()
specifier|public
name|void
name|testGetCharset_tooMany
parameter_list|()
block|{
name|MediaType
name|mediaType
init|=
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; charset=utf-8; charset=utf-16"
argument_list|)
decl_stmt|;
try|try
block|{
name|mediaType
operator|.
name|charset
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testGetCharset_illegalCharset ()
specifier|public
name|void
name|testGetCharset_illegalCharset
parameter_list|()
block|{
name|MediaType
name|mediaType
init|=
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; charset=\"!@#$%^&*()\""
argument_list|)
decl_stmt|;
try|try
block|{
name|mediaType
operator|.
name|charset
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalCharsetNameException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testGetCharset_unsupportedCharset ()
specifier|public
name|void
name|testGetCharset_unsupportedCharset
parameter_list|()
block|{
name|MediaType
name|mediaType
init|=
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; charset=utf-wtf"
argument_list|)
decl_stmt|;
try|try
block|{
name|mediaType
operator|.
name|charset
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedCharsetException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testEquals ()
specifier|public
name|void
name|testEquals
parameter_list|()
block|{
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|MediaType
operator|.
name|create
argument_list|(
literal|"text"
argument_list|,
literal|"plain"
argument_list|)
argument_list|,
name|MediaType
operator|.
name|create
argument_list|(
literal|"TEXT"
argument_list|,
literal|"PLAIN"
argument_list|)
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain"
argument_list|)
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"TEXT/PLAIN"
argument_list|)
argument_list|,
name|MediaType
operator|.
name|create
argument_list|(
literal|"text"
argument_list|,
literal|"plain"
argument_list|)
operator|.
name|withParameter
argument_list|(
literal|"a"
argument_list|,
literal|"1"
argument_list|)
operator|.
name|withoutParameters
argument_list|()
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|MediaType
operator|.
name|create
argument_list|(
literal|"text"
argument_list|,
literal|"plain"
argument_list|)
operator|.
name|withCharset
argument_list|(
name|UTF_8
argument_list|)
argument_list|,
name|MediaType
operator|.
name|create
argument_list|(
literal|"text"
argument_list|,
literal|"plain"
argument_list|)
operator|.
name|withParameter
argument_list|(
literal|"CHARSET"
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|,
name|MediaType
operator|.
name|create
argument_list|(
literal|"text"
argument_list|,
literal|"plain"
argument_list|)
operator|.
name|withParameters
argument_list|(
name|ImmutableMultimap
operator|.
name|of
argument_list|(
literal|"charset"
argument_list|,
literal|"utf-8"
argument_list|)
argument_list|)
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; charset=utf-8"
argument_list|)
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain;  charset=utf-8"
argument_list|)
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; \tcharset=utf-8"
argument_list|)
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; \r\n\tcharset=utf-8"
argument_list|)
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; CHARSET=utf-8"
argument_list|)
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; charset=\"utf-8\""
argument_list|)
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; charset=\"\\u\\tf-\\8\""
argument_list|)
argument_list|,
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; charset=UTF-8"
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain; charset=utf-8; charset=utf-8"
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|MediaType
operator|.
name|create
argument_list|(
literal|"text"
argument_list|,
literal|"plain"
argument_list|)
operator|.
name|withParameter
argument_list|(
literal|"a"
argument_list|,
literal|"value"
argument_list|)
argument_list|,
name|MediaType
operator|.
name|create
argument_list|(
literal|"text"
argument_list|,
literal|"plain"
argument_list|)
operator|.
name|withParameter
argument_list|(
literal|"A"
argument_list|,
literal|"value"
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|MediaType
operator|.
name|create
argument_list|(
literal|"text"
argument_list|,
literal|"plain"
argument_list|)
operator|.
name|withParameter
argument_list|(
literal|"a"
argument_list|,
literal|"VALUE"
argument_list|)
argument_list|,
name|MediaType
operator|.
name|create
argument_list|(
literal|"text"
argument_list|,
literal|"plain"
argument_list|)
operator|.
name|withParameter
argument_list|(
literal|"A"
argument_list|,
literal|"VALUE"
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|MediaType
operator|.
name|create
argument_list|(
literal|"text"
argument_list|,
literal|"plain"
argument_list|)
operator|.
name|withParameters
argument_list|(
name|ImmutableListMultimap
operator|.
name|of
argument_list|(
literal|"a"
argument_list|,
literal|"1"
argument_list|,
literal|"a"
argument_list|,
literal|"2"
argument_list|)
argument_list|)
argument_list|,
name|MediaType
operator|.
name|create
argument_list|(
literal|"text"
argument_list|,
literal|"plain"
argument_list|)
operator|.
name|withParameters
argument_list|(
name|ImmutableListMultimap
operator|.
name|of
argument_list|(
literal|"a"
argument_list|,
literal|"2"
argument_list|,
literal|"a"
argument_list|,
literal|"1"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|MediaType
operator|.
name|create
argument_list|(
literal|"text"
argument_list|,
literal|"csv"
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|MediaType
operator|.
name|create
argument_list|(
literal|"application"
argument_list|,
literal|"atom+xml"
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|testEquals_nonUtf8Charsets ()
annotation|@
name|GwtIncompatible
argument_list|(
literal|"Non-UTF-8 Charset"
argument_list|)
specifier|public
name|void
name|testEquals_nonUtf8Charsets
parameter_list|()
block|{
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|MediaType
operator|.
name|create
argument_list|(
literal|"text"
argument_list|,
literal|"plain"
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|MediaType
operator|.
name|create
argument_list|(
literal|"text"
argument_list|,
literal|"plain"
argument_list|)
operator|.
name|withCharset
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|MediaType
operator|.
name|create
argument_list|(
literal|"text"
argument_list|,
literal|"plain"
argument_list|)
operator|.
name|withCharset
argument_list|(
name|UTF_16
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"com.google.common.testing.NullPointerTester"
argument_list|)
DECL|method|testNullPointer ()
specifier|public
name|void
name|testNullPointer
parameter_list|()
throws|throws
name|Exception
block|{
name|NullPointerTester
name|tester
init|=
operator|new
name|NullPointerTester
argument_list|()
decl_stmt|;
name|tester
operator|.
name|testAllPublicConstructors
argument_list|(
name|MediaType
operator|.
name|class
argument_list|)
expr_stmt|;
name|tester
operator|.
name|testAllPublicStaticMethods
argument_list|(
name|MediaType
operator|.
name|class
argument_list|)
expr_stmt|;
name|tester
operator|.
name|testAllPublicInstanceMethods
argument_list|(
name|MediaType
operator|.
name|parse
argument_list|(
literal|"text/plain"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"text/plain"
argument_list|,
name|MediaType
operator|.
name|create
argument_list|(
literal|"text"
argument_list|,
literal|"plain"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"text/plain; something=\"cr@zy\"; something-else=\"crazy with spaces\""
argument_list|,
name|MediaType
operator|.
name|create
argument_list|(
literal|"text"
argument_list|,
literal|"plain"
argument_list|)
operator|.
name|withParameter
argument_list|(
literal|"something"
argument_list|,
literal|"cr@zy"
argument_list|)
operator|.
name|withParameter
argument_list|(
literal|"something-else"
argument_list|,
literal|"crazy with spaces"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

