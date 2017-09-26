begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2017 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.util.concurrent
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
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
name|StandardSystemProperty
operator|.
name|JAVA_CLASS_PATH
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
name|StandardSystemProperty
operator|.
name|PATH_SEPARATOR
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
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
name|net
operator|.
name|URLClassLoader
import|;
end_import

begin_comment
comment|// TODO(b/65488446): Make this a public API.
end_comment

begin_comment
comment|/** Utility method to parse the system class path. */
end_comment

begin_class
DECL|class|ClassPathUtil
specifier|final
class|class
name|ClassPathUtil
block|{
DECL|method|ClassPathUtil ()
specifier|private
name|ClassPathUtil
parameter_list|()
block|{}
comment|/**    * Returns the URLs in the class path specified by the {@code java.class.path} {@linkplain    * System#getProperty system property}.    */
comment|// TODO(b/65488446): Make this a public API.
DECL|method|parseJavaClassPath ()
specifier|static
name|URL
index|[]
name|parseJavaClassPath
parameter_list|()
block|{
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|URL
argument_list|>
name|urls
init|=
name|ImmutableList
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|entry
range|:
name|Splitter
operator|.
name|on
argument_list|(
name|PATH_SEPARATOR
operator|.
name|value
argument_list|()
argument_list|)
operator|.
name|split
argument_list|(
name|JAVA_CLASS_PATH
operator|.
name|value
argument_list|()
argument_list|)
control|)
block|{
try|try
block|{
try|try
block|{
name|urls
operator|.
name|add
argument_list|(
operator|new
name|File
argument_list|(
name|entry
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|e
parameter_list|)
block|{
comment|// File.toURI checks to see if the file is a directory
name|urls
operator|.
name|add
argument_list|(
operator|new
name|URL
argument_list|(
literal|"file"
argument_list|,
literal|null
argument_list|,
operator|new
name|File
argument_list|(
name|entry
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|AssertionError
name|error
init|=
operator|new
name|AssertionError
argument_list|(
literal|"malformed class path entry: "
operator|+
name|entry
argument_list|)
decl_stmt|;
name|error
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|error
throw|;
block|}
block|}
return|return
name|urls
operator|.
name|build
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|URL
index|[
literal|0
index|]
argument_list|)
return|;
block|}
comment|/** Returns the URLs in the class path. */
DECL|method|getClassPathUrls ()
specifier|static
name|URL
index|[]
name|getClassPathUrls
parameter_list|()
block|{
return|return
name|ClassPathUtil
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|instanceof
name|URLClassLoader
condition|?
operator|(
operator|(
name|URLClassLoader
operator|)
name|ClassPathUtil
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|)
operator|.
name|getURLs
argument_list|()
else|:
name|parseJavaClassPath
argument_list|()
return|;
block|}
block|}
end_class

end_unit
