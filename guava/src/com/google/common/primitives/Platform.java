begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2019 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.primitives
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
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
name|Strings
operator|.
name|lenientFormat
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|Boolean
operator|.
name|parseBoolean
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
name|GwtCompatible
import|;
end_import

begin_comment
comment|/** Methods factored out so that they can be emulated differently in GWT. */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|Platform
specifier|final
class|class
name|Platform
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
name|logger
init|=
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
operator|.
name|getLogger
argument_list|(
name|Platform
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|GWT_RPC_PROPERTY_NAME
specifier|private
specifier|static
specifier|final
name|String
name|GWT_RPC_PROPERTY_NAME
init|=
literal|"guava.gwt.emergency_reenable_rpc"
decl_stmt|;
DECL|method|checkGwtRpcEnabled ()
specifier|static
name|void
name|checkGwtRpcEnabled
parameter_list|()
block|{
if|if
condition|(
operator|!
name|parseBoolean
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
name|GWT_RPC_PROPERTY_NAME
argument_list|,
literal|"true"
argument_list|)
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
name|lenientFormat
argument_list|(
literal|"We are removing GWT-RPC support for Guava types. You can temporarily reenable"
operator|+
literal|" support by setting the system property %s to true. For more about system"
operator|+
literal|" properties, see %s. For more about Guava's GWT-RPC support, see %s."
argument_list|,
name|GWT_RPC_PROPERTY_NAME
argument_list|,
literal|"https://stackoverflow.com/q/5189914/28465"
argument_list|,
literal|"https://groups.google.com/d/msg/guava-announce/zHZTFg7YF3o/rQNnwdHeEwAJ"
argument_list|)
argument_list|)
throw|;
block|}
name|logger
operator|.
name|log
argument_list|(
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
operator|.
name|WARNING
argument_list|,
literal|"In January 2020, we will remove GWT-RPC support for Guava types. You are seeing this"
operator|+
literal|" warning because you are sending a Guava type over GWT-RPC, which will break. You"
operator|+
literal|" can identify which type by looking at the class name in the attached stack trace."
argument_list|,
operator|new
name|Throwable
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|Platform ()
specifier|private
name|Platform
parameter_list|()
block|{}
block|}
end_class

end_unit

