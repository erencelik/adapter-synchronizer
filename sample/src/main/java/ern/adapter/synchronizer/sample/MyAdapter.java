package ern.adapter.synchronizer.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
public class MyAdapter extends ArrayAdapter<MyObject> {

    private LayoutInflater mLayoutInflater;

    public MyAdapter(Context context) {
        super(context, R.layout.list_item_my_object);
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if(convertView == null) {
            holder = new Holder();
            convertView = mLayoutInflater.inflate(R.layout.list_item_my_object, parent, false);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            holder.txtDesc = (TextView) convertView.findViewById(R.id.txtDesc);
            convertView.setTag(holder);
        }
        else holder = (Holder) convertView.getTag();
        holder.bind(getItem(position));
        return convertView;
    }

    private static final class Holder {

        TextView txtTitle;
        TextView txtDesc;

        public void bind(MyObject object) {
            if(object != null) {
                txtTitle.setText(object.getTitle());
                txtDesc.setText(object.getDescription());
            }
        }

    }

}
