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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Objects
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
name|CanIgnoreReturnValue
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
name|CompatibleWith
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
name|DoNotMock
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|javax
operator|.
name|annotation
operator|.
name|CheckForNull
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
comment|/**  * A collection that associates an ordered pair of keys, called a row key and a column key, with a  * single value. A table may be sparse, with only a small fraction of row key / column key pairs  * possessing a corresponding value.  *  *<p>The mappings corresponding to a given row key may be viewed as a {@link Map} whose keys are  * the columns. The reverse is also available, associating a column with a row key / value map. Note  * that, in some implementations, data access by column key may have fewer supported operations or  * worse performance than data access by row key.  *  *<p>The methods returning collections or maps always return views of the underlying table.  * Updating the table can change the contents of those collections, and updating the collections  * will change the table.  *  *<p>All methods that modify the table are optional, and the views returned by the table may or may  * not be modifiable. When modification isn't supported, those methods will throw an {@link  * UnsupportedOperationException}.  *  *<p>See the Guava User Guide article on<a href=  * "https://github.com/google/guava/wiki/NewCollectionTypesExplained#table"> {@code Table}</a>.  *  * @author Jared Levy  * @param<R> the type of the table row keys  * @param<C> the type of the table column keys  * @param<V> the type of the mapped values  * @since 7.0  */
end_comment

begin_annotation
annotation|@
name|DoNotMock
argument_list|(
literal|"Use ImmutableTable, HashBasedTable, or another implementation"
argument_list|)
end_annotation

begin_annotation
annotation|@
name|GwtCompatible
end_annotation

begin_annotation
annotation|@
name|ElementTypesAreNonnullByDefault
end_annotation

