begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Unit test for {@link Ascii}.  *  * @author Craig Berry  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|AsciiTest
specifier|public
class|class
name|AsciiTest
extends|extends
name|TestCase
block|{
comment|/**    * The Unicode points {@code 00c1} and {@code 00e1} are the upper- and    * lowercase forms of A-with-acute-accent, {@code Ã} and {@code Ã¡}.    */
DECL|field|IGNORED
specifier|private
specifier|static
specifier|final
name|String
name|IGNORED
init|=
literal|"`10-=~!@#$%^&*()_+[]\\{}|;':\",./<>?'\u00c1\u00e1\n"
decl_stmt|;
DECL|field|LOWER
specifier|private
specifier|static
specifier|final
name|String
name|LOWER
init|=
literal|"abcdefghijklmnopqrstuvwxyz"
decl_stmt|;
DECL|field|UPPER
specifier|private
specifier|static
specifier|final
name|String
name|UPPER
init|=
literal|"ABCDEFGHIJKLMNOPQRSTUVWXYZ"
decl_stmt|;
DECL|method|testToLowerCase ()
specifier|public
name|void
name|testToLowerCase
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|LOWER
argument_list|,
name|Ascii
operator|.
name|toLowerCase
argument_list|(
name|UPPER
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|LOWER
argument_list|,
name|Ascii
operator|.
name|toLowerCase
argument_list|(
name|LOWER
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|IGNORED
argument_list|,
name|Ascii
operator|.
name|toLowerCase
argument_list|(
name|IGNORED
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foobar"
argument_list|,
name|Ascii
operator|.
name|toLowerCase
argument_list|(
literal|"fOobaR"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToUpperCase ()
specifier|public
name|void
name|testToUpperCase
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|UPPER
argument_list|,
name|Ascii
operator|.
name|toUpperCase
argument_list|(
name|LOWER
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|UPPER
argument_list|,
name|Ascii
operator|.
name|toUpperCase
argument_list|(
name|UPPER
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|IGNORED
argument_list|,
name|Ascii
operator|.
name|toUpperCase
argument_list|(
name|IGNORED
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"FOOBAR"
argument_list|,
name|Ascii
operator|.
name|toUpperCase
argument_list|(
literal|"FoOBAr"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCharsIgnored ()
specifier|public
name|void
name|testCharsIgnored
parameter_list|()
block|{
for|for
control|(
name|char
name|c
range|:
name|IGNORED
operator|.
name|toCharArray
argument_list|()
control|)
block|{
name|String
name|str
init|=
name|String
operator|.
name|valueOf
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|str
argument_list|,
name|c
operator|==
name|Ascii
operator|.
name|toLowerCase
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|str
argument_list|,
name|c
operator|==
name|Ascii
operator|.
name|toUpperCase
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|str
argument_list|,
name|Ascii
operator|.
name|isLowerCase
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|str
argument_list|,
name|Ascii
operator|.
name|isUpperCase
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testCharsLower ()
specifier|public
name|void
name|testCharsLower
parameter_list|()
block|{
for|for
control|(
name|char
name|c
range|:
name|LOWER
operator|.
name|toCharArray
argument_list|()
control|)
block|{
name|String
name|str
init|=
name|String
operator|.
name|valueOf
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|str
argument_list|,
name|c
operator|==
name|Ascii
operator|.
name|toLowerCase
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|str
argument_list|,
name|c
operator|==
name|Ascii
operator|.
name|toUpperCase
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|str
argument_list|,
name|Ascii
operator|.
name|isLowerCase
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|str
argument_list|,
name|Ascii
operator|.
name|isUpperCase
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testCharsUpper ()
specifier|public
name|void
name|testCharsUpper
parameter_list|()
block|{
for|for
control|(
name|char
name|c
range|:
name|UPPER
operator|.
name|toCharArray
argument_list|()
control|)
block|{
name|String
name|str
init|=
name|String
operator|.
name|valueOf
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|str
argument_list|,
name|c
operator|==
name|Ascii
operator|.
name|toLowerCase
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|str
argument_list|,
name|c
operator|==
name|Ascii
operator|.
name|toUpperCase
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|str
argument_list|,
name|Ascii
operator|.
name|isLowerCase
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|str
argument_list|,
name|Ascii
operator|.
name|isUpperCase
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testTruncate ()
specifier|public
name|void
name|testTruncate
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"foobar"
argument_list|,
name|Ascii
operator|.
name|truncate
argument_list|(
literal|"foobar"
argument_list|,
literal|10
argument_list|,
literal|"..."
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"fo..."
argument_list|,
name|Ascii
operator|.
name|truncate
argument_list|(
literal|"foobar"
argument_list|,
literal|5
argument_list|,
literal|"..."
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foobar"
argument_list|,
name|Ascii
operator|.
name|truncate
argument_list|(
literal|"foobar"
argument_list|,
literal|6
argument_list|,
literal|"..."
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"..."
argument_list|,
name|Ascii
operator|.
name|truncate
argument_list|(
literal|"foobar"
argument_list|,
literal|3
argument_list|,
literal|"..."
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foobar"
argument_list|,
name|Ascii
operator|.
name|truncate
argument_list|(
literal|"foobar"
argument_list|,
literal|10
argument_list|,
literal|"â¦"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"fooâ¦"
argument_list|,
name|Ascii
operator|.
name|truncate
argument_list|(
literal|"foobar"
argument_list|,
literal|4
argument_list|,
literal|"â¦"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"fo--"
argument_list|,
name|Ascii
operator|.
name|truncate
argument_list|(
literal|"foobar"
argument_list|,
literal|4
argument_list|,
literal|"--"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foobar"
argument_list|,
name|Ascii
operator|.
name|truncate
argument_list|(
literal|"foobar"
argument_list|,
literal|6
argument_list|,
literal|"â¦"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foobâ¦"
argument_list|,
name|Ascii
operator|.
name|truncate
argument_list|(
literal|"foobar"
argument_list|,
literal|5
argument_list|,
literal|"â¦"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|Ascii
operator|.
name|truncate
argument_list|(
literal|"foobar"
argument_list|,
literal|3
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|Ascii
operator|.
name|truncate
argument_list|(
literal|""
argument_list|,
literal|5
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|Ascii
operator|.
name|truncate
argument_list|(
literal|""
argument_list|,
literal|5
argument_list|,
literal|"..."
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|Ascii
operator|.
name|truncate
argument_list|(
literal|""
argument_list|,
literal|0
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testTruncateIllegalArguments ()
specifier|public
name|void
name|testTruncateIllegalArguments
parameter_list|()
block|{
name|String
name|truncated
init|=
literal|null
decl_stmt|;
try|try
block|{
name|truncated
operator|=
name|Ascii
operator|.
name|truncate
argument_list|(
literal|"foobar"
argument_list|,
literal|2
argument_list|,
literal|"..."
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
name|truncated
operator|=
name|Ascii
operator|.
name|truncate
argument_list|(
literal|"foobar"
argument_list|,
literal|8
argument_list|,
literal|"1234567890"
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
name|truncated
operator|=
name|Ascii
operator|.
name|truncate
argument_list|(
literal|"foobar"
argument_list|,
operator|-
literal|1
argument_list|,
literal|"..."
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
name|truncated
operator|=
name|Ascii
operator|.
name|truncate
argument_list|(
literal|"foobar"
argument_list|,
operator|-
literal|1
argument_list|,
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
block|}
end_class

end_unit

