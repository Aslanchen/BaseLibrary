# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Pgm\Android\SDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontwarn eu.davidea.flexibleadapter.FlexibleAdapter
-keep class eu.davidea.flexibleadapter.FlexibleAdapter{*;}

-dontwarn eu.davidea.flexibleadapter.Payload
-keep enum eu.davidea.flexibleadapter.Payload{*;}
-keepclassmembers enum eu.davidea.flexibleadapter.Payload {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-dontwarn eu.davidea.flexibleadapter.helpers.AnimatorHelper
-keep class eu.davidea.flexibleadapter.helpers.AnimatorHelper{*;}

-dontwarn eu.davidea.flexibleadapter.items.AbstractFlexibleItem
-keep class eu.davidea.flexibleadapter.items.AbstractFlexibleItem{*;}

-dontwarn eu.davidea.viewholders.FlexibleViewHolder
-keep class eu.davidea.viewholders.FlexibleViewHolder{*;}

-dontwarn eu.davidea.flexibleadapter.FlexibleAdapter$OnUpdateListener
-keep interface eu.davidea.flexibleadapter.FlexibleAdapter$OnUpdateListener{*;}

-dontwarn eu.davidea.flexibleadapter.FlexibleAdapter$OnItemLongClickListener
-keep interface eu.davidea.flexibleadapter.FlexibleAdapter$OnItemLongClickListener{*;}

-dontwarn eu.davidea.flexibleadapter.FlexibleAdapter$EndlessScrollListener
-keep interface eu.davidea.flexibleadapter.FlexibleAdapter$EndlessScrollListener{*;}

-dontwarn eu.davidea.flexibleadapter.FlexibleAdapter$OnItemClickListener
-keep interface eu.davidea.flexibleadapter.FlexibleAdapter$OnItemClickListener{*;}

-dontwarn eu.davidea.flexibleadapter.FlexibleAdapter$OnItemSubClickListener
-keep interface eu.davidea.flexibleadapter.FlexibleAdapter$OnItemSubClickListener{*;}

-dontwarn eu.davidea.flexibleadapter.items.IHolder
-keep interface eu.davidea.flexibleadapter.items.IHolder{*;}

-dontwarn eu.davidea.flexibleadapter.items.ISectionable
-keep interface eu.davidea.flexibleadapter.items.ISectionable{*;}

-dontwarn eu.davidea.flexibleadapter.items.IHeader
-keep interface eu.davidea.flexibleadapter.items.IHeader{*;}

-dontwarn eu.davidea.flexibleadapter.items.IFlexible
-keep interface eu.davidea.flexibleadapter.items.IFlexible{*;}

-dontwarn eu.davidea.flexibleadapter.items.IFilterable
-keep interface eu.davidea.flexibleadapter.items.IFilterable{*;}

-dontwarn eu.davidea.flexibleadapter.common.FlexibleItemDecoration
-keep class eu.davidea.flexibleadapter.common.FlexibleItemDecoration{*;}

-dontwarn eu.davidea.flexibleadapter.SelectableAdapter
-keep class eu.davidea.flexibleadapter.SelectableAdapter{*;}

-dontwarn eu.davidea.flexibleadapter.SelectableAdapter$Mode
-keep interface eu.davidea.flexibleadapter.SelectableAdapter$Mode{*;}

-dontwarn eu.davidea.flexibleadapter.helpers.ActionModeHelper
-keep class eu.davidea.flexibleadapter.helpers.ActionModeHelper{*;}