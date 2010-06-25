begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|checkNotNull
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/** An ordering that uses the natural order of the values. */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// TODO: the right way to explain this??
DECL|class|NaturalOrdering
specifier|final
class|class
name|NaturalOrdering
extends|extends
name|Ordering
argument_list|<
name|Comparable
argument_list|>
implements|implements
name|Serializable
block|{
DECL|field|INSTANCE
specifier|static
specifier|final
name|NaturalOrdering
name|INSTANCE
init|=
operator|new
name|NaturalOrdering
argument_list|()
decl_stmt|;
DECL|method|compare (Comparable left, Comparable right)
specifier|public
name|int
name|compare
parameter_list|(
name|Comparable
name|left
parameter_list|,
name|Comparable
name|right
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|right
argument_list|)
expr_stmt|;
comment|// left null is caught later
if|if
condition|(
name|left
operator|==
name|right
condition|)
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// we're permitted to throw CCE
name|int
name|result
init|=
name|left
operator|.
name|compareTo
argument_list|(
name|right
argument_list|)
decl_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// TODO: the right way to explain this??
DECL|method|reverse ()
annotation|@
name|Override
specifier|public
parameter_list|<
name|S
extends|extends
name|Comparable
parameter_list|>
name|Ordering
argument_list|<
name|S
argument_list|>
name|reverse
parameter_list|()
block|{
return|return
operator|(
name|Ordering
operator|)
name|ReverseNaturalOrdering
operator|.
name|INSTANCE
return|;
block|}
comment|// Override to remove a level of indirection from inner loop
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// TODO: the right way to explain this??
DECL|method|binarySearch ( List<? extends Comparable> sortedList, Comparable key)
annotation|@
name|Override
specifier|public
name|int
name|binarySearch
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|Comparable
argument_list|>
name|sortedList
parameter_list|,
name|Comparable
name|key
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|binarySearch
argument_list|(
operator|(
name|List
operator|)
name|sortedList
argument_list|,
name|key
argument_list|)
return|;
block|}
comment|// Override to remove a level of indirection from inner loop
DECL|method|sortedCopy ( Iterable<E> iterable)
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
extends|extends
name|Comparable
parameter_list|>
name|List
argument_list|<
name|E
argument_list|>
name|sortedCopy
parameter_list|(
name|Iterable
argument_list|<
name|E
argument_list|>
name|iterable
parameter_list|)
block|{
name|List
argument_list|<
name|E
argument_list|>
name|list
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|iterable
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|list
argument_list|)
expr_stmt|;
return|return
name|list
return|;
block|}
comment|// preserving singleton-ness gives equals()/hashCode() for free
DECL|method|readResolve ()
specifier|private
name|Object
name|readResolve
parameter_list|()
block|{
return|return
name|INSTANCE
return|;
block|}
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Ordering.natural()"
return|;
block|}
DECL|method|NaturalOrdering ()
specifier|private
name|NaturalOrdering
parameter_list|()
block|{}
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

