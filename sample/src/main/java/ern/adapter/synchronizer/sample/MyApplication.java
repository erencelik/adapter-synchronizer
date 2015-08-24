package ern.adapter.synchronizer.sample;

import android.app.Application;

import java.util.concurrent.atomic.AtomicInteger;

import ern.adapter.sycnhronizer.AdapterSynchronizer;

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
public class MyApplication extends Application {

    private static final AtomicInteger ID = new AtomicInteger(0);

    @Override
    public void onCreate() {
        super.onCreate();
        AdapterSynchronizer.getInstance().init(this);
    }

    public static int getNewId() {
        return ID.incrementAndGet();
    }

}