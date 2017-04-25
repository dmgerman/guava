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
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verify
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
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
name|collect
operator|.
name|ImmutableMap
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

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
comment|// mock
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
name|mock
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
DECL|method|testGetIfPresent ()
specifier|public
name|void
name|testGetIfPresent
parameter_list|()
throws|throws
name|ExecutionException
block|{
name|when
argument_list|(
name|mock
operator|.
name|getIfPresent
argument_list|(
literal|"key"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|Boolean
operator|.
name|TRUE
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
name|getIfPresent
argument_list|(
literal|"key"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetAllPresent ()
specifier|public
name|void
name|testGetAllPresent
parameter_list|()
throws|throws
name|ExecutionException
block|{
name|when
argument_list|(
name|mock
operator|.
name|getAllPresent
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"key"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"key"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"key"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|,
name|forward
operator|.
name|getAllPresent
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"key"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testInvalidate ()
specifier|public
name|void
name|testInvalidate
parameter_list|()
block|{
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
operator|.
name|invalidate
argument_list|(
literal|"key"
argument_list|)
expr_stmt|;
block|}
DECL|method|testInvalidateAllIterable ()
specifier|public
name|void
name|testInvalidateAllIterable
parameter_list|()
block|{
name|forward
operator|.
name|invalidateAll
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"key"
argument_list|)
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mock
argument_list|)
operator|.
name|invalidateAll
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"key"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testInvalidateAll ()
specifier|public
name|void
name|testInvalidateAll
parameter_list|()
block|{
name|forward
operator|.
name|invalidateAll
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|mock
argument_list|)
operator|.
name|invalidateAll
argument_list|()
expr_stmt|;
block|}
DECL|method|testSize ()
specifier|public
name|void
name|testSize
parameter_list|()
block|{
name|when
argument_list|(
name|mock
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|0L
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|forward
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testStats ()
specifier|public
name|void
name|testStats
parameter_list|()
block|{
name|when
argument_list|(
name|mock
operator|.
name|stats
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|null
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
block|}
DECL|method|testAsMap ()
specifier|public
name|void
name|testAsMap
parameter_list|()
block|{
name|when
argument_list|(
name|mock
operator|.
name|asMap
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|null
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
block|}
DECL|method|testCleanUp ()
specifier|public
name|void
name|testCleanUp
parameter_list|()
block|{
name|forward
operator|.
name|cleanUp
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|mock
argument_list|)
operator|.
name|cleanUp
argument_list|()
expr_stmt|;
block|}
comment|/**    * Make sure that all methods are forwarded.    */
DECL|class|OnlyGet
specifier|private
specifier|static
class|class
name|OnlyGet
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ForwardingCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit
