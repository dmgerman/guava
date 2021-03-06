begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the  * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  * express or implied. See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|function
operator|.
name|Function
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|ToIntFunction
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collector
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
comment|/**  * "Overrides" the {@link ImmutableMultiset} static methods that lack {@link  * ImmutableSortedMultiset} equivalents with deprecated, exception-throwing versions. This prevents  * accidents like the following:  *  *<pre>{@code  * List<Object> objects = ...;  * // Sort them:  * Set<Object> sorted = ImmutableSortedMultiset.copyOf(objects);  * // BAD CODE! The returned multiset is actually an unsorted ImmutableMultiset!  * }</pre>  *  *<p>While we could put the overrides in {@link ImmutableSortedMultiset} itself, it seems clearer  * to separate these "do not call" methods from those intended for normal use.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|ImmutableSortedMultisetFauxverideShim
specifier|abstract
class|class
name|ImmutableSortedMultisetFauxverideShim
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
block|{
comment|/**    * Not supported. Use {@link ImmutableSortedMultiset#toImmutableSortedMultiset} instead. This    * method exists only to hide {@link ImmutableMultiset#toImmutableMultiset} from consumers of    * {@code ImmutableSortedMultiset}.    *    * @throws UnsupportedOperationException always    * @deprecated Use {@link ImmutableSortedMultiset#toImmutableSortedMultiset}.    * @since 21.0    */
annotation|@
name|DoNotCall
argument_list|(
literal|"Use toImmutableSortedMultiset."
argument_list|)
annotation|@
name|Deprecated
DECL|method|toImmutableMultiset ()
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Collector
argument_list|<
name|E
argument_list|,
name|?
argument_list|,
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
argument_list|>
name|toImmutableMultiset
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Not supported. Use {@link ImmutableSortedMultiset#toImmutableSortedMultiset} instead. This    * method exists only to hide {@link ImmutableMultiset#toImmutableMultiset} from consumers of    * {@code ImmutableSortedMultiset}.    *    * @throws UnsupportedOperationException always    * @deprecated Use {@link ImmutableSortedMultiset#toImmutableSortedMultiset}.    * @since 22.0    */
annotation|@
name|DoNotCall
argument_list|(
literal|"Use toImmutableSortedMultiset."
argument_list|)
annotation|@
name|Deprecated
specifier|public
specifier|static
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|,
name|E
operator|>
DECL|method|toImmutableMultiset ( Function<? super T, ? extends E> elementFunction, ToIntFunction<? super T> countFunction)
name|Collector
argument_list|<
name|T
argument_list|,
name|?
argument_list|,
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
argument_list|>
name|toImmutableMultiset
argument_list|(
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|E
argument_list|>
name|elementFunction
operator|,
name|ToIntFunction
argument_list|<
name|?
super|super
name|T
argument_list|>
name|countFunction
argument_list|)
block|{
throw|throw
argument_list|new
name|UnsupportedOperationException
argument_list|()
block|;   }
comment|/**    * Not supported. Use {@link ImmutableSortedMultiset#naturalOrder}, which offers better    * type-safety, instead. This method exists only to hide {@link ImmutableMultiset#builder} from    * consumers of {@code ImmutableSortedMultiset}.    *    * @throws UnsupportedOperationException always    * @deprecated Use {@link ImmutableSortedMultiset#naturalOrder}, which offers better type-safety.    */
expr|@
name|DoNotCall
argument_list|(
literal|"Use naturalOrder."
argument_list|)
expr|@
name|Deprecated
DECL|method|builder ()
specifier|public
specifier|static
operator|<
name|E
operator|>
name|ImmutableSortedMultiset
operator|.
name|Builder
argument_list|<
name|E
argument_list|>
name|builder
argument_list|()
block|{
throw|throw
argument_list|new
name|UnsupportedOperationException
argument_list|()
block|;   }
comment|/**    * Not supported.<b>You are attempting to create a multiset that may contain a non-{@code    * Comparable} element.</b> Proper calls will resolve to the version in {@code    * ImmutableSortedMultiset}, not this dummy version.    *    * @throws UnsupportedOperationException always    * @deprecated<b>Pass a parameter of type {@code Comparable} to use {@link    *     ImmutableSortedMultiset#of(Comparable)}.</b>    */
expr|@
name|DoNotCall
argument_list|(
literal|"Elements must be Comparable. (Or, pass a Comparator to orderedBy or copyOf.)"
argument_list|)
expr|@
name|Deprecated
DECL|method|of (E element)
specifier|public
specifier|static
operator|<
name|E
operator|>
name|ImmutableSortedMultiset
argument_list|<
name|E
argument_list|>
name|of
argument_list|(
name|E
name|element
argument_list|)
block|{
throw|throw
argument_list|new
name|UnsupportedOperationException
argument_list|()
block|;   }
comment|/**    * Not supported.<b>You are attempting to create a multiset that may contain a non-{@code    * Comparable} element.</b> Proper calls will resolve to the version in {@code    * ImmutableSortedMultiset}, not this dummy version.    *    * @throws UnsupportedOperationException always    * @deprecated<b>Pass the parameters of type {@code Comparable} to use {@link    *     ImmutableSortedMultiset#of(Comparable, Comparable)}.</b>    */
expr|@
name|DoNotCall
argument_list|(
literal|"Elements must be Comparable. (Or, pass a Comparator to orderedBy or copyOf.)"
argument_list|)
expr|@
name|Deprecated
DECL|method|of (E e1, E e2)
specifier|public
specifier|static
operator|<
name|E
operator|>
name|ImmutableSortedMultiset
argument_list|<
name|E
argument_list|>
name|of
argument_list|(
name|E
name|e1
argument_list|,
name|E
name|e2
argument_list|)
block|{
throw|throw
argument_list|new
name|UnsupportedOperationException
argument_list|()
block|;   }
comment|/**    * Not supported.<b>You are attempting to create a multiset that may contain a non-{@code    * Comparable} element.</b> Proper calls will resolve to the version in {@code    * ImmutableSortedMultiset}, not this dummy version.    *    * @throws UnsupportedOperationException always    * @deprecated<b>Pass the parameters of type {@code Comparable} to use {@link    *     ImmutableSortedMultiset#of(Comparable, Comparable, Comparable)}.</b>    */
expr|@
name|DoNotCall
argument_list|(
literal|"Elements must be Comparable. (Or, pass a Comparator to orderedBy or copyOf.)"
argument_list|)
expr|@
name|Deprecated
DECL|method|of (E e1, E e2, E e3)
specifier|public
specifier|static
operator|<
name|E
operator|>
name|ImmutableSortedMultiset
argument_list|<
name|E
argument_list|>
name|of
argument_list|(
name|E
name|e1
argument_list|,
name|E
name|e2
argument_list|,
name|E
name|e3
argument_list|)
block|{
throw|throw
argument_list|new
name|UnsupportedOperationException
argument_list|()
block|;   }
comment|/**    * Not supported.<b>You are attempting to create a multiset that may contain a non-{@code    * Comparable} element.</b> Proper calls will resolve to the version in {@code    * ImmutableSortedMultiset}, not this dummy version.    *    * @throws UnsupportedOperationException always    * @deprecated<b>Pass the parameters of type {@code Comparable} to use {@link    *     ImmutableSortedMultiset#of(Comparable, Comparable, Comparable, Comparable)}.</b>    */
expr|@
name|DoNotCall
argument_list|(
literal|"Elements must be Comparable. (Or, pass a Comparator to orderedBy or copyOf.)"
argument_list|)
expr|@
name|Deprecated
DECL|method|of (E e1, E e2, E e3, E e4)
specifier|public
specifier|static
operator|<
name|E
operator|>
name|ImmutableSortedMultiset
argument_list|<
name|E
argument_list|>
name|of
argument_list|(
name|E
name|e1
argument_list|,
name|E
name|e2
argument_list|,
name|E
name|e3
argument_list|,
name|E
name|e4
argument_list|)
block|{
throw|throw
argument_list|new
name|UnsupportedOperationException
argument_list|()
block|;   }
comment|/**    * Not supported.<b>You are attempting to create a multiset that may contain a non-{@code    * Comparable} element.</b> Proper calls will resolve to the version in {@code    * ImmutableSortedMultiset}, not this dummy version.    *    * @throws UnsupportedOperationException always    * @deprecated<b>Pass the parameters of type {@code Comparable} to use {@link    *     ImmutableSortedMultiset#of(Comparable, Comparable, Comparable, Comparable, Comparable)} .    *</b>    */
expr|@
name|DoNotCall
argument_list|(
literal|"Elements must be Comparable. (Or, pass a Comparator to orderedBy or copyOf.)"
argument_list|)
expr|@
name|Deprecated
DECL|method|of (E e1, E e2, E e3, E e4, E e5)
specifier|public
specifier|static
operator|<
name|E
operator|>
name|ImmutableSortedMultiset
argument_list|<
name|E
argument_list|>
name|of
argument_list|(
name|E
name|e1
argument_list|,
name|E
name|e2
argument_list|,
name|E
name|e3
argument_list|,
name|E
name|e4
argument_list|,
name|E
name|e5
argument_list|)
block|{
throw|throw
argument_list|new
name|UnsupportedOperationException
argument_list|()
block|;   }
comment|/**    * Not supported.<b>You are attempting to create a multiset that may contain a non-{@code    * Comparable} element.</b> Proper calls will resolve to the version in {@code    * ImmutableSortedMultiset}, not this dummy version.    *    * @throws UnsupportedOperationException always    * @deprecated<b>Pass the parameters of type {@code Comparable} to use {@link    *     ImmutableSortedMultiset#of(Comparable, Comparable, Comparable, Comparable, Comparable,    *     Comparable, Comparable...)} .</b>    */
expr|@
name|DoNotCall
argument_list|(
literal|"Elements must be Comparable. (Or, pass a Comparator to orderedBy or copyOf.)"
argument_list|)
expr|@
name|Deprecated
DECL|method|of ( E e1, E e2, E e3, E e4, E e5, E e6, E... remaining)
specifier|public
specifier|static
operator|<
name|E
operator|>
name|ImmutableSortedMultiset
argument_list|<
name|E
argument_list|>
name|of
argument_list|(
name|E
name|e1
argument_list|,
name|E
name|e2
argument_list|,
name|E
name|e3
argument_list|,
name|E
name|e4
argument_list|,
name|E
name|e5
argument_list|,
name|E
name|e6
argument_list|,
name|E
operator|...
name|remaining
argument_list|)
block|{
throw|throw
argument_list|new
name|UnsupportedOperationException
argument_list|()
block|;   }
comment|/**    * Not supported.<b>You are attempting to create a multiset that may contain non-{@code    * Comparable} elements.</b> Proper calls will resolve to the version in {@code    * ImmutableSortedMultiset}, not this dummy version.    *    * @throws UnsupportedOperationException always    * @deprecated<b>Pass parameters of type {@code Comparable} to use {@link    *     ImmutableSortedMultiset#copyOf(Comparable[])}.</b>    */
expr|@
name|DoNotCall
argument_list|(
literal|"Elements must be Comparable. (Or, pass a Comparator to orderedBy or copyOf.)"
argument_list|)
expr|@
name|Deprecated
DECL|method|copyOf (E[] elements)
specifier|public
specifier|static
operator|<
name|E
operator|>
name|ImmutableSortedMultiset
argument_list|<
name|E
argument_list|>
name|copyOf
argument_list|(
name|E
index|[]
name|elements
argument_list|)
block|{
throw|throw
argument_list|new
name|UnsupportedOperationException
argument_list|()
block|;   }
comment|/*    * We would like to include an unsupported "<E> copyOf(Iterable<E>)" here, providing only the    * properly typed "<E extends Comparable<E>> copyOf(Iterable<E>)" in ImmutableSortedMultiset (and    * likewise for the Iterator equivalent). However, due to a change in Sun's interpretation of the    * JLS (as described at http://bugs.sun.com/view_bug.do?bug_id=6182950), the OpenJDK 7 compiler    * available as of this writing rejects our attempts. To maintain compatibility with that version    * and with any other compilers that interpret the JLS similarly, there is no definition of    * copyOf() here, and the definition in ImmutableSortedMultiset matches that in    * ImmutableMultiset.    *    * The result is that ImmutableSortedMultiset.copyOf() may be called on non-Comparable elements.    * We have not discovered a better solution. In retrospect, the static factory methods should    * have gone in a separate class so that ImmutableSortedMultiset wouldn't "inherit"    * too-permissive factory methods from ImmutableMultiset.    */
block|}
end_class

end_unit

