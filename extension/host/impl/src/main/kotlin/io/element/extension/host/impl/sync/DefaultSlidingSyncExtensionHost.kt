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

package io.element.extension.host.impl.sync

import io.element.android.libraries.matrix.api.MatrixClient
import io.element.extension.host.api.sync.SlidingSyncExtensionHost
import io.element.extension.sync.SlidingSyncExtension
import javax.inject.Inject

class DefaultSlidingSyncExtensionHost @Inject constructor(
    private val slidingSyncExtensions: List<SlidingSyncExtension>,
) : SlidingSyncExtensionHost {
    override fun onNewMatrixClient(matrixClient: MatrixClient) {
        slidingSyncExtensions.forEach { extension ->
            val dataSource = matrixClient.roomSummaryDataSource

            dataSource.addSlidingSyncObserver {
                extension.onSlidingSyncUpdate(it.lists, it.rooms)
            }
        }
    }

    override fun onDestroy(matrixClient: MatrixClient) {
        matrixClient.roomSummaryDataSource.clearSlidingSyncObservers()
    }
}
