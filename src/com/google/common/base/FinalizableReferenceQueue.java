begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.base
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|ref
operator|.
name|Reference
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|ref
operator|.
name|ReferenceQueue
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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLClassLoader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_comment
comment|/**  * A reference queue with an associated background thread that dequeues  * references and invokes {@link FinalizableReference#finalizeReferent()} on  * them.  *  *<p>Keep a strong reference to this object until all of the associated  * referents have been finalized. If this object is garbage collected earlier,  * the backing thread will not invoke {@code finalizeReferent()} on the  * remaining references.  *  * @author Bob Lee  * @since 2010.01.04<b>stable</b> (imported from Google Collections Library)  */
end_comment

begin_class
DECL|class|FinalizableReferenceQueue
specifier|public
class|class
name|FinalizableReferenceQueue
block|{
comment|/*    * The Finalizer thread keeps a phantom reference to this object. When the    * client (ReferenceMap, for example) no longer has a strong reference to    * this object, the garbage collector will reclaim it and enqueue the    * phantom reference. The enqueued reference will trigger the Finalizer to    * stop.    *    * If this library is loaded in the system class loader,    * FinalizableReferenceQueue can load Finalizer directly with no problems.    *    * If this library is loaded in an application class loader, it's important    * that Finalizer not have a strong reference back to the class loader.    * Otherwise, you could have a graph like this:    *    * Finalizer Thread    *   runs instance of -> Finalizer.class    *     loaded by -> Application class loader    *       which loaded -> ReferenceMap.class    *         which has a static -> FinalizableReferenceQueue instance    *    * Even if no other references to classes from the application class loader    * remain, the Finalizer thread keeps an indirect strong reference to the    * queue in ReferenceMap, which keeps the Finalizer running, and as a result,    * the application class loader can never be reclaimed.    *    * This means that dynamically loaded web applications and OSGi bundles can't    * be unloaded.    *    * If the library is loaded in an application class loader, we try to break    * the cycle by loading Finalizer in its own independent class loader:    *    * System class loader    *   -> Application class loader    *     -> ReferenceMap    *     -> FinalizableReferenceQueue    *     -> etc.    *   -> Decoupled class loader    *     -> Finalizer    *    * Now, Finalizer no longer keeps an indirect strong reference to the    * static FinalizableReferenceQueue field in ReferenceMap. The application    * class loader can be reclaimed at which point the Finalizer thread will    * stop and its decoupled class loader can also be reclaimed.    *    * If any of this fails along the way, we fall back to loading Finalizer    * directly in the application class loader.    */
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|Logger
name|logger
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|FinalizableReferenceQueue
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|FINALIZER_CLASS_NAME
specifier|private
specifier|static
specifier|final
name|String
name|FINALIZER_CLASS_NAME
init|=
literal|"com.google.common.base.internal.Finalizer"
decl_stmt|;
comment|/** Reference to Finalizer.startFinalizer(). */
DECL|field|startFinalizer
specifier|private
specifier|static
specifier|final
name|Method
name|startFinalizer
decl_stmt|;
static|static
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|finalizer
init|=
name|loadFinalizer
argument_list|(
operator|new
name|SystemLoader
argument_list|()
argument_list|,
operator|new
name|DecoupledLoader
argument_list|()
argument_list|,
operator|new
name|DirectLoader
argument_list|()
argument_list|)
decl_stmt|;
name|startFinalizer
operator|=
name|getStartFinalizer
argument_list|(
name|finalizer
argument_list|)
expr_stmt|;
block|}
comment|/**    * The actual reference queue that our background thread will poll.    */
DECL|field|queue
specifier|final
name|ReferenceQueue
argument_list|<
name|Object
argument_list|>
name|queue
decl_stmt|;
comment|/**    * Whether or not the background thread started successfully.    */
DECL|field|threadStarted
specifier|final
name|boolean
name|threadStarted
decl_stmt|;
comment|/**    * Constructs a new queue.    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|FinalizableReferenceQueue ()
specifier|public
name|FinalizableReferenceQueue
parameter_list|()
block|{
comment|// We could start the finalizer lazily, but I'd rather it blow up early.
name|ReferenceQueue
argument_list|<
name|Object
argument_list|>
name|queue
decl_stmt|;
name|boolean
name|threadStarted
init|=
literal|false
decl_stmt|;
try|try
block|{
name|queue
operator|=
operator|(
name|ReferenceQueue
argument_list|<
name|Object
argument_list|>
operator|)
name|startFinalizer
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
name|FinalizableReference
operator|.
name|class
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|threadStarted
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
comment|// Finalizer.startFinalizer() is public.
throw|throw
operator|new
name|AssertionError
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|logger
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"Failed to start reference finalizer thread."
operator|+
literal|" Reference cleanup will only occur when new references are"
operator|+
literal|" created."
argument_list|,
name|t
argument_list|)
expr_stmt|;
name|queue
operator|=
operator|new
name|ReferenceQueue
argument_list|<
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|queue
operator|=
name|queue
expr_stmt|;
name|this
operator|.
name|threadStarted
operator|=
name|threadStarted
expr_stmt|;
block|}
comment|/**    * Repeatedly dequeues references from the queue and invokes    * {@link FinalizableReference#finalizeReferent()} on them until the queue    * is empty. This method is a no-op if the background thread was created    * successfully.    */
DECL|method|cleanUp ()
name|void
name|cleanUp
parameter_list|()
block|{
if|if
condition|(
name|threadStarted
condition|)
block|{
return|return;
block|}
name|Reference
argument_list|<
name|?
argument_list|>
name|reference
decl_stmt|;
while|while
condition|(
operator|(
name|reference
operator|=
name|queue
operator|.
name|poll
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
comment|/*        * This is for the benefit of phantom references. Weak and soft        * references will have already been cleared by this point.        */
name|reference
operator|.
name|clear
argument_list|()
expr_stmt|;
try|try
block|{
operator|(
operator|(
name|FinalizableReference
operator|)
name|reference
operator|)
operator|.
name|finalizeReferent
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|logger
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"Error cleaning up after reference."
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Iterates through the given loaders until it finds one that can load    * Finalizer.    *    * @return Finalizer.class    */
DECL|method|loadFinalizer (FinalizerLoader... loaders)
specifier|private
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|loadFinalizer
parameter_list|(
name|FinalizerLoader
modifier|...
name|loaders
parameter_list|)
block|{
for|for
control|(
name|FinalizerLoader
name|loader
range|:
name|loaders
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|finalizer
init|=
name|loader
operator|.
name|loadFinalizer
argument_list|()
decl_stmt|;
if|if
condition|(
name|finalizer
operator|!=
literal|null
condition|)
block|{
return|return
name|finalizer
return|;
block|}
block|}
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
comment|/**    * Loads Finalizer.class.    */
DECL|interface|FinalizerLoader
interface|interface
name|FinalizerLoader
block|{
comment|/**      * Returns Finalizer.class or null if this loader shouldn't or can't load      * it.      *      * @throws SecurityException if we don't have the appropriate privileges      */
DECL|method|loadFinalizer ()
name|Class
argument_list|<
name|?
argument_list|>
name|loadFinalizer
parameter_list|()
function_decl|;
block|}
comment|/**    * Tries to load Finalizer from the system class loader. If Finalizer is    * in the system class path, we needn't create a separate loader.    */
DECL|class|SystemLoader
specifier|static
class|class
name|SystemLoader
implements|implements
name|FinalizerLoader
block|{
DECL|method|loadFinalizer ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|loadFinalizer
parameter_list|()
block|{
name|ClassLoader
name|systemLoader
decl_stmt|;
try|try
block|{
name|systemLoader
operator|=
name|ClassLoader
operator|.
name|getSystemClassLoader
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Not allowed to access system class loader."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
if|if
condition|(
name|systemLoader
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|systemLoader
operator|.
name|loadClass
argument_list|(
name|FINALIZER_CLASS_NAME
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
comment|// Ignore. Finalizer is simply in a child class loader.
return|return
literal|null
return|;
block|}
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
comment|/**    * Try to load Finalizer in its own class loader. If Finalizer's thread    * had a direct reference to our class loader (which could be that of    * a dynamically loaded web application or OSGi bundle), it would prevent    * our class loader from getting garbage collected.    */
DECL|class|DecoupledLoader
specifier|static
class|class
name|DecoupledLoader
implements|implements
name|FinalizerLoader
block|{
DECL|field|LOADING_ERROR
specifier|private
specifier|static
specifier|final
name|String
name|LOADING_ERROR
init|=
literal|"Could not load Finalizer in"
operator|+
literal|" its own class loader. Loading Finalizer in the current class loader"
operator|+
literal|" instead. As a result, you will not be able to garbage collect this"
operator|+
literal|" class loader. To support reclaiming this class loader, either"
operator|+
literal|" resolve the underlying issue, or move Google Collections to your"
operator|+
literal|" system class path."
decl_stmt|;
DECL|method|loadFinalizer ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|loadFinalizer
parameter_list|()
block|{
try|try
block|{
comment|/*          * We use URLClassLoader because it's the only concrete class loader          * implementation in the JDK. If we used our own ClassLoader subclass,          * Finalizer would indirectly reference this class loader:          *          * Finalizer.class ->          *   CustomClassLoader ->          *     CustomClassLoader.class ->          *       This class loader          *          * System class loader will (and must) be the parent.          */
name|ClassLoader
name|finalizerLoader
init|=
name|newLoader
argument_list|(
name|getBaseUrl
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|finalizerLoader
operator|.
name|loadClass
argument_list|(
name|FINALIZER_CLASS_NAME
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
name|LOADING_ERROR
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Gets URL for base of path containing Finalizer.class.      */
DECL|method|getBaseUrl ()
name|URL
name|getBaseUrl
parameter_list|()
throws|throws
name|IOException
block|{
comment|// Find URL pointing to Finalizer.class file.
name|String
name|finalizerPath
init|=
name|FINALIZER_CLASS_NAME
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|".class"
decl_stmt|;
name|URL
name|finalizerUrl
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
name|finalizerPath
argument_list|)
decl_stmt|;
if|if
condition|(
name|finalizerUrl
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|FileNotFoundException
argument_list|(
name|finalizerPath
argument_list|)
throw|;
block|}
comment|// Find URL pointing to base of class path.
name|String
name|urlString
init|=
name|finalizerUrl
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|urlString
operator|.
name|endsWith
argument_list|(
name|finalizerPath
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unsupported path style: "
operator|+
name|urlString
argument_list|)
throw|;
block|}
name|urlString
operator|=
name|urlString
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|urlString
operator|.
name|length
argument_list|()
operator|-
name|finalizerPath
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|URL
argument_list|(
name|finalizerUrl
argument_list|,
name|urlString
argument_list|)
return|;
block|}
comment|/** Creates a class loader with the given base URL as its classpath. */
DECL|method|newLoader (URL base)
name|URLClassLoader
name|newLoader
parameter_list|(
name|URL
name|base
parameter_list|)
block|{
return|return
operator|new
name|URLClassLoader
argument_list|(
operator|new
name|URL
index|[]
block|{
name|base
block|}
argument_list|)
return|;
block|}
block|}
comment|/**    * Loads Finalizer directly using the current class loader. We won't be    * able to garbage collect this class loader, but at least the world    * doesn't end.    */
DECL|class|DirectLoader
specifier|static
class|class
name|DirectLoader
implements|implements
name|FinalizerLoader
block|{
DECL|method|loadFinalizer ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|loadFinalizer
parameter_list|()
block|{
try|try
block|{
return|return
name|Class
operator|.
name|forName
argument_list|(
name|FINALIZER_CLASS_NAME
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**    * Looks up Finalizer.startFinalizer().    */
DECL|method|getStartFinalizer (Class<?> finalizer)
specifier|static
name|Method
name|getStartFinalizer
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|finalizer
parameter_list|)
block|{
try|try
block|{
return|return
name|finalizer
operator|.
name|getMethod
argument_list|(
literal|"startFinalizer"
argument_list|,
name|Class
operator|.
name|class
argument_list|,
name|Object
operator|.
name|class
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
name|AssertionError
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

