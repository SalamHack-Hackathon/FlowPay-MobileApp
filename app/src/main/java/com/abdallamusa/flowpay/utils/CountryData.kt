package com.abdallamusa.flowpay.utils



data class Country(
    val code: String,
    val dialCode: String,
    val nameArabic: String
)

object CountryUtils {
    // A comprehensive list of world countries with Arabic names and dial codes
    val getAllCountries = listOf(
        // === Arab Countries ===
        Country("SA", "+966", "السعودية"),
        Country("EG", "+20", "مصر"),
        Country("AE", "+971", "الإمارات"),
        Country("KW", "+965", "الكويت"),
        Country("QA", "+974", "قطر"),
        Country("BH", "+973", "البحرين"),
        Country("OM", "+968", "عُمان"),
        Country("JO", "+962", "الأردن"),
        Country("LB", "+961", "لبنان"),
        Country("SY", "+963", "سوريا"),
        Country("IQ", "+964", "العراق"),
        Country("PS", "+970", "فلسطين"),
        Country("YE", "+967", "اليمن"),
        Country("SD", "+249", "السودان"),
        Country("DZ", "+213", "الجزائر"),
        Country("MA", "+212", "المغرب"),
        Country("TN", "+216", "تونس"),
        Country("LY", "+218", "ليبيا"),
        Country("MR", "+222", "موريتانيا"),
        Country("SO", "+252", "الصومال"),
        Country("DJ", "+253", "جيبوتي"),
        Country("KM", "+269", "جزر القمر"),

        // === North America ===
        Country("US", "+1", "الولايات المتحدة"),
        Country("CA", "+1", "كندا"),
        Country("MX", "+52", "المكسيك"),

        // === Europe ===
        Country("GB", "+44", "المملكة المتحدة"),
        Country("FR", "+33", "فرنسا"),
        Country("DE", "+49", "ألمانيا"),
        Country("IT", "+39", "إيطاليا"),
        Country("ES", "+34", "إسبانيا"),
        Country("RU", "+7", "روسيا"),
        Country("TR", "+90", "تركيا"),
        Country("NL", "+31", "هولندا"),
        Country("SE", "+46", "السويد"),
        Country("CH", "+41", "سويسرا"),
        Country("BE", "+32", "بلجيكا"),
        Country("AT", "+43", "النمسا"),
        Country("PT", "+351", "البرتغال"),
        Country("GR", "+30", "اليونان"),

        // === Asia ===
        Country("CN", "+86", "الصين"),
        Country("JP", "+81", "اليابان"),
        Country("KR", "+82", "كوريا الجنوبية"),
        Country("IN", "+91", "الهند"),
        Country("PK", "+92", "باكستان"),
        Country("ID", "+62", "إندونيسيا"),
        Country("MY", "+60", "ماليزيا"),
        Country("SG", "+65", "سنغافورة"),
        Country("TH", "+66", "تايلاند"),
        Country("VN", "+84", "فيتنام"),
        Country("PH", "+63", "الفلبين"),
        Country("BD", "+880", "بنغلاديش"),
        Country("LK", "+94", "سريلانكا"),
        Country("AF", "+93", "أفغانستان"),
        Country("IR", "+98", "إيران"),

        // === South America ===
        Country("BR", "+55", "البرازيل"),
        Country("AR", "+54", "الأرجنتين"),
        Country("CO", "+57", "كولومبيا"),
        Country("CL", "+56", "تشيلي"),
        Country("PE", "+51", "بيرو"),
        Country("VE", "+58", "فنزويلا"),

        // === Africa (Non-Arab) ===
        Country("ZA", "+27", "جنوب أفريقيا"),
        Country("NG", "+234", "نيجيريا"),
        Country("KE", "+254", "كينيا"),
        Country("ET", "+251", "إثيوبيا"),
        Country("GH", "+233", "غانا"),
        Country("UG", "+256", "أوغندا"),
        Country("TZ", "+255", "تنزانيا"),

        // === Oceania ===
        Country("AU", "+61", "أستراليا"),
        Country("NZ", "+64", "نيوزيلندا")
    )
}