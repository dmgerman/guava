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

begin_comment
comment|/**  * Optional features of classes derived from {@code Map}.  *  * @author George van den Driessche  */
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
DECL|enum|MapFeature
specifier|public
enum|enum
name|MapFeature
implements|implements
name|Feature
argument_list|<
name|Map
argument_list|>
block|{
comment|/**    * The map does not throw {@code NullPointerException} on calls such as    * {@code containsKey(null)}, {@code get(null)},    * {@code keySet().contains(null)} or {@code remove(null)}.    */
DECL|enumConstant|ALLOWS_NULL_KEY_QUERIES
name|ALLOWS_NULL_KEY_QUERIES
block|,
DECL|enumConstant|ALLOWS_NULL_KEYS
name|ALLOWS_NULL_KEYS
parameter_list|(
name|ALLOWS_NULL_KEY_QUERIES
parameter_list|)
operator|,
comment|/**    * The map does not throw {@code NullPointerException} on calls such as    * {@code containsValue(null)}, {@code values().contains(null)} or    * {@code values().remove(null)}.    */
DECL|enumConstant|ALLOWS_NULL_VALUE_QUERIES
constructor|ALLOWS_NULL_VALUE_QUERIES
operator|,
DECL|enumConstant|ALLOWS_NULL_VALUES
constructor|ALLOWS_NULL_VALUES(ALLOWS_NULL_VALUE_QUERIES
block|)
enum|,
comment|/**    * The map does not throw {@code NullPointerException} on calls such as    * {@code entrySet().contains(null)} or {@code entrySet().remove(null)}    */
DECL|enumConstant|ALLOWS_NULL_ENTRY_QUERIES
name|ALLOWS_NULL_ENTRY_QUERIES
operator|,
comment|/**    * The map does not throw {@code NullPointerException} on any {@code null}    * queries.    *    * @see #ALLOWS_NULL_KEY_QUERIES    * @see #ALLOWS_NULL_VALUE_QUERIES    * @see #ALLOWS_NULL_ENTRY_QUERIES    */
DECL|enumConstant|ALLOWS_ANY_NULL_QUERIES
name|ALLOWS_ANY_NULL_QUERIES
argument_list|(
name|ALLOWS_NULL_ENTRY_QUERIES
argument_list|,
name|ALLOWS_NULL_KEY_QUERIES
argument_list|,
name|ALLOWS_NULL_VALUE_QUERIES
argument_list|)
operator|,
DECL|enumConstant|RESTRICTS_KEYS
name|RESTRICTS_KEYS
operator|,
DECL|enumConstant|RESTRICTS_VALUES
name|RESTRICTS_VALUES
operator|,
DECL|enumConstant|SUPPORTS_PUT
name|SUPPORTS_PUT
operator|,
DECL|enumConstant|SUPPORTS_REMOVE
name|SUPPORTS_REMOVE
operator|,
DECL|enumConstant|FAILS_FAST_ON_CONCURRENT_MODIFICATION
name|FAILS_FAST_ON_CONCURRENT_MODIFICATION
operator|,
comment|/**    * Indicates that the constructor or factory method of a map, usually an    * immutable map, throws an {@link IllegalArgumentException} when presented    * with duplicate keys instead of discarding all but one.    */
DECL|enumConstant|REJECTS_DUPLICATES_AT_CREATION
name|REJECTS_DUPLICATES_AT_CREATION
operator|,
DECL|enumConstant|GENERAL_PURPOSE
name|GENERAL_PURPOSE
argument_list|(
name|SUPPORTS_PUT
argument_list|,
name|SUPPORTS_REMOVE
argument_list|)
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
name|Map
argument_list|>
argument_list|>
name|implied
decl_stmt|;
end_decl_stmt

begin_expr_stmt
DECL|method|MapFeature (Feature<? super Map> .... implied)
name|MapFeature
argument_list|(
name|Feature
argument_list|<
name|?
super|super
name|Map
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
name|Map
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
specifier|public
specifier|abstract
name|MapFeature
index|[]
name|value
argument_list|()
expr|default
block|{}
expr_stmt|;
DECL|method|absent ()
specifier|public
specifier|abstract
name|MapFeature
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

