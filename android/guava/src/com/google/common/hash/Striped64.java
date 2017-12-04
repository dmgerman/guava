begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Written by Doug Lea with assistance from members of JCP JSR-166  * Expert Group and released to the public domain, as explained at  * http://creativecommons.org/publicdomain/zero/1.0/  */
end_comment

begin_comment
comment|/*  * Source:  * http://gee.cs.oswego.edu/cgi-bin/viewcvs.cgi/jsr166/src/jsr166e/Striped64.java?revision=1.9  */
end_comment

begin_package
DECL|package|com.google.common.hash
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|hash
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
name|annotations
operator|.
name|GwtIncompatible
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * A package-local class holding common representation and mechanics for classes supporting dynamic  * striping on 64bit values. The class extends Number so that concrete subclasses must publicly do  * so.  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|Striped64
specifier|abstract
class|class
name|Striped64
extends|extends
name|Number
block|{
comment|/*    * This class maintains a lazily-initialized table of atomically    * updated variables, plus an extra "base" field. The table size    * is a power of two. Indexing uses masked per-thread hash codes.    * Nearly all declarations in this class are package-private,    * accessed directly by subclasses.    *    * Table entries are of class Cell; a variant of AtomicLong padded    * to reduce cache contention on most processors. Padding is    * overkill for most Atomics because they are usually irregularly    * scattered in memory and thus don't interfere much with each    * other. But Atomic objects residing in arrays will tend to be    * placed adjacent to each other, and so will most often share    * cache lines (with a huge negative performance impact) without    * this precaution.    *    * In part because Cells are relatively large, we avoid creating    * them until they are needed.  When there is no contention, all    * updates are made to the base field.  Upon first contention (a    * failed CAS on base update), the table is initialized to size 2.    * The table size is doubled upon further contention until    * reaching the nearest power of two greater than or equal to the    * number of CPUS. Table slots remain empty (null) until they are    * needed.    *    * A single spinlock ("busy") is used for initializing and    * resizing the table, as well as populating slots with new Cells.    * There is no need for a blocking lock; when the lock is not    * available, threads try other slots (or the base).  During these    * retries, there is increased contention and reduced locality,    * which is still better than alternatives.    *    * Per-thread hash codes are initialized to random values.    * Contention and/or table collisions are indicated by failed    * CASes when performing an update operation (see method    * retryUpdate). Upon a collision, if the table size is less than    * the capacity, it is doubled in size unless some other thread    * holds the lock. If a hashed slot is empty, and lock is    * available, a new Cell is created. Otherwise, if the slot    * exists, a CAS is tried.  Retries proceed by "double hashing",    * using a secondary hash (Marsaglia XorShift) to try to find a    * free slot.    *    * The table size is capped because, when there are more threads    * than CPUs, supposing that each thread were bound to a CPU,    * there would exist a perfect hash function mapping threads to    * slots that eliminates collisions. When we reach capacity, we    * search for this mapping by randomly varying the hash codes of    * colliding threads.  Because search is random, and collisions    * only become known via CAS failures, convergence can be slow,    * and because threads are typically not bound to CPUS forever,    * may not occur at all. However, despite these limitations,    * observed contention rates are typically low in these cases.    *    * It is possible for a Cell to become unused when threads that    * once hashed to it terminate, as well as in the case where    * doubling the table causes no thread to hash to it under    * expanded mask.  We do not try to detect or remove such cells,    * under the assumption that for long-running instances, observed    * contention levels will recur, so the cells will eventually be    * needed again; and for short-lived ones, it does not matter.    */
comment|/**    * Padded variant of AtomicLong supporting only raw accesses plus CAS. The value field is placed    * between pads, hoping that the JVM doesn't reorder them.    *    *<p>JVM intrinsics note: It would be possible to use a release-only form of CAS here, if it were    * provided.    */
DECL|class|Cell
specifier|static
specifier|final
class|class
name|Cell
block|{
DECL|field|p0
DECL|field|p1
DECL|field|p2
DECL|field|p3
DECL|field|p4
DECL|field|p5
DECL|field|p6
specifier|volatile
name|long
name|p0
decl_stmt|,
name|p1
decl_stmt|,
name|p2
decl_stmt|,
name|p3
decl_stmt|,
name|p4
decl_stmt|,
name|p5
decl_stmt|,
name|p6
decl_stmt|;
DECL|field|value
specifier|volatile
name|long
name|value
decl_stmt|;
DECL|field|q0
DECL|field|q1
DECL|field|q2
DECL|field|q3
DECL|field|q4
DECL|field|q5
DECL|field|q6
specifier|volatile
name|long
name|q0
decl_stmt|,
name|q1
decl_stmt|,
name|q2
decl_stmt|,
name|q3
decl_stmt|,
name|q4
decl_stmt|,
name|q5
decl_stmt|,
name|q6
decl_stmt|;
DECL|method|Cell (long x)
name|Cell
parameter_list|(
name|long
name|x
parameter_list|)
block|{
name|value
operator|=
name|x
expr_stmt|;
block|}
DECL|method|cas (long cmp, long val)
specifier|final
name|boolean
name|cas
parameter_list|(
name|long
name|cmp
parameter_list|,
name|long
name|val
parameter_list|)
block|{
return|return
name|UNSAFE
operator|.
name|compareAndSwapLong
argument_list|(
name|this
argument_list|,
name|valueOffset
argument_list|,
name|cmp
argument_list|,
name|val
argument_list|)
return|;
block|}
comment|// Unsafe mechanics
DECL|field|UNSAFE
specifier|private
specifier|static
specifier|final
name|sun
operator|.
name|misc
operator|.
name|Unsafe
name|UNSAFE
decl_stmt|;
DECL|field|valueOffset
specifier|private
specifier|static
specifier|final
name|long
name|valueOffset
decl_stmt|;
static|static
block|{
try|try
block|{
name|UNSAFE
operator|=
name|getUnsafe
argument_list|()
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|ak
init|=
name|Cell
operator|.
name|class
decl_stmt|;
name|valueOffset
operator|=
name|UNSAFE
operator|.
name|objectFieldOffset
argument_list|(
name|ak
operator|.
name|getDeclaredField
argument_list|(
literal|"value"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Error
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**    * ThreadLocal holding a single-slot int array holding hash code. Unlike the JDK8 version of this    * class, we use a suboptimal int[] representation to avoid introducing a new type that can impede    * class-unloading when ThreadLocals are not removed.    */
DECL|field|threadHashCode
specifier|static
specifier|final
name|ThreadLocal
argument_list|<
name|int
index|[]
argument_list|>
name|threadHashCode
init|=
operator|new
name|ThreadLocal
argument_list|<>
argument_list|()
decl_stmt|;
comment|/** Generator of new random hash codes */
DECL|field|rng
specifier|static
specifier|final
name|Random
name|rng
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
comment|/** Number of CPUS, to place bound on table size */
DECL|field|NCPU
specifier|static
specifier|final
name|int
name|NCPU
init|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|availableProcessors
argument_list|()
decl_stmt|;
comment|/** Table of cells. When non-null, size is a power of 2. */
DECL|field|cells
specifier|transient
specifier|volatile
name|Cell
index|[]
name|cells
decl_stmt|;
comment|/**    * Base value, used mainly when there is no contention, but also as a fallback during table    * initialization races. Updated via CAS.    */
DECL|field|base
specifier|transient
specifier|volatile
name|long
name|base
decl_stmt|;
comment|/** Spinlock (locked via CAS) used when resizing and/or creating Cells. */
DECL|field|busy
specifier|transient
specifier|volatile
name|int
name|busy
decl_stmt|;
comment|/** Package-private default constructor */
DECL|method|Striped64 ()
name|Striped64
parameter_list|()
block|{}
comment|/** CASes the base field. */
DECL|method|casBase (long cmp, long val)
specifier|final
name|boolean
name|casBase
parameter_list|(
name|long
name|cmp
parameter_list|,
name|long
name|val
parameter_list|)
block|{
return|return
name|UNSAFE
operator|.
name|compareAndSwapLong
argument_list|(
name|this
argument_list|,
name|baseOffset
argument_list|,
name|cmp
argument_list|,
name|val
argument_list|)
return|;
block|}
comment|/** CASes the busy field from 0 to 1 to acquire lock. */
DECL|method|casBusy ()
specifier|final
name|boolean
name|casBusy
parameter_list|()
block|{
return|return
name|UNSAFE
operator|.
name|compareAndSwapInt
argument_list|(
name|this
argument_list|,
name|busyOffset
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
return|;
block|}
comment|/**    * Computes the function of current and new value. Subclasses should open-code this update    * function for most uses, but the virtualized form is needed within retryUpdate.    *    * @param currentValue the current value (of either base or a cell)    * @param newValue the argument from a user update call    * @return result of the update function    */
DECL|method|fn (long currentValue, long newValue)
specifier|abstract
name|long
name|fn
parameter_list|(
name|long
name|currentValue
parameter_list|,
name|long
name|newValue
parameter_list|)
function_decl|;
comment|/**    * Handles cases of updates involving initialization, resizing, creating new Cells, and/or    * contention. See above for explanation. This method suffers the usual non-modularity problems of    * optimistic retry code, relying on rechecked sets of reads.    *    * @param x the value    * @param hc the hash code holder    * @param wasUncontended false if CAS failed before call    */
DECL|method|retryUpdate (long x, @Nullable int[] hc, boolean wasUncontended)
specifier|final
name|void
name|retryUpdate
parameter_list|(
name|long
name|x
parameter_list|,
annotation|@
name|Nullable
name|int
index|[]
name|hc
parameter_list|,
name|boolean
name|wasUncontended
parameter_list|)
block|{
name|int
name|h
decl_stmt|;
if|if
condition|(
name|hc
operator|==
literal|null
condition|)
block|{
name|threadHashCode
operator|.
name|set
argument_list|(
name|hc
operator|=
operator|new
name|int
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
comment|// Initialize randomly
name|int
name|r
init|=
name|rng
operator|.
name|nextInt
argument_list|()
decl_stmt|;
comment|// Avoid zero to allow xorShift rehash
name|h
operator|=
name|hc
index|[
literal|0
index|]
operator|=
operator|(
name|r
operator|==
literal|0
operator|)
condition|?
literal|1
else|:
name|r
expr_stmt|;
block|}
else|else
name|h
operator|=
name|hc
index|[
literal|0
index|]
expr_stmt|;
name|boolean
name|collide
init|=
literal|false
decl_stmt|;
comment|// True if last slot nonempty
for|for
control|(
init|;
condition|;
control|)
block|{
name|Cell
index|[]
name|as
decl_stmt|;
name|Cell
name|a
decl_stmt|;
name|int
name|n
decl_stmt|;
name|long
name|v
decl_stmt|;
if|if
condition|(
operator|(
name|as
operator|=
name|cells
operator|)
operator|!=
literal|null
operator|&&
operator|(
name|n
operator|=
name|as
operator|.
name|length
operator|)
operator|>
literal|0
condition|)
block|{
if|if
condition|(
operator|(
name|a
operator|=
name|as
index|[
operator|(
name|n
operator|-
literal|1
operator|)
operator|&
name|h
index|]
operator|)
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|busy
operator|==
literal|0
condition|)
block|{
comment|// Try to attach new Cell
name|Cell
name|r
init|=
operator|new
name|Cell
argument_list|(
name|x
argument_list|)
decl_stmt|;
comment|// Optimistically create
if|if
condition|(
name|busy
operator|==
literal|0
operator|&&
name|casBusy
argument_list|()
condition|)
block|{
name|boolean
name|created
init|=
literal|false
decl_stmt|;
try|try
block|{
comment|// Recheck under lock
name|Cell
index|[]
name|rs
decl_stmt|;
name|int
name|m
decl_stmt|,
name|j
decl_stmt|;
if|if
condition|(
operator|(
name|rs
operator|=
name|cells
operator|)
operator|!=
literal|null
operator|&&
operator|(
name|m
operator|=
name|rs
operator|.
name|length
operator|)
operator|>
literal|0
operator|&&
name|rs
index|[
name|j
operator|=
operator|(
name|m
operator|-
literal|1
operator|)
operator|&
name|h
index|]
operator|==
literal|null
condition|)
block|{
name|rs
index|[
name|j
index|]
operator|=
name|r
expr_stmt|;
name|created
operator|=
literal|true
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|busy
operator|=
literal|0
expr_stmt|;
block|}
if|if
condition|(
name|created
condition|)
break|break;
continue|continue;
comment|// Slot is now non-empty
block|}
block|}
name|collide
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|wasUncontended
condition|)
comment|// CAS already known to fail
name|wasUncontended
operator|=
literal|true
expr_stmt|;
comment|// Continue after rehash
elseif|else
if|if
condition|(
name|a
operator|.
name|cas
argument_list|(
name|v
operator|=
name|a
operator|.
name|value
argument_list|,
name|fn
argument_list|(
name|v
argument_list|,
name|x
argument_list|)
argument_list|)
condition|)
break|break;
elseif|else
if|if
condition|(
name|n
operator|>=
name|NCPU
operator|||
name|cells
operator|!=
name|as
condition|)
name|collide
operator|=
literal|false
expr_stmt|;
comment|// At max size or stale
elseif|else
if|if
condition|(
operator|!
name|collide
condition|)
name|collide
operator|=
literal|true
expr_stmt|;
elseif|else
if|if
condition|(
name|busy
operator|==
literal|0
operator|&&
name|casBusy
argument_list|()
condition|)
block|{
try|try
block|{
if|if
condition|(
name|cells
operator|==
name|as
condition|)
block|{
comment|// Expand table unless stale
name|Cell
index|[]
name|rs
init|=
operator|new
name|Cell
index|[
name|n
operator|<<
literal|1
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|n
condition|;
operator|++
name|i
control|)
name|rs
index|[
name|i
index|]
operator|=
name|as
index|[
name|i
index|]
expr_stmt|;
name|cells
operator|=
name|rs
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|busy
operator|=
literal|0
expr_stmt|;
block|}
name|collide
operator|=
literal|false
expr_stmt|;
continue|continue;
comment|// Retry with expanded table
block|}
name|h
operator|^=
name|h
operator|<<
literal|13
expr_stmt|;
comment|// Rehash
name|h
operator|^=
name|h
operator|>>>
literal|17
expr_stmt|;
name|h
operator|^=
name|h
operator|<<
literal|5
expr_stmt|;
name|hc
index|[
literal|0
index|]
operator|=
name|h
expr_stmt|;
comment|// Record index for next time
block|}
elseif|else
if|if
condition|(
name|busy
operator|==
literal|0
operator|&&
name|cells
operator|==
name|as
operator|&&
name|casBusy
argument_list|()
condition|)
block|{
name|boolean
name|init
init|=
literal|false
decl_stmt|;
try|try
block|{
comment|// Initialize table
if|if
condition|(
name|cells
operator|==
name|as
condition|)
block|{
name|Cell
index|[]
name|rs
init|=
operator|new
name|Cell
index|[
literal|2
index|]
decl_stmt|;
name|rs
index|[
name|h
operator|&
literal|1
index|]
operator|=
operator|new
name|Cell
argument_list|(
name|x
argument_list|)
expr_stmt|;
name|cells
operator|=
name|rs
expr_stmt|;
name|init
operator|=
literal|true
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|busy
operator|=
literal|0
expr_stmt|;
block|}
if|if
condition|(
name|init
condition|)
break|break;
block|}
elseif|else
if|if
condition|(
name|casBase
argument_list|(
name|v
operator|=
name|base
argument_list|,
name|fn
argument_list|(
name|v
argument_list|,
name|x
argument_list|)
argument_list|)
condition|)
break|break;
comment|// Fall back on using base
block|}
block|}
comment|/** Sets base and all cells to the given value. */
DECL|method|internalReset (long initialValue)
specifier|final
name|void
name|internalReset
parameter_list|(
name|long
name|initialValue
parameter_list|)
block|{
name|Cell
index|[]
name|as
init|=
name|cells
decl_stmt|;
name|base
operator|=
name|initialValue
expr_stmt|;
if|if
condition|(
name|as
operator|!=
literal|null
condition|)
block|{
name|int
name|n
init|=
name|as
operator|.
name|length
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|n
condition|;
operator|++
name|i
control|)
block|{
name|Cell
name|a
init|=
name|as
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|a
operator|!=
literal|null
condition|)
name|a
operator|.
name|value
operator|=
name|initialValue
expr_stmt|;
block|}
block|}
block|}
comment|// Unsafe mechanics
DECL|field|UNSAFE
specifier|private
specifier|static
specifier|final
name|sun
operator|.
name|misc
operator|.
name|Unsafe
name|UNSAFE
decl_stmt|;
DECL|field|baseOffset
specifier|private
specifier|static
specifier|final
name|long
name|baseOffset
decl_stmt|;
DECL|field|busyOffset
specifier|private
specifier|static
specifier|final
name|long
name|busyOffset
decl_stmt|;
static|static
block|{
try|try
block|{
name|UNSAFE
operator|=
name|getUnsafe
argument_list|()
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|sk
init|=
name|Striped64
operator|.
name|class
decl_stmt|;
name|baseOffset
operator|=
name|UNSAFE
operator|.
name|objectFieldOffset
argument_list|(
name|sk
operator|.
name|getDeclaredField
argument_list|(
literal|"base"
argument_list|)
argument_list|)
expr_stmt|;
name|busyOffset
operator|=
name|UNSAFE
operator|.
name|objectFieldOffset
argument_list|(
name|sk
operator|.
name|getDeclaredField
argument_list|(
literal|"busy"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Error
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**    * Returns a sun.misc.Unsafe. Suitable for use in a 3rd party package. Replace with a simple call    * to Unsafe.getUnsafe when integrating into a jdk.    *    * @return a sun.misc.Unsafe    */
DECL|method|getUnsafe ()
specifier|private
specifier|static
name|sun
operator|.
name|misc
operator|.
name|Unsafe
name|getUnsafe
parameter_list|()
block|{
try|try
block|{
return|return
name|sun
operator|.
name|misc
operator|.
name|Unsafe
operator|.
name|getUnsafe
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|tryReflectionInstead
parameter_list|)
block|{     }
try|try
block|{
return|return
name|java
operator|.
name|security
operator|.
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|java
operator|.
name|security
operator|.
name|PrivilegedExceptionAction
argument_list|<
name|sun
operator|.
name|misc
operator|.
name|Unsafe
argument_list|>
argument_list|()
block|{
specifier|public
name|sun
operator|.
name|misc
operator|.
name|Unsafe
name|run
parameter_list|()
throws|throws
name|Exception
block|{
name|Class
argument_list|<
name|sun
operator|.
name|misc
operator|.
name|Unsafe
argument_list|>
name|k
init|=
name|sun
operator|.
name|misc
operator|.
name|Unsafe
operator|.
name|class
decl_stmt|;
for|for
control|(
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
name|f
range|:
name|k
operator|.
name|getDeclaredFields
argument_list|()
control|)
block|{
name|f
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Object
name|x
init|=
name|f
operator|.
name|get
argument_list|(
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|k
operator|.
name|isInstance
argument_list|(
name|x
argument_list|)
condition|)
return|return
name|k
operator|.
name|cast
argument_list|(
name|x
argument_list|)
return|;
block|}
throw|throw
operator|new
name|NoSuchFieldError
argument_list|(
literal|"the Unsafe"
argument_list|)
throw|;
block|}
block|}
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|security
operator|.
name|PrivilegedActionException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not initialize intrinsics"
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

