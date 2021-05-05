begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2021 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.util.concurrent
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
operator|.
name|FIELD
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
operator|.
name|METHOD
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
operator|.
name|PARAMETER
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
operator|.
name|RUNTIME
import|;
end_import

begin_import
import|import static
name|javax
operator|.
name|annotation
operator|.
name|meta
operator|.
name|When
operator|.
name|UNKNOWN
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
name|Target
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nonnull
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|meta
operator|.
name|TypeQualifierNickname
import|;
end_import

begin_comment
comment|/**  * Marks a "top-level" type-variable usage as (a) a Kotlin platform type when the type argument is  * non-nullable and (b) nullable when the type argument is nullable. This is the closest we can get  * to "non-nullable when non-nullable; nullable when nullable" (like the Android<a  * href="https://android.googlesource.com/platform/libcore/+/master/luni/src/main/java/libcore/util/NullFromTypeParam.java">{@code  * NullFromTypeParam}</a>). We use this to "undo" {@link ElementTypesAreNonnullByDefault}.  */
end_comment

begin_annotation_defn
annotation|@
name|GwtCompatible
annotation|@
name|Retention
argument_list|(
name|RUNTIME
argument_list|)
annotation|@
name|Target
argument_list|(
block|{
name|FIELD
block|,
name|METHOD
block|,
name|PARAMETER
block|}
argument_list|)
annotation|@
name|TypeQualifierNickname
annotation|@
name|Nonnull
argument_list|(
name|when
operator|=
name|UNKNOWN
argument_list|)
DECL|annotation|ParametricNullness
annotation_defn|@interface
name|ParametricNullness
block|{}
end_annotation_defn

end_unit

