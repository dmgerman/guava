begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2009 Google Inc. All Rights Reserved.
end_comment

begin_package
DECL|package|com.google.common.collect.testing.testers
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
name|testers
package|;
end_package

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

begin_comment
comment|/**  * This class is emulated in GWT.  *  * @author hhchan@google.com (Hayward Chan)  */
end_comment

begin_class
DECL|class|Platform
class|class
name|Platform
block|{
comment|/**    * Delegate to {@link Class#getMethod(String, Class[])}.  Not    * usable in GWT.    */
DECL|method|getMethod (Class<?> clazz, String methodName)
specifier|static
name|Method
name|getMethod
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|,
name|String
name|methodName
parameter_list|)
block|{
try|try
block|{
return|return
name|clazz
operator|.
name|getMethod
argument_list|(
name|methodName
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**    * Format the template with args, only supports the placeholder    * {@code %s}.    */
DECL|method|format (String template, Object... args)
specifier|static
name|String
name|format
parameter_list|(
name|String
name|template
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
name|template
argument_list|,
name|args
argument_list|)
return|;
block|}
comment|/** See {@link ListListIteratorTester} */
DECL|method|listListIteratorTesterNumIterations ()
specifier|static
name|int
name|listListIteratorTesterNumIterations
parameter_list|()
block|{
return|return
literal|4
return|;
block|}
comment|/** See {@link CollectionIteratorTester} */
DECL|method|collectionIteratorTesterNumIterations ()
specifier|static
name|int
name|collectionIteratorTesterNumIterations
parameter_list|()
block|{
return|return
literal|5
return|;
block|}
block|}
end_class

end_unit

