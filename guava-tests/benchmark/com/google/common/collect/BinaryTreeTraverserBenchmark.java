begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
import|import
name|com
operator|.
name|google
operator|.
name|caliper
operator|.
name|BeforeExperiment
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|caliper
operator|.
name|Benchmark
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|caliper
operator|.
name|Param
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
name|Optional
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
name|primitives
operator|.
name|Ints
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
name|Random
import|;
end_import

begin_comment
comment|/**  * Benchmarks for the {@code TreeTraverser} and optimized {@code BinaryTreeTraverser} operations on  * binary trees.  *  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|BinaryTreeTraverserBenchmark
specifier|public
class|class
name|BinaryTreeTraverserBenchmark
block|{
DECL|class|BinaryNode
specifier|private
specifier|static
class|class
name|BinaryNode
block|{
DECL|field|x
specifier|final
name|int
name|x
decl_stmt|;
DECL|field|left
specifier|final
name|Optional
argument_list|<
name|BinaryNode
argument_list|>
name|left
decl_stmt|;
DECL|field|right
specifier|final
name|Optional
argument_list|<
name|BinaryNode
argument_list|>
name|right
decl_stmt|;
DECL|method|BinaryNode (int x, Optional<BinaryNode> left, Optional<BinaryNode> right)
name|BinaryNode
parameter_list|(
name|int
name|x
parameter_list|,
name|Optional
argument_list|<
name|BinaryNode
argument_list|>
name|left
parameter_list|,
name|Optional
argument_list|<
name|BinaryNode
argument_list|>
name|right
parameter_list|)
block|{
name|this
operator|.
name|x
operator|=
name|x
expr_stmt|;
name|this
operator|.
name|left
operator|=
name|left
expr_stmt|;
name|this
operator|.
name|right
operator|=
name|right
expr_stmt|;
block|}
block|}
DECL|enum|Topology
enum|enum
name|Topology
block|{
DECL|enumConstant|BALANCED
name|BALANCED
block|{
annotation|@
name|Override
name|Optional
argument_list|<
name|BinaryNode
argument_list|>
name|createTree
parameter_list|(
name|int
name|size
parameter_list|,
name|Random
name|rng
parameter_list|)
block|{
if|if
condition|(
name|size
operator|==
literal|0
condition|)
block|{
return|return
name|Optional
operator|.
name|absent
argument_list|()
return|;
block|}
else|else
block|{
name|int
name|leftChildSize
init|=
operator|(
name|size
operator|-
literal|1
operator|)
operator|/
literal|2
decl_stmt|;
name|int
name|rightChildSize
init|=
name|size
operator|-
literal|1
operator|-
name|leftChildSize
decl_stmt|;
return|return
name|Optional
operator|.
name|of
argument_list|(
operator|new
name|BinaryNode
argument_list|(
name|rng
operator|.
name|nextInt
argument_list|()
argument_list|,
name|createTree
argument_list|(
name|leftChildSize
argument_list|,
name|rng
argument_list|)
argument_list|,
name|createTree
argument_list|(
name|rightChildSize
argument_list|,
name|rng
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
block|,
DECL|enumConstant|ALL_LEFT
name|ALL_LEFT
block|{
annotation|@
name|Override
name|Optional
argument_list|<
name|BinaryNode
argument_list|>
name|createTree
parameter_list|(
name|int
name|size
parameter_list|,
name|Random
name|rng
parameter_list|)
block|{
name|Optional
argument_list|<
name|BinaryNode
argument_list|>
name|root
init|=
name|Optional
operator|.
name|absent
argument_list|()
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
name|size
condition|;
name|i
operator|++
control|)
block|{
name|root
operator|=
name|Optional
operator|.
name|of
argument_list|(
operator|new
name|BinaryNode
argument_list|(
name|rng
operator|.
name|nextInt
argument_list|()
argument_list|,
name|root
argument_list|,
name|Optional
operator|.
expr|<
name|BinaryNode
operator|>
name|absent
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|root
return|;
block|}
block|}
block|,
DECL|enumConstant|ALL_RIGHT
name|ALL_RIGHT
block|{
annotation|@
name|Override
name|Optional
argument_list|<
name|BinaryNode
argument_list|>
name|createTree
parameter_list|(
name|int
name|size
parameter_list|,
name|Random
name|rng
parameter_list|)
block|{
name|Optional
argument_list|<
name|BinaryNode
argument_list|>
name|root
init|=
name|Optional
operator|.
name|absent
argument_list|()
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
name|size
condition|;
name|i
operator|++
control|)
block|{
name|root
operator|=
name|Optional
operator|.
name|of
argument_list|(
operator|new
name|BinaryNode
argument_list|(
name|rng
operator|.
name|nextInt
argument_list|()
argument_list|,
name|Optional
operator|.
expr|<
name|BinaryNode
operator|>
name|absent
argument_list|()
argument_list|,
name|root
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|root
return|;
block|}
block|}
block|,
DECL|enumConstant|RANDOM
name|RANDOM
block|{
comment|/**        * Generates a tree with topology selected uniformly at random from the topologies of binary        * trees of the specified size.        */
annotation|@
name|Override
name|Optional
argument_list|<
name|BinaryNode
argument_list|>
name|createTree
parameter_list|(
name|int
name|size
parameter_list|,
name|Random
name|rng
parameter_list|)
block|{
name|int
index|[]
name|keys
init|=
operator|new
name|int
index|[
name|size
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
name|size
condition|;
name|i
operator|++
control|)
block|{
name|keys
index|[
name|i
index|]
operator|=
name|rng
operator|.
name|nextInt
argument_list|()
expr_stmt|;
block|}
return|return
name|createTreap
argument_list|(
name|Ints
operator|.
name|asList
argument_list|(
name|keys
argument_list|)
argument_list|)
return|;
block|}
comment|// See http://en.wikipedia.org/wiki/Treap for details on the algorithm.
specifier|private
name|Optional
argument_list|<
name|BinaryNode
argument_list|>
name|createTreap
parameter_list|(
name|List
argument_list|<
name|Integer
argument_list|>
name|keys
parameter_list|)
block|{
if|if
condition|(
name|keys
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Optional
operator|.
name|absent
argument_list|()
return|;
block|}
name|int
name|minIndex
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|keys
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|keys
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|<
name|keys
operator|.
name|get
argument_list|(
name|minIndex
argument_list|)
condition|)
block|{
name|minIndex
operator|=
name|i
expr_stmt|;
block|}
block|}
name|Optional
argument_list|<
name|BinaryNode
argument_list|>
name|leftChild
init|=
name|createTreap
argument_list|(
name|keys
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
name|minIndex
argument_list|)
argument_list|)
decl_stmt|;
name|Optional
argument_list|<
name|BinaryNode
argument_list|>
name|rightChild
init|=
name|createTreap
argument_list|(
name|keys
operator|.
name|subList
argument_list|(
name|minIndex
operator|+
literal|1
argument_list|,
name|keys
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|Optional
operator|.
name|of
argument_list|(
operator|new
name|BinaryNode
argument_list|(
name|keys
operator|.
name|get
argument_list|(
name|minIndex
argument_list|)
argument_list|,
name|leftChild
argument_list|,
name|rightChild
argument_list|)
argument_list|)
return|;
block|}
block|}
block|;
DECL|method|createTree (int size, Random rng)
specifier|abstract
name|Optional
argument_list|<
name|BinaryNode
argument_list|>
name|createTree
parameter_list|(
name|int
name|size
parameter_list|,
name|Random
name|rng
parameter_list|)
function_decl|;
block|}
DECL|field|BINARY_VIEWER
specifier|private
specifier|static
specifier|final
name|BinaryTreeTraverser
argument_list|<
name|BinaryNode
argument_list|>
name|BINARY_VIEWER
init|=
operator|new
name|BinaryTreeTraverser
argument_list|<
name|BinaryNode
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Optional
argument_list|<
name|BinaryNode
argument_list|>
name|leftChild
parameter_list|(
name|BinaryNode
name|node
parameter_list|)
block|{
return|return
name|node
operator|.
name|left
return|;
block|}
annotation|@
name|Override
specifier|public
name|Optional
argument_list|<
name|BinaryNode
argument_list|>
name|rightChild
parameter_list|(
name|BinaryNode
name|node
parameter_list|)
block|{
return|return
name|node
operator|.
name|right
return|;
block|}
block|}
decl_stmt|;
DECL|field|VIEWER
specifier|private
specifier|static
specifier|final
name|TreeTraverser
argument_list|<
name|BinaryNode
argument_list|>
name|VIEWER
init|=
operator|new
name|TreeTraverser
argument_list|<
name|BinaryNode
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterable
argument_list|<
name|BinaryNode
argument_list|>
name|children
parameter_list|(
name|BinaryNode
name|root
parameter_list|)
block|{
return|return
name|BINARY_VIEWER
operator|.
name|children
argument_list|(
name|root
argument_list|)
return|;
block|}
block|}
decl_stmt|;
DECL|enum|Traversal
enum|enum
name|Traversal
block|{
DECL|enumConstant|PRE_ORDER
name|PRE_ORDER
block|{
annotation|@
name|Override
argument_list|<
name|T
argument_list|>
name|Iterable
argument_list|<
name|T
argument_list|>
name|view
parameter_list|(
name|T
name|root
parameter_list|,
name|TreeTraverser
argument_list|<
name|T
argument_list|>
name|viewer
parameter_list|)
block|{
return|return
name|viewer
operator|.
name|preOrderTraversal
argument_list|(
name|root
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|POST_ORDER
name|POST_ORDER
block|{
annotation|@
name|Override
argument_list|<
name|T
argument_list|>
name|Iterable
argument_list|<
name|T
argument_list|>
name|view
parameter_list|(
name|T
name|root
parameter_list|,
name|TreeTraverser
argument_list|<
name|T
argument_list|>
name|viewer
parameter_list|)
block|{
return|return
name|viewer
operator|.
name|postOrderTraversal
argument_list|(
name|root
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|BREADTH_FIRST
name|BREADTH_FIRST
block|{
annotation|@
name|Override
argument_list|<
name|T
argument_list|>
name|Iterable
argument_list|<
name|T
argument_list|>
name|view
parameter_list|(
name|T
name|root
parameter_list|,
name|TreeTraverser
argument_list|<
name|T
argument_list|>
name|viewer
parameter_list|)
block|{
return|return
name|viewer
operator|.
name|breadthFirstTraversal
argument_list|(
name|root
argument_list|)
return|;
block|}
block|}
block|;
DECL|method|view (T root, TreeTraverser<T> viewer)
specifier|abstract
parameter_list|<
name|T
parameter_list|>
name|Iterable
argument_list|<
name|T
argument_list|>
name|view
parameter_list|(
name|T
name|root
parameter_list|,
name|TreeTraverser
argument_list|<
name|T
argument_list|>
name|viewer
parameter_list|)
function_decl|;
block|}
DECL|field|view
specifier|private
name|Iterable
argument_list|<
name|BinaryNode
argument_list|>
name|view
decl_stmt|;
DECL|field|topology
annotation|@
name|Param
name|Topology
name|topology
decl_stmt|;
annotation|@
name|Param
argument_list|(
block|{
literal|"1"
block|,
literal|"100"
block|,
literal|"10000"
block|,
literal|"1000000"
block|}
argument_list|)
DECL|field|size
name|int
name|size
decl_stmt|;
DECL|field|traversal
annotation|@
name|Param
name|Traversal
name|traversal
decl_stmt|;
DECL|field|useBinaryTraverser
annotation|@
name|Param
name|boolean
name|useBinaryTraverser
decl_stmt|;
annotation|@
name|Param
argument_list|(
block|{
literal|"1234"
block|}
argument_list|)
DECL|field|rng
name|SpecialRandom
name|rng
decl_stmt|;
annotation|@
name|BeforeExperiment
DECL|method|setUp ()
name|void
name|setUp
parameter_list|()
block|{
name|this
operator|.
name|view
operator|=
name|traversal
operator|.
name|view
argument_list|(
name|topology
operator|.
name|createTree
argument_list|(
name|size
argument_list|,
name|rng
argument_list|)
operator|.
name|get
argument_list|()
argument_list|,
name|useBinaryTraverser
condition|?
name|BINARY_VIEWER
else|:
name|VIEWER
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Benchmark
DECL|method|traversal (int reps)
name|int
name|traversal
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|int
name|tmp
init|=
literal|0
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
name|reps
condition|;
name|i
operator|++
control|)
block|{
for|for
control|(
name|BinaryNode
name|node
range|:
name|view
control|)
block|{
name|tmp
operator|+=
name|node
operator|.
name|x
expr_stmt|;
block|}
block|}
return|return
name|tmp
return|;
block|}
block|}
end_class

end_unit

