begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * Contains constant definitions for the standard system property keys.  *  * @author Kurt Alfred Kluever  * @since 14.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|class|StandardSystemProperties
specifier|public
specifier|final
class|class
name|StandardSystemProperties
block|{
DECL|method|StandardSystemProperties ()
specifier|private
name|StandardSystemProperties
parameter_list|()
block|{}
comment|/** Java Runtime Environment version. */
DECL|field|JAVA_VERSION
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_VERSION
init|=
literal|"java.version"
decl_stmt|;
comment|/** Java Runtime Environment vendor. */
DECL|field|JAVA_VENDOR
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_VENDOR
init|=
literal|"java.vendor"
decl_stmt|;
comment|/** Java vendor URL. */
DECL|field|JAVA_VENDOR_URL
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_VENDOR_URL
init|=
literal|"java.vendor.url"
decl_stmt|;
comment|/** Java installation directory. */
DECL|field|JAVA_HOME
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_HOME
init|=
literal|"java.home"
decl_stmt|;
comment|/** Java Virtual Machine specification version. */
DECL|field|JAVA_VM_SPECIFICATION_VERSION
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_VM_SPECIFICATION_VERSION
init|=
literal|"java.vm.specification.version"
decl_stmt|;
comment|/** Java Virtual Machine specification vendor. */
DECL|field|JAVA_VM_SPECIFICATION_VENDOR
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_VM_SPECIFICATION_VENDOR
init|=
literal|"java.vm.specification.vendor"
decl_stmt|;
comment|/** Java Virtual Machine specification name. */
DECL|field|JAVA_VM_SPECIFICATION_NAME
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_VM_SPECIFICATION_NAME
init|=
literal|"java.vm.specification.name"
decl_stmt|;
comment|/** Java Virtual Machine implementation version. */
DECL|field|JAVA_VM_VERSION
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_VM_VERSION
init|=
literal|"java.vm.version"
decl_stmt|;
comment|/** Java Virtual Machine implementation vendor. */
DECL|field|JAVA_VM_VENDOR
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_VM_VENDOR
init|=
literal|"java.vm.vendor"
decl_stmt|;
comment|/** Java Virtual Machine implementation name. */
DECL|field|JAVA_VM_NAME
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_VM_NAME
init|=
literal|"java.vm.name"
decl_stmt|;
comment|/** Java Runtime Environment specification version. */
DECL|field|JAVA_SPECIFICATION_VERSION
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_SPECIFICATION_VERSION
init|=
literal|"java.specification.version"
decl_stmt|;
comment|/** Java Runtime Environment specification vendor. */
DECL|field|JAVA_SPECIFICATION_VENDOR
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_SPECIFICATION_VENDOR
init|=
literal|"java.specification.vendor"
decl_stmt|;
comment|/** Java Runtime Environment specification name. */
DECL|field|JAVA_SPECIFICATION_NAME
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_SPECIFICATION_NAME
init|=
literal|"java.specification.name"
decl_stmt|;
comment|/** Java class format version number. */
DECL|field|JAVA_CLASS_VERSION
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_CLASS_VERSION
init|=
literal|"java.class.version"
decl_stmt|;
comment|/** Java class path. */
DECL|field|JAVA_CLASS_PATH
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_CLASS_PATH
init|=
literal|"java.class.path"
decl_stmt|;
comment|/** List of paths to search when loading libraries. */
DECL|field|JAVA_LIBRARY_PATH
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_LIBRARY_PATH
init|=
literal|"java.library.path"
decl_stmt|;
comment|/** Default temp file path. */
DECL|field|JAVA_IO_TMPDIR
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_IO_TMPDIR
init|=
literal|"java.io.tmpdir"
decl_stmt|;
comment|/** Name of JIT compiler to use. */
DECL|field|JAVA_COMPILER
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_COMPILER
init|=
literal|"java.compiler"
decl_stmt|;
comment|/** Path of extension directory or directories. */
DECL|field|JAVA_EXT_DIRS
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_EXT_DIRS
init|=
literal|"java.ext.dirs"
decl_stmt|;
comment|/** Operating system name. */
DECL|field|OS_NAME
specifier|public
specifier|static
specifier|final
name|String
name|OS_NAME
init|=
literal|"os.name"
decl_stmt|;
comment|/** Operating system architecture. */
DECL|field|OS_ARCH
specifier|public
specifier|static
specifier|final
name|String
name|OS_ARCH
init|=
literal|"os.arch"
decl_stmt|;
comment|/** Operating system version. */
DECL|field|OS_VERSION
specifier|public
specifier|static
specifier|final
name|String
name|OS_VERSION
init|=
literal|"os.version"
decl_stmt|;
comment|/** File separator ("/" on UNIX). */
DECL|field|FILE_SEPARATOR
specifier|public
specifier|static
specifier|final
name|String
name|FILE_SEPARATOR
init|=
literal|"file.separator"
decl_stmt|;
comment|/** Path separator (":" on UNIX). */
DECL|field|PATH_SEPARATOR
specifier|public
specifier|static
specifier|final
name|String
name|PATH_SEPARATOR
init|=
literal|"path.separator"
decl_stmt|;
comment|/** Line separator ("\n" on UNIX). */
DECL|field|LINE_SEPARATOR
specifier|public
specifier|static
specifier|final
name|String
name|LINE_SEPARATOR
init|=
literal|"line.separator"
decl_stmt|;
comment|/** User's account name. */
DECL|field|USER_NAME
specifier|public
specifier|static
specifier|final
name|String
name|USER_NAME
init|=
literal|"user.name"
decl_stmt|;
comment|/** User's home directory. */
DECL|field|USER_HOME
specifier|public
specifier|static
specifier|final
name|String
name|USER_HOME
init|=
literal|"user.home"
decl_stmt|;
comment|/** User's current working directory. */
DECL|field|USER_DIR
specifier|public
specifier|static
specifier|final
name|String
name|USER_DIR
init|=
literal|"user.dir"
decl_stmt|;
block|}
end_class

end_unit

