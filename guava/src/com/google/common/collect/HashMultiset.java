begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
comment|/**  * Multiset implementation backed by a {@link HashMap}.  *  * @author Kevin Bourrillion  * @author Jared Levy  * @since 2.0  */
end_comment

begin_annotation
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|,
name|emulated
operator|=
literal|true
argument_list|)
end_annotation

begin_annotation
annotation|@
name|ElementTypesAreNonnullByDefault
end_annotation

begin_expr_stmt
DECL|class|HashMultiset
specifier|public
name|final
name|class
name|HashMultiset
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|AbstractMapBasedMultiset
argument_list|<
name|E
argument_list|>
block|{
comment|/** Creates a new, empty {@code HashMultiset} using the default initial capacity. */
DECL|method|create ()
specifier|public
specifier|static
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
name|HashMultiset
argument_list|<
name|E
argument_list|>
name|create
argument_list|()
block|{
return|return
operator|new
name|HashMultiset
argument_list|<
name|E
argument_list|>
argument_list|()
return|;
block|}
comment|/**    * Creates a new, empty {@code HashMultiset} with the specified expected number of distinct    * elements.    *    * @param distinctElements the expected number of distinct elements    * @throws IllegalArgumentException if {@code distinctElements} is negative    */
DECL|method|create (int distinctElements)
specifier|public
specifier|static
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
name|HashMultiset
argument_list|<
name|E
argument_list|>
name|create
argument_list|(
name|int
name|distinctElements
argument_list|)
block|{
return|return
operator|new
name|HashMultiset
argument_list|<
name|E
argument_list|>
argument_list|(
name|distinctElements
argument_list|)
return|;
block|}
end_expr_stmt

begin_comment
comment|/**    * Creates a new {@code HashMultiset} containing the specified elements.    *    *<p>This implementation is highly efficient when {@code elements} is itself a {@link Multiset}.    *    * @param elements the elements that the multiset should contain    */
end_comment

begin_expr_stmt
DECL|method|create ( Iterable<? extends E> elements)
specifier|public
specifier|static
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
name|HashMultiset
argument_list|<
name|E
argument_list|>
name|create
argument_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|elements
argument_list|)
block|{
name|HashMultiset
argument_list|<
name|E
argument_list|>
name|multiset
operator|=
name|create
argument_list|(
name|Multisets
operator|.
name|inferDistinctElements
argument_list|(
name|elements
argument_list|)
argument_list|)
block|;
name|Iterables
operator|.
name|addAll
argument_list|(
name|multiset
argument_list|,
name|elements
argument_list|)
block|;
return|return
name|multiset
return|;
block|}
end_expr_stmt

begin_constructor
DECL|method|HashMultiset ()
specifier|private
name|HashMultiset
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|HashMap
argument_list|<
name|E
argument_list|,
name|Count
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
end_constructor

begin_constructor
DECL|method|HashMultiset (int distinctElements)
specifier|private
name|HashMultiset
parameter_list|(
name|int
name|distinctElements
parameter_list|)
block|{
name|super
argument_list|(
name|Maps
operator|.
expr|<
name|E
argument_list|,
name|Count
operator|>
name|newHashMapWithExpectedSize
argument_list|(
name|distinctElements
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_constructor

begin_comment
comment|/**    * @serialData the number of distinct elements, the first element, its count, the second element,    *     its count, and so on    */
end_comment

begin_function
annotation|@
name|GwtIncompatible
comment|// java.io.ObjectOutputStream
DECL|method|writeObject (ObjectOutputStream stream)
specifier|private
name|void
name|writeObject
parameter_list|(
name|ObjectOutputStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
name|stream
operator|.
name|defaultWriteObject
argument_list|()
expr_stmt|;
name|Serialization
operator|.
name|writeMultiset
argument_list|(
name|this
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|GwtIncompatible
comment|// java.io.ObjectInputStream
DECL|method|readObject (ObjectInputStream stream)
specifier|private
name|void
name|readObject
parameter_list|(
name|ObjectInputStream
name|stream
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|stream
operator|.
name|defaultReadObject
argument_list|()
expr_stmt|;
name|int
name|distinctElements
init|=
name|Serialization
operator|.
name|readCount
argument_list|(
name|stream
argument_list|)
decl_stmt|;
name|setBackingMap
argument_list|(
name|Maps
operator|.
expr|<
name|E
argument_list|,
name|Count
operator|>
name|newHashMap
argument_list|()
argument_list|)
expr_stmt|;
name|Serialization
operator|.
name|populateMultiset
argument_list|(
name|this
argument_list|,
name|stream
argument_list|,
name|distinctElements
argument_list|)
expr_stmt|;
block|}
end_function

begin_decl_stmt
annotation|@
name|GwtIncompatible
comment|// Not needed in emulated source.
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
end_decl_stmt

unit|}
end_unit

