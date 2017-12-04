begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS-IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Comparator
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
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_comment
comment|/**  * Contains dummy collection implementations to convince GWT that part of serializing a collection  * is serializing its elements.  *  *<p>Because of our use of final fields in our collections, GWT's normal heuristic for determining  * which classes might be serialized fails. That heuristic is, roughly speaking, to look at each  * parameter and return type of each RPC interface and to assume that implementations of those types  * might be serialized. Those types have their own dependencies -- their fields -- which are  * analyzed recursively and analogously.  *  *<p>For classes with final fields, GWT assumes that the class itself might be serialized but  * doesn't assume the same about its final fields. To work around this, we provide dummy  * implementations of our collections with their dependencies as non-final fields. Even though these  * implementations are never instantiated, they are visible to GWT when it performs its  * serialization analysis, and it assumes that their fields may be serialized.  *  *<p>Currently we provide dummy implementations of all the immutable collection classes necessary  * to support declarations like {@code ImmutableMultiset<String>} in RPC interfaces. Support for  * {@code ImmutableMultiset} in the interface is support for {@code Multiset}, so there is nothing  * further to be done to support the new collection interfaces. It is not support, however, for an  * RPC interface in terms of {@code HashMultiset}. It is still possible to send a {@code  * HashMultiset} over GWT RPC; it is only the declaration of an interface in terms of {@code  * HashMultiset} that we haven't tried to support. (We may wish to revisit this decision in the  * future.)  *  * @author Chris Povirk  */
end_comment

begin_class
annotation|@
name|GwtCompatible
comment|// None of these classes are instantiated, let alone serialized:
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
DECL|class|GwtSerializationDependencies
specifier|final
class|class
name|GwtSerializationDependencies
block|{
DECL|method|GwtSerializationDependencies ()
specifier|private
name|GwtSerializationDependencies
parameter_list|()
block|{}
DECL|class|ImmutableListMultimapDependencies
specifier|static
specifier|final
class|class
name|ImmutableListMultimapDependencies
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|key
name|K
name|key
decl_stmt|;
DECL|field|value
name|V
name|value
decl_stmt|;
DECL|method|ImmutableListMultimapDependencies ()
name|ImmutableListMultimapDependencies
parameter_list|()
block|{
name|super
argument_list|(
literal|null
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
comment|// ImmutableMap is covered by ImmutableSortedMap/ImmutableBiMap.
comment|// ImmutableMultimap is covered by ImmutableSetMultimap/ImmutableListMultimap.
DECL|class|ImmutableSetMultimapDependencies
specifier|static
specifier|final
class|class
name|ImmutableSetMultimapDependencies
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|key
name|K
name|key
decl_stmt|;
DECL|field|value
name|V
name|value
decl_stmt|;
DECL|method|ImmutableSetMultimapDependencies ()
name|ImmutableSetMultimapDependencies
parameter_list|()
block|{
name|super
argument_list|(
literal|null
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*    * We support an interface declared in terms of LinkedListMultimap because it    * supports entry ordering not supported by other implementations.    */
DECL|class|LinkedListMultimapDependencies
specifier|static
specifier|final
class|class
name|LinkedListMultimapDependencies
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|LinkedListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|key
name|K
name|key
decl_stmt|;
DECL|field|value
name|V
name|value
decl_stmt|;
DECL|method|LinkedListMultimapDependencies ()
name|LinkedListMultimapDependencies
parameter_list|()
block|{}
block|}
DECL|class|HashBasedTableDependencies
specifier|static
specifier|final
class|class
name|HashBasedTableDependencies
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
extends|extends
name|HashBasedTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
block|{
DECL|field|data
name|HashMap
argument_list|<
name|R
argument_list|,
name|HashMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|data
decl_stmt|;
DECL|method|HashBasedTableDependencies ()
name|HashBasedTableDependencies
parameter_list|()
block|{
name|super
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|TreeBasedTableDependencies
specifier|static
specifier|final
class|class
name|TreeBasedTableDependencies
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
extends|extends
name|TreeBasedTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
block|{
DECL|field|data
name|TreeMap
argument_list|<
name|R
argument_list|,
name|TreeMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|data
decl_stmt|;
DECL|method|TreeBasedTableDependencies ()
name|TreeBasedTableDependencies
parameter_list|()
block|{
name|super
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*    * We don't normally need "implements Serializable," but we do here. That's    * because ImmutableTable itself is not Serializable as of this writing. We    * need for GWT to believe that this dummy class is serializable, or else it    * won't generate serialization code for R, C, and V.    */
DECL|class|ImmutableTableDependencies
specifier|static
specifier|final
class|class
name|ImmutableTableDependencies
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
extends|extends
name|SingletonImmutableTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
implements|implements
name|Serializable
block|{
DECL|field|rowKey
name|R
name|rowKey
decl_stmt|;
DECL|field|columnKey
name|C
name|columnKey
decl_stmt|;
DECL|field|value
name|V
name|value
decl_stmt|;
DECL|method|ImmutableTableDependencies ()
name|ImmutableTableDependencies
parameter_list|()
block|{
name|super
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|TreeMultimapDependencies
specifier|static
specifier|final
class|class
name|TreeMultimapDependencies
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|TreeMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|keyComparator
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|keyComparator
decl_stmt|;
DECL|field|valueComparator
name|Comparator
argument_list|<
name|?
super|super
name|V
argument_list|>
name|valueComparator
decl_stmt|;
DECL|field|key
name|K
name|key
decl_stmt|;
DECL|field|value
name|V
name|value
decl_stmt|;
DECL|method|TreeMultimapDependencies ()
name|TreeMultimapDependencies
parameter_list|()
block|{
name|super
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

