begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Minimal GWT emulation of {@code com.google.common.collect.testing.Platform}.  *  *<p><strong>This .java file should never be consumed by javac.</strong>  *  * @author Hayward Chan  */
end_comment

begin_class
DECL|class|Platform
specifier|final
class|class
name|Platform
block|{
DECL|method|checkIsInstance (Class<?> clazz, Object obj)
specifier|static
name|boolean
name|checkIsInstance
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|,
name|Object
name|obj
parameter_list|)
block|{
comment|/*      * In GWT, we can't tell whether obj is an instance of clazz because GWT      * doesn't support reflections.  For testing purposes, we give up this      * particular assertion (so that we can keep the rest).      */
return|return
literal|true
return|;
block|}
comment|// Class.cast is not supported in GWT.
DECL|method|checkCast (Class<?> clazz, Object obj)
specifier|static
name|void
name|checkCast
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|,
name|Object
name|obj
parameter_list|)
block|{   }
DECL|method|clone (T[] array)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|clone
parameter_list|(
name|T
index|[]
name|array
parameter_list|)
block|{
return|return
operator|(
name|T
index|[]
operator|)
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|array
argument_list|,
literal|0
argument_list|,
name|array
operator|.
name|length
argument_list|)
return|;
block|}
comment|// TODO: Consolidate different copies in one single place.
DECL|method|format (String template, Object... args)
specifier|static
name|String
name|format
parameter_list|(
name|String
name|template
parameter_list|,
name|Object
modifier|...
name|args
parameter_list|)
block|{
comment|// start substituting the arguments into the '%s' placeholders
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|(
name|template
operator|.
name|length
argument_list|()
operator|+
literal|16
operator|*
name|args
operator|.
name|length
argument_list|)
decl_stmt|;
name|int
name|templateStart
init|=
literal|0
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|i
operator|<
name|args
operator|.
name|length
condition|)
block|{
name|int
name|placeholderStart
init|=
name|template
operator|.
name|indexOf
argument_list|(
literal|"%s"
argument_list|,
name|templateStart
argument_list|)
decl_stmt|;
if|if
condition|(
name|placeholderStart
operator|==
operator|-
literal|1
condition|)
block|{
break|break;
block|}
name|builder
operator|.
name|append
argument_list|(
name|template
operator|.
name|substring
argument_list|(
name|templateStart
argument_list|,
name|placeholderStart
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|args
index|[
name|i
operator|++
index|]
argument_list|)
expr_stmt|;
name|templateStart
operator|=
name|placeholderStart
operator|+
literal|2
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
name|template
operator|.
name|substring
argument_list|(
name|templateStart
argument_list|)
argument_list|)
expr_stmt|;
comment|// if we run out of placeholders, append the extra args in square braces
if|if
condition|(
name|i
operator|<
name|args
operator|.
name|length
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|" ["
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|args
index|[
name|i
operator|++
index|]
argument_list|)
expr_stmt|;
while|while
condition|(
name|i
operator|<
name|args
operator|.
name|length
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|args
index|[
name|i
operator|++
index|]
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|Platform ()
specifier|private
name|Platform
parameter_list|()
block|{}
block|}
end_class

end_unit

