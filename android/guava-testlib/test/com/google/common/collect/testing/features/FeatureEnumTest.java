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
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
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
name|util
operator|.
name|Locale
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
comment|/**  * Since annotations have some reusability issues that force copy and paste  * all over the place, it's worth having a test to ensure that all our Feature  * enums have their annotations correctly set up.  *  * @author George van den Driessche  */
end_comment

begin_class
DECL|class|FeatureEnumTest
specifier|public
class|class
name|FeatureEnumTest
extends|extends
name|TestCase
block|{
DECL|method|assertGoodTesterAnnotation ( Class<? extends Annotation> annotationClass)
specifier|private
specifier|static
name|void
name|assertGoodTesterAnnotation
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotationClass
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|rootLocaleFormat
argument_list|(
literal|"%s must be annotated with @TesterAnnotation."
argument_list|,
name|annotationClass
argument_list|)
argument_list|,
name|annotationClass
operator|.
name|getAnnotation
argument_list|(
name|TesterAnnotation
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|Retention
name|retentionPolicy
init|=
name|annotationClass
operator|.
name|getAnnotation
argument_list|(
name|Retention
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|rootLocaleFormat
argument_list|(
literal|"%s must have a @Retention annotation."
argument_list|,
name|annotationClass
argument_list|)
argument_list|,
name|retentionPolicy
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|rootLocaleFormat
argument_list|(
literal|"%s must have RUNTIME RetentionPolicy."
argument_list|,
name|annotationClass
argument_list|)
argument_list|,
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|,
name|retentionPolicy
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|rootLocaleFormat
argument_list|(
literal|"%s must be inherited."
argument_list|,
name|annotationClass
argument_list|)
argument_list|,
name|annotationClass
operator|.
name|getAnnotation
argument_list|(
name|Inherited
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|propertyName
range|:
operator|new
name|String
index|[]
block|{
literal|"value"
block|,
literal|"absent"
block|}
control|)
block|{
name|Method
name|method
init|=
literal|null
decl_stmt|;
try|try
block|{
name|method
operator|=
name|annotationClass
operator|.
name|getMethod
argument_list|(
name|propertyName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
name|rootLocaleFormat
argument_list|(
literal|"%s must have a property named '%s'."
argument_list|,
name|annotationClass
argument_list|,
name|propertyName
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|returnType
init|=
name|method
operator|.
name|getReturnType
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|rootLocaleFormat
argument_list|(
literal|"%s.%s() must return an array."
argument_list|,
name|annotationClass
argument_list|,
name|propertyName
argument_list|)
argument_list|,
name|returnType
operator|.
name|isArray
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|rootLocaleFormat
argument_list|(
literal|"%s.%s() must return an array of %s."
argument_list|,
name|annotationClass
argument_list|,
name|propertyName
argument_list|,
name|annotationClass
operator|.
name|getDeclaringClass
argument_list|()
argument_list|)
argument_list|,
name|annotationClass
operator|.
name|getDeclaringClass
argument_list|()
argument_list|,
name|returnType
operator|.
name|getComponentType
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// This is public so that tests for Feature enums we haven't yet imagined
comment|// can reuse it.
DECL|method|assertGoodFeatureEnum ( Class<E> featureEnumClass)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Enum
argument_list|<
name|?
argument_list|>
operator|&
name|Feature
argument_list|<
name|?
argument_list|>
parameter_list|>
name|void
name|assertGoodFeatureEnum
parameter_list|(
name|Class
argument_list|<
name|E
argument_list|>
name|featureEnumClass
parameter_list|)
block|{
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|classes
init|=
name|featureEnumClass
operator|.
name|getDeclaredClasses
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|containedClass
range|:
name|classes
control|)
block|{
if|if
condition|(
name|containedClass
operator|.
name|getSimpleName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"Require"
argument_list|)
condition|)
block|{
if|if
condition|(
name|containedClass
operator|.
name|isAnnotation
argument_list|()
condition|)
block|{
name|assertGoodTesterAnnotation
argument_list|(
name|asAnnotation
argument_list|(
name|containedClass
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fail
argument_list|(
name|rootLocaleFormat
argument_list|(
literal|"Feature enum %s contains a class named "
operator|+
literal|"'Require' but it is not an annotation."
argument_list|,
name|featureEnumClass
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
block|}
name|fail
argument_list|(
name|rootLocaleFormat
argument_list|(
literal|"Feature enum %s should contain an "
operator|+
literal|"annotation named 'Require'."
argument_list|,
name|featureEnumClass
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|asAnnotation (Class<?> clazz)
specifier|private
specifier|static
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|asAnnotation
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
if|if
condition|(
name|clazz
operator|.
name|isAnnotation
argument_list|()
condition|)
block|{
return|return
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
operator|)
name|clazz
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|rootLocaleFormat
argument_list|(
literal|"%s is not an annotation."
argument_list|,
name|clazz
argument_list|)
argument_list|)
throw|;
block|}
block|}
DECL|method|testFeatureEnums ()
specifier|public
name|void
name|testFeatureEnums
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGoodFeatureEnum
argument_list|(
name|CollectionFeature
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertGoodFeatureEnum
argument_list|(
name|ListFeature
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertGoodFeatureEnum
argument_list|(
name|SetFeature
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertGoodFeatureEnum
argument_list|(
name|CollectionSize
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertGoodFeatureEnum
argument_list|(
name|MapFeature
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
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
block|}
end_class

end_unit
