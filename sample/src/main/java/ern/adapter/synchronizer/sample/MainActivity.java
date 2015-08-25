package ern.adapter.synchronizer.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ern.adapter.sycnhronizer.AdapterSynchronizer;
import ern.adapter.sycnhronizer.SyncType;

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
public class MainActivity extends AppCompatActivity {

    private static final int ITEM_COUNT_PER_LOAD = 5;

    public final int ADAPTER_ID = MyApplication.getNewId();

    private MyAdapter mAdapter;

    private Object mObject;

    private SyncType mLastOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.forceOverflowMenu(this);
        final ListView listView = (ListView) findViewById(R.id.listView);
        mAdapter = new MyAdapter(this);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                remove(mAdapter.getItem(position));
            }
        });
        addMultipleItems(ITEM_COUNT_PER_LOAD, false);
        AdapterSynchronizer.getInstance().add(ADAPTER_ID, mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_default, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_single:
                addSingleItem();
                return true;
            case R.id.action_add_multi:
                addMultipleItems(ITEM_COUNT_PER_LOAD, true);
                return true;
            case R.id.action_go:
                startActivity(new Intent(this, TrailActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void remove(MyObject object) {
        if(object != null) {
            mObject = object;
            mLastOperation = SyncType.REMOVE;
            mAdapter.remove(object);
            Utils.callSyncDialog(this, mObject, mLastOperation, ADAPTER_ID);
        }
    }

    private void addSingleItem() {
        mLastOperation = SyncType.ADD_SINGLE;
        final int index = mAdapter.getCount() + 1;
        final MyObject object = new MyObject("Adapter " + ADAPTER_ID + " - Item " + index, " Description " + index);
        mObject = object;
        mAdapter.add(object);
        Utils.callSyncDialog(this, mObject, mLastOperation, ADAPTER_ID);
    }

    private void addMultipleItems(int count, boolean callSyncDialog) {
        if(count > 0) {
            mLastOperation = SyncType.ADD_MULTIPLE;
            final List<MyObject> objects = new ArrayList<>(count);
            for(int i = 0; i < count; i++) {
                final int index = mAdapter.getCount() + objects.size() + 1;
                final MyObject object = new MyObject("Adapter " + ADAPTER_ID + " - Item " + index, " Description " + index);
                objects.add(object);
            }
            mAdapter.addAll(objects);
            mObject = objects;
            if(callSyncDialog) Utils.callSyncDialog(this, mObject, mLastOperation, ADAPTER_ID);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}