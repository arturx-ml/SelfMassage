# Keep line numbers for crash reports
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# kotlinx-serialization — keep @Serializable classes
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class ai.mlxdroid.selfmassage.**$$serializer { *; }
-keepclassmembers class ai.mlxdroid.selfmassage.** {
    *** Companion;
}
-keepclasseswithmembers class ai.mlxdroid.selfmassage.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Hilt — keep generated components
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# Data models — keep fields for reflection if needed
-keep class ai.mlxdroid.selfmassage.data.model.** { *; }

# Navigation routes — keep @Serializable route classes
-keep class ai.mlxdroid.selfmassage.navigation.** { *; }
