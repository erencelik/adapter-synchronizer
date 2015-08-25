package ern.adapter.sycnhronizer;

import android.widget.ArrayAdapter;

/*
* Copyright (C) 2015 H. Eren CELIK
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
class SyncAdapter {

    private final int id;

    private final ArrayAdapter adapter;

    private SyncAdapter(int id, ArrayAdapter adapter) {
        this.id = id;
        this.adapter = adapter;
    }

    public static SyncAdapter create(int id, ArrayAdapter adapter) {
        return new SyncAdapter(id, adapter);
    }

    public int getId() {
        return id;
    }

    public ArrayAdapter getAdapter() {
        return adapter;
    }

}