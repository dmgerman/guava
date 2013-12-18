begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_class
DECL|class|CaseFormatTest_gwt
specifier|public
class|class
name|CaseFormatTest_gwt
extends|extends
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|junit
operator|.
name|client
operator|.
name|GWTTestCase
block|{
DECL|method|getModuleName ()
annotation|@
name|Override
specifier|public
name|String
name|getModuleName
parameter_list|()
block|{
return|return
literal|"com.google.common.base.testModule"
return|;
block|}
DECL|method|testConverterToBackward ()
specifier|public
name|void
name|testConverterToBackward
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testConverterToBackward
argument_list|()
expr_stmt|;
block|}
DECL|method|testConverterToForward ()
specifier|public
name|void
name|testConverterToForward
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testConverterToForward
argument_list|()
expr_stmt|;
block|}
DECL|method|testConverter_nullConversions ()
specifier|public
name|void
name|testConverter_nullConversions
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testConverter_nullConversions
argument_list|()
expr_stmt|;
block|}
DECL|method|testConverter_serialization ()
specifier|public
name|void
name|testConverter_serialization
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testConverter_serialization
argument_list|()
expr_stmt|;
block|}
DECL|method|testConverter_toString ()
specifier|public
name|void
name|testConverter_toString
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testConverter_toString
argument_list|()
expr_stmt|;
block|}
DECL|method|testIdentity ()
specifier|public
name|void
name|testIdentity
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testIdentity
argument_list|()
expr_stmt|;
block|}
DECL|method|testLowerCamelToLowerCamel ()
specifier|public
name|void
name|testLowerCamelToLowerCamel
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testLowerCamelToLowerCamel
argument_list|()
expr_stmt|;
block|}
DECL|method|testLowerCamelToLowerHyphen ()
specifier|public
name|void
name|testLowerCamelToLowerHyphen
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testLowerCamelToLowerHyphen
argument_list|()
expr_stmt|;
block|}
DECL|method|testLowerCamelToLowerUnderscore ()
specifier|public
name|void
name|testLowerCamelToLowerUnderscore
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testLowerCamelToLowerUnderscore
argument_list|()
expr_stmt|;
block|}
DECL|method|testLowerCamelToUpperCamel ()
specifier|public
name|void
name|testLowerCamelToUpperCamel
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testLowerCamelToUpperCamel
argument_list|()
expr_stmt|;
block|}
DECL|method|testLowerCamelToUpperUnderscore ()
specifier|public
name|void
name|testLowerCamelToUpperUnderscore
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testLowerCamelToUpperUnderscore
argument_list|()
expr_stmt|;
block|}
DECL|method|testLowerHyphenToLowerCamel ()
specifier|public
name|void
name|testLowerHyphenToLowerCamel
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testLowerHyphenToLowerCamel
argument_list|()
expr_stmt|;
block|}
DECL|method|testLowerHyphenToLowerHyphen ()
specifier|public
name|void
name|testLowerHyphenToLowerHyphen
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testLowerHyphenToLowerHyphen
argument_list|()
expr_stmt|;
block|}
DECL|method|testLowerHyphenToLowerUnderscore ()
specifier|public
name|void
name|testLowerHyphenToLowerUnderscore
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testLowerHyphenToLowerUnderscore
argument_list|()
expr_stmt|;
block|}
DECL|method|testLowerHyphenToUpperCamel ()
specifier|public
name|void
name|testLowerHyphenToUpperCamel
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testLowerHyphenToUpperCamel
argument_list|()
expr_stmt|;
block|}
DECL|method|testLowerHyphenToUpperUnderscore ()
specifier|public
name|void
name|testLowerHyphenToUpperUnderscore
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testLowerHyphenToUpperUnderscore
argument_list|()
expr_stmt|;
block|}
DECL|method|testLowerUnderscoreToLowerCamel ()
specifier|public
name|void
name|testLowerUnderscoreToLowerCamel
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testLowerUnderscoreToLowerCamel
argument_list|()
expr_stmt|;
block|}
DECL|method|testLowerUnderscoreToLowerHyphen ()
specifier|public
name|void
name|testLowerUnderscoreToLowerHyphen
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testLowerUnderscoreToLowerHyphen
argument_list|()
expr_stmt|;
block|}
DECL|method|testLowerUnderscoreToLowerUnderscore ()
specifier|public
name|void
name|testLowerUnderscoreToLowerUnderscore
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testLowerUnderscoreToLowerUnderscore
argument_list|()
expr_stmt|;
block|}
DECL|method|testLowerUnderscoreToUpperCamel ()
specifier|public
name|void
name|testLowerUnderscoreToUpperCamel
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testLowerUnderscoreToUpperCamel
argument_list|()
expr_stmt|;
block|}
DECL|method|testLowerUnderscoreToUpperUnderscore ()
specifier|public
name|void
name|testLowerUnderscoreToUpperUnderscore
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testLowerUnderscoreToUpperUnderscore
argument_list|()
expr_stmt|;
block|}
DECL|method|testUpperCamelToLowerCamel ()
specifier|public
name|void
name|testUpperCamelToLowerCamel
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testUpperCamelToLowerCamel
argument_list|()
expr_stmt|;
block|}
DECL|method|testUpperCamelToLowerHyphen ()
specifier|public
name|void
name|testUpperCamelToLowerHyphen
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testUpperCamelToLowerHyphen
argument_list|()
expr_stmt|;
block|}
DECL|method|testUpperCamelToLowerUnderscore ()
specifier|public
name|void
name|testUpperCamelToLowerUnderscore
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testUpperCamelToLowerUnderscore
argument_list|()
expr_stmt|;
block|}
DECL|method|testUpperCamelToUpperCamel ()
specifier|public
name|void
name|testUpperCamelToUpperCamel
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testUpperCamelToUpperCamel
argument_list|()
expr_stmt|;
block|}
DECL|method|testUpperCamelToUpperUnderscore ()
specifier|public
name|void
name|testUpperCamelToUpperUnderscore
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testUpperCamelToUpperUnderscore
argument_list|()
expr_stmt|;
block|}
DECL|method|testUpperUnderscoreToLowerCamel ()
specifier|public
name|void
name|testUpperUnderscoreToLowerCamel
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testUpperUnderscoreToLowerCamel
argument_list|()
expr_stmt|;
block|}
DECL|method|testUpperUnderscoreToLowerHyphen ()
specifier|public
name|void
name|testUpperUnderscoreToLowerHyphen
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testUpperUnderscoreToLowerHyphen
argument_list|()
expr_stmt|;
block|}
DECL|method|testUpperUnderscoreToLowerUnderscore ()
specifier|public
name|void
name|testUpperUnderscoreToLowerUnderscore
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testUpperUnderscoreToLowerUnderscore
argument_list|()
expr_stmt|;
block|}
DECL|method|testUpperUnderscoreToUpperCamel ()
specifier|public
name|void
name|testUpperUnderscoreToUpperCamel
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testUpperUnderscoreToUpperCamel
argument_list|()
expr_stmt|;
block|}
DECL|method|testUpperUnderscoreToUpperUnderscore ()
specifier|public
name|void
name|testUpperUnderscoreToUpperUnderscore
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CaseFormatTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testUpperUnderscoreToUpperUnderscore
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

