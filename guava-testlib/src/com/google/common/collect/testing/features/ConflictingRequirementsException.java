begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing.features
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
operator|.
name|features
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
name|Set
import|;
end_import

begin_comment
comment|/**  * Thrown when requirements on a tester method or class conflict with each other.  *  * @author George van den Driessche  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ConflictingRequirementsException
specifier|public
class|class
name|ConflictingRequirementsException
extends|extends
name|Exception
block|{
DECL|field|conflicts
specifier|private
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|conflicts
decl_stmt|;
DECL|field|source
specifier|private
name|Object
name|source
decl_stmt|;
DECL|method|ConflictingRequirementsException ( String message, Set<Feature<?>> conflicts, Object source)
specifier|public
name|ConflictingRequirementsException
parameter_list|(
name|String
name|message
parameter_list|,
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|conflicts
parameter_list|,
name|Object
name|source
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|this
operator|.
name|conflicts
operator|=
name|conflicts
expr_stmt|;
name|this
operator|.
name|source
operator|=
name|source
expr_stmt|;
block|}
DECL|method|getConflicts ()
specifier|public
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|getConflicts
parameter_list|()
block|{
return|return
name|conflicts
return|;
block|}
DECL|method|getSource ()
specifier|public
name|Object
name|getSource
parameter_list|()
block|{
return|return
name|source
return|;
block|}
annotation|@
name|Override
DECL|method|getMessage ()
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|super
operator|.
name|getMessage
argument_list|()
operator|+
literal|" (source: "
operator|+
name|source
operator|+
literal|")"
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
end_class

end_unit

