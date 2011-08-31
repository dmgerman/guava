begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|testing
package|;
end_package

begin_comment
comment|/**  * An unhashable object to be used in testing as values in our collections.  *  *<p>This class is GWT compatible.  *  * @author Regina O'Dell  */
end_comment

begin_class
DECL|class|UnhashableObject
specifier|public
class|class
name|UnhashableObject
implements|implements
name|Comparable
argument_list|<
name|UnhashableObject
argument_list|>
block|{
DECL|field|value
specifier|private
specifier|final
name|int
name|value
decl_stmt|;
DECL|method|UnhashableObject (int value)
specifier|public
name|UnhashableObject
parameter_list|(
name|int
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
DECL|method|equals (Object object)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|UnhashableObject
condition|)
block|{
name|UnhashableObject
name|that
init|=
operator|(
name|UnhashableObject
operator|)
name|object
decl_stmt|;
return|return
name|this
operator|.
name|value
operator|==
name|that
operator|.
name|value
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|// needed because otherwise Object.toString() calls hashCode()
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"DontHashMe"
operator|+
name|value
return|;
block|}
annotation|@
name|Override
DECL|method|compareTo (UnhashableObject o)
specifier|public
name|int
name|compareTo
parameter_list|(
name|UnhashableObject
name|o
parameter_list|)
block|{
return|return
operator|(
name|this
operator|.
name|value
operator|<
name|o
operator|.
name|value
operator|)
condition|?
operator|-
literal|1
else|:
operator|(
name|this
operator|.
name|value
operator|>
name|o
operator|.
name|value
operator|)
condition|?
literal|1
else|:
literal|0
return|;
block|}
block|}
end_class

end_unit

