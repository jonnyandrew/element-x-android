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

package io.element.android.appnav

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.lifecycle.subscribe
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.plugin.Plugin
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.operation.push
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.element.android.anvilannotations.ContributesNode
import io.element.android.features.login.api.LoginEntryPoint
import io.element.android.features.onboarding.api.OnBoardingEntryPoint
import io.element.android.features.onboarding.api.PreOnBoardingEntryPoint
import io.element.android.libraries.architecture.BackstackNode
import io.element.android.libraries.architecture.animation.rememberDefaultTransitionHandler
import io.element.android.libraries.di.AppScope
import kotlinx.parcelize.Parcelize
import timber.log.Timber

@ContributesNode(AppScope::class)
class NotLoggedInFlowNode @AssistedInject constructor(
    @Assisted buildContext: BuildContext,
    @Assisted plugins: List<Plugin>,
    private val preOnBoardingEntryPoint: PreOnBoardingEntryPoint,
    private val onBoardingEntryPoint: OnBoardingEntryPoint,
    private val loginEntryPoint: LoginEntryPoint,
) : BackstackNode<NotLoggedInFlowNode.NavTarget>(
    backstack = BackStack(
        initialElement = onboardingNavTarget(preOnBoardingEntryPoint, 0),
        savedStateMap = buildContext.savedStateMap
    ),
    buildContext = buildContext,
    plugins = plugins,
) {
    init {
        lifecycle.subscribe(
            onCreate = { Timber.v("OnCreate") },
            onDestroy = { Timber.v("OnDestroy") }
        )
    }

    sealed interface NavTarget : Parcelable {
        @Parcelize
        sealed interface OnBoarding : NavTarget {
            @Parcelize
            data class Pre(val index: Int = 0) : OnBoarding

            @Parcelize
            object Main : OnBoarding
        }

        @Parcelize
        object LoginFlow : NavTarget
    }

    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node {
        return when (navTarget) {
            is NavTarget.OnBoarding.Pre -> {
                val callback = object : PreOnBoardingEntryPoint.Callback {
                    override fun onDone(index: Int) {
                        backstack.push(onboardingNavTarget(preOnBoardingEntryPoint, index + 1))
                    }
                }
                preOnBoardingEntryPoint
                    .createNode(this, buildContext, navTarget.index, callback)
            }
            NavTarget.OnBoarding.Main -> {
                val callback = object : OnBoardingEntryPoint.Callback {
                    override fun onSignUp() {
                        //NOOP
                    }

                    override fun onSignIn() {
                        backstack.push(NavTarget.LoginFlow)
                    }
                }
                onBoardingEntryPoint
                    .nodeBuilder(this, buildContext)
                    .callback(callback)
                    .build()
            }
            NavTarget.LoginFlow -> {
                loginEntryPoint.createNode(this, buildContext)
            }
        }
    }

    @Composable
    override fun View(modifier: Modifier) {
        Children(
            navModel = backstack,
            modifier = modifier,
            // Animate navigation to login screen
            transitionHandler = rememberDefaultTransitionHandler(),
        )
    }
}
private fun onboardingNavTarget(
    preOnBoardingEntryPoint: PreOnBoardingEntryPoint,
    index: Int
): NotLoggedInFlowNode.NavTarget.OnBoarding =
    if (preOnBoardingEntryPoint.totalNodes > index) {
        NotLoggedInFlowNode.NavTarget.OnBoarding.Pre(index)
    } else {
        NotLoggedInFlowNode.NavTarget.OnBoarding.Main
    }

