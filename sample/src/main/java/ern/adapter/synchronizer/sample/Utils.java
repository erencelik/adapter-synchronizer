package ern.adapter.synchronizer.sample;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;
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
public class Utils {

    private static final String TAG = "Utils";

    public static void callSyncDialog(Context context, final Object object, final SyncType type, int callerAdapterId) {

        final int count = AdapterSynchronizer.getInstance().getAdapterById(callerAdapterId) == null
                ? AdapterSynchronizer.getInstance().getAdapterCount()
                : AdapterSynchronizer.getInstance().getAdapterCount() - 1;

        final CharSequence[] txtAdapters = new CharSequence[count];

        int i = 0;

        for(int id : AdapterSynchronizer.getInstance().getIds()) {
            if(id != callerAdapterId) {
                txtAdapters[i] = "Adapter " + id;
                i++;
            }
        }

        if(txtAdapters.length > 0) {

            final List<Integer> ids = new ArrayList<>();

            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Select adapter(s) to be synchronized");

            builder.setMultiChoiceItems(txtAdapters, null, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    final int adapterId = Integer.valueOf(txtAdapters[which].toString().replace("Adapter ", ""));
                    if (isChecked) ids.add(adapterId);
                    else {
                        if (ids.contains(adapterId))
                            ids.remove(adapterId);
                    }
                }
            });

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AdapterSynchronizer.getInstance().synchronize(object, type, convertPrimitive(ids));
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create().show();

        }

    }

    private static int[] convertPrimitive(List<Integer> integers) {
        final int[] ret = new int[integers.size()];
        for (int i = 0; i < ret.length; i++) ret[i] = integers.get(i);
        return ret;
    }

    public static void forceOverflowMenu(Context context) {
        try {
            final ViewConfiguration config = ViewConfiguration.get(context);
            final Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        }
        catch (Exception exception) {
            Log.e(TAG, "Exception : " + exception.getMessage());
        }
    }

}