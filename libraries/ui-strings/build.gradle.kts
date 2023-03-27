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

// TODO: Remove once https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("io.element.android-library")
    `maven-publish`
    alias(libs.plugins.stemlibrary)
}

android {
    namespace = "io.element.android.libraries.ui.strings"
}

publishing {
    publications {
        register<MavenPublication>("debug") {
            groupId = "io.element.android.x"
            artifactId = "ui-strings"
            version = libs.versions.elementx.extension.sdk.get()

            afterEvaluate {
                from(components["debug"])
            }
        }
    }
}

// forcing the stem string template generator to be cacheable, without this the templates
// are regenerated causing the app module to recompile its sources
tasks.withType(com.likethesalad.android.templates.common.tasks.BaseTask::class.java) {
    outputs.cacheIf { true }
}
