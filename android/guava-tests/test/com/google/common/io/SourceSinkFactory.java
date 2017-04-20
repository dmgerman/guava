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

begin_comment
comment|/**  * A test factory for byte or char sources or sinks. In addition to creating sources or sinks, the  * factory specifies what content should be expected to be read from a source or contained in a sink  * given the content data that was used to create the source or that was written to the sink.  *  *<p>A single {@code SourceSinkFactory} implementation generally corresponds to one specific way of  * creating a source or sink, such as {@link Files#asByteSource(File)}. Implementations of  * {@code SourceSinkFactory} for common.io are found in {@link SourceSinkFactories}.  *  * @param<S> the source or sink type  * @param<T> the data type (byte[] or String)  * @author Colin Decker  */
end_comment

begin_interface
DECL|interface|SourceSinkFactory
specifier|public
interface|interface
name|SourceSinkFactory
parameter_list|<
name|S
parameter_list|,
name|T
parameter_list|>
block|{
comment|/**    * Returns the data to expect the source or sink to contain given the data that was used to create    * the source or written to the sink. Typically, this will just return the input directly, but in    * some cases it may alter the input. For example, if the factory returns a sliced view of a    * source created with some given bytes, this method would return a subsequence of the given    * (byte[]) data.    */
DECL|method|getExpected (T data)
name|T
name|getExpected
parameter_list|(
name|T
name|data
parameter_list|)
function_decl|;
comment|/**    * Cleans up anything created when creating the source or sink.    */
DECL|method|tearDown ()
specifier|public
specifier|abstract
name|void
name|tearDown
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**    * Factory for byte or char sources.    */
DECL|interface|SourceFactory
specifier|public
interface|interface
name|SourceFactory
parameter_list|<
name|S
parameter_list|,
name|T
parameter_list|>
extends|extends
name|SourceSinkFactory
argument_list|<
name|S
argument_list|,
name|T
argument_list|>
block|{
comment|/**      * Creates a new source containing some or all of the given data.      */
DECL|method|createSource (T data)
name|S
name|createSource
parameter_list|(
name|T
name|data
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
comment|/**    * Factory for byte or char sinks.    */
DECL|interface|SinkFactory
specifier|public
interface|interface
name|SinkFactory
parameter_list|<
name|S
parameter_list|,
name|T
parameter_list|>
extends|extends
name|SourceSinkFactory
argument_list|<
name|S
argument_list|,
name|T
argument_list|>
block|{
comment|/**      * Creates a new sink.      */
DECL|method|createSink ()
name|S
name|createSink
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Gets the current content of the created sink.      */
DECL|method|getSinkContents ()
name|T
name|getSinkContents
parameter_list|()
throws|throws
name|IOException
function_decl|;
block|}
comment|/**    * Factory for {@link ByteSource} instances.    */
DECL|interface|ByteSourceFactory
specifier|public
interface|interface
name|ByteSourceFactory
extends|extends
name|SourceFactory
argument_list|<
name|ByteSource
argument_list|,
name|byte
index|[]
argument_list|>
block|{   }
comment|/**    * Factory for {@link ByteSink} instances.    */
DECL|interface|ByteSinkFactory
specifier|public
interface|interface
name|ByteSinkFactory
extends|extends
name|SinkFactory
argument_list|<
name|ByteSink
argument_list|,
name|byte
index|[]
argument_list|>
block|{   }
comment|/**    * Factory for {@link CharSource} instances.    */
DECL|interface|CharSourceFactory
specifier|public
interface|interface
name|CharSourceFactory
extends|extends
name|SourceFactory
argument_list|<
name|CharSource
argument_list|,
name|String
argument_list|>
block|{   }
comment|/**    * Factory for {@link CharSink} instances.    */
DECL|interface|CharSinkFactory
specifier|public
interface|interface
name|CharSinkFactory
extends|extends
name|SinkFactory
argument_list|<
name|CharSink
argument_list|,
name|String
argument_list|>
block|{   }
block|}
end_interface

end_unit

