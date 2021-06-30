begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Tables
operator|.
name|AbstractCell
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|BinaryOperator
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
name|Supplier
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
comment|/** Collectors utilities for {@code common.collect.Table} internals. */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|TableCollectors
specifier|final
class|class
name|TableCollectors
block|{
specifier|static
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|,
name|R
operator|,
name|C
operator|,
name|V
operator|>
DECL|method|toImmutableTable ( Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction)
name|Collector
argument_list|<
name|T
argument_list|,
name|?
argument_list|,
name|ImmutableTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|toImmutableTable
argument_list|(
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|R
argument_list|>
name|rowFunction
operator|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|C
argument_list|>
name|columnFunction
operator|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|valueFunction
argument_list|)
block|{
name|checkNotNull
argument_list|(
name|rowFunction
argument_list|,
literal|"rowFunction"
argument_list|)
block|;
name|checkNotNull
argument_list|(
name|columnFunction
argument_list|,
literal|"columnFunction"
argument_list|)
block|;
name|checkNotNull
argument_list|(
name|valueFunction
argument_list|,
literal|"valueFunction"
argument_list|)
block|;
return|return
name|Collector
operator|.
name|of
argument_list|(
operator|(
name|Supplier
argument_list|<
name|ImmutableTable
operator|.
name|Builder
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
operator|)
name|ImmutableTable
operator|.
name|Builder
operator|::
operator|new
argument_list|,
parameter_list|(
name|builder
parameter_list|,
name|t
parameter_list|)
lambda|->
name|builder
operator|.
name|put
argument_list|(
name|rowFunction
operator|.
name|apply
argument_list|(
name|t
argument_list|)
argument_list|,
name|columnFunction
operator|.
name|apply
argument_list|(
name|t
argument_list|)
argument_list|,
name|valueFunction
operator|.
name|apply
argument_list|(
name|t
argument_list|)
argument_list|)
argument_list|,
name|ImmutableTable
operator|.
name|Builder
operator|::
name|combine
argument_list|,
name|ImmutableTable
operator|.
name|Builder
operator|::
name|build
argument_list|)
return|;
block|}
specifier|static
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|,
name|R
operator|,
name|C
operator|,
name|V
operator|>
DECL|method|toImmutableTable ( Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction)
name|Collector
argument_list|<
name|T
argument_list|,
name|?
argument_list|,
name|ImmutableTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|toImmutableTable
argument_list|(
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|R
argument_list|>
name|rowFunction
operator|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|C
argument_list|>
name|columnFunction
operator|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|valueFunction
operator|,
name|BinaryOperator
argument_list|<
name|V
argument_list|>
name|mergeFunction
argument_list|)
block|{
name|checkNotNull
argument_list|(
name|rowFunction
argument_list|,
literal|"rowFunction"
argument_list|)
block|;
name|checkNotNull
argument_list|(
name|columnFunction
argument_list|,
literal|"columnFunction"
argument_list|)
block|;
name|checkNotNull
argument_list|(
name|valueFunction
argument_list|,
literal|"valueFunction"
argument_list|)
block|;
name|checkNotNull
argument_list|(
name|mergeFunction
argument_list|,
literal|"mergeFunction"
argument_list|)
block|;
comment|/*      * No mutable Table exactly matches the insertion order behavior of ImmutableTable.Builder, but      * the Builder can't efficiently support merging of duplicate values.  Getting around this      * requires some work.      */
return|return
name|Collector
operator|.
name|of
argument_list|(
parameter_list|()
lambda|->
operator|new
name|ImmutableTableCollectorState
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|()
comment|/* GWT isn't currently playing nicely with constructor references? */
argument_list|,
parameter_list|(
name|state
parameter_list|,
name|input
parameter_list|)
lambda|->
name|state
operator|.
name|put
argument_list|(
name|rowFunction
operator|.
name|apply
argument_list|(
name|input
argument_list|)
argument_list|,
name|columnFunction
operator|.
name|apply
argument_list|(
name|input
argument_list|)
argument_list|,
name|valueFunction
operator|.
name|apply
argument_list|(
name|input
argument_list|)
argument_list|,
name|mergeFunction
argument_list|)
argument_list|,
parameter_list|(
name|s1
parameter_list|,
name|s2
parameter_list|)
lambda|->
name|s1
operator|.
name|combine
argument_list|(
name|s2
argument_list|,
name|mergeFunction
argument_list|)
argument_list|,
name|state
lambda|->
name|state
operator|.
name|toTable
argument_list|()
argument_list|)
return|;
block|}
specifier|static
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|,
name|R
expr|extends @
name|Nullable
name|Object
operator|,
name|C
expr|extends @
name|Nullable
name|Object
operator|,
name|V
expr|extends @
name|Nullable
name|Object
operator|,
name|I
expr|extends
name|Table
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
operator|>
DECL|method|toTable ( java.util.function.Function<? super T, ? extends R> rowFunction, java.util.function.Function<? super T, ? extends C> columnFunction, java.util.function.Function<? super T, ? extends V> valueFunction, java.util.function.Supplier<I> tableSupplier)
name|Collector
argument_list|<
name|T
argument_list|,
name|?
argument_list|,
name|I
argument_list|>
name|toTable
argument_list|(
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|R
argument_list|>
name|rowFunction
operator|,
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|C
argument_list|>
name|columnFunction
operator|,
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|valueFunction
operator|,
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
argument_list|<
name|I
argument_list|>
name|tableSupplier
argument_list|)
block|{
return|return
name|toTable
argument_list|(
name|rowFunction
argument_list|,
name|columnFunction
argument_list|,
name|valueFunction
argument_list|,
parameter_list|(
name|v1
parameter_list|,
name|v2
parameter_list|)
lambda|->
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Conflicting values "
operator|+
name|v1
operator|+
literal|" and "
operator|+
name|v2
argument_list|)
throw|;
block|}
operator|,
name|tableSupplier
block|)
class|;
end_class

