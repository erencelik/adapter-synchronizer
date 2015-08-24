package ern.adapter.sycnhronizer;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
public final class AdapterSynchronizer implements Synchronizer {

    private static final String TAG = "AdapterSynchronizer";

    private static final AdapterSynchronizer mInstance = new AdapterSynchronizer();

    private final List<SyncAdapter> mSyncAdapters = new ArrayList<>();

    private InternalSynchronizer mInternalSynchronizer;

    /**
     * Private constructor for constructing single instance
     * Prevents from multiple construction of this class
     */
    private AdapterSynchronizer() {
        // Singleton constructor...
    }

    /**
     * Obtain singleton instance of this class
     * @return a singleton instance of {@link AdapterSynchronizer}
     */
    public static AdapterSynchronizer getInstance() {
        return mInstance;
    }

    /**
     * Initialization method of this module
     * Call this method on your Application's onCreate() method
     * {@link Application#onCreate()}
     * @param context your application context for capturing main looper
     */
    @Override
    public void init(Context context) {
        this.mInternalSynchronizer = new InternalSynchronizer(context);
        this.mInternalSynchronizer.start();
        Log.d(TAG, "Initialization completed");
    }

    /**
     * This method adds an adapter with given unique id
     * If an adapter with given id is already exists then
     * this method will not work as you expected
     * @param id unique id for adding adapter
     * @param adapter an adapter to be added to synchronizer
     */
    @Override
    public void add(int id, ArrayAdapter adapter) {
        if(id > 0 && adapter != null) {
            boolean exists = false;
            for(SyncAdapter syncAdapter : mSyncAdapters) {
                if(syncAdapter.getId() == id) {
                    exists = true;
                    break;
                }
            }
            if(!exists) mSyncAdapters.add(SyncAdapter.create(id, adapter));
            else Log.e(TAG, "Adapter with given id " + id + " was already added");
        }
    }

    /**
     * This method should be called if an adapter will not be using or not being synchronized anymore
     * You could call this i.e your activity finish method {@link Activity#finish()}
     * @param id an identity number for removing adapter
     */
    @Override
    public void remove(int id) {
        if(id > 0) {
            boolean exists = false;
            for(SyncAdapter syncAdapter : mSyncAdapters) {
                if(syncAdapter.getId() == id) {
                    exists = true;
                    mSyncAdapters.remove(syncAdapter);
                    break;
                }
            }
            if(!exists)
                Log.e(TAG, "There is no adapter with this id " + id);
        }
    }

    /**
     * This method synchronize adapter(s) with given object and type
     * @param object an object to be sync to adapter should be instance of T adapter
     * @param type synchronization type should be provided one of them;
     *             {@link ern.adapter.sycnhronizer.SyncType#ADD_SINGLE},
     *             {@link ern.adapter.sycnhronizer.SyncType#ADD_MULTIPLE},
     *             {@link ern.adapter.sycnhronizer.SyncType#REMOVE}
     * @param ids ids for determining which adapter should be synchronized if null or empty or
     *            not specified then all added adapters will be synchronized
     */
    @Override
    public void synchronize(Object object, SyncType type, int... ids) {
        if(mInternalSynchronizer != null) mInternalSynchronizer.sync(object, type, ids);
        else Log.e(TAG, "Initialize method was not called!");
    }

    /**
     * Get list of added adapters
     * @return list that contains array adapters.
     */
    @Override
    public List<ArrayAdapter> getAdapters() {
        final List<ArrayAdapter> adapters = new ArrayList<>(mSyncAdapters.size());
        for(SyncAdapter syncAdapter : mSyncAdapters)
            adapters.add(syncAdapter.getAdapter());
        return adapters;
    }

    /**
     * Get id array from adapters
     * @return array of integers that contains all of adapters ids
     */
    @Override
    public int[] getIds() {
        final int[] ids = new int[mSyncAdapters.size()];
        for(int i = 0; i < mSyncAdapters.size(); i++)
            ids[i] = mSyncAdapters.get(i).getId();
        return ids;
    }

    /**
     * Returns ArrayAdapter of synchronized adapters
     * If adapter not found with given id, then null returned
     * @param id to get adapter associated with
     * @return ArrayAdapter by given id
     */
    @Override
    public ArrayAdapter getAdapterById(int id) {
        if(id > 0) {
            for (SyncAdapter syncAdapter : mSyncAdapters) {
                if (syncAdapter.getId() == id) return syncAdapter.getAdapter();
            }
            return null;
        }
        else return null;
    }

    /**
     * Returns the size of an added adapters
     * @return size of synchronized adapters
     */
    public int getAdapterCount() {
        return mSyncAdapters.size();
    }

    /**
     * InternalSynchronizer class is a Thread that initialized and started
     * when {@link AdapterSynchronizer#init(Context)} method called
     */
    private static final class InternalSynchronizer extends Thread {

        private final Handler mHandler;

        private Object mObject;

        private SyncType mType;

        private int[] mIds;

        public InternalSynchronizer(Context context) {
            this.mHandler = new Handler(context.getMainLooper());
        }

        @SuppressWarnings("InfiniteLoopStatement")
        @Override
        public void run() {
            while(true) {
                synchronized (this) {
                    try {
                        wait();
                        if(mObject != null) {
                            if(mIds == null || mIds.length == 0)
                                mIds = AdapterSynchronizer.getInstance().getIds();
                            for (int id : mIds)
                                work(AdapterSynchronizer.getInstance().getAdapterById(id));
                        }
                    }
                    catch(InterruptedException exception) {
                        Log.e(TAG, exception.getMessage());
                        exception.printStackTrace();
                    }
                }
            }
        }

        /**
         * T
         * @param adapter
         */
        private void work(final ArrayAdapter adapter) {
            if(adapter != null) {
                mHandler.post(new Runnable() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void run() {
                        try {
                            switch (mType) {
                                case ADD_SINGLE:
                                    adapter.add(mObject);
                                    break;
                                case ADD_MULTIPLE:
                                    if(mObject instanceof Collection)
                                        adapter.addAll((Collection) mObject);
                                    else if(mObject instanceof Object[])
                                        adapter.addAll(mObject);
                                    break;
                                case REMOVE:
                                    adapter.remove(mObject);
                                    break;
                                default:
                                    Log.e(TAG, "Unexpected SyncType received");
                            }
                            adapter.notifyDataSetChanged();
                        } catch (NullPointerException | ClassCastException exception) {
                            Log.e(TAG, exception.getMessage());
                            exception.printStackTrace();
                        }
                    }
                });
            }
        }

        public void sync(Object object, SyncType type, int... ids) {
            if(object != null) {
                synchronized (this) {
                    this.mObject = object;
                    this.mType = type;
                    this.mIds = ids;
                    this.notify();
                }
            }
            else Log.e(TAG, "Object is null");
        }

    }


}