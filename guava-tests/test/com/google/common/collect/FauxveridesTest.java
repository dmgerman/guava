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
name|collect
operator|.
name|Lists
operator|.
name|transform
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
name|collect
operator|.
name|Sets
operator|.
name|difference
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
name|collect
operator|.
name|Sets
operator|.
name|newHashSet
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Modifier
operator|.
name|isPublic
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Modifier
operator|.
name|isStatic
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
name|Function
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
name|Joiner
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Tests that all {@code public static} methods "inherited" from superclasses are "overridden" in  * each immutable-collection class. This ensures, for example, that a call written "{@code  * ImmutableSortedSet.copyOf()}" cannot secretly be a call to {@code ImmutableSet.copyOf()}.  *  * @author Chris Povirk  */
end_comment

begin_class
DECL|class|FauxveridesTest
specifier|public
class|class
name|FauxveridesTest
extends|extends
name|TestCase
block|{
DECL|method|testImmutableBiMap ()
specifier|public
name|void
name|testImmutableBiMap
parameter_list|()
block|{
name|doHasAllFauxveridesTest
argument_list|(
name|ImmutableBiMap
operator|.
name|class
argument_list|,
name|ImmutableMap
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testImmutableListMultimap ()
specifier|public
name|void
name|testImmutableListMultimap
parameter_list|()
block|{
name|doHasAllFauxveridesTest
argument_list|(
name|ImmutableListMultimap
operator|.
name|class
argument_list|,
name|ImmutableMultimap
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testImmutableSetMultimap ()
specifier|public
name|void
name|testImmutableSetMultimap
parameter_list|()
block|{
name|doHasAllFauxveridesTest
argument_list|(
name|ImmutableSetMultimap
operator|.
name|class
argument_list|,
name|ImmutableMultimap
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testImmutableSortedMap ()
specifier|public
name|void
name|testImmutableSortedMap
parameter_list|()
block|{
name|doHasAllFauxveridesTest
argument_list|(
name|ImmutableSortedMap
operator|.
name|class
argument_list|,
name|ImmutableMap
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testImmutableSortedSet ()
specifier|public
name|void
name|testImmutableSortedSet
parameter_list|()
block|{
name|doHasAllFauxveridesTest
argument_list|(
name|ImmutableSortedSet
operator|.
name|class
argument_list|,
name|ImmutableSet
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testImmutableSortedMultiset ()
specifier|public
name|void
name|testImmutableSortedMultiset
parameter_list|()
block|{
name|doHasAllFauxveridesTest
argument_list|(
name|ImmutableSortedMultiset
operator|.
name|class
argument_list|,
name|ImmutableMultiset
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|/*    * Demonstrate that ClassCastException is possible when calling    * ImmutableSorted{Set,Map}.copyOf(), whose type parameters we are unable to    * restrict (see ImmutableSortedSetFauxverideShim).    */
DECL|method|testImmutableSortedMapCopyOfMap ()
specifier|public
name|void
name|testImmutableSortedMapCopyOfMap
parameter_list|()
block|{
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|original
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|,
operator|new
name|Object
argument_list|()
argument_list|,
operator|new
name|Object
argument_list|()
argument_list|,
operator|new
name|Object
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|ImmutableSortedMap
operator|.
name|copyOf
argument_list|(
name|original
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testImmutableSortedSetCopyOfIterable ()
specifier|public
name|void
name|testImmutableSortedSetCopyOfIterable
parameter_list|()
block|{
name|Set
argument_list|<
name|Object
argument_list|>
name|original
init|=
name|ImmutableSet
operator|.
name|of
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|,
operator|new
name|Object
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|ImmutableSortedSet
operator|.
name|copyOf
argument_list|(
name|original
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testImmutableSortedSetCopyOfIterator ()
specifier|public
name|void
name|testImmutableSortedSetCopyOfIterator
parameter_list|()
block|{
name|Set
argument_list|<
name|Object
argument_list|>
name|original
init|=
name|ImmutableSet
operator|.
name|of
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|,
operator|new
name|Object
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|ImmutableSortedSet
operator|.
name|copyOf
argument_list|(
name|original
operator|.
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|doHasAllFauxveridesTest (Class<?> descendant, Class<?> ancestor)
specifier|private
name|void
name|doHasAllFauxveridesTest
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|descendant
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|ancestor
parameter_list|)
block|{
name|Set
argument_list|<
name|MethodSignature
argument_list|>
name|required
init|=
name|getAllRequiredToFauxveride
argument_list|(
name|ancestor
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|MethodSignature
argument_list|>
name|found
init|=
name|getAllFauxveridden
argument_list|(
name|descendant
argument_list|,
name|ancestor
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|MethodSignature
argument_list|>
name|missing
init|=
name|ImmutableSortedSet
operator|.
name|copyOf
argument_list|(
name|difference
argument_list|(
name|required
argument_list|,
name|found
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|missing
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|fail
argument_list|(
name|rootLocaleFormat
argument_list|(
literal|"%s should hide the public static methods declared in %s: %s"
argument_list|,
name|descendant
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|ancestor
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|missing
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getAllRequiredToFauxveride (Class<?> ancestor)
specifier|private
specifier|static
name|Set
argument_list|<
name|MethodSignature
argument_list|>
name|getAllRequiredToFauxveride
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|ancestor
parameter_list|)
block|{
return|return
name|getPublicStaticMethodsBetween
argument_list|(
name|ancestor
argument_list|,
name|Object
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getAllFauxveridden (Class<?> descendant, Class<?> ancestor)
specifier|private
specifier|static
name|Set
argument_list|<
name|MethodSignature
argument_list|>
name|getAllFauxveridden
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|descendant
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|ancestor
parameter_list|)
block|{
return|return
name|getPublicStaticMethodsBetween
argument_list|(
name|descendant
argument_list|,
name|ancestor
argument_list|)
return|;
block|}
DECL|method|getPublicStaticMethodsBetween ( Class<?> descendant, Class<?> ancestor)
specifier|private
specifier|static
name|Set
argument_list|<
name|MethodSignature
argument_list|>
name|getPublicStaticMethodsBetween
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|descendant
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|ancestor
parameter_list|)
block|{
name|Set
argument_list|<
name|MethodSignature
argument_list|>
name|methods
init|=
name|newHashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
range|:
name|getClassesBetween
argument_list|(
name|descendant
argument_list|,
name|ancestor
argument_list|)
control|)
block|{
name|methods
operator|.
name|addAll
argument_list|(
name|getPublicStaticMethods
argument_list|(
name|clazz
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|methods
return|;
block|}
DECL|method|getPublicStaticMethods (Class<?> clazz)
specifier|private
specifier|static
name|Set
argument_list|<
name|MethodSignature
argument_list|>
name|getPublicStaticMethods
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|Set
argument_list|<
name|MethodSignature
argument_list|>
name|publicStaticMethods
init|=
name|newHashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|Method
name|method
range|:
name|clazz
operator|.
name|getDeclaredMethods
argument_list|()
control|)
block|{
name|int
name|modifiers
init|=
name|method
operator|.
name|getModifiers
argument_list|()
decl_stmt|;
if|if
condition|(
name|isPublic
argument_list|(
name|modifiers
argument_list|)
operator|&&
name|isStatic
argument_list|(
name|modifiers
argument_list|)
condition|)
block|{
name|publicStaticMethods
operator|.
name|add
argument_list|(
operator|new
name|MethodSignature
argument_list|(
name|method
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|publicStaticMethods
return|;
block|}
comment|/** [descendant, ancestor) */
DECL|method|getClassesBetween (Class<?> descendant, Class<?> ancestor)
specifier|private
specifier|static
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|getClassesBetween
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|descendant
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|ancestor
parameter_list|)
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
name|newHashSet
argument_list|()
decl_stmt|;
while|while
condition|(
operator|!
name|descendant
operator|.
name|equals
argument_list|(
name|ancestor
argument_list|)
condition|)
block|{
name|classes
operator|.
name|add
argument_list|(
name|descendant
argument_list|)
expr_stmt|;
name|descendant
operator|=
name|descendant
operator|.
name|getSuperclass
argument_list|()
expr_stmt|;
block|}
return|return
name|classes
return|;
block|}
comment|/**    * Not really a signature -- just the parts that affect whether one method is a fauxveride of a    * method from an ancestor class.    *    *<p>See JLS 8.4.2 for the definition of the related "override-equivalent."    */
DECL|class|MethodSignature
specifier|private
specifier|static
specifier|final
class|class
name|MethodSignature
implements|implements
name|Comparable
argument_list|<
name|MethodSignature
argument_list|>
block|{
DECL|field|name
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|parameterTypes
specifier|final
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|parameterTypes
decl_stmt|;
DECL|field|typeSignature
specifier|final
name|TypeSignature
name|typeSignature
decl_stmt|;
DECL|method|MethodSignature (Method method)
name|MethodSignature
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|name
operator|=
name|method
operator|.
name|getName
argument_list|()
expr_stmt|;
name|parameterTypes
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
name|method
operator|.
name|getParameterTypes
argument_list|()
argument_list|)
expr_stmt|;
name|typeSignature
operator|=
operator|new
name|TypeSignature
argument_list|(
name|method
operator|.
name|getTypeParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|MethodSignature
condition|)
block|{
name|MethodSignature
name|other
init|=
operator|(
name|MethodSignature
operator|)
name|obj
decl_stmt|;
return|return
name|name
operator|.
name|equals
argument_list|(
name|other
operator|.
name|name
argument_list|)
operator|&&
name|parameterTypes
operator|.
name|equals
argument_list|(
name|other
operator|.
name|parameterTypes
argument_list|)
operator|&&
name|typeSignature
operator|.
name|equals
argument_list|(
name|other
operator|.
name|typeSignature
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hashCode
argument_list|(
name|name
argument_list|,
name|parameterTypes
argument_list|,
name|typeSignature
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|rootLocaleFormat
argument_list|(
literal|"%s%s(%s)"
argument_list|,
name|typeSignature
argument_list|,
name|name
argument_list|,
name|getTypesString
argument_list|(
name|parameterTypes
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|compareTo (MethodSignature o)
specifier|public
name|int
name|compareTo
parameter_list|(
name|MethodSignature
name|o
parameter_list|)
block|{
return|return
name|toString
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|class|TypeSignature
specifier|private
specifier|static
specifier|final
class|class
name|TypeSignature
block|{
DECL|field|parameterSignatures
specifier|final
name|List
argument_list|<
name|TypeParameterSignature
argument_list|>
name|parameterSignatures
decl_stmt|;
DECL|method|TypeSignature (TypeVariable<Method>[] parameters)
name|TypeSignature
parameter_list|(
name|TypeVariable
argument_list|<
name|Method
argument_list|>
index|[]
name|parameters
parameter_list|)
block|{
name|parameterSignatures
operator|=
name|transform
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|parameters
argument_list|)
argument_list|,
operator|new
name|Function
argument_list|<
name|TypeVariable
argument_list|<
name|?
argument_list|>
argument_list|,
name|TypeParameterSignature
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|TypeParameterSignature
name|apply
parameter_list|(
name|TypeVariable
argument_list|<
name|?
argument_list|>
name|from
parameter_list|)
block|{
return|return
operator|new
name|TypeParameterSignature
argument_list|(
name|from
argument_list|)
return|;
block|}
block|}
block|)
empty_stmt|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|TypeSignature
condition|)
block|{
name|TypeSignature
name|other
init|=
operator|(
name|TypeSignature
operator|)
name|obj
decl_stmt|;
return|return
name|parameterSignatures
operator|.
name|equals
argument_list|(
name|other
operator|.
name|parameterSignatures
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|parameterSignatures
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
operator|(
name|parameterSignatures
operator|.
name|isEmpty
argument_list|()
operator|)
condition|?
literal|""
else|:
literal|"<"
operator|+
name|Joiner
operator|.
name|on
argument_list|(
literal|", "
argument_list|)
operator|.
name|join
argument_list|(
name|parameterSignatures
argument_list|)
operator|+
literal|"> "
return|;
block|}
block|}
end_class

begin_class
DECL|class|TypeParameterSignature
specifier|private
specifier|static
specifier|final
class|class
name|TypeParameterSignature
block|{
DECL|field|name
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|bounds
specifier|final
name|List
argument_list|<
name|Type
argument_list|>
name|bounds
decl_stmt|;
DECL|method|TypeParameterSignature (TypeVariable<?> typeParameter)
name|TypeParameterSignature
parameter_list|(
name|TypeVariable
argument_list|<
name|?
argument_list|>
name|typeParameter
parameter_list|)
block|{
name|name
operator|=
name|typeParameter
operator|.
name|getName
argument_list|()
expr_stmt|;
name|bounds
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
name|typeParameter
operator|.
name|getBounds
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|TypeParameterSignature
condition|)
block|{
name|TypeParameterSignature
name|other
init|=
operator|(
name|TypeParameterSignature
operator|)
name|obj
decl_stmt|;
comment|/*          * The name is here only for display purposes;<E extends Number> and<T          * extends Number> are equivalent.          */
return|return
name|bounds
operator|.
name|equals
argument_list|(
name|other
operator|.
name|bounds
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|bounds
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
operator|(
name|bounds
operator|.
name|equals
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|Object
operator|.
name|class
argument_list|)
argument_list|)
operator|)
condition|?
name|name
else|:
name|name
operator|+
literal|" extends "
operator|+
name|getTypesString
argument_list|(
name|bounds
argument_list|)
return|;
block|}
block|}
end_class

begin_function
DECL|method|getTypesString (List<? extends Type> types)
specifier|private
specifier|static
name|String
name|getTypesString
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|Type
argument_list|>
name|types
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|transform
argument_list|(
name|types
argument_list|,
name|SIMPLE_NAME_GETTER
argument_list|)
decl_stmt|;
return|return
name|Joiner
operator|.
name|on
argument_list|(
literal|", "
argument_list|)
operator|.
name|join
argument_list|(
name|names
argument_list|)
return|;
block|}
end_function

begin_decl_stmt
DECL|field|SIMPLE_NAME_GETTER
specifier|private
specifier|static
specifier|final
name|Function
argument_list|<
name|Type
argument_list|,
name|String
argument_list|>
name|SIMPLE_NAME_GETTER
init|=
operator|new
name|Function
argument_list|<
name|Type
argument_list|,
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|apply
parameter_list|(
name|Type
name|from
parameter_list|)
block|{
if|if
condition|(
name|from
operator|instanceof
name|Class
condition|)
block|{
return|return
operator|(
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|from
operator|)
operator|.
name|getSimpleName
argument_list|()
return|;
block|}
return|return
name|from
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
decl_stmt|;
end_decl_stmt

begin_function
DECL|method|rootLocaleFormat (String format, Object... args)
specifier|private
specifier|static
name|String
name|rootLocaleFormat
parameter_list|(
name|String
name|format
parameter_list|,
name|Object
modifier|...
name|args
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
name|Locale
operator|.
name|ROOT
argument_list|,
name|format
argument_list|,
name|args
argument_list|)
return|;
block|}
end_function

unit|}
end_unit

