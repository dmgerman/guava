begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common
package|package
name|com
operator|.
name|google
operator|.
name|common
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
name|reflect
operator|.
name|ClassPath
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
name|reflect
operator|.
name|ClassPath
operator|.
name|ClassInfo
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|junit
operator|.
name|client
operator|.
name|GWTTestCase
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|junit
operator|.
name|tools
operator|.
name|GWTTestSuite
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|Test
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
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * Runs all _gwt tests. Grouping them into a suite is much faster than running each as a one-test  * "suite," as the per-suite setup is expensive.  */
end_comment

begin_class
DECL|class|GwtTestSuite
specifier|public
class|class
name|GwtTestSuite
extends|extends
name|TestCase
block|{
DECL|method|suite ()
specifier|public
specifier|static
name|Test
name|suite
parameter_list|()
throws|throws
name|IOException
block|{
name|GWTTestSuite
name|suite
init|=
operator|new
name|GWTTestSuite
argument_list|()
decl_stmt|;
for|for
control|(
name|ClassInfo
name|info
range|:
name|ClassPath
operator|.
name|from
argument_list|(
name|GwtTestSuite
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
operator|.
name|getTopLevelClasses
argument_list|()
control|)
block|{
if|if
condition|(
name|info
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"_gwt"
argument_list|)
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|info
operator|.
name|load
argument_list|()
decl_stmt|;
comment|// TODO(cpovirk): why does asSubclass() throw? Is it something about ClassLoaders?
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Class
argument_list|<
name|?
extends|extends
name|GWTTestCase
argument_list|>
name|cast
init|=
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|GWTTestCase
argument_list|>
operator|)
name|clazz
decl_stmt|;
name|suite
operator|.
name|addTestSuite
argument_list|(
name|cast
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|suite
return|;
block|}
block|}
end_class

end_unit

