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

package io.element.android.config

import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import io.element.android.libraries.di.AppScope
import io.element.android.libraries.di.SingleIn
import io.element.android.x.config.ElementConfig
import io.element.extension.ElementConfigProvider
import io.element.extension.connection.ConnectionConfig
import timber.log.Timber
import java.util.*

@Module
@ContributesTo(AppScope::class)
interface ConfigModule {
    companion object {
        @Provides
        @SingleIn(AppScope::class)
        fun provideConfigProvider(): ElementConfigProvider = ElementConfig().also {
            Timber.d("CONFIG: ${it.configName} ")
        }

        @Provides
        fun provideConnectionModule(
            elementConfigProvider: ElementConfigProvider
        ): ConnectionConfig? =
            elementConfigProvider.connectionConfig().also {
                Timber.d("CONFIG: ConnectionModule ${it} ")
            }
    }
}
