begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|checkArgument
import|;
end_import

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
name|Map
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

begin_comment
comment|/**  * Static utility methods pertaining to {@code Function} instances.  *  *<p>All methods returns serializable functions as long as they're given serializable parameters.  *  * @author Mike Bostock  * @author Jared Levy  * @since Guava release 02 (imported from Google Collections Library)  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|Functions
specifier|public
specifier|final
class|class
name|Functions
block|{
DECL|method|Functions ()
specifier|private
name|Functions
parameter_list|()
block|{}
comment|/**    * Returns a function that calls {@code toString()} on its argument. The function does not accept    * nulls; it will throw a {@link NullPointerException} when applied to {@code null}.    *    *<p><b>Warning:</b> The returned function may not be<i>consistent with equals</i> (as    * documented at {@link Function#apply}). For example, this function yields different results for    * the two equal instances {@code ImmutableSet.of(1, 2)} and {@code ImmutableSet.of(2, 1)}.    */
DECL|method|toStringFunction ()
specifier|public
specifier|static
name|Function
argument_list|<
name|Object
argument_list|,
name|String
argument_list|>
name|toStringFunction
parameter_list|()
block|{
return|return
name|ToStringFunction
operator|.
name|INSTANCE
return|;
block|}
comment|// enum singleton pattern
DECL|enum|ToStringFunction
specifier|private
enum|enum
name|ToStringFunction
implements|implements
name|Function
argument_list|<
name|Object
argument_list|,
name|String
argument_list|>
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
annotation|@
name|Override
DECL|method|apply (Object o)
specifier|public
name|String
name|apply
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|o
argument_list|)
expr_stmt|;
comment|// eager for GWT.
return|return
name|o
operator|.
name|toString
argument_list|()
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
literal|"toString"
return|;
block|}
block|}
comment|/**    * Returns the identity function.    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|identity ()
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Function
argument_list|<
name|E
argument_list|,
name|E
argument_list|>
name|identity
parameter_list|()
block|{
return|return
operator|(
name|Function
argument_list|<
name|E
argument_list|,
name|E
argument_list|>
operator|)
name|IdentityFunction
operator|.
name|INSTANCE
return|;
block|}
comment|// enum singleton pattern
DECL|enum|IdentityFunction
specifier|private
enum|enum
name|IdentityFunction
implements|implements
name|Function
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
annotation|@
name|Override
DECL|method|apply (Object o)
specifier|public
name|Object
name|apply
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|o
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
literal|"identity"
return|;
block|}
block|}
comment|/**    * Returns a function which performs a map lookup. The returned function throws an {@link    * IllegalArgumentException} if given a key that does not exist in the map.    */
DECL|method|forMap (Map<K, V> map)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Function
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|forMap
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|)
block|{
return|return
operator|new
name|FunctionForMapNoDefault
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|map
argument_list|)
return|;
block|}
DECL|class|FunctionForMapNoDefault
specifier|private
specifier|static
class|class
name|FunctionForMapNoDefault
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|Function
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|map
specifier|final
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
decl_stmt|;
DECL|method|FunctionForMapNoDefault (Map<K, V> map)
name|FunctionForMapNoDefault
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|)
block|{
name|this
operator|.
name|map
operator|=
name|checkNotNull
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (K key)
specifier|public
name|V
name|apply
parameter_list|(
name|K
name|key
parameter_list|)
block|{
name|V
name|result
init|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|result
operator|!=
literal|null
operator|||
name|map
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
argument_list|,
literal|"Key '%s' not present in map"
argument_list|,
name|key
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|method|equals (@ullable Object o)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|FunctionForMapNoDefault
condition|)
block|{
name|FunctionForMapNoDefault
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|that
init|=
operator|(
name|FunctionForMapNoDefault
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|o
decl_stmt|;
return|return
name|map
operator|.
name|equals
argument_list|(
name|that
operator|.
name|map
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|map
operator|.
name|hashCode
argument_list|()
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
literal|"forMap("
operator|+
name|map
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
comment|/**    * Returns a function which performs a map lookup with a default value. The function created by    * this method returns {@code defaultValue} for all inputs that do not belong to the map's key    * set.    *    * @param map source map that determines the function behavior    * @param defaultValue the value to return for inputs that aren't map keys    * @return function that returns {@code map.get(a)} when {@code a} is a key, or {@code    *         defaultValue} otherwise    */
DECL|method|forMap (Map<K, ? extends V> map, @Nullable V defaultValue)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Function
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|forMap
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|map
parameter_list|,
annotation|@
name|Nullable
name|V
name|defaultValue
parameter_list|)
block|{
return|return
operator|new
name|ForMapWithDefault
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|map
argument_list|,
name|defaultValue
argument_list|)
return|;
block|}
DECL|class|ForMapWithDefault
specifier|private
specifier|static
class|class
name|ForMapWithDefault
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|Function
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|map
specifier|final
name|Map
argument_list|<
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|map
decl_stmt|;
DECL|field|defaultValue
specifier|final
name|V
name|defaultValue
decl_stmt|;
DECL|method|ForMapWithDefault (Map<K, ? extends V> map, @Nullable V defaultValue)
name|ForMapWithDefault
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|map
parameter_list|,
annotation|@
name|Nullable
name|V
name|defaultValue
parameter_list|)
block|{
name|this
operator|.
name|map
operator|=
name|checkNotNull
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|this
operator|.
name|defaultValue
operator|=
name|defaultValue
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (K key)
specifier|public
name|V
name|apply
parameter_list|(
name|K
name|key
parameter_list|)
block|{
name|V
name|result
init|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
operator|(
name|result
operator|!=
literal|null
operator|||
name|map
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
operator|)
condition|?
name|result
else|:
name|defaultValue
return|;
block|}
DECL|method|equals (@ullable Object o)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|ForMapWithDefault
condition|)
block|{
name|ForMapWithDefault
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|that
init|=
operator|(
name|ForMapWithDefault
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|o
decl_stmt|;
return|return
name|map
operator|.
name|equals
argument_list|(
name|that
operator|.
name|map
argument_list|)
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|defaultValue
argument_list|,
name|that
operator|.
name|defaultValue
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hashCode
argument_list|(
name|map
argument_list|,
name|defaultValue
argument_list|)
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
literal|"forMap("
operator|+
name|map
operator|+
literal|", defaultValue="
operator|+
name|defaultValue
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
comment|/**    * Returns the composition of two functions. For {@code f: A->B} and {@code g: B->C}, composition    * is defined as the function h such that {@code h(a) == g(f(a))} for each {@code a}.    *    * @param g the second function to apply    * @param f the first function to apply    * @return the composition of {@code f} and {@code g}    * @see<a href="//en.wikipedia.org/wiki/Function_composition">function composition</a>    */
DECL|method|compose (Function<B, C> g, Function<A, ? extends B> f)
specifier|public
specifier|static
parameter_list|<
name|A
parameter_list|,
name|B
parameter_list|,
name|C
parameter_list|>
name|Function
argument_list|<
name|A
argument_list|,
name|C
argument_list|>
name|compose
parameter_list|(
name|Function
argument_list|<
name|B
argument_list|,
name|C
argument_list|>
name|g
parameter_list|,
name|Function
argument_list|<
name|A
argument_list|,
name|?
extends|extends
name|B
argument_list|>
name|f
parameter_list|)
block|{
return|return
operator|new
name|FunctionComposition
argument_list|<
name|A
argument_list|,
name|B
argument_list|,
name|C
argument_list|>
argument_list|(
name|g
argument_list|,
name|f
argument_list|)
return|;
block|}
DECL|class|FunctionComposition
specifier|private
specifier|static
class|class
name|FunctionComposition
parameter_list|<
name|A
parameter_list|,
name|B
parameter_list|,
name|C
parameter_list|>
implements|implements
name|Function
argument_list|<
name|A
argument_list|,
name|C
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|g
specifier|private
specifier|final
name|Function
argument_list|<
name|B
argument_list|,
name|C
argument_list|>
name|g
decl_stmt|;
DECL|field|f
specifier|private
specifier|final
name|Function
argument_list|<
name|A
argument_list|,
name|?
extends|extends
name|B
argument_list|>
name|f
decl_stmt|;
DECL|method|FunctionComposition (Function<B, C> g, Function<A, ? extends B> f)
specifier|public
name|FunctionComposition
parameter_list|(
name|Function
argument_list|<
name|B
argument_list|,
name|C
argument_list|>
name|g
parameter_list|,
name|Function
argument_list|<
name|A
argument_list|,
name|?
extends|extends
name|B
argument_list|>
name|f
parameter_list|)
block|{
name|this
operator|.
name|g
operator|=
name|checkNotNull
argument_list|(
name|g
argument_list|)
expr_stmt|;
name|this
operator|.
name|f
operator|=
name|checkNotNull
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (A a)
specifier|public
name|C
name|apply
parameter_list|(
name|A
name|a
parameter_list|)
block|{
return|return
name|g
operator|.
name|apply
argument_list|(
name|f
operator|.
name|apply
argument_list|(
name|a
argument_list|)
argument_list|)
return|;
block|}
DECL|method|equals (@ullable Object obj)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|FunctionComposition
condition|)
block|{
name|FunctionComposition
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
name|that
init|=
operator|(
name|FunctionComposition
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
return|return
name|f
operator|.
name|equals
argument_list|(
name|that
operator|.
name|f
argument_list|)
operator|&&
name|g
operator|.
name|equals
argument_list|(
name|that
operator|.
name|g
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|f
operator|.
name|hashCode
argument_list|()
operator|^
name|g
operator|.
name|hashCode
argument_list|()
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
name|g
operator|.
name|toString
argument_list|()
operator|+
literal|"("
operator|+
name|f
operator|.
name|toString
argument_list|()
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
comment|/**    * Creates a function that returns the same boolean output as the given predicate for all inputs.    *    *<p>The returned function is<i>consistent with equals</i> (as documented at {@link    * Function#apply}) if and only if {@code predicate} is itself consistent with equals.    */
DECL|method|forPredicate (Predicate<T> predicate)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Function
argument_list|<
name|T
argument_list|,
name|Boolean
argument_list|>
name|forPredicate
parameter_list|(
name|Predicate
argument_list|<
name|T
argument_list|>
name|predicate
parameter_list|)
block|{
return|return
operator|new
name|PredicateFunction
argument_list|<
name|T
argument_list|>
argument_list|(
name|predicate
argument_list|)
return|;
block|}
comment|/** @see Functions#forPredicate */
DECL|class|PredicateFunction
specifier|private
specifier|static
class|class
name|PredicateFunction
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Function
argument_list|<
name|T
argument_list|,
name|Boolean
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|predicate
specifier|private
specifier|final
name|Predicate
argument_list|<
name|T
argument_list|>
name|predicate
decl_stmt|;
DECL|method|PredicateFunction (Predicate<T> predicate)
specifier|private
name|PredicateFunction
parameter_list|(
name|Predicate
argument_list|<
name|T
argument_list|>
name|predicate
parameter_list|)
block|{
name|this
operator|.
name|predicate
operator|=
name|checkNotNull
argument_list|(
name|predicate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (T t)
specifier|public
name|Boolean
name|apply
parameter_list|(
name|T
name|t
parameter_list|)
block|{
return|return
name|predicate
operator|.
name|apply
argument_list|(
name|t
argument_list|)
return|;
block|}
DECL|method|equals (@ullable Object obj)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|PredicateFunction
condition|)
block|{
name|PredicateFunction
argument_list|<
name|?
argument_list|>
name|that
init|=
operator|(
name|PredicateFunction
argument_list|<
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
return|return
name|predicate
operator|.
name|equals
argument_list|(
name|that
operator|.
name|predicate
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|predicate
operator|.
name|hashCode
argument_list|()
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
literal|"forPredicate("
operator|+
name|predicate
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
comment|/**    * Creates a function that returns {@code value} for any input.    *    * @param value the constant value for the function to return    * @return a function that always returns {@code value}    */
DECL|method|constant (@ullable E value)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Function
argument_list|<
name|Object
argument_list|,
name|E
argument_list|>
name|constant
parameter_list|(
annotation|@
name|Nullable
name|E
name|value
parameter_list|)
block|{
return|return
operator|new
name|ConstantFunction
argument_list|<
name|E
argument_list|>
argument_list|(
name|value
argument_list|)
return|;
block|}
DECL|class|ConstantFunction
specifier|private
specifier|static
class|class
name|ConstantFunction
parameter_list|<
name|E
parameter_list|>
implements|implements
name|Function
argument_list|<
name|Object
argument_list|,
name|E
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|value
specifier|private
specifier|final
name|E
name|value
decl_stmt|;
DECL|method|ConstantFunction (@ullable E value)
specifier|public
name|ConstantFunction
parameter_list|(
annotation|@
name|Nullable
name|E
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (@ullable Object from)
specifier|public
name|E
name|apply
parameter_list|(
annotation|@
name|Nullable
name|Object
name|from
parameter_list|)
block|{
return|return
name|value
return|;
block|}
DECL|method|equals (@ullable Object obj)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|ConstantFunction
condition|)
block|{
name|ConstantFunction
argument_list|<
name|?
argument_list|>
name|that
init|=
operator|(
name|ConstantFunction
argument_list|<
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
return|return
name|Objects
operator|.
name|equal
argument_list|(
name|value
argument_list|,
name|that
operator|.
name|value
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
operator|(
name|value
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|value
operator|.
name|hashCode
argument_list|()
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
literal|"constant("
operator|+
name|value
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
block|}
end_class

end_unit

