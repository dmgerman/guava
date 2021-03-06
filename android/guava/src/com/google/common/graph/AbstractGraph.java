begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2016 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.graph
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|graph
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
name|javax
operator|.
name|annotation
operator|.
name|CheckForNull
import|;
end_import

begin_comment
comment|/**  * This class provides a skeletal implementation of {@link Graph}. It is recommended to extend this  * class rather than implement {@link Graph} directly.  *  * @author James Sexton  * @param<N> Node parameter type  * @since 20.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|AbstractGraph
specifier|public
specifier|abstract
class|class
name|AbstractGraph
parameter_list|<
name|N
parameter_list|>
extends|extends
name|AbstractBaseGraph
argument_list|<
name|N
argument_list|>
implements|implements
name|Graph
argument_list|<
name|N
argument_list|>
block|{
annotation|@
name|Override
DECL|method|equals (@heckForNull Object obj)
specifier|public
specifier|final
name|boolean
name|equals
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|==
name|this
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|Graph
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Graph
argument_list|<
name|?
argument_list|>
name|other
init|=
operator|(
name|Graph
argument_list|<
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
return|return
name|isDirected
argument_list|()
operator|==
name|other
operator|.
name|isDirected
argument_list|()
operator|&&
name|nodes
argument_list|()
operator|.
name|equals
argument_list|(
name|other
operator|.
name|nodes
argument_list|()
argument_list|)
operator|&&
name|edges
argument_list|()
operator|.
name|equals
argument_list|(
name|other
operator|.
name|edges
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
specifier|final
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|edges
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
comment|/** Returns a string representation of this graph. */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"isDirected: "
operator|+
name|isDirected
argument_list|()
operator|+
literal|", allowsSelfLoops: "
operator|+
name|allowsSelfLoops
argument_list|()
operator|+
literal|", nodes: "
operator|+
name|nodes
argument_list|()
operator|+
literal|", edges: "
operator|+
name|edges
argument_list|()
return|;
block|}
block|}
end_class

end_unit

