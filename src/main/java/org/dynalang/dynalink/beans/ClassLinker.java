/*
   Copyright 2009-2013 Attila Szegedi

   Licensed under either the Apache License, Version 2.0 (the "Apache
   License") or the BSD License (the "BSD License"), with licensee
   being free to choose either of the two at their discretion.

   You may not use this file except in compliance with either the Apache
   License or the BSD License.

   A copy of the BSD License is available in the root directory of the
   source distribution of the project under the file name
   "Dynalink-License-BSD.txt".

   A copy of the Apache License is available in the root directory of the
   source distribution of the project under the file name
   "Dynalink-License-Apache-2.0.txt". Alternatively, you may obtain a
   copy of the Apache License at <http://www.apache.org/licenses/LICENSE-2.0>

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See your chosen License for the specific language governing permissions
   and limitations under that License.
*/

package org.dynalang.dynalink.beans;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import org.dynalang.dynalink.beans.GuardedInvocationComponent.ValidationType;
import org.dynalang.dynalink.support.Lookup;

/**
 * A linker for java.lang.Class objects. Provides a synthetic property "static" that allows access to static fields and
 * methods on the class (respecting property getter/setter conventions). Note that Class objects are not recognized by
 * the Dynalink as constructors for the instances of the class, {@link StaticClass} is used for this purpose.
 * @author Attila Szegedi
 */
class ClassLinker extends BeanLinker {

    ClassLinker() {
        super(Class.class);
        // Map "classObject.static" to StaticClass.forClass(classObject). Can use EXACT_CLASS since class Class is final.
        setPropertyGetter("static", FOR_CLASS, ValidationType.EXACT_CLASS);
    }

    private static final MethodHandle FOR_CLASS = new Lookup(MethodHandles.lookup()).findStatic(StaticClass.class,
            "forClass", MethodType.methodType(StaticClass.class, Class.class));

}