# adapter-synchronizer
> Library for providing synchronized adapters...

___

Version **1.0.0** is the last version officially released on 24 August 2015.

![Adapter Synchronizer Banner](https://github.com/erencelik/adapter-synchronizer/blob/master/adapter-synchronizer-banner.png "Adapter Synchronizer")

## Setup
Add [library](https://github.com/erencelik/adapter-synchronizer/tree/master/library) module to your project as **Android Library**.

## Features
- Only works with <code>ArrayAdapter</code>.
- Currently supports add single, add multiple and remove methods.

## Sample Application
There is a [sample application](https://github.com/erencelik/adapter-synchronizer/tree/master/sample), you may want to try it, and also application has been published to the Play Store for easy access.

[![Get it on Google Play](http://www.android.com/images/brand/get_it_on_play_logo_small.png)](http://play.google.com/store/apps/details?id=ern.adapter.synchronizer.sample)

If you find any bugs just contact me, and also I will be open-minded to get any ideas or advises that improve this library.

## Usage
Usage of this library is quite simple.
- First initialize **AdapterSynchronizer** in your Application **onCreate** method.
```java

public class MyApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    ...
    AdapterSynchronizer.getInstance().init(this);
    ...
  }

}
```
- Secondly, add your adapter to the **AdapterSynchronizer** with **Positive Unique ID**.
```java
public class FirstActivity extends Activity {
  
  public static final int ADAPTER_ID = 1;

  ...

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ...
    final MyCustomAdapter adapter = new MyCustomAdapter(this);
    AdapterSynchronizer.getInstance().add(adapter, ADAPTER_ID);
    ...
  }

  ...

}
```
- And finally, whenever add/remove  item to/from your adapter call **synchronize** method of **AdapterSynchronizer**
```java
  
...

public void addItem(T item) {
  mAdapter.add(item);
  // With supplying adapter id(s) to synchronize method
  // causes synchronize adapter(s) thats id(s) same as given id(s)
  AdapterSynchronizer.getInstance().synchronize(item, AdapterSynchronizer.SyncType.ADD, 
          FirstActivity.ADAPTER_ID, 
          SecondActivity.ADAPTER_ID);
}

public void removeItem(T item) {
  mAdapter.remove(item);
  // Without supplying adapter id to synchronize method
  // causes all of your bounded adapters to be synchronized.
  AdapterSynchronizer.getInstance().synchronize(item, AdapterSynchronizer.SyncType.REMOVE);
}

...

```

## Future Tasks
- Will work with <code>RecycleView.Adapter</code>.
- Update method will be added asap.
- Performance improvements and will be making library more stable.

## License
  Copyright 2015 H. Eren ÇELİK
  
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
  http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
