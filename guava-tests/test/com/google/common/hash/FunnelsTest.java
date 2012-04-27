begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.hash
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|hash
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
name|hash
operator|.
name|AbstractStreamingHashFunction
operator|.
name|AbstractStreamingHasher
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_comment
comment|/**  * Tests for HashExtractors.  *  * @author andreou@google.com (Dimitris Andreou)  */
end_comment

begin_class
DECL|class|FunnelsTest
specifier|public
class|class
name|FunnelsTest
extends|extends
name|TestCase
block|{
DECL|method|testForBytes ()
specifier|public
name|void
name|testForBytes
parameter_list|()
block|{
name|PrimitiveSink
name|bytePrimitiveSink
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|PrimitiveSink
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bytePrimitiveSink
operator|.
name|putBytes
argument_list|(
name|EasyMock
operator|.
name|aryEq
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|4
block|,
literal|3
block|,
literal|2
block|,
literal|1
block|}
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bytePrimitiveSink
argument_list|)
operator|.
name|once
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|bytePrimitiveSink
argument_list|)
expr_stmt|;
name|Funnels
operator|.
name|byteArrayFunnel
argument_list|()
operator|.
name|funnel
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|4
block|,
literal|3
block|,
literal|2
block|,
literal|1
block|}
argument_list|,
name|bytePrimitiveSink
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|bytePrimitiveSink
argument_list|)
expr_stmt|;
block|}
DECL|method|testForBytes_null ()
specifier|public
name|void
name|testForBytes_null
parameter_list|()
block|{
name|assertNullsThrowException
argument_list|(
name|Funnels
operator|.
name|byteArrayFunnel
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testForStrings ()
specifier|public
name|void
name|testForStrings
parameter_list|()
block|{
name|PrimitiveSink
name|bytePrimitiveSink
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|PrimitiveSink
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bytePrimitiveSink
operator|.
name|putString
argument_list|(
literal|"test"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bytePrimitiveSink
argument_list|)
operator|.
name|once
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|bytePrimitiveSink
argument_list|)
expr_stmt|;
name|Funnels
operator|.
name|stringFunnel
argument_list|()
operator|.
name|funnel
argument_list|(
literal|"test"
argument_list|,
name|bytePrimitiveSink
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|bytePrimitiveSink
argument_list|)
expr_stmt|;
block|}
DECL|method|testForStrings_null ()
specifier|public
name|void
name|testForStrings_null
parameter_list|()
block|{
name|assertNullsThrowException
argument_list|(
name|Funnels
operator|.
name|stringFunnel
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertNullsThrowException (Funnel<?> funnel)
specifier|private
specifier|static
name|void
name|assertNullsThrowException
parameter_list|(
name|Funnel
argument_list|<
name|?
argument_list|>
name|funnel
parameter_list|)
block|{
name|PrimitiveSink
name|bytePrimitiveSink
init|=
operator|new
name|AbstractStreamingHasher
argument_list|(
literal|4
argument_list|,
literal|4
argument_list|)
block|{
annotation|@
name|Override
name|HashCode
name|makeHash
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|process
parameter_list|(
name|ByteBuffer
name|bb
parameter_list|)
block|{
while|while
condition|(
name|bb
operator|.
name|hasRemaining
argument_list|()
condition|)
block|{
name|bb
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
try|try
block|{
name|funnel
operator|.
name|funnel
argument_list|(
literal|null
argument_list|,
name|bytePrimitiveSink
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|ok
parameter_list|)
block|{}
block|}
block|}
end_class

end_unit

