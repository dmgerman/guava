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
name|Charsets
operator|.
name|UTF_8
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FilterWriter
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
name|OutputStreamWriter
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

begin_comment
comment|/**  * A char sink for testing that has configurable behavior.  *  * @author Colin Decker  */
end_comment

begin_class
DECL|class|TestCharSink
specifier|public
class|class
name|TestCharSink
extends|extends
name|CharSink
implements|implements
name|TestStreamSupplier
block|{
DECL|field|byteSink
specifier|private
specifier|final
name|TestByteSink
name|byteSink
decl_stmt|;
DECL|method|TestCharSink (TestOption... options)
specifier|public
name|TestCharSink
parameter_list|(
name|TestOption
modifier|...
name|options
parameter_list|)
block|{
name|this
operator|.
name|byteSink
operator|=
operator|new
name|TestByteSink
argument_list|(
name|options
argument_list|)
expr_stmt|;
block|}
DECL|method|getString ()
specifier|public
name|String
name|getString
parameter_list|()
block|{
return|return
operator|new
name|String
argument_list|(
name|byteSink
operator|.
name|getBytes
argument_list|()
argument_list|,
name|UTF_8
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|wasStreamOpened ()
specifier|public
name|boolean
name|wasStreamOpened
parameter_list|()
block|{
return|return
name|byteSink
operator|.
name|wasStreamOpened
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|wasStreamClosed ()
specifier|public
name|boolean
name|wasStreamClosed
parameter_list|()
block|{
return|return
name|byteSink
operator|.
name|wasStreamClosed
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|openStream ()
specifier|public
name|Writer
name|openStream
parameter_list|()
throws|throws
name|IOException
block|{
comment|// using TestByteSink's output stream to get option behavior, so flush to it on every write
return|return
operator|new
name|FilterWriter
argument_list|(
operator|new
name|OutputStreamWriter
argument_list|(
name|byteSink
operator|.
name|openStream
argument_list|()
argument_list|,
name|UTF_8
argument_list|)
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|int
name|c
parameter_list|)
throws|throws
name|IOException
block|{
name|super
operator|.
name|write
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|flush
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|char
index|[]
name|cbuf
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
name|super
operator|.
name|write
argument_list|(
name|cbuf
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
name|flush
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|String
name|str
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
name|super
operator|.
name|write
argument_list|(
name|str
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

