/*
   Copyright 2009-2012 Attila Szegedi

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package org.dynalang.dynalink.beans;

import java.lang.invoke.MethodHandle;

import org.dynalang.dynalink.linker.CallSiteDescriptor;
import org.dynalang.dynalink.linker.LinkerServices;

/**
 * Represents a single dynamic method. A "dynamic" method can be bound to a single Java method, or can be bound to all
 * overloaded methods of the same name on a class. Getting an invocation of a dynamic method bound to multiple
 * overloaded methods will perform overload resolution (actually, it will perform partial overloaded resolution at link
 * time, but if that fails to identify exactly one target method, it will generate a method handle that will perform the
 * rest of the overload resolution at invocation time for actual argument types).
 *
 * @author Attila Szegedi
 * @version $Id: $
 */
interface DynamicMethod {
    /**
     * Creates an invocation for the dynamic method. If the method is overloaded, it will perform overloaded method
     * resolution based on the formal argument types in the call site. The resulting resolution can either identify a
     * single method to be invoked among the overloads, or it can identify multiple ones. In the latter case, the
     * returned method handle will perform further overload resolution among these candidates at every invocation. If
     * the method to be invoked is a variable arguments (vararg) method, it will pack the extra arguments in an array
     * before the invocation of the underlying method if it is not already done.
     *
     * @param callSiteDescriptor descriptor of the call site
     * @param linkerServices linker services. Used for querying available language-specific type conversions in order to
     * identify candidate overloaded methods.
     * @return an invocation suitable for calling the method from the specified call site.
     */
    abstract MethodHandle getInvocation(CallSiteDescriptor callSiteDescriptor, LinkerServices linkerServices);

    /**
     * True if this dynamic method already contains a method handle with an identical signature as the passed in method
     * handle.
     * @param mh the method handle to check
     * @return true if it already contains an equivalent method handle.
     */
    boolean contains(MethodHandle mh);
}