begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_comment
comment|/**  * An abstract hasher, implementing {@link #putBoolean(boolean)}, {@link #putDouble(double)},  * {@link #putFloat(float)}, {@link #putUnencodedChars(CharSequence)}, and  * {@link #putString(CharSequence, Charset)} as prescribed by {@link Hasher}.  *  * @author Dimitris Andreou  */
end_comment

begin_class
DECL|class|AbstractHasher
specifier|abstract
class|class
name|AbstractHasher
implements|implements
name|Hasher
block|{
DECL|method|putBoolean (boolean b)
annotation|@
name|Override
specifier|public
specifier|final
name|Hasher
name|putBoolean
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
return|return
name|putByte
argument_list|(
name|b
condition|?
operator|(
name|byte
operator|)
literal|1
else|:
operator|(
name|byte
operator|)
literal|0
argument_list|)
return|;
block|}
DECL|method|putDouble (double d)
annotation|@
name|Override
specifier|public
specifier|final
name|Hasher
name|putDouble
parameter_list|(
name|double
name|d
parameter_list|)
block|{
return|return
name|putLong
argument_list|(
name|Double
operator|.
name|doubleToRawLongBits
argument_list|(
name|d
argument_list|)
argument_list|)
return|;
block|}
DECL|method|putFloat (float f)
annotation|@
name|Override
specifier|public
specifier|final
name|Hasher
name|putFloat
parameter_list|(
name|float
name|f
parameter_list|)
block|{
return|return
name|putInt
argument_list|(
name|Float
operator|.
name|floatToRawIntBits
argument_list|(
name|f
argument_list|)
argument_list|)
return|;
block|}
DECL|method|putUnencodedChars (CharSequence charSequence)
annotation|@
name|Override
specifier|public
name|Hasher
name|putUnencodedChars
parameter_list|(
name|CharSequence
name|charSequence
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|len
init|=
name|charSequence
operator|.
name|length
argument_list|()
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
name|putChar
argument_list|(
name|charSequence
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|putString (CharSequence charSequence, Charset charset)
annotation|@
name|Override
specifier|public
name|Hasher
name|putString
parameter_list|(
name|CharSequence
name|charSequence
parameter_list|,
name|Charset
name|charset
parameter_list|)
block|{
return|return
name|putBytes
argument_list|(
name|charSequence
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|(
name|charset
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

