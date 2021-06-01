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
comment|/**  * A {@code Multiset} implementation with predictable iteration order. Its iterator orders elements  * according to when the first occurrence of the element was added. When the multiset contains  * multiple instances of an element, those instances are consecutive in the iteration order. If all  * occurrences of an element are removed, after which that element is added to the multiset, the  * element will appear at the end of the iteration.  *  *<p>See the Guava User Guide article on<a href=  * "https://github.com/google/guava/wiki/NewCollectionTypesExplained#multiset"> {@code  * Multiset}</a>.  *  * @author Kevin Bourrillion  * @author Jared Levy  * @since 2.0  */
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
DECL|class|LinkedHashMultiset
specifier|public
name|final
name|class
name|LinkedHashMultiset
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
comment|/** Creates a new, empty {@code LinkedHashMultiset} using the default initial capacity. */
DECL|method|create ()
specifier|public
specifier|static
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
name|LinkedHashMultiset
argument_list|<
name|E
argument_list|>
name|create
argument_list|()
block|{
return|return
name|create
argument_list|(
name|ObjectCountHashMap
operator|.
name|DEFAULT_SIZE
argument_list|)
return|;
block|}
comment|/**    * Creates a new, empty {@code LinkedHashMultiset} with the specified expected number of distinct    * elements.    *    * @param distinctElements the expected number of distinct elements    * @throws IllegalArgumentException if {@code distinctElements} is negative    */
DECL|method|create (int distinctElements)
specifier|public
specifier|static
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
name|LinkedHashMultiset
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
name|LinkedHashMultiset
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
comment|/**    * Creates a new {@code LinkedHashMultiset} containing the specified elements.    *    *<p>This implementation is highly efficient when {@code elements} is itself a {@link Multiset}.    *    * @param elements the elements that the multiset should contain    */
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
name|LinkedHashMultiset
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
name|LinkedHashMultiset
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

begin_expr_stmt
DECL|method|LinkedHashMultiset (int distinctElements)
name|LinkedHashMultiset
argument_list|(
name|int
name|distinctElements
argument_list|)
block|{
name|super
argument_list|(
name|distinctElements
argument_list|)
block|;   }
expr|@
name|Override
DECL|method|newBackingMap (int distinctElements)
name|ObjectCountHashMap
argument_list|<
name|E
argument_list|>
name|newBackingMap
argument_list|(
name|int
name|distinctElements
argument_list|)
block|{
return|return
operator|new
name|ObjectCountLinkedHashMap
argument_list|<>
argument_list|(
name|distinctElements
argument_list|)
return|;
block|}
end_expr_stmt

unit|}
end_unit