begin_expr_stmt
DECL|interface|Table
specifier|public
expr|interface
name|Table
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
block|{
comment|// TODO(jlevy): Consider adding methods similar to ConcurrentMap methods.
comment|// Accessors
comment|/**    * Returns {@code true} if the table contains a mapping with the specified row and column keys.    *    * @param rowKey key of row to search for    * @param columnKey key of column to search for    */
DECL|method|contains ( @ompatibleWithR) @heckForNull Object rowKey, @CompatibleWith(R) @CheckForNull Object columnKey)
name|boolean
name|contains
argument_list|(
annotation|@
name|CompatibleWith
argument_list|(
literal|"R"
argument_list|)
expr|@
name|CheckForNull
name|Object
name|rowKey
argument_list|,
annotation|@
name|CompatibleWith
argument_list|(
literal|"C"
argument_list|)
expr|@
name|CheckForNull
name|Object
name|columnKey
argument_list|)
block|;
comment|/**    * Returns {@code true} if the table contains a mapping with the specified row key.    *    * @param rowKey key of row to search for    */
DECL|method|containsRow (@ompatibleWithR) @heckForNull Object rowKey)
name|boolean
name|containsRow
argument_list|(
annotation|@
name|CompatibleWith
argument_list|(
literal|"R"
argument_list|)
expr|@
name|CheckForNull
name|Object
name|rowKey
argument_list|)
block|;
comment|/**    * Returns {@code true} if the table contains a mapping with the specified column.    *    * @param columnKey key of column to search for    */
DECL|method|containsColumn (@ompatibleWithR) @heckForNull Object columnKey)
name|boolean
name|containsColumn
argument_list|(
annotation|@
name|CompatibleWith
argument_list|(
literal|"C"
argument_list|)
expr|@
name|CheckForNull
name|Object
name|columnKey
argument_list|)
block|;
comment|/**    * Returns {@code true} if the table contains a mapping with the specified value.    *    * @param value value to search for    */
DECL|method|containsValue (@ompatibleWithR) @heckForNull Object value)
name|boolean
name|containsValue
argument_list|(
annotation|@
name|CompatibleWith
argument_list|(
literal|"V"
argument_list|)
expr|@
name|CheckForNull
name|Object
name|value
argument_list|)
block|;
comment|/**    * Returns the value corresponding to the given row and column keys, or {@code null} if no such    * mapping exists.    *    * @param rowKey key of row to search for    * @param columnKey key of column to search for    */
block|@
name|CheckForNull
DECL|method|get ( @ompatibleWithR) @heckForNull Object rowKey, @CompatibleWith(R) @CheckForNull Object columnKey)
name|V
name|get
argument_list|(
annotation|@
name|CompatibleWith
argument_list|(
literal|"R"
argument_list|)
expr|@
name|CheckForNull
name|Object
name|rowKey
argument_list|,
annotation|@
name|CompatibleWith
argument_list|(
literal|"C"
argument_list|)
expr|@
name|CheckForNull
name|Object
name|columnKey
argument_list|)
block|;
comment|/** Returns {@code true} if the table contains no mappings. */
DECL|method|isEmpty ()
name|boolean
name|isEmpty
argument_list|()
block|;
comment|/** Returns the number of row key / column key / value mappings in the table. */
DECL|method|size ()
name|int
name|size
argument_list|()
block|;
comment|/**    * Compares the specified object with this table for equality. Two tables are equal when their    * cell views, as returned by {@link #cellSet}, are equal.    */
block|@
name|Override
DECL|method|equals (@heckForNull Object obj)
name|boolean
name|equals
argument_list|(
annotation|@
name|CheckForNull
name|Object
name|obj
argument_list|)
block|;
comment|/**    * Returns the hash code for this table. The hash code of a table is defined as the hash code of    * its cell view, as returned by {@link #cellSet}.    */
block|@
name|Override
DECL|method|hashCode ()
name|int
name|hashCode
argument_list|()
block|;
comment|// Mutators
comment|/** Removes all mappings from the table. */
DECL|method|clear ()
name|void
name|clear
argument_list|()
block|;
comment|/**    * Associates the specified value with the specified keys. If the table already contained a    * mapping for those keys, the old value is replaced with the specified value.    *    * @param rowKey row key that the value should be associated with    * @param columnKey column key that the value should be associated with    * @param value value to be associated with the specified keys    * @return the value previously associated with the keys, or {@code null} if no mapping existed    *     for the keys    */
block|@
name|CanIgnoreReturnValue
expr|@
name|CheckForNull
DECL|method|put (@arametricNullness R rowKey, @ParametricNullness C columnKey, @ParametricNullness V value)
name|V
name|put
argument_list|(
annotation|@
name|ParametricNullness
name|R
name|rowKey
argument_list|,
annotation|@
name|ParametricNullness
name|C
name|columnKey
argument_list|,
annotation|@
name|ParametricNullness
name|V
name|value
argument_list|)
block|;
comment|/**    * Copies all mappings from the specified table to this table. The effect is equivalent to calling    * {@link #put} with each row key / column key / value mapping in {@code table}.    *    * @param table the table to add to this table    */
DECL|method|putAll (Table<? extends R, ? extends C, ? extends V> table)
name|void
name|putAll
argument_list|(
name|Table
argument_list|<
name|?
extends|extends
name|R
argument_list|,
name|?
extends|extends
name|C
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|table
argument_list|)
block|;
comment|/**    * Removes the mapping, if any, associated with the given keys.    *    * @param rowKey row key of mapping to be removed    * @param columnKey column key of mapping to be removed    * @return the value previously associated with the keys, or {@code null} if no such value existed    */
block|@
name|CanIgnoreReturnValue
expr|@
name|CheckForNull
DECL|method|remove ( @ompatibleWithR) @heckForNull Object rowKey, @CompatibleWith(R) @CheckForNull Object columnKey)
name|V
name|remove
argument_list|(
annotation|@
name|CompatibleWith
argument_list|(
literal|"R"
argument_list|)
expr|@
name|CheckForNull
name|Object
name|rowKey
argument_list|,
annotation|@
name|CompatibleWith
argument_list|(
literal|"C"
argument_list|)
expr|@
name|CheckForNull
name|Object
name|columnKey
argument_list|)
block|;
comment|// Views
comment|/**    * Returns a view of all mappings that have the given row key. For each row key / column key /    * value mapping in the table with that row key, the returned map associates the column key with    * the value. If no mappings in the table have the provided row key, an empty map is returned.    *    *<p>Changes to the returned map will update the underlying table, and vice versa.    *    * @param rowKey key of row to search for in the table    * @return the corresponding map from column keys to values    */
DECL|method|row (@arametricNullness R rowKey)
name|Map
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
name|row
argument_list|(
annotation|@
name|ParametricNullness
name|R
name|rowKey
argument_list|)
block|;
comment|/**    * Returns a view of all mappings that have the given column key. For each row key / column key /    * value mapping in the table with that column key, the returned map associates the row key with    * the value. If no mappings in the table have the provided column key, an empty map is returned.    *    *<p>Changes to the returned map will update the underlying table, and vice versa.    *    * @param columnKey key of column to search for in the table    * @return the corresponding map from row keys to values    */
DECL|method|column (@arametricNullness C columnKey)
name|Map
argument_list|<
name|R
argument_list|,
name|V
argument_list|>
name|column
argument_list|(
annotation|@
name|ParametricNullness
name|C
name|columnKey
argument_list|)
block|;
comment|/**    * Returns a set of all row key / column key / value triplets. Changes to the returned set will    * update the underlying table, and vice versa. The cell set does not support the {@code add} or    * {@code addAll} methods.    *    * @return set of table cells consisting of row key / column key / value triplets    */
DECL|method|cellSet ()
name|Set
argument_list|<
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|cellSet
argument_list|()
block|;
comment|/**    * Returns a set of row keys that have one or more values in the table. Changes to the set will    * update the underlying table, and vice versa.    *    * @return set of row keys    */
DECL|method|rowKeySet ()
name|Set
argument_list|<
name|R
argument_list|>
name|rowKeySet
argument_list|()
block|;
comment|/**    * Returns a set of column keys that have one or more values in the table. Changes to the set will    * update the underlying table, and vice versa.    *    * @return set of column keys    */
DECL|method|columnKeySet ()
name|Set
argument_list|<
name|C
argument_list|>
name|columnKeySet
argument_list|()
block|;
comment|/**    * Returns a collection of all values, which may contain duplicates. Changes to the returned    * collection will update the underlying table, and vice versa.    *    * @return collection of values    */
DECL|method|values ()
name|Collection
argument_list|<
name|V
argument_list|>
name|values
argument_list|()
block|;
comment|/**    * Returns a view that associates each row key with the corresponding map from column keys to    * values. Changes to the returned map will update this table. The returned map does not support    * {@code put()} or {@code putAll()}, or {@code setValue()} on its entries.    *    *<p>In contrast, the maps returned by {@code rowMap().get()} have the same behavior as those    * returned by {@link #row}. Those maps may support {@code setValue()}, {@code put()}, and {@code    * putAll()}.    *    * @return a map view from each row key to a secondary map from column keys to values    */
DECL|method|rowMap ()
name|Map
argument_list|<
name|R
argument_list|,
name|Map
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|rowMap
argument_list|()
block|;
comment|/**    * Returns a view that associates each column key with the corresponding map from row keys to    * values. Changes to the returned map will update this table. The returned map does not support    * {@code put()} or {@code putAll()}, or {@code setValue()} on its entries.    *    *<p>In contrast, the maps returned by {@code columnMap().get()} have the same behavior as those    * returned by {@link #column}. Those maps may support {@code setValue()}, {@code put()}, and    * {@code putAll()}.    *    * @return a map view from each column key to a secondary map from row keys to values    */
DECL|method|columnMap ()
name|Map
argument_list|<
name|C
argument_list|,
name|Map
argument_list|<
name|R
argument_list|,
name|V
argument_list|>
argument_list|>
name|columnMap
argument_list|()
block|;
comment|/**    * Row key / column key / value triplet corresponding to a mapping in a table.    *    * @since 7.0    */
DECL|interface|Cell
block|interface
name|Cell
operator|<
name|R
expr|extends @
name|Nullable
name|Object
block|,
name|C
expr|extends @
name|Nullable
name|Object
block|,
name|V
expr|extends @
name|Nullable
name|Object
operator|>
block|{
comment|/** Returns the row key of this cell. */
block|@
name|ParametricNullness
DECL|method|getRowKey ()
name|R
name|getRowKey
argument_list|()
block|;
comment|/** Returns the column key of this cell. */
block|@
name|ParametricNullness
DECL|method|getColumnKey ()
name|C
name|getColumnKey
argument_list|()
block|;
comment|/** Returns the value of this cell. */
block|@
name|ParametricNullness
DECL|method|getValue ()
name|V
name|getValue
argument_list|()
block|;
comment|/**      * Compares the specified object with this cell for equality. Two cells are equal when they have      * equal row keys, column keys, and values.      */
block|@
name|Override
DECL|method|equals (@heckForNull Object obj)
name|boolean
name|equals
argument_list|(
annotation|@
name|CheckForNull
name|Object
name|obj
argument_list|)
block|;
comment|/**      * Returns the hash code of this cell.      *      *<p>The hash code of a table cell is equal to {@link Objects#hashCode}{@code (e.getRowKey(),      * e.getColumnKey(), e.getValue())}.      */
block|@
name|Override
DECL|method|hashCode ()
name|int
name|hashCode
argument_list|()
block|;   }
block|}
end_expr_stmt

end_unit

