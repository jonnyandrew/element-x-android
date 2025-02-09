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

@file:OptIn(ExperimentalCoroutinesApi::class)

package io.element.android.features.login.impl.root

import app.cash.molecule.RecompositionClock
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.element.android.features.login.impl.root.LoggedInState
import io.element.android.features.login.impl.root.LoginFormState
import io.element.android.features.login.impl.root.LoginRootEvents
import io.element.android.features.login.impl.root.LoginRootPresenter
import io.element.android.libraries.matrix.test.A_HOMESERVER
import io.element.android.libraries.matrix.test.A_PASSWORD
import io.element.android.libraries.matrix.test.A_SESSION_ID
import io.element.android.libraries.matrix.test.A_THROWABLE
import io.element.android.libraries.matrix.test.A_USER_NAME
import io.element.android.libraries.matrix.test.auth.FakeAuthenticationService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LoginRootPresenterTest {
    @Test
    fun `present - initial state`() = runTest {
        val presenter = LoginRootPresenter(
            FakeAuthenticationService(),
        )
        moleculeFlow(RecompositionClock.Immediate) {
            presenter.present()
        }.test {
            val initialState = awaitItem()
            assertThat(initialState.homeserverDetails).isEqualTo(A_HOMESERVER)
            assertThat(initialState.loggedInState).isEqualTo(LoggedInState.NotLoggedIn)
            assertThat(initialState.formState).isEqualTo(LoginFormState.Default)
            assertThat(initialState.submitEnabled).isFalse()
        }
    }

    @Test
    fun `present - enter login and password`() = runTest {
        val presenter = LoginRootPresenter(
            FakeAuthenticationService(),
        )
        moleculeFlow(RecompositionClock.Immediate) {
            presenter.present()
        }.test {
            val initialState = awaitItem()
            initialState.eventSink.invoke(LoginRootEvents.SetLogin(A_USER_NAME))
            val loginState = awaitItem()
            assertThat(loginState.formState).isEqualTo(LoginFormState(login = A_USER_NAME, password = ""))
            assertThat(loginState.submitEnabled).isFalse()
            initialState.eventSink.invoke(LoginRootEvents.SetPassword(A_PASSWORD))
            val loginAndPasswordState = awaitItem()
            assertThat(loginAndPasswordState.formState).isEqualTo(LoginFormState(login = A_USER_NAME, password = A_PASSWORD))
            assertThat(loginAndPasswordState.submitEnabled).isTrue()
        }
    }

    @Test
    fun `present - submit`() = runTest {
        val presenter = LoginRootPresenter(
            FakeAuthenticationService(),
        )
        moleculeFlow(RecompositionClock.Immediate) {
            presenter.present()
        }.test {
            val initialState = awaitItem()
            initialState.eventSink.invoke(LoginRootEvents.SetLogin(A_USER_NAME))
            initialState.eventSink.invoke(LoginRootEvents.SetPassword(A_PASSWORD))
            skipItems(1)
            val loginAndPasswordState = awaitItem()
            loginAndPasswordState.eventSink.invoke(LoginRootEvents.Submit)
            val submitState = awaitItem()
            assertThat(submitState.loggedInState).isEqualTo(LoggedInState.LoggingIn)
            val loggedInState = awaitItem()
            assertThat(loggedInState.loggedInState).isEqualTo(LoggedInState.LoggedIn(A_SESSION_ID))
        }
    }

    @Test
    fun `present - submit with error`() = runTest {
        val authenticationService = FakeAuthenticationService()
        val presenter = LoginRootPresenter(
            authenticationService,
        )
        moleculeFlow(RecompositionClock.Immediate) {
            presenter.present()
        }.test {
            val initialState = awaitItem()
            initialState.eventSink.invoke(LoginRootEvents.SetLogin(A_USER_NAME))
            initialState.eventSink.invoke(LoginRootEvents.SetPassword(A_PASSWORD))
            skipItems(1)
            val loginAndPasswordState = awaitItem()
            authenticationService.givenLoginError(A_THROWABLE)
            loginAndPasswordState.eventSink.invoke(LoginRootEvents.Submit)
            val submitState = awaitItem()
            assertThat(submitState.loggedInState).isEqualTo(LoggedInState.LoggingIn)
            val loggedInState = awaitItem()
            assertThat(loggedInState.loggedInState).isEqualTo(LoggedInState.ErrorLoggingIn(A_THROWABLE))
        }
    }

    @Test
    fun `present - clear error`() = runTest {
        val authenticationService = FakeAuthenticationService()
        val presenter = LoginRootPresenter(
            authenticationService,
        )
        moleculeFlow(RecompositionClock.Immediate) {
            presenter.present()
        }.test {
            val initialState = awaitItem()

            // Submit will return an error
            authenticationService.givenLoginError(A_THROWABLE)
            initialState.eventSink(LoginRootEvents.Submit)
            awaitItem() // Skip LoggingIn state

            // Check an error was returned
            val submittedState = awaitItem()
            assertThat(submittedState.loggedInState).isEqualTo(LoggedInState.ErrorLoggingIn(A_THROWABLE))

            // Assert the error is then cleared
            submittedState.eventSink(LoginRootEvents.ClearError)
            val clearedState = awaitItem()
            assertThat(clearedState.loggedInState).isEqualTo(LoggedInState.NotLoggedIn)
        }
    }
}
