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
name|GwtIncompatible
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Represents a {@linkplain System#getProperties() standard system property}.  *  * @author Kurt Alfred Kluever  * @since 15.0  */
end_comment

begin_enum
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
comment|// java.lang.System#getProperty
DECL|enum|StandardSystemProperty
specifier|public
enum|enum
name|StandardSystemProperty
block|{
comment|/** Java Runtime Environment version. */
DECL|enumConstant|JAVA_VERSION
name|JAVA_VERSION
argument_list|(
literal|"java.version"
argument_list|)
block|,
comment|/** Java Runtime Environment vendor. */
DECL|enumConstant|JAVA_VENDOR
name|JAVA_VENDOR
argument_list|(
literal|"java.vendor"
argument_list|)
block|,
comment|/** Java vendor URL. */
DECL|enumConstant|JAVA_VENDOR_URL
name|JAVA_VENDOR_URL
argument_list|(
literal|"java.vendor.url"
argument_list|)
block|,
comment|/** Java installation directory. */
DECL|enumConstant|JAVA_HOME
name|JAVA_HOME
argument_list|(
literal|"java.home"
argument_list|)
block|,
comment|/** Java Virtual Machine specification version. */
DECL|enumConstant|JAVA_VM_SPECIFICATION_VERSION
name|JAVA_VM_SPECIFICATION_VERSION
argument_list|(
literal|"java.vm.specification.version"
argument_list|)
block|,
comment|/** Java Virtual Machine specification vendor. */
DECL|enumConstant|JAVA_VM_SPECIFICATION_VENDOR
name|JAVA_VM_SPECIFICATION_VENDOR
argument_list|(
literal|"java.vm.specification.vendor"
argument_list|)
block|,
comment|/** Java Virtual Machine specification name. */
DECL|enumConstant|JAVA_VM_SPECIFICATION_NAME
name|JAVA_VM_SPECIFICATION_NAME
argument_list|(
literal|"java.vm.specification.name"
argument_list|)
block|,
comment|/** Java Virtual Machine implementation version. */
DECL|enumConstant|JAVA_VM_VERSION
name|JAVA_VM_VERSION
argument_list|(
literal|"java.vm.version"
argument_list|)
block|,
comment|/** Java Virtual Machine implementation vendor. */
DECL|enumConstant|JAVA_VM_VENDOR
name|JAVA_VM_VENDOR
argument_list|(
literal|"java.vm.vendor"
argument_list|)
block|,
comment|/** Java Virtual Machine implementation name. */
DECL|enumConstant|JAVA_VM_NAME
name|JAVA_VM_NAME
argument_list|(
literal|"java.vm.name"
argument_list|)
block|,
comment|/** Java Runtime Environment specification version. */
DECL|enumConstant|JAVA_SPECIFICATION_VERSION
name|JAVA_SPECIFICATION_VERSION
argument_list|(
literal|"java.specification.version"
argument_list|)
block|,
comment|/** Java Runtime Environment specification vendor. */
DECL|enumConstant|JAVA_SPECIFICATION_VENDOR
name|JAVA_SPECIFICATION_VENDOR
argument_list|(
literal|"java.specification.vendor"
argument_list|)
block|,
comment|/** Java Runtime Environment specification name. */
DECL|enumConstant|JAVA_SPECIFICATION_NAME
name|JAVA_SPECIFICATION_NAME
argument_list|(
literal|"java.specification.name"
argument_list|)
block|,
comment|/** Java class format version number. */
DECL|enumConstant|JAVA_CLASS_VERSION
name|JAVA_CLASS_VERSION
argument_list|(
literal|"java.class.version"
argument_list|)
block|,
comment|/** Java class path. */
DECL|enumConstant|JAVA_CLASS_PATH
name|JAVA_CLASS_PATH
argument_list|(
literal|"java.class.path"
argument_list|)
block|,
comment|/** List of paths to search when loading libraries. */
DECL|enumConstant|JAVA_LIBRARY_PATH
name|JAVA_LIBRARY_PATH
argument_list|(
literal|"java.library.path"
argument_list|)
block|,
comment|/** Default temp file path. */
DECL|enumConstant|JAVA_IO_TMPDIR
name|JAVA_IO_TMPDIR
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
block|,
comment|/** Name of JIT compiler to use. */
DECL|enumConstant|JAVA_COMPILER
name|JAVA_COMPILER
argument_list|(
literal|"java.compiler"
argument_list|)
block|,
comment|/** Path of extension directory or directories. */
DECL|enumConstant|JAVA_EXT_DIRS
name|JAVA_EXT_DIRS
argument_list|(
literal|"java.ext.dirs"
argument_list|)
block|,
comment|/** Operating system name. */
DECL|enumConstant|OS_NAME
name|OS_NAME
argument_list|(
literal|"os.name"
argument_list|)
block|,
comment|/** Operating system architecture. */
DECL|enumConstant|OS_ARCH
name|OS_ARCH
argument_list|(
literal|"os.arch"
argument_list|)
block|,
comment|/** Operating system version. */
DECL|enumConstant|OS_VERSION
name|OS_VERSION
argument_list|(
literal|"os.version"
argument_list|)
block|,
comment|/** File separator ("/" on UNIX). */
DECL|enumConstant|FILE_SEPARATOR
name|FILE_SEPARATOR
argument_list|(
literal|"file.separator"
argument_list|)
block|,
comment|/** Path separator (":" on UNIX). */
DECL|enumConstant|PATH_SEPARATOR
name|PATH_SEPARATOR
argument_list|(
literal|"path.separator"
argument_list|)
block|,
comment|/** Line separator ("\n" on UNIX). */
DECL|enumConstant|LINE_SEPARATOR
name|LINE_SEPARATOR
argument_list|(
literal|"line.separator"
argument_list|)
block|,
comment|/** User's account name. */
DECL|enumConstant|USER_NAME
name|USER_NAME
argument_list|(
literal|"user.name"
argument_list|)
block|,
comment|/** User's home directory. */
DECL|enumConstant|USER_HOME
name|USER_HOME
argument_list|(
literal|"user.home"
argument_list|)
block|,
comment|/** User's current working directory. */
DECL|enumConstant|USER_DIR
name|USER_DIR
argument_list|(
literal|"user.dir"
argument_list|)
block|;
DECL|field|key
specifier|private
specifier|final
name|String
name|key
decl_stmt|;
DECL|method|StandardSystemProperty (String key)
specifier|private
name|StandardSystemProperty
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
block|}
comment|/**    * Returns the key used to lookup this system property.    */
DECL|method|key ()
specifier|public
name|String
name|key
parameter_list|()
block|{
return|return
name|key
return|;
block|}
comment|/**    * Returns the current value for this system property by delegating to    * {@link System#getProperty(String)}.    */
annotation|@
name|Nullable
DECL|method|value ()
specifier|public
name|String
name|value
parameter_list|()
block|{
return|return
name|System
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**    * Returns a string representation of this system property.    */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|key
argument_list|()
operator|+
literal|"="
operator|+
name|value
argument_list|()
return|;
block|}
block|}
end_enum

end_unit

