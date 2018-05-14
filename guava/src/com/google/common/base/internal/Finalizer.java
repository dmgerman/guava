begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.base.internal
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|ref
operator|.
name|PhantomReference
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

begin_import
import|import
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|qual
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Thread that finalizes referents. All references should implement {@code  * com.google.common.base.FinalizableReference}.  *  *<p>While this class is public, we consider it to be *internal* and not part of our published API.  * It is public so we can access it reflectively across class loaders in secure environments.  *  *<p>This class can't depend on other Guava code. If we were to load this class in the same class  * loader as the rest of Guava, this thread would keep an indirect strong reference to the class  * loader and prevent it from being garbage collected. This poses a problem for environments where  * you want to throw away the class loader. For example, dynamically reloading a web application or  * unloading an OSGi bundle.  *  *<p>{@code com.google.common.base.FinalizableReferenceQueue} loads this class in its own class  * loader. That way, this class doesn't prevent the main class loader from getting garbage  * collected, and this class can detect when the main class loader has been garbage collected and  * stop itself.  */
end_comment

begin_class
DECL|class|Finalizer
specifier|public
class|class
name|Finalizer
implements|implements
name|Runnable
block|{
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
name|Finalizer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
comment|/** Name of FinalizableReference.class. */
DECL|field|FINALIZABLE_REFERENCE
specifier|private
specifier|static
specifier|final
name|String
name|FINALIZABLE_REFERENCE
init|=
literal|"com.google.common.base.FinalizableReference"
decl_stmt|;
comment|/**    * Starts the Finalizer thread. FinalizableReferenceQueue calls this method reflectively.    *    * @param finalizableReferenceClass FinalizableReference.class.    * @param queue a reference queue that the thread will poll.    * @param frqReference a phantom reference to the FinalizableReferenceQueue, which will be queued    *     either when the FinalizableReferenceQueue is no longer referenced anywhere, or when its    *     close() method is called.    */
DECL|method|startFinalizer ( Class<?> finalizableReferenceClass, ReferenceQueue<Object> queue, PhantomReference<Object> frqReference)
specifier|public
specifier|static
name|void
name|startFinalizer
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|finalizableReferenceClass
parameter_list|,
name|ReferenceQueue
argument_list|<
name|Object
argument_list|>
name|queue
parameter_list|,
name|PhantomReference
argument_list|<
name|Object
argument_list|>
name|frqReference
parameter_list|)
block|{
comment|/*      * We use FinalizableReference.class for two things:      *      * 1) To invoke FinalizableReference.finalizeReferent()      *      * 2) To detect when FinalizableReference's class loader has to be garbage collected, at which      * point, Finalizer can stop running      */
if|if
condition|(
operator|!
name|finalizableReferenceClass
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|FINALIZABLE_REFERENCE
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Expected "
operator|+
name|FINALIZABLE_REFERENCE
operator|+
literal|"."
argument_list|)
throw|;
block|}
name|Finalizer
name|finalizer
init|=
operator|new
name|Finalizer
argument_list|(
name|finalizableReferenceClass
argument_list|,
name|queue
argument_list|,
name|frqReference
argument_list|)
decl_stmt|;
name|String
name|threadName
init|=
name|Finalizer
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Thread
name|thread
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|bigThreadConstructor
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|boolean
name|inheritThreadLocals
init|=
literal|false
decl_stmt|;
name|long
name|defaultStackSize
init|=
literal|0
decl_stmt|;
name|thread
operator|=
name|bigThreadConstructor
operator|.
name|newInstance
argument_list|(
operator|(
name|ThreadGroup
operator|)
literal|null
argument_list|,
name|finalizer
argument_list|,
name|threadName
argument_list|,
name|defaultStackSize
argument_list|,
name|inheritThreadLocals
argument_list|)
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
name|INFO
argument_list|,
literal|"Failed to create a thread without inherited thread-local values"
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|thread
operator|==
literal|null
condition|)
block|{
name|thread
operator|=
operator|new
name|Thread
argument_list|(
operator|(
name|ThreadGroup
operator|)
literal|null
argument_list|,
name|finalizer
argument_list|,
name|threadName
argument_list|)
expr_stmt|;
block|}
name|thread
operator|.
name|setDaemon
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|inheritableThreadLocals
operator|!=
literal|null
condition|)
block|{
name|inheritableThreadLocals
operator|.
name|set
argument_list|(
name|thread
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
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
literal|"Failed to clear thread local values inherited by reference finalizer thread."
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|field|finalizableReferenceClassReference
specifier|private
specifier|final
name|WeakReference
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|finalizableReferenceClassReference
decl_stmt|;
DECL|field|frqReference
specifier|private
specifier|final
name|PhantomReference
argument_list|<
name|Object
argument_list|>
name|frqReference
decl_stmt|;
DECL|field|queue
specifier|private
specifier|final
name|ReferenceQueue
argument_list|<
name|Object
argument_list|>
name|queue
decl_stmt|;
comment|// By preference, we will use the Thread constructor that has an `inheritThreadLocals` parameter.
comment|// But before Java 9, our only way not to inherit ThreadLocals is to zap them after the thread
comment|// is created, by accessing a private field.
DECL|field|bigThreadConstructor
specifier|private
specifier|static
specifier|final
annotation|@
name|Nullable
name|Constructor
argument_list|<
name|Thread
argument_list|>
name|bigThreadConstructor
init|=
name|getBigThreadConstructor
argument_list|()
decl_stmt|;
DECL|field|inheritableThreadLocals
specifier|private
specifier|static
specifier|final
annotation|@
name|Nullable
name|Field
name|inheritableThreadLocals
init|=
operator|(
name|bigThreadConstructor
operator|==
literal|null
operator|)
condition|?
name|getInheritableThreadLocalsField
argument_list|()
else|:
literal|null
decl_stmt|;
comment|/** Constructs a new finalizer thread. */
DECL|method|Finalizer ( Class<?> finalizableReferenceClass, ReferenceQueue<Object> queue, PhantomReference<Object> frqReference)
specifier|private
name|Finalizer
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|finalizableReferenceClass
parameter_list|,
name|ReferenceQueue
argument_list|<
name|Object
argument_list|>
name|queue
parameter_list|,
name|PhantomReference
argument_list|<
name|Object
argument_list|>
name|frqReference
parameter_list|)
block|{
name|this
operator|.
name|queue
operator|=
name|queue
expr_stmt|;
name|this
operator|.
name|finalizableReferenceClassReference
operator|=
operator|new
name|WeakReference
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|(
name|finalizableReferenceClass
argument_list|)
expr_stmt|;
comment|// Keep track of the FRQ that started us so we know when to stop.
name|this
operator|.
name|frqReference
operator|=
name|frqReference
expr_stmt|;
block|}
comment|/** Loops continuously, pulling references off the queue and cleaning them up. */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"InfiniteLoopStatement"
argument_list|)
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
if|if
condition|(
operator|!
name|cleanUp
argument_list|(
name|queue
operator|.
name|remove
argument_list|()
argument_list|)
condition|)
block|{
break|break;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
comment|/**    * Cleans up a single reference. Catches and logs all throwables.    *    * @return true if the caller should continue, false if the associated FinalizableReferenceQueue    *     is no longer referenced.    */
DECL|method|cleanUp (Reference<?> reference)
specifier|private
name|boolean
name|cleanUp
parameter_list|(
name|Reference
argument_list|<
name|?
argument_list|>
name|reference
parameter_list|)
block|{
name|Method
name|finalizeReferentMethod
init|=
name|getFinalizeReferentMethod
argument_list|()
decl_stmt|;
if|if
condition|(
name|finalizeReferentMethod
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
do|do
block|{
comment|/*        * This is for the benefit of phantom references. Weak and soft references will have already        * been cleared by this point.        */
name|reference
operator|.
name|clear
argument_list|()
expr_stmt|;
if|if
condition|(
name|reference
operator|==
name|frqReference
condition|)
block|{
comment|/*          * The client no longer has a reference to the FinalizableReferenceQueue. We can stop.          */
return|return
literal|false
return|;
block|}
try|try
block|{
name|finalizeReferentMethod
operator|.
name|invoke
argument_list|(
name|reference
argument_list|)
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
comment|/*        * Loop as long as we have references available so as not to waste CPU looking up the Method        * over and over again.        */
block|}
do|while
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
do|;
return|return
literal|true
return|;
block|}
comment|/** Looks up FinalizableReference.finalizeReferent() method. */
DECL|method|getFinalizeReferentMethod ()
specifier|private
annotation|@
name|Nullable
name|Method
name|getFinalizeReferentMethod
parameter_list|()
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|finalizableReferenceClass
init|=
name|finalizableReferenceClassReference
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|finalizableReferenceClass
operator|==
literal|null
condition|)
block|{
comment|/*        * FinalizableReference's class loader was reclaimed. While there's a chance that other        * finalizable references could be enqueued subsequently (at which point the class loader        * would be resurrected by virtue of us having a strong reference to it), we should pretty        * much just shut down and make sure we don't keep it alive any longer than necessary.        */
return|return
literal|null
return|;
block|}
try|try
block|{
return|return
name|finalizableReferenceClass
operator|.
name|getMethod
argument_list|(
literal|"finalizeReferent"
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
DECL|method|getInheritableThreadLocalsField ()
specifier|private
specifier|static
annotation|@
name|Nullable
name|Field
name|getInheritableThreadLocalsField
parameter_list|()
block|{
try|try
block|{
name|Field
name|inheritableThreadLocals
init|=
name|Thread
operator|.
name|class
operator|.
name|getDeclaredField
argument_list|(
literal|"inheritableThreadLocals"
argument_list|)
decl_stmt|;
name|inheritableThreadLocals
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|inheritableThreadLocals
return|;
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
literal|"Couldn't access Thread.inheritableThreadLocals. Reference finalizer threads will "
operator|+
literal|"inherit thread local values."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
DECL|method|getBigThreadConstructor ()
specifier|private
specifier|static
annotation|@
name|Nullable
name|Constructor
argument_list|<
name|Thread
argument_list|>
name|getBigThreadConstructor
parameter_list|()
block|{
try|try
block|{
return|return
name|Thread
operator|.
name|class
operator|.
name|getConstructor
argument_list|(
name|ThreadGroup
operator|.
name|class
argument_list|,
name|Runnable
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|long
operator|.
name|class
argument_list|,
name|boolean
operator|.
name|class
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// Probably pre Java 9. We'll fall back to Thread.inheritableThreadLocals.
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

