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
name|checkArgument
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
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
name|base
operator|.
name|Preconditions
operator|.
name|checkState
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

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|MessageDigest
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_comment
comment|/**  * {@link HashFunction} adapter for {@link MessageDigest} instances.  *  * @author Kevin Bourrillion  * @author Dimitris Andreou  */
end_comment

begin_class
DECL|class|MessageDigestHashFunction
specifier|final
class|class
name|MessageDigestHashFunction
extends|extends
name|AbstractStreamingHashFunction
implements|implements
name|Serializable
block|{
DECL|field|prototype
specifier|private
specifier|final
name|MessageDigest
name|prototype
decl_stmt|;
DECL|field|bytes
specifier|private
specifier|final
name|int
name|bytes
decl_stmt|;
DECL|field|supportsClone
specifier|private
specifier|final
name|boolean
name|supportsClone
decl_stmt|;
DECL|field|toString
specifier|private
specifier|final
name|String
name|toString
decl_stmt|;
DECL|method|MessageDigestHashFunction (String algorithmName, String toString)
name|MessageDigestHashFunction
parameter_list|(
name|String
name|algorithmName
parameter_list|,
name|String
name|toString
parameter_list|)
block|{
name|this
operator|.
name|prototype
operator|=
name|getMessageDigest
argument_list|(
name|algorithmName
argument_list|)
expr_stmt|;
name|this
operator|.
name|bytes
operator|=
name|prototype
operator|.
name|getDigestLength
argument_list|()
expr_stmt|;
name|this
operator|.
name|toString
operator|=
name|checkNotNull
argument_list|(
name|toString
argument_list|)
expr_stmt|;
name|this
operator|.
name|supportsClone
operator|=
name|supportsClone
argument_list|()
expr_stmt|;
block|}
DECL|method|MessageDigestHashFunction (String algorithmName, int bytes, String toString)
name|MessageDigestHashFunction
parameter_list|(
name|String
name|algorithmName
parameter_list|,
name|int
name|bytes
parameter_list|,
name|String
name|toString
parameter_list|)
block|{
name|this
operator|.
name|toString
operator|=
name|checkNotNull
argument_list|(
name|toString
argument_list|)
expr_stmt|;
name|this
operator|.
name|prototype
operator|=
name|getMessageDigest
argument_list|(
name|algorithmName
argument_list|)
expr_stmt|;
name|int
name|maxLength
init|=
name|prototype
operator|.
name|getDigestLength
argument_list|()
decl_stmt|;
name|checkArgument
argument_list|(
name|bytes
operator|>=
literal|4
operator|&&
name|bytes
operator|<=
name|maxLength
argument_list|,
literal|"bytes (%s) must be>= 4 and< %s"
argument_list|,
name|bytes
argument_list|,
name|maxLength
argument_list|)
expr_stmt|;
name|this
operator|.
name|bytes
operator|=
name|bytes
expr_stmt|;
name|this
operator|.
name|supportsClone
operator|=
name|supportsClone
argument_list|()
expr_stmt|;
block|}
DECL|method|supportsClone ()
specifier|private
name|boolean
name|supportsClone
parameter_list|()
block|{
try|try
block|{
name|prototype
operator|.
name|clone
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|bits ()
specifier|public
name|int
name|bits
parameter_list|()
block|{
return|return
name|bytes
operator|*
name|Byte
operator|.
name|SIZE
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|toString
return|;
block|}
DECL|method|getMessageDigest (String algorithmName)
specifier|private
specifier|static
name|MessageDigest
name|getMessageDigest
parameter_list|(
name|String
name|algorithmName
parameter_list|)
block|{
try|try
block|{
return|return
name|MessageDigest
operator|.
name|getInstance
argument_list|(
name|algorithmName
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|newHasher ()
specifier|public
name|Hasher
name|newHasher
parameter_list|()
block|{
if|if
condition|(
name|supportsClone
condition|)
block|{
try|try
block|{
return|return
operator|new
name|MessageDigestHasher
argument_list|(
operator|(
name|MessageDigest
operator|)
name|prototype
operator|.
name|clone
argument_list|()
argument_list|,
name|bytes
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
comment|// falls through
block|}
block|}
return|return
operator|new
name|MessageDigestHasher
argument_list|(
name|getMessageDigest
argument_list|(
name|prototype
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
argument_list|,
name|bytes
argument_list|)
return|;
block|}
DECL|class|SerializedForm
specifier|private
specifier|static
specifier|final
class|class
name|SerializedForm
implements|implements
name|Serializable
block|{
DECL|field|algorithmName
specifier|private
specifier|final
name|String
name|algorithmName
decl_stmt|;
DECL|field|bytes
specifier|private
specifier|final
name|int
name|bytes
decl_stmt|;
DECL|field|toString
specifier|private
specifier|final
name|String
name|toString
decl_stmt|;
DECL|method|SerializedForm (String algorithmName, int bytes, String toString)
specifier|private
name|SerializedForm
parameter_list|(
name|String
name|algorithmName
parameter_list|,
name|int
name|bytes
parameter_list|,
name|String
name|toString
parameter_list|)
block|{
name|this
operator|.
name|algorithmName
operator|=
name|algorithmName
expr_stmt|;
name|this
operator|.
name|bytes
operator|=
name|bytes
expr_stmt|;
name|this
operator|.
name|toString
operator|=
name|toString
expr_stmt|;
block|}
DECL|method|readResolve ()
specifier|private
name|Object
name|readResolve
parameter_list|()
block|{
return|return
operator|new
name|MessageDigestHashFunction
argument_list|(
name|algorithmName
argument_list|,
name|bytes
argument_list|,
name|toString
argument_list|)
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
DECL|method|writeReplace ()
name|Object
name|writeReplace
parameter_list|()
block|{
return|return
operator|new
name|SerializedForm
argument_list|(
name|prototype
operator|.
name|getAlgorithm
argument_list|()
argument_list|,
name|bytes
argument_list|,
name|toString
argument_list|)
return|;
block|}
comment|/**    * Hasher that updates a message digest.    */
DECL|class|MessageDigestHasher
specifier|private
specifier|static
specifier|final
class|class
name|MessageDigestHasher
extends|extends
name|AbstractByteHasher
block|{
DECL|field|digest
specifier|private
specifier|final
name|MessageDigest
name|digest
decl_stmt|;
DECL|field|bytes
specifier|private
specifier|final
name|int
name|bytes
decl_stmt|;
DECL|field|done
specifier|private
name|boolean
name|done
decl_stmt|;
DECL|method|MessageDigestHasher (MessageDigest digest, int bytes)
specifier|private
name|MessageDigestHasher
parameter_list|(
name|MessageDigest
name|digest
parameter_list|,
name|int
name|bytes
parameter_list|)
block|{
name|this
operator|.
name|digest
operator|=
name|digest
expr_stmt|;
name|this
operator|.
name|bytes
operator|=
name|bytes
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|update (byte b)
specifier|protected
name|void
name|update
parameter_list|(
name|byte
name|b
parameter_list|)
block|{
name|checkNotDone
argument_list|()
expr_stmt|;
name|digest
operator|.
name|update
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|update (byte[] b)
specifier|protected
name|void
name|update
parameter_list|(
name|byte
index|[]
name|b
parameter_list|)
block|{
name|checkNotDone
argument_list|()
expr_stmt|;
name|digest
operator|.
name|update
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|update (byte[] b, int off, int len)
specifier|protected
name|void
name|update
parameter_list|(
name|byte
index|[]
name|b
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
block|{
name|checkNotDone
argument_list|()
expr_stmt|;
name|digest
operator|.
name|update
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
DECL|method|checkNotDone ()
specifier|private
name|void
name|checkNotDone
parameter_list|()
block|{
name|checkState
argument_list|(
operator|!
name|done
argument_list|,
literal|"Cannot re-use a Hasher after calling hash() on it"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|hash ()
specifier|public
name|HashCode
name|hash
parameter_list|()
block|{
name|checkNotDone
argument_list|()
expr_stmt|;
name|done
operator|=
literal|true
expr_stmt|;
return|return
operator|(
name|bytes
operator|==
name|digest
operator|.
name|getDigestLength
argument_list|()
operator|)
condition|?
name|HashCode
operator|.
name|fromBytesNoCopy
argument_list|(
name|digest
operator|.
name|digest
argument_list|()
argument_list|)
else|:
name|HashCode
operator|.
name|fromBytesNoCopy
argument_list|(
name|Arrays
operator|.
name|copyOf
argument_list|(
name|digest
operator|.
name|digest
argument_list|()
argument_list|,
name|bytes
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

