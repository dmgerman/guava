begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing.features
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|testing
operator|.
name|features
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
name|collect
operator|.
name|testing
operator|.
name|Helpers
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Inherited
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
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
name|LinkedHashSet
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
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_comment
comment|/**  * Optional features of classes derived from {@code Collection}.  *  * @author George van den Driessche  */
end_comment

begin_comment
comment|// Enum values use constructors with generic varargs.
end_comment

begin_enum
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|GwtCompatible
DECL|enum|CollectionFeature
specifier|public
enum|enum
name|CollectionFeature
implements|implements
name|Feature
argument_list|<
name|Collection
argument_list|>
block|{
comment|/**    * The collection must not throw {@code NullPointerException} on calls such as {@code    * contains(null)} or {@code remove(null)}, but instead must return a simple {@code false}.    */
DECL|enumConstant|ALLOWS_NULL_QUERIES
name|ALLOWS_NULL_QUERIES
block|,
DECL|enumConstant|ALLOWS_NULL_VALUES
name|ALLOWS_NULL_VALUES
parameter_list|(
name|ALLOWS_NULL_QUERIES
parameter_list|)
operator|,
comment|/**    * Indicates that a collection disallows certain elements (other than {@code null}, whose validity    * as an element is indicated by the presence or absence of {@link #ALLOWS_NULL_VALUES}). From the    * documentation for {@link Collection}:    *    *<blockquote>    *    * "Some collection implementations have restrictions on the elements that they may contain. For    * example, some implementations prohibit null elements, and some have restrictions on the types    * of their elements."    *    *</blockquote>    */
DECL|enumConstant|RESTRICTS_ELEMENTS
constructor|RESTRICTS_ELEMENTS
operator|,
comment|/**    * Indicates that a collection has a well-defined ordering of its elements. The ordering may    * depend on the element values, such as a {@link SortedSet}, or on the insertion ordering, such    * as a {@link LinkedHashSet}. All list tests and sorted-collection tests automatically specify    * this feature.    */
DECL|enumConstant|KNOWN_ORDER
constructor|KNOWN_ORDER
operator|,
comment|/**    * Indicates that a collection has a different {@link Object#toString} representation than most    * collections. If not specified, the collection tests will examine the value returned by {@link    * Object#toString}.    */
DECL|enumConstant|NON_STANDARD_TOSTRING
constructor|NON_STANDARD_TOSTRING
operator|,
comment|/**    * Indicates that the constructor or factory method of a collection, usually an immutable set,    * throws an {@link IllegalArgumentException} when presented with duplicate elements instead of    * collapsing them to a single element or including duplicate instances in the collection.    */
DECL|enumConstant|REJECTS_DUPLICATES_AT_CREATION
constructor|REJECTS_DUPLICATES_AT_CREATION
operator|,
DECL|enumConstant|SUPPORTS_ADD
constructor|SUPPORTS_ADD
operator|,
DECL|enumConstant|SUPPORTS_REMOVE
constructor|SUPPORTS_REMOVE
operator|,
DECL|enumConstant|SUPPORTS_ITERATOR_REMOVE
constructor|SUPPORTS_ITERATOR_REMOVE
operator|,
DECL|enumConstant|FAILS_FAST_ON_CONCURRENT_MODIFICATION
constructor|FAILS_FAST_ON_CONCURRENT_MODIFICATION
operator|,
comment|/**    * Features supported by general-purpose collections - everything but {@link #RESTRICTS_ELEMENTS}.    *    * @see java.util.Collection the definition of general-purpose collections.    */
DECL|enumConstant|GENERAL_PURPOSE
constructor|GENERAL_PURPOSE(SUPPORTS_ADD
operator|,
constructor|SUPPORTS_REMOVE
operator|,
constructor|SUPPORTS_ITERATOR_REMOVE
block|)
enum|,
comment|/** Features supported by collections where only removal is allowed. */
DECL|enumConstant|REMOVE_OPERATIONS
name|REMOVE_OPERATIONS
argument_list|(
name|SUPPORTS_REMOVE
argument_list|,
name|SUPPORTS_ITERATOR_REMOVE
argument_list|)
operator|,
DECL|enumConstant|SERIALIZABLE
name|SERIALIZABLE
operator|,
DECL|enumConstant|SERIALIZABLE_INCLUDING_VIEWS
name|SERIALIZABLE_INCLUDING_VIEWS
argument_list|(
name|SERIALIZABLE
argument_list|)
operator|,
DECL|enumConstant|SUBSET_VIEW
name|SUBSET_VIEW
operator|,
DECL|enumConstant|DESCENDING_VIEW
name|DESCENDING_VIEW
operator|,
comment|/**    * For documenting collections that support no optional features, such as {@link    * java.util.Collections#emptySet}    */
DECL|enumConstant|NONE
name|NONE
enum|;
end_enum

begin_decl_stmt
DECL|field|implied
specifier|private
specifier|final
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
super|super
name|Collection
argument_list|>
argument_list|>
name|implied
decl_stmt|;
end_decl_stmt

begin_expr_stmt
DECL|method|CollectionFeature (Feature<? super Collection>.... implied)
name|CollectionFeature
argument_list|(
name|Feature
argument_list|<
name|?
super|super
name|Collection
argument_list|>
operator|...
name|implied
argument_list|)
block|{
name|this
operator|.
name|implied
operator|=
name|Helpers
operator|.
name|copyToSet
argument_list|(
name|implied
argument_list|)
block|;   }
expr|@
name|Override
DECL|method|getImpliedFeatures ()
specifier|public
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
super|super
name|Collection
argument_list|>
argument_list|>
name|getImpliedFeatures
argument_list|()
block|{
return|return
name|implied
return|;
block|}
end_expr_stmt

begin_annotation_defn
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
annotation|@
name|Inherited
annotation|@
name|TesterAnnotation
DECL|annotation|Require
specifier|public
annotation_defn|@interface
name|Require
block|{
DECL|method|value ()
name|CollectionFeature
index|[]
name|value
argument_list|()
expr|default
block|{}
expr_stmt|;
DECL|method|absent ()
name|CollectionFeature
index|[]
name|absent
argument_list|()
expr|default
block|{}
expr_stmt|;
block|}
end_annotation_defn

unit|}
end_unit

