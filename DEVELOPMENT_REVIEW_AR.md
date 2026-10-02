# مراجعة SplashScreen وخطة التطوير

<!-- review-metadata -->
تاريخ المراجعة: 2026-10-02. الفرع المحلي: `codex/review-develop-2026-10-02`.

المصدر: [aymank2020/SplashScreen](https://github.com/aymank2020/SplashScreen)؛ commit الأساس: `13aef0cd61e71f530126476df2e9d1c58315300e`؛ عدد الملفات المتتبعة في الأساس: 46. Fork: true؛ مؤرشف: false.

نُفذت المرحلة المحددة أدناه بعد مراجعة الكود والاختبارات وتطبيق مراجعة التكامل والأثر؛ المراحل التالية والفجوات لا تُعد مكتملة.

مثال AndroidCodility بـKotlin1.2.20 وAGP3.0.1 وAndroid26. يستخدم Activity مخصصة تنتظر3ثوانٍ قبل الشاشة الرئيسية. المؤقت السابق لا يتوقف عند مغادرة الشاشة ويمكنه فتح الرئيسية من الخلفية أو بعد تدمير Activity.

## التنفيذ الحالي

Handler مربوط صراحة بـmain Looper، مع جدولة الانتقال فقط خلال onResume وإزالة callback في onPause/onDestroy. يحفظ الزمن المتبقي باستخدام uptime وsavedInstanceState؛ العودة أو تدوير الشاشة يكملان ما تبقى دون بدء3ثوانٍ إضافية. يفحص callback أن الشاشة ما زالت resumed ولم تُنهَ. أُضيف Maven Central قبل JCenter دون تغيير Kotlin/AGP.

## خطة المراحل التالية

1. بناء وتشغيل المثال، واختبار الرجوع والخلفية وتدوير الشاشة خلال مهلة الانتظار.
2. الانتقال إلى SplashScreen compatibility API لتجنب شاشة بدء مضاعفة على Android12+، وحذف الانتظار الثابت إن لم تكن هناك تهيئة ضرورية للمستهلك.
3. ترقية Kotlin وAndroidX وAGP تدريجيًا بعد حفظ اختبار انتقال واحد بلا تكرار.

## التكامل والتحقق

المسار: SplashActivity lifecycle → Handler callback → Intent إلى MainActivity → finish. الأثر المتوقع: انتقال واحد من شاشة نشطة فقط، والزمن المتبقي محفوظ. روجعت جميع نقاط الجدولة والإلغاء وموارد المثال؛ لم يُجرَ تجميع Kotlin أو اختبار جهاز. امتلاء قرص العمل أوقف بناء Android الإضافي؛ وجود SDK وحده لا يثبت توافق المشروع القديم. لا يُدّعى نجاح وقت التشغيل أو تطبيق ترحيل SplashScreen الحديث بعد.

## مصادر أولية

- [Android: ترحيل SplashScreen](https://developer.android.com/develop/ui/views/launch/splash-screen/migrate)
- [Android: Handler](https://developer.android.com/reference/android/os/Handler)
- [Gradle: انتهاء JCenter](https://blog.gradle.org/jcenter-shutdown)
