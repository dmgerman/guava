begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|GwtIncompatible
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
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
comment|/**  * Superinterface of {@link SortedMultiset} to introduce a bridge method for {@code elementSet()},  * to ensure binary compatibility with older Guava versions that specified {@code elementSet()} to  * return {@code SortedSet}.  *  * @author Louis Wasserman  */
end_comment

begin_annotation
annotation|@
name|GwtIncompatible
end_annotation

begin_annotation
annotation|@
name|ElementTypesAreNonnullByDefault
end_annotation

begin_expr_stmt
DECL|interface|SortedMultisetBridge
unit|interface
name|SortedMultisetBridge
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|Multiset
argument_list|<
name|E
argument_list|>
block|{   @
name|Override
DECL|method|elementSet ()
name|SortedSet
argument_list|<
name|E
argument_list|>
name|elementSet
argument_list|()
block|; }
end_expr_stmt

end_unit

