/*
 * Copyright (c) 2023 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.element.modulesdk.host.impl

import io.element.modulesdk.ElementModule
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * This module delegates to a list of objects in the order they are given.
 * The result of the final delegate is returned from any method that returns a value.
 */
internal class SimpleCompositeModule<T : ElementModule>(private val objects: Iterable<T>) : InvocationHandler {
    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        return objects.map {
            method?.invoke(it, *(args ?: arrayOf()))
        }
            // Note that all results are ignored except the last
            .lastOrNull()
    }
}

internal inline fun <reified T : ElementModule> Iterable<T>.toSimpleCompositeModule(): T {
    val classLoader = T::class.java.classLoader
    val delegates = this
    val compositeModule = SimpleCompositeModule(delegates)

    return Proxy.newProxyInstance(classLoader, arrayOf(T::class.java), compositeModule) as T
}

internal inline fun <reified T : ElementModule> Array<T>.toSimpleCompositeModule(): T
    = this.asIterable().toSimpleCompositeModule()

