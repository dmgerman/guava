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
name|io
operator|.
name|Files
operator|.
name|simplifyPath
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
name|Splitter
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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_comment
comment|/**  * Unit tests for {@link Files#simplifyPath}.  *  * @author Pablo Bellver  */
end_comment

begin_class
DECL|class|FilesSimplifyPathTest
specifier|public
class|class
name|FilesSimplifyPathTest
extends|extends
name|TestCase
block|{
DECL|method|testSimplifyEmptyString ()
specifier|public
name|void
name|testSimplifyEmptyString
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"."
argument_list|,
name|simplifyPath
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimplifyDot ()
specifier|public
name|void
name|testSimplifyDot
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"."
argument_list|,
name|simplifyPath
argument_list|(
literal|"."
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimplifyWhiteSpace ()
specifier|public
name|void
name|testSimplifyWhiteSpace
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|" "
argument_list|,
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
name|simplifyPath
argument_list|(
literal|"a/../../../.."
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimplifyRootedDotDots ()
specifier|public
name|void
name|testSimplifyRootedDotDots
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"/"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/../../.."
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/../../../"
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
name|simplifyPath
argument_list|(
literal|"x/../b"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// https://code.google.com/p/guava-libraries/issues/detail?id=716
DECL|method|test716 ()
specifier|public
name|void
name|test716
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|simplifyPath
argument_list|(
literal|"./b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|simplifyPath
argument_list|(
literal|"./b/."
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|simplifyPath
argument_list|(
literal|"././b/./."
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|simplifyPath
argument_list|(
literal|"././b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a/b"
argument_list|,
name|simplifyPath
argument_list|(
literal|"./a/b"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testHiddenFiles ()
specifier|public
name|void
name|testHiddenFiles
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|".b"
argument_list|,
name|simplifyPath
argument_list|(
literal|".b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|".b"
argument_list|,
name|simplifyPath
argument_list|(
literal|"./.b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|".metadata/b"
argument_list|,
name|simplifyPath
argument_list|(
literal|".metadata/b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|".metadata/b"
argument_list|,
name|simplifyPath
argument_list|(
literal|"./.metadata/b"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// https://code.google.com/p/guava-libraries/issues/detail?id=716
DECL|method|testMultipleDotFilenames ()
specifier|public
name|void
name|testMultipleDotFilenames
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"..a"
argument_list|,
name|simplifyPath
argument_list|(
literal|"..a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/..a"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/..a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/..a/..b"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/..a/..b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/.....a/..b"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/.....a/..b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"..../...."
argument_list|,
name|simplifyPath
argument_list|(
literal|"..../...."
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"..a../..b.."
argument_list|,
name|simplifyPath
argument_list|(
literal|"..a../..b.."
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSlashDot ()
specifier|public
name|void
name|testSlashDot
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"/"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/."
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// http://code.google.com/p/guava-libraries/issues/detail?id=722
DECL|method|testInitialSlashDotDot ()
specifier|public
name|void
name|testInitialSlashDotDot
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"/c"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/../c"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// http://code.google.com/p/guava-libraries/issues/detail?id=722
DECL|method|testInitialSlashDot ()
specifier|public
name|void
name|testInitialSlashDot
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"/a"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/./a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/.a"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/.a/a/.."
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// http://code.google.com/p/guava-libraries/issues/detail?id=722
DECL|method|testConsecutiveParentsAfterPresent ()
specifier|public
name|void
name|testConsecutiveParentsAfterPresent
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"../.."
argument_list|,
name|simplifyPath
argument_list|(
literal|"./../../"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"../.."
argument_list|,
name|simplifyPath
argument_list|(
literal|"./.././../"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/*    * We co-opt some URI resolution tests for our purposes.    * Some of the tests have queries and anchors that are a little silly here.    */
comment|/** http://gbiv.com/protocols/uri/rfc/rfc2396.html#rfc.section.C.1 */
DECL|method|testRfc2396Normal ()
specifier|public
name|void
name|testRfc2396Normal
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"/a/b/c/g"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/g"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/g"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/./g"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/g"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/g/"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/g?y"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/g?y"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/g#s"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/g#s"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/g?y#s"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/g?y#s"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/;x"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/;x"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/g;x"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/g;x"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/g;x?y#s"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/g;x?y#s"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/."
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/./"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/.."
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/../"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/g"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/../g"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/../.."
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/../../"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/g"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/../../g"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/** http://gbiv.com/protocols/uri/rfc/rfc2396.html#rfc.section.C.2 */
DECL|method|testRfc2396Abnormal ()
specifier|public
name|void
name|testRfc2396Abnormal
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"/a/b/c/g."
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/g."
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/.g"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/.g"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/g.."
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/g.."
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/..g"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/..g"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/g"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/./../g"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/g"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/./g/."
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/g/h"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/g/./h"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/h"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/g/../h"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/g;x=1/y"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/g;x=1/./y"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/y"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/g;x=1/../y"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/** http://gbiv.com/protocols/uri/rfc/rfc3986.html#relative-normal */
DECL|method|testRfc3986Normal ()
specifier|public
name|void
name|testRfc3986Normal
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"/a/b/c/g"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/g"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/g"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/./g"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/g"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/g/"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/g?y"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/g?y"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/g#s"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/g#s"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/g?y#s"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/g?y#s"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/;x"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/;x"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/g;x"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/g;x"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/g;x?y#s"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/g;x?y#s"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/."
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/./"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/.."
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/../"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/g"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/../g"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/../.."
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/../../"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/g"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/../../g"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/** http://gbiv.com/protocols/uri/rfc/rfc3986.html#relative-abnormal */
DECL|method|testRfc3986Abnormal ()
specifier|public
name|void
name|testRfc3986Abnormal
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"/g"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/../../../g"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/g"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/../../../../g"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/g."
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/g."
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/.g"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/.g"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/g.."
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/g.."
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/..g"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/..g"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/g"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/./../g"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/g"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/./g/."
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/g/h"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/g/./h"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/h"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/g/../h"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/g;x=1/y"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/g;x=1/./y"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a/b/c/y"
argument_list|,
name|simplifyPath
argument_list|(
literal|"/a/b/c/g;x=1/../y"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testExtensive ()
specifier|public
name|void
name|testExtensive
parameter_list|()
throws|throws
name|IOException
block|{
name|Splitter
name|splitter
init|=
name|Splitter
operator|.
name|on
argument_list|(
name|CharMatcher
operator|.
name|WHITESPACE
argument_list|)
decl_stmt|;
comment|// Inputs are /b/c/<every possible 10-character string of characters "a./">
comment|// Expected outputs are from realpath -s.
name|URL
name|url
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"testdata/realpathtests.txt"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|line
range|:
name|Resources
operator|.
name|readLines
argument_list|(
name|url
argument_list|,
name|UTF_8
argument_list|)
control|)
block|{
name|Iterator
argument_list|<
name|String
argument_list|>
name|iterator
init|=
name|splitter
operator|.
name|split
argument_list|(
name|line
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|String
name|input
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|expectedOutput
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|iterator
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedOutput
argument_list|,
name|simplifyPath
argument_list|(
name|input
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

