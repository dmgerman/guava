begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.cache
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|cache
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|createMock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|expect
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|replay
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|verify
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutionException
import|;
end_import

begin_comment
comment|/**  * Unit test for {@link ForwardingCache}.  *  * @author Charles Fry  */
end_comment

begin_class
DECL|class|ForwardingCacheTest
specifier|public
class|class
name|ForwardingCacheTest
extends|extends
name|TestCase
block|{
DECL|field|forward
specifier|private
name|Cache
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|forward
decl_stmt|;
DECL|field|mock
specifier|private
name|Cache
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|mock
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// createMock
DECL|method|setUp ()
annotation|@
name|Override
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
comment|/*      * Class parameters must be raw, so we can't create a proxy with generic      * type arguments. The created proxy only records calls and returns null, so      * the type is irrelevant at runtime.      */
name|mock
operator|=
name|createMock
argument_list|(
name|Cache
operator|.
name|class
argument_list|)
expr_stmt|;
name|forward
operator|=
operator|new
name|ForwardingCache
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Cache
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|mock
return|;
block|}
block|}
expr_stmt|;
block|}
DECL|method|testGet ()
specifier|public
name|void
name|testGet
parameter_list|()
throws|throws
name|ExecutionException
block|{
name|expect
argument_list|(
name|mock
operator|.
name|get
argument_list|(
literal|"key"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|mock
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|forward
operator|.
name|get
argument_list|(
literal|"key"
argument_list|)
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mock
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetUnchecked ()
specifier|public
name|void
name|testGetUnchecked
parameter_list|()
block|{
name|expect
argument_list|(
name|mock
operator|.
name|getUnchecked
argument_list|(
literal|"key"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|mock
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|forward
operator|.
name|getUnchecked
argument_list|(
literal|"key"
argument_list|)
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mock
argument_list|)
expr_stmt|;
block|}
DECL|method|testApply ()
specifier|public
name|void
name|testApply
parameter_list|()
block|{
name|expect
argument_list|(
name|mock
operator|.
name|apply
argument_list|(
literal|"key"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|mock
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|forward
operator|.
name|apply
argument_list|(
literal|"key"
argument_list|)
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mock
argument_list|)
expr_stmt|;
block|}
DECL|method|testInvalidate ()
specifier|public
name|void
name|testInvalidate
parameter_list|()
block|{
name|mock
operator|.
name|invalidate
argument_list|(
literal|"key"
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|mock
argument_list|)
expr_stmt|;
name|forward
operator|.
name|invalidate
argument_list|(
literal|"key"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mock
argument_list|)
expr_stmt|;
block|}
DECL|method|testInvalidateAll ()
specifier|public
name|void
name|testInvalidateAll
parameter_list|()
block|{
name|mock
operator|.
name|invalidateAll
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|mock
argument_list|)
expr_stmt|;
name|forward
operator|.
name|invalidateAll
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|mock
argument_list|)
expr_stmt|;
block|}
DECL|method|testSize ()
specifier|public
name|void
name|testSize
parameter_list|()
block|{
name|expect
argument_list|(
name|mock
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|mock
argument_list|)
expr_stmt|;
name|forward
operator|.
name|size
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|mock
argument_list|)
expr_stmt|;
block|}
DECL|method|testStats ()
specifier|public
name|void
name|testStats
parameter_list|()
block|{
name|expect
argument_list|(
name|mock
operator|.
name|stats
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|mock
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|forward
operator|.
name|stats
argument_list|()
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mock
argument_list|)
expr_stmt|;
block|}
DECL|method|testActiveEntries ()
specifier|public
name|void
name|testActiveEntries
parameter_list|()
block|{
name|expect
argument_list|(
name|mock
operator|.
name|activeEntries
argument_list|(
literal|10
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|mock
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|forward
operator|.
name|activeEntries
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mock
argument_list|)
expr_stmt|;
block|}
DECL|method|testAsMap ()
specifier|public
name|void
name|testAsMap
parameter_list|()
block|{
name|expect
argument_list|(
name|mock
operator|.
name|asMap
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|mock
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|forward
operator|.
name|asMap
argument_list|()
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mock
argument_list|)
expr_stmt|;
block|}
DECL|method|testCleanUp ()
specifier|public
name|void
name|testCleanUp
parameter_list|()
block|{
name|mock
operator|.
name|cleanUp
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|mock
argument_list|)
expr_stmt|;
name|forward
operator|.
name|cleanUp
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|mock
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

