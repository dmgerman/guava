begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2016 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|checkState
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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|RequiredModifiers
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
name|ElementType
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
name|annotation
operator|.
name|Target
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
name|Method
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|lang
operator|.
name|model
operator|.
name|element
operator|.
name|Modifier
import|;
end_import

begin_comment
comment|/**  * Tester of subtyping relationships between two types.  *  * Tests should inherit from this class, and declare subtyping relationship with public methods  * annotated by {@link TestSubtype}.  *  *<p>These declaration methods rely on Java static type checking to make sure what we want to  * assert as subtypes are really subtypes according to javac. For example:  *<pre>   {@code  *  *   class MySubtypeTests extends SubtypeTester {  *     @TestSubtype(suppressGetSubtype = true, suppressGetSupertype = true)  *     public<T> Iterable<? extends T> listIsSubtypeOfIterable(List<T> list) {  *       return isSubtype(list);  *     }  *  *     @TestSubtype  *     public List<String> intListIsNotSubtypeOfStringList(List<Integer> intList) {  *       return notSubtype(intList);  *     }  *   }  *  *   public void testMySubtypes() throws Exception {  *     new MySubtypeTests().testAllDeclarations();  *   }  * }</pre>  *  * The calls to {@link #isSubtype} and {@link #notSubtype} tells the framework what assertions need  * to be made.  *  *<p>The declaration methods must be public.  */
end_comment

