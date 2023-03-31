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

package io.element.extension.host.impl

import io.element.extension.ElementExtension
import io.element.extension.ElementExtensionProvider
import io.element.extension.connection.ConnectionConfig
import io.element.extension.host.api.ExtensionHost
import io.element.extension.host.api.onboarding.OnboardingExtensionHost
import io.element.extension.host.impl.onboarding.DefaultOnboardingExtensionHost
import io.element.extension.lifecycle
import io.element.extension.lifecycle.LifecycleExtension
import io.element.extension.login
import io.element.extension.login.LoginExtension
import io.element.extension.onboarding
import javax.inject.Inject

class DefaultExtensionHost @Inject constructor(
    override val connectionConfig: ConnectionConfig?,
    private val extensions: Array<ElementExtension>,
) : ExtensionHost {
    private val extensionProvider: ElementExtensionProvider = object : ElementExtensionProvider {
        override fun extensions(): List<ElementExtension> =
            extensions.toList()
    }

    override val lifecycleExtensions: LifecycleExtension =
        extensionProvider.lifecycle().toSimpleCompositeExtension()

    override val loginExtensions: LoginExtension =
        extensionProvider.login().toSimpleCompositeExtension()

    override val onboardingExtensionHost: OnboardingExtensionHost =
        DefaultOnboardingExtensionHost(
            extensionProvider.onboarding()
        )
}
