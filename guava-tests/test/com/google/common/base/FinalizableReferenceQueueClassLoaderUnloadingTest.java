begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2005 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|StandardSystemProperty
operator|.
name|JAVA_CLASS_PATH
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
name|base
operator|.
name|StandardSystemProperty
operator|.
name|PATH_SEPARATOR
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
name|ImmutableList
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
name|GcFinalization
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|WeakReference
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
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
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
name|security
operator|.
name|Permission
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Policy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|ProtectionDomain
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Semaphore
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicReference
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
comment|/**  * Tests that the {@code ClassLoader} of {@link FinalizableReferenceQueue} can be unloaded. These  * tests are separate from {@link FinalizableReferenceQueueTest} so that they can be excluded from  * coverage runs, as the coverage system interferes with them.  *  * @author Eamonn McManus  */
end_comment

begin_class
DECL|class|FinalizableReferenceQueueClassLoaderUnloadingTest
specifier|public
class|class
name|FinalizableReferenceQueueClassLoaderUnloadingTest
extends|extends
name|TestCase
block|{
comment|/*    * The following tests check that the use of FinalizableReferenceQueue does not prevent the    * ClassLoader that loaded that class from later being garbage-collected. If anything continues    * to reference the FinalizableReferenceQueue class then its ClassLoader cannot be    * garbage-collected, even if there are no more instances of FinalizableReferenceQueue itself.    * The code in FinalizableReferenceQueue goes to considerable trouble to ensure that there are    * no such references and the tests here check that that trouble has not been in vain.    *    * When we reference FinalizableReferenceQueue in this test, we are referencing a class that is    * loaded by this test and that will obviously remain loaded for as long as the test is running.    * So in order to check ClassLoader garbage collection we need to create a new ClassLoader and    * make it load its own version of FinalizableReferenceQueue. Then we need to interact with that    * parallel version through reflection in order to exercise the parallel    * FinalizableReferenceQueue, and then check that the parallel ClassLoader can be    * garbage-collected after that.    */
DECL|class|MyFinalizableWeakReference
specifier|public
specifier|static
class|class
name|MyFinalizableWeakReference
extends|extends
name|FinalizableWeakReference
argument_list|<
name|Object
argument_list|>
block|{
DECL|method|MyFinalizableWeakReference (Object x, FinalizableReferenceQueue queue)
specifier|public
name|MyFinalizableWeakReference
parameter_list|(
name|Object
name|x
parameter_list|,
name|FinalizableReferenceQueue
name|queue
parameter_list|)
block|{
name|super
argument_list|(
name|x
argument_list|,
name|queue
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|finalizeReferent ()
specifier|public
name|void
name|finalizeReferent
parameter_list|()
block|{}
block|}
DECL|class|PermissivePolicy
specifier|private
specifier|static
class|class
name|PermissivePolicy
extends|extends
name|Policy
block|{
annotation|@
name|Override
DECL|method|implies (ProtectionDomain pd, Permission perm)
specifier|public
name|boolean
name|implies
parameter_list|(
name|ProtectionDomain
name|pd
parameter_list|,
name|Permission
name|perm
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
DECL|method|useFrqInSeparateLoader ()
specifier|private
name|WeakReference
argument_list|<
name|ClassLoader
argument_list|>
name|useFrqInSeparateLoader
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|ClassLoader
name|myLoader
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
name|URLClassLoader
name|sepLoader
init|=
operator|new
name|URLClassLoader
argument_list|(
name|getClassPathUrls
argument_list|()
argument_list|,
name|myLoader
operator|.
name|getParent
argument_list|()
argument_list|)
decl_stmt|;
comment|// sepLoader is the loader that we will use to load the parallel FinalizableReferenceQueue (FRQ)
comment|// and friends, and that we will eventually expect to see garbage-collected. The assumption
comment|// is that the ClassLoader of this test is a URLClassLoader, and that it loads FRQ itself
comment|// rather than delegating to a parent ClassLoader. If this assumption is violated the test will
comment|// fail and will need to be rewritten.
name|Class
argument_list|<
name|?
argument_list|>
name|frqC
init|=
name|FinalizableReferenceQueue
operator|.
name|class
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|sepFrqC
init|=
name|sepLoader
operator|.
name|loadClass
argument_list|(
name|frqC
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|frqC
argument_list|,
name|sepFrqC
argument_list|)
expr_stmt|;
comment|// Check the assumptions above.
comment|// FRQ tries to load the Finalizer class (for the reference-collecting thread) in a few ways.
comment|// If the class is accessible to the system ClassLoader (ClassLoader.getSystemClassLoader())
comment|// then FRQ does not bother to load Finalizer.class through a separate ClassLoader. That happens
comment|// in our test environment, which foils the purpose of this test, so we disable the logic for
comment|// our test by setting a static field. We are changing the field in the parallel version of FRQ
comment|// and each test creates its own one of those, so there is no test interference here.
name|Class
argument_list|<
name|?
argument_list|>
name|sepFrqSystemLoaderC
init|=
name|sepLoader
operator|.
name|loadClass
argument_list|(
name|FinalizableReferenceQueue
operator|.
name|SystemLoader
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Field
name|disabled
init|=
name|sepFrqSystemLoaderC
operator|.
name|getDeclaredField
argument_list|(
literal|"disabled"
argument_list|)
decl_stmt|;
name|disabled
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|disabled
operator|.
name|set
argument_list|(
literal|null
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// Now make a parallel FRQ and an associated FinalizableWeakReference to an object, in order to
comment|// exercise some classes from the parallel ClassLoader.
name|AtomicReference
argument_list|<
name|Object
argument_list|>
name|sepFrqA
init|=
operator|new
name|AtomicReference
argument_list|<
name|Object
argument_list|>
argument_list|(
name|sepFrqC
operator|.
name|newInstance
argument_list|()
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|sepFwrC
init|=
name|sepLoader
operator|.
name|loadClass
argument_list|(
name|MyFinalizableWeakReference
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Constructor
argument_list|<
name|?
argument_list|>
name|sepFwrCons
init|=
name|sepFwrC
operator|.
name|getConstructor
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|sepFrqC
argument_list|)
decl_stmt|;
comment|// The object that we will wrap in FinalizableWeakReference is a Stopwatch.
name|Class
argument_list|<
name|?
argument_list|>
name|sepStopwatchC
init|=
name|sepLoader
operator|.
name|loadClass
argument_list|(
name|Stopwatch
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|sepLoader
argument_list|,
name|sepStopwatchC
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|AtomicReference
argument_list|<
name|Object
argument_list|>
name|sepStopwatchA
init|=
operator|new
name|AtomicReference
argument_list|<
name|Object
argument_list|>
argument_list|(
name|sepStopwatchC
operator|.
name|getMethod
argument_list|(
literal|"createUnstarted"
argument_list|)
operator|.
name|invoke
argument_list|(
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|AtomicReference
argument_list|<
name|WeakReference
argument_list|<
name|?
argument_list|>
argument_list|>
name|sepStopwatchRef
init|=
operator|new
name|AtomicReference
argument_list|<
name|WeakReference
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|(
operator|(
name|WeakReference
argument_list|<
name|?
argument_list|>
operator|)
name|sepFwrCons
operator|.
name|newInstance
argument_list|(
name|sepStopwatchA
operator|.
name|get
argument_list|()
argument_list|,
name|sepFrqA
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|sepStopwatchA
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// Clear all references to the Stopwatch and wait for it to be gc'd.
name|sepStopwatchA
operator|.
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|GcFinalization
operator|.
name|awaitClear
argument_list|(
name|sepStopwatchRef
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// Return a weak reference to the parallel ClassLoader. This is the reference that should
comment|// eventually become clear if there are no other references to the ClassLoader.
return|return
operator|new
name|WeakReference
argument_list|<
name|ClassLoader
argument_list|>
argument_list|(
name|sepLoader
argument_list|)
return|;
block|}
DECL|method|doTestUnloadable ()
specifier|private
name|void
name|doTestUnloadable
parameter_list|()
throws|throws
name|Exception
block|{
name|WeakReference
argument_list|<
name|ClassLoader
argument_list|>
name|loaderRef
init|=
name|useFrqInSeparateLoader
argument_list|()
decl_stmt|;
name|GcFinalization
operator|.
name|awaitClear
argument_list|(
name|loaderRef
argument_list|)
expr_stmt|;
block|}
DECL|method|testUnloadableWithoutSecurityManager ()
specifier|public
name|void
name|testUnloadableWithoutSecurityManager
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Test that the use of a FinalizableReferenceQueue does not subsequently prevent the
comment|// loader of that class from being garbage-collected.
name|SecurityManager
name|oldSecurityManager
init|=
name|System
operator|.
name|getSecurityManager
argument_list|()
decl_stmt|;
try|try
block|{
name|System
operator|.
name|setSecurityManager
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|doTestUnloadable
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|setSecurityManager
argument_list|(
name|oldSecurityManager
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testUnloadableWithSecurityManager ()
specifier|public
name|void
name|testUnloadableWithSecurityManager
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Test that the use of a FinalizableReferenceQueue does not subsequently prevent the
comment|// loader of that class from being garbage-collected even if there is a SecurityManager.
comment|// The SecurityManager environment makes such leaks more likely because when you create
comment|// a URLClassLoader with a SecurityManager, the creating code's AccessControlContext is
comment|// captured, and that references the creating code's ClassLoader.
name|Policy
name|oldPolicy
init|=
name|Policy
operator|.
name|getPolicy
argument_list|()
decl_stmt|;
name|SecurityManager
name|oldSecurityManager
init|=
name|System
operator|.
name|getSecurityManager
argument_list|()
decl_stmt|;
try|try
block|{
name|Policy
operator|.
name|setPolicy
argument_list|(
operator|new
name|PermissivePolicy
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|setSecurityManager
argument_list|(
operator|new
name|SecurityManager
argument_list|()
argument_list|)
expr_stmt|;
name|doTestUnloadable
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|setSecurityManager
argument_list|(
name|oldSecurityManager
argument_list|)
expr_stmt|;
name|Policy
operator|.
name|setPolicy
argument_list|(
name|oldPolicy
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|FrqUser
specifier|public
specifier|static
class|class
name|FrqUser
implements|implements
name|Callable
argument_list|<
name|WeakReference
argument_list|<
name|Object
argument_list|>
argument_list|>
block|{
DECL|field|frq
specifier|public
specifier|static
name|FinalizableReferenceQueue
name|frq
init|=
operator|new
name|FinalizableReferenceQueue
argument_list|()
decl_stmt|;
DECL|field|finalized
specifier|public
specifier|static
specifier|final
name|Semaphore
name|finalized
init|=
operator|new
name|Semaphore
argument_list|(
literal|0
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|WeakReference
argument_list|<
name|Object
argument_list|>
name|call
parameter_list|()
block|{
name|WeakReference
argument_list|<
name|Object
argument_list|>
name|wr
init|=
operator|new
name|FinalizableWeakReference
argument_list|<
name|Object
argument_list|>
argument_list|(
operator|new
name|Integer
argument_list|(
literal|23
argument_list|)
argument_list|,
name|frq
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|finalizeReferent
parameter_list|()
block|{
name|finalized
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
return|return
name|wr
return|;
block|}
block|}
DECL|method|testUnloadableInStaticFieldIfClosed ()
specifier|public
name|void
name|testUnloadableInStaticFieldIfClosed
parameter_list|()
throws|throws
name|Exception
block|{
name|Policy
name|oldPolicy
init|=
name|Policy
operator|.
name|getPolicy
argument_list|()
decl_stmt|;
name|SecurityManager
name|oldSecurityManager
init|=
name|System
operator|.
name|getSecurityManager
argument_list|()
decl_stmt|;
try|try
block|{
name|Policy
operator|.
name|setPolicy
argument_list|(
operator|new
name|PermissivePolicy
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|setSecurityManager
argument_list|(
operator|new
name|SecurityManager
argument_list|()
argument_list|)
expr_stmt|;
name|WeakReference
argument_list|<
name|ClassLoader
argument_list|>
name|loaderRef
init|=
name|doTestUnloadableInStaticFieldIfClosed
argument_list|()
decl_stmt|;
name|GcFinalization
operator|.
name|awaitClear
argument_list|(
name|loaderRef
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|setSecurityManager
argument_list|(
name|oldSecurityManager
argument_list|)
expr_stmt|;
name|Policy
operator|.
name|setPolicy
argument_list|(
name|oldPolicy
argument_list|)
expr_stmt|;
block|}
block|}
comment|// If you have a FinalizableReferenceQueue that is a static field of one of the classes of your
comment|// app (like the FrqUser class above), then the app's ClassLoader will never be gc'd. The reason
comment|// is that we attempt to run a thread in a separate ClassLoader that will detect when the FRQ
comment|// is no longer referenced, meaning that the app's ClassLoader has been gc'd, and when that
comment|// happens. But the thread's supposedly separate ClassLoader actually has a reference to the app's
comment|// ClasLoader via its AccessControlContext. It does not seem to be possible to make a
comment|// URLClassLoader without capturing this reference, and it probably would not be desirable for
comment|// security reasons anyway. Therefore, the FRQ.close() method provides a way to stop the thread
comment|// explicitly. This test checks that calling that method does allow an app's ClassLoader to be
comment|// gc'd even if there is a still a FinalizableReferenceQueue in a static field. (Setting the field
comment|// to null would also work, but only if there are no references to the FRQ anywhere else.)
DECL|method|doTestUnloadableInStaticFieldIfClosed ()
specifier|private
name|WeakReference
argument_list|<
name|ClassLoader
argument_list|>
name|doTestUnloadableInStaticFieldIfClosed
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|ClassLoader
name|myLoader
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
name|URLClassLoader
name|sepLoader
init|=
operator|new
name|URLClassLoader
argument_list|(
name|getClassPathUrls
argument_list|()
argument_list|,
name|myLoader
operator|.
name|getParent
argument_list|()
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|frqC
init|=
name|FinalizableReferenceQueue
operator|.
name|class
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|sepFrqC
init|=
name|sepLoader
operator|.
name|loadClass
argument_list|(
name|frqC
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|frqC
argument_list|,
name|sepFrqC
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|sepFrqSystemLoaderC
init|=
name|sepLoader
operator|.
name|loadClass
argument_list|(
name|FinalizableReferenceQueue
operator|.
name|SystemLoader
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Field
name|disabled
init|=
name|sepFrqSystemLoaderC
operator|.
name|getDeclaredField
argument_list|(
literal|"disabled"
argument_list|)
decl_stmt|;
name|disabled
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|disabled
operator|.
name|set
argument_list|(
literal|null
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|frqUserC
init|=
name|FrqUser
operator|.
name|class
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|sepFrqUserC
init|=
name|sepLoader
operator|.
name|loadClass
argument_list|(
name|frqUserC
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|frqUserC
argument_list|,
name|sepFrqUserC
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|sepLoader
argument_list|,
name|sepFrqUserC
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|Callable
argument_list|<
name|?
argument_list|>
name|sepFrqUser
init|=
operator|(
name|Callable
argument_list|<
name|?
argument_list|>
operator|)
name|sepFrqUserC
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|WeakReference
argument_list|<
name|?
argument_list|>
name|finalizableWeakReference
init|=
operator|(
name|WeakReference
argument_list|<
name|?
argument_list|>
operator|)
name|sepFrqUser
operator|.
name|call
argument_list|()
decl_stmt|;
name|GcFinalization
operator|.
name|awaitClear
argument_list|(
name|finalizableWeakReference
argument_list|)
expr_stmt|;
name|Field
name|sepFrqUserFinalizedF
init|=
name|sepFrqUserC
operator|.
name|getField
argument_list|(
literal|"finalized"
argument_list|)
decl_stmt|;
name|Semaphore
name|finalizeCount
init|=
operator|(
name|Semaphore
operator|)
name|sepFrqUserFinalizedF
operator|.
name|get
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|boolean
name|finalized
init|=
name|finalizeCount
operator|.
name|tryAcquire
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|finalized
argument_list|)
expr_stmt|;
name|Field
name|sepFrqUserFrqF
init|=
name|sepFrqUserC
operator|.
name|getField
argument_list|(
literal|"frq"
argument_list|)
decl_stmt|;
name|Closeable
name|frq
init|=
operator|(
name|Closeable
operator|)
name|sepFrqUserFrqF
operator|.
name|get
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|frq
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
operator|new
name|WeakReference
argument_list|<
name|ClassLoader
argument_list|>
argument_list|(
name|sepLoader
argument_list|)
return|;
block|}
DECL|method|getClassPathUrls ()
specifier|private
name|URL
index|[]
name|getClassPathUrls
parameter_list|()
block|{
name|ClassLoader
name|classLoader
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
return|return
name|classLoader
operator|instanceof
name|URLClassLoader
condition|?
operator|(
operator|(
name|URLClassLoader
operator|)
name|classLoader
operator|)
operator|.
name|getURLs
argument_list|()
else|:
name|parseJavaClassPath
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|URL
index|[
literal|0
index|]
argument_list|)
return|;
block|}
comment|/**    * Returns the URLs in the class path specified by the {@code java.class.path} {@linkplain    * System#getProperty system property}.    */
comment|// TODO(b/65488446): Make this a public API.
DECL|method|parseJavaClassPath ()
specifier|private
specifier|static
name|ImmutableList
argument_list|<
name|URL
argument_list|>
name|parseJavaClassPath
parameter_list|()
block|{
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|URL
argument_list|>
name|urls
init|=
name|ImmutableList
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|entry
range|:
name|Splitter
operator|.
name|on
argument_list|(
name|PATH_SEPARATOR
operator|.
name|value
argument_list|()
argument_list|)
operator|.
name|split
argument_list|(
name|JAVA_CLASS_PATH
operator|.
name|value
argument_list|()
argument_list|)
control|)
block|{
try|try
block|{
try|try
block|{
name|urls
operator|.
name|add
argument_list|(
operator|new
name|File
argument_list|(
name|entry
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|e
parameter_list|)
block|{
comment|// File.toURI checks to see if the file is a directory
name|urls
operator|.
name|add
argument_list|(
operator|new
name|URL
argument_list|(
literal|"file"
argument_list|,
literal|null
argument_list|,
operator|new
name|File
argument_list|(
name|entry
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|AssertionError
name|error
init|=
operator|new
name|AssertionError
argument_list|(
literal|"malformed class path entry: "
operator|+
name|entry
argument_list|)
decl_stmt|;
name|error
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|error
throw|;
block|}
block|}
return|return
name|urls
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

