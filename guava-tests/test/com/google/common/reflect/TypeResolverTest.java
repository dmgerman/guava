begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.reflect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|reflect
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|ParameterizedType
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
name|Type
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
name|Map
import|;
end_import

begin_comment
comment|/**  * Unit tests of {@link TypeResolver}.  *  * @author Ben Yu  */
end_comment

begin_class
annotation|@
name|AndroidIncompatible
comment|// lots of failures, possibly some related to bad equals() implementations?
DECL|class|TypeResolverTest
specifier|public
class|class
name|TypeResolverTest
extends|extends
name|TestCase
block|{
DECL|method|testWhere_noMapping ()
specifier|public
name|void
name|testWhere_noMapping
parameter_list|()
block|{
name|Type
name|t
init|=
name|aTypeVariable
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|t
argument_list|,
operator|new
name|TypeResolver
argument_list|()
operator|.
name|resolveType
argument_list|(
name|t
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testWhere_typeVariableMapping ()
specifier|public
name|void
name|testWhere_typeVariableMapping
parameter_list|()
block|{
name|Type
name|t
init|=
name|aTypeVariable
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|class
argument_list|,
operator|new
name|TypeResolver
argument_list|()
operator|.
name|where
argument_list|(
name|t
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|resolveType
argument_list|(
name|t
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testWhere_indirectMapping ()
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|testWhere_indirectMapping
parameter_list|()
block|{
name|Type
name|t1
init|=
operator|new
name|TypeCapture
argument_list|<
name|T
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
decl_stmt|;
name|Type
name|t2
init|=
name|aTypeVariable
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|class
argument_list|,
operator|new
name|TypeResolver
argument_list|()
operator|.
name|where
argument_list|(
name|t1
argument_list|,
name|t2
argument_list|)
operator|.
name|where
argument_list|(
name|t2
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|resolveType
argument_list|(
name|t1
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testWhere_typeVariableSelfMapping ()
specifier|public
name|void
name|testWhere_typeVariableSelfMapping
parameter_list|()
block|{
name|TypeResolver
name|resolver
init|=
operator|new
name|TypeResolver
argument_list|()
decl_stmt|;
name|Type
name|t
init|=
name|aTypeVariable
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|t
argument_list|,
name|resolver
operator|.
name|where
argument_list|(
name|t
argument_list|,
name|t
argument_list|)
operator|.
name|resolveType
argument_list|(
name|t
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testWhere_parameterizedSelfMapping ()
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|testWhere_parameterizedSelfMapping
parameter_list|()
block|{
name|TypeResolver
name|resolver
init|=
operator|new
name|TypeResolver
argument_list|()
decl_stmt|;
name|Type
name|t
init|=
operator|new
name|TypeCapture
argument_list|<
name|List
argument_list|<
name|T
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|t
argument_list|,
name|resolver
operator|.
name|where
argument_list|(
name|t
argument_list|,
name|t
argument_list|)
operator|.
name|resolveType
argument_list|(
name|t
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testWhere_genericArraySelfMapping ()
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|testWhere_genericArraySelfMapping
parameter_list|()
block|{
name|TypeResolver
name|resolver
init|=
operator|new
name|TypeResolver
argument_list|()
decl_stmt|;
name|Type
name|t
init|=
operator|new
name|TypeCapture
argument_list|<
name|T
index|[]
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|t
argument_list|,
name|resolver
operator|.
name|where
argument_list|(
name|t
argument_list|,
name|t
argument_list|)
operator|.
name|resolveType
argument_list|(
name|t
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testWhere_rawClassSelfMapping ()
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|testWhere_rawClassSelfMapping
parameter_list|()
block|{
name|TypeResolver
name|resolver
init|=
operator|new
name|TypeResolver
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|resolver
operator|.
name|where
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|resolveType
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testWhere_wildcardSelfMapping ()
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|testWhere_wildcardSelfMapping
parameter_list|()
block|{
name|TypeResolver
name|resolver
init|=
operator|new
name|TypeResolver
argument_list|()
decl_stmt|;
name|Type
name|t
init|=
name|aWildcardType
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|t
argument_list|,
name|resolver
operator|.
name|where
argument_list|(
name|t
argument_list|,
name|t
argument_list|)
operator|.
name|resolveType
argument_list|(
name|t
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testWhere_duplicateMapping ()
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|testWhere_duplicateMapping
parameter_list|()
block|{
name|Type
name|t
init|=
name|aTypeVariable
argument_list|()
decl_stmt|;
name|TypeResolver
name|resolver
init|=
operator|new
name|TypeResolver
argument_list|()
operator|.
name|where
argument_list|(
name|t
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|resolver
operator|.
name|where
argument_list|(
name|t
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testWhere_recursiveMapping ()
specifier|public
parameter_list|<
name|T1
parameter_list|,
name|T2
extends|extends
name|List
argument_list|<
name|T1
argument_list|>
parameter_list|>
name|void
name|testWhere_recursiveMapping
parameter_list|()
block|{
name|Type
name|t1
init|=
operator|new
name|TypeCapture
argument_list|<
name|T1
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
decl_stmt|;
name|Type
name|t2
init|=
operator|new
name|TypeCapture
argument_list|<
name|T2
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|t2
argument_list|,
operator|new
name|TypeResolver
argument_list|()
operator|.
name|where
argument_list|(
name|t1
argument_list|,
name|t2
argument_list|)
operator|.
name|resolveType
argument_list|(
name|t1
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testWhere_genericArrayMapping ()
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|testWhere_genericArrayMapping
parameter_list|()
block|{
name|Type
name|t
init|=
operator|new
name|TypeCapture
argument_list|<
name|T
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|class
argument_list|,
operator|new
name|TypeResolver
argument_list|()
operator|.
name|where
argument_list|(
operator|new
name|TypeCapture
argument_list|<
name|T
index|[]
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
argument_list|,
name|String
index|[]
operator|.
expr|class
argument_list|)
operator|.
name|resolveType
argument_list|(
name|t
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testWhere_primitiveArrayMapping ()
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|testWhere_primitiveArrayMapping
parameter_list|()
block|{
name|Type
name|t
init|=
operator|new
name|TypeCapture
argument_list|<
name|T
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|int
operator|.
name|class
argument_list|,
operator|new
name|TypeResolver
argument_list|()
operator|.
name|where
argument_list|(
operator|new
name|TypeCapture
argument_list|<
name|T
index|[]
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
argument_list|,
name|int
index|[]
operator|.
expr|class
argument_list|)
operator|.
name|resolveType
argument_list|(
name|t
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testWhere_parameterizedTypeMapping ()
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|testWhere_parameterizedTypeMapping
parameter_list|()
block|{
name|Type
name|t
init|=
operator|new
name|TypeCapture
argument_list|<
name|T
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|class
argument_list|,
operator|new
name|TypeResolver
argument_list|()
operator|.
name|where
argument_list|(
operator|new
name|TypeCapture
argument_list|<
name|List
argument_list|<
name|T
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
argument_list|,
operator|new
name|TypeCapture
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
argument_list|)
operator|.
name|resolveType
argument_list|(
name|t
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|subtypeOf
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|,
operator|new
name|TypeResolver
argument_list|()
operator|.
name|where
argument_list|(
operator|new
name|TypeCapture
argument_list|<
name|List
argument_list|<
name|T
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
argument_list|,
operator|new
name|TypeCapture
argument_list|<
name|List
argument_list|<
name|?
extends|extends
name|String
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
block|)
function|.resolveType
parameter_list|(
name|t
parameter_list|)
block|)
class|;
end_class

begin_expr_stmt
name|assertEquals
argument_list|(
name|Types
operator|.
name|supertypeOf
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|,
operator|new
name|TypeResolver
argument_list|()
operator|.
name|where
argument_list|(
operator|new
name|TypeCapture
argument_list|<
name|List
argument_list|<
name|T
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
argument_list|,
operator|new
name|TypeCapture
argument_list|<
name|List
argument_list|<
name|?
super|super
name|String
argument_list|>
argument_list|>
argument_list|()
block|{}
end_expr_stmt

begin_expr_stmt
operator|.
name|capture
argument_list|()
end_expr_stmt

begin_expr_stmt
unit|)
operator|.
name|resolveType
argument_list|(
name|t
argument_list|)
end_expr_stmt

begin_empty_stmt
unit|)
empty_stmt|;
end_empty_stmt

begin_function
unit|}    public
DECL|method|testWhere_wildcardTypeMapping ()
parameter_list|<
name|T
parameter_list|>
name|void
name|testWhere_wildcardTypeMapping
parameter_list|()
block|{
name|Type
name|t
init|=
operator|new
name|TypeCapture
argument_list|<
name|T
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|class
argument_list|,
operator|new
name|TypeResolver
argument_list|()
operator|.
name|where
argument_list|(
operator|new
name|TypeCapture
argument_list|<
name|List
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
operator|,
operator|new
name|TypeCapture
argument_list|<
name|List
argument_list|<
name|?
extends|extends
name|String
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
block|)
function|.resolveType
parameter_list|(
name|t
parameter_list|)
end_function

begin_empty_stmt
unit|)
empty_stmt|;
end_empty_stmt

begin_expr_stmt
name|assertEquals
argument_list|(
name|String
operator|.
name|class
argument_list|,
operator|new
name|TypeResolver
argument_list|()
operator|.
name|where
argument_list|(
operator|new
name|TypeCapture
argument_list|<
name|List
argument_list|<
name|?
super|super
name|T
argument_list|>
argument_list|>
argument_list|()
block|{}
end_expr_stmt

begin_expr_stmt
operator|.
name|capture
argument_list|()
operator|,
operator|new
name|TypeCapture
argument_list|<
name|List
argument_list|<
name|?
super|super
name|String
argument_list|>
argument_list|>
argument_list|()
block|{}
end_expr_stmt

begin_expr_stmt
operator|.
name|capture
argument_list|()
end_expr_stmt

begin_expr_stmt
unit|)
operator|.
name|resolveType
argument_list|(
name|t
argument_list|)
end_expr_stmt

begin_empty_stmt
unit|)
empty_stmt|;
end_empty_stmt

begin_function
unit|}    public
DECL|method|testWhere_incompatibleGenericArrayMapping ()
parameter_list|<
name|T
parameter_list|>
name|void
name|testWhere_incompatibleGenericArrayMapping
parameter_list|()
block|{
try|try
block|{
operator|new
name|TypeResolver
argument_list|()
operator|.
name|where
argument_list|(
operator|new
name|TypeCapture
argument_list|<
name|T
index|[]
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
end_function

begin_function
DECL|method|testWhere_incompatibleParameterizedTypeMapping ()
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|testWhere_incompatibleParameterizedTypeMapping
parameter_list|()
block|{
try|try
block|{
operator|new
name|TypeResolver
argument_list|()
operator|.
name|where
argument_list|(
operator|new
name|TypeCapture
argument_list|<
name|Iterable
argument_list|<
name|T
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
argument_list|,
name|List
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
end_function

begin_function
DECL|method|testWhere_impossibleParameterizedTypeMapping ()
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|testWhere_impossibleParameterizedTypeMapping
parameter_list|()
block|{
try|try
block|{
operator|new
name|TypeResolver
argument_list|()
operator|.
name|where
argument_list|(
operator|new
name|TypeCapture
argument_list|<
name|List
argument_list|<
name|T
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
argument_list|,
operator|new
name|TypeCapture
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
end_function

begin_function
DECL|method|testWhere_incompatibleWildcardUpperBound ()
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|testWhere_incompatibleWildcardUpperBound
parameter_list|()
block|{
try|try
block|{
operator|new
name|TypeResolver
argument_list|()
operator|.
name|where
argument_list|(
operator|new
name|TypeCapture
argument_list|<
name|List
argument_list|<
name|?
extends|extends
name|String
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
operator|,
operator|new
name|TypeCapture
argument_list|<
name|List
argument_list|<
name|?
extends|extends
name|Integer
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
block|)
empty_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
end_function

begin_catch
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
end_catch

begin_function
unit|}    public
DECL|method|testWhere_incompatibleWildcardLowerBound ()
parameter_list|<
name|T
parameter_list|>
name|void
name|testWhere_incompatibleWildcardLowerBound
parameter_list|()
block|{
try|try
block|{
operator|new
name|TypeResolver
argument_list|()
operator|.
name|where
argument_list|(
operator|new
name|TypeCapture
argument_list|<
name|List
argument_list|<
name|?
super|super
name|String
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
operator|,
operator|new
name|TypeCapture
argument_list|<
name|List
argument_list|<
name|?
super|super
name|Integer
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
block|)
empty_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
end_function

begin_catch
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
end_catch

begin_function
unit|}    public
DECL|method|testWhere_incompatibleWildcardBounds ()
parameter_list|<
name|T
parameter_list|>
name|void
name|testWhere_incompatibleWildcardBounds
parameter_list|()
block|{
try|try
block|{
operator|new
name|TypeResolver
argument_list|()
operator|.
name|where
argument_list|(
operator|new
name|TypeCapture
argument_list|<
name|List
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
operator|,
operator|new
name|TypeCapture
argument_list|<
name|List
argument_list|<
name|?
super|super
name|String
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
block|)
empty_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
end_function

begin_catch
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
end_catch

begin_function
unit|}    public
DECL|method|testWhere_wrongOrder ()
parameter_list|<
name|T
parameter_list|>
name|void
name|testWhere_wrongOrder
parameter_list|()
block|{
try|try
block|{
operator|new
name|TypeResolver
argument_list|()
operator|.
name|where
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|aTypeVariable
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
end_function

begin_function
DECL|method|testWhere_mapFromConcreteParameterizedType ()
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|testWhere_mapFromConcreteParameterizedType
parameter_list|()
block|{
try|try
block|{
operator|new
name|TypeResolver
argument_list|()
operator|.
name|where
argument_list|(
operator|new
name|TypeCapture
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
argument_list|,
name|aTypeVariable
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
end_function

begin_function
DECL|method|testWhere_mapFromConcreteGenericArrayType ()
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|testWhere_mapFromConcreteGenericArrayType
parameter_list|()
block|{
try|try
block|{
operator|new
name|TypeResolver
argument_list|()
operator|.
name|where
argument_list|(
operator|new
name|TypeCapture
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
argument_list|,
name|aTypeVariable
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
end_function

begin_function
DECL|method|testWhere_actualArgHasWildcard ()
specifier|public
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|void
name|testWhere_actualArgHasWildcard
parameter_list|()
block|{
name|TypeResolver
name|resolver
init|=
operator|new
name|TypeResolver
argument_list|()
operator|.
name|where
argument_list|(
operator|new
name|TypeCapture
argument_list|<
name|Iterable
argument_list|<
name|Map
argument_list|<
name|?
argument_list|,
name|V
argument_list|>
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
operator|,
operator|new
name|TypeCapture
argument_list|<
name|Iterable
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
block|)
function|;
end_function

begin_expr_stmt
name|assertEquals
argument_list|(
operator|new
name|TypeCapture
argument_list|<
name|K
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
argument_list|,
name|resolver
operator|.
name|resolveType
argument_list|(
operator|new
name|TypeCapture
argument_list|<
name|K
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|assertEquals
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
name|resolver
operator|.
name|resolveType
argument_list|(
operator|new
name|TypeCapture
argument_list|<
name|V
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
end_expr_stmt

begin_function
unit|}    public
DECL|method|testWhere_mapFromWildcard ()
parameter_list|<
name|T
parameter_list|>
name|void
name|testWhere_mapFromWildcard
parameter_list|()
block|{
name|Type
name|subType
init|=
operator|new
name|TypeCapture
argument_list|<
name|TypedKeyMap
argument_list|<
name|T
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|TypeCapture
argument_list|<
name|TypedKeyMap
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
argument_list|,
operator|new
name|TypeResolver
argument_list|()
operator|.
name|where
argument_list|(
operator|new
name|TypeCapture
argument_list|<
name|Map
argument_list|<
name|Integer
argument_list|,
name|T
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
argument_list|,
operator|new
name|TypeCapture
argument_list|<
name|Map
argument_list|<
name|?
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
block|)
function|.resolveType
parameter_list|(
name|subType
parameter_list|)
end_function

begin_empty_stmt
unit|)
empty_stmt|;
end_empty_stmt

begin_function
unit|}    public
DECL|method|testWhere_mapFromWildcardToParameterized ()
parameter_list|<
name|T
parameter_list|>
name|void
name|testWhere_mapFromWildcardToParameterized
parameter_list|()
block|{
name|Type
name|subType
init|=
operator|new
name|TypeCapture
argument_list|<
name|TypedListKeyMap
argument_list|<
name|T
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|TypeCapture
argument_list|<
name|TypedListKeyMap
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
argument_list|,
operator|new
name|TypeResolver
argument_list|()
operator|.
name|where
argument_list|(
operator|new
name|TypeCapture
argument_list|<
name|Map
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|,
name|T
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
argument_list|,
operator|new
name|TypeCapture
argument_list|<
name|Map
argument_list|<
name|?
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
block|)
function|.resolveType
parameter_list|(
name|subType
parameter_list|)
end_function

begin_empty_stmt
unit|)
empty_stmt|;
end_empty_stmt

begin_function
unit|}    public
DECL|method|testWhere_mapFromBoundedWildcard ()
parameter_list|<
name|T
parameter_list|>
name|void
name|testWhere_mapFromBoundedWildcard
parameter_list|()
block|{
name|Type
name|subType
init|=
operator|new
name|TypeCapture
argument_list|<
name|TypedKeyMap
argument_list|<
name|T
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
decl_stmt|;
comment|// TODO(benyu): This should check equality to an expected value, see discussion in cl/98674873
name|Type
name|unused
init|=
operator|new
name|TypeResolver
argument_list|()
operator|.
name|where
argument_list|(
operator|new
name|TypeCapture
argument_list|<
name|Map
argument_list|<
name|Integer
argument_list|,
name|T
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
argument_list|,
operator|new
name|TypeCapture
argument_list|<
name|Map
argument_list|<
name|?
extends|extends
name|Number
argument_list|,
name|?
extends|extends
name|Number
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
block|)
function|.resolveType
parameter_list|(
name|subType
parameter_list|)
function|;
end_function

begin_expr_stmt
unit|}    interface
DECL|interface|TypedKeyMap
name|TypedKeyMap
argument_list|<
name|T
argument_list|>
expr|extends
name|Map
argument_list|<
name|Integer
argument_list|,
name|T
argument_list|>
block|{}
DECL|interface|TypedListKeyMap
expr|interface
name|TypedListKeyMap
argument_list|<
name|T
argument_list|>
expr|extends
name|Map
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|,
name|T
argument_list|>
block|{}
DECL|method|aTypeVariable ()
specifier|private
specifier|static
operator|<
name|T
operator|>
name|Type
name|aTypeVariable
argument_list|()
block|{
return|return
operator|new
name|TypeCapture
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
block|}
operator|.
name|capture
argument_list|()
expr_stmt|;
end_expr_stmt

begin_function
unit|}    private
DECL|method|aWildcardType ()
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Type
name|aWildcardType
parameter_list|()
block|{
name|ParameterizedType
name|parameterizedType
init|=
operator|(
name|ParameterizedType
operator|)
operator|new
name|TypeCapture
argument_list|<
name|List
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
expr_stmt|;
return|return
name|parameterizedType
operator|.
name|getActualTypeArguments
argument_list|()
index|[
literal|0
index|]
return|;
block|}
end_function

unit|}
end_unit

