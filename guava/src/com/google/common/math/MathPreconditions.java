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
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
comment|/**  * A collection of preconditions for math functions.  *   * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|MathPreconditions
specifier|final
class|class
name|MathPreconditions
block|{
DECL|method|checkPositive (String role, int x)
specifier|static
name|int
name|checkPositive
parameter_list|(
name|String
name|role
parameter_list|,
name|int
name|x
parameter_list|)
block|{
if|if
condition|(
name|x
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|role
operator|+
literal|" ("
operator|+
name|x
operator|+
literal|") must be> 0"
argument_list|)
throw|;
block|}
return|return
name|x
return|;
block|}
DECL|method|checkPositive (String role, long x)
specifier|static
name|long
name|checkPositive
parameter_list|(
name|String
name|role
parameter_list|,
name|long
name|x
parameter_list|)
block|{
if|if
condition|(
name|x
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|role
operator|+
literal|" ("
operator|+
name|x
operator|+
literal|") must be> 0"
argument_list|)
throw|;
block|}
return|return
name|x
return|;
block|}
DECL|method|checkPositive (String role, BigInteger x)
specifier|static
name|BigInteger
name|checkPositive
parameter_list|(
name|String
name|role
parameter_list|,
name|BigInteger
name|x
parameter_list|)
block|{
if|if
condition|(
name|x
operator|.
name|signum
argument_list|()
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|role
operator|+
literal|" ("
operator|+
name|x
operator|+
literal|") must be> 0"
argument_list|)
throw|;
block|}
return|return
name|x
return|;
block|}
DECL|method|checkNonNegative (String role, int x)
specifier|static
name|int
name|checkNonNegative
parameter_list|(
name|String
name|role
parameter_list|,
name|int
name|x
parameter_list|)
block|{
if|if
condition|(
name|x
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|role
operator|+
literal|" ("
operator|+
name|x
operator|+
literal|") must be>= 0"
argument_list|)
throw|;
block|}
return|return
name|x
return|;
block|}
DECL|method|checkNonNegative (String role, long x)
specifier|static
name|long
name|checkNonNegative
parameter_list|(
name|String
name|role
parameter_list|,
name|long
name|x
parameter_list|)
block|{
if|if
condition|(
name|x
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|role
operator|+
literal|" ("
operator|+
name|x
operator|+
literal|") must be>= 0"
argument_list|)
throw|;
block|}
return|return
name|x
return|;
block|}
DECL|method|checkNonNegative (String role, BigInteger x)
specifier|static
name|BigInteger
name|checkNonNegative
parameter_list|(
name|String
name|role
parameter_list|,
name|BigInteger
name|x
parameter_list|)
block|{
if|if
condition|(
name|checkNotNull
argument_list|(
name|x
argument_list|)
operator|.
name|signum
argument_list|()
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|role
operator|+
literal|" ("
operator|+
name|x
operator|+
literal|") must be>= 0"
argument_list|)
throw|;
block|}
return|return
name|x
return|;
block|}
DECL|method|checkRoundingUnnecessary (boolean condition)
specifier|static
name|void
name|checkRoundingUnnecessary
parameter_list|(
name|boolean
name|condition
parameter_list|)
block|{
if|if
condition|(
operator|!
name|condition
condition|)
block|{
throw|throw
operator|new
name|ArithmeticException
argument_list|(
literal|"mode was UNNECESSARY, but rounding was necessary"
argument_list|)
throw|;
block|}
block|}
DECL|method|checkInRange (boolean condition)
specifier|static
name|void
name|checkInRange
parameter_list|(
name|boolean
name|condition
parameter_list|)
block|{
if|if
condition|(
operator|!
name|condition
condition|)
block|{
throw|throw
operator|new
name|ArithmeticException
argument_list|(
literal|"not in range"
argument_list|)
throw|;
block|}
block|}
DECL|method|checkNoOverflow (boolean condition)
specifier|static
name|void
name|checkNoOverflow
parameter_list|(
name|boolean
name|condition
parameter_list|)
block|{
if|if
condition|(
operator|!
name|condition
condition|)
block|{
throw|throw
operator|new
name|ArithmeticException
argument_list|(
literal|"overflow"
argument_list|)
throw|;
block|}
block|}
DECL|method|MathPreconditions ()
specifier|private
name|MathPreconditions
parameter_list|()
block|{}
block|}
end_class

end_unit

