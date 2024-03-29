# Android Support Library
-optimizationpasses 5
-overloadaggressively
-dontpreverify
-repackageclasses 'o'
-allowaccessmodification

#-keepattributes InnerClasses
-keep class **.R
-keep class **.R$* {
    <fields>;
}

# Keep the source line when using ProGuard
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile