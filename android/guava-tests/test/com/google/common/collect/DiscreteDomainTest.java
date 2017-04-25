begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|testing
operator|.
name|SerializableTester
operator|.
name|reserializeAndAssert
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
name|GwtIncompatible
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
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
comment|/**  * Tests for {@link DiscreteDomain}.  *  * @author Chris Povirk  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
comment|// SerializableTester
DECL|class|DiscreteDomainTest
specifier|public
class|class
name|DiscreteDomainTest
extends|extends
name|TestCase
block|{
DECL|method|testSerialization ()
specifier|public
name|void
name|testSerialization
parameter_list|()
block|{
name|reserializeAndAssert
argument_list|(
name|DiscreteDomain
operator|.
name|integers
argument_list|()
argument_list|)
expr_stmt|;
name|reserializeAndAssert
argument_list|(
name|DiscreteDomain
operator|.
name|longs
argument_list|()
argument_list|)
expr_stmt|;
name|reserializeAndAssert
argument_list|(
name|DiscreteDomain
operator|.
name|bigIntegers
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testIntegersOffset ()
specifier|public
name|void
name|testIntegersOffset
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|DiscreteDomain
operator|.
name|integers
argument_list|()
operator|.
name|offset
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
name|DiscreteDomain
operator|.
name|integers
argument_list|()
operator|.
name|offset
argument_list|(
name|Integer
operator|.
name|MIN_VALUE
argument_list|,
operator|(
literal|1L
operator|<<
literal|32
operator|)
operator|-
literal|1
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testIntegersOffsetExceptions ()
specifier|public
name|void
name|testIntegersOffsetExceptions
parameter_list|()
block|{
try|try
block|{
name|DiscreteDomain
operator|.
name|integers
argument_list|()
operator|.
name|offset
argument_list|(
literal|0
argument_list|,
operator|-
literal|1
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
try|try
block|{
name|DiscreteDomain
operator|.
name|integers
argument_list|()
operator|.
name|offset
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
literal|1
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
DECL|method|testLongsOffset ()
specifier|public
name|void
name|testLongsOffset
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|DiscreteDomain
operator|.
name|longs
argument_list|()
operator|.
name|offset
argument_list|(
literal|0L
argument_list|,
literal|1
argument_list|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|,
name|DiscreteDomain
operator|.
name|longs
argument_list|()
operator|.
name|offset
argument_list|(
literal|0L
argument_list|,
name|Long
operator|.
name|MAX_VALUE
argument_list|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testLongsOffsetExceptions ()
specifier|public
name|void
name|testLongsOffsetExceptions
parameter_list|()
block|{
try|try
block|{
name|DiscreteDomain
operator|.
name|longs
argument_list|()
operator|.
name|offset
argument_list|(
literal|0L
argument_list|,
operator|-
literal|1
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
try|try
block|{
name|DiscreteDomain
operator|.
name|longs
argument_list|()
operator|.
name|offset
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|,
literal|1
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
DECL|method|testBigIntegersOffset ()
specifier|public
name|void
name|testBigIntegersOffset
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|BigInteger
operator|.
name|ONE
argument_list|,
name|DiscreteDomain
operator|.
name|bigIntegers
argument_list|()
operator|.
name|offset
argument_list|(
name|BigInteger
operator|.
name|ZERO
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|)
argument_list|,
name|DiscreteDomain
operator|.
name|bigIntegers
argument_list|()
operator|.
name|offset
argument_list|(
name|BigInteger
operator|.
name|ZERO
argument_list|,
name|Long
operator|.
name|MAX_VALUE
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testBigIntegersOffsetExceptions ()
specifier|public
name|void
name|testBigIntegersOffsetExceptions
parameter_list|()
block|{
try|try
block|{
name|DiscreteDomain
operator|.
name|bigIntegers
argument_list|()
operator|.
name|offset
argument_list|(
name|BigInteger
operator|.
name|ZERO
argument_list|,
operator|-
literal|1
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
