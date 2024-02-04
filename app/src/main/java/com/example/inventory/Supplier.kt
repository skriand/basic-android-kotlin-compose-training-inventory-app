/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory


/**
 * Provides the list of dummy contacts.
 * This sample implements this as constants, but real-life apps should use a database and such.
 */
data class Supplier(val name: String) {

    val icon = R.drawable.logo_avatar

    companion object {
        /**
         * Representative invalid contact ID.
         */
        val invalidId = -1

        /**
         * The contact ID.
         */
        val id = "contact_id"

        /**
         * The list of dummy contacts.
         */
        val suppliers = arrayOf(
            Supplier("Tereasa"),
            Supplier("Chang"),
            Supplier("Kory"),
            Supplier("Clare"),
            Supplier("Landon"),
            Supplier("Kyle"),
            Supplier("Deana"),
            Supplier("Daria"),
            Supplier("Melisa"),
            Supplier("Sammie")
        )

        /**
         * Finds a [Contact] specified by a contact ID.
         *
         * @param id The contact ID. This needs to be a valid ID.
         * @return A [Contact]
         */
        fun byId(id: Int) = suppliers[id]
    }
}