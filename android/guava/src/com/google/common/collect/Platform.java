begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|lang
operator|.
name|reflect
operator|.
name|Array
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
comment|/**  * Methods factored out so that they can be emulated differently in GWT.  *  * @author Hayward Chan  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|Platform
specifier|final
class|class
name|Platform
block|{
comment|/** Returns the platform preferred implementation of a map based on a hash table. */
specifier|static
operator|<
name|K
expr|extends @
name|Nullable
name|Object
operator|,
name|V
expr|extends @
name|Nullable
name|Object
operator|>
DECL|method|newHashMapWithExpectedSize (int expectedSize)
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newHashMapWithExpectedSize
argument_list|(
name|int
name|expectedSize
argument_list|)
block|{
return|return
name|CompactHashMap
operator|.
name|createWithExpectedSize
argument_list|(
name|expectedSize
argument_list|)
return|;
block|}
comment|/**    * Returns the platform preferred implementation of an insertion ordered map based on a hash    * table.    */
specifier|static
operator|<
name|K
expr|extends @
name|Nullable
name|Object
operator|,
name|V
expr|extends @
name|Nullable
name|Object
operator|>
DECL|method|newLinkedHashMapWithExpectedSize (int expectedSize)
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newLinkedHashMapWithExpectedSize
argument_list|(
name|int
name|expectedSize
argument_list|)
block|{
return|return
name|CompactLinkedHashMap
operator|.
name|createWithExpectedSize
argument_list|(
name|expectedSize
argument_list|)
return|;
block|}
comment|/** Returns the platform preferred implementation of a set based on a hash table. */
DECL|method|newHashSetWithExpectedSize (int expectedSize)
specifier|static
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
name|Set
argument_list|<
name|E
argument_list|>
name|newHashSetWithExpectedSize
argument_list|(
name|int
name|expectedSize
argument_list|)
block|{
return|return
name|CompactHashSet
operator|.
name|createWithExpectedSize
argument_list|(
name|expectedSize
argument_list|)
return|;
block|}
comment|/**    * Returns the platform preferred implementation of an insertion ordered set based on a hash    * table.    */
DECL|method|newLinkedHashSetWithExpectedSize (int expectedSize)
specifier|static
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
name|Set
argument_list|<
name|E
argument_list|>
name|newLinkedHashSetWithExpectedSize
argument_list|(
name|int
name|expectedSize
argument_list|)
block|{
return|return
name|CompactLinkedHashSet
operator|.
name|createWithExpectedSize
argument_list|(
name|expectedSize
argument_list|)
return|;
block|}
comment|/**    * Returns the platform preferred map implementation that preserves insertion order when used only    * for insertions.    */
specifier|static
operator|<
name|K
expr|extends @
name|Nullable
name|Object
operator|,
name|V
expr|extends @
name|Nullable
name|Object
operator|>
DECL|method|preservesInsertionOrderOnPutsMap ()
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|preservesInsertionOrderOnPutsMap
argument_list|()
block|{
return|return
name|CompactHashMap
operator|.
name|create
argument_list|()
return|;
block|}
comment|/**    * Returns the platform preferred set implementation that preserves insertion order when used only    * for insertions.    */
DECL|method|preservesInsertionOrderOnAddsSet ()
specifier|static
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
name|Set
argument_list|<
name|E
argument_list|>
name|preservesInsertionOrderOnAddsSet
argument_list|()
block|{
return|return
name|CompactHashSet
operator|.
name|create
argument_list|()
return|;
block|}
comment|/**    * Returns a new array of the given length with the same type as a reference array.    *    * @param reference any array of the desired type    * @param length the length of the new array    */
comment|/*    * The new array contains nulls, even if the old array did not. If we wanted to be accurate, we    * would declare a return type of `@Nullable T[]`. However, we've decided not to think too hard    * about arrays for now, as they're a mess. (We previously discussed this in the review of    * ObjectArrays, which is the main caller of this method.)    */
DECL|method|newArray (T[] reference, int length)
specifier|static
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
name|T
index|[]
name|newArray
argument_list|(
name|T
index|[]
name|reference
argument_list|,
name|int
name|length
argument_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|type
operator|=
name|reference
operator|.
name|getClass
argument_list|()
operator|.
name|getComponentType
argument_list|()
block|;
comment|// the cast is safe because
comment|// result.getClass() == reference.getClass().getComponentType()
block|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|T
index|[]
name|result
operator|=
operator|(
name|T
index|[]
operator|)
name|Array
operator|.
name|newInstance
argument_list|(
name|type
argument_list|,
name|length
argument_list|)
block|;
return|return
name|result
return|;
block|}
comment|/** Equivalent to Arrays.copyOfRange(source, from, to, arrayOfType.getClass()). */
comment|/*    * Arrays are a mess from a nullness perspective, and Class instances for object-array types are    * even worse. For now, we just suppress and move on with our lives.    *    * - https://github.com/jspecify/jspecify/issues/65    *    * - https://github.com/jspecify/jdk/commit/71d826792b8c7ef95d492c50a274deab938f2552    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"nullness"
argument_list|)
DECL|method|copy (Object[] source, int from, int to, T[] arrayOfType)
specifier|static
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
name|T
index|[]
name|copy
argument_list|(
name|Object
index|[]
name|source
argument_list|,
name|int
name|from
argument_list|,
name|int
name|to
argument_list|,
name|T
index|[]
name|arrayOfType
argument_list|)
block|{
return|return
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|source
argument_list|,
name|from
argument_list|,
name|to
argument_list|,
operator|(
name|Class
operator|<
operator|?
expr|extends
name|T
index|[]
operator|>
operator|)
name|arrayOfType
operator|.
name|getClass
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Configures the given map maker to use weak keys, if possible; does nothing otherwise (i.e., in    * GWT). This is sometimes acceptable, when only server-side code could generate enough volume    * that reclamation becomes important.    */
DECL|method|tryWeakKeys (MapMaker mapMaker)
specifier|static
name|MapMaker
name|tryWeakKeys
parameter_list|(
name|MapMaker
name|mapMaker
parameter_list|)
block|{
return|return
name|mapMaker
operator|.
name|weakKeys
argument_list|()
return|;
block|}
DECL|method|reduceIterationsIfGwt (int iterations)
specifier|static
name|int
name|reduceIterationsIfGwt
parameter_list|(
name|int
name|iterations
parameter_list|)
block|{
return|return
name|iterations
return|;
block|}
DECL|method|reduceExponentIfGwt (int exponent)
specifier|static
name|int
name|reduceExponentIfGwt
parameter_list|(
name|int
name|exponent
parameter_list|)
block|{
return|return
name|exponent
return|;
block|}
DECL|method|checkGwtRpcEnabled ()
specifier|static
name|void
name|checkGwtRpcEnabled
parameter_list|()
block|{}
DECL|method|Platform ()
specifier|private
name|Platform
parameter_list|()
block|{}
block|}
end_class

end_unit

