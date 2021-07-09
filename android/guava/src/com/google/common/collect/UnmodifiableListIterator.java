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
name|DoNotCall
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ListIterator
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
name|qual
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * A list iterator that does not support {@link #remove}, {@link #add}, or {@link #set}.  *  * @since 7.0  * @author Louis Wasserman  */
end_comment

begin_annotation
annotation|@
name|GwtCompatible
end_annotation

begin_annotation
annotation|@
name|ElementTypesAreNonnullByDefault
end_annotation

begin_expr_stmt
DECL|class|UnmodifiableListIterator
specifier|public
specifier|abstract
name|class
name|UnmodifiableListIterator
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|UnmodifiableIterator
argument_list|<
name|E
argument_list|>
expr|implements
name|ListIterator
argument_list|<
name|E
argument_list|>
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|UnmodifiableListIterator ()
specifier|protected
name|UnmodifiableListIterator
argument_list|()
block|{}
comment|/**    * Guaranteed to throw an exception and leave the underlying data unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
expr|@
name|Deprecated
expr|@
name|Override
expr|@
name|DoNotCall
argument_list|(
literal|"Always throws UnsupportedOperationException"
argument_list|)
DECL|method|add (@arametricNullness E e)
specifier|public
name|final
name|void
name|add
argument_list|(
annotation|@
name|ParametricNullness
name|E
name|e
argument_list|)
block|{
throw|throw
argument_list|new
name|UnsupportedOperationException
argument_list|()
block|;   }
comment|/**    * Guaranteed to throw an exception and leave the underlying data unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
expr|@
name|Deprecated
expr|@
name|Override
expr|@
name|DoNotCall
argument_list|(
literal|"Always throws UnsupportedOperationException"
argument_list|)
DECL|method|set (@arametricNullness E e)
specifier|public
name|final
name|void
name|set
argument_list|(
annotation|@
name|ParametricNullness
name|E
name|e
argument_list|)
block|{
throw|throw
argument_list|new
name|UnsupportedOperationException
argument_list|()
block|;   }
block|}
end_expr_stmt

end_unit

