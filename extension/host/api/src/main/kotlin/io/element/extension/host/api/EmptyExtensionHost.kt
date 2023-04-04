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

package io.element.extension.host.api

import io.element.extension.connection.ConnectionConfig
import io.element.extension.host.api.onboarding.EmptyOnboardingExtensionHost
import io.element.extension.host.api.onboarding.OnboardingExtensionHost
import io.element.extension.host.api.sync.EmptySlidingSyncExtensionHost
import io.element.extension.host.api.sync.SlidingSyncExtensionHost
import io.element.extension.lifecycle.LifecycleExtension
import io.element.extension.login.LoginExtension

/**
 * Convenience for a module host that does nothing
 */
class EmptyExtensionHost : ExtensionHost {
    override val lifecycleExtensions: LifecycleExtension?
        get() = null
    override val connectionConfig: ConnectionConfig?
        get() = null
    override val loginExtensions: LoginExtension?
        get() = null
    override val onboardingExtensionHost: OnboardingExtensionHost
        get() = EmptyOnboardingExtensionHost()
    override val slidingSyncExtensionHost: SlidingSyncExtensionHost
        get() = EmptySlidingSyncExtensionHost()
}
