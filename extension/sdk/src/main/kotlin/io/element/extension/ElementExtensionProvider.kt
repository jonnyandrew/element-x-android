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

package io.element.extension

import io.element.extension.lifecycle.LifecycleExtension
import io.element.extension.login.LoginExtension
import io.element.extension.onboarding.OnboardingExtension
import io.element.extension.sync.SlidingSyncExtension

/**
 * Implement this interface to provide an extension
 */
interface ElementExtensionProvider {
    fun extensions(): List<ElementExtension>
}

fun ElementExtensionProvider.login() = get<LoginExtension>()
fun ElementExtensionProvider.lifecycle() = get<LifecycleExtension>()
fun ElementExtensionProvider.onboarding() = get<OnboardingExtension>()
fun ElementExtensionProvider.slidingSync() = get<SlidingSyncExtension>()

private inline fun <reified T : ElementExtension>
    ElementExtensionProvider.get() = extensions().filterIsInstance<T>()

