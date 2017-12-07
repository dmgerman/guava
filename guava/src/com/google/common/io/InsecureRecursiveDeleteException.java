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
name|FileSystemException
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

begin_import
import|import
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|compatqual
operator|.
name|NullableDecl
import|;
end_import

begin_comment
comment|/**  * Exception indicating that a recursive delete can't be performed because the file system does not  * have the support necessary to guarantee that it is not vulnerable to race conditions that would  * allow it to delete files and directories outside of the directory being deleted (i.e., {@link  * SecureDirectoryStream} is not supported).  *  *<p>{@link RecursiveDeleteOption#ALLOW_INSECURE} can be used to force the recursive delete method  * to proceed anyway.  *  * @since 21.0  * @author Colin Decker  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
annotation|@
name|J2ObjCIncompatible
comment|// java.nio.file
DECL|class|InsecureRecursiveDeleteException
specifier|public
specifier|final
class|class
name|InsecureRecursiveDeleteException
extends|extends
name|FileSystemException
block|{
DECL|method|InsecureRecursiveDeleteException (@ullableDecl String file)
specifier|public
name|InsecureRecursiveDeleteException
parameter_list|(
annotation|@
name|NullableDecl
name|String
name|file
parameter_list|)
block|{
name|super
argument_list|(
name|file
argument_list|,
literal|null
argument_list|,
literal|"unable to guarantee security of recursive delete"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

