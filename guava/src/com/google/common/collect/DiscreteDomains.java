begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
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
comment|/**  * Factories for common {@link DiscreteDomain} instances.  *  *<p>See the Guava User Guide section on<a href=  * "http://code.google.com/p/guava-libraries/wiki/RangesExplained#Discrete_Domains">  * {@code DiscreteDomain}</a>.  *  * @author Gregory Kick  * @since 10.0  * @deprecated Merged into {@link DiscreteDomain}.  This class is scheduled for deletion in release  *             15.0.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|Deprecated
DECL|class|DiscreteDomains
specifier|public
specifier|final
class|class
name|DiscreteDomains
block|{
DECL|method|DiscreteDomains ()
specifier|private
name|DiscreteDomains
parameter_list|()
block|{}
comment|/**    * Returns the discrete domain for values of type {@code Integer}.    */
DECL|method|integers ()
specifier|public
specifier|static
name|DiscreteDomain
argument_list|<
name|Integer
argument_list|>
name|integers
parameter_list|()
block|{
return|return
name|DiscreteDomain
operator|.
name|integers
argument_list|()
return|;
block|}
comment|/**    * Returns the discrete domain for values of type {@code Long}.    */
DECL|method|longs ()
specifier|public
specifier|static
name|DiscreteDomain
argument_list|<
name|Long
argument_list|>
name|longs
parameter_list|()
block|{
return|return
name|DiscreteDomain
operator|.
name|longs
argument_list|()
return|;
block|}
block|}
end_class

end_unit

