begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.math
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|math
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
name|math
operator|.
name|MathTesting
operator|.
name|ALL_BIGINTEGER_CANDIDATES
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|math
operator|.
name|MathTesting
operator|.
name|FINITE_DOUBLE_CANDIDATES
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|math
operator|.
name|MathTesting
operator|.
name|POSITIVE_FINITE_DOUBLE_CANDIDATES
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
name|sun
operator|.
name|misc
operator|.
name|FpUtils
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

begin_comment
comment|/**  * Tests for {@link DoubleUtils}.  *   * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|DoubleUtilsTest
specifier|public
class|class
name|DoubleUtilsTest
extends|extends
name|TestCase
block|{
annotation|@
name|SuppressUnderAndroid
comment|// no FpUtils
DECL|method|testNextDown ()
specifier|public
name|void
name|testNextDown
parameter_list|()
block|{
for|for
control|(
name|double
name|d
range|:
name|FINITE_DOUBLE_CANDIDATES
control|)
block|{
name|assertEquals
argument_list|(
name|FpUtils
operator|.
name|nextDown
argument_list|(
name|d
argument_list|)
argument_list|,
name|DoubleUtils
operator|.
name|nextDown
argument_list|(
name|d
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressUnderAndroid
comment|// TODO(cpovirk): File bug for BigDecimal.doubleValue().
DECL|method|testBigToDouble ()
specifier|public
name|void
name|testBigToDouble
parameter_list|()
block|{
for|for
control|(
name|BigInteger
name|b
range|:
name|ALL_BIGINTEGER_CANDIDATES
control|)
block|{
if|if
condition|(
name|b
operator|.
name|doubleValue
argument_list|()
operator|!=
name|DoubleUtils
operator|.
name|bigToDouble
argument_list|(
name|b
argument_list|)
condition|)
block|{
name|failFormat
argument_list|(
literal|"Converting %s to double: expected doubleValue %s but got bigToDouble %s"
argument_list|,
name|b
argument_list|,
name|b
operator|.
name|doubleValue
argument_list|()
argument_list|,
name|DoubleUtils
operator|.
name|bigToDouble
argument_list|(
name|b
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testEnsureNonNegative ()
specifier|public
name|void
name|testEnsureNonNegative
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|0.0
argument_list|,
name|DoubleUtils
operator|.
name|ensureNonNegative
argument_list|(
literal|0.0
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|double
name|positiveValue
range|:
name|POSITIVE_FINITE_DOUBLE_CANDIDATES
control|)
block|{
name|assertEquals
argument_list|(
name|positiveValue
argument_list|,
name|DoubleUtils
operator|.
name|ensureNonNegative
argument_list|(
name|positiveValue
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0.0
argument_list|,
name|DoubleUtils
operator|.
name|ensureNonNegative
argument_list|(
operator|-
name|positiveValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|Double
operator|.
name|POSITIVE_INFINITY
argument_list|,
name|DoubleUtils
operator|.
name|ensureNonNegative
argument_list|(
name|Double
operator|.
name|POSITIVE_INFINITY
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0.0
argument_list|,
name|DoubleUtils
operator|.
name|ensureNonNegative
argument_list|(
name|Double
operator|.
name|NEGATIVE_INFINITY
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|DoubleUtils
operator|.
name|ensureNonNegative
argument_list|(
name|Double
operator|.
name|NaN
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected IllegalArgumentException from ensureNonNegative(Double.NaN)"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|failFormat (String template, Object... args)
specifier|private
specifier|static
name|void
name|failFormat
parameter_list|(
name|String
name|template
parameter_list|,
name|Object
modifier|...
name|args
parameter_list|)
block|{
name|fail
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|template
argument_list|,
name|args
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

