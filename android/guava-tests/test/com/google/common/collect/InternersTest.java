begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
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
name|Function
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
name|Interners
operator|.
name|StrongInterner
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
name|Interners
operator|.
name|WeakInterner
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
name|GcFinalization
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
name|lang
operator|.
name|ref
operator|.
name|WeakReference
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
comment|/**  * Unit test for {@link Interners}.  *  * @author Kevin Bourrillion  */
end_comment

begin_class
DECL|class|InternersTest
specifier|public
class|class
name|InternersTest
extends|extends
name|TestCase
block|{
DECL|method|testStrong_simplistic ()
specifier|public
name|void
name|testStrong_simplistic
parameter_list|()
block|{
name|String
name|canonical
init|=
literal|"a"
decl_stmt|;
name|String
name|not
init|=
operator|new
name|String
argument_list|(
literal|"a"
argument_list|)
decl_stmt|;
name|Interner
argument_list|<
name|String
argument_list|>
name|pool
init|=
name|Interners
operator|.
name|newStrongInterner
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|canonical
argument_list|,
name|pool
operator|.
name|intern
argument_list|(
name|canonical
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|canonical
argument_list|,
name|pool
operator|.
name|intern
argument_list|(
name|not
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testStrong_null ()
specifier|public
name|void
name|testStrong_null
parameter_list|()
block|{
name|Interner
argument_list|<
name|String
argument_list|>
name|pool
init|=
name|Interners
operator|.
name|newStrongInterner
argument_list|()
decl_stmt|;
try|try
block|{
name|pool
operator|.
name|intern
argument_list|(
literal|null
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
DECL|method|testStrong_builder ()
specifier|public
name|void
name|testStrong_builder
parameter_list|()
block|{
name|int
name|concurrencyLevel
init|=
literal|42
decl_stmt|;
name|Interner
argument_list|<
name|Object
argument_list|>
name|interner
init|=
name|Interners
operator|.
name|newBuilder
argument_list|()
operator|.
name|strong
argument_list|()
operator|.
name|concurrencyLevel
argument_list|(
name|concurrencyLevel
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|interner
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|StrongInterner
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testWeak_simplistic ()
specifier|public
name|void
name|testWeak_simplistic
parameter_list|()
block|{
name|String
name|canonical
init|=
literal|"a"
decl_stmt|;
name|String
name|not
init|=
operator|new
name|String
argument_list|(
literal|"a"
argument_list|)
decl_stmt|;
name|Interner
argument_list|<
name|String
argument_list|>
name|pool
init|=
name|Interners
operator|.
name|newWeakInterner
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|canonical
argument_list|,
name|pool
operator|.
name|intern
argument_list|(
name|canonical
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|canonical
argument_list|,
name|pool
operator|.
name|intern
argument_list|(
name|not
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testWeak_null ()
specifier|public
name|void
name|testWeak_null
parameter_list|()
block|{
name|Interner
argument_list|<
name|String
argument_list|>
name|pool
init|=
name|Interners
operator|.
name|newWeakInterner
argument_list|()
decl_stmt|;
try|try
block|{
name|pool
operator|.
name|intern
argument_list|(
literal|null
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
DECL|method|testWeak_builder ()
specifier|public
name|void
name|testWeak_builder
parameter_list|()
block|{
name|int
name|concurrencyLevel
init|=
literal|42
decl_stmt|;
name|Interner
argument_list|<
name|Object
argument_list|>
name|interner
init|=
name|Interners
operator|.
name|newBuilder
argument_list|()
operator|.
name|weak
argument_list|()
operator|.
name|concurrencyLevel
argument_list|(
name|concurrencyLevel
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|interner
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|WeakInterner
operator|.
name|class
argument_list|)
expr_stmt|;
name|WeakInterner
argument_list|<
name|Object
argument_list|>
name|weakInterner
init|=
operator|(
name|WeakInterner
argument_list|<
name|Object
argument_list|>
operator|)
name|interner
decl_stmt|;
name|assertEquals
argument_list|(
name|concurrencyLevel
argument_list|,
name|weakInterner
operator|.
name|map
operator|.
name|concurrencyLevel
argument_list|)
expr_stmt|;
block|}
DECL|method|testWeak_afterGC ()
specifier|public
name|void
name|testWeak_afterGC
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|Integer
name|canonical
init|=
operator|new
name|Integer
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|Integer
name|not
init|=
operator|new
name|Integer
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|Interner
argument_list|<
name|Integer
argument_list|>
name|pool
init|=
name|Interners
operator|.
name|newWeakInterner
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|canonical
argument_list|,
name|pool
operator|.
name|intern
argument_list|(
name|canonical
argument_list|)
argument_list|)
expr_stmt|;
name|WeakReference
argument_list|<
name|Integer
argument_list|>
name|signal
init|=
operator|new
name|WeakReference
argument_list|<
name|Integer
argument_list|>
argument_list|(
name|canonical
argument_list|)
decl_stmt|;
name|canonical
operator|=
literal|null
expr_stmt|;
comment|// Hint to the JIT that canonical is unreachable
name|GcFinalization
operator|.
name|awaitClear
argument_list|(
name|signal
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|not
argument_list|,
name|pool
operator|.
name|intern
argument_list|(
name|not
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testAsFunction_simplistic ()
specifier|public
name|void
name|testAsFunction_simplistic
parameter_list|()
block|{
name|String
name|canonical
init|=
literal|"a"
decl_stmt|;
name|String
name|not
init|=
operator|new
name|String
argument_list|(
literal|"a"
argument_list|)
decl_stmt|;
name|Function
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|internerFunction
init|=
name|Interners
operator|.
name|asFunction
argument_list|(
name|Interners
operator|.
expr|<
name|String
operator|>
name|newStrongInterner
argument_list|()
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|canonical
argument_list|,
name|internerFunction
operator|.
name|apply
argument_list|(
name|canonical
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|canonical
argument_list|,
name|internerFunction
operator|.
name|apply
argument_list|(
name|not
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testNullPointerExceptions ()
specifier|public
name|void
name|testNullPointerExceptions
parameter_list|()
block|{
operator|new
name|NullPointerTester
argument_list|()
operator|.
name|testAllPublicStaticMethods
argument_list|(
name|Interners
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testConcurrencyLevel_Zero ()
specifier|public
name|void
name|testConcurrencyLevel_Zero
parameter_list|()
block|{
name|Interners
operator|.
name|InternerBuilder
name|builder
init|=
name|Interners
operator|.
name|newBuilder
argument_list|()
decl_stmt|;
try|try
block|{
name|builder
operator|.
name|concurrencyLevel
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testConcurrencyLevel_Negative ()
specifier|public
name|void
name|testConcurrencyLevel_Negative
parameter_list|()
block|{
name|Interners
operator|.
name|InternerBuilder
name|builder
init|=
name|Interners
operator|.
name|newBuilder
argument_list|()
decl_stmt|;
try|try
block|{
name|builder
operator|.
name|concurrencyLevel
argument_list|(
operator|-
literal|42
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
block|}
end_class

end_unit

