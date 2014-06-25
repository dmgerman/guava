begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|testing
operator|.
name|SerializableTester
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
name|lang
operator|.
name|annotation
operator|.
name|Retention
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
import|;
end_import

begin_comment
comment|/**  * Tests for {@link Enums}.  *  * @author Steve McKay  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|EnumsTest
specifier|public
class|class
name|EnumsTest
extends|extends
name|TestCase
block|{
DECL|enum|TestEnum
specifier|private
enum|enum
name|TestEnum
block|{
DECL|enumConstant|CHEETO
name|CHEETO
block|,
DECL|enumConstant|HONDA
name|HONDA
block|,
DECL|enumConstant|POODLE
name|POODLE
block|,   }
DECL|enum|OtherEnum
specifier|private
enum|enum
name|OtherEnum
block|{}
DECL|method|testGetIfPresent ()
specifier|public
name|void
name|testGetIfPresent
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|Optional
operator|.
name|of
argument_list|(
name|TestEnum
operator|.
name|CHEETO
argument_list|)
argument_list|,
name|Enums
operator|.
name|getIfPresent
argument_list|(
name|TestEnum
operator|.
name|class
argument_list|,
literal|"CHEETO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Optional
operator|.
name|of
argument_list|(
name|TestEnum
operator|.
name|HONDA
argument_list|)
argument_list|,
name|Enums
operator|.
name|getIfPresent
argument_list|(
name|TestEnum
operator|.
name|class
argument_list|,
literal|"HONDA"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Optional
operator|.
name|of
argument_list|(
name|TestEnum
operator|.
name|POODLE
argument_list|)
argument_list|,
name|Enums
operator|.
name|getIfPresent
argument_list|(
name|TestEnum
operator|.
name|class
argument_list|,
literal|"POODLE"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Enums
operator|.
name|getIfPresent
argument_list|(
name|TestEnum
operator|.
name|class
argument_list|,
literal|"CHEETO"
argument_list|)
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Enums
operator|.
name|getIfPresent
argument_list|(
name|TestEnum
operator|.
name|class
argument_list|,
literal|"HONDA"
argument_list|)
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Enums
operator|.
name|getIfPresent
argument_list|(
name|TestEnum
operator|.
name|class
argument_list|,
literal|"POODLE"
argument_list|)
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TestEnum
operator|.
name|CHEETO
argument_list|,
name|Enums
operator|.
name|getIfPresent
argument_list|(
name|TestEnum
operator|.
name|class
argument_list|,
literal|"CHEETO"
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TestEnum
operator|.
name|HONDA
argument_list|,
name|Enums
operator|.
name|getIfPresent
argument_list|(
name|TestEnum
operator|.
name|class
argument_list|,
literal|"HONDA"
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TestEnum
operator|.
name|POODLE
argument_list|,
name|Enums
operator|.
name|getIfPresent
argument_list|(
name|TestEnum
operator|.
name|class
argument_list|,
literal|"POODLE"
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetIfPresent_caseSensitive ()
specifier|public
name|void
name|testGetIfPresent_caseSensitive
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|Enums
operator|.
name|getIfPresent
argument_list|(
name|TestEnum
operator|.
name|class
argument_list|,
literal|"cHEETO"
argument_list|)
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Enums
operator|.
name|getIfPresent
argument_list|(
name|TestEnum
operator|.
name|class
argument_list|,
literal|"Honda"
argument_list|)
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Enums
operator|.
name|getIfPresent
argument_list|(
name|TestEnum
operator|.
name|class
argument_list|,
literal|"poodlE"
argument_list|)
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetIfPresent_whenNoMatchingConstant ()
specifier|public
name|void
name|testGetIfPresent_whenNoMatchingConstant
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|Optional
operator|.
name|absent
argument_list|()
argument_list|,
name|Enums
operator|.
name|getIfPresent
argument_list|(
name|TestEnum
operator|.
name|class
argument_list|,
literal|"WOMBAT"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Create a second ClassLoader and use it to get a second version of the TestEnum class.
comment|// Run Enums.getIfPresent on that other TestEnum and then return a WeakReference containing the
comment|// new ClassLoader. If Enums.getIfPresent does caching that prevents the shadow TestEnum
comment|// (and therefore its ClassLoader) from being unloaded, then this WeakReference will never be
comment|// cleared.
DECL|method|testStringConverter_convert ()
specifier|public
name|void
name|testStringConverter_convert
parameter_list|()
block|{
name|Converter
argument_list|<
name|String
argument_list|,
name|TestEnum
argument_list|>
name|converter
init|=
name|Enums
operator|.
name|stringConverter
argument_list|(
name|TestEnum
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|TestEnum
operator|.
name|CHEETO
argument_list|,
name|converter
operator|.
name|convert
argument_list|(
literal|"CHEETO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TestEnum
operator|.
name|HONDA
argument_list|,
name|converter
operator|.
name|convert
argument_list|(
literal|"HONDA"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TestEnum
operator|.
name|POODLE
argument_list|,
name|converter
operator|.
name|convert
argument_list|(
literal|"POODLE"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|converter
operator|.
name|convert
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|converter
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
DECL|method|testStringConverter_convertError ()
specifier|public
name|void
name|testStringConverter_convertError
parameter_list|()
block|{
name|Converter
argument_list|<
name|String
argument_list|,
name|TestEnum
argument_list|>
name|converter
init|=
name|Enums
operator|.
name|stringConverter
argument_list|(
name|TestEnum
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|converter
operator|.
name|convert
argument_list|(
literal|"xxx"
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
block|{     }
block|}
DECL|method|testStringConverter_reverse ()
specifier|public
name|void
name|testStringConverter_reverse
parameter_list|()
block|{
name|Converter
argument_list|<
name|String
argument_list|,
name|TestEnum
argument_list|>
name|converter
init|=
name|Enums
operator|.
name|stringConverter
argument_list|(
name|TestEnum
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"CHEETO"
argument_list|,
name|converter
operator|.
name|reverse
argument_list|()
operator|.
name|convert
argument_list|(
name|TestEnum
operator|.
name|CHEETO
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"HONDA"
argument_list|,
name|converter
operator|.
name|reverse
argument_list|()
operator|.
name|convert
argument_list|(
name|TestEnum
operator|.
name|HONDA
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"POODLE"
argument_list|,
name|converter
operator|.
name|reverse
argument_list|()
operator|.
name|convert
argument_list|(
name|TestEnum
operator|.
name|POODLE
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testStringConverter_nullConversions ()
specifier|public
name|void
name|testStringConverter_nullConversions
parameter_list|()
block|{
name|Converter
argument_list|<
name|String
argument_list|,
name|TestEnum
argument_list|>
name|converter
init|=
name|Enums
operator|.
name|stringConverter
argument_list|(
name|TestEnum
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|converter
operator|.
name|convert
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|converter
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
DECL|method|testStringConverter_serialization ()
specifier|public
name|void
name|testStringConverter_serialization
parameter_list|()
block|{
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|Enums
operator|.
name|stringConverter
argument_list|(
name|TestEnum
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
DECL|annotation|ExampleAnnotation
specifier|private
annotation_defn|@interface
name|ExampleAnnotation
block|{}
DECL|enum|AnEnum
specifier|private
enum|enum
name|AnEnum
block|{
DECL|enumConstant|ExampleAnnotation
DECL|enumConstant|FOO
annotation|@
name|ExampleAnnotation
name|FOO
block|,
DECL|enumConstant|BAR
name|BAR
block|}
block|}
end_class

end_unit

