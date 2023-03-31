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

package io.element.android.features.onboarding.impl

import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.squareup.anvil.annotations.ContributesBinding
import io.element.android.features.onboarding.api.PreOnBoardingEntryPoint
import io.element.android.libraries.di.AppScope
import io.element.extension.host.api.ExtensionHost
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class DefaultPreOnBoardingEntryPoint @Inject constructor(
    private val extensionHost: ExtensionHost
) : PreOnBoardingEntryPoint {
    override val totalNodes: Int
        get() = extensionHost.onboardingExtensionHost.totalNodes

    override fun createNode(
        parentNode: Node,
        buildContext: BuildContext,
        index: Int,
        callback: PreOnBoardingEntryPoint.Callback
    ): Node {
        val onDone: () -> Unit = { callback.onDone(index) }

        return extensionHost.onboardingExtensionHost
            .createOnboardingNode(buildContext, index, onDone)
    }
}