begin_expr_stmt
unit|}    static
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|,
name|R
expr|extends @
name|Nullable
name|Object
operator|,
name|C
expr|extends @
name|Nullable
name|Object
operator|,
name|V
expr|extends @
name|Nullable
name|Object
operator|,
name|I
expr|extends
name|Table
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
operator|>
DECL|method|toTable ( java.util.function.Function<? super T, ? extends R> rowFunction, java.util.function.Function<? super T, ? extends C> columnFunction, java.util.function.Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction, java.util.function.Supplier<I> tableSupplier)
name|Collector
argument_list|<
name|T
argument_list|,
name|?
argument_list|,
name|I
argument_list|>
name|toTable
argument_list|(
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|R
argument_list|>
name|rowFunction
operator|,
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|C
argument_list|>
name|columnFunction
operator|,
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|valueFunction
operator|,
name|BinaryOperator
argument_list|<
name|V
argument_list|>
name|mergeFunction
operator|,
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
argument_list|<
name|I
argument_list|>
name|tableSupplier
argument_list|)
block|{
name|checkNotNull
argument_list|(
name|rowFunction
argument_list|)
block|;
name|checkNotNull
argument_list|(
name|columnFunction
argument_list|)
block|;
name|checkNotNull
argument_list|(
name|valueFunction
argument_list|)
block|;
name|checkNotNull
argument_list|(
name|mergeFunction
argument_list|)
block|;
name|checkNotNull
argument_list|(
name|tableSupplier
argument_list|)
block|;
return|return
name|Collector
operator|.
name|of
argument_list|(
name|tableSupplier
argument_list|,
parameter_list|(
name|table
parameter_list|,
name|input
parameter_list|)
lambda|->
name|mergeTables
argument_list|(
name|table
argument_list|,
name|rowFunction
operator|.
name|apply
argument_list|(
name|input
argument_list|)
argument_list|,
name|columnFunction
operator|.
name|apply
argument_list|(
name|input
argument_list|)
argument_list|,
name|valueFunction
operator|.
name|apply
argument_list|(
name|input
argument_list|)
argument_list|,
name|mergeFunction
argument_list|)
argument_list|,
parameter_list|(
name|table1
parameter_list|,
name|table2
parameter_list|)
lambda|->
block|{
lambda|for (Table.Cell<R
argument_list|,
name|C
argument_list|,
name|V
operator|>
name|cell2
operator|:
name|table2
operator|.
name|cellSet
argument_list|()
argument_list|)
block|{
name|mergeTables
argument_list|(
name|table1
argument_list|,
name|cell2
operator|.
name|getRowKey
argument_list|()
argument_list|,
name|cell2
operator|.
name|getColumnKey
argument_list|()
argument_list|,
name|cell2
operator|.
name|getValue
argument_list|()
argument_list|,
name|mergeFunction
argument_list|)
block|;           }
return|return
name|table1
return|;
block|}
end_expr_stmt

begin_empty_stmt
unit|)
empty_stmt|;
end_empty_stmt

begin_class
unit|}    private
DECL|class|ImmutableTableCollectorState
specifier|static
specifier|final
class|class
name|ImmutableTableCollectorState
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
block|{
DECL|field|insertionOrder
specifier|final
name|List
argument_list|<
name|MutableCell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|insertionOrder
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|table
specifier|final
name|Table
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|MutableCell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|table
init|=
name|HashBasedTable
operator|.
name|create
argument_list|()
decl_stmt|;
DECL|method|put (R row, C column, V value, BinaryOperator<V> merger)
name|void
name|put
parameter_list|(
name|R
name|row
parameter_list|,
name|C
name|column
parameter_list|,
name|V
name|value
parameter_list|,
name|BinaryOperator
argument_list|<
name|V
argument_list|>
name|merger
parameter_list|)
block|{
name|MutableCell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|oldCell
init|=
name|table
operator|.
name|get
argument_list|(
name|row
argument_list|,
name|column
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldCell
operator|==
literal|null
condition|)
block|{
name|MutableCell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|cell
init|=
operator|new
name|MutableCell
argument_list|<>
argument_list|(
name|row
argument_list|,
name|column
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|insertionOrder
operator|.
name|add
argument_list|(
name|cell
argument_list|)
expr_stmt|;
name|table
operator|.
name|put
argument_list|(
name|row
argument_list|,
name|column
argument_list|,
name|cell
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|oldCell
operator|.
name|merge
argument_list|(
name|value
argument_list|,
name|merger
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|combine ( ImmutableTableCollectorState<R, C, V> other, BinaryOperator<V> merger)
name|ImmutableTableCollectorState
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|combine
parameter_list|(
name|ImmutableTableCollectorState
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|other
parameter_list|,
name|BinaryOperator
argument_list|<
name|V
argument_list|>
name|merger
parameter_list|)
block|{
for|for
control|(
name|MutableCell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|cell
range|:
name|other
operator|.
name|insertionOrder
control|)
block|{
name|put
argument_list|(
name|cell
operator|.
name|getRowKey
argument_list|()
argument_list|,
name|cell
operator|.
name|getColumnKey
argument_list|()
argument_list|,
name|cell
operator|.
name|getValue
argument_list|()
argument_list|,
name|merger
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|toTable ()
name|ImmutableTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|toTable
parameter_list|()
block|{
return|return
name|ImmutableTable
operator|.
name|copyOf
argument_list|(
name|insertionOrder
argument_list|)
return|;
block|}
block|}
end_class

begin_class
DECL|class|MutableCell
specifier|private
specifier|static
specifier|final
class|class
name|MutableCell
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractCell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
block|{
DECL|field|row
specifier|private
specifier|final
name|R
name|row
decl_stmt|;
DECL|field|column
specifier|private
specifier|final
name|C
name|column
decl_stmt|;
DECL|field|value
specifier|private
name|V
name|value
decl_stmt|;
DECL|method|MutableCell (R row, C column, V value)
name|MutableCell
parameter_list|(
name|R
name|row
parameter_list|,
name|C
name|column
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|this
operator|.
name|row
operator|=
name|checkNotNull
argument_list|(
name|row
argument_list|,
literal|"row"
argument_list|)
expr_stmt|;
name|this
operator|.
name|column
operator|=
name|checkNotNull
argument_list|(
name|column
argument_list|,
literal|"column"
argument_list|)
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|checkNotNull
argument_list|(
name|value
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getRowKey ()
specifier|public
name|R
name|getRowKey
parameter_list|()
block|{
return|return
name|row
return|;
block|}
annotation|@
name|Override
DECL|method|getColumnKey ()
specifier|public
name|C
name|getColumnKey
parameter_list|()
block|{
return|return
name|column
return|;
block|}
annotation|@
name|Override
DECL|method|getValue ()
specifier|public
name|V
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
DECL|method|merge (V value, BinaryOperator<V> mergeFunction)
name|void
name|merge
parameter_list|(
name|V
name|value
parameter_list|,
name|BinaryOperator
argument_list|<
name|V
argument_list|>
name|mergeFunction
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|value
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|checkNotNull
argument_list|(
name|mergeFunction
operator|.
name|apply
argument_list|(
name|this
operator|.
name|value
argument_list|,
name|value
argument_list|)
argument_list|,
literal|"mergeFunction.apply"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_expr_stmt
specifier|private
specifier|static
operator|<
name|R
expr|extends @
name|Nullable
name|Object
operator|,
name|C
expr|extends @
name|Nullable
name|Object
operator|,
name|V
expr|extends @
name|Nullable
name|Object
operator|>
DECL|method|mergeTables ( Table<R, C, V> table, @ParametricNullness R row, @ParametricNullness C column, @ParametricNullness V value, BinaryOperator<V> mergeFunction)
name|void
name|mergeTables
argument_list|(
name|Table
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|table
argument_list|,
annotation|@
name|ParametricNullness
name|R
name|row
argument_list|,
annotation|@
name|ParametricNullness
name|C
name|column
argument_list|,
annotation|@
name|ParametricNullness
name|V
name|value
argument_list|,
name|BinaryOperator
argument_list|<
name|V
argument_list|>
name|mergeFunction
argument_list|)
block|{
name|checkNotNull
argument_list|(
name|value
argument_list|)
block|;
name|V
name|oldValue
operator|=
name|table
operator|.
name|get
argument_list|(
name|row
argument_list|,
name|column
argument_list|)
block|;
if|if
condition|(
name|oldValue
operator|==
literal|null
condition|)
block|{
name|table
operator|.
name|put
argument_list|(
name|row
argument_list|,
name|column
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
end_expr_stmt

begin_else
else|else
block|{
name|V
name|newValue
init|=
name|mergeFunction
operator|.
name|apply
argument_list|(
name|oldValue
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|newValue
operator|==
literal|null
condition|)
block|{
name|table
operator|.
name|remove
argument_list|(
name|row
argument_list|,
name|column
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|table
operator|.
name|put
argument_list|(
name|row
argument_list|,
name|column
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
block|}
block|}
end_else

begin_expr_stmt
unit|}    private
DECL|method|TableCollectors ()
name|TableCollectors
argument_list|()
block|{}
end_expr_stmt

unit|}
end_unit

