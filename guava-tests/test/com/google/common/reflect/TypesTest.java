begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2011 Google Inc. All Rights Reserved.
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
import|import static
name|org
operator|.
name|junit
operator|.
name|contrib
operator|.
name|truth
operator|.
name|Truth
operator|.
name|ASSERT
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
name|testing
operator|.
name|EqualsTester
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
name|testing
operator|.
name|NullPointerTester
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
name|testing
operator|.
name|NullPointerTester
operator|.
name|Visibility
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
name|testing
operator|.
name|SerializableTester
import|;
end_import

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
name|GenericArrayType
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
name|GenericDeclaration
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
name|lang
operator|.
name|reflect
operator|.
name|TypeVariable
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
name|WildcardType
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
name|HashMap
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
comment|/**  * Tests for {@link Types}.  *  * @author benyu@google.com (Jige Yu)  */
end_comment

begin_class
DECL|class|TypesTest
specifier|public
class|class
name|TypesTest
extends|extends
name|TestCase
block|{
DECL|method|testNewParameterizedType_ownerTypeImplied ()
specifier|public
name|void
name|testNewParameterizedType_ownerTypeImplied
parameter_list|()
throws|throws
name|Exception
block|{
name|ParameterizedType
name|jvmType
init|=
operator|(
name|ParameterizedType
operator|)
operator|new
name|TypeCapture
argument_list|<
name|Map
operator|.
name|Entry
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
decl_stmt|;
name|ParameterizedType
name|ourType
init|=
name|Types
operator|.
name|newParameterizedType
argument_list|(
name|Map
operator|.
name|Entry
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|jvmType
argument_list|,
name|ourType
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Map
operator|.
name|class
argument_list|,
name|ourType
operator|.
name|getOwnerType
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testNewParameterizedType ()
specifier|public
name|void
name|testNewParameterizedType
parameter_list|()
block|{
name|ParameterizedType
name|jvmType
init|=
operator|(
name|ParameterizedType
operator|)
operator|new
name|TypeCapture
argument_list|<
name|HashMap
argument_list|<
name|String
argument_list|,
name|int
index|[]
index|[]
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
decl_stmt|;
name|ParameterizedType
name|ourType
init|=
name|Types
operator|.
name|newParameterizedType
argument_list|(
name|HashMap
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|int
index|[]
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|jvmType
argument_list|,
name|ourType
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|jvmType
operator|.
name|toString
argument_list|()
argument_list|,
name|ourType
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|jvmType
operator|.
name|hashCode
argument_list|()
argument_list|,
name|ourType
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|HashMap
operator|.
name|class
argument_list|,
name|ourType
operator|.
name|getRawType
argument_list|()
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|ourType
operator|.
name|getActualTypeArguments
argument_list|()
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
name|jvmType
operator|.
name|getActualTypeArguments
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|Types
operator|.
name|newArrayType
argument_list|(
name|Types
operator|.
name|newArrayType
argument_list|(
name|int
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|ourType
operator|.
name|getActualTypeArguments
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|ourType
operator|.
name|getOwnerType
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testNewParameterizedType_nonStaticLocalClass ()
specifier|public
name|void
name|testNewParameterizedType_nonStaticLocalClass
parameter_list|()
block|{
class|class
name|LocalClass
parameter_list|<
name|T
parameter_list|>
block|{}
name|Type
name|jvmType
init|=
operator|new
name|LocalClass
argument_list|<
name|String
argument_list|>
argument_list|()
block|{}
operator|.
name|getClass
argument_list|()
operator|.
name|getGenericSuperclass
argument_list|()
decl_stmt|;
name|Type
name|ourType
init|=
name|Types
operator|.
name|newParameterizedType
argument_list|(
name|LocalClass
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|jvmType
argument_list|,
name|ourType
argument_list|)
expr_stmt|;
block|}
DECL|method|testNewParameterizedType_staticLocalClass ()
specifier|public
name|void
name|testNewParameterizedType_staticLocalClass
parameter_list|()
block|{
name|doTestNewParameterizedType_staticLocalClass
argument_list|()
expr_stmt|;
block|}
DECL|method|doTestNewParameterizedType_staticLocalClass ()
specifier|private
specifier|static
name|void
name|doTestNewParameterizedType_staticLocalClass
parameter_list|()
block|{
class|class
name|LocalClass
parameter_list|<
name|T
parameter_list|>
block|{}
name|Type
name|jvmType
init|=
operator|new
name|LocalClass
argument_list|<
name|String
argument_list|>
argument_list|()
block|{}
operator|.
name|getClass
argument_list|()
operator|.
name|getGenericSuperclass
argument_list|()
decl_stmt|;
name|Type
name|ourType
init|=
name|Types
operator|.
name|newParameterizedType
argument_list|(
name|LocalClass
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|jvmType
argument_list|,
name|ourType
argument_list|)
expr_stmt|;
block|}
DECL|method|testNewParameterizedTypeWithOwner ()
specifier|public
name|void
name|testNewParameterizedTypeWithOwner
parameter_list|()
block|{
name|ParameterizedType
name|jvmType
init|=
operator|(
name|ParameterizedType
operator|)
operator|new
name|TypeCapture
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|int
index|[]
index|[]
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
decl_stmt|;
name|ParameterizedType
name|ourType
init|=
name|Types
operator|.
name|newParameterizedTypeWithOwner
argument_list|(
name|Map
operator|.
name|class
argument_list|,
name|Map
operator|.
name|Entry
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|int
index|[]
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|jvmType
argument_list|,
name|ourType
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
operator|new
name|TypeCapture
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
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
name|addEqualityGroup
argument_list|(
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
operator|.
name|testEquals
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|jvmType
operator|.
name|toString
argument_list|()
argument_list|,
name|ourType
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Map
operator|.
name|class
argument_list|,
name|ourType
operator|.
name|getOwnerType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Map
operator|.
name|Entry
operator|.
name|class
argument_list|,
name|ourType
operator|.
name|getRawType
argument_list|()
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|ourType
operator|.
name|getActualTypeArguments
argument_list|()
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
name|jvmType
operator|.
name|getActualTypeArguments
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testNewParameterizedType_serializable ()
specifier|public
name|void
name|testNewParameterizedType_serializable
parameter_list|()
block|{
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|Types
operator|.
name|newParameterizedType
argument_list|(
name|Map
operator|.
name|Entry
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testNewParameterizedType_ownerMismatch ()
specifier|public
name|void
name|testNewParameterizedType_ownerMismatch
parameter_list|()
block|{
try|try
block|{
name|Types
operator|.
name|newParameterizedTypeWithOwner
argument_list|(
name|Number
operator|.
name|class
argument_list|,
name|List
operator|.
name|class
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
DECL|method|testNewParameterizedType_ownerMissing ()
specifier|public
name|void
name|testNewParameterizedType_ownerMissing
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|Types
operator|.
name|newParameterizedType
argument_list|(
name|Map
operator|.
name|Entry
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|,
name|Types
operator|.
name|newParameterizedTypeWithOwner
argument_list|(
literal|null
argument_list|,
name|Map
operator|.
name|Entry
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testNewParameterizedType_invalidTypeParameters ()
specifier|public
name|void
name|testNewParameterizedType_invalidTypeParameters
parameter_list|()
block|{
try|try
block|{
name|Types
operator|.
name|newParameterizedTypeWithOwner
argument_list|(
name|Map
operator|.
name|class
argument_list|,
name|Map
operator|.
name|Entry
operator|.
name|class
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
DECL|method|testNewParameterizedType_primitiveTypeParameters ()
specifier|public
name|void
name|testNewParameterizedType_primitiveTypeParameters
parameter_list|()
block|{
try|try
block|{
name|Types
operator|.
name|newParameterizedTypeWithOwner
argument_list|(
name|Map
operator|.
name|class
argument_list|,
name|Map
operator|.
name|Entry
operator|.
name|class
argument_list|,
name|int
operator|.
name|class
argument_list|,
name|int
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
DECL|method|testNewArrayType ()
specifier|public
name|void
name|testNewArrayType
parameter_list|()
block|{
name|Type
name|jvmType1
init|=
operator|new
name|TypeCapture
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
index|[]
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
decl_stmt|;
name|GenericArrayType
name|ourType1
init|=
operator|(
name|GenericArrayType
operator|)
name|Types
operator|.
name|newArrayType
argument_list|(
name|Types
operator|.
name|newParameterizedType
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|Type
name|jvmType2
init|=
operator|new
name|TypeCapture
argument_list|<
name|List
index|[]
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
decl_stmt|;
name|Type
name|ourType2
init|=
name|Types
operator|.
name|newArrayType
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|jvmType1
argument_list|,
name|ourType1
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|jvmType2
argument_list|,
name|ourType2
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
name|assertEquals
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
name|ourType1
operator|.
name|getGenericComponentType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|jvmType1
operator|.
name|toString
argument_list|()
argument_list|,
name|ourType1
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|jvmType2
operator|.
name|toString
argument_list|()
argument_list|,
name|ourType2
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testNewArrayTypeOfArray ()
specifier|public
name|void
name|testNewArrayTypeOfArray
parameter_list|()
block|{
name|Type
name|jvmType
init|=
operator|new
name|TypeCapture
argument_list|<
name|int
index|[]
index|[]
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
decl_stmt|;
name|Type
name|ourType
init|=
name|Types
operator|.
name|newArrayType
argument_list|(
name|int
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|jvmType
operator|.
name|toString
argument_list|()
argument_list|,
name|ourType
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|jvmType
argument_list|,
name|ourType
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|testNewArrayType_primitive ()
specifier|public
name|void
name|testNewArrayType_primitive
parameter_list|()
block|{
name|Type
name|jvmType
init|=
operator|new
name|TypeCapture
argument_list|<
name|int
index|[]
argument_list|>
argument_list|()
block|{}
operator|.
name|capture
argument_list|()
decl_stmt|;
name|Type
name|ourType
init|=
name|Types
operator|.
name|newArrayType
argument_list|(
name|int
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|jvmType
operator|.
name|toString
argument_list|()
argument_list|,
name|ourType
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|jvmType
argument_list|,
name|ourType
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|testNewArrayType_upperBoundedWildcard ()
specifier|public
name|void
name|testNewArrayType_upperBoundedWildcard
parameter_list|()
block|{
name|Type
name|wildcard
init|=
name|Types
operator|.
name|subtypeOf
argument_list|(
name|Number
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|subtypeOf
argument_list|(
name|Number
index|[]
operator|.
expr|class
argument_list|)
argument_list|,
name|Types
operator|.
name|newArrayType
argument_list|(
name|wildcard
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testNewArrayType_lowerBoundedWildcard ()
specifier|public
name|void
name|testNewArrayType_lowerBoundedWildcard
parameter_list|()
block|{
name|Type
name|wildcard
init|=
name|Types
operator|.
name|supertypeOf
argument_list|(
name|Number
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|supertypeOf
argument_list|(
name|Number
index|[]
operator|.
expr|class
argument_list|)
argument_list|,
name|Types
operator|.
name|newArrayType
argument_list|(
name|wildcard
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testNewArrayType_serializable ()
specifier|public
name|void
name|testNewArrayType_serializable
parameter_list|()
block|{
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|Types
operator|.
name|newArrayType
argument_list|(
name|int
index|[]
operator|.
expr|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|class|WithWildcardType
specifier|private
specifier|static
class|class
name|WithWildcardType
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
DECL|method|withoutBound (List<?> list)
name|void
name|withoutBound
parameter_list|(
name|List
argument_list|<
name|?
argument_list|>
name|list
parameter_list|)
block|{}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
DECL|method|withObjectBound (List<? extends Object> list)
name|void
name|withObjectBound
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
name|list
parameter_list|)
block|{}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
DECL|method|withUpperBound (List<? extends int[][]> list)
name|void
name|withUpperBound
argument_list|(
name|List
operator|<
condition|?
then|extends
name|int
index|[]
index|[]
operator|>
name|list
argument_list|)
block|{}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
DECL|method|withLowerBound (List<? super String[][]> list)
name|void
name|withLowerBound
argument_list|(
name|List
operator|<
condition|?
name|super
name|String
index|[]
index|[]
operator|>
name|list
argument_list|)
block|{}
DECL|method|getWildcardType (String methodName)
specifier|static
name|WildcardType
name|getWildcardType
parameter_list|(
name|String
name|methodName
parameter_list|)
throws|throws
name|Exception
block|{
name|ParameterizedType
name|parameterType
init|=
operator|(
name|ParameterizedType
operator|)
name|WithWildcardType
operator|.
name|class
operator|.
name|getDeclaredMethod
argument_list|(
name|methodName
argument_list|,
name|List
operator|.
name|class
argument_list|)
operator|.
name|getGenericParameterTypes
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
return|return
operator|(
name|WildcardType
operator|)
name|parameterType
operator|.
name|getActualTypeArguments
argument_list|()
index|[
literal|0
index|]
return|;
block|}
block|}
DECL|method|testNewWildcardType ()
specifier|public
name|void
name|testNewWildcardType
parameter_list|()
throws|throws
name|Exception
block|{
name|WildcardType
name|noBoundJvmType
init|=
name|WithWildcardType
operator|.
name|getWildcardType
argument_list|(
literal|"withoutBound"
argument_list|)
decl_stmt|;
name|WildcardType
name|objectBoundJvmType
init|=
name|WithWildcardType
operator|.
name|getWildcardType
argument_list|(
literal|"withObjectBound"
argument_list|)
decl_stmt|;
name|WildcardType
name|upperBoundJvmType
init|=
name|WithWildcardType
operator|.
name|getWildcardType
argument_list|(
literal|"withUpperBound"
argument_list|)
decl_stmt|;
name|WildcardType
name|lowerBoundJvmType
init|=
name|WithWildcardType
operator|.
name|getWildcardType
argument_list|(
literal|"withLowerBound"
argument_list|)
decl_stmt|;
name|WildcardType
name|objectBound
init|=
name|Types
operator|.
name|subtypeOf
argument_list|(
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
name|WildcardType
name|upperBound
init|=
name|Types
operator|.
name|subtypeOf
argument_list|(
name|int
index|[]
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|WildcardType
name|lowerBound
init|=
name|Types
operator|.
name|supertypeOf
argument_list|(
name|String
index|[]
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|assertEqualWildcardType
argument_list|(
name|noBoundJvmType
argument_list|,
name|objectBound
argument_list|)
expr_stmt|;
name|assertEqualWildcardType
argument_list|(
name|objectBoundJvmType
argument_list|,
name|objectBound
argument_list|)
expr_stmt|;
name|assertEqualWildcardType
argument_list|(
name|upperBoundJvmType
argument_list|,
name|upperBound
argument_list|)
expr_stmt|;
name|assertEqualWildcardType
argument_list|(
name|lowerBoundJvmType
argument_list|,
name|lowerBound
argument_list|)
expr_stmt|;
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|noBoundJvmType
argument_list|,
name|objectBoundJvmType
argument_list|,
name|objectBound
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|upperBoundJvmType
argument_list|,
name|upperBound
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|lowerBoundJvmType
argument_list|,
name|lowerBound
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|testNewWildcardType_primitiveTypeBound ()
specifier|public
name|void
name|testNewWildcardType_primitiveTypeBound
parameter_list|()
block|{
try|try
block|{
name|Types
operator|.
name|subtypeOf
argument_list|(
name|int
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
DECL|method|testNewWildcardType_serializable ()
specifier|public
name|void
name|testNewWildcardType_serializable
parameter_list|()
block|{
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|Types
operator|.
name|supertypeOf
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|Types
operator|.
name|subtypeOf
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|Types
operator|.
name|subtypeOf
argument_list|(
name|Object
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertEqualWildcardType ( WildcardType expected, WildcardType actual)
specifier|private
specifier|static
name|void
name|assertEqualWildcardType
parameter_list|(
name|WildcardType
name|expected
parameter_list|,
name|WildcardType
name|actual
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|expected
operator|.
name|toString
argument_list|()
argument_list|,
name|actual
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|actual
operator|.
name|toString
argument_list|()
argument_list|,
name|expected
operator|.
name|hashCode
argument_list|()
argument_list|,
name|actual
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|actual
operator|.
name|getLowerBounds
argument_list|()
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
name|expected
operator|.
name|getLowerBounds
argument_list|()
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|actual
operator|.
name|getUpperBounds
argument_list|()
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
name|expected
operator|.
name|getUpperBounds
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|WithTypeVariable
specifier|private
specifier|static
class|class
name|WithTypeVariable
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
DECL|method|withoutBound (List<T> list)
parameter_list|<
name|T
parameter_list|>
name|void
name|withoutBound
parameter_list|(
name|List
argument_list|<
name|T
argument_list|>
name|list
parameter_list|)
block|{}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
DECL|method|withObjectBound (List<T> list)
parameter_list|<
name|T
extends|extends
name|Object
parameter_list|>
name|void
name|withObjectBound
parameter_list|(
name|List
argument_list|<
name|T
argument_list|>
name|list
parameter_list|)
block|{}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
DECL|method|withUpperBound (List<T> list)
parameter_list|<
name|T
extends|extends
name|Number
operator|&
name|CharSequence
parameter_list|>
name|void
name|withUpperBound
parameter_list|(
name|List
argument_list|<
name|T
argument_list|>
name|list
parameter_list|)
block|{}
DECL|method|getTypeVariable (String methodName)
specifier|static
name|TypeVariable
argument_list|<
name|?
argument_list|>
name|getTypeVariable
parameter_list|(
name|String
name|methodName
parameter_list|)
throws|throws
name|Exception
block|{
name|ParameterizedType
name|parameterType
init|=
operator|(
name|ParameterizedType
operator|)
name|WithTypeVariable
operator|.
name|class
operator|.
name|getDeclaredMethod
argument_list|(
name|methodName
argument_list|,
name|List
operator|.
name|class
argument_list|)
operator|.
name|getGenericParameterTypes
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
return|return
operator|(
name|TypeVariable
argument_list|<
name|?
argument_list|>
operator|)
name|parameterType
operator|.
name|getActualTypeArguments
argument_list|()
index|[
literal|0
index|]
return|;
block|}
block|}
DECL|method|testNewTypeVariable ()
specifier|public
name|void
name|testNewTypeVariable
parameter_list|()
throws|throws
name|Exception
block|{
name|TypeVariable
argument_list|<
name|?
argument_list|>
name|noBoundJvmType
init|=
name|WithTypeVariable
operator|.
name|getTypeVariable
argument_list|(
literal|"withoutBound"
argument_list|)
decl_stmt|;
name|TypeVariable
argument_list|<
name|?
argument_list|>
name|objectBoundJvmType
init|=
name|WithTypeVariable
operator|.
name|getTypeVariable
argument_list|(
literal|"withObjectBound"
argument_list|)
decl_stmt|;
name|TypeVariable
argument_list|<
name|?
argument_list|>
name|upperBoundJvmType
init|=
name|WithTypeVariable
operator|.
name|getTypeVariable
argument_list|(
literal|"withUpperBound"
argument_list|)
decl_stmt|;
name|TypeVariable
argument_list|<
name|?
argument_list|>
name|noBound
init|=
name|withBounds
argument_list|(
name|noBoundJvmType
argument_list|)
decl_stmt|;
name|TypeVariable
argument_list|<
name|?
argument_list|>
name|objectBound
init|=
name|withBounds
argument_list|(
name|objectBoundJvmType
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
name|TypeVariable
argument_list|<
name|?
argument_list|>
name|upperBound
init|=
name|withBounds
argument_list|(
name|upperBoundJvmType
argument_list|,
name|Number
operator|.
name|class
argument_list|,
name|CharSequence
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEqualTypeVariable
argument_list|(
name|noBoundJvmType
argument_list|,
name|noBound
argument_list|)
expr_stmt|;
name|assertEqualTypeVariable
argument_list|(
name|noBoundJvmType
argument_list|,
name|withBounds
argument_list|(
name|noBoundJvmType
argument_list|,
name|Object
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEqualTypeVariable
argument_list|(
name|objectBoundJvmType
argument_list|,
name|objectBound
argument_list|)
expr_stmt|;
name|assertEqualTypeVariable
argument_list|(
name|upperBoundJvmType
argument_list|,
name|upperBound
argument_list|)
expr_stmt|;
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|noBoundJvmType
argument_list|,
name|noBound
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|objectBoundJvmType
argument_list|,
name|objectBound
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|upperBoundJvmType
argument_list|,
name|upperBound
argument_list|,
name|withBounds
argument_list|(
name|upperBoundJvmType
argument_list|,
name|CharSequence
operator|.
name|class
argument_list|)
argument_list|)
comment|// bounds ignored
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|testNewTypeVariable_primitiveTypeBound ()
specifier|public
name|void
name|testNewTypeVariable_primitiveTypeBound
parameter_list|()
block|{
try|try
block|{
name|Types
operator|.
name|newTypeVariable
argument_list|(
name|List
operator|.
name|class
argument_list|,
literal|"E"
argument_list|,
name|int
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
DECL|method|testNewTypeVariable_serializable ()
specifier|public
name|void
name|testNewTypeVariable_serializable
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|SerializableTester
operator|.
name|reserialize
argument_list|(
name|Types
operator|.
name|newTypeVariable
argument_list|(
name|List
operator|.
name|class
argument_list|,
literal|"E"
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|withBounds ( TypeVariable<D> typeVariable, Type... bounds)
specifier|private
specifier|static
parameter_list|<
name|D
extends|extends
name|GenericDeclaration
parameter_list|>
name|TypeVariable
argument_list|<
name|D
argument_list|>
name|withBounds
parameter_list|(
name|TypeVariable
argument_list|<
name|D
argument_list|>
name|typeVariable
parameter_list|,
name|Type
modifier|...
name|bounds
parameter_list|)
block|{
return|return
name|Types
operator|.
name|newTypeVariable
argument_list|(
name|typeVariable
operator|.
name|getGenericDeclaration
argument_list|()
argument_list|,
name|typeVariable
operator|.
name|getName
argument_list|()
argument_list|,
name|bounds
argument_list|)
return|;
block|}
DECL|method|assertEqualTypeVariable ( TypeVariable<?> expected, TypeVariable<?> actual)
specifier|private
specifier|static
name|void
name|assertEqualTypeVariable
parameter_list|(
name|TypeVariable
argument_list|<
name|?
argument_list|>
name|expected
parameter_list|,
name|TypeVariable
argument_list|<
name|?
argument_list|>
name|actual
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|expected
operator|.
name|toString
argument_list|()
argument_list|,
name|actual
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|getName
argument_list|()
argument_list|,
name|actual
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|getGenericDeclaration
argument_list|()
argument_list|,
name|actual
operator|.
name|getGenericDeclaration
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|actual
operator|.
name|toString
argument_list|()
argument_list|,
name|expected
operator|.
name|hashCode
argument_list|()
argument_list|,
name|actual
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|actual
operator|.
name|getBounds
argument_list|()
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
name|expected
operator|.
name|getBounds
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Working with arrays requires defensive code. Verify that we clone the    * type array for both input and output.    */
DECL|method|testNewParameterizedTypeImmutability ()
specifier|public
name|void
name|testNewParameterizedTypeImmutability
parameter_list|()
block|{
name|Type
index|[]
name|typesIn
init|=
block|{
name|String
operator|.
name|class
block|,
name|Integer
operator|.
name|class
block|}
decl_stmt|;
name|ParameterizedType
name|parameterizedType
init|=
name|Types
operator|.
name|newParameterizedType
argument_list|(
name|Map
operator|.
name|class
argument_list|,
name|typesIn
argument_list|)
decl_stmt|;
name|typesIn
index|[
literal|0
index|]
operator|=
literal|null
expr_stmt|;
name|typesIn
index|[
literal|1
index|]
operator|=
literal|null
expr_stmt|;
name|Type
index|[]
name|typesOut
init|=
name|parameterizedType
operator|.
name|getActualTypeArguments
argument_list|()
decl_stmt|;
name|typesOut
index|[
literal|0
index|]
operator|=
literal|null
expr_stmt|;
name|typesOut
index|[
literal|1
index|]
operator|=
literal|null
expr_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|parameterizedType
operator|.
name|getActualTypeArguments
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
name|parameterizedType
operator|.
name|getActualTypeArguments
argument_list|()
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
DECL|method|testNewParameterizedTypeWithWrongNumberOfTypeArguments ()
specifier|public
name|void
name|testNewParameterizedTypeWithWrongNumberOfTypeArguments
parameter_list|()
block|{
try|try
block|{
name|Types
operator|.
name|newParameterizedType
argument_list|(
name|Map
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
name|Long
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
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|int
index|[]
operator|.
expr|class
operator|.
name|getName
argument_list|()
argument_list|,
name|Types
operator|.
name|toString
argument_list|(
name|int
index|[]
operator|.
expr|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|int
index|[]
index|[]
operator|.
expr|class
operator|.
name|getName
argument_list|()
argument_list|,
name|Types
operator|.
name|toString
argument_list|(
name|int
index|[]
index|[]
operator|.
expr|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|String
index|[]
operator|.
expr|class
operator|.
name|getName
argument_list|()
argument_list|,
name|Types
operator|.
name|toString
argument_list|(
name|String
index|[]
operator|.
expr|class
argument_list|)
argument_list|)
expr_stmt|;
name|Type
name|elementType
init|=
name|List
operator|.
name|class
operator|.
name|getTypeParameters
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
name|assertEquals
argument_list|(
name|elementType
operator|.
name|toString
argument_list|()
argument_list|,
name|Types
operator|.
name|toString
argument_list|(
name|elementType
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testNullPointers ()
specifier|public
name|void
name|testNullPointers
parameter_list|()
throws|throws
name|Exception
block|{
operator|new
name|NullPointerTester
argument_list|()
operator|.
name|setDefault
argument_list|(
name|Type
index|[]
operator|.
expr|class
argument_list|,
operator|new
name|Type
index|[]
block|{
name|Map
operator|.
name|class
block|}
argument_list|)
operator|.
name|setDefault
argument_list|(
name|Type
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|setDefault
argument_list|(
name|GenericDeclaration
operator|.
name|class
argument_list|,
name|Types
operator|.
name|class
argument_list|)
operator|.
name|testStaticMethods
argument_list|(
name|Types
operator|.
name|class
argument_list|,
name|Visibility
operator|.
name|PACKAGE
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

