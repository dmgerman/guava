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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_comment
comment|/**  * Methods factored out so that they can be emulated differently in GWT.  *  *<p>This class is emulated in GWT.  *  * @author Hayward Chan  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|Platform
specifier|final
class|class
name|Platform
block|{
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
name|array
operator|.
name|clone
argument_list|()
return|;
block|}
comment|// Class.cast is not supported in GWT.  This method is a no-op in GWT.
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
block|{
name|clazz
operator|.
name|cast
argument_list|(
name|obj
argument_list|)
expr_stmt|;
block|}
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
return|return
name|String
operator|.
name|format
argument_list|(
name|Locale
operator|.
name|ROOT
argument_list|,
name|template
argument_list|,
name|args
argument_list|)
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

