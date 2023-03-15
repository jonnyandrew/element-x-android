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

plugins {
    id("io.element.android-library")
    `maven-publish`
}

android {
    namespace = "io.element.android.modulesdk.sdk"

    publishing {
        // TODO: Publish the release variant
        singleVariant("debug") {
            withSourcesJar()
        }
    }

}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.2")
}


publishing {
    publications {
        register<MavenPublication>("debug") {
            groupId = "io.element.android.modulesdk.sdk"
            artifactId = "modulesdk"
            version = "0.1.0"

            afterEvaluate {
                from(components["debug"])
            }
        }
    }
}
