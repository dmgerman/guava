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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|Beta
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_comment
comment|/**  * Static factories for creating {@link HashCode} instances; most users should never have to use  * this. All returned instances are {@link Serializable}.  *  * @author Dimitris Andreou  * @since 12.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|HashCodes
specifier|public
specifier|final
class|class
name|HashCodes
block|{
DECL|method|HashCodes ()
specifier|private
name|HashCodes
parameter_list|()
block|{}
comment|/**    * Creates a 32-bit {@code HashCode}, of which the bytes will form the passed int, interpreted    * in little endian order.    *    * @deprecated Use {@link HashCode#fromInt} instead. This method is scheduled to be removed in    *     Guava 16.0.    */
annotation|@
name|Deprecated
DECL|method|fromInt (int hash)
specifier|public
specifier|static
name|HashCode
name|fromInt
parameter_list|(
name|int
name|hash
parameter_list|)
block|{
return|return
name|HashCode
operator|.
name|fromInt
argument_list|(
name|hash
argument_list|)
return|;
block|}
comment|/**    * Creates a 64-bit {@code HashCode}, of which the bytes will form the passed long, interpreted    * in little endian order.    *    * @deprecated Use {@link HashCode#fromLong} instead. This method is scheduled to be removed in    *     Guava 16.0.    */
annotation|@
name|Deprecated
DECL|method|fromLong (long hash)
specifier|public
specifier|static
name|HashCode
name|fromLong
parameter_list|(
name|long
name|hash
parameter_list|)
block|{
return|return
name|HashCode
operator|.
name|fromLong
argument_list|(
name|hash
argument_list|)
return|;
block|}
comment|/**    * Creates a {@code HashCode} from a byte array. The array is defensively copied to preserve    * the immutability contract of {@code HashCode}. The array cannot be empty.    *    * @deprecated Use {@link HashCode#fromBytes} instead. This method is scheduled to be removed in    *     Guava 16.0.    */
annotation|@
name|Deprecated
DECL|method|fromBytes (byte[] bytes)
specifier|public
specifier|static
name|HashCode
name|fromBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
return|return
name|HashCode
operator|.
name|fromBytes
argument_list|(
name|bytes
argument_list|)
return|;
block|}
block|}
end_class

end_unit

