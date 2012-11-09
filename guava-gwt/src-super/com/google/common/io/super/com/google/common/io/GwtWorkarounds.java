begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
comment|/**  * Provides simple GWT-compatible substitutes for {@code InputStream}, {@code OutputStream},  * {@code Reader}, and {@code Writer} so that {@code BaseEncoding} can use streaming implementations  * while remaining GWT-compatible.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|GwtWorkarounds
specifier|final
class|class
name|GwtWorkarounds
block|{
DECL|method|GwtWorkarounds ()
specifier|private
name|GwtWorkarounds
parameter_list|()
block|{}
comment|/**    * A GWT-compatible substitute for a {@code Reader}.    */
DECL|interface|CharInput
interface|interface
name|CharInput
block|{
DECL|method|read ()
name|int
name|read
parameter_list|()
throws|throws
name|IOException
function_decl|;
DECL|method|close ()
name|void
name|close
parameter_list|()
throws|throws
name|IOException
function_decl|;
block|}
comment|/**    * Views a {@code CharSequence} as a {@code CharInput}.    */
DECL|method|asCharInput (final CharSequence chars)
specifier|static
name|CharInput
name|asCharInput
parameter_list|(
specifier|final
name|CharSequence
name|chars
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|chars
argument_list|)
expr_stmt|;
return|return
operator|new
name|CharInput
argument_list|()
block|{
name|int
name|index
init|=
literal|0
decl_stmt|;
annotation|@
name|Override
specifier|public
name|int
name|read
parameter_list|()
block|{
if|if
condition|(
name|index
operator|<
name|chars
operator|.
name|length
argument_list|()
condition|)
block|{
return|return
name|chars
operator|.
name|charAt
argument_list|(
name|index
operator|++
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|-
literal|1
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
block|{
name|index
operator|=
name|chars
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
comment|/**    * A GWT-compatible substitute for an {@code InputStream}.    */
DECL|interface|ByteInput
interface|interface
name|ByteInput
block|{
DECL|method|read ()
name|int
name|read
parameter_list|()
throws|throws
name|IOException
function_decl|;
DECL|method|close ()
name|void
name|close
parameter_list|()
throws|throws
name|IOException
function_decl|;
block|}
comment|/**    * A GWT-compatible substitute for an {@code OutputStream}.    */
DECL|interface|ByteOutput
interface|interface
name|ByteOutput
block|{
DECL|method|write (byte b)
name|void
name|write
parameter_list|(
name|byte
name|b
parameter_list|)
throws|throws
name|IOException
function_decl|;
DECL|method|flush ()
name|void
name|flush
parameter_list|()
throws|throws
name|IOException
function_decl|;
DECL|method|close ()
name|void
name|close
parameter_list|()
throws|throws
name|IOException
function_decl|;
block|}
comment|/**    * A GWT-compatible substitute for a {@code Writer}.    */
DECL|interface|CharOutput
interface|interface
name|CharOutput
block|{
DECL|method|write (char c)
name|void
name|write
parameter_list|(
name|char
name|c
parameter_list|)
throws|throws
name|IOException
function_decl|;
DECL|method|flush ()
name|void
name|flush
parameter_list|()
throws|throws
name|IOException
function_decl|;
DECL|method|close ()
name|void
name|close
parameter_list|()
throws|throws
name|IOException
function_decl|;
block|}
comment|/**    * Returns a {@code CharOutput} whose {@code toString()} method can be used    * to get the combined output.    */
DECL|method|stringBuilderOutput (int initialSize)
specifier|static
name|CharOutput
name|stringBuilderOutput
parameter_list|(
name|int
name|initialSize
parameter_list|)
block|{
specifier|final
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|(
name|initialSize
argument_list|)
decl_stmt|;
return|return
operator|new
name|CharOutput
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|char
name|c
parameter_list|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|flush
parameter_list|()
block|{}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
block|{}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

