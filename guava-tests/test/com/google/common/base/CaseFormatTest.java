begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2006 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|CaseFormat
operator|.
name|LOWER_CAMEL
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
name|CaseFormat
operator|.
name|LOWER_HYPHEN
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
name|CaseFormat
operator|.
name|LOWER_UNDERSCORE
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
name|CaseFormat
operator|.
name|UPPER_CAMEL
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
name|CaseFormat
operator|.
name|UPPER_UNDERSCORE
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

begin_comment
comment|/**  * Unit test for {@link CaseFormat}.  *  * @author Mike Bostock  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|CaseFormatTest
specifier|public
class|class
name|CaseFormatTest
extends|extends
name|TestCase
block|{
DECL|method|testIdentity ()
specifier|public
name|void
name|testIdentity
parameter_list|()
block|{
for|for
control|(
name|CaseFormat
name|from
range|:
name|CaseFormat
operator|.
name|values
argument_list|()
control|)
block|{
name|assertSame
argument_list|(
name|from
operator|+
literal|" to "
operator|+
name|from
argument_list|,
literal|"foo"
argument_list|,
name|from
operator|.
name|to
argument_list|(
name|from
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|CaseFormat
name|to
range|:
name|CaseFormat
operator|.
name|values
argument_list|()
control|)
block|{
name|assertEquals
argument_list|(
name|from
operator|+
literal|" to "
operator|+
name|to
argument_list|,
literal|""
argument_list|,
name|from
operator|.
name|to
argument_list|(
name|to
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|from
operator|+
literal|" to "
operator|+
name|to
argument_list|,
literal|" "
argument_list|,
name|from
operator|.
name|to
argument_list|(
name|to
argument_list|,
literal|" "
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"NullPointerTester"
argument_list|)
DECL|method|testNullArguments ()
specifier|public
name|void
name|testNullArguments
parameter_list|()
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
name|testAllPublicStaticMethods
argument_list|(
name|CaseFormat
operator|.
name|class
argument_list|)
expr_stmt|;
for|for
control|(
name|CaseFormat
name|format
range|:
name|CaseFormat
operator|.
name|values
argument_list|()
control|)
block|{
name|tester
operator|.
name|testAllPublicInstanceMethods
argument_list|(
name|format
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testLowerHyphenToLowerHyphen ()
specifier|public
name|void
name|testLowerHyphenToLowerHyphen
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|LOWER_HYPHEN
operator|.
name|to
argument_list|(
name|LOWER_HYPHEN
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo-bar"
argument_list|,
name|LOWER_HYPHEN
operator|.
name|to
argument_list|(
name|LOWER_HYPHEN
argument_list|,
literal|"foo-bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testLowerHyphenToLowerUnderscore ()
specifier|public
name|void
name|testLowerHyphenToLowerUnderscore
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|LOWER_HYPHEN
operator|.
name|to
argument_list|(
name|LOWER_UNDERSCORE
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo_bar"
argument_list|,
name|LOWER_HYPHEN
operator|.
name|to
argument_list|(
name|LOWER_UNDERSCORE
argument_list|,
literal|"foo-bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testLowerHyphenToLowerCamel ()
specifier|public
name|void
name|testLowerHyphenToLowerCamel
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|LOWER_HYPHEN
operator|.
name|to
argument_list|(
name|LOWER_CAMEL
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"fooBar"
argument_list|,
name|LOWER_HYPHEN
operator|.
name|to
argument_list|(
name|LOWER_CAMEL
argument_list|,
literal|"foo-bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testLowerHyphenToUpperCamel ()
specifier|public
name|void
name|testLowerHyphenToUpperCamel
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"Foo"
argument_list|,
name|LOWER_HYPHEN
operator|.
name|to
argument_list|(
name|UPPER_CAMEL
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"FooBar"
argument_list|,
name|LOWER_HYPHEN
operator|.
name|to
argument_list|(
name|UPPER_CAMEL
argument_list|,
literal|"foo-bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testLowerHyphenToUpperUnderscore ()
specifier|public
name|void
name|testLowerHyphenToUpperUnderscore
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"FOO"
argument_list|,
name|LOWER_HYPHEN
operator|.
name|to
argument_list|(
name|UPPER_UNDERSCORE
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"FOO_BAR"
argument_list|,
name|LOWER_HYPHEN
operator|.
name|to
argument_list|(
name|UPPER_UNDERSCORE
argument_list|,
literal|"foo-bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testLowerUnderscoreToLowerHyphen ()
specifier|public
name|void
name|testLowerUnderscoreToLowerHyphen
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|LOWER_UNDERSCORE
operator|.
name|to
argument_list|(
name|LOWER_HYPHEN
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo-bar"
argument_list|,
name|LOWER_UNDERSCORE
operator|.
name|to
argument_list|(
name|LOWER_HYPHEN
argument_list|,
literal|"foo_bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testLowerUnderscoreToLowerUnderscore ()
specifier|public
name|void
name|testLowerUnderscoreToLowerUnderscore
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|LOWER_UNDERSCORE
operator|.
name|to
argument_list|(
name|LOWER_UNDERSCORE
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo_bar"
argument_list|,
name|LOWER_UNDERSCORE
operator|.
name|to
argument_list|(
name|LOWER_UNDERSCORE
argument_list|,
literal|"foo_bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testLowerUnderscoreToLowerCamel ()
specifier|public
name|void
name|testLowerUnderscoreToLowerCamel
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|LOWER_UNDERSCORE
operator|.
name|to
argument_list|(
name|LOWER_CAMEL
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"fooBar"
argument_list|,
name|LOWER_UNDERSCORE
operator|.
name|to
argument_list|(
name|LOWER_CAMEL
argument_list|,
literal|"foo_bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testLowerUnderscoreToUpperCamel ()
specifier|public
name|void
name|testLowerUnderscoreToUpperCamel
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"Foo"
argument_list|,
name|LOWER_UNDERSCORE
operator|.
name|to
argument_list|(
name|UPPER_CAMEL
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"FooBar"
argument_list|,
name|LOWER_UNDERSCORE
operator|.
name|to
argument_list|(
name|UPPER_CAMEL
argument_list|,
literal|"foo_bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testLowerUnderscoreToUpperUnderscore ()
specifier|public
name|void
name|testLowerUnderscoreToUpperUnderscore
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"FOO"
argument_list|,
name|LOWER_UNDERSCORE
operator|.
name|to
argument_list|(
name|UPPER_UNDERSCORE
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"FOO_BAR"
argument_list|,
name|LOWER_UNDERSCORE
operator|.
name|to
argument_list|(
name|UPPER_UNDERSCORE
argument_list|,
literal|"foo_bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testLowerCamelToLowerHyphen ()
specifier|public
name|void
name|testLowerCamelToLowerHyphen
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|LOWER_CAMEL
operator|.
name|to
argument_list|(
name|LOWER_HYPHEN
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo-bar"
argument_list|,
name|LOWER_CAMEL
operator|.
name|to
argument_list|(
name|LOWER_HYPHEN
argument_list|,
literal|"fooBar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"h-t-t-p"
argument_list|,
name|LOWER_CAMEL
operator|.
name|to
argument_list|(
name|LOWER_HYPHEN
argument_list|,
literal|"HTTP"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testLowerCamelToLowerUnderscore ()
specifier|public
name|void
name|testLowerCamelToLowerUnderscore
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|LOWER_CAMEL
operator|.
name|to
argument_list|(
name|LOWER_UNDERSCORE
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo_bar"
argument_list|,
name|LOWER_CAMEL
operator|.
name|to
argument_list|(
name|LOWER_UNDERSCORE
argument_list|,
literal|"fooBar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"h_t_t_p"
argument_list|,
name|LOWER_CAMEL
operator|.
name|to
argument_list|(
name|LOWER_UNDERSCORE
argument_list|,
literal|"hTTP"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testLowerCamelToLowerCamel ()
specifier|public
name|void
name|testLowerCamelToLowerCamel
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|LOWER_CAMEL
operator|.
name|to
argument_list|(
name|LOWER_CAMEL
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"fooBar"
argument_list|,
name|LOWER_CAMEL
operator|.
name|to
argument_list|(
name|LOWER_CAMEL
argument_list|,
literal|"fooBar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testLowerCamelToUpperCamel ()
specifier|public
name|void
name|testLowerCamelToUpperCamel
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"Foo"
argument_list|,
name|LOWER_CAMEL
operator|.
name|to
argument_list|(
name|UPPER_CAMEL
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"FooBar"
argument_list|,
name|LOWER_CAMEL
operator|.
name|to
argument_list|(
name|UPPER_CAMEL
argument_list|,
literal|"fooBar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"HTTP"
argument_list|,
name|LOWER_CAMEL
operator|.
name|to
argument_list|(
name|UPPER_CAMEL
argument_list|,
literal|"hTTP"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testLowerCamelToUpperUnderscore ()
specifier|public
name|void
name|testLowerCamelToUpperUnderscore
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"FOO"
argument_list|,
name|LOWER_CAMEL
operator|.
name|to
argument_list|(
name|UPPER_UNDERSCORE
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"FOO_BAR"
argument_list|,
name|LOWER_CAMEL
operator|.
name|to
argument_list|(
name|UPPER_UNDERSCORE
argument_list|,
literal|"fooBar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testUpperCamelToLowerHyphen ()
specifier|public
name|void
name|testUpperCamelToLowerHyphen
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|UPPER_CAMEL
operator|.
name|to
argument_list|(
name|LOWER_HYPHEN
argument_list|,
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo-bar"
argument_list|,
name|UPPER_CAMEL
operator|.
name|to
argument_list|(
name|LOWER_HYPHEN
argument_list|,
literal|"FooBar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testUpperCamelToLowerUnderscore ()
specifier|public
name|void
name|testUpperCamelToLowerUnderscore
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|UPPER_CAMEL
operator|.
name|to
argument_list|(
name|LOWER_UNDERSCORE
argument_list|,
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo_bar"
argument_list|,
name|UPPER_CAMEL
operator|.
name|to
argument_list|(
name|LOWER_UNDERSCORE
argument_list|,
literal|"FooBar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testUpperCamelToLowerCamel ()
specifier|public
name|void
name|testUpperCamelToLowerCamel
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|UPPER_CAMEL
operator|.
name|to
argument_list|(
name|LOWER_CAMEL
argument_list|,
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"fooBar"
argument_list|,
name|UPPER_CAMEL
operator|.
name|to
argument_list|(
name|LOWER_CAMEL
argument_list|,
literal|"FooBar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hTTP"
argument_list|,
name|UPPER_CAMEL
operator|.
name|to
argument_list|(
name|LOWER_CAMEL
argument_list|,
literal|"HTTP"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testUpperCamelToUpperCamel ()
specifier|public
name|void
name|testUpperCamelToUpperCamel
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"Foo"
argument_list|,
name|UPPER_CAMEL
operator|.
name|to
argument_list|(
name|UPPER_CAMEL
argument_list|,
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"FooBar"
argument_list|,
name|UPPER_CAMEL
operator|.
name|to
argument_list|(
name|UPPER_CAMEL
argument_list|,
literal|"FooBar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testUpperCamelToUpperUnderscore ()
specifier|public
name|void
name|testUpperCamelToUpperUnderscore
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"FOO"
argument_list|,
name|UPPER_CAMEL
operator|.
name|to
argument_list|(
name|UPPER_UNDERSCORE
argument_list|,
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"FOO_BAR"
argument_list|,
name|UPPER_CAMEL
operator|.
name|to
argument_list|(
name|UPPER_UNDERSCORE
argument_list|,
literal|"FooBar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"H_T_T_P"
argument_list|,
name|UPPER_CAMEL
operator|.
name|to
argument_list|(
name|UPPER_UNDERSCORE
argument_list|,
literal|"HTTP"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"H__T__T__P"
argument_list|,
name|UPPER_CAMEL
operator|.
name|to
argument_list|(
name|UPPER_UNDERSCORE
argument_list|,
literal|"H_T_T_P"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testUpperUnderscoreToLowerHyphen ()
specifier|public
name|void
name|testUpperUnderscoreToLowerHyphen
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|UPPER_UNDERSCORE
operator|.
name|to
argument_list|(
name|LOWER_HYPHEN
argument_list|,
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo-bar"
argument_list|,
name|UPPER_UNDERSCORE
operator|.
name|to
argument_list|(
name|LOWER_HYPHEN
argument_list|,
literal|"FOO_BAR"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testUpperUnderscoreToLowerUnderscore ()
specifier|public
name|void
name|testUpperUnderscoreToLowerUnderscore
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|UPPER_UNDERSCORE
operator|.
name|to
argument_list|(
name|LOWER_UNDERSCORE
argument_list|,
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo_bar"
argument_list|,
name|UPPER_UNDERSCORE
operator|.
name|to
argument_list|(
name|LOWER_UNDERSCORE
argument_list|,
literal|"FOO_BAR"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testUpperUnderscoreToLowerCamel ()
specifier|public
name|void
name|testUpperUnderscoreToLowerCamel
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|UPPER_UNDERSCORE
operator|.
name|to
argument_list|(
name|LOWER_CAMEL
argument_list|,
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"fooBar"
argument_list|,
name|UPPER_UNDERSCORE
operator|.
name|to
argument_list|(
name|LOWER_CAMEL
argument_list|,
literal|"FOO_BAR"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testUpperUnderscoreToUpperCamel ()
specifier|public
name|void
name|testUpperUnderscoreToUpperCamel
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"Foo"
argument_list|,
name|UPPER_UNDERSCORE
operator|.
name|to
argument_list|(
name|UPPER_CAMEL
argument_list|,
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"FooBar"
argument_list|,
name|UPPER_UNDERSCORE
operator|.
name|to
argument_list|(
name|UPPER_CAMEL
argument_list|,
literal|"FOO_BAR"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"HTTP"
argument_list|,
name|UPPER_UNDERSCORE
operator|.
name|to
argument_list|(
name|UPPER_CAMEL
argument_list|,
literal|"H_T_T_P"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testUpperUnderscoreToUpperUnderscore ()
specifier|public
name|void
name|testUpperUnderscoreToUpperUnderscore
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"FOO"
argument_list|,
name|UPPER_UNDERSCORE
operator|.
name|to
argument_list|(
name|UPPER_UNDERSCORE
argument_list|,
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"FOO_BAR"
argument_list|,
name|UPPER_UNDERSCORE
operator|.
name|to
argument_list|(
name|UPPER_UNDERSCORE
argument_list|,
literal|"FOO_BAR"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testConverterToForward ()
specifier|public
name|void
name|testConverterToForward
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"FooBar"
argument_list|,
name|UPPER_UNDERSCORE
operator|.
name|converterTo
argument_list|(
name|UPPER_CAMEL
argument_list|)
operator|.
name|convert
argument_list|(
literal|"FOO_BAR"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"fooBar"
argument_list|,
name|UPPER_UNDERSCORE
operator|.
name|converterTo
argument_list|(
name|LOWER_CAMEL
argument_list|)
operator|.
name|convert
argument_list|(
literal|"FOO_BAR"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"FOO_BAR"
argument_list|,
name|UPPER_CAMEL
operator|.
name|converterTo
argument_list|(
name|UPPER_UNDERSCORE
argument_list|)
operator|.
name|convert
argument_list|(
literal|"FooBar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"FOO_BAR"
argument_list|,
name|LOWER_CAMEL
operator|.
name|converterTo
argument_list|(
name|UPPER_UNDERSCORE
argument_list|)
operator|.
name|convert
argument_list|(
literal|"fooBar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testConverterToBackward ()
specifier|public
name|void
name|testConverterToBackward
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"FOO_BAR"
argument_list|,
name|UPPER_UNDERSCORE
operator|.
name|converterTo
argument_list|(
name|UPPER_CAMEL
argument_list|)
operator|.
name|reverse
argument_list|()
operator|.
name|convert
argument_list|(
literal|"FooBar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"FOO_BAR"
argument_list|,
name|UPPER_UNDERSCORE
operator|.
name|converterTo
argument_list|(
name|LOWER_CAMEL
argument_list|)
operator|.
name|reverse
argument_list|()
operator|.
name|convert
argument_list|(
literal|"fooBar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"FooBar"
argument_list|,
name|UPPER_CAMEL
operator|.
name|converterTo
argument_list|(
name|UPPER_UNDERSCORE
argument_list|)
operator|.
name|reverse
argument_list|()
operator|.
name|convert
argument_list|(
literal|"FOO_BAR"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"fooBar"
argument_list|,
name|LOWER_CAMEL
operator|.
name|converterTo
argument_list|(
name|UPPER_UNDERSCORE
argument_list|)
operator|.
name|reverse
argument_list|()
operator|.
name|convert
argument_list|(
literal|"FOO_BAR"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testConverter_nullConversions ()
specifier|public
name|void
name|testConverter_nullConversions
parameter_list|()
block|{
for|for
control|(
name|CaseFormat
name|outer
range|:
name|CaseFormat
operator|.
name|values
argument_list|()
control|)
block|{
for|for
control|(
name|CaseFormat
name|inner
range|:
name|CaseFormat
operator|.
name|values
argument_list|()
control|)
block|{
name|assertNull
argument_list|(
name|outer
operator|.
name|converterTo
argument_list|(
name|inner
argument_list|)
operator|.
name|convert
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|outer
operator|.
name|converterTo
argument_list|(
name|inner
argument_list|)
operator|.
name|reverse
argument_list|()
operator|.
name|convert
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

