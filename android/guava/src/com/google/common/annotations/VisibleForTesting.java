begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2006 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.annotations
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
package|;
end_package

begin_comment
comment|/**  * Annotates a program element that exists, or is more widely visible than otherwise necessary, only  * for use in test code.  *  *<p><b>Do not use this interface</b> for public or protected declarations: it is a fig leaf for  * bad design, and it does not prevent anyone from using the declaration---and experience has shown  * that they will. If the method breaks the encapsulation of its class, then its internal  * representation will be hard to change. Instead, use<a  * href="http://errorprone.info/bugpattern/RestrictedApiChecker">RestrictedApiChecker</a>, which  * enforces fine-grained visibility policies.  *  * @author Johannes Henkel  */
end_comment

begin_annotation_defn
annotation|@
name|GwtCompatible
DECL|annotation|VisibleForTesting
specifier|public
annotation_defn|@interface
name|VisibleForTesting
block|{ }
end_annotation_defn

end_unit

