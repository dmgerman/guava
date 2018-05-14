begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.base
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
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
name|checkState
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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|errorprone
operator|.
name|annotations
operator|.
name|CanIgnoreReturnValue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|NoSuchElementException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|compatqual
operator|.
name|NullableDecl
import|;
end_import

begin_comment
comment|/**  * Note this class is a copy of {@link com.google.common.collect.AbstractIterator} (for dependency  * reasons).  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|AbstractIterator
specifier|abstract
class|class
name|AbstractIterator
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Iterator
argument_list|<
name|T
argument_list|>
block|{
DECL|field|state
specifier|private
name|State
name|state
init|=
name|State
operator|.
name|NOT_READY
decl_stmt|;
DECL|method|AbstractIterator ()
specifier|protected
name|AbstractIterator
parameter_list|()
block|{}
DECL|enum|State
specifier|private
enum|enum
name|State
block|{
DECL|enumConstant|READY
name|READY
block|,
DECL|enumConstant|NOT_READY
name|NOT_READY
block|,
DECL|enumConstant|DONE
name|DONE
block|,
DECL|enumConstant|FAILED
name|FAILED
block|,   }
DECL|field|next
annotation|@
name|NullableDecl
specifier|private
name|T
name|next
decl_stmt|;
DECL|method|computeNext ()
specifier|protected
specifier|abstract
name|T
name|computeNext
parameter_list|()
function_decl|;
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|NullableDecl
DECL|method|endOfData ()
specifier|protected
specifier|final
name|T
name|endOfData
parameter_list|()
block|{
name|state
operator|=
name|State
operator|.
name|DONE
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|hasNext ()
specifier|public
specifier|final
name|boolean
name|hasNext
parameter_list|()
block|{
name|checkState
argument_list|(
name|state
operator|!=
name|State
operator|.
name|FAILED
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|state
condition|)
block|{
case|case
name|READY
case|:
return|return
literal|true
return|;
case|case
name|DONE
case|:
return|return
literal|false
return|;
default|default:
block|}
return|return
name|tryToComputeNext
argument_list|()
return|;
block|}
DECL|method|tryToComputeNext ()
specifier|private
name|boolean
name|tryToComputeNext
parameter_list|()
block|{
name|state
operator|=
name|State
operator|.
name|FAILED
expr_stmt|;
comment|// temporary pessimism
name|next
operator|=
name|computeNext
argument_list|()
expr_stmt|;
if|if
condition|(
name|state
operator|!=
name|State
operator|.
name|DONE
condition|)
block|{
name|state
operator|=
name|State
operator|.
name|READY
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|next ()
specifier|public
specifier|final
name|T
name|next
parameter_list|()
block|{
if|if
condition|(
operator|!
name|hasNext
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
name|state
operator|=
name|State
operator|.
name|NOT_READY
expr_stmt|;
name|T
name|result
init|=
name|next
decl_stmt|;
name|next
operator|=
literal|null
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|remove ()
specifier|public
specifier|final
name|void
name|remove
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
end_class

end_unit

