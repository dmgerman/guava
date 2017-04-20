begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|io
operator|.
name|Serializable
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|class|PairwiseEquivalence
specifier|final
class|class
name|PairwiseEquivalence
parameter_list|<
name|T
parameter_list|>
extends|extends
name|Equivalence
argument_list|<
name|Iterable
argument_list|<
name|T
argument_list|>
argument_list|>
implements|implements
name|Serializable
block|{
DECL|field|elementEquivalence
specifier|final
name|Equivalence
argument_list|<
name|?
super|super
name|T
argument_list|>
name|elementEquivalence
decl_stmt|;
DECL|method|PairwiseEquivalence (Equivalence<? super T> elementEquivalence)
name|PairwiseEquivalence
parameter_list|(
name|Equivalence
argument_list|<
name|?
super|super
name|T
argument_list|>
name|elementEquivalence
parameter_list|)
block|{
name|this
operator|.
name|elementEquivalence
operator|=
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|elementEquivalence
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doEquivalent (Iterable<T> iterableA, Iterable<T> iterableB)
specifier|protected
name|boolean
name|doEquivalent
parameter_list|(
name|Iterable
argument_list|<
name|T
argument_list|>
name|iterableA
parameter_list|,
name|Iterable
argument_list|<
name|T
argument_list|>
name|iterableB
parameter_list|)
block|{
name|Iterator
argument_list|<
name|T
argument_list|>
name|iteratorA
init|=
name|iterableA
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|T
argument_list|>
name|iteratorB
init|=
name|iterableB
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iteratorA
operator|.
name|hasNext
argument_list|()
operator|&&
name|iteratorB
operator|.
name|hasNext
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|elementEquivalence
operator|.
name|equivalent
argument_list|(
name|iteratorA
operator|.
name|next
argument_list|()
argument_list|,
name|iteratorB
operator|.
name|next
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
operator|!
name|iteratorA
operator|.
name|hasNext
argument_list|()
operator|&&
operator|!
name|iteratorB
operator|.
name|hasNext
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|doHash (Iterable<T> iterable)
specifier|protected
name|int
name|doHash
parameter_list|(
name|Iterable
argument_list|<
name|T
argument_list|>
name|iterable
parameter_list|)
block|{
name|int
name|hash
init|=
literal|78721
decl_stmt|;
for|for
control|(
name|T
name|element
range|:
name|iterable
control|)
block|{
name|hash
operator|=
name|hash
operator|*
literal|24943
operator|+
name|elementEquivalence
operator|.
name|hash
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
return|return
name|hash
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@ullable Object object)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|PairwiseEquivalence
condition|)
block|{
name|PairwiseEquivalence
argument_list|<
name|?
argument_list|>
name|that
init|=
operator|(
name|PairwiseEquivalence
argument_list|<
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
return|return
name|this
operator|.
name|elementEquivalence
operator|.
name|equals
argument_list|(
name|that
operator|.
name|elementEquivalence
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|elementEquivalence
operator|.
name|hashCode
argument_list|()
operator|^
literal|0x46a3eb07
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|elementEquivalence
operator|+
literal|".pairwise()"
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1
decl_stmt|;
block|}
end_class

end_unit