begin_class
annotation|@
name|AndroidIncompatible
comment|// only used by android incompatible tests.
DECL|class|SubtypeTester
specifier|abstract
class|class
name|SubtypeTester
implements|implements
name|Cloneable
block|{
comment|/** Annotates a public method that declares subtype assertion. */
annotation|@
name|RequiredModifiers
argument_list|(
name|Modifier
operator|.
name|PUBLIC
argument_list|)
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
annotation|@
name|Target
argument_list|(
name|ElementType
operator|.
name|METHOD
argument_list|)
DECL|annotation|TestSubtype
annotation_defn|@interface
name|TestSubtype
block|{
comment|/** Suppresses the assertion on {@link TypeToken#getSubtype}. */
DECL|method|suppressGetSubtype ()
DECL|field|false
name|boolean
name|suppressGetSubtype
parameter_list|()
default|default
literal|false
function_decl|;
comment|/** Suppresses the assertion on {@link TypeToken#getSupertype}. */
DECL|method|suppressGetSupertype ()
DECL|field|false
name|boolean
name|suppressGetSupertype
parameter_list|()
default|default
literal|false
function_decl|;
block|}
DECL|field|method
specifier|private
name|Method
name|method
init|=
literal|null
decl_stmt|;
comment|/** Call this in a {@link TestSubtype} public method asserting subtype relationship. */
DECL|method|isSubtype (T sub)
specifier|final
parameter_list|<
name|T
parameter_list|>
name|T
name|isSubtype
parameter_list|(
name|T
name|sub
parameter_list|)
block|{
name|Type
name|returnType
init|=
name|method
operator|.
name|getGenericReturnType
argument_list|()
decl_stmt|;
name|Type
name|paramType
init|=
name|getOnlyParameterType
argument_list|()
decl_stmt|;
name|TestSubtype
name|spec
init|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|TestSubtype
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|TypeToken
operator|.
name|of
argument_list|(
name|paramType
argument_list|)
operator|.
name|isSubtypeOf
argument_list|(
name|returnType
argument_list|)
argument_list|)
operator|.
name|named
argument_list|(
literal|"%s is subtype of %s"
argument_list|,
name|paramType
argument_list|,
name|returnType
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|TypeToken
operator|.
name|of
argument_list|(
name|returnType
argument_list|)
operator|.
name|isSupertypeOf
argument_list|(
name|paramType
argument_list|)
argument_list|)
operator|.
name|named
argument_list|(
literal|"%s is supertype of %s"
argument_list|,
name|returnType
argument_list|,
name|paramType
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|spec
operator|.
name|suppressGetSubtype
argument_list|()
condition|)
block|{
name|assertThat
argument_list|(
name|getSubtype
argument_list|(
name|returnType
argument_list|,
name|TypeToken
operator|.
name|of
argument_list|(
name|paramType
argument_list|)
operator|.
name|getRawType
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|paramType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|spec
operator|.
name|suppressGetSupertype
argument_list|()
condition|)
block|{
name|assertThat
argument_list|(
name|getSupertype
argument_list|(
name|paramType
argument_list|,
name|TypeToken
operator|.
name|of
argument_list|(
name|returnType
argument_list|)
operator|.
name|getRawType
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|returnType
argument_list|)
expr_stmt|;
block|}
return|return
name|sub
return|;
block|}
comment|/**    * Call this in a {@link TestSubtype} public method asserting that subtype relationship does not    * hold.    */
DECL|method|notSubtype (@uppressWarningsR) Object sub)
specifier|final
parameter_list|<
name|X
parameter_list|>
name|X
name|notSubtype
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|Object
name|sub
parameter_list|)
block|{
name|Type
name|returnType
init|=
name|method
operator|.
name|getGenericReturnType
argument_list|()
decl_stmt|;
name|Type
name|paramType
init|=
name|getOnlyParameterType
argument_list|()
decl_stmt|;
name|TestSubtype
name|spec
init|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|TestSubtype
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|TypeToken
operator|.
name|of
argument_list|(
name|paramType
argument_list|)
operator|.
name|isSubtypeOf
argument_list|(
name|returnType
argument_list|)
argument_list|)
operator|.
name|named
argument_list|(
literal|"%s is subtype of %s"
argument_list|,
name|paramType
argument_list|,
name|returnType
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|TypeToken
operator|.
name|of
argument_list|(
name|returnType
argument_list|)
operator|.
name|isSupertypeOf
argument_list|(
name|paramType
argument_list|)
argument_list|)
operator|.
name|named
argument_list|(
literal|"%s is supertype of %s"
argument_list|,
name|returnType
argument_list|,
name|paramType
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|spec
operator|.
name|suppressGetSubtype
argument_list|()
condition|)
block|{
try|try
block|{
name|assertThat
argument_list|(
name|getSubtype
argument_list|(
name|returnType
argument_list|,
name|TypeToken
operator|.
name|of
argument_list|(
name|paramType
argument_list|)
operator|.
name|getRawType
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|paramType
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|notSubtype1
parameter_list|)
block|{
comment|// The raw class isn't even a subclass.
block|}
block|}
if|if
condition|(
operator|!
name|spec
operator|.
name|suppressGetSupertype
argument_list|()
condition|)
block|{
try|try
block|{
name|assertThat
argument_list|(
name|getSupertype
argument_list|(
name|paramType
argument_list|,
name|TypeToken
operator|.
name|of
argument_list|(
name|returnType
argument_list|)
operator|.
name|getRawType
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|returnType
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|notSubtype2
parameter_list|)
block|{
comment|// The raw class isn't even a subclass.
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|testAllDeclarations ()
specifier|final
name|void
name|testAllDeclarations
parameter_list|()
throws|throws
name|Exception
block|{
name|checkState
argument_list|(
name|method
operator|==
literal|null
argument_list|)
expr_stmt|;
name|Method
index|[]
name|methods
init|=
name|getClass
argument_list|()
operator|.
name|getMethods
argument_list|()
decl_stmt|;
name|Arrays
operator|.
name|sort
argument_list|(
name|methods
argument_list|,
operator|new
name|Comparator
argument_list|<
name|Method
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|Method
name|a
parameter_list|,
name|Method
name|b
parameter_list|)
block|{
return|return
name|a
operator|.
name|getName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|b
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
for|for
control|(
name|Method
name|method
range|:
name|methods
control|)
block|{
if|if
condition|(
name|method
operator|.
name|isAnnotationPresent
argument_list|(
name|TestSubtype
operator|.
name|class
argument_list|)
condition|)
block|{
name|method
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|SubtypeTester
name|tester
init|=
operator|(
name|SubtypeTester
operator|)
name|clone
argument_list|()
decl_stmt|;
name|tester
operator|.
name|method
operator|=
name|method
expr_stmt|;
name|method
operator|.
name|invoke
argument_list|(
name|tester
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|null
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getOnlyParameterType ()
specifier|private
name|Type
name|getOnlyParameterType
parameter_list|()
block|{
name|assertThat
argument_list|(
name|method
operator|.
name|getGenericParameterTypes
argument_list|()
argument_list|)
operator|.
name|hasLength
argument_list|(
literal|1
argument_list|)
expr_stmt|;
return|return
name|method
operator|.
name|getGenericParameterTypes
argument_list|()
index|[
literal|0
index|]
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
DECL|method|getSupertype (Type type, Class<?> superclass)
specifier|private
specifier|static
name|Type
name|getSupertype
parameter_list|(
name|Type
name|type
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|superclass
parameter_list|)
block|{
name|Class
name|rawType
init|=
name|superclass
decl_stmt|;
return|return
name|TypeToken
operator|.
name|of
argument_list|(
name|type
argument_list|)
operator|.
name|getSupertype
argument_list|(
name|rawType
argument_list|)
operator|.
name|getType
argument_list|()
return|;
block|}
DECL|method|getSubtype (Type type, Class<?> subclass)
specifier|private
specifier|static
name|Type
name|getSubtype
parameter_list|(
name|Type
name|type
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|subclass
parameter_list|)
block|{
return|return
name|TypeToken
operator|.
name|of
argument_list|(
name|type
argument_list|)
operator|.
name|getSubtype
argument_list|(
name|subclass
argument_list|)
operator|.
name|getType
argument_list|()
return|;
block|}
block|}
end_class

end_unit

