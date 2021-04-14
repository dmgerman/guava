begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.escape
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|escape
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
name|GwtCompatible
import|;
end_import

begin_comment
comment|/**  * Methods factored out so that they can be emulated differently in GWT.  *  * @author Jesse Wilson  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|Platform
specifier|final
class|class
name|Platform
block|{
DECL|method|Platform ()
specifier|private
name|Platform
parameter_list|()
block|{}
comment|/** Returns a thread-local 1024-char array. */
DECL|method|charBufferFromThreadLocal ()
specifier|static
name|char
index|[]
name|charBufferFromThreadLocal
parameter_list|()
block|{
return|return
name|DEST_TL
operator|.
name|get
argument_list|()
return|;
block|}
comment|/**    * A thread-local destination buffer to keep us from creating new buffers. The starting size is    * 1024 characters. If we grow past this we don't put it back in the threadlocal, we just keep    * going and grow as needed.    */
DECL|field|DEST_TL
specifier|private
specifier|static
specifier|final
name|ThreadLocal
argument_list|<
name|char
index|[]
argument_list|>
name|DEST_TL
init|=
operator|new
name|ThreadLocal
argument_list|<
name|char
index|[]
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|char
index|[]
name|initialValue
parameter_list|()
block|{
return|return
operator|new
name|char
index|[
literal|1024
index|]
return|;
block|}
block|}
decl_stmt|;
block|}
end_class

end_unit

