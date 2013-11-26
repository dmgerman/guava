begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.net
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|net
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
name|GwtCompatible
import|;
end_import

begin_comment
comment|/**  * Specifies the type of a top-level domain definition.  */
end_comment

begin_enum
annotation|@
name|GwtCompatible
DECL|enum|TldType
enum|enum
name|TldType
block|{
comment|/** private definition of a top-level domain */
DECL|enumConstant|PRIVATE
name|PRIVATE
argument_list|(
literal|':'
argument_list|,
literal|','
argument_list|)
block|,
comment|/** ICANN definition of a top-level domain */
DECL|enumConstant|ICANN
name|ICANN
argument_list|(
literal|'!'
argument_list|,
literal|'?'
argument_list|)
block|;
comment|/** The character used for an inner node in the trie encoding */
DECL|field|innerNodeCode
specifier|private
specifier|final
name|char
name|innerNodeCode
decl_stmt|;
comment|/** The character used for a leaf node in the trie encoding */
DECL|field|leafNodeCode
specifier|private
specifier|final
name|char
name|leafNodeCode
decl_stmt|;
DECL|method|TldType (char innerNodeCode, char leafNodeCode)
specifier|private
name|TldType
parameter_list|(
name|char
name|innerNodeCode
parameter_list|,
name|char
name|leafNodeCode
parameter_list|)
block|{
name|this
operator|.
name|innerNodeCode
operator|=
name|innerNodeCode
expr_stmt|;
name|this
operator|.
name|leafNodeCode
operator|=
name|leafNodeCode
expr_stmt|;
block|}
DECL|method|getLeafNodeCode ()
specifier|public
name|char
name|getLeafNodeCode
parameter_list|()
block|{
return|return
name|leafNodeCode
return|;
block|}
DECL|method|getInnerNodeCode ()
specifier|public
name|char
name|getInnerNodeCode
parameter_list|()
block|{
return|return
name|innerNodeCode
return|;
block|}
comment|/** Returns a TldType of the right type according to the given code */
DECL|method|fromCode (char code)
specifier|public
specifier|static
name|TldType
name|fromCode
parameter_list|(
name|char
name|code
parameter_list|)
block|{
for|for
control|(
name|TldType
name|value
range|:
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|value
operator|.
name|getInnerNodeCode
argument_list|()
operator|==
name|code
operator|||
name|value
operator|.
name|getLeafNodeCode
argument_list|()
operator|==
name|code
condition|)
block|{
return|return
name|value
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No enum corresponding to given code: "
operator|+
name|code
argument_list|)
throw|;
block|}
DECL|method|fromIsPrivate (boolean isPrivate)
specifier|public
specifier|static
name|TldType
name|fromIsPrivate
parameter_list|(
name|boolean
name|isPrivate
parameter_list|)
block|{
return|return
name|isPrivate
condition|?
name|PRIVATE
else|:
name|ICANN
return|;
block|}
block|}
end_enum

end_unit

