begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Preconditions
operator|.
name|checkNotNull
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
name|SourceSinkFactory
operator|.
name|ByteSinkFactory
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
name|SourceSinkFactory
operator|.
name|ByteSourceFactory
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
name|SourceSinkFactory
operator|.
name|CharSinkFactory
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
name|SourceSinkFactory
operator|.
name|CharSourceFactory
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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|CharBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|CheckForNull
import|;
end_import

begin_comment
comment|/**  * {@link SourceSinkFactory} implementations.  *  * @author Colin Decker  */
end_comment

begin_class
DECL|class|SourceSinkFactories
specifier|public
class|class
name|SourceSinkFactories
block|{
DECL|method|SourceSinkFactories ()
specifier|private
name|SourceSinkFactories
parameter_list|()
block|{}
DECL|method|stringCharSourceFactory ()
specifier|public
specifier|static
name|CharSourceFactory
name|stringCharSourceFactory
parameter_list|()
block|{
return|return
operator|new
name|StringSourceFactory
argument_list|()
return|;
block|}
DECL|method|byteArraySourceFactory ()
specifier|public
specifier|static
name|ByteSourceFactory
name|byteArraySourceFactory
parameter_list|()
block|{
return|return
operator|new
name|ByteArraySourceFactory
argument_list|()
return|;
block|}
DECL|method|emptyByteSourceFactory ()
specifier|public
specifier|static
name|ByteSourceFactory
name|emptyByteSourceFactory
parameter_list|()
block|{
return|return
operator|new
name|EmptyByteSourceFactory
argument_list|()
return|;
block|}
DECL|method|emptyCharSourceFactory ()
specifier|public
specifier|static
name|CharSourceFactory
name|emptyCharSourceFactory
parameter_list|()
block|{
return|return
operator|new
name|EmptyCharSourceFactory
argument_list|()
return|;
block|}
DECL|method|fileByteSourceFactory ()
specifier|public
specifier|static
name|ByteSourceFactory
name|fileByteSourceFactory
parameter_list|()
block|{
return|return
operator|new
name|FileByteSourceFactory
argument_list|()
return|;
block|}
DECL|method|fileByteSinkFactory ()
specifier|public
specifier|static
name|ByteSinkFactory
name|fileByteSinkFactory
parameter_list|()
block|{
return|return
operator|new
name|FileByteSinkFactory
argument_list|(
literal|null
argument_list|)
return|;
block|}
DECL|method|appendingFileByteSinkFactory ()
specifier|public
specifier|static
name|ByteSinkFactory
name|appendingFileByteSinkFactory
parameter_list|()
block|{
name|String
name|initialString
init|=
name|IoTestCase
operator|.
name|ASCII
operator|+
name|IoTestCase
operator|.
name|I18N
decl_stmt|;
return|return
operator|new
name|FileByteSinkFactory
argument_list|(
name|initialString
operator|.
name|getBytes
argument_list|(
name|Charsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
return|;
block|}
DECL|method|fileCharSourceFactory ()
specifier|public
specifier|static
name|CharSourceFactory
name|fileCharSourceFactory
parameter_list|()
block|{
return|return
operator|new
name|FileCharSourceFactory
argument_list|()
return|;
block|}
DECL|method|fileCharSinkFactory ()
specifier|public
specifier|static
name|CharSinkFactory
name|fileCharSinkFactory
parameter_list|()
block|{
return|return
operator|new
name|FileCharSinkFactory
argument_list|(
literal|null
argument_list|)
return|;
block|}
DECL|method|appendingFileCharSinkFactory ()
specifier|public
specifier|static
name|CharSinkFactory
name|appendingFileCharSinkFactory
parameter_list|()
block|{
name|String
name|initialString
init|=
name|IoTestCase
operator|.
name|ASCII
operator|+
name|IoTestCase
operator|.
name|I18N
decl_stmt|;
return|return
operator|new
name|FileCharSinkFactory
argument_list|(
name|initialString
argument_list|)
return|;
block|}
DECL|method|urlByteSourceFactory ()
specifier|public
specifier|static
name|ByteSourceFactory
name|urlByteSourceFactory
parameter_list|()
block|{
return|return
operator|new
name|UrlByteSourceFactory
argument_list|()
return|;
block|}
DECL|method|urlCharSourceFactory ()
specifier|public
specifier|static
name|CharSourceFactory
name|urlCharSourceFactory
parameter_list|()
block|{
return|return
operator|new
name|UrlCharSourceFactory
argument_list|()
return|;
block|}
DECL|method|asByteSourceFactory (final CharSourceFactory factory)
specifier|public
specifier|static
name|ByteSourceFactory
name|asByteSourceFactory
parameter_list|(
specifier|final
name|CharSourceFactory
name|factory
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|factory
argument_list|)
expr_stmt|;
return|return
operator|new
name|ByteSourceFactory
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ByteSource
name|createSource
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|factory
operator|.
name|createSource
argument_list|(
operator|new
name|String
argument_list|(
name|data
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
operator|.
name|asByteSource
argument_list|(
name|Charsets
operator|.
name|UTF_8
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|getExpected
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
block|{
return|return
name|factory
operator|.
name|getExpected
argument_list|(
operator|new
name|String
argument_list|(
name|data
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
operator|.
name|getBytes
argument_list|(
name|Charsets
operator|.
name|UTF_8
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|IOException
block|{
name|factory
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|asCharSourceFactory (final ByteSourceFactory factory)
specifier|public
specifier|static
name|CharSourceFactory
name|asCharSourceFactory
parameter_list|(
specifier|final
name|ByteSourceFactory
name|factory
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|factory
argument_list|)
expr_stmt|;
return|return
operator|new
name|CharSourceFactory
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|CharSource
name|createSource
parameter_list|(
name|String
name|string
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|factory
operator|.
name|createSource
argument_list|(
name|string
operator|.
name|getBytes
argument_list|(
name|Charsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
operator|.
name|asCharSource
argument_list|(
name|Charsets
operator|.
name|UTF_8
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getExpected
parameter_list|(
name|String
name|data
parameter_list|)
block|{
return|return
operator|new
name|String
argument_list|(
name|factory
operator|.
name|getExpected
argument_list|(
name|data
operator|.
name|getBytes
argument_list|(
name|Charsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|IOException
block|{
name|factory
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|asCharSinkFactory (final ByteSinkFactory factory)
specifier|public
specifier|static
name|CharSinkFactory
name|asCharSinkFactory
parameter_list|(
specifier|final
name|ByteSinkFactory
name|factory
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|factory
argument_list|)
expr_stmt|;
return|return
operator|new
name|CharSinkFactory
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|CharSink
name|createSink
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|factory
operator|.
name|createSink
argument_list|()
operator|.
name|asCharSink
argument_list|(
name|Charsets
operator|.
name|UTF_8
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getSinkContents
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|String
argument_list|(
name|factory
operator|.
name|getSinkContents
argument_list|()
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getExpected
parameter_list|(
name|String
name|data
parameter_list|)
block|{
comment|/*          * Get what the byte sink factory would expect for no written bytes, then append expected          * string to that.          */
name|byte
index|[]
name|factoryExpectedForNothing
init|=
name|factory
operator|.
name|getExpected
argument_list|(
operator|new
name|byte
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
return|return
operator|new
name|String
argument_list|(
name|factoryExpectedForNothing
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
operator|+
name|checkNotNull
argument_list|(
name|data
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|IOException
block|{
name|factory
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|asSlicedByteSourceFactory ( final ByteSourceFactory factory, final long off, final long len)
specifier|public
specifier|static
name|ByteSourceFactory
name|asSlicedByteSourceFactory
parameter_list|(
specifier|final
name|ByteSourceFactory
name|factory
parameter_list|,
specifier|final
name|long
name|off
parameter_list|,
specifier|final
name|long
name|len
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|factory
argument_list|)
expr_stmt|;
return|return
operator|new
name|ByteSourceFactory
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ByteSource
name|createSource
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|factory
operator|.
name|createSource
argument_list|(
name|bytes
argument_list|)
operator|.
name|slice
argument_list|(
name|off
argument_list|,
name|len
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|getExpected
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
name|byte
index|[]
name|baseExpected
init|=
name|factory
operator|.
name|getExpected
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|int
name|startOffset
init|=
operator|(
name|int
operator|)
name|Math
operator|.
name|min
argument_list|(
name|off
argument_list|,
name|baseExpected
operator|.
name|length
argument_list|)
decl_stmt|;
name|int
name|actualLen
init|=
operator|(
name|int
operator|)
name|Math
operator|.
name|min
argument_list|(
name|len
argument_list|,
name|baseExpected
operator|.
name|length
operator|-
name|startOffset
argument_list|)
decl_stmt|;
return|return
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|baseExpected
argument_list|,
name|startOffset
argument_list|,
name|startOffset
operator|+
name|actualLen
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|IOException
block|{
name|factory
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|StringSourceFactory
specifier|private
specifier|static
class|class
name|StringSourceFactory
implements|implements
name|CharSourceFactory
block|{
annotation|@
name|Override
DECL|method|createSource (String data)
specifier|public
name|CharSource
name|createSource
parameter_list|(
name|String
name|data
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|CharSource
operator|.
name|wrap
argument_list|(
name|data
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getExpected (String data)
specifier|public
name|String
name|getExpected
parameter_list|(
name|String
name|data
parameter_list|)
block|{
return|return
name|data
return|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|IOException
block|{}
block|}
DECL|class|ByteArraySourceFactory
specifier|private
specifier|static
class|class
name|ByteArraySourceFactory
implements|implements
name|ByteSourceFactory
block|{
annotation|@
name|Override
DECL|method|createSource (byte[] bytes)
specifier|public
name|ByteSource
name|createSource
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|ByteSource
operator|.
name|wrap
argument_list|(
name|bytes
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getExpected (byte[] bytes)
specifier|public
name|byte
index|[]
name|getExpected
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
return|return
name|bytes
return|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|IOException
block|{}
block|}
DECL|class|EmptyCharSourceFactory
specifier|private
specifier|static
class|class
name|EmptyCharSourceFactory
implements|implements
name|CharSourceFactory
block|{
annotation|@
name|Override
DECL|method|createSource (String data)
specifier|public
name|CharSource
name|createSource
parameter_list|(
name|String
name|data
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|CharSource
operator|.
name|empty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getExpected (String data)
specifier|public
name|String
name|getExpected
parameter_list|(
name|String
name|data
parameter_list|)
block|{
return|return
literal|""
return|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|IOException
block|{}
block|}
DECL|class|EmptyByteSourceFactory
specifier|private
specifier|static
class|class
name|EmptyByteSourceFactory
implements|implements
name|ByteSourceFactory
block|{
annotation|@
name|Override
DECL|method|createSource (byte[] bytes)
specifier|public
name|ByteSource
name|createSource
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|ByteSource
operator|.
name|empty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getExpected (byte[] bytes)
specifier|public
name|byte
index|[]
name|getExpected
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
return|return
operator|new
name|byte
index|[
literal|0
index|]
return|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|IOException
block|{}
block|}
DECL|class|FileFactory
specifier|private
specifier|abstract
specifier|static
class|class
name|FileFactory
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|Logger
name|logger
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|FileFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|fileThreadLocal
specifier|private
specifier|final
name|ThreadLocal
argument_list|<
name|File
argument_list|>
name|fileThreadLocal
init|=
operator|new
name|ThreadLocal
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|createFile ()
specifier|protected
name|File
name|createFile
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|file
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"SinkSourceFile"
argument_list|,
literal|"txt"
argument_list|)
decl_stmt|;
name|fileThreadLocal
operator|.
name|set
argument_list|(
name|file
argument_list|)
expr_stmt|;
return|return
name|file
return|;
block|}
DECL|method|getFile ()
specifier|protected
name|File
name|getFile
parameter_list|()
block|{
return|return
name|fileThreadLocal
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|tearDown ()
specifier|public
specifier|final
name|void
name|tearDown
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|fileThreadLocal
operator|.
name|get
argument_list|()
operator|.
name|delete
argument_list|()
condition|)
block|{
name|logger
operator|.
name|warning
argument_list|(
literal|"Unable to delete file: "
operator|+
name|fileThreadLocal
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|fileThreadLocal
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
DECL|class|FileByteSourceFactory
specifier|private
specifier|static
class|class
name|FileByteSourceFactory
extends|extends
name|FileFactory
implements|implements
name|ByteSourceFactory
block|{
annotation|@
name|Override
DECL|method|createSource (byte[] bytes)
specifier|public
name|ByteSource
name|createSource
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
throws|throws
name|IOException
block|{
name|checkNotNull
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
name|File
name|file
init|=
name|createFile
argument_list|()
decl_stmt|;
name|OutputStream
name|out
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
try|try
block|{
name|out
operator|.
name|write
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|Files
operator|.
name|asByteSource
argument_list|(
name|file
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getExpected (byte[] bytes)
specifier|public
name|byte
index|[]
name|getExpected
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
return|return
name|checkNotNull
argument_list|(
name|bytes
argument_list|)
return|;
block|}
block|}
DECL|class|FileByteSinkFactory
specifier|private
specifier|static
class|class
name|FileByteSinkFactory
extends|extends
name|FileFactory
implements|implements
name|ByteSinkFactory
block|{
DECL|field|initialBytes
specifier|private
specifier|final
name|byte
index|[]
name|initialBytes
decl_stmt|;
DECL|method|FileByteSinkFactory (@heckForNull byte[] initialBytes)
specifier|private
name|FileByteSinkFactory
parameter_list|(
annotation|@
name|CheckForNull
name|byte
index|[]
name|initialBytes
parameter_list|)
block|{
name|this
operator|.
name|initialBytes
operator|=
name|initialBytes
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createSink ()
specifier|public
name|ByteSink
name|createSink
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|file
init|=
name|createFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|initialBytes
operator|!=
literal|null
condition|)
block|{
name|FileOutputStream
name|out
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
try|try
block|{
name|out
operator|.
name|write
argument_list|(
name|initialBytes
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|Files
operator|.
name|asByteSink
argument_list|(
name|file
argument_list|,
name|FileWriteMode
operator|.
name|APPEND
argument_list|)
return|;
block|}
return|return
name|Files
operator|.
name|asByteSink
argument_list|(
name|file
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getExpected (byte[] bytes)
specifier|public
name|byte
index|[]
name|getExpected
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
if|if
condition|(
name|initialBytes
operator|==
literal|null
condition|)
block|{
return|return
name|checkNotNull
argument_list|(
name|bytes
argument_list|)
return|;
block|}
else|else
block|{
name|byte
index|[]
name|result
init|=
operator|new
name|byte
index|[
name|initialBytes
operator|.
name|length
operator|+
name|bytes
operator|.
name|length
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|initialBytes
argument_list|,
literal|0
argument_list|,
name|result
argument_list|,
literal|0
argument_list|,
name|initialBytes
operator|.
name|length
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|result
argument_list|,
name|initialBytes
operator|.
name|length
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getSinkContents ()
specifier|public
name|byte
index|[]
name|getSinkContents
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|file
init|=
name|getFile
argument_list|()
decl_stmt|;
name|InputStream
name|in
init|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|100
index|]
decl_stmt|;
name|int
name|read
decl_stmt|;
while|while
condition|(
operator|(
name|read
operator|=
name|in
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|read
argument_list|)
expr_stmt|;
block|}
return|return
name|out
operator|.
name|toByteArray
argument_list|()
return|;
block|}
block|}
DECL|class|FileCharSourceFactory
specifier|private
specifier|static
class|class
name|FileCharSourceFactory
extends|extends
name|FileFactory
implements|implements
name|CharSourceFactory
block|{
annotation|@
name|Override
DECL|method|createSource (String string)
specifier|public
name|CharSource
name|createSource
parameter_list|(
name|String
name|string
parameter_list|)
throws|throws
name|IOException
block|{
name|checkNotNull
argument_list|(
name|string
argument_list|)
expr_stmt|;
name|File
name|file
init|=
name|createFile
argument_list|()
decl_stmt|;
name|Writer
name|writer
init|=
operator|new
name|OutputStreamWriter
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
try|try
block|{
name|writer
operator|.
name|write
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|Files
operator|.
name|asCharSource
argument_list|(
name|file
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getExpected (String string)
specifier|public
name|String
name|getExpected
parameter_list|(
name|String
name|string
parameter_list|)
block|{
return|return
name|checkNotNull
argument_list|(
name|string
argument_list|)
return|;
block|}
block|}
DECL|class|FileCharSinkFactory
specifier|private
specifier|static
class|class
name|FileCharSinkFactory
extends|extends
name|FileFactory
implements|implements
name|CharSinkFactory
block|{
DECL|field|initialString
specifier|private
specifier|final
name|String
name|initialString
decl_stmt|;
DECL|method|FileCharSinkFactory (@heckForNull String initialString)
specifier|private
name|FileCharSinkFactory
parameter_list|(
annotation|@
name|CheckForNull
name|String
name|initialString
parameter_list|)
block|{
name|this
operator|.
name|initialString
operator|=
name|initialString
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createSink ()
specifier|public
name|CharSink
name|createSink
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|file
init|=
name|createFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|initialString
operator|!=
literal|null
condition|)
block|{
name|Writer
name|writer
init|=
operator|new
name|OutputStreamWriter
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
try|try
block|{
name|writer
operator|.
name|write
argument_list|(
name|initialString
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|Files
operator|.
name|asCharSink
argument_list|(
name|file
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|,
name|FileWriteMode
operator|.
name|APPEND
argument_list|)
return|;
block|}
return|return
name|Files
operator|.
name|asCharSink
argument_list|(
name|file
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getExpected (String string)
specifier|public
name|String
name|getExpected
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|string
argument_list|)
expr_stmt|;
return|return
name|initialString
operator|==
literal|null
condition|?
name|string
else|:
name|initialString
operator|+
name|string
return|;
block|}
annotation|@
name|Override
DECL|method|getSinkContents ()
specifier|public
name|String
name|getSinkContents
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|file
init|=
name|getFile
argument_list|()
decl_stmt|;
name|Reader
name|reader
init|=
operator|new
name|InputStreamReader
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|CharBuffer
name|buffer
init|=
name|CharBuffer
operator|.
name|allocate
argument_list|(
literal|100
argument_list|)
decl_stmt|;
while|while
condition|(
name|reader
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|Java8Compatibility
operator|.
name|flip
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
name|Java8Compatibility
operator|.
name|clear
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
DECL|class|UrlByteSourceFactory
specifier|private
specifier|static
class|class
name|UrlByteSourceFactory
extends|extends
name|FileByteSourceFactory
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"CheckReturnValue"
argument_list|)
comment|// only using super.createSource to create a file
annotation|@
name|Override
DECL|method|createSource (byte[] bytes)
specifier|public
name|ByteSource
name|createSource
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
throws|throws
name|IOException
block|{
name|super
operator|.
name|createSource
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
return|return
name|Resources
operator|.
name|asByteSource
argument_list|(
name|getFile
argument_list|()
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|class|UrlCharSourceFactory
specifier|private
specifier|static
class|class
name|UrlCharSourceFactory
extends|extends
name|FileCharSourceFactory
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"CheckReturnValue"
argument_list|)
comment|// only using super.createSource to create a file
annotation|@
name|Override
DECL|method|createSource (String string)
specifier|public
name|CharSource
name|createSource
parameter_list|(
name|String
name|string
parameter_list|)
throws|throws
name|IOException
block|{
name|super
operator|.
name|createSource
argument_list|(
name|string
argument_list|)
expr_stmt|;
comment|// just ignore returned CharSource
return|return
name|Resources
operator|.
name|asCharSource
argument_list|(
name|getFile
argument_list|()
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

