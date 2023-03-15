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

package io.element.modulesdk.host.api

import io.element.modulesdk.connection.ConnectionModule
import io.element.modulesdk.lifecycle.LifecycleModule

/**
 * Interface between the host app and the modules
 */
interface ModuleHost {
    val connectionModule: ConnectionModule?
    val lifecycleModule: LifecycleModule?
}
