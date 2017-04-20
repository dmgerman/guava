begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|CharMatcher
operator|.
name|whitespace
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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|Charsets
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
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|DataInputStream
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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestSuite
import|;
end_import

begin_comment
comment|/**  * Unit test for {@link Resources}.  *  * @author Chris Nokleberg  */
end_comment

begin_class
DECL|class|ResourcesTest
specifier|public
class|class
name|ResourcesTest
extends|extends
name|IoTestCase
block|{
DECL|method|suite ()
specifier|public
specifier|static
name|TestSuite
name|suite
parameter_list|()
block|{
name|TestSuite
name|suite
init|=
operator|new
name|TestSuite
argument_list|()
decl_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|ByteSourceTester
operator|.
name|tests
argument_list|(
literal|"Resources.asByteSource[URL]"
argument_list|,
name|SourceSinkFactories
operator|.
name|urlByteSourceFactory
argument_list|()
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|CharSourceTester
operator|.
name|tests
argument_list|(
literal|"Resources.asCharSource[URL, Charset]"
argument_list|,
name|SourceSinkFactories
operator|.
name|urlCharSourceFactory
argument_list|()
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTestSuite
argument_list|(
name|ResourcesTest
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|suite
return|;
block|}
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
throws|throws
name|IOException
block|{
name|URL
name|resource
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"testdata/i18n.txt"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|I18N
argument_list|,
name|Resources
operator|.
name|toString
argument_list|(
name|resource
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|Resources
operator|.
name|toString
argument_list|(
name|resource
argument_list|,
name|Charsets
operator|.
name|US_ASCII
argument_list|)
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|I18N
argument_list|)
expr_stmt|;
block|}
DECL|method|testToToByteArray ()
specifier|public
name|void
name|testToToByteArray
parameter_list|()
throws|throws
name|IOException
block|{
name|byte
index|[]
name|data
init|=
name|Resources
operator|.
name|toByteArray
argument_list|(
name|classfile
argument_list|(
name|Resources
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0xCAFEBABE
argument_list|,
operator|new
name|DataInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
argument_list|)
operator|.
name|readInt
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testReadLines ()
specifier|public
name|void
name|testReadLines
parameter_list|()
throws|throws
name|IOException
block|{
comment|// TODO(chrisn): Check in a better resource
name|URL
name|resource
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"testdata/i18n.txt"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|I18N
argument_list|)
argument_list|,
name|Resources
operator|.
name|readLines
argument_list|(
name|resource
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testReadLines_withLineProcessor ()
specifier|public
name|void
name|testReadLines_withLineProcessor
parameter_list|()
throws|throws
name|IOException
block|{
name|URL
name|resource
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"testdata/alice_in_wonderland.txt"
argument_list|)
decl_stmt|;
name|LineProcessor
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|collectAndLowercaseAndTrim
init|=
operator|new
name|LineProcessor
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|collector
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|processLine
parameter_list|(
name|String
name|line
parameter_list|)
block|{
name|collector
operator|.
name|add
argument_list|(
name|whitespace
argument_list|()
operator|.
name|trimFrom
argument_list|(
name|line
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getResult
parameter_list|()
block|{
return|return
name|collector
return|;
block|}
block|}
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|result
init|=
name|Resources
operator|.
name|readLines
argument_list|(
name|resource
argument_list|,
name|Charsets
operator|.
name|US_ASCII
argument_list|,
name|collectAndLowercaseAndTrim
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3600
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ALICE'S ADVENTURES IN WONDERLAND"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"THE END"
argument_list|,
name|result
operator|.
name|get
argument_list|(
name|result
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCopyToOutputStream ()
specifier|public
name|void
name|testCopyToOutputStream
parameter_list|()
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|URL
name|resource
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"testdata/i18n.txt"
argument_list|)
decl_stmt|;
name|Resources
operator|.
name|copy
argument_list|(
name|resource
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|I18N
argument_list|,
name|out
operator|.
name|toString
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetResource_notFound ()
specifier|public
name|void
name|testGetResource_notFound
parameter_list|()
block|{
try|try
block|{
name|Resources
operator|.
name|getResource
argument_list|(
literal|"no such resource"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertThat
argument_list|(
name|e
argument_list|)
operator|.
name|hasMessage
argument_list|(
literal|"resource no such resource not found."
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testGetResource ()
specifier|public
name|void
name|testGetResource
parameter_list|()
block|{
name|assertNotNull
argument_list|(
name|Resources
operator|.
name|getResource
argument_list|(
literal|"com/google/common/io/testdata/i18n.txt"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetResource_relativePath_notFound ()
specifier|public
name|void
name|testGetResource_relativePath_notFound
parameter_list|()
block|{
try|try
block|{
name|Resources
operator|.
name|getResource
argument_list|(
name|getClass
argument_list|()
argument_list|,
literal|"com/google/common/io/testdata/i18n.txt"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertThat
argument_list|(
name|e
argument_list|)
operator|.
name|hasMessage
argument_list|(
literal|"resource com/google/common/io/testdata/i18n.txt"
operator|+
literal|" relative to com.google.common.io.ResourcesTest not found."
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testGetResource_relativePath ()
specifier|public
name|void
name|testGetResource_relativePath
parameter_list|()
block|{
name|assertNotNull
argument_list|(
name|Resources
operator|.
name|getResource
argument_list|(
name|getClass
argument_list|()
argument_list|,
literal|"testdata/i18n.txt"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetResource_contextClassLoader ()
specifier|public
name|void
name|testGetResource_contextClassLoader
parameter_list|()
throws|throws
name|IOException
block|{
comment|// Check that we can find a resource if it is visible to the context class
comment|// loader, even if it is not visible to the loader of the Resources class.
name|File
name|tempFile
init|=
name|createTempFile
argument_list|()
decl_stmt|;
name|PrintWriter
name|writer
init|=
operator|new
name|PrintWriter
argument_list|(
name|tempFile
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"rud a chur ar an mÃ©ar fhada"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// First check that we can't find it without setting the context loader.
comment|// This is a sanity check that the test doesn't spuriously pass because
comment|// the resource is visible to the system class loader.
try|try
block|{
name|Resources
operator|.
name|getResource
argument_list|(
name|tempFile
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should get IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
comment|// Now set the context loader to one that should find the resource.
name|URL
name|baseUrl
init|=
name|tempFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
decl_stmt|;
name|URLClassLoader
name|loader
init|=
operator|new
name|URLClassLoader
argument_list|(
operator|new
name|URL
index|[]
block|{
name|baseUrl
block|}
argument_list|)
decl_stmt|;
name|ClassLoader
name|oldContextLoader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|loader
argument_list|)
expr_stmt|;
name|URL
name|url
init|=
name|Resources
operator|.
name|getResource
argument_list|(
name|tempFile
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|text
init|=
name|Resources
operator|.
name|toString
argument_list|(
name|url
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"rud a chur ar an mÃ©ar fhada\n"
argument_list|,
name|text
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|oldContextLoader
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testGetResource_contextClassLoaderNull ()
specifier|public
name|void
name|testGetResource_contextClassLoaderNull
parameter_list|()
block|{
name|ClassLoader
name|oldContextLoader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|Resources
operator|.
name|getResource
argument_list|(
literal|"com/google/common/io/testdata/i18n.txt"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|Resources
operator|.
name|getResource
argument_list|(
literal|"no such resource"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should get IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{       }
block|}
finally|finally
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|oldContextLoader
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testNulls ()
specifier|public
name|void
name|testNulls
parameter_list|()
block|{
operator|new
name|NullPointerTester
argument_list|()
operator|.
name|setDefault
argument_list|(
name|URL
operator|.
name|class
argument_list|,
name|classfile
argument_list|(
name|ResourcesTest
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|testAllPublicStaticMethods
argument_list|(
name|Resources
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|classfile (Class<?> c)
specifier|private
specifier|static
name|URL
name|classfile
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
return|return
name|c
operator|.
name|getResource
argument_list|(
name|c
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|".class"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

