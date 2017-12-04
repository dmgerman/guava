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
name|lang
operator|.
name|reflect
operator|.
name|Constructor
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

begin_comment
comment|/**  * Unit tests of {@link Element}.  *  * @author Ben Yu  */
end_comment

begin_class
DECL|class|ElementTest
specifier|public
class|class
name|ElementTest
extends|extends
name|TestCase
block|{
DECL|method|testPrivateField ()
specifier|public
name|void
name|testPrivateField
parameter_list|()
throws|throws
name|Exception
block|{
name|Element
name|element
init|=
name|A
operator|.
name|field
argument_list|(
literal|"privateField"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isPrivate
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isAbstract
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isPackagePrivate
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isProtected
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isPublic
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isFinal
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isStatic
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isAnnotationPresent
argument_list|(
name|Tested
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testPackagePrivateField ()
specifier|public
name|void
name|testPackagePrivateField
parameter_list|()
throws|throws
name|Exception
block|{
name|Element
name|element
init|=
name|A
operator|.
name|field
argument_list|(
literal|"packagePrivateField"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isPrivate
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isPackagePrivate
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isProtected
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isPublic
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isFinal
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isStatic
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isAnnotationPresent
argument_list|(
name|Tested
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testProtectedField ()
specifier|public
name|void
name|testProtectedField
parameter_list|()
throws|throws
name|Exception
block|{
name|Element
name|element
init|=
name|A
operator|.
name|field
argument_list|(
literal|"protectedField"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isPrivate
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isPackagePrivate
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isProtected
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isPublic
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isFinal
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isStatic
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isAnnotationPresent
argument_list|(
name|Tested
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testPublicField ()
specifier|public
name|void
name|testPublicField
parameter_list|()
throws|throws
name|Exception
block|{
name|Element
name|element
init|=
name|A
operator|.
name|field
argument_list|(
literal|"publicField"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isPrivate
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isPackagePrivate
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isProtected
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isPublic
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isFinal
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isStatic
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isAnnotationPresent
argument_list|(
name|Tested
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testFinalField ()
specifier|public
name|void
name|testFinalField
parameter_list|()
throws|throws
name|Exception
block|{
name|Element
name|element
init|=
name|A
operator|.
name|field
argument_list|(
literal|"finalField"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isFinal
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isStatic
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isAnnotationPresent
argument_list|(
name|Tested
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testStaticField ()
specifier|public
name|void
name|testStaticField
parameter_list|()
throws|throws
name|Exception
block|{
name|Element
name|element
init|=
name|A
operator|.
name|field
argument_list|(
literal|"staticField"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isStatic
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isAnnotationPresent
argument_list|(
name|Tested
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testVolatileField ()
specifier|public
name|void
name|testVolatileField
parameter_list|()
throws|throws
name|Exception
block|{
name|Element
name|element
init|=
name|A
operator|.
name|field
argument_list|(
literal|"volatileField"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isVolatile
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testTransientField ()
specifier|public
name|void
name|testTransientField
parameter_list|()
throws|throws
name|Exception
block|{
name|Element
name|element
init|=
name|A
operator|.
name|field
argument_list|(
literal|"transientField"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isTransient
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testConstructor ()
specifier|public
name|void
name|testConstructor
parameter_list|()
throws|throws
name|Exception
block|{
name|Element
name|element
init|=
name|A
operator|.
name|constructor
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isPublic
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isPackagePrivate
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isAbstract
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isStatic
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isAnnotationPresent
argument_list|(
name|Tested
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testAbstractMethod ()
specifier|public
name|void
name|testAbstractMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|Element
name|element
init|=
name|A
operator|.
name|method
argument_list|(
literal|"abstractMethod"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isPackagePrivate
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isAbstract
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isFinal
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isAnnotationPresent
argument_list|(
name|Tested
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testOverridableMethod ()
specifier|public
name|void
name|testOverridableMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|Element
name|element
init|=
name|A
operator|.
name|method
argument_list|(
literal|"overridableMethod"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isPackagePrivate
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isAbstract
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isFinal
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isAnnotationPresent
argument_list|(
name|Tested
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testPrivateMethod ()
specifier|public
name|void
name|testPrivateMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|Element
name|element
init|=
name|A
operator|.
name|method
argument_list|(
literal|"privateMethod"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isAbstract
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isPrivate
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isPackagePrivate
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isPublic
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isProtected
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isAnnotationPresent
argument_list|(
name|Tested
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testProtectedMethod ()
specifier|public
name|void
name|testProtectedMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|Element
name|element
init|=
name|A
operator|.
name|method
argument_list|(
literal|"protectedMethod"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isAbstract
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isPrivate
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isPackagePrivate
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isFinal
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isPublic
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isProtected
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isAnnotationPresent
argument_list|(
name|Tested
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testFinalMethod ()
specifier|public
name|void
name|testFinalMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|Element
name|element
init|=
name|A
operator|.
name|method
argument_list|(
literal|"publicFinalMethod"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isAbstract
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isPrivate
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isFinal
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isPublic
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isAnnotationPresent
argument_list|(
name|Tested
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testNativeMethod ()
specifier|public
name|void
name|testNativeMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|Element
name|element
init|=
name|A
operator|.
name|method
argument_list|(
literal|"nativeMethod"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isNative
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isPackagePrivate
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSynchronizedMethod ()
specifier|public
name|void
name|testSynchronizedMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|Element
name|element
init|=
name|A
operator|.
name|method
argument_list|(
literal|"synchronizedMethod"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|element
operator|.
name|isSynchronized
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testUnannotatedMethod ()
specifier|public
name|void
name|testUnannotatedMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|Element
name|element
init|=
name|A
operator|.
name|method
argument_list|(
literal|"notAnnotatedMethod"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|element
operator|.
name|isAnnotationPresent
argument_list|(
name|Tested
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testEquals ()
specifier|public
name|void
name|testEquals
parameter_list|()
throws|throws
name|Exception
block|{
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|A
operator|.
name|field
argument_list|(
literal|"privateField"
argument_list|)
argument_list|,
name|A
operator|.
name|field
argument_list|(
literal|"privateField"
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|A
operator|.
name|field
argument_list|(
literal|"publicField"
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|A
operator|.
name|constructor
argument_list|()
argument_list|,
name|A
operator|.
name|constructor
argument_list|()
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|A
operator|.
name|method
argument_list|(
literal|"privateMethod"
argument_list|)
argument_list|,
name|A
operator|.
name|method
argument_list|(
literal|"privateMethod"
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|A
operator|.
name|method
argument_list|(
literal|"publicFinalMethod"
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|testNulls ()
specifier|public
name|void
name|testNulls
parameter_list|()
block|{
operator|new
name|NullPointerTester
argument_list|()
operator|.
name|testAllPublicStaticMethods
argument_list|(
name|Element
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
DECL|annotation|Tested
specifier|private
annotation_defn|@interface
name|Tested
block|{}
DECL|class|A
specifier|private
specifier|abstract
specifier|static
class|class
name|A
block|{
DECL|field|privateField
annotation|@
name|Tested
specifier|private
name|boolean
name|privateField
decl_stmt|;
DECL|field|packagePrivateField
annotation|@
name|Tested
name|int
name|packagePrivateField
decl_stmt|;
DECL|field|protectedField
annotation|@
name|Tested
specifier|protected
name|int
name|protectedField
decl_stmt|;
DECL|field|publicField
annotation|@
name|Tested
specifier|public
name|String
name|publicField
decl_stmt|;
DECL|field|staticField
annotation|@
name|Tested
specifier|private
specifier|static
name|Iterable
argument_list|<
name|String
argument_list|>
name|staticField
decl_stmt|;
DECL|field|finalField
annotation|@
name|Tested
specifier|private
specifier|final
name|Object
name|finalField
decl_stmt|;
DECL|field|volatileField
specifier|private
specifier|volatile
name|char
name|volatileField
decl_stmt|;
DECL|field|transientField
specifier|private
specifier|transient
name|long
name|transientField
decl_stmt|;
annotation|@
name|Tested
DECL|method|A (Object finalField)
specifier|public
name|A
parameter_list|(
name|Object
name|finalField
parameter_list|)
block|{
name|this
operator|.
name|finalField
operator|=
name|finalField
expr_stmt|;
block|}
annotation|@
name|Tested
DECL|method|abstractMethod ()
specifier|abstract
name|void
name|abstractMethod
parameter_list|()
function_decl|;
annotation|@
name|Tested
DECL|method|overridableMethod ()
name|void
name|overridableMethod
parameter_list|()
block|{}
annotation|@
name|Tested
DECL|method|protectedMethod ()
specifier|protected
name|void
name|protectedMethod
parameter_list|()
block|{}
annotation|@
name|Tested
DECL|method|privateMethod ()
specifier|private
name|void
name|privateMethod
parameter_list|()
block|{}
annotation|@
name|Tested
DECL|method|publicFinalMethod ()
specifier|public
specifier|final
name|void
name|publicFinalMethod
parameter_list|()
block|{}
DECL|method|notAnnotatedMethod ()
name|void
name|notAnnotatedMethod
parameter_list|()
block|{}
DECL|method|field (String name)
specifier|static
name|Element
name|field
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|Element
name|element
init|=
operator|new
name|Element
argument_list|(
name|A
operator|.
name|class
operator|.
name|getDeclaredField
argument_list|(
name|name
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|name
argument_list|,
name|element
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|A
operator|.
name|class
argument_list|,
name|element
operator|.
name|getDeclaringClass
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|element
return|;
block|}
DECL|method|constructor ()
specifier|static
name|Element
name|constructor
parameter_list|()
throws|throws
name|Exception
block|{
name|Constructor
argument_list|<
name|?
argument_list|>
name|constructor
init|=
name|A
operator|.
name|class
operator|.
name|getDeclaredConstructor
argument_list|(
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
name|Element
name|element
init|=
operator|new
name|Element
argument_list|(
name|constructor
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|constructor
operator|.
name|getName
argument_list|()
argument_list|,
name|element
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|A
operator|.
name|class
argument_list|,
name|element
operator|.
name|getDeclaringClass
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|element
return|;
block|}
DECL|method|method (String name, Class<?>... parameterTypes)
specifier|static
name|Element
name|method
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|parameterTypes
parameter_list|)
throws|throws
name|Exception
block|{
name|Element
name|element
init|=
operator|new
name|Element
argument_list|(
name|A
operator|.
name|class
operator|.
name|getDeclaredMethod
argument_list|(
name|name
argument_list|,
name|parameterTypes
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|name
argument_list|,
name|element
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|A
operator|.
name|class
argument_list|,
name|element
operator|.
name|getDeclaringClass
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|element
return|;
block|}
DECL|method|nativeMethod ()
specifier|native
name|void
name|nativeMethod
parameter_list|()
function_decl|;
DECL|method|synchronizedMethod ()
specifier|synchronized
name|void
name|synchronizedMethod
parameter_list|()
block|{}
block|}
block|}
end_class

end_unit

