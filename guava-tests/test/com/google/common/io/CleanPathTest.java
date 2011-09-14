begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not  * use this file except in compliance with the License.  You may obtain a copy  * of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the  * License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.io
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
package|;
end_package

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
comment|/**  * Unit tests for Files.simplifyPath().  *  * @author Pablo Bellver  */
end_comment

begin_class
DECL|class|CleanPathTest
specifier|public
class|class
name|CleanPathTest
extends|extends
name|TestCase
block|{
DECL|method|testSimplify ()
specifier|public
name|void
name|testSimplify
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|Files
operator|.
name|simplifyPath
argument_list|(
literal|"."
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimplify1 ()
specifier|public
name|void
name|testSimplify1
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"."
argument_list|,
name|Files
operator|.
name|simplifyPath
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|" "
argument_list|,
name|Files
operator|.
name|simplifyPath
argument_list|(
literal|" "
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimplify2 ()
specifier|public
name|void
name|testSimplify2
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"x"
argument_list|,
name|Files
operator|.
name|simplifyPath
argument_list|(
literal|"x"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimplify3 ()
specifier|public
name|void
name|testSimplify3
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"/a/b/c/d"
argument_list|,
name|Files
operator|.
name|simplifyPath
argument_list|(
literal|"/a/b/c/d"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimplify4 ()
specifier|public
name|void
name|testSimplify4
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"/a/b/c/d"
argument_list|,
name|Files
operator|.
name|simplifyPath
argument_list|(
literal|"/a/b/c/d/"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimplify5 ()
specifier|public
name|void
name|testSimplify5
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"/a/b"
argument_list|,
name|Files
operator|.
name|simplifyPath
argument_list|(
literal|"/a//b"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimplify6 ()
specifier|public
name|void
name|testSimplify6
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"/a/b"
argument_list|,
name|Files
operator|.
name|simplifyPath
argument_list|(
literal|"//a//b/"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimplify7 ()
specifier|public
name|void
name|testSimplify7
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"/"
argument_list|,
name|Files
operator|.
name|simplifyPath
argument_list|(
literal|"/.."
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimplify8 ()
specifier|public
name|void
name|testSimplify8
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"/"
argument_list|,
name|Files
operator|.
name|simplifyPath
argument_list|(
literal|"/././././"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimplify9 ()
specifier|public
name|void
name|testSimplify9
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"/a"
argument_list|,
name|Files
operator|.
name|simplifyPath
argument_list|(
literal|"/a/b/.."
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimplify10 ()
specifier|public
name|void
name|testSimplify10
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"/"
argument_list|,
name|Files
operator|.
name|simplifyPath
argument_list|(
literal|"/a/b/../../.."
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimplify11 ()
specifier|public
name|void
name|testSimplify11
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"/"
argument_list|,
name|Files
operator|.
name|simplifyPath
argument_list|(
literal|"//a//b/..////../..//"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimplify12 ()
specifier|public
name|void
name|testSimplify12
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"/x"
argument_list|,
name|Files
operator|.
name|simplifyPath
argument_list|(
literal|"//a//../x//"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimplify13 ()
specifier|public
name|void
name|testSimplify13
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"../c"
argument_list|,
name|Files
operator|.
name|simplifyPath
argument_list|(
literal|"a/b/../../../c"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimplifyDotDot ()
specifier|public
name|void
name|testSimplifyDotDot
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|".."
argument_list|,
name|Files
operator|.
name|simplifyPath
argument_list|(
literal|".."
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimplifyDotDotSlash ()
specifier|public
name|void
name|testSimplifyDotDotSlash
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|".."
argument_list|,
name|Files
operator|.
name|simplifyPath
argument_list|(
literal|"../"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|".."
argument_list|,
name|Files
operator|.
name|simplifyPath
argument_list|(
literal|"a/../.."
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|".."
argument_list|,
name|Files
operator|.
name|simplifyPath
argument_list|(
literal|"a/../../"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimplifyDotDots ()
specifier|public
name|void
name|testSimplifyDotDots
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"../.."
argument_list|,
name|Files
operator|.
name|simplifyPath
argument_list|(
literal|"a/../../.."
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"../../.."
argument_list|,
name|Files
operator|.
name|simplifyPath
argument_list|(
literal|"a/../../../.."
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// b/4558855
DECL|method|testMadbotsBug ()
specifier|public
name|void
name|testMadbotsBug
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"../this"
argument_list|,
name|Files
operator|.
name|simplifyPath
argument_list|(
literal|"../this"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"../this/is/ok"
argument_list|,
name|Files
operator|.
name|simplifyPath
argument_list|(
literal|"../this/is/ok"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"../ok"
argument_list|,
name|Files
operator|.
name|simplifyPath
argument_list|(
literal|"../this/../ok"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// https://code.google.com/p/guava-libraries/issues/detail?id=705
DECL|method|test705 ()
specifier|public
name|void
name|test705
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"../b"
argument_list|,
name|Files
operator|.
name|simplifyPath
argument_list|(
literal|"x/../../b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|Files
operator|.
name|simplifyPath
argument_list|(
literal|"x/../b"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

