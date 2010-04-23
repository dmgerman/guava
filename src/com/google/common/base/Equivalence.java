begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (C) 2010 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|Beta
import|;
end_import

begin_comment
comment|/**  * A strategy for determining whether two instances are considered equivalent.  * Examples of equivalences are the {@link Equivalences#identity() identity  * equivalence} and {@link Equivalences#equals equals equivalence}.  *  * @author Bob Lee  * @since 4  */
end_comment

begin_interface
annotation|@
name|Beta
DECL|interface|Equivalence
specifier|public
interface|interface
name|Equivalence
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**    * Returns {@code true} if the given objects are considered equivalent.    *    *<p>The<code>equivalent</code> method implements an equivalence relation    * on non-null object references:    *<ul>    *<li>It is<i>reflexive</i>: for any non-null reference value    *<code>x</code>,<code>x.equals(x)</code> should return    *<code>true</code>.    *<li>It is<i>symmetric</i>: for any non-null reference values    *<code>x</code> and<code>y</code>,<code>x.equals(y)</code>    *     should return<code>true</code> if and only if    *<code>y.equals(x)</code> returns<code>true</code>.    *<li>It is<i>transitive</i>: for any non-null reference values    *<code>x</code>,<code>y</code>, and<code>z</code>, if    *<code>x.equals(y)</code> returns<code>true</code> and    *<code>y.equals(z)</code> returns<code>true</code>, then    *<code>x.equals(z)</code> should return<code>true</code>.    *<li>It is<i>consistent</i>: for any non-null reference values    *<code>x</code> and<code>y</code>, multiple invocations of    *<tt>x.equals(y)</tt> consistently return<code>true</code>    *     or consistently return<code>false</code>, provided no    *     information used in<code>equals</code> comparisons on the    *     objects is modified.    *<li>For any non-null reference value<code>x</code>,    *<code>x.equals(null)</code> should return<code>false</code>.    *</ul>    */
DECL|method|equivalent (T a, T b)
name|boolean
name|equivalent
parameter_list|(
name|T
name|a
parameter_list|,
name|T
name|b
parameter_list|)
function_decl|;
comment|/**    * Returns a hash code for {@code object}. This function<b>must</b> return    * the same value for any two instances which are {@link #equivalent}, and    * should as often as possible return a distinct value for instances which    * are not equivalent.    *    * @see Object#hashCode the same contractual obligations apply here    * @throws NullPointerException if t is null    */
DECL|method|hash (T t)
name|int
name|hash
parameter_list|(
name|T
name|t
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

