# Kotlin
-keepattributes *Annotation*, InnerClasses

-keep class kotlin.reflect.jvm.internal.** { *; }
-keep class kotlin.text.RegexOption

-keepclassmembers class kotlinx.** {
    volatile <fields>;
}
