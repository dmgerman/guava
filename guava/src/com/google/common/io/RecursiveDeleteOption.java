begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2014 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|GwtIncompatible
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|j2objc
operator|.
name|annotations
operator|.
name|J2ObjCIncompatible
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|SecureDirectoryStream
import|;
end_import

begin_comment
comment|/**  * Options for use with recursive delete methods ({@link MoreFiles#deleteRecursively} and  * {@link MoreFiles#deleteDirectoryContents}).  *  * @since 21.0  * @author Colin Decker  */
end_comment

begin_enum
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
annotation|@
name|J2ObjCIncompatible
comment|// java.nio.file
DECL|enum|RecursiveDeleteOption
specifier|public
enum|enum
name|RecursiveDeleteOption
block|{
comment|/**    * Specifies that the recursive delete should not throw an exception when it can't be guaranteed    * that it can be done securely, without vulnerability to race conditions (i.e. when the file    * system does not support {@link SecureDirectoryStream}).    *    *<p><b>Warning:</b> On a file system that supports symbolic links, it is possible for an    * insecure recursive delete to delete files and directories that are<i>outside</i> the    * directory being deleted. This can happen if, after checking that a file is a directory (and    * not a symbolic link), that directory is deleted and replaced by a symbolic link to an outside    * directory before the call that opens the directory to read its entries. File systems that    * support {@code SecureDirectoryStream} do not have this vulnerability.    */
DECL|enumConstant|ALLOW_INSECURE
name|ALLOW_INSECURE
block|}
end_enum

end_unit

